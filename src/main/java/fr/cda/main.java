package fr.cda;

import fr.cda.view.FrontApp;
import fr.cda.model.BackApp;
import fr.cda.model.Category;
import fr.cda.model.Database;
import fr.cda.model.Product;


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
