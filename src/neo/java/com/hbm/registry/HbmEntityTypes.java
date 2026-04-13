package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.entity.mob.CyberCrabEntity;
import com.hbm.entity.mob.GlowingOneEntity;
import com.hbm.entity.mob.NuclearCreeperEntity;
import com.hbm.entity.mob.TaintedCreeperEntity;
import com.hbm.entity.projectile.AAShellEntity;
import com.hbm.entity.projectile.ChopperMineEntity;
import com.hbm.entity.projectile.FallingNukeEntity;
import com.hbm.entity.projectile.GenericGrenadeEntity;
import com.hbm.entity.projectile.RocketEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(Registries.ENTITY_TYPE, HbmNuclearTech.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<GlowingOneEntity>> ENTITY_GLOWING_ONE = ENTITY_TYPES.register(
        "entity_glowing_one",
        () -> EntityType.Builder.of(GlowingOneEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("entity_glowing_one")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<NuclearCreeperEntity>> ENTITY_NUCLEAR_CREEPER = ENTITY_TYPES.register(
        "entity_nuclear_creeper",
        () -> EntityType.Builder.of(NuclearCreeperEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.7F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("entity_nuclear_creeper")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TaintedCreeperEntity>> ENTITY_TAINTED_CREEPER = ENTITY_TYPES.register(
        "entity_tainted_creeper",
        () -> EntityType.Builder.of(TaintedCreeperEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.7F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("entity_tainted_creeper")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<CyberCrabEntity>> ENTITY_CYBER_CRAB = ENTITY_TYPES.register(
        "entity_cyber_crab",
        () -> EntityType.Builder.of(CyberCrabEntity::new, MobCategory.MONSTER)
            .sized(0.5F, 0.4F)
            .clientTrackingRange(8)
            .updateInterval(3)
            .build("entity_cyber_crab")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<GenericGrenadeEntity>> ENTITY_GRENADE_GENERIC = ENTITY_TYPES.register(
        "entity_grenade_generic",
        () -> EntityType.Builder.<GenericGrenadeEntity>of((type, level) -> new GenericGrenadeEntity(type, level), MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(8)
            .updateInterval(2)
            .build("entity_grenade_generic")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<AAShellEntity>> ENTITY_AA_SHELL = ENTITY_TYPES.register(
        "entity_aa_shell",
        () -> EntityType.Builder.<AAShellEntity>of(AAShellEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(8)
            .updateInterval(2)
            .build("entity_aa_shell")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<RocketEntity>> ENTITY_ROCKET = ENTITY_TYPES.register(
        "entity_rocket",
        () -> EntityType.Builder.<RocketEntity>of(RocketEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(8)
            .updateInterval(2)
            .build("entity_rocket")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<FallingNukeEntity>> ENTITY_FALLING_NUKE = ENTITY_TYPES.register(
        "entity_falling_nuke",
        () -> EntityType.Builder.<FallingNukeEntity>of(FallingNukeEntity::new, MobCategory.MISC)
            .sized(1.0F, 1.0F)
            .clientTrackingRange(16)
            .updateInterval(2)
            .build("entity_falling_nuke")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<ChopperMineEntity>> ENTITY_CHOPPER_MINE = ENTITY_TYPES.register(
        "entity_chopper_mine",
        () -> EntityType.Builder.<ChopperMineEntity>of(ChopperMineEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(8)
            .updateInterval(2)
            .build("entity_chopper_mine")
    );

    private HbmEntityTypes() {
    }
}
