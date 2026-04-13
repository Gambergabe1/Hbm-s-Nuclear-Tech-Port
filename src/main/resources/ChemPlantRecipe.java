package com.hbm.recipe;

import com.hbm.recipe.serializer.ChemPlantRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;

import java.util.List;

public class ChemPlantRecipe implements Recipe<Container> {
    public static final RecipeType<ChemPlantRecipe> TYPE = RecipeType.register("hbm:chem_plant");
    
    private final ResourceLocation id;
    private final List<Ingredient> inputs;
    private final List<ItemStack> outputs;
    private final int duration;
    
    public ChemPlantRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> outputs, int duration) {
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
        this.duration = duration;
    }
    
    @Override
    public boolean matches(Container container, Level level) {
        if (this.inputs.size() > container.getContainerSize()) {
            return false;
        }
        
        for (int i = 0; i < this.inputs.size(); i++) {
            if (!this.inputs.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.outputs.isEmpty() ? ItemStack.EMPTY : this.outputs.get(0).copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.outputs.isEmpty() ? ItemStack.EMPTY : this.outputs.get(0);
    }
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return new ChemPlantRecipeSerializer();
    }
    
    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(this.inputs);
        return ingredients;
    }
    
    public List<Ingredient> getInputs() {
        return this.inputs;
    }
    
    public List<ItemStack> getOutputs() {
        return this.outputs;
    }
    
    public int getDuration() {
        return this.duration;
    }
}