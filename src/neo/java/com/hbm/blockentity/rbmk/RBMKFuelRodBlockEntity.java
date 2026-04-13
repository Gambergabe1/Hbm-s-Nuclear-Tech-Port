package com.hbm.blockentity.rbmk;

import com.hbm.api.radiation.IRadiationSource;
import com.hbm.config.RadiationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * RBMK fuel rod block entity.
 * Generates heat through nuclear fission and depletes fuel over time.
 * Emits radiation based on neutron flux and fuel amount.
 */
public class RBMKFuelRodBlockEntity extends AbstractRBMKBlockEntity implements IRadiationSource {
    
    // Fuel tracking
    private double fuelAmount = 100.0; // Percentage (0-100)
    private double depletionRate = 0.001; // Fuel consumed per tick at full power
    
    // Neutron flux (chain reaction intensity)
    private double neutronFlux = 0.0;
    private double targetNeutronFlux = 0.0;
    
    // Heat generation
    private double heatGeneration = 0.0;
    
    public RBMKFuelRodBlockEntity(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
    
    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        
        // Calculate neutron flux based on surrounding fuel rods
        calculateNeutronFlux();
        
        // Generate heat from fission
        generateHeat();
        
        // Deplete fuel
        depleteFuel();
        
        // Check for criticality
        if (neutronFlux > 1000.0) {
            heat += neutronFlux * 0.01;
        }
    }
    
    /**
     * Calculates neutron flux from surrounding fuel rods.
     */
    private void calculateNeutronFlux() {
        int adjacentFuelRods = countAdjacentFuelRods();
        
        // Base flux from self
        double selfFlux = fuelAmount > 0 ? 10.0 : 0.0;
        
        // Flux from neighbors
        double neighborFlux = adjacentFuelRods * 5.0;
        
        // Moderator bonus
        double moderatorBonus = countAdjacentModerators() * 2.0;
        
        // Control rod absorption
        double controlRodAbsorption = countAdjacentControlRods() * 0.5;
        
        // Calculate target flux
        targetNeutronFlux = (selfFlux + neighborFlux + moderatorBonus) * (1.0 - controlRodAbsorption);
        
        // Smooth flux changes
        double fluxChangeSpeed = 0.05;
        neutronFlux += (targetNeutronFlux - neutronFlux) * fluxChangeSpeed;
    }
    
    /**
     * Generates heat based on neutron flux.
     */
    private void generateHeat() {
        if (fuelAmount <= 0) {
            heatGeneration = 0;
            return;
        }
        
        heatGeneration = neutronFlux * 0.1;
        this.heat += heatGeneration;
    }
    
    /**
     * Depletes fuel based on neutron flux.
     */
    private void depleteFuel() {
        if (fuelAmount <= 0 || neutronFlux <= 0) {
            return;
        }
        
        double depletion = depletionRate * neutronFlux * 0.01;
        fuelAmount = Math.max(0, fuelAmount - depletion);
    }
    
    /**
     * Counts adjacent fuel rods.
     */
    private int countAdjacentFuelRods() {
        if (level == null) return 0;
        
        int count = 0;
        for (Direction dir : HEAT_DIRECTIONS) {
            BlockEntity be = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
            if (be instanceof RBMKFuelRodBlockEntity) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Counts adjacent moderator blocks.
     */
    private int countAdjacentModerators() {
        if (level == null) return 0;
        
        int count = 0;
        for (Direction dir : HEAT_DIRECTIONS) {
            BlockEntity be = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
            if (be instanceof RBMKModeratorBlockEntity) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Counts adjacent control rods.
     */
    private int countAdjacentControlRods() {
        if (level == null) return 0;
        
        int count = 0;
        for (Direction dir : HEAT_DIRECTIONS) {
            BlockEntity be = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
            if (be instanceof RBMKControlRodBlockEntity controlRod) {
                count += controlRod.getInsertionLevel() / 100.0;
            }
        }
        return count;
    }
    
    @Override
    public double getMaxHeat() {
        return 2500.0;
    }
    
    @Override
    public double getPassiveCooling() {
        return 5.0;
    }
    
    @Override
    public RBMKColumnType getConsoleType() {
        return RBMKColumnType.FUEL_ROD;
    }
    
    @Override
    public boolean isCoreBlock() {
        return true;
    }
    
    // =====GETTERS/SETTERS=====
    
    public double getFuelAmount() {
        return fuelAmount;
    }
    
    public double getNeutronFlux() {
        return neutronFlux;
    }
    
    public double getHeatGeneration() {
        return heatGeneration;
    }
    
    public boolean hasFuel() {
        return fuelAmount > 0;
    }
    
    // =====RADIATION SOURCE IMPLEMENTATION=====
    
    @Override
    public double getRadiationStrength() {
        if (!RadiationConfig.rbmkRadiationEnabled) {
            return 0.0;
        }
        
        // Radiation scales with neutron flux and fuel amount
        double baseRadiation = neutronFlux * 0.01; // Scale to reasonable levels
        double fuelFactor = fuelAmount / 100.0;
        
        return baseRadiation * fuelFactor * RadiationConfig.rbmkRadiationMultiplier;
    }
    
    @Override
    public boolean isRadiationActive() {
        return RadiationConfig.rbmkRadiationEnabled && hasFuel() && neutronFlux > 0;
    }
    
    @Override
    public BlockPos getSourcePosition() {
        return worldPosition;
    }
    
    @Override
    public double getRadiationRange() {
        return RadiationConfig.rbmkRadiationRange;
    }
    
    @Override
    public IRadiationSource.RadiationType getRadiationType() {
        // RBMKs emit primarily gamma radiation
        return IRadiationSource.RadiationType.GAMMA;
    }
}
