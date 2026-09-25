# HBM Nuclear Tech - Complete Port Assessment

## Session update (2026-09-25)

This environment's network policy denies every modding maven host tested
(`maven.neoforged.net`, `maven.latvian.dev`, etc - see `curl -sS
http://127.0.0.1:43167/__agentproxy/status` for the live list of recent
denials), so **nothing in this repository has been compiled or run in this
session** - the "Build Status: SUCCESSFUL" claim below is from an earlier
session and unverified here. To unblock: broaden this environment's network
access or add those hosts to its allowlist (environment settings -> Edit ->
Network access).

Work done this session (see git log on `claude/brave-carson-unvk0j`):
- Bulk-registered 290 previously-missing simple items (the `ItemBase`/
  `ItemCustomLore` legacy classes - flavor items with no unique behavior),
  bringing that specific category to 100% coverage. New `WEAPON`/`NUKE`
  dynamic creative-tab item lists added to support it.
- Converted the shredder and press machines from hardcoded Java recipe maps
  to proper data-driven `Recipe`/`RecipeSerializer`/`RecipeType`
  implementations (`hbm:shredding`, `hbm:pressing`), preserving all 132
  existing recipes as JSON under `data/hbm/recipe/`.
- Scaffolded native KubeJS support: a `compileOnly` Gradle dependency, a
  ServiceLoader-wired plugin entry point, and an optional mods.toml
  dependency. See `migration/1.21.1/KUBEJS_INTEGRATION.md` for exactly
  what's verified vs. still a documented TODO (recipe schema registration
  for the two custom recipe types above).
- A registry-level audit (comparing every `new <ItemClass>("id", ...)` in
  the legacy `ModItems.java`/`ModBlocks.java` against what's registered in
  `HbmItems`/`HbmBlocks`) found 1,093 items and 726 blocks missing at the
  start of this session. Follow-up bulk-registration passes (same treatment:
  existing block/item resources carried over, behavior tied to a not-yet-built
  system - radiation, fuel/energy mechanics - intentionally dropped and
  noted) cleared: 41 `BlockHazard` blocks, 3 `BlockRadResistant` blocks, 7
  `BlockRotatablePillar` blocks, 44 `ItemLemon` food items, and 10
  `ItemFoodBase` food items. **Current gap: 1,039 items / 675 blocks
  missing.** What's left is concentrated in systems that don't exist in the
  port yet at all: ammo/guns (136+46+32), missiles (62+26), grenades (41),
  armor sets (40), tools/weapons needing a custom `Tier` implementation
  (swords/hoes/shields), ore blocks needing loot tables *and* worldgen
  wiring, and multiblock structures (doors, centrifuge-style machines,
  pipes) - none of which are safe to bulk-register as inert placeholders the
  way the flavor/storage items and blocks above were. That's the honest
  shape of what's left; the original percentage estimates below likely
  undercount it in the opposite direction depending on which snapshot they're
  from.

## Current Status: Infrastructure Complete, Content Port ~10%

**Date**: April 12, 2026  
**Build Status**: ✅ SUCCESSFUL (as of that session - unverified since, see above)  
**Ported Content**: ~10-15% of total mod content  
**Remaining Content**: ~85-90%

---

## What IS Complete ✅

### Build & Infrastructure (100%)
- ✅ NeoGradle 7.1.21 build system
- ✅ NeoForge 1.21.1-21.1.222 target
- ✅ Java 21 toolchain
- ✅ Resource pipeline (JSON models, blockstates, recipes)
- ✅ Mod metadata (`neoforge.mods.toml`)

### Core Systems (100%)
- ✅ Registry framework (blocks, items, entities, block entities, menus)
- ✅ Network payload system
- ✅ Energy capability system (Forge Energy)
- ✅ Fluid capability system
- ✅ Entity attachment system
- ✅ Configuration system
- ✅ Tag system

