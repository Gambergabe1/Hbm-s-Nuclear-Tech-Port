package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class BatteryArmorModItem extends ArmorModItem {
    private final double multiplier;

    public BatteryArmorModItem(Properties properties, double multiplier) {
        super(properties, ArmorModSlot.BATTERY, true, true, true, true);
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("Armor Battery Multiplier: " + multiplier + "x").withStyle(ChatFormatting.AQUA));
    }
}
