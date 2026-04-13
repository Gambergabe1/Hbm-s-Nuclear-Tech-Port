package com.hbm.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ShredderRecipe {

    private final Ingredient ingredient;
    private final ItemStack result;

    public ShredderRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    // Method to check if this recipe matches an input item.
    public boolean matches(ItemStack input) {
        return ingredient.test(input);
    }

    // Method to get the output for this recipe.
    public ItemStack getOutput() {
        return result.copy();
    }
}
