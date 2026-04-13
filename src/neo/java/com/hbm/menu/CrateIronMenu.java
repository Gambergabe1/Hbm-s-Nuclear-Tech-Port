package com.hbm.menu;

import com.hbm.blockentity.IronCrateBlockEntity;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public final class CrateIronMenu extends ChestMenu {
    public static final int ROWS = 4;

    public CrateIronMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(IronCrateBlockEntity.SLOT_COUNT));
    }

    public CrateIronMenu(int containerId, Inventory playerInventory, Container container) {
        super(HbmMenuTypes.CRATE_IRON.get(), containerId, playerInventory, container, ROWS);
    }
}
