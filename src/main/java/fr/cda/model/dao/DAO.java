package fr.cda.model.dao;

import fr.cda.model.Database;
import fr.cda.model.ID;
import fr.cda.model.OperationResult;

/**
 * A DAO is the only type able to interact with the {@link Database}
 * @param <T> Specific type for a proper specialisation of this class
 */
public abstract class DAO<T>
{
    protected final Database database;

    /**
     * Creation of a DAO linked to a specific {@link Database}
     * @param database The {@link Database} linked to this DAO
     */
    public DAO(Database database)
    {
        this.database = database;
    }

    /**
     * Action to insert a data in the {@link Database}
     * @param data The data to insert
     * @return An {@link OperationResult} able to tell if the Operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link ID} generated during the insertion
     */
    public abstract OperationResult<ID> CreateNewData(final T data);

    /**
     * Action to read a data from the linked {@link Database}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the Operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the data you tried to access
     */
    public abstract OperationResult<T> ReadData(final ID id);

    /**
     * Action to retrieve all the data of a specific type from the {@link Database}
     * @return An {@link OperationResult} able to tell if the Operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the data you tried to access
     */
    public abstract OperationResult<T[]> ReadAllData();

    /**
     * Action to update a data in the {@link Database}
     * @param data The data containing the updated value AND the {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the Operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public abstract OperationResult<Void> UpdateData(final T data);

    /**
     * Action to delete a data in the {@link Database}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the Operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public abstract OperationResult<Void> DeleteData(final ID id);


    public abstract OperationResult<Void> SaveDataToFile(final String pathFile);
}
