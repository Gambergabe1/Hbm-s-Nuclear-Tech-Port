package com.hbm.registry;

import com.hbm.HbmNuclearTech;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class HbmFluidBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HbmNuclearTech.MODID);

    private HbmFluidBlocks() {
    }

    public static DeferredBlock<LiquidBlock> register(String name, Supplier<? extends FlowingFluid> fluidSupplier) {
        return BLOCKS.register(name, () -> new LiquidBlock(
            fluidSupplier.get(),
            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
        ));
    }
}
