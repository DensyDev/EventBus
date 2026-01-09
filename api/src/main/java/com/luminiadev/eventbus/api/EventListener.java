package com.luminiadev.eventbus.api;

/**
 * Event listener interface.
 *
 * @param <E> the type of the event
 */
@FunctionalInterface
public interface EventListener<E extends Event> {

    /**
     * Called when handling an event.
     *
     * @param event the listened event
     */
    void onEvent(E event);
}
