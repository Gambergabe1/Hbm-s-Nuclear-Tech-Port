package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.client.state.HbmClientState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BarrelStatePayload(BlockPos pos, FluidStack fluid) implements CustomPacketPayload {
    public static final Type<BarrelStatePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "barrel_state")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BarrelStatePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BarrelStatePayload::pos,
            FluidStack.STREAM_CODEC,
            BarrelStatePayload::fluid,
            BarrelStatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BarrelStatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> HbmClientState.putBarrelState(payload.pos(), payload.fluid()));
    }
}
