package com.hbm.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record DepthDepositConfig(String oreBlock, String fillerBlock, String genTargetBlock, int size, double fill) implements FeatureConfiguration {
    public static final Codec<DepthDepositConfig> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            Codec.STRING.fieldOf("ore_block").forGetter(DepthDepositConfig::oreBlock),
            Codec.STRING.fieldOf("filler_block").forGetter(DepthDepositConfig::fillerBlock),
            Codec.STRING.fieldOf("gen_target_block").forGetter(DepthDepositConfig::genTargetBlock),
            Codec.INT.fieldOf("size").forGetter(DepthDepositConfig::size),
            Codec.DOUBLE.fieldOf("fill").forGetter(DepthDepositConfig::fill)
        ).apply(builder, DepthDepositConfig::new)
    );
}
