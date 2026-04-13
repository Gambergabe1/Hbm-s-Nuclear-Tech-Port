package com.hbm.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMachineMenu extends AbstractContainerMenu {
    protected final Inventory playerInventory;
    protected final Container container;
    protected final ContainerData data;
    protected final ContainerLevelAccess access;
    private final int machineSlotCount;
    private final int playerMainStart;
    private final int playerHotbarStart;
    private final int playerSlotEnd;

    protected AbstractMachineMenu(
        MenuType<?> type,
        int containerId,
        Inventory playerInventory,
        Container container,
        int expectedContainerSize,
        ContainerData data,
        int expectedDataCount
    ) {
        this(type, containerId, playerInventory, container, expectedContainerSize, data, expectedDataCount, BlockPos.ZERO);
    }

    protected AbstractMachineMenu(
        MenuType<?> type,
        int containerId,
        Inventory playerInventory,
        Container container,
        int expectedContainerSize,
        ContainerData data,
        int expectedDataCount,
        BlockPos fallbackPos
    ) {
        super(type, containerId);
        checkContainerSize(container, expectedContainerSize);
        checkContainerDataCount(data, expectedDataCount);
        this.playerInventory = playerInventory;
        this.container = container;
        this.data = data;
        this.machineSlotCount = expectedContainerSize;
        this.playerMainStart = machineSlotCount;
        this.playerHotbarStart = playerMainStart + 27;
        this.playerSlotEnd = playerHotbarStart + 9;
        if (container instanceof net.minecraft.world.level.block.entity.BlockEntity blockEntity && blockEntity.getLevel() != null) {
            this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        } else if (!BlockPos.ZERO.equals(fallbackPos)) {
            this.access = ContainerLevelAccess.create(playerInventory.player.level(), fallbackPos);
        } else {
            this.access = ContainerLevelAccess.NULL;
        }
    }

    protected final void addPlayerInventorySlots(int x, int y) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, x + column * 18, y + row * 18));
            }
        }

        int hotbarY = y + 58;
        for (int hotbar = 0; hotbar < 9; hotbar++) {
            addSlot(new Slot(playerInventory, hotbar, x + hotbar * 18, hotbarY));
        }
    }

    protected final boolean moveToSingleSlot(ItemStack stack, int slot) {
        return moveItemStackTo(stack, slot, slot + 1, false);
    }

    protected final boolean moveToRange(ItemStack stack, int startInclusive, int endExclusive) {
        return moveItemStackTo(stack, startInclusive, endExclusive, false);
    }

    protected final boolean moveMachineStackToPlayer(ItemStack stack) {
        return moveItemStackTo(stack, machineSlotCount, playerSlotEnd, true);
    }

    protected final boolean moveWithinPlayerInventory(ItemStack stack, int index) {
        if (index < playerHotbarStart) {
            return moveItemStackTo(stack, playerHotbarStart, playerSlotEnd, false);
        }
        return moveItemStackTo(stack, playerMainStart, playerHotbarStart, false);
    }

    protected final boolean isMachineStillValid(Player player, net.minecraft.world.level.block.Block block) {
        return container.stillValid(player) && stillValid(access, player, block);
    }

    protected final int getScaledData(int index, int pixels, int maxValue) {
        if (maxValue <= 0) {
            return 0;
        }
        return data.get(index) * pixels / maxValue;
    }

    protected final int getDataValue(int index) {
        return data.get(index);
    }

    protected final Container getMenuContainer() {
        return container;
    }

    @Override
    public final ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack quickMoved = stack.copy();

        if (index < machineSlotCount) {
            if (!moveMachineStackToPlayer(stack)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveFromPlayerInventory(stack, index)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == quickMoved.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return quickMoved;
    }

    protected abstract boolean moveFromPlayerInventory(ItemStack stack, int index);
}
