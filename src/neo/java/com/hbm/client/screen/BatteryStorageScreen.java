package com.hbm.client.screen;

import com.hbm.HbmNuclearTech;
import com.hbm.blockentity.BatteryStorageBlockEntity;
import com.hbm.menu.BatteryStorageMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BatteryStorageScreen extends AbstractContainerScreen<BatteryStorageMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        HbmNuclearTech.MODID,
        "textures/gui/storage/gui_battery.png"
    );

    public BatteryStorageScreen(BatteryStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelX = 8;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        guiGraphics.blit(TEXTURE, left, top, 0, 0, imageWidth, imageHeight, 256, 256);

        int power = menu.getPowerScaled(52);
        if (power > 0) {
            guiGraphics.blit(TEXTURE, left + 71, top + 69 - power, 176, 52 - power, 34, power, 256, 256);
        }

        guiGraphics.blit(TEXTURE, left + 7, top + 34, 176, 52 + menu.getRedLowMode() * 18, 18, 18, 256, 256);
        guiGraphics.blit(TEXTURE, left + 151, top + 34, 176, 52 + menu.getRedHighMode() * 18, 18, 18, 256, 256);
        guiGraphics.blit(TEXTURE, left + 152, top + 17, 194, 52 + menu.getPriority() * 16, 16, 16, 256, 256);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int localX = (int) mouseX - leftPos;
        int localY = (int) mouseY - topPos;
        if (localX >= 6 && localX < 24 && localY >= 33 && localY < 51) {
            return pressButton(0);
        }
        if (localX >= 150 && localX < 168 && localY >= 33 && localY < 51) {
            return pressButton(1);
        }
        if (localX >= 151 && localX < 167 && localY >= 16 && localY < 32) {
            return pressButton(2);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String titleText = title.getString() + " (" + formatNumber(menu.getStoredPower()) + " HE)";
        guiGraphics.drawString(font, titleText, (imageWidth - font.width(titleText)) / 2, 6, 4210752, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int localX = mouseX - leftPos;
        int localY = mouseY - topPos;
        if (localX >= 71 && localX < 105 && localY >= 17 && localY < 69) {
            guiGraphics.renderComponentTooltip(font, createPowerTooltip(), mouseX, mouseY);
        } else if (localX >= 6 && localX < 24 && localY >= 33 && localY < 51) {
            guiGraphics.renderComponentTooltip(font, List.of(
                Component.literal("No redstone: " + modeName(menu.getRedLowMode())).withStyle(ChatFormatting.YELLOW)
            ), mouseX, mouseY);
        } else if (localX >= 150 && localX < 168 && localY >= 33 && localY < 51) {
            guiGraphics.renderComponentTooltip(font, List.of(
                Component.literal("Redstone: " + modeName(menu.getRedHighMode())).withStyle(ChatFormatting.YELLOW)
            ), mouseX, mouseY);
        } else if (localX >= 151 && localX < 167 && localY >= 16 && localY < 32) {
            guiGraphics.renderComponentTooltip(font, createPriorityTooltip(), mouseX, mouseY);
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean pressButton(int buttonId) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameMode == null) {
            return false;
        }

        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, buttonId);
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    private List<Component> createPowerTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(formatNumber(menu.getStoredPower()) + " / " + formatNumber(menu.getMaxPower()) + " HE"));

        long delta = menu.getPowerDelta();
        ChatFormatting color = ChatFormatting.YELLOW;
        String prefix = "";
        if (delta > 0L) {
            color = ChatFormatting.GREEN;
            prefix = "+";
        } else if (delta < 0L) {
            color = ChatFormatting.RED;
            prefix = "-";
        }

        tooltip.add(Component.literal(prefix + formatNumber(Math.abs(delta)) + " HE/s").withStyle(color));
        return tooltip;
    }

    private List<Component> createPriorityTooltip() {
        List<Component> tooltip = new ArrayList<>();
        String key = switch (menu.getPriority()) {
            case 0 -> "battery.priority.low";
            case 2 -> "battery.priority.high";
            default -> "battery.priority.normal";
        };

        tooltip.add(Component.translatable(key));
        tooltip.add(Component.translatable("battery.priority.recommended").withStyle(ChatFormatting.GRAY));
        for (String line : Component.translatable(key + ".desc").getString().split("\\$")) {
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
        return tooltip;
    }

    private static String modeName(int mode) {
        return switch (mode) {
            case BatteryStorageBlockEntity.MODE_INPUT -> "Input";
            case BatteryStorageBlockEntity.MODE_BUFFER -> "Buffer";
            case BatteryStorageBlockEntity.MODE_OUTPUT -> "Output";
            case BatteryStorageBlockEntity.MODE_NONE -> "Disabled";
            default -> "Input";
        };
    }

    private static String formatNumber(long value) {
        return String.format(Locale.ROOT, "%,d", value);
    }
}
