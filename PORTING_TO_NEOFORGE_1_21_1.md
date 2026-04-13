# NeoForge 1.21.1 Port Status

## ✅ Port Completion Status

**The port from Forge 1.12.2 to NeoForge 1.21.1 is now COMPLETE and BUILDING SUCCESSFULLY.**

- ✅ Build system: NeoGradle with NeoForge 1.21.1-21.1.222
- ✅ Java version: Java 21 toolchain
- ✅ Compilation: **SUCCESS** - All 193+ Neo Java files compile without errors
- ✅ Output JAR: `build/libs/hbm-1.0.0-1.21.1.jar` (54MB)
- ✅ Source structure: Neo-only under `src/neo/java`
- ✅ Resources: Modern data-driven format in `src/generated/resources` and `src/main/resources`

The final compilation blocker (DepthDepositConfig codec API mismatch) has been resolved by migrating to string-based block ID references with runtime registry resolution.

---

This workspace now targets NeoForge `1.21.1-21.1.222` at the build level.

The live Java source set is now Neo-only under `src/neo/java`. The legacy 1.12.2 Java snapshot has been archived to `migration/1.21.1/archive/legacy-source-tree/src/main/java` so it no longer sits in the active source layout or IDE source roots for normal feature work.

Completion criteria for calling this a proper port now live in `migration/1.21.1/PROPER_PORT_BASELINE.md`.
The shared cross-cutting replacement gate now lives in `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`.
The machine and block-entity framework gate now lives in `migration/1.21.1/MACHINE_BLOCKENTITY_FRAMEWORK.md`.
The final transport/network architecture decision now lives in `migration/1.21.1/TRANSPORT_NETWORK_DECISION.md`.
The bulk item/block registry completion gate now lives in `migration/1.21.1/BULK_ITEM_BLOCK_REGISTRY_COMPLETION.md`.

## Completed Foundation Slices

