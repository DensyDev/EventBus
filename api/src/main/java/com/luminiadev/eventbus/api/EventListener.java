package com.luminiadev.eventbus.api;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
