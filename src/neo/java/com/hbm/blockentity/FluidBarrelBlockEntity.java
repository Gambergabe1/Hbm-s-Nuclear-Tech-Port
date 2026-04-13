package com.hbm.blockentity;

import com.hbm.api.fluid.HbmFluidTank;
import com.hbm.api.fluid.HbmFluidTraits;
import com.hbm.menu.FluidBarrelMenu;
import com.hbm.network.HbmNetwork;
import com.hbm.registry.HbmBlockEntityTypes;
import com.hbm.registry.HbmBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public final class FluidBarrelBlockEntity extends AbstractFluidMachineBlockEntity {
    public static final int SLOT_DRAIN_IN = 0;
    public static final int SLOT_DRAIN_OUT = 1;
    public static final int SLOT_FILL_IN = 2;
    public static final int SLOT_FILL_OUT = 3;
    public static final int SLOT_COUNT = 4;

    private static final int MODE_FILL_ONLY = 0;
    private static final int MODE_BALANCED = 1;
    private static final int MODE_DRAIN_ONLY = 2;
    private static final int MODE_LOCKED = 3;
    private static final int MODE_COUNT = 4;

    private int mode = MODE_FILL_ONLY;
    private int age;
    private final MachineStateTracker<FluidMachineTrackedState> trackedState = new MachineStateTracker<>(state -> {
        if (level instanceof ServerLevel serverLevel) {
            HbmNetwork.syncBarrelState(serverLevel, worldPosition, state.fluid());
        }
    });

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> mode;
                case 1 -> getFluidTank().getCapacity();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> mode = value;
                case 1 -> getFluidTank().setCapacityAndClamp(value);
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public FluidBarrelBlockEntity(BlockPos pos, BlockState state, int capacity) {
        super(HbmBlockEntityTypes.BARREL.get(), pos, state, SLOT_COUNT, capacity);
    }

    public FluidBarrelBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, 8000);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FluidBarrelBlockEntity barrel) {
        if (!level.isClientSide) {
            barrel.serverTick();
        }
    }

    public void cycleMode() {
        mode = (mode + 1) % MODE_COUNT;
        setChanged();
    }

    public HbmFluidTank getFluidTank() {
        return super.getFluidTank();
    }

    public int getMode() {
        return mode;
    }

    public int getCapacity() {
        return getFluidTank().getCapacity();
    }

    @Override
    public boolean canAcceptFluid() {
        return mode != MODE_DRAIN_ONLY && mode != MODE_LOCKED;
    }

    @Override
    public boolean canProvideFluid() {
        return mode != MODE_FILL_ONLY && mode != MODE_LOCKED;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncTrackedState();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.barrel");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new FluidBarrelMenu(id, playerInventory, this, data, worldPosition);
    }

    @Override
    protected void loadMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        mode = tag.getInt("mode");
    }

    @Override
    protected void saveMachineData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("mode", mode);
    }

    private void serverTick() {
        boolean changed = false;

        if (canAcceptFluid()) {
            changed |= drainContainerIntoTank(SLOT_DRAIN_IN, SLOT_DRAIN_OUT);
        }

        if (canProvideFluid()) {
            changed |= fillContainerFromTank(SLOT_FILL_IN, SLOT_FILL_OUT);
        }

        if (handleFluidTraits()) {
            return;
        }

        age = (age + 1) % 20;

        if ((mode == MODE_BALANCED || mode == MODE_DRAIN_ONLY) && (age == 9 || age == 19)) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = worldPosition.relative(direction);
                IFluidHandler neighborHandler = level.getCapability(
                    Capabilities.FluidHandler.BLOCK,
                    neighborPos,
                    direction.getOpposite()
                );
                if (neighborHandler == null) {
                    continue;
                }

                FluidStack drained = drain(4000, IFluidHandler.FluidAction.SIMULATE);
                if (drained.isEmpty()) {
                    continue;
                }

                int filled = neighborHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                if (filled > 0) {
                    drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    changed = true;
                }
            }
        }

        if (changed) {
            setChanged();
        }
    }

    private boolean handleFluidTraits() {
        FluidStack storedFluid = getFluidTank().getFluid();
        if (storedFluid.isEmpty()) {
            return false;
        }

        BlockState state = getBlockState();
        HbmFluidTraits.ContainerReaction reaction = HbmFluidTraits.getContainerReaction(storedFluid, getContainerMaterial(state));
        switch (reaction) {
            case EXPLODES -> {
                level.destroyBlock(worldPosition, false);
                level.explode(
                    null,
                    worldPosition.getX() + 0.5D,
                    worldPosition.getY() + 0.5D,
                    worldPosition.getZ() + 0.5D,
                    5.0F,
                    Level.ExplosionInteraction.TNT
                );
                return true;
            }
            case DESTROYS -> {
                level.destroyBlock(worldPosition, false);
                level.playSound(
                    null,
                    worldPosition,
                    SoundEvents.LAVA_EXTINGUISH,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
                );
                return true;
            }
            case CORRODES -> {
                corrodeIntoCorrodedBarrel(storedFluid);
                return true;
            }
            case SAFE -> {
            }
        }

        if (state.is(HbmBlocks.BARREL_CORRODED.get()) && level.random.nextInt(3) == 0) {
            FluidStack leaked = getFluidTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
            if (!leaked.isEmpty()) {
                setChanged();
            }
        }

        return false;
    }

    private static HbmFluidTraits.ContainerMaterial getContainerMaterial(BlockState state) {
        if (state.is(HbmBlocks.BARREL_PLASTIC.get())) {
            return HbmFluidTraits.ContainerMaterial.PLASTIC;
        }
        if (state.is(HbmBlocks.BARREL_IRON.get()) || state.is(HbmBlocks.BARREL_CORRODED.get())) {
            return HbmFluidTraits.ContainerMaterial.IRON;
        }
        if (state.is(HbmBlocks.BARREL_STEEL.get())) {
            return HbmFluidTraits.ContainerMaterial.STEEL;
        }
        if (state.is(HbmBlocks.BARREL_TCALLOY.get())) {
            return HbmFluidTraits.ContainerMaterial.TCALLOY;
        }
        if (state.is(HbmBlocks.BARREL_ANTIMATTER.get())) {
            return HbmFluidTraits.ContainerMaterial.ANTIMATTER;
        }
        return HbmFluidTraits.ContainerMaterial.STEEL;
    }

    private void syncTrackedState() {
        trackedState.sync(new FluidMachineTrackedState(getFluidTank().getFluid()));
    }

    private void corrodeIntoCorrodedBarrel(FluidStack storedFluid) {
        net.minecraft.core.NonNullList<ItemStack> preservedItems = net.minecraft.core.NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        for (int i = 0; i < SLOT_COUNT; i++) {
            preservedItems.set(i, items.get(i).copy());
        }
        int preservedMode = mode;

        level.playSound(
            null,
            worldPosition,
            SoundEvents.LAVA_EXTINGUISH,
            SoundSource.BLOCKS,
            1.0F,
            1.0F
        );
        level.setBlock(worldPosition, HbmBlocks.BARREL_CORRODED.get().defaultBlockState(), 3);

        if (level.getBlockEntity(worldPosition) instanceof FluidBarrelBlockEntity corrodedBarrel) {
            corrodedBarrel.items = preservedItems;
            corrodedBarrel.mode = preservedMode;
            corrodedBarrel.getFluidTank().fill(storedFluid.copy(), IFluidHandler.FluidAction.EXECUTE);
            corrodedBarrel.setChanged();
        }
    }
}
