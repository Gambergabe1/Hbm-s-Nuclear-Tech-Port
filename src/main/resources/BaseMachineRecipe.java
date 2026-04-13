package com.hbm.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;

public abstract class BaseMachineRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient input;
    protected final ItemStack output;
    protected final int duration;
    
    public BaseMachineRecipe(ResourceLocation id, Ingredient input, ItemStack output, int duration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.duration = duration;
    }
    
    @Override
    public boolean matches(Container container, Level level) {
        return this.input.test(container.getItem(0));
    }
    
    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.output.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
    }
    
    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    
    public Ingredient getInput() {
        return this.input;
    }
    
    public ItemStack getOutput() {
        return this.output;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    // Abstract method to be implemented by subclasses for their specific RecipeType
    @Override
    public abstract RecipeType<?> getType();
    
    @Override
    public abstract RecipeSerializer<?> getSerializer();
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.input);
        return ingredients;
    }
}