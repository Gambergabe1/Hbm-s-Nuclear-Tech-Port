package com.hbm.client;

import com.hbm.client.model.CyberCrabModel;
import com.hbm.client.overlay.HbmGuiLayers;
import com.hbm.client.renderer.blockentity.FluidBarrelBlockEntityRenderer;
import com.hbm.client.renderer.blockentity.PressBlockEntityRenderer;
import com.hbm.client.screen.BurnerPressScreen;
import com.hbm.client.screen.BatteryStorageScreen;
import com.hbm.client.renderer.CyberCrabRenderer;
import com.hbm.client.renderer.GlowingOneRenderer;
import com.hbm.client.renderer.NuclearCreeperRenderer;
import com.hbm.client.renderer.TaintedCreeperRenderer;
import com.hbm.client.state.HbmClientState;
import com.hbm.registry.HbmFluidItems;
import com.hbm.registry.HbmFluids;
import com.hbm.client.screen.CrateDeshScreen;
import com.hbm.client.screen.CrateIronScreen;
import com.hbm.client.screen.CrateSteelScreen;
import com.hbm.client.screen.ElectricFurnaceScreen;
import com.hbm.client.screen.ElectricPressScreen;
import com.hbm.client.screen.FluidBarrelScreen;
import com.hbm.client.screen.SafeScreen;
import com.hbm.client.screen.ShredderScreen;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmEntityTypes;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

public final class HbmClientEvents {
    private HbmClientEvents() {
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_GLOWING_ONE.get(), GlowingOneRenderer::new);
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_NUCLEAR_CREEPER.get(), NuclearCreeperRenderer::new);
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_TAINTED_CREEPER.get(), TaintedCreeperRenderer::new);
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_CYBER_CRAB.get(), CyberCrabRenderer::new);
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_GRENADE_GENERIC.get(), context -> new ThrownItemRenderer<>(context, 1.0F, false));
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_AA_SHELL.get(), context -> new ThrownItemRenderer<>(context, 1.0F, false));
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_ROCKET.get(), context -> new ThrownItemRenderer<>(context, 1.0F, false));
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_FALLING_NUKE.get(), context -> new ThrownItemRenderer<>(context, 1.0F, false));
        event.registerEntityRenderer(HbmEntityTypes.ENTITY_CHOPPER_MINE.get(), context -> new ThrownItemRenderer<>(context, 1.0F, false));
        event.registerBlockEntityRenderer(HbmBlockEntityTypes.MACHINE_PRESS.get(), PressBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(HbmBlockEntityTypes.MACHINE_EPRESS.get(), PressBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(HbmBlockEntityTypes.BARREL.get(), FluidBarrelBlockEntityRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CyberCrabModel.LAYER_LOCATION, CyberCrabModel::createBodyLayer);
    }

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        HbmGuiLayers.register(event);
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        HbmKeyMappings.register(event);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(HbmMenuTypes.CRATE_DESH.get(), CrateDeshScreen::new);
        event.register(HbmMenuTypes.CRATE_IRON.get(), CrateIronScreen::new);
        event.register(HbmMenuTypes.CRATE_STEEL.get(), CrateSteelScreen::new);
        event.register(HbmMenuTypes.SAFE.get(), SafeScreen::new);
        event.register(HbmMenuTypes.MACHINE_PRESS.get(), BurnerPressScreen::new);
        event.register(HbmMenuTypes.MACHINE_EPRESS.get(), ElectricPressScreen::new);
        event.register(HbmMenuTypes.MACHINE_ELECTRIC_FURNACE.get(), ElectricFurnaceScreen::new);
        event.register(HbmMenuTypes.MACHINE_BATTERY.get(), BatteryStorageScreen::new);
        event.register(HbmMenuTypes.MACHINE_SHREDDER.get(), ShredderScreen::new);
        event.register(HbmMenuTypes.BARREL.get(), FluidBarrelScreen::new);
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        HbmFluids.registerClientExtensions(event);
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        if (HbmFluidItems.bucketItems().length > 0) {
            event.register(new DynamicFluidContainerModel.Colors(), HbmFluidItems.bucketItems());
        }
    }

    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        HbmClientState.clearTransientState();
    }
}
