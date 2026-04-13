package com.hbm.api.radiation;

import net.minecraft.core.BlockPos;

/**
 * Interface for objects that emit radiation.
 * Implemented by RBMK reactors, radioactive blocks, entities, etc.
 */
public interface IRadiationSource {
    
    /**
     * Gets the current radiation strength in Sieverts.
     * This is the base radiation output before distance falloff.
     * 
     * @return radiation strength in Sv
     */
    double getRadiationStrength();
    
    /**
     * Whether this radiation source is currently active.
     * Inactive sources don't emit radiation.
     * 
     * @return true if emitting radiation
     */
    boolean isRadiationActive();
    
    /**
     * Gets the position of this radiation source.
     * 
     * @return source position
     */
    BlockPos getSourcePosition();
    
    /**
     * Gets the effective range of this radiation source in blocks.
     * Beyond this range, radiation is negligible.
     * 
     * @return range in blocks
     */
    default double getRadiationRange() {
        return 32.0; // Default 32 block range
    }
    
    /**
     * Gets the radiation type.
     * Different types have different shielding requirements.
     * 
     * @return radiation type
     */
    default RadiationType getRadiationType() {
        return RadiationType.GAMMA;
    }
    
    /**
     * Calculates radiation exposure at a specific position.
     * Uses inverse square law for falloff.
     * 
     * @param position position to check
     * @return exposure in Sv at that position
     */
    default double getExposureAt(BlockPos position) {
        if (!isRadiationActive()) {
            return 0.0;
        }
        
        BlockPos sourcePos = getSourcePosition();
        double distance = position.distSqr(sourcePos);
        
        if (distance > getRadiationRange() * getRadiationRange()) {
            return 0.0;
        }
        
        // Inverse square law with minimum threshold
        double falloff = 1.0 / (distance + 1.0);
        return getRadiationStrength() * falloff;
    }
    
    /**
     * Types of radiation with different properties.
     */
    enum RadiationType {
        /**
         * Alpha radiation - stopped by paper, dangerous if ingested.
         */
        ALPHA,
        
        /**
         * Beta radiation - stopped by aluminum, moderate penetration.
         */
        BETA,
        
        /**
         * Gamma radiation - highly penetrating, requires lead/concrete.
         */
        GAMMA,
        
        /**
         * Neutron radiation - most dangerous, requires water/concrete.
         */
        NEUTRON,
        
        /**
         * Digamma radiation - endgame radiation, ignores most shielding.
         */
        DIGAMMA
    }
}
