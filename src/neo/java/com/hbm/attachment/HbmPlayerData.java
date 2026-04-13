package com.hbm.attachment;

import com.hbm.network.HbmNetwork;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public final class HbmPlayerData extends EntityAttachmentData {
    private final boolean[] keyStates = new boolean[HbmKey.values().length];
    private boolean backpackEnabled = true;
    private boolean hudEnabled = true;

    public HbmPlayerData(IAttachmentHolder holder) {
        super(holder);
    }

    public boolean getKeyPressed(HbmKey key) {
        return keyStates[key.ordinal()];
    }

    public void setKeyPressed(HbmKey key, boolean pressed) {
        int keyIndex = key.ordinal();
        boolean previous = keyStates[keyIndex];
        keyStates[keyIndex] = pressed;

        boolean changed = previous != pressed;
        if (!previous && pressed) {
            if (key == HbmKey.TOGGLE_JETPACK) {
                backpackEnabled = !backpackEnabled;
                changed = true;
            } else if (key == HbmKey.TOGGLE_HEAD) {
                hudEnabled = !hudEnabled;
                changed = true;
            }
        }

        if (changed) {
            syncIfServer();
        }
    }

    public boolean isBackpackEnabled() {
        return backpackEnabled;
    }

    public void setBackpackEnabled(boolean enabled) {
        if (backpackEnabled != enabled) {
            backpackEnabled = enabled;
            syncIfServer();
        }
    }

    public boolean isHudEnabled() {
        return hudEnabled;
    }

    public void setHudEnabled(boolean enabled) {
        if (hudEnabled != enabled) {
            hudEnabled = enabled;
            syncIfServer();
        }
    }

    public boolean isJetpackActive() {
        return backpackEnabled && getKeyPressed(HbmKey.JETPACK);
    }

    public HbmPlayerData copyForRespawn(IAttachmentHolder holder) {
        HbmPlayerData copy = new HbmPlayerData(holder);
        copy.backpackEnabled = backpackEnabled;
        copy.hudEnabled = hudEnabled;
        return copy;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        int keyMask = 0;
        for (int i = 0; i < keyStates.length; i++) {
            if (keyStates[i]) {
                keyMask |= 1 << i;
            }
        }

        tag.putInt("key_mask", keyMask);
        tag.putBoolean("enableBackpack", backpackEnabled);
        tag.putBoolean("enableHUD", hudEnabled);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        int keyMask = nbt.getInt("key_mask");
        for (int i = 0; i < keyStates.length; i++) {
            keyStates[i] = (keyMask & (1 << i)) != 0;
        }

        backpackEnabled = !nbt.contains("enableBackpack") || nbt.getBoolean("enableBackpack");
        hudEnabled = !nbt.contains("enableHUD") || nbt.getBoolean("enableHUD");
    }

    @Override
    protected void sync(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            HbmNetwork.syncPlayerAttachment(player);
        }
    }
}
