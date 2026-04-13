package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.menu.ShredderMenu;
import com.hbm.machine.ShredderRecipeRegistry;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmItems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ShredderBlockEntity extends AbstractProcessorMachineBlockEntity {
    public static final int INPUT_START = 0;
    public static final int INPUT_END = 9;
    public static final int OUTPUT_START = 9;
    public static final int OUTPUT_END = 27;
    public static final int LEFT_BLADE_SLOT = 27;
    public static final int RIGHT_BLADE_SLOT = 28;
    public static final int BATTERY_SLOT = 29;
    public static final int SLOT_COUNT = 30;
    public static final int MAX_PROGRESS = 60;
    public static final int MAX_POWER = 10_000;
    private static final int POWER_PER_TICK = 5;

    public ShredderBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_SHREDDER.get(), pos, blockState, SLOT_COUNT, MAX_POWER, MAX_POWER, 0);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ShredderBlockEntity blockEntity) {
        blockEntity.tickProcessorMachine();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machineShredder");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= INPUT_START && slot < INPUT_END) {
            return ShredderRecipeRegistry.hasRecipe(stack);
        }
        if (slot == LEFT_BLADE_SLOT || slot == RIGHT_BLADE_SLOT) {
            return isBlade(stack);
        }
        if (slot == BATTERY_SLOT) {
            return HbmEnergyHelper.canDischargeIntoMachine(stack);
        }
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ShredderMenu(containerId, inventory, this, getDataAccess());
    }

    @Override
    protected int getBatterySlot() {
        return BATTERY_SLOT;
    }

    @Override
    protected int getMaxProgress() {
        return MAX_PROGRESS;
    }

    @Override
    protected int getPowerPerTick() {
        return POWER_PER_TICK;
    }

    public int getBladeState(int slot) {
        ItemStack blade = items.get(slot);
        if (!isBlade(blade)) {
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

    @Override
    protected ItemStack getCurrentResult() {
        if (!hasWorkingBlades()) {
            return ItemStack.EMPTY;
        }

        for (int slot = INPUT_START; slot < INPUT_END; slot++) {
            ItemStack input = items.get(slot);
            if (input.isEmpty()) {
                continue;
            }

            ItemStack result = ShredderRecipeRegistry.getResult(input);
            if (!result.isEmpty() && canMergeResultIntoRange(OUTPUT_START, OUTPUT_END, result)) {
                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canProcessResult(ItemStack result) {
        return canMergeResultIntoRange(OUTPUT_START, OUTPUT_END, result);
    }

    @Override
    protected void completeProcessing(ItemStack result) {
        processItems();
        damageBlade(LEFT_BLADE_SLOT);
        damageBlade(RIGHT_BLADE_SLOT);
    }

    private void processItems() {
        for (int slot = INPUT_START; slot < INPUT_END; slot++) {
            ItemStack input = items.get(slot);
            if (input.isEmpty()) {
                continue;
            }

            ItemStack result = ShredderRecipeRegistry.getResult(input);
            if (result.isEmpty() || !canMergeResultIntoRange(OUTPUT_START, OUTPUT_END, result)) {
                continue;
            }

            storeOutput(result);
            input.shrink(1);
            if (input.isEmpty()) {
                items.set(slot, ItemStack.EMPTY);
            }
        }
    }

    private void storeOutput(ItemStack result) {
        mergeResultIntoRange(OUTPUT_START, OUTPUT_END, result);
    }

    private boolean hasWorkingBlades() {
        return getBladeState(LEFT_BLADE_SLOT) > 0
            && getBladeState(LEFT_BLADE_SLOT) < 3
            && getBladeState(RIGHT_BLADE_SLOT) > 0
            && getBladeState(RIGHT_BLADE_SLOT) < 3;
    }

    private void damageBlade(int slot) {
        ItemStack blade = items.get(slot);
        if (!blade.isDamageableItem() || blade.getMaxDamage() <= 0) {
            return;
        }
        blade.setDamageValue(Math.min(blade.getMaxDamage(), blade.getDamageValue() + 1));
    }

    public static boolean isBlade(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.is(HbmItems.BLADES_ALUMINUM.get())
            || stack.is(HbmItems.BLADES_GOLD.get())
            || stack.is(HbmItems.BLADES_IRON.get())
            || stack.is(HbmItems.BLADES_STEEL.get())
            || stack.is(HbmItems.BLADES_TITANIUM.get())
            || stack.is(HbmItems.BLADES_ADVANCED_ALLOY.get())
            || stack.is(HbmItems.BLADES_COMBINE_STEEL.get())
            || stack.is(HbmItems.BLADES_SCHRABIDIUM.get())
            || stack.is(HbmItems.BLADES_DESH.get());
    }
}
