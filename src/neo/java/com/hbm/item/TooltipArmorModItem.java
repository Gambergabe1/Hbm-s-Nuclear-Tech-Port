package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TooltipArmorModItem extends ArmorModItem {
    private final List<Component> tooltipLines;

    public TooltipArmorModItem(
        Properties properties,
        ArmorModSlot slot,
        boolean helmet,
        boolean chestplate,
        boolean leggings,
        boolean boots,
        List<Component> tooltipLines
    ) {
        super(properties, slot, helmet, chestplate, leggings, boots);
        this.tooltipLines = List.copyOf(tooltipLines);
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.addAll(tooltipLines);
    }
}
