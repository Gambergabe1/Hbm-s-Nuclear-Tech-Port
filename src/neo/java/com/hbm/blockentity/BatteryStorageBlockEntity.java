package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.block.BatteryStorageBlock;
import com.hbm.menu.BatteryStorageMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public final class BatteryStorageBlockEntity extends AbstractLongEnergyMachineBlockEntity {
    public static final int SLOT_DISCHARGE_IN = 0;
    public static final int SLOT_DISCHARGE_OUT = 1;
    public static final int SLOT_CHARGE_IN = 2;
    public static final int SLOT_CHARGE_OUT = 3;
    public static final int SLOT_COUNT = 4;

    public static final int MODE_INPUT = 0;
    public static final int MODE_BUFFER = 1;
    public static final int MODE_OUTPUT = 2;
    public static final int MODE_NONE = 3;

    private static final int DATA_POWER_LOW = 0;
    private static final int DATA_POWER_HIGH = 1;
    private static final int DATA_DELTA_LOW = 2;
    private static final int DATA_DELTA_HIGH = 3;
    private static final int DATA_RED_LOW = 4;
    private static final int DATA_RED_HIGH = 5;
    private static final int DATA_PRIORITY = 6;
    private static final int DATA_COUNT = 7;

    private final IEnergyStorage externalEnergyView = energyStorage.createView(this::canAcceptExternalEnergy, this::canOutputExternalEnergy);
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_POWER_LOW -> lowerLong(energyStorage.getEnergyStored());
                case DATA_POWER_HIGH -> upperLong(energyStorage.getEnergyStored());
                case DATA_DELTA_LOW -> lowerLong(powerDelta);
                case DATA_DELTA_HIGH -> upperLong(powerDelta);
                case DATA_RED_LOW -> redLow;
                case DATA_RED_HIGH -> redHigh;
                case DATA_PRIORITY -> priority.ordinal();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_RED_LOW -> redLow = sanitizeMode(value);
                case DATA_RED_HIGH -> redHigh = sanitizeMode(value);
                case DATA_PRIORITY -> priority = ConnectionPriority.byOrdinal(value);
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    private final long[] powerLog = new long[20];
    private long powerDelta;
    private int redLow = MODE_INPUT;
    private int redHigh = MODE_OUTPUT;
    private ConnectionPriority priority = ConnectionPriority.NORMAL;
    private int lastComparatorOutput = -1;

    public BatteryStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_BATTERY.get(), pos, blockState, SLOT_COUNT, resolveCapacity(blockState), resolveCapacity(blockState), resolveCapacity(blockState));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BatteryStorageBlockEntity blockEntity) {
        blockEntity.tickServer();
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    public IEnergyStorage getEnergyCapability() {
        return externalEnergyView;
    }

    public long getMaxPower() {
        return resolveCapacity(getBlockState());
    }

    public long getPowerDelta() {
        return powerDelta;
    }

    public int getComparatorOutput() {
        long maxPower = getMaxPower();
        if (maxPower <= 0L || energyStorage.getEnergyStored() <= 0L) {
            return 0;
        }

        long scaled = energyStorage.getEnergyStored() * 15L / maxPower;
        return (int) Math.max(1L, Math.min(15L, scaled));
    }

    public int getRedLowMode() {
        return redLow;
    }

    public int getRedHighMode() {
        return redHigh;
    }

    public int getPriorityOrdinal() {
        return priority.ordinal();
    }

    public void cycleRedLowMode() {
        redLow = (redLow + 1) % 4;
        setChanged();
    }

    public void cycleRedHighMode() {
        redHigh = (redHigh + 1) % 4;
        setChanged();
    }

    public void cyclePriority() {
        priority = priority.next();
        setChanged();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.battery");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_DISCHARGE_IN -> HbmEnergyHelper.canDischargeIntoMachine(stack);
            case SLOT_CHARGE_IN -> HbmEnergyHelper.getChargeDemand(stack) > 0;
            default -> false;
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BatteryStorageMenu(containerId, inventory, this, dataAccess, worldPosition);
    }

    @Override
    protected void loadMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        redLow = sanitizeMode(tag.getShort("redLow"));
        redHigh = sanitizeMode(tag.getShort("redHigh"));
        priority = ConnectionPriority.byOrdinal(tag.getByte("priority"));
        powerDelta = 0L;
        lastComparatorOutput = -1;
        for (int index = 0; index < powerLog.length; index++) {
            powerLog[index] = 0L;
        }
    }

    @Override
    protected void saveMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putShort("redLow", (short) redLow);
        tag.putShort("redHigh", (short) redHigh);
        tag.putByte("priority", (byte) priority.ordinal());
    }

    private void tickServer() {
        long previousPower = energyStorage.getEnergyStored();
        boolean changed = false;

        if (chargeFromBatterySlot(SLOT_DISCHARGE_IN) > 0) {
            changed = true;
        }

        if (pushEnergyToNeighbors() > 0L) {
            changed = true;
        }

        if (chargeItemFromStorage(SLOT_CHARGE_IN, getItemTransferLimit()) > 0) {
            changed = true;
        }

        if (moveFinishedEnergyItem(SLOT_DISCHARGE_IN, SLOT_DISCHARGE_OUT, true)) {
            changed = true;
        }
        if (moveFinishedEnergyItem(SLOT_CHARGE_IN, SLOT_CHARGE_OUT, false)) {
            changed = true;
        }

        if (updatePowerDelta(previousPower)) {
            changed = true;
        }
        if (updateComparatorOutput()) {
            changed = true;
        }

        if (changed) {
            setChanged();
        }
    }

    private long pushEnergyToNeighbors() {
        if (level == null || !canOutputExternalEnergy()) {
            return 0L;
        }

        int transferBudget = (int) Math.min(
            Integer.MAX_VALUE,
            Math.min(energyStorage.getEnergyStored(), getMaxTransferPerTick())
        );
        if (transferBudget <= 0) {
            return 0L;
        }

        List<HbmEnergyHelper.EnergyTransferEndpoint> endpoints = new java.util.ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = worldPosition.relative(direction);
            IEnergyStorage target = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                neighborPos,
                direction.getOpposite()
            );
            if (target == null || !target.canReceive()) {
                continue;
            }

            HbmEnergyHelper.EnergyTransferEndpoint endpoint = HbmEnergyHelper.createTransferEndpoint(
                target,
                level.getBlockEntity(neighborPos),
                neighborPos.asLong()
            );
            if (!HbmEnergyHelper.canTransferToStorage(getPriorityOrdinal(), true, endpoint.priority(), endpoint.storageEndpoint())) {
                continue;
            }

            endpoints.add(endpoint);
        }

        if (endpoints.isEmpty()) {
            return 0L;
        }

        int transferred = HbmEnergyHelper.fairReceive(endpoints, transferBudget, false);
        if (transferred <= 0) {
            return 0L;
        }

        return energyStorage.extractEnergy(transferred, false);
    }

    private boolean moveFinishedEnergyItem(int sourceSlot, int targetSlot, boolean drainingSlot) {
        ItemStack source = items.get(sourceSlot);
        if (source.isEmpty() || !isTransferFinished(source, drainingSlot)) {
            return false;
        }

        ItemStack target = items.get(targetSlot);
        ItemStack moved = source.copyWithCount(1);
        if (!canMergeIntoOutput(target, moved)) {
            return false;
        }

        if (target.isEmpty()) {
            items.set(targetSlot, moved);
        } else {
            target.grow(1);
        }

        source.shrink(1);
        if (source.isEmpty()) {
            items.set(sourceSlot, ItemStack.EMPTY);
        }
        return true;
    }

    private boolean isTransferFinished(ItemStack stack, boolean drainingSlot) {
        if (stack.isEmpty()) {
            return false;
        }

        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (itemEnergy == null) {
            return true;
        }

        return drainingSlot ? itemEnergy.getEnergyStored() <= 0 : itemEnergy.receiveEnergy(Integer.MAX_VALUE, true) <= 0;
    }

    private boolean canMergeIntoOutput(ItemStack target, ItemStack moved) {
        if (target.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(target, moved)) {
            return false;
        }
        return target.getCount() < target.getMaxStackSize();
    }

    private boolean updatePowerDelta(long previousPower) {
        long averagePower = (energyStorage.getEnergyStored() >> 1) + (previousPower >> 1);
        long previousDelta = powerDelta;
        powerDelta = averagePower - powerLog[0];
        for (int index = 1; index < powerLog.length; index++) {
            powerLog[index - 1] = powerLog[index];
        }
        powerLog[powerLog.length - 1] = averagePower;
        return powerDelta != previousDelta;
    }

    private boolean updateComparatorOutput() {
        if (level == null) {
            return false;
        }

        int currentOutput = getComparatorOutput();
        if (currentOutput == lastComparatorOutput) {
            return false;
        }

        lastComparatorOutput = currentOutput;
        level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        return true;
    }

    private long getMaxTransferPerTick() {
        return Math.max(0L, getMaxPower() / 20L);
    }

    private int getItemTransferLimit() {
        return (int) Math.min(Integer.MAX_VALUE, getMaxTransferPerTick());
    }

    private int getRelevantMode() {
        if (level != null && level.hasNeighborSignal(worldPosition)) {
            return redHigh;
        }
        return redLow;
    }

    private boolean canAcceptExternalEnergy() {
        int mode = getRelevantMode();
        return mode == MODE_INPUT || mode == MODE_BUFFER;
    }

    private boolean canOutputExternalEnergy() {
        int mode = getRelevantMode();
        return mode == MODE_BUFFER || mode == MODE_OUTPUT;
    }

    private static long resolveCapacity(BlockState state) {
        if (state.getBlock() instanceof BatteryStorageBlock batteryBlock) {
            return batteryBlock.getEnergyCapacity();
        }
        return 0L;
    }

    private static int sanitizeMode(int mode) {
        if (mode < MODE_INPUT || mode > MODE_NONE) {
            return MODE_INPUT;
        }
        return mode;
    }

    private static int lowerLong(long value) {
        return (int) value;
    }

    private static int upperLong(long value) {
        return (int) (value >>> 32);
    }

    public enum ConnectionPriority {
        LOW,
        NORMAL,
        HIGH;

        public ConnectionPriority next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public static ConnectionPriority byOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length) {
                return NORMAL;
            }
            return values()[ordinal];
        }
    }
}
