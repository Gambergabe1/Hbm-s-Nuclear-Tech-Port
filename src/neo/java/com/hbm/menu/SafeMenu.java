package com.hbm.menu;

import com.hbm.blockentity.SafeBlockEntity;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public final class SafeMenu extends AbstractGridStorageMenu {
    public static final int COLUMNS = 5;
    public static final int ROWS = 3;

    public SafeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SafeBlockEntity.SLOT_COUNT));
    }

    public SafeMenu(int containerId, Inventory playerInventory, Container container) {
        super(HbmMenuTypes.SAFE.get(), containerId, playerInventory, container, COLUMNS, ROWS, 44, 18, 8, 86);
    }
}
