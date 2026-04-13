package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.client.state.HbmClientState;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SurveyStatePayload(int rbmkHeight) implements CustomPacketPayload {
    public static final Type<SurveyStatePayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "survey_state")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SurveyStatePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        SurveyStatePayload::rbmkHeight,
        SurveyStatePayload::new
    );

    public SurveyStatePayload {
        rbmkHeight = Math.max(rbmkHeight, 0);
    }

    @Override
    public Type<SurveyStatePayload> type() {
        return TYPE;
    }

    public static void handle(SurveyStatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            HbmClientState.setRbmkColumnHeight(payload.rbmkHeight());
            var player = Minecraft.getInstance().player;
            if (player != null) {
                HbmClientState.updateLocalHud(player);
            }
        });
    }
}
