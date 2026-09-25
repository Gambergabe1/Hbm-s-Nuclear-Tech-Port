package com.hbm;

import org.slf4j.Logger;

import com.hbm.bootstrap.HbmBootstrap;
import com.hbm.config.HbmCommonConfig;
import com.hbm.config.RadiationConfig;
import com.hbm.registry.HbmRegistries;
import com.hbm.registry.HbmRBMKBlocks;
import com.hbm.registry.HbmRBMKItems;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(HbmNuclearTech.MODID)
public final class HbmNuclearTech {
    public static final String MODID = "hbm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HbmNuclearTech(IEventBus modEventBus, ModContainer modContainer) {
        HbmBootstrap.register(modEventBus, modContainer);
        HbmRegistries.register(modEventBus);

        // Entity attachments (radiation data etc) are registered by
        // HbmRegistries via HbmAttachmentTypes.ATTACHMENT_TYPES above.

        // Register RBMK reactor system
        HbmRBMKBlocks.BLOCKS.register(modEventBus);
        HbmRBMKBlocks.BLOCK_ENTITIES.register(modEventBus);
        HbmRBMKItems.ITEMS.register(modEventBus);
        
        // Register radiation config
        modContainer.registerConfig(ModConfig.Type.COMMON, HbmCommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, RadiationConfig.SPEC);
    }
}
