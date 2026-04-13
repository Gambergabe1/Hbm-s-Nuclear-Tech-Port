package com.hbm.block;

import com.hbm.blockentity.ShredderBlockEntity;
import com.hbm.registry.HbmBlockEntityTypes;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class ShredderBlock extends AbstractMachineBlock {
    public static final MapCodec<ShredderBlock> CODEC = simpleCodec(ShredderBlock::new);

    public ShredderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<ShredderBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShredderBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_SHREDDER.get(), ShredderBlockEntity::serverTick);
    }
}
