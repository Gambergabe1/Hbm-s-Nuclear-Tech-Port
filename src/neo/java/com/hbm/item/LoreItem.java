package com.hbm.item;

import java.util.List;

import com.hbm.util.LegacyTooltipUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class LoreItem extends Item {
    public LoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        LegacyTooltipUtil.appendLegacyDescription(this, stack, tooltipComponents);
    }
}
