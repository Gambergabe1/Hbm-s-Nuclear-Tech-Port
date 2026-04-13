package com.hbm.block;

import com.hbm.blockentity.DeshCrateBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class DeshCrateBlock extends AbstractStorageBlock {
    public static final MapCodec<DeshCrateBlock> CODEC = simpleCodec(DeshCrateBlock::new);

    public DeshCrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<DeshCrateBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DeshCrateBlockEntity(pos, state);
    }
}
