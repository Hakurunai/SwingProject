package fr.cda.model.dao;

import fr.cda.model.*;

/**
 * Specialisation of the DAO for handling {@link Product} object
 */
public class ProductDAO extends DAO<Product>
{
    public ProductDAO(final Database database)
    {
        super(database);
    }

    /**
     * Specialisation to create a new {@link Product}
     * @param data The data you tried to insert in the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, the {@link ID} generated during the insertion
     */
    @Override
    public OperationResult<ID> CreateNewData(final Product data)
    {
        return database.CreateNewProduct(data);
    }

    /**
     * Specialisation to read the data of a {@link Product}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, a copy of the {@link Product} targeted
     */
    @Override
    public OperationResult<Product> ReadData(final ID id)
    {
        return database.ReadProduct(id);
    }

    /**
     * Specialisation to retrieve all the {@link Product} from the {@link Database}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, an array of copy of the {@link Product}
     */
    @Override
    public OperationResult<Product[]> ReadAllData()
    {
        return database.ReadAllProduct();
    }

    /**
     * Retrieve all the categories already inserted in the database
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, an array of {@link Category} corresponding to all the categories in the database
     */
    public OperationResult<Category[]> ReadAllProductCategories()
    {
        return database.ReadAllProductCategories();
    }

    /**
     * Specialisation to update a {@link Product}
     * @param data A {@link Product} containing the updated values AND the {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    @Override
    public OperationResult<Void> UpdateData(final Product data)
    {
        return database.UpdateProduct(data);
    }

    /**
     * Specialisation to remove a {@link Product} from the {@link Database}
     * @param id The {@link ID} of the targeted object
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    @Override
    public OperationResult<Void> DeleteData(final ID id)
    {
        return database.DeleteProduct(id);
    }


    /**
     * Use to create a new {@link Category} of {@link Product} in the linked {@link Database}
     * @param category The {@link Category} you want to add
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> CreateNewProductCategory(final Category category)
    {
        return database.CreateNewProductCategory(category);
    }
}
