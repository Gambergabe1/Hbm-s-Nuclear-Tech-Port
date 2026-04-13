package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.CrateDeshMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class CrateDeshScreen extends AbstractStorageScreen<CrateDeshMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/storage/gui_crate_desh.png"
    );

    public CrateDeshScreen(CrateDeshMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 248, 256, 44, 162, 0x3F1515);
    }
}
