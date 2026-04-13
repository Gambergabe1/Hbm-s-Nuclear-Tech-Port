package com.hbm.armor;

import java.util.ArrayList;
import java.util.List;

import com.hbm.item.ArmorModItem;
import com.hbm.item.BatteryArmorModItem;
import com.hbm.item.CharmArmorModItem;
import com.hbm.item.CladdingArmorModItem;
import com.hbm.item.GasMaskFilterItem;
import com.hbm.item.HealthArmorModItem;
import com.hbm.item.InsertArmorModItem;
import com.hbm.item.LodestoneArmorModItem;
import com.hbm.item.MedalArmorModItem;
import com.hbm.item.PadsArmorModItem;
import com.hbm.item.ServoArmorModItem;
import com.hbm.registry.HbmDataComponents;
import com.hbm.registry.HbmTags;
import com.hbm.util.HbmItemComponentUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class HbmArmorUtil {
    public static final String MOD_SLOT_KEY = "mod_slot_";
    public static final String FILTER_KEY = "hfrFilter";

    private HbmArmorUtil() {
    }

    public static boolean canInstallArmorMod(ItemStack armorStack, ItemStack modStack) {
        if (armorStack.isEmpty() || modStack.isEmpty()) {
            return false;
        }
        if (!(armorStack.getItem() instanceof ArmorItem armorItem)) {
            return false;
        }
        if (!(modStack.getItem() instanceof ArmorModItem armorModItem)) {
            return false;
        }
        if (!armorModItem.isApplicableTo(armorItem.getType().getSlot())) {
            return false;
        }

        return getInstalledArmorMod(armorStack, armorModItem.getSlot()).isEmpty();
    }

    public static boolean installArmorMod(ItemStack armorStack, ItemStack modStack) {
        if (!canInstallArmorMod(armorStack, modStack)) {
            return false;
        }

        ArmorModItem armorModItem = (ArmorModItem) modStack.getItem();
        setStoredModData(armorStack, armorModItem.getSlot(), encodeItemSnapshot(modStack));
        return true;
    }

    public static ItemStack getInstalledArmorMod(ItemStack armorStack, ArmorModSlot slot) {
        return decodeItemSnapshot(getStoredModData(armorStack, slot));
    }

    public static List<ItemStack> getInstalledArmorMods(ItemStack armorStack) {
        List<ItemStack> mods = new ArrayList<>();
        for (ArmorModSlot slot : ArmorModSlot.values()) {
            ItemStack mod = getInstalledArmorMod(armorStack, slot);
            if (!mod.isEmpty()) {
                mods.add(mod);
            }
        }
        return mods;
    }

    public static boolean hasArmorMods(ItemStack armorStack) {
        return !getInstalledArmorMods(armorStack).isEmpty();
    }

    public static ItemStack popLastArmorMod(ItemStack armorStack) {
        ArmorModSlot[] slots = ArmorModSlot.values();
        for (int index = slots.length - 1; index >= 0; index--) {
            ItemStack removed = removeArmorMod(armorStack, slots[index]);
            if (!removed.isEmpty()) {
                return removed;
            }
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack removeArmorMod(ItemStack armorStack, ArmorModSlot slot) {
        ItemStack removed = getInstalledArmorMod(armorStack, slot);
        if (!removed.isEmpty()) {
            setStoredModData(armorStack, slot, null);
        }
        return removed;
    }

    public static void setInstalledArmorMod(ItemStack armorStack, ArmorModSlot slot, ItemStack modStack) {
        if (modStack == null || modStack.isEmpty()) {
            setStoredModData(armorStack, slot, null);
            return;
        }

        setStoredModData(armorStack, slot, encodeItemSnapshot(modStack));
    }

    public static ItemStack getGasMaskFilter(ItemStack maskStack) {
        CompoundTag tag = maskStack.get(HbmDataComponents.GAS_MASK_FILTER);
        return decodeItemSnapshot(tag);
    }

    public static void setGasMaskFilter(ItemStack maskStack, ItemStack filterStack) {
        setEncodedFilter(maskStack, filterStack.isEmpty() ? null : encodeItemSnapshot(filterStack));
    }

    public static ItemStack removeGasMaskFilter(ItemStack maskStack) {
        ItemStack removed = getGasMaskFilter(maskStack);
        if (!removed.isEmpty()) {
            setEncodedFilter(maskStack, null);
        }
        return removed;
    }

    public static ItemStack getGasMaskFilterRecursively(ItemStack armorStack) {
        ItemStack directFilter = getGasMaskFilter(armorStack);
        if (!directFilter.isEmpty()) {
            return directFilter;
        }

        ItemStack attachment = getInstalledArmorMod(armorStack, ArmorModSlot.HELMET_ONLY);
        if (attachment.getItem() instanceof FilteredMaskItem) {
            return getGasMaskFilter(attachment);
        }

        return ItemStack.EMPTY;
    }

    public static FilterInstallResult installFilterOnArmor(ItemStack armorStack, ItemStack filterStack) {
        if (filterStack.isEmpty() || !(filterStack.getItem() instanceof GasMaskFilterItem)) {
            return FilterInstallResult.failure();
        }

        if (armorStack.getItem() instanceof FilteredMaskItem filteredMaskItem) {
            return swapFilter(armorStack, filterStack, filteredMaskItem);
        }

        ItemStack attachment = getInstalledArmorMod(armorStack, ArmorModSlot.HELMET_ONLY);
        if (attachment.getItem() instanceof FilteredMaskItem filteredMaskItem) {
            FilterInstallResult result = swapFilter(attachment, filterStack, filteredMaskItem);
            if (result.success()) {
                setStoredModData(armorStack, ArmorModSlot.HELMET_ONLY, encodeItemSnapshot(attachment));
            }
            return result;
        }

        return FilterInstallResult.failure();
    }

    public static void damageGasMaskFilter(LivingEntity entity, int damage) {
        if (damage <= 0) {
            return;
        }

        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty()) {
            return;
        }

        if (helmet.getItem() instanceof FilteredMaskItem) {
            damageFilterCarrier(helmet, damage);
            return;
        }

        ItemStack attachment = getInstalledArmorMod(helmet, ArmorModSlot.HELMET_ONLY);
        if (attachment.getItem() instanceof FilteredMaskItem) {
            damageFilterCarrier(attachment, damage);
            setStoredModData(helmet, ArmorModSlot.HELMET_ONLY, encodeItemSnapshot(attachment));
        }
    }

    public static float getRadiationResistance(LivingEntity livingEntity) {
        float resistance = 0.0F;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            if (armorPiece.getItem() instanceof RadiationResistanceProvider provider) {
                resistance += provider.getRadiationResistance(armorPiece);
            }
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof CladdingArmorModItem claddingArmorModItem) {
                    resistance += claddingArmorModItem.getRadiationResistance();
                }
            }
        }

        return resistance;
    }

    public static double getHealthBonus(LivingEntity livingEntity) {
        double bonus = 0.0D;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof HealthArmorModItem healthArmorModItem) {
                    bonus += healthArmorModItem.getHealthBonus();
                }
            }
        }

        return bonus;
    }

    public static double getKnockbackResistanceBonus(LivingEntity livingEntity) {
        double bonus = 0.0D;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof CladdingArmorModItem claddingArmorModItem) {
                    bonus += claddingArmorModItem.getKnockbackResistance();
                }
            }
        }

        return bonus;
    }

    public static float getFallDamageMultiplier(LivingEntity livingEntity) {
        float multiplier = 1.0F;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            ItemStack bootsMod = getInstalledArmorMod(armorPiece, ArmorModSlot.BOOTS_ONLY);
            if (bootsMod.getItem() instanceof PadsArmorModItem padsArmorModItem) {
                multiplier = Math.min(multiplier, padsArmorModItem.getFallDamageMultiplier());
            }
        }

        return multiplier;
    }

    public static double getInsertSpeedModifier(LivingEntity livingEntity) {
        double modifier = 0.0D;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            ItemStack insert = getInstalledArmorMod(armorPiece, ArmorModSlot.KEVLAR);
            if (insert.getItem() instanceof InsertArmorModItem insertArmorModItem) {
                modifier = Math.min(modifier, insertArmorModItem.getSpeedMultiplier() - 1.0D);
            }
        }

        return modifier;
    }

    public static int getLodestoneRange(LivingEntity livingEntity) {
        int range = 0;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof LodestoneArmorModItem lodestoneArmorModItem) {
                    range = Math.max(range, lodestoneArmorModItem.getRange());
                }
            }
        }

        return range;
    }

    public static float getRadiationReductionPerTick(LivingEntity livingEntity) {
        float reduction = 0.0F;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof MedalArmorModItem medalArmorModItem) {
                    reduction += medalArmorModItem.getRadiationReductionPerTick();
                }
            }
        }

        return reduction;
    }

    public static double getBatteryMultiplier(LivingEntity livingEntity) {
        double multiplier = 1.0D;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof BatteryArmorModItem batteryArmorModItem) {
                    multiplier = Math.max(multiplier, batteryArmorModItem.getMultiplier());
                }
            }
        }

        return multiplier;
    }

    public static ServoStats getServoStats(LivingEntity livingEntity) {
        double attackBonus = 0.0D;
        double speedBonus = 0.0D;
        int hasteAmplifier = -1;
        int jumpAmplifier = -1;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            if (!(armorPiece.getItem() instanceof ArmorItem armorItem)) {
                continue;
            }

            ItemStack servoMod = getInstalledArmorMod(armorPiece, ArmorModSlot.SERVOS);
            if (!(servoMod.getItem() instanceof ServoArmorModItem servoArmorModItem)) {
                continue;
            }

            if (armorItem.getType().getSlot() == EquipmentSlot.CHEST) {
                attackBonus = Math.max(attackBonus, servoArmorModItem.getAttackBonus());
                hasteAmplifier = Math.max(hasteAmplifier, servoArmorModItem.getHasteAmplifier());
            } else if (armorItem.getType().getSlot() == EquipmentSlot.LEGS) {
                speedBonus = Math.max(speedBonus, servoArmorModItem.getSpeedBonus());
                jumpAmplifier = Math.max(jumpAmplifier, servoArmorModItem.getJumpAmplifier());
            }
        }

        return new ServoStats(attackBonus, speedBonus, hasteAmplifier, jumpAmplifier);
    }

    public static float getBroadcastDamageMultiplier(LivingEntity livingEntity) {
        float multiplier = 1.0F;

        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.getItem() instanceof CharmArmorModItem charmArmorModItem) {
                    multiplier = Math.min(multiplier, charmArmorModItem.getBroadcastDamageMultiplier());
                }
            }
        }

        return multiplier;
    }

    public static boolean hasInstalledMod(LivingEntity livingEntity, Item item) {
        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            for (ItemStack mod : getInstalledArmorMods(armorPiece)) {
                if (mod.is(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasMonoMask(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.isEmpty()) {
            return false;
        }

        if (HbmTags.itemMatches(helmet, HbmTags.monoMasks())) {
            return true;
        }

        ItemStack attachment = getInstalledArmorMod(helmet, ArmorModSlot.HELMET_ONLY);
        return HbmTags.itemMatches(attachment, HbmTags.monoMaskAttachments());
    }

    public static void appendInstalledModsTooltip(ItemStack armorStack, List<Component> tooltipComponents) {
        List<ItemStack> mods = getInstalledArmorMods(armorStack);
        if (mods.isEmpty()) {
            return;
        }

        tooltipComponents.add(Component.translatable("desc.applicableslot").withStyle(ChatFormatting.DARK_PURPLE));
        for (ItemStack mod : mods) {
            tooltipComponents.add(Component.literal("  ").append(mod.getHoverName()).withStyle(ChatFormatting.DARK_PURPLE));
            if (mod.getItem() instanceof FilteredMaskItem) {
                appendGasMaskTooltip(mod, tooltipComponents, "    ");
            }
        }
    }

    public static void appendGasMaskTooltip(ItemStack maskStack, List<Component> tooltipComponents) {
        appendGasMaskTooltip(maskStack, tooltipComponents, "");
    }

    public static void appendGasMaskTooltip(ItemStack maskStack, List<Component> tooltipComponents, String indent) {
        ItemStack filter = getGasMaskFilter(maskStack);
        if (filter.isEmpty()) {
            tooltipComponents.add(Component.literal(indent).append(Component.translatable("desc.nofilter")).withStyle(ChatFormatting.RED));
            return;
        }

        tooltipComponents.add(Component.literal(indent).append(Component.translatable("desc.infilter")).withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.literal(indent + "  ").append(filter.getHoverName()).withStyle(ChatFormatting.YELLOW));

        if (filter.getItem() instanceof GasMaskFilterItem gasMaskFilterItem) {
            tooltipComponents.add(Component.literal(indent + "  ").append(gasMaskFilterItem.getDurabilityTooltip(filter)).withStyle(ChatFormatting.YELLOW));
        }
    }

    public static void appendHazardListTooltip(List<Component> tooltipComponents, Component title, List<HbmHazardClass> hazardClasses, ChatFormatting color) {
        if (hazardClasses.isEmpty()) {
            return;
        }

        tooltipComponents.add(title.copy().withStyle(color));
        for (HbmHazardClass hazardClass : hazardClasses) {
            tooltipComponents.add(Component.literal("  - ").append(Component.translatable(hazardClass.translationKey())).withStyle(color));
        }
    }

    public static void giveBackToPlayer(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private static FilterInstallResult swapFilter(ItemStack maskStack, ItemStack filterStack, FilteredMaskItem filteredMaskItem) {
        if (!filteredMaskItem.isFilterApplicable(maskStack, filterStack)) {
            return FilterInstallResult.failure();
        }

        ItemStack previousFilter = getGasMaskFilter(maskStack);
        setGasMaskFilter(maskStack, filterStack);
        return new FilterInstallResult(true, previousFilter);
    }

    private static void damageFilterCarrier(ItemStack carrierStack, int damage) {
        ItemStack filter = getGasMaskFilter(carrierStack);
        if (!(filter.getItem() instanceof GasMaskFilterItem gasMaskFilterItem)) {
            return;
        }

        if (gasMaskFilterItem.damageFilter(filter, damage)) {
            removeGasMaskFilter(carrierStack);
        } else {
            setGasMaskFilter(carrierStack, filter);
        }
    }

    private static CompoundTag encodeItemSnapshot(ItemStack stack) {
        CompoundTag tag = HbmItemComponentUtil.encodeRegistryEntry(stack);
        if (tag.isEmpty()) {
            return tag;
        }

        if (stack.getItem() instanceof GasMaskFilterItem gasMaskFilterItem) {
            int filterDamage = gasMaskFilterItem.getFilterDamage(stack);
            HbmItemComponentUtil.putDamage(tag, filterDamage);
        } else {
            HbmItemComponentUtil.putDamage(tag, stack.getDamageValue());
        }

        if (stack.getItem() instanceof FilteredMaskItem) {
            ItemStack filter = getGasMaskFilter(stack);
            if (!filter.isEmpty()) {
                tag.put(FILTER_KEY, encodeItemSnapshot(filter));
            }
        }

        return tag;
    }

    private static ItemStack decodeItemSnapshot(CompoundTag tag) {
        ItemStack stack = HbmItemComponentUtil.decodeRegistryEntry(tag);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (stack.getItem() instanceof GasMaskFilterItem gasMaskFilterItem && HbmItemComponentUtil.hasDamage(tag)) {
            gasMaskFilterItem.setFilterDamage(stack, HbmItemComponentUtil.getDamage(tag));
        } else if (HbmItemComponentUtil.hasDamage(tag)) {
            stack.setDamageValue(HbmItemComponentUtil.getDamage(tag));
        }
        if (stack.getItem() instanceof FilteredMaskItem && tag.contains(FILTER_KEY, Tag.TAG_COMPOUND)) {
            setEncodedFilter(stack, tag.getCompound(FILTER_KEY));
        }
        return stack;
    }

    private static CompoundTag getStoredModData(ItemStack armorStack, ArmorModSlot slot) {
        CompoundTag mods = HbmItemComponentUtil.getCompoundComponentCopy(armorStack, HbmDataComponents.ARMOR_MODS.get());
        String slotKey = MOD_SLOT_KEY + slot.legacyIndex();
        if (!mods.contains(slotKey, Tag.TAG_COMPOUND)) {
            return new CompoundTag();
        }

        return mods.getCompound(slotKey).copy();
    }

    private static void setStoredModData(ItemStack armorStack, ArmorModSlot slot, CompoundTag modData) {
        CompoundTag mods = HbmItemComponentUtil.getCompoundComponentCopy(armorStack, HbmDataComponents.ARMOR_MODS.get());
        String slotKey = MOD_SLOT_KEY + slot.legacyIndex();

        if (modData == null || modData.isEmpty()) {
            mods.remove(slotKey);
        } else {
            mods.put(slotKey, modData.copy());
        }

        HbmItemComponentUtil.setCompoundComponent(armorStack, HbmDataComponents.ARMOR_MODS.get(), mods);
    }

    private static void setEncodedFilter(ItemStack maskStack, CompoundTag filterData) {
        HbmItemComponentUtil.setCompoundComponent(maskStack, HbmDataComponents.GAS_MASK_FILTER.get(), filterData);
    }

    public record FilterInstallResult(boolean success, ItemStack replacedFilter) {
        public static FilterInstallResult failure() {
            return new FilterInstallResult(false, ItemStack.EMPTY);
        }
    }

    public record ServoStats(double attackBonus, double speedBonus, int hasteAmplifier, int jumpAmplifier) {
    }
}
