package com.hbm.tileentity.machine;

import com.hbm.recipe.RecipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntityMachineBase extends BlockEntity implements Container {
    protected ItemStack[] inventory;
    protected int progress = 0;
    protected int maxProgress = 0;
    protected boolean isProcessing = false;
    
    public TileEntityMachineBase(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        this.inventory = new ItemStack[inventorySize];
        for (int i = 0; i < inventorySize; i++) {
            this.inventory[i] = ItemStack.EMPTY;
        }
    }
    
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.progress = nbt.getInt("Progress");
        this.maxProgress = nbt.getInt("MaxProgress");
        this.isProcessing = nbt.getBoolean("IsProcessing");
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Progress", this.progress);
        nbt.putInt("MaxProgress", this.maxProgress);
        nbt.putBoolean("IsProcessing", this.isProcessing);
    }
    
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            if (this.isProcessing) {
                this.progress++;
                if (this.progress >= this.maxProgress) {
                    this.finishProcess();
                }
            } else {
                this.tryStartProcess();
            }
        }
    }
    
    protected abstract void tryStartProcess();
    protected abstract void finishProcess();
    
    // Container implementation
    @Override
    public int getContainerSize() {
        return this.inventory.length;
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(int index) {
        return this.inventory[index];
    }
    
    @Override
    public ItemStack removeItem(int index, int count) {
        return net.minecraft.core.NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY).remove(index);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public void setItem(int index, ItemStack stack) {
        this.inventory[index] = stack;
    }
    
    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }
    
    @Override
    public void clearContent() {
        for (int i = 0; i < this.inventory.length; i++) {
            this.inventory[i] = ItemStack.EMPTY;
        }
    }
}