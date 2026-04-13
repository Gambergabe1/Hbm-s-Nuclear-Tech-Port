package com.hbm.block.rbmk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all RBMK reactor components.
 */
public abstract class AbstractRBMKBlock extends BaseEntityBlock {
    
    protected static final VoxelShape BASE_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final VoxelShape LID_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.25, 1.0);
    
    public AbstractRBMKBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(p -> this);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Return base shape for now - lid logic will be added when block entities are properly wired
        return BASE_SHAPE;
    }
    
    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            return onRBMKBlockActivated(be, player, hand, stack);
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    /**
     * Called when an RBMK block is activated.
     */
    protected abstract ItemInteractionResult onRBMKBlockActivated(BlockEntity be, Player player, InteractionHand hand, ItemStack stack);
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be != null) {
                onRBMKBlockBroken(be);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    
    /**
     * Called when an RBMK block is broken.
     */
    protected abstract void onRBMKBlockBroken(BlockEntity be);
    
    /**
     * Returns the height of the RBMK column.
     */
    public int getColumnHeight() {
        return 4;
    }
    
    /**
     * Whether this RBMK component can have a lid.
     */
    public boolean canHaveLid() {
        return true;
    }
    
    /**
     * Whether the lid is removable.
     */
    public boolean isLidRemovable() {
        return true;
    }
}
