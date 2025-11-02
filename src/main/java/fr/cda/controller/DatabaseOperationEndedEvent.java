package fr.cda.controller;

/**
 * Event published by the {@link GUIController} when an operation he asked to his linked {@link AppController} is ended
 * Useful to retrieve the message contained in the {@link fr.cda.model.OperationResult} to display it in the interface
 * @param message A message containing some information about the ended operation
 */
public record DatabaseOperationEndedEvent(String message)
{
}
