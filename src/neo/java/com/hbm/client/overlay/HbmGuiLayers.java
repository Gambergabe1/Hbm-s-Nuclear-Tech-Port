package com.hbm.client.overlay;

import java.util.ArrayList;
import java.util.List;

import com.hbm.HbmNuclearTech;
import com.hbm.block.FluidBarrelBlock;
import com.hbm.client.state.HbmClientState;
import com.hbm.registry.HbmBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.fluids.FluidStack;

public final class HbmGuiLayers {
    private static final ResourceLocation STATUS_LAYER = ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "status_hud");
    private static final ResourceLocation LOOK_LAYER = ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "look_overlay");
    private static final ResourceLocation NOTIFICATION_LAYER = ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "hud_notifications");
    private static final int PANEL_BACKGROUND = 0xB0141922;
    private static final int PANEL_BORDER = 0xAA54606E;
    private static final int PANEL_TEXT = 0xFFE7EDF2;
    private static final int TITLE_TEXT = 0xFF88F0A5;
    private static final int PRESS_MAX_PROGRESS = 200;

    private HbmGuiLayers() {
    }

    public static void register(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, STATUS_LAYER, HbmGuiLayers::renderStatusLayer);
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, LOOK_LAYER, HbmGuiLayers::renderLookOverlay);
        event.registerAbove(VanillaGuiLayers.OVERLAY_MESSAGE, NOTIFICATION_LAYER, HbmGuiLayers::renderNotificationLayer);
    }

    private static void renderStatusLayer(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.options.hideGui) {
            return;
        }

        HbmClientState.updateLocalHud(player);
        HbmClientState.HudSnapshot snapshot = HbmClientState.getHudSnapshot();
        if (!snapshot.hudEnabled()) {
            return;
        }

        List<PanelLine> lines = new ArrayList<>();
        lines.add(new PanelLine(snapshot.backpackEnabled() ? "Backpack: armed" : "Backpack: offline", snapshot.backpackEnabled() ? 0xFF7BE69A : 0xFFE68C7B));
        lines.add(new PanelLine(snapshot.jetpackActive() ? "Jetpack: active" : "Jetpack: standby", snapshot.jetpackActive() ? 0xFF7BC4FF : 0xFFC6D0DA));
        lines.add(new PanelLine(String.format(java.util.Locale.ROOT, "Rads: %.2f", snapshot.rads()), statusColor(snapshot.rads(), 5.0F, 15.0F)));
        lines.add(new PanelLine(String.format(java.util.Locale.ROOT, "Env: %.2f", snapshot.environmentRads()), statusColor(snapshot.environmentRads(), 2.0F, 8.0F)));

        if (snapshot.digamma() > 0.01F) {
            lines.add(new PanelLine(String.format(java.util.Locale.ROOT, "Digamma: %.2f", snapshot.digamma()), statusColor(snapshot.digamma(), 1.0F, 5.0F)));
        }
        if (snapshot.contagion() > 0) {
            lines.add(new PanelLine("Contagion: " + formatTicks(snapshot.contagion()), 0xFFF5B75F));
        }
        if (snapshot.bombTimer() > 0) {
            lines.add(new PanelLine("Bomb timer: " + formatTicks(snapshot.bombTimer()), 0xFFFF7C72));
        }
        if (snapshot.rbmkColumnHeight() > 0) {
            lines.add(new PanelLine("RBMK survey: " + snapshot.rbmkColumnHeight() + " m", 0xFF9AD4FF));
        }

        drawPanel(guiGraphics, guiGraphics.guiWidth() - panelWidth(minecraft.font, "NTM Status", lines) - 10, 10, "NTM Status", TITLE_TEXT, lines);
    }

    private static void renderLookOverlay(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.options.hideGui || !HbmClientState.getHudSnapshot().hudEnabled()) {
            return;
        }

        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHit) || blockHit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = blockHit.getBlockPos();
        if (!player.level().isLoaded(pos)) {
            return;
        }

        BlockState state = player.level().getBlockState(pos);
        PanelData panel = createBlockPanel(pos, state);
        if (panel == null) {
            return;
        }

        int left = guiGraphics.guiWidth() / 2 + 12;
        int top = guiGraphics.guiHeight() / 2 - 8;
        drawPanel(guiGraphics, left, top, panel.title(), panel.titleColor(), panel.lines());
    }

    private static PanelData createBlockPanel(BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof FluidBarrelBlock barrelBlock) {
            FluidStack fluid = HbmClientState.getBarrelState(pos);
            List<PanelLine> lines = new ArrayList<>();
            if (fluid.isEmpty()) {
                lines.add(new PanelLine("Fluid: empty", PANEL_TEXT));
            } else {
                lines.add(new PanelLine("Fluid: " + fluid.getHoverName().getString(), 0xFF88D8FF));
                lines.add(new PanelLine(
                    String.format(java.util.Locale.ROOT, "Fill: %,d / %,d mB", fluid.getAmount(), barrelBlock.getCapacity()),
                    PANEL_TEXT
                ));
                lines.add(new PanelLine("Level: " + Mth.floor((fluid.getAmount() / (float) barrelBlock.getCapacity()) * 100.0F) + "%", PANEL_TEXT));
            }
            return new PanelData(Component.translatable("container.barrel").getString(), 0xFF8ED4FF, lines);
        }

        if (state.is(HbmBlocks.MACHINE_PRESS.get()) || state.is(HbmBlocks.MACHINE_EPRESS.get())) {
            HbmClientState.PressMachineState pressState = HbmClientState.getPressMachineState(pos);
            if (pressState == null) {
                return null;
            }

            List<PanelLine> lines = new ArrayList<>();
            lines.add(new PanelLine("Input: " + stackLabel(pressState.input()), PANEL_TEXT));
            lines.add(new PanelLine("Stamp: " + stackLabel(pressState.stamp()), PANEL_TEXT));
            lines.add(new PanelLine("Cycle: " + Mth.floor((pressState.progress() / (float) PRESS_MAX_PROGRESS) * 100.0F) + "%", PANEL_TEXT));
            return new PanelData(
                state.is(HbmBlocks.MACHINE_EPRESS.get()) ? Component.translatable("container.epress").getString() : Component.translatable("container.press").getString(),
                0xFFF0C878,
                lines
            );
        }

        return null;
    }

    private static void renderNotificationLayer(GuiGraphics guiGraphics, net.minecraft.client.DeltaTracker partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        List<HbmClientState.HudNotification> notifications = HbmClientState.getHudNotifications();
        if (notifications.isEmpty()) {
            return;
        }

        Font font = minecraft.font;
        int bottom = guiGraphics.guiHeight() - 56;
        for (int index = 0; index < notifications.size(); index++) {
            HbmClientState.HudNotification notification = notifications.get(index);
            float alpha = notification.alpha();
            if (alpha <= 0.0F) {
                continue;
            }

            int width = font.width(notification.message()) + 12;
            int left = (guiGraphics.guiWidth() - width) / 2;
            int top = bottom - (notifications.size() - index) * 16;
            guiGraphics.fill(left, top, left + width, top + 13, withAlpha(0x10161E, 0.8F * alpha));
            guiGraphics.fill(left, top, left + width, top + 1, withAlpha(0x6FE3A4, alpha));
            guiGraphics.drawCenteredString(font, notification.message(), guiGraphics.guiWidth() / 2, top + 3, withAlpha(0xF2F7FB, alpha));
        }
    }

    private static void drawPanel(GuiGraphics guiGraphics, int left, int top, String title, int titleColor, List<PanelLine> lines) {
        Font font = Minecraft.getInstance().font;
        int width = panelWidth(font, title, lines);
        int height = 7 + (lines.size() + 1) * 10;

        guiGraphics.fill(left, top, left + width, top + height, PANEL_BACKGROUND);
        guiGraphics.fill(left, top, left + width, top + 1, PANEL_BORDER);
        guiGraphics.fill(left, top + height - 1, left + width, top + height, PANEL_BORDER);
        guiGraphics.fill(left, top, left + 1, top + height, PANEL_BORDER);
        guiGraphics.fill(left + width - 1, top, left + width, top + height, PANEL_BORDER);

        int y = top + 4;
        guiGraphics.drawString(font, title, left + 6, y, titleColor, true);
        y += 10;
        for (PanelLine line : lines) {
            guiGraphics.drawString(font, line.text(), left + 6, y, line.color(), true);
            y += 10;
        }
    }

    private static int panelWidth(Font font, String title, List<PanelLine> lines) {
        int width = font.width(title);
        for (PanelLine line : lines) {
            width = Math.max(width, font.width(line.text()));
        }
        return width + 12;
    }

    private static int statusColor(float value, float warnThreshold, float dangerThreshold) {
        if (value >= dangerThreshold) {
            return 0xFFFF7C72;
        }
        if (value >= warnThreshold) {
            return 0xFFF5B75F;
        }
        return 0xFF7BE69A;
    }

    private static String formatTicks(int ticks) {
        int totalSeconds = Mth.ceil(ticks / 20.0F);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(java.util.Locale.ROOT, "%d:%02d", minutes, seconds);
    }

    private static String stackLabel(net.minecraft.world.item.ItemStack stack) {
        return stack.isEmpty() ? "none" : stack.getHoverName().getString();
    }

    private static int withAlpha(int rgb, float alpha) {
        int alphaChannel = Mth.clamp((int) (255.0F * alpha), 0, 255);
        return alphaChannel << 24 | (rgb & 0x00FFFFFF);
    }

    private record PanelLine(String text, int color) {
    }

    private record PanelData(String title, int titleColor, List<PanelLine> lines) {
    }
}
