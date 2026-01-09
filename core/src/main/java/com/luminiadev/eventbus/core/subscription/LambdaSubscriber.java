package com.luminiadev.eventbus.core.subscription;

import com.luminiadev.eventbus.api.Event;
import com.luminiadev.eventbus.api.EventListener;

import java.util.concurrent.ExecutorService;

/**
 * Lambda subscriber for handling EventListener.
 *
 * @param <E> the event type
 */
public class LambdaSubscriber<E extends Event> extends AbstractSubscriber<E> {

    private final EventListener<E> listener;

    public LambdaSubscriber(Class<E> eventClass, int priority, boolean ignoreCancelled, boolean async, EventListener<E> listener, ExecutorService asyncExecutor) {
        super(eventClass, priority, ignoreCancelled, async, asyncExecutor);
        this.listener = listener;
    }

    @Override
    protected void executeInternal(E event) {
        listener.onEvent(event);
    }
}
