package com.hbm.block;

import com.hbm.armor.HbmHazardClass;
import com.hbm.util.HbmEffectUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GasBlock extends Block {
    private final HbmHazardClass hazard;

    public GasBlock(HbmHazardClass hazard, Properties properties) {
        super(properties);
        this.hazard = hazard;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            level.removeBlock(pos, false);
            return;
        }

        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos));
        for (LivingEntity living : list) {
            HbmEffectUtil.applyGasExposure(living, hazard);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living && level.getGameTime() % 20 == 0) {
            HbmEffectUtil.applyGasExposure(living, hazard);
        }
    }
}
