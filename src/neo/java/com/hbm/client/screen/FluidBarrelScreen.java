package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.client.state.HbmClientState;
import com.hbm.menu.FluidBarrelMenu;
import com.hbm.network.HbmNetwork;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public final class FluidBarrelScreen extends AbstractMachineScreen<FluidBarrelMenu> {
    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "textures/gui/storage/gui_barrel.png");

    public FluidBarrelScreen(FluidBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE, 176, 166);
    }

    @Override
    protected void renderMachineGraphics(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        blitTexture(guiGraphics, 151, 34, 176, menu.getMode() * 18, 18, 18);

        FluidStack fluid = HbmClientState.getBarrelState(menu.getPos());
        if (!fluid.isEmpty()) {
            renderFluid(guiGraphics, fluid, leftPos + 71, topPos + 16, 34, 52, Math.max(menu.getCapacity(), 1));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isClickWithinArea(151, 34, 18, 18, mouseX, mouseY)) {
            HbmNetwork.cycleBarrelMode(menu.getPos());
            playButtonClick();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderExtraTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isHoveringArea(151, 34, 18, 18, mouseX, mouseY)) {
            String modeName;
            switch (menu.getMode()) {
                case 0: modeName = "Fill Only"; break;
                case 1: modeName = "Balanced"; break;
                case 2: modeName = "Drain Only"; break;
                case 3: modeName = "Locked"; break;
                default: modeName = "Unknown Mode"; break;
            }
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal(modeName));
            guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
        if (isHoveringArea(71, 16, 34, 52, mouseX, mouseY)) {
            FluidStack fluid = HbmClientState.getBarrelState(menu.getPos());
            int capacity = Math.max(menu.getCapacity(), 1);
            List<Component> tooltip = new ArrayList<>();
            if (fluid.isEmpty()) {
                tooltip.add(Component.translatable("hbm.fluid.empty"));
            } else {
                tooltip.add(fluid.getHoverName());
                tooltip.add(Component.literal(fluid.getAmount() + " / " + capacity + " mB"));
            }
            guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    private void renderFluid(GuiGraphics guiGraphics, FluidStack fluid, int x, int y, int width, int height, int capacity) {
        int renderHeight = Math.min(height, (int) Math.ceil(height * (double) fluid.getAmount() / capacity));
        if (renderHeight <= 0) {
            return;
        }

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid().getFluidType());
        ResourceLocation stillTexture = extensions.getStillTexture(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int color = extensions.getTintColor(fluid);

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255.0F;

        guiGraphics.setColor(red, green, blue, alpha);
        int drawnHeight = 0;
        while (drawnHeight < renderHeight) {
            int drawHeight = Math.min(renderHeight - drawnHeight, 16);
            for (int drawnWidth = 0; drawnWidth < width; drawnWidth += 16) {
                int drawWidth = Math.min(width - drawnWidth, 16);
                guiGraphics.blit(x + drawnWidth, y + height - drawnHeight - drawHeight, 0, drawWidth, drawHeight, sprite);
            }
            drawnHeight += drawHeight;
        }
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
