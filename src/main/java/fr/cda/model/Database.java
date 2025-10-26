package fr.cda.model;

import fr.cda.util.LoggerHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent a database in this program, living only in RAM
 */
public class Database
{
    private static final String ERROR_ID_ALREADY_EXISTING = "The ID used is already present in the database";
    private static final String ERROR_UNKNOWN_ID = "The ID used is unknown from the database";
    private static final String ERROR_UNKNOWN_PRODUCT_CATEGORY = "The Category used is unknown from the database";


    private final Map<Category, Map<ID, Product>> productMap;
    private final Map<ID, Order> orderMap;

    private final DataIdGenerator productIDGenerator;
    private final DataIdGenerator orderIDGenerator;

    private final String dataPath;

    /**
     * Creation of a Database
     * @param dataPath Path who will be used to retrieve and save the data
     */
    public Database(final String dataPath)
    {
        LoggerHelper.log.info("START : creation of a database who will use this path to find her data : " + dataPath);

        this.dataPath = dataPath;

        productIDGenerator =  new DataIdGenerator();
        orderIDGenerator = new DataIdGenerator();

        productMap = new HashMap<>();
        orderMap = new HashMap<>();
    }


    public OperationResult<Void> SaveProductToFile(final String productPath)
    {
        //Todo : Implement
        return null;
    }

    public OperationResult<Void> SaveOrderToFile(final String productPath)
    {
        //Todo : Implement
        return null;
    }

    /**
     * Add a new Product to the database
     * @param product a non-complete {@link Product} to add to the database
     * @return An OperationResult telling if the operation was a success
     * and containing the newly generated ID for the product added
     */
    public OperationResult<ID> CreateNewProduct(final Product product)
    {
        final Category CATEGORY = product.getCategory();

        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        final ID newProductID = new ID(CATEGORY.getCategoryName() + "-" + productIDGenerator.GetNextIdAsString());
        if (productMap.get(CATEGORY).containsKey(newProductID))
            return OperationResult.FAILURE(ERROR_ID_ALREADY_EXISTING);


        Product newProduct = new Product(newProductID, product.getName(), CATEGORY, product.getPrice());
        productMap.get(CATEGORY).put(newProductID, newProduct);

        return OperationResult.SUCCESS(newProduct.getId(), "Creation of a new product has been done successfully");
    }

    /**
     * Add a new Category to the internal Hashmap "productMap"
     * @param category The category you want to add
     * @return An OperationResult able to tell you if the category has been added or not via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> CreateNewProductCategory(final Category category)
    {
        OperationResult<Void> checkCategoryResult = CheckIfProductCategoryExist(category);
        if (checkCategoryResult.HasSucceeded())
            return OperationResult.FAILURE("The category is already present in the database : " +  category.getCategoryName());

        productMap.put(category, new HashMap<>());
        return OperationResult.SUCCESS("Creation of a new category has been done successfully : " + category.getCategoryName());
    }

    /**
     * Read the data of a specific Product own internally
     * @param productId The key to find the correct Product
     * @return An OperationResult with the complete Product targeted
     */
    public OperationResult<Product> ReadProduct(final ID productId)
    {
        OperationResult<String[]> splitOperation = SplitProductID(productId);
        if (!splitOperation.HasSucceeded())
            return OperationResult.FAILURE(splitOperation.getMessage());


        final Category CATEGORY = new Category(splitOperation.getData()[0]);
        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        if (!productMap.get(CATEGORY).containsKey(productId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + productId.getId());


        return OperationResult.SUCCESS(productMap.get(CATEGORY).get(productId), "Reading of product ID "
                                                                                   + productId.getId()
                                                                                   + " is successful" );
    }

    /**
     * Used to determine if a category already exist for the productMap
     * @param cat The category you want to test
     * @return An OperationResult able to tell you if the operation was a success via {@link OperationResult#HasSucceeded()}
     */
    private OperationResult<Void> CheckIfProductCategoryExist(final Category cat)
    {
        if (!productMap.containsKey(cat))
            return OperationResult.FAILURE(ERROR_UNKNOWN_PRODUCT_CATEGORY + " : " + cat.getCategoryName());

        return OperationResult.SUCCESS("Category exist in database");
    }

    /**
     * Used to split an id From a product in his two parts : CategoryName and a Number
     * @param productId The id you want to split
     * @return An OperationResult containing, in case of success, a String[] with 2 elements : CategoryName and then a number
     */
    private OperationResult<String[]> SplitProductID(final ID productId)
    {
        //A product id follow this format : "CATEGORY-NUMBER"
        String[] idSplit = productId.getId().split("-", 2);

        if (idSplit.length != 2)
        {
            final String SPLIT_FAIL = "The ID of the product seems to be malformed. " +
                                      "Impossible to extract a category and a number from it. " +
                                      "ID value : " + productId.getId();
            return OperationResult.FAILURE(SPLIT_FAIL);
        }

        return OperationResult.SUCCESS(idSplit, "Product ID was correctly split");
    }

    private void Init()
    {

    }
}
