package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.BurnerPressMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class BurnerPressScreen extends AbstractMachineScreen<BurnerPressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/gui_press.png"
    );

    public BurnerPressScreen(BurnerPressMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 166);
    }

    @Override
    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int powerFrame = menu.getPowerFrame();
        blitTexture(guiGraphics, 25, 16, 176, 14 + 18 * powerFrame, 18, 18);

        int burn = menu.getBurnScaled(13);
        blitVerticalBar(guiGraphics, 27, 36, 176, 0, 13, 13, burn);

        int progress = menu.getProgressScaled(16);
        blitVerticalBar(guiGraphics, 79, 35, 194, 0, 18, 16, progress);
    }
}
