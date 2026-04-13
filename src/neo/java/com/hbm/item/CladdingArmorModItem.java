package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CladdingArmorModItem extends ArmorModItem {
    private final float radiationResistance;
    private final double knockbackResistance;
    private final List<Component> extraTooltipLines;

    public CladdingArmorModItem(
        Properties properties,
        float radiationResistance,
        double knockbackResistance,
        List<Component> extraTooltipLines
    ) {
        super(properties, ArmorModSlot.CLADDING, true, true, true, true);
        this.radiationResistance = radiationResistance;
        this.knockbackResistance = knockbackResistance;
        this.extraTooltipLines = List.copyOf(extraTooltipLines);
    }

    public float getRadiationResistance() {
        return radiationResistance;
    }

    public double getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        if (radiationResistance > 0.0F) {
            tooltipComponents.add(Component.literal("+" + radiationResistance + " rad-resistance").withStyle(ChatFormatting.YELLOW));
        }
        if (knockbackResistance > 0.0D) {
            tooltipComponents.add(Component.literal("+" + knockbackResistance + " knockback resistance").withStyle(ChatFormatting.WHITE));
        }
        tooltipComponents.addAll(extraTooltipLines);
    }
}
