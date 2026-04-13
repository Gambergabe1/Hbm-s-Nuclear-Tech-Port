package com.hbm.block;

import com.hbm.blockentity.TransformerChargerBlockEntity;
import com.hbm.registry.HbmBlockEntityTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Locale;

public final class TransformerChargerBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<TransformerChargerBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.INT.fieldOf("max_charge_rate").forGetter(TransformerChargerBlock::getMaxChargeRate),
            Codec.INT.fieldOf("energy_capacity").forGetter(TransformerChargerBlock::getEnergyCapacity),
            Codec.BOOL.fieldOf("pointing_up").forGetter(TransformerChargerBlock::isPointingUp),
            propertiesCodec()
        ).apply(instance, TransformerChargerBlock::new)
    );

    private final int maxChargeRate;
    private final int energyCapacity;
    private final boolean pointingUp;

    public TransformerChargerBlock(int maxChargeRate, int energyCapacity, boolean pointingUp, BlockBehaviour.Properties properties) {
        super(properties);
        this.maxChargeRate = Math.max(0, maxChargeRate);
        this.energyCapacity = Math.max(0, energyCapacity);
        this.pointingUp = pointingUp;
    }

    @Override
    public MapCodec<TransformerChargerBlock> codec() {
        return CODEC;
    }

    public int getMaxChargeRate() {
        return maxChargeRate;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    public boolean isPointingUp() {
        return pointingUp;
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
        if (player.isCrouching()) {
            return InteractionResult.PASS;
        }
        if (level.getBlockEntity(pos) instanceof TransformerChargerBlockEntity charger) {
            if (!level.isClientSide()) {
                player.displayClientMessage(charger.createStatusMessage(), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        if (player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(level.getBlockEntity(pos) instanceof TransformerChargerBlockEntity charger)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int charged = charger.chargeFromHeldItem(stack);
        if (charged <= 0) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            player.displayClientMessage(
                Component.literal("Charged " + formatNumber(charged) + " HE")
                    .withStyle(ChatFormatting.GREEN),
                true
            );
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TransformerChargerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_TRANSFORMER.get(), TransformerChargerBlockEntity::serverTick);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String path = BuiltInRegistries.BLOCK.getKey(this).getPath();
        String descKey = "tile." + path + ".desc";
        String resolved = Component.translatable(descKey).getString();
        if (!resolved.equals(descKey)) {
            tooltip.add(Component.translatable(descKey).withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(
            Component.literal("Max Charge Rate: " + formatRate(maxChargeRate))
                .withStyle(ChatFormatting.GREEN)
        );
        tooltip.add(
            Component.literal("Internal Buffer: " + formatNumber(energyCapacity) + " HE")
                .withStyle(ChatFormatting.GOLD)
        );
    }

    private static String formatRate(int perTick) {
        if (perTick >= Integer.MAX_VALUE / 4) {
            return "Infinite HE/s";
        }
        return formatNumber(perTick * 20L) + " HE/s";
    }

    private static String formatNumber(long value) {
        return String.format(Locale.ROOT, "%,d", value);
    }
}
