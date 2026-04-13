package com.hbm.recipe;

import com.hbm.main.Hbm;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HbmRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Hbm.MODID);
    
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
    
    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}