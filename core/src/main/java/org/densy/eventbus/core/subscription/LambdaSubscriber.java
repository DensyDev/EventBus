package org.densy.eventbus.core.subscription;

import org.densy.eventbus.api.Event;
import org.densy.eventbus.api.EventListener;

import java.util.concurrent.ExecutorService;

/**
 * Lambda subscriber for handling EventListener.
 *
 * @param <E> the event type
 */
public class LambdaSubscriber<E extends Event> extends AbstractSubscriber<E> {

    private final EventListener<E> listener;

    public LambdaSubscriber(Class<E> eventClass, int priority, boolean handleCancelled, boolean async, EventListener<E> listener, ExecutorService asyncExecutor) {
        super(eventClass, priority, handleCancelled, async, asyncExecutor);
        this.listener = listener;
    }

    @Override
    protected void execute(E event) {
        this.listener.onEvent(event);
    }
}
