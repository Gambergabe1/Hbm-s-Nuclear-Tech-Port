package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ReviveArmorModItem extends ArmorModItem {
    private final List<Component> extraTooltipLines;

    public ReviveArmorModItem(Properties properties, List<Component> extraTooltipLines) {
        super(properties, ArmorModSlot.EXTRA, false, false, true, false);
        this.extraTooltipLines = List.copyOf(extraTooltipLines);
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.addAll(extraTooltipLines);
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(
            Component.literal((stack.getMaxDamage() - stack.getDamageValue()) + " revives left").withStyle(ChatFormatting.GOLD)
        );
    }
}
