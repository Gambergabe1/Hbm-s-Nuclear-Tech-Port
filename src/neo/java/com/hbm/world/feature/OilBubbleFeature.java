package com.hbm.world.feature;

import com.hbm.registry.HbmBlocks;
import com.hbm.world.feature.config.OilBubbleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class OilBubbleFeature extends Feature<OilBubbleConfig> {
    public OilBubbleFeature() {
        super(OilBubbleConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OilBubbleConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos centerPos = context.origin();
        OilBubbleConfig config = context.config();

        int x = centerPos.getX();
        int y = centerPos.getY();
        int z = centerPos.getZ();
        int radius = config.radius();
        int r2 = radius * radius;
        int r22 = r2 / 2;
        boolean placed = false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for(int xx = -radius; xx < radius; xx++) {
            int X = xx + x;
            int XX = xx * xx;
            for(int yy = -radius; yy < radius; yy++) {
                int Y = yy + y;
                int YY = XX + yy * yy * 3;
                for(int zz = -radius; zz < radius; zz++) {
                    int Z = zz + z;
                    int ZZ = YY + zz * zz;
                    if(ZZ < r22) {
                        pos.set(X, Y, Z);
                        BlockState stateAtPos = world.getBlockState(pos);
                        if(stateAtPos.is(Blocks.STONE)) {
                            world.setBlock(pos, HbmBlocks.STONE_CRACKED.get().defaultBlockState(), 3);
                            placed = true;
                        }
                    }
                }
            }
        }
        return placed;
    }
}
