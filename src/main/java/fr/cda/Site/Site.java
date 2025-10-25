package fr.cda.Site;

import fr.cda.model.Order;
import fr.cda.model.Product;
import fr.cda.util.LoggerHelper;
import fr.cda.view.MainApp;

import javax.swing.*;
import java.util.HashMap;

public class Site
{
    private MainApp mainApp;

    private SiteConfig siteConfig;

    private HashMap<String, Product> allProduct = new HashMap();
    private HashMap<String, Order> allOrder = new HashMap();

    public Site()
    {
        LoadConfig();
        LaunchApp();
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
    private void LoadDefaultConfig() {siteConfig = SiteConfig.DEFAULT;}

    /**
     * Order and call the different steps necessary for the application to work
     */
    private void LaunchApp()
    {
        //Todo : load files in the site

        //Todo : create the DAO and inject itself onto it

        //Todo : create the controller and inject the DTO inside

        //Todo : create the application and inject the controller inside
        CreateApplication();
    }

    /**
     * Instantiate the MainApp object, resulting in the creation of the interface
     */
    private void CreateApplication()
    {
        SwingUtilities.invokeLater(() ->
        {
            mainApp = new MainApp(siteConfig.getSiteName(), siteConfig.getAppWidth(), siteConfig.getAppHeight(), true);
        });
    }
}
