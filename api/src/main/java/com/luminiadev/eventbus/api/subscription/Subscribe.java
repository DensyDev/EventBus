package com.luminiadev.eventbus.api.subscription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler.
 * The method must have exactly one parameter which extends {@link com.luminiadev.eventbus.api.Event}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * Defines the execution priority of the handler.
     * Higher values are executed first. Default is 0 (Normal).
     *
     * @return the execution priority
     */
    int priority() default 0;

    /**
     * Defines whether the handler should be executed asynchronously.
     * If true, the event bus will use its executor service to run this method.
     *
     * @return true if the handler should run asynchronously
     */
    boolean async() default false;

    /**
     * Defines if the handler should still run if the event has been cancelled.
     *
     * @return false if the handler ignores the cancelled state
     */
    boolean ignoreCancelled() default false;
}