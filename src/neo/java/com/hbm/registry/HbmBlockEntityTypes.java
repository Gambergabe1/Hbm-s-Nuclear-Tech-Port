package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.blockentity.BatteryStorageBlockEntity;
import com.hbm.blockentity.BurnerPressBlockEntity;
import com.hbm.blockentity.DeshCrateBlockEntity;
import com.hbm.blockentity.EnergyCableBlockEntity;
import com.hbm.blockentity.ElectricFurnaceBlockEntity;
import com.hbm.blockentity.ElectricPressBlockEntity;
import com.hbm.blockentity.FluidBarrelBlockEntity;
import com.hbm.blockentity.FluidDuctBlockEntity;
import com.hbm.blockentity.IronCrateBlockEntity;
import com.hbm.blockentity.SafeBlockEntity;
import com.hbm.blockentity.ShredderBlockEntity;
import com.hbm.blockentity.SteelCrateBlockEntity;
import com.hbm.blockentity.TransformerChargerBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HbmNuclearTech.MODID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IronCrateBlockEntity>> CRATE_IRON =
        BLOCK_ENTITY_TYPES.register(
            "crate_iron",
            () -> BlockEntityType.Builder.of(IronCrateBlockEntity::new, HbmBlocks.CRATE_IRON.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeshCrateBlockEntity>> CRATE_DESH =
        BLOCK_ENTITY_TYPES.register(
            "crate_desh",
            () -> BlockEntityType.Builder.of(DeshCrateBlockEntity::new, HbmBlocks.CRATE_DESH.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SafeBlockEntity>> SAFE =
        BLOCK_ENTITY_TYPES.register(
            "safe",
            () -> BlockEntityType.Builder.of(SafeBlockEntity::new, HbmBlocks.SAFE.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SteelCrateBlockEntity>> CRATE_STEEL =
        BLOCK_ENTITY_TYPES.register(
            "crate_steel",
            () -> BlockEntityType.Builder.of(SteelCrateBlockEntity::new, HbmBlocks.CRATE_STEEL.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BurnerPressBlockEntity>> MACHINE_PRESS =
        BLOCK_ENTITY_TYPES.register(
            "machine_press",
            () -> BlockEntityType.Builder.of(BurnerPressBlockEntity::new, HbmBlocks.MACHINE_PRESS.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricPressBlockEntity>> MACHINE_EPRESS =
        BLOCK_ENTITY_TYPES.register(
            "machine_epress",
            () -> BlockEntityType.Builder.of(ElectricPressBlockEntity::new, HbmBlocks.MACHINE_EPRESS.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFurnaceBlockEntity>> MACHINE_ELECTRIC_FURNACE =
        BLOCK_ENTITY_TYPES.register(
            "tileentity_machine_electric_furnace",
            () -> BlockEntityType.Builder.of(ElectricFurnaceBlockEntity::new, HbmBlocks.MACHINE_ELECTRIC_FURNACE.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TransformerChargerBlockEntity>> MACHINE_TRANSFORMER =
        BLOCK_ENTITY_TYPES.register(
            "machine_transformer",
            () -> BlockEntityType.Builder.of(
                TransformerChargerBlockEntity::new,
                HbmBlocks.MACHINE_TRANSFORMER.get(),
                HbmBlocks.MACHINE_TRANSFORMER_20.get(),
                HbmBlocks.MACHINE_TRANSFORMER_DNT.get(),
                HbmBlocks.MACHINE_TRANSFORMER_DNT_20.get()
            ).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BatteryStorageBlockEntity>> MACHINE_BATTERY =
        BLOCK_ENTITY_TYPES.register(
            "machine_battery",
            () -> BlockEntityType.Builder.of(
                BatteryStorageBlockEntity::new,
                HbmBlocks.MACHINE_BATTERY_POTATO.get(),
                HbmBlocks.MACHINE_BATTERY.get(),
                HbmBlocks.MACHINE_LITHIUM_BATTERY.get(),
                HbmBlocks.MACHINE_DESH_BATTERY.get(),
                HbmBlocks.MACHINE_SATURNITE_BATTERY.get(),
                HbmBlocks.MACHINE_SCHRABIDIUM_BATTERY.get(),
                HbmBlocks.MACHINE_EUPHEMIUM_BATTERY.get(),
                HbmBlocks.MACHINE_RADSPICE_BATTERY.get(),
                HbmBlocks.MACHINE_DINEUTRONIUM_BATTERY.get(),
                HbmBlocks.MACHINE_ELECTRONIUM_BATTERY.get()
            ).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyCableBlockEntity>> ENERGY_TRANSPORT =
        BLOCK_ENTITY_TYPES.register(
            "energy_transport",
            () -> BlockEntityType.Builder.of(
                EnergyCableBlockEntity::new,
                HbmBlocks.RED_CABLE.get(),
                HbmBlocks.CABLE_SWITCH.get(),
                HbmBlocks.CABLE_DETECTOR.get(),
                HbmBlocks.CABLE_DIODE.get(),
                HbmBlocks.RED_CABLE_GAUGE.get(),
                HbmBlocks.RED_CONNECTOR.get()
            ).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidDuctBlockEntity>> FLUID_DUCT =
        BLOCK_ENTITY_TYPES.register(
            "fluid_duct",
            () -> BlockEntityType.Builder.of(
                FluidDuctBlockEntity::new,
                HbmBlocks.FLUID_DUCT_MK2.get(),
                HbmBlocks.FLUID_DUCT_SOLID.get(),
                HbmBlocks.FLUID_DUCT_SOLID_SEALED.get()
            ).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShredderBlockEntity>> MACHINE_SHREDDER =
        BLOCK_ENTITY_TYPES.register(
            "machine_shredder",
            () -> BlockEntityType.Builder.of(ShredderBlockEntity::new, HbmBlocks.MACHINE_SHREDDER.get()).build(null)
        );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidBarrelBlockEntity>> BARREL =
        BLOCK_ENTITY_TYPES.register(
            "barrel",
            () -> BlockEntityType.Builder.of(FluidBarrelBlockEntity::new,
                HbmBlocks.BARREL_PLASTIC.get(),
                HbmBlocks.BARREL_CORRODED.get(),
                HbmBlocks.BARREL_IRON.get(),
                HbmBlocks.BARREL_STEEL.get(),
                HbmBlocks.BARREL_TCALLOY.get(),
                HbmBlocks.BARREL_ANTIMATTER.get()
            ).build(null)
        );

    private HbmBlockEntityTypes() {
    }
}
