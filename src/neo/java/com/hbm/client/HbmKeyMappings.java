package com.hbm.client;

import com.hbm.attachment.HbmKey;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class HbmKeyMappings {
    private static final String CATEGORY = "key.categories.hbm";

    private static final KeyMapping TOGGLE_JETPACK_KEY = new KeyMapping(
        CATEGORY + ".toggleBack",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_C,
        CATEGORY
    );
    private static final KeyMapping TOGGLE_HUD_KEY = new KeyMapping(
        CATEGORY + ".toggleHUD",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_V,
        CATEGORY
    );
    private static final KeyMapping RELOAD_KEY = new KeyMapping(
        CATEGORY + ".reload",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY
    );
    private static final KeyMapping CRANE_UP_KEY = new KeyMapping(
        CATEGORY + ".craneMoveUp",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UP,
        CATEGORY
    );
    private static final KeyMapping CRANE_DOWN_KEY = new KeyMapping(
        CATEGORY + ".craneMoveDown",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_DOWN,
        CATEGORY
    );
    private static final KeyMapping CRANE_LEFT_KEY = new KeyMapping(
        CATEGORY + ".craneMoveLeft",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_LEFT,
        CATEGORY
    );
    private static final KeyMapping CRANE_RIGHT_KEY = new KeyMapping(
        CATEGORY + ".craneMoveRight",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_RIGHT,
        CATEGORY
    );
    private static final KeyMapping CRANE_LOAD_KEY = new KeyMapping(
        CATEGORY + ".craneLoad",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_ENTER,
        CATEGORY
    );

    private HbmKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_JETPACK_KEY);
        event.register(TOGGLE_HUD_KEY);
        event.register(RELOAD_KEY);
        event.register(CRANE_UP_KEY);
        event.register(CRANE_DOWN_KEY);
        event.register(CRANE_LEFT_KEY);
        event.register(CRANE_RIGHT_KEY);
        event.register(CRANE_LOAD_KEY);
    }

    public static boolean isPressed(Minecraft minecraft, HbmKey key) {
        return switch (key) {
            case JETPACK -> minecraft.options.keyJump.isDown();
            case TOGGLE_JETPACK -> TOGGLE_JETPACK_KEY.isDown();
            case TOGGLE_HEAD -> TOGGLE_HUD_KEY.isDown();
            case RELOAD -> RELOAD_KEY.isDown();
            case CRANE_UP -> CRANE_UP_KEY.isDown();
            case CRANE_DOWN -> CRANE_DOWN_KEY.isDown();
            case CRANE_LEFT -> CRANE_LEFT_KEY.isDown();
            case CRANE_RIGHT -> CRANE_RIGHT_KEY.isDown();
            case CRANE_LOAD -> CRANE_LOAD_KEY.isDown();
        };
    }
}
