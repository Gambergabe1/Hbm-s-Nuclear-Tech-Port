package com.hbm.blockentity;

import java.util.Objects;
import java.util.function.Consumer;

public final class MachineStateTracker<T> {
    private final Consumer<T> syncAction;
    private T lastSnapshot;

    public MachineStateTracker(Consumer<T> syncAction) {
        this.syncAction = Objects.requireNonNull(syncAction, "syncAction");
    }

    public void sync(T snapshot) {
        if (snapshot == null || snapshot.equals(lastSnapshot)) {
            return;
        }

        lastSnapshot = snapshot;
        syncAction.accept(snapshot);
    }

    public void reset() {
        lastSnapshot = null;
    }
}
