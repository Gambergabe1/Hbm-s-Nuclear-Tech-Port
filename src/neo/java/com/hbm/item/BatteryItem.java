package com.hbm.item;

import java.util.List;
import java.util.Locale;

import com.hbm.registry.HbmDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class BatteryItem extends Item {

    private final int capacity;
    private final int chargeRate;
    private final int dischargeRate;

    public BatteryItem(Properties properties, int capacity, int chargeRate, int dischargeRate) {
        super(properties);
        this.capacity = Math.max(0, capacity);
        this.chargeRate = Math.max(0, chargeRate);
        this.dischargeRate = Math.max(0, dischargeRate);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getChargeRate() {
        return chargeRate;
    }

    public int getDischargeRate() {
        return dischargeRate;
    }

    public int getStoredEnergy(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        Integer energy = stack.get(HbmDataComponents.ENERGY);
        if (energy == null) {
            return capacity;
        }
        return Math.max(0, Math.min(capacity, energy));
    }

    public void setStoredEnergy(ItemStack stack, int energy) {
        int clampedEnergy = Math.max(0, Math.min(capacity, energy));
        if (clampedEnergy >= capacity) {
            stack.remove(HbmDataComponents.ENERGY);
        } else {
            stack.set(HbmDataComponents.ENERGY, clampedEnergy);
        }
    }

    public ItemStack createEmptyStack() {
        ItemStack stack = new ItemStack(this);
        setStoredEnergy(stack, 0);
        return stack;
    }

    public IEnergyStorage createEnergyStorage(ItemStack stack) {
        return new StackBatteryEnergyStorage(this, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int charge = getStoredEnergy(stack);
        tooltipComponents.add(
            Component.translatable("desc.energystore")
                .append(Component.literal(" " + formatNumber(charge) + "/" + formatNumber(capacity) + " HE"))
                .withStyle(ChatFormatting.GOLD)
        );
        tooltipComponents.add(
            Component.translatable("desc.energychargerate")
                .append(Component.literal(" " + formatNumber(chargeRate * 20) + " HE/s"))
                .withStyle(ChatFormatting.GREEN)
        );
        tooltipComponents.add(
            Component.translatable("desc.energydchargerate")
                .append(Component.literal(" " + formatNumber(dischargeRate * 20) + " HE/s"))
                .withStyle(ChatFormatting.RED)
        );
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (capacity <= 0) {
            return 0;
        }
        return Math.round(13.0F * ((float) getStoredEnergy(stack) / (float) capacity));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00D65A;
    }

    private static String formatNumber(int value) {
        return String.format(Locale.ROOT, "%,d", value);
    }

    private static final class StackBatteryEnergyStorage implements IEnergyStorage {
        private final BatteryItem batteryItem;
        private final ItemStack stack;

        private StackBatteryEnergyStorage(BatteryItem batteryItem, ItemStack stack) {
            this.batteryItem = batteryItem;
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive() || maxReceive <= 0) {
                return 0;
            }

            int stored = batteryItem.getStoredEnergy(stack);
            int received = Math.min(batteryItem.getChargeRate(), Math.min(maxReceive, batteryItem.getCapacity() - stored));
            if (!simulate && received > 0) {
                batteryItem.setStoredEnergy(stack, stored + received);
            }
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract() || maxExtract <= 0) {
                return 0;
            }

            int stored = batteryItem.getStoredEnergy(stack);
            int extracted = Math.min(batteryItem.getDischargeRate(), Math.min(maxExtract, stored));
            if (!simulate && extracted > 0) {
                batteryItem.setStoredEnergy(stack, stored - extracted);
            }
            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return batteryItem.getStoredEnergy(stack);
        }

        @Override
        public int getMaxEnergyStored() {
            return batteryItem.getCapacity();
        }

        @Override
        public boolean canExtract() {
            return batteryItem.getDischargeRate() > 0;
        }

        @Override
        public boolean canReceive() {
            return batteryItem.getChargeRate() > 0;
        }
    }
}
