package com.hbm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class HbmCommonConfig {
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.IntValue NUCLEAR_CREEPER_FUSE_TICKS;
    private static final ModConfigSpec.IntValue NUCLEAR_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.IntValue NUCLEAR_CREEPER_HEAL_INTERVAL_TICKS;
    private static final ModConfigSpec.IntValue NUCLEAR_CREEPER_RADIATION_INTERVAL_TICKS;
    private static final ModConfigSpec.DoubleValue NUCLEAR_CREEPER_RADIATION_RADIUS;
    private static final ModConfigSpec.DoubleValue NUCLEAR_CREEPER_RADIATION_PER_PULSE;

    private static final ModConfigSpec.IntValue TAINTED_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.IntValue TAINTED_CREEPER_HEAL_INTERVAL_TICKS;
    private static final ModConfigSpec.IntValue TAINTED_CREEPER_RADIATION_INTERVAL_TICKS;
    private static final ModConfigSpec.DoubleValue TAINTED_CREEPER_RADIATION_RADIUS;
    private static final ModConfigSpec.DoubleValue TAINTED_CREEPER_RADIATION_PER_PULSE;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Common gameplay defaults already moved off the legacy 1.12 config path.").push("mobs");

        builder.comment("Config for the Neo nuclear creeper rewrite slice.").push("nuclear_creeper");
        NUCLEAR_CREEPER_FUSE_TICKS = builder.defineInRange("fuse_ticks", 75, 1, Short.MAX_VALUE);
        NUCLEAR_CREEPER_EXPLOSION_RADIUS = builder.defineInRange("explosion_radius", 10, 1, Byte.MAX_VALUE);
        NUCLEAR_CREEPER_HEAL_INTERVAL_TICKS = builder.defineInRange("heal_interval_ticks", 10, 1, 20 * 60);
        NUCLEAR_CREEPER_RADIATION_INTERVAL_TICKS = builder.defineInRange("radiation_interval_ticks", 20, 1, 20 * 60);
        NUCLEAR_CREEPER_RADIATION_RADIUS = builder.defineInRange("radiation_radius", 8.0D, 0.0D, 128.0D);
        NUCLEAR_CREEPER_RADIATION_PER_PULSE = builder.defineInRange("radiation_per_pulse", 6.0D, 0.0D, 1_000.0D);
        builder.pop();

        builder.comment("Config for the Neo tainted creeper rewrite slice.").push("tainted_creeper");
        TAINTED_CREEPER_EXPLOSION_RADIUS = builder.defineInRange("explosion_radius", 5, 1, Byte.MAX_VALUE);
        TAINTED_CREEPER_HEAL_INTERVAL_TICKS = builder.defineInRange("heal_interval_ticks", 10, 1, 20 * 60);
        TAINTED_CREEPER_RADIATION_INTERVAL_TICKS = builder.defineInRange("radiation_interval_ticks", 20, 1, 20 * 60);
        TAINTED_CREEPER_RADIATION_RADIUS = builder.defineInRange("radiation_radius", 4.0D, 0.0D, 128.0D);
        TAINTED_CREEPER_RADIATION_PER_PULSE = builder.defineInRange("radiation_per_pulse", 1.0D, 0.0D, 1_000.0D);
        builder.pop();

        builder.pop();
        SPEC = builder.build();
    }

    private HbmCommonConfig() {
    }

    public static int nuclearCreeperFuseTicks() {
        return NUCLEAR_CREEPER_FUSE_TICKS.get();
    }

    public static int nuclearCreeperExplosionRadius() {
        return NUCLEAR_CREEPER_EXPLOSION_RADIUS.get();
    }

    public static int nuclearCreeperHealIntervalTicks() {
        return NUCLEAR_CREEPER_HEAL_INTERVAL_TICKS.get();
    }

    public static int nuclearCreeperRadiationIntervalTicks() {
        return NUCLEAR_CREEPER_RADIATION_INTERVAL_TICKS.get();
    }

    public static double nuclearCreeperRadiationRadius() {
        return NUCLEAR_CREEPER_RADIATION_RADIUS.get();
    }

    public static float nuclearCreeperRadiationPerPulse() {
        return NUCLEAR_CREEPER_RADIATION_PER_PULSE.get().floatValue();
    }

    public static int taintedCreeperExplosionRadius() {
        return TAINTED_CREEPER_EXPLOSION_RADIUS.get();
    }

    public static int taintedCreeperHealIntervalTicks() {
        return TAINTED_CREEPER_HEAL_INTERVAL_TICKS.get();
    }

    public static int taintedCreeperRadiationIntervalTicks() {
        return TAINTED_CREEPER_RADIATION_INTERVAL_TICKS.get();
    }

    public static double taintedCreeperRadiationRadius() {
        return TAINTED_CREEPER_RADIATION_RADIUS.get();
    }

    public static float taintedCreeperRadiationPerPulse() {
        return TAINTED_CREEPER_RADIATION_PER_PULSE.get().floatValue();
    }
}
