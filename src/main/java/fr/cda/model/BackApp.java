package fr.cda.model;

import fr.cda.controller.AppController;

public class BackApp
{
    private final Database database;
    private final AppController appController;
    private boolean hasAnActiveConnection = false;

    public BackApp(final Database database)
    {
        this.database = database;
        appController = new AppController(database);
    }

    public boolean HasNoActiveConnection()
    {
        return hasAnActiveConnection;
    }

    public OperationResult<AppController> RequestNewConnection()
    {
        if (HasNoActiveConnection())
            return OperationResult.FAILURE("The connection is not accessible");

        hasAnActiveConnection = true;
        return OperationResult.SUCCESS(appController, "Connection granted");
    }
}
