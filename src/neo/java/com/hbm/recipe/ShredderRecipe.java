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
 * Data-driven shredder recipe: one item in, one item out. Loaded from
 * {@code data/hbm/recipe/shredding/*.json} and exposed to KubeJS via the
 * "hbm:shredding" schema registered in the KubeJS plugin.
 */
public record ShredderRecipe(Ingredient ingredient, ItemStack result) implements Recipe<SingleRecipeInput> {

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
        return HbmRecipeSerializers.SHREDDER_SERIALIZER.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return (RecipeType<? extends Recipe<SingleRecipeInput>>) (RecipeType<?>) HbmRecipeSerializers.SHREDDER_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<ShredderRecipe> {
        public static final MapCodec<ShredderRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ShredderRecipe::ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(ShredderRecipe::result)
        ).apply(instance, ShredderRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ShredderRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ShredderRecipe::ingredient,
            ItemStack.STREAM_CODEC, ShredderRecipe::result,
            ShredderRecipe::new
        );

        @Override
        public MapCodec<ShredderRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShredderRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
