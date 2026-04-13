package com.hbm.item;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingRemainderLoreItem extends LoreItem {
    private final Supplier<? extends Item> remainderItem;

    public CraftingRemainderLoreItem(Properties properties, Supplier<? extends Item> remainderItem) {
        super(properties);
        this.remainderItem = remainderItem;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(remainderItem.get());
    }
}
