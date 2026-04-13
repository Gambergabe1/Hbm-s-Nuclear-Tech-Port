package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.block.EnergyCableDiodeBlock;
import com.hbm.block.EnergyCableGaugeBlock;
import com.hbm.block.EnergyCableSwitchBlock;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnergyCableBlockEntity extends BlockEntity {
    private static final ThreadLocal<Set<EnergyRouteKey>> ENERGY_ROUTE_GUARD = ThreadLocal.withInitial(HashSet::new);
    private static final int MAX_DIODE_LEVEL = 17;
    private static final int MAX_DIODE_PULSES_PER_TICK = 10;

    private final IEnergyStorage[] sidedHandlers = new IEnergyStorage[Direction.values().length];
    private final IEnergyStorage unsidedHandler = new NetworkEnergyStorage(null);

    private int diodeLevel = 1;
    private RoutingPriority diodePriority = RoutingPriority.NORMAL;
    private long lastGaugeRollTick = Long.MIN_VALUE;
    private long gaugeTransferCurrentSecond;
    private long gaugeTransferLastSecond;
    private int lastComparatorOutput = -1;
    private long diodeTransferTick = Long.MIN_VALUE;
    private int diodeTransferredThisTick;
    private long diodePulseTick = Long.MIN_VALUE;
    private int diodePulsesThisTick;

    public EnergyCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.ENERGY_TRANSPORT.get(), pos, blockState);
        for (Direction direction : Direction.values()) {
            sidedHandlers[direction.get3DDataValue()] = new NetworkEnergyStorage(direction);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnergyCableBlockEntity blockEntity) {
        blockEntity.tickServer();
    }

    public IEnergyStorage getEnergyCapability(Direction side) {
        return side == null ? unsidedHandler : sidedHandlers[side.get3DDataValue()];
    }

    public void onBlockStateChanged() {
        if (isGauge()) {
            updateGaugeComparator();
        }
    }

    public long getGaugeTransferLastSecond() {
        return gaugeTransferLastSecond;
    }

    public int getGaugeComparatorOutput() {
        if (gaugeTransferLastSecond <= 0L) {
            return 0;
        }
        return Math.min(15, 1 + (int) Math.floor(Math.log10(gaugeTransferLastSecond)));
    }

    public void cycleDiodeLevel() {
        diodeLevel = diodeLevel >= MAX_DIODE_LEVEL ? 1 : diodeLevel + 1;
        setChanged();
    }

    public void cycleDiodePriority() {
        diodePriority = diodePriority.next();
        setChanged();
    }

    public String getDiodeStatus() {
        return "Diode limit: " + getDiodeTransferLimit() + " HE/t, priority: " + diodePriority.getDisplayName();
    }

    public boolean isCableNetworkMember() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof EnergyCableDiodeBlock) {
            return false;
        }
        if (state.hasProperty(EnergyCableSwitchBlock.STATE)) {
            return state.getValue(EnergyCableSwitchBlock.STATE);
        }
        return true;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        diodeLevel = Math.max(1, Math.min(MAX_DIODE_LEVEL, tag.getInt("diodeLevel")));
        diodePriority = RoutingPriority.byOrdinal(tag.getInt("diodePriority"));
        gaugeTransferCurrentSecond = 0L;
        gaugeTransferLastSecond = Math.max(0L, tag.getLong("gaugeTransfer"));
        lastComparatorOutput = -1;
        diodeTransferTick = Long.MIN_VALUE;
        diodeTransferredThisTick = 0;
        diodePulseTick = Long.MIN_VALUE;
        diodePulsesThisTick = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("diodeLevel", diodeLevel);
        tag.putInt("diodePriority", diodePriority.ordinal());
        tag.putLong("gaugeTransfer", gaugeTransferLastSecond);
    }

    private void tickServer() {
        if (!isGauge() || level == null) {
            return;
        }

        long gameTime = level.getGameTime();
        if (gameTime % 20L != 0L || lastGaugeRollTick == gameTime) {
            return;
        }

        lastGaugeRollTick = gameTime;
        gaugeTransferLastSecond = gaugeTransferCurrentSecond;
        gaugeTransferCurrentSecond = 0L;
        updateGaugeComparator();
    }

    private void updateGaugeComparator() {
        if (!isGauge() || level == null) {
            return;
        }

        int output = getGaugeComparatorOutput();
        if (output == lastComparatorOutput) {
            return;
        }

        lastComparatorOutput = output;
        level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
    }

    private boolean isGauge() {
        return getBlockState().getBlock() instanceof EnergyCableGaugeBlock;
    }

    private void addGaugeTransfer(long amount) {
        if (!isGauge() || amount <= 0L) {
            return;
        }
        gaugeTransferCurrentSecond += amount;
    }

    private int routeReceiveAcrossNetwork(int maxReceive, boolean simulate, Direction inputSide) {
        if (level == null || maxReceive <= 0 || !isCableNetworkMember()) {
            return 0;
        }

        EnergyNetworkScan network = scanCableNetwork();
        EnergyRouteKey routeKey = new EnergyRouteKey(level.dimension(), network.rootPos(), EnergyOperation.RECEIVE);
        Set<EnergyRouteKey> guard = ENERGY_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return 0;
        }

        try {
            BlockPos excludedPos = inputSide == null ? null : worldPosition.relative(inputSide);
            HbmEnergyHelper.EnergyRoutingMetadata senderRouting = HbmEnergyHelper.getEndpointRouting(
                excludedPos == null ? null : level.getBlockEntity(excludedPos)
            );
            List<HbmEnergyHelper.EnergyTransferEndpoint> consumers = collectEnergyEndpoints(network, excludedPos, EndpointMode.CONSUMER);
            consumers.removeIf(endpoint -> !HbmEnergyHelper.canTransferToStorage(
                senderRouting.priority(),
                senderRouting.storage(),
                endpoint.priority(),
                endpoint.storageEndpoint()
            ));
            int transferred = HbmEnergyHelper.fairReceive(consumers, maxReceive, simulate);

            if (!simulate && transferred > 0) {
                network.recordTransfer(transferred);
            }
            return transferred;
        } finally {
            guard.remove(routeKey);
        }
    }

    private int routeExtractAcrossNetwork(int maxExtract, boolean simulate, Direction outputSide) {
        if (level == null || maxExtract <= 0 || !isCableNetworkMember()) {
            return 0;
        }

        EnergyNetworkScan network = scanCableNetwork();
        EnergyRouteKey routeKey = new EnergyRouteKey(level.dimension(), network.rootPos(), EnergyOperation.EXTRACT);
        Set<EnergyRouteKey> guard = ENERGY_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return 0;
        }

        try {
            BlockPos excludedPos = outputSide == null ? null : worldPosition.relative(outputSide);
            HbmEnergyHelper.EnergyRoutingMetadata requesterRouting = HbmEnergyHelper.getEndpointRouting(
                excludedPos == null ? null : level.getBlockEntity(excludedPos)
            );
            List<HbmEnergyHelper.EnergyTransferEndpoint> providers = collectEnergyEndpoints(network, excludedPos, EndpointMode.PROVIDER);
            providers.removeIf(endpoint -> !HbmEnergyHelper.canExtractFromStorage(
                requesterRouting.priority(),
                requesterRouting.storage(),
                endpoint.priority(),
                endpoint.storageEndpoint()
            ));
            int transferred = HbmEnergyHelper.fairExtract(providers, maxExtract, simulate);

            if (!simulate && transferred > 0) {
                network.recordTransfer(transferred);
            }
            return transferred;
        } finally {
            guard.remove(routeKey);
        }
    }

    private int receiveThroughDiode(int maxReceive, boolean simulate) {
        if (level == null || maxReceive <= 0) {
            return 0;
        }

        EnergyRouteKey routeKey = new EnergyRouteKey(level.dimension(), worldPosition, EnergyOperation.DIODE_RECEIVE);
        Set<EnergyRouteKey> guard = ENERGY_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return 0;
        }

        try {
            if (!simulate && !tryUseDiodePulse()) {
                return 0;
            }

            int remainingBudget = getAvailableDiodeTransfer();
            if (remainingBudget <= 0) {
                return 0;
            }

            Direction outputDirection = getDiodeOutputDirection();
            IEnergyStorage target = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                worldPosition.relative(outputDirection),
                outputDirection.getOpposite()
            );
            if (target == null || !target.canReceive()) {
                return 0;
            }

            int transferred = target.receiveEnergy(Math.min(maxReceive, remainingBudget), simulate);
            if (!simulate && transferred > 0) {
                recordDiodeTransfer(transferred);
            }
            return transferred;
        } finally {
            guard.remove(routeKey);
        }
    }

    private int extractThroughDiode(int maxExtract, boolean simulate) {
        if (level == null || maxExtract <= 0) {
            return 0;
        }

        EnergyRouteKey routeKey = new EnergyRouteKey(level.dimension(), worldPosition, EnergyOperation.DIODE_EXTRACT);
        Set<EnergyRouteKey> guard = ENERGY_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return 0;
        }

        try {
            if (!simulate && !tryUseDiodePulse()) {
                return 0;
            }

            int remaining = Math.min(maxExtract, getAvailableDiodeTransfer());
            if (remaining <= 0) {
                return 0;
            }

            List<HbmEnergyHelper.EnergyTransferEndpoint> providers = collectDiodeInputEndpoints();
            int transferred = HbmEnergyHelper.fairExtract(providers, remaining, simulate);

            if (!simulate && transferred > 0) {
                recordDiodeTransfer(transferred);
            }
            return transferred;
        } finally {
            guard.remove(routeKey);
        }
    }

    private List<HbmEnergyHelper.EnergyTransferEndpoint> collectEnergyEndpoints(
        EnergyNetworkScan network,
        BlockPos excludedPos,
        EndpointMode mode
    ) {
        List<HbmEnergyHelper.EnergyTransferEndpoint> endpoints = new ArrayList<>();
        Set<EndpointKey> seen = new HashSet<>();

        for (EnergyCableBlockEntity member : network.members()) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = member.worldPosition.relative(direction);
                if (excludedPos != null && excludedPos.equals(neighborPos)) {
                    continue;
                }

                BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                if (neighborEntity instanceof EnergyCableBlockEntity cable && cable.isCableNetworkMember()) {
                    continue;
                }

                Direction accessSide = direction.getOpposite();
                EndpointKey key = new EndpointKey(neighborPos, accessSide);
                if (!seen.add(key)) {
                    continue;
                }

                IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, accessSide);
                if (storage == null) {
                    continue;
                }
                if (mode == EndpointMode.CONSUMER && !storage.canReceive()) {
                    continue;
                }
                if (mode == EndpointMode.PROVIDER && !storage.canExtract()) {
                    continue;
                }

                endpoints.add(HbmEnergyHelper.createTransferEndpoint(storage, neighborEntity, neighborPos.asLong()));
            }
        }

        return endpoints;
    }

    private List<HbmEnergyHelper.EnergyTransferEndpoint> collectDiodeInputEndpoints() {
        List<HbmEnergyHelper.EnergyTransferEndpoint> endpoints = new ArrayList<>();
        Direction output = getDiodeOutputDirection();

        for (Direction direction : Direction.values()) {
            if (direction == output) {
                continue;
            }

            BlockPos neighborPos = worldPosition.relative(direction);
            IEnergyStorage storage = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                neighborPos,
                direction.getOpposite()
            );
            if (storage == null || !storage.canExtract()) {
                continue;
            }

            BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
            endpoints.add(HbmEnergyHelper.createTransferEndpoint(storage, neighborEntity, neighborPos.asLong()));
        }
        return endpoints;
    }

    private EnergyNetworkScan scanCableNetwork() {
        if (level == null) {
            return new EnergyNetworkScan(worldPosition, List.of(this));
        }

        List<EnergyCableBlockEntity> members = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(worldPosition);
        visited.add(worldPosition);
        BlockPos root = worldPosition;

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.removeFirst();
            BlockEntity blockEntity = level.getBlockEntity(currentPos);
            if (!(blockEntity instanceof EnergyCableBlockEntity cable) || !cable.isCableNetworkMember()) {
                continue;
            }

            members.add(cable);
            if (currentPos.asLong() < root.asLong()) {
                root = currentPos;
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!visited.add(neighborPos)) {
                    continue;
                }

                BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                if (neighborEntity instanceof EnergyCableBlockEntity neighborCable && neighborCable.isCableNetworkMember()) {
                    queue.addLast(neighborPos);
                }
            }
        }

        return new EnergyNetworkScan(root, members);
    }

    private Direction getDiodeOutputDirection() {
        return getBlockState().getValue(EnergyCableDiodeBlock.FACING).getOpposite();
    }

    private int getDiodeTransferLimit() {
        int limit = 5;
        for (int index = 1; index < diodeLevel; index++) {
            if (limit > Integer.MAX_VALUE / 10) {
                return Integer.MAX_VALUE;
            }
            limit *= 10;
        }
        return limit;
    }

    private int getAvailableDiodeTransfer() {
        if (level == null) {
            return getDiodeTransferLimit();
        }

        long tick = level.getGameTime();
        if (diodeTransferTick != tick) {
            return getDiodeTransferLimit();
        }
        return Math.max(0, getDiodeTransferLimit() - diodeTransferredThisTick);
    }

    private void recordDiodeTransfer(int amount) {
        if (level == null || amount <= 0) {
            return;
        }

        long tick = level.getGameTime();
        if (diodeTransferTick != tick) {
            diodeTransferTick = tick;
            diodeTransferredThisTick = 0;
        }
        diodeTransferredThisTick = Math.min(getDiodeTransferLimit(), diodeTransferredThisTick + amount);
    }

    private boolean tryUseDiodePulse() {
        if (level == null) {
            return true;
        }

        long tick = level.getGameTime();
        if (diodePulseTick != tick) {
            diodePulseTick = tick;
            diodePulsesThisTick = 0;
        }
        if (diodePulsesThisTick >= MAX_DIODE_PULSES_PER_TICK) {
            return false;
        }

        diodePulsesThisTick++;
        return true;
    }

    public int getRoutingPriorityOrdinal() {
        return getBlockState().getBlock() instanceof EnergyCableDiodeBlock ? diodePriority.ordinal() : HbmEnergyHelper.PRIORITY_NORMAL;
    }

    private record EnergyNetworkScan(BlockPos rootPos, List<EnergyCableBlockEntity> members) {
        private void recordTransfer(int amount) {
            for (EnergyCableBlockEntity member : members) {
                member.addGaugeTransfer(amount);
            }
        }
    }

    private record EndpointKey(BlockPos pos, Direction side) {
    }

    private record EnergyRouteKey(ResourceKey<Level> dimension, BlockPos rootPos, EnergyOperation operation) {
    }

    private enum EndpointMode {
        CONSUMER,
        PROVIDER
    }

    private enum EnergyOperation {
        RECEIVE,
        EXTRACT,
        DIODE_RECEIVE,
        DIODE_EXTRACT
    }

    private enum RoutingPriority {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High");

        private final String displayName;

        RoutingPriority(String displayName) {
            this.displayName = displayName;
        }

        public RoutingPriority next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public String getDisplayName() {
            return displayName;
        }

        public static RoutingPriority byOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return NORMAL;
            }
            return values()[ordinal];
        }
    }

    private final class NetworkEnergyStorage implements IEnergyStorage {
        private final Direction side;

        private NetworkEnergyStorage(Direction side) {
            this.side = side;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (getBlockState().getBlock() instanceof EnergyCableDiodeBlock) {
                return canReceive() ? receiveThroughDiode(maxReceive, simulate) : 0;
            }
            return routeReceiveAcrossNetwork(maxReceive, simulate, side);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (getBlockState().getBlock() instanceof EnergyCableDiodeBlock) {
                return canExtract() ? extractThroughDiode(maxExtract, simulate) : 0;
            }
            return routeExtractAcrossNetwork(maxExtract, simulate, side);
        }

        @Override
        public int getEnergyStored() {
            return 0;
        }

        @Override
        public int getMaxEnergyStored() {
            return 0;
        }

        @Override
        public boolean canExtract() {
            if (getBlockState().getBlock() instanceof EnergyCableDiodeBlock) {
                return side == null || side == getDiodeOutputDirection();
            }
            return isCableNetworkMember();
        }

        @Override
        public boolean canReceive() {
            if (getBlockState().getBlock() instanceof EnergyCableDiodeBlock) {
                return side == null || side != getDiodeOutputDirection();
            }
            return isCableNetworkMember();
        }
    }
}
