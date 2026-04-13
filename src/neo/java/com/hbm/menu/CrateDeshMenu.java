package com.hbm.menu;

import com.hbm.blockentity.DeshCrateBlockEntity;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public final class CrateDeshMenu extends AbstractGridStorageMenu {
    public static final int COLUMNS = 13;
    public static final int ROWS = 8;

    public CrateDeshMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(DeshCrateBlockEntity.SLOT_COUNT));
    }

    public CrateDeshMenu(int containerId, Inventory playerInventory, Container container) {
        super(HbmMenuTypes.CRATE_DESH.get(), containerId, playerInventory, container, COLUMNS, ROWS, 8, 18, 44, 174);
    }
}
