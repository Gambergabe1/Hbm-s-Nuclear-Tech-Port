package com.hbm.explosion;

import java.util.List;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ExplosionLarge {
    private ExplosionLarge() {
    }

    public static void spawnParticlesRadial(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.SMOKE, x, y, z, count, 0.75D, 0.35D, 0.75D, 0.01D);
    }

    public static void spawnParticles(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.EXPLOSION, x, y, z, count, 0.5D, 0.25D, 0.5D, 0.01D);
    }

    public static void spawnBurst(Level world, double x, double y, double z, int count, double strength) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }

        RandomSource random = world.getRandom();
        int particleCount = Math.max(1, count);
        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double velocity = strength * (0.25D + random.nextDouble() * 0.5D);
            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                x,
                y,
                z,
                0,
                Math.cos(angle) * velocity,
                random.nextDouble() * 0.2D,
                Math.sin(angle) * velocity,
                0.0D
            );
        }
    }

    public static void spawnShock(Level world, double x, double y, double z, int count, double strength) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }

        RandomSource random = world.getRandom();
        int particleCount = Math.max(1, count);
        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double velocity = strength * (0.25D + random.nextDouble() * 0.35D);
            serverLevel.sendParticles(
                ParticleTypes.POOF,
                x,
                y + 0.5D,
                z,
                0,
                Math.cos(angle) * velocity,
                random.nextDouble() * 0.1D,
                Math.sin(angle) * velocity,
                0.0D
            );
        }
    }

    public static void spawnRubble(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.POOF, x, y, z, count, 0.75D, 0.5D, 0.75D, 0.02D);
    }

    public static void spawnShrapnels(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.CRIT, x, y, z, count, 0.75D, 0.5D, 0.75D, 0.02D);
    }

    public static void jolt(Level world, double posX, double posY, double posZ, Vec3 vector, float strength, float depth) {
        if (world.isClientSide) {
            return;
        }

        Vec3 step = vector.lengthSqr() > 0.0D ? vector.normalize() : Vec3.ZERO;
        for (double travelled = 0.0D; travelled <= depth; travelled += 3.0D) {
            world.explode(
                null,
                posX + step.x() * travelled,
                posY + step.y() * travelled,
                posZ + step.z() * travelled,
                Math.max(1.0F, strength * 0.5F),
                Level.ExplosionInteraction.TNT
            );
        }

        spawnParticles(world, posX, posY + 2.0D, posZ, cloudFunction((int) strength));
        spawnRubble(world, posX, posY + 2.0D, posZ, rubbleFunction((int) strength));
        spawnShrapnels(world, posX, posY + 2.0D, posZ, shrapnelFunction((int) strength));
    }

    public static void spawnTracers(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.END_ROD, x, y, z, count, 0.25D, 0.25D, 0.25D, 0.01D);
    }

    public static void spawnShrapnelShower(
        Level world,
        double x,
        double y,
        double z,
        double motionX,
        double motionY,
        double motionZ,
        int count,
        double deviation
    ) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }

        RandomSource random = world.getRandom();
        int particleCount = Math.max(1, count);
        for (int i = 0; i < particleCount; i++) {
            serverLevel.sendParticles(
                ParticleTypes.CRIT,
                x,
                y,
                z,
                0,
                motionX + random.nextGaussian() * deviation,
                motionY + random.nextGaussian() * deviation,
                motionZ + random.nextGaussian() * deviation,
                0.0D
            );
        }
    }

    public static void spawnMissileDebris(
        Level world,
        double x,
        double y,
        double z,
        double motionX,
        double motionY,
        double motionZ,
        double deviation,
        List<ItemStack> debris,
        ItemStack rareDrop
    ) {
        if (world.isClientSide || debris == null) {
            return;
        }

        RandomSource random = world.getRandom();
        for (ItemStack stack : debris) {
            if (stack.isEmpty()) {
                continue;
            }

            int drops = Math.max(1, random.nextInt(stack.getCount() + 1));
            for (int i = 0; i < drops; i++) {
                ItemStack piece = stack.copy();
                piece.setCount(1);
                ItemEntity entity = new ItemEntity(world, x, y, z, piece);
                entity.setDeltaMovement(
                    motionX + random.nextGaussian() * deviation,
                    motionY + random.nextGaussian() * deviation,
                    motionZ + random.nextGaussian() * deviation
                );
                world.addFreshEntity(entity);
            }
        }

        if (!rareDrop.isEmpty() && random.nextFloat() < 0.1F) {
            ItemEntity entity = new ItemEntity(world, x, y, z, rareDrop.copy());
            entity.setDeltaMovement(motionX, motionY, motionZ);
            world.addFreshEntity(entity);
        }
    }

    public static void explode(Level world, double x, double y, double z, float strength, boolean cloud, boolean rubble, boolean shrapnel) {
        if (world.isClientSide) {
            return;
        }

        world.explode(null, x, y, z, Math.max(1.0F, strength), Level.ExplosionInteraction.TNT);
        if (cloud) {
            spawnParticles(world, x, y + 2.0D, z, cloudFunction((int) strength));
        }
        if (rubble) {
            spawnRubble(world, x, y + 2.0D, z, rubbleFunction((int) strength));
        }
        if (shrapnel) {
            spawnShrapnels(world, x, y + 2.0D, z, shrapnelFunction((int) strength));
        }
    }

    public static void explodeArea(
        Level world,
        double x,
        double y,
        double z,
        float radius,
        float strength,
        boolean cloud,
        boolean rubble,
        boolean shrapnel
    ) {
        if (world.isClientSide) {
            return;
        }

        float effectiveRadius = Math.max(1.0F, radius);
        float effectiveStrength = Math.max(1.0F, strength);
        Vec3 center = new Vec3(x, y, z);
        DamageSource damageSource = Explosion.getDefaultDamageSource(world, null);

        for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(x - effectiveRadius, y - effectiveRadius, z - effectiveRadius, x + effectiveRadius, y + effectiveRadius, z + effectiveRadius))) {
            if (!entity.isAlive() || entity.isSpectator()) {
                continue;
            }

            double distance = Math.sqrt(entity.distanceToSqr(center));
            if (distance > effectiveRadius) {
                continue;
            }

            float exposure = Explosion.getSeenPercent(center, entity);
            float scale = (1.0F - (float) (distance / effectiveRadius)) * exposure;
            if (scale <= 0.0F) {
                continue;
            }

            entity.hurt(damageSource, effectiveStrength * scale);
            Vec3 push = entity.position().subtract(center);
            if (push.lengthSqr() > 1.0E-6D) {
                Vec3 velocity = push.normalize().scale(Math.max(0.25F, effectiveStrength * 0.05F) * scale);
                entity.setDeltaMovement(entity.getDeltaMovement().add(velocity));
                entity.hurtMarked = true;
            }
        }

        if (cloud) {
            spawnParticles(world, x, y + 2.0D, z, cloudFunction((int) effectiveStrength));
        }
        if (rubble) {
            spawnRubble(world, x, y + 2.0D, z, rubbleFunction((int) effectiveStrength));
        }
        if (shrapnel) {
            spawnShrapnels(world, x, y + 2.0D, z, shrapnelFunction((int) effectiveStrength));
        }
    }

    public static int cloudFunction(int value) {
        return (int) (545 * (1 - Math.pow(Math.E, -value / 15.0D)) + 15);
    }

    public static int rubbleFunction(int value) {
        return Math.max(1, value / 10);
    }

    public static int shrapnelFunction(int value) {
        return Math.max(1, value / 3);
    }

    public static void explodeFire(Level world, double x, double y, double z, float strength, boolean cloud, boolean rubble, boolean shrapnel) {
        explode(world, x, y, z, strength, cloud, rubble, shrapnel);
        spawnBurst(world, x, y + 1.0D, z, Math.max(6, (int) strength), Math.max(1.0D, strength * 0.15D));
    }

    public static void spawnOilSpills(Level world, double x, double y, double z, int count) {
        sendParticles(world, ParticleTypes.LARGE_SMOKE, x, y, z, count, 0.6D, 0.2D, 0.6D, 0.01D);
    }

    public static void buster(Level world, double x, double y, double z, Vec3 vector, float strength, float depth) {
        jolt(world, x, y, z, vector, strength, depth);
    }

    private static void sendParticles(
        Level world,
        ParticleOptions particle,
        double x,
        double y,
        double z,
        int count,
        double spreadX,
        double spreadY,
        double spreadZ,
        double speed
    ) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }
        serverLevel.sendParticles(particle, x, y, z, Math.max(1, count), spreadX, spreadY, spreadZ, speed);
    }
}
