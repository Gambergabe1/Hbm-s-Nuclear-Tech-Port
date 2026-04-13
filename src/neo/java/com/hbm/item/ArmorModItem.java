package com.hbm.item;

import java.util.List;

import com.hbm.armor.ArmorModSlot;
import com.hbm.armor.HbmArmorUtil;
import com.hbm.util.LegacyTooltipUtil;

import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.Level;

public class ArmorModItem extends Item {
    private final ArmorModSlot slot;
    private final boolean helmet;
    private final boolean chestplate;
    private final boolean leggings;
    private final boolean boots;

    public ArmorModItem(Properties properties, ArmorModSlot slot, boolean helmet, boolean chestplate, boolean leggings, boolean boots) {
        super(properties);
        this.slot = slot;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ArmorModSlot getSlot() {
        return slot;
    }

    public boolean isApplicableTo(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case HEAD -> helmet;
            case CHEST -> chestplate;
            case LEGS -> leggings;
            case FEET -> boots;
            default -> false;
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        appendSpecificTooltipLines(stack, tooltipComponents);
        if (!tooltipComponents.isEmpty()) {
            tooltipComponents.add(Component.empty());
        }
        LegacyTooltipUtil.appendLegacyDescription(this, stack, tooltipComponents);
        tooltipComponents.add(Component.translatable("desc.applicable").withStyle(ChatFormatting.DARK_PURPLE));
        if (helmet && chestplate && leggings && boots) {
            tooltipComponents.add(Component.literal("  ").append(Component.translatable("desc.applicableall")));
        } else {
            if (helmet) {
                tooltipComponents.add(Component.literal("  ").append(Component.translatable("desc.applicableh")));
            }
            if (chestplate) {
                tooltipComponents.add(Component.literal("  ").append(Component.translatable("desc.applicablec")));
            }
            if (leggings) {
                tooltipComponents.add(Component.literal("  ").append(Component.translatable("desc.applicablel")));
            }
            if (boots) {
                tooltipComponents.add(Component.literal("  ").append(Component.translatable("desc.applicableb")));
            }
        }
        tooltipComponents.add(Component.translatable("desc.applicableslot").withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.literal("  ").append(Component.translatable(slot.translationKey())));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || stack.isEmpty()) {
            return false;
        }

        ItemStack armorStack = slot.getItem();
        if (!HbmArmorUtil.canInstallArmorMod(armorStack, stack)) {
            return false;
        }

        ItemStack updatedArmor = armorStack.copy();
        ItemStack singleMod = stack.copy();
        singleMod.setCount(1);
        if (!HbmArmorUtil.installArmorMod(updatedArmor, singleMod)) {
            return false;
        }

        slot.set(updatedArmor);
        stack.shrink(1);
        player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 1.15F);
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        EquipmentSlot[] preferredSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        ItemStack selectedArmor = ItemStack.EMPTY;
        EquipmentSlot selectedSlot = null;

        for (EquipmentSlot equipmentSlot : preferredSlots) {
            ItemStack armorPiece = player.getItemBySlot(equipmentSlot);
            if (!HbmArmorUtil.canInstallArmorMod(armorPiece, stack)) {
                continue;
            }
            if (!selectedArmor.isEmpty()) {
                return InteractionResultHolder.pass(stack);
            }
            selectedArmor = armorPiece;
            selectedSlot = equipmentSlot;
        }

        if (selectedArmor.isEmpty() || selectedSlot == null) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide()) {
            ItemStack updatedArmor = selectedArmor.copy();
            ItemStack singleMod = stack.copy();
            singleMod.setCount(1);
            if (HbmArmorUtil.installArmorMod(updatedArmor, singleMod)) {
                player.setItemSlot(selectedSlot, updatedArmor);
                stack.shrink(1);
                player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 1.15F);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    protected void appendSpecificTooltipLines(ItemStack stack, List<Component> tooltipComponents) {
    }
}
