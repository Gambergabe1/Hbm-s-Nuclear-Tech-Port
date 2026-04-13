package com.hbm.machine;

import java.util.HashMap;
import java.util.Map;

import com.hbm.registry.HbmBlocks;
import com.hbm.registry.HbmItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public final class ShredderRecipeRegistry {
    private static final Map<Item, ItemStack> RECIPES = new HashMap<>();

    static {
        add(HbmItems.INGOT_ADVANCED_ALLOY.get(), stack(HbmItems.POWDER_ADVANCED_ALLOY.get(), 1));
        add(HbmBlocks.BLOCK_ADVANCED_ALLOY.get(), stack(HbmItems.POWDER_ADVANCED_ALLOY.get(), 9));
        add(HbmItems.INGOT_COMBINE_STEEL.get(), stack(HbmItems.POWDER_COMBINE_STEEL.get(), 1));
        add(HbmBlocks.BLOCK_COMBINE_STEEL.get(), stack(HbmItems.POWDER_COMBINE_STEEL.get(), 9));
        add(HbmItems.INGOT_RED_COPPER.get(), stack(HbmItems.POWDER_RED_COPPER.get(), 1));
        add(HbmBlocks.BLOCK_RED_COPPER.get(), stack(HbmItems.POWDER_RED_COPPER.get(), 9));
        add(HbmItems.COIL_COPPER.get(), stack(HbmItems.POWDER_RED_COPPER.get(), 1));
        add(HbmItems.COIL_COPPER_TORUS.get(), stack(HbmItems.POWDER_RED_COPPER.get(), 2));
        add(HbmItems.COIL_ADVANCED_ALLOY.get(), stack(HbmItems.POWDER_ADVANCED_ALLOY.get(), 1));
        add(HbmItems.COIL_ADVANCED_TORUS.get(), stack(HbmItems.POWDER_ADVANCED_ALLOY.get(), 2));
        add(HbmItems.COIL_GOLD.get(), stack(HbmItems.POWDER_GOLD.get(), 1));
        add(HbmItems.COIL_GOLD_TORUS.get(), stack(HbmItems.POWDER_GOLD.get(), 2));
        add(HbmItems.INGOT_MAGNETIZED_TUNGSTEN.get(), stack(HbmItems.POWDER_MAGNETIZED_TUNGSTEN.get(), 1));
        add(HbmItems.COIL_TUNGSTEN.get(), stack(HbmItems.POWDER_TUNGSTEN.get(), 1));
        add(HbmItems.COIL_MAGNETIZED_TUNGSTEN.get(), stack(HbmItems.POWDER_MAGNETIZED_TUNGSTEN.get(), 1));
        add(HbmItems.INGOT_BORON.get(), stack(HbmItems.POWDER_BORON.get(), 1));
        add(HbmBlocks.BLOCK_BORON.get(), stack(HbmItems.POWDER_BORON.get(), 9));
        add(HbmItems.INGOT_DURA_STEEL.get(), stack(HbmItems.POWDER_DURA_STEEL.get(), 1));
        add(HbmBlocks.BLOCK_DURA_STEEL.get(), stack(HbmItems.POWDER_DURA_STEEL.get(), 9));
        add(HbmItems.INGOT_DESH.get(), stack(HbmItems.POWDER_DESH.get(), 1));
        add(HbmBlocks.BLOCK_DESH.get(), stack(HbmItems.POWDER_DESH.get(), 9));
        add(HbmItems.INGOT_ZIRCONIUM.get(), stack(HbmItems.POWDER_ZIRCONIUM.get(), 1));
        add(HbmBlocks.BLOCK_ZIRCONIUM.get(), stack(HbmItems.POWDER_ZIRCONIUM.get(), 9));
        add(HbmItems.INGOT_ASBESTOS.get(), stack(HbmItems.POWDER_ASBESTOS.get(), 1));
        add(HbmItems.CRYSTAL_ASBESTOS.get(), stack(HbmItems.POWDER_ASBESTOS.get(), 3));
        add(HbmItems.INGOT_CADMIUM.get(), stack(HbmItems.POWDER_CADMIUM.get(), 1));
        add(HbmBlocks.BLOCK_CADMIUM.get(), stack(HbmItems.POWDER_CADMIUM.get(), 9));
        add(HbmItems.INGOT_BISMUTH.get(), stack(HbmItems.POWDER_BISMUTH.get(), 1));
        add(HbmBlocks.BLOCK_BISMUTH.get(), stack(HbmItems.POWDER_BISMUTH.get(), 9));
        add(HbmItems.INGOT_URANIUM.get(), stack(HbmItems.POWDER_URANIUM.get(), 1));
        add(HbmItems.CRYSTAL_URANIUM.get(), stack(HbmItems.POWDER_URANIUM.get(), 3));
        add(HbmItems.INGOT_PLUTONIUM.get(), stack(HbmItems.POWDER_PLUTONIUM.get(), 1));
        add(HbmItems.CRYSTAL_PLUTONIUM.get(), stack(HbmItems.POWDER_PLUTONIUM.get(), 3));
        add(HbmItems.INGOT_NEPTUNIUM.get(), stack(HbmItems.POWDER_NEPTUNIUM.get(), 1));
        add(HbmItems.INGOT_POLONIUM.get(), stack(HbmItems.POWDER_POLONIUM.get(), 1));
        add(HbmItems.CRYSTAL_THORIUM.get(), stack(HbmItems.POWDER_THORIUM.get(), 3));
        add(HbmItems.INGOT_SCHRABIDIUM.get(), stack(HbmItems.POWDER_SCHRABIDIUM.get(), 1));
        add(HbmItems.INGOT_SCHRABIDATE.get(), stack(HbmItems.POWDER_SCHRABIDATE.get(), 1));
        add(HbmItems.INGOT_EUPHEMIUM.get(), stack(HbmItems.POWDER_EUPHEMIUM.get(), 1));
        add(HbmBlocks.BLOCK_EUPHEMIUM.get(), stack(HbmItems.POWDER_EUPHEMIUM.get(), 9));
        add(HbmItems.INGOT_DINEUTRONIUM.get(), stack(HbmItems.POWDER_DINEUTRONIUM.get(), 1));
        add(HbmBlocks.BLOCK_DINEUTRONIUM.get(), stack(HbmItems.POWDER_DINEUTRONIUM.get(), 9));
        add(HbmItems.CRYSTAL_LITHIUM.get(), stack(HbmItems.POWDER_LITHIUM.get(), 3));
        add(HbmItems.CRYSTAL_COBALT.get(), stack(HbmItems.POWDER_COBALT.get(), 3));
        add(HbmItems.INGOT_NIOBIUM.get(), stack(HbmItems.POWDER_NIOBIUM.get(), 1));
        add(HbmBlocks.BLOCK_NIOBIUM.get(), stack(HbmItems.POWDER_NIOBIUM.get(), 9));
        add(HbmItems.INGOT_NEODYMIUM.get(), stack(HbmItems.POWDER_NEODYMIUM.get(), 1));
        add(HbmItems.INGOT_BROMINE.get(), stack(HbmItems.POWDER_BROMINE.get(), 1));
        add(HbmItems.INGOT_CAESIUM.get(), stack(HbmItems.POWDER_CAESIUM.get(), 1));
        add(HbmItems.INGOT_CERIUM.get(), stack(HbmItems.POWDER_CERIUM.get(), 1));
        add(HbmItems.INGOT_TENNESSINE.get(), stack(HbmItems.POWDER_TENNESSINE.get(), 1));
        add(HbmItems.INGOT_ASTATINE.get(), stack(HbmItems.POWDER_ASTATINE.get(), 1));
        add(HbmItems.INGOT_CO60.get(), stack(HbmItems.POWDER_CO60.get(), 1));
        add(HbmItems.INGOT_STRONTIUM.get(), stack(HbmItems.POWDER_STRONTIUM.get(), 1));
        add(HbmItems.INGOT_SR90.get(), stack(HbmItems.POWDER_SR90.get(), 1));
        add(HbmItems.INGOT_IODINE.get(), stack(HbmItems.POWDER_IODINE.get(), 1));
        add(HbmItems.INGOT_I131.get(), stack(HbmItems.POWDER_I131.get(), 1));
        add(HbmItems.INGOT_PB209.get(), stack(HbmItems.POWDER_PB209.get(), 1));
        add(HbmItems.INGOT_RA226.get(), stack(HbmItems.POWDER_RA226.get(), 1));
        add(HbmItems.INGOT_AC227.get(), stack(HbmItems.POWDER_AC227.get(), 1));
        add(HbmItems.INGOT_RADSPICE.get(), stack(HbmItems.POWDER_RADSPICE.get(), 1));
        add(HbmItems.INGOT_POLYMER.get(), stack(HbmItems.POWDER_POLYMER.get(), 1));
        add(HbmBlocks.BLOCK_POLYMER.get(), stack(HbmItems.POWDER_POLYMER.get(), 9));
        add(HbmItems.INGOT_BAKELITE.get(), stack(HbmItems.POWDER_BAKELITE.get(), 1));
        add(HbmBlocks.BLOCK_BAKELITE.get(), stack(HbmItems.POWDER_BAKELITE.get(), 9));
        add(HbmItems.INGOT_ACTINIUM.get(), stack(HbmItems.POWDER_ACTINIUM.get(), 1));
        add(HbmBlocks.BLOCK_ACTINIUM.get(), stack(HbmItems.POWDER_ACTINIUM.get(), 9));
        add(HbmItems.INGOT_REIIUM.get(), stack(HbmItems.POWDER_REIIUM.get(), 1));
        add(HbmBlocks.BLOCK_REIIUM.get(), stack(HbmItems.POWDER_REIIUM.get(), 9));
        add(HbmItems.INGOT_WEIDANIUM.get(), stack(HbmItems.POWDER_WEIDANIUM.get(), 1));
        add(HbmBlocks.BLOCK_WEIDANIUM.get(), stack(HbmItems.POWDER_WEIDANIUM.get(), 9));
        add(HbmItems.INGOT_AUSTRALIUM.get(), stack(HbmItems.POWDER_AUSTRALIUM.get(), 1));
        add(HbmBlocks.BLOCK_AUSTRALIUM.get(), stack(HbmItems.POWDER_AUSTRALIUM.get(), 9));
        add(HbmItems.INGOT_VERTICIUM.get(), stack(HbmItems.POWDER_VERTICIUM.get(), 1));
        add(HbmBlocks.BLOCK_VERTICIUM.get(), stack(HbmItems.POWDER_VERTICIUM.get(), 9));
        add(HbmItems.INGOT_UNOBTAINIUM.get(), stack(HbmItems.POWDER_UNOBTAINIUM.get(), 1));
        add(HbmBlocks.BLOCK_UNOBTAINIUM.get(), stack(HbmItems.POWDER_UNOBTAINIUM.get(), 9));
        add(HbmItems.INGOT_DAFFERGON.get(), stack(HbmItems.POWDER_DAFFERGON.get(), 1));
        add(HbmBlocks.BLOCK_DAFFERGON.get(), stack(HbmItems.POWDER_DAFFERGON.get(), 9));
        add(HbmItems.CRYSTAL_IRON.get(), stack(HbmItems.POWDER_IRON.get(), 3));
        add(HbmItems.CRYSTAL_GOLD.get(), stack(HbmItems.POWDER_GOLD.get(), 3));
        add(HbmItems.CRYSTAL_REDSTONE.get(), new ItemStack(Items.REDSTONE, 8));
        add(HbmItems.CRYSTAL_LAPIS.get(), stack(HbmItems.POWDER_LAPIS.get(), 12));
        add(HbmItems.CRYSTAL_DIAMOND.get(), stack(HbmItems.POWDER_DIAMOND.get(), 3));
        add(HbmItems.CRYSTAL_TITANIUM.get(), stack(HbmItems.POWDER_TITANIUM.get(), 3));
        add(HbmItems.CRYSTAL_SULFUR.get(), stack(HbmItems.SULFUR.get(), 8));
        add(HbmItems.CRYSTAL_NITER.get(), stack(HbmItems.NITER.get(), 8));
        add(HbmItems.CRYSTAL_COPPER.get(), stack(HbmItems.POWDER_COPPER.get(), 3));
        add(HbmItems.CRYSTAL_TUNGSTEN.get(), stack(HbmItems.POWDER_TUNGSTEN.get(), 3));
        add(HbmItems.CRYSTAL_ALUMINIUM.get(), stack(HbmItems.POWDER_ALUMINIUM.get(), 3));
        add(HbmItems.CRYSTAL_FLUORITE.get(), stack(HbmItems.FLUORITE.get(), 8));
        add(HbmItems.CRYSTAL_BERYLLIUM.get(), stack(HbmItems.POWDER_BERYLLIUM.get(), 3));
        add(HbmItems.CRYSTAL_LEAD.get(), stack(HbmItems.POWDER_LEAD.get(), 3));
        add(HbmItems.CRYSTAL_RARE.get(), stack(HbmItems.POWDER_DESH_MIX.get(), 2));
        add(HbmBlocks.CRATE_STEEL.get(), stack(HbmItems.POWDER_STEEL.get(), 8));
        add(HbmBlocks.CRATE_IRON.get(), stack(HbmItems.POWDER_IRON.get(), 8));
        add(HbmBlocks.CONCRETE.get(), new ItemStack(Blocks.GRAVEL));
        add(HbmBlocks.CONCRETE_SMOOTH.get(), new ItemStack(Blocks.GRAVEL));
        add(HbmBlocks.BRICK_CONCRETE.get(), new ItemStack(Blocks.GRAVEL));
        add(HbmBlocks.BRICK_LIGHT.get(), new ItemStack(Items.CLAY_BALL, 4));
        add(HbmBlocks.STONE_GNEISS.get(), stack(HbmItems.POWDER_LITHIUM_TINY.get(), 1));
        add(HbmBlocks.METEOR_POLISHED.get(), stack(HbmItems.POWDER_METEORITE.get(), 1));
        add(HbmBlocks.METEOR_BRICK.get(), stack(HbmItems.POWDER_METEORITE.get(), 1));
        add(HbmBlocks.METEOR_BRICK_MOSSY.get(), stack(HbmItems.POWDER_METEORITE.get(), 1));
        add(HbmBlocks.METEOR_BRICK_CRACKED.get(), stack(HbmItems.POWDER_METEORITE.get(), 1));
        add(HbmBlocks.METEOR_BRICK_CHISELED.get(), stack(HbmItems.POWDER_METEORITE.get(), 1));
        add(Blocks.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4));
        add(Blocks.PACKED_ICE, stack(HbmItems.POWDER_ICE.get(), 1));
        add(Blocks.QUARTZ_BLOCK, stack(HbmItems.POWDER_QUARTZ.get(), 4));
        add(Blocks.QUARTZ_BRICKS, stack(HbmItems.POWDER_QUARTZ.get(), 4));
        add(Blocks.QUARTZ_PILLAR, stack(HbmItems.POWDER_QUARTZ.get(), 4));
        add(Blocks.CHISELED_QUARTZ_BLOCK, stack(HbmItems.POWDER_QUARTZ.get(), 4));
        add(Items.QUARTZ, stack(HbmItems.POWDER_QUARTZ.get(), 1));
        add(Blocks.NETHER_QUARTZ_ORE, stack(HbmItems.POWDER_QUARTZ.get(), 2));
        add(Blocks.STONE, new ItemStack(Blocks.GRAVEL));
        add(Blocks.COBBLESTONE, new ItemStack(Blocks.GRAVEL));
        add(Blocks.STONE_BRICKS, new ItemStack(Blocks.GRAVEL));
        add(Blocks.GRAVEL, new ItemStack(Blocks.SAND));
        add(Blocks.SANDSTONE, new ItemStack(Blocks.SAND, 4));
        add(Blocks.BRICKS, new ItemStack(Items.CLAY_BALL, 4));
        add(Items.BRICK, new ItemStack(Items.CLAY_BALL));
        add(Items.BONE, new ItemStack(Items.BONE_MEAL, 5));
    }

    private ShredderRecipeRegistry() {
    }

    public static ItemStack getResult(ItemStack input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = RECIPES.get(input.getItem());
        return result == null ? ItemStack.EMPTY : result.copy();
    }

    public static boolean hasRecipe(ItemStack input) {
        return !getResult(input).isEmpty();
    }

    private static void add(ItemLike input, ItemStack output) {
        RECIPES.put(input.asItem(), output.copy());
    }

    private static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }
}
