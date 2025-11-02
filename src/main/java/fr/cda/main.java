package fr.cda;

import fr.cda.view.FrontApp;
import fr.cda.model.BackApp;
import fr.cda.model.Database;

/**
 * Entry point of the program
 */
public class main
{
    /**
     * Start the main thread and use to set up the initial entity between each others
     */
    public static void RunApp()
    {
        Database database = new Database();
        BackApp backApp = new BackApp(database);
        FrontApp frontApp = new FrontApp();
        frontApp.EstablishConnectionWithBack(backApp);
    }


    public static void main(String[] args)
    {
        RunApp();
    }
}
