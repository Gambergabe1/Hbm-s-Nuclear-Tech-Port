# Bulk Item And Block Registry Completion

Established: 2026-04-09
Related baseline: `migration/1.21.1/PROPER_PORT_BASELINE.md`
Shared-platform prerequisite: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`

## Purpose

This milestone covers the bulk registry completion pass for items and blocks after the shared substrate is stable enough to support it.

The point of this phase is to remove the huge declared-registry gap for plain content without mixing it together with machine rewrites, entity behavior, or high-risk gameplay items.

The bulk pass should happen in this order:

1. simple materials and decor
2. functional blocks with no UI
3. functional blocks with UI or block entities
4. high-behavior items last

## Why This Is A Separate Gate

The current gap report still shows large declared coverage holes:

- Items: 2,224 legacy declared, 703 covered, 1,521 missing, 31.6% coverage
- Blocks: 987 legacy declared, 238 covered, 749 missing, 24.1% coverage

These figures come from `migration/1.21.1/reports/neo-port-gap.md`.

This milestone is not about claiming gameplay parity from raw registration count alone. It is about clearing the low-risk and medium-risk content surface efficiently while preserving room for later behavior-heavy ports.

## Current Neo Registry Foothold

The live registry code already supports bulk-style porting:

- Item registry aggregation: `src/neo/java/com/hbm/registry/HbmItems.java`
- Block registry aggregation: `src/neo/java/com/hbm/registry/HbmBlocks.java`
- Creative tab wiring: `src/neo/java/com/hbm/registry/HbmCreativeTabs.java`
- Progress measurement: `migration/1.21.1/reports/neo-port-gap.md` and `migration/1.21.1/reports/neo-port-gap.json`

The registry helpers already imply a batching strategy:

- plain item helpers:
  - `registerSimplePartItem`
  - `registerSimpleControlItem`
  - `registerSimpleConsumableItem`
  - `registerLorePartItem`
  - `registerLoreItem`
  - `registerDurableControlItem`
  - `registerBatteryItem`
- plain block helpers:
  - `registerSimpleBlock`
  - `registerStoneBlock`
  - `registerMetalBlock`
  - `registerOreBlock`
  - `registerStairsBlock`
  - `registerSlabBlock`
  - `registerBarrel`
  - `registerBattery`

This means a large amount of registry completion can and should be done by porting content families into the existing helper model instead of treating every ID as a one-off.

## What Counts As "Bulk Registry Completion"

For this milestone, a content family counts as complete only when all of the following are true for that family:

- the item or block is registered in NeoForge
- the item or block has creative-tab placement
- the item or block has the required model/state/lang coverage already expected for the currently ported content surface
- block items, loot tables, and tags exist where applicable
- recipes exist if the content is meant to be craftable or machine-producible at this stage
- the family does not silently depend on unported custom behavior

For this milestone, registration-only stubs are not enough if they create a misleading sense of completion.

## Porting Order

### 1. Simple Materials And Decor

This wave should be the bulk of the next completion push.

Examples:

- ingots, billets, nuggets, powders, scraps, wastes, crystals, coils, assemblies, and similar plain items
- storage blocks, ores, and companion decorative blocks
- stairs and slabs that are purely geometry variants
- simple decorative construction families

Selection rule:

- choose content that fits the existing plain-item or plain-block helpers
- choose families that do not require a custom item class, block entity, menu, screen, or dedicated gameplay logic

Why first:

- these families remove the largest number of missing IDs per unit of implementation effort
- they improve recipes, tags, and creative coverage without forcing framework work

### 2. Functional Blocks With No UI

Examples:

- redstone-adjacent or utility blocks with state but no menu
- world blocks with custom interaction but no container path
- passive transport/control blocks that sit on the already-decided transport architecture

Selection rule:

- the block may need a custom block class
- it should not require a new block entity or custom screen

Why second:

- these blocks are still relatively cheap once the shared substrate is in place
- they expand the live feature surface without dragging in machine framework debt

### 3. Functional Blocks With UI Or Block Entities

Examples:

- storage variants not yet ported
- machines that fit the machine framework
- fluid or energy containers that need ticking state, persistence, and menus

Selection rule:

- do not start a broad machine wave until the machine/block-entity framework is ready enough to absorb it
- new ports in this phase should ideally validate and strengthen `MACHINE_BLOCKENTITY_FRAMEWORK.md`

Why third:

- this is where registry completion starts to overlap with framework risk
- doing it too early creates more bespoke machine code instead of reducing it

### 4. High-Behavior Items Last

Examples:

- weapons and ammo families
- armor or tool families with custom effects
- complex consumables
- deployment items that spawn entities or drive custom systems

Selection rule:

- if an item needs custom use behavior, special networking, special rendering, or system-level integration, it belongs here

Why last:

- these items inflate registry counts but are not low-risk
- porting them too early makes the gap report look better while leaving real behavior debt behind

## Batch Selection Rules

### Rule 1: Port Families, Not Random IDs

Do not cherry-pick isolated IDs just because they are easy.

Prefer a coherent family such as:

- an ore plus its storage block plus its ingot/plate/powder chain
- a decorative block family plus its slab/stair variants
- a whole plain-material batch for one creative tab

### Rule 2: Keep The Family On One Side Of The Risk Boundary

If most of a family is plain and a few members are behavior-heavy, split it deliberately:

- plain members now
- behavior-heavy members later

Do not block a large low-risk batch because a few siblings are complicated.

### Rule 3: Use Existing Helpers First

Before adding a new item or block class, check whether the family already fits the registry helpers in `HbmItems` or `HbmBlocks`.

If a new helper is needed, it should support a family, not a single ID.

### Rule 4: Keep Tabs And Data Honest

Do not register large batches without also keeping:

- creative tab exposure
- tags
- loot
- recipes
- language coverage

in sync with the registered slice.

### Rule 5: Do Not Let Registry Coverage Outrun Behavior Clarity

A plain item or plain block is safe to bulk-port.

A behavior-heavy item or block should not be bulk-ported just to move the coverage percentage.

## Milestone Checklist

### 1. Item Family Batching

Status: Partial

- [x] The Neo item registry already supports large plain-item batches.
- [ ] Remaining plain material families are inventoried into batchable groups.
- [ ] Remaining high-behavior item families are explicitly separated from plain-item batches.
- [ ] The next bulk item waves primarily use existing item helper patterns rather than one-off registrations.

### 2. Block Family Batching

Status: Partial

- [x] The Neo block registry already supports large plain-block batches.
- [x] The registry already supports stairs, slabs, barrels, ores, and batteries through helper patterns.
- [ ] Remaining decorative and structural block families are inventoried into batchable groups.
- [ ] Remaining functional block families are split into no-UI, UI/block-entity, and transport-adjacent phases.

### 3. Creative Tab Coverage

Status: Partial

- [x] Dynamic parts/control/consumable tab hooks already exist.
- [ ] Bulk-ported item families land in the intended tabs when registered.
- [ ] Bulk-ported block families are reflected in the appropriate building/machine tab surface.
- [ ] Creative tabs are used as a sanity check that the visible content surface matches the declared registry surface.

### 4. Data And Resource Companions

Status: Partial

- [ ] Block items, loot tables, tags, and recipes are kept in step with each bulk block wave.
- [ ] Item models, blockstates, and lang coverage are kept in step with each bulk item/block wave.
- [ ] A batch is not treated as complete if it only compiles but lacks the expected data-side coverage.

### 5. Gap-Driven Planning

Status: Partial

- [x] `neo-port-gap.md` already provides declared item/block coverage measurement.
- [ ] Missing item IDs are grouped into actionable families rather than worked one at a time.
- [ ] Missing block IDs are grouped into actionable families rather than worked one at a time.
- [ ] The gap report is regenerated after major bulk waves so the next family selection is evidence-based.

### 6. Risk Boundaries

Status: Open

- [ ] Plain item and decor waves stay focused on low-risk content.
- [ ] Functional no-UI blocks stay separated from block-entity/UI work.
- [ ] Functional UI/block-entity blocks stay aligned with the machine framework milestone.
- [ ] High-behavior items are intentionally deferred until their dependent systems are ready.

## Recommended Batch Order

If work starts immediately, the most efficient order is:

1. Continue plain materials and companion storage/ore families that already fit `registerSimplePartItem`, `registerLorePartItem`, `registerMetalBlock`, and `registerOreBlock`.
2. Continue decorative and structural block families that already fit `registerStoneBlock`, `registerStairsBlock`, and `registerSlabBlock`.
3. Continue passive functional blocks that need a custom block class but no UI.
4. Only then widen the machine/storage/block-entity surface further.
5. Keep weapons, ammo, armor-adjacent high-behavior items, and other system-heavy items for their later milestone.

## Exit Criteria

This milestone is complete only when all of the following are true:

- The gap report no longer shows huge missing slices for plain items and plain blocks.
- Most remaining missing item and block IDs are behavior-heavy families, not low-risk registry content.
- New bulk item/block waves are being added by family using shared registry helpers instead of ad hoc single-ID ports.
- Creative tabs, tags, loot, recipes, and basic resource coverage remain in sync with the registry surface being claimed as ported.
- The bulk registry pass is no longer blocked by missing shared substrate work.

## Recommended Next Checks

When working this milestone, validate each batch against these questions:

- Did this batch remove a coherent family from the gap report, or just make the percentage look slightly better?
- Did the batch stay within the intended risk level for its phase?
- Did the batch require a new framework concept, and if so, should that concept live in the shared substrate first?
- After this batch, is the next bulk family now obvious?