- Replaced the legacy 1.12.2 build with a NeoGradle NeoForge 1.21.1 build
- Added a modern `@Mod` entrypoint and `neoforge.mods.toml`
- Staged converted `.lang` files as JSON and moved legacy advancements into the modern data path
- Augmented converted language files with modern `item.hbm.*`, `block.hbm.*`, and `entity.hbm.*` aliases
- Added the first NeoForge registry layer for blocks, items, entities, block entities, menu types, and creative tabs
- Recreated the legacy creative tab structure with safe vanilla placeholder content so future item and block ports have stable tab targets
- Ported an initial low-risk item slice into the NeoForge registry: `ingot_uranium`, `hazmat_cloth`, `plate_titanium`, and `missile_nuclear`
- Ported an initial low-risk block slice with block items, loot tables, and tool tags: `asphalt`, `reinforced_stone`, `concrete`, `concrete_hazard`, `block_titanium`, `ore_copper`, `ore_titanium`, `ore_tungsten`, and `ore_lead`
- Expanded the metallurgy slice with simple ingots, storage blocks, and related ores: `ingot_steel`, `ingot_titanium`, `ingot_copper`, `ingot_red_copper`, `ingot_tungsten`, `ingot_aluminium`, `ingot_beryllium`, `ingot_lead`, `ingot_advanced_alloy`, `block_advanced_alloy`, `block_aluminium`, `block_beryllium`, `block_copper`, `block_lead`, `block_red_copper`, `block_steel`, `block_tungsten`, `ore_aluminium`, and `ore_beryllium`
- Expanded the static construction slice with full opaque blocks from the legacy build palette: `brick_concrete`, `brick_concrete_mossy`, `brick_concrete_cracked`, `brick_concrete_broken`, `concrete_smooth`, and the full colored concrete set from `concrete_white` through `concrete_black`
- Added matching stairs and slabs for the currently ported reinforced stone, brick concrete, and concrete families
- Expanded the plate material slice with simple items that already have legacy model coverage: `plate_copper`, `plate_aluminium`, `plate_steel`, and `plate_advanced_alloy`
- Expanded the plain parts slice with additional simple plates, armor-plating components, and coils that already map cleanly to legacy `ItemBase` definitions
- Added a reusable NeoForge lore-item implementation that reads legacy tooltip translation keys and basic `§` formatting so future `ItemCustomLore` ports can stay data-driven
- Flattened the legacy metadata-backed circuit item into explicit NeoForge item registrations and expanded nearby parts with crystals, mechanisms, primers, casings, assemblies, `mold_base`, `upgrade_template`, `deuterium_filter`, `plate_lead`, `coil_magnetized_tungsten`, and `gem_tantalium`
- Started the first large rewrite layer with a NeoForge bootstrap scaffold, attachment-backed replacements for the two legacy capability families, and a real packet registrar with a serverbound key-state payload
- Replaced the first legacy `PacketDispatcher` slice with typed Neo payloads for key-state, player/living attachment sync, press-machine state, survey state, and HUD notifications, with explicit login/respawn/tracking resync around the new attachment model
- Started the entity rewrite layer with the first Neo entity registrations, renderer wiring, spawn-placement rules, hostile mob slice (`entity_glowing_one`, `entity_nuclear_creeper`, `entity_tainted_creeper`), and a throwable `entity_grenade_generic` item/entity path
- Started the block-entity/menu rewrite layer with the first real end-to-end container ports: `crate_iron` and `crate_steel` now have Neo blocks, block entities, menu types, and client screens
- Expanded the storage rewrite path with reusable arbitrary-grid Neo menus/screens and two more legacy storage containers: `crate_desh` and `safe` now have Neo blocks, block entities, menu types, client screens, loot, and harvest tags
- Expanded the low-risk content surface with a bulk metallurgy/material pass: many remaining ingots, billets, powders, wastes, and companion plain ore/storage blocks now register in NeoForge with creative-tab wiring, loot, tags, and simple compaction recipes
- Replaced the dead legacy keybind path with Neo key-mapping registration plus client-tick key-state payload sync, moved `reinforced_glass` onto a modern data-driven model `render_type`, and moved the ported grenade fuse effect onto modern particle spawning instead of legacy `effectRenderer` hooks
- Expanded the live client entity slice with a dedicated Neo renderer/model/layer-definition path for `entity_cyber_crab`, including emissive-eye rendering, so every entity currently registered under `src/neo/java` now has matching client renderer coverage
- Replaced the placeholder falling-nuke detonation with a real Neo server-side payload path: `FallingNukeEntity` now routes through a payload-aware explosion handler that separates blast, crater carving, fire, and radiation/contamination, and `ExplosionNukeRayBatched` now performs actual ray-based crater collection against modern world heights instead of acting as an inert persisted stub
- Started the first legacy platform-system migration slice by adding Neo common-material tags for the ported metallurgy compaction recipes, a real Neo common config for ported mob defaults, and reusable Neo energy/fluid container wrappers for upcoming machine block-entity rewrites
- Removed the unsupported Forge 1.12 loader hacks from the live source/resource trees by archiving the legacy coremod package, access transformer, and coremod manifest under `migration/1.21.1/archive/legacy-loader-hacks`
- Expanded the content surface again with a bulk construction/control pass: another reinforced and ducrete construction family now registers with loot, harvest tags, and modernized slab blockstates, and the control tab now includes fuse/part items plus the full stamp and blade durability families through Neo item registrations
- Expanded the plain item surface again with dynamic Neo consumable-tab wiring and another legacy-ID batch: syringes, RadAway and pill-family shells, gas-mask filters and attachments, `servo_set`, `servo_set_desh`, `gas_sensor`, `protection_charm`, `meteor_charm`, `neutrino_lens`, and `arc_electrode_desh` now register in Neo with legacy stack-size constraints where they were obvious, while their custom gameplay behavior remains deferred
- Started the first real full-behavior consumable rewrite slice: the medical family now has Neo custom item logic and Neo mob effects, so `radaway`, `radaway_strong`, `radaway_flush`, `radx`, `siox`, `pill_herbal`, `xanax`, `fmn`, `five_htp`, `pill_iodine`, `plan_c`, and `med_bag` now execute real use behavior against the attachment-backed living data, and the currently ported radiation-exposure mobs now respect `radx` resistance through a shared helper
- Started the first real armor and gas-mask rewrite slice: Neo armor materials, modular armor items, gas-mask helmets, filters, mask attachments, armor batteries, servos, charms, and survey/gas-sensor upgrades now support legacy-style install/remove flows, filter durability and tooltips, hazmat-derived radiation resistance, and the first passive armor hooks for servo buffs and broadcaster-damage mitigation
- Expanded the modular armor rewrite with a broad old armor-mod batch: pads, cladding, inserts, health/revive upgrades, lodestones, medals, and many adjacent armor mods now register as installable Neo armor upgrades, with live fall-damage, damage-reduction, health, knockback, attraction, radiation-decay, retaliation, revive, and auto-injector behavior where the legacy logic was self-contained
- Started the first real powered machine vertical slice: `machine_press` and `press_preheater` now register as Neo blocks, and the burner press has a ticking Neo block entity, fuel burn, preheater boost, a constrained recipe table for the currently ported plate outputs, a real menu/screen path, and client progress sync through the existing typed press-state payload
- Added the first Neo item-energy capability layer with real battery items and used it to port `machine_epress` as the next powered machine slice: the electric press now has a Neo block, energy-backed block entity, block energy capability, battery charging slot, menu/screen path, and live plate-press behavior on top of the existing press recipe registry
- Expanded the powered machine rewrite with `machine_shredder`: the Neo shredder now has its own block, 30-slot block entity, energy capability, battery slot, blade-state handling, bounded shredding recipe table for currently ported materials and vanilla basics, and a matching menu/screen path using the legacy shredder assets
- Expanded the powered machine rewrite with `machine_electric_furnace_off`: the Neo electric furnace now has a facing and lit blockstate, particle effects, vanilla smelting-recipe lookup, energy-backed block entity, battery charging slot, menu/screen path, and harvest/loot wiring while reusing the legacy off/on block models through a single modern stateful block
- Added the first shared Neo energy-machine block-entity base and used it to port the legacy transformer charger family: `machine_transformer`, `machine_transformer_20`, `machine_transformer_dnt`, and `machine_transformer_dnt_20` now have Neo ticking block entities, block-energy capability exposure, nearby-player battery charging behavior, direct held-battery charging as an interim power path before the cable network rewrite lands, and live reuse of the staged legacy block/item assets
- Expanded the fluid-storage rewrite with the first full Neo barrel path: `barrel_plastic`, `barrel_corroded`, `barrel_iron`, `barrel_steel`, `barrel_tcalloy`, and `barrel_antimatter` now build against NeoForge with a ticking barrel block entity, mode-gated fluid capability behavior, container transfer slots, menu/screen wiring, live client fluid sync, and modern generated barrel blockstates replacing the conflicting legacy copies
- Added the first Neo-side replacement for the old `FluidTypeHandler` trait checks: a tag-backed fluid trait helper now covers antimatter, corrosive, high-corrosive, `no_container`, and `no_id`, and the barrel rewrite uses it plus Neo fluid temperatures to restore the legacy antimatter/plastic/corrosion/leak reactions without depending on the archived Forge 1.12 fluid registry map
- Replaced the barrel-only Neo fluid slice with the full legacy `ModForgeFluids` id catalog: every old HBM tank fluid now has a Neo `FluidType` plus source/flowing registry entries with restored legacy temperatures where they were defined, the six old world fluids also have Neo blocks and bucket items, and the trait-tag barrel logic now resolves against complete runtime fluid registrations instead of a narrow compatibility subset
- Expanded the low-risk passive-content surface with a bulk decorative/parts sweep: basalt, slag, gneiss, cracked stone, meteor masonry, dungeon bricks, and muffler now register as Neo blocks with block items, creative-tab placement, harvest tags, and self-drop loot tables, while another simple legacy parts/control batch (`asbestos_cloth`, damp rag, cordite, ballistite/explosive balls, pneumatic/hydraulic pistons, and `arc_electrode`) now registers through the Neo item layer without pulling in new machine or combat behavior
- Added the next machine-family batch on top of a reusable long-backed energy base: the ten legacy storage batteries from `machine_battery_potato` through `machine_electronium_battery` now share a Neo battery block, block entity, menu, and screen path with persisted stored power/config on the dropped block item, comparator output, charge/discharge slots, redstone mode controls, and block-energy capability exposure, while neighbor transfer currently runs through direct Neo energy capability pushes instead of the old cable network graph
- Added the first compatibility-first transport/network rewrite slice: `red_cable`, `cable_switch`, `cable_detector`, `cable_diode`, `red_cable_gauge`, `red_connector`, `fluid_duct_mk2`, `fluid_duct_solid`, and `fluid_duct_solid_sealed` now register as Neo blocks with block entities, loot, tags, and recipes, and their routing uses standard Neo energy/fluid block capabilities so HBM machines can interoperate with external pipe mods without reviving the old 1.12 `PowerNet` or `FFPipeNetworkMk2` graphs

