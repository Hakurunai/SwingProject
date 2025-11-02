package fr.cda.view.swing.async;

import fr.cda.async.AsyncQueue;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Specialization for Swing of AsyncQueue. The consumer used as a callback will be wrapped by SwingUtilities.invokeLater
 */
public class SwingAsyncQueue extends AsyncQueue
{
    /**
     *
     * @param threadName The name of the internal Thread. Can be used in the log for example
     */
    public SwingAsyncQueue(final String threadName)
    {
        super(threadName);
    }

    /**
     *
     * @param operation a supplier who will be executed first
     * @param onOperationDone a Consumer acting as a callback WHO WILL BE WRAPPED WITH SwingUtilities.invokeLater
     *                        who will use the returned value from the supplier as a parameter
     * @param <T> a type expected to be sequentially use by the Supplier then the Consumer
     */
    @Override
    public <T> void SubmitAsyncOperation(Supplier<T> operation, Consumer<T> onOperationDone)
    {
        Consumer<T> swingCallback = result ->
        {
            SwingUtilities.invokeLater(() ->
            {
                onOperationDone.accept(result);
            });
        };
        super.SubmitAsyncOperation(operation, swingCallback);
    }
}
