package com.hbm.attachment;

import java.util.ArrayList;
import java.util.List;

import com.hbm.network.HbmNetwork;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public final class HbmLivingData extends EntityAttachmentData {
    private float rads;
    private float neutrons;
    private float environmentRads;
    private float radiationBuffer;
    private float digamma;
    private int asbestos;
    private int blacklung;
    private int bombTimer;
    private int contagion;
    private final List<ContaminationEffect> contaminationEffects = new ArrayList<>();

    public HbmLivingData(IAttachmentHolder holder) {
        super(holder);
    }

    public float getRads() {
        return rads;
    }

    public void setRads(float rads) {
        float clamped = Mth.clamp(rads, 0.0F, 2500.0F);
        if (this.rads != clamped) {
            this.rads = clamped;
            syncIfServer();
        }
    }

    public void increaseRads(float rads) {
        setRads(this.rads + rads);
    }

    public void decreaseRads(float rads) {
        setRads(this.rads - rads);
    }

    public float getNeutrons() {
        return neutrons;
    }

    public void setNeutrons(float neutrons) {
        float clamped = Math.max(neutrons, 0.0F);
        if (this.neutrons != clamped) {
            this.neutrons = clamped;
            syncIfServer();
        }
    }

    public float getEnvironmentRads() {
        return environmentRads;
    }

    public void setEnvironmentRads(float environmentRads) {
        if (this.environmentRads != environmentRads) {
            this.environmentRads = environmentRads;
            syncIfServer();
        }
    }

    public float getRadiationBuffer() {
        return radiationBuffer;
    }

    public void setRadiationBuffer(float radiationBuffer) {
        if (this.radiationBuffer != radiationBuffer) {
            this.radiationBuffer = radiationBuffer;
            syncIfServer();
        }
    }

    public float getDigamma() {
        return digamma;
    }

    public void setDigamma(float digamma) {
        if (this.digamma != digamma) {
            this.digamma = digamma;
            syncIfServer();
        }
    }

    public void increaseDigamma(float digamma) {
        setDigamma(Mth.clamp(this.digamma + digamma, 0.0F, 1000.0F));
    }

    public void decreaseDigamma(float digamma) {
        setDigamma(Mth.clamp(this.digamma - digamma, 0.0F, 1000.0F));
    }

    public int getAsbestos() {
        return asbestos;
    }

    public void setAsbestos(int asbestos) {
        if (this.asbestos != asbestos) {
            this.asbestos = asbestos;
            syncIfServer();
        }
    }

    public int getBlacklung() {
        return blacklung;
    }

    public void setBlacklung(int blacklung) {
        if (this.blacklung != blacklung) {
            this.blacklung = blacklung;
            syncIfServer();
        }
    }

    public int getBombTimer() {
        return bombTimer;
    }

    public void setBombTimer(int bombTimer) {
        if (this.bombTimer != bombTimer) {
            this.bombTimer = bombTimer;
            syncIfServer();
        }
    }

    public int getContagion() {
        return contagion;
    }

    public void setContagion(int contagion) {
        if (this.contagion != contagion) {
            this.contagion = contagion;
            syncIfServer();
        }
    }

    public List<ContaminationEffect> getContaminationEffects() {
        return List.copyOf(contaminationEffects);
    }

    public void setContaminationEffects(List<ContaminationEffect> contaminationEffects) {
        this.contaminationEffects.clear();
        this.contaminationEffects.addAll(contaminationEffects);
        syncIfServer();
    }

    public void addContaminationEffect(ContaminationEffect effect) {
        contaminationEffects.add(effect);
        syncIfServer();
    }

    public void clearContaminationEffects() {
        if (!contaminationEffects.isEmpty()) {
            contaminationEffects.clear();
            syncIfServer();
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag contamination = new ListTag();

        for (ContaminationEffect effect : contaminationEffects) {
            contamination.add(effect.toTag());
        }

        tag.putFloat("rads", rads);
        tag.putFloat("neutrons", neutrons);
        tag.putFloat("envRads", environmentRads);
        tag.putFloat("radBuf", radiationBuffer);
        tag.putFloat("digamma", digamma);
        tag.putInt("asbestos", asbestos);
        tag.putInt("blacklung", blacklung);
        tag.putInt("bombtimer", bombTimer);
        tag.putInt("contagion", contagion);
        tag.put("contamination", contamination);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        rads = Mth.clamp(nbt.getFloat("rads"), 0.0F, 2500.0F);
        neutrons = Math.max(nbt.getFloat("neutrons"), 0.0F);
        environmentRads = nbt.getFloat("envRads");
        radiationBuffer = nbt.getFloat("radBuf");
        digamma = nbt.getFloat("digamma");
        asbestos = nbt.getInt("asbestos");
        blacklung = nbt.getInt("blacklung");
        bombTimer = nbt.getInt("bombtimer");
        contagion = nbt.getInt("contagion");

        contaminationEffects.clear();
        ListTag contamination = nbt.getList("contamination", Tag.TAG_COMPOUND);
        for (int i = 0; i < contamination.size(); i++) {
            contaminationEffects.add(ContaminationEffect.fromTag(contamination.getCompound(i)));
        }
    }

    @Override
    protected void sync(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            HbmNetwork.syncLivingAttachment(livingEntity);
        }
    }

    public record ContaminationEffect(float maxRadiation, int maxTime, int time, boolean ignoreArmor) {
        public float currentRadiation() {
            if (maxTime <= 0) {
                return 0.0F;
            }

            return maxRadiation * ((float) time / (float) maxTime);
        }

        private CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("maxRadiation", maxRadiation);
            tag.putInt("maxTime", maxTime);
            tag.putInt("time", time);
            tag.putBoolean("ignoreArmor", ignoreArmor);
            return tag;
        }

        private static ContaminationEffect fromTag(CompoundTag tag) {
            return new ContaminationEffect(
                tag.getFloat("maxRadiation"),
                tag.getInt("maxTime"),
                tag.getInt("time"),
                tag.getBoolean("ignoreArmor")
            );
        }
    }
}
