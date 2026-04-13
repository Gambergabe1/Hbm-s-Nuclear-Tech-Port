package com.hbm.blockentity;

import net.neoforged.neoforge.fluids.FluidStack;

final class FluidMachineTrackedState {
    private final FluidStack fluid;

    FluidMachineTrackedState(FluidStack fluid) {
        this.fluid = fluid == null ? FluidStack.EMPTY : fluid.copy();
    }

    FluidStack fluid() {
        return fluid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FluidMachineTrackedState state)) {
            return false;
        }

        return fluid.getAmount() == state.fluid.getAmount()
            && FluidStack.isSameFluidSameComponents(fluid, state.fluid);
    }

    @Override
    public int hashCode() {
        return 31 * fluid.getAmount() + fluid.getFluid().hashCode();
    }
}
