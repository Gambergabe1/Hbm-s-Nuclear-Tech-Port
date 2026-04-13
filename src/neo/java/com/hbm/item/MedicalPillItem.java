package com.hbm.item;

import java.util.List;
import java.util.function.BiConsumer;

import com.hbm.util.HbmEffectUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MedicalPillItem extends LoreItem {
    private static final int USE_DURATION_TICKS = 10;

    private final SoundEvent useSound;
    private final BiConsumer<Level, LivingEntity> consumeAction;
    private final List<Component> tooltipLines;

    public MedicalPillItem(
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
        if (HbmEffectUtil.hasPotionSickness(player)) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), useSound, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide) {
            consumeAction.accept(level, livingEntity);
            if (livingEntity instanceof Player player) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
            } else {
                stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.addAll(tooltipLines);
    }
}
