package com.hbm.entity;

import com.hbm.entity.mob.CyberCrabEntity;
import com.hbm.entity.mob.GlowingOneEntity;
import com.hbm.entity.mob.NuclearCreeperEntity;
import com.hbm.entity.mob.TaintedCreeperEntity;
import com.hbm.registry.HbmEntityTypes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public final class HbmEntityEvents {
    private HbmEntityEvents() {
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        register(event, HbmEntityTypes.ENTITY_GLOWING_ONE.get(), GlowingOneEntity.createAttributes());
        register(event, HbmEntityTypes.ENTITY_NUCLEAR_CREEPER.get(), NuclearCreeperEntity.createAttributes());
        register(event, HbmEntityTypes.ENTITY_TAINTED_CREEPER.get(), TaintedCreeperEntity.createAttributes());
        register(event, HbmEntityTypes.ENTITY_CYBER_CRAB.get(), CyberCrabEntity.createAttributes());
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
            HbmEntityTypes.ENTITY_GLOWING_ONE.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkAnyLightMonsterSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
            HbmEntityTypes.ENTITY_NUCLEAR_CREEPER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
            HbmEntityTypes.ENTITY_TAINTED_CREEPER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
            HbmEntityTypes.ENTITY_CYBER_CRAB.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    private static void register(
        EntityAttributeCreationEvent event,
        EntityType<? extends LivingEntity> entityType,
        AttributeSupplier.Builder attributes
    ) {
        event.put(entityType, attributes.build());
    }
}
