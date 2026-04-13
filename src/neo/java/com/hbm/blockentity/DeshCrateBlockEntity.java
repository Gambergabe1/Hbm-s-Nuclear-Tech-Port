package com.hbm.blockentity;

import com.hbm.menu.CrateDeshMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public final class DeshCrateBlockEntity extends LockableStorageBlockEntity {
    public static final int SLOT_COUNT = 104;

    public DeshCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.CRATE_DESH.get(), pos, blockState, SLOT_COUNT, "container.crateDesh");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CrateDeshMenu(containerId, inventory, this);
    }
}
