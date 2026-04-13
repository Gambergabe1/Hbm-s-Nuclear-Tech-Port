package com.hbm.entity.mob;

import com.hbm.config.HbmCommonConfig;
import com.hbm.util.HbmEffectUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class TaintedCreeperEntity extends Creeper {
    public TaintedCreeperEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        applyConfiguredDefaults();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && !isRemoved()) {
            if (tickCount % HbmCommonConfig.taintedCreeperHealIntervalTicks() == 0 && getHealth() < getMaxHealth()) {
                heal(1.0F);
            }

            if (tickCount % HbmCommonConfig.taintedCreeperRadiationIntervalTicks() == 0) {
                level().getEntitiesOfClass(
                    LivingEntity.class,
                    getBoundingBox().inflate(HbmCommonConfig.taintedCreeperRadiationRadius())
                ).forEach(entity -> {
                    if (entity != this) {
                        HbmEffectUtil.applyRadiation(entity, HbmCommonConfig.taintedCreeperRadiationPerPulse());
                    }
                });
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (!tag.contains("ExplosionRadius", 99)) {
            applyConfiguredDefaults();
        }
    }

    private void applyConfiguredDefaults() {
        CompoundTag defaults = new CompoundTag();
        defaults.putByte("ExplosionRadius", (byte) HbmCommonConfig.taintedCreeperExplosionRadius());
        if (isIgnited()) {
            defaults.putBoolean("ignited", true);
        }
        defaults.putBoolean("powered", isPowered());
        super.readAdditionalSaveData(defaults);
    }
}
