package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.CrateSteelMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class CrateSteelScreen extends AbstractStorageScreen<CrateSteelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/storage/gui_crate_steel.png"
    );

    public CrateSteelScreen(CrateSteelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 222, 8, 128, 1842204);
    }
}
