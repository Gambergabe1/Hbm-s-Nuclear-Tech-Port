package com.hbm.integration.kubejs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;

/**
 * Entry point for HBM's native KubeJS integration.
 *
 * This class is only ever loaded by KubeJS's own ServiceLoader lookup
 * (see src/main/resources/META-INF/services/dev.latvian.mods.kubejs.plugin.KubeJSPlugin),
 * which only runs when KubeJS is actually present. The base HBM mod jar
 * builds and runs fine without KubeJS installed - "kubejs-neoforge" is a
 * compileOnly dependency (see build.gradle) and nothing in the always-loaded
 * mod code touches KubeJS classes.
 *
 * Verification status (as of this port pass): the ServiceLoader wiring and
 * this class's shape follow KubeJS's documented plugin discovery mechanism,
 * but nothing here has been compiled or run against a real KubeJS jar - this
 * environment's network policy denies maven.latvian.dev, so the dependency
 * cannot currently be resolved. Recipe scripting support for HBM's custom
 * machine recipe types (shredder, press, etc.) still needs a RecipeSchema
 * registered for each type via KubeJS's recipe schema API
 * (dev.latvian.mods.kubejs.recipe.schema, roughly RecipeSchemaRegistry /
 * RecipeSchema / RecipeComponent as of the KubeJS 2101.x line for 1.21.1) -
 * deferred here rather than guessed, since getting field/method names wrong
 * would silently fail to compile with no way to check in this session.
 * Machines that already reuse a vanilla RecipeType (e.g. the electric
 * furnace, which uses minecraft:smelting) already work with KubeJS's stock
 * recipe events with zero extra code.
 */
public class HbmKubeJSPlugin extends KubeJSPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger("hbm/kubejs");

    @Override
    public void init() {
        LOGGER.info("HBM's Nuclear Tech: KubeJS integration active");
    }
}
