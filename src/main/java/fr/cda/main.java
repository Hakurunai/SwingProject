package fr.cda;

import fr.cda.Site.Site;

public class main
{
    public final static boolean RUN_APP = true;

    private static Site site;

    public static void RunApp()
    {
        site = new Site();
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
