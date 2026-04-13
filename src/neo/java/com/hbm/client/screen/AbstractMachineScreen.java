package com.hbm.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AbstractMachineScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation texture;
    private final int labelColor;
    private final int textureWidth;
    private final int textureHeight;

    protected AbstractMachineScreen(
        T menu,
        Inventory playerInventory,
        Component title,
        ResourceLocation texture,
        int imageWidth,
        int imageHeight
    ) {
        this(menu, playerInventory, title, texture, imageWidth, imageHeight, 8, imageHeight - 94, 4210752, 256, 256);
    }

    protected AbstractMachineScreen(
        T menu,
        Inventory playerInventory,
        Component title,
        ResourceLocation texture,
        int imageWidth,
        int imageHeight,
        int inventoryLabelX,
        int inventoryLabelY,
        int labelColor,
        int textureWidth,
        int textureHeight
    ) {
        super(menu, playerInventory, title);
        this.texture = texture;
        this.labelColor = labelColor;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
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
        renderExtraTooltips(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected final void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);
        renderMachineGraphics(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, (imageWidth - font.width(title)) / 2, 6, labelColor, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, labelColor, false);
    }

    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    protected void renderExtraTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    protected final ResourceLocation texture() {
        return texture;
    }

    protected final void blitVerticalBar(
        GuiGraphics guiGraphics,
        int x,
        int y,
        int u,
        int v,
        int width,
        int maxHeight,
        int filledHeight
    ) {
        if (filledHeight <= 0) {
            return;
        }

        guiGraphics.blit(
            texture,
            leftPos + x,
            topPos + y + maxHeight - filledHeight,
            u,
            v + maxHeight - filledHeight,
            width,
            filledHeight,
            textureWidth,
            textureHeight
        );
    }

    protected final void blitHorizontalBar(
        GuiGraphics guiGraphics,
        int x,
        int y,
        int u,
        int v,
        int maxWidth,
        int height,
        int filledWidth
    ) {
        if (filledWidth <= 0) {
            return;
        }

        guiGraphics.blit(
            texture,
            leftPos + x,
            topPos + y,
            u,
            v,
            Math.min(maxWidth, filledWidth),
            height,
            textureWidth,
            textureHeight
        );
    }

    protected final void blitTexture(GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height) {
        guiGraphics.blit(texture, leftPos + x, topPos + y, u, v, width, height, textureWidth, textureHeight);
    }

    protected final boolean isHoveringArea(int x, int y, int width, int height, int mouseX, int mouseY) {
        int localX = mouseX - leftPos;
        int localY = mouseY - topPos;
        return localX >= x && localX < x + width && localY >= y && localY < y + height;
    }

    protected final boolean isClickWithinArea(int x, int y, int width, int height, double mouseX, double mouseY) {
        double localX = mouseX - leftPos;
        double localY = mouseY - topPos;
        return localX >= x && localX < x + width && localY >= y && localY < y + height;
    }

    protected final void playButtonClick() {
        Minecraft.getInstance()
            .getSoundManager()
            .play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
