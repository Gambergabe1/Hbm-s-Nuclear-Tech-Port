package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HealthArmorModItem extends ArmorModItem {
    private final double healthBonus;
    private final List<Component> extraTooltipLines;

    public HealthArmorModItem(
        Properties properties,
        boolean helmet,
        boolean chestplate,
        boolean leggings,
        boolean boots,
        double healthBonus,
        List<Component> extraTooltipLines
    ) {
        super(properties, ArmorModSlot.EXTRA, helmet, chestplate, leggings, boots);
        this.healthBonus = healthBonus;
        this.extraTooltipLines = List.copyOf(extraTooltipLines);
    }

    public double getHealthBonus() {
        return healthBonus;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("+" + formatHearts(healthBonus) + " health").withStyle(ChatFormatting.RED));
        tooltipComponents.addAll(extraTooltipLines);
    }

    private static String formatHearts(double value) {
        double hearts = Math.round(value * 5.0D) / 10.0D / 2.0D;
        return Double.toString(hearts);
    }
}
