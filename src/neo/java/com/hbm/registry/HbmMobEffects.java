package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.effect.PotionSicknessMobEffect;
import com.hbm.effect.RadawayMobEffect;
import com.hbm.effect.SimpleMobEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(Registries.MOB_EFFECT, HbmNuclearTech.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> RADX = MOB_EFFECTS.register(
        "radx",
        () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0x225900)
    );
    public static final DeferredHolder<MobEffect, RadawayMobEffect> RADAWAY = MOB_EFFECTS.register(
        "radaway",
        () -> new RadawayMobEffect(0xFFE400)
    );
    public static final DeferredHolder<MobEffect, MobEffect> STABILITY = MOB_EFFECTS.register(
        "stability",
        () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xD0D0D0)
    );
    public static final DeferredHolder<MobEffect, PotionSicknessMobEffect> POTION_SICKNESS = MOB_EFFECTS.register(
        "potionsickness",
        () -> new PotionSicknessMobEffect(0xFF8080)
    );

    private HbmMobEffects() {
    }
}
