package com.hbm.recipe.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbm.recipe.ChemPlantRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class ChemPlantRecipeSerializer implements RecipeSerializer<ChemPlantRecipe> {
    
    @Override
    public ChemPlantRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray inputArray = json.getAsJsonArray("inputs");
        List<Ingredient> inputs = new ArrayList<>();
        
        for (int i = 0; i < inputArray.size(); i++) {
            inputs.add(Ingredient.fromJson(inputArray.get(i)));
        }
        
        JsonArray outputArray = json.getAsJsonArray("outputs");
        List<ItemStack> outputs = new ArrayList<>();
        
        for (int i = 0; i < outputArray.size(); i++) {
            outputs.add(ItemStack.fromJson(outputArray.get(i).getAsJsonObject()));
        }
        
        int duration = json.has("duration") ? json.get("duration").getAsInt() : 200;
        
        return new ChemPlantRecipe(recipeId, inputs, outputs, duration);
    }
    
    @Override
    public ChemPlantRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        int inputCount = buffer.readInt();
        List<Ingredient> inputs = new ArrayList<>();
        
        for (int i = 0; i < inputCount; i++) {
            inputs.add(Ingredient.fromNetwork(buffer));
        }
        
        int outputCount = buffer.readInt();
        List<ItemStack> outputs = new ArrayList<>();
        
        for (int i = 0; i < outputCount; i++) {
            outputs.add(buffer.readItem());
        }
        
        int duration = buffer.readInt();
        
        return new ChemPlantRecipe(recipeId, inputs, outputs, duration);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buffer, ChemPlantRecipe recipe) {
        buffer.writeInt(recipe.getInputs().size());
        for (Ingredient ingredient : recipe.getInputs()) {
            ingredient.toNetwork(buffer);
        }
        
        buffer.writeInt(recipe.getOutputs().size());
        for (ItemStack stack : recipe.getOutputs()) {
            buffer.writeItem(stack);
        }
        
        buffer.writeInt(recipe.getDuration());
    }
}