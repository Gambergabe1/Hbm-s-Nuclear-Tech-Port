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

    // --- ArmorFSB materials (legacy EnumHelper.addArmorMaterial defense arrays were
    //     [boots, legs, chest, helmet]) ---
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PAA = ARMOR_MATERIALS.register(
        "paa",
        () -> createMaterial("paa", 3, 8, 6, 3, 25, 2.0F, () -> Ingredient.of(HbmItems.PLATE_PAA.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SCHRABIDIUM = ARMOR_MATERIALS.register(
        "schrabidium",
        () -> createMaterial("schrabidium", 3, 8, 6, 3, 50, 2.0F, () -> Ingredient.of(HbmItems.INGOT_SCHRABIDIUM.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARMOR_STEEL = ARMOR_MATERIALS.register(
        "armor_steel",
        () -> createMaterial("armor_steel", 2, 6, 5, 2, 5, 0.0F, () -> Ingredient.of(HbmItems.INGOT_STEEL.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARMOR_ALLOY = ARMOR_MATERIALS.register(
        "armor_alloy",
        () -> createMaterial("armor_alloy", 3, 8, 6, 3, 12, 0.0F, () -> Ingredient.of(HbmItems.INGOT_ADVANCED_ALLOY.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARMOR_TITANIUM = ARMOR_MATERIALS.register(
        "armor_titanium",
        () -> createMaterial("armor_titanium", 3, 8, 6, 3, 9, 2.0F, () -> Ingredient.of(HbmItems.INGOT_TITANIUM.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CMB = ARMOR_MATERIALS.register(
        "cmb",
        () -> createMaterial("cmb", 3, 8, 6, 3, 50, 2.0F, () -> Ingredient.of(HbmItems.INGOT_COMBINE_STEEL.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SECURITY = ARMOR_MATERIALS.register(
        "security",
        () -> createMaterial("security", 3, 8, 6, 3, 15, 2.0F, () -> Ingredient.of(HbmItems.PLATE_KEVLAR.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ASBESTOS = ARMOR_MATERIALS.register(
        "asbestos",
        () -> createMaterial("asbestos", 1, 4, 3, 1, 5, 0.0F, () -> Ingredient.of(HbmItems.ASBESTOS_CLOTH.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COBALT = ARMOR_MATERIALS.register(
        "cobalt",
        () -> createMaterial("cobalt", 3, 8, 6, 3, 25, 2.0F, () -> Ingredient.of(HbmItems.INGOT_COBALT.get()))
    );
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> STARMETAL = ARMOR_MATERIALS.register(
        "starmetal",
        () -> createMaterial("starmetal", 3, 8, 6, 3, 100, 2.0F, () -> Ingredient.of(HbmItems.INGOT_STARMETAL.get()))
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
