# Client Rewrite

Status: Partial

This milestone tracks the NeoForge-native replacement for the currently live client slice: HUD, crosshair overlays, block-entity visuals, screen registration, and client-only state handling.

## Complete For The Live Neo Slice

- [x] Client event wiring runs through NeoForge client lifecycle events.
- [x] Menu screen registration exists for the currently ported storage and machine menus.
- [x] A shared GUI-layer path exists for status HUD, machine look overlays, and transient HUD notifications.
- [x] HUD notifications no longer depend on vanilla action-bar chat messages.
- [x] A block-entity renderer registration path exists for Neo machine visuals.
- [x] Press machine world-space visuals consume synced press-state payloads.
- [x] Barrel client visuals consume synced barrel-state payloads.
- [x] Client transient state is cleared on logout/world transitions.

## Current Live Coverage

- `HbmGuiLayers` owns the shared HUD, look-overlay, and notification layers.
- `HbmClientState` owns HUD snapshots, transient notifications, press tracked state, and barrel tracked state.
- `PressBlockEntityRenderer` provides world-space stamp/input/head travel visuals for both burner and electric presses.
- `FluidBarrelBlockEntityRenderer` provides a fluid-level world-space indicator for synced barrel contents.
- `HbmHudNotificationPayload` now feeds the shared HUD notification queue instead of vanilla overlay chat.

## Still Release-Blocking

- [ ] The global legacy `GuiContainer` backlog is still not ported.
- [ ] Most legacy `@SideOnly` client logic is still only present in the archived 1.12.2 reference tree.
- [ ] Legacy particles, special render layers, shader-like effects, and complex machine renderers outside the live Neo slice are still missing.
- [ ] Entity, armor, weapon, and item-special visuals beyond the current Neo slice are still incomplete.

## Exit Condition

This milestone is only fully complete when the remaining legacy GUI, renderer, particle, overlay, and special-visual backlog is either ported to NeoForge or explicitly accepted as a cut in `PROPER_PORT_BASELINE.md`.
