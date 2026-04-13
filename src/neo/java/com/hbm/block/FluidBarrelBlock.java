package com.hbm.block;

import com.hbm.blockentity.FluidBarrelBlockEntity;
import com.hbm.network.HbmNetwork;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Locale;

public class FluidBarrelBlock extends BaseEntityBlock {
    public static final MapCodec<FluidBarrelBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("capacity").forGetter(FluidBarrelBlock::getCapacity),
                    propertiesCodec()
            ).apply(instance, FluidBarrelBlock::new)
    );

    private final int capacity;
    public static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public FluidBarrelBlock(int capacity, BlockBehaviour.Properties properties) {
        super(properties);
        this.capacity = capacity;
    }

    @Override
    public MapCodec<? extends FluidBarrelBlock> codec() {
        return CODEC;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider menuProvider) {
            if (blockEntity instanceof FluidBarrelBlockEntity barrel && player instanceof ServerPlayer serverPlayer) {
                HbmNetwork.sendBarrelStateTo(serverPlayer, pos, barrel.getFluidTank().getFluid());
            }
            player.openMenu(menuProvider, pos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(newState.getBlock() instanceof FluidBarrelBlock) && blockEntity instanceof FluidBarrelBlockEntity barrel) {
                Containers.dropContents(level, pos, barrel);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidBarrelBlockEntity(pos, state, capacity);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.BARREL.get(), FluidBarrelBlockEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        Block block = Block.byItem(stack.getItem());
        if (!(block instanceof FluidBarrelBlock fluidBarrelBlock)) {
            return;
        }

        tooltip.add(
            Component.translatable("desc.capacity", formatCapacity(fluidBarrelBlock.getCapacity()))
                .withStyle(ChatFormatting.AQUA)
        );

        if (block == HbmBlocks.BARREL_PLASTIC.get()) {
            addTooltip(tooltip, "desc.cannothot", ChatFormatting.YELLOW);
            addTooltip(tooltip, "desc.cannotcor", ChatFormatting.YELLOW);
            addTooltip(tooltip, "desc.cannotam", ChatFormatting.YELLOW);
            return;
        }

        if (block == HbmBlocks.BARREL_CORRODED.get()) {
            addTooltip(tooltip, "desc.canhot", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.canhighcor", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.cannotam", ChatFormatting.YELLOW);
            addTooltip(tooltip, "desc.leaky", ChatFormatting.RED);
            return;
        }

        if (block == HbmBlocks.BARREL_IRON.get()) {
            addTooltip(tooltip, "desc.canhot", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.cannotcor1", ChatFormatting.YELLOW);
            addTooltip(tooltip, "desc.cannotam", ChatFormatting.YELLOW);
            return;
        }

        if (block == HbmBlocks.BARREL_STEEL.get()) {
            addTooltip(tooltip, "desc.canhot", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.cancor", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.cannothighcor", ChatFormatting.YELLOW);
            addTooltip(tooltip, "desc.cannotam", ChatFormatting.YELLOW);
            return;
        }

        if (block == HbmBlocks.BARREL_ANTIMATTER.get()) {
            addTooltip(tooltip, "desc.canhot", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.canhighcor", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.canam", ChatFormatting.GREEN);
            return;
        }

        if (block == HbmBlocks.BARREL_TCALLOY.get()) {
            addTooltip(tooltip, "desc.canhot", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.canhighcor", ChatFormatting.GREEN);
            addTooltip(tooltip, "desc.cannotam", ChatFormatting.YELLOW);
        }
    }

    private static void addTooltip(List<Component> tooltip, String key, ChatFormatting formatting) {
        tooltip.add(Component.translatable(key).withStyle(formatting));
    }

    private static String formatCapacity(int capacity) {
        return String.format(Locale.ROOT, "%,d", capacity);
    }
}
