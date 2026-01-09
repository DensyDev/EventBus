package com.luminiadev.eventbus.core;

import com.luminiadev.eventbus.api.CallResult;
import com.luminiadev.eventbus.api.Event;
import com.luminiadev.eventbus.api.subscription.Subscriber;
import com.luminiadev.eventbus.core.util.Utils;
import lombok.ToString;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

@ToString
public class CallResultImpl implements CallResult {

    private final Event event;
    private final Map<Subscriber<?>, Throwable> exceptions;

    public CallResultImpl(Event event, Map<Subscriber<?>, Throwable> exceptions) {
        this.event = event;
        this.exceptions = Collections.unmodifiableMap(exceptions);
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
        return Utils.isEventCancelled(event);
    }
}
