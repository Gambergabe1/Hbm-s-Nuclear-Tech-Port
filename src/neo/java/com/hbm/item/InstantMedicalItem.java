package com.hbm.item;

import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class InstantMedicalItem extends LoreItem {
    private final SoundEvent useSound;
    private final BiConsumer<Level, LivingEntity> consumeAction;
    private final List<Component> tooltipLines;

    public InstantMedicalItem(
        Properties properties,
        SoundEvent useSound,
        BiConsumer<Level, LivingEntity> consumeAction,
        List<Component> tooltipLines
    ) {
        super(properties);
        this.useSound = useSound;
        this.consumeAction = consumeAction;
        this.tooltipLines = List.copyOf(tooltipLines);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), useSound, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide) {
            consumeAction.accept(level, player);
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.hasInfiniteMaterials()) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.addAll(tooltipLines);
    }

    public static Component grayTooltip(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GRAY);
    }

    public static Component greenTooltip(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GREEN);
    }
}