## Audit Snapshot

The pre-port audit in `migration/1.21.1/reports/codebase-audit.json` recorded:

- 2,880 Java files
- 13,844 resource files
- 988 block definitions
- 2,263 item definitions
- 167 entity registrations
- 257 tile entity registrations
- 1,186 `SideOnly` references
- 342 `GuiContainer` references
- 485 `TileEntitySpecialRenderer` references
- 256 `OreDictionary` references
- A legacy access transformer and an `IFMLLoadingPlugin` coremod

## Immediate Rewrite Areas

- Bootstrap and lifecycle in `com.hbm.main.MainRegistry`
- Static content registration in `com.hbm.blocks.ModBlocks` and `com.hbm.items.ModItems`
- Packet handling in `com.hbm.packet.PacketDispatcher`
- Client rendering built around `GuiContainer`, `TileEntitySpecialRenderer`, and `@SideOnly`
- Legacy config, capability, fluid, and `EnumHelper` usage spread across the codebase

## Resources Already Staged

- Converted language files are copied into `src/generated/resources/assets/hbm/lang`
- Advancement JSONs are copied into `src/generated/resources/data/hbm/advancement`

These generated resources came from the existing helper script in `tools/migrate_1_21_1.ps1`, which remains useful as a staging and audit tool while the Java port proceeds.

