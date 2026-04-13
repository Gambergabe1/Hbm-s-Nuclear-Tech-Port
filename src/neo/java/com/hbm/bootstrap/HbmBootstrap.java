package com.hbm.bootstrap;

import com.hbm.HbmNuclearTech;
import com.hbm.client.HbmClientEvents;
import com.hbm.client.HbmClientInput;
import com.hbm.entity.HbmEntityEvents;
import com.hbm.event.HbmArmorEvents;
import com.hbm.event.HbmCapabilityEvents;
import com.hbm.network.HbmNetwork;
import com.hbm.network.HbmNetworkEvents;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

public final class HbmBootstrap {
    private HbmBootstrap() {
    }

    public static void register(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(HbmBootstrap::commonSetup);
        modEventBus.addListener(HbmCapabilityEvents::registerCapabilities);
        modEventBus.addListener(HbmEntityEvents::registerAttributes);
        modEventBus.addListener(HbmEntityEvents::registerSpawnPlacements);
        modEventBus.addListener(HbmNetwork::register);
        NeoForge.EVENT_BUS.addListener(HbmNetworkEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(HbmNetworkEvents::onPlayerRespawn);
        NeoForge.EVENT_BUS.addListener(HbmNetworkEvents::onStartTracking);
        NeoForge.EVENT_BUS.addListener(HbmArmorEvents::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(HbmArmorEvents::onLivingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(HbmArmorEvents::onLivingDeath);
        NeoForge.EVENT_BUS.addListener(com.hbm.util.HazardHandler::onLivingTick);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(HbmClientEvents::registerEntityRenderers);
            modEventBus.addListener(HbmClientEvents::registerLayerDefinitions);
            modEventBus.addListener(HbmClientEvents::registerGuiLayers);
            modEventBus.addListener(HbmClientEvents::registerKeyMappings);
            modEventBus.addListener(HbmClientEvents::registerMenuScreens);
            modEventBus.addListener(HbmClientEvents::registerClientExtensions);
            modEventBus.addListener(HbmClientEvents::registerItemColors);
            NeoForge.EVENT_BUS.addListener(HbmClientInput::onClientTick);
            NeoForge.EVENT_BUS.addListener(HbmClientEvents::onClientLogout);
        }

        HbmNuclearTech.LOGGER.info(
            "Initializing {} for NeoForge 1.21.1 with version {}",
            HbmNuclearTech.MODID,
            modContainer.getModInfo().getVersion()
        );
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> HbmNuclearTech.LOGGER.info(
            "Lifecycle scaffold online: bootstrap, attachments, and packet registration now run through NeoForge entrypoints"
        ));
    }
}
