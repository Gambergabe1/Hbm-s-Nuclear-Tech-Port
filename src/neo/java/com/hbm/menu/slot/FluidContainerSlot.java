package com.hbm.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public final class FluidContainerSlot extends FilteredSlot {
    public FluidContainerSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y, FluidContainerSlot::hasFluidHandler);
    }

    public static boolean hasFluidHandler(ItemStack stack) {
        return !stack.isEmpty() && FluidUtil.getFluidHandler(stack.copyWithCount(1)).isPresent();
    }
}
