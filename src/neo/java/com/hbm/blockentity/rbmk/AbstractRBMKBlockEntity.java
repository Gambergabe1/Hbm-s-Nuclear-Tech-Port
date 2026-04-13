package com.hbm.blockentity.rbmk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Base block entity for all RBMK reactor components.
 * Handles heat transfer, water/steam mechanics, and meltdown physics.
 */
public abstract class AbstractRBMKBlockEntity extends BlockEntity {
    
    // RBMK column height
    public static final int RBMK_HEIGHT = 4;
    
    // Heat mechanics
    public double heat = 20.0; // Current temperature in Celsius
    public double jumpHeight = 0.0; // For lid animation
    public float downwardSpeed = 0.0F;
    public boolean falling = false;
    protected static final byte GRAVITY = 5; // blocks per s^2
    
    // Water/Steam mechanics (RealSim mode)
    public int water = 0;
    public int steam = 0;
    public static final int MAX_WATER = 16000 * 20;
    public static final int MAX_STEAM = 16000 * 20;
    
    // Cache for neighbors
    protected AbstractRBMKBlockEntity[] heatCache = new AbstractRBMKBlockEntity[4];
    protected static final Direction[] HEAT_DIRECTIONS = {
        Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };
    
    // Meltdown tracking
    protected static Set<AbstractRBMKBlockEntity> meltdownColumns = new HashSet<>();
    
