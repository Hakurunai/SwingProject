package fr.cda.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a container of Runnable with his own thread
 * He will automatically run any new runnable added to it
 * Runnables are executed sequentially by arrival order, only when the previous one is ended
 */
public class AsyncProcessQueue
{
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final Thread workerThread;

    /**
     * This ctor will directly instantiate and start the internal thread
     * @param threadName Specific name for the internal thread
     */
    public AsyncProcessQueue(final String threadName)
    {
        workerThread = new Thread(this::ProcessOperationOnQueue, threadName);
        workerThread.setDaemon(true); //allow to close the app while the thread is still running
        workerThread.start();
    }

    /**
     * Add a new runnable to the container to execute
     * @param runnable The runnable to execute
     */
    public void Enqueue(final Runnable runnable)
    {
        queue.offer(runnable);
    }

    /**
     * Method executed by the internal Thread as long as this container exist
     */
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
