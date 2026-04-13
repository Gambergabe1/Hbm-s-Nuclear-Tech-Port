package com.hbm.blockentity;

import com.hbm.machine.PressRecipeRegistry;
import com.hbm.menu.BurnerPressMenu;
import com.hbm.network.HbmNetwork;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public final class BurnerPressBlockEntity extends AbstractMachineBlockEntity {
    public static final int SLOT_FUEL = 0;
    public static final int SLOT_STAMP = 1;
    public static final int SLOT_INPUT = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_COUNT = 4;
    public static final int MAX_PROGRESS = 200;
    public static final int MAX_POWER = 700;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> power;
                case 1 -> progress;
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> power = value;
                case 1 -> progress = value;
                case 2 -> burnTime = value;
                case 3 -> maxBurnTime = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private int progress;
    private int power;
    private int burnTime;
    private int maxBurnTime;
    private boolean retracting;
    private final MachineStateTracker<PressMachineTrackedState> trackedState = new MachineStateTracker<>(state -> {
        if (level instanceof ServerLevel serverLevel) {
            HbmNetwork.syncPressMachineState(serverLevel, worldPosition, state.input(), state.stamp(), state.progress());
        }
    });

    public BurnerPressBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_PRESS.get(), pos, blockState, SLOT_COUNT);
    }

    public static void serverTick(
        net.minecraft.world.level.Level level,
        BlockPos pos,
        BlockState state,
        BurnerPressBlockEntity blockEntity
    ) {
        blockEntity.tickServer();
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    public int getProgress() {
        return progress;
    }

    public int getPower() {
        return power;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.press");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_FUEL -> stack.getBurnTime(RecipeType.SMELTING) > 0;
            case SLOT_STAMP -> PressRecipeRegistry.isStamp(stack);
            case SLOT_INPUT -> !PressRecipeRegistry.isStamp(stack) && PressRecipeRegistry.hasAnyRecipeFor(stack);
            case SLOT_OUTPUT -> false;
            default -> false;
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BurnerPressMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    protected void loadMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        progress = tag.getInt("progress");
        power = tag.getInt("power");
        burnTime = tag.getInt("burn_time");
        maxBurnTime = tag.getInt("max_burn_time");
        retracting = tag.getBoolean("retracting");
    }

    @Override
    protected void saveMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("progress", progress);
        tag.putInt("power", power);
        tag.putInt("burn_time", burnTime);
        tag.putInt("max_burn_time", maxBurnTime);
        tag.putBoolean("retracting", retracting);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncTrackedState();
    }

    private void tickServer() {
        boolean changed = false;

        if (burnTime > 0) {
            burnTime--;
            power = Math.min(MAX_POWER, power + (isPreheated() ? 5 : 1));
            changed = true;
        } else if (power > 0) {
            power--;
            changed = true;
        }

        if (!level.hasNeighborSignal(worldPosition)) {
            if (burnTime == 0 && canStartBurning()) {
                ItemStack fuelStack = items.get(SLOT_FUEL);
                int newBurnTime = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (newBurnTime > 0) {
                    maxBurnTime = Math.max(1, newBurnTime / 8);
                    burnTime = maxBurnTime;
                    consumeFuel(fuelStack);
                    changed = true;
                }
            }

            int speed = Math.max(1, power * 25 / MAX_POWER);
            ItemStack result = getCurrentResult();
            boolean canProcess = power >= MAX_POWER / 3 && !result.isEmpty() && canOutput(result);

            if (canProcess) {
                if (progress >= MAX_PROGRESS) {
                    craft(result);
                    startRetracting();
                    changed = true;
                }

                if (!retracting) {
                    progress = Math.min(MAX_PROGRESS, progress + speed);
                    changed = true;
                }
            } else if (progress > 0) {
                startRetracting();
            }

            if (retracting && progress > 0) {
                progress = Math.max(0, progress - speed);
                changed = true;
            }

            if (progress <= 0 && retracting) {
                progress = 0;
                retracting = false;
                changed = true;
            }
        }

        if (changed) {
            setChanged();
        } else {
            syncTrackedState();
        }
    }

    private boolean isPreheated() {
        return level != null && level.getBlockState(worldPosition.below()).is(HbmBlocks.PRESS_PREHEATER.get());
    }

    private boolean canStartBurning() {
        return !items.get(SLOT_FUEL).isEmpty() && items.get(SLOT_FUEL).getBurnTime(RecipeType.SMELTING) > 0;
    }

    private ItemStack getCurrentResult() {
        return PressRecipeRegistry.getResult(items.get(SLOT_INPUT), items.get(SLOT_STAMP));
    }

    private boolean canOutput(ItemStack result) {
        return canMergeResultIntoSlot(SLOT_OUTPUT, result);
    }

    private void craft(ItemStack result) {
        mergeResultIntoSlot(SLOT_OUTPUT, result);

        items.get(SLOT_INPUT).shrink(1);
        if (items.get(SLOT_INPUT).isEmpty()) {
            items.set(SLOT_INPUT, ItemStack.EMPTY);
        }

        ItemStack stamp = items.get(SLOT_STAMP);
        if (stamp.isDamageableItem()) {
            stamp.setDamageValue(stamp.getDamageValue() + 1);
            if (stamp.getDamageValue() >= stamp.getMaxDamage()) {
                items.set(SLOT_STAMP, ItemStack.EMPTY);
            }
        }
    }

    private void startRetracting() {
        if (!retracting) {
            retracting = true;
        }
    }

    private void consumeFuel(ItemStack fuelStack) {
        ItemStack remainder = fuelStack.getCraftingRemainingItem();
        fuelStack.shrink(1);
        if (fuelStack.isEmpty()) {
            items.set(SLOT_FUEL, remainder);
        }
    }

    private void syncTrackedState() {
        trackedState.sync(new PressMachineTrackedState(items.get(SLOT_INPUT), items.get(SLOT_STAMP), progress));
    }
}
