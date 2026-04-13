package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.block.rbmk.*;
import com.hbm.blockentity.rbmk.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all RBMK reactor components.
 */
public class HbmRBMKBlocks {
    
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HbmNuclearTech.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, HbmNuclearTech.MODID);
    
    // =====RBMK BLOCKS=====
    
    public static final DeferredBlock<Block> RBMK_FUEL_ROD = registerBlock(
        "rbmk_fuel_rod",
        () -> new RBMKFuelRodBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(3.0F, 6.0F)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops())
    );
    
    public static final DeferredBlock<Block> RBMK_MODERATOR = registerBlock(
        "rbmk_moderator",
        () -> new RBMKModeratorBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .strength(2.0F, 4.0F)
            .sound(SoundType.STONE))
    );
    
    public static final DeferredBlock<Block> RBMK_CONTROL_ROD = registerBlock(
        "rbmk_control_rod",
        () -> new RBMKControlRodBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(3.0F, 6.0F)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops())
    );
    
    public static final DeferredBlock<Block> RBMK_BOILER = registerBlock(
        "rbmk_boiler",
        () -> new RBMKBoilerBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(3.0F, 6.0F)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops())
    );
    
    public static final DeferredBlock<Block> RBMK_CONSOLE = registerBlock(
        "rbmk_console",
        () -> new RBMKConsoleBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(4.0F, 8.0F)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops())
    );
    
    public static final DeferredBlock<Block> RBMK_REFLECTOR = registerBlock(
        "rbmk_reflector",
        () -> new RBMKModeratorBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_GRAY)
            .strength(2.5F, 5.0F)
            .sound(SoundType.METAL))
    );
    
    public static final DeferredBlock<Block> RBMK_ABSORBER = registerBlock(
        "rbmk_absorber",
        () -> new RBMKControlRodBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BROWN)
            .strength(3.0F, 6.0F)
            .sound(SoundType.METAL))
    );
    
    // =====RBMK BLOCK ENTITIES=====
    
    public static final Supplier<BlockEntityType<RBMKFuelRodBlockEntity>> RBMK_FUEL_ROD_BE = BLOCK_ENTITIES.register(
        "rbmk_fuel_rod",
        () -> BlockEntityType.Builder.of(RBMKFuelRodBlockEntity::new, RBMK_FUEL_ROD.get()).build(null)
    );
    
    public static final Supplier<BlockEntityType<RBMKModeratorBlockEntity>> RBMK_MODERATOR_BE = BLOCK_ENTITIES.register(
        "rbmk_moderator",
        () -> BlockEntityType.Builder.of(RBMKModeratorBlockEntity::new, RBMK_MODERATOR.get(), RBMK_REFLECTOR.get()).build(null)
    );
    
    public static final Supplier<BlockEntityType<RBMKControlRodBlockEntity>> RBMK_CONTROL_ROD_BE = BLOCK_ENTITIES.register(
        "rbmk_control_rod",
        () -> BlockEntityType.Builder.of(RBMKControlRodBlockEntity::new, RBMK_CONTROL_ROD.get(), RBMK_ABSORBER.get()).build(null)
    );
    
    public static final Supplier<BlockEntityType<RBMKBoilerBlockEntity>> RBMK_BOILER_BE = BLOCK_ENTITIES.register(
        "rbmk_boiler",
        () -> BlockEntityType.Builder.of(RBMKBoilerBlockEntity::new, RBMK_BOILER.get()).build(null)
    );
    
    public static final Supplier<BlockEntityType<RBMKConsoleBlockEntity>> RBMK_CONSOLE_BE = BLOCK_ENTITIES.register(
        "rbmk_console",
        () -> BlockEntityType.Builder.of(RBMKConsoleBlockEntity::new, RBMK_CONSOLE.get()).build(null)
    );
    
    // =====HELPER METHODS=====
    
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        HbmRBMKItems.ITEMS.register(name, () -> new net.minecraft.world.item.BlockItem(
            block.get(),
            new net.minecraft.world.item.Item.Properties()
        ));
    }
}
