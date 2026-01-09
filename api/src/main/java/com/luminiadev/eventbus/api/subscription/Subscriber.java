package com.luminiadev.eventbus.api.subscription;

import com.luminiadev.eventbus.api.Event;

public interface Subscriber<E extends Event> {

    Class<E> getEventClass();

    int getPriority();

    boolean isIgnoreCancelled();

    boolean isAsync();

    void execute(E event);
}
