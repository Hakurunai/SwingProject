package fr.cda.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This object can process multiple operation sequentially on his own thread
 * via the method SubmitAsyncOperation
 */
public class AsyncQueue
{
    ExecutorService executor;

    public AsyncQueue(final String threadName)
    {
        executor = Executors.newSingleThreadExecutor(runnable ->
        {
            Thread thread = new Thread(runnable, threadName);
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * We can stack a new process who will be executed when all the previous one are ended
     * @param operation a supplier who will be executed first
     * @param onOperationDone a Consumer acting as a callback who will use the returned value from the supplier as a parameter
     * @param <T>
     */
    public <T> void SubmitAsyncOperation(final Supplier<T> operation, final Consumer<T> onOperationDone)
    {
        CompletableFuture.supplyAsync(operation, executor)
                .thenAccept(onOperationDone);
    }

    /**
     * Proper way to stop the internal Thread
     */
    public void Shutdown()
    {
        executor.shutdown();
    }
}
