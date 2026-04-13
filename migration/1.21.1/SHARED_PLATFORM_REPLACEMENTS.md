# Shared Platform Replacements

Established: 2026-04-09
Related baseline: `migration/1.21.1/PROPER_PORT_BASELINE.md`
Platform conventions: `migration/1.21.1/PLATFORM_CONVENTIONS.md`

## Purpose

This milestone covers the cross-cutting replacements that new NeoForge content should build on instead of recreating old 1.12.2 patterns piecemeal.

The goal is not to mirror legacy internals exactly. The goal is to replace them with modern, stable NeoForge-native interfaces so new content no longer needs direct references to legacy idioms such as `OreDictionary`, old config access, or ad-hoc capability patterns.

## Audit Inputs

The current audit still reports significant legacy platform surface in the excluded 1.12.2 tree:

- `oreDictionaryUses`: 256
- `legacyConfigUses`: 11
- `capabilityInjectUses`: 4
- `sideOnlyUses`: 1186
- `guiContainerUses`: 342

These counts come from `migration/1.21.1/reports/codebase-audit.json` and define the cleanup pressure behind this milestone.

## Current Neo Platform Foothold

The current shared-platform foothold already exists in the live Neo tree:

- Config: `src/neo/java/com/hbm/config/HbmCommonConfig.java`
- Attachments: `src/neo/java/com/hbm/attachment/*` and `src/neo/java/com/hbm/registry/HbmAttachmentTypes.java`
- Data components: `src/neo/java/com/hbm/registry/HbmDataComponents.java`
- Energy helpers: `src/neo/java/com/hbm/api/energy/*`
- Fluid helpers: `src/neo/java/com/hbm/api/fluid/*`
- Capability registration: `src/neo/java/com/hbm/event/HbmCapabilityEvents.java`
- Common tags: `src/neo/java/com/hbm/registry/HbmTags.java`
- Registry aggregation: `src/neo/java/com/hbm/registry/HbmRegistries.java`
- Bootstrap wiring: `src/neo/java/com/hbm/bootstrap/HbmBootstrap.java`
- Common helpers already acting as compatibility layers: `src/neo/java/com/hbm/util/*`

This milestone formalizes how those packages should be used and what still needs to be replaced before large-scale content porting continues.

## Rules For New Neo Code

### Allowed Dependencies For New Content

New NeoForge gameplay code should prefer the shared layer instead of inventing per-feature infrastructure:

- `HbmCommonConfig` for common gameplay tuning.
- `HbmTags` for item/block tag lookups and common-material matching.
- `HbmAttachmentAccess` and `HbmAttachmentTypes` for player/living runtime state.
- `HbmDataComponents` for item-held persistent state.
- `HbmLongEnergyStorage` and `HbmEnergyHelper` for long-backed energy behavior.
- `HbmFluidTank` and `HbmFluidTraits` for shared fluid handling and trait checks.
- `HbmCapabilityEvents` registration patterns for item/block/block-entity capabilities.
- `HbmRegistries` registration conventions for mod registries.

### Disallowed Dependencies For New Content

New NeoForge code should not introduce fresh references to:

- `OreDictionary`
- legacy config classes such as `GeneralConfig`
- `@CapabilityInject`
- archived loader hacks or coremod-only mechanisms
- new direct dependencies on archived legacy runtime classes under `migration/1.21.1/archive/legacy-source-tree/src/main/java` unless the code is being actively rewritten and immediately moved into the Neo tree
- per-feature ad-hoc energy/fluid/attachment abstractions when a shared platform type already exists

### Allowed Compatibility Layers

Some legacy-facing behavior may remain encapsulated if it is intentionally acting as a compatibility boundary instead of leaking old patterns into new code:

- legacy tooltip translation and formatting helpers
- data migration adapters
- wrapper logic that exposes modern Neo capabilities while preserving player-facing semantics

The rule is simple: compatibility code may exist at the boundary, but leaf content should depend on the modern wrapper, not on the old concept directly.

## Milestone Checklist

### 1. Config Replacement

Status: Complete

- [x] A Neo common config root exists.
- [x] The first ported mob families already read from Neo config instead of the old 1.12 path.
- [x] Remaining legacy config consumers are inventoried and mapped to new config keys.
- [x] The project has a documented rule for what belongs in config versus what should be data-driven.
- [x] New content ports do not reach back into legacy config assumptions for default values or toggles.

### 2. Tag And Material Matching Replacement

Status: Complete

