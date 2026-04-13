package com.hbm.menu;

import com.hbm.blockentity.SteelCrateBlockEntity;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public final class CrateSteelMenu extends ChestMenu {
    public static final int ROWS = 6;

    public CrateSteelMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SteelCrateBlockEntity.SLOT_COUNT));
    }

    public CrateSteelMenu(int containerId, Inventory playerInventory, Container container) {
        super(HbmMenuTypes.CRATE_STEEL.get(), containerId, playerInventory, container, ROWS);
    }
}
