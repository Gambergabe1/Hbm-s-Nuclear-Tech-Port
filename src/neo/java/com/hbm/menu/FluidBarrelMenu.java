package com.hbm.menu;

import com.hbm.blockentity.FluidBarrelBlockEntity;
import com.hbm.menu.slot.FluidContainerSlot;
import com.hbm.menu.slot.OutputSlot;
import com.hbm.registry.HbmMenuTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public final class FluidBarrelMenu extends AbstractMachineMenu {
    private final BlockPos pos;

    public FluidBarrelMenu(int containerId, Inventory playerInventory) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(FluidBarrelBlockEntity.SLOT_COUNT),
            new SimpleContainerData(2),
            BlockPos.ZERO
        );
    }

    public FluidBarrelMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(
            containerId,
            playerInventory,
            new SimpleContainer(FluidBarrelBlockEntity.SLOT_COUNT),
            new SimpleContainerData(2),
            buffer.readBlockPos()
        );
    }

    public FluidBarrelMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, BlockPos pos) {
        super(HbmMenuTypes.BARREL.get(), containerId, playerInventory, container, FluidBarrelBlockEntity.SLOT_COUNT, data, 2, pos);
        this.pos = pos;
        addSlot(new FluidContainerSlot(container, FluidBarrelBlockEntity.SLOT_DRAIN_IN, 35, 17));
        addSlot(new OutputSlot(container, FluidBarrelBlockEntity.SLOT_DRAIN_OUT, 35, 53));
        addSlot(new FluidContainerSlot(container, FluidBarrelBlockEntity.SLOT_FILL_IN, 125, 17));
        addSlot(new OutputSlot(container, FluidBarrelBlockEntity.SLOT_FILL_OUT, 125, 53));
        addPlayerInventorySlots(8, 84);
        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        if (container instanceof FluidBarrelBlockEntity barrel) {
            return barrel.stillValid(player);
        }

        return access.evaluate(
            (level, blockPos) -> level.getBlockEntity(blockPos) instanceof FluidBarrelBlockEntity barrel && barrel.stillValid(player),
            true
        );
    }

    @Override
    protected boolean moveFromPlayerInventory(ItemStack stack, int index) {
        if (FluidContainerSlot.hasFluidHandler(stack)) {
            return moveToRange(stack, 0, FluidBarrelBlockEntity.SLOT_COUNT);
        }
        return moveWithinPlayerInventory(stack, index);
    }

    public int getMode() {
        return getDataValue(0);
    }

    public int getCapacity() {
        return getDataValue(1);
    }

    public BlockPos getPos() {
        return pos;
    }
}
