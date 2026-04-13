package com.hbm.block;

import com.hbm.blockentity.SteelCrateBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class SteelCrateBlock extends AbstractStorageBlock {
    public static final MapCodec<SteelCrateBlock> CODEC = simpleCodec(SteelCrateBlock::new);

    public SteelCrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<SteelCrateBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SteelCrateBlockEntity(pos, state);
    }
}
