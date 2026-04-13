package com.hbm.menu;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.blockentity.ElectricPressBlockEntity;
import com.hbm.menu.slot.BatteryInputSlot;
import com.hbm.registry.HbmBlocks;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;

public final class ElectricPressMenu extends AbstractPressMenu {
    public ElectricPressMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            createClientContainer(),
            createClientData(2)
        );
    }

    public ElectricPressMenu(int containerId, Inventory playerInventory, FriendlyByteBuf ignoredBuffer) {
        this(containerId, playerInventory);
    }

    public ElectricPressMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HbmMenuTypes.MACHINE_EPRESS.get(), containerId, playerInventory, container, data, 2, 44);
    }

    @Override
    public boolean stillValid(Player player) {
        return isMachineStillValid(player, HbmBlocks.MACHINE_EPRESS.get());
    }

    @Override
    protected Slot createPowerSlot(Container container, int x, int y) {
        return new BatteryInputSlot(container, ElectricPressBlockEntity.SLOT_BATTERY, x, y);
    }

    @Override
    protected boolean isPowerItem(ItemStack stack) {
        return HbmEnergyHelper.canDischargeIntoMachine(stack);
    }

    public int getPowerScaled(int pixels) {
        return getScaledData(0, pixels, ElectricPressBlockEntity.MAX_POWER);
    }

    public int getProgressScaled(int pixels) {
        return getScaledData(1, pixels, ElectricPressBlockEntity.MAX_PROGRESS);
    }
}
