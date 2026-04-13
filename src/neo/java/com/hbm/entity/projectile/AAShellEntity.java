package com.hbm.entity.projectile;

import java.util.List;

import com.hbm.registry.HbmEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import com.hbm.registry.HbmItems;

public class AAShellEntity extends Entity implements ItemSupplier {

    public int fuse = 5;
    public int dFuse = 30;

    public AAShellEntity(EntityType<? extends AAShellEntity> type, Level level) {
        super(type, level);
    }

    public AAShellEntity(Level level, double x, double y, double z) {
        this(HbmEntityTypes.ENTITY_AA_SHELL.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(HbmItems.AMMO_AA_SHELL.get());
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.fuse = tag.getInt("Fuse");
        this.dFuse = tag.getInt("DFuse");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Fuse", this.fuse);
        tag.putInt("DFuse", this.dFuse);
    }

    @Override
    public void tick() {
        super.tick();

        if (fuse > 0)
            fuse--;

        if (dFuse > 0) {
            dFuse--;
        } else {
            explode();
            return;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        Vec3 movement = this.getDeltaMovement();

        for (int i = 0; i < 5; i++) {
            this.setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);

            updateRotation();

            if (fuse == 0) {
                List<Entity> list = this.level().getEntities(this, new AABB(this.getX() - 5, this.getY() - 5, this.getZ() - 5, this.getX() + 5, this.getY() + 5, this.getZ() + 5));
                for (Entity e : list) {
                    float size = e.getBbWidth() * e.getBbWidth() * e.getBbHeight();
                    if (size >= 0.5) {
                        explode();
                        return;
                    }
                }
            }

            if (!this.level().getBlockState(this.blockPosition()).isAir()) {
                explode();
                return;
            }
        }
    }

    private void updateRotation() {
        Vec3 movement = this.getDeltaMovement();
        double d0 = movement.x;
        double d1 = movement.y;
        double d2 = movement.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        this.setYRot((float) (Mth.atan2(d0, d2) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(d1, (double) f) * (double) (180F / (float) Math.PI)) - 90.0F);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    private void explode() {
        if (!level().isClientSide) {
            com.hbm.explosion.ExplosionLarge.explode(level(), getX(), getY(), getZ(), 3.0F, true, true, true);
            this.discard();
        }
    }
}
