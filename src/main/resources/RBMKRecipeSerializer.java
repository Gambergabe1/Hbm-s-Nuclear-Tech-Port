package com.hbm.recipe.serializer;

import com.google.gson.JsonObject;
import com.hbm.recipe.RBMKRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RBMKRecipeSerializer implements RecipeSerializer<RBMKRecipe> {
    
    @Override
    public RBMKRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack output = ItemStack.fromJson(json.get("output"));
        int duration = json.has("duration") ? json.get("duration").getAsInt() : 200;
        int heat = json.has("heat") ? json.get("heat").getAsInt() : 0;
        int power = json.has("power") ? json.get("power").getAsInt() : 0;
        
        return new RBMKRecipe(recipeId, input, output, duration, heat, power);
    }
    
    @Override
    public RBMKRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int duration = buffer.readInt();
        int heat = buffer.readInt();
        int power = buffer.readInt();
        
        return new RBMKRecipe(recipeId, input, output, duration, heat, power);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, RBMKRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeInt(recipe.getDuration());
        buffer.writeInt(recipe.getHeat());
        buffer.writeInt(recipe.getPower());
    }
}