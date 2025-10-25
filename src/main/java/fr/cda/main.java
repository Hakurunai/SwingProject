package fr.cda;

import fr.cda.Site.Site;
import fr.cda.async.AsyncQueue;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class main
{
    public final static boolean RUN_APP = false;

    private static Site site;

    public static void RunApp()
    {
        site = new Site();
    }

    public static void QuickTest()
    {
        AsyncQueue q = new AsyncQueue("QuickTest");

        Supplier<Integer> supplier = () -> 10;
        Consumer<Integer> consumer = (param) ->
        {
            for (int i = 1; i <= param; i++)
            {
                System.out.println(i);
            }
        };

        q.SubmitAsyncOperation(supplier, consumer);
        q.SubmitAsyncOperation(supplier, consumer);
        q.SubmitAsyncOperation(supplier, consumer);
        q.SubmitAsyncOperation(supplier, consumer);
        q.Shutdown();
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException p_e)
        {
            throw new RuntimeException(p_e);
        }
    }


    public static void main(String[] args)
    {
        if (RUN_APP == true)
            RunApp();
        else
            QuickTest();
    }
}
