package com.luminiadev.eventbus.core.util;

import com.luminiadev.eventbus.api.Cancellable;
import com.luminiadev.eventbus.api.subscription.Subscriber;

/**
 * A general event utilities.
 */
public class Utils {

    /**
     * Checks that the event has been canceled.
     *
     * @param event the event instance
     * @return true if cancelled, otherwise false
     */
    public static boolean isEventCancelled(Object event) {
        return event instanceof Cancellable cancellable && cancellable.isCancelled();
    }

    /**
     * Checks whether to call the subscriber handler.
     *
     * @param subscriber the event subscriber
     * @param event      the event
     * @return true if should, otherwise false
     */
    public static boolean shouldCallSubscriber(Subscriber<?> subscriber, Object event) {
        return !subscriber.isIgnoreCancelled() || !isEventCancelled(event);
    }
}
