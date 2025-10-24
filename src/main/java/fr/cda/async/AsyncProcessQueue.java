package fr.cda.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncProcessQueue
{
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final Thread workerThread;

    public AsyncProcessQueue(final String threadName)
    {
        workerThread = new Thread(this::ProcessOperationOnQueue, threadName);
        workerThread.setDaemon(true); //allow to close the app while the thread is still running
        workerThread.start();
    }

    public void Enqueue(final Runnable task)
    {
        queue.offer(task);
    }

    private void ProcessOperationOnQueue()
    {
        try
        {
            while (true)
            {
                Runnable task = queue.take(); // allow the Thread to wait for a new Task without wasting CPU power
                try
                {
                    task.run();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
