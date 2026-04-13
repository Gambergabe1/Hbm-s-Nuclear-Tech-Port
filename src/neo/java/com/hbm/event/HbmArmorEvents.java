package com.hbm.event;

import java.util.ArrayList;
import java.util.List;

import com.hbm.armor.ArmorModSlot;
import com.hbm.armor.HbmArmorUtil;
import com.hbm.armor.HbmArmorUtil.ServoStats;
import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.item.InsertArmorModItem;
import com.hbm.item.ReviveArmorModItem;
import com.hbm.registry.HbmItems;
import com.hbm.registry.HbmMobEffects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class HbmArmorEvents {
    private static final ResourceLocation SERVO_ATTACK_ID = ResourceLocation.fromNamespaceAndPath("hbm", "armor_servo_attack");
    private static final ResourceLocation SERVO_SPEED_ID = ResourceLocation.fromNamespaceAndPath("hbm", "armor_servo_speed");
    private static final ResourceLocation INSERT_SPEED_ID = ResourceLocation.fromNamespaceAndPath("hbm", "armor_insert_speed");
    private static final ResourceLocation HEALTH_ID = ResourceLocation.fromNamespaceAndPath("hbm", "armor_extra_health");
    private static final ResourceLocation KNOCKBACK_ID = ResourceLocation.fromNamespaceAndPath("hbm", "armor_cladding_knockback");
    private static final EquipmentSlot[] ARMOR_SLOTS = {
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET
    };

    private HbmArmorEvents() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        ServoStats servoStats = HbmArmorUtil.getServoStats(player);
        updateTransientModifier(
            player.getAttribute(Attributes.ATTACK_DAMAGE),
            SERVO_ATTACK_ID,
            servoStats.attackBonus(),
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        updateTransientModifier(
            player.getAttribute(Attributes.MOVEMENT_SPEED),
            SERVO_SPEED_ID,
            servoStats.speedBonus(),
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        updateTransientModifier(
            player.getAttribute(Attributes.MOVEMENT_SPEED),
            INSERT_SPEED_ID,
            HbmArmorUtil.getInsertSpeedModifier(player),
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        updateTransientModifier(
            player.getAttribute(Attributes.MAX_HEALTH),
            HEALTH_ID,
            HbmArmorUtil.getHealthBonus(player),
            AttributeModifier.Operation.ADD_VALUE
        );
        updateTransientModifier(
            player.getAttribute(Attributes.KNOCKBACK_RESISTANCE),
            KNOCKBACK_ID,
            HbmArmorUtil.getKnockbackResistanceBonus(player),
            AttributeModifier.Operation.ADD_VALUE
        );

        if (servoStats.hasteAmplifier() >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 65, servoStats.hasteAmplifier(), false, false, true));
        }
        if (servoStats.jumpAmplifier() >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 65, servoStats.jumpAmplifier(), false, false, true));
        }

        float radReduction = HbmArmorUtil.getRadiationReductionPerTick(player);
        if (radReduction > 0.0F) {
            HbmAttachmentAccess.living(player).decreaseRads(radReduction);
        }

        if (HbmArmorUtil.hasInstalledMod(player, HbmItems.SERUM.get()) && player.hasEffect(MobEffects.POISON)) {
            player.removeEffect(MobEffects.POISON);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 4, false, false, true));
        }
        if (HbmArmorUtil.hasInstalledMod(player, HbmItems.MORNING_GLORY.get()) && player.hasEffect(MobEffects.WITHER)) {
            player.removeEffect(MobEffects.WITHER);
        }
        if (HbmArmorUtil.hasInstalledMod(player, HbmItems.SPIDER_MILK.get())) {
            clearHarmfulEffects(player);
        }

        int lodestoneRange = HbmArmorUtil.getLodestoneRange(player);
        if (lodestoneRange > 0) {
            attractNearbyItems(player, lodestoneRange);
        }

        tryTriggerAutoInjector(player);
    }

    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.level().isClientSide()) {
            return;
        }

        float amount = event.getAmount();

        if ("broadcast".equals(event.getSource().getMsgId())) {
            float multiplier = HbmArmorUtil.getBroadcastDamageMultiplier(livingEntity);
            if (multiplier <= 0.0F) {
                amount = 0.0F;
            } else if (multiplier < 1.0F) {
                amount *= multiplier;
            }
        }

        if (event.getSource().is(DamageTypeTags.IS_FALL)) {
            amount *= HbmArmorUtil.getFallDamageMultiplier(livingEntity);
        }

        for (EquipmentSlot equipmentSlot : ARMOR_SLOTS) {
            ItemStack armorPiece = livingEntity.getItemBySlot(equipmentSlot);
            if (armorPiece.isEmpty()) {
                continue;
            }

            ItemStack insert = HbmArmorUtil.getInstalledArmorMod(armorPiece, ArmorModSlot.KEVLAR);
            if (insert.getItem() instanceof InsertArmorModItem insertArmorModItem) {
                amount *= insertArmorModItem.getDamageMultiplier();
                if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                    amount *= insertArmorModItem.getProjectileMultiplier();
                }
                if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                    amount *= insertArmorModItem.getExplosionMultiplier();
                }
                damageInstalledMod(armorPiece, ArmorModSlot.KEVLAR, insert, 1);
            }

            ItemStack extra = HbmArmorUtil.getInstalledArmorMod(armorPiece, ArmorModSlot.EXTRA);
            if (extra.isEmpty()) {
                continue;
            }

            if (extra.is(HbmItems.ARMOR_POLISH.get()) && livingEntity.getRandom().nextInt(20) == 0) {
                amount = 0.0F;
            }
            if (extra.is(HbmItems.BANDAID.get()) && livingEntity.getRandom().nextInt(100) < 3) {
                amount = 0.0F;
                livingEntity.heal(livingEntity.getMaxHealth());
            }
            if (extra.is(HbmItems.INK.get()) && livingEntity.getRandom().nextInt(10) == 0) {
                amount = 0.0F;
                dropInkFlowers(livingEntity);
            }
            if (extra.is(HbmItems.QUARTZ_PLUTONIUM.get())) {
                HbmAttachmentAccess.living(livingEntity).decreaseRads(10.0F);
            }
            if (extra.is(HbmItems.MORNING_GLORY.get()) && livingEntity.getRandom().nextInt(20) == 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 4, false, false, true));
            }
            if (extra.is(HbmItems.WD40.get()) && armorPiece.isDamaged() && livingEntity.getRandom().nextInt(5) != 0) {
                armorPiece.setDamageValue(Math.max(0, armorPiece.getDamageValue() - 1));
            }

            applyBathwaterRetaliation(extra, event, livingEntity);
        }

        event.setAmount(amount);
    }

    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.level().isClientSide()) {
            return;
        }

        for (EquipmentSlot equipmentSlot : ARMOR_SLOTS) {
            ItemStack armorPiece = livingEntity.getItemBySlot(equipmentSlot);
            if (armorPiece.isEmpty()) {
                continue;
            }

            ItemStack extra = HbmArmorUtil.getInstalledArmorMod(armorPiece, ArmorModSlot.EXTRA);
            if (extra.getItem() instanceof ReviveArmorModItem) {
                damageInstalledMod(armorPiece, ArmorModSlot.EXTRA, extra, 1);
                livingEntity.setHealth(livingEntity.getMaxHealth());
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 99, false, false, true));
                event.setCanceled(true);
                return;
            }

            if (extra.is(HbmItems.SHACKLES.get()) && HbmAttachmentAccess.living(livingEntity).getRads() < 1000.0F) {
                livingEntity.setHealth(livingEntity.getMaxHealth());
                HbmAttachmentAccess.living(livingEntity).increaseRads(100.0F);
                event.setCanceled(true);
                return;
            }
        }
    }

    private static void updateTransientModifier(
        AttributeInstance attributeInstance,
        ResourceLocation modifierId,
        double amount,
        AttributeModifier.Operation operation
    ) {
        if (attributeInstance == null) {
            return;
        }
        if (amount == 0.0D) {
            attributeInstance.removeModifier(modifierId);
            return;
        }

        attributeInstance.addOrUpdateTransientModifier(
            new AttributeModifier(modifierId, amount, operation)
        );
    }

    private static void tryTriggerAutoInjector(Player player) {
        if (HbmAttachmentAccess.living(player).getDigamma() < 5.0F) {
            return;
        }

        for (EquipmentSlot equipmentSlot : ARMOR_SLOTS) {
            ItemStack armorPiece = player.getItemBySlot(equipmentSlot);
            if (armorPiece.isEmpty()) {
                continue;
            }

            ItemStack extra = HbmArmorUtil.getInstalledArmorMod(armorPiece, ArmorModSlot.EXTRA);
            if (!extra.is(HbmItems.INJECTOR_5HTP.get())) {
                continue;
            }

            HbmArmorUtil.removeArmorMod(armorPiece, ArmorModSlot.EXTRA);
            HbmAttachmentAccess.living(player).decreaseDigamma(5.0F);
            player.addEffect(new MobEffectInstance(HbmMobEffects.STABILITY, 60 * 20, 0, false, false, true));
            player.heal(20.0F);
            player.playSound(SoundEvents.HONEY_DRINK, 1.0F, 1.0F);
            break;
        }
    }

    private static void attractNearbyItems(Player player, int range) {
        AABB bounds = player.getBoundingBox().inflate(range, range, range);
        Vec3 origin = player.position().add(0.0D, player.getBbHeight() * 0.5D, 0.0D);

        for (ItemEntity itemEntity : player.level().getEntitiesOfClass(ItemEntity.class, bounds)) {
            Vec3 direction = origin.subtract(itemEntity.position());
            if (direction.lengthSqr() < 1.0E-4D) {
                continue;
            }

            Vec3 motion = direction.normalize().scale(0.05D);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(motion));
            if (motion.y > 0.0D && itemEntity.getDeltaMovement().y < 0.04D) {
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(0.0D, 0.2D, 0.0D));
            }
        }
    }

    private static void clearHarmfulEffects(LivingEntity livingEntity) {
        List<net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect>> harmfulEffects = new ArrayList<>();
        for (MobEffectInstance effectInstance : livingEntity.getActiveEffects()) {
            if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                harmfulEffects.add(effectInstance.getEffect());
            }
        }
        for (var effect : harmfulEffects) {
            livingEntity.removeEffect(effect);
        }
    }

    private static void applyBathwaterRetaliation(ItemStack extra, LivingIncomingDamageEvent event, LivingEntity livingEntity) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (extra.is(HbmItems.BATHWATER.get())) {
            attacker.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2, false, false, true));
        } else if (extra.is(HbmItems.BATHWATER_MK2.get())) {
            attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 4, false, false, true));
        } else if (extra.is(HbmItems.BATHWATER_MK3.get())) {
            HbmAttachmentAccess.living(attacker).increaseRads(100.0F);
        }
    }

    private static void dropInkFlowers(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide()) {
            return;
        }

        if (livingEntity.getRandom().nextInt(10) == 0) {
            livingEntity.spawnAtLocation(Items.DANDELION);
        }
        livingEntity.spawnAtLocation(Items.POPPY);
    }

    private static void damageInstalledMod(ItemStack armorPiece, ArmorModSlot slot, ItemStack modStack, int amount) {
        if (!modStack.isDamageableItem() || amount <= 0) {
            return;
        }

        modStack.setDamageValue(modStack.getDamageValue() + amount);
        if (modStack.getDamageValue() >= modStack.getMaxDamage()) {
            HbmArmorUtil.removeArmorMod(armorPiece, slot);
        } else {
            HbmArmorUtil.setInstalledArmorMod(armorPiece, slot, modStack);
        }
    }
}
