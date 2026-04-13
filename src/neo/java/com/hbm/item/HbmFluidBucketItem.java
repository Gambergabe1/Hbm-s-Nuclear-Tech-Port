package com.hbm.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidUtil;

public final class HbmFluidBucketItem extends BucketItem {
    public HbmFluidBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component containedName = FluidUtil.getFluidContained(stack)
            .map(fluidStack -> (Component) Component.literal("Bucket of ").append(fluidStack.getHoverName()))
            .orElse(null);
        return containedName != null ? containedName : super.getName(stack);
    }
}
