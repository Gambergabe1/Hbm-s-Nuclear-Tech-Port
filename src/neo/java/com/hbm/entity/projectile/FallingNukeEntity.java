package com.hbm.entity.projectile;

import com.hbm.registry.HbmEntityTypes;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.NukeExplosionHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import com.hbm.registry.HbmItems;

public class FallingNukeEntity extends Entity implements ItemSupplier {

    public static final EntityDataAccessor<Direction> FACING = SynchedEntityData.defineId(FallingNukeEntity.class, EntityDataSerializers.DIRECTION);

    public float tnt;
    public float nuke;
    public float hydro;
    public float bale;
    public float dirty;
    public float schrab;
    public float sol;
    public float euph;

    public FallingNukeEntity(EntityType<? extends FallingNukeEntity> type, Level level) {
        super(type, level);
    }

    public FallingNukeEntity(Level level, float tnt, float nuke, float hydro, float bale, float dirty, float schrab, float sol, float euph) {
        this(HbmEntityTypes.ENTITY_FALLING_NUKE.get(), level);
        this.tnt = tnt;
        this.nuke = nuke;
        this.hydro = hydro;
        this.bale = bale;
        this.dirty = dirty;
        this.schrab = schrab;
        this.sol = sol;
        this.euph = euph;
        this.setXRot(90);
        this.setYRot(90);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(HbmItems.AMMO_FALLING_NUKE.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FACING, Direction.NORTH);
    }

    @Override
    public void tick() {
        super.tick();

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        Vec3 movement = this.getDeltaMovement();
        this.setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);

        this.setDeltaMovement(movement.x * 0.99, movement.y - 0.05, movement.z * 0.99);

        movement = this.getDeltaMovement();
        if (movement.y < -1) {
            this.setDeltaMovement(movement.x, -1, movement.z);
        }

        updateRotation();

        if (!this.level().getBlockState(this.blockPosition()).isAir()) {
            if (!this.level().isClientSide) {
                explode();
                this.discard();
            }
        }
    }

    private void updateRotation() {
        this.xRotO = this.getXRot();
        if (this.getXRot() > -75) {
            this.setXRot(this.getXRot() - 2);
        }
    }

    private void explode() {
        if (nuke <= 0.0F && hydro <= 0.0F && bale <= 0.0F && dirty <= 0.0F && schrab <= 0.0F && sol <= 0.0F && euph <= 0.0F) {
            float strength = Math.max(1.0F, tnt);
            ExplosionLarge.explode(level(), getX(), getY(), getZ(), strength, true, true, true);
            return;
        }

        NukeExplosionHandler.explodePayload(level(), this, getX(), getY(), getZ(), tnt, nuke, hydro, bale, dirty, schrab, sol, euph);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tnt = tag.getFloat("tnt");
        this.nuke = tag.getFloat("nuke");
        this.hydro = tag.getFloat("hydro");
        this.bale = tag.getFloat("bale");
        this.dirty = tag.getFloat("dirty");
        this.schrab = tag.getFloat("schrab");
        this.sol = tag.getFloat("sol");
        this.euph = tag.getFloat("euph");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("tnt", tnt);
        tag.putFloat("nuke", nuke);
        tag.putFloat("hydro", hydro);
        tag.putFloat("bale", bale);
        tag.putFloat("dirty", dirty);
        tag.putFloat("schrab", schrab);
        tag.putFloat("sol", sol);
        tag.putFloat("euph", euph);
    }
}
