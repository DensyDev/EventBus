package com.luminiadev.eventbus.api;

import com.luminiadev.eventbus.api.subscription.Subscriber;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public interface EventBus {

    @UnmodifiableView
    Map<Class<? extends Event>, List<Subscriber<?>>> getSubscribers();

    @UnmodifiableView
    <E extends Event> List<Subscriber<E>> getSubscribers(Class<E> eventClass);

    void subscribe(Object listener);

    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler);

    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority);

    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority, boolean ignoreCancelled);

    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority, boolean ignoreCancelled, boolean async);

    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, Subscriber<E> subscriber);

    void unsubscribe(Object listener);

    void unsubscribe(Subscriber<?> subscriber);

    <E extends Event> E call(E event);

    ExecutorService getAsyncExecutor();
}
