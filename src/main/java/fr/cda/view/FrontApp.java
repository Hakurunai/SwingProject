package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.BackApp;
import fr.cda.util.LoggerHelper;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

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
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

    }
}
