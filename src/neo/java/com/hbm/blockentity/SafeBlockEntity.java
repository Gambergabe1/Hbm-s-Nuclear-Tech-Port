package com.hbm.blockentity;

import com.hbm.menu.SafeMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public final class SafeBlockEntity extends LockableStorageBlockEntity {
    public static final int SLOT_COUNT = 15;

    public SafeBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.SAFE.get(), pos, blockState, SLOT_COUNT, "container.safe");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SafeMenu(containerId, inventory, this);
    }
}
