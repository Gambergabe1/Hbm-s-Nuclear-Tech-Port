package com.hbm.network.payload;

import com.hbm.HbmNuclearTech;
import com.hbm.client.state.HbmClientState;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PressMachineStatePayload(BlockPos pos, ItemStack input, ItemStack stamp, int progress) implements CustomPacketPayload {
    public static final Type<PressMachineStatePayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(HbmNuclearTech.MODID, "press_machine_state")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PressMachineStatePayload> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        PressMachineStatePayload::pos,
        ItemStack.STREAM_CODEC,
        PressMachineStatePayload::input,
        ItemStack.STREAM_CODEC,
        PressMachineStatePayload::stamp,
        ByteBufCodecs.VAR_INT,
        PressMachineStatePayload::progress,
        PressMachineStatePayload::new
    );

    public PressMachineStatePayload {
        pos = pos.immutable();
        input = input == null ? ItemStack.EMPTY : input.copy();
        stamp = stamp == null ? ItemStack.EMPTY : stamp.copy();
        progress = Math.max(progress, 0);
    }

    public static PressMachineStatePayload from(BlockPos pos, ItemStack input, ItemStack stamp, int progress) {
        return new PressMachineStatePayload(pos, input, stamp, progress);
    }

    @Override
    public Type<PressMachineStatePayload> type() {
        return TYPE;
    }

    public static void handle(PressMachineStatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> HbmClientState.putPressMachineState(payload.pos(), payload.input(), payload.stamp(), payload.progress()));
    }
}
