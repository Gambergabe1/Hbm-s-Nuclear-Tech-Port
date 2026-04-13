package com.hbm.blockentity.rbmk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * RBMK moderator block entity.
 * Slows down neutrons to increase fission efficiency.
 */
public class RBMKModeratorBlockEntity extends AbstractRBMKBlockEntity {
    
    public RBMKModeratorBlockEntity(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
    
    @Override
    public double getMaxHeat() {
        return 1200.0;
    }
    
    @Override
    public double getPassiveCooling() {
        return 4.0;
    }
    
    @Override
    public RBMKColumnType getConsoleType() {
        return RBMKColumnType.MODERATOR;
    }
}
