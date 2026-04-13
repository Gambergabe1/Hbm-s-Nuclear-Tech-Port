package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMachineBlockEntity extends BaseContainerBlockEntity {
    protected final int slotCount;
    protected NonNullList<ItemStack> items;

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int slotCount) {
        super(type, pos, blockState);
        this.slotCount = Math.max(0, slotCount);
        this.items = NonNullList.withSize(this.slotCount, ItemStack.EMPTY);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return slotCount;
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
            worldPosition.getX() + 0.5D,
            worldPosition.getY() + 0.5D,
            worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        loadSharedMachineData(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        saveSharedMachineData(tag, registries);
    }

    protected void loadSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        loadMachineData(tag, registries);
    }

    protected void saveSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        saveMachineData(tag, registries);
    }

    protected void loadMachineData(CompoundTag tag, HolderLookup.Provider registries) {
    }

    protected void saveMachineData(CompoundTag tag, HolderLookup.Provider registries) {
    }

    protected final boolean canMergeResultIntoSlot(int slot, ItemStack result) {
        if (slot < 0 || slot >= items.size() || result.isEmpty()) {
            return false;
        }

        ItemStack output = items.get(slot);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    protected final void mergeResultIntoSlot(int slot, ItemStack result) {
        if (!canMergeResultIntoSlot(slot, result)) {
            return;
        }

        ItemStack output = items.get(slot);
        if (output.isEmpty()) {
            items.set(slot, result.copy());
        } else {
            output.grow(result.getCount());
        }
    }

    protected final boolean canMergeResultIntoRange(int startInclusive, int endExclusive, ItemStack result) {
        if (result.isEmpty()) {
            return false;
        }

        for (int slot = Math.max(0, startInclusive); slot < Math.min(items.size(), endExclusive); slot++) {
            if (canMergeResultIntoSlot(slot, result)) {
                return true;
            }
        }

        return false;
    }

    protected final boolean mergeResultIntoRange(int startInclusive, int endExclusive, ItemStack result) {
        if (result.isEmpty()) {
            return false;
        }

        for (int slot = Math.max(0, startInclusive); slot < Math.min(items.size(), endExclusive); slot++) {
            if (!canMergeResultIntoSlot(slot, result)) {
                continue;
            }

            mergeResultIntoSlot(slot, result);
            return true;
        }

        return false;
    }
}
