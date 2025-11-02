package fr.cda.event;

/**
 * Interface used to work in tandem with the {@link EventBus} class
 * @param <T> The type of event a listener will subscribe to
 */
public interface IEventListener<T>
{
    void OnEvent(T event);
}
