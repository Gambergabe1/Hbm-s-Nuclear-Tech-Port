package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.CrateIronMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class CrateIronScreen extends AbstractStorageScreen<CrateIronMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/storage/gui_crate_iron.png"
    );

    public CrateIronScreen(CrateIronMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 186, 8, 92, 4210752);
    }
}
