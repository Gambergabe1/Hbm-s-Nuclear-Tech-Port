package com.hbm.entity.projectile;

import com.hbm.registry.HbmEntityTypes;
import com.hbm.registry.HbmItems;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GenericGrenadeEntity extends ThrowableItemProjectile {
    private static final int DEFAULT_MAX_FUSE = 100;
    private static final double BOUNCE_STRENGTH = 0.25D;
    private static final double SURFACE_FRICTION = 0.8D;

    private int fuseTime;
    private int maxFuseTicks = DEFAULT_MAX_FUSE;

    public GenericGrenadeEntity(EntityType<? extends GenericGrenadeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GenericGrenadeEntity(Level level, LivingEntity owner) {
        super(HbmEntityTypes.ENTITY_GRENADE_GENERIC.get(), owner, level);
    }

    public GenericGrenadeEntity(Level level, double x, double y, double z) {
        super(HbmEntityTypes.ENTITY_GRENADE_GENERIC.get(), x, y, z, level);
    }

    @Override
    public void tick() {
        super.tick();
        updateVisualRotation();
        if (level().isClientSide) {
            spawnFuseParticles();
        }
        if (!level().isClientSide && !isRemoved() && ++fuseTime >= maxFuseTicks) {
            explode();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return HbmItems.GRENADE_GENERIC.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level().isClientSide || isRemoved()) {
            return;
        }

        Vec3 movement = getDeltaMovement();
        Vec3 normal = position().subtract(result.getEntity().getBoundingBox().getCenter());
        if (normal.lengthSqr() < 1.0E-6D) {
            normal = movement.lengthSqr() > 1.0E-6D ? movement.normalize().scale(-1.0D) : new Vec3(0.0D, 1.0D, 0.0D);
        } else {
            normal = normal.normalize();
        }

        double projected = movement.dot(normal);
        Vec3 reflected = movement.subtract(normal.scale(2.0D * projected)).scale(BOUNCE_STRENGTH * 1.5D);
        setDeltaMovement(reflected);
        setPos(position().add(normal.scale(0.05D)));
        hasImpulse = true;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide || isRemoved()) {
            return;
        }

        Direction face = result.getDirection();
        Vec3 movement = getDeltaMovement();
        double x = face.getAxis() == Direction.Axis.X ? -movement.x * BOUNCE_STRENGTH : movement.x * SURFACE_FRICTION;
        double y = face.getAxis() == Direction.Axis.Y ? -movement.y * BOUNCE_STRENGTH : movement.y * SURFACE_FRICTION;
        double z = face.getAxis() == Direction.Axis.Z ? -movement.z * BOUNCE_STRENGTH : movement.z * SURFACE_FRICTION;

        if (face == Direction.UP && Math.abs(y) < 0.08D) {
            y = 0.0D;
        }

        setDeltaMovement(x, y, z);
        setPos(
            getX() + face.getStepX() * 0.01D,
            getY() + face.getStepY() * 0.01D,
            getZ() + face.getStepZ() * 0.01D
        );
        hasImpulse = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Fuse", fuseTime);
        tag.putInt("MaxFuse", maxFuseTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        fuseTime = tag.getInt("Fuse");
        maxFuseTicks = Math.max(1, tag.getInt("MaxFuse"));
    }

    private void explode() {
        if (isRemoved()) {
            return;
        }

        level().explode(this, getX(), getY(), getZ(), 2.0F, true, Level.ExplosionInteraction.TNT);
        discard();
    }

    public void setFuseTicks(int fuseTicks) {
        maxFuseTicks = Math.max(1, fuseTicks);
    }

    private void updateVisualRotation() {
        Vec3 movement = getDeltaMovement();
        if (movement.lengthSqr() <= 1.0E-6D) {
            return;
        }

        xRotO = getXRot();
        yRotO = getYRot();
        setXRot(getXRot() - (float) (movement.length() * 25.0D));
        float targetYaw = (float) (Mth.atan2(movement.x, movement.z) * (180.0D / Math.PI));
        setYRot(Mth.rotLerp(0.2F, getYRot(), targetYaw));
    }

    private void spawnFuseParticles() {
        Vec3 movement = getDeltaMovement();
        double particleX = getX() - movement.x * 0.25D;
        double particleY = getY() + 0.15D;
        double particleZ = getZ() - movement.z * 0.25D;
        level().addParticle(ParticleTypes.SMOKE, particleX, particleY, particleZ, 0.0D, 0.01D, 0.0D);

        if (random.nextInt(3) == 0) {
            level().addParticle(ParticleTypes.FLAME, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
