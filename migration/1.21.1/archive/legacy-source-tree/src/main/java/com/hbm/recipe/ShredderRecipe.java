package com.hbm.recipe;

import com.hbm.recipe.serializer.ShredderRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ShredderRecipe extends BaseMachineRecipe {
    public static final RecipeType<ShredderRecipe> TYPE = RecipeType.register("hbm:shredding");
    
    public ShredderRecipe(ResourceLocation id, Ingredient input, ItemStack output, int duration) {
        super(id, input, output, duration);
    }
    
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return new ShredderRecipeSerializer();
    }
}