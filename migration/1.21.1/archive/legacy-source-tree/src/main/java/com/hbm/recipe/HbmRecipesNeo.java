package com.hbm.recipe;

import com.hbm.main.HbmMod;
import com.hbm.recipe.serializer.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class HbmRecipesNeo {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.RECIPE_TYPES, HbmMod.MODID);
        
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
        DeferredRegister.create(NeoForgeRegistries.RECIPE_SERIALIZERS, HbmMod.MODID);
    
    // Register recipe types
    public static final RegistryObject<RecipeType<PressRecipe>> PRESS_TYPE = 
        RECIPE_TYPES.register("pressing", () -> PressRecipe.TYPE);
        
    public static final RegistryObject<RecipeType<ShredderRecipe>> SHREDDER_TYPE = 
        RECIPE_TYPES.register("shredding", () -> ShredderRecipe.TYPE);
        
    public static final RegistryObject<RecipeType<CentrifugeRecipe>> CENTRIFUGE_TYPE = 
        RECIPE_TYPES.register("centrifuging", () -> CentrifugeRecipe.TYPE);
        
    public static final RegistryObject<RecipeType<RBMKRecipe>> RBMK_TYPE = 
        RECIPE_TYPES.register("rbmk", () -> RBMKRecipe.TYPE);
        
    public static final RegistryObject<RecipeType<ChemPlantRecipe>> CHEM_PLANT_TYPE = 
        RECIPE_TYPES.register("chem_plant", () -> ChemPlantRecipe.TYPE);
    
    // Register recipe serializers
    public static final RegistryObject<RecipeSerializer<PressRecipe>> PRESS_SERIALIZER = 
        RECIPE_SERIALIZERS.register("pressing", PressRecipeSerializer::new);
        
    public static final RegistryObject<RecipeSerializer<ShredderRecipe>> SHREDDER_SERIALIZER = 
        RECIPE_SERIALIZERS.register("shredding", ShredderRecipeSerializer::new);
        
    public static final RegistryObject<RecipeSerializer<CentrifugeRecipe>> CENTRIFUGE_SERIALIZER = 
        RECIPE_SERIALIZERS.register("centrifuging", CentrifugeRecipeSerializer::new);
        
    public static final RegistryObject<RecipeSerializer<RBMKRecipe>> RBMK_SERIALIZER = 
        RECIPE_SERIALIZERS.register("rbmk", RBMKRecipeSerializer::new);
        
    public static final RegistryObject<RecipeSerializer<ChemPlantRecipe>> CHEM_PLANT_SERIALIZER = 
        RECIPE_SERIALIZERS.register("chem_plant", ChemPlantRecipeSerializer::new);
    
    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}