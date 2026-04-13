package com.hbm.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;

public final class FuelSlot extends FilteredSlot {
    public FuelSlot(Container container, int slot, int x, int y, RecipeType<?> recipeType) {
        super(container, slot, x, y, stack -> stack.getBurnTime(recipeType) > 0);
    }
}
