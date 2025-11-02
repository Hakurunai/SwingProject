package fr.cda.controller;

/**
 * Transmitted event by the {@link GUIController#eventBus}
 * Use to determine if the connection status has changed between the {@link GUIController} and his referenced {@link AppController}
 * @param message A simple message explaining why the event has been published
 * @param isConnected A value telling if the {@link GUIController} is connected to the {@link fr.cda.model.Database}
 * via an {@link AppController}
 */
public record DatabaseConnectionEvent(String message, boolean isConnected)
{
}
