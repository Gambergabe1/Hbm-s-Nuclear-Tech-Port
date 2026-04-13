package com.hbm.item;

import java.util.ArrayList;
import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class InsertArmorModItem extends ArmorModItem {
    private final float damageMultiplier;
    private final float projectileMultiplier;
    private final float explosionMultiplier;
    private final float speedMultiplier;

    public InsertArmorModItem(
        Properties properties,
        float damageMultiplier,
        float projectileMultiplier,
        float explosionMultiplier,
        float speedMultiplier
    ) {
        super(properties, ArmorModSlot.KEVLAR, false, true, false, false);
        this.damageMultiplier = damageMultiplier;
        this.projectileMultiplier = projectileMultiplier;
        this.explosionMultiplier = explosionMultiplier;
        this.speedMultiplier = speedMultiplier;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getProjectileMultiplier() {
        return projectileMultiplier;
    }

    public float getExplosionMultiplier() {
        return explosionMultiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        if (damageMultiplier != 1.0F) {
            tooltipComponents.add(formatPercentLine(damageMultiplier, "Damage", ChatFormatting.RED));
        }
        if (projectileMultiplier != 1.0F) {
            tooltipComponents.add(formatPercentLine(projectileMultiplier, "Projectile Damage", ChatFormatting.YELLOW));
        }
        if (explosionMultiplier != 1.0F) {
            tooltipComponents.add(formatPercentLine(explosionMultiplier, "Explosion Damage", ChatFormatting.YELLOW));
        }
        if (speedMultiplier != 1.0F) {
            tooltipComponents.add(formatPercentLine(speedMultiplier, "Speed", ChatFormatting.BLUE));
        }
        tooltipComponents.add(
            Component.literal("Durability: " + (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage())
        );
    }

    public String shortSummary(ItemStack stack) {
        List<String> parts = new ArrayList<>();
        if (damageMultiplier != 1.0F) {
            parts.add(formatCompactPercent(damageMultiplier, "dmg"));
        }
        if (projectileMultiplier != 1.0F) {
            parts.add(formatCompactPercent(projectileMultiplier, "proj"));
        }
        if (explosionMultiplier != 1.0F) {
            parts.add(formatCompactPercent(explosionMultiplier, "exp"));
        }
        if (speedMultiplier != 1.0F) {
            parts.add(formatCompactPercent(speedMultiplier, "speed"));
        }
        return String.join(" / ", parts) + " / " + (stack.getMaxDamage() - stack.getDamageValue()) + "HP";
    }

    private static Component formatPercentLine(float multiplier, String label, ChatFormatting formatting) {
        int percent = Math.round(Math.abs((1.0F - multiplier) * 100.0F));
        String sign = multiplier < 1.0F ? "-" : "+";
        return Component.literal(sign + percent + "% " + label).withStyle(formatting);
    }

    private static String formatCompactPercent(float multiplier, String suffix) {
        int percent = Math.round(Math.abs((1.0F - multiplier) * 100.0F));
        String sign = multiplier < 1.0F ? "-" : "+";
        return sign + percent + "% " + suffix;
    }
}
