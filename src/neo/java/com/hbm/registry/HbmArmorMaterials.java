package com.hbm.registry;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import com.hbm.HbmNuclearTech;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
        DeferredRegister.create(Registries.ARMOR_MATERIAL, HbmNuclearTech.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HAZMAT = ARMOR_MATERIALS.register(
        "hazmat",
        () -> createMaterial("hazmat", 2, 5, 4, 1, 5, 0.0F, () -> Ingredient.of(HbmItems.HAZMAT_CLOTH.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HAZMAT_RED = ARMOR_MATERIALS.register(
        "hazmat_red",
        () -> createMaterial("hazmat_red", 2, 5, 4, 1, 5, 0.0F, () -> Ingredient.of(HbmItems.HAZMAT_CLOTH_RED.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HAZMAT_GREY = ARMOR_MATERIALS.register(
        "hazmat_grey",
        () -> createMaterial("hazmat_grey", 2, 5, 4, 1, 5, 0.0F, () -> Ingredient.of(HbmItems.HAZMAT_CLOTH_GREY.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HAZMAT_PAA = ARMOR_MATERIALS.register(
        "hazmat_paa",
        () -> createMaterial("hazmat_paa", 3, 8, 6, 3, 25, 2.0F, () -> Ingredient.of(HbmItems.PLATE_ADVANCED_ALLOY.get()))
    );

    private HbmArmorMaterials() {
    }

    private static ArmorMaterial createMaterial(
        String layerName,
        int helmetDefense,
        int chestDefense,
        int legsDefense,
        int bootsDefense,
        int enchantmentValue,
        float toughness,
        Supplier<Ingredient> repairIngredient
    ) {
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.BOOTS, bootsDefense);
        defense.put(ArmorItem.Type.LEGGINGS, legsDefense);
        defense.put(ArmorItem.Type.CHESTPLATE, chestDefense);
        defense.put(ArmorItem.Type.HELMET, helmetDefense);
        defense.put(ArmorItem.Type.BODY, chestDefense);
        return new ArmorMaterial(
            defense,
            enchantmentValue,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            repairIngredient,
            List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, layerName))),
            toughness,
            0.0F
        );
    }
}
