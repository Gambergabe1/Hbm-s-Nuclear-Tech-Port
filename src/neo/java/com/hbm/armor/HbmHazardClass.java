package com.hbm.armor;

public enum HbmHazardClass {
    PARTICLE_COARSE("hazard.particleCoarse"),
    PARTICLE_FINE("hazard.particleFine"),
    GAS_CHLORINE("hazard.gasChlorine"),
    GAS_MONOXIDE("hazard.gasMonoxide"),
    GAS_CORROSIVE("hazard.corrosive"),
    BACTERIA("hazard.bacteria"),
    NERVE_AGENT("hazard.nerveAgent"),
    RAD_GAS("hazard.radGas"),
    LIGHT("hazard.light"),
    SAND("hazard.sand");

    private final String translationKey;

    HbmHazardClass(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}
