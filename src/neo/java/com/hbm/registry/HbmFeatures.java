package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.world.feature.DepthDepositFeature;
import com.hbm.world.feature.OilBubbleFeature;
import com.hbm.world.feature.OilSpotFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, HbmNuclearTech.MODID);

    public static final DeferredHolder<Feature<?>, Feature<com.hbm.world.feature.config.DepthDepositConfig>> DEPTH_DEPOSIT =
        register("depth_deposit", DepthDepositFeature::new);
    public static final DeferredHolder<Feature<?>, Feature<com.hbm.world.feature.config.OilSpotConfig>> OIL_SPOT =
        register("oil_spot", OilSpotFeature::new);
    public static final DeferredHolder<Feature<?>, Feature<com.hbm.world.feature.config.OilBubbleConfig>> OIL_BUBBLE =
        register("oil_bubble", OilBubbleFeature::new);

    private static <C extends FeatureConfiguration> DeferredHolder<Feature<?>, Feature<C>> register(
        String name,
        java.util.function.Supplier<? extends Feature<C>> supplier
    ) {
        return FEATURES.register(name, supplier::get);
    }

    private HbmFeatures() {
    }
}
