package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class RadiationConfig {
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.DoubleValue PASSIVE_DRAIN_PER_SECOND;
    private static final ModConfigSpec.DoubleValue HUNGER_THRESHOLD;
    private static final ModConfigSpec.DoubleValue WEAKNESS_THRESHOLD;
    private static final ModConfigSpec.DoubleValue SLOWNESS_THRESHOLD;
    private static final ModConfigSpec.DoubleValue POISON_THRESHOLD;
    private static final ModConfigSpec.DoubleValue DAMAGE_THRESHOLD;
    private static final ModConfigSpec.DoubleValue DAMAGE_PER_SECOND;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Radiation exposure ('rads', 0-2500) thresholds at which HazardHandler applies effects to living entities.")
            .push("radiation");

        PASSIVE_DRAIN_PER_SECOND = builder.defineInRange("passive_drain_per_second", 0.01D, 0.0D, 100.0D);
        HUNGER_THRESHOLD = builder.defineInRange("hunger_threshold", 200.0D, 0.0D, 2500.0D);
        WEAKNESS_THRESHOLD = builder.defineInRange("weakness_threshold", 400.0D, 0.0D, 2500.0D);
        SLOWNESS_THRESHOLD = builder.defineInRange("slowness_threshold", 600.0D, 0.0D, 2500.0D);
        POISON_THRESHOLD = builder.defineInRange("poison_threshold", 800.0D, 0.0D, 2500.0D);
        DAMAGE_THRESHOLD = builder.defineInRange("damage_threshold", 1000.0D, 0.0D, 2500.0D);
        DAMAGE_PER_SECOND = builder.defineInRange("damage_per_second", 1.0D, 0.0D, 100.0D);

        builder.pop();
        SPEC = builder.build();
    }

    private RadiationConfig() {
    }

    public static float passiveDrainPerSecond() {
        return PASSIVE_DRAIN_PER_SECOND.get().floatValue();
    }

    public static float hungerThreshold() {
        return HUNGER_THRESHOLD.get().floatValue();
    }

    public static float weaknessThreshold() {
        return WEAKNESS_THRESHOLD.get().floatValue();
    }

    public static float slownessThreshold() {
        return SLOWNESS_THRESHOLD.get().floatValue();
    }

    public static float poisonThreshold() {
        return POISON_THRESHOLD.get().floatValue();
    }

    public static float damageThreshold() {
        return DAMAGE_THRESHOLD.get().floatValue();
    }

    public static float damagePerSecond() {
        return DAMAGE_PER_SECOND.get().floatValue();
    }
}
