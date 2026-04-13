package com.hbm.blockentity;

import com.hbm.block.FluidDuctBlock;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FluidDuctBlockEntity extends BlockEntity {
    private static final ThreadLocal<Set<FluidRouteKey>> FLUID_ROUTE_GUARD = ThreadLocal.withInitial(HashSet::new);
    private static final int EXTRACTION_RATE = 1000;

    private final IFluidHandler[] sidedHandlers = new IFluidHandler[Direction.values().length];
    private final IFluidHandler unsidedHandler = new NetworkFluidHandler(null);
    private ResourceLocation lockedFluidId;

    public FluidDuctBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.FLUID_DUCT.get(), pos, blockState);
        for (Direction direction : Direction.values()) {
            sidedHandlers[direction.get3DDataValue()] = new NetworkFluidHandler(direction);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidDuctBlockEntity blockEntity) {
        if (state.getValue(FluidDuctBlock.EXTRACTS)) {
            blockEntity.extractFromNeighbors();
        }
    }

    public IFluidHandler getFluidCapability(Direction side) {
        return side == null ? unsidedHandler : sidedHandlers[side.get3DDataValue()];
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        lockedFluidId = tag.contains("lockedFluid") ? ResourceLocation.tryParse(tag.getString("lockedFluid")) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (lockedFluidId != null) {
            tag.putString("lockedFluid", lockedFluidId.toString());
        }
    }

    private void extractFromNeighbors() {
        if (level == null) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = worldPosition.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof FluidDuctBlockEntity) {
                continue;
            }

            IFluidHandler source = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.getOpposite());
            if (source == null) {
                continue;
            }

            FluidStack available = source.drain(EXTRACTION_RATE, IFluidHandler.FluidAction.SIMULATE);
            if (available.isEmpty()) {
                continue;
            }

            int accepted = routeFill(
                available.copyWithAmount(Math.min(EXTRACTION_RATE, available.getAmount())),
                IFluidHandler.FluidAction.SIMULATE,
                neighborPos
            );
            if (accepted <= 0) {
                continue;
            }

            FluidStack drained = source.drain(available.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
            if (!drained.isEmpty() && routeFill(drained, IFluidHandler.FluidAction.EXECUTE, neighborPos) > 0) {
                setChanged();
            }
        }
    }

    private int routeFill(FluidStack resource, IFluidHandler.FluidAction action, BlockPos excludedPos) {
        if (level == null || resource.isEmpty()) {
            return 0;
        }

        FluidNetworkScan network = scanNetwork();
        ResourceLocation persistedFluidId = resolveNetworkFluidId(network);
        ResourceLocation networkFluidId = getEffectiveNetworkFluidId(network);
        if (persistedFluidId != null && networkFluidId == null && action == IFluidHandler.FluidAction.EXECUTE) {
            clearNetworkFluidLock(network);
        }
        if (!matchesLockedFluid(networkFluidId, resource)) {
            return 0;
        }

        FluidRouteKey routeKey = new FluidRouteKey(level.dimension(), network.rootPos(), FluidOperation.FILL);
        Set<FluidRouteKey> guard = FLUID_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return 0;
        }

        try {
            List<FluidEndpoint> endpoints = collectEndpoints(network, excludedPos, handler ->
                handler.fill(resource.copyWithAmount(1), IFluidHandler.FluidAction.SIMULATE) > 0
            );
            FillPlan plan = planFillDistribution(resource, endpoints);
            if (plan.transferred() <= 0) {
                return 0;
            }
            if (action == IFluidHandler.FluidAction.SIMULATE) {
                return plan.transferred();
            }

            int transferred = executeFillPlan(resource, plan);
            if (transferred > 0) {
                setNetworkFluidLock(network, getFluidKey(resource));
            }
            return transferred;
        } finally {
            guard.remove(routeKey);
        }
    }

    private FluidStack routeDrain(FluidStack request, int maxDrain, IFluidHandler.FluidAction action, BlockPos excludedPos) {
        if (level == null || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        FluidNetworkScan network = scanNetwork();
        ResourceLocation persistedFluidId = resolveNetworkFluidId(network);
        ResourceLocation networkFluidId = getEffectiveNetworkFluidId(network);
        if (persistedFluidId != null && networkFluidId == null && action == IFluidHandler.FluidAction.EXECUTE) {
            clearNetworkFluidLock(network);
        }
        if (!request.isEmpty() && !matchesLockedFluid(networkFluidId, request)) {
            return FluidStack.EMPTY;
        }

        FluidRouteKey routeKey = new FluidRouteKey(level.dimension(), network.rootPos(), FluidOperation.DRAIN);
        Set<FluidRouteKey> guard = FLUID_ROUTE_GUARD.get();
        if (!guard.add(routeKey)) {
            return FluidStack.EMPTY;
        }

        try {
            List<FluidEndpoint> providers = collectEndpoints(
                network,
                excludedPos,
                handler -> !handler.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty()
            );
            FluidStack template = request.isEmpty()
                ? findDrainTemplate(providers, maxDrain, networkFluidId)
                : request.copyWithAmount(maxDrain);
            if (template.isEmpty()) {
                return FluidStack.EMPTY;
            }

            DrainPlan plan = planDrainDistribution(template, maxDrain, providers);
            if (plan.transferred() <= 0) {
                return FluidStack.EMPTY;
            }
            if (action == IFluidHandler.FluidAction.SIMULATE) {
                return template.copyWithAmount(plan.transferred());
            }

            FluidStack drainedTotal = executeDrainPlan(template, plan);
            if (action == IFluidHandler.FluidAction.EXECUTE && !drainedTotal.isEmpty()) {
                updateNetworkFluidLockAfterDrain(network, excludedPos, getFluidKey(drainedTotal));
            }

            return drainedTotal;
        } finally {
            guard.remove(routeKey);
        }
    }

    private FluidStack findDrainTemplate(List<FluidEndpoint> providers, int maxDrain, ResourceLocation networkFluidId) {
        for (FluidEndpoint endpoint : providers) {
            FluidStack preview = endpoint.handler().drain(maxDrain, IFluidHandler.FluidAction.SIMULATE);
            if (!preview.isEmpty() && matchesLockedFluid(networkFluidId, preview)) {
                return preview.copyWithAmount(Math.min(maxDrain, preview.getAmount()));
            }
        }
        return FluidStack.EMPTY;
    }

    private DrainPlan planDrainDistribution(FluidStack template, int maxDrain, List<FluidEndpoint> providers) {
        if (providers.isEmpty() || template.isEmpty() || maxDrain <= 0) {
            return DrainPlan.EMPTY;
        }

        int[] capacities = new int[providers.size()];
        int drainingProviders = 0;
        for (int index = 0; index < providers.size(); index++) {
            FluidStack simulated = providers.get(index).handler().drain(
                template.copyWithAmount(maxDrain),
                IFluidHandler.FluidAction.SIMULATE
            );
            if (simulated.isEmpty() || !FluidStack.isSameFluidSameComponents(simulated, template)) {
                continue;
            }

            capacities[index] = Math.max(0, Math.min(maxDrain, simulated.getAmount()));
            if (capacities[index] > 0) {
                drainingProviders++;
            }
        }

        if (drainingProviders <= 0) {
            return DrainPlan.EMPTY;
        }

        int[] allocations = new int[providers.size()];
        int baseShare = maxDrain / drainingProviders;
        int remainder = maxDrain % drainingProviders;
        long plannedTransfer = 0L;

        for (int index = 0; index < providers.size(); index++) {
            if (capacities[index] <= 0) {
                continue;
            }

            int share = baseShare;
            if (remainder > 0) {
                share++;
                remainder--;
            }

            allocations[index] = Math.min(capacities[index], share);
            plannedTransfer += allocations[index];
        }

        long leftover = (long) maxDrain - plannedTransfer;
        if (leftover > 0L) {
            for (int index = 0; index < providers.size() && leftover > 0L; index++) {
                int spareCapacity = capacities[index] - allocations[index];
                if (spareCapacity <= 0) {
                    continue;
                }

                int extra = (int) Math.min(spareCapacity, leftover);
                allocations[index] += extra;
                plannedTransfer += extra;
                leftover -= extra;
            }
        }

        int transferred = (int) Math.min(Integer.MAX_VALUE, plannedTransfer);
        return transferred > 0 ? new DrainPlan(providers, allocations, transferred) : DrainPlan.EMPTY;
    }

    private FluidStack executeDrainPlan(FluidStack template, DrainPlan plan) {
        FluidStack drainedTotal = FluidStack.EMPTY;

        for (int index = 0; index < plan.endpoints().size(); index++) {
            int allocation = plan.allocations()[index];
            if (allocation <= 0) {
                continue;
            }

            FluidStack drained = plan.endpoints().get(index).handler().drain(
                template.copyWithAmount(allocation),
                IFluidHandler.FluidAction.EXECUTE
            );
            if (drained.isEmpty() || !FluidStack.isSameFluidSameComponents(drained, template)) {
                continue;
            }

            if (drainedTotal.isEmpty()) {
                drainedTotal = drained.copy();
            } else {
                drainedTotal.grow(drained.getAmount());
            }
        }

        return drainedTotal;
    }

    private FillPlan planFillDistribution(FluidStack resource, List<FluidEndpoint> endpoints) {
        if (endpoints.isEmpty()) {
            return FillPlan.EMPTY;
        }

        int[] capacities = new int[endpoints.size()];
        int acceptingEndpoints = 0;
        for (int index = 0; index < endpoints.size(); index++) {
            int capacity = endpoints.get(index).handler().fill(
                resource.copyWithAmount(resource.getAmount()),
                IFluidHandler.FluidAction.SIMULATE
            );
            capacities[index] = Math.max(0, capacity);
            if (capacities[index] > 0) {
                acceptingEndpoints++;
            }
        }

        if (acceptingEndpoints <= 0) {
            return FillPlan.EMPTY;
        }

        int[] allocations = new int[endpoints.size()];
        int baseShare = resource.getAmount() / acceptingEndpoints;
        int remainder = resource.getAmount() % acceptingEndpoints;
        int plannedTransfer = 0;

        for (int index = 0; index < endpoints.size(); index++) {
            if (capacities[index] <= 0) {
                continue;
            }

            int share = baseShare;
            if (remainder > 0) {
                share++;
                remainder--;
            }

            allocations[index] = Math.min(capacities[index], share);
            plannedTransfer += allocations[index];
        }

        int leftover = resource.getAmount() - plannedTransfer;
        if (leftover > 0) {
            for (int index = 0; index < endpoints.size() && leftover > 0; index++) {
                int spareCapacity = capacities[index] - allocations[index];
                if (spareCapacity <= 0) {
                    continue;
                }

                int extra = Math.min(spareCapacity, leftover);
                allocations[index] += extra;
                plannedTransfer += extra;
                leftover -= extra;
            }
        }

        return plannedTransfer > 0 ? new FillPlan(endpoints, allocations, plannedTransfer) : FillPlan.EMPTY;
    }

    private int executeFillPlan(FluidStack resource, FillPlan plan) {
        int transferred = 0;

        for (int index = 0; index < plan.endpoints().size(); index++) {
            int allocation = plan.allocations()[index];
            if (allocation <= 0) {
                continue;
            }

            int filled = plan.endpoints().get(index).handler().fill(
                resource.copyWithAmount(allocation),
                IFluidHandler.FluidAction.EXECUTE
            );
            if (filled > 0) {
                transferred += filled;
            }
        }

        return transferred;
    }

    private ResourceLocation resolveNetworkFluidId(FluidNetworkScan network) {
        for (FluidDuctBlockEntity member : network.members()) {
            if (member.lockedFluidId != null) {
                return member.lockedFluidId;
            }
        }
        return null;
    }

    private ResourceLocation getEffectiveNetworkFluidId(FluidNetworkScan network) {
        ResourceLocation networkFluidId = resolveNetworkFluidId(network);
        if (networkFluidId == null || hasFluidProvider(network, null, networkFluidId)) {
            return networkFluidId;
        }
        return null;
    }

    private void setNetworkFluidLock(FluidNetworkScan network, ResourceLocation fluidId) {
        for (FluidDuctBlockEntity member : network.members()) {
            member.setLockedFluidId(fluidId);
        }
    }

    private void clearNetworkFluidLock(FluidNetworkScan network) {
        setNetworkFluidLock(network, null);
    }

    private void updateNetworkFluidLockAfterDrain(FluidNetworkScan network, BlockPos excludedPos, ResourceLocation drainedFluidId) {
        if (hasFluidProvider(network, excludedPos, drainedFluidId)) {
            setNetworkFluidLock(network, drainedFluidId);
        } else {
            clearNetworkFluidLock(network);
        }
    }

    private boolean hasFluidProvider(FluidNetworkScan network, BlockPos excludedPos, ResourceLocation fluidId) {
        if (fluidId == null) {
            return false;
        }

        for (FluidEndpoint endpoint : collectEndpoints(
            network,
            excludedPos,
            handler -> !handler.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty()
        )) {
            FluidStack preview = endpoint.handler().drain(1, IFluidHandler.FluidAction.SIMULATE);
            if (!preview.isEmpty() && fluidId.equals(getFluidKey(preview))) {
                return true;
            }
        }

        return false;
    }

    private void setLockedFluidId(ResourceLocation fluidId) {
        if ((lockedFluidId == null && fluidId == null) || (lockedFluidId != null && lockedFluidId.equals(fluidId))) {
            return;
        }

        lockedFluidId = fluidId;
        setChanged();
    }

    private static boolean matchesLockedFluid(ResourceLocation networkFluidId, FluidStack stack) {
        if (networkFluidId == null) {
            return true;
        }
        return !stack.isEmpty() && networkFluidId.equals(getFluidKey(stack));
    }

    private static ResourceLocation getFluidKey(FluidStack stack) {
        return stack.isEmpty() ? null : getFluidKey(stack.getFluid());
    }

    private static ResourceLocation getFluidKey(Fluid fluid) {
        return fluid == null ? null : BuiltInRegistries.FLUID.getKey(fluid);
    }

    private List<FluidEndpoint> collectEndpoints(
        FluidNetworkScan network,
        BlockPos excludedPos,
        java.util.function.Predicate<IFluidHandler> filter
    ) {
        List<FluidEndpoint> endpoints = new ArrayList<>();
        Set<EndpointKey> seen = new HashSet<>();

        for (FluidDuctBlockEntity duct : network.members()) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = duct.worldPosition.relative(direction);
                if (excludedPos != null && excludedPos.equals(neighborPos)) {
                    continue;
                }

                if (level.getBlockEntity(neighborPos) instanceof FluidDuctBlockEntity) {
                    continue;
                }

                Direction accessSide = direction.getOpposite();
                EndpointKey key = new EndpointKey(neighborPos, accessSide);
                if (!seen.add(key)) {
                    continue;
                }

                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, accessSide);
                if (handler == null || !filter.test(handler)) {
                    continue;
                }

                endpoints.add(new FluidEndpoint(neighborPos, accessSide, handler));
            }
        }

        endpoints.sort(Comparator.comparingLong(endpoint -> endpoint.pos().asLong()));
        return endpoints;
    }

    private FluidNetworkScan scanNetwork() {
        if (level == null) {
            return new FluidNetworkScan(worldPosition, List.of(this));
        }

        List<FluidDuctBlockEntity> members = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(worldPosition);
        visited.add(worldPosition);
        BlockPos root = worldPosition;

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.removeFirst();
            BlockEntity blockEntity = level.getBlockEntity(currentPos);
            if (!(blockEntity instanceof FluidDuctBlockEntity duct)) {
                continue;
            }

            members.add(duct);
            if (currentPos.asLong() < root.asLong()) {
                root = currentPos;
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (visited.add(neighborPos) && level.getBlockEntity(neighborPos) instanceof FluidDuctBlockEntity) {
                    queue.addLast(neighborPos);
                }
            }
        }

        return new FluidNetworkScan(root, members);
    }

    private record FluidNetworkScan(BlockPos rootPos, List<FluidDuctBlockEntity> members) {
    }

    private record FluidEndpoint(BlockPos pos, Direction side, IFluidHandler handler) {
    }

    private record FillPlan(List<FluidEndpoint> endpoints, int[] allocations, int transferred) {
        private static final FillPlan EMPTY = new FillPlan(List.of(), new int[0], 0);
    }

    private record DrainPlan(List<FluidEndpoint> endpoints, int[] allocations, int transferred) {
        private static final DrainPlan EMPTY = new DrainPlan(List.of(), new int[0], 0);
    }

    private record EndpointKey(BlockPos pos, Direction side) {
    }

    private record FluidRouteKey(ResourceKey<Level> dimension, BlockPos rootPos, FluidOperation operation) {
    }

    private enum FluidOperation {
        FILL,
        DRAIN
    }

    private final class NetworkFluidHandler implements IFluidHandler {
        private final Direction side;

        private NetworkFluidHandler(Direction side) {
            this.side = side;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            if (tank != 0) {
                return FluidStack.EMPTY;
            }
            BlockPos excludedPos = side == null ? null : worldPosition.relative(side);
            return routeDrain(FluidStack.EMPTY, Integer.MAX_VALUE, FluidAction.SIMULATE, excludedPos);
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 ? Integer.MAX_VALUE : 0;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return tank == 0 && !stack.isEmpty() && matchesLockedFluid(getEffectiveNetworkFluidId(scanNetwork()), stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            BlockPos excludedPos = side == null ? null : worldPosition.relative(side);
            return routeFill(resource, action, excludedPos);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            BlockPos excludedPos = side == null ? null : worldPosition.relative(side);
            return routeDrain(resource, resource.getAmount(), action, excludedPos);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            BlockPos excludedPos = side == null ? null : worldPosition.relative(side);
            return routeDrain(FluidStack.EMPTY, maxDrain, action, excludedPos);
        }
    }
}
