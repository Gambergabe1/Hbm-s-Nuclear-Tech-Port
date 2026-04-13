package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.blockentity.FluidBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BarrelModePayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<BarrelModePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "barrel_mode")
    );

    public static final StreamCodec<FriendlyByteBuf, BarrelModePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BarrelModePayload::pos,
            BarrelModePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BarrelModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.getBlockEntity(payload.pos()) instanceof FluidBarrelBlockEntity barrel) {
                barrel.cycleMode();
            }
        });
    }
}
