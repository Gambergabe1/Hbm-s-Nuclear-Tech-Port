package com.hbm.recipe.serializer;

import com.google.gson.JsonObject;
import com.hbm.recipe.ShredderRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ShredderRecipeSerializer implements RecipeSerializer<ShredderRecipe> {
    
    @Override
    public ShredderRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack output = ItemStack.fromJson(json.get("output"));
        int duration = json.has("duration") ? json.get("duration").getAsInt() : 200;
        
        return new ShredderRecipe(recipeId, input, output, duration);
    }
    
    @Override
    public ShredderRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int duration = buffer.readInt();
        
        return new ShredderRecipe(recipeId, input, output, duration);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, ShredderRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeInt(recipe.getDuration());
    }
}