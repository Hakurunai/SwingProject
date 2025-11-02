package fr.cda.event;

public interface IEventListener<T>
{
    void OnEvent(T event);
}
