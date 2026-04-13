package com.hbm.api.fluid;

import java.util.function.Predicate;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class HbmFluidTank extends FluidTank {
    private Runnable changeListener = () -> {};

    public HbmFluidTank(int capacity) {
        super(capacity);
    }

    public HbmFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public HbmFluidTank setChangeListener(Runnable changeListener) {
        this.changeListener = changeListener != null ? changeListener : () -> {};
        return this;
    }

    public HbmFluidTank load(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        readFromNBT(lookupProvider, nbt);
        return this;
    }

    public CompoundTag save(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        return writeToNBT(lookupProvider, nbt);
    }

    public void setCapacityAndClamp(int capacity) {
        int clampedCapacity = Math.max(0, capacity);
        if (this.capacity == clampedCapacity && getFluidAmount() <= clampedCapacity) {
            return;
        }

        this.capacity = clampedCapacity;
        if (!fluid.isEmpty() && fluid.getAmount() > clampedCapacity) {
            fluid = fluid.copyWithAmount(clampedCapacity);
        }
        onContentsChanged();
    }

    @Override
    public void setFluid(FluidStack stack) {
        FluidStack newStack = stack.isEmpty() ? FluidStack.EMPTY : stack.copy();
        boolean changed = !FluidStack.isSameFluidSameComponents(fluid, newStack)
            || fluid.getAmount() != newStack.getAmount();
        fluid = newStack;
        if (changed) {
            onContentsChanged();
        }
    }

    @Override
    protected void onContentsChanged() {
        changeListener.run();
    }
}
