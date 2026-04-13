package com.hbm.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

abstract class EntityAttachmentData implements INBTSerializable<CompoundTag> {
    protected final IAttachmentHolder holder;

    protected EntityAttachmentData(IAttachmentHolder holder) {
        this.holder = holder;
    }

    protected abstract void sync(Entity entity);

    protected final void syncIfServer() {
        if (holder instanceof Entity entity && !entity.level().isClientSide()) {
            sync(entity);
        }
    }
}
