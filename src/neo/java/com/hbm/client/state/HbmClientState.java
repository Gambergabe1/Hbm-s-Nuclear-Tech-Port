package com.hbm.client.state;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentHashMap;

import com.hbm.attachment.HbmAttachmentAccess;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public final class HbmClientState {
    private static final long HUD_NOTIFICATION_DURATION_MS = 3_000L;
    private static final int MAX_HUD_NOTIFICATIONS = 3;
    private static final Map<BlockPos, PressMachineState> PRESS_MACHINE_STATES = new ConcurrentHashMap<>();
    private static final Map<BlockPos, FluidStack> BARREL_STATES = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedDeque<HudNotification> HUD_NOTIFICATIONS = new ConcurrentLinkedDeque<>();
    private static volatile int rbmkColumnHeight;
    private static volatile HudSnapshot hudSnapshot = HudSnapshot.empty();

    private HbmClientState() {
    }

    public static HudSnapshot getHudSnapshot() {
        return hudSnapshot;
    }

    public static void updateLocalHud(Player player) {
        var playerData = HbmAttachmentAccess.player(player);
        var livingData = HbmAttachmentAccess.living(player);
        hudSnapshot = new HudSnapshot(
            playerData.isBackpackEnabled(),
            playerData.isHudEnabled(),
            playerData.isJetpackActive(),
            livingData.getRads(),
            livingData.getEnvironmentRads(),
            livingData.getDigamma(),
            livingData.getBombTimer(),
            livingData.getContagion(),
            rbmkColumnHeight
        );
    }

    public static int getRbmkColumnHeight() {
        return rbmkColumnHeight;
    }

    public static void setRbmkColumnHeight(int rbmkColumnHeight) {
        HbmClientState.rbmkColumnHeight = Math.max(rbmkColumnHeight, 0);
        hudSnapshot = hudSnapshot.withRbmkColumnHeight(HbmClientState.rbmkColumnHeight);
    }

    public static PressMachineState getPressMachineState(BlockPos pos) {
        return PRESS_MACHINE_STATES.get(pos);
    }

    public static void putPressMachineState(BlockPos pos, ItemStack input, ItemStack stamp, int progress) {
        if ((input == null || input.isEmpty()) && (stamp == null || stamp.isEmpty()) && progress <= 0) {
            PRESS_MACHINE_STATES.remove(pos);
            return;
        }

        PRESS_MACHINE_STATES.put(pos.immutable(), new PressMachineState(input, stamp, progress));
    }

    public static FluidStack getBarrelState(BlockPos pos) {
        return BARREL_STATES.getOrDefault(pos, FluidStack.EMPTY);
    }

    public static void putBarrelState(BlockPos pos, FluidStack fluid) {
        if (fluid == null || fluid.isEmpty()) {
            BARREL_STATES.remove(pos);
            return;
        }

        BARREL_STATES.put(pos.immutable(), fluid.copy());
    }

    public static void pushHudNotification(String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        long expiresAtMillis = System.currentTimeMillis() + HUD_NOTIFICATION_DURATION_MS;
        HUD_NOTIFICATIONS.addLast(new HudNotification(message.strip(), expiresAtMillis));
        while (HUD_NOTIFICATIONS.size() > MAX_HUD_NOTIFICATIONS) {
            HUD_NOTIFICATIONS.pollFirst();
        }
    }

    public static List<HudNotification> getHudNotifications() {
        pruneNotifications();
        return HUD_NOTIFICATIONS.stream().toList();
    }

    public static void clearTransientState() {
        PRESS_MACHINE_STATES.clear();
        BARREL_STATES.clear();
        HUD_NOTIFICATIONS.clear();
        rbmkColumnHeight = 0;
        hudSnapshot = HudSnapshot.empty();
    }

    private static void pruneNotifications() {
        long now = System.currentTimeMillis();
        while (true) {
            HudNotification notification = HUD_NOTIFICATIONS.peekFirst();
            if (notification == null || notification.expiresAtMillis() > now) {
                return;
            }
            HUD_NOTIFICATIONS.pollFirst();
        }
    }

    public record HudSnapshot(
        boolean backpackEnabled,
        boolean hudEnabled,
        boolean jetpackActive,
        float rads,
        float environmentRads,
        float digamma,
        int bombTimer,
        int contagion,
        int rbmkColumnHeight
    ) {
        private static HudSnapshot empty() {
            return new HudSnapshot(true, true, false, 0.0F, 0.0F, 0.0F, 0, 0, 0);
        }

        private HudSnapshot withRbmkColumnHeight(int rbmkColumnHeight) {
            return new HudSnapshot(
                backpackEnabled,
                hudEnabled,
                jetpackActive,
                rads,
                environmentRads,
                digamma,
                bombTimer,
                contagion,
                rbmkColumnHeight
            );
        }
    }

    public record PressMachineState(ItemStack input, ItemStack stamp, int progress) {
        public PressMachineState {
            input = input == null ? ItemStack.EMPTY : input.copy();
            stamp = stamp == null ? ItemStack.EMPTY : stamp.copy();
            progress = Math.max(progress, 0);
        }
    }

    public record HudNotification(String message, long expiresAtMillis) {
        public float alpha() {
            long remaining = expiresAtMillis - System.currentTimeMillis();
            if (remaining <= 0L) {
                return 0.0F;
            }
            if (remaining >= 500L) {
                return 1.0F;
            }
            return remaining / 500.0F;
        }
    }
}
