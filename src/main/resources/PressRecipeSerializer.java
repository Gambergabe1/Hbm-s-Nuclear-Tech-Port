package com.hbm.recipe.serializer;

import com.google.gson.JsonObject;
import com.hbm.recipe.PressRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PressRecipeSerializer implements RecipeSerializer<PressRecipe> {
    
    @Override
    public PressRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack output = ItemStack.fromJson(json.get("output"));
        int duration = json.has("duration") ? json.get("duration").getAsInt() : 200;
        
        return new PressRecipe(recipeId, input, output, duration);
    }
    
    @Override
    public PressRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int duration = buffer.readInt();
        
        return new PressRecipe(recipeId, input, output, duration);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, PressRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeInt(recipe.getDuration());
    }
}