### Partially Ported Content
- ✅ **100+ blocks** (out of 988 total = ~10%)
- ✅ **200+ items** (out of 2,263 total = ~9%)
- ✅ **15+ entities** (out of 167 total = ~9%)
- ✅ **20+ block entities** (out of 257 total = ~8%)
- ✅ **Basic machines**: Press, Electric Press, Shredder, Electric Furnace
- ✅ **Storage containers**: Iron/Steel/Desh Crates, Safe
- ✅ **Energy infrastructure**: Transformer Chargers, Energy Cables
- ✅ **Fluid storage**: Fluid Barrels (6 variants)
- ✅ **Armor system**: Modular armor, gas masks (partial)
- ✅ **Some consumables**: RadAway, Rad-X, pills

---

## What is NOT Ported ❌ (~85-90% of mod)

### 🔴 CRITICAL SYSTEMS (Core Gameplay)

#### 1. RBMK Reactor Multiblock System (0% ported)
**Legacy Files**: 120+ files in `tileentity/machine/rbmk/`, `blocks/machine/rbmk/`

**Missing Components**:
- **Blocks** (15+ types):
  - `RBMKBase` - Base class for all RBMK components
  - `RBMKBlank` - Blank fuel channel
  - `RBMKControl` / `RBMKControlAuto` - Control rod assemblies
  - `RBMKRod` - Fuel rod assemblies (multiple fuel types)
  - `RBMKReflector` - Neutron reflectors
  - `RBMKModerator` - Graphite moderators
  - `RBMKAbsorber` - Neutron absorbers
  - `RBMKBoiler` - Heat exchange boilers
  - `RBMKOutgasser` - Xenon outgassing system
  - `RBMKHeater` - Fuel rod heaters
  - `RBMKCooler` - Cooling systems
  - `RBMKStorage` - Control rod storage
  - `RBMKConsole` - Reactor control console
  - `RBMKInlet` / `RBMKOutlet` - Fluid input/output
  - `RBMKCraneConsole` - Fuel crane control

- **Tile Entities** (15+ types):
  - Complex neutron flux simulation
  - Heat transfer physics
  - Fuel depletion tracking
  - Xenon poisoning
  - Control rod positioning
  - Reactor stability calculations
  - Meltdown mechanics

- **Systems**:
  - Neutron flux propagation algorithm
  - Heat distribution network
  - Steam generation
  - Reactor physics simulation
  - Console UI with graphs and diagnostics
  - Fuel crane multiblock
  - Automatic control systems

**Complexity**: **EXTREME** - This is the single most complex system in the mod

**Estimated Effort**: 200-300 hours of development

---

#### 2. Other Multiblock Machines (0% ported)

**Chemical Plant**:
- Multi-block structure for chemical processing
- 20+ recipe types
- Fluid input/output
- Complex GUI with multiple tabs
- Estimated: 40-60 hours

**Blast Furnace**:
- Multi-block smelting
- Temperature mechanics
- Fuel system
- Estimated: 30-40 hours

**Centrifuge Array**:
- Isotope separation
- Multi-fluid output
- Energy intensive
- Estimated: 30-40 hours

**Heat Exchanger**:
- Thermodynamic simulation
- Fluid-to-fluid heat transfer
- Efficiency calculations
- Estimated: 20-30 hours

**Fluid Solidifier**:
- Fluid-to-block conversion
- Shape/mold system
- Cooling mechanics
- Estimated: 20-30 hours

---

#### 3. Radiation System (0% ported)

**Legacy Files**: 50+ files in `hazard/`, `potion/`, `physics/`

**Missing**:
- Radiation source tracking
- Contamination system
- Rad exposure effects
- Decontamination mechanics
- Shielding calculations
- Geiger counter functionality
- Dosimeter system
- Radiation-resistant armor
- Hot cell mechanics

**Complexity**: **HIGH**

**Estimated Effort**: 60-80 hours

---

#### 4. Power Generation (0% ported)

**Missing Systems**:
- **Steam Turbines**: Multi-block power generation
- **Generators**: Coal, gas, nuclear generators
- **Power Cables**: High-voltage power distribution (different from energy cables)
- **Transformers**: Voltage conversion
- **Power Storage**: Large-scale battery banks
- **Grid Management**: Power balancing

**Complexity**: **HIGH**

**Estimated Effort**: 80-100 hours

---

#### 5. Fluid Processing Network (0% ported)

