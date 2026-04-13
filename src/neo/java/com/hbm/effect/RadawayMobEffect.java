package com.hbm.effect;

import com.hbm.attachment.HbmAttachmentAccess;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public final class RadawayMobEffect extends MobEffect {
    public RadawayMobEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        HbmAttachmentAccess.living(livingEntity).decreaseRads((amplifier + 1) * 0.05F);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
