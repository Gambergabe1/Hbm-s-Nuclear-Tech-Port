package com.hbm.menu.slot;

import net.minecraft.world.Container;

public final class OutputSlot extends FilteredSlot {
    public OutputSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y, stack -> false);
    }
}
