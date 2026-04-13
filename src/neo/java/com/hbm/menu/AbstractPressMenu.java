package com.hbm.menu;

import com.hbm.blockentity.BurnerPressBlockEntity;
import com.hbm.machine.PressRecipeRegistry;
import com.hbm.menu.slot.FilteredSlot;
import com.hbm.menu.slot.OutputSlot;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractPressMenu extends AbstractMachineMenu {
    protected AbstractPressMenu(
        MenuType<?> type,
        int containerId,
        Inventory playerInventory,
        Container container,
        ContainerData data,
        int dataCount,
        int powerSlotX
    ) {
        super(type, containerId, playerInventory, container, BurnerPressBlockEntity.SLOT_COUNT, data, dataCount);
        addSlot(createPowerSlot(container, powerSlotX, 53));
        addSlot(new FilteredSlot(container, BurnerPressBlockEntity.SLOT_STAMP, 80, 17, PressRecipeRegistry::isStamp));
        addSlot(new FilteredSlot(
            container,
            BurnerPressBlockEntity.SLOT_INPUT,
            80,
            53,
            stack -> !PressRecipeRegistry.isStamp(stack) && PressRecipeRegistry.hasAnyRecipeFor(stack)
        ));
        addSlot(new OutputSlot(container, BurnerPressBlockEntity.SLOT_OUTPUT, 140, 35));
        addPlayerInventorySlots(8, 84);
        addDataSlots(data);
    }

    protected static Container createClientContainer() {
        return new SimpleContainer(BurnerPressBlockEntity.SLOT_COUNT);
    }

    protected static ContainerData createClientData(int dataCount) {
        return new SimpleContainerData(dataCount);
    }

    protected static void ignoreBuffer(FriendlyByteBuf ignoredBuffer) {
    }

    protected abstract Slot createPowerSlot(Container container, int x, int y);

    protected abstract boolean isPowerItem(ItemStack stack);

    @Override
    protected final boolean moveFromPlayerInventory(ItemStack stack, int index) {
        if (isPowerItem(stack)) {
            return moveToSingleSlot(stack, BurnerPressBlockEntity.SLOT_FUEL);
        }
        if (PressRecipeRegistry.isStamp(stack)) {
            return moveToSingleSlot(stack, BurnerPressBlockEntity.SLOT_STAMP);
        }
        if (PressRecipeRegistry.hasAnyRecipeFor(stack)) {
            return moveToSingleSlot(stack, BurnerPressBlockEntity.SLOT_INPUT);
        }
        return moveWithinPlayerInventory(stack, index);
    }
}
