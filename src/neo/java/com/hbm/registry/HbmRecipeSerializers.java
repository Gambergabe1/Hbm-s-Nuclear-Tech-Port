package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.recipe.ShredderRecipe;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmRecipeSerializers {
    public static final DeferredRegister<net.minecraft.world.item.crafting.RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(Registries.RECIPE_TYPE, HbmNuclearTech.MODID);
    public static final DeferredRegister<net.minecraft.world.item.crafting.RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, HbmNuclearTech.MODID);

    public static final DeferredHolder<net.minecraft.world.item.crafting.RecipeType<?>, net.minecraft.world.item.crafting.RecipeType<ShredderRecipe>> SHREDDER_TYPE =
        RECIPE_TYPES.register("shredding", () -> net.minecraft.world.item.crafting.RecipeType.simple(
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "shredding")));

    public static final DeferredHolder<net.minecraft.world.item.crafting.RecipeSerializer<?>, ShredderRecipe.Serializer> SHREDDER_SERIALIZER =
        RECIPE_SERIALIZERS.register("shredding", ShredderRecipe.Serializer::new);

    private HbmRecipeSerializers() {
    }

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
