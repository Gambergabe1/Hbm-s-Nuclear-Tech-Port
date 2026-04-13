package com.hbm.client;

import java.util.Arrays;

import com.hbm.attachment.HbmKey;
import com.hbm.network.payload.KeybindStatePayload;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class HbmClientInput {
    private static final boolean[] LAST_SENT_STATES = new boolean[HbmKey.values().length];
    private static boolean initialized;

    private HbmClientInput() {
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            reset();
            return;
        }

        for (HbmKey key : HbmKey.values()) {
            int keyIndex = key.ordinal();
            boolean pressed = HbmKeyMappings.isPressed(minecraft, key);

            if (!initialized) {
                LAST_SENT_STATES[keyIndex] = pressed;
                if (pressed) {
                    PacketDistributor.sendToServer(new KeybindStatePayload(key, true));
                }
                continue;
            }

            if (LAST_SENT_STATES[keyIndex] == pressed) {
                continue;
            }

            LAST_SENT_STATES[keyIndex] = pressed;
            PacketDistributor.sendToServer(new KeybindStatePayload(key, pressed));
        }

        initialized = true;
    }

    private static void reset() {
        Arrays.fill(LAST_SENT_STATES, false);
        initialized = false;
    }
}
