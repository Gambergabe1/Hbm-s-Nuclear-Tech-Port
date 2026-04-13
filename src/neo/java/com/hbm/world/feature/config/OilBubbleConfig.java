package com.hbm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record OilBubbleConfig(int radius) implements FeatureConfiguration {
    public static final Codec<OilBubbleConfig> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.INT.fieldOf("radius").forGetter(OilBubbleConfig::radius)
        ).apply(builder, OilBubbleConfig::new)
    );
}
