# Proper Port Baseline

Established: 2026-04-09
Target: NeoForge 1.21.1 port of HBM's Nuclear Tech

## Purpose

This document defines what "proper port" means for this repository.

A clean `.\gradlew.bat build` is necessary, but it is not sufficient. A proper port means the NeoForge codebase reproduces the intended gameplay surface of the 1.12.2 reference build unless a difference is explicitly accepted here as an intentional cut or compatibility-first replacement.

## Definition Of Done

The port is considered proper only when all of the following are true:

- The live implementation exists under `src/neo/java` and the live resource paths included by the NeoForge build, including `src/main/resources` and `src/generated/resources` where applicable.
- The live source layout no longer depends on a legacy `src/main/java` tree for ordinary feature work; any preserved 1.12.2 Java snapshot is archived under `migration/1.21.1/archive/legacy-source-tree/src/main/java`.
- Every legacy registry entry is either:
  - ported with working behavior, or
  - explicitly listed as an accepted cut in this document.
- Player-visible systems behave correctly in singleplayer and multiplayer.
- Save/load, menu sync, entity sync, and worldgen all work without relying on legacy runtime assumptions.
- Open "deferred", "interim", or "compatibility-first" notes are either resolved or given an explicit final decision below.

## Decision Rules

### Default Rule

Parity is the default. If a feature existed in the 1.12.2 reference mod, the NeoForge port should be expected to provide it unless this document says otherwise.

### What May Stay Compatibility-First

The following kinds of changes may remain in the final port if player-facing behavior is preserved or improved:

- Replacing legacy internals with NeoForge-native systems such as attachments, tags, data components, config specs, or capability-based IO.
- Replacing old registration, metadata, or OreDictionary-era plumbing with modern registries and tags.
- Removing legacy loader hacks, coremod behavior, and access transformer assumptions that are not part of gameplay.
- Replacing backend-only transport implementation details if the resulting throughput, automation, redstone behavior, menu behavior, and interoperability are documented and acceptable.

### What Cannot Be Hand-Waved As Compatibility-First

The following are release-blocking unless explicitly accepted as cuts:

- Missing items, blocks, entities, block entities, menus, recipes, or worldgen.
- Registered content with missing custom behavior.
- Missing GUIs, overlays, renderers, model layers, or client effects required for the feature to function.
- Missing multiplayer sync or save/load parity.
- Major behavior reductions hidden behind "temporary" or "interim" implementations.

### Rules For Accepting An Intentional Cut

An intentional cut is only valid if all of the following are recorded:

- The exact feature or behavior being cut.
- Why it is being cut.
- Player-facing impact.
- Whether there is a replacement behavior.
- Whether the cut is final or only accepted for a milestone branch.

If a cut is not listed here, it is not accepted.

## Release-Blocking Checklist

Use this as the tracked parity checklist for major subsystems.

### 1. Build, Bootstrap, And Registry Coverage

Status: Partial

- [x] NeoForge is the primary build target.
- [x] Legacy coremod and access-transformer hacks are archived out of the live source set.
- [ ] The NeoForge source set contains the complete runtime implementation for the released mod.
- [x] The legacy Java tree has been archived out of the live source layout and is no longer an active reference path for normal port work.
- [ ] `migration/1.21.1/reports/neo-port-gap.md` shows no unresolved registry gaps other than explicitly accepted cuts.

### 2. Shared Platform Systems

Status: Complete

