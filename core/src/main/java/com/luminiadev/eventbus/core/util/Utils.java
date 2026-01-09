package com.luminiadev.eventbus.core.util;

import com.luminiadev.eventbus.api.Cancellable;
import com.luminiadev.eventbus.api.subscription.Subscriber;

public class Utils {

    public static boolean isEventCancelled(Object event) {
        return event instanceof Cancellable cancellable && cancellable.isCancelled();
    }

    public static boolean shouldCallSubscriber(Subscriber<?> subscriber, Object event) {
        return !subscriber.isIgnoreCancelled() || !isEventCancelled(event);
    }
}
