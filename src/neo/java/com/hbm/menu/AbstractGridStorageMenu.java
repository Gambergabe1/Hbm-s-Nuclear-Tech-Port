package com.hbm.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGridStorageMenu extends AbstractContainerMenu {
    private final Container container;
    private final int storageSlotCount;

    protected AbstractGridStorageMenu(
        MenuType<?> type,
        int containerId,
        Inventory playerInventory,
        Container container,
        int columns,
        int rows,
        int storageX,
        int storageY,
        int playerInventoryX,
        int playerInventoryY
    ) {
        super(type, containerId);
        this.container = container;
        this.storageSlotCount = columns * rows;
        checkContainerSize(container, storageSlotCount);
        container.startOpen(playerInventory.player);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlot(new Slot(container, column + row * columns, storageX + column * 18, storageY + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, playerInventoryX + column * 18, playerInventoryY + row * 18));
            }
        }

        int hotbarY = playerInventoryY + 58;
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, playerInventoryX + column * 18, hotbarY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = slot.getItem();
        ItemStack quickMoved = stackInSlot.copy();

        if (index < storageSlotCount) {
            if (!moveItemStackTo(stackInSlot, storageSlotCount, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stackInSlot, 0, storageSlotCount, false)) {
            return ItemStack.EMPTY;
        }

        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, stackInSlot);
        return quickMoved;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }
}
