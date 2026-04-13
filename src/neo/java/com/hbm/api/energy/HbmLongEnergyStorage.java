package com.hbm.api.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.function.BooleanSupplier;

public class HbmLongEnergyStorage {
    private long capacity;
    private long maxReceive;
    private long maxExtract;
    private long energy;
    private Runnable changeListener = () -> {
    };

    public HbmLongEnergyStorage(long capacity, long maxReceive, long maxExtract) {
        this.capacity = Math.max(0L, capacity);
        this.maxReceive = Math.max(0L, maxReceive);
        this.maxExtract = Math.max(0L, maxExtract);
    }

    public HbmLongEnergyStorage setChangeListener(Runnable changeListener) {
        this.changeListener = changeListener != null ? changeListener : () -> {
        };
        return this;
    }

    public long getEnergyStored() {
        return energy;
    }

    public long getMaxEnergyStored() {
        return capacity;
    }

    public long getMaxReceive() {
        return maxReceive;
    }

    public long getMaxExtract() {
        return maxExtract;
    }

    public void setEnergyStored(long energy) {
        long clamped = clamp(energy, 0L, capacity);
        if (this.energy == clamped) {
            return;
        }

        this.energy = clamped;
        onEnergyChanged();
    }

    public void setCapacity(long capacity) {
        long clampedCapacity = Math.max(0L, capacity);
        if (this.capacity == clampedCapacity && this.energy <= clampedCapacity) {
            return;
        }

        this.capacity = clampedCapacity;
        if (this.energy > clampedCapacity) {
            this.energy = clampedCapacity;
        }
        onEnergyChanged();
    }

    public void setTransferRates(long maxReceive, long maxExtract) {
        long clampedReceive = Math.max(0L, maxReceive);
        long clampedExtract = Math.max(0L, maxExtract);
        if (this.maxReceive == clampedReceive && this.maxExtract == clampedExtract) {
            return;
        }

        this.maxReceive = clampedReceive;
        this.maxExtract = clampedExtract;
        onEnergyChanged();
    }

    public long receiveEnergy(long toReceive, boolean simulate) {
        if (toReceive <= 0L || maxReceive <= 0L || energy >= capacity) {
            return 0L;
        }

        long received = Math.min(capacity - energy, Math.min(maxReceive, toReceive));
        if (!simulate && received > 0L) {
            energy += received;
            onEnergyChanged();
        }
        return received;
    }

    public long extractEnergy(long toExtract, boolean simulate) {
        if (toExtract <= 0L || maxExtract <= 0L || energy <= 0L) {
            return 0L;
        }

        long extracted = Math.min(energy, Math.min(maxExtract, toExtract));
        if (!simulate && extracted > 0L) {
            energy -= extracted;
            onEnergyChanged();
        }
        return extracted;
    }

    public IEnergyStorage createView(BooleanSupplier canReceive, BooleanSupplier canExtract) {
        BooleanSupplier receiveSupplier = canReceive != null ? canReceive : () -> true;
        BooleanSupplier extractSupplier = canExtract != null ? canExtract : () -> true;
        return new LongEnergyView(receiveSupplier, extractSupplier);
    }

    protected void onEnergyChanged() {
        changeListener.run();
    }

    private static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int toIntSaturated(long value) {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(0L, value));
    }

    private final class LongEnergyView implements IEnergyStorage {
        private final BooleanSupplier canReceive;
        private final BooleanSupplier canExtract;

        private LongEnergyView(BooleanSupplier canReceive, BooleanSupplier canExtract) {
            this.canReceive = canReceive;
            this.canExtract = canExtract;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) {
                return 0;
            }

            long received = HbmLongEnergyStorage.this.receiveEnergy(Math.max(0, maxReceive), simulate);
            return toIntSaturated(received);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract()) {
                return 0;
            }

            long extracted = HbmLongEnergyStorage.this.extractEnergy(Math.max(0, maxExtract), simulate);
            return toIntSaturated(extracted);
        }

        @Override
        public int getEnergyStored() {
            return toIntSaturated(energy);
        }

        @Override
        public int getMaxEnergyStored() {
            return toIntSaturated(capacity);
        }

        @Override
        public boolean canExtract() {
            return canExtract.getAsBoolean() && maxExtract > 0L;
        }

        @Override
        public boolean canReceive() {
            return canReceive.getAsBoolean() && maxReceive > 0L;
        }
    }
}
