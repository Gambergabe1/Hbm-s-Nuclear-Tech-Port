package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PadsArmorModItem extends ArmorModItem {
    private final float fallDamageMultiplier;
    private final boolean staticCharge;

    public PadsArmorModItem(Properties properties, float fallDamageMultiplier, boolean staticCharge) {
        super(properties, ArmorModSlot.BOOTS_ONLY, false, false, false, true);
        this.fallDamageMultiplier = fallDamageMultiplier;
        this.staticCharge = staticCharge;
    }

    public float getFallDamageMultiplier() {
        return fallDamageMultiplier;
    }

    public boolean hasStaticCharge() {
        return staticCharge;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        if (fallDamageMultiplier != 1.0F) {
            tooltipComponents.add(
                Component.literal("-" + Math.round((1.0F - fallDamageMultiplier) * 100.0F) + "% fall damage").withStyle(ChatFormatting.RED)
            );
        }
        if (staticCharge) {
            tooltipComponents.add(Component.literal("Passively charges electric armor when walking").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
