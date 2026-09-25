package com.hbm.registry;

import java.util.function.Supplier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Custom tool tiers ported from the legacy EnumHelper.addToolMaterial(...)
 * definitions in MainRegistry.java. "Incorrect blocks for drops" is mapped
 * to the closest vanilla harvest-level equivalent (there's no equivalent
 * custom-tag system ported yet); everything else (durability, mining
 * speed, attack damage bonus, enchantment value, repair ingredient) is
 * carried over as-is.
 */
public final class HbmToolTiers {
    private HbmToolTiers() {
    }

    public static final Tier STEEL = simple(500, 7.5F, 2.0F, Tiers.IRON, 10, () -> HbmItems.INGOT_STEEL.get());
    public static final Tier TITANIUM = simple(750, 9.0F, 2.5F, Tiers.IRON, 15, () -> HbmItems.INGOT_TITANIUM.get());
    public static final Tier ALLOY = simple(2000, 15.0F, 5.0F, Tiers.DIAMOND, 5, () -> HbmItems.INGOT_ADVANCED_ALLOY.get());
    public static final Tier CMB = simple(8500, 40.0F, 55.0F, Tiers.NETHERITE, 100, () -> HbmItems.INGOT_COMBINE_STEEL.get());
    public static final Tier DESH = unbreakable(7.5F, 2.0F, Tiers.IRON, 10, () -> HbmItems.INGOT_DESH.get());
    public static final Tier COBALT = simple(750, 9.0F, 2.5F, Tiers.NETHERITE, 15, null);
    public static final Tier COBALT_DECORATED = simple(1000, 15.0F, 2.5F, Tiers.NETHERITE, 25, () -> HbmItems.INGOT_COBALT.get());
    public static final Tier SAW = simple(750, 2.0F, 3.5F, Tiers.IRON, 25, null);
    public static final Tier BAT = simple(500, 1.5F, 3.0F, Tiers.WOOD, 25, null);
    public static final Tier BAT_NAIL = simple(450, 1.0F, 4.0F, Tiers.WOOD, 25, null);
    public static final Tier GOLF_CLUB = simple(1000, 2.0F, 5.0F, Tiers.STONE, 25, null);
    public static final Tier PIPE_RUSTY = simple(350, 1.5F, 4.5F, Tiers.STONE, 25, null);
    public static final Tier PIPE_LEAD = simple(250, 1.5F, 5.5F, Tiers.STONE, 25, null);
    public static final Tier STARMETAL = simple(1000, 20.0F, 2.5F, Tiers.DIAMOND, 30, () -> HbmItems.INGOT_STARMETAL.get());
    public static final Tier HAMMER = unbreakable(50.0F, 999999996.0F, Tiers.DIAMOND, 200, null);
    public static final Tier ELEC = unbreakable(30.0F, 12.0F, Tiers.IRON, 2, null);
    public static final Tier BOTTLE_OPENER = simple(250, 1.5F, 0.5F, Tiers.STONE, 200, null);
    public static final Tier SLEDGE = unbreakable(25.0F, 26.0F, Tiers.STONE, 200, null);
    public static final Tier SCHRABIDIUM = simple(10000, 50.0F, 100.0F, Tiers.NETHERITE, 200, () -> HbmItems.INGOT_SCHRABIDIUM.get());
    public static final Tier MESE_GAVEL = unbreakable(50.0F, 0.0F, Tiers.NETHERITE, 200, () -> HbmItems.PLATE_PAA.get());

    private static Tier simple(
        int uses,
        float speed,
        float attackDamageBonus,
        Tier incorrectBlocksSource,
        int enchantmentValue,
        Supplier<Item> repairItem
    ) {
        return new SimpleTier(uses, speed, attackDamageBonus, incorrectBlocksSource.getIncorrectBlocksForDrops(), enchantmentValue, repairItem);
    }

    private static Tier unbreakable(
        float speed,
        float attackDamageBonus,
        Tier incorrectBlocksSource,
        int enchantmentValue,
        Supplier<Item> repairItem
    ) {
        // The legacy material used maxUses=0, which the old EnumHelper-based tool
        // material system treated as "never breaks". There's no direct modern
        // equivalent wired up here (that would mean never calling
        // Properties#durability(...) at all, which the tiered item constructors
        // don't give tier-level control over), so this approximates it with an
        // effectively-unlimited use count instead.
        return new SimpleTier(Integer.MAX_VALUE, speed, attackDamageBonus, incorrectBlocksSource.getIncorrectBlocksForDrops(), enchantmentValue, repairItem);
    }

    private record SimpleTier(
        int uses,
        float speed,
        float attackDamageBonus,
        TagKey<Block> incorrectBlocksForDrops,
        int enchantmentValue,
        Supplier<Item> repairItem
    ) implements Tier {
        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return incorrectBlocksForDrops;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repairItem == null ? Ingredient.EMPTY : Ingredient.of(repairItem.get());
        }
    }
}
