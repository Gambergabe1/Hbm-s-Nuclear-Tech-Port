package com.hbm.entity.mob;

import java.util.List;

import com.hbm.util.HbmEffectUtil;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class GlowingOneEntity extends Zombie {
    private static final int EFFECT_RADIUS = 16;

    public GlowingOneEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 200;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
            .add(Attributes.MAX_HEALTH, 250.0D)
            .add(Attributes.ATTACK_DAMAGE, 50.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 2.0D)
            .add(Attributes.ARMOR, 10.0D)
            .add(Attributes.ARMOR_TOUGHNESS, 5.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide || isRemoved() || tickCount % 20 != 0) {
            return;
        }

        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(EFFECT_RADIUS));
        for (LivingEntity entity : entities) {
            if (entity == this) {
                continue;
            }

            double distance = Math.sqrt(distanceToSqr(entity));
            if (distance >= EFFECT_RADIUS) {
                continue;
            }

            if (entity instanceof Zombie zombie) {
                float healAmount = (float) (0.4D * (EFFECT_RADIUS - distance));
                if (healAmount > 0.0F && zombie.getHealth() < zombie.getMaxHealth()) {
                    zombie.heal(healAmount);
                }
            } else {
                HbmEffectUtil.applyRadiation(entity, 2.0F);
            }
        }
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
