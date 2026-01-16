package org.densy.eventbus.api;

import org.densy.eventbus.api.subscription.Subscriber;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

/**
 * Represents the result of an event call.
 */
public interface CallResult {

    /**
     * Retrieves a map of exceptions thrown during the event execution.
     *
     * @return an unmodifiable map of subscribers and their respective throwables
     */
    @UnmodifiableView
    Map<Subscriber<?>, Throwable> getExceptions();

    /**
     * Checks if the event was handled without any exceptions.
     *
     * @return true if no exceptions were thrown, false otherwise
     */
    boolean isSuccess();

    /**
     * Checks if the event ended in a cancelled state.
     *
     * @return true if the event was cancelled, false otherwise
     */
    boolean isCancelled();
}