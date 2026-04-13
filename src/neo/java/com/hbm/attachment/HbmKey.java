package com.hbm.attachment;

public enum HbmKey {
    JETPACK,
    TOGGLE_JETPACK,
    TOGGLE_HEAD,
    RELOAD,
    CRANE_UP,
    CRANE_DOWN,
    CRANE_LEFT,
    CRANE_RIGHT,
    CRANE_LOAD;

    private static final HbmKey[] VALUES = values();

    public int id() {
        return ordinal();
    }

    public static HbmKey byId(int id) {
        if (id < 0 || id >= VALUES.length) {
            throw new IllegalArgumentException("Unknown HBM key id: " + id);
        }

        return VALUES[id];
    }
}
