package com.hbm.registry;

import com.hbm.HbmNuclearTech;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public final class HbmTags {
    public static final String COMMON_NAMESPACE = "c";

    private HbmTags() {
    }

    public static TagKey<Item> commonItem(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(COMMON_NAMESPACE, path));
    }

    public static TagKey<Block> commonBlock(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(COMMON_NAMESPACE, path));
    }

    public static TagKey<Item> commonIngot(String material) {
        return commonItem("ingots/" + material);
    }

    public static TagKey<Item> commonPlate(String material) {
        return commonItem("plates/" + material);
    }

    public static TagKey<Item> commonOreItem(String material) {
        return commonItem("ores/" + material);
    }

    public static TagKey<Block> commonOreBlock(String material) {
        return commonBlock("ores/" + material);
    }

    public static TagKey<Item> commonStorageBlockItem(String material) {
        return commonItem("storage_blocks/" + material);
    }

    public static TagKey<Block> commonStorageBlock(String material) {
        return commonBlock("storage_blocks/" + material);
    }

    public static TagKey<Item> modItem(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, path));
    }

    public static TagKey<Block> modBlock(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, path));
    }

    public static TagKey<Item> monoMasks() {
        return modItem("mono_masks");
    }

    public static TagKey<Item> monoMaskAttachments() {
        return modItem("mono_mask_attachments");
    }

    public static TagKey<Item> pressStamps() {
        return modItem("press_stamps");
    }

    public static boolean itemMatches(ItemStack stack, TagKey<Item> tag) {
        return !stack.isEmpty() && stack.is(tag);
    }
}
