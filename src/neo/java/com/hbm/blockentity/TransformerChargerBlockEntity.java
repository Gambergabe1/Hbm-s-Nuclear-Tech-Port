package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.block.TransformerChargerBlock;
import com.hbm.registry.HbmBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Locale;

public final class TransformerChargerBlockEntity extends AbstractEnergyMachineBlockEntity {
    public static final int RANGE = 3;
    public static final int STANDARD_RATE = 50_000;
    public static final int STANDARD_CAPACITY = 1_000_000;

    private final IEnergyStorage energyCapability = energyStorage.createView(() -> true, () -> false);

    private long totalCapacity;
    private long totalEnergy;
    private int requestedChargeRate;
    private int actualChargeRate;
    private int maxChargeRate = STANDARD_RATE;
    private boolean pointingUp = true;

    public TransformerChargerBlockEntity(BlockPos pos, BlockState blockState) {
        super(HbmBlockEntityTypes.MACHINE_TRANSFORMER.get(), pos, blockState, 0, STANDARD_CAPACITY, STANDARD_RATE, STANDARD_RATE);
        updateConfigFromBlock();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TransformerChargerBlockEntity blockEntity) {
        blockEntity.tickServer();
    }

    public IEnergyStorage getEnergyCapability() {
        return energyCapability;
    }

    public int chargeFromHeldItem(ItemStack stack) {
        updateConfigFromBlock();
        return HbmEnergyHelper.chargeStorageFromItem(stack, energyStorage);
    }

    public Component createStatusMessage() {
        String stored = formatNumber(energyStorage.getEnergyStored()) + "/" + formatNumber(energyStorage.getMaxEnergyStored()) + " HE";
        if (totalCapacity <= 0L) {
            return Component.literal("Stored " + stored + " | Nothing to charge");
        }

        return Component.literal(
            "Stored " + stored
                + " | Charge " + formatNumber((long) actualChargeRate * 20L)
                + "/" + formatNumber((long) requestedChargeRate * 20L)
                + " HE/s"
                + " | Nearby " + formatNumber(totalEnergy)
                + "/" + formatNumber(totalCapacity)
                + " HE"
        );
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    private void tickServer() {
        updateConfigFromBlock();

        long previousCapacity = totalCapacity;
        long previousEnergy = totalEnergy;
        int previousRequested = requestedChargeRate;
        int previousActual = actualChargeRate;

        recomputeNearbyDemand();

        int budget = Math.min(maxChargeRate, energyStorage.getEnergyStored());
        actualChargeRate = budget > 0 ? distributeCharge(budget) : 0;

        if (previousCapacity != totalCapacity
            || previousEnergy != totalEnergy
            || previousRequested != requestedChargeRate
            || previousActual != actualChargeRate) {
            setChanged();
        }
    }

    private void updateConfigFromBlock() {
        if (!(getBlockState().getBlock() instanceof TransformerChargerBlock chargerBlock)) {
            return;
        }

        maxChargeRate = chargerBlock.getMaxChargeRate();
        pointingUp = chargerBlock.isPointingUp();
        energyStorage.setCapacity(chargerBlock.getEnergyCapacity());
        energyStorage.setTransferRates(maxChargeRate, maxChargeRate);
    }

    private void recomputeNearbyDemand() {
        totalCapacity = 0L;
        totalEnergy = 0L;
        requestedChargeRate = 0;

        for (Player player : getNearbyPlayers()) {
            Inventory inventory = player.getInventory();
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                ItemStack stack = inventory.getItem(slot);
                int demand = HbmEnergyHelper.getChargeDemand(stack);
                if (demand <= 0) {
                    continue;
                }

                totalCapacity += HbmEnergyHelper.getMaxStoredEnergy(stack);
                totalEnergy += HbmEnergyHelper.getStoredEnergy(stack);
                requestedChargeRate = clampAdd(requestedChargeRate, demand);
            }
        }
    }

    private int distributeCharge(int maxTransfer) {
        int transferred = 0;

        for (Player player : getNearbyPlayers()) {
            Inventory inventory = player.getInventory();
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                if (transferred >= maxTransfer) {
                    return transferred;
                }

                int moved = HbmEnergyHelper.dischargeStorageToItem(
                    energyStorage,
                    inventory.getItem(slot),
                    maxTransfer - transferred
                );
                if (moved > 0) {
                    transferred += moved;
                }
            }
        }

        return transferred;
    }

    private List<Player> getNearbyPlayers() {
        if (level == null) {
            return List.of();
        }

        double minX = worldPosition.getX();
        double minZ = worldPosition.getZ();
        double maxX = minX + 1.0D;
        double maxZ = minZ + 1.0D;
        double minY = pointingUp ? worldPosition.getY() : worldPosition.getY() - RANGE;
        double maxY = pointingUp ? worldPosition.getY() + RANGE + 1.0D : worldPosition.getY() + 1.0D;

        return level.getEntitiesOfClass(Player.class, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    private static int clampAdd(int current, int value) {
        if (value <= 0) {
            return current;
        }
        if (current > Integer.MAX_VALUE - value) {
            return Integer.MAX_VALUE;
        }
        return current + value;
    }

    private static String formatNumber(long value) {
        return String.format(Locale.ROOT, "%,d", value);
    }
}