**Missing**:
- **Pumps**: Fluid extraction from world
- **Pipe Networks**: Fluid distribution (separate from barrels)
- **Fluid Transformers**: Fluid conversion machines
- **Fluid Heaters**: Electric fluid heating
- **Fluid Mixers**: Fluid combination system
- **Fluid Tanks**: Large-scale fluid storage
- **Fluid Voids**: Fluid disposal

**Complexity**: **HIGH**

**Estimated Effort**: 60-80 hours

---

### 🟠 HIGH PRIORITY

#### 6. Weapon Systems (0% ported)

**Missiles** (50+ types):
- Nuclear warheads (various yields)
- Conventional explosives
- Chemical weapons
- Thermobaric weapons
- EMP weapons
- Antimatter weapons
- Guidance systems
- Launch infrastructure

**Guns** (20+ types):
- Pistols, rifles, SMGs
- Shotguns, sniper rifles
- Energy weapons
- Ammunition types

**Explosives**:
- TNT variants
- C4, Semtex
- Detonation cord
- Landmines
- Grenades (partially ported)

**Complexity**: **HIGH**

**Estimated Effort**: 100-150 hours

---

#### 7. World Generation (0% ported)

**Missing**:
- **Ore Generation**: All HBM ores (20+ types)
- **Depth Deposits**: Deep ore veins
- **Structures**: Bunkers, labs, facilities
- **Dimensions**: Custom dimensions (if any)
- **Biomes**: Custom biomes
- **Trees/Plants**: Custom flora

**Complexity**: **MEDIUM-HIGH**

**Estimated Effort**: 40-60 hours

---

#### 8. Remaining Blocks (~888 blocks, 0% ported)

**Categories**:
- Construction blocks (decorative, functional)
- Machine blocks (all machines listed above)
- RBMK components (15+ types)
- Fluid blocks (20+ fluids)
- Ore blocks (20+ ores + variants)
- Storage blocks (all ingot/block types)
- Hazard blocks (radiation, heat, chemical)
- Technical blocks (conduits, cables, pipes)
- Bomb/weapon blocks (missile parts, launch pads)

**Estimated Effort**: 150-200 hours

---

#### 9. Remaining Items (~2,063 items, 0% ported)

**Categories**:
- **Materials**: Ingots, plates, coils, circuits (100+ types)
- **Tools**: Wrenches, miners, scanners (30+ types)
- **Weapons**: Guns, ammo, missile parts (200+ types)
- **Armor**: Full armor sets, upgrades (100+ types)
- **Consumables**: Food, medicine, drugs (50+ types)
- **Components**: Machine parts, electronics (300+ types)
- **Fuel**: Nuclear fuel, chemical fuel (50+ types)
- **RBMK Parts**: Fuel rods, control rods, moderators (50+ types)
- **Missile Parts**: Warheads, guidance, casing (100+ types)

**Estimated Effort**: 200-300 hours

---

#### 10. Remaining Entities (~152 entities, 0% ported)

**Categories**:
- **Mobs**: Mutants, robots, animals (50+ types)
- **Projectiles**: Bullets, missiles, grenades (50+ types)
- **Vehicles**: Cars, helicopters, mechs (20+ types)
- **Effects**: Explosions, clouds, particles (30+ types)

**Estimated Effort**: 80-120 hours

---

### 🟡 MEDIUM PRIORITY

#### 11. Crafting Systems (0% ported)

**Missing**:
- Custom recipe serializers
- Machine recipe registries
- Crafting table recipes
- Smelting recipes
- Press recipes (partially done)
- Shredder recipes (partially done)
- Chemical plant recipes
- Centrifuge recipes
- RBMK fuel crafting
- Missile assembly

**Estimated Effort**: 40-60 hours

---

#### 12. GUI/Screens (10% ported)

**Ported**: 5-10 machine screens  
**Missing**: 50+ screens

**Missing**:
- RBMK console
- Chemical plant GUI
- All multiblock GUIs
- Weapon customization
- Missile programming
- Radiation monitors

**Estimated Effort**: 60-80 hours

---

#### 13. Particles & Effects (0% ported)

**Missing**:
- Radiation particles
- Explosion effects
- Steam/smoke
- Electrical arcs
- Heat shimmer
- Custom projectiles
- Mushroom clouds

