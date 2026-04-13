package com.hbm.recipe;

import com.hbm.recipe.serializer.CentrifugeRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.NonNullList;

public class CentrifugeRecipe extends BaseMachineRecipe {
    public static final RecipeType<CentrifugeRecipe> TYPE = RecipeType.register("hbm:centrifuging");
    private final ItemStack output2;
    
    public CentrifugeRecipe(ResourceLocation id, Ingredient input, ItemStack output1, ItemStack output2, int duration) {
        super(id, input, output1, duration);
        this.output2 = output2;
    }
    
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return new CentrifugeRecipeSerializer();
    }
    
    public ItemStack getOutput1() {
        return this.output;
    }
    
    public ItemStack getOutput2() {
        return this.output2;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.input);
        return ingredients;
    }
}