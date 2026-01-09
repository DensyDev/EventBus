package com.luminiadev.eventbus.core.subscription;

import com.luminiadev.eventbus.api.Event;
import com.luminiadev.eventbus.api.subscription.Subscriber;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;

/**
 * Basic class for all subscribers.
 *
 * @param <E> the event type
 */
@AllArgsConstructor
public abstract class AbstractSubscriber<E extends Event> implements Subscriber<E> {

    private final Class<E> eventClass;
    private final int priority;
    private final boolean ignoreCancelled;
    private final boolean async;
    private final ExecutorService asyncExecutor;

    @Override
    public Class<E> getEventClass() {
        return eventClass;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

    @Override
    public boolean isAsync() {
        return async;
    }

    @Override
    public final void execute(E event) {
        // If the event is asynchronous, we call the
        // internal handler method via the async executor.
        if (!async) {
            executeInternal(event);
        } else {
            asyncExecutor.submit(() -> executeInternal(event));
        }
    }

    /**
     * Internal method for calling an event handler.
     *
     * @param event the event
     */
    protected abstract void executeInternal(E event);
}
