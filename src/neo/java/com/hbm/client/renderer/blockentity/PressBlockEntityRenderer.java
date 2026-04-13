package com.hbm.client.renderer.blockentity;

import com.hbm.block.AbstractFacingMachineBlock;
import com.hbm.client.state.HbmClientState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class PressBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final int MAX_PROGRESS = 200;
    private final ItemRenderer itemRenderer;

    public PressBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        HbmClientState.PressMachineState state = HbmClientState.getPressMachineState(blockEntity.getBlockPos());
        Level level = blockEntity.getLevel();
        if (state == null || level == null) {
            return;
        }

        float progress = Mth.clamp(state.progress() / (float) MAX_PROGRESS, 0.0F, 1.0F);
        Direction facing = blockEntity.getBlockState().hasProperty(AbstractFacingMachineBlock.FACING)
            ? blockEntity.getBlockState().getValue(AbstractFacingMachineBlock.FACING)
            : Direction.NORTH;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));

        renderPressHead(progress, poseStack, buffer);
        renderInput(state.input(), progress, poseStack, buffer, packedLight, packedOverlay, level, blockEntity.getBlockPos().hashCode());
        renderStamp(state.stamp(), progress, poseStack, buffer, packedLight, packedOverlay, level, blockEntity.getBlockPos().hashCode() + 31);

        poseStack.popPose();
    }

    private void renderPressHead(float progress, PoseStack poseStack, MultiBufferSource buffer) {
        double headBottom = 0.92D - progress * 0.42D;
        LevelRenderer.renderLineBox(
            poseStack,
            buffer.getBuffer(RenderType.lines()),
            -0.24D,
            headBottom,
            -0.34D,
            0.24D,
            headBottom + 0.12D,
            0.06D,
            0.78F,
            0.80F,
            0.83F,
            0.85F
        );
    }

    private void renderInput(
        ItemStack stack,
        float progress,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay,
        Level level,
        int seed
    ) {
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.28D - progress * 0.04D, -0.14D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.55F, 0.55F, 0.55F);
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, level, seed);
        poseStack.popPose();
    }

    private void renderStamp(
        ItemStack stack,
        float progress,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay,
        Level level,
        int seed
    ) {
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 1.00D - progress * 0.42D, -0.14D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.65F, 0.65F, 0.65F);
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, level, seed);
        poseStack.popPose();
    }
}