Detailed milestone checklist: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`

- [x] Modern bootstrap, registry wiring, and common config scaffolding exist.
- [x] Modern attachment-backed replacements exist for the first legacy capability families.
- [x] Modern fluid and energy wrapper layers exist.
- [x] Remaining legacy config assumptions are removed or mapped to Neo config.
- [x] Remaining OreDictionary-era logic is replaced with tags or modern recipe logic.
- [x] Remaining legacy helper systems that new features still depend on are ported or retired.

### 3. Items, Blocks, And Creative Surface

Status: Partial

Detailed milestone checklist: `migration/1.21.1/BULK_ITEM_BLOCK_REGISTRY_COMPLETION.md`

- [x] The NeoForge item and block registries cover an initial live slice.
- [ ] All released items are registered in NeoForge.
- [ ] All released blocks are registered in NeoForge.
- [ ] Creative tabs expose the intended live content instead of placeholder-only coverage.
- [ ] Registration-level coverage matches behavior-level coverage.

### 4. Machines, Block Entities, Menus, And Screens

Status: Partial

Detailed milestone checklist: `migration/1.21.1/MACHINE_BLOCKENTITY_FRAMEWORK.md`
Porting checklist: `migration/1.21.1/MACHINE_PORTING_CHECKLIST.md`

- [x] The first storage and machine vertical slices are live in NeoForge.
- [x] The reusable machine, block-entity, menu, and screen framework is complete for the currently ported Neo machine families.
- [ ] Every legacy machine family needed for the released mod has a Neo block, block entity, menu, screen, persistence, sync path, and automation behavior.
- [ ] Machine inventories, fluids, energy, redstone, comparator output, and upgrade behavior match the intended reference behavior.
- [ ] No machine is considered complete while using placeholder behavior that materially changes throughput, automation, or progression.

### 5. Client Rendering, HUD, And Input

Status: Partial

Detailed milestone checklist: `migration/1.21.1/CLIENT_REWRITE.md`

- [x] The first client event, key mapping, and renderer rewrites are live.
- [ ] Remaining legacy `GuiContainer`, client-only registration, and renderer paths are replaced.
- [x] All currently ported entities, machines, overlays, and item visuals have complete client coverage.
- [ ] Missing particles, shader-like effects, special render layers, and HUD elements are either ported or explicitly cut.

### 6. Entities, Projectiles, AI, And Explosions

Status: Partial

- [x] A first hostile-mob and projectile slice exists.
- [x] A first Neo-native nuke detonation path exists.
- [x] The current hostile slice has live spawn placements, biome spawn modifiers, and entity loot/progression data instead of placeholder-only registration.
- [ ] All released entity registrations are ported.
- [ ] AI, spawning, persistence, tracking, damage, effects, and renderer behavior match the intended reference behavior.
- [ ] Large-effect entities such as clouds, special projectiles, vehicles, and exotic hazards are validated in multiplayer.

### 7. Fluids, Transport, And Automation

Status: Partial

Detailed decision and checklist: `migration/1.21.1/TRANSPORT_NETWORK_DECISION.md`

- [x] Neo fluid registrations and barrel/duct foundations exist.
- [x] Neo energy and fluid capability interoperability exists for the first machine families.
- [x] Final transport semantics are decided for power and fluid networks.
- [ ] Cable, connector, duct, and machine interaction behavior is stable enough that later content will not need to be re-ported.
- [x] Any final capability-first transport replacement is documented and accepted in the ledger below.

### 8. Armor, Consumables, Status Effects, And Player Systems

Status: Partial

- [x] The first medical, gas-mask, armor, and armor-mod behavior slices are live.
- [ ] All released consumables with custom gameplay have real Neo behavior.
- [ ] Radiation resistance, hazard handling, armor passive effects, and install/remove flows match the intended design.
- [ ] No registered item ships with behavior still marked as deferred unless it is explicitly accepted below.

### 9. Recipes, Loot, Tags, Advancements, And Progression

Status: Partial

- [x] Language and advancement resources were staged into modern paths.
- [x] Common tags and some compaction/data resources exist.
- [x] The current hostile progression slice has live mob loot tables and a real nuclear-creeper kill advancement trigger.
- [ ] Machine recipes, crafting recipes, loot, and tags cover the released feature surface.
- [ ] Progression-critical data is complete enough for a fresh world to progress normally.
- [ ] Data generation or hand-authored data covers future maintenance needs.

### 10. Worldgen And Structures

Status: Partial

- [x] A small Neo worldgen foothold exists.
- [x] The current ore and oil foothold is wired through configured features, placed features, and biome modifiers.
- [ ] Ores, deposits, fluids, structures, and biome integration match the intended world progression.
- [ ] Worldgen uses modern configured/placed feature and biome wiring without hidden legacy assumptions.
- [ ] Fresh-world testing confirms that progression resources generate in practical quantities.

### 11. Multiplayer, Save Compatibility, And Migration Safety

Status: Open

- [ ] Menus, entities, attachments, machine state, and HUD-critical data are validated on a dedicated server.
- [ ] Chunk save/load, dropped item persistence, block entity persistence, and world reload behavior are validated.
- [ ] The project has an explicit statement on whether old 1.12.2 worlds are unsupported, partially supported, or migration-assisted.
- [ ] Any non-portable save assumptions are documented before release.

### 12. Validation And Sign-Off

Status: Open

- [ ] A repeatable smoke-test checklist exists for client, server, and new-world startup.
- [ ] Regression coverage exists for high-risk systems such as machines, fluids, explosions, armor mods, and radiation.
- [ ] Known parity breaks are tracked here instead of only inside narrative status notes.
- [ ] The port can be signed off without needing the old source tree open side-by-side for confidence.

## Deferred And Interim Behavior Ledger

No behavior described as deferred, interim, or compatibility-first is accepted by default. Each entry must be resolved or explicitly accepted here.

| ID | Area | Current State | Final Decision | Exit Requirement |
| --- | --- | --- | --- | --- |
| PP-001 | Plain consumables and support items | Some registered consumables/support items are present in NeoForge while their custom gameplay behavior remains deferred. | Schedule out unless each item is explicitly cut. | Implement the missing behavior for each affected item or list the exact cut and player impact. |
| PP-002 | Transformer charger behavior | Transformer chargers keep a direct held-battery/manual charging path in addition to standard Neo capability input. | Accepted final auxiliary behavior. | Keep it documented as a convenience path; do not treat it as a blocker for the final transport architecture. |
| PP-003 | Battery neighbor transfer | Battery blocks transfer power through adjacent Neo energy capability endpoints instead of the old cable graph. | Accepted final transport contract. | Future battery and storage ports should continue to expose and consume standard Neo energy block capabilities. |
| PP-004 | Energy and fluid transport | HBM transport stays capability-first and routes through standard Neo energy/fluid block capabilities instead of reviving `PowerNet` or `FFPipeNetworkMk2`. | Accepted final architecture. | Keep HBM cables and ducts interoperable with any adjacent modded endpoint that exposes the standard NeoForge block capabilities. |

## Accepted Cuts

As of 2026-04-09, there are no accepted final cuts recorded here.

If a feature is intentionally not being ported, add it here before treating the port as complete.

## Suggested Review Cadence

Update this document when one of the following happens:

- A major subsystem moves from partial to complete.
- A deferred or interim behavior is resolved.
- A final compatibility-first decision is made.
- A feature is intentionally cut.
- A new report reveals a parity gap that was previously hidden.
