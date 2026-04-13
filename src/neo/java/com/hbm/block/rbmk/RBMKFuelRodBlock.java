package com.hbm.block.rbmk;

import com.hbm.blockentity.rbmk.RBMKFuelRodBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * RBMK fuel rod block.
 * The heart of the nuclear reactor - generates heat through fission.
 */
public class RBMKFuelRodBlock extends AbstractRBMKBlock {
    
    public RBMKFuelRodBlock(Properties properties) {
        super(properties);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RBMKFuelRodBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, null,
            (lvl, pos, st, be) -> {
                if (be instanceof RBMKFuelRodBlockEntity fuelRod) {
                    fuelRod.serverTick(lvl, pos, st);
                }
            });
    }
    
    @Override
    public int getColumnHeight() {
        return 4;
    }
    
    @Override
    protected ItemInteractionResult onRBMKBlockActivated(BlockEntity be, Player player, InteractionHand hand, ItemStack stack) {
        return ItemInteractionResult.SUCCESS;
    }
    
    @Override
    protected void onRBMKBlockBroken(BlockEntity be) {
        // TODO: Drop fuel rod contents
    }
}
