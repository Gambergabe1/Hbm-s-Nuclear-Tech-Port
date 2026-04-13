package com.hbm.api.energy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.energy.EnergyStorage;

import java.util.function.BooleanSupplier;

public class HbmEnergyStorage extends EnergyStorage {
    private Runnable changeListener = () -> {};

    public HbmEnergyStorage(int capacity) {
        super(capacity);
    }

    public HbmEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public HbmEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public HbmEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public HbmEnergyStorage setChangeListener(Runnable changeListener) {
        this.changeListener = changeListener != null ? changeListener : () -> {};
        return this;
    }

    public void setEnergyStored(int energy) {
        int clamped = Mth.clamp(energy, 0, capacity);
        if (this.energy == clamped) {
            return;
        }

        this.energy = clamped;
        onEnergyChanged();
    }

    public void setCapacity(int capacity) {
        int clampedCapacity = Math.max(0, capacity);
        if (this.capacity == clampedCapacity && this.energy <= clampedCapacity) {
            return;
        }

        this.capacity = clampedCapacity;
        if (this.energy > clampedCapacity) {
            this.energy = clampedCapacity;
        }
        onEnergyChanged();
    }

    public void setTransferRates(int maxReceive, int maxExtract) {
        int clampedReceive = Math.max(0, maxReceive);
        int clampedExtract = Math.max(0, maxExtract);
        if (this.maxReceive == clampedReceive && this.maxExtract == clampedExtract) {
            return;
        }

        this.maxReceive = clampedReceive;
        this.maxExtract = clampedExtract;
        onEnergyChanged();
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        int received = super.receiveEnergy(toReceive, simulate);
        if (!simulate && received > 0) {
            onEnergyChanged();
        }
        return received;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        int extracted = super.extractEnergy(toExtract, simulate);
        if (!simulate && extracted > 0) {
            onEnergyChanged();
        }
        return extracted;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
        int previous = this.energy;
        super.deserializeNBT(provider, nbt);
        this.energy = Mth.clamp(this.energy, 0, this.capacity);
        if (this.energy != previous) {
            onEnergyChanged();
        }
    }

    public IEnergyStorage createView(BooleanSupplier canReceive, BooleanSupplier canExtract) {
        BooleanSupplier receiveSupplier = canReceive != null ? canReceive : () -> true;
        BooleanSupplier extractSupplier = canExtract != null ? canExtract : () -> true;
        return new EnergyView(receiveSupplier, extractSupplier);
    }

    protected void onEnergyChanged() {
        changeListener.run();
    }

    private final class EnergyView implements IEnergyStorage {
        private final BooleanSupplier canReceive;
        private final BooleanSupplier canExtract;

        private EnergyView(BooleanSupplier canReceive, BooleanSupplier canExtract) {
            this.canReceive = canReceive;
            this.canExtract = canExtract;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) {
                return 0;
            }
            return HbmEnergyStorage.this.receiveEnergy(Math.max(0, maxReceive), simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract()) {
                return 0;
            }
            return HbmEnergyStorage.this.extractEnergy(Math.max(0, maxExtract), simulate);
        }

        @Override
        public int getEnergyStored() {
            return HbmEnergyStorage.this.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return HbmEnergyStorage.this.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return canExtract.getAsBoolean() && HbmEnergyStorage.this.maxExtract > 0;
        }

        @Override
        public boolean canReceive() {
            return canReceive.getAsBoolean() && HbmEnergyStorage.this.maxReceive > 0;
        }
    }
}
