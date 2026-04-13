package com.hbm.menu;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.blockentity.ElectricFurnaceBlockEntity;
import com.hbm.menu.slot.BatteryInputSlot;
import com.hbm.menu.slot.FilteredSlot;
import com.hbm.menu.slot.OutputSlot;
import com.hbm.registry.HbmBlocks;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public final class ElectricFurnaceMenu extends AbstractMachineMenu {
    public ElectricFurnaceMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(ElectricFurnaceBlockEntity.SLOT_COUNT),
            new SimpleContainerData(2)
        );
    }

    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, FriendlyByteBuf ignoredBuffer) {
        this(containerId, playerInventory);
    }

    public ElectricFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HbmMenuTypes.MACHINE_ELECTRIC_FURNACE.get(), containerId, playerInventory, container, ElectricFurnaceBlockEntity.SLOT_COUNT, data, 2);
        addSlot(new BatteryInputSlot(container, ElectricFurnaceBlockEntity.SLOT_BATTERY, 56, 53));
        addSlot(new FilteredSlot(
            container,
            ElectricFurnaceBlockEntity.SLOT_INPUT,
            56,
            17,
            stack -> ElectricFurnaceBlockEntity.hasRecipe(playerInventory.player.level(), stack)
        ));
        addSlot(new OutputSlot(container, ElectricFurnaceBlockEntity.SLOT_OUTPUT, 116, 35));
        addPlayerInventorySlots(8, 84);
        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return isMachineStillValid(player, HbmBlocks.MACHINE_ELECTRIC_FURNACE.get());
    }

    @Override
    protected boolean moveFromPlayerInventory(ItemStack stack, int index) {
        if (HbmEnergyHelper.canDischargeIntoMachine(stack)) {
            return moveToSingleSlot(stack, ElectricFurnaceBlockEntity.SLOT_BATTERY);
        }
        if (ElectricFurnaceBlockEntity.hasRecipe(playerInventory.player.level(), stack)) {
            return moveToSingleSlot(stack, ElectricFurnaceBlockEntity.SLOT_INPUT);
        }
        return moveWithinPlayerInventory(stack, index);
    }

    public int getPowerScaled(int pixels) {
        return getScaledData(0, pixels, ElectricFurnaceBlockEntity.MAX_POWER);
    }

    public int getProgressScaled(int pixels) {
        return getScaledData(1, pixels, ElectricFurnaceBlockEntity.MAX_PROGRESS);
    }

    public boolean isProcessing() {
        return getDataValue(1) > 0;
    }
}
