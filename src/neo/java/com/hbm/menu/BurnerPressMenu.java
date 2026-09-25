package com.hbm.menu;

import com.hbm.blockentity.BurnerPressBlockEntity;
import com.hbm.menu.slot.FuelSlot;
import com.hbm.registry.HbmBlocks;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public final class BurnerPressMenu extends AbstractPressMenu {
    public BurnerPressMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            createClientContainer(),
            createClientData(4)
        );
    }

    public BurnerPressMenu(int containerId, Inventory playerInventory, FriendlyByteBuf ignoredBuffer) {
        this(containerId, playerInventory);
    }

    public BurnerPressMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HbmMenuTypes.MACHINE_PRESS.get(), containerId, playerInventory, container, data, 4, 26);
    }

    @Override
    public boolean stillValid(Player player) {
        return isMachineStillValid(player, HbmBlocks.MACHINE_PRESS.get());
    }

    @Override
    protected Slot createPowerSlot(Container container, int x, int y) {
        return new FuelSlot(container, BurnerPressBlockEntity.SLOT_FUEL, x, y, RecipeType.SMELTING);
    }

    @Override
    protected boolean isPowerItem(ItemStack stack) {
        return stack.getBurnTime(RecipeType.SMELTING) > 0;
    }

    public int getPowerFrame() {
        return getDataValue(0) <= 0 ? 0 : Math.min(12, getDataValue(0) * 12 / BurnerPressBlockEntity.MAX_POWER);
    }

    public int getBurnScaled(int pixels) {
        int maxBurn = getDataValue(3);
        if (maxBurn <= 0) {
            return 0;
        }
        return getDataValue(2) * pixels / maxBurn;
    }

    public int getProgressScaled(int pixels) {
        return getScaledData(1, pixels, BurnerPressBlockEntity.MAX_PROGRESS);
    }
}
