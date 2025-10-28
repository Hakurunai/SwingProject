package fr.cda;

import fr.cda.Site.Site;
import fr.cda.model.BackApp;
import fr.cda.model.Category;
import fr.cda.model.Database;
import fr.cda.model.Product;

import java.util.concurrent.*;


public class main
{
    public final static boolean RUN_APP = true;

    private static Site site;

    public static void RunApp()
    {
        //site = new Site();
        Database database = new Database();
        BackApp backApp = new BackApp(database);
    }

    public static void QuickTest()
    {
        Database database = new Database();

        var res = database.CreateNewProduct(Product.GenerateProductDTO("NAME", new Category("Livre"), 1f, 0));
        System.out.println(res.getMessage());
    }


    public static void main(String[] args)
    {
        if (RUN_APP == true)
            RunApp();
        else
            QuickTest();
    }
}
