# NeoForge Port Gap Report

Generated: 2026-04-12T21:23:15.2063841Z

## Source Sets

- NeoForge Java files under src/neo/java: 193
- Archived legacy Java reference files under migration/1.21.1/archive/legacy-source-tree/src/main/java: 2882

## Coverage Summary

| Area | Legacy | Neo | Covered | Missing | Coverage |
| --- | ---: | ---: | ---: | ---: | ---: |
| Items (declared) | 2224 | 828 | 773 | 1451 | 34.8% |
| Blocks | 987 | 257 | 256 | 731 | 25.9% |
| Entities | 167 | 9 | 8 | 159 | 4.8% |
| Block Entities | 257 | 13 | 1 | 256 | 0.4% |

## Immediate Blockers

- NeoForge menu registrations: 10
- Legacy GuiContainer uses from audit: 342
- Legacy @SideOnly uses from audit: 1186
- Legacy tile entity registrations from audit: 257
- Legacy OreDictionary uses from audit: 256
- Access transformer present: False
- Coremod plugin present: False

## Sample Missing Item IDs

- ?!?!
- [screams geometrically]
- 31!
- Aged like a fine wine! Well, almost.
- ajr_boots
- ajr_legs
- ajr_plate
- ajro_boots
- ajro_legs
- ajro_plate
- alloy_axe
- alloy_boots
- alloy_helmet
- alloy_hoe
- alloy_legs
- alloy_pickaxe
- alloy_plate
- alloy_shield
- alloy_shovel
- alloy_sword
- ammo_12gauge
- ammo_12gauge_du
- ammo_12gauge_incendiary
- ammo_12gauge_marauder
- ammo_12gauge_shrapnel
- ammo_12gauge_sleek
- ammo_20gauge
- ammo_20gauge_caustic
- ammo_20gauge_explosive
- ammo_20gauge_flechette

## Sample Missing Block IDs

- absorber
- absorber_green
- absorber_pink
- absorber_red
- ams_base
- ams_emitter
- ams_limiter
- ancient_scrap
- anvil_bismuth
- anvil_dnt
- anvil_ferrouranium
- anvil_iron
- anvil_lead
- anvil_meteorite
- anvil_murky
- anvil_osmiridium
- anvil_schrabidate
- anvil_starmetal
- anvil_steel
- ash_digamma
- balefire
- baleonitite_0
- baleonitite_1
- baleonitite_2
- baleonitite_3
- baleonitite_4
- baleonitite_core
- baleonitite_slaked
- barbed_wire
- barbed_wire_acid

## Sample Missing Entity IDs

- entity_agent_orange
- entity_b_smoke_fx
- entity_balefire
- entity_baleflare
- entity_balls_o_tron
- entity_balls_o_tron_seg
- entity_black_hole
- entity_bobmazon
- entity_bomber
- entity_booster
- entity_boxcar
- entity_building
- entity_bullet
- entity_bullet_mk2
- entity_burning_foeq
- entity_c_item
- entity_c_package
- entity_carrier
- entity_chlorine_fx
- entity_cloud_fleija

## Sample Missing Block Entity IDs

- tileentity_acidomatic
- tileentity_ams_base
- tileentity_ams_emitter
- tileentity_ams_limiter
- tileentity_arc_welder
- tileentity_barrel
- tileentity_blastdoor
- tileentity_bm_power_box
- tileentity_bomb_multi
- tileentity_book_crafting
- tileentity_cable
- tileentity_cable_diode
- tileentity_cable_gauge
- tileentity_cable_switch
- tileentity_charger
- tileentity_chlorine_seal
- tileentity_chungus
- tileentity_compact_launcher
- tileentity_condenser_powered
- tileentity_connector_redwire

## Notes

- This report measures declared registry coverage, not gameplay parity or rendering/network correctness.
- Legacy ItemAutogen families are not fully expanded here; they still represent major unported surface area.
- Legacy metadata circuits are treated as covered when explicit NeoForge circuit_* items exist.
