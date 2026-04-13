package com.hbm.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class HbmNetworkEvents {
    private HbmNetworkEvents() {
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            HbmNetwork.syncPlayerAttachment(player);
            HbmNetwork.syncLivingAttachment(player);
        }
    }

    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            HbmNetwork.syncPlayerAttachment(player);
            HbmNetwork.syncLivingAttachment(player);
        }
    }

    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer trackingPlayer)) {
            return;
        }

        if (event.getTarget() instanceof Player trackedPlayer) {
            HbmNetwork.sendPlayerAttachmentTo(trackingPlayer, trackedPlayer);
        }
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            HbmNetwork.sendLivingAttachmentTo(trackingPlayer, livingEntity);
        }
    }
}