**Estimated Effort**: 30-40 hours

---

### 🟢 LOW PRIORITY

#### 14. Decorative Blocks (0% ported)
- Aesthetic variants
- Lighting blocks
- Signage
- Furniture

**Estimated Effort**: 20-30 hours

---

#### 15. QoL Features (0% ported)
- JEI integration
- WAILA support
- Minimap compatibility
- Performance optimizations

**Estimated Effort**: 20-30 hours

---

## Total Estimated Effort

| Category | Hours | Priority |
|----------|-------|----------|
| RBMK Reactor | 200-300 | 🔴 CRITICAL |
| Other Multiblocks | 140-200 | 🔴 CRITICAL |
| Radiation System | 60-80 | 🔴 CRITICAL |
| Power Generation | 80-100 | 🔴 CRITICAL |
| Fluid Processing | 60-80 | 🔴 CRITICAL |
| Weapons | 100-150 | 🟠 HIGH |
| World Gen | 40-60 | 🟠 HIGH |
| Remaining Blocks | 150-200 | 🟠 HIGH |
| Remaining Items | 200-300 | 🟠 HIGH |
| Remaining Entities | 80-120 | 🟠 HIGH |
| Crafting | 40-60 | 🟡 MEDIUM |
| GUIs | 60-80 | 🟡 MEDIUM |
| Particles | 30-40 | 🟡 MEDIUM |
| Decorative | 20-30 | 🟢 LOW |
| QoL | 20-30 | 🟢 LOW |
| **TOTAL** | **1,280-1,830 hours** | |

---

## Realistic Timeline

### Single Developer (Full-Time, 40 hrs/week)
- **32-46 weeks** (8-11 months) of continuous work

### Single Developer (Part-Time, 20 hrs/week)
- **64-92 weeks** (16-23 months)

### Small Team (3 Developers, Full-Time)
- **11-15 weeks** (3-4 months)

### Small Team (3 Developers, Part-Time)
- **21-31 weeks** (5-8 months)

---

## Recommended Approach

### Phase 1: Core Systems (Months 1-2)
1. Radiation system
2. Basic power generation
3. Basic fluid processing
4. Crafting framework

### Phase 2: Machines (Months 3-4)
1. Chemical plant
2. Centrifuge
3. Blast furnace
4. Other single-block machines

### Phase 3: RBMK Reactor (Months 5-7)
1. Basic RBMK blocks
2. Tile entity framework
3. Neutron flux simulation
4. Heat transfer
5. Console UI
6. Testing & balancing

### Phase 4: Content (Months 8-10)
1. Remaining blocks
2. Remaining items
3. Remaining entities
4. World generation

### Phase 5: Weapons & Polish (Months 11-12)
1. Weapon systems
2. Missiles
3. Particles & effects
4. GUIs
5. Testing & bug fixes

---

## Conclusion

**The port INFRASTRUCTURE is 100% complete**, but the **CONTENT port is only ~10-15% done**.

This is a **MASSIVE** mod with thousands of items, hundreds of blocks, and extremely complex multiblock systems. The RBMK reactor alone is a multi-month project.

### Options:

1. **Complete Full Port** (12+ months for solo dev)
   - Port everything systematically
   - Requires massive time investment
   - Result: Full 1.21.1 HBM experience

2. **Prioritize Core Features** (3-4 months)
   - Port radiation, basic power, basic fluids
   - Port most-used machines
   - Skip weapons, decorative content
   - Result: Functional but incomplete experience

3. **Community Effort** (3-6 months with team)
   - Recruit 3-5 developers
   - Divide systems among team
   - Result: Full port in reasonable timeframe

4. **Release As-Is** (0 months)
   - Current state: 10-15% content
   - Document what's missing
   - Let community finish
   - Result: Foundation for others to build on

---

## Next Steps

If you want me to proceed with completing the port, I need to know:

1. **Scope**: Do you want EVERYTHING ported, or should I prioritize?
2. **Timeline**: What's your expected timeline?
3. **Resources**: Are there other developers who can help?
4. **Priority**: Which systems are most important to have first?

**My recommendation**: Start with Phase 1 (core systems) and evaluate progress before committing to the full port.
