package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LockableStorageBlockEntity extends AbstractStorageBlockEntity {
    private int legacyLockPins;
    private double legacyLockModifier = 0.1D;
    private boolean legacyLocked;

    protected LockableStorageBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        String translationKey
    ) {
        super(type, pos, blockState, slotCount, translationKey);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        legacyLockPins = tag.getInt("lock");
        legacyLockModifier = tag.contains("lockMod") ? tag.getDouble("lockMod") : 0.1D;
        legacyLocked = tag.getBoolean("isLocked");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (legacyLockPins != 0) {
            tag.putInt("lock", legacyLockPins);
        }
        tag.putDouble("lockMod", legacyLockModifier);
        tag.putBoolean("isLocked", legacyLocked);
    }
}
