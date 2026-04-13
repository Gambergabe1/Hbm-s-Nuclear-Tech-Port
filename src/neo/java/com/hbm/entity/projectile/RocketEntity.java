package com.hbm.entity.projectile;

import java.util.List;

import com.hbm.registry.HbmEntityTypes;
import com.hbm.registry.HbmItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.projectile.ItemSupplier;

public class RocketEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<Boolean> CRITICAL = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);

    public int explosionSize = 5;
    private int ticksInGround;
    private int ticksInAir;
    private boolean inGround;
    private double damage = 2.0D;

    public RocketEntity(EntityType<? extends RocketEntity> type, Level level) {
        super(type, level);
    }

    public RocketEntity(Level level, double x, double y, double z) {
        this(HbmEntityTypes.ENTITY_ROCKET.get(), level);
        this.setPos(x, y, z);
    }

    public RocketEntity(Level level, LivingEntity shooter) {
        this(HbmEntityTypes.ENTITY_ROCKET.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(HbmItems.AMMO_ROCKET.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CRITICAL, false);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 movement = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = movement.horizontalDistance();
            this.setYRot((float)(Mth.atan2(movement.x, movement.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(movement.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (this.inGround) {
            this.ticksInGround++;
            if (this.ticksInGround >= 1) {
                this.explode();
            }
            return;
        }

        this.ticksInAir++;
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }

        movement = this.getDeltaMovement();
        double d2 = movement.x;
        double d3 = movement.y;
        double d4 = movement.z;

        double d5 = this.getX() + d2;
        double d6 = this.getY() + d3;
        double d7 = this.getZ() + d4;
        
        double d8 = movement.horizontalDistance();
        this.setYRot((float)(Mth.atan2(d2, d4) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(d3, d8) * (double)(180F / (float)Math.PI)));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));

        float f = 0.99F;
        if (this.isInWater()) {
            f = 0.8F;
        }

        this.setDeltaMovement(movement.scale((double)f));
        if (!this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - 0.05D, vec34.z);
        }

        this.setPos(d5, d6, d7);
        this.checkInsideBlocks();

        if (this.ticksInAir > 250) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            explode();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.inGround = true;
        explode();
    }

    private void explode() {
        if (!level().isClientSide) {
            com.hbm.explosion.ExplosionLarge.explode(level(), getX(), getY(), getZ(), (float)explosionSize, true, true, true);
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.ticksInGround = tag.getShort("life");
        this.inGround = tag.getBoolean("inGround");
        this.damage = tag.getDouble("damage");
        this.explosionSize = tag.getInt("explosionSize");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("life", (short)this.ticksInGround);
        tag.putBoolean("inGround", this.inGround);
        tag.putDouble("damage", this.damage);
        tag.putInt("explosionSize", this.explosionSize);
    }
}
