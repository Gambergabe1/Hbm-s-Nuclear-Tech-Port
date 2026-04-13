package com.hbm.item;

import com.hbm.entity.projectile.GenericGrenadeEntity;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class GrenadeItem extends Item {
    private final int fuseTicks;

    public GrenadeItem(Properties properties, int fuseTicks) {
        super(properties);
        this.fuseTicks = Math.max(1, fuseTicks);
    }

    public int getFuseTicks() {
        return fuseTicks;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.literal("Fuse: " + formatFuseSeconds()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.SNOWBALL_THROW,
            SoundSource.PLAYERS,
            0.5F,
            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!level.isClientSide) {
            GenericGrenadeEntity grenade = new GenericGrenadeEntity(level, player);
            grenade.setItem(stack);
            grenade.setFuseTicks(fuseTicks);
            grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(grenade);
        }

        player.getCooldowns().addCooldown(this, 10);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private String formatFuseSeconds() {
        if (fuseTicks % 20 == 0) {
            return (fuseTicks / 20) + "s";
        }
        return String.format(java.util.Locale.ROOT, "%.1fs", fuseTicks / 20.0D);
    }
}
