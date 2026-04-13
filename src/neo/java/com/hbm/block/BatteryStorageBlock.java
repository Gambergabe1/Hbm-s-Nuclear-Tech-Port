package com.hbm.block;

import com.hbm.blockentity.BatteryStorageBlockEntity;
import com.hbm.registry.HbmBlockEntityTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Locale;

public final class BatteryStorageBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<BatteryStorageBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.LONG.fieldOf("energy_capacity").forGetter(BatteryStorageBlock::getEnergyCapacity),
            propertiesCodec()
        ).apply(instance, BatteryStorageBlock::new)
    );
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final long energyCapacity;

    public BatteryStorageBlock(long energyCapacity, BlockBehaviour.Properties properties) {
        super(properties);
        this.energyCapacity = Math.max(0L, energyCapacity);
        registerDefaultState(stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    public MapCodec<BatteryStorageBlock> codec() {
        return CODEC;
    }

    public long getEnergyCapacity() {
        return energyCapacity;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        MenuProvider menuProvider = getMenuProvider(state, level, pos);
        if (menuProvider != null) {
            player.openMenu(menuProvider, pos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Containers.dropContentsOnDestroy(state, newState, level, pos);
            super.onRemove(state, level, pos, newState, isMoving);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof BatteryStorageBlockEntity battery) {
            return battery.getComparatorOutput();
        }
        return 0;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        Float explosionRadius = params.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
        if (explosionRadius != null) {
            RandomSource random = params.getLevel().getRandom();
            if (random.nextFloat() > 1.0F / explosionRadius) {
                return List.of();
            }
        }

        ItemStack stack = new ItemStack(this);
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BatteryStorageBlockEntity battery) {
            applySavedBatteryData(stack, battery, params.getLevel().registryAccess());
        }
        return List.of(stack);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof BatteryStorageBlockEntity battery) {
            applySavedBatteryData(stack, battery, level.registryAccess());
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Capacity: " + formatNumber(energyCapacity) + " HE").withStyle(ChatFormatting.GOLD));

        CompoundTag tag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).copyTag();
        if (!tag.isEmpty()) {
            long stored = tag.getLong("power");
            int redLow = tag.contains("redLow") ? tag.getShort("redLow") : BatteryStorageBlockEntity.MODE_INPUT;
            int redHigh = tag.contains("redHigh") ? tag.getShort("redHigh") : BatteryStorageBlockEntity.MODE_OUTPUT;
            int priority = tag.contains("priority") ? tag.getByte("priority") : 1;

            tooltip.add(
                Component.literal("Stored: " + formatNumber(stored) + " / " + formatNumber(energyCapacity) + " HE")
                    .withStyle(stored > 0L ? ChatFormatting.GREEN : ChatFormatting.RED)
            );
            tooltip.add(Component.literal("No redstone: " + modeName(redLow)).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Redstone: " + modeName(redHigh)).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Priority: " + priorityName(priority)).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryStorageBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, HbmBlockEntityTypes.MACHINE_BATTERY.get(), BatteryStorageBlockEntity::serverTick);
    }

    private static void applySavedBatteryData(ItemStack stack, BatteryStorageBlockEntity battery, HolderLookup.Provider registries) {
        CompoundTag data = battery.saveCustomOnly(registries);
        battery.removeComponentsFromTag(data);
        BlockItem.setBlockEntityData(stack, HbmBlockEntityTypes.MACHINE_BATTERY.get(), data);
        if (battery.getCustomName() != null) {
            stack.set(DataComponents.CUSTOM_NAME, battery.getCustomName());
        }
    }

    private static String modeName(int mode) {
        return switch (mode) {
            case BatteryStorageBlockEntity.MODE_INPUT -> "Input";
            case BatteryStorageBlockEntity.MODE_BUFFER -> "Buffer";
            case BatteryStorageBlockEntity.MODE_OUTPUT -> "Output";
            case BatteryStorageBlockEntity.MODE_NONE -> "Disabled";
            default -> "Input";
        };
    }

    private static String priorityName(int priority) {
        return switch (priority) {
            case 0 -> "Low";
            case 2 -> "High";
            default -> "Normal";
        };
    }

    private static String formatNumber(long value) {
        return String.format(Locale.ROOT, "%,d", value);
    }
}
