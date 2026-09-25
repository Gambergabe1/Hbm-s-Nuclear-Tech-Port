package com.hbm.blockentity.rbmk;

import com.hbm.registry.HbmRBMKBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * RBMK boiler block entity.
 * Converts water to steam using reactor heat.
 */
public class RBMKBoilerBlockEntity extends AbstractRBMKBlockEntity {

    public RBMKBoilerBlockEntity(BlockPos pos, BlockState state) {
        super(HbmRBMKBlocks.RBMK_BOILER_BE.get(), pos, state);
    }
    
    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        
        // Boilers actively boil water when hot
        boilWater();
    }
    
    @Override
    public double getMaxHeat() {
        return 1000.0;
    }
    
    @Override
    public double getPassiveCooling() {
        return 1.0;
    }
    
    @Override
    public RBMKColumnType getConsoleType() {
        return RBMKColumnType.BOILER;
    }
}
