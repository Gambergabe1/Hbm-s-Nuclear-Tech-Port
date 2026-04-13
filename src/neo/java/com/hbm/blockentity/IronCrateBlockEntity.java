package com.hbm.blockentity;

import com.hbm.menu.CrateIronMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public final class IronCrateBlockEntity extends AbstractStorageBlockEntity {
    public static final int SLOT_COUNT = 36;

    public IronCrateBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.CRATE_IRON.get(), pos, blockState, SLOT_COUNT, "container.crateIron");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CrateIronMenu(containerId, inventory, this);
    }
}
