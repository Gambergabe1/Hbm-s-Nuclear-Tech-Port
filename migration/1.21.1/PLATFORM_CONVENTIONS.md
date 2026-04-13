# Platform Conventions

Established: 2026-04-12
Related milestone: `migration/1.21.1/SHARED_PLATFORM_REPLACEMENTS.md`

## Purpose

This document defines the stable platform rules that future NeoForge ports should build on.

The goal is to stop future content waves from reopening old questions about where state belongs, when tags should replace hard-coded item matching, or how capabilities should be exposed.

## Config Versus Data

Use `HbmCommonConfig` only for server-operator tuning that is expected to vary by pack or deployment.

- Put mob timing, damage, radius, and similar gameplay knobs in config.
- Keep content identity, material membership, item families, and recipe matching data-driven through registries and tags.
- Do not add config keys for things that should be solved by tags, recipes, loot tables, blockstates, or item/block registries.
- Do not port legacy `GeneralConfig` defaults verbatim unless the resulting value is still a real operator-facing tuning knob in Neo.

## Tag And Material Rules

Material matching must use tags or explicit registry entries, never legacy name heuristics.

- Common material families map to `c:` tags through `HbmTags`: `ingots/<material>`, `plates/<material>`, `ores/<material>`, and `storage_blocks/<material>`.
- Mod-specific behavior groups belong in `hbm:` tags through `HbmTags.modItem` / `HbmTags.modBlock`.
- When a behavior depends on “which family this item belongs to,” add or reuse a tag first.
- When a behavior depends on “this exact item or block,” use an explicit registry reference.

## Runtime State Placement

Pick one state home and keep the responsibility narrow.

- Use attachments for runtime state that belongs to entities, players, or other living actors and must survive save/load plus multiplayer sync.
- Use data components for persistent item-held state such as stored energy, radiation metadata, armor-mod payloads, and gas-mask filters.
- Use block entities for placed-world state such as inventories, tanks, machine progress, redstone settings, and network-local caches.
- Do not add new ad-hoc global maps or hidden singleton state when an attachment, component, or block entity can own the data directly.

## Attachment Lifecycle

The live attachment model is:

- Registration in `HbmAttachmentTypes`.
- Access through `HbmAttachmentAccess`.
- Save/load handled by serializable Neo attachments.
- Player respawn copy handled by the attachment copy handler in `HbmAttachmentTypes.PLAYER_DATA`.
- Login, respawn, and tracking sync handled through `HbmNetworkEvents`.

New gameplay state that needs persistence and multiplayer visibility should follow that path instead of recreating the old capability families.

## Data-Component Conventions

Use shared helpers before inventing per-feature serialization.

- Persistent compound payloads should go through shared component helpers such as `HbmItemComponentUtil`.
- Tooltip readouts for shared item state should live alongside the shared helper or the item type that owns the component contract.
- If two features need the same item snapshot or compound-component behavior, move it into the shared layer immediately.

## Energy And Fluid Rules

The canonical machine platform is capability-first.

- Powered machines should inherit the shared energy base classes and expose cached capability views from the shared energy storage wrappers.
- Fluid containers and machines should use `HbmFluidTank` and `HbmFluidTraits` instead of local tank abstractions or duplicated fluid-trait checks.
- Container compatibility rules for antimatter, corrosive fluids, and hot fluids belong in `HbmFluidTraits`.
- Direct held-battery charging is only acceptable where the project has already documented it as an accepted auxiliary path.

## Capability Exposure Rules

Capability registration stays centralized in `HbmCapabilityEvents`.

- Register item and block-entity capabilities through the Neo capability registration event, never through injected legacy patterns.
- Use stable wrapper instances when the backing storage is long-lived.
- Respect sided access by consuming the event context or a direction-specific wrapper when the block entity supports it.
- A null capability context means “unsided view” and must be handled intentionally rather than by accident.

## Registry And Package Placement

Shared types belong where contributors will expect them:

- `registry`: deferred registers, tag keys, materials, effects, and other registry declarations.
- `api`: shared energy, fluid, and other reusable capability-facing primitives.
- `attachment`: entity/player runtime state models and access helpers.
- `event`: centralized NeoForge event handlers and capability registration.
- `config`: Neo config roots and config-backed accessors.
- `util`: compatibility helpers and shared leaf utilities that do not own registry state.

If a new type does not clearly fit one of those packages, the design probably is not settled enough yet.

## Porting Guardrails

Before adding new content:

- Build on the shared platform layer first.
- Prefer tags over hard-coded item IDs for family matching.
- Prefer shared component helpers over feature-local compound-tag plumbing.
- Prefer shared energy/fluid wrappers over local capability implementations.
- Treat archived legacy code as historical reference only, not as a dependency to copy back into Neo.
