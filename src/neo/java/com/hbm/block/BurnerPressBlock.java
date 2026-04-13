package com.hbm.block;

import com.hbm.blockentity.BurnerPressBlockEntity;
import com.hbm.registry.HbmBlockEntityTypes;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class BurnerPressBlock extends AbstractMachineBlock {
    public static final MapCodec<BurnerPressBlock> CODEC = simpleCodec(BurnerPressBlock::new);

    public BurnerPressBlock(BlockBehaviour.Properties properties) {
        super(properties, true);
    }

    @Override
    public MapCodec<BurnerPressBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurnerPressBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_PRESS.get(), BurnerPressBlockEntity::serverTick);
    }
}
