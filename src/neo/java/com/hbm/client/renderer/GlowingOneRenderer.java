package com.hbm.client.renderer;

import com.hbm.HbmNuclearTech;
import com.hbm.entity.mob.GlowingOneEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class GlowingOneRenderer extends ZombieRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/entity/glowingone.png"
    );

    public GlowingOneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie entity) {
        return entity instanceof GlowingOneEntity ? TEXTURE : super.getTextureLocation(entity);
    }
}
