package com.hbm.entity.mob;

import com.hbm.api.entity.IRadiationImmune;
import com.hbm.registry.HbmEntityTypes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CyberCrabEntity extends Monster implements IRadiationImmune {
    private boolean explodedOnDeath;

    public CyberCrabEntity(EntityType<? extends CyberCrabEntity> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, (entity) -> entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() < 0.5F));
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && isAlive() && (isInWaterRainOrBubble() || isOnFire())) {
            hurt(damageSources().generic(), 10.0F);
        }
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource damageSource) {
        if (!explodedOnDeath && !level().isClientSide) {
            explodedOnDeath = true;
            level().explode(this, getX(), getY(), getZ(), 0.1F, Level.ExplosionInteraction.TNT);
        }

        super.die(damageSource);
    }
}
