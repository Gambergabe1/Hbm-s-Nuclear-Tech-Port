package com.hbm.event;

import com.hbm.item.BatteryItem;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmItems;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class HbmCapabilityEvents {
    private HbmCapabilityEvents() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            (stack, context) -> stack.getItem() instanceof BatteryItem batteryItem ? batteryItem.createEnergyStorage(stack) : null,
            HbmItems.BATTERY_GENERIC.get(),
            HbmItems.BATTERY_RED_CELL.get(),
            HbmItems.BATTERY_RED_CELL_6.get(),
            HbmItems.BATTERY_RED_CELL_24.get(),
            HbmItems.BATTERY_ADVANCED.get(),
            HbmItems.BATTERY_ADVANCED_CELL.get(),
            HbmItems.BATTERY_ADVANCED_CELL_4.get(),
            HbmItems.BATTERY_ADVANCED_CELL_12.get(),
            HbmItems.BATTERY_LITHIUM.get(),
            HbmItems.BATTERY_LITHIUM_CELL.get(),
            HbmItems.BATTERY_LITHIUM_CELL_3.get(),
            HbmItems.BATTERY_LITHIUM_CELL_6.get()
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.MACHINE_EPRESS.get(),
            (blockEntity, context) -> blockEntity.getEnergyStorage()
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.MACHINE_ELECTRIC_FURNACE.get(),
            (blockEntity, context) -> blockEntity.getEnergyStorage()
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.MACHINE_TRANSFORMER.get(),
            (blockEntity, context) -> blockEntity.getEnergyCapability()
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.MACHINE_BATTERY.get(),
            (blockEntity, context) -> blockEntity.getEnergyCapability()
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.ENERGY_TRANSPORT.get(),
            (blockEntity, context) -> blockEntity.getEnergyCapability(context)
        );
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            HbmBlockEntityTypes.MACHINE_SHREDDER.get(),
            (blockEntity, context) -> blockEntity.getEnergyStorage()
        );
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            HbmBlockEntityTypes.BARREL.get(),
            (blockEntity, context) -> blockEntity
        );
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            HbmBlockEntityTypes.FLUID_DUCT.get(),
            (blockEntity, context) -> blockEntity.getFluidCapability(context)
        );
    }
}
