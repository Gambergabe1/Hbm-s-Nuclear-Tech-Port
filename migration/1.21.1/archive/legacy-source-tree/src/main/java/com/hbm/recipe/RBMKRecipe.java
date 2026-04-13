package com.hbm.recipe;

import com.hbm.recipe.serializer.RBMKRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RBMKRecipe extends BaseMachineRecipe {
    public static final RecipeType<RBMKRecipe> TYPE = RecipeType.register("hbm:rbmk");
    private final int heat;
    private final int power;
    
    public RBMKRecipe(ResourceLocation id, Ingredient input, ItemStack output, int duration, int heat, int power) {
        super(id, input, output, duration);
        this.heat = heat;
        this.power = power;
    }
    
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return new RBMKRecipeSerializer();
    }
    
    public int getHeat() {
        return this.heat;
    }
    
    public int getPower() {
        return this.power;
    }
}