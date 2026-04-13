package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractProcessorMachineBlockEntity extends AbstractEnergyMachineBlockEntity {
    protected int progress;
    private final ContainerData dataAccess = createEnergyProgressData(() -> progress, value -> progress = value);

    protected AbstractProcessorMachineBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        int energyCapacity,
        int maxReceive,
        int maxExtract
    ) {
        super(type, pos, blockState, slotCount, energyCapacity, maxReceive, maxExtract);
    }

    public final ContainerData getDataAccess() {
        return dataAccess;
    }

    protected final void tickProcessorMachine() {
        boolean changed = chargeFromBatterySlot(getBatterySlot()) > 0;
        ItemStack result = getCurrentResult();
        boolean canProcess = isMachineEnabled()
            && energyStorage.getEnergyStored() >= getPowerPerTick()
            && !result.isEmpty()
            && canProcessResult(result);

        if (canProcess) {
            energyStorage.extractEnergy(getPowerPerTick(), false);
            progress = Math.min(getMaxProgress(), progress + getProgressPerTick());
            changed = true;
            if (progress >= getMaxProgress()) {
                completeProcessing(result);
                progress = 0;
                changed = true;
            }
        } else {
            int nextProgress = getBlockedProgress(progress);
            if (nextProgress != progress) {
                progress = nextProgress;
                changed = true;
            }
        }

        onProcessingStateChanged(canProcess);

        if (changed) {
            setChanged();
        } else {
            onIdleServerTick();
        }
    }

    @Override
    protected final void loadMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        progress = tag.getInt("progress");
        loadProcessorMachineData(tag, registries);
    }

    @Override
    protected final void saveMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("progress", progress);
        saveProcessorMachineData(tag, registries);
    }

    protected void loadProcessorMachineData(CompoundTag tag, HolderLookup.Provider registries) {
    }

    protected void saveProcessorMachineData(CompoundTag tag, HolderLookup.Provider registries) {
    }

    protected boolean isMachineEnabled() {
        return true;
    }

    protected int getBlockedProgress(int currentProgress) {
        return currentProgress > 0 ? 0 : currentProgress;
    }

    protected int getProgressPerTick() {
        return 1;
    }

    protected void onProcessingStateChanged(boolean active) {
    }

    protected void onIdleServerTick() {
    }

    protected abstract int getBatterySlot();

    protected abstract int getMaxProgress();

    protected abstract int getPowerPerTick();

    protected abstract ItemStack getCurrentResult();

    protected abstract boolean canProcessResult(ItemStack result);

    protected abstract void completeProcessing(ItemStack result);
}
