package com.hbm.registry;

import net.neoforged.bus.api.IEventBus;

public final class HbmRegistries {
    private HbmRegistries() {
    }

    public static void register(IEventBus modEventBus) {
        HbmFluids.FLUID_TYPES.register(modEventBus);
        HbmFluids.FLUIDS.register(modEventBus);
        HbmFluidBlocks.BLOCKS.register(modEventBus);
        HbmFluidItems.ITEMS.register(modEventBus);
        HbmDataComponents.COMPONENTS.register(modEventBus);
        HbmAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        HbmArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        HbmMobEffects.MOB_EFFECTS.register(modEventBus);
        HbmBlocks.BLOCKS.register(modEventBus);
        HbmItems.ITEMS.register(modEventBus);
        HbmBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        HbmMenuTypes.MENU_TYPES.register(modEventBus);
        HbmEntityTypes.ENTITY_TYPES.register(modEventBus);
        HbmCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        HbmFeatures.FEATURES.register(modEventBus);
    }
}
