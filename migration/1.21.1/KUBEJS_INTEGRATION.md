# KubeJS Integration Status

## What's wired up

- `build.gradle`: `compileOnly "dev.latvian.mods:kubejs-neoforge:${kubejs_version}"`
  plus a `KubeJS Maven` repository entry (`https://maven.latvian.dev/releases`).
  Version pinned in `gradle.properties` as `kubejs_version`.
- `src/main/resources/META-INF/services/dev.latvian.mods.kubejs.plugin.KubeJSPlugin`
  - standard Java `ServiceLoader` registration file pointing at
    `com.hbm.integration.kubejs.HbmKubeJSPlugin`. KubeJS only instantiates this
    class when KubeJS itself is present and loaded, so the base mod never
    touches KubeJS classes and works with KubeJS absent.
- `com.hbm.integration.kubejs.HbmKubeJSPlugin` - plugin entry point, currently
  just logs on `init()`.

## Why it's incomplete

This environment's network egress policy denies `maven.latvian.dev` (and every
other modding maven mirror tested), so none of this has actually been
compiled against a real KubeJS jar. To unblock: broaden the environment's
network access or add `maven.latvian.dev` (and `maven.neoforged.net`, which is
also required just to build the base mod) to its allowed domains.

## What's next once builds work

1. Confirm `kubejs_version` in `gradle.properties` against
   `https://maven.latvian.dev/releases/dev/latvian/mods/kubejs-neoforge/` -
   it's currently a best guess for the 1.21.1 (KubeJS "2101.x") line.
2. Register a `RecipeSchema` for each HBM custom data-driven recipe type so
   pack/addon authors can add recipes for them from KubeJS scripts, not just
   via JSON:
   - `hbm:shredding` (`com.hbm.recipe.ShredderRecipe`, registered in
     `com.hbm.registry.HbmRecipeSerializers`) - one `Ingredient` in, one
     `ItemStack` out. This is the first and currently only HBM machine with a
     proper data-driven `Recipe`/`RecipeSerializer` implementation (converted
     from a hardcoded `Map<Item, ItemStack>` in this same pass - see
     `src/main/resources/data/hbm/recipe/shredding/*.json` for the 122
     recipes that were preserved by the conversion).
   - The API surface is `dev.latvian.mods.kubejs.recipe.schema` (roughly
     `RecipeSchemaRegistry` / `RecipeSchema` / `RecipeComponent`, exposed via
     an override on `KubeJSPlugin`) as of the KubeJS 2101.x line, but exact
     class/method names were **not** guessed into this codebase because
     nothing here could be compiled to check them - get this from the actual
     `kubejs-neoforge` jar/sources once it resolves.
   - The Press machine (`com.hbm.machine.PressRecipeRegistry`) is still a
     hardcoded Java lookup and should get the same data-driven treatment
     before it's worth exposing to KubeJS - its current "any plate stamp +
     recognized ingot" shape doesn't map cleanly onto a simple two-ingredient
     recipe and needs a small design decision first (see the class for the
     current behavior).
3. Machines that already reuse a vanilla `RecipeType` need nothing extra:
   the electric furnace (`ElectricFurnaceBlockEntity`) already runs on
   `RecipeType.SMELTING`, which KubeJS supports out of the box.
4. Consider exposing HBM-specific gameplay hooks as KubeJS events (radiation
   ticks, RBMK meltdown, etc.) once the base recipe integration is verified -
   lower priority than recipes for "native support" in the usual sense.
