# Machine Porting Checklist

Established: 2026-04-12
Related milestone: `migration/1.21.1/MACHINE_BLOCKENTITY_FRAMEWORK.md`
Shared-platform prerequisite: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`

## Required Building Blocks

Port new machines from these shared layers first:

- block entities:
  - `src/neo/java/com/hbm/blockentity/AbstractMachineBlockEntity.java`
  - `src/neo/java/com/hbm/blockentity/AbstractEnergyMachineBlockEntity.java`
  - `src/neo/java/com/hbm/blockentity/AbstractLongEnergyMachineBlockEntity.java`
  - `src/neo/java/com/hbm/blockentity/AbstractFluidMachineBlockEntity.java`
  - `src/neo/java/com/hbm/blockentity/AbstractProcessorMachineBlockEntity.java`
- blocks:
  - `src/neo/java/com/hbm/block/AbstractMachineBlock.java`
  - `src/neo/java/com/hbm/block/AbstractFacingMachineBlock.java`
  - `src/neo/java/com/hbm/block/AbstractLitFacingMachineBlock.java`
- menus:
  - `src/neo/java/com/hbm/menu/AbstractMachineMenu.java`
  - `src/neo/java/com/hbm/menu/AbstractPressMenu.java`
  - `src/neo/java/com/hbm/menu/slot/*`
- screens:
  - `src/neo/java/com/hbm/client/screen/AbstractMachineScreen.java`
- sync:
  - `src/neo/java/com/hbm/blockentity/MachineStateTracker.java`
  - `src/neo/java/com/hbm/client/state/HbmClientState.java`

If a new machine does not fit these bases, document the missing pattern first and add the new framework piece before writing the concrete machine.

## State Ownership Rules

Use machine state channels consistently:

- `ContainerData`:
  - use for menu-visible numeric state such as power, progress, burn time, mode ids, or capacity numbers
  - this is the default path for server-to-open-menu sync
- custom payloads plus `HbmClientState`:
  - use only for world-space or renderer-facing state that must exist client-side outside an open menu
  - current examples are press render state and barrel fluid state
- server-only state:
  - use for derived or transient values that only affect server logic and never need client presentation

When a machine needs tracked payload sync, use `MachineStateTracker` with an immutable snapshot object instead of hand-rolled `lastSynced*` fields.

## Persistence Rules

Use the shared save keys and behaviors:

- common keys:
  - `power`
  - `machine_damage`
  - `progress`
  - `burn_time`
  - `max_burn_time`
  - `tank`
  - `capacity`
  - `mode`
- transient processor progress belongs in the block entity and resets only when the machine logic says so
- storage-like machines may persist configuration or stored energy onto dropped items when player expectation depends on it
- processor machines should usually drop contents and reset transient work state when broken unless the legacy design explicitly preserved more

Release-grade save/load and reload validation still belongs to `migration/1.21.1/PROPER_PORT_BASELINE.md` section 10.

## Redstone And Comparator Rules

Current shared semantics:

- processors only observe simple redstone pause when their block entity overrides `isMachineEnabled()`
- if a machine did not have clear legacy redstone gating, do not invent one during the port
- storage and energy buffers expose comparator output from stored fill ratio when player automation depends on it
- processor machines do not gain comparator output by default; only port it when the legacy behavior or gameplay contract requires it
- future mode-based redstone controls should be exposed through shared menu or screen controls, not bespoke booleans hidden in each machine

## IO And Automation Rules

Current shared conventions:

- energy-backed machines reuse `chargeFromBatterySlot(...)` and long-energy machines reuse `chargeItemFromStorage(...)`
- fluid container machines reuse `drainContainerIntoTank(...)` and `fillContainerFromTank(...)`
- slot acceptance belongs in shared slot types or slot predicates, not duplicated `Slot` subclasses inside each menu
- if a machine has no explicit item automation surface yet, keep that behavior closed and document it instead of adding half-ported sided inventory rules

## UI Rules

Use the menu and screen framework instead of rebuilding UI scaffolding:

- menus should declare slot maps, data counts, and routing rules on top of `AbstractMachineMenu`
- screens should use `AbstractMachineScreen` helpers for background draw, bars, hover regions, and button hitboxes
- common slot roles should use `BatteryInputSlot`, `FuelSlot`, `FluidContainerSlot`, `OutputSlot`, or `FilteredSlot`

## Porting Steps

For each new machine family:

1. Choose the correct shared block-entity base.
2. Reuse the shared machine block base that matches the machine state model.
3. Register slots through shared slot helpers.
4. Put menu-visible numbers in `ContainerData`.
5. Add tracked payload sync only if a renderer needs state outside the menu.
6. Reuse shared screen helpers for bars, icons, and click regions.
7. Document any deviation from the shared rules in the milestone file before merging.
