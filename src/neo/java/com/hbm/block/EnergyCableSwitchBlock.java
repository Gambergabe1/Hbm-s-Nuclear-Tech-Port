package com.hbm.block;

import com.hbm.blockentity.EnergyCableBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class EnergyCableSwitchBlock extends EnergyCableBlock {
    public static final MapCodec<EnergyCableSwitchBlock> CODEC = simpleCodec(EnergyCableSwitchBlock::new);
    public static final BooleanProperty STATE = BooleanProperty.create("state");

    public EnergyCableSwitchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(STATE, false));
    }

    @Override
    public MapCodec<? extends EnergyCableSwitchBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        boolean enabled = !state.getValue(STATE);
        level.setBlock(pos, state.setValue(STATE, enabled), 3);
        player.displayClientMessage(Component.literal(enabled ? "Power switch enabled" : "Power switch disabled"), true);
        if (level.getBlockEntity(pos) instanceof EnergyCableBlockEntity cable) {
            cable.onBlockStateChanged();
        }
        return InteractionResult.CONSUME;
    }
}
