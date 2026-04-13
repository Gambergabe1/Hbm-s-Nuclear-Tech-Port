package com.hbm.blockentity.rbmk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * RBMK control rod block entity.
 * Absorbs neutrons to control the chain reaction.
 */
public class RBMKControlRodBlockEntity extends AbstractRBMKBlockEntity {
    
    private int insertionLevel = 0;
    private int targetInsertionLevel = 0;
    
    public RBMKControlRodBlockEntity(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
    
    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        
        // Smooth insertion level changes
        if (insertionLevel != targetInsertionLevel) {
            int delta = Integer.compare(targetInsertionLevel, insertionLevel);
            insertionLevel += delta;
            setChanged();
        }
    }
    
    /**
     * Sets the control rod insertion level (0-100%).
     */
    public void setInsertionLevel(int level) {
        this.targetInsertionLevel = Math.clamp(level, 0, 100);
    }
    
    /**
     * Returns the current insertion level (0-100%).
     */
    public int getInsertionLevel() {
        return insertionLevel;
    }
    
    @Override
    public double getMaxHeat() {
        return 1000.0;
    }
    
    @Override
    public double getPassiveCooling() {
        return 5.0;
    }
    
    @Override
    public RBMKColumnType getConsoleType() {
        return RBMKColumnType.CONTROL_ROD;
    }
}
