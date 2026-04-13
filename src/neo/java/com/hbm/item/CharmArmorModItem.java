package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CharmArmorModItem extends ArmorModItem {
    private final float broadcastDamageMultiplier;
    private final List<String> extraLines;

    public CharmArmorModItem(Properties properties, float broadcastDamageMultiplier, List<String> extraLines) {
        super(properties, ArmorModSlot.HELMET_ONLY, false, true, false, false);
        this.broadcastDamageMultiplier = broadcastDamageMultiplier;
        this.extraLines = extraLines;
    }

    public float getBroadcastDamageMultiplier() {
        return broadcastDamageMultiplier;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("You feel blessed.").withStyle(ChatFormatting.AQUA));
        for (String line : extraLines) {
            tooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.AQUA));
        }
    }
}
