package com.hbm.api.energy;

import com.hbm.blockentity.BatteryStorageBlockEntity;
import com.hbm.blockentity.EnergyCableBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class HbmEnergyHelper {
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    private HbmEnergyHelper() {
    }

    public static boolean canDischargeIntoMachine(ItemStack stack) {
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage != null && storage.canExtract() && storage.getEnergyStored() > 0;
    }

    public static int chargeStorageFromItem(ItemStack stack, IEnergyStorage receivingStorage) {
        if (stack.isEmpty()) {
            return 0;
        }

        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (itemEnergy == null || !itemEnergy.canExtract()) {
            return 0;
        }

        int toReceive = receivingStorage.receiveEnergy(Integer.MAX_VALUE, true);
        if (toReceive <= 0) {
            return 0;
        }

        int extracted = itemEnergy.extractEnergy(toReceive, false);
        if (extracted <= 0) {
            return 0;
        }

        return receivingStorage.receiveEnergy(extracted, false);
    }

    public static int getChargeDemand(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (itemEnergy == null || !itemEnergy.canReceive()) {
            return 0;
        }

        return itemEnergy.receiveEnergy(Integer.MAX_VALUE, true);
    }

    public static int getStoredEnergy(ItemStack stack) {
        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return itemEnergy != null ? itemEnergy.getEnergyStored() : 0;
    }

    public static int getMaxStoredEnergy(ItemStack stack) {
        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return itemEnergy != null ? itemEnergy.getMaxEnergyStored() : 0;
    }

    public static int dischargeStorageToItem(IEnergyStorage sourceStorage, ItemStack stack, int maxTransfer) {
        if (stack.isEmpty() || maxTransfer <= 0) {
            return 0;
        }

        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (itemEnergy == null || !itemEnergy.canReceive()) {
            return 0;
        }

        int toExtract = sourceStorage.extractEnergy(maxTransfer, true);
        if (toExtract <= 0) {
            return 0;
        }

        int received = itemEnergy.receiveEnergy(toExtract, false);
        if (received <= 0) {
            return 0;
        }

        return sourceStorage.extractEnergy(received, false);
    }

    public static EnergyRoutingMetadata getEndpointRouting(BlockEntity blockEntity) {
        if (blockEntity instanceof BatteryStorageBlockEntity battery) {
            return new EnergyRoutingMetadata(battery.getPriorityOrdinal(), true);
        }
        if (blockEntity instanceof EnergyCableBlockEntity cable) {
            return new EnergyRoutingMetadata(cable.getRoutingPriorityOrdinal(), false);
        }
        return EnergyRoutingMetadata.DEFAULT;
    }

    public static EnergyTransferEndpoint createTransferEndpoint(IEnergyStorage storage, BlockEntity blockEntity, long sortKey) {
        EnergyRoutingMetadata routing = getEndpointRouting(blockEntity);
        return new EnergyTransferEndpoint(storage, routing.priority(), routing.storage(), sortKey);
    }

    public static boolean canTransferToStorage(
        int senderPriority,
        boolean senderStorage,
        int receiverPriority,
        boolean receiverStorage
    ) {
        if (!senderStorage || !receiverStorage) {
            return true;
        }
        return clampPriority(senderPriority) <= clampPriority(receiverPriority);
    }

    public static boolean canExtractFromStorage(
        int requesterPriority,
        boolean requesterStorage,
        int providerPriority,
        boolean providerStorage
    ) {
        if (!requesterStorage || !providerStorage) {
            return true;
        }
        return clampPriority(providerPriority) <= clampPriority(requesterPriority);
    }

    public static int fairReceive(List<EnergyTransferEndpoint> endpoints, int maxReceive, boolean simulate) {
        return fairTransfer(endpoints, maxReceive, simulate, true);
    }

    public static int fairExtract(List<EnergyTransferEndpoint> endpoints, int maxExtract, boolean simulate) {
        return fairTransfer(endpoints, maxExtract, simulate, false);
    }

    private static int fairTransfer(
        List<EnergyTransferEndpoint> endpoints,
        int amount,
        boolean simulate,
        boolean receiving
    ) {
        if (amount <= 0 || endpoints.isEmpty()) {
            return 0;
        }

        List<EnergyTransferEndpoint> sorted = new ArrayList<>(endpoints);
        sorted.sort(
            Comparator.comparingInt(EnergyTransferEndpoint::priority).reversed()
                .thenComparingLong(EnergyTransferEndpoint::sortKey)
        );

        int remaining = amount;
        int transferred = 0;
        int index = 0;

        while (index < sorted.size() && remaining > 0) {
            int priority = sorted.get(index).priority();
            int groupEnd = index + 1;
            while (groupEnd < sorted.size() && sorted.get(groupEnd).priority() == priority) {
                groupEnd++;
            }

            int moved = fairTransferGroup(sorted, index, groupEnd, remaining, simulate, receiving);
            transferred += moved;
            remaining -= moved;
            index = groupEnd;
        }

        return transferred;
    }

    private static int fairTransferGroup(
        List<EnergyTransferEndpoint> endpoints,
        int start,
        int end,
        int budget,
        boolean simulate,
        boolean receiving
    ) {
        int size = end - start;
        int[] capacities = new int[size];
        long totalCapacity = 0L;

        for (int index = 0; index < size; index++) {
            IEnergyStorage storage = endpoints.get(start + index).storage();
            int capacity = receiving ? storage.receiveEnergy(budget, true) : storage.extractEnergy(budget, true);
            capacities[index] = Math.max(0, capacity);
            totalCapacity += capacities[index];
        }

        if (totalCapacity <= 0L) {
            return 0;
        }

        int[] allocations = new int[size];
        long[] remainders = new long[size];
        int assigned = 0;

        for (int index = 0; index < size; index++) {
            if (capacities[index] <= 0) {
                continue;
            }

            long weighted = (long) capacities[index] * (long) budget;
            allocations[index] = (int) Math.min(capacities[index], weighted / totalCapacity);
            remainders[index] = weighted % totalCapacity;
            assigned += allocations[index];
        }

        int leftover = budget - assigned;
        if (leftover > 0) {
            Integer[] order = new Integer[size];
            for (int index = 0; index < size; index++) {
                order[index] = index;
            }

            Arrays.sort(
                order,
                Comparator.<Integer>comparingLong(index -> remainders[index]).reversed()
                    .thenComparingLong(index -> endpoints.get(start + index).sortKey())
            );

            for (int index : order) {
                if (leftover <= 0) {
                    break;
                }

                int extraCapacity = capacities[index] - allocations[index];
                if (extraCapacity <= 0) {
                    continue;
                }

                int extra = Math.min(extraCapacity, leftover);
                allocations[index] += extra;
                leftover -= extra;
            }
        }

        int transferred = 0;
        for (int index = 0; index < size; index++) {
            int allocation = allocations[index];
            if (allocation <= 0) {
                continue;
            }

            IEnergyStorage storage = endpoints.get(start + index).storage();
            int moved = receiving ? storage.receiveEnergy(allocation, simulate) : storage.extractEnergy(allocation, simulate);
            transferred += Math.max(0, moved);
        }

        return transferred;
    }

    public static int clampPriority(int priority) {
        return Math.max(PRIORITY_LOW, Math.min(PRIORITY_HIGH, priority));
    }

    public record EnergyRoutingMetadata(int priority, boolean storage) {
        public static final EnergyRoutingMetadata DEFAULT = new EnergyRoutingMetadata(PRIORITY_NORMAL, false);

        public EnergyRoutingMetadata {
            priority = clampPriority(priority);
        }
    }

    public record EnergyTransferEndpoint(IEnergyStorage storage, int priority, boolean storageEndpoint, long sortKey) {
        public EnergyTransferEndpoint {
            priority = clampPriority(priority);
        }
    }
}
