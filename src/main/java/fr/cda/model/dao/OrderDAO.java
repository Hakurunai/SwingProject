package fr.cda.model.dao;

import fr.cda.model.Database;
import fr.cda.model.ID;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;


/**
 * Specialisation of the DAO for handling {@link Order} object
 */
public class OrderDAO extends DAO<Order>
{
    public OrderDAO(Database database)
    {
        super(database);
    }

    /**
     * Specialisation to create a new {@link Order}
     * @param data The data you tried to insert in the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, the {@link ID} generated during the insertion
     */
    @Override
    public OperationResult<ID> CreateNewData(Order data)
    {
        return database.CreateNewOrder(data);
    }

    /**
     * Specialisation to read the data of an {@link Order}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, a copy of the {@link Order} targeted
     */
    @Override
    public OperationResult<Order> ReadData(ID id)
    {
        return database.ReadOrder(id);
    }

    /**
     * Specialisation to retrieve all the {@link Order} from the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, an array of copy of the {@link Order}
     */
    @Override
    public OperationResult<Order[]> ReadAllData()
    {
        return database.ReadAllOrder();
    }

    /**
     * Specialisation to update an {@link Order}
     * @param data An {@link Order} containing the updated values AND the {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    @Override
    public OperationResult<Void> UpdateData(Order data)
    {
        return database.UpdateOrder(data);
    }

    /**
     * Specialisation to remove an {@link Order} from the {@link Database}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    @Override
    public OperationResult<Void> DeleteData(ID id)
    {
        return database.DeleteOrder(id);
    }

    @Override
    public OperationResult<Void> SaveDataToFile(String pathFile)
    {
        return database.SaveOrderToFile(pathFile);
    }
}
