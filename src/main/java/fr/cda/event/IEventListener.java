package fr.cda.event;

public interface IEventListener<T>
{
    public void OnEvent(T event);
}
