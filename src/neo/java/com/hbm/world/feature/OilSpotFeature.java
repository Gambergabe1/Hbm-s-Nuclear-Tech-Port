package com.hbm.world.feature;

import com.hbm.registry.HbmBlocks;
import com.hbm.world.feature.config.OilSpotConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.Heightmap;

public class OilSpotFeature extends Feature<OilSpotConfig> {
    public OilSpotFeature() {
        super(OilSpotConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OilSpotConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos centerPos = context.origin();
        RandomSource random = context.random();
        OilSpotConfig config = context.config();

        boolean placed = false;
        int x = centerPos.getX();
        int z = centerPos.getZ();

        for (int i = 0; i < config.count(); i++) {
            int rX = x + (int) Math.round(random.nextGaussian() * config.width());
            int rZ = z + (int) Math.round(random.nextGaussian() * config.width());
            int rY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, rX, rZ) - 1;
            if (rY < world.getMinBuildHeight()) {
                continue;
            }

            BlockPos pos = new BlockPos(rX, rY, rZ);
            BlockState stateAtPos = world.getBlockState(pos);
            BlockState replacement = null;

            if (stateAtPos.is(Blocks.GRASS_BLOCK) || stateAtPos.is(Blocks.DIRT) || stateAtPos.is(Blocks.COARSE_DIRT)) {
                replacement = Blocks.COARSE_DIRT.defaultBlockState();
            } else if (stateAtPos.is(Blocks.SAND) || stateAtPos.is(Blocks.RED_SAND)) {
                replacement = Blocks.GRAVEL.defaultBlockState();
            } else if (stateAtPos.is(Blocks.STONE)) {
                replacement = HbmBlocks.STONE_CRACKED.get().defaultBlockState();
            }

            if (replacement != null) {
                world.setBlock(pos, replacement, 3);
                placed = true;
            }
        }
        return placed;
    }
}
