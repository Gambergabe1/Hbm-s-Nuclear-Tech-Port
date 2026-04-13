package com.hbm.item;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LodestoneArmorModItem extends ArmorModItem {
    private final int range;

    public LodestoneArmorModItem(Properties properties, int range) {
        super(properties, ArmorModSlot.EXTRA, true, true, true, true);
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("Attracts nearby items").withStyle(ChatFormatting.DARK_GRAY));
        tooltipComponents.add(Component.literal("Item attraction range: " + range).withStyle(ChatFormatting.DARK_GRAY));
    }
}
