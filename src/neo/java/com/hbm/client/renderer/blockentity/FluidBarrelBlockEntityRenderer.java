package com.hbm.client.renderer.blockentity;

import com.hbm.blockentity.FluidBarrelBlockEntity;
import com.hbm.client.state.HbmClientState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FluidBarrelBlockEntityRenderer implements BlockEntityRenderer<FluidBarrelBlockEntity> {
    public FluidBarrelBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
        FluidBarrelBlockEntity blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay
    ) {
        FluidStack fluid = HbmClientState.getBarrelState(blockEntity.getBlockPos());
        if (fluid.isEmpty()) {
            fluid = blockEntity.getFluidTank().getFluid();
        }
        if (fluid.isEmpty() || blockEntity.getCapacity() <= 0) {
            return;
        }

        float fillRatio = Mth.clamp(fluid.getAmount() / (float) blockEntity.getCapacity(), 0.02F, 1.0F);
        int tint = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
        float red = ((tint >> 16) & 0xFF) / 255.0F;
        float green = ((tint >> 8) & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;

        double minX = 2.5D / 16.0D;
        double minY = 1.0D / 16.0D;
        double minZ = 2.5D / 16.0D;
        double maxX = 13.5D / 16.0D;
        double maxY = minY + fillRatio * (13.0D / 16.0D);
        double maxZ = 13.5D / 16.0D;

        LevelRenderer.renderLineBox(
            poseStack,
            buffer.getBuffer(RenderType.lines()),
            minX,
            minY,
            minZ,
            maxX,
            maxY,
            maxZ,
            red,
            green,
            blue,
            0.90F
        );
    }
}
