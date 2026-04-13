package com.hbm.block;

import com.hbm.blockentity.IronCrateBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class IronCrateBlock extends AbstractStorageBlock {
    public static final MapCodec<IronCrateBlock> CODEC = simpleCodec(IronCrateBlock::new);

    public IronCrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<IronCrateBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IronCrateBlockEntity(pos, state);
    }
}
