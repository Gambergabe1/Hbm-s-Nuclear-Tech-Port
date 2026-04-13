package com.hbm.attachment;

import com.hbm.registry.HbmAttachmentTypes;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class HbmAttachmentAccess {
    private HbmAttachmentAccess() {
    }

    public static HbmPlayerData player(Player player) {
        return player.getData(HbmAttachmentTypes.PLAYER_DATA);
    }

    public static HbmLivingData living(LivingEntity entity) {
        return entity.getData(HbmAttachmentTypes.LIVING_DATA);
    }
}
