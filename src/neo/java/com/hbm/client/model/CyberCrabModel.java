package com.hbm.client.model;

import com.hbm.HbmNuclearTech;
import com.hbm.entity.mob.CyberCrabEntity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public final class CyberCrabModel extends HierarchicalModel<CyberCrabEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "cyber_crab"),
        "main"
    );
    private static final float HALF_PI = (float) (Math.PI / 2.0D);

    private final ModelPart root;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightRearLeg;
    private final ModelPart leftRearLeg;
    private final ModelPart rightFrontFoot;
    private final ModelPart leftFrontFoot;
    private final ModelPart rightRearFoot;
    private final ModelPart leftRearFoot;

    public CyberCrabModel(ModelPart root) {
        this.root = root;
        ModelPart body = root.getChild("body");
        this.rightFrontLeg = body.getChild("right_front_leg");
        this.leftFrontLeg = body.getChild("left_front_leg");
        this.rightRearLeg = body.getChild("right_rear_leg");
        this.leftRearLeg = body.getChild("left_rear_leg");
        this.rightFrontFoot = body.getChild("right_front_foot");
        this.leftFrontFoot = body.getChild("left_front_foot");
        this.rightRearFoot = body.getChild("right_rear_foot");
        this.leftRearFoot = body.getChild("left_rear_foot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
            "body",
            CubeListBuilder.create(),
            PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -HALF_PI, 0.0F)
        );

        body.addOrReplaceChild(
            "shell_base",
            CubeListBuilder.create().texOffs(1, 1).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 4.0F),
            PartPose.offset(-2.0F, -3.0F, -2.0F)
        );
        body.addOrReplaceChild(
            "shell_mid",
            CubeListBuilder.create().texOffs(17, 1).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 6.0F),
            PartPose.offset(-2.0F, -4.0F, -3.0F)
        );
        body.addOrReplaceChild(
            "shell_top",
            CubeListBuilder.create().texOffs(33, 1).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F),
            PartPose.offset(-1.5F, -5.0F, -1.5F)
        );
        body.addOrReplaceChild(
            "shell_bridge",
            CubeListBuilder.create().texOffs(49, 1).mirror().addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 2.0F),
            PartPose.offset(-2.0F, -4.5F, -1.0F)
        );
        body.addOrReplaceChild(
            "shell_wide",
            CubeListBuilder.create().texOffs(1, 9).mirror().addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 4.0F),
            PartPose.offset(-3.0F, -4.0F, -2.0F)
        );
        body.addOrReplaceChild(
            "right_front_leg",
            CubeListBuilder.create().texOffs(25, 9).mirror().addBox(-0.5F, 0.0F, 2.0F, 1.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.17453293F, 0.78539816F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_front_leg",
            CubeListBuilder.create().texOffs(41, 9).mirror().addBox(-0.5F, 0.0F, 2.0F, 1.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.17453293F, -0.78539816F, 0.0F)
        );
        body.addOrReplaceChild(
            "right_rear_leg",
            CubeListBuilder.create().texOffs(1, 17).mirror().addBox(-0.5F, 0.0F, 2.0F, 1.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.17453293F, -2.35619449F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_rear_leg",
            CubeListBuilder.create().texOffs(17, 17).mirror().addBox(-0.5F, 0.0F, 2.0F, 1.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.17453293F, 2.35619449F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_front_foot",
            CubeListBuilder.create().texOffs(57, 9).mirror().addBox(-0.5F, 1.0F, 4.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.17453293F, -0.78539816F, 0.0F)
        );
        body.addOrReplaceChild(
            "right_front_foot",
            CubeListBuilder.create().texOffs(33, 17).mirror().addBox(-0.5F, 1.0F, 4.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.17453293F, 0.78539816F, 0.0F)
        );
        body.addOrReplaceChild(
            "right_rear_foot",
            CubeListBuilder.create().texOffs(41, 17).mirror().addBox(-0.5F, 1.0F, 4.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.17453293F, -2.35619449F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_rear_foot",
            CubeListBuilder.create().texOffs(49, 17).mirror().addBox(-0.5F, 1.0F, 4.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.17453293F, 2.35619449F, 0.0F)
        );
        body.addOrReplaceChild(
            "right_front_fang",
            CubeListBuilder.create().texOffs(17, 1).mirror().addBox(-0.5F, 0.0F, 1.5F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.43633231F, -0.6981317F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_front_fang",
            CubeListBuilder.create().texOffs(33, 9).mirror().addBox(-0.5F, 0.0F, 1.5F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.43633231F, 0.87266463F, 0.0F)
        );
        body.addOrReplaceChild(
            "right_rear_fang",
            CubeListBuilder.create().texOffs(49, 9).mirror().addBox(-0.5F, 0.0F, 1.5F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.43633231F, -2.26892803F, 0.0F)
        );
        body.addOrReplaceChild(
            "left_rear_fang",
            CubeListBuilder.create().texOffs(9, 17).mirror().addBox(-0.5F, 0.0F, 1.5F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.43633231F, 2.44346095F, 0.0F)
        );
        body.addOrReplaceChild(
            "shell_fore",
            CubeListBuilder.create().texOffs(1, 25).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 4.0F),
            PartPose.offset(-1.0F, -4.5F, -2.0F)
        );
        body.addOrReplaceChild(
            "shell_core",
            CubeListBuilder.create().texOffs(17, 25).mirror().addBox(0.0F, 0.0F, 0.0F, 5.0F, 1.0F, 3.0F),
            PartPose.offset(-2.5F, -3.5F, -1.5F)
        );
        body.addOrReplaceChild(
            "shell_back",
            CubeListBuilder.create().texOffs(33, 25).mirror().addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 5.0F),
            PartPose.offset(-1.5F, -3.5F, -2.5F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(CyberCrabEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        float sway = -(Mth.cos(limbSwing * 1.3324F) * 0.4F) * limbSwingAmount * 1.5F;
        this.rightFrontFoot.yRot += sway;
        this.leftFrontFoot.yRot -= sway;
        this.rightRearFoot.yRot -= sway;
        this.leftRearFoot.yRot += sway;

        this.rightFrontLeg.yRot = this.rightFrontFoot.yRot;
        this.leftFrontLeg.yRot = this.leftFrontFoot.yRot;
        this.rightRearLeg.yRot = this.rightRearFoot.yRot;
        this.leftRearLeg.yRot = this.leftRearFoot.yRot;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
