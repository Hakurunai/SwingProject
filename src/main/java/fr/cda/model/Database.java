package fr.cda.model;

import fr.cda.util.LoggerHelper;

import java.time.LocalDate;
import java.util.*;

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
     * Add a new {@link  Product} to the database
     * @param product a non-complete {@link Product} created via
     * {@link Product#GenerateProductDTO(String, Category, float)} to add to the database
     * @return An OperationResult telling if the operation was a success
     * and containing the newly generated ID for the product added
     */
    public OperationResult<ID> CreateNewProduct(final Product product)
    {
        final Category CATEGORY = product.getCategory();

        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        final ID newProductID = new ID(CATEGORY.categoryName() + "-" + productIDGenerator.GetNextIdAsString());
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
            return OperationResult.FAILURE("The category is already present in the database : " +  category.categoryName());

        productMap.put(category, new LinkedHashMap<>());
        return OperationResult.SUCCESS("Creation of a new category has been done successfully : " + category.categoryName());
    }

    /**
     * Read the data of a specific Product own internally
     * @param productId The key to find the correct Product
     * @return An OperationResult with the complete Product targeted
     */
    public OperationResult<Product> ReadProduct(final ID productId)
    {
        final OperationResult<String[]> SPLIT_OPERATION = SplitProductID(productId);
        if (!SPLIT_OPERATION.HasSucceeded())
            return OperationResult.FAILURE(SPLIT_OPERATION.getMessage());


        final Category CATEGORY = new Category(SPLIT_OPERATION.getData()[0]);
        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        if (!productMap.get(CATEGORY).containsKey(productId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + productId.id());

        return OperationResult.SUCCESS(new Product(productMap.get(CATEGORY).get(productId)),
                                                        "Found Product in database");
    }

    /**
     *
     * @return all Product from the database, sorted by category
     */
    public OperationResult<Product[]> ReadAllProduct()
    {
        List<Product> allProducts = new ArrayList<>();

        for (Map<ID, Product> map : productMap.values())
        {
            for (Product product : map.values())
            {
                allProducts.add(new Product(product)); //Deep copy
            }
        }

        return OperationResult.SUCCESS(allProducts.toArray(Product[]::new),
                "SUCCESS : read all Product from databases");
    }

    /**
     * Allow the possibility to update an internal Product
     * @param product The product you want to update. His ID will be used to find the internal one,
     *                his data will then be copied
     * @return An OperationResult able to tell you if the update was successful via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> UpdateProduct(final Product product)
    {
        OperationResult<Product> checkIfProductExist = ReadProduct(product.getId());
        if (!checkIfProductExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfProductExist.getMessage());

        Product internalCopy = new Product(product);
        productMap.get(product.getCategory()).put(product.getId(), internalCopy);
        return OperationResult.SUCCESS("SUCCESS : update of a Product. ID : " + product.getId());
    }

    /**
     * Delete a Product from the Database
     * @param id The id of the targeted Product
     * @return An OperationResult able to tell you if the deletion was a success using {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteProduct(final ID id)
    {
        OperationResult<Product> checkIfProductExist = ReadProduct(id);
        if (!checkIfProductExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfProductExist.getMessage());

        Product copy = checkIfProductExist.getData();
        productMap.get(copy.getCategory()).remove(copy.getId());
        return OperationResult.SUCCESS("SUCCESS : deletion of a Product. ID : " + copy.getId());
    }

    /**
     * Add a new {@link  Order} in the Database
     * @param order A non complete {@link Order} created via {@link Order#GenerateOrderDTO(String, List)}
     * @return An {@link OperationResult} containing in case of success the generated ID for the Order inserted
     */
    public OperationResult<ID> CreateNewOrder(final Order order)
    {
        final ID newProductID = new ID(orderIDGenerator.GetNextIdAsString());

        var internalData = ReadOrder(newProductID);
        if (internalData.HasSucceeded())
            return OperationResult.FAILURE("New generated ID is already in Database : " + newProductID.id());

        Order newOrder = new Order(newProductID, LocalDate.now(), order.getClientName(), order.getOrderedProduct());
        orderMap.put(newProductID, newOrder);

        return OperationResult.SUCCESS(newOrder.getId(), "Creation of a new order has been done successfully");
    }

    /**
     * Read the data of an internal {@link Order}
     * @param orderId The {@link ID} of the targeted Order
     * @return An {@link OperationResult} containing in case of success a copy of the internal {@link Order}
     */
    public OperationResult<Order> ReadOrder(final ID orderId)
    {
        if (!orderMap.containsKey(orderId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + orderId.id());

        return OperationResult.SUCCESS(new Order(orderMap.get(orderId)), "Found order in Database");
    }

    /**
     *
     * @return all the {@link Order} from the {@link Database}
     */
    public OperationResult<Order[]> ReadAllOrder()
    {
        List<Order> allOrders = new ArrayList<>();

        for(Order order : orderMap.values())
        {
            allOrders.add(new Order(order));
        }
        return OperationResult.SUCCESS(allOrders.toArray(Order[]::new), "SUCCESS : read all Order from databases");
    }

    /**
     * Allow to update values in an internal Order
     * @param order An {@link Order} containing the new values AND the targeted {@link ID}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> UpdateOrder(final Order order)
    {
        OperationResult<Order> checkIfOrderExist = ReadOrder(order.getId());
        if (!checkIfOrderExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfOrderExist.getMessage());

        Order internalCopy = new Order(order);
        orderMap.put(order.getId(), internalCopy);
        return OperationResult.SUCCESS("SUCCESS : update of an Order. ID : " + order.getId());
    }

    /**
     * This method allow you to remove an Order from the {@link Database}
     * @param orderId The {@link ID} of the targeted {@link Order}
     * @return An {@link OperationResult} able to tell you if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteOrder(final ID orderId)
    {
        OperationResult<Order> checkIfOrderExist = ReadOrder(orderId);
        if (!checkIfOrderExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfOrderExist.getMessage());

        Order copy = checkIfOrderExist.getData();
        orderMap.remove(copy.getId());
        return OperationResult.SUCCESS("SUCCESS : deletion of a Product. ID : " + orderId.id());
    }


    /**
     * Used to determine if a category already exist for the productMap
     * @param category The category you want to test
     * @return An OperationResult able to tell you if the operation was a success via {@link OperationResult#HasSucceeded()}
     */
    private OperationResult<Void> CheckIfProductCategoryExist(final Category category)
    {
        if (!productMap.containsKey(category))
            return OperationResult.FAILURE(ERROR_UNKNOWN_PRODUCT_CATEGORY + " : " + category.categoryName());

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
        String[] idSplit = productId.id().split("-", 2);

        if (idSplit.length != 2)
        {
            final String SPLIT_FAIL = "The ID of the product seems to be malformed. " +
                                      "Impossible to extract a category and a number from it. " +
                                      "ID value : " + productId.id();
            return OperationResult.FAILURE(SPLIT_FAIL);
        }

        return OperationResult.SUCCESS(idSplit, "Product ID was correctly split");
    }

    private void Init()
    {

    }
}
