package com.hbm.api.fluid;

import com.hbm.HbmNuclearTech;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public final class HbmFluidTraits {
    private static final int HOT_TEMPERATURE = 373;

    private HbmFluidTraits() {
    }

    public enum Trait {
        ANTIMATTER("traits/antimatter"),
        CORROSIVE("traits/corrosive"),
        HIGHLY_CORROSIVE("traits/corrosive_2"),
        NO_CONTAINER("traits/no_container"),
        NO_ID("traits/no_id");

        private final TagKey<Fluid> tag;

        Trait(String path) {
            this.tag = TagKey.create(
                Registries.FLUID,
                ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, path)
            );
        }

        public TagKey<Fluid> tag() {
            return tag;
        }
    }

    public enum ContainerMaterial {
        PLASTIC,
        IRON,
        STEEL,
        TCALLOY,
        ANTIMATTER
    }

    public enum ContainerReaction {
        SAFE,
        CORRODES,
        DESTROYS,
        EXPLODES
    }

    public static boolean contains(FluidStack stack, Trait trait) {
        return !stack.isEmpty() && stack.is(trait.tag());
    }

    public static boolean contains(Fluid fluid, Trait trait) {
        return fluid != null && fluid.builtInRegistryHolder().is(trait.tag());
    }

    public static boolean isAntimatter(FluidStack stack) {
        return contains(stack, Trait.ANTIMATTER);
    }

    public static boolean isCorrosivePlastic(FluidStack stack) {
        return contains(stack, Trait.CORROSIVE) || contains(stack, Trait.HIGHLY_CORROSIVE);
    }

    public static boolean isCorrosiveIron(FluidStack stack) {
        return contains(stack, Trait.HIGHLY_CORROSIVE);
    }

    public static boolean isHot(FluidStack stack) {
        return !stack.isEmpty() && stack.getFluidType().getTemperature(stack) >= HOT_TEMPERATURE;
    }

    public static boolean noContainer(FluidStack stack) {
        return contains(stack, Trait.NO_CONTAINER);
    }

    public static boolean noId(FluidStack stack) {
        return contains(stack, Trait.NO_ID);
    }

    public static ContainerReaction getContainerReaction(FluidStack stack, ContainerMaterial material) {
        if (stack.isEmpty() || material == null) {
            return ContainerReaction.SAFE;
        }

        if (isAntimatter(stack)) {
            return material == ContainerMaterial.ANTIMATTER ? ContainerReaction.SAFE : ContainerReaction.EXPLODES;
        }

        return switch (material) {
            case PLASTIC -> isCorrosivePlastic(stack) || isHot(stack) ? ContainerReaction.DESTROYS : ContainerReaction.SAFE;
            case IRON -> isCorrosivePlastic(stack) ? ContainerReaction.CORRODES : ContainerReaction.SAFE;
            case STEEL -> isCorrosiveIron(stack) ? ContainerReaction.CORRODES : ContainerReaction.SAFE;
            case TCALLOY, ANTIMATTER -> ContainerReaction.SAFE;
        };
    }
}
