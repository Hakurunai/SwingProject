package fr.cda.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus
{
    private final Map<Class<?>, List<IEventListener<?>>> subscribers = new HashMap<>();

    public <T> void Subscribe(final Class<T> eventType, final IEventListener<T> listener)
    {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("Unchecked")
    public <T> void Publish(T eventType)
    {
        List<IEventListener<?>> handlers = subscribers.get(eventType.getClass());
        if (handlers == null)
            return;

        for (IEventListener<?> handler : handlers)
            ((IEventListener<T>) handler).OnEvent(eventType);
    }
}
