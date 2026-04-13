package com.hbm.menu.slot;

import com.hbm.api.energy.HbmEnergyHelper;

import net.minecraft.world.Container;

public final class BatteryInputSlot extends FilteredSlot {
    public BatteryInputSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y, HbmEnergyHelper::canDischargeIntoMachine);
    }
}
