# Transport And Network Decision

Established: 2026-04-09
Related baseline: `migration/1.21.1/PROPER_PORT_BASELINE.md`
Shared-platform prerequisite: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`

## Decision Summary

The NeoForge port will not recreate the old 1.12.2 `PowerNet` or `FFPipeNetworkMk2` graphs as the canonical transport architecture.

The final transport model for this port is capability-first:

- HBM energy cables and fluid ducts may still form HBM-local routing networks.
- Those networks will route through standard NeoForge block capabilities.
- HBM machines, storage blocks, cables, and ducts are expected to expose and consume the standard Neo capability contracts.
- Interoperability is a release requirement: HBM transport should work with any modded machine, pipe, cable, tank, battery, or wire that exposes the standard NeoForge block capabilities on its block faces.

## Final Architecture Decision

### 1. Canonical Energy Contract

The canonical energy interop contract is `Capabilities.EnergyStorage.BLOCK`.

HBM energy cables, chargers, batteries, and powered machines should treat block energy capabilities as the supported automation boundary.

### 2. Canonical Fluid Contract

The canonical fluid interop contract is `Capabilities.FluidHandler.BLOCK`.

HBM fluid ducts, barrels, tanks, and fluid machines should treat block fluid capabilities as the supported automation boundary.

### 3. HBM Transport Blocks Remain Useful

HBM cables and ducts are still valid gameplay content. They are not being removed.

What changes is the architectural role:

- They are no longer the foundation of a custom global transport API that everything else must understand.
- They are HBM-local routing blocks that bridge adjacent compatible capability endpoints.
- They may keep HBM-specific behaviors such as switches, diodes, gauges, extraction toggles, or fluid-locking rules as long as those behaviors operate on top of the capability model.

### 4. Interoperability Requirement

The supported interop target is explicit:

- If another mod exposes NeoForge block energy or fluid capabilities on block faces, HBM transport should be expected to interact with it.
- If HBM exposes those same capabilities on its blocks, outside mods should be able to interact with HBM.

This is the meaning of "cables and pipes and such work with any pipe or power wires" for the Neo port.

### 5. Scope Boundary

This decision does not promise compatibility with mods that only expose custom transport APIs and do not expose NeoForge block capabilities.

The supported contract is the standard NeoForge capability surface, not every mod's internal graph implementation.

## Why This Is The Final Decision

The live Neo implementation already points in this direction:

- `EnergyCableBlockEntity` scans contiguous HBM cable members and routes to adjacent `Capabilities.EnergyStorage.BLOCK` endpoints.
- `FluidDuctBlockEntity` scans contiguous HBM duct members and routes to adjacent `Capabilities.FluidHandler.BLOCK` endpoints.
- `BatteryStorageBlockEntity` already pushes power to adjacent Neo energy-capability endpoints.
- `TransformerChargerBlockEntity` already exposes Neo block energy capability input.
- `HbmCapabilityEvents` already registers HBM machine and transport capability exposure through NeoForge events.

Recreating the old 1.12.2 custom graph as a second transport contract would:

- increase future port cost
- reduce cross-mod interoperability
- create two automation models to maintain
- force more machine rewrites later

## Current Implementation Notes

The current capability-first implementation is already substantial enough to be the final direction:

- Energy cable routing:
  - contiguous HBM cable members are scanned
  - external capability endpoints are collected from adjacent non-cable blocks
  - energy is distributed using the shared energy helper and routing priorities
- Fluid duct routing:
  - contiguous HBM duct members are scanned
  - external capability endpoints are collected from adjacent non-duct blocks
  - fluid is distributed across capability endpoints with fluid-locking and extraction behavior
- Battery blocks:
  - continue to push to adjacent Neo energy-capability targets
  - do not require resurrection of an old cable graph to participate in automation
- Transformer chargers:
  - keep their direct held-item/manual charging path as a supported convenience feature
  - use Neo block energy capability as the automation input path

## Stability Rules Going Forward

### 1. No New Custom Global Transport API

Do not introduce a new HBM-only transport API that machines must implement in parallel with Neo capabilities.

If HBM transport needs internal routing helpers, keep them behind the cable/duct implementation, not as a second public automation contract.

### 2. All HBM Automation Blocks Must Expose Standard Capabilities

New HBM machines and storage blocks should expose:

- block energy capability when they store, consume, or provide energy
- block fluid capability when they store, consume, or provide fluids

This is mandatory for cross-mod automation compatibility.

### 3. HBM Cables And Ducts Must Treat Capability Endpoints As First-Class

HBM transport blocks should not special-case HBM endpoints as the only intended targets.

They may have HBM-specific priority or mode logic, but the routing layer must continue to accept external capability endpoints.

### 4. Machine Ports Must Not Depend On Legacy Graph Semantics

When porting future machines:

- do not assume membership in a legacy global cable graph
- do not add code that only works when connected to HBM transport blocks
- do not block standard capability-based interop

### 5. Special HBM Transport Features May Stay

The following kinds of features are still allowed on top of the capability-first model:

- cable switches
- diodes and routing priorities
- cable gauges and comparator output
- duct extraction toggles
- duct fluid-lock behavior

These are gameplay features, not reasons to revive the old transport architecture.

## Effects On Existing Ledger Items

This decision resolves the transport-related entries in the parity baseline:

- `PP-002`: transformer manual charge path is accepted as a final auxiliary convenience path
- `PP-003`: direct Neo capability neighbor transfer is accepted as the final battery transport contract
- `PP-004`: capability-first energy and fluid transport is accepted as the final architecture

## Exit Criteria

This decision is fully landed only when all of the following are true:

- The decision remains documented and linked from the baseline.
- Future machine ports expose standard Neo energy and fluid capabilities where appropriate.
- HBM cables and ducts continue to interoperate with external mods through Neo capability endpoints.
- No future milestone depends on reviving `PowerNet` or `FFPipeNetworkMk2`.
- Machine and cable behavior is stable enough that later ports do not require another transport rewrite.
