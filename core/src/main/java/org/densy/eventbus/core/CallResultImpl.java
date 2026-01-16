package org.densy.eventbus.core;

import org.densy.eventbus.api.CallResult;
import org.densy.eventbus.api.subscription.Subscriber;
import lombok.ToString;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

@ToString
public class CallResultImpl implements CallResult {

    private final Map<Subscriber<?>, Throwable> exceptions;
    private final boolean cancelled;

    public CallResultImpl(Map<Subscriber<?>, Throwable> exceptions, boolean cancelled) {
        this.exceptions = Collections.unmodifiableMap(exceptions);
        this.cancelled = cancelled;
    }

    @Override
    public @UnmodifiableView Map<Subscriber<?>, Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public boolean isSuccess() {
        return exceptions.isEmpty();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
