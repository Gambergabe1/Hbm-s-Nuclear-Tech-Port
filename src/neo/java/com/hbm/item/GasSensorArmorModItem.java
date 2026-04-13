package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GasSensorArmorModItem extends ArmorModItem {
    public GasSensorArmorModItem(Properties properties) {
        super(properties, ArmorModSlot.EXTRA, true, true, true, true);
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("Detects gas in 5x3x5 around the player").withStyle(ChatFormatting.YELLOW));
    }
}
