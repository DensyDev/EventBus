package org.densy.eventbus.core.util;

import org.densy.eventbus.api.Cancellable;
import org.densy.eventbus.api.subscription.Subscriber;

import java.util.*;

/**
 * A general event utilities.
 */
public class Utils {

    /**
     * Checks that the event has been canceled.
     *
     * @param event the event object
     * @return true if cancelled, otherwise false
     */
    public static boolean isEventCancelled(Object event) {
        return event instanceof Cancellable cancellable && cancellable.isCancelled();
    }

    /**
     * Checks whether to call the subscriber handler.
     *
     * @param subscriber the event subscriber
     * @param event      the event object
     * @return true if should, otherwise false
     */
    public static boolean shouldCallSubscriber(Subscriber<?> subscriber, Object event) {
        return subscriber.isHandleCancelled() || !isEventCancelled(event);
    }

    /**
     * Retrieves the entire type hierarchy of a class, including all superclasses and interfaces.
     *
     * @param clazz the class to inspect
     * @return a set of all inherited classes and interfaces
     */
    public static Set<Class<?>> getClassHierarchy(Class<?> clazz) {
        Set<Class<?>> result = new LinkedHashSet<>();
        Queue<Class<?>> stack = new LinkedList<>();

        if (clazz != null) {
            stack.add(clazz);
        }

        while (!stack.isEmpty()) {
            Class<?> current = stack.poll();
            if (current == null || !result.add(current)) {
                continue;
            }

            Class<?> superClass = current.getSuperclass();
            if (superClass != null) {
                stack.add(superClass);
            }

            Collections.addAll(stack, current.getInterfaces());
        }

        return result;
    }
}
