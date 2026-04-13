package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ServoArmorModItem extends ArmorModItem {
    private final double attackBonus;
    private final double speedBonus;
    private final int hasteAmplifier;
    private final int jumpAmplifier;

    public ServoArmorModItem(Properties properties, double attackBonus, double speedBonus, int hasteAmplifier, int jumpAmplifier) {
        super(properties, ArmorModSlot.SERVOS, false, true, true, false);
        this.attackBonus = attackBonus;
        this.speedBonus = speedBonus;
        this.hasteAmplifier = hasteAmplifier;
        this.jumpAmplifier = jumpAmplifier;
    }

    public double getAttackBonus() {
        return attackBonus;
    }

    public double getSpeedBonus() {
        return speedBonus;
    }

    public int getHasteAmplifier() {
        return hasteAmplifier;
    }

    public int getJumpAmplifier() {
        return jumpAmplifier;
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        tooltipComponents.add(Component.literal("Chestplate: Haste " + roman(hasteAmplifier + 1) + " / Damage +" + (int) (attackBonus * 100) + "%")
            .withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.literal("Leggings: Speed +" + (int) (speedBonus * 100) + "% / Jump " + roman(jumpAmplifier + 1))
            .withStyle(ChatFormatting.DARK_PURPLE));
    }

    private static String roman(int value) {
        return switch (value) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            default -> Integer.toString(value);
        };
    }
}
