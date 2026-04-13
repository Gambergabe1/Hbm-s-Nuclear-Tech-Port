package com.hbm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractMachineBlock extends BaseEntityBlock {
    private final boolean sneakBypassesMenu;

    protected AbstractMachineBlock(BlockBehaviour.Properties properties) {
        this(properties, false);
    }

    protected AbstractMachineBlock(BlockBehaviour.Properties properties, boolean sneakBypassesMenu) {
        super(properties);
        this.sneakBypassesMenu = sneakBypassesMenu;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        if (sneakBypassesMenu && player.isCrouching()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        MenuProvider menuProvider = getMenuProvider(state, level, pos);
        if (menuProvider != null) {
            player.openMenu(menuProvider);
            onMenuOpened(player);
        }

        return InteractionResult.CONSUME;
    }

    protected void onMenuOpened(Player player) {
        player.awardStat(Stats.INTERACT_WITH_FURNACE);
        PiglinAi.angerNearbyPiglins(player, true);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
