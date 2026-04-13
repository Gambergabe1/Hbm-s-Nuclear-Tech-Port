package com.hbm.armor;

import java.util.List;

import net.minecraft.world.item.ItemStack;

public interface FilteredMaskItem {
    List<HbmHazardClass> getHazardBlacklist(ItemStack stack);

    boolean isFilterApplicable(ItemStack maskStack, ItemStack filterStack);
}
