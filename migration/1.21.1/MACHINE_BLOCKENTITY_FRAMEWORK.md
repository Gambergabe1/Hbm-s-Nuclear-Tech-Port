# Machine And Block-Entity Framework

Established: 2026-04-09
Related baseline: `migration/1.21.1/PROPER_PORT_BASELINE.md`
Shared-platform prerequisite: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`
Porting checklist: `migration/1.21.1/MACHINE_PORTING_CHECKLIST.md`

## Purpose

This milestone covers the reusable machine, block-entity, menu, and screen framework that future ports should build on.

The goal is not "a few machines happen to compile." The goal is that a new machine family mostly becomes:

- a block registration
- a block-entity registration
- a menu registration
- a screen registration
- a recipe or data hook
- thin machine-specific logic

It should not require a fresh rewrite of inventory persistence, progress loops, quick-move routing, payload tracking, fluid-container handling, or common GUI scaffolding.

## Audit Inputs

The underlying parity problem is still large:

- Neo block-entity registrations: 13
- Neo menu registrations: 10
- Legacy tile entity registrations in the audit: 257
- Legacy `GuiContainer` uses in the audit: 342

These figures still come from `migration/1.21.1/reports/neo-port-gap.md` and `migration/1.21.1/reports/codebase-audit.json`.

This milestone closes the framework gap, not the entire legacy machine backlog.

## Framework Outcome

The live Neo tree now has shared machine infrastructure across server, menu, screen, and block layers.

### Shared Block-Entity Bases

- `AbstractMachineBlockEntity`
  - owns inventory storage, common save/load, result merge helpers, and common validity checks
- `AbstractEnergyMachineBlockEntity`
  - adds int-backed energy storage and machine damage handling on top of the generic machine base
- `AbstractLongEnergyMachineBlockEntity`
  - adds long-backed energy storage on top of the generic machine base
- `AbstractFluidMachineBlockEntity`
  - owns tank persistence and reusable container fill/drain behavior
- `AbstractProcessorMachineBlockEntity`
  - owns progress persistence, `ContainerData`, powered tick flow, blocked-progress handling, and completion hooks

### Shared Machine Sync

- `MachineStateTracker`
  - standardizes server-side "sync only when snapshot changes" behavior
- `HbmClientState`
  - remains the single client cache for world-space machine payload state
- current tracked payload users:
  - press machine state
  - barrel fluid state

### Shared Menu Layer

- `AbstractMachineMenu`
  - owns player inventory layout, quick-move finalization, shared routing helpers, and stable constructor conventions
- `AbstractPressMenu`
  - owns common press slot layout and routing
- slot helpers under `src/neo/java/com/hbm/menu/slot/`
  - `BatteryInputSlot`
  - `FuelSlot`
  - `FluidContainerSlot`
  - `OutputSlot`
  - `FilteredSlot`

### Shared Screen Layer

- `AbstractMachineScreen`
  - owns texture draw, label draw, render flow, bar helpers, hover-region helpers, and click-hitbox helpers
- `AbstractStorageScreen`
  - remains the storage-specific base already in use

### Shared Block Layer

- `AbstractMachineBlock`
  - owns menu opening, drop-on-remove, and default machine interaction flow
- `AbstractFacingMachineBlock`
  - adds reusable horizontal facing behavior
- `AbstractLitFacingMachineBlock`
  - adds reusable lit-state processors on top of the facing base

## Machine Families Now On The Framework

The currently ported Neo machine families were migrated onto the shared framework:

- `BurnerPressBlockEntity`, `BurnerPressMenu`, `BurnerPressScreen`, `BurnerPressBlock`
- `ElectricPressBlockEntity`, `ElectricPressMenu`, `ElectricPressScreen`, `ElectricPressBlock`
- `ElectricFurnaceBlockEntity`, `ElectricFurnaceMenu`, `ElectricFurnaceScreen`, `ElectricFurnaceBlock`
- `ShredderBlockEntity`, `ShredderMenu`, `ShredderScreen`, `ShredderBlock`
- `FluidBarrelBlockEntity`, `FluidBarrelMenu`, `FluidBarrelScreen`

This is the framework proof point for the next machine waves. It does not mean the remaining legacy machine families are already ported.

## Conventions Locked By This Milestone

The following are now project conventions and should be treated as framework rules:

- machine inventories persist through `AbstractMachineBlockEntity` and its derivatives
- processor progress lives in `AbstractProcessorMachineBlockEntity`
- menu-visible numbers belong in `ContainerData`
- world-space renderer state belongs in typed payloads plus `HbmClientState`
- tracked payload sync uses `MachineStateTracker`, not per-machine `lastSynced*` fields
- player-facing machine menus extend `AbstractMachineMenu` or a more specific shared derivative
- player-facing machine screens extend `AbstractMachineScreen` unless they are pure storage screens
- machine slot roles should use shared slot helpers before creating a one-off slot class
- machine blocks should extend the shared machine block base that matches their state model

Detailed porting rules now live in `migration/1.21.1/MACHINE_PORTING_CHECKLIST.md`.

## Milestone Checklist

### 1. Block-Entity Base Classes

Status: Complete

- [x] Shared storage block-entity base exists.
- [x] Shared int-backed energy machine base exists.
- [x] Shared long-backed energy machine base exists.
- [x] A shared fluid-machine or fluid-container base exists for machines that combine inventory and tank behavior.
- [x] A shared processor-machine base exists for progress-driven recipe execution.
- [x] Common machine save/load hooks are sufficient that new machines only override machine-specific fields.

### 2. Machine State And Sync Model

Status: Complete

- [x] `ContainerData` is used for menu-visible machine state.
- [x] Typed custom payloads exist for machine-adjacent client state such as press and barrel rendering.
- [x] There is a documented rule for when machine state belongs in `ContainerData`, when it belongs in a custom payload, and when it should live entirely server-side.
- [x] Client-side machine state caches are standardized through `HbmClientState`.
- [x] Common tracked-state helpers exist so machines do not hand-roll last-synced fields for every world-space animation or renderer dependency.

### 3. Inventory And Slot Layout Patterns

Status: Complete

- [x] Shared grid-storage menu layout exists.
- [x] Shared slot helper types exist for common machine roles such as fuel, battery-in, input, output, and fluid-container slots.
- [x] Shared `quickMoveStack` helpers exist for common machine families.
- [x] Sided insertion/extraction rules are standardized where automation behavior matters.
- [x] New machines no longer need to hand-write most slot routing logic from scratch.

### 4. Energy And Fluid IO Integration

Status: Complete

- [x] Shared energy storage and capability helpers exist.
- [x] Shared fluid tank and trait helpers exist.
- [x] Shared machine base classes expose energy or fluid state with consistent lifetime rules.
- [x] Common battery charging and discharge slot behavior is reusable instead of being re-implemented per machine.
- [x] Common container-fill and container-drain behavior is reusable instead of being re-implemented per machine.
- [x] IO contracts for neighbor transfer, capability pull and push, and machine buffering are documented for future ports.

### 5. Menu Framework

Status: Complete

- [x] Storage menus already have a reusable base.
- [x] A reusable processor or energy-machine menu base exists.
- [x] Common button-adjacent menu plumbing follows one shared constructor and data-binding pattern.
- [x] Menu constructors and buffer-based client constructors follow one stable pattern across machines.
- [x] New machine menus mostly declare slot maps and data bindings instead of re-implementing the entire container class.

### 6. Screen Framework

Status: Complete

- [x] Storage screens already have a reusable base.
- [x] A reusable processor or energy-machine screen base exists.
- [x] Shared rendering helpers exist for vertical bars, horizontal bars, overlays, mode icons, and tooltips.
- [x] Screen-side button hitbox handling is standardized for common machine controls.
- [x] New machine screens mostly become texture coordinates and lightweight tooltip definitions rather than full custom classes.

### 7. Machine Block Patterns

Status: Complete

- [x] Some stateful machine blocks already cover facing, lit state, menu opening, and ticker wiring.
- [x] Some machine blocks already expose comparator behavior where needed.
- [x] A reusable machine-block base exists for common facing, menu, ticker, and drop behavior.
- [x] A reusable lit-machine-block variant exists for lit-state processors.
- [x] Comparator and redstone contracts are documented for current machine categories.
- [x] Stateful block item persistence rules are documented for machines that retain stored energy or other configuration when broken.

### 8. Persistence, Drop Behavior, And Reload Safety

Status: Complete For Framework

- [x] Storage and energy machine bases own the common save/load behavior.
- [x] Battery blocks already persist stored power and config onto the dropped item.
- [x] The project has a documented rule for which machines should persist state onto dropped items and which should drop plain contents only.
- [x] Save keys and machine-data conventions are stable enough that new ports do not invent random field names.
- [ ] Runtime reload safety validation remains tracked in `migration/1.21.1/PROPER_PORT_BASELINE.md` section 10.

### 9. Redstone And Comparator Semantics

Status: Complete

- [x] Several machines already observe neighbor redstone state.
- [x] Battery blocks already expose comparator output.
- [x] The project has a shared rule for redstone semantics.
- [x] Comparator semantics are defined for storage, energy, fluid, and processor machines.
- [x] Future redstone-mode controls are expected to build on shared UI patterns instead of per-machine ad hoc plumbing.

### 10. Recipe And Tick Framework

Status: Complete

- [x] The port already proves custom and vanilla recipe-backed machines can work.
- [x] Shared processor helpers exist for recipe lookup, output merge checks, progress accumulation, and craft completion.
- [x] Shared tick patterns exist for fueled machines, powered machines, and passive transfer machines.
- [x] Recipe invalidation and input-change handling are standardized enough for the currently ported machine families.
- [x] New processors mostly provide recipe resolution and per-tick cost rules, not the entire processing loop.

### 11. Validation And Porting Guardrails

Status: Complete

- [x] There is a written checklist for porting a new machine family onto the framework.
- [x] New machine ports are expected to extend shared bases first and only add new framework pieces when the pattern is clearly reusable.
- [x] The parity baseline treats this milestone as a release gate for the remaining machine surface.
- [x] Future machine waves now have a framework path that reduces bespoke UI and sync code instead of expanding it.

## Exit Condition

This milestone is complete as a framework milestone.

Remaining work is now mostly content parity work:

- porting additional legacy machine families onto the shared framework
- validating runtime save/load and dedicated-server behavior
- finishing recipe, automation, and progression parity for the remaining machine backlog
