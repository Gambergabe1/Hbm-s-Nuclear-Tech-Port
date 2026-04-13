package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.attachment.HbmKey;
import com.hbm.attachment.HbmPlayerData;
import com.hbm.network.HbmNetwork;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record KeybindStatePayload(HbmKey key, boolean pressed) implements CustomPacketPayload {
    public static final Type<KeybindStatePayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "keybind_state")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, KeybindStatePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        payload -> payload.key().id(),
        ByteBufCodecs.BOOL,
        KeybindStatePayload::pressed,
        (keyId, pressed) -> new KeybindStatePayload(HbmKey.byId(keyId), pressed)
    );

    @Override
    public Type<KeybindStatePayload> type() {
        return TYPE;
    }

    public static void handle(KeybindStatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            HbmPlayerData playerData = HbmAttachmentAccess.player(player);
            boolean wasPressed = playerData.getKeyPressed(payload.key());
            playerData.setKeyPressed(payload.key(), payload.pressed());

            if (!payload.pressed() || wasPressed) {
                return;
            }

            if (payload.key() == HbmKey.TOGGLE_JETPACK) {
                HbmNetwork.sendHudNotification(player, playerData.isBackpackEnabled() ? "Jetpack ON" : "Jetpack OFF");
            } else if (payload.key() == HbmKey.TOGGLE_HEAD) {
                HbmNetwork.sendHudNotification(player, playerData.isHudEnabled() ? "HUD ON" : "HUD OFF");
            }
        });
    }
}
