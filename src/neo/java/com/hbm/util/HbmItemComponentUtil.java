package com.hbm.util;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HbmItemComponentUtil {
    public static final String STACK_ID_KEY = "id";
    public static final String STACK_DAMAGE_KEY = "Damage";

    private HbmItemComponentUtil() {
    }

    public static CompoundTag getCompoundComponentCopy(ItemStack stack, DataComponentType<CompoundTag> componentType) {
        CompoundTag data = stack.get(componentType);
        return data == null ? new CompoundTag() : data.copy();
    }

    public static void setCompoundComponent(ItemStack stack, DataComponentType<CompoundTag> componentType, CompoundTag data) {
        if (data == null || data.isEmpty()) {
            stack.remove(componentType);
        } else {
            stack.set(componentType, data.copy());
        }
    }

    public static CompoundTag encodeRegistryEntry(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        if (stack.isEmpty() || stack.is(Items.AIR)) {
            return tag;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null) {
            return tag;
        }

        tag.putString(STACK_ID_KEY, itemId.toString());
        return tag;
    }

    public static ItemStack decodeRegistryEntry(CompoundTag tag) {
        if (tag == null || tag.isEmpty() || !tag.contains(STACK_ID_KEY, Tag.TAG_STRING)) {
            return ItemStack.EMPTY;
        }

        ResourceLocation itemId = ResourceLocation.tryParse(tag.getString(STACK_ID_KEY));
        if (itemId == null) {
            return ItemStack.EMPTY;
        }

        Item item = BuiltInRegistries.ITEM.get(itemId);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }

    public static void putDamage(CompoundTag tag, int damage) {
        if (tag == null || damage <= 0) {
            return;
        }
        tag.putInt(STACK_DAMAGE_KEY, damage);
    }

    public static boolean hasDamage(CompoundTag tag) {
        return tag != null && tag.contains(STACK_DAMAGE_KEY, Tag.TAG_INT);
    }

    public static int getDamage(CompoundTag tag) {
        return hasDamage(tag) ? tag.getInt(STACK_DAMAGE_KEY) : 0;
    }
}
