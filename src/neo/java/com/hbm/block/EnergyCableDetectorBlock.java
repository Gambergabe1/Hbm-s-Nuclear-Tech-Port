package com.hbm.block;

import com.hbm.blockentity.EnergyCableBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class EnergyCableDetectorBlock extends EnergyCableSwitchBlock {
    public static final MapCodec<EnergyCableDetectorBlock> CODEC = simpleCodec(EnergyCableDetectorBlock::new);

    public EnergyCableDetectorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends EnergyCableDetectorBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        return InteractionResult.PASS;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        updatePoweredState(level, pos, state);
    }

    @Override
    protected void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block neighborBlock,
        BlockPos neighborPos,
        boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, isMoving);
        updatePoweredState(level, pos, state);
    }

    private static void updatePoweredState(Level level, BlockPos pos, BlockState state) {
        boolean powered = level.hasNeighborSignal(pos);
        if (state.getValue(STATE) != powered) {
            level.setBlock(pos, state.setValue(STATE, powered), 3);
            if (level.getBlockEntity(pos) instanceof EnergyCableBlockEntity cable) {
                cable.onBlockStateChanged();
            }
        }
    }
}