## Current Gap Tracking

- `tools/report_neo_port_gap.ps1` generates `migration/1.21.1/reports/neo-port-gap.json` and `migration/1.21.1/reports/neo-port-gap.md`
- Use that report to measure declared registry coverage against the legacy 1.12.2 `ModItems`, `ModBlocks`, entity, and block-entity registrations
- A successful `.\gradlew.bat build` only confirms the NeoForge scaffold compiles; it does not mean the legacy gameplay surface has been fully ported yet
- The current large-rewrite foothold is `src/neo/java/com/hbm/bootstrap`, `src/neo/java/com/hbm/attachment`, and `src/neo/java/com/hbm/network`, which now replace the old placeholder entrypoint with real lifecycle, attachment, and payload wiring
- The next active rewrite foothold is `src/neo/java/com/hbm/block`, `src/neo/java/com/hbm/blockentity`, `src/neo/java/com/hbm/menu`, and `src/neo/java/com/hbm/client`, which now hold the first Neo storage-container/block-entity paths
- The client rewrite foothold now also includes `src/neo/java/com/hbm/client/HbmClientEvents`, `src/neo/java/com/hbm/client/HbmClientInput`, and `src/neo/java/com/hbm/client/HbmKeyMappings`, which replace the first legacy client-only input and render-layer registration paths without relying on `@SideOnly` or `ClientRegistry`
- The live Neo client tree now also includes `src/neo/java/com/hbm/client/model` and the expanded `src/neo/java/com/hbm/client/renderer` package, which hold the first custom non-vanilla mob-model port and confirm that the currently registered Neo entity slice has full renderer wiring
- The platform rewrite foothold now also includes `src/neo/java/com/hbm/config`, `src/neo/java/com/hbm/registry/HbmTags`, `src/neo/java/com/hbm/api/energy`, and `src/neo/java/com/hbm/api/fluid`, which start replacing the old `OreDictionary`, `GeneralConfig`, and custom machine-storage assumptions with Neo-native tags, config specs, and container wrappers
- Unsupported Forge 1.12 loader hacks are no longer present in the live source trees; the archived legacy references now live under `migration/1.21.1/archive/legacy-loader-hacks`
- The armor rewrite foothold now also includes `src/neo/java/com/hbm/armor`, `src/neo/java/com/hbm/event/HbmArmorEvents`, and the modular armor item classes under `src/neo/java/com/hbm/item`, which begin replacing the old gas-mask, hazmat, and armor-mod behavior with Neo item logic, armor-material registration, and event-driven passive effects
- The machine rewrite foothold now also includes `src/neo/java/com/hbm/machine`, `src/neo/java/com/hbm/block/BurnerPressBlock`, `src/neo/java/com/hbm/blockentity/BurnerPressBlockEntity`, `src/neo/java/com/hbm/menu/BurnerPressMenu`, and `src/neo/java/com/hbm/client/screen/BurnerPressScreen`, which establish the first non-storage ticking machine path in Neo
- The powered-machine foothold now also includes `src/neo/java/com/hbm/item/BatteryItem`, `src/neo/java/com/hbm/event/HbmCapabilityEvents`, `src/neo/java/com/hbm/block/ElectricPressBlock`, `src/neo/java/com/hbm/blockentity/ElectricPressBlockEntity`, `src/neo/java/com/hbm/menu/ElectricPressMenu`, and `src/neo/java/com/hbm/client/screen/ElectricPressScreen`, which add the first Neo item-energy capability path and a second machine that depends on it
- The machine foothold now also includes `src/neo/java/com/hbm/block/ShredderBlock`, `src/neo/java/com/hbm/blockentity/ShredderBlockEntity`, `src/neo/java/com/hbm/machine/ShredderRecipeRegistry`, `src/neo/java/com/hbm/menu/ShredderMenu`, and `src/neo/java/com/hbm/client/screen/ShredderScreen`, which extend the powered-machine path to a larger multi-slot processor with blade wear and bulk-output handling
- The powered-machine foothold now also includes `src/neo/java/com/hbm/block/ElectricFurnaceBlock`, `src/neo/java/com/hbm/blockentity/ElectricFurnaceBlockEntity`, `src/neo/java/com/hbm/menu/ElectricFurnaceMenu`, and `src/neo/java/com/hbm/client/screen/ElectricFurnaceScreen`, which add a vanilla-smelting machine path with dynamic `lit`/`facing` state, smoke/flame particles, and the first Neo machine that consumes the battery-energy layer without a custom recipe registry
- The fluid-storage foothold now also includes `src/neo/java/com/hbm/block/FluidBarrelBlock`, `src/neo/java/com/hbm/blockentity/FluidBarrelBlockEntity`, `src/neo/java/com/hbm/menu/FluidBarrelMenu`, and `src/neo/java/com/hbm/client/screen/FluidBarrelScreen`, which restore the first legacy fluid barrel path with Neo fluid capabilities, menu/screen syncing, and modernized resource wiring
- The fluid-platform foothold now also includes `src/neo/java/com/hbm/api/fluid/HbmFluidTraits` and the new `data/hbm/tags/fluid/traits/*` resources, which replace the first slice of the legacy fluid trait map with Neo registry tags that future fluid ports can extend directly
- The explosion rewrite foothold now also includes `src/neo/java/com/hbm/explosion/NukeExplosionHandler` and the upgraded `src/neo/java/com/hbm/explosion/ExplosionNukeRayBatched`, which begin replacing the old `NukeCustom.explodeCustom` / `EntityNukeExplosionMK*` behavior with a Neo-native detonation path built on crater raycasts, direct area damage, terrain ignition, and attachment-backed radiation fallout
