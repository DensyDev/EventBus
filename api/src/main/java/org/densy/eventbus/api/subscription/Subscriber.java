package org.densy.eventbus.api.subscription;

import org.densy.eventbus.api.Event;

/**
 * Represents a registered subscription to a specific event type.
 *
 * @param <E> the type of event
 */
public interface Subscriber<E extends Event> {

    /**
     * Gets the class of the event being listened for.
     *
     * @return the event class
     */
    Class<E> getEventClass();

    /**
     * Gets the execution priority of this subscriber. Higher values result in earlier execution.
     *
     * @return the priority level
     */
    int getPriority();

    /**
     * Checks if this subscriber should process events that have been cancelled.
     *
     * @return true if the subscriber ignores cancellation state, false otherwise
     */
    boolean isHandleCancelled();

    /**
     * Checks if this subscriber should be executed asynchronously.
     *
     * @return true if executed on a separate thread, false if on the calling thread
     */
    boolean isAsync();

    /**
     * Executes the handler logic for the given event.
     *
     * @param event the event instance to process
     */
    void call(E event);
}