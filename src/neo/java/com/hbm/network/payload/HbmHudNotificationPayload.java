package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.client.state.HbmClientState;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HbmHudNotificationPayload(String message) implements CustomPacketPayload {
    public static final Type<HbmHudNotificationPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "hud_notification")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HbmHudNotificationPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        HbmHudNotificationPayload::message,
        HbmHudNotificationPayload::new
    );

    public HbmHudNotificationPayload {
        message = message == null ? "" : message;
    }

    @Override
    public Type<HbmHudNotificationPayload> type() {
        return TYPE;
    }

    public static void handle(HbmHudNotificationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> HbmClientState.pushHudNotification(payload.message()));
    }
}