    public AbstractRBMKBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    // =====CORE MECHANICS=====
    
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            return;
        }
        
        // Perform physics simulation
        transferHeat();
        boilWater();
        coolPassively();
        animateLid();
        
        // Check for overheating
        if (heat > getMaxHeat() * 1.5) {
            onOverheat();
        }
        
        // Mark dirty to save
        setChanged();
    }
    
    /**
     * Transfers heat between neighboring RBMK components to equalize temperature.
     */
    private void transferHeat() {
        if (level == null) return;
        
        List<AbstractRBMKBlockEntity> neighbors = new ArrayList<>();
        neighbors.add(this);
        
        double totalHeat = this.heat;
        int totalWater = this.water;
        int totalSteam = this.steam;
        
        // Check all 4 horizontal neighbors
        for (int i = 0; i < HEAT_DIRECTIONS.length; i++) {
            Direction dir = HEAT_DIRECTIONS[i];
            
            // Invalidate cache if needed
            if (heatCache[i] != null && heatCache[i].isRemoved()) {
                heatCache[i] = null;
            }
            
            // Load neighbor if not cached
            if (heatCache[i] == null) {
                BlockEntity be = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
                if (be instanceof AbstractRBMKBlockEntity rbmk) {
                    heatCache[i] = rbmk;
                }
            }
            
            // Add to calculation
            if (heatCache[i] != null) {
                neighbors.add(heatCache[i]);
                totalHeat += heatCache[i].heat;
                totalWater += heatCache[i].water;
                totalSteam += heatCache[i].steam;
            }
        }
        
        // Equalize if there are neighbors
        if (neighbors.size() > 1) {
            double targetHeat = totalHeat / neighbors.size();
            double heatFlowRate = 0.1; // Default: 10% per tick
            
            int avgWater = totalWater / neighbors.size();
            int remainderWater = totalWater % neighbors.size();
            int avgSteam = totalSteam / neighbors.size();
            int remainderSteam = totalSteam % neighbors.size();
            
            // Move heat toward average
            for (AbstractRBMKBlockEntity rbmk : neighbors) {
                double delta = targetHeat - rbmk.heat;
                rbmk.heat += delta * heatFlowRate;
                rbmk.water = avgWater;
                rbmk.steam = avgSteam;
            }
            
            // Add remainders to this block
            this.water += remainderWater;
            this.steam += remainderSteam;
        }
    }
    
    /**
     * Boils water into steam when temperature exceeds 100°C (RealSim mode).
     */
    protected void boilWater() {
        if (heat < 100.0 || water <= 0) {
            return;
        }
        
        double heatConsumption = 2.0; // Default
        double availableHeat = (this.heat - 100.0) / heatConsumption;
        double availableWater = this.water;
        double availableSpace = MAX_STEAM - this.steam;
        
        int processed = (int) Math.floor(Math.min(availableHeat, Math.min(availableWater, availableSpace)));
        processed = Math.min(processed, 10); // Default boiler speed
        
        this.water -= processed;
        this.steam += processed;
        this.heat -= processed * heatConsumption;
    }
    
    /**
     * Applies passive cooling to prevent infinite heat buildup.
     */
    protected void coolPassively() {
        this.heat -= getPassiveCooling();
        
        // Clamp to ambient temperature
        if (heat < 20.0) {
            heat = 20.0;
        }
    }
    
    /**
     * Animates the RBMK lid jumping when overheated.
     */
    protected void animateLid() {
        double jumpTemp = 800.0; // Default jump temperature
        
        if (heat <= jumpTemp && !falling) {
            return;
        }
        
        if (!falling) {
            // Linear rise
            if (heat > jumpTemp) {
                if (jumpHeight > 0 || level.random.nextInt((int)(25.0 * getMaxHeat() / (heat - jumpTemp + 200.0)) + 1) == 0) {
                    double change = (heat - jumpTemp) * 0.0002;
                    double heightLimit = (heat - jumpTemp) * 0.002;
                    
                    jumpHeight += change;
                    
                    if (jumpHeight > heightLimit) {
                        jumpHeight = heightLimit;
                        falling = true;
                    }
                }
            }
        } else {
            // Gravity fall
            if (jumpHeight > 0) {
                downwardSpeed += GRAVITY * 0.05F;
                jumpHeight -= downwardSpeed;
            } else {
                jumpHeight = 0;
                downwardSpeed = 0;
                falling = false;
                // Play lid slam sound
                playLidSound();
            }
        }
    }
    
    // =====ABSTRACT METHODS=====
    
    /**
     * Returns the maximum heat this component can reach before melting.
     */
    public abstract double getMaxHeat();
    
    /**
     * Returns the passive cooling rate in degrees per tick.
     */
    public abstract double getPassiveCooling();
    
    /**
     * Returns the column type for the RBMK console.
     */
    public abstract RBMKColumnType getConsoleType();
    
    // =====LID MECHANICS=====
    
    public boolean hasLid() {
        return !isLidRemovable() || getLidDirection() != null;
    }
    
    public boolean isLidRemovable() {
        return true;
    }
    
    @Nullable
    public Direction getLidDirection() {
        return null;
    }
    
    public double getLidHeight() {
        return jumpHeight;
    }
    
    protected void playLidSound() {
        if (level != null) {
            // TODO: Play RBMK lid slam sound
        }
    }
    
    // =====MELTDOWN MECHANICS=====
    
    /**
     * Initiates a full RBMK meltdown - the worst-case scenario.
     */
    public void initiateMeltdown() {
        if (level == null || level.isClientSide) {
            return;
        }
        
        meltdownColumns.clear();
        collectAllConnectedColumns(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
        
        // Calculate bounds
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        
        for (AbstractRBMKBlockEntity rbmk : meltdownColumns) {
            minX = Math.min(minX, rbmk.worldPosition.getX());
            maxX = Math.max(maxX, rbmk.worldPosition.getX());
            minZ = Math.min(minZ, rbmk.worldPosition.getZ());
            maxZ = Math.max(maxZ, rbmk.worldPosition.getZ());
        }
        
        // Destroy all RBMK components
        for (AbstractRBMKBlockEntity rbmk : meltdownColumns) {
            int distFromMinX = rbmk.worldPosition.getX() - minX;
            int distFromMaxX = maxX - rbmk.worldPosition.getX();
            int distFromMinZ = rbmk.worldPosition.getZ() - minZ;
            int distFromMaxZ = maxZ - rbmk.worldPosition.getZ();
            
            int minDist = Math.min(distFromMinX, Math.min(distFromMaxX, 
                           Math.min(distFromMinZ, distFromMaxZ)));
            
            rbmk.meltComponent(minDist + 1);
        }
        
        // Spawn corium and radiation
        spawnCorium();
        spawnRadiation();
        
        // Clear tracking
        meltdownColumns.clear();
    }
    
    /**
     * Collects all connected RBMK columns for meltdown calculation.
     */
    private void collectAllConnectedColumns(int x, int y, int z) {
        if (level == null) return;
        
        BlockEntity be = level.getBlockEntity(new BlockPos(x, y, z));
        if (be instanceof AbstractRBMKBlockEntity rbmk) {
            if (!meltdownColumns.contains(rbmk)) {
                meltdownColumns.add(rbmk);
                
                // Recurse in all 4 directions
                collectAllConnectedColumns(x + 1, y, z);
                collectAllConnectedColumns(x - 1, y, z);
                collectAllConnectedColumns(x, y, z + 1);
                collectAllConnectedColumns(x, y, z - 1);
            }
        }
    }
    
    /**
     * Melts this RBMK component during a meltdown.
     */
    protected void meltComponent(int distanceFromEdge) {
        if (level == null) return;
        
        int height = getColumnHeight();
        int destroyCount = Math.min(distanceFromEdge, height);
        destroyCount = Math.max(1, destroyCount);
        
        // TODO: Convert to debris/corium when blocks are registered
    }
    
    protected void spawnCorium() {
        // TODO: Spawn corium blocks at the base
    }
    
    protected void spawnRadiation() {
        // TODO: Spawn radiation effect
    }
    
    /**
     * Called when temperature exceeds critical threshold.
     */
    protected void onOverheat() {
        if (level == null) return;
        
        // TODO: Spawn lava
    }
    
    // =====FINDING CORE ENTITY=====
    
    /**
     * Finds the core RBMK block entity for this column.
     */
    @Nullable
    public AbstractRBMKBlockEntity getCoreEntity() {
        if (level == null) return null;
        
        // Search downward to find the base of the column
        for (int i = 0; i <= RBMK_HEIGHT; i++) {
            BlockPos checkPos = new BlockPos(worldPosition.getX(), worldPosition.getY() - i, worldPosition.getZ());
            BlockEntity be = level.getBlockEntity(checkPos);
            
            if (be instanceof AbstractRBMKBlockEntity rbmk && rbmk.isCoreBlock()) {
                return rbmk;
            }
        }
        
        return this;
    }
    
    /**
     * Whether this block entity is the core (bottom) of the RBMK column.
     */
    public boolean isCoreBlock() {
        return true;
    }
    
    public int getColumnHeight() {
        return RBMK_HEIGHT;
    }
    
    // =====NBT=====
    
    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putDouble("heat", heat);
        tag.putDouble("jumpHeight", jumpHeight);
        tag.putInt("water", water);
        tag.putInt("steam", steam);
        tag.putBoolean("falling", falling);
        tag.putFloat("downwardSpeed", downwardSpeed);
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        heat = tag.getDouble("heat");
        jumpHeight = tag.getDouble("jumpHeight");
        water = tag.getInt("water");
        steam = tag.getInt("steam");
        falling = tag.getBoolean("falling");
        downwardSpeed = tag.getFloat("downwardSpeed");
    }
}
