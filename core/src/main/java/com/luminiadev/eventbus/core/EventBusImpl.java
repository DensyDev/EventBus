package com.luminiadev.eventbus.core;

import com.luminiadev.eventbus.api.Event;
import com.luminiadev.eventbus.api.EventBus;
import com.luminiadev.eventbus.api.EventListener;
import com.luminiadev.eventbus.api.exception.EventException;
import com.luminiadev.eventbus.api.subscription.Subscribe;
import com.luminiadev.eventbus.api.subscription.Subscriber;
import com.luminiadev.eventbus.core.subscription.LambdaSubscriber;
import com.luminiadev.eventbus.core.subscription.MethodSubscriber;
import com.luminiadev.eventbus.core.util.Utils;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBusImpl implements EventBus {

    private final Map<Class<? extends Event>, List<Subscriber<?>>> subscribersByEvent = new ConcurrentHashMap<>();
    private final Map<Object, List<Subscriber<?>>> subscribersByListener = new ConcurrentHashMap<>();
    private final ExecutorService asyncExecutor;

    private static final Comparator<Subscriber<?>> PRIORITY_COMPARATOR = (s1, s2) -> Integer.compare(s2.getPriority(), s1.getPriority());

    public EventBusImpl() {
        this(Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r);
            thread.setName("EventBus-Async-" + thread.getId());
            thread.setDaemon(true);
            return thread;
        }));
    }

    public EventBusImpl(ExecutorService asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public @UnmodifiableView Map<Class<? extends Event>, List<Subscriber<?>>> getSubscribers() {
        return Collections.unmodifiableMap(subscribersByEvent);
    }

    @Override
    public @UnmodifiableView <E extends Event> List<Subscriber<E>> getSubscribers(Class<E> eventClass) {
        List<Subscriber<?>> subscribers = subscribersByEvent.get(eventClass);
        if (subscribers == null) {
            return Collections.emptyList();
        }
        // noinspection unchecked
        return Collections.unmodifiableList((List<Subscriber<E>>) (List<?>) subscribers);
    }

    @Override
    public void subscribe(Object listener) {
        if (subscribersByListener.containsKey(listener)) {
            return;
        }

        for (Class<?> type = listener.getClass(); type != null; type = type.getSuperclass()) {
            for (Method method : type.getDeclaredMethods()) {
                Subscribe annotation = method.getAnnotation(Subscribe.class);

                if (annotation == null) {
                    continue;
                }

                if (method.getReturnType() != void.class) {
                    throw new EventException("Method " + method + " must return void");
                }

                if (method.getParameterCount() != 1) {
                    throw new EventException("Method " + method + " must have exactly 1 parameter");
                }

                Class<?> paramType = method.getParameterTypes()[0];
                if (!Event.class.isAssignableFrom(paramType)) {
                    throw new EventException("Parameter in " + method + " must be an Event subclass");
                }

                @SuppressWarnings("unchecked")
                Class<Event> eventClass = (Class<Event>) paramType;

                Subscriber<Event> subscriber = new MethodSubscriber<>(
                        eventClass,
                        annotation.priority(),
                        annotation.ignoreCancelled(),
                        annotation.async(),
                        method,
                        listener,
                        asyncExecutor
                );

                subscribe(eventClass, subscriber);
                subscribersByListener.computeIfAbsent(listener, k -> new ArrayList<>()).add(subscriber);
            }
        }
    }

    @Override
    public <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler) {
        return this.subscribe(eventClass, eventHandler, 0);
    }

    @Override
    public <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority) {
        return this.subscribe(eventClass, eventHandler, priority, false);
    }

    public <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority, boolean ignoreCancelled) {
        return this.subscribe(eventClass, eventHandler, priority, ignoreCancelled, false);
    }

    @Override
    public <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventHandler, int priority, boolean ignoreCancelled, boolean async) {
        LambdaSubscriber<E> subscriber = new LambdaSubscriber<>(
                eventClass,
                priority,
                ignoreCancelled,
                async,
                eventHandler,
                asyncExecutor
        );
        return this.subscribe(eventClass, subscriber);
    }

    @Override
    public <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, Subscriber<E> subscriber) {
        subscribersByEvent.compute(eventClass, (k, list) -> {
            if (list == null) {
                list = new CopyOnWriteArrayList<>();
            }
            list.add(subscriber);
            list.sort(PRIORITY_COMPARATOR);
            return list;
        });
        return subscriber;
    }

    @Override
    public void unsubscribe(Object listener) {
        List<Subscriber<?>> subscribers = subscribersByListener.get(listener);
        if (subscribers == null) {
            return;
        }
        for (Subscriber<?> subscriber : subscribers) {
            this.unsubscribe(subscriber);
        }
        subscribersByListener.remove(listener);
    }

    @Override
    public void unsubscribe(Subscriber<?> subscriber) {
        Class<? extends Event> eventClass = subscriber.getEventClass();

        subscribersByEvent.computeIfPresent(eventClass, (k, list) -> {
            list.remove(subscriber);
            return list.isEmpty() ? null : list; // remove the list if it is empty
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Event> E callEvent(E event) {
        Class<E> eventClass = (Class<E>) event.getClass();

        List<Subscriber<E>> subscribers = this.getSubscribers(eventClass);
        if (!subscribers.isEmpty()) {
            for (Subscriber<E> subscriber : subscribers) {
                if (Utils.shouldCallSubscriber(subscriber, event)) {
                    subscriber.execute(event);
                }
            }
        }
        return event;
    }

    @Override
    public ExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }
}
