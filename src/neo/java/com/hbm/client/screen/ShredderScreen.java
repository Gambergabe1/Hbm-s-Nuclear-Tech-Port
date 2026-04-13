package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.menu.ShredderMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class ShredderScreen extends AbstractMachineScreen<ShredderMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/processing/gui_shredder.png"
    );
    private static final Component BLADE_WARNING = Component.literal("Shredder blades are broken or missing!")
        .withStyle(ChatFormatting.RED);

    public ShredderScreen(ShredderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 233);
    }

    @Override
    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int power = menu.getPowerScaled(88);
        blitVerticalBar(guiGraphics, 8, 18, 176, 72, 16, 88, power);

        int progress = menu.getProgressScaled(34);
        blitHorizontalBar(guiGraphics, 63, 89, 176, 54, 35, 18, progress + 1);

        renderBladeState(guiGraphics, 43, 71, menu.getBladeState(true), true);
        renderBladeState(guiGraphics, 79, 71, menu.getBladeState(false), false);

        if (menu.hasBladeWarning()) {
            guiGraphics.fill(leftPos + 12, topPos + 36, leftPos + 28, topPos + 52, 0xCCAA0000);
            guiGraphics.drawString(font, "!", leftPos + 18, topPos + 40, 0xFFFFFF, false);
        }
    }

    private void renderBladeState(GuiGraphics guiGraphics, int x, int y, int state, boolean leftBlade) {
        if (state <= 0) {
            return;
        }

        int u = leftBlade ? 176 : 194;
        int v = switch (state) {
            case 1 -> 0;
            case 2 -> 18;
            case 3 -> 36;
            default -> 0;
        };
        blitTexture(guiGraphics, x, y, u, v, 18, 18);
    }

    @Override
    protected void renderExtraTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (menu.hasBladeWarning() && isHoveringArea(12, 36, 16, 16, mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, BLADE_WARNING, mouseX, mouseY);
        }
    }
}
