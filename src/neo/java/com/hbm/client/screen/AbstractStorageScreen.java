package com.hbm.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AbstractStorageScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation texture;
    private final int labelColor;

    protected AbstractStorageScreen(
        T menu,
        Inventory playerInventory,
        Component title,
        ResourceLocation texture,
        int imageWidth,
        int imageHeight,
        int inventoryLabelX,
        int inventoryLabelY,
        int labelColor
    ) {
        super(menu, playerInventory, title);
        this.texture = texture;
        this.labelColor = labelColor;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.inventoryLabelX = inventoryLabelX;
        this.inventoryLabelY = inventoryLabelY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = (width - imageWidth) / 2;
        int top = (height - imageHeight) / 2;
        guiGraphics.blit(texture, left, top, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, (imageWidth - font.width(title)) / 2, 6, labelColor, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, labelColor, false);
    }
}
