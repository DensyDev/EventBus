package com.luminiadev.eventbus.api;

/**
 * A collection of constant values representing event execution priorities.
 * Handlers with higher priority values are executed before those with lower values.
 */
public interface EventPriorities {

    /**
     * The highest priority. Handlers with this priority are executed first.
     */
    int FIRST = 100;

    /**
     * A high priority for handlers that should run early in the execution chain.
     */
    int EARLY = 50;

    /**
     * The default priority for most event handlers.
     */
    int NORMAL = 0;

    /**
     * A low priority for handlers that should run towards the end of the execution chain.
     */
    int LATE = -50;

    /**
     * The lowest priority. Handlers with this priority are executed last.
     */
    int LAST = -100;
}