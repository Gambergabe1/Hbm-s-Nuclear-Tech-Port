package com.hbm.machine;

import java.util.Optional;

import com.hbm.recipe.PressRecipe;
import com.hbm.registry.HbmRecipeSerializers;
import com.hbm.registry.HbmTags;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class PressRecipeRegistry {
    private PressRecipeRegistry() {
    }

    public static boolean isStamp(ItemStack stack) {
        return HbmTags.itemMatches(stack, HbmTags.pressStamps());
    }

    public static boolean hasAnyRecipeFor(Level level, ItemStack input) {
        return findRecipe(level, input).isPresent();
    }

    public static ItemStack getResult(Level level, ItemStack input, ItemStack stamp) {
        if (level == null || input.isEmpty() || stamp.isEmpty() || !isStamp(stamp)) {
            return ItemStack.EMPTY;
        }
        return findRecipe(level, input)
            .map(holder -> holder.value().assemble(new SingleRecipeInput(input), level.registryAccess()))
            .orElse(ItemStack.EMPTY);
    }

    private static Optional<RecipeHolder<PressRecipe>> findRecipe(Level level, ItemStack input) {
        if (level == null || input.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager()
            .getRecipeFor(HbmRecipeSerializers.PRESSING_TYPE.get(), new SingleRecipeInput(input), level);
    }
}
