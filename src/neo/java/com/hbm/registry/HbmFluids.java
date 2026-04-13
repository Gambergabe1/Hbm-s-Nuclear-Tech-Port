package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.fluid.HbmFluidType;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public final class HbmFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
        NeoForgeRegistries.Keys.FLUID_TYPES,
        HbmNuclearTech.MODID
    );
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, HbmNuclearTech.MODID);
    private static final Map<String, FluidEntry> ENTRIES = new LinkedHashMap<>();

    // Full legacy ModForgeFluids id catalog, with Neo fluid ids/temperatures restored.
    public static final FluidEntry SPENTSTEAM = register(gasTank("spentsteam").withTemperature(40 + 273));
    public static final FluidEntry STEAM = register(gasTank("steam").withTemperature(100 + 273));
    public static final FluidEntry HOTSTEAM = register(gasTank("hotsteam").withTemperature(300 + 273));
    public static final FluidEntry SUPERHOTSTEAM = register(gasTank("superhotsteam").withTemperature(450 + 273));
    public static final FluidEntry ULTRAHOTSTEAM = register(gasTank("ultrahotsteam").withTemperature(600 + 273));
    public static final FluidEntry COOLANT = register(tank("coolant").withTemperature(203));
    public static final FluidEntry HOTCOOLANT = register(tank("hotcoolant").withTemperature(400 + 273));
    public static final FluidEntry PERFLUOROMETHYL = register(tank("perfluoromethyl").withTemperature(15 + 273));

    public static final FluidEntry HEAVYWATER = register(tank("heavywater"));
    public static final FluidEntry DEUTERIUM = register(gasTank("deuterium"));
    public static final FluidEntry TRITIUM = register(gasTank("tritium"));

    public static final FluidEntry OIL = register(tank("oil"));
    public static final FluidEntry HOTOIL = register(tank("hotoil").withTemperature(350 + 273));
    public static final FluidEntry CRACKOIL = register(tank("crackoil"));
    public static final FluidEntry HOTCRACKOIL = register(tank("hotcrackoil").withTemperature(350 + 273));
    public static final FluidEntry OIL_DS = register(tank("oil_ds"));
    public static final FluidEntry HOTOIL_DS = register(tank("hotoil_ds"));
    public static final FluidEntry CRACKOIL_DS = register(tank("crackoil_ds"));
    public static final FluidEntry HOTCRACKOIL_DS = register(tank("hotcrackoil_ds"));
    public static final FluidEntry OIL_COKER = register(tank("oil_coker"));

    public static final FluidEntry HEAVYOIL = register(tank("heavyoil"));
    public static final FluidEntry HEAVYOIL_VACUUM = register(tank("heavyoil_vacuum"));
    public static final FluidEntry BITUMEN = register(tank("bitumen"));
    public static final FluidEntry SMEAR = register(tank("smear"));
    public static final FluidEntry HEATINGOIL = register(tank("heatingoil"));
    public static final FluidEntry HEATINGOIL_VACUUM = register(tank("heatingoil_vacuum"));

    public static final FluidEntry RECLAIMED = register(tank("reclaimed"));
    public static final FluidEntry PETROIL = register(tank("petroil"));

    public static final FluidEntry FRACKSOL = register(tank("fracksol"));
    public static final FluidEntry LUBRICANT = register(tank("lubricant"));

    public static final FluidEntry NAPHTHA = register(tank("naphtha"));
    public static final FluidEntry NAPHTHA_CRACK = register(tank("naphtha_crack"));
    public static final FluidEntry NAPHTHA_DS = register(tank("naphtha_ds"));
    public static final FluidEntry NAPHTHA_COKER = register(tank("naphtha_coker"));

    public static final FluidEntry DIESEL = register(tank("diesel"));
    public static final FluidEntry DIESEL_CRACK = register(tank("diesel_crack"));
    public static final FluidEntry DIESEL_REFORM = register(tank("diesel_reform"));
    public static final FluidEntry DIESEL_CRACK_REFORM = register(tank("diesel_crack_reform"));

    public static final FluidEntry LIGHTOIL = register(tank("lightoil"));
    public static final FluidEntry LIGHTOIL_CRACK = register(tank("lightoil_crack"));
    public static final FluidEntry LIGHTOIL_DS = register(tank("lightoil_ds"));
    public static final FluidEntry LIGHTOIL_VACUUM = register(tank("lightoil_vacuum"));
    public static final FluidEntry KEROSENE = register(tank("kerosene"));
    public static final FluidEntry KEROSENE_REFORM = register(tank("kerosene_reform"));

    public static final FluidEntry GAS = register(gasTank("gas"));
    public static final FluidEntry GAS_COKER = register(gasTank("gas_coker"));
    public static final FluidEntry PETROLEUM = register(gasTank("petroleum"));

    public static final FluidEntry AROMATICS = register(tank("aromatics"));
    public static final FluidEntry UNSATURATEDS = register(tank("unsaturateds"));
    public static final FluidEntry XYLENE = register(gasTank("xylene"));

    public static final FluidEntry CHLORINE = register(gasTank("chlorine"));
    public static final FluidEntry PHOSGENE = register(gasTank("phosgene"));
    public static final FluidEntry WOODOIL = register(tank("woodoil"));
    public static final FluidEntry COALCREOSOTE = register(tank("coalcreosote"));
    public static final FluidEntry COALOIL = register(tank("coaloil"));
    public static final FluidEntry COALGAS = register(gasTank("coalgas"));
    public static final FluidEntry COALGAS_LEADED = register(gasTank("coalgas_leaded"));
    public static final FluidEntry PETROIL_LEADED = register(tank("petroil_leaded"));
    public static final FluidEntry GASOLINE_LEADED = register(tank("gasoline_leaded"));
    public static final FluidEntry SYNGAS = register(gasTank("syngas"));

    public static final FluidEntry REFORMATE = register(tank("reformate"));
    public static final FluidEntry REFORMGAS = register(gasTank("reformgas"));

    public static final FluidEntry BIOGAS = register(gasTank("biogas"));
    public static final FluidEntry BIOFUEL = register(tank("biofuel"));
    public static final FluidEntry SOURGAS = register(gasTank("sourgas"));

    public static final FluidEntry ETHANOL = register(tank("ethanol"));
    public static final FluidEntry FISHOIL = register(tank("fishoil"));
    public static final FluidEntry SUNFLOWEROIL = register(tank("sunfloweroil"));
    public static final FluidEntry COLLOID = register(tank("colloid"));

    public static final FluidEntry NITAN = register(tank("nitan"));

    public static final FluidEntry UF6 = register(gasTank("uf6"));
    public static final FluidEntry PUF6 = register(gasTank("puf6"));
    public static final FluidEntry SAS3 = register(gasTank("sas3"));

    public static final FluidEntry AMAT = register(tank("amat"));
    public static final FluidEntry ASCHRAB = register(tank("aschrab"));

    public static final FluidEntry ACID = register(tank("acid"));
    public static final FluidEntry SULFURIC_ACID = register(tank("sulfuric_acid"));
    public static final FluidEntry NITRIC_ACID = register(tank("nitric_acid"));
    public static final FluidEntry SOLVENT = register(tank("solvent"));
    public static final FluidEntry RADIOSOLVENT = register(tank("radiosolvent"));
    public static final FluidEntry NITROGLYCERIN = register(tank("nitroglycerin"));

    public static final FluidEntry LIQUID_OSMIRIDIUM = register(tank("liquid_osmiridium").withTemperature(573));
    public static final FluidEntry CRYOGEL = register(tank("cryogel").withTemperature(50));

    public static final FluidEntry HYDROGEN = register(gasTank("hydrogen"));
    public static final FluidEntry OXYGEN = register(gasTank("oxygen"));
    public static final FluidEntry XENON = register(gasTank("xenon"));
    public static final FluidEntry BALEFIRE = register(tank("balefire").withTemperature(15000 + 273).withLightLevel(15));

    public static final FluidEntry MERCURY = register(tank("mercury"));

    public static final FluidEntry PLASMA_HD = register(gasTank("plasma_hd").withTemperature(25000 + 273).withLightLevel(15));
    public static final FluidEntry PLASMA_HT = register(gasTank("plasma_ht").withTemperature(30000 + 273).withLightLevel(15));
    public static final FluidEntry PLASMA_DT = register(gasTank("plasma_dt").withTemperature(32500 + 273).withLightLevel(15));
    public static final FluidEntry PLASMA_PUT = register(gasTank("plasma_put").withTemperature(50000 + 273).withLightLevel(15));
    public static final FluidEntry PLASMA_XM = register(gasTank("plasma_xm").withTemperature(45000 + 273).withLightLevel(15));
    public static final FluidEntry PLASMA_BF = register(gasTank("plasma_bf").withTemperature(85000 + 273).withLightLevel(15));

    public static final FluidEntry IONGEL = register(tank("iongel"));
    public static final FluidEntry UU_MATTER = register(tank("ic2uu_matter").withTemperature(1000000 + 273));

    public static final FluidEntry PAIN = register(tank("pain"));
    public static final FluidEntry WASTEFLUID = register(tank("wastefluid"));
    public static final FluidEntry WASTEGAS = register(tank("wastegas"));
    public static final FluidEntry GASOLINE = register(tank("gasoline"));
    public static final FluidEntry EXPERIENCE = register(tank("experience"));
    public static final FluidEntry ENDERJUICE = register(tank("ender"));

    public static final FluidEntry TOXIC_FLUID = register(
        blockFluid("toxic_fluid", "toxic_still", "toxic_flowing")
            .withDensity(2500)
            .withViscosity(2000)
            .withTemperature(70 + 273)
            .withBucketId("bucket_toxic")
    );
    public static final FluidEntry RADWATER_FLUID = register(
        blockFluid("radwater_fluid", "minecraft:block/water_still", "minecraft:block/water_flow")
            .withDensity(1000)
            .withBucketId("bucket_radwater_fluid")
    );
    public static final FluidEntry MUD_FLUID = register(
        blockFluid("mud_fluid", "mud_still", "mud_flowing")
            .withDensity(2500)
            .withViscosity(3000)
            .withLightLevel(5)
            .withTemperature(1773)
            .withBucketId("bucket_mud")
    );
    public static final FluidEntry SCHRABIDIC = register(
        blockFluid("schrabidic", "schrabidic_acid_still", "schrabidic_acid_flowing")
            .withDensity(31200)
            .withViscosity(500)
            .withLightLevel(15)
            .withBucketId("bucket_schrabidic")
    );
    public static final FluidEntry CORIUM_FLUID = register(
        blockFluid("corium_fluid", "corium_still", "corium_flowing")
            .withDensity(31200)
            .withViscosity(2000)
            .withTemperature(3000 + 273)
            .withBucketId("bucket_corium")
    );
    public static final FluidEntry VOLCANIC_LAVA_FLUID = register(
        blockFluid("volcanic_lava_fluid", "volcanic_lava_still", "volcanic_lava_flowing")
            .withDensity(3000)
            .withViscosity(3000)
            .withTemperature(1300 + 273)
            .withLightLevel(15)
            .withBucketId("bucket_volcanic_lava")
    );

    private HbmFluids() {
    }

    public static Collection<FluidEntry> all() {
        return Collections.unmodifiableCollection(ENTRIES.values());
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for (FluidEntry entry : ENTRIES.values()) {
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return entry.fluidType.get().stillTexture();
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return entry.fluidType.get().flowingTexture();
                }

                @Override
                public int getTintColor() {
                    return entry.fluidType.get().tintColor();
                }
            }, entry.fluidType.get());
        }
    }

    private static FluidEntry register(FluidSpec spec) {
        AtomicReference<DeferredHolder<Fluid, ? extends Fluid>> stillHolder = new AtomicReference<>();
        AtomicReference<DeferredHolder<Fluid, ? extends Fluid>> flowingHolder = new AtomicReference<>();
        AtomicReference<DeferredBlock<LiquidBlock>> blockHolder = new AtomicReference<>();
        AtomicReference<DeferredItem<Item>> bucketHolder = new AtomicReference<>();

        DeferredHolder<FluidType, HbmFluidType> fluidType = FLUID_TYPES.register(
            spec.name,
            () -> new HbmFluidType(
                texture(spec.stillTexture),
                texture(spec.flowingTexture),
                0xFFFFFFFF,
                createProperties(spec)
            )
        );

        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(
            fluidType,
            () -> stillHolder.get().get(),
            () -> flowingHolder.get().get()
        )
            .tickRate(5)
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .explosionResistance(100.0F);

        if (spec.hasWorldBlock()) {
            properties.block(() -> blockHolder.get().get());
        }
        if (spec.hasBucket()) {
            properties.bucket(() -> bucketHolder.get().get());
        }

        stillHolder.set(FLUIDS.register(spec.name, () -> new BaseFlowingFluid.Source(properties)));
        flowingHolder.set(FLUIDS.register("flowing_" + spec.name, () -> new BaseFlowingFluid.Flowing(properties)));

        FluidEntry entry = new FluidEntry(
            spec,
            fluidType,
            stillHolder.get(),
            flowingHolder.get()
        );

        if (spec.hasWorldBlock()) {
            blockHolder.set(HbmFluidBlocks.register("fluid_" + spec.name, () -> (FlowingFluid) stillHolder.get().get()));
            entry.block = blockHolder.get();
        }
        if (spec.hasBucket()) {
            bucketHolder.set(HbmFluidItems.registerBucket(spec.bucketId, () -> stillHolder.get().get()));
            entry.bucket = bucketHolder.get();
        }

        ENTRIES.put(spec.name, entry);
        return entry;
    }

    private static FluidType.Properties createProperties(FluidSpec spec) {
        boolean lavaSounds = spec.temperature >= 1300;
        FluidType.Properties properties = FluidType.Properties.create()
            .descriptionId("fluid." + spec.name)
            .temperature(spec.temperature)
            .density(spec.density)
            .viscosity(spec.viscosity)
            .lightLevel(spec.lightLevel)
            .sound(SoundActions.BUCKET_FILL, lavaSounds ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, lavaSounds ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY);

        if (spec.density <= 0) {
            properties.canDrown(false).canSwim(false).canPushEntity(false);
        }

        return properties;
    }

    private static FluidSpec tank(String name) {
        return simple(name).withoutPlacement();
    }

    private static FluidSpec gasTank(String name) {
        return gas(name).withoutPlacement();
    }

    private static FluidSpec simple(String name) {
        return new FluidSpec(name, name, name, 300, 1000, 1000, 0, "bucket_" + name, true, true);
    }

    private static FluidSpec gas(String name) {
        return simple(name).withDensity(-10).withViscosity(100);
    }

    private static FluidSpec blockFluid(String name, String stillTexture, String flowingTexture) {
        return new FluidSpec(name, stillTexture, flowingTexture, 300, 1000, 1000, 0, "bucket_" + name, true, true);
    }

    private static ResourceLocation texture(String texturePath) {
        if (texturePath.contains(":")) {
            return ResourceLocation.parse(texturePath);
        }
        return ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "blocks/forgefluid/" + texturePath);
    }

    public static final class FluidEntry {
        private final FluidSpec spec;
        private final DeferredHolder<FluidType, HbmFluidType> fluidType;
        private final DeferredHolder<Fluid, ? extends Fluid> source;
        private final DeferredHolder<Fluid, ? extends Fluid> flowing;
        @Nullable
        private DeferredBlock<LiquidBlock> block;
        @Nullable
        private DeferredItem<Item> bucket;

        private FluidEntry(
            FluidSpec spec,
            DeferredHolder<FluidType, HbmFluidType> fluidType,
            DeferredHolder<Fluid, ? extends Fluid> source,
            DeferredHolder<Fluid, ? extends Fluid> flowing
        ) {
            this.spec = spec;
            this.fluidType = fluidType;
            this.source = source;
            this.flowing = flowing;
        }

        public String name() {
            return spec.name;
        }

        public DeferredHolder<FluidType, HbmFluidType> fluidType() {
            return fluidType;
        }

        public DeferredHolder<Fluid, ? extends Fluid> source() {
            return source;
        }

        public DeferredHolder<Fluid, ? extends Fluid> flowing() {
            return flowing;
        }

        @Nullable
        public DeferredBlock<LiquidBlock> block() {
            return block;
        }

        @Nullable
        public DeferredItem<Item> bucket() {
            return bucket;
        }
    }

    private record FluidSpec(
        String name,
        String stillTexture,
        String flowingTexture,
        int temperature,
        int density,
        int viscosity,
        int lightLevel,
        String bucketId,
        boolean bucket,
        boolean worldBlock
    ) {
        private FluidSpec withTemperature(int temperature) {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, bucket, worldBlock);
        }

        private FluidSpec withDensity(int density) {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, bucket, worldBlock);
        }

        private FluidSpec withViscosity(int viscosity) {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, bucket, worldBlock);
        }

        private FluidSpec withLightLevel(int lightLevel) {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, bucket, worldBlock);
        }

        private FluidSpec withBucketId(String bucketId) {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, bucket, worldBlock);
        }

        private FluidSpec withoutPlacement() {
            return new FluidSpec(name, stillTexture, flowingTexture, temperature, density, viscosity, lightLevel, bucketId, false, false);
        }

        private boolean hasBucket() {
            return bucket;
        }

        private boolean hasWorldBlock() {
            return worldBlock;
        }
    }
}
