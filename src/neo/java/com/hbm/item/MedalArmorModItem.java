package com.hbm.item;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MedalArmorModItem extends ArmorModItem {
    private final float radiationReductionPerTick;

    public MedalArmorModItem(Properties properties, float radiationReductionPerTick) {
        super(properties, ArmorModSlot.EXTRA, false, true, false, false);
        this.radiationReductionPerTick = radiationReductionPerTick;
    }

    public float getRadiationReductionPerTick() {
        return radiationReductionPerTick;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("-" + radiationReductionPerTick * 20.0F + " RAD/s").withStyle(ChatFormatting.GOLD));
        float halfLifeSeconds = 15.0F / radiationReductionPerTick;
        if (halfLifeSeconds < 60.0F) {
            tooltipComponents.add(Component.literal(Math.round(halfLifeSeconds) + "s Item Decontamination Halflife").withStyle(ChatFormatting.YELLOW));
        } else {
            tooltipComponents.add(Component.literal(Math.round(halfLifeSeconds / 60.0F) + "min Item Decontamination Halflife").withStyle(ChatFormatting.YELLOW));
        }
    }
}
