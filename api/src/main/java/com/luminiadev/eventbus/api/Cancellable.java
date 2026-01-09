package com.luminiadev.eventbus.api;

/**
 * An interface for defining that an event can be canceled.
 */
public interface Cancellable {

    /**
     * Checks if event is canceled.
     *
     * @return true if event cancelled, otherwise false
     */
    boolean isCancelled();

    /**
     * Sets the event to canceled or cancels the cancellation.
     *
     * @param cancelled is event cancelled
     */
    void setCancelled(boolean cancelled);

    /**
     * Cancels the event.
     */
    default void cancel() {
        this.setCancelled(true);
    }
}
