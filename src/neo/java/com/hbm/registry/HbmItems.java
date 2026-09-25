package com.hbm.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.hbm.armor.ArmorModSlot;
import com.hbm.HbmNuclearTech;
import com.hbm.armor.HbmHazardClass;
import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.item.ArmorModItem;
import com.hbm.item.BatteryItem;
import com.hbm.item.BatteryArmorModItem;
import com.hbm.item.CharmArmorModItem;
import com.hbm.item.CladdingArmorModItem;
import com.hbm.item.CraftingRemainderItem;
import com.hbm.item.CraftingRemainderLoreItem;
import com.hbm.item.HealthArmorModItem;
import com.hbm.item.InsertArmorModItem;
import com.hbm.item.GasMaskArmorItem;
import com.hbm.item.GasMaskAttachmentItem;
import com.hbm.item.GasMaskFilterItem;
import com.hbm.item.GrenadeItem;
import com.hbm.item.DurableTooltipItem;
import com.hbm.item.GasSensorArmorModItem;
import com.hbm.item.InstantMedicalItem;
import com.hbm.item.LodestoneArmorModItem;
import com.hbm.item.LoreItem;
import com.hbm.item.MedalArmorModItem;
import com.hbm.item.MedicalPillItem;
import com.hbm.item.ModularArmorItem;
import com.hbm.item.PadsArmorModItem;
import com.hbm.item.ReviveArmorModItem;
import com.hbm.item.ServoArmorModItem;
import com.hbm.item.SurveyLensArmorModItem;
import com.hbm.item.TooltipArmorModItem;
import com.hbm.util.HbmEffectUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HbmNuclearTech.MODID);
    private static final List<Supplier<? extends Item>> PARTS_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> CONTROL_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> CONSUMABLE_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> WEAPON_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> NUKE_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    private static final List<Supplier<? extends Item>> MISSILE_TAB_DYNAMIC_ITEMS = new ArrayList<>();
    public static final DeferredItem<Item> INGOT_ADVANCED_ALLOY = ITEMS.registerSimpleItem("ingot_advanced_alloy");
    public static final DeferredItem<Item> INGOT_ALUMINIUM = ITEMS.registerSimpleItem("ingot_aluminium");
    public static final DeferredItem<Item> INGOT_BERYLLIUM = ITEMS.registerSimpleItem("ingot_beryllium");
    public static final DeferredItem<Item> INGOT_COPPER = ITEMS.registerSimpleItem("ingot_copper");
    public static final DeferredItem<Item> INGOT_LEAD = ITEMS.registerSimpleItem("ingot_lead");
    public static final DeferredItem<Item> INGOT_RED_COPPER = ITEMS.registerSimpleItem("ingot_red_copper");
    public static final DeferredItem<Item> INGOT_STEEL = ITEMS.registerSimpleItem("ingot_steel");
    public static final DeferredItem<Item> INGOT_TITANIUM = ITEMS.registerSimpleItem("ingot_titanium");
    public static final DeferredItem<Item> INGOT_TUNGSTEN = ITEMS.registerSimpleItem("ingot_tungsten");
    public static final DeferredItem<Item> INGOT_URANIUM = ITEMS.registerSimpleItem("ingot_uranium");
    public static final DeferredItem<Item> HAZMAT_CLOTH = ITEMS.registerSimpleItem("hazmat_cloth");
    public static final DeferredItem<Item> HAZMAT_CLOTH_RED = registerSimplePartItem("hazmat_cloth_red");
    public static final DeferredItem<Item> HAZMAT_CLOTH_GREY = registerSimplePartItem("hazmat_cloth_grey");
    public static final DeferredItem<Item> UPGRADE_TEMPLATE = registerLoreItem("upgrade_template", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> DEUTERIUM_FILTER = ITEMS.registerSimpleItem("deuterium_filter");
    public static final DeferredItem<Item> CIRCUIT_VACUUM_TUBE = ITEMS.registerSimpleItem("circuit_vacuum_tube");
    public static final DeferredItem<Item> CIRCUIT_CAPACITOR = ITEMS.registerSimpleItem("circuit_capacitor");
    public static final DeferredItem<Item> CIRCUIT_CAPACITOR_TANTALIUM = ITEMS.registerSimpleItem("circuit_capacitor_tantalium");
    public static final DeferredItem<Item> CIRCUIT_ATOMIC_CLOCK = ITEMS.registerSimpleItem("circuit_atomic_clock");
    public static final DeferredItem<Item> CIRCUIT_PCB = ITEMS.registerSimpleItem("circuit_pcb");
    public static final DeferredItem<Item> CIRCUIT_CRT_TUBE = ITEMS.registerSimpleItem("circuit_crt_tube");
    public static final DeferredItem<Item> CIRCUIT_SILICON = ITEMS.registerSimpleItem("circuit_silicon");
    public static final DeferredItem<Item> CIRCUIT_CHIP = ITEMS.registerSimpleItem("circuit_chip");
    public static final DeferredItem<Item> CIRCUIT_CHIP_BISMOID = ITEMS.registerSimpleItem("circuit_chip_bismoid");
    public static final DeferredItem<Item> CIRCUIT_CHIP_QUANTUM = ITEMS.registerSimpleItem("circuit_chip_quantum");
    public static final DeferredItem<Item> CIRCUIT_ANALOG = ITEMS.registerSimpleItem("circuit_analog");
    public static final DeferredItem<Item> CIRCUIT_BASIC = ITEMS.registerSimpleItem("circuit_basic");
    public static final DeferredItem<Item> CIRCUIT_CAPACITOR_BOARD = ITEMS.registerSimpleItem("circuit_capacitor_board");
    public static final DeferredItem<Item> CIRCUIT_ADVANCED = ITEMS.registerSimpleItem("circuit_advanced");
    public static final DeferredItem<Item> CIRCUIT_BISMOID = ITEMS.registerSimpleItem("circuit_bismoid");
    public static final DeferredItem<Item> CIRCUIT_QUANTUM = ITEMS.registerSimpleItem("circuit_quantum");
    public static final DeferredItem<Item> CIRCUIT_CONTROLLER_CHASSIS = ITEMS.registerSimpleItem("circuit_controller_chassis");
    public static final DeferredItem<Item> CIRCUIT_CONTROLLER = ITEMS.registerSimpleItem("circuit_controller");
    public static final DeferredItem<Item> CIRCUIT_CONTROLLER_ADVANCED = ITEMS.registerSimpleItem("circuit_controller_advanced");
    public static final DeferredItem<Item> CIRCUIT_CONTROLLER_QUANTUM = ITEMS.registerSimpleItem("circuit_controller_quantum");
    public static final DeferredItem<Item> MECHANISM_REVOLVER_1 = ITEMS.registerSimpleItem("mechanism_revolver_1");
    public static final DeferredItem<Item> MECHANISM_REVOLVER_2 = ITEMS.registerSimpleItem("mechanism_revolver_2");
    public static final DeferredItem<Item> MECHANISM_RIFLE_1 = ITEMS.registerSimpleItem("mechanism_rifle_1");
    public static final DeferredItem<Item> MECHANISM_RIFLE_2 = ITEMS.registerSimpleItem("mechanism_rifle_2");
    public static final DeferredItem<Item> MECHANISM_LAUNCHER_1 = ITEMS.registerSimpleItem("mechanism_launcher_1");
    public static final DeferredItem<Item> MECHANISM_LAUNCHER_2 = ITEMS.registerSimpleItem("mechanism_launcher_2");
    public static final DeferredItem<Item> MECHANISM_SPECIAL = ITEMS.registerSimpleItem("mechanism_special");
    public static final DeferredItem<Item> PRIMER_357 = ITEMS.registerSimpleItem("primer_357");
    public static final DeferredItem<Item> PRIMER_44 = ITEMS.registerSimpleItem("primer_44");
    public static final DeferredItem<Item> PRIMER_9 = ITEMS.registerSimpleItem("primer_9");
    public static final DeferredItem<Item> PRIMER_50 = ITEMS.registerSimpleItem("primer_50");
    public static final DeferredItem<Item> PRIMER_BUCKSHOT = ITEMS.registerSimpleItem("primer_buckshot");
    public static final DeferredItem<Item> CASING_357 = ITEMS.registerSimpleItem("casing_357");
    public static final DeferredItem<Item> CASING_44 = ITEMS.registerSimpleItem("casing_44");
    public static final DeferredItem<Item> CASING_9 = ITEMS.registerSimpleItem("casing_9");
    public static final DeferredItem<Item> CASING_50 = ITEMS.registerSimpleItem("casing_50");
    public static final DeferredItem<Item> CASING_BUCKSHOT = ITEMS.registerSimpleItem("casing_buckshot");
    public static final DeferredItem<Item> MOLD_BASE = ITEMS.registerSimpleItem("mold_base");
    public static final DeferredItem<Item> PLATE_ADVANCED_ALLOY = ITEMS.registerSimpleItem("plate_advanced_alloy");
    public static final DeferredItem<Item> PLATE_ALUMINIUM = ITEMS.registerSimpleItem("plate_aluminium");
    public static final DeferredItem<Item> PLATE_ARMOR_AJR = ITEMS.registerSimpleItem("plate_armor_ajr");
    public static final DeferredItem<Item> PLATE_ARMOR_DNT = ITEMS.registerSimpleItem("plate_armor_dnt");
    public static final DeferredItem<Item> PLATE_ARMOR_FAU = ITEMS.registerSimpleItem("plate_armor_fau");
    public static final DeferredItem<Item> PLATE_ARMOR_HEV = ITEMS.registerSimpleItem("plate_armor_hev");
    public static final DeferredItem<Item> PLATE_ARMOR_LUNAR = ITEMS.registerSimpleItem("plate_armor_lunar");
    public static final DeferredItem<Item> PLATE_ARMOR_TITANIUM = ITEMS.registerSimpleItem("plate_armor_titanium");
    public static final DeferredItem<Item> PLATE_COMBINE_STEEL = ITEMS.registerSimpleItem("plate_combine_steel");
    public static final DeferredItem<Item> PLATE_COPPER = ITEMS.registerSimpleItem("plate_copper");
    public static final DeferredItem<Item> PLATE_DALEKANIUM = ITEMS.registerSimpleItem("plate_dalekanium");
    public static final DeferredItem<Item> PLATE_GOLD = ITEMS.registerSimpleItem("plate_gold");
    public static final DeferredItem<Item> PLATE_IRON = ITEMS.registerSimpleItem("plate_iron");
    public static final DeferredItem<Item> PLATE_LEAD = registerLoreItem("plate_lead");
    public static final DeferredItem<Item> PLATE_MIXED = ITEMS.registerSimpleItem("plate_mixed");
    public static final DeferredItem<Item> PLATE_SATURNITE = ITEMS.registerSimpleItem("plate_saturnite");
    public static final DeferredItem<Item> PLATE_STEEL = ITEMS.registerSimpleItem("plate_steel");
    public static final DeferredItem<Item> PLATE_TITANIUM = ITEMS.registerSimpleItem("plate_titanium");
    public static final DeferredItem<Item> CRYSTAL_IRON = ITEMS.registerSimpleItem("crystal_iron");
    public static final DeferredItem<Item> CRYSTAL_GOLD = ITEMS.registerSimpleItem("crystal_gold");
    public static final DeferredItem<Item> CRYSTAL_REDSTONE = ITEMS.registerSimpleItem("crystal_redstone");
    public static final DeferredItem<Item> CRYSTAL_LAPIS = ITEMS.registerSimpleItem("crystal_lapis");
    public static final DeferredItem<Item> CRYSTAL_DIAMOND = ITEMS.registerSimpleItem("crystal_diamond");
    public static final DeferredItem<Item> CRYSTAL_URANIUM = registerLoreItem("crystal_uranium");
    public static final DeferredItem<Item> CRYSTAL_THORIUM = registerLoreItem("crystal_thorium");
    public static final DeferredItem<Item> CRYSTAL_PLUTONIUM = registerLoreItem("crystal_plutonium");
    public static final DeferredItem<Item> CRYSTAL_TITANIUM = ITEMS.registerSimpleItem("crystal_titanium");
    public static final DeferredItem<Item> CRYSTAL_SULFUR = ITEMS.registerSimpleItem("crystal_sulfur");
    public static final DeferredItem<Item> CRYSTAL_NITER = ITEMS.registerSimpleItem("crystal_niter");
    public static final DeferredItem<Item> CRYSTAL_COPPER = ITEMS.registerSimpleItem("crystal_copper");
    public static final DeferredItem<Item> CRYSTAL_TUNGSTEN = ITEMS.registerSimpleItem("crystal_tungsten");
    public static final DeferredItem<Item> CRYSTAL_ALUMINIUM = ITEMS.registerSimpleItem("crystal_aluminium");
    public static final DeferredItem<Item> CRYSTAL_FLUORITE = ITEMS.registerSimpleItem("crystal_fluorite");
    public static final DeferredItem<Item> CRYSTAL_BERYLLIUM = ITEMS.registerSimpleItem("crystal_beryllium");
    public static final DeferredItem<Item> CRYSTAL_LEAD = registerLoreItem("crystal_lead");
    public static final DeferredItem<Item> CRYSTAL_ASBESTOS = registerLoreItem("crystal_asbestos");
    public static final DeferredItem<Item> CRYSTAL_RARE = ITEMS.registerSimpleItem("crystal_rare");
    public static final DeferredItem<Item> CRYSTAL_PHOSPHORUS = registerLoreItem("crystal_phosphorus");
    public static final DeferredItem<Item> CRYSTAL_LITHIUM = registerLoreItem("crystal_lithium");
    public static final DeferredItem<Item> CRYSTAL_CINNEBAR = ITEMS.registerSimpleItem("crystal_cinnebar");
    public static final DeferredItem<Item> CRYSTAL_COBALT = ITEMS.registerSimpleItem("crystal_cobalt");
    public static final DeferredItem<Item> CRYSTAL_STARMETAL = ITEMS.registerSimpleItem("crystal_starmetal");
    public static final DeferredItem<Item> CINNEBAR = registerSimplePartItem("cinnebar");
    public static final DeferredItem<Item> RARE_EARTH_CHUNK = registerSimplePartItem("rare_earth_chunk");
    public static final DeferredItem<Item> FRAGMENT_ACTINIUM = registerSimplePartItem("fragment_actinium");
    public static final DeferredItem<Item> FRAGMENT_BORON = registerSimplePartItem("fragment_boron");
    public static final DeferredItem<Item> FRAGMENT_CERIUM = registerSimplePartItem("fragment_cerium");
    public static final DeferredItem<Item> FRAGMENT_COBALT = registerSimplePartItem("fragment_cobalt");
    public static final DeferredItem<Item> FRAGMENT_COLTAN = registerSimplePartItem("fragment_coltan");
    public static final DeferredItem<Item> FRAGMENT_LANTHANIUM = registerSimplePartItem("fragment_lanthanium");
    public static final DeferredItem<Item> FRAGMENT_METEORITE = registerSimplePartItem("fragment_meteorite");
    public static final DeferredItem<Item> FRAGMENT_NEODYMIUM = registerSimplePartItem("fragment_neodymium");
    public static final DeferredItem<Item> FRAGMENT_NIOBIUM = registerSimplePartItem("fragment_niobium");
    public static final DeferredItem<Item> GEM_TANTALIUM = registerLoreItem("gem_tantalium");
    public static final DeferredItem<Item> COIL_ADVANCED_ALLOY = ITEMS.registerSimpleItem("coil_advanced_alloy");
    public static final DeferredItem<Item> COIL_ADVANCED_TORUS = ITEMS.registerSimpleItem("coil_advanced_torus");
    public static final DeferredItem<Item> COIL_COPPER = ITEMS.registerSimpleItem("coil_copper");
    public static final DeferredItem<Item> COIL_COPPER_TORUS = ITEMS.registerSimpleItem("coil_copper_torus");
    public static final DeferredItem<Item> COIL_GOLD = ITEMS.registerSimpleItem("coil_gold");
    public static final DeferredItem<Item> COIL_GOLD_TORUS = ITEMS.registerSimpleItem("coil_gold_torus");
    public static final DeferredItem<Item> COIL_MAGNETIZED_TUNGSTEN = registerLoreItem("coil_magnetized_tungsten");
    public static final DeferredItem<Item> COIL_TUNGSTEN = ITEMS.registerSimpleItem("coil_tungsten");
    public static final DeferredItem<Item> ASSEMBLY_IRON = ITEMS.registerSimpleItem("assembly_iron");
    public static final DeferredItem<Item> ASSEMBLY_STEEL = ITEMS.registerSimpleItem("assembly_steel");
    public static final DeferredItem<Item> ASSEMBLY_LEAD = registerLoreItem("assembly_lead");
    public static final DeferredItem<Item> ASSEMBLY_GOLD = ITEMS.registerSimpleItem("assembly_gold");
    public static final DeferredItem<Item> ASSEMBLY_SCHRABIDIUM = ITEMS.registerSimpleItem("assembly_schrabidium");
    public static final DeferredItem<Item> ASSEMBLY_NIGHTMARE = ITEMS.registerSimpleItem("assembly_nightmare");
    public static final DeferredItem<Item> ASSEMBLY_DESH = ITEMS.registerSimpleItem("assembly_desh");
    public static final DeferredItem<Item> ASSEMBLY_NOPIP = ITEMS.registerSimpleItem("assembly_nopip");
    public static final DeferredItem<Item> ASSEMBLY_SMG = ITEMS.registerSimpleItem("assembly_smg");
    public static final DeferredItem<Item> ASSEMBLY_556 = ITEMS.registerSimpleItem("assembly_556");
    public static final DeferredItem<Item> ASSEMBLY_UZI = ITEMS.registerSimpleItem("assembly_uzi");
    public static final DeferredItem<Item> ASSEMBLY_ACTIONEXPRESS = ITEMS.registerSimpleItem("assembly_actionexpress");
    public static final DeferredItem<Item> ASSEMBLY_CALAMITY = ITEMS.registerSimpleItem("assembly_calamity");
    public static final DeferredItem<Item> ASSEMBLY_LACUNAE = ITEMS.registerSimpleItem("assembly_lacunae");
    public static final DeferredItem<Item> ASSEMBLY_NUKE = ITEMS.registerSimpleItem("assembly_nuke");
    public static final DeferredItem<Item> GRENADE_GENERIC = ITEMS.register(
        "grenade_generic",
        () -> new GrenadeItem(new Item.Properties().stacksTo(16), 100)
    );
    public static final DeferredItem<Item> ENTITY_GLOWING_ONE_SPAWN_EGG = ITEMS.register(
        "entity_glowing_one_spawn_egg",
        () -> new DeferredSpawnEggItem(HbmEntityTypes.ENTITY_GLOWING_ONE, 0x357C2E, 0x4CFF00, new Item.Properties())
    );
    public static final DeferredItem<Item> ENTITY_NUCLEAR_CREEPER_SPAWN_EGG = ITEMS.register(
        "entity_nuclear_creeper_spawn_egg",
        () -> new DeferredSpawnEggItem(HbmEntityTypes.ENTITY_NUCLEAR_CREEPER, 0x3D3D3D, 0xCECECE, new Item.Properties())
    );
    public static final DeferredItem<Item> ENTITY_TAINTED_CREEPER_SPAWN_EGG = ITEMS.register(
        "entity_tainted_creeper_spawn_egg",
        () -> new DeferredSpawnEggItem(HbmEntityTypes.ENTITY_TAINTED_CREEPER, 0x009CCA, 0x00F761, new Item.Properties())
    );
    public static final DeferredItem<Item> MISSILE_NUCLEAR = ITEMS.registerSimpleItem("missile_nuclear");
    public static final DeferredItem<Item> AMMO_ROCKET = ITEMS.registerSimpleItem("ammo_rocket");
    public static final DeferredItem<Item> AMMO_AA_SHELL = ITEMS.registerSimpleItem("ammo_aa_shell");
    public static final DeferredItem<Item> AMMO_FALLING_NUKE = ITEMS.registerSimpleItem("ammo_falling_nuke");
    public static final DeferredItem<Item> AMMO_CHOPPER_MINE = ITEMS.registerSimpleItem("ammo_chopper_mine");
    public static final DeferredItem<Item> INGOT_ASBESTOS = registerSimplePartItem("ingot_asbestos");
    public static final DeferredItem<Item> INGOT_SILICON = registerSimplePartItem("ingot_silicon");
    public static final DeferredItem<Item> INGOT_MAGNETIZED_TUNGSTEN = registerSimplePartItem("ingot_magnetized_tungsten");
    public static final DeferredItem<Item> INGOT_COMBINE_STEEL = registerLorePartItem("ingot_combine_steel");
    public static final DeferredItem<Item> INGOT_DURA_STEEL = registerLorePartItem("ingot_dura_steel");
    public static final DeferredItem<Item> INGOT_TECHNETIUM = registerSimplePartItem("ingot_technetium");
    public static final DeferredItem<Item> INGOT_TCALLOY = registerSimplePartItem("ingot_tcalloy");
    public static final DeferredItem<Item> INGOT_CDALLOY = registerSimplePartItem("ingot_cdalloy");
    public static final DeferredItem<Item> INGOT_POLYMER = registerLorePartItem("ingot_polymer");
    public static final DeferredItem<Item> INGOT_BAKELITE = registerLorePartItem("ingot_bakelite");
    public static final DeferredItem<Item> INGOT_RUBBER = registerLorePartItem("ingot_rubber");
    public static final DeferredItem<Item> INGOT_BIORUBBER = registerLorePartItem("ingot_biorubber");
    public static final DeferredItem<Item> INGOT_PC = registerLorePartItem("ingot_pc");
    public static final DeferredItem<Item> INGOT_PVC = registerLorePartItem("ingot_pvc");
    public static final DeferredItem<Item> INGOT_DESH = registerLorePartItem("ingot_desh");
    public static final DeferredItem<Item> INGOT_SATURNITE = registerLorePartItem("ingot_saturnite");
    public static final DeferredItem<Item> INGOT_FERROURANIUM = registerSimplePartItem("ingot_ferrouranium");
    public static final DeferredItem<Item> INGOT_STARMETAL = registerLorePartItem("ingot_starmetal");
    public static final DeferredItem<Item> INGOT_BSCCO = registerLorePartItem("ingot_bscco");
    public static final DeferredItem<Item> INGOT_OSMIRIDIUM = registerLorePartItem("ingot_osmiridium");
    public static final DeferredItem<Item> INGOT_EUPHEMIUM = registerLorePartItem("ingot_euphemium");
    public static final DeferredItem<Item> INGOT_DINEUTRONIUM = registerLorePartItem("ingot_dineutronium");
    public static final DeferredItem<Item> INGOT_CADMIUM = registerSimplePartItem("ingot_cadmium");
    public static final DeferredItem<Item> INGOT_BISMUTH = registerLorePartItem("ingot_bismuth");
    public static final DeferredItem<Item> INGOT_ARSENIC = registerLorePartItem("ingot_arsenic");
    public static final DeferredItem<Item> INGOT_ZIRCONIUM = registerSimplePartItem("ingot_zirconium");
    public static final DeferredItem<Item> INGOT_CALCIUM = registerSimplePartItem("ingot_calcium");
    public static final DeferredItem<Item> INGOT_MUD = registerSimplePartItem("ingot_mud");
    public static final DeferredItem<Item> INGOT_TH232 = registerLorePartItem("ingot_th232");
    public static final DeferredItem<Item> INGOT_U233 = registerLorePartItem("ingot_u233");
    public static final DeferredItem<Item> INGOT_U235 = registerLorePartItem("ingot_u235");
    public static final DeferredItem<Item> INGOT_U238 = registerLorePartItem("ingot_u238");
    public static final DeferredItem<Item> INGOT_PLUTONIUM = registerLorePartItem("ingot_plutonium");
    public static final DeferredItem<Item> INGOT_PU238 = registerLorePartItem("ingot_pu238");
    public static final DeferredItem<Item> INGOT_PU239 = registerLorePartItem("ingot_pu239");
    public static final DeferredItem<Item> INGOT_PU240 = registerLorePartItem("ingot_pu240");
    public static final DeferredItem<Item> INGOT_PU241 = registerLorePartItem("ingot_pu241");
    public static final DeferredItem<Item> INGOT_PU_MIX = registerLorePartItem("ingot_pu_mix");
    public static final DeferredItem<Item> INGOT_AM241 = registerLorePartItem("ingot_am241");
    public static final DeferredItem<Item> INGOT_AM242 = registerLorePartItem("ingot_am242");
    public static final DeferredItem<Item> INGOT_AM_MIX = registerLorePartItem("ingot_am_mix");
    public static final DeferredItem<Item> INGOT_SCHRARANIUM = registerLorePartItem("ingot_schraranium");
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM = registerLorePartItem("ingot_schrabidium");
    public static final DeferredItem<Item> INGOT_SCHRABIDATE = registerLorePartItem("ingot_schrabidate");
    public static final DeferredItem<Item> INGOT_SOLINIUM = registerLorePartItem("ingot_solinium");
    public static final DeferredItem<Item> INGOT_THORIUM_FUEL = registerLorePartItem("ingot_thorium_fuel");
    public static final DeferredItem<Item> INGOT_URANIUM_FUEL = registerLorePartItem("ingot_uranium_fuel");
    public static final DeferredItem<Item> INGOT_MOX_FUEL = registerLorePartItem("ingot_mox_fuel");
    public static final DeferredItem<Item> INGOT_PLUTONIUM_FUEL = registerLorePartItem("ingot_plutonium_fuel");
    public static final DeferredItem<Item> INGOT_NEPTUNIUM_FUEL = registerLorePartItem("ingot_neptunium_fuel");
    public static final DeferredItem<Item> INGOT_AMERICIUM_FUEL = registerLorePartItem("ingot_americium_fuel");
    public static final DeferredItem<Item> INGOT_LES = registerLorePartItem("ingot_les");
    public static final DeferredItem<Item> INGOT_SCHRABIDIUM_FUEL = registerLorePartItem("ingot_schrabidium_fuel");
    public static final DeferredItem<Item> INGOT_HES = registerLorePartItem("ingot_hes");
    public static final DeferredItem<Item> INGOT_NEPTUNIUM = registerLorePartItem("ingot_neptunium");
    public static final DeferredItem<Item> INGOT_TENNESSINE = registerLorePartItem("ingot_tennessine");
    public static final DeferredItem<Item> INGOT_POLONIUM = registerLorePartItem("ingot_polonium");
    public static final DeferredItem<Item> INGOT_PHOSPHORUS = registerLorePartItem("ingot_phosphorus");
    public static final DeferredItem<Item> INGOT_BORON = registerSimplePartItem("ingot_boron");
    public static final DeferredItem<Item> INGOT_FIBERGLASS = registerLorePartItem("ingot_fiberglass");
    public static final DeferredItem<Item> INGOT_NIOBIUM = registerLorePartItem("ingot_niobium");
    public static final DeferredItem<Item> INGOT_ACTINIUM = registerLorePartItem("ingot_actinium");
    public static final DeferredItem<Item> INGOT_NEODYMIUM = registerLorePartItem("ingot_neodymium");
    public static final DeferredItem<Item> INGOT_BROMINE = registerLorePartItem("ingot_bromine");
    public static final DeferredItem<Item> INGOT_CAESIUM = registerLorePartItem("ingot_caesium");
    public static final DeferredItem<Item> INGOT_CERIUM = registerLorePartItem("ingot_cerium");
    public static final DeferredItem<Item> INGOT_LANTHANIUM = registerLorePartItem("ingot_lanthanium");
    public static final DeferredItem<Item> INGOT_TANTALIUM = registerLorePartItem("ingot_tantalium");
    public static final DeferredItem<Item> INGOT_ASTATINE = registerLorePartItem("ingot_astatine");
    public static final DeferredItem<Item> INGOT_FIREBRICK = registerSimplePartItem("ingot_firebrick");
    public static final DeferredItem<Item> INGOT_COBALT = registerLorePartItem("ingot_cobalt");
    public static final DeferredItem<Item> INGOT_CO60 = registerLorePartItem("ingot_co60");
    public static final DeferredItem<Item> INGOT_STRONTIUM = registerLorePartItem("ingot_strontium");
    public static final DeferredItem<Item> INGOT_SR90 = registerLorePartItem("ingot_sr90");
    public static final DeferredItem<Item> INGOT_IODINE = registerLorePartItem("ingot_iodine");
    public static final DeferredItem<Item> INGOT_I131 = registerLorePartItem("ingot_i131");
    public static final DeferredItem<Item> INGOT_AU198 = registerLorePartItem("ingot_au198");
    public static final DeferredItem<Item> INGOT_PB209 = registerLorePartItem("ingot_pb209");
    public static final DeferredItem<Item> INGOT_RA226 = registerLorePartItem("ingot_ra226");
    public static final DeferredItem<Item> INGOT_AC227 = registerLorePartItem("ingot_ac227");
    public static final DeferredItem<Item> INGOT_GH336 = registerLorePartItem("ingot_gh336");
    public static final DeferredItem<Item> INGOT_RADSPICE = registerLorePartItem("ingot_radspice");
    public static final DeferredItem<Item> INGOT_REIIUM = registerLorePartItem("ingot_reiium");
    public static final DeferredItem<Item> INGOT_WEIDANIUM = registerLorePartItem("ingot_weidanium");
    public static final DeferredItem<Item> INGOT_AUSTRALIUM = registerLorePartItem("ingot_australium");
    public static final DeferredItem<Item> INGOT_VERTICIUM = registerLorePartItem("ingot_verticium");
    public static final DeferredItem<Item> INGOT_UNOBTAINIUM = registerLorePartItem("ingot_unobtainium");
    public static final DeferredItem<Item> INGOT_DAFFERGON = registerLorePartItem("ingot_daffergon");
    public static final DeferredItem<Item> BILLET_TH232 = registerLorePartItem("billet_th232");
    public static final DeferredItem<Item> BILLET_URANIUM = registerLorePartItem("billet_uranium");
    public static final DeferredItem<Item> BILLET_U233 = registerLorePartItem("billet_u233");
    public static final DeferredItem<Item> BILLET_U235 = registerLorePartItem("billet_u235");
    public static final DeferredItem<Item> BILLET_U238 = registerLorePartItem("billet_u238");
    public static final DeferredItem<Item> BILLET_PLUTONIUM = registerLorePartItem("billet_plutonium");
    public static final DeferredItem<Item> BILLET_PU238 = registerLorePartItem("billet_pu238");
    public static final DeferredItem<Item> BILLET_PU239 = registerLorePartItem("billet_pu239");
    public static final DeferredItem<Item> BILLET_PU240 = registerLorePartItem("billet_pu240");
    public static final DeferredItem<Item> BILLET_PU241 = registerLorePartItem("billet_pu241");
    public static final DeferredItem<Item> BILLET_PU_MIX = registerLorePartItem("billet_pu_mix");
    public static final DeferredItem<Item> BILLET_AM241 = registerLorePartItem("billet_am241");
    public static final DeferredItem<Item> BILLET_AM242 = registerLorePartItem("billet_am242");
    public static final DeferredItem<Item> BILLET_AM_MIX = registerLorePartItem("billet_am_mix");
    public static final DeferredItem<Item> BILLET_NEPTUNIUM = registerLorePartItem("billet_neptunium");
    public static final DeferredItem<Item> BILLET_POLONIUM = registerLorePartItem("billet_polonium");
    public static final DeferredItem<Item> BILLET_TECHNETIUM = registerLorePartItem("billet_technetium");
    public static final DeferredItem<Item> BILLET_CO60 = registerLorePartItem("billet_co60");
    public static final DeferredItem<Item> BILLET_SR90 = registerLorePartItem("billet_sr90");
    public static final DeferredItem<Item> BILLET_AU198 = registerLorePartItem("billet_au198");
    public static final DeferredItem<Item> BILLET_PB209 = registerLorePartItem("billet_pb209");
    public static final DeferredItem<Item> BILLET_RA226 = registerLorePartItem("billet_ra226");
    public static final DeferredItem<Item> BILLET_AC227 = registerLorePartItem("billet_ac227");
    public static final DeferredItem<Item> BILLET_GH336 = registerLorePartItem("billet_gh336");
    public static final DeferredItem<Item> BILLET_BERYLLIUM = registerSimplePartItem("billet_beryllium");
    public static final DeferredItem<Item> BILLET_SILICON = registerSimplePartItem("billet_silicon");
    public static final DeferredItem<Item> BILLET_BISMUTH = registerSimplePartItem("billet_bismuth");
    public static final DeferredItem<Item> BILLET_ZIRCONIUM = registerSimplePartItem("billet_zirconium");
    public static final DeferredItem<Item> BILLET_ZFB_BISMUTH = registerLorePartItem("billet_zfb_bismuth");
    public static final DeferredItem<Item> BILLET_ZFB_PU241 = registerLorePartItem("billet_zfb_pu241");
    public static final DeferredItem<Item> BILLET_ZFB_AM_MIX = registerLorePartItem("billet_zfb_am_mix");
    public static final DeferredItem<Item> BILLET_SCHRABIDIUM = registerLorePartItem("billet_schrabidium");
    public static final DeferredItem<Item> BILLET_SOLINIUM = registerLorePartItem("billet_solinium");
    public static final DeferredItem<Item> BILLET_THORIUM_FUEL = registerLorePartItem("billet_thorium_fuel");
    public static final DeferredItem<Item> BILLET_URANIUM_FUEL = registerLorePartItem("billet_uranium_fuel");
    public static final DeferredItem<Item> BILLET_MOX_FUEL = registerLorePartItem("billet_mox_fuel");
    public static final DeferredItem<Item> BILLET_PLUTONIUM_FUEL = registerLorePartItem("billet_plutonium_fuel");
    public static final DeferredItem<Item> BILLET_NEPTUNIUM_FUEL = registerLorePartItem("billet_neptunium_fuel");
    public static final DeferredItem<Item> BILLET_AMERICIUM_FUEL = registerLorePartItem("billet_americium_fuel");
    public static final DeferredItem<Item> BILLET_LES = registerLorePartItem("billet_les");
    public static final DeferredItem<Item> BILLET_SCHRABIDIUM_FUEL = registerLorePartItem("billet_schrabidium_fuel");
    public static final DeferredItem<Item> BILLET_HES = registerLorePartItem("billet_hes");
    public static final DeferredItem<Item> EUPHEMIUM_CAPACITOR = registerLoreControlItem("euphemium_capacitor", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> FACTORY_CORE_TITANIUM = registerLoreItem("factory_core_titanium", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> FACTORY_CORE_ADVANCED = registerLoreItem("factory_core_advanced", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> FUSION_CORE_INFINITE = ITEMS.register("fusion_core_infinite", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BILLET_PO210BE = registerLorePartItem("billet_po210be");
    public static final DeferredItem<Item> BILLET_RA226BE = registerLorePartItem("billet_ra226be");
    public static final DeferredItem<Item> BILLET_PU238BE = registerLorePartItem("billet_pu238be");
    public static final DeferredItem<Item> BILLET_AUSTRALIUM = registerLorePartItem("billet_australium");
    public static final DeferredItem<Item> BILLET_AUSTRALIUM_LESSER = registerLorePartItem("billet_australium_lesser");
    public static final DeferredItem<Item> BILLET_AUSTRALIUM_GREATER = registerLorePartItem("billet_australium_greater");
    public static final DeferredItem<Item> BILLET_UNOBTAINIUM = registerLorePartItem("billet_unobtainium");
    public static final DeferredItem<Item> BILLET_YHARONITE = registerSimplePartItem("billet_yharonite");
    public static final DeferredItem<Item> BILLET_BALEFIRE_GOLD = registerLorePartItem("billet_balefire_gold");
    public static final DeferredItem<Item> BILLET_FLASHLEAD = registerLorePartItem("billet_flashlead");
    public static final DeferredItem<Item> BILLET_NUCLEAR_WASTE = registerLorePartItem("billet_nuclear_waste");
    public static final DeferredItem<Item> NUGGET_URANIUM = registerLorePartItem("nugget_uranium");
    public static final DeferredItem<Item> NUGGET_U233 = registerLorePartItem("nugget_u233");
    public static final DeferredItem<Item> NUGGET_U235 = registerLorePartItem("nugget_u235");
    public static final DeferredItem<Item> NUGGET_U238 = registerLorePartItem("nugget_u238");
    public static final DeferredItem<Item> NUGGET_PLUTONIUM = registerLorePartItem("nugget_plutonium");
    public static final DeferredItem<Item> NUGGET_PU238 = registerLorePartItem("nugget_pu238");
    public static final DeferredItem<Item> NUGGET_PU239 = registerLorePartItem("nugget_pu239");
    public static final DeferredItem<Item> NUGGET_PU240 = registerLorePartItem("nugget_pu240");
    public static final DeferredItem<Item> NUGGET_TH232 = registerLorePartItem("nugget_th232");
    public static final DeferredItem<Item> NUGGET_PU241 = registerLorePartItem("nugget_pu241");
    public static final DeferredItem<Item> NUGGET_PU_MIX = registerLorePartItem("nugget_pu_mix");
    public static final DeferredItem<Item> NUGGET_AM241 = registerLorePartItem("nugget_am241");
    public static final DeferredItem<Item> NUGGET_AM242 = registerLorePartItem("nugget_am242");
    public static final DeferredItem<Item> NUGGET_AM_MIX = registerLorePartItem("nugget_am_mix");
    public static final DeferredItem<Item> NUGGET_TECHNETIUM = registerLorePartItem("nugget_technetium");
    public static final DeferredItem<Item> NUGGET_NEPTUNIUM = registerLorePartItem("nugget_neptunium");
    public static final DeferredItem<Item> NUGGET_POLONIUM = registerLorePartItem("nugget_polonium");
    public static final DeferredItem<Item> NUGGET_THORIUM_FUEL = registerLorePartItem("nugget_thorium_fuel");
    public static final DeferredItem<Item> NUGGET_URANIUM_FUEL = registerLorePartItem("nugget_uranium_fuel");
    public static final DeferredItem<Item> NUGGET_MOX_FUEL = registerLorePartItem("nugget_mox_fuel");
    public static final DeferredItem<Item> NUGGET_PLUTONIUM_FUEL = registerLorePartItem("nugget_plutonium_fuel");
    public static final DeferredItem<Item> NUGGET_NEPTUNIUM_FUEL = registerLorePartItem("nugget_neptunium_fuel");
    public static final DeferredItem<Item> NUGGET_AMERICIUM_FUEL = registerLorePartItem("nugget_americium_fuel");
    public static final DeferredItem<Item> NUGGET_LES = registerLorePartItem("nugget_les");
    public static final DeferredItem<Item> NUGGET_SCHRABIDIUM_FUEL = registerLorePartItem("nugget_schrabidium_fuel");
    public static final DeferredItem<Item> NUGGET_HES = registerLorePartItem("nugget_hes");
    public static final DeferredItem<Item> NUGGET_LEAD = registerLorePartItem("nugget_lead");
    public static final DeferredItem<Item> NUGGET_BERYLLIUM = registerSimplePartItem("nugget_beryllium");
    public static final DeferredItem<Item> NUGGET_SILICON = registerSimplePartItem("nugget_silicon");
    public static final DeferredItem<Item> NUGGET_CADMIUM = registerSimplePartItem("nugget_cadmium");
    public static final DeferredItem<Item> NUGGET_BISMUTH = registerSimplePartItem("nugget_bismuth");
    public static final DeferredItem<Item> NUGGET_ARSENIC = registerLorePartItem("nugget_arsenic");
    public static final DeferredItem<Item> NUGGET_ZIRCONIUM = registerLorePartItem("nugget_zirconium");
    public static final DeferredItem<Item> NUGGET_TANTALIUM = registerLorePartItem("nugget_tantalium");
    public static final DeferredItem<Item> NUGGET_DESH = registerLorePartItem("nugget_desh");
    public static final DeferredItem<Item> NUGGET_OSMIRIDIUM = registerLorePartItem("nugget_osmiridium");
    public static final DeferredItem<Item> NUGGET_SCHRABIDIUM = registerLorePartItem("nugget_schrabidium");
    public static final DeferredItem<Item> NUGGET_SOLINIUM = registerLorePartItem("nugget_solinium");
    public static final DeferredItem<Item> NUGGET_EUPHEMIUM = registerLorePartItem("nugget_euphemium");
    public static final DeferredItem<Item> NUGGET_DINEUTRONIUM = registerLorePartItem("nugget_dineutronium");
    public static final DeferredItem<Item> NUGGET_NIOBIUM = registerSimplePartItem("nugget_niobium");
    public static final DeferredItem<Item> NUGGET_ACTINIUM = registerSimplePartItem("nugget_actinium");
    public static final DeferredItem<Item> NUGGET_AC227 = registerLorePartItem("nugget_ac227");
    public static final DeferredItem<Item> NUGGET_COBALT = registerLorePartItem("nugget_cobalt");
    public static final DeferredItem<Item> NUGGET_CO60 = registerLorePartItem("nugget_co60");
    public static final DeferredItem<Item> NUGGET_STRONTIUM = registerLorePartItem("nugget_strontium");
    public static final DeferredItem<Item> NUGGET_SR90 = registerLorePartItem("nugget_sr90");
    public static final DeferredItem<Item> NUGGET_PB209 = registerLorePartItem("nugget_pb209");
    public static final DeferredItem<Item> NUGGET_GH336 = registerLorePartItem("nugget_gh336");
    public static final DeferredItem<Item> NUGGET_AU198 = registerLorePartItem("nugget_au198");
    public static final DeferredItem<Item> NUGGET_RA226 = registerLorePartItem("nugget_ra226");
    public static final DeferredItem<Item> NUGGET_RADSPICE = registerLorePartItem("nugget_radspice");
    public static final DeferredItem<Item> NUGGET_REIIUM = registerLorePartItem("nugget_reiium");
    public static final DeferredItem<Item> NUGGET_WEIDANIUM = registerLorePartItem("nugget_weidanium");
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM = registerLorePartItem("nugget_australium");
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM_LESSER = registerLorePartItem("nugget_australium_lesser");
    public static final DeferredItem<Item> NUGGET_AUSTRALIUM_GREATER = registerLorePartItem("nugget_australium_greater");
    public static final DeferredItem<Item> NUGGET_VERTICIUM = registerLorePartItem("nugget_verticium");
    public static final DeferredItem<Item> NUGGET_UNOBTAINIUM = registerLorePartItem("nugget_unobtainium");
    public static final DeferredItem<Item> NUGGET_UNOBTAINIUM_LESSER = registerLorePartItem("nugget_unobtainium_lesser");
    public static final DeferredItem<Item> NUGGET_UNOBTAINIUM_GREATER = registerLorePartItem("nugget_unobtainium_greater");
    public static final DeferredItem<Item> NUGGET_DAFFERGON = registerLorePartItem("nugget_daffergon");
    public static final DeferredItem<Item> NUGGET_MERCURY = registerLorePartItem("nugget_mercury");
    public static final DeferredItem<Item> BOTTLE_MERCURY = registerLorePartItem("bottle_mercury");
    public static final DeferredItem<Item> POWDER_IRON = registerSimplePartItem("powder_iron");
    public static final DeferredItem<Item> POWDER_GOLD = registerSimplePartItem("powder_gold");
    public static final DeferredItem<Item> POWDER_DIAMOND = registerSimplePartItem("powder_diamond");
    public static final DeferredItem<Item> POWDER_EMERALD = registerSimplePartItem("powder_emerald");
    public static final DeferredItem<Item> POWDER_LAPIS = registerSimplePartItem("powder_lapis");
    public static final DeferredItem<Item> POWDER_TITANIUM = registerSimplePartItem("powder_titanium");
    public static final DeferredItem<Item> POWDER_TUNGSTEN = registerSimplePartItem("powder_tungsten");
    public static final DeferredItem<Item> POWDER_COPPER = registerSimplePartItem("powder_copper");
    public static final DeferredItem<Item> POWDER_BERYLLIUM = registerSimplePartItem("powder_beryllium");
    public static final DeferredItem<Item> POWDER_ALUMINIUM = registerSimplePartItem("powder_aluminium");
    public static final DeferredItem<Item> POWDER_LEAD = registerLorePartItem("powder_lead");
    public static final DeferredItem<Item> TRITIUM_DEUTERIUM_CAKE = registerLorePartItem("tritium_deuterium_cake");
    public static final DeferredItem<Item> POWDER_ADVANCED_ALLOY = registerSimplePartItem("powder_advanced_alloy");
    public static final DeferredItem<Item> POWDER_COMBINE_STEEL = registerSimplePartItem("powder_combine_steel");
    public static final DeferredItem<Item> POWDER_TCALLOY = registerLorePartItem("powder_tcalloy");
    public static final DeferredItem<Item> POWDER_CDALLOY = registerSimplePartItem("powder_cdalloy");
    public static final DeferredItem<Item> POWDER_MAGNETIZED_TUNGSTEN = registerLorePartItem("powder_magnetized_tungsten");
    public static final DeferredItem<Item> POWDER_CHLOROPHYTE = registerSimplePartItem("powder_chlorophyte");
    public static final DeferredItem<Item> POWDER_RED_COPPER = registerSimplePartItem("powder_red_copper");
    public static final DeferredItem<Item> POWDER_STEEL = registerSimplePartItem("powder_steel");
    public static final DeferredItem<Item> POWDER_LITHIUM = registerLorePartItem("powder_lithium");
    public static final DeferredItem<Item> REDSTONE_DEPLETED = registerSimplePartItem("redstone_depleted");
    public static final DeferredItem<Item> SULFUR = registerSimplePartItem("sulfur");
    public static final DeferredItem<Item> NITER = registerSimplePartItem("niter");
    public static final DeferredItem<Item> FLUORITE = registerSimplePartItem("fluorite");
    public static final DeferredItem<Item> POWDER_CALCIUM = registerSimplePartItem("powder_calcium");
    public static final DeferredItem<Item> POWDER_QUARTZ = registerSimplePartItem("powder_quartz");
    public static final DeferredItem<Item> POWDER_BORAX = registerSimplePartItem("powder_borax");
    public static final DeferredItem<Item> POWDER_DURA_STEEL = registerLorePartItem("powder_dura_steel");
    public static final DeferredItem<Item> POWDER_POLYMER = registerLorePartItem("powder_polymer");
    public static final DeferredItem<Item> POWDER_BAKELITE = registerLorePartItem("powder_bakelite");
    public static final DeferredItem<Item> POWDER_LANTHANIUM = registerLorePartItem("powder_lanthanium");
    public static final DeferredItem<Item> POWDER_ACTINIUM = registerLorePartItem("powder_actinium");
    public static final DeferredItem<Item> POWDER_BORON = registerLorePartItem("powder_boron");
    public static final DeferredItem<Item> POWDER_SEMTEX_MIX = registerSimplePartItem("powder_semtex_mix");
    public static final DeferredItem<Item> POWDER_DESH = registerSimplePartItem("powder_desh");
    public static final DeferredItem<Item> POWDER_ZIRCONIUM = registerSimplePartItem("powder_zirconium");
    public static final DeferredItem<Item> POWDER_ASBESTOS = registerLorePartItem("powder_asbestos");
    public static final DeferredItem<Item> POWDER_CADMIUM = registerSimplePartItem("powder_cadmium");
    public static final DeferredItem<Item> POWDER_BISMUTH = registerSimplePartItem("powder_bismuth");
    public static final DeferredItem<Item> POWDER_YELLOWCAKE = registerLorePartItem("powder_yellowcake");
    public static final DeferredItem<Item> POWDER_THORIUM = registerLorePartItem("powder_thorium");
    public static final DeferredItem<Item> POWDER_URANIUM = registerLorePartItem("powder_uranium");
    public static final DeferredItem<Item> POWDER_PLUTONIUM = registerLorePartItem("powder_plutonium");
    public static final DeferredItem<Item> POWDER_NEPTUNIUM = registerLorePartItem("powder_neptunium");
    public static final DeferredItem<Item> POWDER_POLONIUM = registerLorePartItem("powder_polonium");
    public static final DeferredItem<Item> POWDER_SCHRABIDIUM = registerLorePartItem("powder_schrabidium");
    public static final DeferredItem<Item> POWDER_SCHRABIDATE = registerLorePartItem("powder_schrabidate");
    public static final DeferredItem<Item> POWDER_EUPHEMIUM = registerLorePartItem("powder_euphemium");
    public static final DeferredItem<Item> POWDER_DINEUTRONIUM = registerLorePartItem("powder_dineutronium");
    public static final DeferredItem<Item> POWDER_IODINE = registerLorePartItem("powder_iodine");
    public static final DeferredItem<Item> POWDER_ASTATINE = registerLorePartItem("powder_astatine");
    public static final DeferredItem<Item> POWDER_NEODYMIUM = registerLorePartItem("powder_neodymium");
    public static final DeferredItem<Item> POWDER_CAESIUM = registerLorePartItem("powder_caesium");
    public static final DeferredItem<Item> POWDER_REIIUM = registerLorePartItem("powder_reiium");
    public static final DeferredItem<Item> POWDER_WEIDANIUM = registerLorePartItem("powder_weidanium");
    public static final DeferredItem<Item> POWDER_AUSTRALIUM = registerLorePartItem("powder_australium");
    public static final DeferredItem<Item> POWDER_VERTICIUM = registerLorePartItem("powder_verticium");
    public static final DeferredItem<Item> POWDER_UNOBTAINIUM = registerLorePartItem("powder_unobtainium");
    public static final DeferredItem<Item> POWDER_DAFFERGON = registerLorePartItem("powder_daffergon");
    public static final DeferredItem<Item> POWDER_STRONTIUM = registerLorePartItem("powder_strontium");
    public static final DeferredItem<Item> POWDER_COBALT = registerLorePartItem("powder_cobalt");
    public static final DeferredItem<Item> POWDER_BROMINE = registerLorePartItem("powder_bromine");
    public static final DeferredItem<Item> POWDER_NIOBIUM = registerLorePartItem("powder_niobium");
    public static final DeferredItem<Item> POWDER_TANTALIUM = registerLorePartItem("powder_tantalium");
    public static final DeferredItem<Item> POWDER_TENNESSINE = registerLorePartItem("powder_tennessine");
    public static final DeferredItem<Item> POWDER_CERIUM = registerLorePartItem("powder_cerium");
    public static final DeferredItem<Item> POWDER_ICE = registerLorePartItem("powder_ice");
    public static final DeferredItem<Item> POWDER_DESH_MIX = registerSimplePartItem("powder_desh_mix");
    public static final DeferredItem<Item> POWDER_DESH_READY = registerSimplePartItem("powder_desh_ready");
    public static final DeferredItem<Item> POWDER_NITAN_MIX = registerLorePartItem("powder_nitan_mix");
    public static final DeferredItem<Item> POWDER_SPARK_MIX = registerLorePartItem("powder_spark_mix");
    public static final DeferredItem<Item> POWDER_METEORITE = registerSimplePartItem("powder_meteorite");
    public static final DeferredItem<Item> POWDER_FLUX = registerSimplePartItem("powder_flux");
    public static final DeferredItem<Item> POWDER_MAGIC = registerLorePartItem("powder_magic");
    public static final DeferredItem<Item> POWDER_CLOUD = registerLorePartItem("powder_cloud");
    public static final DeferredItem<Item> POWDER_BALEFIRE = registerLorePartItem("powder_balefire");
    public static final DeferredItem<Item> POWDER_COLTAN_ORE = registerLorePartItem("powder_coltan_ore");
    public static final DeferredItem<Item> POWDER_COLTAN = registerSimplePartItem("powder_coltan");
    public static final DeferredItem<Item> POWDER_POISON = registerLorePartItem("powder_poison");
    public static final DeferredItem<Item> POWDER_THERMITE = registerLorePartItem("powder_thermite");
    public static final DeferredItem<Item> POWDER_POWER = registerLorePartItem("powder_power");
    public static final DeferredItem<Item> POWDER_TEKTITE = registerLorePartItem("powder_tektite");
    public static final DeferredItem<Item> POWDER_PALEOGENITE = registerLorePartItem("powder_paleogenite");
    public static final DeferredItem<Item> POWDER_IMPURE_OSMIRIDIUM = registerLorePartItem("powder_impure_osmiridium");
    public static final DeferredItem<Item> POWDER_OSMIRIDIUM = registerLorePartItem("powder_osmiridium");
    public static final DeferredItem<Item> POWDER_CO60 = registerLorePartItem("powder_co60");
    public static final DeferredItem<Item> POWDER_SR90 = registerLorePartItem("powder_sr90");
    public static final DeferredItem<Item> POWDER_AT209 = registerLorePartItem("powder_at209");
    public static final DeferredItem<Item> POWDER_PB209 = registerLorePartItem("powder_pb209");
    public static final DeferredItem<Item> POWDER_I131 = registerLorePartItem("powder_i131");
    public static final DeferredItem<Item> POWDER_CS137 = registerLorePartItem("powder_cs137");
    public static final DeferredItem<Item> POWDER_XE135 = registerLorePartItem("powder_xe135");
    public static final DeferredItem<Item> POWDER_AU198 = registerLorePartItem("powder_au198");
    public static final DeferredItem<Item> POWDER_RA226 = registerLorePartItem("powder_ra226");
    public static final DeferredItem<Item> POWDER_AC227 = registerLorePartItem("powder_ac227");
    public static final DeferredItem<Item> POWDER_RADSPICE = registerLorePartItem("powder_radspice");
    public static final DeferredItem<Item> POWDER_IRON_TINY = registerSimplePartItem("powder_iron_tiny");
    public static final DeferredItem<Item> POWDER_STEEL_TINY = registerSimplePartItem("powder_steel_tiny");
    public static final DeferredItem<Item> POWDER_LITHIUM_TINY = registerLorePartItem("powder_lithium_tiny");
    public static final DeferredItem<Item> POWDER_LANTHANIUM_TINY = registerSimplePartItem("powder_lanthanium_tiny");
    public static final DeferredItem<Item> POWDER_ACTINIUM_TINY = registerSimplePartItem("powder_actinium_tiny");
    public static final DeferredItem<Item> POWDER_BORON_TINY = registerSimplePartItem("powder_boron_tiny");
    public static final DeferredItem<Item> POWDER_IODINE_TINY = registerLorePartItem("powder_iodine_tiny");
    public static final DeferredItem<Item> POWDER_NEODYMIUM_TINY = registerLorePartItem("powder_neodymium_tiny");
    public static final DeferredItem<Item> POWDER_COBALT_TINY = registerLorePartItem("powder_cobalt_tiny");
    public static final DeferredItem<Item> POWDER_NIOBIUM_TINY = registerLorePartItem("powder_niobium_tiny");
    public static final DeferredItem<Item> POWDER_CERIUM_TINY = registerLorePartItem("powder_cerium_tiny");
    public static final DeferredItem<Item> POWDER_METEORITE_TINY = registerSimplePartItem("powder_meteorite_tiny");
    public static final DeferredItem<Item> POWDER_PALEOGENITE_TINY = registerLorePartItem("powder_paleogenite_tiny");
    public static final DeferredItem<Item> POWDER_CO60_TINY = registerLorePartItem("powder_co60_tiny");
    public static final DeferredItem<Item> POWDER_SR90_TINY = registerLorePartItem("powder_sr90_tiny");
    public static final DeferredItem<Item> POWDER_AT209_TINY = registerLorePartItem("powder_at209_tiny");
    public static final DeferredItem<Item> POWDER_PB209_TINY = registerLorePartItem("powder_pb209_tiny");
    public static final DeferredItem<Item> POWDER_I131_TINY = registerLorePartItem("powder_i131_tiny");
    public static final DeferredItem<Item> POWDER_CS137_TINY = registerLorePartItem("powder_cs137_tiny");
    public static final DeferredItem<Item> POWDER_XE135_TINY = registerLorePartItem("powder_xe135_tiny");
    public static final DeferredItem<Item> POWDER_AU198_TINY = registerLorePartItem("powder_au198_tiny");
    public static final DeferredItem<Item> POWDER_AC227_TINY = registerLorePartItem("powder_ac227_tiny");
    public static final DeferredItem<Item> POWDER_RADSPICE_TINY = registerLorePartItem("powder_radspice_tiny");
    public static final DeferredItem<Item> LITHIUM = registerLorePartItem("lithium");
    public static final DeferredItem<Item> TRINITITE = registerLorePartItem("trinitite");
    public static final DeferredItem<Item> NUCLEAR_WASTE = registerLorePartItem("nuclear_waste");
    public static final DeferredItem<Item> NUCLEAR_WASTE_TINY = registerLorePartItem("nuclear_waste_tiny");
    public static final DeferredItem<Item> NUCLEAR_WASTE_VITRIFIED = registerLorePartItem("nuclear_waste_vitrified");
    public static final DeferredItem<Item> NUCLEAR_WASTE_VITRIFIED_TINY = registerLorePartItem("nuclear_waste_vitrified_tiny");
    public static final DeferredItem<Item> WASTE_URANIUM = registerLorePartItem("waste_uranium");
    public static final DeferredItem<Item> WASTE_THORIUM = registerLorePartItem("waste_thorium");
    public static final DeferredItem<Item> WASTE_PLUTONIUM = registerLorePartItem("waste_plutonium");
    public static final DeferredItem<Item> WASTE_MOX = registerLorePartItem("waste_mox");
    public static final DeferredItem<Item> WASTE_SCHRABIDIUM = registerLorePartItem("waste_schrabidium");
    public static final DeferredItem<Item> WASTE_URANIUM_HOT = registerLorePartItem("waste_uranium_hot");
    public static final DeferredItem<Item> WASTE_THORIUM_HOT = registerLorePartItem("waste_thorium_hot");
    public static final DeferredItem<Item> WASTE_PLUTONIUM_HOT = registerLorePartItem("waste_plutonium_hot");
    public static final DeferredItem<Item> WASTE_MOX_HOT = registerLorePartItem("waste_mox_hot");
    public static final DeferredItem<Item> WASTE_SCHRABIDIUM_HOT = registerLorePartItem("waste_schrabidium_hot");
    public static final DeferredItem<Item> SCRAP = registerSimplePartItem("scrap");
    public static final DeferredItem<Item> SCRAP_OIL = registerSimplePartItem("scrap_oil");
    public static final DeferredItem<Item> DUST = registerSimplePartItem("dust");
    public static final DeferredItem<Item> FALLOUT = registerLorePartItem("falloutitem");
    public static final DeferredItem<Item> ASBESTOS_CLOTH = registerSimplePartItem("asbestos_cloth");
    public static final DeferredItem<Item> RAG_DAMP = registerSimplePartItem("rag_damp");
    public static final DeferredItem<Item> CORDITE = registerLorePartItem("cordite");
    public static final DeferredItem<Item> BALLISTITE = registerLorePartItem("ballistite");
    public static final DeferredItem<Item> BALL_DYNAMITE = registerLorePartItem("ball_dynamite");
    public static final DeferredItem<Item> BALL_TNT = registerLorePartItem("ball_tnt");
    public static final DeferredItem<Item> BALL_TATB = registerSimplePartItem("ball_tatb");
    public static final DeferredItem<Item> BALL_FIRECLAY = registerSimplePartItem("ball_fireclay");
    public static final DeferredItem<Item> FLYWHEEL_BERYLLIUM = registerSimplePartItem("flywheel_beryllium");
    public static final DeferredItem<Item> COMPONENT_LIMITER = registerSimplePartItem("component_limiter");
    public static final DeferredItem<Item> COMPONENT_EMITTER = registerSimplePartItem("component_emitter");
    public static final DeferredItem<Item> PISTON_PNEUMATIC = registerSimplePartItem("piston_pneumatic");
    public static final DeferredItem<Item> PISTON_HYDRAULIC = registerSimplePartItem("piston_hydraulic");
    public static final DeferredItem<Item> PISTON_ELECTRO = registerSimplePartItem("piston_electro");
    public static final DeferredItem<Item> MOTOR_DESH = registerSimplePartItem("motor_desh");
    public static final DeferredItem<Item> ROTOR_STEEL = registerSimplePartItem("rotor_steel");
    public static final DeferredItem<Item> BLADE_TITANIUM = registerSimplePartItem("blade_titanium");
    public static final DeferredItem<Item> TURBINE_TITANIUM = registerSimplePartItem("turbine_titanium");
    public static final DeferredItem<Item> BLADE_TUNGSTEN = registerSimplePartItem("blade_tungsten");
    public static final DeferredItem<Item> TURBINE_TUNGSTEN = registerSimplePartItem("turbine_tungsten");
    public static final DeferredItem<Item> DRILL_TITANIUM = registerSimplePartItem("drill_titanium");
    public static final DeferredItem<Item> CATALYST_CLAY = registerSimplePartItem("catalyst_clay");
    public static final DeferredItem<Item> FILTER_COAL = registerSimplePartItem("filter_coal");
    public static final DeferredItem<Item> NEUTRON_REFLECTOR = registerSimplePartItem("neutron_reflector");
    public static final DeferredItem<Item> RTG_UNIT = registerSimplePartItem("rtg_unit");
    public static final DeferredItem<Item> THERMO_UNIT_EMPTY = registerSimplePartItem("thermo_unit_empty");
    public static final DeferredItem<Item> CENTRIFUGE_ELEMENT = registerSimplePartItem("centrifuge_element");
    public static final DeferredItem<Item> REACTOR_CORE = registerSimplePartItem("reactor_core");
    public static final DeferredItem<Item> LOW_DENSITY_ELEMENT = registerSimplePartItem("low_density_element");
    public static final DeferredItem<Item> HEAVY_DUTY_ELEMENT = registerSimplePartItem("heavy_duty_element");
    public static final DeferredItem<Item> HULL_BIG_ALUMINIUM = registerSimplePartItem("hull_big_aluminium");
    public static final DeferredItem<Item> HULL_BIG_TITANIUM = registerSimplePartItem("hull_big_titanium");
    public static final DeferredItem<Item> FINS_FLAT = registerSimplePartItem("fins_flat");
    public static final DeferredItem<Item> FINS_SMALL_STEEL = registerSimplePartItem("fins_small_steel");
    public static final DeferredItem<Item> FINS_BIG_STEEL = registerSimplePartItem("fins_big_steel");
    public static final DeferredItem<Item> FINS_TRI_STEEL = registerSimplePartItem("fins_tri_steel");
    public static final DeferredItem<Item> FINS_QUAD_TITANIUM = registerSimplePartItem("fins_quad_titanium");
    public static final DeferredItem<Item> SPHERE_STEEL = registerSimplePartItem("sphere_steel");
    public static final DeferredItem<Item> PEDESTAL_STEEL = registerSimplePartItem("pedestal_steel");
    public static final DeferredItem<Item> DYSFUNCTIONAL_REACTOR = registerSimplePartItem("dysfunctional_reactor");
    public static final DeferredItem<Item> GENERATOR_STEEL = registerSimplePartItem("generator_steel");
    public static final DeferredItem<Item> SAT_HEAD_MAPPER = registerSimplePartItem("sat_head_mapper");
    public static final DeferredItem<Item> SAT_HEAD_SCANNER = registerSimplePartItem("sat_head_scanner");
    public static final DeferredItem<Item> SAT_HEAD_RADAR = registerSimplePartItem("sat_head_radar");
    public static final DeferredItem<Item> SAT_HEAD_LASER = registerSimplePartItem("sat_head_laser");
    public static final DeferredItem<Item> SAT_HEAD_RESONATOR = registerSimplePartItem("sat_head_resonator");
    public static final DeferredItem<Item> SEG_10 = registerSimplePartItem("seg_10");
    public static final DeferredItem<Item> SEG_15 = registerSimplePartItem("seg_15");
    public static final DeferredItem<Item> SEG_20 = registerSimplePartItem("seg_20");
    public static final DeferredItem<Item> CAP_ALUMINIUM = registerSimplePartItem("cap_aluminium");
    public static final DeferredItem<Item> FUEL_TANK_SMALL = registerSimplePartItem("fuel_tank_small");
    public static final DeferredItem<Item> FUEL_TANK_MEDIUM = registerSimplePartItem("fuel_tank_medium");
    public static final DeferredItem<Item> FUEL_TANK_LARGE = registerSimplePartItem("fuel_tank_large");
    public static final DeferredItem<Item> TANK_STEEL = registerSimplePartItem("tank_steel");
    public static final DeferredItem<Item> COMBINE_SCRAP = registerSimplePartItem("combine_scrap");
    public static final DeferredItem<Item> ROD_EMPTY = registerSingleStackControlItem("rod_empty");
    public static final DeferredItem<Item> ROD_DUAL_EMPTY = registerSingleStackControlItem("rod_dual_empty");
    public static final DeferredItem<Item> ROD_QUAD_EMPTY = registerSingleStackControlItem("rod_quad_empty");
    public static final DeferredItem<Item> ROD_TH232 = registerRemainderLoreControlItem("rod_th232", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_TH232 = registerRemainderLoreControlItem("rod_dual_th232", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_TH232 = registerRemainderLoreControlItem("rod_quad_th232", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_URANIUM = registerRemainderLoreControlItem("rod_uranium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_URANIUM = registerRemainderLoreControlItem("rod_dual_uranium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_URANIUM = registerRemainderLoreControlItem("rod_quad_uranium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_U233 = registerRemainderLoreControlItem("rod_u233", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_U233 = registerRemainderLoreControlItem("rod_dual_u233", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_U233 = registerRemainderLoreControlItem("rod_quad_u233", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_U235 = registerRemainderLoreControlItem("rod_u235", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_U235 = registerRemainderLoreControlItem("rod_dual_u235", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_U235 = registerRemainderLoreControlItem("rod_quad_u235", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_U238 = registerRemainderLoreControlItem("rod_u238", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_U238 = registerRemainderLoreControlItem("rod_dual_u238", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_U238 = registerRemainderLoreControlItem("rod_quad_u238", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_PLUTONIUM = registerRemainderLoreControlItem("rod_plutonium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_PLUTONIUM = registerRemainderLoreControlItem("rod_dual_plutonium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_PLUTONIUM = registerRemainderLoreControlItem("rod_quad_plutonium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_PU238 = registerRemainderLoreControlItem("rod_pu238", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_PU238 = registerRemainderLoreControlItem("rod_dual_pu238", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_PU238 = registerRemainderLoreControlItem("rod_quad_pu238", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_PU239 = registerRemainderLoreControlItem("rod_pu239", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_PU239 = registerRemainderLoreControlItem("rod_dual_pu239", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_PU239 = registerRemainderLoreControlItem("rod_quad_pu239", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_PU240 = registerRemainderLoreControlItem("rod_pu240", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_PU240 = registerRemainderLoreControlItem("rod_dual_pu240", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_PU240 = registerRemainderLoreControlItem("rod_quad_pu240", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_RGP = registerRemainderLoreControlItem("rod_rgp", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_RGP = registerRemainderLoreControlItem("rod_dual_rgp", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_RGP = registerRemainderLoreControlItem("rod_quad_rgp", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_NEPTUNIUM = registerRemainderLoreControlItem("rod_neptunium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_NEPTUNIUM = registerRemainderLoreControlItem("rod_dual_neptunium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_NEPTUNIUM = registerRemainderLoreControlItem("rod_quad_neptunium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_POLONIUM = registerRemainderLoreControlItem("rod_polonium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_POLONIUM = registerRemainderLoreControlItem("rod_dual_polonium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_POLONIUM = registerRemainderLoreControlItem("rod_quad_polonium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_LEAD = registerRemainderControlItem("rod_lead", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_LEAD = registerRemainderControlItem("rod_dual_lead", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_LEAD = registerRemainderControlItem("rod_quad_lead", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_SCHRABIDIUM = registerRemainderLoreControlItem("rod_schrabidium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_SCHRABIDIUM = registerRemainderLoreControlItem("rod_dual_schrabidium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_SCHRABIDIUM = registerRemainderLoreControlItem("rod_quad_schrabidium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_SOLINIUM = registerRemainderLoreControlItem("rod_solinium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_SOLINIUM = registerRemainderLoreControlItem("rod_dual_solinium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_SOLINIUM = registerRemainderLoreControlItem("rod_quad_solinium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_BALEFIRE = registerRemainderLoreControlItem("rod_balefire", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_BALEFIRE = registerRemainderLoreControlItem("rod_dual_balefire", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_BALEFIRE = registerRemainderLoreControlItem("rod_quad_balefire", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_BALEFIRE_BLAZING = registerRemainderLoreControlItem("rod_balefire_blazing", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_BALEFIRE_BLAZING = registerRemainderLoreControlItem("rod_dual_balefire_blazing", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_BALEFIRE_BLAZING = registerRemainderLoreControlItem("rod_quad_balefire_blazing", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_THORIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_thorium_fuel_depleted", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_THORIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_dual_thorium_fuel_depleted", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_THORIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_quad_thorium_fuel_depleted", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_URANIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_uranium_fuel_depleted", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_URANIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_dual_uranium_fuel_depleted", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_URANIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_quad_uranium_fuel_depleted", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_PLUTONIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_plutonium_fuel_depleted", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_PLUTONIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_dual_plutonium_fuel_depleted", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_PLUTONIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_quad_plutonium_fuel_depleted", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_MOX_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_mox_fuel_depleted", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_MOX_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_dual_mox_fuel_depleted", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_MOX_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_quad_mox_fuel_depleted", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_SCHRABIDIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_schrabidium_fuel_depleted", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_SCHRABIDIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_dual_schrabidium_fuel_depleted", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_SCHRABIDIUM_FUEL_DEPLETED = registerRemainderLoreControlItem("rod_quad_schrabidium_fuel_depleted", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_WASTE = registerRemainderLoreControlItem("rod_waste", ROD_EMPTY);
    public static final DeferredItem<Item> ROD_DUAL_WASTE = registerRemainderLoreControlItem("rod_dual_waste", ROD_DUAL_EMPTY);
    public static final DeferredItem<Item> ROD_QUAD_WASTE = registerRemainderLoreControlItem("rod_quad_waste", ROD_QUAD_EMPTY);
    public static final DeferredItem<Item> ROD_WATER = registerRemainderLoreControlItem("rod_water", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_WATER = registerRemainderLoreControlItem("rod_dual_water", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_WATER = registerRemainderLoreControlItem("rod_quad_water", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_COOLANT = registerRemainderLoreControlItem("rod_coolant", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_COOLANT = registerRemainderLoreControlItem("rod_dual_coolant", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_COOLANT = registerRemainderLoreControlItem("rod_quad_coolant", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_LITHIUM = registerRemainderLoreControlItem("rod_lithium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_LITHIUM = registerRemainderLoreControlItem("rod_dual_lithium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_LITHIUM = registerRemainderLoreControlItem("rod_quad_lithium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_TRITIUM = registerRemainderLoreControlItem("rod_tritium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_TRITIUM = registerRemainderLoreControlItem("rod_dual_tritium", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_TRITIUM = registerRemainderLoreControlItem("rod_quad_tritium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_AC227 = registerRemainderLoreControlItem("rod_ac227", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_AC227 = registerRemainderLoreControlItem("rod_dual_ac227", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_AC227 = registerRemainderLoreControlItem("rod_quad_ac227", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_COBALT = registerRemainderControlItem("rod_cobalt", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_COBALT = registerRemainderControlItem("rod_dual_cobalt", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_COBALT = registerRemainderControlItem("rod_quad_cobalt", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_CO60 = registerRemainderLoreControlItem("rod_co60", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_CO60 = registerRemainderLoreControlItem("rod_dual_co60", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_CO60 = registerRemainderLoreControlItem("rod_quad_co60", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_RA226 = registerRemainderLoreControlItem("rod_ra226", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DUAL_RA226 = registerRemainderLoreControlItem("rod_dual_ra226", ROD_DUAL_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_RA226 = registerRemainderLoreControlItem("rod_quad_ra226", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_QUAD_EUPHEMIUM = registerRemainderLoreControlItem("rod_quad_euphemium", ROD_QUAD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_EUPHEMIUM = registerRemainderLoreControlItem("rod_euphemium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_REIIUM = registerRemainderLoreControlItem("rod_reiium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_WEIDANIUM = registerRemainderLoreControlItem("rod_weidanium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_AUSTRALIUM = registerRemainderLoreControlItem("rod_australium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_VERTICIUM = registerRemainderLoreControlItem("rod_verticium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_UNOBTAINIUM = registerRemainderLoreControlItem("rod_unobtainium", ROD_EMPTY, 1);
    public static final DeferredItem<Item> ROD_DAFFERGON = registerRemainderLoreControlItem("rod_daffergon", ROD_EMPTY, 1);
    public static final DeferredItem<Item> SYRINGE_EMPTY = registerSimpleConsumableItem("syringe_empty");
    public static final DeferredItem<Item> SYRINGE_METAL_EMPTY = registerSimpleConsumableItem("syringe_metal_empty");
    public static final DeferredItem<Item> BOTTLE_EMPTY = registerSimpleConsumableItem("bottle_empty");
    public static final DeferredItem<Item> BOTTLE2_EMPTY = registerSimpleConsumableItem("bottle2_empty");
    public static final DeferredItem<Item> CAN_KEY = registerSimpleConsumableItem("can_key");
    public static final DeferredItem<Item> CAN_EMPTY = registerSimpleConsumableItem("can_empty");
    public static final DeferredItem<Item> CAP_NUKA = registerSimpleConsumableItem("cap_nuka");
    public static final DeferredItem<Item> CAP_QUANTUM = registerSimpleConsumableItem("cap_quantum");
    public static final DeferredItem<Item> CAP_SPARKLE = registerSimpleConsumableItem("cap_sparkle");
    public static final DeferredItem<Item> CAP_RAD = registerSimpleConsumableItem("cap_rad");
    public static final DeferredItem<Item> CAP_KORL = registerSimpleConsumableItem("cap_korl");
    public static final DeferredItem<Item> CAP_FRITZ = registerSimpleConsumableItem("cap_fritz");
    public static final DeferredItem<Item> CAP_SUNSET = registerSimpleConsumableItem("cap_sunset");
    public static final DeferredItem<Item> CAP_STAR = registerSimpleConsumableItem("cap_star");
    public static final DeferredItem<Item> COIN_CREEPER = registerSimpleConsumableItem("coin_creeper");
    public static final DeferredItem<Item> MED_BAG = registerInstantConsumableItem(
        "med_bag",
        properties -> properties.stacksTo(1),
        (level, livingEntity) -> {
            livingEntity.setHealth(livingEntity.getMaxHealth());
            HbmEffectUtil.clearLegacyNegativeEffects(livingEntity);
        },
        SoundEvents.HONEY_DRINK,
        List.of(
            InstantMedicalItem.greenTooltip("Full heal, regardless of max health"),
            InstantMedicalItem.grayTooltip("Removes negative effects")
        )
    );
    public static final DeferredItem<Item> RADAWAY = registerInstantConsumableItem(
        "radaway",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.RADAWAY, 200, 24), livingEntity),
        SoundEvents.HONEY_DRINK
    );
    public static final DeferredItem<Item> RADAWAY_STRONG = registerInstantConsumableItem(
        "radaway_strong",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.RADAWAY, 100, 99), livingEntity),
        SoundEvents.HONEY_DRINK
    );
    public static final DeferredItem<Item> RADAWAY_FLUSH = registerInstantConsumableItem(
        "radaway_flush",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.RADAWAY, 50, 399), livingEntity),
        SoundEvents.HONEY_DRINK
    );
    public static final DeferredItem<Item> RADX = registerMedicalPillItem(
        "radx",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.RADX, 3 * 60 * 20, 3), livingEntity);
        },
        List.of(InstantMedicalItem.grayTooltip("Increases radiation resistance by 0.4 for 3 minutes"))
    );
    public static final DeferredItem<Item> SIOX = registerMedicalPillItem(
        "siox",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            HbmEffectUtil.reduceLungDamage(livingEntity);
        },
        List.of(InstantMedicalItem.grayTooltip("Reverses mesothelioma with the power of Asbestos!"))
    );
    public static final DeferredItem<Item> PILL_HERBAL = registerMedicalPillItem(
        "pill_herbal",
        (level, livingEntity) -> {
            HbmEffectUtil.reduceLungDamage(livingEntity);
            HbmAttachmentAccess.living(livingEntity).decreaseRads(100.0F);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10 * 20, 0), livingEntity);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 60 * 20, 2), livingEntity);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 10 * 60 * 20, 2), livingEntity);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 5 * 20, 2), livingEntity);
            HbmEffectUtil.applyPotionSickness(livingEntity, 10 * 60);
        },
        List.of(
            InstantMedicalItem.grayTooltip("Effective treatment against lung disease and mild radiation poisoning"),
            InstantMedicalItem.grayTooltip("Comes with side effects")
        )
    );
    public static final DeferredItem<Item> XANAX = registerMedicalPillItem(
        "xanax",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            HbmAttachmentAccess.living(livingEntity).decreaseDigamma(0.5F);
        },
        List.of(InstantMedicalItem.grayTooltip("Removes 500mDRX"))
    );
    public static final DeferredItem<Item> FMN = registerMedicalPillItem(
        "fmn",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            HbmAttachmentAccess.living(livingEntity).setDigamma(Math.min(HbmAttachmentAccess.living(livingEntity).getDigamma(), 2.0F));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0), livingEntity);
        },
        List.of(InstantMedicalItem.grayTooltip("Removes all DRX above 2,000mDRX"))
    );
    public static final DeferredItem<Item> FIVE_HTP = registerMedicalPillItem(
        "five_htp",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            HbmAttachmentAccess.living(livingEntity).setDigamma(0.0F);
            livingEntity.addEffect(new MobEffectInstance(HbmMobEffects.STABILITY, 10 * 60 * 20, 0), livingEntity);
        },
        List.of(InstantMedicalItem.grayTooltip("Removes all DRX, Stability for 10 minutes"))
    );
    public static final DeferredItem<Item> PILL_IODINE = registerMedicalPillItem(
        "pill_iodine",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            HbmEffectUtil.clearLegacyNegativeEffects(livingEntity);
        },
        List.of(InstantMedicalItem.grayTooltip("Removes negative effects"))
    );
    public static final DeferredItem<Item> PLAN_C = registerMedicalPillItem(
        "plan_c",
        (level, livingEntity) -> {
            HbmEffectUtil.applyPotionSickness(livingEntity, 5);
            for (int i = 0; i < 10 && livingEntity.isAlive(); i++) {
                livingEntity.hurt(level.damageSources().magic(), 1000.0F);
            }
        },
        List.of(InstantMedicalItem.grayTooltip("Deadly"))
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER_RAG = registerGasMaskFilterItem(
        "gas_mask_filter_rag",
        4000,
        List.of(HbmHazardClass.PARTICLE_COARSE)
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER_PISS = registerGasMaskFilterItem(
        "gas_mask_filter_piss",
        4000,
        List.of(HbmHazardClass.PARTICLE_COARSE, HbmHazardClass.GAS_CHLORINE)
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER_MONO = registerGasMaskFilterItem(
        "gas_mask_filter_mono",
        12000,
        List.of(HbmHazardClass.PARTICLE_COARSE, HbmHazardClass.GAS_MONOXIDE)
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER = registerGasMaskFilterItem(
        "gas_mask_filter",
        18000,
        List.of(
            HbmHazardClass.PARTICLE_COARSE,
            HbmHazardClass.PARTICLE_FINE,
            HbmHazardClass.GAS_CHLORINE,
            HbmHazardClass.BACTERIA,
            HbmHazardClass.NERVE_AGENT
        )
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER_COMBO = registerGasMaskFilterItem(
        "gas_mask_filter_combo",
        24000,
        List.of(
            HbmHazardClass.PARTICLE_COARSE,
            HbmHazardClass.PARTICLE_FINE,
            HbmHazardClass.GAS_CHLORINE,
            HbmHazardClass.BACTERIA,
            HbmHazardClass.GAS_MONOXIDE,
            HbmHazardClass.NERVE_AGENT
        )
    );
    public static final DeferredItem<Item> GAS_MASK_FILTER_RADON = registerGasMaskFilterItem(
        "gas_mask_filter_radon",
        32000,
        List.of(
            HbmHazardClass.RAD_GAS,
            HbmHazardClass.PARTICLE_COARSE,
            HbmHazardClass.PARTICLE_FINE,
            HbmHazardClass.GAS_CHLORINE,
            HbmHazardClass.BACTERIA,
            HbmHazardClass.GAS_MONOXIDE,
            HbmHazardClass.NERVE_AGENT
        )
    );
    public static final DeferredItem<Item> ATTACHMENT_MASK = registerGasMaskAttachmentItem(
        "attachment_mask",
        List.of(HbmHazardClass.GAS_CORROSIVE),
        true
    );
    public static final DeferredItem<Item> ATTACHMENT_MASK_MONO = registerGasMaskAttachmentItem(
        "attachment_mask_mono",
        List.of(HbmHazardClass.GAS_CHLORINE, HbmHazardClass.GAS_CORROSIVE, HbmHazardClass.BACTERIA),
        false
    );
    public static final DeferredItem<Item> SERVO_SET =
        registerServoArmorModItem("servo_set", 0.5D, 0.25D, 0, 1);
    public static final DeferredItem<Item> SERVO_SET_DESH =
        registerServoArmorModItem("servo_set_desh", 1.5D, 0.5D, 2, 2);
    public static final DeferredItem<Item> ARMOR_BATTERY =
        registerBatteryArmorModItem("armor_battery", 1.25D);
    public static final DeferredItem<Item> ARMOR_BATTERY_MK2 =
        registerBatteryArmorModItem("armor_battery_mk2", 1.5D);
    public static final DeferredItem<Item> ARMOR_BATTERY_MK3 =
        registerBatteryArmorModItem("armor_battery_mk3", 2.0D);
    public static final DeferredItem<Item> ARMOR_BATTERY_MK4 =
        registerBatteryArmorModItem("armor_battery_mk4", 10.0D);
    public static final DeferredItem<Item> GAS_SENSOR =
        registerGasSensorArmorModItem("gas_sensor");
    public static final DeferredItem<Item> PROTECTION_CHARM = registerCharmArmorModItem(
        "protection_charm",
        0.5F,
        List.of(
            "Diverts meteors away from the player.",
            "Meteors no longer destroy blocks.",
            "Halves broadcaster damage."
        )
    );
    public static final DeferredItem<Item> METEOR_CHARM = registerCharmArmorModItem(
        "meteor_charm",
        0.0F,
        List.of(
            "Disables meteorite spawning.",
            "Negates broadcaster damage."
        )
    );
    public static final DeferredItem<Item> NEUTRINO_LENS =
        registerSurveyLensArmorModItem("neutrino_lens");
    public static final DeferredItem<Item> GAS_MASK = registerGasMaskArmorItem(
        "gas_mask",
        ArmorMaterials.IRON,
        armorTexture("gasmask.png"),
        armorTexture("gasmask.png"),
        miscTexture("overlay_gasmask.png"),
        0.07F,
        List.of(HbmHazardClass.GAS_CORROSIVE)
    );
    public static final DeferredItem<Item> GAS_MASK_M65 = registerGasMaskArmorItem(
        "gas_mask_m65",
        ArmorMaterials.IRON,
        armorTexture("modelm65.png"),
        armorTexture("modelm65.png"),
        miscTexture("overlay_goggles.png"),
        0.095F,
        List.of(HbmHazardClass.GAS_CORROSIVE)
    );
    public static final DeferredItem<Item> GAS_MASK_MONO = registerGasMaskArmorItem(
        "gas_mask_mono",
        ArmorMaterials.IRON,
        armorTexture("modelm65mono.png"),
        armorTexture("modelm65mono.png"),
        miscTexture("overlay_goggles.png"),
        0.0F,
        List.of(HbmHazardClass.GAS_CHLORINE, HbmHazardClass.GAS_CORROSIVE, HbmHazardClass.BACTERIA)
    );
    public static final DeferredItem<Item> HAZMAT_HELMET = registerGasMaskArmorItem(
        "hazmat_helmet",
        HbmArmorMaterials.HAZMAT,
        armorTexture("hazmat_1.png"),
        armorTexture("hazmat_1.png"),
        miscTexture("overlay_hazmat.png"),
        0.12F,
        List.of()
    );
    public static final DeferredItem<Item> HAZMAT_PLATE = registerArmorItem(
        "hazmat_plate",
        HbmArmorMaterials.HAZMAT,
        ArmorItem.Type.CHESTPLATE,
        armorTexture("hazmat_1.png"),
        armorTexture("hazmat_2.png"),
        0.24F
    );
    public static final DeferredItem<Item> HAZMAT_LEGS = registerArmorItem(
        "hazmat_legs",
        HbmArmorMaterials.HAZMAT,
        ArmorItem.Type.LEGGINGS,
        armorTexture("hazmat_1.png"),
        armorTexture("hazmat_2.png"),
        0.18F
    );
    public static final DeferredItem<Item> HAZMAT_BOOTS = registerArmorItem(
        "hazmat_boots",
        HbmArmorMaterials.HAZMAT,
        ArmorItem.Type.BOOTS,
        armorTexture("hazmat_1.png"),
        armorTexture("hazmat_2.png"),
        0.06F
    );
    public static final DeferredItem<Item> HAZMAT_HELMET_RED = registerGasMaskArmorItem(
        "hazmat_helmet_red",
        HbmArmorMaterials.HAZMAT_RED,
        armorTexture("modelhazred.png"),
        armorTexture("modelhazred.png"),
        miscTexture("overlay_goggles.png"),
        0.2F,
        List.of()
    );
    public static final DeferredItem<Item> HAZMAT_PLATE_RED = registerArmorItem(
        "hazmat_plate_red",
        HbmArmorMaterials.HAZMAT_RED,
        ArmorItem.Type.CHESTPLATE,
        armorTexture("hazmat_1_red.png"),
        armorTexture("hazmat_2_red.png"),
        0.4F
    );
    public static final DeferredItem<Item> HAZMAT_LEGS_RED = registerArmorItem(
        "hazmat_legs_red",
        HbmArmorMaterials.HAZMAT_RED,
        ArmorItem.Type.LEGGINGS,
        armorTexture("hazmat_1_red.png"),
        armorTexture("hazmat_2_red.png"),
        0.3F
    );
    public static final DeferredItem<Item> HAZMAT_BOOTS_RED = registerArmorItem(
        "hazmat_boots_red",
        HbmArmorMaterials.HAZMAT_RED,
        ArmorItem.Type.BOOTS,
        armorTexture("hazmat_1_red.png"),
        armorTexture("hazmat_2_red.png"),
        0.1F
    );
    public static final DeferredItem<Item> HAZMAT_HELMET_GREY = registerGasMaskArmorItem(
        "hazmat_helmet_grey",
        HbmArmorMaterials.HAZMAT_GREY,
        armorTexture("modelhazgrey.png"),
        armorTexture("modelhazgrey.png"),
        miscTexture("overlay_goggles.png"),
        0.4F,
        List.of()
    );
    public static final DeferredItem<Item> HAZMAT_PLATE_GREY = registerArmorItem(
        "hazmat_plate_grey",
        HbmArmorMaterials.HAZMAT_GREY,
        ArmorItem.Type.CHESTPLATE,
        armorTexture("hazmat_1_grey.png"),
        armorTexture("hazmat_2_grey.png"),
        0.8F
    );
    public static final DeferredItem<Item> HAZMAT_LEGS_GREY = registerArmorItem(
        "hazmat_legs_grey",
        HbmArmorMaterials.HAZMAT_GREY,
        ArmorItem.Type.LEGGINGS,
        armorTexture("hazmat_1_grey.png"),
        armorTexture("hazmat_2_grey.png"),
        0.6F
    );
    public static final DeferredItem<Item> HAZMAT_BOOTS_GREY = registerArmorItem(
        "hazmat_boots_grey",
        HbmArmorMaterials.HAZMAT_GREY,
        ArmorItem.Type.BOOTS,
        armorTexture("hazmat_1_grey.png"),
        armorTexture("hazmat_2_grey.png"),
        0.2F
    );
    public static final DeferredItem<Item> HAZMAT_PAA_HELMET = registerGasMaskArmorItem(
        "hazmat_paa_helmet",
        HbmArmorMaterials.HAZMAT_PAA,
        armorTexture("modelhazpaa.png"),
        armorTexture("modelhazpaa.png"),
        miscTexture("overlay_hazmat.png"),
        0.6F,
        List.of()
    );
    public static final DeferredItem<Item> HAZMAT_PAA_PLATE = registerArmorItem(
        "hazmat_paa_plate",
        HbmArmorMaterials.HAZMAT_PAA,
        ArmorItem.Type.CHESTPLATE,
        armorTexture("hazmat_paa_1.png"),
        armorTexture("hazmat_paa_2.png"),
        1.2F
    );
    public static final DeferredItem<Item> HAZMAT_PAA_LEGS = registerArmorItem(
        "hazmat_paa_legs",
        HbmArmorMaterials.HAZMAT_PAA,
        ArmorItem.Type.LEGGINGS,
        armorTexture("hazmat_paa_1.png"),
        armorTexture("hazmat_paa_2.png"),
        0.9F
    );
    public static final DeferredItem<Item> HAZMAT_PAA_BOOTS = registerArmorItem(
        "hazmat_paa_boots",
        HbmArmorMaterials.HAZMAT_PAA,
        ArmorItem.Type.BOOTS,
        armorTexture("hazmat_paa_1.png"),
        armorTexture("hazmat_paa_2.png"),
        0.3F
    );
    public static final DeferredItem<Item> BACK_TESLA = registerTooltipArmorModItem(
        "back_tesla",
        ArmorModSlot.CHEST_ONLY,
        false,
        true,
        false,
        false,
        List.of(tooltip(ChatFormatting.YELLOW, "Zaps nearby entities (requires full electric set)"))
    );
    public static final DeferredItem<Item> PADS_RUBBER = registerPadsArmorModItem("pads_rubber", 0.5F, false);
    public static final DeferredItem<Item> PADS_SLIME = registerPadsArmorModItem("pads_slime", 0.25F, false);
    public static final DeferredItem<Item> PADS_STATIC = registerPadsArmorModItem("pads_static", 0.75F, true);
    public static final DeferredItem<Item> CLADDING_PAINT = registerCladdingArmorModItem("cladding_paint", 0.025F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_RUBBER = registerCladdingArmorModItem("cladding_rubber", 0.05F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_LEAD = registerCladdingArmorModItem("cladding_lead", 0.1F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_DESH = registerCladdingArmorModItem("cladding_desh", 0.2F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_PAA = registerCladdingArmorModItem("cladding_paa", 0.3F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_GHIORSIUM = registerCladdingArmorModItem("cladding_ghiorsium", 0.5F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_EUPHEMIUM = registerCladdingArmorModItem("cladding_euphemium", 0.8F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_DI = registerCladdingArmorModItem("cladding_di", 1.2F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_ELECTRONIUM = registerCladdingArmorModItem("cladding_electronium", 2.0F, 0.0D, List.of());
    public static final DeferredItem<Item> CLADDING_IRON = registerCladdingArmorModItem("cladding_iron", 0.0F, 0.5D, List.of());
    public static final DeferredItem<Item> CLADDING_OBSIDIAN = registerTooltipArmorModItem(
        "cladding_obsidian",
        ArmorModSlot.CLADDING,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.DARK_PURPLE, "Makes dropped armor indestructible"))
    );
    public static final DeferredItem<Item> INSERT_KEVLAR = registerInsertArmorModItem("insert_kevlar", 1500, 1.0F, 0.9F, 1.0F, 1.0F);
    public static final DeferredItem<Item> INSERT_SAPI = registerInsertArmorModItem("insert_sapi", 1750, 1.0F, 0.85F, 1.0F, 1.0F);
    public static final DeferredItem<Item> INSERT_ESAPI = registerInsertArmorModItem("insert_esapi", 2000, 0.95F, 0.8F, 1.0F, 1.0F);
    public static final DeferredItem<Item> INSERT_XSAPI = registerInsertArmorModItem("insert_xsapi", 2500, 0.9F, 0.75F, 1.0F, 1.0F);
    public static final DeferredItem<Item> INSERT_STEEL = registerInsertArmorModItem("insert_steel", 1000, 1.0F, 0.95F, 0.75F, 0.95F);
    public static final DeferredItem<Item> INSERT_DU = registerInsertArmorModItem("insert_du", 1500, 0.9F, 0.85F, 0.5F, 0.9F);
    public static final DeferredItem<Item> INSERT_FERROURANIUM = registerInsertArmorModItem("insert_ferrouranium", 2000, 1.0F, 0.9F, 0.9F, 1.0F);
    public static final DeferredItem<Item> INSERT_POLONIUM = registerInsertArmorModItem("insert_polonium", 500, 0.9F, 1.0F, 0.25F, 0.9F);
    public static final DeferredItem<Item> INSERT_GHIORSIUM = registerInsertArmorModItem("insert_ghiorsium", 2000, 0.8F, 0.75F, 0.35F, 0.9F);
    public static final DeferredItem<Item> INSERT_ERA = registerInsertArmorModItem("insert_era", 25, 0.5F, 1.0F, 0.25F, 1.0F);
    public static final DeferredItem<Item> INSERT_DI = registerInsertArmorModItem("insert_di", 4000, 1.0F, 0.01F, 0.01F, 0.01F);
    public static final DeferredItem<Item> INSERT_YHARONITE = registerInsertArmorModItem("insert_yharonite", 9999, 0.01F, 1.0F, 1.0F, 1.0F);
    public static final DeferredItem<Item> INSERT_DOXIUM = registerInsertArmorModItem("insert_doxium", 9999, 5.0F, 1.0F, 1.0F, 1.0F);
    public static final DeferredItem<Item> ARMOR_POLISH = registerTooltipArmorModItem(
        "armor_polish",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.BLUE, "5% chance to nullify damage"))
    );
    public static final DeferredItem<Item> BANDAID = registerTooltipArmorModItem(
        "bandaid",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.RED, "3% chance for full heal when damaged"))
    );
    public static final DeferredItem<Item> SERUM = registerTooltipArmorModItem(
        "serum",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.GREEN, "Cures poison and gives strength"))
    );
    public static final DeferredItem<Item> QUARTZ_PLUTONIUM = registerTooltipArmorModItem(
        "quartz_plutonium",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.DARK_GRAY, "Taking damage removes 10 RAD"))
    );
    public static final DeferredItem<Item> MORNING_GLORY = registerTooltipArmorModItem(
        "morning_glory",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.LIGHT_PURPLE, "5% chance to apply resistance when hit, wither immunity"))
    );
    public static final DeferredItem<Item> LODESTONE = registerLodestoneArmorModItem("lodestone", 5);
    public static final DeferredItem<Item> HORSESHOE_MAGNET = registerLodestoneArmorModItem("horseshoe_magnet", 8);
    public static final DeferredItem<Item> INDUSTRIAL_MAGNET = registerLodestoneArmorModItem("industrial_magnet", 12);
    public static final DeferredItem<Item> BATHWATER = registerTooltipArmorModItem(
        "bathwater",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.LIGHT_PURPLE, "Inflicts Poison II on the attacker"))
    );
    public static final DeferredItem<Item> BATHWATER_MK2 = registerTooltipArmorModItem(
        "bathwater_mk2",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.YELLOW, "Inflicts Wither IV on the attacker"))
    );
    public static final DeferredItem<Item> BATHWATER_MK3 = registerTooltipArmorModItem(
        "bathwater_mk3",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.DARK_RED, "Inflicts Radiation on the attacker"))
    );
    public static final DeferredItem<Item> SPIDER_MILK = registerTooltipArmorModItem(
        "spider_milk",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(tooltip(ChatFormatting.WHITE, "Removes bad potion effects"))
    );
    public static final DeferredItem<Item> INK = registerTooltipArmorModItem(
        "ink",
        ArmorModSlot.EXTRA,
        true,
        true,
        true,
        true,
        List.of(
            tooltip(ChatFormatting.LIGHT_PURPLE, "10% chance to nullify damage"),
            tooltip(ChatFormatting.LIGHT_PURPLE, "Flowers!")
        )
    );
    public static final DeferredItem<Item> HEART_PIECE = registerHealthArmorModItem(
        "heart_piece",
        false,
        true,
        false,
        false,
        10.0D,
        List.of()
    );
    public static final DeferredItem<Item> HEART_CONTAINER = registerHealthArmorModItem(
        "heart_container",
        false,
        true,
        false,
        false,
        40.0D,
        List.of()
    );
    public static final DeferredItem<Item> HEART_BOOSTER = registerHealthArmorModItem(
        "heart_booster",
        false,
        true,
        false,
        false,
        80.0D,
        List.of()
    );
    public static final DeferredItem<Item> HEART_FAB = registerHealthArmorModItem(
        "heart_fab",
        false,
        true,
        false,
        false,
        120.0D,
        List.of()
    );
    public static final DeferredItem<Item> BLACK_DIAMOND = registerHealthArmorModItem(
        "black_diamond",
        false,
        true,
        false,
        false,
        80.0D,
        List.of(tooltip(ChatFormatting.DARK_GRAY, "Nostalgia"))
    );
    public static final DeferredItem<Item> WD40 = registerHealthArmorModItem(
        "wd40",
        true,
        true,
        true,
        true,
        4.0D,
        List.of(tooltip(ChatFormatting.BLUE, "Highly reduces damage taken by armor"))
    );
    public static final DeferredItem<Item> SCRUMPY = registerReviveArmorModItem(
        "scrumpy",
        1,
        List.of(
            tooltip(ChatFormatting.GOLD, "But how did you survive?"),
            tooltip(ChatFormatting.RED, "I was drunk.")
        )
    );
    public static final DeferredItem<Item> WILD_P = registerReviveArmorModItem(
        "wild_p",
        3,
        List.of(tooltip(ChatFormatting.DARK_GRAY, "Explosive Reactive Plot Armor"))
    );
    public static final DeferredItem<Item> FABSOLS_VODKA = registerReviveArmorModItem(
        "fabsols_vodka",
        9999,
        List.of(tooltip(ChatFormatting.RED, "Man literally too angry to die."))
    );
    public static final DeferredItem<Item> SHACKLES = registerTooltipArmorModItem(
        "shackles",
        ArmorModSlot.EXTRA,
        false,
        false,
        true,
        false,
        List.of(
            tooltip(ChatFormatting.RED, "You will speak when I ask you to."),
            tooltip(ChatFormatting.RED, "You will eat when I tell you to."),
            tooltip(ChatFormatting.GOLD, "Infinite revives while radiation stays below 1000 RAD")
        )
    );
    public static final DeferredItem<Item> INJECTOR_5HTP = registerTooltipArmorModItem(
        "injector_5htp",
        ArmorModSlot.EXTRA,
        false,
        true,
        false,
        false,
        List.of(tooltip(ChatFormatting.BLUE, "Automatically stabilizes severe DRX exposure"))
    );
    public static final DeferredItem<Item> INJECTOR_KNIFE = registerTooltipArmorModItem(
        "injector_knife",
        ArmorModSlot.EXTRA,
        false,
        true,
        false,
        false,
        List.of(
            tooltip(ChatFormatting.RED, "Automatically injects blood-fuel stimulants."),
            tooltip(ChatFormatting.RED, "Further knife-injector behavior is still pending.")
        )
    );
    public static final DeferredItem<Item> DECONTAMINATION_MODULE = registerMedalArmorModItem("decontamination_module", 0.05F);
    public static final DeferredItem<Item> MEDAL_LIQUIDATOR = registerMedalArmorModItem("medal_liquidator", 0.5F);
    public static final DeferredItem<Item> MEDAL_GHOUL = registerMedalArmorModItem("medal_ghoul", 2.5F);
    public static final DeferredItem<Item> V1 = registerTooltipArmorModItem(
        "v1",
        ArmorModSlot.EXTRA,
        false,
        true,
        false,
        false,
        List.of(tooltip(ChatFormatting.RED, "BLOOD IS FUEL"))
    );
    public static final DeferredItem<Item> POCKET_PTSD = registerTooltipArmorModItem(
        "pocket_ptsd",
        ArmorModSlot.EXTRA,
        true,
        false,
        false,
        false,
        List.of(
            tooltip(ChatFormatting.YELLOW, "Alerts when incoming missiles are detected"),
            tooltip(ChatFormatting.YELLOW, "Range: 1000m")
        )
    );
    public static final DeferredItem<Item> FUSE = registerLoreControlItem("fuse", properties -> properties.stacksTo(16));
    public static final DeferredItem<Item> OVERFUSE = registerLoreControlItem("overfuse", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> ARC_ELECTRODE =
        registerLoreControlItem("arc_electrode", properties -> properties.durability(250));
    public static final DeferredItem<Item> ARC_ELECTRODE_DESH =
        registerLoreControlItem("arc_electrode_desh", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> ARC_ELECTRODE_BURNT = registerSimpleControlItem("arc_electrode_burnt", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> PISTON_SELENIUM = registerSimpleControlItem("piston_selenium", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> THERMO_ELEMENT = registerSimpleControlItem("thermo_element", properties -> properties.stacksTo(16));
    public static final DeferredItem<Item> CATALYTIC_CONVERTER = registerSimpleControlItem("catalytic_converter", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> ANTIKNOCK = registerSimpleControlItem("antiknock");
    public static final DeferredItem<Item> PART_LITHIUM = registerSimpleControlItem("part_lithium");
    public static final DeferredItem<Item> PART_BERYLLIUM = registerSimpleControlItem("part_beryllium");
    public static final DeferredItem<Item> PART_CARBON = registerSimpleControlItem("part_carbon");
    public static final DeferredItem<Item> PART_COPPER = registerSimpleControlItem("part_copper");
    public static final DeferredItem<Item> PART_PLUTONIUM = registerSimpleControlItem("part_plutonium");
    public static final DeferredItem<Item> CAPSULE_EMPTY = registerSimpleControlItem("capsule_empty");
    public static final DeferredItem<Item> AMS_CATALYST_BLANK = registerSingleStackControlItem("ams_catalyst_blank");
    public static final DeferredItem<Item> STAMP_STONE_FLAT = registerDurableControlItem("stamp_stone_flat", 16, false);
    public static final DeferredItem<Item> STAMP_STONE_PLATE = registerDurableControlItem("stamp_stone_plate", 16, true);
    public static final DeferredItem<Item> STAMP_STONE_WIRE = registerDurableControlItem("stamp_stone_wire", 16, true);
    public static final DeferredItem<Item> STAMP_STONE_CIRCUIT = registerDurableControlItem("stamp_stone_circuit", 16, true);
    public static final DeferredItem<Item> STAMP_IRON_FLAT = registerDurableControlItem("stamp_iron_flat", 64, false);
    public static final DeferredItem<Item> STAMP_IRON_PLATE = registerDurableControlItem("stamp_iron_plate", 64, true);
    public static final DeferredItem<Item> STAMP_IRON_WIRE = registerDurableControlItem("stamp_iron_wire", 64, true);
    public static final DeferredItem<Item> STAMP_IRON_CIRCUIT = registerDurableControlItem("stamp_iron_circuit", 128, true);
    public static final DeferredItem<Item> STAMP_TITANIUM_FLAT = registerDurableControlItem("stamp_titanium_flat", 96, false);
    public static final DeferredItem<Item> STAMP_TITANIUM_PLATE = registerDurableControlItem("stamp_titanium_plate", 96, true);
    public static final DeferredItem<Item> STAMP_TITANIUM_WIRE = registerDurableControlItem("stamp_titanium_wire", 96, true);
    public static final DeferredItem<Item> STAMP_TITANIUM_CIRCUIT = registerDurableControlItem("stamp_titanium_circuit", 96, true);
    public static final DeferredItem<Item> STAMP_OBSIDIAN_FLAT = registerDurableControlItem("stamp_obsidian_flat", 128, false);
    public static final DeferredItem<Item> STAMP_OBSIDIAN_PLATE = registerDurableControlItem("stamp_obsidian_plate", 128, true);
    public static final DeferredItem<Item> STAMP_OBSIDIAN_WIRE = registerDurableControlItem("stamp_obsidian_wire", 128, true);
    public static final DeferredItem<Item> STAMP_OBSIDIAN_CIRCUIT = registerDurableControlItem("stamp_obsidian_circuit", 128, true);
    public static final DeferredItem<Item> STAMP_STEEL_FLAT = registerDurableControlItem("stamp_steel_flat", 256, false);
    public static final DeferredItem<Item> STAMP_STEEL_PLATE = registerDurableControlItem("stamp_steel_plate", 256, true);
    public static final DeferredItem<Item> STAMP_STEEL_WIRE = registerDurableControlItem("stamp_steel_wire", 256, true);
    public static final DeferredItem<Item> STAMP_STEEL_CIRCUIT = registerDurableControlItem("stamp_steel_circuit", 128, true);
    public static final DeferredItem<Item> STAMP_SCHRABIDIUM_FLAT = registerDurableControlItem("stamp_schrabidium_flat", 4096, false);
    public static final DeferredItem<Item> STAMP_SCHRABIDIUM_PLATE = registerDurableControlItem("stamp_schrabidium_plate", 4096, true);
    public static final DeferredItem<Item> STAMP_SCHRABIDIUM_WIRE = registerDurableControlItem("stamp_schrabidium_wire", 4096, true);
    public static final DeferredItem<Item> STAMP_SCHRABIDIUM_CIRCUIT = registerDurableControlItem("stamp_schrabidium_circuit", 4096, true);
    public static final DeferredItem<Item> STAMP_DESH_FLAT = registerDurableControlItem("stamp_desh_flat", 0, false);
    public static final DeferredItem<Item> STAMP_DESH_PLATE = registerDurableControlItem("stamp_desh_plate", 0, true);
    public static final DeferredItem<Item> STAMP_DESH_WIRE = registerDurableControlItem("stamp_desh_wire", 0, true);
    public static final DeferredItem<Item> STAMP_DESH_CIRCUIT = registerDurableControlItem("stamp_desh_circuit", 0, true);
    public static final DeferredItem<Item> STAMP_DESH_357 = registerDurableControlItem("stamp_desh_357", 0, false);
    public static final DeferredItem<Item> STAMP_DESH_44 = registerDurableControlItem("stamp_desh_44", 0, false);
    public static final DeferredItem<Item> STAMP_DESH_9 = registerDurableControlItem("stamp_desh_9", 0, false);
    public static final DeferredItem<Item> STAMP_DESH_50 = registerDurableControlItem("stamp_desh_50", 0, false);
    public static final DeferredItem<Item> STAMP_357 = registerDurableControlItem("stamp_357", 512, false);
    public static final DeferredItem<Item> STAMP_44 = registerDurableControlItem("stamp_44", 512, false);
    public static final DeferredItem<Item> STAMP_9 = registerDurableControlItem("stamp_9", 512, false);
    public static final DeferredItem<Item> STAMP_50 = registerDurableControlItem("stamp_50", 512, false);
    public static final DeferredItem<Item> BLADES_ALUMINUM = registerDurableControlItem("blades_aluminum", 24, false);
    public static final DeferredItem<Item> BLADES_GOLD = registerDurableControlItem("blades_gold", 32, false);
    public static final DeferredItem<Item> BLADES_IRON = registerDurableControlItem("blades_iron", 64, false);
    public static final DeferredItem<Item> BLADES_STEEL = registerDurableControlItem("blades_steel", 128, false);
    public static final DeferredItem<Item> BLADES_TITANIUM = registerDurableControlItem("blades_titanium", 96, false);
    public static final DeferredItem<Item> BLADES_ADVANCED_ALLOY = registerDurableControlItem("blades_advanced_alloy", 256, false);
    public static final DeferredItem<Item> BLADES_COMBINE_STEEL = registerDurableControlItem("blades_combine_steel", 1024, false);
    public static final DeferredItem<Item> BLADES_SCHRABIDIUM = registerDurableControlItem("blades_schrabidium", 4096, false);
    public static final DeferredItem<Item> BLADES_DESH = registerDurableControlItem("blades_desh", 0, false);
    public static final DeferredItem<Item> BATTERY_GENERIC = registerBatteryItem("battery_generic", 5_000, 100, 100);
    public static final DeferredItem<Item> BATTERY_RED_CELL = registerBatteryItem("battery_red_cell", 15_000, 100, 100);
    public static final DeferredItem<Item> BATTERY_RED_CELL_6 = registerBatteryItem("battery_red_cell_6", 90_000, 600, 600);
    public static final DeferredItem<Item> BATTERY_RED_CELL_24 = registerBatteryItem("battery_red_cell_24", 360_000, 2_400, 2_400);
    public static final DeferredItem<Item> BATTERY_ADVANCED = registerBatteryItem("battery_advanced", 20_000, 500, 500);
    public static final DeferredItem<Item> BATTERY_ADVANCED_CELL = registerBatteryItem("battery_advanced_cell", 60_000, 500, 500);
    public static final DeferredItem<Item> BATTERY_ADVANCED_CELL_4 = registerBatteryItem("battery_advanced_cell_4", 240_000, 2_000, 2_000);
    public static final DeferredItem<Item> BATTERY_ADVANCED_CELL_12 = registerBatteryItem("battery_advanced_cell_12", 720_000, 6_000, 6_000);
    public static final DeferredItem<Item> BATTERY_LITHIUM = registerBatteryItem("battery_lithium", 250_000, 2_000, 2_000);
    public static final DeferredItem<Item> BATTERY_LITHIUM_CELL = registerBatteryItem("battery_lithium_cell", 750_000, 2_000, 2_000);
    public static final DeferredItem<Item> BATTERY_LITHIUM_CELL_3 = registerBatteryItem("battery_lithium_cell_3", 2_250_000, 6_000, 6_000);
    public static final DeferredItem<Item> BATTERY_LITHIUM_CELL_6 = registerBatteryItem("battery_lithium_cell_6", 4_500_000, 12_000, 12_000);
    public static final DeferredItem<BlockItem> ASPHALT = ITEMS.registerSimpleBlockItem("asphalt", HbmBlocks.ASPHALT);
    public static final DeferredItem<BlockItem> BLOCK_SLAG = ITEMS.registerSimpleBlockItem("block_slag", HbmBlocks.BLOCK_SLAG);
    public static final DeferredItem<BlockItem> STONE_GNEISS = ITEMS.registerSimpleBlockItem("stone_gneiss", HbmBlocks.STONE_GNEISS);
    public static final DeferredItem<BlockItem> STONE_CRACKED = ITEMS.registerSimpleBlockItem("stone_cracked", HbmBlocks.STONE_CRACKED);
    public static final DeferredItem<BlockItem> BASALT = ITEMS.registerSimpleBlockItem("basalt", HbmBlocks.BASALT);
    public static final DeferredItem<BlockItem> BASALT_SMOOTH = ITEMS.registerSimpleBlockItem("basalt_smooth", HbmBlocks.BASALT_SMOOTH);
    public static final DeferredItem<BlockItem> BASALT_BRICK = ITEMS.registerSimpleBlockItem("basalt_brick", HbmBlocks.BASALT_BRICK);
    public static final DeferredItem<BlockItem> BASALT_POLISHED = ITEMS.registerSimpleBlockItem("basalt_polished", HbmBlocks.BASALT_POLISHED);
    public static final DeferredItem<BlockItem> BASALT_TILES = ITEMS.registerSimpleBlockItem("basalt_tiles", HbmBlocks.BASALT_TILES);
    public static final DeferredItem<BlockItem> METEOR_POLISHED = ITEMS.registerSimpleBlockItem("meteor_polished", HbmBlocks.METEOR_POLISHED);
    public static final DeferredItem<BlockItem> METEOR_BRICK = ITEMS.registerSimpleBlockItem("meteor_brick", HbmBlocks.METEOR_BRICK);
    public static final DeferredItem<BlockItem> METEOR_BRICK_MOSSY = ITEMS.registerSimpleBlockItem("meteor_brick_mossy", HbmBlocks.METEOR_BRICK_MOSSY);
    public static final DeferredItem<BlockItem> METEOR_BRICK_CRACKED = ITEMS.registerSimpleBlockItem("meteor_brick_cracked", HbmBlocks.METEOR_BRICK_CRACKED);
    public static final DeferredItem<BlockItem> METEOR_BRICK_CHISELED = ITEMS.registerSimpleBlockItem("meteor_brick_chiseled", HbmBlocks.METEOR_BRICK_CHISELED);
    public static final DeferredItem<BlockItem> BRICK_DUNGEON = ITEMS.registerSimpleBlockItem("brick_dungeon", HbmBlocks.BRICK_DUNGEON);
    public static final DeferredItem<BlockItem> BRICK_DUNGEON_FLAT = ITEMS.registerSimpleBlockItem("brick_dungeon_flat", HbmBlocks.BRICK_DUNGEON_FLAT);
    public static final DeferredItem<BlockItem> BRICK_DUNGEON_TILE = ITEMS.registerSimpleBlockItem("brick_dungeon_tile", HbmBlocks.BRICK_DUNGEON_TILE);
    public static final DeferredItem<BlockItem> BRICK_DUNGEON_CIRCLE = ITEMS.registerSimpleBlockItem("brick_dungeon_circle", HbmBlocks.BRICK_DUNGEON_CIRCLE);
    public static final DeferredItem<BlockItem> MUFFLER = ITEMS.registerSimpleBlockItem("muffler", HbmBlocks.MUFFLER);
    public static final DeferredItem<BlockItem> REINFORCED_GLASS = ITEMS.registerSimpleBlockItem("reinforced_glass", HbmBlocks.REINFORCED_GLASS);
    public static final DeferredItem<BlockItem> CRATE_IRON = ITEMS.registerSimpleBlockItem("crate_iron", HbmBlocks.CRATE_IRON);
    public static final DeferredItem<BlockItem> CRATE_STEEL = ITEMS.registerSimpleBlockItem("crate_steel", HbmBlocks.CRATE_STEEL);
    public static final DeferredItem<BlockItem> CRATE_DESH = ITEMS.registerSimpleBlockItem("crate_desh", HbmBlocks.CRATE_DESH);
    public static final DeferredItem<BlockItem> SAFE = ITEMS.registerSimpleBlockItem("safe", HbmBlocks.SAFE);
    public static final DeferredItem<BlockItem> MACHINE_PRESS = ITEMS.registerSimpleBlockItem("machine_press", HbmBlocks.MACHINE_PRESS);
    public static final DeferredItem<BlockItem> MACHINE_EPRESS = ITEMS.registerSimpleBlockItem("machine_epress", HbmBlocks.MACHINE_EPRESS);
    public static final DeferredItem<BlockItem> MACHINE_ELECTRIC_FURNACE = ITEMS.registerSimpleBlockItem("machine_electric_furnace_off", HbmBlocks.MACHINE_ELECTRIC_FURNACE);
    public static final DeferredItem<BlockItem> MACHINE_TRANSFORMER = ITEMS.registerSimpleBlockItem("machine_transformer", HbmBlocks.MACHINE_TRANSFORMER);
    public static final DeferredItem<BlockItem> MACHINE_TRANSFORMER_20 = ITEMS.registerSimpleBlockItem("machine_transformer_20", HbmBlocks.MACHINE_TRANSFORMER_20);
    public static final DeferredItem<BlockItem> MACHINE_TRANSFORMER_DNT = ITEMS.registerSimpleBlockItem("machine_transformer_dnt", HbmBlocks.MACHINE_TRANSFORMER_DNT);
    public static final DeferredItem<BlockItem> MACHINE_TRANSFORMER_DNT_20 = ITEMS.registerSimpleBlockItem("machine_transformer_dnt_20", HbmBlocks.MACHINE_TRANSFORMER_DNT_20);
    public static final DeferredItem<BlockItem> MACHINE_BATTERY_POTATO = ITEMS.registerSimpleBlockItem("machine_battery_potato", HbmBlocks.MACHINE_BATTERY_POTATO);
    public static final DeferredItem<BlockItem> MACHINE_BATTERY = ITEMS.registerSimpleBlockItem("machine_battery", HbmBlocks.MACHINE_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_LITHIUM_BATTERY = ITEMS.registerSimpleBlockItem("machine_lithium_battery", HbmBlocks.MACHINE_LITHIUM_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_DESH_BATTERY = ITEMS.registerSimpleBlockItem("machine_desh_battery", HbmBlocks.MACHINE_DESH_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_SATURNITE_BATTERY = ITEMS.registerSimpleBlockItem("machine_saturnite_battery", HbmBlocks.MACHINE_SATURNITE_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_SCHRABIDIUM_BATTERY = ITEMS.registerSimpleBlockItem("machine_schrabidium_battery", HbmBlocks.MACHINE_SCHRABIDIUM_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_EUPHEMIUM_BATTERY = ITEMS.registerSimpleBlockItem("machine_euphemium_battery", HbmBlocks.MACHINE_EUPHEMIUM_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_RADSPICE_BATTERY = ITEMS.registerSimpleBlockItem("machine_radspice_battery", HbmBlocks.MACHINE_RADSPICE_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_DINEUTRONIUM_BATTERY = ITEMS.registerSimpleBlockItem("machine_dineutronium_battery", HbmBlocks.MACHINE_DINEUTRONIUM_BATTERY);
    public static final DeferredItem<BlockItem> MACHINE_ELECTRONIUM_BATTERY = ITEMS.registerSimpleBlockItem("machine_electronium_battery", HbmBlocks.MACHINE_ELECTRONIUM_BATTERY);
    public static final DeferredItem<BlockItem> RED_CABLE = ITEMS.registerSimpleBlockItem("red_cable", HbmBlocks.RED_CABLE);
    public static final DeferredItem<BlockItem> CABLE_SWITCH = ITEMS.registerSimpleBlockItem("cable_switch", HbmBlocks.CABLE_SWITCH);
    public static final DeferredItem<BlockItem> CABLE_DETECTOR = ITEMS.registerSimpleBlockItem("cable_detector", HbmBlocks.CABLE_DETECTOR);
    public static final DeferredItem<BlockItem> CABLE_DIODE = ITEMS.registerSimpleBlockItem("cable_diode", HbmBlocks.CABLE_DIODE);
    public static final DeferredItem<BlockItem> RED_CABLE_GAUGE = ITEMS.registerSimpleBlockItem("red_cable_gauge", HbmBlocks.RED_CABLE_GAUGE);
    public static final DeferredItem<BlockItem> RED_CONNECTOR = ITEMS.registerSimpleBlockItem("red_connector", HbmBlocks.RED_CONNECTOR);
    public static final DeferredItem<BlockItem> FLUID_DUCT_MK2 = ITEMS.registerSimpleBlockItem("fluid_duct_mk2", HbmBlocks.FLUID_DUCT_MK2);
    public static final DeferredItem<BlockItem> FLUID_DUCT_SOLID = ITEMS.registerSimpleBlockItem("fluid_duct_solid", HbmBlocks.FLUID_DUCT_SOLID);
    public static final DeferredItem<BlockItem> FLUID_DUCT_SOLID_SEALED = ITEMS.registerSimpleBlockItem("fluid_duct_solid_sealed", HbmBlocks.FLUID_DUCT_SOLID_SEALED);
    public static final DeferredItem<BlockItem> MACHINE_SHREDDER = ITEMS.registerSimpleBlockItem("machine_shredder", HbmBlocks.MACHINE_SHREDDER);
    public static final DeferredItem<BlockItem> PRESS_PREHEATER = ITEMS.registerSimpleBlockItem("press_preheater", HbmBlocks.PRESS_PREHEATER);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE = ITEMS.registerSimpleBlockItem("brick_concrete", HbmBlocks.BRICK_CONCRETE);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_STAIRS = ITEMS.registerSimpleBlockItem("brick_concrete_stairs", HbmBlocks.BRICK_CONCRETE_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_SLAB = ITEMS.registerSimpleBlockItem("brick_concrete_slab", HbmBlocks.BRICK_CONCRETE_SLAB);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_MOSSY = ITEMS.registerSimpleBlockItem("brick_concrete_mossy", HbmBlocks.BRICK_CONCRETE_MOSSY);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_MOSSY_STAIRS = ITEMS.registerSimpleBlockItem("brick_concrete_mossy_stairs", HbmBlocks.BRICK_CONCRETE_MOSSY_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_MOSSY_SLAB = ITEMS.registerSimpleBlockItem("brick_concrete_mossy_slab", HbmBlocks.BRICK_CONCRETE_MOSSY_SLAB);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_CRACKED = ITEMS.registerSimpleBlockItem("brick_concrete_cracked", HbmBlocks.BRICK_CONCRETE_CRACKED);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_CRACKED_STAIRS = ITEMS.registerSimpleBlockItem("brick_concrete_cracked_stairs", HbmBlocks.BRICK_CONCRETE_CRACKED_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_CRACKED_SLAB = ITEMS.registerSimpleBlockItem("brick_concrete_cracked_slab", HbmBlocks.BRICK_CONCRETE_CRACKED_SLAB);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_BROKEN = ITEMS.registerSimpleBlockItem("brick_concrete_broken", HbmBlocks.BRICK_CONCRETE_BROKEN);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_BROKEN_STAIRS = ITEMS.registerSimpleBlockItem("brick_concrete_broken_stairs", HbmBlocks.BRICK_CONCRETE_BROKEN_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_CONCRETE_BROKEN_SLAB = ITEMS.registerSimpleBlockItem("brick_concrete_broken_slab", HbmBlocks.BRICK_CONCRETE_BROKEN_SLAB);
    public static final DeferredItem<BlockItem> REINFORCED_BRICK = ITEMS.registerSimpleBlockItem("reinforced_brick", HbmBlocks.REINFORCED_BRICK);
    public static final DeferredItem<BlockItem> REINFORCED_BRICK_STAIRS = ITEMS.registerSimpleBlockItem("reinforced_brick_stairs", HbmBlocks.REINFORCED_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> REINFORCED_BRICK_SLAB = ITEMS.registerSimpleBlockItem("reinforced_brick_slab", HbmBlocks.REINFORCED_BRICK_SLAB);
    public static final DeferredItem<BlockItem> BRICK_COMPOUND = ITEMS.registerSimpleBlockItem("brick_compound", HbmBlocks.BRICK_COMPOUND);
    public static final DeferredItem<BlockItem> BRICK_COMPOUND_STAIRS = ITEMS.registerSimpleBlockItem("brick_compound_stairs", HbmBlocks.BRICK_COMPOUND_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_COMPOUND_SLAB = ITEMS.registerSimpleBlockItem("brick_compound_slab", HbmBlocks.BRICK_COMPOUND_SLAB);
    public static final DeferredItem<BlockItem> BRICK_FIRE = ITEMS.registerSimpleBlockItem("brick_fire", HbmBlocks.BRICK_FIRE);
    public static final DeferredItem<BlockItem> BRICK_FIRE_STAIRS = ITEMS.registerSimpleBlockItem("brick_fire_stairs", HbmBlocks.BRICK_FIRE_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_FIRE_SLAB = ITEMS.registerSimpleBlockItem("brick_fire_slab", HbmBlocks.BRICK_FIRE_SLAB);
    public static final DeferredItem<BlockItem> BRICK_LIGHT = ITEMS.registerSimpleBlockItem("brick_light", HbmBlocks.BRICK_LIGHT);
    public static final DeferredItem<BlockItem> BRICK_LIGHT_STAIRS = ITEMS.registerSimpleBlockItem("brick_light_stairs", HbmBlocks.BRICK_LIGHT_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_LIGHT_SLAB = ITEMS.registerSimpleBlockItem("brick_light_slab", HbmBlocks.BRICK_LIGHT_SLAB);
    public static final DeferredItem<BlockItem> REINFORCED_SAND = ITEMS.registerSimpleBlockItem("reinforced_sand", HbmBlocks.REINFORCED_SAND);
    public static final DeferredItem<BlockItem> REINFORCED_SAND_STAIRS = ITEMS.registerSimpleBlockItem("reinforced_sand_stairs", HbmBlocks.REINFORCED_SAND_STAIRS);
    public static final DeferredItem<BlockItem> REINFORCED_SAND_SLAB = ITEMS.registerSimpleBlockItem("reinforced_sand_slab", HbmBlocks.REINFORCED_SAND_SLAB);
    public static final DeferredItem<BlockItem> BRICK_OBSIDIAN = ITEMS.registerSimpleBlockItem("brick_obsidian", HbmBlocks.BRICK_OBSIDIAN);
    public static final DeferredItem<BlockItem> BRICK_OBSIDIAN_STAIRS = ITEMS.registerSimpleBlockItem("brick_obsidian_stairs", HbmBlocks.BRICK_OBSIDIAN_STAIRS);
    public static final DeferredItem<BlockItem> BRICK_OBSIDIAN_SLAB = ITEMS.registerSimpleBlockItem("brick_obsidian_slab", HbmBlocks.BRICK_OBSIDIAN_SLAB);
    public static final DeferredItem<BlockItem> CMB_BRICK = ITEMS.registerSimpleBlockItem("cmb_brick", HbmBlocks.CMB_BRICK);
    public static final DeferredItem<BlockItem> CMB_BRICK_REINFORCED = ITEMS.registerSimpleBlockItem("cmb_brick_reinforced", HbmBlocks.CMB_BRICK_REINFORCED);
    public static final DeferredItem<BlockItem> CMB_BRICK_REINFORCED_STAIRS = ITEMS.registerSimpleBlockItem("cmb_brick_reinforced_stairs", HbmBlocks.CMB_BRICK_REINFORCED_STAIRS);
    public static final DeferredItem<BlockItem> CMB_BRICK_REINFORCED_SLAB = ITEMS.registerSimpleBlockItem("cmb_brick_reinforced_slab", HbmBlocks.CMB_BRICK_REINFORCED_SLAB);
    public static final DeferredItem<BlockItem> DUCRETE_SMOOTH = ITEMS.registerSimpleBlockItem("ducrete_smooth", HbmBlocks.DUCRETE_SMOOTH);
    public static final DeferredItem<BlockItem> DUCRETE_SMOOTH_STAIRS = ITEMS.registerSimpleBlockItem("ducrete_smooth_stairs", HbmBlocks.DUCRETE_SMOOTH_STAIRS);
    public static final DeferredItem<BlockItem> DUCRETE_SMOOTH_SLAB = ITEMS.registerSimpleBlockItem("ducrete_smooth_slab", HbmBlocks.DUCRETE_SMOOTH_SLAB);
    public static final DeferredItem<BlockItem> DUCRETE = ITEMS.registerSimpleBlockItem("ducrete", HbmBlocks.DUCRETE);
    public static final DeferredItem<BlockItem> DUCRETE_STAIRS = ITEMS.registerSimpleBlockItem("ducrete_stairs", HbmBlocks.DUCRETE_STAIRS);
    public static final DeferredItem<BlockItem> DUCRETE_SLAB = ITEMS.registerSimpleBlockItem("ducrete_slab", HbmBlocks.DUCRETE_SLAB);
    public static final DeferredItem<BlockItem> DUCRETE_BRICK = ITEMS.registerSimpleBlockItem("ducrete_brick", HbmBlocks.DUCRETE_BRICK);
    public static final DeferredItem<BlockItem> DUCRETE_BRICK_STAIRS = ITEMS.registerSimpleBlockItem("ducrete_brick_stairs", HbmBlocks.DUCRETE_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> DUCRETE_BRICK_SLAB = ITEMS.registerSimpleBlockItem("ducrete_brick_slab", HbmBlocks.DUCRETE_BRICK_SLAB);
    public static final DeferredItem<BlockItem> DUCRETE_REINFORCED = ITEMS.registerSimpleBlockItem("ducrete_reinforced", HbmBlocks.DUCRETE_REINFORCED);
    public static final DeferredItem<BlockItem> DUCRETE_REINFORCED_STAIRS = ITEMS.registerSimpleBlockItem("ducrete_reinforced_stairs", HbmBlocks.DUCRETE_REINFORCED_STAIRS);
    public static final DeferredItem<BlockItem> DUCRETE_REINFORCED_SLAB = ITEMS.registerSimpleBlockItem("ducrete_reinforced_slab", HbmBlocks.DUCRETE_REINFORCED_SLAB);
    public static final DeferredItem<BlockItem> TILE_LAB = ITEMS.registerSimpleBlockItem("tile_lab", HbmBlocks.TILE_LAB);
    public static final DeferredItem<BlockItem> TILE_LAB_STAIRS = ITEMS.registerSimpleBlockItem("tile_lab_stairs", HbmBlocks.TILE_LAB_STAIRS);
    public static final DeferredItem<BlockItem> TILE_LAB_SLAB = ITEMS.registerSimpleBlockItem("tile_lab_slab", HbmBlocks.TILE_LAB_SLAB);
    public static final DeferredItem<BlockItem> TILE_LAB_CRACKED = ITEMS.registerSimpleBlockItem("tile_lab_cracked", HbmBlocks.TILE_LAB_CRACKED);
    public static final DeferredItem<BlockItem> TILE_LAB_CRACKED_STAIRS = ITEMS.registerSimpleBlockItem("tile_lab_cracked_stairs", HbmBlocks.TILE_LAB_CRACKED_STAIRS);
    public static final DeferredItem<BlockItem> TILE_LAB_CRACKED_SLAB = ITEMS.registerSimpleBlockItem("tile_lab_cracked_slab", HbmBlocks.TILE_LAB_CRACKED_SLAB);
    public static final DeferredItem<BlockItem> TILE_LAB_BROKEN = ITEMS.registerSimpleBlockItem("tile_lab_broken", HbmBlocks.TILE_LAB_BROKEN);
    public static final DeferredItem<BlockItem> TILE_LAB_BROKEN_STAIRS = ITEMS.registerSimpleBlockItem("tile_lab_broken_stairs", HbmBlocks.TILE_LAB_BROKEN_STAIRS);
    public static final DeferredItem<BlockItem> TILE_LAB_BROKEN_SLAB = ITEMS.registerSimpleBlockItem("tile_lab_broken_slab", HbmBlocks.TILE_LAB_BROKEN_SLAB);
    public static final DeferredItem<BlockItem> BLOCK_ADVANCED_ALLOY = ITEMS.registerSimpleBlockItem("block_advanced_alloy", HbmBlocks.BLOCK_ADVANCED_ALLOY);
    public static final DeferredItem<BlockItem> BLOCK_ALUMINIUM = ITEMS.registerSimpleBlockItem("block_aluminium", HbmBlocks.BLOCK_ALUMINIUM);
    public static final DeferredItem<BlockItem> BLOCK_BERYLLIUM = ITEMS.registerSimpleBlockItem("block_beryllium", HbmBlocks.BLOCK_BERYLLIUM);
    public static final DeferredItem<BlockItem> BLOCK_BISMUTH = ITEMS.registerSimpleBlockItem("block_bismuth", HbmBlocks.BLOCK_BISMUTH);
    public static final DeferredItem<BlockItem> BLOCK_BORON = ITEMS.registerSimpleBlockItem("block_boron", HbmBlocks.BLOCK_BORON);
    public static final DeferredItem<BlockItem> BLOCK_CADMIUM = ITEMS.registerSimpleBlockItem("block_cadmium", HbmBlocks.BLOCK_CADMIUM);
    public static final DeferredItem<BlockItem> BLOCK_COBALT = ITEMS.registerSimpleBlockItem("block_cobalt", HbmBlocks.BLOCK_COBALT);
    public static final DeferredItem<BlockItem> BLOCK_COMBINE_STEEL = ITEMS.registerSimpleBlockItem("block_combine_steel", HbmBlocks.BLOCK_COMBINE_STEEL);
    public static final DeferredItem<BlockItem> BLOCK_COPPER = ITEMS.registerSimpleBlockItem("block_copper", HbmBlocks.BLOCK_COPPER);
    public static final DeferredItem<BlockItem> BLOCK_DESH = ITEMS.registerSimpleBlockItem("block_desh", HbmBlocks.BLOCK_DESH);
    public static final DeferredItem<BlockItem> BLOCK_DINEUTRONIUM = ITEMS.registerSimpleBlockItem("block_dineutronium", HbmBlocks.BLOCK_DINEUTRONIUM);
    public static final DeferredItem<BlockItem> BLOCK_DURA_STEEL = ITEMS.registerSimpleBlockItem("block_dura_steel", HbmBlocks.BLOCK_DURA_STEEL);
    public static final DeferredItem<BlockItem> BLOCK_EUPHEMIUM = ITEMS.registerSimpleBlockItem("block_euphemium", HbmBlocks.BLOCK_EUPHEMIUM);
    public static final DeferredItem<BlockItem> BLOCK_FLUORITE = ITEMS.registerSimpleBlockItem("block_fluorite", HbmBlocks.BLOCK_FLUORITE);
    public static final DeferredItem<BlockItem> BLOCK_LANTHANIUM = ITEMS.registerSimpleBlockItem("block_lanthanium", HbmBlocks.BLOCK_LANTHANIUM);
    public static final DeferredItem<BlockItem> BLOCK_LEAD = ITEMS.registerSimpleBlockItem("block_lead", HbmBlocks.BLOCK_LEAD);
    public static final DeferredItem<BlockItem> BLOCK_NIOBIUM = ITEMS.registerSimpleBlockItem("block_niobium", HbmBlocks.BLOCK_NIOBIUM);
    public static final DeferredItem<BlockItem> BLOCK_RED_COPPER = ITEMS.registerSimpleBlockItem("block_red_copper", HbmBlocks.BLOCK_RED_COPPER);
    public static final DeferredItem<BlockItem> BLOCK_SATURNITE = ITEMS.registerSimpleBlockItem("block_saturnite", HbmBlocks.BLOCK_SATURNITE);
    public static final DeferredItem<BlockItem> BLOCK_STARMETAL = ITEMS.registerSimpleBlockItem("block_starmetal", HbmBlocks.BLOCK_STARMETAL);
    public static final DeferredItem<BlockItem> BLOCK_STEEL = ITEMS.registerSimpleBlockItem("block_steel", HbmBlocks.BLOCK_STEEL);
    public static final DeferredItem<BlockItem> BLOCK_TANTALIUM = ITEMS.registerSimpleBlockItem("block_tantalium", HbmBlocks.BLOCK_TANTALIUM);
    public static final DeferredItem<BlockItem> BLOCK_ACTINIUM = ITEMS.registerSimpleBlockItem("block_actinium", HbmBlocks.BLOCK_ACTINIUM);
    public static final DeferredItem<BlockItem> BLOCK_AUSTRALIUM = ITEMS.registerSimpleBlockItem("block_australium", HbmBlocks.BLOCK_AUSTRALIUM);
    public static final DeferredItem<BlockItem> BLOCK_BAKELITE = ITEMS.registerSimpleBlockItem("block_bakelite", HbmBlocks.BLOCK_BAKELITE);
    public static final DeferredItem<BlockItem> BLOCK_DAFFERGON = ITEMS.registerSimpleBlockItem("block_daffergon", HbmBlocks.BLOCK_DAFFERGON);
    public static final DeferredItem<BlockItem> BLOCK_POLYMER = ITEMS.registerSimpleBlockItem("block_polymer", HbmBlocks.BLOCK_POLYMER);
    public static final DeferredItem<BlockItem> BLOCK_REIIUM = ITEMS.registerSimpleBlockItem("block_reiium", HbmBlocks.BLOCK_REIIUM);
    public static final DeferredItem<BlockItem> BLOCK_RUBBER = ITEMS.registerSimpleBlockItem("block_rubber", HbmBlocks.BLOCK_RUBBER);
    public static final DeferredItem<BlockItem> BLOCK_UNOBTAINIUM = ITEMS.registerSimpleBlockItem("block_unobtainium", HbmBlocks.BLOCK_UNOBTAINIUM);
    public static final DeferredItem<BlockItem> BLOCK_VERTICIUM = ITEMS.registerSimpleBlockItem("block_verticium", HbmBlocks.BLOCK_VERTICIUM);
    public static final DeferredItem<BlockItem> BLOCK_WEIDANIUM = ITEMS.registerSimpleBlockItem("block_weidanium", HbmBlocks.BLOCK_WEIDANIUM);
    public static final DeferredItem<BlockItem> BLOCK_CAP_NUKA = ITEMS.registerSimpleBlockItem("block_cap_nuka", HbmBlocks.BLOCK_CAP_NUKA);
    public static final DeferredItem<BlockItem> BLOCK_CAP_QUANTUM = ITEMS.registerSimpleBlockItem("block_cap_quantum", HbmBlocks.BLOCK_CAP_QUANTUM);
    public static final DeferredItem<BlockItem> BLOCK_CAP_RAD = ITEMS.registerSimpleBlockItem("block_cap_rad", HbmBlocks.BLOCK_CAP_RAD);
    public static final DeferredItem<BlockItem> BLOCK_CAP_SPARKLE = ITEMS.registerSimpleBlockItem("block_cap_sparkle", HbmBlocks.BLOCK_CAP_SPARKLE);
    public static final DeferredItem<BlockItem> BLOCK_CAP_KORL = ITEMS.registerSimpleBlockItem("block_cap_korl", HbmBlocks.BLOCK_CAP_KORL);
    public static final DeferredItem<BlockItem> BLOCK_CAP_FRITZ = ITEMS.registerSimpleBlockItem("block_cap_fritz", HbmBlocks.BLOCK_CAP_FRITZ);
    public static final DeferredItem<BlockItem> BLOCK_CAP_SUNSET = ITEMS.registerSimpleBlockItem("block_cap_sunset", HbmBlocks.BLOCK_CAP_SUNSET);
    public static final DeferredItem<BlockItem> BLOCK_CAP_STAR = ITEMS.registerSimpleBlockItem("block_cap_star", HbmBlocks.BLOCK_CAP_STAR);
    // --- Block items for bulk-ported pillar blocks ---
    public static final DeferredItem<BlockItem> CONCRETE_PILLAR = ITEMS.registerSimpleBlockItem("concrete_pillar", HbmBlocks.CONCRETE_PILLAR);
    public static final DeferredItem<BlockItem> METEOR_PILLAR = ITEMS.registerSimpleBlockItem("meteor_pillar", HbmBlocks.METEOR_PILLAR);
    public static final DeferredItem<BlockItem> BLOCK_SCHRABIDIUM_CLUSTER = ITEMS.registerSimpleBlockItem("block_schrabidium_cluster", HbmBlocks.BLOCK_SCHRABIDIUM_CLUSTER);
    public static final DeferredItem<BlockItem> BLOCK_EUPHEMIUM_CLUSTER = ITEMS.registerSimpleBlockItem("block_euphemium_cluster", HbmBlocks.BLOCK_EUPHEMIUM_CLUSTER);
    public static final DeferredItem<BlockItem> BLOCK_TRITIUM = ITEMS.registerSimpleBlockItem("block_tritium", HbmBlocks.BLOCK_TRITIUM);
    public static final DeferredItem<BlockItem> BLOCK_INSULATOR = ITEMS.registerSimpleBlockItem("block_insulator", HbmBlocks.BLOCK_INSULATOR);
    public static final DeferredItem<BlockItem> BLOCK_FIBERGLASS = ITEMS.registerSimpleBlockItem("block_fiberglass", HbmBlocks.BLOCK_FIBERGLASS);
    public static final DeferredItem<BlockItem> REINFORCED_STONE = ITEMS.registerSimpleBlockItem("reinforced_stone", HbmBlocks.REINFORCED_STONE);
    public static final DeferredItem<BlockItem> REINFORCED_STONE_STAIRS = ITEMS.registerSimpleBlockItem("reinforced_stone_stairs", HbmBlocks.REINFORCED_STONE_STAIRS);
    public static final DeferredItem<BlockItem> REINFORCED_STONE_SLAB = ITEMS.registerSimpleBlockItem("reinforced_stone_slab", HbmBlocks.REINFORCED_STONE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE = ITEMS.registerSimpleBlockItem("concrete", HbmBlocks.CONCRETE);
    public static final DeferredItem<BlockItem> CONCRETE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_stairs", HbmBlocks.CONCRETE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_SLAB = ITEMS.registerSimpleBlockItem("concrete_slab", HbmBlocks.CONCRETE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_SMOOTH = ITEMS.registerSimpleBlockItem("concrete_smooth", HbmBlocks.CONCRETE_SMOOTH);
    public static final DeferredItem<BlockItem> CONCRETE_SMOOTH_STAIRS = ITEMS.registerSimpleBlockItem("concrete_smooth_stairs", HbmBlocks.CONCRETE_SMOOTH_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_SMOOTH_SLAB = ITEMS.registerSimpleBlockItem("concrete_smooth_slab", HbmBlocks.CONCRETE_SMOOTH_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_WHITE = ITEMS.registerSimpleBlockItem("concrete_white", HbmBlocks.CONCRETE_WHITE);
    public static final DeferredItem<BlockItem> CONCRETE_WHITE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_white_stairs", HbmBlocks.CONCRETE_WHITE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_WHITE_SLAB = ITEMS.registerSimpleBlockItem("concrete_white_slab", HbmBlocks.CONCRETE_WHITE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_ORANGE = ITEMS.registerSimpleBlockItem("concrete_orange", HbmBlocks.CONCRETE_ORANGE);
    public static final DeferredItem<BlockItem> CONCRETE_ORANGE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_orange_stairs", HbmBlocks.CONCRETE_ORANGE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_ORANGE_SLAB = ITEMS.registerSimpleBlockItem("concrete_orange_slab", HbmBlocks.CONCRETE_ORANGE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_MAGENTA = ITEMS.registerSimpleBlockItem("concrete_magenta", HbmBlocks.CONCRETE_MAGENTA);
    public static final DeferredItem<BlockItem> CONCRETE_MAGENTA_STAIRS = ITEMS.registerSimpleBlockItem("concrete_magenta_stairs", HbmBlocks.CONCRETE_MAGENTA_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_MAGENTA_SLAB = ITEMS.registerSimpleBlockItem("concrete_magenta_slab", HbmBlocks.CONCRETE_MAGENTA_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_LIGHT_BLUE = ITEMS.registerSimpleBlockItem("concrete_light_blue", HbmBlocks.CONCRETE_LIGHT_BLUE);
    public static final DeferredItem<BlockItem> CONCRETE_LIGHT_BLUE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_light_blue_stairs", HbmBlocks.CONCRETE_LIGHT_BLUE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_LIGHT_BLUE_SLAB = ITEMS.registerSimpleBlockItem("concrete_light_blue_slab", HbmBlocks.CONCRETE_LIGHT_BLUE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_YELLOW = ITEMS.registerSimpleBlockItem("concrete_yellow", HbmBlocks.CONCRETE_YELLOW);
    public static final DeferredItem<BlockItem> CONCRETE_YELLOW_STAIRS = ITEMS.registerSimpleBlockItem("concrete_yellow_stairs", HbmBlocks.CONCRETE_YELLOW_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_YELLOW_SLAB = ITEMS.registerSimpleBlockItem("concrete_yellow_slab", HbmBlocks.CONCRETE_YELLOW_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_LIME = ITEMS.registerSimpleBlockItem("concrete_lime", HbmBlocks.CONCRETE_LIME);
    public static final DeferredItem<BlockItem> CONCRETE_LIME_STAIRS = ITEMS.registerSimpleBlockItem("concrete_lime_stairs", HbmBlocks.CONCRETE_LIME_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_LIME_SLAB = ITEMS.registerSimpleBlockItem("concrete_lime_slab", HbmBlocks.CONCRETE_LIME_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_PINK = ITEMS.registerSimpleBlockItem("concrete_pink", HbmBlocks.CONCRETE_PINK);
    public static final DeferredItem<BlockItem> CONCRETE_PINK_STAIRS = ITEMS.registerSimpleBlockItem("concrete_pink_stairs", HbmBlocks.CONCRETE_PINK_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_PINK_SLAB = ITEMS.registerSimpleBlockItem("concrete_pink_slab", HbmBlocks.CONCRETE_PINK_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_GRAY = ITEMS.registerSimpleBlockItem("concrete_gray", HbmBlocks.CONCRETE_GRAY);
    public static final DeferredItem<BlockItem> CONCRETE_GRAY_STAIRS = ITEMS.registerSimpleBlockItem("concrete_gray_stairs", HbmBlocks.CONCRETE_GRAY_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_GRAY_SLAB = ITEMS.registerSimpleBlockItem("concrete_gray_slab", HbmBlocks.CONCRETE_GRAY_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_SILVER = ITEMS.registerSimpleBlockItem("concrete_silver", HbmBlocks.CONCRETE_SILVER);
    public static final DeferredItem<BlockItem> CONCRETE_SILVER_STAIRS = ITEMS.registerSimpleBlockItem("concrete_silver_stairs", HbmBlocks.CONCRETE_SILVER_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_SILVER_SLAB = ITEMS.registerSimpleBlockItem("concrete_silver_slab", HbmBlocks.CONCRETE_SILVER_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_CYAN = ITEMS.registerSimpleBlockItem("concrete_cyan", HbmBlocks.CONCRETE_CYAN);
    public static final DeferredItem<BlockItem> CONCRETE_CYAN_STAIRS = ITEMS.registerSimpleBlockItem("concrete_cyan_stairs", HbmBlocks.CONCRETE_CYAN_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_CYAN_SLAB = ITEMS.registerSimpleBlockItem("concrete_cyan_slab", HbmBlocks.CONCRETE_CYAN_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_PURPLE = ITEMS.registerSimpleBlockItem("concrete_purple", HbmBlocks.CONCRETE_PURPLE);
    public static final DeferredItem<BlockItem> CONCRETE_PURPLE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_purple_stairs", HbmBlocks.CONCRETE_PURPLE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_PURPLE_SLAB = ITEMS.registerSimpleBlockItem("concrete_purple_slab", HbmBlocks.CONCRETE_PURPLE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_BLUE = ITEMS.registerSimpleBlockItem("concrete_blue", HbmBlocks.CONCRETE_BLUE);
    public static final DeferredItem<BlockItem> CONCRETE_BLUE_STAIRS = ITEMS.registerSimpleBlockItem("concrete_blue_stairs", HbmBlocks.CONCRETE_BLUE_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_BLUE_SLAB = ITEMS.registerSimpleBlockItem("concrete_blue_slab", HbmBlocks.CONCRETE_BLUE_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_BROWN = ITEMS.registerSimpleBlockItem("concrete_brown", HbmBlocks.CONCRETE_BROWN);
    public static final DeferredItem<BlockItem> CONCRETE_BROWN_STAIRS = ITEMS.registerSimpleBlockItem("concrete_brown_stairs", HbmBlocks.CONCRETE_BROWN_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_BROWN_SLAB = ITEMS.registerSimpleBlockItem("concrete_brown_slab", HbmBlocks.CONCRETE_BROWN_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_GREEN = ITEMS.registerSimpleBlockItem("concrete_green", HbmBlocks.CONCRETE_GREEN);
    public static final DeferredItem<BlockItem> CONCRETE_GREEN_STAIRS = ITEMS.registerSimpleBlockItem("concrete_green_stairs", HbmBlocks.CONCRETE_GREEN_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_GREEN_SLAB = ITEMS.registerSimpleBlockItem("concrete_green_slab", HbmBlocks.CONCRETE_GREEN_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_RED = ITEMS.registerSimpleBlockItem("concrete_red", HbmBlocks.CONCRETE_RED);
    public static final DeferredItem<BlockItem> CONCRETE_RED_STAIRS = ITEMS.registerSimpleBlockItem("concrete_red_stairs", HbmBlocks.CONCRETE_RED_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_RED_SLAB = ITEMS.registerSimpleBlockItem("concrete_red_slab", HbmBlocks.CONCRETE_RED_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_BLACK = ITEMS.registerSimpleBlockItem("concrete_black", HbmBlocks.CONCRETE_BLACK);
    public static final DeferredItem<BlockItem> CONCRETE_BLACK_STAIRS = ITEMS.registerSimpleBlockItem("concrete_black_stairs", HbmBlocks.CONCRETE_BLACK_STAIRS);
    public static final DeferredItem<BlockItem> CONCRETE_BLACK_SLAB = ITEMS.registerSimpleBlockItem("concrete_black_slab", HbmBlocks.CONCRETE_BLACK_SLAB);
    public static final DeferredItem<BlockItem> CONCRETE_HAZARD = ITEMS.registerSimpleBlockItem("concrete_hazard", HbmBlocks.CONCRETE_HAZARD);
    public static final DeferredItem<BlockItem> BLOCK_TITANIUM = ITEMS.registerSimpleBlockItem("block_titanium", HbmBlocks.BLOCK_TITANIUM);
    public static final DeferredItem<BlockItem> BLOCK_TUNGSTEN = ITEMS.registerSimpleBlockItem("block_tungsten", HbmBlocks.BLOCK_TUNGSTEN);
    public static final DeferredItem<BlockItem> ORE_ALUMINIUM = ITEMS.registerSimpleBlockItem("ore_aluminium", HbmBlocks.ORE_ALUMINIUM);
    public static final DeferredItem<BlockItem> ORE_BERYLLIUM = ITEMS.registerSimpleBlockItem("ore_beryllium", HbmBlocks.ORE_BERYLLIUM);
    public static final DeferredItem<BlockItem> ORE_CINNEBAR = ITEMS.registerSimpleBlockItem("ore_cinnebar", HbmBlocks.ORE_CINNEBAR);
    public static final DeferredItem<BlockItem> ORE_COBALT = ITEMS.registerSimpleBlockItem("ore_cobalt", HbmBlocks.ORE_COBALT);
    public static final DeferredItem<BlockItem> ORE_COPPER = ITEMS.registerSimpleBlockItem("ore_copper", HbmBlocks.ORE_COPPER);
    public static final DeferredItem<BlockItem> ORE_FLUORITE = ITEMS.registerSimpleBlockItem("ore_fluorite", HbmBlocks.ORE_FLUORITE);
    public static final DeferredItem<BlockItem> ORE_TITANIUM = ITEMS.registerSimpleBlockItem("ore_titanium", HbmBlocks.ORE_TITANIUM);
    public static final DeferredItem<BlockItem> ORE_TUNGSTEN = ITEMS.registerSimpleBlockItem("ore_tungsten", HbmBlocks.ORE_TUNGSTEN);
    public static final DeferredItem<BlockItem> ORE_LEAD = ITEMS.registerSimpleBlockItem("ore_lead", HbmBlocks.ORE_LEAD);
    public static final DeferredItem<BlockItem> ORE_NITER = ITEMS.registerSimpleBlockItem("ore_niter", HbmBlocks.ORE_NITER);
    public static final DeferredItem<BlockItem> ORE_SULFUR = ITEMS.registerSimpleBlockItem("ore_sulfur", HbmBlocks.ORE_SULFUR);
    public static final DeferredItem<BlockItem> CRYSTAL_HARDENED = ITEMS.registerSimpleBlockItem("crystal_hardened", HbmBlocks.CRYSTAL_HARDENED);
    public static final DeferredItem<BlockItem> ORE_BEDROCK_OIL = ITEMS.registerSimpleBlockItem("ore_bedrock_oil", HbmBlocks.ORE_BEDROCK_OIL);
    public static final DeferredItem<BlockItem> ORE_OIL_EMPTY = ITEMS.registerSimpleBlockItem("ore_oil_empty", HbmBlocks.ORE_OIL_EMPTY);
    public static final DeferredItem<BlockItem> METEOR_BATTERY = ITEMS.registerSimpleBlockItem("meteor_battery", HbmBlocks.METEOR_BATTERY);
    public static final DeferredItem<BlockItem> BRICK_JUNGLE = ITEMS.registerSimpleBlockItem("brick_jungle", HbmBlocks.BRICK_JUNGLE);
    public static final DeferredItem<BlockItem> BRICK_JUNGLE_CRACKED = ITEMS.registerSimpleBlockItem("brick_jungle_cracked", HbmBlocks.BRICK_JUNGLE_CRACKED);
    public static final DeferredItem<BlockItem> BLOCK_NITER = ITEMS.registerSimpleBlockItem("block_niter", HbmBlocks.BLOCK_NITER);
    public static final DeferredItem<BlockItem> BLOCK_SULFUR = ITEMS.registerSimpleBlockItem("block_sulfur", HbmBlocks.BLOCK_SULFUR);
    public static final DeferredItem<BlockItem> TEKTITE = ITEMS.registerSimpleBlockItem("tektite", HbmBlocks.TEKTITE);
    public static final DeferredItem<BlockItem> ORE_TEKTITE_OSMIRIDIUM = ITEMS.registerSimpleBlockItem("ore_tektite_osmiridium", HbmBlocks.ORE_TEKTITE_OSMIRIDIUM);
    public static final DeferredItem<BlockItem> DECO_TITANIUM = ITEMS.registerSimpleBlockItem("deco_titanium", HbmBlocks.DECO_TITANIUM);
    public static final DeferredItem<BlockItem> DECO_RED_COPPER = ITEMS.registerSimpleBlockItem("deco_red_copper", HbmBlocks.DECO_RED_COPPER);
    public static final DeferredItem<BlockItem> DECO_TUNGSTEN = ITEMS.registerSimpleBlockItem("deco_tungsten", HbmBlocks.DECO_TUNGSTEN);
    public static final DeferredItem<BlockItem> DECO_ALUMINIUM = ITEMS.registerSimpleBlockItem("deco_aluminium", HbmBlocks.DECO_ALUMINIUM);
    public static final DeferredItem<BlockItem> DECO_STEEL = ITEMS.registerSimpleBlockItem("deco_steel", HbmBlocks.DECO_STEEL);
    public static final DeferredItem<BlockItem> DECO_LEAD = ITEMS.registerSimpleBlockItem("deco_lead", HbmBlocks.DECO_LEAD);
    public static final DeferredItem<BlockItem> DECO_BERYLLIUM = ITEMS.registerSimpleBlockItem("deco_beryllium", HbmBlocks.DECO_BERYLLIUM);
    public static final DeferredItem<BlockItem> BLOCK_SMORE = ITEMS.registerSimpleBlockItem("block_smore", HbmBlocks.BLOCK_SMORE);
    public static final DeferredItem<BlockItem> BLOCK_FOAM = ITEMS.registerSimpleBlockItem("block_foam", HbmBlocks.BLOCK_FOAM);
    public static final DeferredItem<BlockItem> SEAL_FRAME = ITEMS.registerSimpleBlockItem("seal_frame", HbmBlocks.SEAL_FRAME);
    public static final DeferredItem<BlockItem> STRUCT_LAUNCHER = ITEMS.registerSimpleBlockItem("struct_launcher", HbmBlocks.STRUCT_LAUNCHER);
    public static final DeferredItem<BlockItem> STRUCT_SCAFFOLD = ITEMS.registerSimpleBlockItem("struct_scaffold", HbmBlocks.STRUCT_SCAFFOLD);
    public static final DeferredItem<BlockItem> FACTORY_TITANIUM_HULL = ITEMS.registerSimpleBlockItem("factory_titanium_hull", HbmBlocks.FACTORY_TITANIUM_HULL);
    public static final DeferredItem<BlockItem> FACTORY_ADVANCED_HULL = ITEMS.registerSimpleBlockItem("factory_advanced_hull", HbmBlocks.FACTORY_ADVANCED_HULL);
    public static final DeferredItem<BlockItem> FUSION_HATCH = ITEMS.registerSimpleBlockItem("fusion_hatch", HbmBlocks.FUSION_HATCH);
    public static final DeferredItem<BlockItem> FUSION_CORE_BLOCK = ITEMS.registerSimpleBlockItem("fusion_core_block", HbmBlocks.FUSION_CORE_BLOCK);
    public static final DeferredItem<BlockItem> WATZ_ELEMENT = ITEMS.registerSimpleBlockItem("watz_element", HbmBlocks.WATZ_ELEMENT);
    public static final DeferredItem<BlockItem> WATZ_COOLER = ITEMS.registerSimpleBlockItem("watz_cooler", HbmBlocks.WATZ_COOLER);
    public static final DeferredItem<BlockItem> FWATZ_SCAFFOLD = ITEMS.registerSimpleBlockItem("fwatz_scaffold", HbmBlocks.FWATZ_SCAFFOLD);
    public static final DeferredItem<BlockItem> FWATZ_COMPUTER = ITEMS.registerSimpleBlockItem("fwatz_computer", HbmBlocks.FWATZ_COMPUTER);
    public static final DeferredItem<BlockItem> PINK_PLANKS = ITEMS.registerSimpleBlockItem("pink_planks", HbmBlocks.PINK_PLANKS);
    // --- Bulk-ported food items (ItemLemon equivalents) ---
    public static final DeferredItem<Item> BIO_WAFER = registerFoodPartItem("bio_wafer", 8, 8F);
    public static final DeferredItem<Item> INGOT_SEMTEX = registerFoodPartItem("ingot_semtex", 4, 5F);
    public static final DeferredItem<Item> LEMON = registerFoodConsumableItem("lemon", 3, 5F);
    public static final DeferredItem<Item> DEFINITELYFOOD = registerFoodConsumableItem("definitelyfood", 2, 5F);
    public static final DeferredItem<Item> MED_IPECAC = registerFoodConsumableItem("med_ipecac", 0, 0F);
    public static final DeferredItem<Item> MED_PTSD = registerFoodConsumableItem("med_ptsd", 0, 0F);
    public static final DeferredItem<Item> MED_SCHIZOPHRENIA = registerFoodItemNoTab("med_schizophrenia", 0, 0F);
    public static final DeferredItem<Item> LOOPS = registerFoodConsumableItem("loops", 4, 5F);
    public static final DeferredItem<Item> LOOP_STEW = registerFoodConsumableItem("loop_stew", 10, 10F);
    public static final DeferredItem<Item> FOODITEM = registerFoodItemNoTab("fooditem", 2, 5F);
    public static final DeferredItem<Item> TWINKIE = registerFoodConsumableItem("twinkie", 3, 5F);
    public static final DeferredItem<Item> STATIC_SANDWICH = registerFoodConsumableItem("static_sandwich", 6, 5F);
    public static final DeferredItem<Item> NUGGET = registerFoodConsumableItem("nugget", 200, 200F);
    public static final DeferredItem<Item> MARSHMALLOW = registerFoodConsumableItem("marshmallow", 2, 2F);
    public static final DeferredItem<Item> MARSHMALLOW_ROASTED = registerFoodConsumableItem("marshmallow_roasted", 6, 6F);
    public static final DeferredItem<Item> SPONGEBOB_MACARONI = registerFoodConsumableItem("spongebob_macaroni", 5, 5F);
    public static final DeferredItem<Item> CANNED_BEEF = registerFoodConsumableItem("canned_beef", 8, 5F);
    public static final DeferredItem<Item> CANNED_TUNA = registerFoodConsumableItem("canned_tuna", 4, 5F);
    public static final DeferredItem<Item> CANNED_MYSTERY = registerFoodConsumableItem("canned_mystery", 6, 5F);
    public static final DeferredItem<Item> CANNED_PASHTET = registerFoodConsumableItem("canned_pashtet", 4, 5F);
    public static final DeferredItem<Item> CANNED_CHEESE = registerFoodConsumableItem("canned_cheese", 3, 5F);
    public static final DeferredItem<Item> CANNED_JIZZ = registerFoodConsumableItem("canned_jizz", 15, 5F);
    public static final DeferredItem<Item> CANNED_MILK = registerFoodConsumableItem("canned_milk", 5, 5F);
    public static final DeferredItem<Item> CANNED_ASS = registerFoodConsumableItem("canned_ass", 6, 5F);
    public static final DeferredItem<Item> CANNED_PIZZA = registerFoodConsumableItem("canned_pizza", 8, 5F);
    public static final DeferredItem<Item> CANNED_TUBE = registerFoodConsumableItem("canned_tube", 2, 5F);
    public static final DeferredItem<Item> CANNED_TOMATO = registerFoodConsumableItem("canned_tomato", 4, 5F);
    public static final DeferredItem<Item> CANNED_ASBESTOS = registerFoodConsumableItem("canned_asbestos", 7, 5F);
    public static final DeferredItem<Item> CANNED_BHOLE = registerFoodConsumableItem("canned_bhole", 10, 5F);
    public static final DeferredItem<Item> CANNED_HOTDOGS = registerFoodConsumableItem("canned_hotdogs", 5, 5F);
    public static final DeferredItem<Item> CANNED_LEFTOVERS = registerFoodConsumableItem("canned_leftovers", 1, 5F);
    public static final DeferredItem<Item> CANNED_YOGURT = registerFoodConsumableItem("canned_yogurt", 3, 5F);
    public static final DeferredItem<Item> CANNED_STEW = registerFoodConsumableItem("canned_stew", 5, 5F);
    public static final DeferredItem<Item> CANNED_CHINESE = registerFoodConsumableItem("canned_chinese", 6, 5F);
    public static final DeferredItem<Item> CANNED_OIL = registerFoodConsumableItem("canned_oil", 3, 5F);
    public static final DeferredItem<Item> CANNED_FIST = registerFoodConsumableItem("canned_fist", 6, 5F);
    public static final DeferredItem<Item> CANNED_SPAM = registerFoodConsumableItem("canned_spam", 8, 5F);
    public static final DeferredItem<Item> CANNED_FRIED = registerFoodConsumableItem("canned_fried", 10, 5F);
    public static final DeferredItem<Item> CANNED_NAPALM = registerFoodConsumableItem("canned_napalm", 6, 5F);
    public static final DeferredItem<Item> CANNED_DIESEL = registerFoodConsumableItem("canned_diesel", 6, 5F);
    public static final DeferredItem<Item> CANNED_KEROSENE = registerFoodConsumableItem("canned_kerosene", 6, 5F);
    public static final DeferredItem<Item> CANNED_RECURSION = registerFoodConsumableItem("canned_recursion", 1, 5F);
    public static final DeferredItem<Item> CANNED_BARK = registerFoodConsumableItem("canned_bark", 2, 5F);
    public static final DeferredItem<Item> PUDDING = registerFoodConsumableItem("pudding", 6, 15F);


    // --- Block items for bulk-ported BlockHazard-equivalent blocks ---
    // --- Block items for bulk-ported BlockRadResistant-equivalent blocks ---
    public static final DeferredItem<BlockItem> REINFORCED_LIGHT = ITEMS.registerSimpleBlockItem("reinforced_light", HbmBlocks.REINFORCED_LIGHT);
    public static final DeferredItem<BlockItem> BLOCK_NITER_REINFORCED = ITEMS.registerSimpleBlockItem("block_niter_reinforced", HbmBlocks.BLOCK_NITER_REINFORCED);
    public static final DeferredItem<BlockItem> HAZMAT = ITEMS.registerSimpleBlockItem("hazmat", HbmBlocks.HAZMAT);
    public static final DeferredItem<BlockItem> BLOCK_METEOR_MOLTEN = ITEMS.registerSimpleBlockItem("block_meteor_molten", HbmBlocks.BLOCK_METEOR_MOLTEN);
    public static final DeferredItem<BlockItem> BRICK_JUNGLE_LAVA = ITEMS.registerSimpleBlockItem("brick_jungle_lava", HbmBlocks.BRICK_JUNGLE_LAVA);
    public static final DeferredItem<BlockItem> BRICK_JUNGLE_OOZE = ITEMS.registerSimpleBlockItem("brick_jungle_ooze", HbmBlocks.BRICK_JUNGLE_OOZE);
    public static final DeferredItem<BlockItem> BRICK_JUNGLE_MYSTIC = ITEMS.registerSimpleBlockItem("brick_jungle_mystic", HbmBlocks.BRICK_JUNGLE_MYSTIC);
    public static final DeferredItem<BlockItem> BLOCK_THORIUM = ITEMS.registerSimpleBlockItem("block_thorium", HbmBlocks.BLOCK_THORIUM);
    public static final DeferredItem<BlockItem> BLOCK_THORIUM_FUEL = ITEMS.registerSimpleBlockItem("block_thorium_fuel", HbmBlocks.BLOCK_THORIUM_FUEL);
    public static final DeferredItem<BlockItem> BLOCK_NEPTUNIUM = ITEMS.registerSimpleBlockItem("block_neptunium", HbmBlocks.BLOCK_NEPTUNIUM);
    public static final DeferredItem<BlockItem> BLOCK_POLONIUM = ITEMS.registerSimpleBlockItem("block_polonium", HbmBlocks.BLOCK_POLONIUM);
    public static final DeferredItem<BlockItem> BLOCK_MOX_FUEL = ITEMS.registerSimpleBlockItem("block_mox_fuel", HbmBlocks.BLOCK_MOX_FUEL);
    public static final DeferredItem<BlockItem> BLOCK_PLUTONIUM = ITEMS.registerSimpleBlockItem("block_plutonium", HbmBlocks.BLOCK_PLUTONIUM);
    public static final DeferredItem<BlockItem> BLOCK_PU238 = ITEMS.registerSimpleBlockItem("block_pu238", HbmBlocks.BLOCK_PU238);
    public static final DeferredItem<BlockItem> BLOCK_PU239 = ITEMS.registerSimpleBlockItem("block_pu239", HbmBlocks.BLOCK_PU239);
    public static final DeferredItem<BlockItem> BLOCK_PU240 = ITEMS.registerSimpleBlockItem("block_pu240", HbmBlocks.BLOCK_PU240);
    public static final DeferredItem<BlockItem> BLOCK_PU_MIX = ITEMS.registerSimpleBlockItem("block_pu_mix", HbmBlocks.BLOCK_PU_MIX);
    public static final DeferredItem<BlockItem> BLOCK_PLUTONIUM_FUEL = ITEMS.registerSimpleBlockItem("block_plutonium_fuel", HbmBlocks.BLOCK_PLUTONIUM_FUEL);
    public static final DeferredItem<BlockItem> BLOCK_URANIUM = ITEMS.registerSimpleBlockItem("block_uranium", HbmBlocks.BLOCK_URANIUM);
    public static final DeferredItem<BlockItem> BLOCK_U233 = ITEMS.registerSimpleBlockItem("block_u233", HbmBlocks.BLOCK_U233);
    public static final DeferredItem<BlockItem> BLOCK_U235 = ITEMS.registerSimpleBlockItem("block_u235", HbmBlocks.BLOCK_U235);
    public static final DeferredItem<BlockItem> BLOCK_U238 = ITEMS.registerSimpleBlockItem("block_u238", HbmBlocks.BLOCK_U238);
    public static final DeferredItem<BlockItem> BLOCK_URANIUM_FUEL = ITEMS.registerSimpleBlockItem("block_uranium_fuel", HbmBlocks.BLOCK_URANIUM_FUEL);
    public static final DeferredItem<BlockItem> BLOCK_TRINITITE = ITEMS.registerSimpleBlockItem("block_trinitite", HbmBlocks.BLOCK_TRINITITE);
    public static final DeferredItem<BlockItem> BLOCK_SCHRARANIUM = ITEMS.registerSimpleBlockItem("block_schraranium", HbmBlocks.BLOCK_SCHRARANIUM);
    public static final DeferredItem<BlockItem> BLOCK_SCHRABIDIUM = ITEMS.registerSimpleBlockItem("block_schrabidium", HbmBlocks.BLOCK_SCHRABIDIUM);
    public static final DeferredItem<BlockItem> BLOCK_SCHRABIDATE = ITEMS.registerSimpleBlockItem("block_schrabidate", HbmBlocks.BLOCK_SCHRABIDATE);
    public static final DeferredItem<BlockItem> BLOCK_SOLINIUM = ITEMS.registerSimpleBlockItem("block_solinium", HbmBlocks.BLOCK_SOLINIUM);
    public static final DeferredItem<BlockItem> BLOCK_SCHRABIDIUM_FUEL = ITEMS.registerSimpleBlockItem("block_schrabidium_fuel", HbmBlocks.BLOCK_SCHRABIDIUM_FUEL);
    public static final DeferredItem<BlockItem> BLOCK_AU198 = ITEMS.registerSimpleBlockItem("block_au198", HbmBlocks.BLOCK_AU198);
    public static final DeferredItem<BlockItem> BLOCK_MAGNETIZED_TUNGSTEN = ITEMS.registerSimpleBlockItem("block_magnetized_tungsten", HbmBlocks.BLOCK_MAGNETIZED_TUNGSTEN);
    public static final DeferredItem<BlockItem> FROZEN_PLANKS = ITEMS.registerSimpleBlockItem("frozen_planks", HbmBlocks.FROZEN_PLANKS);
    public static final DeferredItem<BlockItem> FROZEN_DIRT = ITEMS.registerSimpleBlockItem("frozen_dirt", HbmBlocks.FROZEN_DIRT);
    public static final DeferredItem<BlockItem> BLOCK_RA226 = ITEMS.registerSimpleBlockItem("block_ra226", HbmBlocks.BLOCK_RA226);
    public static final DeferredItem<BlockItem> BLOCK_RADSPICE = ITEMS.registerSimpleBlockItem("block_radspice", HbmBlocks.BLOCK_RADSPICE);
    public static final DeferredItem<BlockItem> BALEONITITE_SLAKED = ITEMS.registerSimpleBlockItem("baleonitite_slaked", HbmBlocks.BALEONITITE_SLAKED);
    public static final DeferredItem<BlockItem> BALEONITITE_0 = ITEMS.registerSimpleBlockItem("baleonitite_0", HbmBlocks.BALEONITITE_0);
    public static final DeferredItem<BlockItem> BALEONITITE_1 = ITEMS.registerSimpleBlockItem("baleonitite_1", HbmBlocks.BALEONITITE_1);
    public static final DeferredItem<BlockItem> BALEONITITE_2 = ITEMS.registerSimpleBlockItem("baleonitite_2", HbmBlocks.BALEONITITE_2);
    public static final DeferredItem<BlockItem> BALEONITITE_3 = ITEMS.registerSimpleBlockItem("baleonitite_3", HbmBlocks.BALEONITITE_3);
    public static final DeferredItem<BlockItem> BALEONITITE_4 = ITEMS.registerSimpleBlockItem("baleonitite_4", HbmBlocks.BALEONITITE_4);
    public static final DeferredItem<BlockItem> BALEONITITE_CORE = ITEMS.registerSimpleBlockItem("baleonitite_core", HbmBlocks.BALEONITITE_CORE);
    public static final DeferredItem<BlockItem> BLOCK_WHITE_PHOSPHORUS = ITEMS.registerSimpleBlockItem("block_white_phosphorus", HbmBlocks.BLOCK_WHITE_PHOSPHORUS);
    public static final DeferredItem<BlockItem> BLOCK_CORIUM = ITEMS.registerSimpleBlockItem("block_corium", HbmBlocks.BLOCK_CORIUM);

    // --- Bulk-ported simple items (ItemBase/ItemCustomLore equivalents) ---
    public static final DeferredItem<Item> CANISTER_NAPALM = registerSimpleControlItem("canister_napalm");
    public static final DeferredItem<Item> IGNITER = registerSimpleNukeItem("igniter");
    public static final DeferredItem<Item> GUN_SUPER_SHOTGUN = registerSimpleWeaponItem("gun_super_shotgun");
    public static final DeferredItem<Item> PELLET_RTG_DEPLETED_BISMUTH = registerSimpleControlItem("pellet_rtg_depleted_bismuth");
    public static final DeferredItem<Item> PELLET_RTG_DEPLETED_LEAD = registerSimpleControlItem("pellet_rtg_depleted_lead");
    public static final DeferredItem<Item> PELLET_RTG_DEPLETED_MERCURY = registerSimpleControlItem("pellet_rtg_depleted_mercury");
    public static final DeferredItem<Item> PELLET_RTG_DEPLETED_NEPTUNIUM = registerSimpleControlItem("pellet_rtg_depleted_neptunium");
    public static final DeferredItem<Item> PELLET_RTG_DEPLETED_ZIRCONIUM = registerSimpleControlItem("pellet_rtg_depleted_zirconium");
    public static final DeferredItem<Item> CHLORINE_PINWHEEL = registerSimplePartItem("chlorine_pinwheel");
    public static final DeferredItem<Item> RING_STARMETAL = registerSimplePartItem("ring_starmetal");
    public static final DeferredItem<Item> THERMO_UNIT_ENDO = registerSimplePartItem("thermo_unit_endo");
    public static final DeferredItem<Item> THERMO_UNIT_EXO = registerSimplePartItem("thermo_unit_exo");
    public static final DeferredItem<Item> LEVITATION_UNIT = registerSimplePartItem("levitation_unit");
    public static final DeferredItem<Item> MAGNETRON = registerSimplePartItem("magnetron");
    public static final DeferredItem<Item> PELLET_BUCKSHOT = registerSimplePartItem("pellet_buckshot");
    public static final DeferredItem<Item> PELLET_FLECHETTE = registerSimplePartItem("pellet_flechette");
    public static final DeferredItem<Item> PELLET_CHLOROPHYTE = registerSimplePartItem("pellet_chlorophyte");
    public static final DeferredItem<Item> PELLET_MERCURY = registerSimplePartItem("pellet_mercury");
    public static final DeferredItem<Item> PELLET_METEORITE = registerSimplePartItem("pellet_meteorite");
    public static final DeferredItem<Item> PELLET_CANISTER = registerSimplePartItem("pellet_canister");
    public static final DeferredItem<Item> PELLET_CLAWS = registerSimplePartItem("pellet_claws");
    public static final DeferredItem<Item> PELLET_CHARGED = registerSimplePartItem("pellet_charged");
    public static final DeferredItem<Item> PELLET_CLUSTER = registerSimplePartItem("pellet_cluster");
    public static final DeferredItem<Item> PELLET_GAS = registerSimplePartItem("pellet_gas");
    public static final DeferredItem<Item> TOOTHPICKS = registerSimplePartItem("toothpicks");
    public static final DeferredItem<Item> DUCTTAPE = registerSimplePartItem("ducttape");
    public static final DeferredItem<Item> MOTOR = registerSimplePartItem("motor");
    public static final DeferredItem<Item> PHOTO_PANEL = registerSimplePartItem("photo_panel");
    public static final DeferredItem<Item> SAT_BASE = registerSimplePartItem("sat_base");
    public static final DeferredItem<Item> THRUSTER_NUCLEAR = registerSimplePartItem("thruster_nuclear");
    public static final DeferredItem<Item> PIPES_STEEL = registerSimplePartItem("pipes_steel");
    public static final DeferredItem<Item> RAG_PISS = registerSimplePartItem("rag_piss");
    public static final DeferredItem<Item> RING_PULL = registerSimpleConsumableItem("ring_pull");
    public static final DeferredItem<Item> COIN_MASKMAN = registerSimpleConsumableItem("coin_maskman");
    public static final DeferredItem<Item> COIN_RADIATION = registerSimpleConsumableItem("coin_radiation");
    public static final DeferredItem<Item> COIN_WORM = registerSimpleConsumableItem("coin_worm");
    public static final DeferredItem<Item> COIN_UFO = registerSimpleConsumableItem("coin_ufo");
    public static final DeferredItem<Item> PLATE_PAA = registerSimplePartItem("plate_paa");
    public static final DeferredItem<Item> PLATE_SCHRABIDIUM = registerSimplePartItem("plate_schrabidium");
    public static final DeferredItem<Item> PLATE_KEVLAR = registerSimplePartItem("plate_kevlar");
    public static final DeferredItem<Item> PLATE_POLYMER = registerSimplePartItem("plate_polymer");
    public static final DeferredItem<Item> PLATE_DESH = registerSimplePartItem("plate_desh");
    public static final DeferredItem<Item> PLATE_EUPHEMIUM = registerSimplePartItem("plate_euphemium");
    public static final DeferredItem<Item> PLATE_DINEUTRONIUM = registerSimplePartItem("plate_dineutronium");
    public static final DeferredItem<Item> CRYSTAL_SCHRARANIUM = registerSimplePartItem("crystal_schraranium");
    public static final DeferredItem<Item> CRYSTAL_SCHRABIDIUM = registerSimplePartItem("crystal_schrabidium");
    public static final DeferredItem<Item> CRYSTAL_TRIXITE = registerSimplePartItem("crystal_trixite");
    public static final DeferredItem<Item> CRYSTAL_OSMIRIDIUM = registerSimplePartItem("crystal_osmiridium");
    public static final DeferredItem<Item> GEM_VOLCANIC = registerSimplePartItem("gem_volcanic");
    public static final DeferredItem<Item> GUN_REVOLVER_IRON_AMMO = registerSimpleWeaponItem("gun_revolver_iron_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_AMMO = registerSimpleWeaponItem("gun_revolver_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_LEAD_AMMO = registerSimpleWeaponItem("gun_revolver_lead_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_GOLD_AMMO = registerSimpleWeaponItem("gun_revolver_gold_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_SCHRABIDIUM_AMMO = registerSimpleWeaponItem("gun_revolver_schrabidium_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_NIGHTMARE_AMMO = registerSimpleWeaponItem("gun_revolver_nightmare_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_NIGHTMARE2_AMMO = registerSimpleWeaponItem("gun_revolver_nightmare2_ammo");
    public static final DeferredItem<Item> GUN_REVOLVER_CURSED_AMMO = registerSimpleWeaponItem("gun_revolver_cursed_ammo");
    public static final DeferredItem<Item> GUN_MP_AMMO = registerSimpleWeaponItem("gun_mp_ammo");
    public static final DeferredItem<Item> GUN_SPARK_AMMO = registerSimpleWeaponItem("gun_spark_ammo");
    public static final DeferredItem<Item> GUN_EUTHANASIA_AMMO = registerSimpleWeaponItem("gun_euthanasia_ammo");
    public static final DeferredItem<Item> GUN_BF_AMMO = registerSimpleWeaponItem("gun_bf_ammo");
    public static final DeferredItem<Item> GUN_STINGER_AMMO = registerSimpleWeaponItem("gun_stinger_ammo");
    public static final DeferredItem<Item> GUN_XVL1456_AMMO = registerSimpleWeaponItem("gun_xvl1456_ammo");
    public static final DeferredItem<Item> GUN_HP_AMMO = registerSimpleWeaponItem("gun_hp_ammo");
    public static final DeferredItem<Item> GUN_DASH_AMMO = registerSimpleControlItem("gun_dash_ammo");
    public static final DeferredItem<Item> GUN_DEFABRICATOR_AMMO = registerSimpleWeaponItem("gun_defabricator_ammo");
    public static final DeferredItem<Item> GUN_CRYOLATOR_AMMO = registerSimpleWeaponItem("gun_cryolator_ammo");
    public static final DeferredItem<Item> GUN_JACK_AMMO = registerSimpleWeaponItem("gun_jack_ammo");
    public static final DeferredItem<Item> GUN_IMMOLATOR_AMMO = registerSimpleWeaponItem("gun_immolator_ammo");
    public static final DeferredItem<Item> GUN_OSIPR_AMMO = registerSimpleWeaponItem("gun_osipr_ammo");
    public static final DeferredItem<Item> GUN_OSIPR_AMMO2 = registerSimpleWeaponItem("gun_osipr_ammo2");
    public static final DeferredItem<Item> GUN_EMP_AMMO = registerSimpleWeaponItem("gun_emp_ammo");
    public static final DeferredItem<Item> FOLLY_SHELL = registerSimplePartItem("folly_shell");
    public static final DeferredItem<Item> FOLLY_BULLET = registerSimplePartItem("folly_bullet");
    public static final DeferredItem<Item> FOLLY_BULLET_NUCLEAR = registerSimplePartItem("folly_bullet_nuclear");
    public static final DeferredItem<Item> FOLLY_BULLET_DU = registerSimplePartItem("folly_bullet_du");
    public static final DeferredItem<Item> ENERGY_BALL = registerSimpleControlItem("energy_ball");
    public static final DeferredItem<Item> CHARGE_RAILGUN = registerSimpleWeaponItem("charge_railgun");
    public static final DeferredItem<Item> RUNE_BLANK = registerSimplePartItem("rune_blank");
    public static final DeferredItem<Item> RUNE_ISA = registerSimplePartItem("rune_isa");
    public static final DeferredItem<Item> RUNE_DAGAZ = registerSimplePartItem("rune_dagaz");
    public static final DeferredItem<Item> RUNE_HAGALAZ = registerSimplePartItem("rune_hagalaz");
    public static final DeferredItem<Item> RUNE_JERA = registerSimplePartItem("rune_jera");
    public static final DeferredItem<Item> RUNE_THURISAZ = registerSimplePartItem("rune_thurisaz");
    public static final DeferredItem<Item> DEBRIS_GRAPHITE = registerSimpleControlItem("debris_graphite");
    public static final DeferredItem<Item> DEBRIS_METAL = registerSimpleControlItem("debris_metal");
    public static final DeferredItem<Item> DEBRIS_FUEL = registerSimpleControlItem("debris_fuel");
    public static final DeferredItem<Item> RBMK_FUEL_EMPTY = registerSimpleControlItem("rbmk_fuel_empty");
    public static final DeferredItem<Item> PARTICLE_EMPTY = registerSimpleControlItem("particle_empty");
    public static final DeferredItem<Item> PARTICLE_HYDROGEN = registerSimpleControlItem("particle_hydrogen");
    public static final DeferredItem<Item> PARTICLE_COPPER = registerSimpleControlItem("particle_copper");
    public static final DeferredItem<Item> PARTICLE_LEAD = registerSimpleControlItem("particle_lead");
    public static final DeferredItem<Item> PARTICLE_APROTON = registerSimpleControlItem("particle_aproton");
    public static final DeferredItem<Item> PARTICLE_AELECTRON = registerSimpleControlItem("particle_aelectron");
    public static final DeferredItem<Item> PARTICLE_MUON = registerSimpleControlItem("particle_muon");
    public static final DeferredItem<Item> PARTICLE_AMAT = registerSimpleControlItem("particle_amat");
    public static final DeferredItem<Item> PARTICLE_ASCHRAB = registerSimpleControlItem("particle_aschrab");
    public static final DeferredItem<Item> PARTICLE_HIGGS = registerSimpleControlItem("particle_higgs");
    public static final DeferredItem<Item> PARTICLE_TACHYON = registerSimpleControlItem("particle_tachyon");
    public static final DeferredItem<Item> PARTICLE_DARK = registerSimpleControlItem("particle_dark");
    public static final DeferredItem<Item> PARTICLE_STRANGE = registerSimpleControlItem("particle_strange");
    public static final DeferredItem<Item> PARTICLE_SPARKTICLE = registerSimpleControlItem("particle_sparkticle");
    public static final DeferredItem<Item> KEY_RED = registerSimpleConsumableItem("key_red");
    public static final DeferredItem<Item> PIN = registerSimpleConsumableItem("pin");
    public static final DeferredItem<Item> MECH_KEY = registerSimpleConsumableItem("mech_key");
    public static final DeferredItem<Item> AMS_MUZZLE = registerSimpleControlItem("ams_muzzle");
    public static final DeferredItem<Item> GADGET_EXPLOSIVE = registerSimpleNukeItem("gadget_explosive");
    public static final DeferredItem<Item> MAN_EXPLOSIVE = registerSimpleNukeItem("man_explosive");
    public static final DeferredItem<Item> EGG_BALEFIRE_SHARD = registerSimpleNukeItem("egg_balefire_shard");
    public static final DeferredItem<Item> EGG_BALEFIRE = registerSimpleNukeItem("egg_balefire");
    public static final DeferredItem<Item> DEMON_CORE_CLOSED = registerSimpleNukeItem("demon_core_closed");
    public static final DeferredItem<Item> CUSTOM_TNT = registerSimpleNukeItem("custom_tnt");
    public static final DeferredItem<Item> CUSTOM_NUKE = registerSimpleNukeItem("custom_nuke");
    public static final DeferredItem<Item> CUSTOM_HYDRO = registerSimpleNukeItem("custom_hydro");
    public static final DeferredItem<Item> CUSTOM_AMAT = registerSimpleNukeItem("custom_amat");
    public static final DeferredItem<Item> CUSTOM_DIRTY = registerSimpleNukeItem("custom_dirty");
    public static final DeferredItem<Item> CUSTOM_SCHRAB = registerSimpleNukeItem("custom_schrab");
    public static final DeferredItem<Item> CUSTOM_SOL = registerSimpleNukeItem("custom_sol");
    public static final DeferredItem<Item> CUSTOM_EUPH = registerSimpleNukeItem("custom_euph");
    public static final DeferredItem<Item> CUSTOM_FALL = registerSimpleNukeItem("custom_fall");
    public static final DeferredItem<Item> MISSILE_ASSEMBLY = registerSimplePartItem("missile_assembly");
    public static final DeferredItem<Item> MISSILE_SOYUZ_LANDER = registerSimpleControlItem("missile_soyuz_lander");
    public static final DeferredItem<Item> WARHEAD_GENERIC_SMALL = registerSimplePartItem("warhead_generic_small");
    public static final DeferredItem<Item> WARHEAD_INCENDIARY_SMALL = registerSimplePartItem("warhead_incendiary_small");
    public static final DeferredItem<Item> WARHEAD_CLUSTER_SMALL = registerSimplePartItem("warhead_cluster_small");
    public static final DeferredItem<Item> WARHEAD_BUSTER_SMALL = registerSimplePartItem("warhead_buster_small");
    public static final DeferredItem<Item> WARHEAD_GENERIC_MEDIUM = registerSimplePartItem("warhead_generic_medium");
    public static final DeferredItem<Item> WARHEAD_INCENDIARY_MEDIUM = registerSimplePartItem("warhead_incendiary_medium");
    public static final DeferredItem<Item> WARHEAD_CLUSTER_MEDIUM = registerSimplePartItem("warhead_cluster_medium");
    public static final DeferredItem<Item> WARHEAD_BUSTER_MEDIUM = registerSimplePartItem("warhead_buster_medium");
    public static final DeferredItem<Item> WARHEAD_GENERIC_LARGE = registerSimplePartItem("warhead_generic_large");
    public static final DeferredItem<Item> WARHEAD_INCENDIARY_LARGE = registerSimplePartItem("warhead_incendiary_large");
    public static final DeferredItem<Item> WARHEAD_CLUSTER_LARGE = registerSimplePartItem("warhead_cluster_large");
    public static final DeferredItem<Item> WARHEAD_BUSTER_LARGE = registerSimplePartItem("warhead_buster_large");
    public static final DeferredItem<Item> WARHEAD_N2 = registerSimplePartItem("warhead_n2");
    public static final DeferredItem<Item> WARHEAD_NUCLEAR = registerSimplePartItem("warhead_nuclear");
    public static final DeferredItem<Item> WARHEAD_MIRVLET = registerSimplePartItem("warhead_mirvlet");
    public static final DeferredItem<Item> WARHEAD_MIRV = registerSimplePartItem("warhead_mirv");
    public static final DeferredItem<Item> WARHEAD_VOLCANO = registerSimplePartItem("warhead_volcano");
    public static final DeferredItem<Item> WARHEAD_THERMO_ENDO = registerSimplePartItem("warhead_thermo_endo");
    public static final DeferredItem<Item> WARHEAD_THERMO_EXO = registerSimplePartItem("warhead_thermo_exo");
    public static final DeferredItem<Item> THRUSTER_SMALL = registerSimplePartItem("thruster_small");
    public static final DeferredItem<Item> THRUSTER_MEDIUM = registerSimplePartItem("thruster_medium");
    public static final DeferredItem<Item> THRUSTER_LARGE = registerSimplePartItem("thruster_large");
    public static final DeferredItem<Item> HULL_SMALL_STEEL = registerSimplePartItem("hull_small_steel");
    public static final DeferredItem<Item> HULL_SMALL_ALUMINIUM = registerSimplePartItem("hull_small_aluminium");
    public static final DeferredItem<Item> HULL_BIG_STEEL = registerSimplePartItem("hull_big_steel");
    public static final DeferredItem<Item> MISSILE_SKIN_CAMO = registerSimpleControlItem("missile_skin_camo");
    public static final DeferredItem<Item> MISSILE_SKIN_DESERT = registerSimpleControlItem("missile_skin_desert");
    public static final DeferredItem<Item> MISSILE_SKIN_FLAMES = registerSimpleControlItem("missile_skin_flames");
    public static final DeferredItem<Item> MISSILE_SKIN_MANLY_PINK = registerSimpleControlItem("missile_skin_manly_pink");
    public static final DeferredItem<Item> MISSILE_SKIN_ORANGE_INSULATION = registerSimpleControlItem("missile_skin_orange_insulation");
    public static final DeferredItem<Item> MISSILE_SKIN_SLEEK = registerSimpleControlItem("missile_skin_sleek");
    public static final DeferredItem<Item> MISSILE_SKIN_SOVIET_GLORY = registerSimpleControlItem("missile_skin_soviet_glory");
    public static final DeferredItem<Item> MISSILE_SKIN_SOVIET_STANK = registerSimpleControlItem("missile_skin_soviet_stank");
    public static final DeferredItem<Item> MISSILE_SKIN_METAL = registerSimpleControlItem("missile_skin_metal");
    public static final DeferredItem<Item> FLAME_PONY = registerSimplePartItem("flame_pony");
    public static final DeferredItem<Item> FLAME_CONSPIRACY = registerSimplePartItem("flame_conspiracy");
    public static final DeferredItem<Item> FLAME_POLITICS = registerSimplePartItem("flame_politics");
    public static final DeferredItem<Item> FLAME_OPINION = registerSimplePartItem("flame_opinion");
    public static final DeferredItem<Item> BURNT_BARK = registerSimpleConsumableItem("burnt_bark");
    public static final DeferredItem<Item> BOOK_SECRET = registerSimpleConsumableItem("book_secret");
    public static final DeferredItem<Item> CRYSTAL_HORN = registerSimplePartItem("crystal_horn");
    public static final DeferredItem<Item> CRYSTAL_CHARRED = registerSimplePartItem("crystal_charred");
    public static final DeferredItem<Item> WATCH = registerSimpleConsumableItem("watch");
    public static final DeferredItem<Item> CHOPPER_HEAD = registerSimplePartItem("chopper_head");
    public static final DeferredItem<Item> CHOPPER_GUN = registerSimplePartItem("chopper_gun");
    public static final DeferredItem<Item> CHOPPER_TORSO = registerSimplePartItem("chopper_torso");
    public static final DeferredItem<Item> CHOPPER_TAIL = registerSimplePartItem("chopper_tail");
    public static final DeferredItem<Item> CHOPPER_WING = registerSimplePartItem("chopper_wing");
    public static final DeferredItem<Item> CHOPPER_BLADES = registerSimplePartItem("chopper_blades");
    public static final DeferredItem<Item> SHIMMER_HEAD = registerSimplePartItem("shimmer_head");
    public static final DeferredItem<Item> SHIMMER_AXE_HEAD = registerSimplePartItem("shimmer_axe_head");
    public static final DeferredItem<Item> SHIMMER_HANDLE = registerSimplePartItem("shimmer_handle");
    public static final DeferredItem<Item> TELEPAD = registerSimplePartItem("telepad");
    public static final DeferredItem<Item> ENTANGLEMENT_KIT = registerSimplePartItem("entanglement_kit");
    public static final DeferredItem<Item> BOB_METALWORKS = registerSimpleControlItem("bob_metalworks");
    public static final DeferredItem<Item> BOB_ASSEMBLY = registerSimpleControlItem("bob_assembly");
    public static final DeferredItem<Item> BOB_CHEMISTRY = registerSimpleControlItem("bob_chemistry");
    public static final DeferredItem<Item> BOB_OIL = registerSimpleControlItem("bob_oil");
    public static final DeferredItem<Item> BOB_NUCLEAR = registerSimpleControlItem("bob_nuclear");
    public static final DeferredItem<Item> DIGAMMA_SEE = registerSimpleControlItem("digamma_see");
    public static final DeferredItem<Item> DIGAMMA_FEEL = registerSimpleControlItem("digamma_feel");
    public static final DeferredItem<Item> DIGAMMA_KNOW = registerSimpleControlItem("digamma_know");
    public static final DeferredItem<Item> DIGAMMA_KAUAI_MOHO = registerSimpleControlItem("digamma_kauai_moho");
    public static final DeferredItem<Item> DIGAMMA_UP_ON_TOP = registerSimpleControlItem("digamma_up_on_top");
    public static final DeferredItem<Item> SMOKE1 = registerSimpleControlItem("smoke1");
    public static final DeferredItem<Item> SMOKE2 = registerSimpleControlItem("smoke2");
    public static final DeferredItem<Item> SMOKE3 = registerSimpleControlItem("smoke3");
    public static final DeferredItem<Item> SMOKE4 = registerSimpleControlItem("smoke4");
    public static final DeferredItem<Item> SMOKE5 = registerSimpleControlItem("smoke5");
    public static final DeferredItem<Item> SMOKE6 = registerSimpleControlItem("smoke6");
    public static final DeferredItem<Item> SMOKE7 = registerSimpleControlItem("smoke7");
    public static final DeferredItem<Item> SMOKE8 = registerSimpleControlItem("smoke8");
    public static final DeferredItem<Item> B_SMOKE1 = registerSimpleControlItem("b_smoke1");
    public static final DeferredItem<Item> B_SMOKE2 = registerSimpleControlItem("b_smoke2");
    public static final DeferredItem<Item> B_SMOKE3 = registerSimpleControlItem("b_smoke3");
    public static final DeferredItem<Item> B_SMOKE4 = registerSimpleControlItem("b_smoke4");
    public static final DeferredItem<Item> B_SMOKE5 = registerSimpleControlItem("b_smoke5");
    public static final DeferredItem<Item> B_SMOKE6 = registerSimpleControlItem("b_smoke6");
    public static final DeferredItem<Item> B_SMOKE7 = registerSimpleControlItem("b_smoke7");
    public static final DeferredItem<Item> B_SMOKE8 = registerSimpleControlItem("b_smoke8");
    public static final DeferredItem<Item> D_SMOKE1 = registerSimpleControlItem("d_smoke1");
    public static final DeferredItem<Item> D_SMOKE2 = registerSimpleControlItem("d_smoke2");
    public static final DeferredItem<Item> D_SMOKE3 = registerSimpleControlItem("d_smoke3");
    public static final DeferredItem<Item> D_SMOKE4 = registerSimpleControlItem("d_smoke4");
    public static final DeferredItem<Item> D_SMOKE5 = registerSimpleControlItem("d_smoke5");
    public static final DeferredItem<Item> D_SMOKE6 = registerSimpleControlItem("d_smoke6");
    public static final DeferredItem<Item> D_SMOKE7 = registerSimpleControlItem("d_smoke7");
    public static final DeferredItem<Item> D_SMOKE8 = registerSimpleControlItem("d_smoke8");
    public static final DeferredItem<Item> CLOUD1 = registerSimpleControlItem("cloud1");
    public static final DeferredItem<Item> CLOUD2 = registerSimpleControlItem("cloud2");
    public static final DeferredItem<Item> CLOUD3 = registerSimpleControlItem("cloud3");
    public static final DeferredItem<Item> CLOUD4 = registerSimpleControlItem("cloud4");
    public static final DeferredItem<Item> CLOUD5 = registerSimpleControlItem("cloud5");
    public static final DeferredItem<Item> CLOUD6 = registerSimpleControlItem("cloud6");
    public static final DeferredItem<Item> CLOUD7 = registerSimpleControlItem("cloud7");
    public static final DeferredItem<Item> CLOUD8 = registerSimpleControlItem("cloud8");
    public static final DeferredItem<Item> GASFLAME1 = registerSimpleControlItem("gasflame1");
    public static final DeferredItem<Item> GASFLAME2 = registerSimpleControlItem("gasflame2");
    public static final DeferredItem<Item> GASFLAME3 = registerSimpleControlItem("gasflame3");
    public static final DeferredItem<Item> GASFLAME4 = registerSimpleControlItem("gasflame4");
    public static final DeferredItem<Item> GASFLAME5 = registerSimpleControlItem("gasflame5");
    public static final DeferredItem<Item> GASFLAME6 = registerSimpleControlItem("gasflame6");
    public static final DeferredItem<Item> GASFLAME7 = registerSimpleControlItem("gasflame7");
    public static final DeferredItem<Item> GASFLAME8 = registerSimpleControlItem("gasflame8");
    public static final DeferredItem<Item> FLAME_1 = registerSimpleControlItem("flame_1");
    public static final DeferredItem<Item> FLAME_2 = registerSimpleControlItem("flame_2");
    public static final DeferredItem<Item> FLAME_3 = registerSimpleControlItem("flame_3");
    public static final DeferredItem<Item> FLAME_4 = registerSimpleControlItem("flame_4");
    public static final DeferredItem<Item> FLAME_5 = registerSimpleControlItem("flame_5");
    public static final DeferredItem<Item> FLAME_6 = registerSimpleControlItem("flame_6");
    public static final DeferredItem<Item> FLAME_7 = registerSimpleControlItem("flame_7");
    public static final DeferredItem<Item> FLAME_8 = registerSimpleControlItem("flame_8");
    public static final DeferredItem<Item> FLAME_9 = registerSimpleControlItem("flame_9");
    public static final DeferredItem<Item> FLAME_10 = registerSimpleControlItem("flame_10");
    public static final DeferredItem<Item> ORANGE1 = registerSimpleControlItem("orange1");
    public static final DeferredItem<Item> ORANGE2 = registerSimpleControlItem("orange2");
    public static final DeferredItem<Item> ORANGE3 = registerSimpleControlItem("orange3");
    public static final DeferredItem<Item> ORANGE4 = registerSimpleControlItem("orange4");
    public static final DeferredItem<Item> ORANGE5 = registerSimpleControlItem("orange5");
    public static final DeferredItem<Item> ORANGE6 = registerSimpleControlItem("orange6");
    public static final DeferredItem<Item> ORANGE7 = registerSimpleControlItem("orange7");
    public static final DeferredItem<Item> ORANGE8 = registerSimpleControlItem("orange8");
    public static final DeferredItem<Item> PC1 = registerSimpleControlItem("pc1");
    public static final DeferredItem<Item> PC2 = registerSimpleControlItem("pc2");
    public static final DeferredItem<Item> PC3 = registerSimpleControlItem("pc3");
    public static final DeferredItem<Item> PC4 = registerSimpleControlItem("pc4");
    public static final DeferredItem<Item> PC5 = registerSimpleControlItem("pc5");
    public static final DeferredItem<Item> PC6 = registerSimpleControlItem("pc6");
    public static final DeferredItem<Item> PC7 = registerSimpleControlItem("pc7");
    public static final DeferredItem<Item> PC8 = registerSimpleControlItem("pc8");
    public static final DeferredItem<Item> CHLORINE1 = registerSimpleControlItem("chlorine1");
    public static final DeferredItem<Item> CHLORINE2 = registerSimpleControlItem("chlorine2");
    public static final DeferredItem<Item> CHLORINE3 = registerSimpleControlItem("chlorine3");
    public static final DeferredItem<Item> CHLORINE4 = registerSimpleControlItem("chlorine4");
    public static final DeferredItem<Item> CHLORINE5 = registerSimpleControlItem("chlorine5");
    public static final DeferredItem<Item> CHLORINE6 = registerSimpleControlItem("chlorine6");
    public static final DeferredItem<Item> CHLORINE7 = registerSimpleControlItem("chlorine7");
    public static final DeferredItem<Item> CHLORINE8 = registerSimpleControlItem("chlorine8");
    public static final DeferredItem<Item> LN2_1 = registerSimpleControlItem("ln2_1");
    public static final DeferredItem<Item> LN2_2 = registerSimpleControlItem("ln2_2");
    public static final DeferredItem<Item> LN2_3 = registerSimpleControlItem("ln2_3");
    public static final DeferredItem<Item> LN2_4 = registerSimpleControlItem("ln2_4");
    public static final DeferredItem<Item> LN2_5 = registerSimpleControlItem("ln2_5");
    public static final DeferredItem<Item> LN2_6 = registerSimpleControlItem("ln2_6");
    public static final DeferredItem<Item> LN2_7 = registerSimpleControlItem("ln2_7");
    public static final DeferredItem<Item> LN2_8 = registerSimpleControlItem("ln2_8");
    public static final DeferredItem<Item> LN2_9 = registerSimpleControlItem("ln2_9");
    public static final DeferredItem<Item> LN2_10 = registerSimpleControlItem("ln2_10");
    public static final DeferredItem<Item> GAS1 = registerSimpleControlItem("gas1");
    public static final DeferredItem<Item> GAS2 = registerSimpleControlItem("gas2");
    public static final DeferredItem<Item> GAS3 = registerSimpleControlItem("gas3");
    public static final DeferredItem<Item> GAS4 = registerSimpleControlItem("gas4");
    public static final DeferredItem<Item> GAS5 = registerSimpleControlItem("gas5");
    public static final DeferredItem<Item> GAS6 = registerSimpleControlItem("gas6");
    public static final DeferredItem<Item> GAS7 = registerSimpleControlItem("gas7");
    public static final DeferredItem<Item> GAS8 = registerSimpleControlItem("gas8");
    public static final DeferredItem<Item> SPILL1 = registerSimpleControlItem("spill1");
    public static final DeferredItem<Item> SPILL2 = registerSimpleControlItem("spill2");
    public static final DeferredItem<Item> SPILL3 = registerSimpleControlItem("spill3");
    public static final DeferredItem<Item> SPILL4 = registerSimpleControlItem("spill4");
    public static final DeferredItem<Item> SPILL5 = registerSimpleControlItem("spill5");
    public static final DeferredItem<Item> SPILL6 = registerSimpleControlItem("spill6");
    public static final DeferredItem<Item> SPILL7 = registerSimpleControlItem("spill7");
    public static final DeferredItem<Item> SPILL8 = registerSimpleControlItem("spill8");
    public static final DeferredItem<Item> NOTHING = registerSimpleControlItem("nothing");
    public static final DeferredItem<Item> DUCC = registerSimpleControlItem("ducc");
    public static final DeferredItem<Item> DISCHARGE = registerSimpleControlItem("discharge");
    public static final DeferredItem<Item> UNDEFINED = registerSimplePartItem("undefined");

    // --- Bulk-ported food items (ItemFoodBase equivalents) ---
    public static final DeferredItem<Item> INGOT_SMORE = registerFoodPartItem("ingot_smore", 10, 20.0F);
    public static final DeferredItem<Item> BOMB_WAFFLE = registerFoodConsumableItem("bomb_waffle", 20, 0.0F);
    public static final DeferredItem<Item> SCHNITZEL_VEGAN = registerFoodConsumableItem("schnitzel_vegan", 3, 6.0F);
    public static final DeferredItem<Item> COTTON_CANDY = registerFoodConsumableItem("cotton_candy", 5, 0.0F);
    public static final DeferredItem<Item> APPLE_LEAD = registerFoodConsumableItem("apple_lead", 3, 0.0F);
    public static final DeferredItem<Item> APPLE_LEAD1 = registerFoodConsumableItem("apple_lead1", 5, 0.0F);
    public static final DeferredItem<Item> APPLE_LEAD2 = registerFoodConsumableItem("apple_lead2", 10, 0.0F);
    public static final DeferredItem<Item> APPLE_SCHRABIDIUM = registerFoodConsumableItem("apple_schrabidium", 5, 25.0F);
    public static final DeferredItem<Item> APPLE_SCHRABIDIUM1 = registerFoodConsumableItem("apple_schrabidium1", 10, 50.0F);
    public static final DeferredItem<Item> APPLE_SCHRABIDIUM2 = registerFoodConsumableItem("apple_schrabidium2", 20, 100.0F);

    // --- Bulk-ported IV/syringe consumables (ItemSimpleConsumable stragglers) ---
    public static final DeferredItem<Item> IV_EMPTY = registerInstantConsumableItem(
        "iv_empty",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> {
            livingEntity.hurt(level.damageSources().magic(), 5.0F);
            if (livingEntity instanceof Player player) {
                player.getInventory().add(new ItemStack(HbmItems.IV_BLOOD.get()));
            }
        },
        SoundEvents.PLAYER_HURT
    );
    public static final DeferredItem<Item> IV_BLOOD = registerInstantConsumableItem(
        "iv_blood",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> {
            livingEntity.heal(3.0F);
            if (livingEntity instanceof Player player) {
                player.getInventory().add(new ItemStack(HbmItems.IV_EMPTY.get()));
            }
        },
        SoundEvents.HONEY_DRINK
    );
    public static final DeferredItem<Item> IV_XP_EMPTY = registerInstantConsumableItem(
        "iv_xp_empty",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> {
            if (livingEntity instanceof Player player) {
                player.giveExperiencePoints(-100);
                player.getInventory().add(new ItemStack(HbmItems.IV_XP.get()));
            }
        },
        SoundEvents.HONEY_DRINK
    );
    public static final DeferredItem<Item> IV_XP = registerInstantConsumableItem(
        "iv_xp",
        properties -> properties.stacksTo(16),
        (level, livingEntity) -> {
            if (livingEntity instanceof Player player) {
                player.giveExperiencePoints(100);
                player.getInventory().add(new ItemStack(HbmItems.IV_XP_EMPTY.get()));
            }
        },
        SoundEvents.EXPERIENCE_ORB_PICKUP
    );

    // --- Bulk-ported ItemBattery stragglers (int-safe capacities only; spark_cell_25/100/1000/2500/10000/power and memory
    //     exceed Integer.MAX_VALUE and need a long-backed energy storage before they can be ported) ---
    public static final DeferredItem<Item> BATTERY_SCHRABIDIUM = registerBatteryItem("battery_schrabidium", 1000000, 5000, 5000);
    public static final DeferredItem<Item> BATTERY_SCHRABIDIUM_CELL = registerBatteryItem("battery_schrabidium_cell", 3000000, 15000, 15000);
    public static final DeferredItem<Item> BATTERY_SCHRABIDIUM_CELL_2 = registerBatteryItem("battery_schrabidium_cell_2", 6000000, 30000, 30000);
    public static final DeferredItem<Item> BATTERY_SCHRABIDIUM_CELL_4 = registerBatteryItem("battery_schrabidium_cell_4", 12000000, 60000, 60000);
    public static final DeferredItem<Item> BATTERY_TRIXITE = registerBatteryItem("battery_trixite", 5000000, 40000, 200000);
    public static final DeferredItem<Item> BATTERY_SPARK = registerBatteryItem("battery_spark", 100000000, 2000000, 2000000);
    public static final DeferredItem<Item> BATTERY_SPARK_CELL_6 = registerBatteryItem("battery_spark_cell_6", 600000000, 2000000, 2000000);
    public static final DeferredItem<Item> BATTERY_POTATO = registerBatteryItem("battery_potato", 100, 0, 100);
    public static final DeferredItem<Item> BATTERY_SU = registerBatteryItem("battery_su", 1500, 0, 100);
    public static final DeferredItem<Item> BATTERY_SU_L = registerBatteryItem("battery_su_l", 3500, 0, 100);
    public static final DeferredItem<Item> BATTERY_STEAM = registerBatteryItem("battery_steam", 60000, 3, 6000);
    public static final DeferredItem<Item> BATTERY_STEAM_LARGE = registerBatteryItem("battery_steam_large", 100000, 5, 10000);
    public static final DeferredItem<Item> ENERGY_CORE = registerBatteryItem("energy_core", 10000000, 0, 1000);

    // --- Bulk-ported ammo items (ItemAmmo; no gun/reload system exists yet, so these are inert crafting materials for now) ---
    public static final DeferredItem<Item> AMMO_357_DESH = registerSimpleWeaponItem("ammo_357_desh");
    public static final DeferredItem<Item> AMMO_4GAUGE_CANISTER = registerSimpleWeaponItem("ammo_4gauge_canister");
    public static final DeferredItem<Item> AMMO_4GAUGE_CLAW = registerSimpleWeaponItem("ammo_4gauge_claw");
    public static final DeferredItem<Item> AMMO_4GAUGE_VAMPIRE = registerSimpleWeaponItem("ammo_4gauge_vampire");
    public static final DeferredItem<Item> AMMO_4GAUGE_VOID = registerSimpleWeaponItem("ammo_4gauge_void");
    public static final DeferredItem<Item> AMMO_5MM_CHLOROPHYTE = registerSimpleWeaponItem("ammo_5mm_chlorophyte");
    public static final DeferredItem<Item> AMMO_9MM_CHLOROPHYTE = registerSimpleWeaponItem("ammo_9mm_chlorophyte");
    public static final DeferredItem<Item> AMMO_556_CHLOROPHYTE = registerSimpleWeaponItem("ammo_556_chlorophyte");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE_CHLOROPHYTE = registerSimpleWeaponItem("ammo_556_flechette_chlorophyte");
    public static final DeferredItem<Item> AMMO_50AE_CHLOROPHYTE = registerSimpleWeaponItem("ammo_50ae_chlorophyte");
    public static final DeferredItem<Item> AMMO_50BMG_CHLOROPHYTE = registerSimpleWeaponItem("ammo_50bmg_chlorophyte");
    public static final DeferredItem<Item> AMMO_50BMG_FLECHETTE = registerSimpleWeaponItem("ammo_50bmg_flechette");
    public static final DeferredItem<Item> AMMO_50BMG_FLECHETTE_AM = registerSimpleWeaponItem("ammo_50bmg_flechette_am");
    public static final DeferredItem<Item> AMMO_50BMG_FLECHETTE_PO = registerSimpleWeaponItem("ammo_50bmg_flechette_po");
    public static final DeferredItem<Item> AMMO_44_CHLOROPHYTE = registerSimpleWeaponItem("ammo_44_chlorophyte");
    public static final DeferredItem<Item> AMMO_22LR_CHLOROPHYTE = registerSimpleWeaponItem("ammo_22lr_chlorophyte");
    public static final DeferredItem<Item> AMMO_ROCKET_CANISTER = registerSimpleWeaponItem("ammo_rocket_canister");
    public static final DeferredItem<Item> AMMO_20GAUGE = registerSimpleWeaponItem("ammo_20gauge");
    public static final DeferredItem<Item> AMMO_20GAUGE_SLUG = registerSimpleWeaponItem("ammo_20gauge_slug");
    public static final DeferredItem<Item> AMMO_20GAUGE_FLECHETTE = registerSimpleWeaponItem("ammo_20gauge_flechette");
    public static final DeferredItem<Item> AMMO_20GAUGE_INCENDIARY = registerSimpleWeaponItem("ammo_20gauge_incendiary");
    public static final DeferredItem<Item> AMMO_20GAUGE_EXPLOSIVE = registerSimpleWeaponItem("ammo_20gauge_explosive");
    public static final DeferredItem<Item> AMMO_20GAUGE_CAUSTIC = registerSimpleWeaponItem("ammo_20gauge_caustic");
    public static final DeferredItem<Item> AMMO_20GAUGE_SHOCK = registerSimpleWeaponItem("ammo_20gauge_shock");
    public static final DeferredItem<Item> AMMO_20GAUGE_WITHER = registerSimpleWeaponItem("ammo_20gauge_wither");
    public static final DeferredItem<Item> AMMO_20GAUGE_SLEEK = registerSimpleWeaponItem("ammo_20gauge_sleek");
    public static final DeferredItem<Item> AMMO_20GAUGE_SHRAPNEL = registerSimpleWeaponItem("ammo_20gauge_shrapnel");
    public static final DeferredItem<Item> AMMO_4GAUGE = registerSimpleWeaponItem("ammo_4gauge");
    public static final DeferredItem<Item> AMMO_4GAUGE_SLUG = registerSimpleWeaponItem("ammo_4gauge_slug");
    public static final DeferredItem<Item> AMMO_4GAUGE_FLECHETTE = registerSimpleWeaponItem("ammo_4gauge_flechette");
    public static final DeferredItem<Item> AMMO_4GAUGE_FLECHETTE_PHOSPHORUS = registerSimpleWeaponItem("ammo_4gauge_flechette_phosphorus");
    public static final DeferredItem<Item> AMMO_4GAUGE_EXPLOSIVE = registerSimpleWeaponItem("ammo_4gauge_explosive");
    public static final DeferredItem<Item> AMMO_4GAUGE_SEMTEX = registerSimpleWeaponItem("ammo_4gauge_semtex");
    public static final DeferredItem<Item> AMMO_4GAUGE_BALEFIRE = registerSimpleWeaponItem("ammo_4gauge_balefire");
    public static final DeferredItem<Item> AMMO_4GAUGE_KAMPF = registerSimpleWeaponItem("ammo_4gauge_kampf");
    public static final DeferredItem<Item> AMMO_4GAUGE_SLEEK = registerSimpleWeaponItem("ammo_4gauge_sleek");
    public static final DeferredItem<Item> AMMO_ROCKET_HE = registerSimpleWeaponItem("ammo_rocket_he");
    public static final DeferredItem<Item> AMMO_ROCKET_INCENDIARY = registerSimpleWeaponItem("ammo_rocket_incendiary");
    public static final DeferredItem<Item> AMMO_ROCKET_PHOSPHORUS = registerSimpleWeaponItem("ammo_rocket_phosphorus");
    public static final DeferredItem<Item> AMMO_ROCKET_SHRAPNEL = registerSimpleWeaponItem("ammo_rocket_shrapnel");
    public static final DeferredItem<Item> AMMO_ROCKET_EMP = registerSimpleWeaponItem("ammo_rocket_emp");
    public static final DeferredItem<Item> AMMO_ROCKET_GLARE = registerSimpleWeaponItem("ammo_rocket_glare");
    public static final DeferredItem<Item> AMMO_ROCKET_TOXIC = registerSimpleWeaponItem("ammo_rocket_toxic");
    public static final DeferredItem<Item> AMMO_ROCKET_SLEEK = registerSimpleWeaponItem("ammo_rocket_sleek");
    public static final DeferredItem<Item> AMMO_ROCKET_NUCLEAR = registerSimpleWeaponItem("ammo_rocket_nuclear");
    public static final DeferredItem<Item> AMMO_ROCKET_RPC = registerSimpleWeaponItem("ammo_rocket_rpc");
    public static final DeferredItem<Item> AMMO_GRENADE = registerSimpleWeaponItem("ammo_grenade");
    public static final DeferredItem<Item> AMMO_GRENADE_HE = registerSimpleWeaponItem("ammo_grenade_he");
    public static final DeferredItem<Item> AMMO_GRENADE_INCENDIARY = registerSimpleWeaponItem("ammo_grenade_incendiary");
    public static final DeferredItem<Item> AMMO_GRENADE_PHOSPHORUS = registerSimpleWeaponItem("ammo_grenade_phosphorus");
    public static final DeferredItem<Item> AMMO_GRENADE_TOXIC = registerSimpleWeaponItem("ammo_grenade_toxic");
    public static final DeferredItem<Item> AMMO_GRENADE_CONCUSSION = registerSimpleWeaponItem("ammo_grenade_concussion");
    public static final DeferredItem<Item> AMMO_GRENADE_FINNED = registerSimpleWeaponItem("ammo_grenade_finned");
    public static final DeferredItem<Item> AMMO_GRENADE_SLEEK = registerSimpleWeaponItem("ammo_grenade_sleek");
    public static final DeferredItem<Item> AMMO_GRENADE_NUCLEAR = registerSimpleWeaponItem("ammo_grenade_nuclear");
    public static final DeferredItem<Item> AMMO_GRENADE_TRACER = registerSimpleWeaponItem("ammo_grenade_tracer");
    public static final DeferredItem<Item> AMMO_GRENADE_KAMPF = registerSimpleWeaponItem("ammo_grenade_kampf");
    public static final DeferredItem<Item> AMMO_SHELL = registerSimpleWeaponItem("ammo_shell");
    public static final DeferredItem<Item> AMMO_SHELL_EXPLOSIVE = registerSimpleWeaponItem("ammo_shell_explosive");
    public static final DeferredItem<Item> AMMO_SHELL_APFSDS_T = registerSimpleWeaponItem("ammo_shell_apfsds_t");
    public static final DeferredItem<Item> AMMO_SHELL_APFSDS_DU = registerSimpleWeaponItem("ammo_shell_apfsds_du");
    public static final DeferredItem<Item> AMMO_SHELL_W9 = registerSimpleWeaponItem("ammo_shell_w9");
    public static final DeferredItem<Item> AMMO_DGK = registerSimpleWeaponItem("ammo_dgk");
    public static final DeferredItem<Item> AMMO_NUKE_LOW = registerSimpleWeaponItem("ammo_nuke_low");
    public static final DeferredItem<Item> AMMO_NUKE = registerSimpleWeaponItem("ammo_nuke");
    public static final DeferredItem<Item> AMMO_NUKE_HIGH = registerSimpleWeaponItem("ammo_nuke_high");
    public static final DeferredItem<Item> AMMO_NUKE_TOTS = registerSimpleWeaponItem("ammo_nuke_tots");
    public static final DeferredItem<Item> AMMO_NUKE_SAFE = registerSimpleWeaponItem("ammo_nuke_safe");
    public static final DeferredItem<Item> AMMO_NUKE_PUMPKIN = registerSimpleWeaponItem("ammo_nuke_pumpkin");
    public static final DeferredItem<Item> AMMO_MIRV = registerSimpleWeaponItem("ammo_mirv");
    public static final DeferredItem<Item> AMMO_MIRV_LOW = registerSimpleWeaponItem("ammo_mirv_low");
    public static final DeferredItem<Item> AMMO_MIRV_HIGH = registerSimpleWeaponItem("ammo_mirv_high");
    public static final DeferredItem<Item> AMMO_MIRV_SAFE = registerSimpleWeaponItem("ammo_mirv_safe");
    public static final DeferredItem<Item> AMMO_MIRV_SPECIAL = registerSimpleWeaponItem("ammo_mirv_special");
    public static final DeferredItem<Item> AMMO_FUEL = registerSimpleWeaponItem("ammo_fuel");
    public static final DeferredItem<Item> AMMO_FUEL_NAPALM = registerSimpleWeaponItem("ammo_fuel_napalm");
    public static final DeferredItem<Item> AMMO_FUEL_PHOSPHORUS = registerSimpleWeaponItem("ammo_fuel_phosphorus");
    public static final DeferredItem<Item> AMMO_FUEL_VAPORIZER = registerSimpleWeaponItem("ammo_fuel_vaporizer");
    public static final DeferredItem<Item> AMMO_FUEL_GAS = registerSimpleWeaponItem("ammo_fuel_gas");
    public static final DeferredItem<Item> AMMO_DART = registerSimpleWeaponItem("ammo_dart");
    public static final DeferredItem<Item> AMMO_12GAUGE = registerSimpleWeaponItem("ammo_12gauge");
    public static final DeferredItem<Item> AMMO_12GAUGE_INCENDIARY = registerSimpleWeaponItem("ammo_12gauge_incendiary");
    public static final DeferredItem<Item> AMMO_12GAUGE_SHRAPNEL = registerSimpleWeaponItem("ammo_12gauge_shrapnel");
    public static final DeferredItem<Item> AMMO_12GAUGE_DU = registerSimpleWeaponItem("ammo_12gauge_du");
    public static final DeferredItem<Item> AMMO_12GAUGE_SLEEK = registerSimpleWeaponItem("ammo_12gauge_sleek");
    public static final DeferredItem<Item> AMMO_12GAUGE_MARAUDER = registerSimpleWeaponItem("ammo_12gauge_marauder");
    public static final DeferredItem<Item> AMMO_22LR = registerSimpleWeaponItem("ammo_22lr");
    public static final DeferredItem<Item> AMMO_22LR_AP = registerSimpleWeaponItem("ammo_22lr_ap");
    public static final DeferredItem<Item> AMMO_44 = registerSimpleWeaponItem("ammo_44");
    public static final DeferredItem<Item> AMMO_44_AP = registerSimpleWeaponItem("ammo_44_ap");
    public static final DeferredItem<Item> AMMO_44_DU = registerSimpleWeaponItem("ammo_44_du");
    public static final DeferredItem<Item> AMMO_44_PHOSPHORUS = registerSimpleWeaponItem("ammo_44_phosphorus");
    public static final DeferredItem<Item> AMMO_44_PIP = registerSimpleWeaponItem("ammo_44_pip");
    public static final DeferredItem<Item> AMMO_44_BJ = registerSimpleWeaponItem("ammo_44_bj");
    public static final DeferredItem<Item> AMMO_44_SILVER = registerSimpleWeaponItem("ammo_44_silver");
    public static final DeferredItem<Item> AMMO_44_ROCKET = registerSimpleWeaponItem("ammo_44_rocket");
    public static final DeferredItem<Item> AMMO_44_STAR = registerSimpleWeaponItem("ammo_44_star");
    public static final DeferredItem<Item> AMMO_9MM = registerSimpleWeaponItem("ammo_9mm");
    public static final DeferredItem<Item> AMMO_9MM_AP = registerSimpleWeaponItem("ammo_9mm_ap");
    public static final DeferredItem<Item> AMMO_9MM_DU = registerSimpleWeaponItem("ammo_9mm_du");
    public static final DeferredItem<Item> AMMO_9MM_ROCKET = registerSimpleWeaponItem("ammo_9mm_rocket");
    public static final DeferredItem<Item> AMMO_556 = registerSimpleWeaponItem("ammo_556");
    public static final DeferredItem<Item> AMMO_556_PHOSPHORUS = registerSimpleWeaponItem("ammo_556_phosphorus");
    public static final DeferredItem<Item> AMMO_556_AP = registerSimpleWeaponItem("ammo_556_ap");
    public static final DeferredItem<Item> AMMO_556_DU = registerSimpleWeaponItem("ammo_556_du");
    public static final DeferredItem<Item> AMMO_556_STAR = registerSimpleWeaponItem("ammo_556_star");
    public static final DeferredItem<Item> AMMO_556_SLEEK = registerSimpleWeaponItem("ammo_556_sleek");
    public static final DeferredItem<Item> AMMO_556_TRACER = registerSimpleWeaponItem("ammo_556_tracer");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE = registerSimpleWeaponItem("ammo_556_flechette");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE_INCENDIARY = registerSimpleWeaponItem("ammo_556_flechette_incendiary");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE_PHOSPHORUS = registerSimpleWeaponItem("ammo_556_flechette_phosphorus");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE_DU = registerSimpleWeaponItem("ammo_556_flechette_du");
    public static final DeferredItem<Item> AMMO_556_FLECHETTE_SLEEK = registerSimpleWeaponItem("ammo_556_flechette_sleek");
    public static final DeferredItem<Item> AMMO_556_K = registerSimpleWeaponItem("ammo_556_k");
    public static final DeferredItem<Item> AMMO_50BMG = registerSimpleWeaponItem("ammo_50bmg");
    public static final DeferredItem<Item> AMMO_50BMG_INCENDIARY = registerSimpleWeaponItem("ammo_50bmg_incendiary");
    public static final DeferredItem<Item> AMMO_50BMG_PHOSPHORUS = registerSimpleWeaponItem("ammo_50bmg_phosphorus");
    public static final DeferredItem<Item> AMMO_50BMG_EXPLOSIVE = registerSimpleWeaponItem("ammo_50bmg_explosive");
    public static final DeferredItem<Item> AMMO_50BMG_DU = registerSimpleWeaponItem("ammo_50bmg_du");
    public static final DeferredItem<Item> AMMO_50BMG_STAR = registerSimpleWeaponItem("ammo_50bmg_star");
    public static final DeferredItem<Item> AMMO_50BMG_SLEEK = registerSimpleWeaponItem("ammo_50bmg_sleek");
    public static final DeferredItem<Item> AMMO_75BOLT = registerSimpleWeaponItem("ammo_75bolt");
    public static final DeferredItem<Item> AMMO_75BOLT_INCENDIARY = registerSimpleWeaponItem("ammo_75bolt_incendiary");
    public static final DeferredItem<Item> AMMO_75BOLT_HE = registerSimpleWeaponItem("ammo_75bolt_he");
    public static final DeferredItem<Item> AMMO_50BMG_AP = registerSimpleWeaponItem("ammo_50bmg_ap");
    public static final DeferredItem<Item> AMMO_5MM = registerSimpleWeaponItem("ammo_5mm");
    public static final DeferredItem<Item> AMMO_5MM_EXPLOSIVE = registerSimpleWeaponItem("ammo_5mm_explosive");
    public static final DeferredItem<Item> AMMO_5MM_DU = registerSimpleWeaponItem("ammo_5mm_du");
    public static final DeferredItem<Item> AMMO_5MM_STAR = registerSimpleWeaponItem("ammo_5mm_star");
    public static final DeferredItem<Item> AMMO_50AE = registerSimpleWeaponItem("ammo_50ae");
    public static final DeferredItem<Item> AMMO_50AE_AP = registerSimpleWeaponItem("ammo_50ae_ap");
    public static final DeferredItem<Item> AMMO_50AE_DU = registerSimpleWeaponItem("ammo_50ae_du");
    public static final DeferredItem<Item> AMMO_50AE_STAR = registerSimpleWeaponItem("ammo_50ae_star");
    public static final DeferredItem<Item> AMMO_FOLLY = registerSimpleWeaponItem("ammo_folly");
    public static final DeferredItem<Item> AMMO_FOLLY_NUCLEAR = registerSimpleWeaponItem("ammo_folly_nuclear");
    public static final DeferredItem<Item> AMMO_FOLLY_DU = registerSimpleWeaponItem("ammo_folly_du");

    // --- Bulk-ported grenade items (ItemGrenade; no throw/entity behavior yet, same as ammo) ---
    public static final DeferredItem<Item> GRENADE_STRONG = registerSimpleWeaponItem("grenade_strong");
    public static final DeferredItem<Item> GRENADE_FRAG = registerSimpleWeaponItem("grenade_frag");
    public static final DeferredItem<Item> GRENADE_FIRE = registerSimpleWeaponItem("grenade_fire");
    public static final DeferredItem<Item> GRENADE_SHRAPNEL = registerSimpleWeaponItem("grenade_shrapnel");
    public static final DeferredItem<Item> GRENADE_CLUSTER = registerSimpleWeaponItem("grenade_cluster");
    public static final DeferredItem<Item> GRENADE_FLARE = registerSimpleWeaponItem("grenade_flare");
    public static final DeferredItem<Item> GRENADE_ELECTRIC = registerSimpleWeaponItem("grenade_electric");
    public static final DeferredItem<Item> GRENADE_POISON = registerSimpleWeaponItem("grenade_poison");
    public static final DeferredItem<Item> GRENADE_GAS = registerSimpleWeaponItem("grenade_gas");
    public static final DeferredItem<Item> GRENADE_MIRV = registerSimpleWeaponItem("grenade_mirv");
    public static final DeferredItem<Item> GRENADE_BURST = registerSimpleWeaponItem("grenade_burst");
    public static final DeferredItem<Item> GRENADE_PULSE = registerSimpleWeaponItem("grenade_pulse");
    public static final DeferredItem<Item> GRENADE_PLASMA = registerSimpleWeaponItem("grenade_plasma");
    public static final DeferredItem<Item> GRENADE_TAU = registerSimpleWeaponItem("grenade_tau");
    public static final DeferredItem<Item> GRENADE_SCHRABIDIUM = registerSimpleWeaponItem("grenade_schrabidium");
    public static final DeferredItem<Item> GRENADE_LEMON = registerSimpleWeaponItem("grenade_lemon");
    public static final DeferredItem<Item> GRENADE_MK2 = registerSimpleWeaponItem("grenade_mk2");
    public static final DeferredItem<Item> GRENADE_NUCLEAR = registerSimpleWeaponItem("grenade_nuclear");
    public static final DeferredItem<Item> GRENADE_ZOMG = registerSimpleWeaponItem("grenade_zomg");
    public static final DeferredItem<Item> GRENADE_SOLINIUM = registerSimpleWeaponItem("grenade_solinium");
    public static final DeferredItem<Item> GRENADE_BLACK_HOLE = registerSimpleWeaponItem("grenade_black_hole");
    public static final DeferredItem<Item> GRENADE_IF_GENERIC = registerSimpleWeaponItem("grenade_if_generic");
    public static final DeferredItem<Item> GRENADE_IF_HE = registerSimpleWeaponItem("grenade_if_he");
    public static final DeferredItem<Item> GRENADE_IF_BOUNCY = registerSimpleWeaponItem("grenade_if_bouncy");
    public static final DeferredItem<Item> GRENADE_IF_STICKY = registerSimpleWeaponItem("grenade_if_sticky");
    public static final DeferredItem<Item> GRENADE_IF_INCENDIARY = registerSimpleWeaponItem("grenade_if_incendiary");
    public static final DeferredItem<Item> GRENADE_IF_TOXIC = registerSimpleWeaponItem("grenade_if_toxic");
    public static final DeferredItem<Item> GRENADE_IF_CONCUSSION = registerSimpleWeaponItem("grenade_if_concussion");
    public static final DeferredItem<Item> GRENADE_IF_BRIMSTONE = registerSimpleWeaponItem("grenade_if_brimstone");
    public static final DeferredItem<Item> GRENADE_IF_MYSTERY = registerSimpleWeaponItem("grenade_if_mystery");
    public static final DeferredItem<Item> GRENADE_IF_HOPWIRE = registerSimpleWeaponItem("grenade_if_hopwire");
    public static final DeferredItem<Item> GRENADE_IF_SPARK = registerSimpleWeaponItem("grenade_if_spark");
    public static final DeferredItem<Item> GRENADE_IF_NULL = registerSimpleWeaponItem("grenade_if_null");

    // --- Bulk-ported gun items (ItemGunBase; no firing/ammo-compatibility system yet, durability preserved where the legacy declaration set one) ---
    public static final DeferredItem<Item> GUN_REVOLVER_IRON = registerDurableWeaponItem("gun_revolver_iron", 100);
    public static final DeferredItem<Item> GUN_REVOLVER = registerDurableWeaponItem("gun_revolver", 0);
    public static final DeferredItem<Item> GUN_REVOLVER_SATURNITE = registerDurableWeaponItem("gun_revolver_saturnite", 0);
    public static final DeferredItem<Item> GUN_REVOLVER_GOLD = registerDurableWeaponItem("gun_revolver_gold", 1000);
    public static final DeferredItem<Item> GUN_REVOLVER_LEAD = registerDurableWeaponItem("gun_revolver_lead", 250);
    public static final DeferredItem<Item> GUN_REVOLVER_SCHRABIDIUM = registerDurableWeaponItem("gun_revolver_schrabidium", 20000);
    public static final DeferredItem<Item> GUN_REVOLVER_CURSED = registerDurableWeaponItem("gun_revolver_cursed", 5000);
    public static final DeferredItem<Item> GUN_REVOLVER_NIGHTMARE = registerDurableWeaponItem("gun_revolver_nightmare", 6);
    public static final DeferredItem<Item> GUN_REVOLVER_NIGHTMARE2 = registerDurableWeaponItem("gun_revolver_nightmare2", 6);
    public static final DeferredItem<Item> GUN_REVOLVER_PIP = registerDurableWeaponItem("gun_revolver_pip", 1000);
    public static final DeferredItem<Item> GUN_REVOLVER_NOPIP = registerDurableWeaponItem("gun_revolver_nopip", 1000);
    public static final DeferredItem<Item> GUN_REVOLVER_BLACKJACK = registerDurableWeaponItem("gun_revolver_blackjack", 1000);
    public static final DeferredItem<Item> GUN_REVOLVER_SILVER = registerDurableWeaponItem("gun_revolver_silver", 1000);
    public static final DeferredItem<Item> GUN_REVOLVER_RED = registerDurableWeaponItem("gun_revolver_red", 1000);
    public static final DeferredItem<Item> GUN_DEAGLE = registerDurableWeaponItem("gun_deagle", 0);
    public static final DeferredItem<Item> GUN_FLECHETTE = registerDurableWeaponItem("gun_flechette", 0);
    public static final DeferredItem<Item> GUN_AR15 = registerDurableWeaponItem("gun_ar15", 0);
    public static final DeferredItem<Item> GUN_UBOINIK = registerDurableWeaponItem("gun_uboinik", 0);
    public static final DeferredItem<Item> GUN_KS23 = registerDurableWeaponItem("gun_ks23", 0);
    public static final DeferredItem<Item> GUN_SAUER = registerDurableWeaponItem("gun_sauer", 0);
    public static final DeferredItem<Item> GUN_CALAMITY = registerDurableWeaponItem("gun_calamity", 0);
    public static final DeferredItem<Item> GUN_CALAMITY_DUAL = registerDurableWeaponItem("gun_calamity_dual", 0);
    public static final DeferredItem<Item> GUN_BOLT_ACTION = registerDurableWeaponItem("gun_bolt_action", 0);
    public static final DeferredItem<Item> GUN_BOLT_ACTION_GREEN = registerDurableWeaponItem("gun_bolt_action_green", 0);
    public static final DeferredItem<Item> GUN_UZI = registerDurableWeaponItem("gun_uzi", 0);
    public static final DeferredItem<Item> GUN_UZI_SILENCER = registerDurableWeaponItem("gun_uzi_silencer", 0);
    public static final DeferredItem<Item> GUN_UZI_SATURNITE = registerDurableWeaponItem("gun_uzi_saturnite", 0);
    public static final DeferredItem<Item> GUN_UZI_SATURNITE_SILENCER = registerDurableWeaponItem("gun_uzi_saturnite_silencer", 0);
    public static final DeferredItem<Item> GUN_MP40 = registerDurableWeaponItem("gun_mp40", 0);
    public static final DeferredItem<Item> GUN_THOMPSON = registerDurableWeaponItem("gun_thompson", 0);
    public static final DeferredItem<Item> GUN_RPG = registerDurableWeaponItem("gun_rpg", 0);
    public static final DeferredItem<Item> GUN_KARL = registerDurableWeaponItem("gun_karl", 0);
    public static final DeferredItem<Item> GUN_PANZERSCHRECK = registerDurableWeaponItem("gun_panzerschreck", 0);
    public static final DeferredItem<Item> GUN_QUADRO = registerDurableWeaponItem("gun_quadro", 0);
    public static final DeferredItem<Item> GUN_LEVER_ACTION = registerDurableWeaponItem("gun_lever_action", 0);
    public static final DeferredItem<Item> GUN_LEVER_ACTION_DARK = registerDurableWeaponItem("gun_lever_action_dark", 0);
    public static final DeferredItem<Item> GUN_HK69 = registerDurableWeaponItem("gun_hk69", 0);
    public static final DeferredItem<Item> GUN_FATMAN = registerDurableWeaponItem("gun_fatman", 0);
    public static final DeferredItem<Item> GUN_PROTO = registerDurableWeaponItem("gun_proto", 0);
    public static final DeferredItem<Item> GUN_MIRV = registerDurableWeaponItem("gun_mirv", 0);
    public static final DeferredItem<Item> GUN_BF = registerDurableWeaponItem("gun_bf", 0);
    public static final DeferredItem<Item> GUN_ZOMG = registerDurableWeaponItem("gun_zomg", 0);
    public static final DeferredItem<Item> GUN_MP = registerDurableWeaponItem("gun_mp", 0);
    public static final DeferredItem<Item> GUN_BOLTER = registerDurableWeaponItem("gun_bolter", 0);
    public static final DeferredItem<Item> GUN_FLAMER = registerDurableWeaponItem("gun_flamer", 0);
    public static final DeferredItem<Item> GUN_EMP = registerDurableWeaponItem("gun_emp", 0);

    // --- Bulk-ported standard missile items (ItemMissileStandard) ---
    public static final DeferredItem<Item> MISSILE_GENERIC = registerSimpleMissileItem("missile_generic");
    public static final DeferredItem<Item> MISSILE_STRONG = registerSimpleMissileItem("missile_strong");
    public static final DeferredItem<Item> MISSILE_BURST = registerSimpleMissileItem("missile_burst");
    public static final DeferredItem<Item> MISSILE_INCENDIARY = registerSimpleMissileItem("missile_incendiary");
    public static final DeferredItem<Item> MISSILE_INCENDIARY_STRONG = registerSimpleMissileItem("missile_incendiary_strong");
    public static final DeferredItem<Item> MISSILE_INFERNO = registerSimpleMissileItem("missile_inferno");
    public static final DeferredItem<Item> MISSILE_CLUSTER = registerSimpleMissileItem("missile_cluster");
    public static final DeferredItem<Item> MISSILE_CLUSTER_STRONG = registerSimpleMissileItem("missile_cluster_strong");
    public static final DeferredItem<Item> MISSILE_RAIN = registerSimpleMissileItem("missile_rain");
    public static final DeferredItem<Item> MISSILE_BUSTER = registerSimpleMissileItem("missile_buster");
    public static final DeferredItem<Item> MISSILE_BUSTER_STRONG = registerSimpleMissileItem("missile_buster_strong");
    public static final DeferredItem<Item> MISSILE_DRILL = registerSimpleMissileItem("missile_drill");
    public static final DeferredItem<Item> MISSILE_N2 = registerSimpleMissileItem("missile_n2");
    public static final DeferredItem<Item> MISSILE_NUCLEAR_CLUSTER = registerSimpleMissileItem("missile_nuclear_cluster");
    public static final DeferredItem<Item> MISSILE_VOLCANO = registerSimpleMissileItem("missile_volcano");
    public static final DeferredItem<Item> MISSILE_ENDO = registerSimpleMissileItem("missile_endo");
    public static final DeferredItem<Item> MISSILE_EXO = registerSimpleMissileItem("missile_exo");
    public static final DeferredItem<Item> MISSILE_DOOMSDAY = registerSimpleMissileItem("missile_doomsday");
    public static final DeferredItem<Item> MISSILE_TAINT = registerSimpleMissileItem("missile_taint");
    public static final DeferredItem<Item> MISSILE_MICRO = registerSimpleMissileItem("missile_micro");
    public static final DeferredItem<Item> MISSILE_BHOLE = registerSimpleMissileItem("missile_bhole");
    public static final DeferredItem<Item> MISSILE_SCHRABIDIUM = registerSimpleMissileItem("missile_schrabidium");
    public static final DeferredItem<Item> MISSILE_EMP = registerSimpleMissileItem("missile_emp");
    public static final DeferredItem<Item> MISSILE_EMP_STRONG = registerSimpleMissileItem("missile_emp_strong");
    public static final DeferredItem<Item> MISSILE_ANTI_BALLISTIC = registerSimpleMissileItem("missile_anti_ballistic");
    public static final DeferredItem<Item> MISSILE_CARRIER = registerSimpleMissileItem("missile_carrier");

    // --- Bulk-ported starter kit items (ItemStarterKit; 'give kit contents' behavior not carried over) ---
    public static final DeferredItem<Item> STEALTH_BOY = registerSimpleConsumableItem("stealth_boy", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> EUPHEMIUM_KIT = ITEMS.register("euphemium_kit", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GRENADE_KIT = registerSimpleWeaponItem("grenade_kit");
    public static final DeferredItem<Item> GADGET_KIT = registerSimpleNukeItem("gadget_kit");
    public static final DeferredItem<Item> BOY_KIT = registerSimpleNukeItem("boy_kit");
    public static final DeferredItem<Item> MAN_KIT = registerSimpleNukeItem("man_kit");
    public static final DeferredItem<Item> MIKE_KIT = registerSimpleNukeItem("mike_kit");
    public static final DeferredItem<Item> TSAR_KIT = registerSimpleNukeItem("tsar_kit");
    public static final DeferredItem<Item> PROTOTYPE_KIT = registerSimpleNukeItem("prototype_kit");
    public static final DeferredItem<Item> FLEIJA_KIT = registerSimpleNukeItem("fleija_kit");
    public static final DeferredItem<Item> SOLINIUM_KIT = registerSimpleNukeItem("solinium_kit");
    public static final DeferredItem<Item> BALEFIRE_KIT = registerSimpleNukeItem("balefire_kit");
    public static final DeferredItem<Item> MULTI_KIT = registerSimpleNukeItem("multi_kit");
    public static final DeferredItem<Item> CUSTOM_KIT = registerSimpleNukeItem("custom_kit");
    public static final DeferredItem<Item> MISSILE_KIT = registerSimpleMissileItem("missile_kit");
    public static final DeferredItem<Item> T45_KIT = registerSimpleConsumableItem("t45_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> HAZMAT_KIT = registerSimpleConsumableItem("hazmat_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> HAZMAT_RED_KIT = registerSimpleConsumableItem("hazmat_red_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> HAZMAT_GREY_KIT = registerSimpleConsumableItem("hazmat_grey_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> NUKE_STARTER_KIT = registerSimpleConsumableItem("nuke_starter_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> NUKE_ADVANCED_KIT = registerSimpleConsumableItem("nuke_advanced_kit", properties -> properties.stacksTo(1));
    public static final DeferredItem<Item> LETTER = registerSimpleConsumableItem("letter", properties -> properties.stacksTo(1));

    // --- Bulk-ported multitool passive items (ItemMultitoolPassive; hidden-from-creative like their legacy setCreativeTab(null)) ---
    public static final DeferredItem<Item> MULTITOOL_HIT = ITEMS.register("multitool_hit", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_EXT = ITEMS.register("multitool_ext", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_MINER = ITEMS.register("multitool_miner", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_BEAM = ITEMS.register("multitool_beam", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_SKY = ITEMS.register("multitool_sky", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_MEGA = ITEMS.register("multitool_mega", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_JOULE = ITEMS.register("multitool_joule", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MULTITOOL_DECON = ITEMS.register("multitool_decon", () -> new Item(new Item.Properties().stacksTo(1)));

    // --- Bulk-ported swords/hoes using HbmToolTiers (ModSword/ModHoe) ---
    public static final DeferredItem<Item> CROWBAR = registerSwordItem("crowbar", HbmToolTiers.STEEL);
    public static final DeferredItem<Item> WEAPON_SAW = registerSwordItem("weapon_saw", HbmToolTiers.SAW);
    public static final DeferredItem<Item> WEAPON_BAT = registerSwordItem("weapon_bat", HbmToolTiers.BAT);
    public static final DeferredItem<Item> WEAPON_BAT_NAIL = registerSwordItem("weapon_bat_nail", HbmToolTiers.BAT_NAIL);
    public static final DeferredItem<Item> WEAPON_GOLF_CLUB = registerSwordItem("weapon_golf_club", HbmToolTiers.GOLF_CLUB);
    public static final DeferredItem<Item> WEAPON_PIPE_RUSTY = registerSwordItem("weapon_pipe_rusty", HbmToolTiers.PIPE_RUSTY);
    public static final DeferredItem<Item> WEAPON_PIPE_LEAD = registerSwordItem("weapon_pipe_lead", HbmToolTiers.PIPE_LEAD);
    public static final DeferredItem<Item> REER_GRAAR = registerSwordItem("reer_graar", HbmToolTiers.TITANIUM);
    public static final DeferredItem<Item> TITANIUM_HOE = registerHoeItem("titanium_hoe", HbmToolTiers.TITANIUM);
    public static final DeferredItem<Item> STEEL_HOE = registerHoeItem("steel_hoe", HbmToolTiers.STEEL);
    public static final DeferredItem<Item> ALLOY_HOE = registerHoeItem("alloy_hoe", HbmToolTiers.ALLOY);
    public static final DeferredItem<Item> DESH_HOE = registerHoeItem("desh_hoe", HbmToolTiers.DESH);
    public static final DeferredItem<Item> COBALT_HOE = registerHoeItem("cobalt_hoe", HbmToolTiers.COBALT);
    public static final DeferredItem<Item> COBALT_DECORATED_HOE = registerHoeItem("cobalt_decorated_hoe", HbmToolTiers.COBALT_DECORATED);
    public static final DeferredItem<Item> STARMETAL_HOE = registerHoeItem("starmetal_hoe", HbmToolTiers.STARMETAL);
    public static final DeferredItem<Item> CMB_HOE = registerHoeItem("cmb_hoe", HbmToolTiers.CMB);

    private HbmItems() {
    }

    public static void addDynamicPartsTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : PARTS_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static void addDynamicControlTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : CONTROL_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static void addDynamicConsumableTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : CONSUMABLE_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static void addDynamicWeaponTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : WEAPON_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static void addDynamicNukeTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : NUKE_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static void addDynamicMissileTabItems(CreativeModeTab.Output output) {
        for (Supplier<? extends Item> item : MISSILE_TAB_DYNAMIC_ITEMS) {
            output.accept(item.get());
        }
    }

    public static Iterable<Supplier<? extends Item>> getDynamicPartsTabItems() {
        return Collections.unmodifiableList(PARTS_TAB_DYNAMIC_ITEMS);
    }

    private static DeferredItem<Item> registerSimplePartItem(String name) {
        DeferredItem<Item> item = ITEMS.registerSimpleItem(name);
        PARTS_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSimpleControlItem(String name) {
        return registerSimpleControlItem(name, UnaryOperator.identity());
    }

    private static DeferredItem<Item> registerSingleStackControlItem(String name) {
        return registerSimpleControlItem(name, properties -> properties.stacksTo(1));
    }

    private static DeferredItem<Item> registerSimpleControlItem(String name, UnaryOperator<Item.Properties> propertiesFactory) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(propertiesFactory.apply(new Item.Properties())));
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerRemainderControlItem(
        String name,
        Supplier<? extends Item> remainder,
        int stackSize
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new CraftingRemainderItem(new Item.Properties().stacksTo(stackSize), remainder)
        );
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSimpleConsumableItem(String name) {
        return registerSimpleConsumableItem(name, UnaryOperator.identity());
    }

    private static DeferredItem<Item> registerSimpleConsumableItem(String name, UnaryOperator<Item.Properties> propertiesFactory) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(propertiesFactory.apply(new Item.Properties())));
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static Item.Properties foodProperties(int nutrition, float saturationModifier) {
        return new Item.Properties().food(
            new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build()
        );
    }

    private static DeferredItem<Item> registerFoodConsumableItem(String name, int nutrition, float saturationModifier) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(foodProperties(nutrition, saturationModifier)));
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerFoodPartItem(String name, int nutrition, float saturationModifier) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(foodProperties(nutrition, saturationModifier)));
        PARTS_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerFoodItemNoTab(String name, int nutrition, float saturationModifier) {
        return ITEMS.register(name, () -> new Item(foodProperties(nutrition, saturationModifier)));
    }

    private static DeferredItem<Item> registerSimpleWeaponItem(String name) {
        DeferredItem<Item> item = ITEMS.registerSimpleItem(name);
        WEAPON_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSwordItem(String name, net.minecraft.world.item.Tier tier) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new net.minecraft.world.item.SwordItem(tier, new Item.Properties()));
        WEAPON_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerHoeItem(String name, net.minecraft.world.item.Tier tier) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new net.minecraft.world.item.HoeItem(tier, new Item.Properties()));
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerDurableWeaponItem(String name, int durability) {
        DeferredItem<Item> item = ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties().stacksTo(1);
            if (durability > 0) {
                properties.durability(durability);
            }
            return new Item(properties);
        });
        WEAPON_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSimpleNukeItem(String name) {
        DeferredItem<Item> item = ITEMS.registerSimpleItem(name);
        NUKE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSimpleMissileItem(String name) {
        DeferredItem<Item> item = ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
        MISSILE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerInstantConsumableItem(
        String name,
        UnaryOperator<Item.Properties> propertiesFactory,
        java.util.function.BiConsumer<net.minecraft.world.level.Level, net.minecraft.world.entity.LivingEntity> consumeAction,
        net.minecraft.sounds.SoundEvent useSound
    ) {
        return registerInstantConsumableItem(name, propertiesFactory, consumeAction, useSound, List.of());
    }

    private static DeferredItem<Item> registerInstantConsumableItem(
        String name,
        UnaryOperator<Item.Properties> propertiesFactory,
        java.util.function.BiConsumer<net.minecraft.world.level.Level, net.minecraft.world.entity.LivingEntity> consumeAction,
        net.minecraft.sounds.SoundEvent useSound,
        List<Component> tooltipLines
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new InstantMedicalItem(propertiesFactory.apply(new Item.Properties()), useSound, consumeAction, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerMedicalPillItem(
        String name,
        java.util.function.BiConsumer<net.minecraft.world.level.Level, net.minecraft.world.entity.LivingEntity> consumeAction,
        List<Component> tooltipLines
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new MedicalPillItem(new Item.Properties(), SoundEvents.GENERIC_EAT, consumeAction, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerLorePartItem(String name) {
        DeferredItem<Item> item = registerLoreItem(name);
        PARTS_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerLoreControlItem(String name, UnaryOperator<Item.Properties> propertiesFactory) {
        DeferredItem<Item> item = registerLoreItem(name, propertiesFactory);
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerRemainderLoreControlItem(String name, Supplier<? extends Item> remainder) {
        return registerRemainderLoreControlItem(name, remainder, 64);
    }

    private static DeferredItem<Item> registerRemainderLoreControlItem(
        String name,
        Supplier<? extends Item> remainder,
        int stackSize
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new CraftingRemainderLoreItem(new Item.Properties().stacksTo(stackSize), remainder)
        );
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerLoreConsumableItem(String name, UnaryOperator<Item.Properties> propertiesFactory) {
        DeferredItem<Item> item = registerLoreItem(name, propertiesFactory);
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerDurableControlItem(String name, int durability, boolean templateFolderHint) {
        DeferredItem<Item> item = ITEMS.register(name, () -> {
            Item.Properties properties = new Item.Properties();
            if (durability > 0) {
                properties.durability(durability);
            } else {
                properties.stacksTo(1);
            }
            return new DurableTooltipItem(properties, templateFolderHint);
        });
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerBatteryItem(String name, int capacity, int chargeRate, int dischargeRate) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new BatteryItem(new Item.Properties().stacksTo(1), capacity, chargeRate, dischargeRate)
        );
        CONTROL_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerGasMaskFilterItem(String name, int durability, List<HbmHazardClass> protectedHazards) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new GasMaskFilterItem(new Item.Properties().stacksTo(8), durability, protectedHazards)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerGasMaskAttachmentItem(String name, List<HbmHazardClass> hazardBlacklist, boolean gasProtectionTooltip) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new GasMaskAttachmentItem(new Item.Properties().stacksTo(1), hazardBlacklist, gasProtectionTooltip)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerServoArmorModItem(
        String name,
        double attackBonus,
        double speedBonus,
        int hasteAmplifier,
        int jumpAmplifier
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new ServoArmorModItem(new Item.Properties().stacksTo(1), attackBonus, speedBonus, hasteAmplifier, jumpAmplifier)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerBatteryArmorModItem(String name, double multiplier) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new BatteryArmorModItem(new Item.Properties().stacksTo(1), multiplier)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerGasSensorArmorModItem(String name) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new GasSensorArmorModItem(new Item.Properties().stacksTo(1))
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerCharmArmorModItem(String name, float broadcastDamageMultiplier, List<String> tooltipLines) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new CharmArmorModItem(new Item.Properties().stacksTo(1), broadcastDamageMultiplier, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerSurveyLensArmorModItem(String name) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new SurveyLensArmorModItem(new Item.Properties().stacksTo(1))
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerTooltipArmorModItem(
        String name,
        ArmorModSlot slot,
        boolean helmet,
        boolean chestplate,
        boolean leggings,
        boolean boots,
        List<Component> tooltipLines
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new TooltipArmorModItem(new Item.Properties().stacksTo(1), slot, helmet, chestplate, leggings, boots, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerCladdingArmorModItem(
        String name,
        float radiationResistance,
        double knockbackResistance,
        List<Component> tooltipLines
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new CladdingArmorModItem(new Item.Properties().stacksTo(1), radiationResistance, knockbackResistance, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerPadsArmorModItem(String name, float fallDamageMultiplier, boolean staticCharge) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new PadsArmorModItem(new Item.Properties().stacksTo(1), fallDamageMultiplier, staticCharge)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerInsertArmorModItem(
        String name,
        int durability,
        float damageMultiplier,
        float projectileMultiplier,
        float explosionMultiplier,
        float speedMultiplier
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new InsertArmorModItem(
                new Item.Properties().durability(durability).stacksTo(1),
                damageMultiplier,
                projectileMultiplier,
                explosionMultiplier,
                speedMultiplier
            )
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerHealthArmorModItem(
        String name,
        boolean helmet,
        boolean chestplate,
        boolean leggings,
        boolean boots,
        double healthBonus,
        List<Component> tooltipLines
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new HealthArmorModItem(new Item.Properties().stacksTo(1), helmet, chestplate, leggings, boots, healthBonus, tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerLodestoneArmorModItem(String name, int range) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new LodestoneArmorModItem(new Item.Properties().stacksTo(1), range)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerMedalArmorModItem(String name, float radiationReductionPerTick) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new MedalArmorModItem(new Item.Properties().stacksTo(1), radiationReductionPerTick)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerReviveArmorModItem(String name, int durability, List<Component> tooltipLines) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new ReviveArmorModItem(new Item.Properties().durability(durability).stacksTo(1), tooltipLines)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerArmorItem(
        String name,
        Holder<net.minecraft.world.item.ArmorMaterial> material,
        ArmorItem.Type type,
        ResourceLocation outerTexture,
        ResourceLocation innerTexture,
        float radiationResistance
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new ModularArmorItem(material, type, new Item.Properties().stacksTo(1), outerTexture, innerTexture, null, radiationResistance)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static DeferredItem<Item> registerGasMaskArmorItem(
        String name,
        Holder<net.minecraft.world.item.ArmorMaterial> material,
        ResourceLocation outerTexture,
        ResourceLocation innerTexture,
        ResourceLocation overlayTexture,
        float radiationResistance,
        List<HbmHazardClass> hazardBlacklist
    ) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new GasMaskArmorItem(material, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1), outerTexture, innerTexture, overlayTexture, radiationResistance, hazardBlacklist)
        );
        CONSUMABLE_TAB_DYNAMIC_ITEMS.add(item);
        return item;
    }

    private static ResourceLocation armorTexture(String fileName) {
        return ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "textures/armor/" + fileName);
    }

    private static ResourceLocation miscTexture(String fileName) {
        return ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "textures/misc/" + fileName);
    }

    private static Component tooltip(ChatFormatting color, String text) {
        return Component.literal(text).withStyle(color);
    }

    private static DeferredItem<Item> registerLoreItem(String name) {
        return registerLoreItem(name, UnaryOperator.identity());
    }

    private static DeferredItem<Item> registerLoreItem(String name, UnaryOperator<Item.Properties> propertiesFactory) {
        return ITEMS.register(name, () -> new LoreItem(propertiesFactory.apply(new Item.Properties())));
    }
}
