package com.hbm.entity.mob;

import java.util.List;

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

public class NuclearCreeperEntity extends Creeper {
    public NuclearCreeperEntity(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        applyConfiguredDefaults();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 50.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && !isRemoved()) {
            if (tickCount % HbmCommonConfig.nuclearCreeperHealIntervalTicks() == 0 && getHealth() < getMaxHealth()) {
                heal(1.0F);
            }

            if (tickCount % HbmCommonConfig.nuclearCreeperRadiationIntervalTicks() == 0) {
                applyRadiationAura(
                    HbmCommonConfig.nuclearCreeperRadiationRadius(),
                    HbmCommonConfig.nuclearCreeperRadiationPerPulse()
                );
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (!tag.contains("Fuse", 99) || !tag.contains("ExplosionRadius", 99)) {
            applyConfiguredDefaults();
        }
    }

    private void applyRadiationAura(double radius, float rads) {
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius));
        for (LivingEntity entity : entities) {
            if (entity != this) {
                HbmEffectUtil.applyRadiation(entity, rads);
            }
        }
    }

    private void applyConfiguredDefaults() {
        CompoundTag defaults = new CompoundTag();
        defaults.putShort("Fuse", (short) HbmCommonConfig.nuclearCreeperFuseTicks());
        defaults.putByte("ExplosionRadius", (byte) HbmCommonConfig.nuclearCreeperExplosionRadius());
        if (isIgnited()) {
            defaults.putBoolean("ignited", true);
        }
        defaults.putBoolean("powered", isPowered());
        super.readAdditionalSaveData(defaults);
    }
}
