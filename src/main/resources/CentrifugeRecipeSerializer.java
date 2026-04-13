package com.hbm.recipe.serializer;

import com.google.gson.JsonObject;
import com.hbm.recipe.CentrifugeRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CentrifugeRecipeSerializer implements RecipeSerializer<CentrifugeRecipe> {
    
    @Override
    public CentrifugeRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack output1 = ItemStack.fromJson(json.getAsJsonObject("output1"));
        ItemStack output2 = json.has("output2") ? ItemStack.fromJson(json.getAsJsonObject("output2")) : ItemStack.EMPTY;
        int duration = json.has("duration") ? json.get("duration").getAsInt() : 200;
        
        return new CentrifugeRecipe(recipeId, input, output1, output2, duration);
    }
    
    @Override
    public CentrifugeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output1 = buffer.readItem();
        ItemStack output2 = buffer.readItem();
        int duration = buffer.readInt();
        
        return new CentrifugeRecipe(recipeId, input, output1, output2, duration);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, CentrifugeRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput1());
        buffer.writeItem(recipe.getOutput2());
        buffer.writeInt(recipe.getDuration());
    }
}