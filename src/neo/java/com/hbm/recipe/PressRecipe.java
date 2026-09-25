package com.hbm.recipe;

import com.hbm.registry.HbmRecipeSerializers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

/**
 * Data-driven press recipe: one item in, one item out. Any item in the
 * {@code #hbm:press_stamps} tag works as the (non-consumed, not part of the
 * recipe match) stamp catalyst - that check lives in the press block entities
 * / menus, mirroring how vanilla keeps furnace fuel out of the smelting
 * recipe JSON. Loaded from {@code data/hbm/recipe/pressing/*.json}.
 */
public record PressRecipe(Ingredient ingredient, ItemStack result) implements Recipe<SingleRecipeInput> {

    @Override
    public boolean matches(SingleRecipeInput input, net.minecraft.world.level.Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return HbmRecipeSerializers.PRESSING_SERIALIZER.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return (RecipeType<? extends Recipe<SingleRecipeInput>>) (RecipeType<?>) HbmRecipeSerializers.PRESSING_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<PressRecipe> {
        public static final MapCodec<PressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(PressRecipe::ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(PressRecipe::result)
        ).apply(instance, PressRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PressRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, PressRecipe::ingredient,
            ItemStack.STREAM_CODEC, PressRecipe::result,
            PressRecipe::new
        );

        @Override
        public MapCodec<PressRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PressRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
