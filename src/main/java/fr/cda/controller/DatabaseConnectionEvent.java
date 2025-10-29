package fr.cda.controller;

public record DatabaseConnectionEvent(String message, boolean isConnected)
{
}
