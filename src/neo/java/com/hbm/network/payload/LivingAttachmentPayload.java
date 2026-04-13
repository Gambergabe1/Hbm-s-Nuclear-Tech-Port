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
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LivingAttachmentPayload(int entityId, CompoundTag data) implements CustomPacketPayload {
    public static final Type<LivingAttachmentPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "living_attachment")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LivingAttachmentPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        LivingAttachmentPayload::entityId,
        ByteBufCodecs.COMPOUND_TAG,
        LivingAttachmentPayload::data,
        LivingAttachmentPayload::new
    );

    public LivingAttachmentPayload {
        data = data == null ? new CompoundTag() : data.copy();
    }

    @Override
    public Type<LivingAttachmentPayload> type() {
        return TYPE;
    }

    public static void handle(LivingAttachmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) {
                return;
            }

            var entity = minecraft.level.getEntity(payload.entityId());
            if (entity instanceof LivingEntity livingEntity) {
                HbmAttachmentAccess.living(livingEntity).deserializeNBT(minecraft.level.registryAccess(), payload.data().copy());
                if (minecraft.player != null && livingEntity.getId() == minecraft.player.getId()) {
                    HbmClientState.updateLocalHud(minecraft.player);
                }
            }
        });
    }
}
