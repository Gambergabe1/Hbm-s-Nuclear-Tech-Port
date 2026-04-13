package com.hbm.blockentity;

import com.hbm.menu.CrateSteelMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public final class SteelCrateBlockEntity extends AbstractStorageBlockEntity {
    public static final int SLOT_COUNT = 54;

    public SteelCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.CRATE_STEEL.get(), pos, blockState, SLOT_COUNT, "container.crateSteel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CrateSteelMenu(containerId, inventory, this);
    }
}
