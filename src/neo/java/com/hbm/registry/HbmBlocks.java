package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.block.BatteryStorageBlock;
import com.hbm.block.BurnerPressBlock;
import com.hbm.block.DeshCrateBlock;
import com.hbm.block.EnergyCableBlock;
import com.hbm.block.EnergyCableDetectorBlock;
import com.hbm.block.EnergyCableDiodeBlock;
import com.hbm.block.EnergyCableGaugeBlock;
import com.hbm.block.EnergyCableSwitchBlock;
import com.hbm.block.ElectricFurnaceBlock;
import com.hbm.block.ElectricPressBlock;
import com.hbm.block.FluidBarrelBlock;
import com.hbm.block.FluidDuctBlock;
import com.hbm.block.GasBlock;
import com.hbm.block.IronCrateBlock;
import com.hbm.block.SafeBlock;
import com.hbm.block.ShredderBlock;
import com.hbm.block.SteelCrateBlock;
import com.hbm.block.TransformerChargerBlock;
import com.hbm.armor.HbmHazardClass;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HbmNuclearTech.MODID);
    public static final DeferredBlock<Block> ASPHALT = registerStoneBlock("asphalt", MapColor.COLOR_BLACK, 2.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_SLAG = registerStoneBlock("block_slag", MapColor.COLOR_BLACK, 2.0F, 5.0F);
    public static final DeferredBlock<Block> STONE_GNEISS = registerStoneBlock("stone_gneiss", MapColor.STONE, 1.5F, 10.0F);
    public static final DeferredBlock<Block> STONE_CRACKED = registerStoneBlock("stone_cracked", MapColor.STONE, 5.0F, 10.0F);
    public static final DeferredBlock<Block> BASALT = registerStoneBlock("basalt", MapColor.COLOR_BLACK, 5.0F, 10.0F);
    public static final DeferredBlock<Block> BASALT_SMOOTH = registerStoneBlock("basalt_smooth", MapColor.COLOR_BLACK, 5.0F, 10.0F);
    public static final DeferredBlock<Block> BASALT_BRICK = registerStoneBlock("basalt_brick", MapColor.COLOR_BLACK, 5.0F, 10.0F);
    public static final DeferredBlock<Block> BASALT_POLISHED = registerStoneBlock("basalt_polished", MapColor.COLOR_BLACK, 5.0F, 10.0F);
    public static final DeferredBlock<Block> BASALT_TILES = registerStoneBlock("basalt_tiles", MapColor.COLOR_BLACK, 5.0F, 10.0F);
    public static final DeferredBlock<Block> METEOR_POLISHED = registerStoneBlock("meteor_polished", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> METEOR_BRICK = registerStoneBlock("meteor_brick", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> METEOR_BRICK_MOSSY = registerStoneBlock("meteor_brick_mossy", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> METEOR_BRICK_CRACKED = registerStoneBlock("meteor_brick_cracked", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> METEOR_BRICK_CHISELED = registerStoneBlock("meteor_brick_chiseled", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_DUNGEON = registerStoneBlock("brick_dungeon", MapColor.COLOR_GRAY, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_DUNGEON_FLAT = registerStoneBlock("brick_dungeon_flat", MapColor.COLOR_GRAY, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_DUNGEON_TILE = registerStoneBlock("brick_dungeon_tile", MapColor.COLOR_GRAY, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_DUNGEON_CIRCLE = registerStoneBlock("brick_dungeon_circle", MapColor.COLOR_GRAY, 15.0F, 900.0F);
    public static final DeferredBlock<Block> MUFFLER = BLOCKS.registerSimpleBlock(
        "muffler",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(0.8F)
            .sound(SoundType.WOOL)
    );
    public static final DeferredBlock<Block> REINFORCED_GLASS = BLOCKS.register(
        "reinforced_glass",
        () -> new Block(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.NONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 15.0F)
                .sound(SoundType.GLASS)
                .noOcclusion()
        )
    );
    public static final DeferredBlock<Block> REINFORCED_STONE = registerStoneBlock("reinforced_stone", MapColor.STONE, 5.0F, 10.0F);
    public static final DeferredBlock<StairBlock> REINFORCED_STONE_STAIRS = registerStairsBlock("reinforced_stone_stairs", REINFORCED_STONE);
    public static final DeferredBlock<SlabBlock> REINFORCED_STONE_SLAB = registerSlabBlock("reinforced_stone_slab", REINFORCED_STONE);
    public static final DeferredBlock<Block> BRICK_CONCRETE = registerStoneBlock("brick_concrete", MapColor.STONE, 15.0F, 6000.0F);
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_STAIRS = registerStairsBlock("brick_concrete_stairs", BRICK_CONCRETE);
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_SLAB = registerSlabBlock("brick_concrete_slab", BRICK_CONCRETE);
    public static final DeferredBlock<Block> BRICK_CONCRETE_MOSSY = registerStoneBlock("brick_concrete_mossy", MapColor.COLOR_GREEN, 15.0F, 6000.0F);
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_MOSSY_STAIRS = registerStairsBlock("brick_concrete_mossy_stairs", BRICK_CONCRETE_MOSSY);
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_MOSSY_SLAB = registerSlabBlock("brick_concrete_mossy_slab", BRICK_CONCRETE_MOSSY);
    public static final DeferredBlock<Block> BRICK_CONCRETE_CRACKED = registerStoneBlock("brick_concrete_cracked", MapColor.STONE, 15.0F, 2000.0F);
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_CRACKED_STAIRS = registerStairsBlock("brick_concrete_cracked_stairs", BRICK_CONCRETE_CRACKED);
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_CRACKED_SLAB = registerSlabBlock("brick_concrete_cracked_slab", BRICK_CONCRETE_CRACKED);
    public static final DeferredBlock<Block> BRICK_CONCRETE_BROKEN = registerStoneBlock("brick_concrete_broken", MapColor.STONE, 15.0F, 1500.0F);
    public static final DeferredBlock<StairBlock> BRICK_CONCRETE_BROKEN_STAIRS = registerStairsBlock("brick_concrete_broken_stairs", BRICK_CONCRETE_BROKEN);
    public static final DeferredBlock<SlabBlock> BRICK_CONCRETE_BROKEN_SLAB = registerSlabBlock("brick_concrete_broken_slab", BRICK_CONCRETE_BROKEN);
    public static final DeferredBlock<Block> REINFORCED_BRICK = registerStoneBlock("reinforced_brick", MapColor.STONE, 15.0F, 8000.0F);
    public static final DeferredBlock<StairBlock> REINFORCED_BRICK_STAIRS = registerStairsBlock("reinforced_brick_stairs", REINFORCED_BRICK);
    public static final DeferredBlock<SlabBlock> REINFORCED_BRICK_SLAB = registerSlabBlock("reinforced_brick_slab", REINFORCED_BRICK);
    public static final DeferredBlock<Block> BRICK_COMPOUND = registerStoneBlock("brick_compound", MapColor.STONE, 15.0F, 10000.0F);
    public static final DeferredBlock<StairBlock> BRICK_COMPOUND_STAIRS = registerStairsBlock("brick_compound_stairs", BRICK_COMPOUND);
    public static final DeferredBlock<SlabBlock> BRICK_COMPOUND_SLAB = registerSlabBlock("brick_compound_slab", BRICK_COMPOUND);
    public static final DeferredBlock<Block> BRICK_FIRE = registerStoneBlock("brick_fire", MapColor.COLOR_RED, 10.0F, 100.0F);
    public static final DeferredBlock<StairBlock> BRICK_FIRE_STAIRS = registerStairsBlock("brick_fire_stairs", BRICK_FIRE);
    public static final DeferredBlock<SlabBlock> BRICK_FIRE_SLAB = registerSlabBlock("brick_fire_slab", BRICK_FIRE);
    public static final DeferredBlock<Block> BRICK_LIGHT = registerStoneBlock("brick_light", MapColor.COLOR_YELLOW, 15.0F, 1000.0F);
    public static final DeferredBlock<StairBlock> BRICK_LIGHT_STAIRS = registerStairsBlock("brick_light_stairs", BRICK_LIGHT);
    public static final DeferredBlock<SlabBlock> BRICK_LIGHT_SLAB = registerSlabBlock("brick_light_slab", BRICK_LIGHT);
    public static final DeferredBlock<Block> REINFORCED_SAND = registerStoneBlock("reinforced_sand", MapColor.SAND, 15.0F, 400.0F);
    public static final DeferredBlock<StairBlock> REINFORCED_SAND_STAIRS = registerStairsBlock("reinforced_sand_stairs", REINFORCED_SAND);
    public static final DeferredBlock<SlabBlock> REINFORCED_SAND_SLAB = registerSlabBlock("reinforced_sand_slab", REINFORCED_SAND);
    public static final DeferredBlock<Block> BRICK_OBSIDIAN = registerStoneBlock("brick_obsidian", MapColor.COLOR_BLACK, 15.0F, 8000.0F);
    public static final DeferredBlock<StairBlock> BRICK_OBSIDIAN_STAIRS = registerStairsBlock("brick_obsidian_stairs", BRICK_OBSIDIAN);
    public static final DeferredBlock<SlabBlock> BRICK_OBSIDIAN_SLAB = registerSlabBlock("brick_obsidian_slab", BRICK_OBSIDIAN);
    public static final DeferredBlock<Block> CMB_BRICK = registerStoneBlock("cmb_brick", MapColor.COLOR_GRAY, 25.0F, 6000.0F);
    public static final DeferredBlock<Block> CMB_BRICK_REINFORCED = registerStoneBlock("cmb_brick_reinforced", MapColor.COLOR_GRAY, 25.0F, 60000.0F);
    public static final DeferredBlock<StairBlock> CMB_BRICK_REINFORCED_STAIRS = registerStairsBlock("cmb_brick_reinforced_stairs", CMB_BRICK_REINFORCED);
    public static final DeferredBlock<SlabBlock> CMB_BRICK_REINFORCED_SLAB = registerSlabBlock("cmb_brick_reinforced_slab", CMB_BRICK_REINFORCED);
    public static final DeferredBlock<Block> DUCRETE_SMOOTH = registerStoneBlock("ducrete_smooth", MapColor.STONE, 20.0F, 8000.0F);
    public static final DeferredBlock<StairBlock> DUCRETE_SMOOTH_STAIRS = registerStairsBlock("ducrete_smooth_stairs", DUCRETE_SMOOTH);
    public static final DeferredBlock<SlabBlock> DUCRETE_SMOOTH_SLAB = registerSlabBlock("ducrete_smooth_slab", DUCRETE_SMOOTH);
    public static final DeferredBlock<Block> DUCRETE = registerStoneBlock("ducrete", MapColor.STONE, 20.0F, 8000.0F);
    public static final DeferredBlock<StairBlock> DUCRETE_STAIRS = registerStairsBlock("ducrete_stairs", DUCRETE);
    public static final DeferredBlock<SlabBlock> DUCRETE_SLAB = registerSlabBlock("ducrete_slab", DUCRETE);
    public static final DeferredBlock<Block> DUCRETE_BRICK = registerStoneBlock("ducrete_brick", MapColor.STONE, 15.0F, 12000.0F);
    public static final DeferredBlock<StairBlock> DUCRETE_BRICK_STAIRS = registerStairsBlock("ducrete_brick_stairs", DUCRETE_BRICK);
    public static final DeferredBlock<SlabBlock> DUCRETE_BRICK_SLAB = registerSlabBlock("ducrete_brick_slab", DUCRETE_BRICK);
    public static final DeferredBlock<Block> DUCRETE_REINFORCED = registerStoneBlock("ducrete_reinforced", MapColor.STONE, 20.0F, 24000.0F);
    public static final DeferredBlock<StairBlock> DUCRETE_REINFORCED_STAIRS = registerStairsBlock("ducrete_reinforced_stairs", DUCRETE_REINFORCED);
    public static final DeferredBlock<SlabBlock> DUCRETE_REINFORCED_SLAB = registerSlabBlock("ducrete_reinforced_slab", DUCRETE_REINFORCED);
    public static final DeferredBlock<Block> TILE_LAB = registerStoneBlock("tile_lab", MapColor.SNOW, 1.0F, 20.0F, SoundType.GLASS);
    public static final DeferredBlock<StairBlock> TILE_LAB_STAIRS = registerStairsBlock("tile_lab_stairs", TILE_LAB);
    public static final DeferredBlock<SlabBlock> TILE_LAB_SLAB = registerSlabBlock("tile_lab_slab", TILE_LAB);
    public static final DeferredBlock<Block> TILE_LAB_CRACKED = registerStoneBlock("tile_lab_cracked", MapColor.SNOW, 1.0F, 20.0F, SoundType.GLASS);
    public static final DeferredBlock<StairBlock> TILE_LAB_CRACKED_STAIRS = registerStairsBlock("tile_lab_cracked_stairs", TILE_LAB_CRACKED);
    public static final DeferredBlock<SlabBlock> TILE_LAB_CRACKED_SLAB = registerSlabBlock("tile_lab_cracked_slab", TILE_LAB_CRACKED);
    public static final DeferredBlock<Block> TILE_LAB_BROKEN = registerStoneBlock("tile_lab_broken", MapColor.SNOW, 1.0F, 20.0F, SoundType.GLASS);
    public static final DeferredBlock<StairBlock> TILE_LAB_BROKEN_STAIRS = registerStairsBlock("tile_lab_broken_stairs", TILE_LAB_BROKEN);
    public static final DeferredBlock<SlabBlock> TILE_LAB_BROKEN_SLAB = registerSlabBlock("tile_lab_broken_slab", TILE_LAB_BROKEN);
    public static final DeferredBlock<Block> CONCRETE = registerStoneBlock("concrete", MapColor.STONE, 5.0F, 10.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_STAIRS = registerStairsBlock("concrete_stairs", CONCRETE);
    public static final DeferredBlock<SlabBlock> CONCRETE_SLAB = registerSlabBlock("concrete_slab", CONCRETE);
    public static final DeferredBlock<Block> CONCRETE_SMOOTH = registerStoneBlock("concrete_smooth", MapColor.STONE, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_SMOOTH_STAIRS = registerStairsBlock("concrete_smooth_stairs", CONCRETE_SMOOTH);
    public static final DeferredBlock<SlabBlock> CONCRETE_SMOOTH_SLAB = registerSlabBlock("concrete_smooth_slab", CONCRETE_SMOOTH);
    public static final DeferredBlock<Block> CONCRETE_WHITE = registerStoneBlock("concrete_white", MapColor.SNOW, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_WHITE_STAIRS = registerStairsBlock("concrete_white_stairs", CONCRETE_WHITE);
    public static final DeferredBlock<SlabBlock> CONCRETE_WHITE_SLAB = registerSlabBlock("concrete_white_slab", CONCRETE_WHITE);
    public static final DeferredBlock<Block> CONCRETE_ORANGE = registerStoneBlock("concrete_orange", MapColor.COLOR_ORANGE, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_ORANGE_STAIRS = registerStairsBlock("concrete_orange_stairs", CONCRETE_ORANGE);
    public static final DeferredBlock<SlabBlock> CONCRETE_ORANGE_SLAB = registerSlabBlock("concrete_orange_slab", CONCRETE_ORANGE);
    public static final DeferredBlock<Block> CONCRETE_MAGENTA = registerStoneBlock("concrete_magenta", MapColor.COLOR_MAGENTA, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_MAGENTA_STAIRS = registerStairsBlock("concrete_magenta_stairs", CONCRETE_MAGENTA);
    public static final DeferredBlock<SlabBlock> CONCRETE_MAGENTA_SLAB = registerSlabBlock("concrete_magenta_slab", CONCRETE_MAGENTA);
    public static final DeferredBlock<Block> CONCRETE_LIGHT_BLUE = registerStoneBlock("concrete_light_blue", MapColor.COLOR_LIGHT_BLUE, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_LIGHT_BLUE_STAIRS = registerStairsBlock("concrete_light_blue_stairs", CONCRETE_LIGHT_BLUE);
    public static final DeferredBlock<SlabBlock> CONCRETE_LIGHT_BLUE_SLAB = registerSlabBlock("concrete_light_blue_slab", CONCRETE_LIGHT_BLUE);
    public static final DeferredBlock<Block> CONCRETE_YELLOW = registerStoneBlock("concrete_yellow", MapColor.COLOR_YELLOW, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_YELLOW_STAIRS = registerStairsBlock("concrete_yellow_stairs", CONCRETE_YELLOW);
    public static final DeferredBlock<SlabBlock> CONCRETE_YELLOW_SLAB = registerSlabBlock("concrete_yellow_slab", CONCRETE_YELLOW);
    public static final DeferredBlock<Block> CONCRETE_LIME = registerStoneBlock("concrete_lime", MapColor.COLOR_LIGHT_GREEN, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_LIME_STAIRS = registerStairsBlock("concrete_lime_stairs", CONCRETE_LIME);
    public static final DeferredBlock<SlabBlock> CONCRETE_LIME_SLAB = registerSlabBlock("concrete_lime_slab", CONCRETE_LIME);
    public static final DeferredBlock<Block> CONCRETE_PINK = registerStoneBlock("concrete_pink", MapColor.COLOR_PINK, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_PINK_STAIRS = registerStairsBlock("concrete_pink_stairs", CONCRETE_PINK);
    public static final DeferredBlock<SlabBlock> CONCRETE_PINK_SLAB = registerSlabBlock("concrete_pink_slab", CONCRETE_PINK);
    public static final DeferredBlock<Block> CONCRETE_GRAY = registerStoneBlock("concrete_gray", MapColor.COLOR_GRAY, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_GRAY_STAIRS = registerStairsBlock("concrete_gray_stairs", CONCRETE_GRAY);
    public static final DeferredBlock<SlabBlock> CONCRETE_GRAY_SLAB = registerSlabBlock("concrete_gray_slab", CONCRETE_GRAY);
    public static final DeferredBlock<Block> CONCRETE_SILVER = registerStoneBlock("concrete_silver", MapColor.COLOR_LIGHT_GRAY, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_SILVER_STAIRS = registerStairsBlock("concrete_silver_stairs", CONCRETE_SILVER);
    public static final DeferredBlock<SlabBlock> CONCRETE_SILVER_SLAB = registerSlabBlock("concrete_silver_slab", CONCRETE_SILVER);
    public static final DeferredBlock<Block> CONCRETE_CYAN = registerStoneBlock("concrete_cyan", MapColor.COLOR_CYAN, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_CYAN_STAIRS = registerStairsBlock("concrete_cyan_stairs", CONCRETE_CYAN);
    public static final DeferredBlock<SlabBlock> CONCRETE_CYAN_SLAB = registerSlabBlock("concrete_cyan_slab", CONCRETE_CYAN);
    public static final DeferredBlock<Block> CONCRETE_PURPLE = registerStoneBlock("concrete_purple", MapColor.COLOR_PURPLE, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_PURPLE_STAIRS = registerStairsBlock("concrete_purple_stairs", CONCRETE_PURPLE);
    public static final DeferredBlock<SlabBlock> CONCRETE_PURPLE_SLAB = registerSlabBlock("concrete_purple_slab", CONCRETE_PURPLE);
    public static final DeferredBlock<Block> CONCRETE_BLUE = registerStoneBlock("concrete_blue", MapColor.COLOR_BLUE, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_BLUE_STAIRS = registerStairsBlock("concrete_blue_stairs", CONCRETE_BLUE);
    public static final DeferredBlock<SlabBlock> CONCRETE_BLUE_SLAB = registerSlabBlock("concrete_blue_slab", CONCRETE_BLUE);
    public static final DeferredBlock<Block> CONCRETE_BROWN = registerStoneBlock("concrete_brown", MapColor.COLOR_BROWN, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_BROWN_STAIRS = registerStairsBlock("concrete_brown_stairs", CONCRETE_BROWN);
    public static final DeferredBlock<SlabBlock> CONCRETE_BROWN_SLAB = registerSlabBlock("concrete_brown_slab", CONCRETE_BROWN);
    public static final DeferredBlock<Block> CONCRETE_GREEN = registerStoneBlock("concrete_green", MapColor.COLOR_GREEN, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_GREEN_STAIRS = registerStairsBlock("concrete_green_stairs", CONCRETE_GREEN);
    public static final DeferredBlock<SlabBlock> CONCRETE_GREEN_SLAB = registerSlabBlock("concrete_green_slab", CONCRETE_GREEN);
    public static final DeferredBlock<Block> CONCRETE_RED = registerStoneBlock("concrete_red", MapColor.COLOR_RED, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_RED_STAIRS = registerStairsBlock("concrete_red_stairs", CONCRETE_RED);
    public static final DeferredBlock<SlabBlock> CONCRETE_RED_SLAB = registerSlabBlock("concrete_red_slab", CONCRETE_RED);
    public static final DeferredBlock<Block> CONCRETE_BLACK = registerStoneBlock("concrete_black", MapColor.COLOR_BLACK, 15.0F, 4000.0F);
    public static final DeferredBlock<StairBlock> CONCRETE_BLACK_STAIRS = registerStairsBlock("concrete_black_stairs", CONCRETE_BLACK);
    public static final DeferredBlock<SlabBlock> CONCRETE_BLACK_SLAB = registerSlabBlock("concrete_black_slab", CONCRETE_BLACK);
    public static final DeferredBlock<Block> CONCRETE_HAZARD = registerStoneBlock("concrete_hazard", MapColor.COLOR_YELLOW, 5.0F, 10.0F);
    public static final DeferredBlock<IronCrateBlock> CRATE_IRON = BLOCKS.register(
        "crate_iron",
        () -> new IronCrateBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(3.5F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<SteelCrateBlock> CRATE_STEEL = BLOCKS.register(
        "crate_steel",
        () -> new SteelCrateBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 20.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<DeshCrateBlock> CRATE_DESH = BLOCKS.register(
        "crate_desh",
        () -> new DeshCrateBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(7.5F, 300.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<SafeBlock> SAFE = BLOCKS.register(
        "safe",
        () -> new SafeBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(7.5F, 10000.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<BurnerPressBlock> MACHINE_PRESS = BLOCKS.register(
        "machine_press",
        () -> new BurnerPressBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 20.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<ElectricPressBlock> MACHINE_EPRESS = BLOCKS.register(
        "machine_epress",
        () -> new ElectricPressBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 20.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<ElectricFurnaceBlock> MACHINE_ELECTRIC_FURNACE = BLOCKS.register(
        "machine_electric_furnace_off",
        () -> new ElectricFurnaceBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(ElectricFurnaceBlock.LIT) ? 15 : 0)
        )
    );
    public static final DeferredBlock<TransformerChargerBlock> MACHINE_TRANSFORMER = BLOCKS.register(
        "machine_transformer",
        () -> new TransformerChargerBlock(
            50_000,
            1_000_000,
            true,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<TransformerChargerBlock> MACHINE_TRANSFORMER_20 = BLOCKS.register(
        "machine_transformer_20",
        () -> new TransformerChargerBlock(
            50_000,
            1_000_000,
            false,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<TransformerChargerBlock> MACHINE_TRANSFORMER_DNT = BLOCKS.register(
        "machine_transformer_dnt",
        () -> new TransformerChargerBlock(
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            true,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<TransformerChargerBlock> MACHINE_TRANSFORMER_DNT_20 = BLOCKS.register(
        "machine_transformer_dnt_20",
        () -> new TransformerChargerBlock(
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            false,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_BATTERY_POTATO = registerBattery("machine_battery_potato", 100_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_BATTERY = registerBattery("machine_battery", 10_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_LITHIUM_BATTERY = registerBattery("machine_lithium_battery", 100_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_DESH_BATTERY = registerBattery("machine_desh_battery", 1_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_SATURNITE_BATTERY = registerBattery("machine_saturnite_battery", 10_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_SCHRABIDIUM_BATTERY = registerBattery("machine_schrabidium_battery", 100_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_EUPHEMIUM_BATTERY = registerBattery("machine_euphemium_battery", 1_000_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_RADSPICE_BATTERY = registerBattery("machine_radspice_battery", 10_000_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_DINEUTRONIUM_BATTERY = registerBattery("machine_dineutronium_battery", 100_000_000_000_000L);
    public static final DeferredBlock<BatteryStorageBlock> MACHINE_ELECTRONIUM_BATTERY = registerBattery("machine_electronium_battery", 1_000_000_000_000_000L);
    public static final DeferredBlock<EnergyCableBlock> RED_CABLE = BLOCKS.register(
        "red_cable",
        () -> new EnergyCableBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<EnergyCableSwitchBlock> CABLE_SWITCH = BLOCKS.register(
        "cable_switch",
        () -> new EnergyCableSwitchBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<EnergyCableDetectorBlock> CABLE_DETECTOR = BLOCKS.register(
        "cable_detector",
        () -> new EnergyCableDetectorBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<EnergyCableDiodeBlock> CABLE_DIODE = BLOCKS.register(
        "cable_diode",
        () -> new EnergyCableDiodeBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<EnergyCableGaugeBlock> RED_CABLE_GAUGE = BLOCKS.register(
        "red_cable_gauge",
        () -> new EnergyCableGaugeBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<EnergyCableBlock> RED_CONNECTOR = BLOCKS.register(
        "red_connector",
        () -> new EnergyCableBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<FluidDuctBlock> FLUID_DUCT_MK2 = BLOCKS.register(
        "fluid_duct_mk2",
        () -> new FluidDuctBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<FluidDuctBlock> FLUID_DUCT_SOLID = BLOCKS.register(
        "fluid_duct_solid",
        () -> new FluidDuctBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 10.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<FluidDuctBlock> FLUID_DUCT_SOLID_SEALED = BLOCKS.register(
        "fluid_duct_solid_sealed",
        () -> new FluidDuctBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(15.0F, 10000.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<ShredderBlock> MACHINE_SHREDDER = BLOCKS.register(
        "machine_shredder",
        () -> new ShredderBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(4.0F, 20.0F)
                .sound(SoundType.METAL)
        )
    );
    public static final DeferredBlock<Block> BARREL_PLASTIC = registerBarrel("barrel_plastic", 12000, MapColor.COLOR_GRAY, SoundType.STONE);
    public static final DeferredBlock<Block> BARREL_CORRODED = registerBarrel("barrel_corroded", 6000, MapColor.COLOR_RED, SoundType.METAL);
    public static final DeferredBlock<Block> BARREL_IRON = registerBarrel("barrel_iron", 8000, MapColor.METAL, SoundType.METAL);
    public static final DeferredBlock<Block> BARREL_STEEL = registerBarrel("barrel_steel", 16000, MapColor.METAL, SoundType.METAL);
    public static final DeferredBlock<Block> BARREL_TCALLOY = registerBarrel("barrel_tcalloy", 24000, MapColor.METAL, SoundType.METAL);
    public static final DeferredBlock<Block> BARREL_ANTIMATTER = registerBarrel("barrel_antimatter", 16000, MapColor.COLOR_PURPLE, SoundType.METAL);

    public static final DeferredBlock<Block> PRESS_PREHEATER = registerMetalBlock("press_preheater", 4.0F, 20.0F);
    public static final DeferredBlock<Block> BLOCK_ADVANCED_ALLOY = registerMetalBlock("block_advanced_alloy", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_ALUMINIUM = registerMetalBlock("block_aluminium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_BERYLLIUM = registerMetalBlock("block_beryllium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_BISMUTH = registerMetalBlock("block_bismuth", 5.0F, 30.0F);
    public static final DeferredBlock<Block> BLOCK_BORON = registerMetalBlock("block_boron", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_CADMIUM = registerMetalBlock("block_cadmium", 5.0F, 30.0F);
    public static final DeferredBlock<Block> BLOCK_COBALT = registerMetalBlock("block_cobalt", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_COMBINE_STEEL = registerMetalBlock("block_combine_steel", 5.0F, 600.0F);
    public static final DeferredBlock<Block> BLOCK_COPPER = registerMetalBlock("block_copper", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_DESH = registerMetalBlock("block_desh", 5.0F, 600.0F);
    public static final DeferredBlock<Block> BLOCK_DINEUTRONIUM = registerMetalBlock("block_dineutronium", 5.0F, 120000.0F);
    public static final DeferredBlock<Block> BLOCK_DURA_STEEL = registerMetalBlock("block_dura_steel", 5.0F, 600.0F);
    public static final DeferredBlock<Block> BLOCK_EUPHEMIUM = registerMetalBlock("block_euphemium", 5.0F, 60000.0F);
    public static final DeferredBlock<Block> BLOCK_FLUORITE = registerMetalBlock("block_fluorite", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_LANTHANIUM = registerMetalBlock("block_lanthanium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_LEAD = registerMetalBlock("block_lead", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_NIOBIUM = registerMetalBlock("block_niobium", 5.0F, 30.0F);
    public static final DeferredBlock<Block> BLOCK_RED_COPPER = registerMetalBlock("block_red_copper", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_SATURNITE = registerMetalBlock("block_saturnite", 6.0F, 800.0F);
    public static final DeferredBlock<Block> BLOCK_STARMETAL = registerMetalBlock("block_starmetal", 5.0F, 600.0F);
    public static final DeferredBlock<Block> BLOCK_STEEL = registerMetalBlock("block_steel", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_TANTALIUM = registerMetalBlock("block_tantalium", 5.0F, 30.0F);
    public static final DeferredBlock<Block> BLOCK_TITANIUM = registerMetalBlock("block_titanium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_TUNGSTEN = registerMetalBlock("block_tungsten", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_ZIRCONIUM = registerMetalBlock("block_zirconium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_ACTINIUM = registerMetalBlock("block_actinium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_AUSTRALIUM = registerMetalBlock("block_australium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_BAKELITE = registerStoneBlock("block_bakelite", MapColor.STONE, 3.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_DAFFERGON = registerMetalBlock("block_daffergon", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_POLYMER = registerSimpleBlock(
        "block_polymer",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 10.0F)
            .sound(SoundType.METAL)
    );
    public static final DeferredBlock<Block> BLOCK_REIIUM = registerMetalBlock("block_reiium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_RUBBER = registerStoneBlock("block_rubber", MapColor.STONE, 3.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_UNOBTAINIUM = registerMetalBlock("block_unobtainium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_VERTICIUM = registerMetalBlock("block_verticium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_WEIDANIUM = registerMetalBlock("block_weidanium", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_NUKA = registerMetalPillarBlock("block_cap_nuka", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_QUANTUM = registerMetalPillarBlock("block_cap_quantum", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_RAD = registerMetalPillarBlock("block_cap_rad", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_SPARKLE = registerMetalPillarBlock("block_cap_sparkle", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_KORL = registerMetalPillarBlock("block_cap_korl", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_FRITZ = registerMetalPillarBlock("block_cap_fritz", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_SUNSET = registerMetalPillarBlock("block_cap_sunset", 5.0F, 10.0F);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_CAP_STAR = registerMetalPillarBlock("block_cap_star", 5.0F, 10.0F);
    // --- Bulk-ported simple pillar blocks (BlockRotatablePillar equivalents) ---
    public static final DeferredBlock<RotatedPillarBlock> CONCRETE_PILLAR = registerPillarBlock("concrete_pillar", MapColor.STONE, 15.0F, 4000.0F, SoundType.STONE);
    public static final DeferredBlock<RotatedPillarBlock> METEOR_PILLAR = registerPillarBlock("meteor_pillar", MapColor.STONE, 15.0F, 900.0F, SoundType.STONE);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_SCHRABIDIUM_CLUSTER = registerPillarBlock("block_schrabidium_cluster", MapColor.STONE, 5.0F, 60000.0F, SoundType.STONE);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_EUPHEMIUM_CLUSTER = registerPillarBlock("block_euphemium_cluster", MapColor.STONE, 5.0F, 60000.0F, SoundType.STONE);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_TRITIUM = registerPillarBlock("block_tritium", MapColor.NONE, 3.0F, 2.0F, SoundType.GLASS);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_INSULATOR = registerPillarBlock("block_insulator", MapColor.COLOR_LIGHT_GRAY, 5.0F, 10.0F, SoundType.WOOL);
    public static final DeferredBlock<RotatedPillarBlock> BLOCK_FIBERGLASS = registerPillarBlock("block_fiberglass", MapColor.COLOR_LIGHT_GRAY, 5.0F, 10.0F, SoundType.WOOL);
    public static final DeferredBlock<Block> ORE_ALUMINIUM = registerOreBlock("ore_aluminium", 1);
    public static final DeferredBlock<Block> ORE_BERYLLIUM = registerOreBlock("ore_beryllium", 2, 15.0F);
    public static final DeferredBlock<Block> ORE_CINNEBAR = registerOreBlock("ore_cinnebar", 1);
    public static final DeferredBlock<Block> ORE_COBALT = registerOreBlock("ore_cobalt", 15, 15.0F);
    public static final DeferredBlock<Block> ORE_COPPER = registerOreBlock("ore_copper", 2);
    public static final DeferredBlock<Block> ORE_FLUORITE = registerOreBlock("ore_fluorite", 1);
    public static final DeferredBlock<Block> ORE_TITANIUM = registerOreBlock("ore_titanium", 2);
    public static final DeferredBlock<Block> ORE_TUNGSTEN = registerOreBlock("ore_tungsten", 2);
    public static final DeferredBlock<Block> ORE_LEAD = registerOreBlock("ore_lead", 2);
    public static final DeferredBlock<Block> ORE_NITER = registerOreBlock("ore_niter", 1);
    public static final DeferredBlock<Block> ORE_SULFUR = registerOreBlock("ore_sulfur", 1);
    public static final DeferredBlock<Block> CRYSTAL_HARDENED = registerSimpleBlock(
        "crystal_hardened",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(15.0F, 3_600_000.0F)
            .sound(SoundType.METAL)
    );
    public static final DeferredBlock<Block> ORE_BEDROCK_OIL = registerSimpleBlock(
        "ore_bedrock_oil",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(-1.0F, 6_000_000.0F)
            .sound(SoundType.STONE)
    );
    public static final DeferredBlock<Block> ORE_OIL_EMPTY = registerStoneBlock("ore_oil_empty", MapColor.STONE, 5.0F, 10.0F);
    public static final DeferredBlock<Block> METEOR_BATTERY = registerStoneBlock("meteor_battery", MapColor.COLOR_BLACK, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_JUNGLE = registerStoneBlock("brick_jungle", MapColor.COLOR_GREEN, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BRICK_JUNGLE_CRACKED = registerStoneBlock("brick_jungle_cracked", MapColor.COLOR_GREEN, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BLOCK_NITER = registerMetalBlock("block_niter", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_SULFUR = registerMetalBlock("block_sulfur", 5.0F, 10.0F);
    public static final DeferredBlock<Block> TEKTITE = registerStoneBlock("tektite", MapColor.COLOR_BLACK, 1.5F, 10.0F);
    public static final DeferredBlock<Block> ORE_TEKTITE_OSMIRIDIUM = registerStoneBlock("ore_tektite_osmiridium", MapColor.COLOR_BLACK, 2.5F, 20.0F);
    public static final DeferredBlock<Block> DECO_TITANIUM = registerMetalBlock("deco_titanium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_RED_COPPER = registerMetalBlock("deco_red_copper", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_TUNGSTEN = registerMetalBlock("deco_tungsten", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_ALUMINIUM = registerMetalBlock("deco_aluminium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_STEEL = registerMetalBlock("deco_steel", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_LEAD = registerMetalBlock("deco_lead", 5.0F, 10.0F);
    public static final DeferredBlock<Block> DECO_BERYLLIUM = registerMetalBlock("deco_beryllium", 5.0F, 10.0F);
    public static final DeferredBlock<Block> BLOCK_SMORE = registerStoneBlock("block_smore", MapColor.COLOR_BROWN, 15.0F, 900.0F);
    public static final DeferredBlock<Block> BLOCK_FOAM = registerSimpleBlock(
        "block_foam",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .strength(0.5F, 0.0F)
            .sound(SoundType.SNOW)
    );
    public static final DeferredBlock<Block> SEAL_FRAME = registerMetalBlock("seal_frame", 10.0F, 100.0F);
    public static final DeferredBlock<Block> STRUCT_LAUNCHER = registerMetalBlock("struct_launcher", 5.0F, 10.0F);
    public static final DeferredBlock<Block> STRUCT_SCAFFOLD = registerMetalBlock("struct_scaffold", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FACTORY_TITANIUM_HULL = registerMetalBlock("factory_titanium_hull", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FACTORY_ADVANCED_HULL = registerMetalBlock("factory_advanced_hull", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FUSION_HATCH = registerMetalBlock("fusion_hatch", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FUSION_CORE_BLOCK = registerMetalBlock("fusion_core_block", 5.0F, 10.0F);
    public static final DeferredBlock<Block> WATZ_ELEMENT = registerMetalBlock("watz_element", 5.0F, 10.0F);
    public static final DeferredBlock<Block> WATZ_COOLER = registerMetalBlock("watz_cooler", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FWATZ_SCAFFOLD = registerMetalBlock("fwatz_scaffold", 5.0F, 10.0F);
    public static final DeferredBlock<Block> FWATZ_COMPUTER = registerMetalBlock("fwatz_computer", 5.0F, 10.0F);
    public static final DeferredBlock<Block> PINK_PLANKS = registerSimpleBlock(
        "pink_planks",
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)
    );

    public static final DeferredBlock<Block> CHLORINE_GAS = BLOCKS.register(
        "gas_chlorine",
        () -> new GasBlock(
            HbmHazardClass.GAS_CHLORINE,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
                .noCollission()
                .noOcclusion()
                .randomTicks()
                .replaceable()
                .pushReaction(PushReaction.DESTROY)
        )
    );

    // --- Bulk-ported simple blocks (BlockHazard equivalents; radiation behavior TODO once the radiation system exists) ---

    // --- Bulk-ported simple blocks (BlockRadResistant equivalents; radiation shielding TODO once the radiation system exists) ---
    public static final DeferredBlock<Block> REINFORCED_LIGHT = registerStoneBlock("reinforced_light", MapColor.STONE, 15.0F, 300.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BLOCK_NITER_REINFORCED = registerStoneBlock("block_niter_reinforced", MapColor.METAL, 15.0F, 6000.0F, SoundType.METAL);
    public static final DeferredBlock<Block> HAZMAT = registerStoneBlock("hazmat", MapColor.COLOR_LIGHT_GRAY, 15.0F, 100.0F, SoundType.WOOL);
    public static final DeferredBlock<Block> BLOCK_METEOR_MOLTEN = registerStoneBlock("block_meteor_molten", MapColor.STONE, 15.0F, 15.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BRICK_JUNGLE_LAVA = registerStoneBlock("brick_jungle_lava", MapColor.STONE, 15.0F, 900.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BRICK_JUNGLE_OOZE = registerStoneBlock("brick_jungle_ooze", MapColor.STONE, 15.0F, 900.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BRICK_JUNGLE_MYSTIC = registerStoneBlock("brick_jungle_mystic", MapColor.STONE, 15.0F, 900.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BLOCK_THORIUM = registerStoneBlock("block_thorium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_THORIUM_FUEL = registerStoneBlock("block_thorium_fuel", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_NEPTUNIUM = registerStoneBlock("block_neptunium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_POLONIUM = registerStoneBlock("block_polonium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_MOX_FUEL = registerStoneBlock("block_mox_fuel", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PLUTONIUM = registerStoneBlock("block_plutonium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PU238 = registerStoneBlock("block_pu238", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PU239 = registerStoneBlock("block_pu239", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PU240 = registerStoneBlock("block_pu240", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PU_MIX = registerStoneBlock("block_pu_mix", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_PLUTONIUM_FUEL = registerStoneBlock("block_plutonium_fuel", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_URANIUM = registerStoneBlock("block_uranium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_U233 = registerStoneBlock("block_u233", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_U235 = registerStoneBlock("block_u235", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_U238 = registerStoneBlock("block_u238", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_URANIUM_FUEL = registerStoneBlock("block_uranium_fuel", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_TRINITITE = registerStoneBlock("block_trinitite", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_SCHRARANIUM = registerStoneBlock("block_schraranium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_SCHRABIDIUM = registerStoneBlock("block_schrabidium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_SCHRABIDATE = registerStoneBlock("block_schrabidate", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_SOLINIUM = registerStoneBlock("block_solinium", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_SCHRABIDIUM_FUEL = registerStoneBlock("block_schrabidium_fuel", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_AU198 = registerStoneBlock("block_au198", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_MAGNETIZED_TUNGSTEN = registerStoneBlock("block_magnetized_tungsten", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> FROZEN_PLANKS = registerStoneBlock("frozen_planks", MapColor.WOOD, 0.5F, 0.5F, SoundType.WOOD);
    public static final DeferredBlock<Block> FROZEN_DIRT = registerStoneBlock("frozen_dirt", MapColor.DIRT, 0.5F, 0.5F, SoundType.GRAVEL);
    public static final DeferredBlock<Block> BLOCK_RA226 = registerStoneBlock("block_ra226", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BLOCK_RADSPICE = registerStoneBlock("block_radspice", MapColor.METAL, 5.0F, 5.0F, SoundType.METAL);
    public static final DeferredBlock<Block> BALEONITITE_SLAKED = registerStoneBlock("baleonitite_slaked", MapColor.STONE, 5.0F, 6F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_0 = registerStoneBlock("baleonitite_0", MapColor.STONE, 5.0F, 6F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_1 = registerStoneBlock("baleonitite_1", MapColor.STONE, 5.0F, 6F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_2 = registerStoneBlock("baleonitite_2", MapColor.STONE, 5.0F, 6F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_3 = registerStoneBlock("baleonitite_3", MapColor.STONE, 5.0F, 7F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_4 = registerStoneBlock("baleonitite_4", MapColor.STONE, 5.0F, 8F, SoundType.STONE);
    public static final DeferredBlock<Block> BALEONITITE_CORE = registerStoneBlock("baleonitite_core", MapColor.STONE, 10.0F, 9F, SoundType.STONE);
    public static final DeferredBlock<Block> BLOCK_WHITE_PHOSPHORUS = registerStoneBlock("block_white_phosphorus", MapColor.STONE, 5.0F, 5.0F, SoundType.STONE);
    public static final DeferredBlock<Block> BLOCK_CORIUM = registerStoneBlock("block_corium", MapColor.METAL, 100.0F, 100.0F, SoundType.METAL);

    // --- Bulk-ported ore blocks (BlockNTMOre; world generation not wired up yet - see migration notes) ---
    public static final DeferredBlock<Block> BASALT_FLUORITE = registerOreBlock("basalt_fluorite", 2);
    public static final DeferredBlock<Block> BASALT_SULFUR = registerOreBlock("basalt_sulfur", 2);
    public static final DeferredBlock<Block> BLOCK_METEOR = registerOreBlock("block_meteor", 2);
    public static final DeferredBlock<Block> BLOCK_METEOR_BROKEN = registerOreBlock("block_meteor_broken", 0);
    public static final DeferredBlock<Block> BLOCK_METEOR_COBBLE = registerOreBlock("block_meteor_cobble", 0);
    public static final DeferredBlock<Block> BLOCK_METEOR_TREASURE = registerOreBlock("block_meteor_treasure", 2);
    public static final DeferredBlock<Block> ORE_ASBESTOS = registerOreBlock("ore_asbestos", 6);
    public static final DeferredBlock<Block> ORE_AUSTRALIUM = registerOreBlock("ore_australium", 10000);
    public static final DeferredBlock<Block> ORE_COLTAN = registerOreBlock("ore_coltan", 20);
    public static final DeferredBlock<Block> ORE_DAFFERGON = registerOreBlock("ore_daffergon", 10000000);
    public static final DeferredBlock<Block> ORE_GNEISS_ASBESTOS = registerOreBlock("ore_gneiss_asbestos", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_COPPER = registerOreBlock("ore_gneiss_copper", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_GAS = registerOreBlock("ore_gneiss_gas", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_GOLD = registerOreBlock("ore_gneiss_gold", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_IRON = registerOreBlock("ore_gneiss_iron", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_LITHIUM = registerOreBlock("ore_gneiss_lithium", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_RARE = registerOreBlock("ore_gneiss_rare", 2);
    public static final DeferredBlock<Block> ORE_GNEISS_SCHRABIDIUM = registerOreBlock("ore_gneiss_schrabidium", 2);
    public static final DeferredBlock<Block> ORE_HEMATITE = registerOreBlock("ore_hematite", 2);
    public static final DeferredBlock<Block> ORE_LIGNITE = registerOreBlock("ore_lignite", 2);
    public static final DeferredBlock<Block> ORE_MALACHITE = registerOreBlock("ore_malachite", 2);
    public static final DeferredBlock<Block> ORE_METEOR_ALUMINIUM = registerOreBlock("ore_meteor_aluminium", 30);
    public static final DeferredBlock<Block> ORE_METEOR_COPPER = registerOreBlock("ore_meteor_copper", 30);
    public static final DeferredBlock<Block> ORE_METEOR_LEAD = registerOreBlock("ore_meteor_lead", 30);
    public static final DeferredBlock<Block> ORE_METEOR_LITHIUM = registerOreBlock("ore_meteor_lithium", 30);
    public static final DeferredBlock<Block> ORE_METEOR_STARMETAL = registerOreBlock("ore_meteor_starmetal", 60);
    public static final DeferredBlock<Block> ORE_METEOR_SULFUR = registerOreBlock("ore_meteor_sulfur", 30);
    public static final DeferredBlock<Block> ORE_METEOR_THORIUM = registerOreBlock("ore_meteor_thorium", 30);
    public static final DeferredBlock<Block> ORE_METEOR_TITANIUM = registerOreBlock("ore_meteor_titanium", 30);
    public static final DeferredBlock<Block> ORE_METEOR_TUNGSTEN = registerOreBlock("ore_meteor_tungsten", 30);
    public static final DeferredBlock<Block> ORE_METEOR_URANIUM = registerOreBlock("ore_meteor_uranium", 30);
    public static final DeferredBlock<Block> ORE_NETHER_COBALT = registerOreBlock("ore_nether_cobalt", 2);
    public static final DeferredBlock<Block> ORE_NETHER_FIRE = registerOreBlock("ore_nether_fire", 2);
    public static final DeferredBlock<Block> ORE_NETHER_PLUTONIUM = registerOreBlock("ore_nether_plutonium", 2);
    public static final DeferredBlock<Block> ORE_NETHER_SCHRABIDIUM = registerOreBlock("ore_nether_schrabidium", 2);
    public static final DeferredBlock<Block> ORE_NETHER_SULFUR = registerOreBlock("ore_nether_sulfur", 2);
    public static final DeferredBlock<Block> ORE_NETHER_TUNGSTEN = registerOreBlock("ore_nether_tungsten", 2);
    public static final DeferredBlock<Block> ORE_OIL = registerOreBlock("ore_oil", 2);
    public static final DeferredBlock<Block> ORE_RARE = registerOreBlock("ore_rare", 12);
    public static final DeferredBlock<Block> ORE_REIIUM = registerOreBlock("ore_reiium", 100);
    public static final DeferredBlock<Block> ORE_SCHRABIDIUM = registerOreBlock("ore_schrabidium", 300);
    public static final DeferredBlock<Block> ORE_THORIUM = registerOreBlock("ore_thorium", 2);
    public static final DeferredBlock<Block> ORE_TIKITE = registerOreBlock("ore_tikite", 2);
    public static final DeferredBlock<Block> ORE_UNOBTAINIUM = registerOreBlock("ore_unobtainium", 1000000);
    public static final DeferredBlock<Block> ORE_VERTICIUM = registerOreBlock("ore_verticium", 100000);
    public static final DeferredBlock<Block> ORE_WEIDANIUM = registerOreBlock("ore_weidanium", 1000);

    private HbmBlocks() {
    }

    private static DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties) {
        return BLOCKS.registerSimpleBlock(name, properties);
    }

    private static DeferredBlock<Block> registerStoneBlock(String name, MapColor mapColor, float destroyTime, float explosionResistance) {
        return registerStoneBlock(name, mapColor, destroyTime, explosionResistance, SoundType.STONE);
    }

    private static DeferredBlock<Block> registerStoneBlock(
        String name,
        MapColor mapColor,
        float destroyTime,
        float explosionResistance,
        SoundType soundType
    ) {
        return BLOCKS.registerSimpleBlock(
            name,
            BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .requiresCorrectToolForDrops()
                .strength(destroyTime, explosionResistance)
                .sound(soundType)
        );
    }

    private static DeferredBlock<Block> registerMetalBlock(String name, float destroyTime, float explosionResistance) {
        return BLOCKS.registerSimpleBlock(
            name,
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(destroyTime, explosionResistance)
                .sound(SoundType.METAL)
        );
    }

    private static DeferredBlock<RotatedPillarBlock> registerMetalPillarBlock(String name, float destroyTime, float explosionResistance) {
        return registerPillarBlock(name, MapColor.METAL, destroyTime, explosionResistance, SoundType.METAL);
    }

    private static DeferredBlock<RotatedPillarBlock> registerPillarBlock(
        String name,
        MapColor mapColor,
        float destroyTime,
        float explosionResistance,
        SoundType soundType
    ) {
        return BLOCKS.register(
            name,
            () -> new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(mapColor)
                    .requiresCorrectToolForDrops()
                    .strength(destroyTime, explosionResistance)
                    .sound(soundType)
            )
        );
    }

    private static DeferredBlock<Block> registerOreBlock(String name, int xp) {
        return registerOreBlock(name, xp, 10.0F);
    }

    private static DeferredBlock<Block> registerOreBlock(String name, int xp, float explosionResistance) {
        return BLOCKS.register(name, () -> new DropExperienceBlock(
            ConstantInt.of(xp),
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(5.0F, explosionResistance)
                .sound(SoundType.STONE)
        ));
    }

    private static DeferredBlock<StairBlock> registerStairsBlock(String name, DeferredBlock<? extends Block> baseBlock) {
        return BLOCKS.register(name, () -> new StairBlock(
            baseBlock.get().defaultBlockState(),
            BlockBehaviour.Properties.ofFullCopy(baseBlock.get())
        ));
    }

    private static DeferredBlock<SlabBlock> registerSlabBlock(String name, DeferredBlock<? extends Block> baseBlock) {
        return BLOCKS.register(name, () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock.get())));
    }

    private static DeferredBlock<Block> registerBarrel(String name, int capacity, MapColor mapColor, SoundType soundType) {
        return BLOCKS.register(
            name,
            () -> new FluidBarrelBlock(
                capacity,
                BlockBehaviour.Properties.of()
                    .mapColor(mapColor)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 5.0F)
                    .sound(soundType)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<BatteryStorageBlock> registerBattery(String name, long capacity) {
        return BLOCKS.register(
            name,
            () -> new BatteryStorageBlock(
                capacity,
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 10.0F)
                    .sound(SoundType.METAL)
            )
        );
    }
}
