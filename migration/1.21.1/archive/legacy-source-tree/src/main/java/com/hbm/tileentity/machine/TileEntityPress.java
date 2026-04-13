package com.hbm.tileentity.machine;

import com.hbm.recipe.PressRecipe;
import com.hbm.recipe.RecipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityPress extends TileEntityMachineBase {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    
    public TileEntityPress(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 2); // 2 slots: input and output
    }
    
    @Override
    protected void tryStartProcess() {
        if (this.level == null) return;
        
        // Create a simple container with just the input item
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, this.inventory[INPUT_SLOT]);
        
        // Try to find a matching recipe
        RecipeHelper.getPressRecipe(container, this.level).ifPresent(recipe -> {
            ItemStack result = recipe.assemble(container, this.level.registryAccess());
            
            // Check if we can insert the result
            if (this.canInsertResult(OUTPUT_SLOT, result)) {
                this.isProcessing = true;
                this.maxProgress = recipe.getDuration();
                this.progress = 0;
            }
        });
    }
    
    @Override
    protected void finishProcess() {
        if (this.level == null) return;
        
        // Create a simple container with just the input item
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, this.inventory[INPUT_SLOT]);
        
        // Find the recipe again to get the result
        RecipeHelper.getPressRecipe(container, this.level).ifPresent(recipe -> {
            ItemStack result = recipe.assemble(container, this.level.registryAccess());
            
            // Add to output slot
            if (this.inventory[OUTPUT_SLOT].isEmpty()) {
                this.inventory[OUTPUT_SLOT] = result.copy();
            } else {
                this.inventory[OUTPUT_SLOT].grow(result.getCount());
            }
            
            // Consume input
            this.inventory[INPUT_SLOT].shrink(1);
        });
        
        this.isProcessing = false;
        this.progress = 0;
        this.maxProgress = 0;
        
        // Mark dirty to sync with client
        this.setChanged();
    }
    
    private boolean canInsertResult(int slot, ItemStack result) {
        if (this.inventory[slot].isEmpty()) {
            return true;
        }
        
        return ItemStack.isSameItemSameTags(this.inventory[slot], result) && 
               this.inventory[slot].getCount() + result.getCount() <= this.inventory[slot].getMaxStackSize();
    }
}