package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.attachment.HbmAttachmentAccess;
import com.hbm.client.state.HbmClientState;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerAttachmentPayload(int entityId, CompoundTag data) implements CustomPacketPayload {
    public static final Type<PlayerAttachmentPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "player_attachment")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerAttachmentPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        PlayerAttachmentPayload::entityId,
        ByteBufCodecs.COMPOUND_TAG,
        PlayerAttachmentPayload::data,
        PlayerAttachmentPayload::new
    );

    public PlayerAttachmentPayload {
        data = data == null ? new CompoundTag() : data.copy();
    }

    @Override
    public Type<PlayerAttachmentPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerAttachmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) {
                return;
            }

            var entity = minecraft.level.getEntity(payload.entityId());
            if (entity instanceof Player player) {
                HbmAttachmentAccess.player(player).deserializeNBT(minecraft.level.registryAccess(), payload.data().copy());
                if (minecraft.player != null && player.getId() == minecraft.player.getId()) {
                    HbmClientState.updateLocalHud(minecraft.player);
                }
            }
        });
    }
}
