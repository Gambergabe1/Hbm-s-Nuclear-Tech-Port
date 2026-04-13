package com.hbm.menu;

import com.hbm.api.energy.HbmEnergyHelper;
import com.hbm.block.BatteryStorageBlock;
import com.hbm.blockentity.BatteryStorageBlockEntity;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class BatteryStorageMenu extends AbstractContainerMenu {
    private static final int MACHINE_SLOT_END = 4;
    private static final int PLAYER_SLOT_START = 4;
    private static final int PLAYER_SLOT_END = 40;

    private final Container container;
    private final ContainerData data;
    private final BlockPos pos;
    private final ContainerLevelAccess access;

    public BatteryStorageMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(BatteryStorageBlockEntity.SLOT_COUNT),
            new SimpleContainerData(7),
            BlockPos.ZERO
        );
    }

    public BatteryStorageMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(BatteryStorageBlockEntity.SLOT_COUNT),
            new SimpleContainerData(7),
            buffer.readBlockPos()
        );
    }

    public BatteryStorageMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, BlockPos pos) {
        super(HbmMenuTypes.MACHINE_BATTERY.get(), containerId);
        checkContainerSize(container, BatteryStorageBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, 7);
        this.container = container;
        this.data = data;
        this.pos = pos;
        if (container instanceof BatteryStorageBlockEntity blockEntity && blockEntity.getLevel() != null) {
            this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        } else if (!BlockPos.ZERO.equals(pos)) {
            this.access = ContainerLevelAccess.create(playerInventory.player.level(), pos);
        } else {
            this.access = ContainerLevelAccess.NULL;
        }

        addSlot(new Slot(container, BatteryStorageBlockEntity.SLOT_DISCHARGE_IN, 35, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return HbmEnergyHelper.canDischargeIntoMachine(stack);
            }
        });
        addSlot(new Slot(container, BatteryStorageBlockEntity.SLOT_DISCHARGE_OUT, 35, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        addSlot(new Slot(container, BatteryStorageBlockEntity.SLOT_CHARGE_IN, 125, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return HbmEnergyHelper.getChargeDemand(stack) > 0;
            }
        });
        addSlot(new Slot(container, BatteryStorageBlockEntity.SLOT_CHARGE_OUT, 125, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int hotbar = 0; hotbar < 9; hotbar++) {
            addSlot(new Slot(playerInventory, hotbar, 8 + hotbar * 18, 142));
        }

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        if (container instanceof BatteryStorageBlockEntity battery) {
            return battery.stillValid(player);
        }

        return access.evaluate(
            (level, blockPos) -> level.getBlockEntity(blockPos) instanceof BatteryStorageBlockEntity battery && battery.stillValid(player),
            true
        );
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (container instanceof BatteryStorageBlockEntity battery) {
            switch (id) {
                case 0 -> battery.cycleRedLowMode();
                case 1 -> battery.cycleRedHighMode();
                case 2 -> battery.cyclePriority();
                default -> {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack quickMoved = stack.copy();

        if (index < MACHINE_SLOT_END) {
            if (!moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (HbmEnergyHelper.canDischargeIntoMachine(stack)) {
            if (!moveItemStackTo(stack, BatteryStorageBlockEntity.SLOT_DISCHARGE_IN, BatteryStorageBlockEntity.SLOT_DISCHARGE_IN + 1, false)
                && (!canChargeItem(stack)
                || !moveItemStackTo(stack, BatteryStorageBlockEntity.SLOT_CHARGE_IN, BatteryStorageBlockEntity.SLOT_CHARGE_IN + 1, false))) {
                return ItemStack.EMPTY;
            }
        } else if (canChargeItem(stack)) {
            if (!moveItemStackTo(stack, BatteryStorageBlockEntity.SLOT_CHARGE_IN, BatteryStorageBlockEntity.SLOT_CHARGE_IN + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < PLAYER_SLOT_START + 27) {
            if (!moveItemStackTo(stack, PLAYER_SLOT_START + 27, PLAYER_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_SLOT_START + 27, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == quickMoved.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return quickMoved;
    }

    public long getStoredPower() {
        return combineLong(data.get(0), data.get(1));
    }

    public long getPowerDelta() {
        return combineLong(data.get(2), data.get(3));
    }

    public int getRedLowMode() {
        return data.get(4);
    }

    public int getRedHighMode() {
        return data.get(5);
    }

    public int getPriority() {
        return data.get(6);
    }

    public long getMaxPower() {
        if (container instanceof BatteryStorageBlockEntity battery) {
            return battery.getMaxPower();
        }

        if (BlockPos.ZERO.equals(pos)) {
            return 0L;
        }

        Player player = slots.isEmpty() ? null : slots.get(PLAYER_SLOT_START).container instanceof Inventory inventory ? inventory.player : null;
        if (player == null) {
            return 0L;
        }

        if (player.level().getBlockState(pos).getBlock() instanceof BatteryStorageBlock batteryBlock) {
            return batteryBlock.getEnergyCapacity();
        }
        return 0L;
    }

    public int getPowerScaled(int pixels) {
        long maxPower = getMaxPower();
        if (pixels <= 0 || maxPower <= 0L) {
            return 0;
        }
        return (int) Math.min(pixels, getStoredPower() * pixels / maxPower);
    }

    private static boolean canChargeItem(ItemStack stack) {
        return HbmEnergyHelper.getChargeDemand(stack) > 0;
    }

    private static long combineLong(int low, int high) {
        return (low & 0xFFFFFFFFL) | ((long) high << 32);
    }
}
