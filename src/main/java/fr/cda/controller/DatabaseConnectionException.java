package fr.cda.controller;

/**
 * An exception designed to be thrown by the {@link GUIController} if there is a problem with Database connection and
 * someone try to use his services
 */
public class DatabaseConnectionException extends RuntimeException
{
    public DatabaseConnectionException(String message)
    {
        super(message);
    }
}
