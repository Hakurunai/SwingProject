package fr.cda;

import fr.cda.event.EventBus;
import fr.cda.view.FrontApp;
import fr.cda.model.BackApp;
import fr.cda.model.Database;


public class main
{
    public final static boolean RUN_APP = true;


    public static void RunApp()
    {
        Database database = new Database();
        BackApp backApp = new BackApp(database);
        FrontApp frontApp = new FrontApp();
        frontApp.EstablishConnectionWithBack(backApp);
    }

    public static void QuickTest()
    {
//        TestReceiver testReceiver = new TestReceiver();
//        EventBus bus = new EventBus();
//        bus.Subscribe(String.class, testReceiver);
//        bus.Subscribe(Integer.class, testReceiver.integerListener);
//
//        bus.Publish(10);
    }


    public static void main(String[] args)
    {
        if (RUN_APP == true)
            RunApp();
        else
            QuickTest();
    }
}
