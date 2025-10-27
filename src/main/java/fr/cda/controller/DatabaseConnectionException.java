package fr.cda.controller;

public class DatabaseConnectionException extends RuntimeException
{
    public DatabaseConnectionException(String message)
    {
        super(message);
    }
}
