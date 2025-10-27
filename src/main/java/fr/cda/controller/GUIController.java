package fr.cda.controller;

import fr.cda.model.*;
import fr.cda.swing.async.SwingAsyncQueue;
import fr.cda.util.LoggerHelper;

import java.util.function.Consumer;

public class GUIController
{
    private AppController appController;
    private SwingAsyncQueue swingAsyncQueue;

    public OperationResult<Void> ConnectToBackApp(BackApp backApp)
    {
        LoggerHelper.log.info("TRY to connect the GUIController to the BackApp");
        swingAsyncQueue = new SwingAsyncQueue("GUIControllerThread");

        if (backApp.HasNoActiveConnection())
            return OperationResult.FAILURE("Connection failed : No active connection available");

        OperationResult<AppController> connectionResult = backApp.RequestNewConnection();
        if (!connectionResult.HasSucceeded())
            return OperationResult.FAILURE(connectionResult.getMessage());

        appController = connectionResult.getData();
        return OperationResult.SUCCESS("Connected to Back App");
    }

    public OperationResult<Void> IsConnected()
    {
        if (appController != null)
            return OperationResult.SUCCESS("App Controller is valid");

        return OperationResult.FAILURE("App Controller is null");
    }

    public void SaveProductToFile(final String filePath, final Consumer<OperationResult<String>> callback)
    {
        //todo : implement
    }

    public void SaveOrderToFile(final String filePath, final Consumer<OperationResult<String>> callback)
    {
        //todo : implement
    }

    public void CreateNewProduct(final Product data, final Consumer<OperationResult<ID>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProduct(data),
                callback);
    }

    public void CreateNewProductCategory(final Category category, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProductCategory(category),
                callback);
    }

    public void ReadProduct(final ID id, final Consumer<OperationResult<Product>> consumer) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadProduct(id),
                consumer);
    }

    public void ReadAllProduct(final Consumer<OperationResult<Product[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllProduct(),
                callback);
    }

    public void UpdateProduct(final Product product, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.UpdateProduct(product),
                callback);
    }

    public void DeleteProduct(final ID id, Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteProduct(id),
                callback);
    }

    public void CreateNewOrder(final Order data, final Consumer<OperationResult<ID>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewOrder(data),
                callback);
    }

    public void ReadOrder(final ID id, final Consumer<OperationResult<Order>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadOrder(id),
                callback);
    }

    public void ReadAllOrder(final Consumer<OperationResult<Order[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllOrder(),
                callback);
    }

    public void UpdateOrder(final Order order, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.UpdateOrder(order),
                callback);
    }

    public void DeleteOrder(final ID id, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteOrder(id),
                callback);
    }

    public void SendDeliveredOrderByMail(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SendDeliveredOrderByMail(),
                callback);
    }

    public void SaveBackViaFTP(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SaveBackViaFTP(),
                callback);
    }

    private void VerifyConnection() throws DatabaseConnectionException
    {
        if (!IsConnected().HasSucceeded())
            throw new DatabaseConnectionException("App Controller is not connected");
    }
}
