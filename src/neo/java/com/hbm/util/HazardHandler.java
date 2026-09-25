package com.hbm.util;

import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.attachment.HbmLivingData;
import com.hbm.config.RadiationConfig;
import com.hbm.registry.HbmDataComponents;
import com.hbm.registry.HbmMobEffects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.ArrayList;
import java.util.List;

public final class HazardHandler {
    private HazardHandler() {
    }

    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living) || living.level().isClientSide()) {
            return;
        }

        HbmLivingData data = HbmAttachmentAccess.living(living);

        // Inventory Radiation
        if (living.tickCount % 20 == 0) {
            float totalInventoryRad = 0;
            for (ItemStack stack : living.getAllSlots()) {
                if (!stack.isEmpty()) {
                    Float itemRad = stack.get(HbmDataComponents.ITEM_RADIATION);
                    if (itemRad != null) {
                        totalInventoryRad += itemRad * stack.getCount();
                    }
                }
            }

            if (living instanceof Player player) {
                for (ItemStack stack : player.getInventory().items) {
                    if (!stack.isEmpty()) {
                        Float itemRad = stack.get(HbmDataComponents.ITEM_RADIATION);
                        if (itemRad != null) {
                            totalInventoryRad += itemRad * stack.getCount();
                        }
                    }
                }
            }

            if (totalInventoryRad > 0) {
                HbmEffectUtil.applyRadiation(living, totalInventoryRad);
            }
        }

        // Process contamination effects
        List<HbmLivingData.ContaminationEffect> effects = data.getContaminationEffects();
        if (!effects.isEmpty()) {
            List<HbmLivingData.ContaminationEffect> newEffects = new ArrayList<>();
            for (HbmLivingData.ContaminationEffect effect : effects) {
                if (effect.time() > 0) {
                    float rad = effect.currentRadiation();
                    if (effect.ignoreArmor()) {
                        data.increaseRads(rad);
                    } else {
                        HbmEffectUtil.applyRadiation(living, rad);
                    }

                    newEffects.add(new HbmLivingData.ContaminationEffect(
                        effect.maxRadiation(),
                        effect.maxTime(),
                        effect.time() - 1,
                        effect.ignoreArmor()
                    ));
                }
            }
            data.setContaminationEffects(newEffects);
        }

        // Radiation passive drain (very slow)
        if (living.tickCount % 20 == 0) {
            data.decreaseRads(RadiationConfig.passiveDrainPerSecond());
        }

        // Apply radiation effects
        float rads = data.getRads();
        if (rads > RadiationConfig.hungerThreshold()) {
            living.addEffect(new MobEffectInstance(MobEffects.HUNGER, 40, 0, false, false));
        }
        if (rads > RadiationConfig.weaknessThreshold()) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false));
        }
        if (rads > RadiationConfig.slownessThreshold()) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
            living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 0, false, false));
        }
        if (rads > RadiationConfig.poisonThreshold()) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0, false, false));
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false));
        }
        if (rads > RadiationConfig.damageThreshold()) {
            if (living.tickCount % 20 == 0) {
                living.hurt(living.damageSources().generic(), RadiationConfig.damagePerSecond());
            }
        }
    }
}
