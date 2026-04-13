package com.hbm.item;

import java.util.List;

import com.hbm.armor.HbmArmorUtil;
import com.hbm.armor.HbmHazardClass;
import com.hbm.util.LegacyTooltipUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class GasMaskFilterItem extends Item {
    private static final String FILTER_DAMAGE_KEY = "Damage";

    private final int maxFilterDamage;
    private final List<HbmHazardClass> protectedHazards;

    public GasMaskFilterItem(Properties properties, int maxFilterDamage, List<HbmHazardClass> protectedHazards) {
        super(properties);
        this.maxFilterDamage = maxFilterDamage;
        this.protectedHazards = protectedHazards;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        LegacyTooltipUtil.appendLegacyDescription(this, stack, tooltipComponents);
        HbmArmorUtil.appendHazardListTooltip(
            tooltipComponents,
            Component.translatable("hazard.prot"),
            protectedHazards,
            ChatFormatting.GREEN
        );
        tooltipComponents.add(getDurabilityTooltip(stack).copy().withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || stack.isEmpty()) {
            return false;
        }

        ItemStack updatedArmor = slot.getItem().copy();
        ItemStack singleFilter = stack.copy();
        singleFilter.setCount(1);
        HbmArmorUtil.FilterInstallResult result = HbmArmorUtil.installFilterOnArmor(updatedArmor, singleFilter);
        if (!result.success()) {
            return false;
        }

        slot.set(updatedArmor);
        stack.shrink(1);
        HbmArmorUtil.giveBackToPlayer(player, result.replacedFilter());
        player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 1.0F);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty()) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide()) {
            ItemStack updatedHelmet = helmet.copy();
            ItemStack singleFilter = stack.copy();
            singleFilter.setCount(1);
            HbmArmorUtil.FilterInstallResult result = HbmArmorUtil.installFilterOnArmor(updatedHelmet, singleFilter);
            if (!result.success()) {
                return InteractionResultHolder.pass(stack);
            }

            player.setItemSlot(EquipmentSlot.HEAD, updatedHelmet);
            stack.shrink(1);
            HbmArmorUtil.giveBackToPlayer(player, result.replacedFilter());
            player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getFilterDamage(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int damage = getFilterDamage(stack);
        return Math.round(13.0F * Math.max(0.0F, (float) (maxFilterDamage - damage) / (float) maxFilterDamage));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x8AB800;
    }

    public int getFilterDamage(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(FILTER_DAMAGE_KEY);
    }

    public void setFilterDamage(ItemStack stack, int damage) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            if (damage <= 0) {
                tag.remove(FILTER_DAMAGE_KEY);
            } else {
                tag.putInt(FILTER_DAMAGE_KEY, Math.min(damage, maxFilterDamage));
            }
        });
    }

    public boolean damageFilter(ItemStack stack, int damage) {
        int updatedDamage = getFilterDamage(stack) + damage;
        setFilterDamage(stack, updatedDamage);
        return updatedDamage >= maxFilterDamage;
    }

    public Component getDurabilityTooltip(ItemStack stack) {
        int damage = getFilterDamage(stack);
        int remaining = Math.max(0, maxFilterDamage - damage);
        int percentage = maxFilterDamage <= 0 ? 0 : Math.round((remaining * 100.0F) / maxFilterDamage);
        return Component.literal(percentage + "% " + remaining + "/" + maxFilterDamage);
    }
}
