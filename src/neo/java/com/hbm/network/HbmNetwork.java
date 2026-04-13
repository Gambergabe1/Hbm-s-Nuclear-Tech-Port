package com.hbm.network;

import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.network.payload.BarrelModePayload;
import com.hbm.network.payload.BarrelStatePayload;
import com.hbm.network.payload.HbmHudNotificationPayload;
import com.hbm.network.payload.KeybindStatePayload;
import com.hbm.network.payload.LivingAttachmentPayload;
import com.hbm.network.payload.PlayerAttachmentPayload;
import com.hbm.network.payload.PressMachineStatePayload;
import com.hbm.network.payload.SurveyStatePayload;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class HbmNetwork {
    public static final String PROTOCOL_VERSION = "1";

    private HbmNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToClient(HbmHudNotificationPayload.TYPE, HbmHudNotificationPayload.STREAM_CODEC, HbmHudNotificationPayload::handle);
        registrar.playToClient(LivingAttachmentPayload.TYPE, LivingAttachmentPayload.STREAM_CODEC, LivingAttachmentPayload::handle);
        registrar.playToClient(PlayerAttachmentPayload.TYPE, PlayerAttachmentPayload.STREAM_CODEC, PlayerAttachmentPayload::handle);
        registrar.playToClient(PressMachineStatePayload.TYPE, PressMachineStatePayload.STREAM_CODEC, PressMachineStatePayload::handle);
        registrar.playToClient(BarrelStatePayload.TYPE, BarrelStatePayload.STREAM_CODEC, BarrelStatePayload::handle);
        registrar.playToClient(SurveyStatePayload.TYPE, SurveyStatePayload.STREAM_CODEC, SurveyStatePayload::handle);
        registrar.playToServer(KeybindStatePayload.TYPE, KeybindStatePayload.STREAM_CODEC, KeybindStatePayload::handle);
        registrar.playToServer(BarrelModePayload.TYPE, BarrelModePayload.STREAM_CODEC, BarrelModePayload::handle);
    }

    public static void syncPlayerAttachment(ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
            player,
            new PlayerAttachmentPayload(player.getId(), HbmAttachmentAccess.player(player).serializeNBT(player.level().registryAccess()))
        );
    }

    public static void sendPlayerAttachmentTo(ServerPlayer recipient, Player trackedPlayer) {
        PacketDistributor.sendToPlayer(
            recipient,
            new PlayerAttachmentPayload(
                trackedPlayer.getId(),
                HbmAttachmentAccess.player(trackedPlayer).serializeNBT(trackedPlayer.level().registryAccess())
            )
        );
    }

    public static void syncLivingAttachment(LivingEntity livingEntity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
            livingEntity,
            new LivingAttachmentPayload(
                livingEntity.getId(),
                HbmAttachmentAccess.living(livingEntity).serializeNBT(livingEntity.level().registryAccess())
            )
        );
    }

    public static void sendLivingAttachmentTo(ServerPlayer recipient, LivingEntity livingEntity) {
        PacketDistributor.sendToPlayer(
            recipient,
            new LivingAttachmentPayload(
                livingEntity.getId(),
                HbmAttachmentAccess.living(livingEntity).serializeNBT(livingEntity.level().registryAccess())
            )
        );
    }

    public static void sendHudNotification(ServerPlayer player, String message) {
        PacketDistributor.sendToPlayer(player, new HbmHudNotificationPayload(message));
    }

    public static void syncPressMachineState(ServerLevel level, BlockPos pos, ItemStack input, ItemStack stamp, int progress) {
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), PressMachineStatePayload.from(pos, input, stamp, progress));
    }

    public static void syncBarrelState(ServerLevel level, BlockPos pos, FluidStack fluid) {
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), new BarrelStatePayload(pos, fluid));
    }

    public static void sendBarrelStateTo(ServerPlayer player, BlockPos pos, FluidStack fluid) {
        PacketDistributor.sendToPlayer(player, new BarrelStatePayload(pos, fluid));
    }

    public static void cycleBarrelMode(BlockPos pos) {
        PacketDistributor.sendToServer(new BarrelModePayload(pos));
    }

    public static void sendSurveyState(ServerLevel level, int rbmkHeight) {
        PacketDistributor.sendToPlayersInDimension(level, new SurveyStatePayload(rbmkHeight));
    }
}
