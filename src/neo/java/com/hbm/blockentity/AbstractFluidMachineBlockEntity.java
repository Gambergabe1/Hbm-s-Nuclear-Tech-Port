package com.hbm.blockentity;

import com.hbm.api.fluid.HbmFluidTank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public abstract class AbstractFluidMachineBlockEntity extends AbstractMachineBlockEntity implements IFluidHandler {
    private final HbmFluidTank tank;

    protected AbstractFluidMachineBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        int capacity
    ) {
        super(type, pos, blockState, slotCount);
        this.tank = new HbmFluidTank(capacity).setChangeListener(this::setChanged);
    }

    public HbmFluidTank getFluidTank() {
        return tank;
    }

    protected boolean canAcceptFluid() {
        return true;
    }

    protected boolean canProvideFluid() {
        return true;
    }

    @Override
    protected void loadSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("capacity")) {
            tank.setCapacityAndClamp(tag.getInt("capacity"));
        }
        tank.load(registries, tag.getCompound("tank"));
        super.loadSharedMachineData(tag, registries);
    }

    @Override
    protected void saveSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("capacity", tank.getCapacity());
        tag.put("tank", tank.save(registries, new CompoundTag()));
        super.saveSharedMachineData(tag, registries);
    }

    @Override
    public int getTanks() {
        return tank.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int tankIndex) {
        return tank.getFluidInTank(tankIndex);
    }

    @Override
    public int getTankCapacity(int tankIndex) {
        return tank.getTankCapacity(tankIndex);
    }

    @Override
    public boolean isFluidValid(int tankIndex, FluidStack stack) {
        return tank.isFluidValid(tankIndex, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!canAcceptFluid()) {
            return 0;
        }

        int filled = tank.fill(resource, action);
        if (action.execute() && filled > 0) {
            setChanged();
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!canProvideFluid()) {
            return FluidStack.EMPTY;
        }

        FluidStack drained = tank.drain(resource, action);
        if (action.execute() && !drained.isEmpty()) {
            setChanged();
        }
        return drained;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!canProvideFluid()) {
            return FluidStack.EMPTY;
        }

        FluidStack drained = tank.drain(maxDrain, action);
        if (action.execute() && !drained.isEmpty()) {
            setChanged();
        }
        return drained;
    }

    protected final boolean drainContainerIntoTank(int inputSlot, int outputSlot) {
        ItemStack input = items.get(inputSlot);
        if (input.isEmpty() || tank.getFluidAmount() >= tank.getCapacity()) {
            return false;
        }

        ItemStack singleItem = input.copyWithCount(1);
        FluidActionResult simulated = FluidUtil.tryEmptyContainer(singleItem, this, Integer.MAX_VALUE, null, false);
        if (!simulated.isSuccess() || !canStoreProcessedContainer(outputSlot, simulated.getResult())) {
            return false;
        }

        FluidActionResult executed = FluidUtil.tryEmptyContainer(singleItem, this, Integer.MAX_VALUE, null, true);
        if (!executed.isSuccess()) {
            return false;
        }

        storeProcessedContainer(inputSlot, outputSlot, executed.getResult());
        return true;
    }

    protected final boolean fillContainerFromTank(int inputSlot, int outputSlot) {
        ItemStack input = items.get(inputSlot);
        if (input.isEmpty() || tank.getFluid().isEmpty()) {
            return false;
        }

        ItemStack singleItem = input.copyWithCount(1);
        FluidActionResult simulated = FluidUtil.tryFillContainer(singleItem, this, Integer.MAX_VALUE, null, false);
        if (!simulated.isSuccess() || !canStoreProcessedContainer(outputSlot, simulated.getResult())) {
            return false;
        }

        FluidActionResult executed = FluidUtil.tryFillContainer(singleItem, this, Integer.MAX_VALUE, null, true);
        if (!executed.isSuccess()) {
            return false;
        }

        storeProcessedContainer(inputSlot, outputSlot, executed.getResult());
        return true;
    }

    protected final boolean canStoreProcessedContainer(int outputSlot, ItemStack result) {
        return result.isEmpty() || canMergeResultIntoSlot(outputSlot, result);
    }

    protected final void storeProcessedContainer(int inputSlot, int outputSlot, ItemStack result) {
        items.get(inputSlot).shrink(1);
        if (items.get(inputSlot).isEmpty()) {
            items.set(inputSlot, ItemStack.EMPTY);
        }

        if (!result.isEmpty()) {
            mergeResultIntoSlot(outputSlot, result);
        }
    }
}
