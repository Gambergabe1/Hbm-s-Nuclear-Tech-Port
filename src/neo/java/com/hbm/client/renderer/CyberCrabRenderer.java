package com.hbm.client.renderer;

import com.hbm.HbmNuclearTech;
import com.hbm.client.model.CyberCrabModel;
import com.hbm.entity.mob.CyberCrabEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public final class CyberCrabRenderer extends MobRenderer<CyberCrabEntity, CyberCrabModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/entity/crab.png"
    );
    private static final ResourceLocation EMISSIVE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/entity/crab_e.png"
    );

    public CyberCrabRenderer(EntityRendererProvider.Context context) {
        super(context, new CyberCrabModel(context.bakeLayer(CyberCrabModel.LAYER_LOCATION)), 0.35F);
        this.addLayer(new CyberCrabEyesLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CyberCrabEntity entity) {
        return TEXTURE;
    }

    private static final class CyberCrabEyesLayer extends EyesLayer<CyberCrabEntity, CyberCrabModel> {
        private CyberCrabEyesLayer(CyberCrabRenderer renderer) {
            super(renderer);
        }

        @Override
        public RenderType renderType() {
            return RenderType.eyes(EMISSIVE_TEXTURE);
        }
    }
}
