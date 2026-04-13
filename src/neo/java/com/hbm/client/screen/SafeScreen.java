package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.SafeMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class SafeScreen extends AbstractStorageScreen<SafeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/storage/gui_safe.png"
    );

    public SafeScreen(SafeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 168, 8, 74, 4210752);
    }
}
