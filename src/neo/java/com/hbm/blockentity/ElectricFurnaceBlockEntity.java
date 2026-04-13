package com.hbm.blockentity;

import java.util.Optional;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.block.ElectricFurnaceBlock;
import com.hbm.menu.ElectricFurnaceMenu;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class ElectricFurnaceBlockEntity extends AbstractProcessorMachineBlockEntity {
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_COUNT = 3;
    public static final int MAX_PROGRESS = 100;
    public static final int MAX_POWER = 100_000;
    private static final int POWER_PER_TICK = 50;

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_ELECTRIC_FURNACE.get(), pos, blockState, SLOT_COUNT, MAX_POWER, MAX_POWER, 0);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity blockEntity) {
        blockEntity.tickProcessorMachine();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.electricFurnace");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_BATTERY -> HbmEnergyHelper.canDischargeIntoMachine(stack);
            case SLOT_INPUT -> hasRecipe(level, stack);
            case SLOT_OUTPUT -> false;
            default -> false;
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ElectricFurnaceMenu(containerId, inventory, this, getDataAccess());
    }

    @Override
    protected int getBatterySlot() {
        return SLOT_BATTERY;
    }

    @Override
    protected int getMaxProgress() {
        return MAX_PROGRESS;
    }

    @Override
    protected int getPowerPerTick() {
        return POWER_PER_TICK;
    }

    @Override
    protected boolean isMachineEnabled() {
        return level == null || !level.hasNeighborSignal(worldPosition);
    }

    @Override
    protected ItemStack getCurrentResult() {
        if (level == null) {
            return ItemStack.EMPTY;
        }

        Optional<RecipeHolder<SmeltingRecipe>> recipe = getRecipe(items.get(SLOT_INPUT));
        if (recipe.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return recipe.get().value().assemble(new SingleRecipeInput(items.get(SLOT_INPUT)), level.registryAccess());
    }

    @Override
    protected boolean canProcessResult(ItemStack result) {
        return canMergeResultIntoSlot(SLOT_OUTPUT, result);
    }

    @Override
    protected void completeProcessing(ItemStack result) {
        mergeResultIntoSlot(SLOT_OUTPUT, result);

        items.get(SLOT_INPUT).shrink(1);
        if (items.get(SLOT_INPUT).isEmpty()) {
            items.set(SLOT_INPUT, ItemStack.EMPTY);
        }
    }

    @Override
    protected void onProcessingStateChanged(boolean active) {
        updateLitState(active);
    }

    private void updateLitState(boolean lit) {
        if (level == null) {
            return;
        }

        BlockState state = getBlockState();
        if (state.getValue(ElectricFurnaceBlock.LIT) == lit) {
            return;
        }

        level.setBlock(worldPosition, state.setValue(ElectricFurnaceBlock.LIT, lit), Block.UPDATE_CLIENTS);
    }

    private Optional<RecipeHolder<SmeltingRecipe>> getRecipe(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return Optional.empty();
        }

        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level);
    }

    public static boolean hasRecipe(Level level, ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return false;
        }

        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level).isPresent();
    }
}
