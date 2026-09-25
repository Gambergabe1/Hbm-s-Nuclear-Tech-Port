package com.hbm.menu;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.blockentity.ShredderBlockEntity;
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

public final class ShredderMenu extends AbstractMachineMenu {
    public ShredderMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(ShredderBlockEntity.SLOT_COUNT),
            new SimpleContainerData(2)
        );
    }

    public ShredderMenu(int containerId, Inventory playerInventory, FriendlyByteBuf ignoredBuffer) {
        this(containerId, playerInventory);
    }

    public ShredderMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(HbmMenuTypes.MACHINE_SHREDDER.get(), containerId, playerInventory, container, ShredderBlockEntity.SLOT_COUNT, data, 2);

        addMachineSlot(0, 44, 18, false, false);
        addMachineSlot(1, 62, 18, false, false);
        addMachineSlot(2, 80, 18, false, false);
        addMachineSlot(3, 44, 36, false, false);
        addMachineSlot(4, 62, 36, false, false);
        addMachineSlot(5, 80, 36, false, false);
        addMachineSlot(6, 44, 54, false, false);
        addMachineSlot(7, 62, 54, false, false);
        addMachineSlot(8, 80, 54, false, false);
        addMachineSlot(9, 116, 18, true, false);
        addMachineSlot(10, 134, 18, true, false);
        addMachineSlot(11, 152, 18, true, false);
        addMachineSlot(12, 116, 36, true, false);
        addMachineSlot(13, 134, 36, true, false);
        addMachineSlot(14, 152, 36, true, false);
        addMachineSlot(15, 116, 54, true, false);
        addMachineSlot(16, 134, 54, true, false);
        addMachineSlot(17, 152, 54, true, false);
        addMachineSlot(18, 116, 72, true, false);
        addMachineSlot(19, 134, 72, true, false);
        addMachineSlot(20, 152, 72, true, false);
        addMachineSlot(21, 116, 90, true, false);
        addMachineSlot(22, 134, 90, true, false);
        addMachineSlot(23, 152, 90, true, false);
        addMachineSlot(24, 116, 108, true, false);
        addMachineSlot(25, 134, 108, true, false);
        addMachineSlot(26, 152, 108, true, false);
        addMachineSlot(ShredderBlockEntity.LEFT_BLADE_SLOT, 44, 108, false, true);
        addMachineSlot(ShredderBlockEntity.RIGHT_BLADE_SLOT, 80, 108, false, true);
        addMachineSlot(ShredderBlockEntity.BATTERY_SLOT, 8, 108, false, false);

        addPlayerInventorySlots(8, 151);
        addDataSlots(data);
    }

    private void addMachineSlot(int slot, int x, int y, boolean outputOnly, boolean bladeSlot) {
        if (outputOnly) {
            addSlot(new OutputSlot(container, slot, x, y));
            return;
        }
        if (bladeSlot) {
            addSlot(new FilteredSlot(container, slot, x, y, ShredderBlockEntity::isBlade));
            return;
        }
        if (slot == ShredderBlockEntity.BATTERY_SLOT) {
            addSlot(new BatteryInputSlot(container, slot, x, y));
            return;
        }
        addSlot(new FilteredSlot(container, slot, x, y, stack -> ShredderBlockEntity.hasRecipe(playerInventory.player.level(), stack)));
    }

    @Override
    public boolean stillValid(Player player) {
        return isMachineStillValid(player, HbmBlocks.MACHINE_SHREDDER.get());
    }

    @Override
    protected boolean moveFromPlayerInventory(ItemStack stack, int index) {
        if (HbmEnergyHelper.canDischargeIntoMachine(stack)) {
            return moveToSingleSlot(stack, ShredderBlockEntity.BATTERY_SLOT);
        }
        if (ShredderBlockEntity.isBlade(stack)) {
            return moveToRange(stack, ShredderBlockEntity.LEFT_BLADE_SLOT, ShredderBlockEntity.BATTERY_SLOT);
        }
        if (ShredderBlockEntity.hasRecipe(playerInventory.player.level(), stack)) {
            return moveToRange(stack, ShredderBlockEntity.INPUT_START, ShredderBlockEntity.INPUT_END);
        }
        return moveWithinPlayerInventory(stack, index);
    }

    public int getPowerScaled(int pixels) {
        return getScaledData(0, pixels, ShredderBlockEntity.MAX_POWER);
    }

    public int getProgressScaled(int pixels) {
        return getScaledData(1, pixels, ShredderBlockEntity.MAX_PROGRESS);
    }

    public int getBladeState(boolean left) {
        int slot = left ? ShredderBlockEntity.LEFT_BLADE_SLOT : ShredderBlockEntity.RIGHT_BLADE_SLOT;
        ItemStack blade = getMenuContainer().getItem(slot);
        if (!ShredderBlockEntity.isBlade(blade)) {
            return 0;
        }
        if (!blade.isDamageableItem() || blade.getMaxDamage() <= 0) {
            return 1;
        }
        if (blade.getDamageValue() >= blade.getMaxDamage()) {
            return 3;
        }
        if (blade.getDamageValue() >= blade.getMaxDamage() / 2) {
            return 2;
        }
        return 1;
    }

    public boolean hasBladeWarning() {
        int leftState = getBladeState(true);
        int rightState = getBladeState(false);
        return leftState == 0 || leftState == 3 || rightState == 0 || rightState == 3;
    }
}
