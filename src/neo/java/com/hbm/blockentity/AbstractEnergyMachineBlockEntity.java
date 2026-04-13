package com.hbm.blockentity;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.api.energy.HbmEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.hbm.api.block.IDamageableMachine;

public abstract class AbstractEnergyMachineBlockEntity extends AbstractMachineBlockEntity implements IDamageableMachine {
    protected final HbmEnergyStorage energyStorage;
    protected int damage;

    protected AbstractEnergyMachineBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState,
        int slotCount,
        int energyCapacity,
        int maxReceive,
        int maxExtract
    ) {
        super(type, pos, blockState, slotCount);
        this.energyStorage = new HbmEnergyStorage(
            Math.max(0, energyCapacity),
            Math.max(0, maxReceive),
            Math.max(0, maxExtract)
        ) {
            @Override
            protected void onEnergyChanged() {
                AbstractEnergyMachineBlockEntity.this.setChanged();
            }
        };
    }

    @Override
    public int getMaxDamage() {
        return 1000;
    }

    @Override
    public int getMachineDamage() {
        return damage;
    }

    @Override
    public void setMachineDamage(int damage) {
        this.damage = damage;
        this.setChanged();
    }

    @Override
    public void onMachineDestroyed(Level level, BlockPos pos) {
        level.destroyBlock(pos, true);
        com.hbm.explosion.ExplosionLarge.explode(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0F, true, true, true);
    }

    protected final ContainerData createEnergyProgressData(IntSupplier progressGetter, IntConsumer progressSetter) {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> progressGetter.getAsInt();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> energyStorage.setEnergyStored(value);
                    case 1 -> progressSetter.accept(value);
                    default -> {
                    }
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    protected final int chargeFromBatterySlot(int slot) {
        if (slot < 0 || slot >= items.size()) {
            return 0;
        }
        return HbmEnergyHelper.chargeStorageFromItem(items.get(slot), energyStorage);
    }

    public HbmEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void loadSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        energyStorage.setEnergyStored(tag.getInt("power"));
        this.damage = tag.getInt("machine_damage");
        super.loadSharedMachineData(tag, registries);
    }

    @Override
    protected void saveSharedMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("power", energyStorage.getEnergyStored());
        tag.putInt("machine_damage", this.damage);
        super.saveSharedMachineData(tag, registries);
    }
}
