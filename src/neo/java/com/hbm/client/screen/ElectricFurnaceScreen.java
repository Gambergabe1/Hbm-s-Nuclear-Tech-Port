package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.ElectricFurnaceMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class ElectricFurnaceScreen extends AbstractMachineScreen<ElectricFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/guielectricfurnace.png"
    );

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 166);
    }

    @Override
    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int power = menu.getPowerScaled(52);
        blitVerticalBar(guiGraphics, 20, 17, 200, 0, 16, 52, power);

        if (menu.isProcessing()) {
            blitTexture(guiGraphics, 55, 34, 176, 31, 18, 18);
        }

        int progress = menu.getProgressScaled(24);
        blitHorizontalBar(guiGraphics, 79, 34, 176, 14, 25, 17, progress + 1);
    }

    @Override
    protected void renderExtraTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isHoveringArea(20, 17, 16, 52, mouseX, mouseY)) {
            int power = menu.getPowerScaled(52);
            int percent = (power * 100) / 52;
            guiGraphics.renderTooltip(font, Component.literal("Stored Power: " + percent + "%"), mouseX, mouseY);
        }
    }
}
