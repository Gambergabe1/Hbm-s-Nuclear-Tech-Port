package com.hbm.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class DurableTooltipItem extends Item {
    private final boolean templateFolderHint;

    public DurableTooltipItem(Properties properties, boolean templateFolderHint) {
        super(properties);
        this.templateFolderHint = templateFolderHint;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (templateFolderHint) {
            tooltipComponents.add(Component.translatable("info.templatefolder").withStyle(ChatFormatting.YELLOW));
        }

        if (stack.getMaxDamage() > 0 && stack.getDamageValue() == 0) {
            tooltipComponents.add(Component.literal("Durability: " + stack.getMaxDamage() + " / " + stack.getMaxDamage()));
        }
    }
}
