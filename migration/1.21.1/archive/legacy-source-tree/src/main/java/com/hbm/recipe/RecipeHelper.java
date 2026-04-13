package com.hbm.recipe;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

public class RecipeHelper {
    
    public static <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> recipeType, C container, Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        return recipeManager.getRecipeFor(recipeType, container, level);
    }
    
    public static Optional<PressRecipe> getPressRecipe(Container container, Level level) {
        return getRecipeFor(PressRecipe.TYPE, container, level);
    }
    
    public static Optional<ShredderRecipe> getShredderRecipe(Container container, Level level) {
        return getRecipeFor(ShredderRecipe.TYPE, container, level);
    }
    
    public static Optional<CentrifugeRecipe> getCentrifugeRecipe(Container container, Level level) {
        return getRecipeFor(CentrifugeRecipe.TYPE, container, level);
    }
    
    public static Optional<RBMKRecipe> getRBMKRecipe(Container container, Level level) {
        return getRecipeFor(RBMKRecipe.TYPE, container, level);
    }
    
    public static Optional<ChemPlantRecipe> getChemPlantRecipe(Container container, Level level) {
        return getRecipeFor(ChemPlantRecipe.TYPE, container, level);
    }
}