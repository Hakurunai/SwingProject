package fr.cda.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classic EventBus system : anyone knowing him can subscribe to a type of desired event and be notified if
 * the bus publish an event of this type
 */
public class EventBus
{
    private final Map<Class<?>, List<IEventListener<?>>> subscribers = new HashMap<>();

    /**
     * Method to call if you want to be notified of a future event published by the bus
     * @param eventType The type of event you subscribe to, not an instance of an object
     * @param listener The object who will receive the notification of a specific type of event
     * @param <T> The specific type of event you want to subscribe to
     */
    public <T> void Subscribe(final Class<T> eventType, final IEventListener<T> listener)
    {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Method to call to ask the bus to publish an event of any type
     * Already registered listener of this type will be notified
     * @param eventType The event you want to send, all listener will receive it
     * @param <T> The type of event you want to publish
     */
    public <T> void Publish(T eventType)
    {
        List<IEventListener<?>> handlers = subscribers.get(eventType.getClass());
        if (handlers == null)
            return;

        for (IEventListener<?> handler : handlers)
            ((IEventListener<T>) handler).OnEvent(eventType);
    }
}
