package com.hbm.blockentity;

import net.minecraft.world.item.ItemStack;

final class PressMachineTrackedState {
    private final ItemStack input;
    private final ItemStack stamp;
    private final int progress;

    PressMachineTrackedState(ItemStack input, ItemStack stamp, int progress) {
        this.input = input == null ? ItemStack.EMPTY : input.copy();
        this.stamp = stamp == null ? ItemStack.EMPTY : stamp.copy();
        this.progress = Math.max(progress, 0);
    }

    ItemStack input() {
        return input;
    }

    ItemStack stamp() {
        return stamp;
    }

    int progress() {
        return progress;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PressMachineTrackedState state)) {
            return false;
        }

        return progress == state.progress
            && stacksEqual(input, state.input)
            && stacksEqual(stamp, state.stamp);
    }

    @Override
    public int hashCode() {
        return 31 * progress + stackHash(input) + 17 * stackHash(stamp);
    }

    private static boolean stacksEqual(ItemStack left, ItemStack right) {
        return left.getCount() == right.getCount() && ItemStack.isSameItemSameComponents(left, right);
    }

    private static int stackHash(ItemStack stack) {
        return 31 * stack.getCount() + ItemStack.hashItemAndComponents(stack);
    }
}
