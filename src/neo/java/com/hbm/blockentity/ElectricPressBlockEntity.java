package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.machine.PressRecipeRegistry;
import com.hbm.menu.ElectricPressMenu;
import com.hbm.network.HbmNetwork;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ElectricPressBlockEntity extends AbstractProcessorMachineBlockEntity {
    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_STAMP = 1;
    public static final int SLOT_INPUT = 2;
    public static final int SLOT_OUTPUT = 3;
    public static final int SLOT_COUNT = 4;
    public static final int MAX_PROGRESS = 200;
    public static final int MAX_POWER = 50_000;
    private static final int POWER_PER_TICK = 100;
    private static final int PROGRESS_PER_TICK = 25;

    private final MachineStateTracker<PressMachineTrackedState> trackedState = new MachineStateTracker<>(state -> {
        if (level instanceof ServerLevel serverLevel) {
            HbmNetwork.syncPressMachineState(serverLevel, worldPosition, state.input(), state.stamp(), state.progress());
        }
    });

    public ElectricPressBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_EPRESS.get(), pos, blockState, SLOT_COUNT, MAX_POWER, MAX_POWER, 0);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ElectricPressBlockEntity blockEntity) {
        blockEntity.tickProcessorMachine();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.epress");
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_BATTERY -> HbmEnergyHelper.canDischargeIntoMachine(stack);
            case SLOT_STAMP -> PressRecipeRegistry.isStamp(stack);
            case SLOT_INPUT -> !PressRecipeRegistry.isStamp(stack) && PressRecipeRegistry.hasAnyRecipeFor(level, stack);
            case SLOT_OUTPUT -> false;
            default -> false;
        };
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ElectricPressMenu(containerId, inventory, this, getDataAccess());
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
    protected int getProgressPerTick() {
        return PROGRESS_PER_TICK;
    }

    @Override
    protected int getBlockedProgress(int currentProgress) {
        return Math.max(0, currentProgress - PROGRESS_PER_TICK);
    }

    @Override
    protected ItemStack getCurrentResult() {
        return PressRecipeRegistry.getResult(level, items.get(SLOT_INPUT), items.get(SLOT_STAMP));
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

        ItemStack stamp = items.get(SLOT_STAMP);
        if (stamp.isDamageableItem()) {
            stamp.setDamageValue(stamp.getDamageValue() + 1);
            if (stamp.getDamageValue() >= stamp.getMaxDamage()) {
                items.set(SLOT_STAMP, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncTrackedState();
    }

    @Override
    protected void onIdleServerTick() {
        syncTrackedState();
    }

    private void syncTrackedState() {
        trackedState.sync(new PressMachineTrackedState(items.get(SLOT_INPUT), items.get(SLOT_STAMP), progress));
    }
}
