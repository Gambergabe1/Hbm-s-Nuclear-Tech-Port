package com.hbm.armor;

public enum ArmorModSlot {
    HELMET_ONLY(0, "desc.applicableh1"),
    CHEST_ONLY(1, "desc.applicablec1"),
    LEGS_ONLY(2, "desc.applicablel1"),
    BOOTS_ONLY(3, "desc.applicableb1"),
    SERVOS(4, "desc.applicableservo"),
    CLADDING(5, "desc.applicablecladding"),
    KEVLAR(6, "desc.applicableinsert"),
    EXTRA(7, "desc.applicableextra"),
    BATTERY(8, "armorMod.type.battery");

    private final int legacyIndex;
    private final String translationKey;

    ArmorModSlot(int legacyIndex, String translationKey) {
        this.legacyIndex = legacyIndex;
        this.translationKey = translationKey;
    }

    public int legacyIndex() {
        return legacyIndex;
    }

    public String translationKey() {
        return translationKey;
    }
}
