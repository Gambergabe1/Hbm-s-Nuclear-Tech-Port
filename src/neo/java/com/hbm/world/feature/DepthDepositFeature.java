package com.hbm.world.feature;

import com.hbm.world.feature.config.DepthDepositConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class DepthDepositFeature extends Feature<DepthDepositConfig> {

    public DepthDepositFeature() {
        super(DepthDepositConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<DepthDepositConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos centerPos = context.origin();
        RandomSource random = context.random();
        DepthDepositConfig config = context.config();

        int x = centerPos.getX();
        int y = centerPos.getY();
        int z = centerPos.getZ();

        int size = config.size();
        double fill = config.fill();
        
        Block oreBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(config.oreBlock()));
        Block fillerBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(config.fillerBlock()));
        Block genTargetBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(config.genTargetBlock()));

        int radius = size;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dx * dx + dy * dy + dz * dz <= radius * radius && dz <= radius; dz++) {
                    BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);
                    double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    BlockState stateAtPos = world.getBlockState(pos);

                    // Check if the block at pos is the target block (STONE, BEDROCK)
                    if (stateAtPos.is(genTargetBlock) || stateAtPos.is(Blocks.BEDROCK)) { // Allowing BEDROCK replacement as per original logic
                        if (len + random.nextInt(2) < size * fill) {
                            world.setBlock(pos, oreBlock.defaultBlockState(), 3);
                        } else if (len + random.nextInt(2) <= size) {
                            world.setBlock(pos, fillerBlock.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        return true;
    }
}
