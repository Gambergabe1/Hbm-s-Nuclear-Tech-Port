package com.hbm.main;

import com.hbm.recipe.HbmRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HbmMod.MODID)
public class HbmMod {
    public static final String MODID = "hbm";
    public static final Logger LOGGER = LogManager.getLogger();

    public HbmMod(IEventBus modEventBus) {
        // Register recipe types
        HbmRecipes.register(modEventBus);
        
        // Register mod lifecycle events
        modEventBus.addListener(this::commonSetup);
        
        // Register client-side events only on client
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ModEventHandlerClient::onClientSetup);
        }
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HBM mod setup complete!");
    }
}