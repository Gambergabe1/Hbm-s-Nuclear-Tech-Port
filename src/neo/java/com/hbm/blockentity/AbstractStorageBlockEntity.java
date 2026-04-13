package com.hbm.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractStorageBlockEntity extends BaseContainerBlockEntity {
    private final int slotCount;
    private final String translationKey;
    private NonNullList<ItemStack> items;

    protected AbstractStorageBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        String translationKey
    ) {
        super(type, pos, blockState);
        this.slotCount = slotCount;
        this.translationKey = translationKey;
        this.items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return slotCount;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(translationKey);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
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
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }
}
