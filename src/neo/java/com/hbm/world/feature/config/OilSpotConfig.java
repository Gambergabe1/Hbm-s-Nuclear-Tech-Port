package com.hbm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record OilSpotConfig(int width, int count) implements FeatureConfiguration {
    public static final Codec<OilSpotConfig> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.INT.fieldOf("width").forGetter(OilSpotConfig::width),
            Codec.INT.fieldOf("count").forGetter(OilSpotConfig::count)
        ).apply(builder, OilSpotConfig::new)
    );
}
