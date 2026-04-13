package com.hbm.block;

import com.hbm.blockentity.ElectricFurnaceBlockEntity;
import com.hbm.registry.HbmBlockEntityTypes;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class ElectricFurnaceBlock extends AbstractLitFacingMachineBlock {
    public static final MapCodec<ElectricFurnaceBlock> CODEC = simpleCodec(ElectricFurnaceBlock::new);

    public ElectricFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<ElectricFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        net.minecraft.core.Direction facing = state.getValue(FACING);
        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + random.nextDouble() * 6.0D / 16.0D;
        double centerZ = pos.getZ() + 0.5D;
        double frontOffset = 0.52D;
        double sideOffset = random.nextDouble() * 0.6D - 0.3D;

        double particleX = centerX;
        double particleZ = centerZ;

        if (facing == Direction.WEST) {
            particleX = centerX - frontOffset;
            particleZ = centerZ + sideOffset;
        } else if (facing == Direction.EAST) {
            particleX = centerX + frontOffset;
            particleZ = centerZ + sideOffset;
        } else if (facing == Direction.NORTH) {
            particleX = centerX + sideOffset;
            particleZ = centerZ - frontOffset;
        } else if (facing == Direction.SOUTH) {
            particleX = centerX + sideOffset;
            particleZ = centerZ + frontOffset;
        }

        level.addParticle(ParticleTypes.SMOKE, particleX, centerY, particleZ, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, particleX, centerY, particleZ, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricFurnaceBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_ELECTRIC_FURNACE.get(), ElectricFurnaceBlockEntity::serverTick);
    }
}
