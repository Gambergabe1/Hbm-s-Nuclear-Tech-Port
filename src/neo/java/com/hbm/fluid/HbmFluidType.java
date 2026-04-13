package com.hbm.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

public final class HbmFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final int tintColor;

    public HbmFluidType(
        ResourceLocation stillTexture,
        ResourceLocation flowingTexture,
        int tintColor,
        Properties properties
    ) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.tintColor = tintColor;
    }

    public ResourceLocation stillTexture() {
        return stillTexture;
    }

    public ResourceLocation flowingTexture() {
        return flowingTexture;
    }

    public int tintColor() {
        return tintColor;
    }
}
