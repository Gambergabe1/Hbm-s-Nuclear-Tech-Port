package com.hbm.armor;

import net.minecraft.world.item.ItemStack;

public interface RadiationResistanceProvider {
    float getRadiationResistance(ItemStack stack);
}
