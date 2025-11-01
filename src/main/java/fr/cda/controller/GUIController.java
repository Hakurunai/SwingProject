package fr.cda.controller;

import fr.cda.event.EventBus;
import fr.cda.model.*;
import fr.cda.view.swing.async.SwingAsyncQueue;
import fr.cda.util.LoggerHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class GUIController
{
    private AppController appController;
    private final SwingAsyncQueue swingAsyncQueue;

    public final EventBus eventBus;

    //region CTOR
    public GUIController()
    {
        swingAsyncQueue = new SwingAsyncQueue("AsyncQueue");
        eventBus = new EventBus();
    }
    //endregion CTOR

    //region PUBLIC_METHODS

    //region CRUD_OPERATION

    //region Product_Crud
    /**
     * Action used to insert a new {@link Product} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param data The {@link Product} you want to insert generated via {@link Product#GenerateProductDTO(String, Category, float, int)}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void CreateNewProduct(final Product data, final Consumer<OperationResult<ID>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProduct(data),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to insert a new product {@link Category} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param category The {@link Category} you want to insert
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void CreateNewProductCategory(final Category category, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.CreateNewProductCategory(category),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to get the data of a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id The {@link ID} of the targeted {@link Product}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadProduct(final ID id, final Consumer<OperationResult<Product>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadProduct(id),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to read get the data of all {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadAllProduct(final Consumer<OperationResult<Product[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllProduct(),
                DatabaseOperationCallbackWrapper(callback));
    }


    /**
     * Retrieve all the categories already inserted in the database
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadAllProductCategory(final Consumer<OperationResult<Category[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllProductCategories(),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to update the data of a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param product A {@link Product} containing all the updated data AND the corresponding {@link ID}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void UpdateProduct(final Product product, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.UpdateProduct(product),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to delete a {@link Product} from the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param id The {@link ID} of the targeted {@link Product}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void DeleteProduct(final ID id, Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteProduct(id),
                DatabaseOperationCallbackWrapper(callback));
    }
    //endregion Product_Crud

    //region Order_Crud
    /**
     * Action used to insert a new {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param data The {@link Order} you want to insert generated via {@link Order#GenerateOrderDTO(String, LocalDate, List)}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
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
     * @param id The {@link ID} of the targeted {@link Order}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
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
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void ReadAllOrder(final Consumer<OperationResult<Order[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.ReadAllOrder(),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to update the data of an {@link Order} in the {@link Database}. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param order A {@link Order} containing all the updated data AND the corresponding {@link ID}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
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
     * @param id The {@link ID} of the targeted {@link Order}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void DeleteOrder(final ID id, final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.DeleteOrder(id),
                callback);
    }
    //endregion Order_Crud

    //endregion CRUD_OPERATION

    public void QueryProfitMade(final Consumer<OperationResult<String>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.GenerateProfitFile(),
                callback);
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


    /**
     * Method to call to ask the linked {@link Database} to perform {@link Database#MakeAllDeliveries()} through a Chain of Responsibility
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void MakeAllDeliveries(final Consumer<OperationResult<Order[]>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.MakeAllDeliveries(),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to send the data of the deliverer order by mail. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void SendDeliveredOrderByMail(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SendDeliveredOrderByMail(),
                DatabaseOperationCallbackWrapper(callback));
    }

    /**
     * Action used to save the data in the {@link Database} on a server via FTP protocol. A callback is then used on the {@link OperationResult}
     * returned by the {@link Database}
     * @param callback A {@link java.util.function.Consumer} used with the result returned by the {@link Database}
     * @throws DatabaseConnectionException Throw in case of an issue with the reference of the {@link AppController}, cf {@link GUIController#VerifyConnection()}
     */
    public void SaveBackViaFTP(final Consumer<OperationResult<Void>> callback) throws DatabaseConnectionException
    {
        VerifyConnection();

        swingAsyncQueue.SubmitAsyncOperation(
                () -> appController.SaveBackViaFTP(),
                DatabaseOperationCallbackWrapper(callback));
    }
    //endregion PUBLIC_METHODS

    //region PRIVATE_METHODS
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

    /**
     * This method decorate the original callback passed in parameter to publish an event with the message of the {@link OperationResult}
     * @param callBack The callback you want to decorate with
     * @return A wrapped callback doing an extra step : Publishing a {@link DatabaseOperationEndedEvent} on the {{@link #eventBus}}
     * @param <T> The data possibly contained in the {@link OperationResult}
     */
    private <T> Consumer<OperationResult<T>> DatabaseOperationCallbackWrapper(final Consumer<OperationResult<T>> callBack)
    {
        return result ->
        {
            eventBus.Publish(new DatabaseOperationEndedEvent(result.getMessage()));
            callBack.accept(result);
        };
    }
    //endregion PRIVATE_METHODS
}
