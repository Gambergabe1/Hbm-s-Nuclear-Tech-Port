package com.hbm.explosion;

import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.attachment.HbmLivingData;
import com.hbm.util.HbmEffectUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class NukeExplosionHandler {
    private static final int CUSTOM_CRATER_THRESHOLD = 8;
    private static final int MAX_CRATER_RADIUS = 72;
    private static final int MAX_CRATER_STRENGTH = 72;
    private static final int MAX_FIRE_ATTEMPTS = 256;

    private NukeExplosionHandler() {
    }

    public static void explodePayload(
        Level level,
        @Nullable Entity source,
        double x,
        double y,
        double z,
        float tnt,
        float nuke,
        float hydro,
        float bale,
        float dirty,
        float schrab,
        float sol,
        float euph
    ) {
        if (level.isClientSide) {
            return;
        }

        PayloadProfile profile = PayloadProfile.fromPayload(tnt, nuke, hydro, bale, dirty, schrab, sol, euph);
        if (!profile.requiresCustomHandling()) {
            ExplosionLarge.explode(level, x, y, z, profile.fallbackStrength(), true, true, true);
            return;
        }

        playDetonationEffects(level, x, y, z, profile);
        applyEntityEffects(level, source, x, y, z, profile);

        if (profile.craterRadius() >= CUSTOM_CRATER_THRESHOLD) {
            ExplosionNukeRayBatched crater = new ExplosionNukeRayBatched(
                level,
                x,
                y,
                z,
                profile.craterStrength(),
                profile.craterRadius(),
                profile.ignoreWater()
            );
            crater.collectAll();
            crater.processAll();
        } else if (profile.fallbackStrength() > 0.0F) {
            ExplosionLarge.explode(level, x, y, z, profile.fallbackStrength(), true, true, true);
        }

        igniteTerrain(level, x, y, z, profile);
        spawnAftermathParticles(level, x, y, z, profile);
    }

    private static void playDetonationEffects(Level level, double x, double y, double z, PayloadProfile profile) {
        float volume = Math.min(16.0F, 2.0F + profile.blastRadius() * 0.125F);
        float pitch = 0.55F + level.getRandom().nextFloat() * 0.15F;
        level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, volume, pitch);
    }

    private static void applyEntityEffects(Level level, @Nullable Entity source, double x, double y, double z, PayloadProfile profile) {
        Vec3 origin = new Vec3(x, y, z);
        DamageSource blastDamage = Explosion.getDefaultDamageSource(level, source);
        double maxRadius = Math.max(profile.blastRadius(), Math.max(profile.thermalRadius(), profile.radiationRadius()));
        AABB bounds = new AABB(x - maxRadius, y - maxRadius, z - maxRadius, x + maxRadius, y + maxRadius, z + maxRadius);

        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, bounds)) {
            if (!living.isAlive() || living == source) {
                continue;
            }

            double distance = Math.sqrt(living.distanceToSqr(origin));
            if (distance > maxRadius) {
                continue;
            }

            float exposure = Explosion.getSeenPercent(origin, living);
            if (exposure <= 0.0F) {
                continue;
            }

            if (distance <= profile.blastRadius()) {
                float blastScale = scaledExposure(distance, profile.blastRadius(), exposure);
                if (blastScale > 0.0F) {
                    float damage = profile.maxBlastDamage() * blastScale * blastScale + profile.maxBlastDamage() * 0.1F * blastScale;
                    if (damage > 0.5F) {
                        living.hurt(blastDamage, damage);
                    }

                    Vec3 push = living.position().add(0.0D, living.getBbHeight() * 0.5D, 0.0D).subtract(origin);
                    if (push.lengthSqr() > 1.0E-6D) {
                        Vec3 velocity = push.normalize().scale(profile.maxKnockback() * blastScale);
                        living.setDeltaMovement(living.getDeltaMovement().add(velocity.x, velocity.y * 0.75D + 0.05D, velocity.z));
                        living.hurtMarked = true;
                    }
                }
            }

            if (profile.thermalRadius() > 0.0F && distance <= profile.thermalRadius()) {
                float fireScale = scaledExposure(distance, profile.thermalRadius(), exposure);
                if (fireScale > 0.0F) {
                    living.igniteForSeconds(Math.max(1.0F, profile.fireSeconds() * fireScale));
                    float fireDamage = profile.maxFireDamage() * fireScale;
                    if (fireDamage > 0.5F) {
                        living.hurt(level.damageSources().inFire(), fireDamage);
                    }
                }
            }

            if (profile.radiationRadius() > 0.0F && distance <= profile.radiationRadius()) {
                float radiationScale = scaledExposure(distance, profile.radiationRadius(), exposure);
                if (radiationScale > 0.0F) {
                    HbmEffectUtil.applyRadiation(living, profile.instantRadiation() * radiationScale);
                    if (profile.contaminationTicks() > 0 && profile.contaminationMaxRadiation() > 0.0F) {
                        HbmAttachmentAccess.living(living).addContaminationEffect(
                            new HbmLivingData.ContaminationEffect(
                                profile.contaminationMaxRadiation() * radiationScale,
                                profile.contaminationTicks(),
                                profile.contaminationTicks(),
                                profile.ignoreArmorContamination()
                            )
                        );
                    }
                }
            }
        }
    }

    private static void igniteTerrain(Level level, double x, double y, double z, PayloadProfile profile) {
        if (!profile.ignitesBlocks() || profile.thermalRadius() < 4.0F) {
            return;
        }

        RandomSource random = level.getRandom();
        int attempts = Mth.clamp((int) (profile.thermalRadius() * profile.thermalRadius()), 24, MAX_FIRE_ATTEMPTS);
        BlockPos.MutableBlockPos firePos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < attempts; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double radius = Math.sqrt(random.nextDouble()) * profile.thermalRadius();
            int blockX = Mth.floor(x + Math.cos(angle) * radius);
            int blockZ = Mth.floor(z + Math.sin(angle) * radius);
            int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockX, blockZ);

            firePos.set(blockX, surfaceY, blockZ);
            if (!level.isInWorldBounds(firePos) || !level.getFluidState(firePos).isEmpty()) {
                continue;
            }

            if (!level.getBlockState(firePos).isAir()) {
                firePos.move(0, 1, 0);
            }
            if (!level.isInWorldBounds(firePos) || !level.getBlockState(firePos).isAir()) {
                continue;
            }

            BlockState fireState = BaseFireBlock.getState(level, firePos);
            if (!fireState.isAir() && random.nextFloat() < profile.firePlacementChance()) {
                level.setBlock(firePos, fireState, 3);
            }
        }
    }

    private static void spawnAftermathParticles(Level level, double x, double y, double z, PayloadProfile profile) {
        int visualStrength = profile.visualStrength();
        ExplosionLarge.spawnParticles(level, x, y + 2.0D, z, Math.min(1500, ExplosionLarge.cloudFunction(visualStrength)));
        ExplosionLarge.spawnShock(level, x, y + 0.5D, z, Math.min(256, visualStrength * 2), Math.max(0.5D, profile.blastRadius() * 0.08D));
        ExplosionLarge.spawnRubble(level, x, y + 2.0D, z, Math.min(256, Math.max(8, visualStrength / 2)));
        ExplosionLarge.spawnShrapnels(level, x, y + 2.0D, z, Math.min(384, Math.max(12, visualStrength)));

        if (profile.thermalRadius() > 0.0F) {
            ExplosionLarge.spawnBurst(level, x, y + 1.0D, z, Math.min(180, visualStrength * 2), Math.max(1.0D, profile.thermalRadius() * 0.06D));
        }
        if (profile.radiationRadius() > 0.0F) {
            ExplosionLarge.spawnParticlesRadial(level, x, y + 1.0D, z, Math.min(768, visualStrength * 4));
        }
        if (level.isClientSide) {
            return;
        }
        level.addParticle(ParticleTypes.FLASH, x, y + 1.0D, z, 0.0D, 0.0D, 0.0D);
    }

    private static float scaledExposure(double distance, float radius, float exposure) {
        if (radius <= 0.0F) {
            return 0.0F;
        }

        float distanceFactor = 1.0F - (float) (distance / radius);
        if (distanceFactor <= 0.0F) {
            return 0.0F;
        }

        return distanceFactor * exposure;
    }

    private record PayloadProfile(
        float fallbackStrength,
        int craterRadius,
        int craterStrength,
        float blastRadius,
        float maxBlastDamage,
        float maxKnockback,
        float thermalRadius,
        float maxFireDamage,
        float fireSeconds,
        boolean ignitesBlocks,
        float firePlacementChance,
        float radiationRadius,
        float instantRadiation,
        int contaminationTicks,
        float contaminationMaxRadiation,
        boolean ignoreArmorContamination,
        boolean ignoreWater
    ) {
        private static PayloadProfile fromPayload(
            float tnt,
            float nuke,
            float hydro,
            float bale,
            float dirty,
            float schrab,
            float sol,
            float euph
        ) {
            float conventionalYield = positive(tnt);
            float nuclearYield = positive(nuke) * 1.8F;
            float thermonuclearYield = positive(hydro) * 2.7F;
            float balefireYield = positive(bale) * 3.4F;
            float schrabYield = positive(schrab) * 4.0F;
            float solYield = positive(sol) * 4.4F;
            float chaosYield = positive(euph) * 5.0F;
            float dirtyYield = positive(dirty) * 0.25F;

            float kineticYield = conventionalYield + nuclearYield + thermonuclearYield + balefireYield + schrabYield + solYield + chaosYield + dirtyYield;
            float craterYield = conventionalYield * 0.7F
                + nuclearYield * 1.15F
                + thermonuclearYield * 1.35F
                + balefireYield * 1.45F
                + schrabYield * 1.55F
                + solYield * 1.7F
                + chaosYield * 1.9F;
            float thermalYield = conventionalYield * 0.4F
                + thermonuclearYield * 1.35F
                + balefireYield * 1.75F
                + solYield * 1.4F
                + chaosYield * 1.8F;
            float radiationYield = positive(dirty) * 1.6F
                + nuclearYield * 1.3F
                + thermonuclearYield * 1.55F
                + schrabYield * 1.8F
                + chaosYield * 1.1F;

            float fallbackStrength = Math.max(1.0F, (float) Math.sqrt(Math.max(1.0F, kineticYield)));
            int craterRadius = craterYield > 0.0F ? Mth.clamp(Math.round((float) Math.sqrt(craterYield) * 2.6F), 0, MAX_CRATER_RADIUS) : 0;
            int craterStrength = craterYield > 0.0F ? Mth.clamp(Math.round((float) Math.sqrt(craterYield) * 1.8F), 0, MAX_CRATER_STRENGTH) : 0;
            float blastRadius = Math.max(4.0F, (float) Math.sqrt(Math.max(1.0F, kineticYield)) * 2.4F);
            float maxBlastDamage = Math.max(6.0F, (float) Math.sqrt(Math.max(1.0F, kineticYield)) * 7.5F);
            float maxKnockback = Math.max(0.2F, blastRadius * 0.045F);
            float thermalRadius = thermalYield > 0.0F ? Math.max(4.0F, (float) Math.sqrt(thermalYield) * 2.1F) : 0.0F;
            float maxFireDamage = thermalYield > 0.0F ? Math.max(2.0F, (float) Math.sqrt(thermalYield) * 2.8F) : 0.0F;
            float fireSeconds = thermalYield > 0.0F ? Math.max(2.0F, (float) Math.sqrt(thermalYield) * 0.9F) : 0.0F;
            boolean ignitesBlocks = thermalYield > 0.0F;
            float firePlacementChance = Mth.clamp(0.15F + thermalYield / 120.0F, 0.15F, 0.9F);
            float radiationRadius = radiationYield > 0.0F ? Math.max(6.0F, (float) Math.sqrt(radiationYield) * 2.4F) : 0.0F;
            float instantRadiation = radiationYield > 0.0F ? Math.max(1.0F, radiationYield * 1.1F) : 0.0F;
            int contaminationTicks = radiationYield > 0.0F ? Mth.clamp(Math.round(120.0F + radiationYield * 18.0F), 80, 20 * 60) : 0;
            float contaminationMaxRadiation = radiationYield > 0.0F ? Mth.clamp(radiationYield * 0.035F, 0.2F, 6.0F) : 0.0F;
            boolean ignoreArmorContamination = euph > 0.0F || schrab > 0.0F;
            boolean ignoreWater = thermalYield > 0.0F;

            return new PayloadProfile(
                fallbackStrength,
                craterRadius,
                craterStrength,
                blastRadius,
                maxBlastDamage,
                maxKnockback,
                thermalRadius,
                maxFireDamage,
                fireSeconds,
                ignitesBlocks,
                firePlacementChance,
                radiationRadius,
                instantRadiation,
                contaminationTicks,
                contaminationMaxRadiation,
                ignoreArmorContamination,
                ignoreWater
            );
        }

        private boolean requiresCustomHandling() {
            return craterRadius >= CUSTOM_CRATER_THRESHOLD
                || thermalRadius >= 8.0F
                || radiationRadius >= 8.0F
                || contaminationTicks > 0;
        }

        private int visualStrength() {
            return Math.max(8, Math.max(craterRadius, Math.round(blastRadius)));
        }

        private static float positive(float value) {
            return Math.max(0.0F, value);
        }
    }
}
