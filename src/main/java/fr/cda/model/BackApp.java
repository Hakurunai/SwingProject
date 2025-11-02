package fr.cda.model;

import fr.cda.controller.AppController;

/**
 * Object owing the {@link Database} and the {@link AppController} of the application
 */
public class BackApp
{
    private final Database database;
    private final AppController appController;
    private boolean hasAnActiveConnection = false;

    /**
     *
     * @param database
     */
    public BackApp(final Database database)
    {
        this.database = database;
        appController = new AppController(database);
    }

    /**
     * Call to know if the BackApp is already linked to something using her {@link AppController}
     * @return True if no one has ever requested a connection, false otherwise
     */
    public boolean HasAnActiveConnection()
    {
        return hasAnActiveConnection;
    }

    /**
     * Use to retrieve the internal {@link AppController}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, the internal {@link AppController}
     * */
    public OperationResult<AppController> RequestNewConnection()
    {
        if (HasAnActiveConnection())
            return OperationResult.FAILURE("The connection is not accessible");

        hasAnActiveConnection = true;
        return OperationResult.SUCCESS(appController, "Connection granted");
    }
}
