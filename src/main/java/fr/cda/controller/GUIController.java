package fr.cda.controller;

import fr.cda.event.EventBus;
import fr.cda.model.*;
import fr.cda.view.swing.async.SwingAsyncQueue;
import fr.cda.util.LoggerHelper;

import java.util.function.Consumer;

public class GUIController
{
    private AppController appController;
    private SwingAsyncQueue swingAsyncQueue;

    public final EventBus eventBus;

    public GUIController()
    {
        swingAsyncQueue = new SwingAsyncQueue("AsyncQueue");
        eventBus = new EventBus();
    }

    /**
     * Action used to get the {@link AppController} owned by the {@link BackApp}
     * @param backApp The {@link BackApp} you want to be connected with
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> ConnectToBackApp(BackApp backApp)
    {
        LoggerHelper.log.info("TRY to connect the GUIController to the BackApp");

        if (backApp.HasAnActiveConnection())
            return OperationResult.FAILURE("Connection failed : No active connection available");

        OperationResult<AppController> connectionResult = backApp.RequestNewConnection();
        if (!connectionResult.HasSucceeded())
            return OperationResult.FAILURE(connectionResult.getMessage());

        appController = connectionResult.getData();
        eventBus.Publish(new DatabaseConnectionEvent("Connected  to backApp", true));

        return OperationResult.SUCCESS("Connected to Back App");
    }

    /**
     * Action used to know if the {@link GUIController} is still linked to a valid {@link AppController}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
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

    public void MakeDeliveries()
    {
        VerifyConnection();

//        swingAsyncQueue.SubmitAsyncOperation(
//                () -> appController.ReadAllOrder(),
//                callback);
    }

    /**
     * Action used to insert a new {@link Product} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param data The {@link Product} you want to insert
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void CreateNewProduct(final Product data, final Consumer<OperationResult<ID>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProduct(data),
                callback);
    }

    /**
     * Action used to insert a new product {@link Category} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param category
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void CreateNewProductCategory(final Category category, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProductCategory(category),
                callback);
    }

    /**
     * Action used to get the data of a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id
     * @param consumer
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadProduct(final ID id, final Consumer<OperationResult<Product>> consumer) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadProduct(id),
                consumer);
    }

    /**
     * Action used to read get the data of all {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadAllProduct(final Consumer<OperationResult<Product[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllProduct(),
                callback);
    }

    /**
     * Action used to update the data of a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param product
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void UpdateProduct(final Product product, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.UpdateProduct(product),
                callback);
    }

    /**
     * Action used to delete a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void DeleteProduct(final ID id, Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteProduct(id),
                callback);
    }

    /**
     * Action used to insert a new {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param data
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void CreateNewOrder(final Order data, final Consumer<OperationResult<ID>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewOrder(data),
                callback);
    }

    /**
     * Action used to get the data of an {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadOrder(final ID id, final Consumer<OperationResult<Order>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadOrder(id),
                callback);
    }

    /**
     * Action used to get the data of all {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadAllOrder(final Consumer<OperationResult<Order[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllOrder(),
                callback);
    }

    /**
     * Action used to update the data of an {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param order
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void UpdateOrder(final Order order, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.UpdateOrder(order),
                callback);
    }

    /**
     * Action used to delete an {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void DeleteOrder(final ID id, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteOrder(id),
                callback);
    }

    /**
     * Action used to send the data of the deliverer order by mail. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void SendDeliveredOrderByMail(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SendDeliveredOrderByMail(),
                callback);
    }

    /**
     * Action used to save the data in the {@link Database} on a server via FTP protocol. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void SaveBackViaFTP(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SaveBackViaFTP(),
                callback);
    }

    /**
     * Internal method used to throw a {@link DatabaseConnectionException} if the internal {@link AppController} reference
     * of the linked {@link Database} is not valid. This method is called before each operation on the linked {@link Database}
     * @throws DatabaseConnectionException An exception throw if the controller detect an issue with the {@link Database} through her {@link AppController}
     */
    private void VerifyConnection() throws DatabaseConnectionException
    {
        if (!IsConnected().HasSucceeded())
            throw new DatabaseConnectionException("App Controller is not connected");
    }
}
