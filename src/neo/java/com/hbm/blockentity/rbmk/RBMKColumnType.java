package com.hbm.blockentity.rbmk;

/**
 * Enumerates the different types of RBMK columns.
 */
public enum RBMKColumnType {
    
    BLANK,
    FUEL_ROD,
    CONTROL_ROD,
    REFLECTOR,
    MODERATOR,
    ABSORBER,
    BOILER,
    OUTGASSER,
    HEATER,
    COOLER,
    STORAGE,
    CONSOLE,
    INLET,
    OUTLET;
    
    /**
     * Whether this column type generates heat.
     */
    public boolean generatesHeat() {
        return this == FUEL_ROD || this == HEATER;
    }
    
    /**
     * Whether this column type produces steam.
     */
    public boolean producesSteam() {
        return this == BOILER;
    }
    
    /**
     * Whether this column type absorbs neutrons.
     */
    public boolean absorbsNeutrons() {
        return this == CONTROL_ROD || this == ABSORBER;
    }
}
