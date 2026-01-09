package com.luminiadev.eventbus.api;

import com.luminiadev.eventbus.api.subscription.Subscriber;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * A component used for dispatching events and managing subscriptions.
 */
public interface EventBus {

    /**
     * Retrieves all registered subscribers.
     *
     * @return an unmodifiable map of event classes and their associated subscribers
     */
    @UnmodifiableView
    Map<Class<? extends Event>, List<Subscriber<?>>> getSubscribers();

    /**
     * Retrieves a list of subscribers for a specific event class.
     *
     * @param eventClass the class of the event
     * @param <E>        the type of the event
     * @return an unmodifiable list of subscribers for the given event type
     */
    @UnmodifiableView
    <E extends Event> List<Subscriber<E>> getSubscribers(Class<E> eventClass);

    /**
     * Registers all methods marked with @Subscribe annotation within the given listener object.
     *
     * @param listener the object containing event handler methods
     * @see com.luminiadev.eventbus.api.subscription.Subscribe
     */
    void subscribe(Object listener);

    /**
     * Subscribes a functional handler to a specific event type.
     *
     * @param eventClass    the class of the event to listen for
     * @param eventListener the logic to execute when the event is called
     * @param <E>           the type of the event
     * @return the created subscriber instance
     */
    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventListener);

    /**
     * Subscribes a functional handler with a specific priority.
     *
     * @param eventClass    the class of the event
     * @param eventListener the event listener
     * @param priority      the execution priority (higher values execute earlier)
     * @param <E>           the type of the event
     * @return the created subscriber instance
     */
    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventListener, int priority);

    /**
     * Subscribes a functional handler with priority and cancellation ignore settings.
     *
     * @param eventClass      the class of the event
     * @param eventListener   the event listener
     * @param priority        the execution priority (higher values execute earlier)
     * @param ignoreCancelled if false, the handler runs even if the event was cancelled
     * @param <E>             the type of the event
     * @return the created subscriber instance
     */
    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventListener, int priority, boolean ignoreCancelled);

    /**
     * Subscribes a functional handler with full control over execution settings.
     *
     * @param eventClass      the class of the event
     * @param eventListener   the event listener
     * @param priority        the execution priority (higher values execute earlier)
     * @param ignoreCancelled if false, the handler runs even if the event was cancelled
     * @param async           if true, the handler will be executed asynchronously
     * @param <E>             the type of the event
     * @return the created subscriber instance
     */
    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, EventListener<E> eventListener, int priority, boolean ignoreCancelled, boolean async);

    /**
     * Directly registers a pre-defined subscriber.
     *
     * @param eventClass the class of the event
     * @param subscriber the subscriber instance to register
     * @param <E>        the type of the event
     * @return the registered subscriber
     */
    <E extends Event> Subscriber<E> subscribe(Class<E> eventClass, Subscriber<E> subscriber);

    /**
     * Unregisters all event handlers associated with the given listener object.
     *
     * @param listener the listener object to unregister
     */
    void unsubscribe(Object listener);

    /**
     * Unregisters a specific subscriber.
     *
     * @param subscriber the subscriber to remove
     */
    void unsubscribe(Subscriber<?> subscriber);

    /**
     * Dispatches an event to all registered subscribers.
     *
     * @param event the event instance to call
     * @param <E>   the type of the event
     * @return the event instance after all handlers have processed it
     */
    <E extends Event> E call(E event);

    /**
     * Retrieves the executor service used for handling asynchronous events.
     *
     * @return the async executor service
     */
    ExecutorService getAsyncExecutor();
}