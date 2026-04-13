package com.hbm.api.block;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public interface IDamageableMachine {
    int getMaxDamage();
    int getMachineDamage();
    void setMachineDamage(int damage);
    
    default void damageMachine(int amount) {
        setMachineDamage(Math.min(getMaxDamage(), getMachineDamage() + amount));
    }
    
    default void repairMachine(int amount) {
        setMachineDamage(Math.max(0, getMachineDamage() - amount));
    }
    
    default boolean isDestroyed() {
        return getMachineDamage() >= getMaxDamage();
    }

    void onMachineDestroyed(Level level, BlockPos pos);
}
