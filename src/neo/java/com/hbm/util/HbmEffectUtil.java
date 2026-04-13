package com.hbm.util;

import com.hbm.api.entity.IRadiationImmune;
import com.hbm.armor.HbmArmorUtil;
import com.hbm.armor.HbmHazardClass;
import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.registry.HbmMobEffects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class HbmEffectUtil {
    private static final int MAX_LUNG_DAMAGE = 60 * 60 * 20;

    private HbmEffectUtil() {
    }

    public static void applyPotionSickness(LivingEntity livingEntity, int durationSeconds) {
        if (durationSeconds <= 0) {
            return;
        }

        livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.POTION_SICKNESS, durationSeconds * 20), livingEntity);
    }

    public static boolean hasPotionSickness(LivingEntity livingEntity) {
        return livingEntity.hasEffect(HbmMobEffects.POTION_SICKNESS);
    }

    public static void applyRadiation(LivingEntity livingEntity, float baseRads) {
        if (baseRads <= 0.0F || livingEntity instanceof IRadiationImmune) {
            return;
        }

        HbmAttachmentAccess.living(livingEntity).increaseRads(baseRads * getRadiationMultiplier(livingEntity));
    }

    public static void applyGasExposure(LivingEntity livingEntity, HbmHazardClass hazard) {
        if (livingEntity.level().isClientSide()) {
            return;
        }

        boolean protectedFromGas = false;
        ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmet.isEmpty()) {
            ItemStack filter = HbmArmorUtil.getGasMaskFilterRecursively(helmet);
            if (!filter.isEmpty() && filter.getItem() instanceof com.hbm.item.GasMaskFilterItem) {
                List<HbmHazardClass> blacklist = java.util.Collections.emptyList();
                if (helmet.getItem() instanceof com.hbm.armor.FilteredMaskItem mask) {
                    blacklist = mask.getHazardBlacklist(helmet);
                } else {
                    ItemStack mod = HbmArmorUtil.getInstalledArmorMod(helmet, com.hbm.armor.ArmorModSlot.HELMET_ONLY);
                    if (mod.getItem() instanceof com.hbm.armor.FilteredMaskItem mask) {
                        blacklist = mask.getHazardBlacklist(mod);
                    }
                }

                if (!blacklist.contains(hazard)) {
                    protectedFromGas = true;
                    if (livingEntity.getRandom().nextInt(20) == 0) {
                        HbmArmorUtil.damageGasMaskFilter(livingEntity, 1);
                    }
                }
            }
        }

        if (!protectedFromGas) {
            applyHazardEffects(livingEntity, hazard);
        }
    }

    private static void applyHazardEffects(LivingEntity livingEntity, HbmHazardClass hazard) {
        switch (hazard) {
            case GAS_CHLORINE -> {
                livingEntity.hurt(livingEntity.damageSources().generic(), 2.0F);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            }
            case RAD_GAS -> applyRadiation(livingEntity, 0.5F);
            case NERVE_AGENT -> {
                livingEntity.hurt(livingEntity.damageSources().generic(), 4.0F);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            }
            // Add more as needed
        }
    }

    public static float getRadiationResistance(LivingEntity livingEntity) {
        float resistance = HbmArmorUtil.getRadiationResistance(livingEntity);
        MobEffectInstance radx = livingEntity.getEffect(HbmMobEffects.RADX);
        if (radx != null) {
            resistance += 0.1F * (radx.getAmplifier() + 1);
        }

        return resistance;
    }

    public static float getRadiationMultiplier(LivingEntity livingEntity) {
        return (float) Math.pow(10.0D, -getRadiationResistance(livingEntity));
    }

    public static void clearLegacyNegativeEffects(LivingEntity livingEntity) {
        livingEntity.removeEffect(MobEffects.BLINDNESS);
        livingEntity.removeEffect(MobEffects.CONFUSION);
        livingEntity.removeEffect(MobEffects.DIG_SLOWDOWN);
        livingEntity.removeEffect(MobEffects.HUNGER);
        livingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        livingEntity.removeEffect(MobEffects.POISON);
        livingEntity.removeEffect(MobEffects.WEAKNESS);
        livingEntity.removeEffect(MobEffects.WITHER);
    }

    public static void reduceLungDamage(LivingEntity livingEntity) {
        var livingData = HbmAttachmentAccess.living(livingEntity);
        livingData.setAsbestos(0);
        livingData.setBlacklung(Math.min(livingData.getBlacklung(), MAX_LUNG_DAMAGE / 5));
    }
}
