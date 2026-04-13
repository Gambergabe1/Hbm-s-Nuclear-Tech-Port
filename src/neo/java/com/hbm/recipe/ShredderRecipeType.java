package com.hbm.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public final class ShredderRecipeType implements RecipeType<Recipe<?>> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("hbm", "shredding");
    public static final ShredderRecipeType INSTANCE = new ShredderRecipeType();

    private ShredderRecipeType() {
    }

    @Override
    public String toString() {
        return ID.toString();
    }
}
