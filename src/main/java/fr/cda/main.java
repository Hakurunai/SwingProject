package fr.cda;

import fr.cda.view.MainApp;

import javax.swing.*;

public class main
{
    public final static boolean RUN_APP = true;

    public static final String MAIN_APP_NAME = "MyName";
    public static final int MAIN_APP_WIDTH = 720;
    public static final int MAIN_APP_HEIGHT = 400;

    public static void RunApp()
    {
        //InvokeLater is used to avoid potential issue whith swing with thread
        SwingUtilities.invokeLater(() ->
        {
            MainApp mainApp = new MainApp(MAIN_APP_NAME, MAIN_APP_WIDTH, MAIN_APP_HEIGHT, true);
        });
    }

    public static void QuickTest()
    {

    }

    public static void main(String[] args)
    {
        if (RUN_APP == true)
            RunApp();
        else
            QuickTest();
    }
}
