package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.BackApp;
import fr.cda.util.LoggerHelper;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * The object owning the complete view AND the {@link GUIController}
 */
public class FrontApp
{
    private HomeFrame mainApp;

    private SwingViewConfig siteConfig;

    final GUIController controller;

    public FrontApp()
    {
        controller = new GUIController();

        LoadConfig();
        LaunchApp();
    }

    /**
     * Basic method to emulate the connection between a back and a front
     * Allow us to link our internal {@link GUIController} to an extern {@link fr.cda.controller.AppController}
     * @param back The back we want to be connected with
     */
    public void EstablishConnectionWithBack(final BackApp back)
    {
        LoggerHelper.log.info("TRY to connect the FrontApp to the BackApp");
        if (back.HasAnActiveConnection())
        {
            LoggerHelper.log.error("FAIL impossible to get a connection from the BackApp");
            return;
        }
        controller.ConnectToBackApp(back);
    }

    /**
     * Load the config for the site, trying to load the custom one first
     */
    private void LoadConfig()
    {
        LoggerHelper.log.info("START loading config");
        if (!LoadCustomConfig())
        {
            LoggerHelper.log.warn("FAIL to load custom config, load default config instead");
            LoadDefaultConfig();
        }
    }

    /**
     * Load a custom file config for the site
     * @return true if the loading was successful, else otherwise
     */
    private boolean LoadCustomConfig()
    {
        LoggerHelper.log.info("TRY loading custom config");
        //Todo : load custom config
        return false;
    }


    /**
     * Load a static SiteConfig inside this object
     */
    private void LoadDefaultConfig() {siteConfig = SwingViewConfig.DEFAULT;}

    /**
     * Order and call the different steps necessary for the application to work
     */
    private void LaunchApp()
    {
        CreateApplication();
    }

    /**
     * Instantiate the MainApp object, resulting in the creation of the interface
     */
    private void CreateApplication()
    {
        try
        {
            SwingUtilities.invokeAndWait(() ->
            {
                mainApp = new HomeFrame(siteConfig, controller);
                mainApp.Display();
            });
        } catch (InterruptedException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
