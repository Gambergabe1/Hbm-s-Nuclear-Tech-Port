package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.BatteryStorageMenu;
import com.hbm.menu.BurnerPressMenu;
import com.hbm.menu.CrateDeshMenu;
import com.hbm.menu.CrateIronMenu;
import com.hbm.menu.CrateSteelMenu;
import com.hbm.menu.ElectricFurnaceMenu;
import com.hbm.menu.ElectricPressMenu;
import com.hbm.menu.FluidBarrelMenu;
import com.hbm.menu.SafeMenu;
import com.hbm.menu.ShredderMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HbmMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(Registries.MENU, HbmNuclearTech.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<CrateIronMenu>> CRATE_IRON =
        MENU_TYPES.register("crate_iron", () -> new MenuType<>(CrateIronMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<CrateDeshMenu>> CRATE_DESH =
        MENU_TYPES.register("crate_desh", () -> new MenuType<>(CrateDeshMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<CrateSteelMenu>> CRATE_STEEL =
        MENU_TYPES.register("crate_steel", () -> new MenuType<>(CrateSteelMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<SafeMenu>> SAFE =
        MENU_TYPES.register("safe", () -> new MenuType<>(SafeMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<BurnerPressMenu>> MACHINE_PRESS =
        MENU_TYPES.register("machine_press", () -> new MenuType<>(BurnerPressMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricPressMenu>> MACHINE_EPRESS =
        MENU_TYPES.register("machine_epress", () -> new MenuType<>(ElectricPressMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ElectricFurnaceMenu>> MACHINE_ELECTRIC_FURNACE =
        MENU_TYPES.register("machine_electric_furnace", () -> new MenuType<>(ElectricFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<BatteryStorageMenu>> MACHINE_BATTERY =
        MENU_TYPES.register("machine_battery", () -> new MenuType<>(BatteryStorageMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<ShredderMenu>> MACHINE_SHREDDER =
        MENU_TYPES.register("machine_shredder", () -> new MenuType<>(ShredderMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidBarrelMenu>> BARREL =
        MENU_TYPES.register("barrel", () -> new MenuType<>(FluidBarrelMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private HbmMenuTypes() {
    }
}
