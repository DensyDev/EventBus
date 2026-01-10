package com.luminiadev.eventbus.core.subscription;

import com.luminiadev.eventbus.api.Event;
import com.luminiadev.eventbus.api.exception.EventException;
import me.sunlan.fastreflection.FastMemberLoader;
import me.sunlan.fastreflection.FastMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Method subscriber for handling methods subscribed using the @Subscribe annotation.
 *
 * @param <E> the event type
 */
public class MethodSubscriber<E extends Event> extends AbstractSubscriber<E> {

    private static final Map<ClassLoader, FastMemberLoader> FAST_MEMBER_LOADERS = new ConcurrentHashMap<>();

    private final FastMethod method;
    private final Object listener;

    public MethodSubscriber(Class<E> eventClass, int priority, boolean handleCancelled, boolean async, Method method, Object listener, ExecutorService asyncExecutor) {
        super(eventClass, priority, handleCancelled, async, asyncExecutor);
        this.method = FastMethod.create(
                method,
                FAST_MEMBER_LOADERS.computeIfAbsent(
                        listener.getClass().getClassLoader(),
                        FastMemberLoader::new
                ),
                true
        );
        this.listener = listener;
    }

    @Override
    protected void execute(E event) {
        try {
            this.method.invoke(listener, event);
        } catch (Throwable t) {
            throw new EventException("Failed to call method " + method.getName() + " for listener " + listener, t);
        }
    }
}
