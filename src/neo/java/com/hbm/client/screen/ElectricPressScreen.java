package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.ElectricPressMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class ElectricPressScreen extends AbstractMachineScreen<ElectricPressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/gui_epress.png"
    );

    public ElectricPressScreen(ElectricPressMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 166);
    }

    @Override
    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int power = menu.getPowerScaled(52);
        blitVerticalBar(guiGraphics, 26, 17, 176, 0, 16, 52, power);

        int progress = menu.getProgressScaled(16);
        blitVerticalBar(guiGraphics, 79, 35, 192, 0, 18, 16, progress);
    }
}
