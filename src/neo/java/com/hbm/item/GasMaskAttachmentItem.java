package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;
import com.hbm.armor.FilteredMaskItem;
import com.hbm.armor.HbmArmorUtil;
import com.hbm.armor.HbmHazardClass;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GasMaskAttachmentItem extends ArmorModItem implements FilteredMaskItem {
    private final List<HbmHazardClass> hazardBlacklist;
    private final boolean gasProtectionTooltip;

    public GasMaskAttachmentItem(Properties properties, List<HbmHazardClass> hazardBlacklist, boolean gasProtectionTooltip) {
        super(properties, ArmorModSlot.HELMET_ONLY, true, false, false, false);
        this.hazardBlacklist = hazardBlacklist;
        this.gasProtectionTooltip = gasProtectionTooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        HbmArmorUtil.appendGasMaskTooltip(stack, tooltipComponents);
        HbmArmorUtil.appendHazardListTooltip(
            tooltipComponents,
            Component.translatable("hazard.neverProtects"),
            hazardBlacklist,
            ChatFormatting.RED
        );
    }

    @Override
    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
        if (gasProtectionTooltip) {
            tooltipComponents.add(Component.literal("Gas protection").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                ItemStack removedFilter = HbmArmorUtil.removeGasMaskFilter(stack);
                if (!removedFilter.isEmpty()) {
                    HbmArmorUtil.giveBackToPlayer(player, removedFilter);
                    player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 0.85F);
                    return InteractionResultHolder.success(stack);
                }
            } else if (!HbmArmorUtil.getGasMaskFilter(stack).isEmpty()) {
                return InteractionResultHolder.success(stack);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public List<HbmHazardClass> getHazardBlacklist(ItemStack stack) {
        return hazardBlacklist;
    }

    @Override
    public boolean isFilterApplicable(ItemStack maskStack, ItemStack filterStack) {
        return filterStack.getItem() instanceof GasMaskFilterItem;
    }
}
