package com.hbm.entity.projectile;

import com.hbm.registry.HbmEntityTypes;
import com.hbm.registry.HbmItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ChopperMineEntity extends Entity implements ItemSupplier {

    public int timer = 0;
    public Entity shooter;

    public ChopperMineEntity(EntityType<? extends ChopperMineEntity> type, Level level) {
        super(type, level);
    }

    public ChopperMineEntity(Level level, double x, double y, double z, double moX, double moY, double moZ, Entity shooter) {
        this(HbmEntityTypes.ENTITY_CHOPPER_MINE.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(moX, moY, moZ);
        this.shooter = shooter;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(HbmItems.AMMO_CHOPPER_MINE.get());
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.timer = tag.getInt("Timer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Timer", this.timer);
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        Vec3 movement = this.getDeltaMovement();
        this.setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);

        if (!this.level().getBlockState(this.blockPosition()).isAir() || timer >= 100) {
            if (!this.level().isClientSide) {
                explode();
                this.discard();
            }
        }

        if (movement.y > -0.85) {
            this.setDeltaMovement(movement.x * 0.9, movement.y - 0.05, movement.z * 0.9);
        } else {
            this.setDeltaMovement(movement.x * 0.9, movement.y, movement.z * 0.9);
        }

        timer++;
    }

    private void explode() {
        level().explode(shooter, getX(), getY(), getZ(), 5.0F, Level.ExplosionInteraction.TNT);
    }
}
