package fr.cda.controller;

import fr.cda.model.*;
import fr.cda.model.dao.OrderDAO;
import fr.cda.model.dao.ProductDAO;

import java.util.List;


public class AppController
{
    private ProductDAO productDAO;
    private OrderDAO orderDAO;

    public AppController(Database database)
    {
        productDAO = new ProductDAO(database);
        orderDAO = new OrderDAO(database);
    }

    public OperationResult<String> SaveProductToFile(final String filePath)
    {
        //todo: implement
        return null;
    }

    public OperationResult<String> SaveOrderToFile(final String filePath)
    {
        //todo: implement
        return null;
    }

    /**
     * Action to call if you want to create a new {@link Product} in the {@link Database}
     * @param data The new {@link Product} to insert generated via {@link Product#GenerateProductDTO(String, Category, float, int)}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link ID} generated during the insertion for this object
     */
    public OperationResult<ID> CreateNewProduct(final Product data)
    {
        return productDAO.CreateNewData(data);
    }

    /**
     * Action to call if you want to create a new {@link Category} for the {@link Product} in the {@link Database}
     * @param category The {@link Category} you want to insert
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> CreateNewProductCategory(final Category category)
    {
        return productDAO.CreateNewProductCategory(category);
    }

    /**
     * Action to call if you want to get the data of a {@link Product} from the {@link Database}
     * @param id The {@link ID} of the targeted {@link Product}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Product> ReadProduct(final ID id)
    {
        return productDAO.ReadData(id);
    }

    /**
     * Action to call if you want to get the data of all {@link Product} from the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing an array of copied {@link Product}
     */
    public OperationResult<Product[]> ReadAllProduct()
    {
        return productDAO.ReadAllData();
    }

    /**
     * Retrieve all the categories already inserted in the database
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, an array of {@link Category} corresponding to all the categories in the database
     */
    public OperationResult<Category[]> ReadAllProductCategories()
    {
        return productDAO.ReadAllProductCategories();
    }

    /**
     * Action to call if you want to update the data of a {@link Product} in the {@link Database}
     * @param product A {@link Product} containing the updated data AND a valid {@link ID}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> UpdateProduct(final Product product)
    {
        return productDAO.UpdateData(product);
    }

    /**
     * Action to call if you want to delete a {@link Product} in the {@link Database}
     * @param id The {@link ID} of the targeted {@link Product}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteProduct(final ID id)
    {
        return productDAO.DeleteData(id);
    }

    /**
     * Action to call if you want to create a new {@link Order} in the {@link Database}
     * @param data The new {@link Order} to insert generated via {@link Order#GenerateOrderDTO(String, List)}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link ID} generated during the insertion for this object
     */
    public OperationResult<ID> CreateNewOrder(final Order data)
    {
        return orderDAO.CreateNewData(data);
    }

    /**
     * Action to call if you want to get the data of a {@link Order} from the {@link Database}
     * @param id The {@link ID} of the targeted {@link Order}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Order> ReadOrder(final ID id)
    {
        return orderDAO.ReadData(id);
    }

    /**
     * Action to call if you want to get the data of all {@link Order} from the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing an array of copied {@link Order}
     */
    public OperationResult<Order[]> ReadAllOrder()
    {
        return orderDAO.ReadAllData();
    }

    /**
     * Action to call if you want to update the data of an {@link Order} in the {@link Database}
     * @param order An {@link Order} containing the updated data AND a valid {@link ID}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> UpdateOrder(final Order order)
    {
        return orderDAO.UpdateData(order);
    }

    /**
     * Action to call if you want to delete an {@link Order} in the {@link Database}
     * @param id The {@link ID} of the targeted {@link Order}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteOrder(final ID id)
    {
        return orderDAO.DeleteData(id);
    }

    /**
     * Used to call {@link OrderDAO#MakeAllDeliveries()} on the linked {@link OrderDAO}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the array of {@link Order} that we cannot fulfill, with an explanation set on each one of them
     */
    public OperationResult<Order[]> MakeAllDeliveries()
    {
        return orderDAO.MakeAllDeliveries();
    }

    public OperationResult<Void> SendDeliveredOrderByMail()
    {
        //todo : implement
        return null;
    }

    public OperationResult<Void> SaveBackViaFTP()
    {
        //todo : implement
        return null;
    }
}
