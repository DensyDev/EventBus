package com.luminiadev.eventbus.api;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    default void cancel() {
        this.setCancelled(true);
    }
}
