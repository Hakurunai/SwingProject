package fr.cda.controller;

/**
 * An exception specifically designed to be throw by the GUIController if the connection with the Database has a problem
 */
public class DatabaseConnectionException extends RuntimeException
{
    public DatabaseConnectionException(String message)
    {
        super(message);
    }
}
