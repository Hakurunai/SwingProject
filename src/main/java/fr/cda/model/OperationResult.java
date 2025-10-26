package fr.cda.model;

/**
 * Represent a success/failure of an operation. Transport a
 * message and an object resulting of this operation.
 * @param <T> A data transported by this object as a result of an operation
 */
public class OperationResult <T>
{
    private final T data;
    private final String message;
    private final boolean bSuccess;

    private OperationResult(T data, String message, boolean bSuccess)
    {
        this.data = data;
        this.message = message;
        this.bSuccess = bSuccess;
    }

    /**
     * Used to create an OperationResult with a success status
     * @param data A T object resulting of the operation
     * @param message A confirming message of the success
     * @return An OperationResult of type T whose {@link #HasSucceeded()} will return true
     * @param <T> The data type of the contained data
     */
    public static <T> OperationResult<T> SUCCESS(final T data, final String message)
    {
        return new OperationResult<>(data, message, true);
    }

    /**
     * Used to create an OperationResult with a success status without any data to transport
     * @param message A confirming message of the success
     * @return An OperationResult of type T whose {@link #HasSucceeded()} will return true
     * @param <T> The data type of the contained data, defaulting to null (Void) here
     */
    public static <T> OperationResult<T> SUCCESS(final String message)
    {
        return new OperationResult<>(null, message, true);
    }

    /**
     * Used to create an OperationResult with a failed status
     * @param message The message explaining why the operation has failed
     * @return An OperationResult of type T whose {@link #HasSucceeded()} will return false
     * @param <T> The data type of the contained data, defaulting to null (Void) here
     */
    public static <T> OperationResult<T> FAILURE(final String message)
    {
        return new OperationResult<>(null, message, false);
    }

    /**
     * Used to know if the operation has succeeded
     * @return true in case of success, false otherwise
     */
    public boolean HasSucceeded() { return bSuccess; }

    public T getData()
    {
        return data;
    }

    public String getMessage()
    {
        return message;
    }
}
