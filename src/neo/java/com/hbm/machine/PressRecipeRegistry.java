package com.hbm.machine;

import com.hbm.registry.HbmItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PressRecipeRegistry {
    private PressRecipeRegistry() {
    }

    public static boolean isStamp(ItemStack stack) {
        return isPlateStamp(stack);
    }

    public static boolean hasAnyRecipeFor(ItemStack input) {
        return !getPlateResult(input).isEmpty();
    }

    public static ItemStack getResult(ItemStack input, ItemStack stamp) {
        if (input.isEmpty() || stamp.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (isPlateStamp(stamp)) {
            return getPlateResult(input);
        }
        return ItemStack.EMPTY;
    }

    private static boolean isPlateStamp(ItemStack stamp) {
        return stamp.is(HbmItems.STAMP_STONE_PLATE.get())
            || stamp.is(HbmItems.STAMP_IRON_PLATE.get())
            || stamp.is(HbmItems.STAMP_STEEL_PLATE.get())
            || stamp.is(HbmItems.STAMP_TITANIUM_PLATE.get())
            || stamp.is(HbmItems.STAMP_OBSIDIAN_PLATE.get())
            || stamp.is(HbmItems.STAMP_SCHRABIDIUM_PLATE.get())
            || stamp.is(HbmItems.STAMP_DESH_PLATE.get());
    }

    private static ItemStack getPlateResult(ItemStack input) {
        if (input.is(Items.IRON_INGOT)) {
            return new ItemStack(HbmItems.PLATE_IRON.get());
        }
        if (input.is(Items.GOLD_INGOT)) {
            return new ItemStack(HbmItems.PLATE_GOLD.get());
        }
        if (input.is(HbmItems.INGOT_TITANIUM.get())) {
            return new ItemStack(HbmItems.PLATE_TITANIUM.get());
        }
        if (input.is(HbmItems.INGOT_ALUMINIUM.get())) {
            return new ItemStack(HbmItems.PLATE_ALUMINIUM.get());
        }
        if (input.is(HbmItems.INGOT_STEEL.get())) {
            return new ItemStack(HbmItems.PLATE_STEEL.get());
        }
        if (input.is(HbmItems.INGOT_LEAD.get())) {
            return new ItemStack(HbmItems.PLATE_LEAD.get());
        }
        if (input.is(HbmItems.INGOT_COPPER.get())) {
            return new ItemStack(HbmItems.PLATE_COPPER.get());
        }
        if (input.is(HbmItems.INGOT_ADVANCED_ALLOY.get())) {
            return new ItemStack(HbmItems.PLATE_ADVANCED_ALLOY.get());
        }
        if (input.is(HbmItems.INGOT_COMBINE_STEEL.get())) {
            return new ItemStack(HbmItems.PLATE_COMBINE_STEEL.get());
        }
        if (input.is(HbmItems.INGOT_SATURNITE.get())) {
            return new ItemStack(HbmItems.PLATE_SATURNITE.get());
        }
        return ItemStack.EMPTY;
    }
}
