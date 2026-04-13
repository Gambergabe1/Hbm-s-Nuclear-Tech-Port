package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.api.energy.HbmLongEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class AbstractLongEnergyMachineBlockEntity extends AbstractMachineBlockEntity {
    protected final HbmLongEnergyStorage energyStorage;
    protected final IEnergyStorage internalEnergyView;

    protected AbstractLongEnergyMachineBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        long energyCapacity,
        long maxReceive,
        long maxExtract
    ) {
        super(type, pos, blockState, slotCount);
        this.energyStorage = new HbmLongEnergyStorage(
            Math.max(0L, energyCapacity),
            Math.max(0L, maxReceive),
            Math.max(0L, maxExtract)
        ).setChangeListener(this::setChanged);
        this.internalEnergyView = this.energyStorage.createView(() -> true, () -> true);
    }

    protected final int chargeFromBatterySlot(int slot) {
        if (slot < 0 || slot >= items.size()) {
            return 0;
        }
        return HbmEnergyHelper.chargeStorageFromItem(items.get(slot), internalEnergyView);
    }

    protected final int chargeItemFromStorage(int slot, int maxTransfer) {
        if (slot < 0 || slot >= items.size()) {
            return 0;
        }
        return HbmEnergyHelper.dischargeStorageToItem(internalEnergyView, items.get(slot), maxTransfer);
    }

    public HbmLongEnergyStorage getLongEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void loadSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        energyStorage.setEnergyStored(tag.getLong("power"));
        super.loadSharedMachineData(tag, registries);
    }

    @Override
    protected void saveSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLong("power", energyStorage.getEnergyStored());
        super.saveSharedMachineData(tag, registries);
    }
}