- [x] `HbmTags` exists and already provides common item/block tag helpers.
- [x] New recipes and machine lookups use tags or explicit registries instead of ore-dictionary-style matching.
- [x] Existing Neo recipe and machine code does not reintroduce material matching through hard-coded legacy-name conventions where tags should exist.
- [x] A clear mapping exists for the most common historical material families: ingots, plates, ores, and storage blocks.

### 3. Attachment And Persistent Runtime State

Status: Complete

- [x] Player and living attachments exist as the first shared state model.
- [x] Attachment access is centralized behind `HbmAttachmentAccess`.
- [x] New runtime gameplay state is added through attachments or data components by default, not through ad-hoc entity fields plus custom sync.
- [x] Attachment copy, save, load, login, respawn, and tracking behavior is documented for future ports.
- [x] Leaf gameplay code no longer needs to know about legacy capability families.

### 4. Data Component Conventions

Status: Complete

- [x] Shared item data components exist for energy, radiation, armor mods, and gas-mask filters.
- [x] New item-held state uses shared data-component conventions before custom NBT conventions are added.
- [x] The project has a documented rule for when to use data components versus block-entity state versus attachments.
- [x] Common serialization and tooltip/readout helpers exist where repeated item-state presentation is needed.

### 5. Energy Platform Replacement

Status: Complete

- [x] Shared long-backed energy storage exists.
- [x] Shared charge/discharge/routing helpers exist.
- [x] Neo capability registration exists for the first energy-bearing items and block entities.
- [x] New powered machines use the shared energy layer instead of custom one-off storage code.
- [x] Any remaining old machine-energy assumptions are expressed through shared helpers or shared base classes.
- [x] A final decision exists for whether the capability-based transport model is the long-term replacement for legacy power graphs.

### 6. Fluid Platform Replacement

Status: Complete

- [x] Shared fluid tank wrapper exists.
- [x] Shared fluid trait tags exist.
- [x] Neo fluid capability registration exists for the first fluid block entities.
- [x] New fluid machines and containers use the shared fluid layer instead of feature-local tank implementations.
- [x] Shared rules exist for corrosive, antimatter, hot-fluid, and container restrictions so that fluid behavior does not drift by feature.
- [x] Final fluid transport expectations are documented for ducts, storage, and mod interoperability.

### 7. Capability Registration Conventions

Status: Complete

- [x] Capability registration is centralized in `HbmCapabilityEvents`.
- [x] New item and block-entity capabilities follow the same event-driven pattern instead of open-coded registration logic spread across features.
- [x] Capability exposure rules are documented for sided access, null-context handling, and wrapper lifetimes.
- [x] No new Neo code relies on old injected capability patterns.

### 8. Registry And Bootstrap Conventions

Status: Complete

- [x] Central registry aggregation exists.
- [x] Common bootstrap wiring exists for setup, capabilities, networking, and client hooks.
- [x] New registries follow the same package and aggregation conventions instead of adding hidden registration entrypoints.
- [x] The project has a stable rule for where new shared types belong: `registry`, `api`, `attachment`, `event`, `config`, or `util`.

### 9. Common Helper Consolidation

Status: Complete

- [x] Shared helpers already exist for hazards, effects, and legacy-to-modern tooltip formatting.
- [x] Cross-cutting helper logic that will be reused by multiple content families is moved into shared helpers before more leaf features copy it.
- [x] Compatibility helpers are clearly separated from final modern abstractions so future contributors know what to build on.
- [x] Repeated translation, radiation, hazard, or install/remove logic is not duplicated across ported content slices.

### 10. Porting Guardrails

Status: Complete

- [x] Contributors have a written rule that new Neo content must build on the shared platform layer first.
- [x] The parity baseline links here and treats this milestone as a release gate.
- [x] Future milestone docs reference this one before starting new content waves.

## Exit Criteria

This milestone is complete only when all of the following are true:

- New content ports do not need direct references to `OreDictionary`, legacy config classes, or old capability idioms.
- Shared config, tag, attachment, data-component, energy, and fluid primitives are stable enough that new content can be ported without inventing new cross-cutting infrastructure.
- Capability registration, registry aggregation, and bootstrap conventions are consistent and documented.
- Remaining compatibility-first decisions for energy and fluid transport are explicitly accepted or replaced.
- The common helper layer is acting as a platform boundary, not as a dumping ground for feature-specific hacks.

## Recommended Next Checks

When working this milestone, validate changes against these questions:

- Did the new code depend on a shared platform type rather than reintroducing a legacy concept?
- Did the change reduce future port cost for multiple features, or only solve one leaf feature?
- Did it remove a legacy idiom from the dependency graph, or just wrap it without changing how future code will be written?
- Would a contributor know where to plug the next machine, fluid container, armor system, or stateful item after reading this file?
