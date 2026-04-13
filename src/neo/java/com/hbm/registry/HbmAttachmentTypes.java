package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.attachment.HbmLivingData;
import com.hbm.attachment.HbmPlayerData;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class HbmAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
        NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
        HbmNuclearTech.MODID
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HbmPlayerData>> PLAYER_DATA = ATTACHMENT_TYPES.register(
        "player_data",
        () -> AttachmentType.serializable(HbmPlayerData::new)
            .copyHandler((attachment, holder, provider) -> attachment.copyForRespawn(holder))
            .copyOnDeath()
            .build()
    );
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<HbmLivingData>> LIVING_DATA = ATTACHMENT_TYPES.register(
        "living_data",
        () -> AttachmentType.serializable(HbmLivingData::new).build()
    );

    private HbmAttachmentTypes() {
    }
}
