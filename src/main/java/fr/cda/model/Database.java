package fr.cda.model;

import fr.cda.Config;
import fr.cda.util.LoggerHelper;
import fr.cda.util.SerializerHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represent a database in this program, living only in RAM, and containing all the data for our {@link Product} and {@link Order}
 * The data came from read CSV files
 */
public class Database
{
    private static final String ERROR_UNKNOWN_ID = "ERROR : The ID used is unknown from the database";


    private final Map<Category, ProductCategoryMap> productMap;
    private final Map<ID, Order> orderMap;

    private final DataIdGenerator orderIDGenerator;

    //region Ctor
    /**
     * Creation of a Database
     */
    public Database()
    {
        LoggerHelper.log.info("START : creation of a database");

        orderIDGenerator = new DataIdGenerator();

        productMap = new HashMap<>();
        orderMap = new HashMap<>();

        Init();
    }
    //endregion Ctor

    //region PUBLIC_METHODS

    //region CRUD OPERATION

    //region Product_Crud

    /**
     * Add a new {@link  Product} to the database
     * @param product a non-complete {@link Product} created via
     * {@link Product#GenerateProductDTO(String, Category, float, int)} to add to the database
     * @return An OperationResult telling if the operation was a success
     * and containing the newly generated ID for the product added
     */
    public OperationResult<ID> CreateNewProduct(final Product product)
    {
        final Category CATEGORY = product.getCategory();

        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        final ID newProductID = new ID(CATEGORY.categoryName() + "-" + productMap.get(CATEGORY).GetNextCategoryIdAsString());
        if (productMap.get(CATEGORY).getProductMap().containsKey(newProductID))
            return OperationResult.FAILURE("ERROR : The ID used is already present in the database. ID : " + newProductID.id());


        Product newProduct = new Product(newProductID, product.getName(), CATEGORY, product.getPrice(), product.getStoredQuantity());
        productMap.get(CATEGORY).getProductMap().put(newProductID, newProduct);

        return OperationResult.SUCCESS(newProduct.getId(), "SUCCESS : Creation of a new product has been done successfully. ID : "
                                                                    + newProductID.id());
    }

    /**
     * Add a new Category to the internal Hashmap "productMap"
     * @param category The category you want to add
     * @return An OperationResult able to tell you if the category has been added or not via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> CreateNewProductCategory(final Category category)
    {
        Category internalCat = new Category(category.categoryName().toUpperCase());

        OperationResult<Void> checkCategoryResult = CheckIfProductCategoryExist(internalCat);
        if (checkCategoryResult.HasSucceeded())
            return OperationResult.FAILURE("ERROR : The category is already present in the database : " + internalCat.categoryName());

        productMap.put(internalCat, new ProductCategoryMap());
        return OperationResult.SUCCESS("SUCCESS : Creation of a new category has been done successfully : " + internalCat.categoryName());
    }


    /**
     * Read the data of a specific {@link Product} own internally and return a COPY of it
     * @param productId The {@link ID} to find the correct {@link Product}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     *      * and, in case of success, containing a COPY of the {@link Product} targeted
     */
    public OperationResult<Product> ReadProduct(final ID productId)
    {
        final var INTERNAL_READ_OPERATION = ReadProductInternal(productId);

        //Return a copy of the reference
        if (INTERNAL_READ_OPERATION.HasSucceeded())
            return OperationResult.SUCCESS(new Product(INTERNAL_READ_OPERATION.getData()), INTERNAL_READ_OPERATION.getMessage());

        //No risk, INTERNAL_READ_OPERATION.getData() already return null
        else
            return INTERNAL_READ_OPERATION;
    }

    /**
     *
     * @return all {@link Product} from the database, sorted by {@link Category}
     */
    public OperationResult<Product[]> ReadAllProduct()
    {
        List<Product> allProducts = new ArrayList<>();

        for (ProductCategoryMap map : productMap.values())
        {
            for (Product product : map.getProductMap().values())
            {
                allProducts.add(new Product(product)); //Deep copy
            }
        }

        return OperationResult.SUCCESS(allProducts.toArray(Product[]::new),
                "SUCCESS : Read all Product from database");
    }

    /**
     * Retrieve all the categories already inserted in the database
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, an array of {@link Category} corresponding to all the categories in the database
     */
    public OperationResult<Category[]> ReadAllProductCategories()
    {
        return OperationResult.SUCCESS(productMap.keySet().toArray(Category[]::new), "SUCCESS : Read all Categories from database");
    }

    /**
     * Allow the possibility to update an internal {@link Product}
     * @param updatedProduct The product you want to update. His ID will be used to find the internal one,
     *                his data will then be copied
     * @return An {@link OperationResult} able to tell you if the update was successful via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> UpdateProduct(final Product updatedProduct)
    {
        OperationResult<Product> checkIfProductExist = ReadProduct(updatedProduct.getId());
        if (!checkIfProductExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfProductExist.getMessage());

        Product internalProduct = checkIfProductExist.getData();
        System.out.println("The two categories are the same ? "
        + (internalProduct.getCategory() == updatedProduct.getCategory()));

        //Category is the same, just need to update the data
        if (internalProduct.getCategory().equals(updatedProduct.getCategory()))
        {
            productMap.get(updatedProduct.getCategory()).getProductMap().put(updatedProduct.getId(), new Product(updatedProduct));
            return OperationResult.SUCCESS("SUCCESS : Update of a Product. ID : " + updatedProduct.getId().id());
        }
        //Category different : need to put the object on his new productMap and remove it from the previous one, while updating
        //his ID
        else
        {
            //Create a new Product on his new Category, generate a new internal copy with a new ID
            final OperationResult<ID> creationResult = CreateNewProduct(updatedProduct);
            if (creationResult.HasSucceeded())
            {
                //Remove the data from the previous productMap only in case of success
                DeleteProduct(internalProduct.getId());
                return OperationResult.SUCCESS("SUCCESS : " + updatedProduct.getId().id() + " was correctly updated to " + creationResult.getData().id());
            }
            else
            {
                return OperationResult.FAILURE(creationResult.getMessage());
            }
        }
    }

    /**
     * Delete a {@link Product} from the Database
     * @param id The id of the targeted Product
     * @return An {@link OperationResult} able to tell you if the deletion was a success using {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteProduct(final ID id)
    {
        OperationResult<Product> checkIfProductExist = ReadProduct(id);
        if (!checkIfProductExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfProductExist.getMessage());

        Product copy = checkIfProductExist.getData();
        productMap.get(copy.getCategory()).getProductMap().remove(copy.getId());
        return OperationResult.SUCCESS("SUCCESS : Deletion of a Product. ID : " + copy.getId().id());
    }
    //endregion Product_Crud

    //region Order_Crud
    /**
     * Add a new {@link  Order} in the Database
     * @param order A non complete {@link Order} created via {@link Order#GenerateOrderDTO(String, LocalDate, List)}
     * @return An {@link OperationResult} containing in case of success the generated ID for the Order inserted
     */
    public OperationResult<ID> CreateNewOrder(final Order order)
    {
        final ID newProductID = new ID(orderIDGenerator.GetNextIdAsString());

        var internalData = ReadOrderInternal(newProductID);
        if (internalData.HasSucceeded())
            return OperationResult.FAILURE("ERROR : New generated ID is already in Database : " + newProductID.id());

        Order newOrder = new Order(newProductID, order.getCreationDate(), order.getClientName(), order.getOrderedProduct());
        orderMap.put(newProductID, newOrder);

        return OperationResult.SUCCESS(newOrder.getId(), "SUCCESS : Creation of a new order has been done successfully, ID : "
                                                            +  newProductID.id());
    }

    /**
     * Read the data of an internal {@link Order} adn return a COPY of it
     * @param orderId The {@link ID} of the targeted Order
     * @return An {@link OperationResult} able to tell you if the deletion was a success using {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, a COPY of the internal {@link Order}
     */
    public OperationResult<Order> ReadOrder(final ID orderId)
    {
        var INTERNAL_READ_OPERATION = ReadOrderInternal(orderId);

        //Return a copy of the reference
        if (INTERNAL_READ_OPERATION.HasSucceeded())
            return OperationResult.SUCCESS(new Order(INTERNAL_READ_OPERATION.getData()), INTERNAL_READ_OPERATION.getMessage());

        //No risk, INTERNAL_READ_OPERATION.getData() already return null
        else
            return INTERNAL_READ_OPERATION;
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
        return OperationResult.SUCCESS(allOrders.toArray(Order[]::new), "SUCCESS : Read all Order from databases");
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
        return OperationResult.SUCCESS("SUCCESS : Update of an Order completed. ID : " + order.getId().id());
    }

    /**
     * This method allow you to remove an Order from the {@link Database}
     * @param orderId The {@link ID} of the targeted {@link Order}
     * @return An {@link OperationResult} able to tell you if the operation succeeded via {@link OperationResult#HasSucceeded()}
     */
    public OperationResult<Void> DeleteOrder(final ID orderId)
    {
        OperationResult<Order> checkIfOrderExist = ReadOrderInternal(orderId);
        if (!checkIfOrderExist.HasSucceeded())
            return OperationResult.FAILURE(checkIfOrderExist.getMessage());

        Order internalOrder = checkIfOrderExist.getData();
        orderMap.remove(internalOrder.getId());
        return OperationResult.SUCCESS("SUCCESS : Deletion of a Product. ID : " + orderId.id());
    }
    //endregion Order_Crud
    //endregion CRUD_OPERATION

    /**
     * Operation to call to "delivered" the {@link Order} based on the status of the internal storage
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the array of {@link Order} that we cannot fulfill, with an explanation set on each one of them
     */
    public OperationResult<Order[]> MakeAllDeliveries()
    {
        final List<Order> internalOrders = orderMap.values().stream().filter(order -> !order.isDelivered()).toList();
        List<Order> impossibleOrderToFulfill = new ArrayList<>();

        for (Order order : internalOrders)
        {
            if (CheckIfOrderIsDoable(order))
                FulfillOrder(order);
            else
                impossibleOrderToFulfill.add(order);
        }

        for (Order order : impossibleOrderToFulfill)
        {
            SetNonDoableOrderReason(order);
        }
        return OperationResult.SUCCESS(impossibleOrderToFulfill.toArray(Order[]::new),
                "SUCCESS : All possible deliveries has been made. Here are the one impossible to fulfill right now");
    }
    //endregion PUBLIC_METHODS

    //region PRIVATE_METHODS

    //region INIT
    /**
     * Use to order the different initialization call needed by the {@link Database}
     */
    private void Init()
    {
        LoggerHelper.log.info("START : initialization of the Database");
        InitProduct();
        InitOrder();
    }

    /**
     * Used internally to read all Orders from the configuration file, called after {@link #InitProduct()}
     */
    private void InitOrder()
    {
        LoggerHelper.log.info("TRY : initialization of Order in the database");
        try
        {
            final String[] DATA = SerializerHelper.ReadFile(Config.DB_ORDER_FILE_PATH);
            for (String line : DATA)
            {
                ExtractFromFileAndInsertOrder(line);
            }
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("FAIL : {}",  e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Used in {@link #InitOrder()} to create the Orders from the read CSV file
     * @param line The content of the {@link Order} CSV file
     */
    private void ExtractFromFileAndInsertOrder(final String line)
    {
        //Todo : check if content is ill-formed

        //FORMAT : ID ; CreationDate ; ClientName ; ProductRef (ID=Quantity)...
        //1;21/09/2022;Macron Brigitte;LIVRE-1=1;LIVRE-2=2;...

        final int ORDER_CONTENT_INDEX_START_POSITION = 3;

        String[] dataContent = line.split(";");
        ID orderID = new ID(dataContent[0]);

        var readRes = ReadOrderInternal(orderID);
        if (readRes.HasSucceeded())
        {
            LoggerHelper.log.warn("CHECK : You tried to initialize an already existing Order in the database" + orderID.id());
            return;
        }

        ArrayList<String> orderContentFromFile = new ArrayList<>();
        for (int i = ORDER_CONTENT_INDEX_START_POSITION ; i < dataContent.length; ++i)
        {
            orderContentFromFile.add(dataContent[i]);
        }
        List<OrderDetail> orderDetails = ExtractOrderDetails(orderContentFromFile, orderID);

        Order newOrder = new Order(orderID, LocalDate.parse(dataContent[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dataContent[2], orderDetails);
        orderMap.put(orderID, newOrder);
    }

    /**
     * Internally used to Read all the {@link Product} from the configuration file
     */
    private void InitProduct()
    {
        LoggerHelper.log.info("TRY : initialization of Product in the database");
        try
        {
            final String[] DATA = SerializerHelper.ReadFile(Config.DB_PRODUCT_FILE_PATH);

            for (String line : DATA)
            {
                ExtractFromFileAndInsertProduct(line);
            }
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("FAIL : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Used in {@link #InitProduct()} to create the Products from the read CSV file
     * @param line The content of the {@link Product} CSV file
     */
    private void ExtractFromFileAndInsertProduct(final String line)
    {
        //Todo : check if content is ill-formed

        //FORMAT : ID (Category-Number) ; Name ; Price ; Quantity
        //LIVRE-1;Les Miserables de Victor Hugo ;8.50;6
        String[] dataContent = line.split(";");
        final ID readedID = new ID(dataContent[0]);

        OperationResult<Category> extractedCategoryOperation = ExtractProductCategoryFromID(readedID);
        if (!extractedCategoryOperation.HasSucceeded())
        {
            LoggerHelper.log.warn("CHECK : {}", extractedCategoryOperation.getMessage());
            return;
        }
        final Category category = extractedCategoryOperation.getData();

        var res = CheckIfProductCategoryExist(category);
        if (!res.HasSucceeded())
        {
            CreateNewProductCategory(category);
        }
        else
        {
            var resProd = ReadProductInternal(readedID);
            if (resProd.HasSucceeded())
            {
                LoggerHelper.log.warn("CHECK : You tried to initialize an already existing Product in the database. ID : "
                                              + resProd.getData().getId());
                return;
            }
        }

        UpdateProductIdGenerator(readedID, category);
        Product newProduct = new Product(readedID, dataContent[1], category,
                Float.parseFloat(dataContent[2]),
                Integer.parseInt(dataContent[3]));

        productMap.get(category).getProductMap().put(newProduct.getId(), newProduct);
    }
    //endregion INIT

    //region Product_Methods
    /**
     * Read the data of a specific {@link Product} own internally and return a REFERENCE to it
     * @param productId The {@link ID} to find the correct {@link Product}
     * @return An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing a REFERENCE of the {@link Product} targeted
     */
    private OperationResult<Product> ReadProductInternal(final ID productId)
    {
        final var CATEGORY_OPERATION = ExtractProductCategoryFromID(productId);
        if (!CATEGORY_OPERATION.HasSucceeded())
            return OperationResult.FAILURE(CATEGORY_OPERATION.getMessage());


        final Category CATEGORY = CATEGORY_OPERATION.getData();
        final OperationResult<Void> CHECK_CATEGORY_RESULT = CheckIfProductCategoryExist(CATEGORY);
        if (!CHECK_CATEGORY_RESULT.HasSucceeded())
            return OperationResult.FAILURE(CHECK_CATEGORY_RESULT.getMessage());


        if (!productMap.get(CATEGORY).getProductMap().containsKey(productId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + productId.id());


        return OperationResult.SUCCESS(productMap.get(CATEGORY).getProductMap().get(productId),
                "SUCCESS : Product found in database. ID : " + productId.id());
    }


    /**
     * Used to split an id From a product in his two parts : CategoryName and a Number
     * @param productId The id you want to split
     * @return An {@link OperationResult} containing, in case of success, a String[] with 2 elements : CategoryName and then a number
     */
    private OperationResult<String[]> SplitProductID(final ID productId)
    {
        //A product id follow this format : "CATEGORY-NUMBER"
        String[] idSplit = productId.id().split("-", 2);

        if (idSplit.length != 2)
        {
            final String SPLIT_FAIL = "ERROR : The ID of the product seems to be malformed. " +
                                              "Impossible to extract a category and a number from it. " +
                                              "ID value : " + productId.id();
            return OperationResult.FAILURE(SPLIT_FAIL);
        }

        return OperationResult.SUCCESS(idSplit, "SUCCESS : Product ID was correctly split. ID : " + productId.id());
    }

    /**
     * Extract and generate a {@link Category} from a {@link Product} {@link ID}
     * @param readedID The {@link ID} you want to extract a {@link Category} from
     * @return An {@link OperationResult} able to tell you if the update was successful via {@link OperationResult#HasSucceeded()}
     * and in case of success, containing the {@link Category} extracted
     */
    private OperationResult<Category> ExtractProductCategoryFromID(final ID readedID)
    {
        final var SPLIT_OPERATION = SplitProductID(readedID);
        if (!SPLIT_OPERATION.HasSucceeded())
            return OperationResult.FAILURE(SPLIT_OPERATION.getMessage());

        String[] splitedId = SPLIT_OPERATION.getData();
        return OperationResult.SUCCESS(new Category(splitedId[0]), "SUCCESS : Category extracted from ID : " +  readedID.id());
    }

    /**
     * Used to determine if a category already exist for the productMap
     * @param category The category you want to test
     * @return An {@link OperationResult} able to tell you if the operation was a success via {@link OperationResult#HasSucceeded()}
     */
    private OperationResult<Void> CheckIfProductCategoryExist(final Category category)
    {
        if (!productMap.containsKey(category))
            return OperationResult.FAILURE("ERROR : The Category used is unknown from the database : " + category.categoryName());

        return OperationResult.SUCCESS("SUCCESS : Category found in database. Category : " + category.categoryName());
    }

    /**
     * Update the internal {@link DataIdGenerator} specific to a {@link Category} of {@link Product}
     * during the initialisation of the {@link Database}
     * @param productId The {@link ID} currently in insertion
     * @param category The {@link Category} owning the targeted {@link Product}
     */
    private void UpdateProductIdGenerator(final ID productId, final Category category)
    {
        //todo : check if the id is malformed
        //Product ID Format : Category-Number
        String id = productId.id();
        long idValue = Long.parseLong(id.split("-")[1]);
        productMap.get(category).TryUpdateCategoryID(idValue);
    }
    //endregion Product_Methods

    //region Order_Methods
    /**
     * Read the data of an internal {@link Order} and return a REFERENCE of it
     * @param orderId The {@link ID} of the targeted Order
     * @return An {@link OperationResult} able to tell you if the deletion was a success using {@link OperationResult#HasSucceeded()}
     * and containing, in case of success, a REFERENCE of the internal {@link Order}
     */
    public OperationResult<Order> ReadOrderInternal(final ID orderId)
    {
        if (!orderMap.containsKey(orderId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + orderId.id());

        return OperationResult.SUCCESS(orderMap.get(orderId), "SUCCESS : Order found in Database. ID :  " + orderId.id());
    }

    /**
     * Return true if the storage is in capacity to provide an {@link Order} with all the desired content
     * @param order The {@link Order} you want to check
     * @return True if the storage has enough of each desired {@link Product}, false otherwise
     */
    private boolean CheckIfOrderIsDoable(final Order order)
    {
        if (order.isDelivered())
            return false;

        for (OrderDetail detail : order.getOrderedProduct())
        {
            OperationResult<Product> res = ReadProductInternal(detail.productID());
            if (!res.HasSucceeded())
                return false;

            if (res.getData().getStoredQuantity() < detail.productQuantity())
                return false;
        }
        return true;
    }

    /**
     * Decrement the stored {@link Product} listed in the {@link Order} by the amount in the {@link OrderDetail}
     * @param order The {@link Order} to fulfill
     */
    private void FulfillOrder(final Order order)
    {
        for (OrderDetail detail : order.getOrderedProduct())
        {
            Product product = ReadProductInternal(detail.productID()).getData();
            product.setStoredQuantity(product.getStoredQuantity() - detail.productQuantity());
        }
        order.UpdateToDeliveredStatus();
    }

    /**
     * Check for each {@link Product} on the {@link Order} if it exists in the {@link Database} and if the stock are sufficient
     * to fulfill the order. It then set a message in the {@link Order} to explain the reason
     * @param order The {@link Order} you want to set on non Delivered status
     */
    private void SetNonDoableOrderReason(final Order order)
    {
        StringBuilder builder = new StringBuilder();

        for (OrderDetail detail : order.getOrderedProduct())
        {
            OperationResult<Product> res = ReadProductInternal(detail.productID());
            if (!res.HasSucceeded())
            {
                builder.append(res.getMessage()).append("\n");
                continue;
            }

            Product product = res.getData();
            if (product.getStoredQuantity() < detail.productQuantity())
            {
                final int MISSING_QUANTITY = detail.productQuantity() - product.getStoredQuantity();
                builder.append(MISSING_QUANTITY)
                        .append(" ").append(detail.productID().id())
                        .append(" are missing\n");
            }
        }
        order.UpdateToNonDeliveredStatus(builder.toString());
    }


    /**
     * Use by {@link #ExtractFromFileAndInsertOrder(String)} to get all the detail from an Order
     * @param orderContent All the content from an Order from the read CSV file
     * @param orderID The ID of the {@link Order} currently processed, used in a logError
     * @return A List containing all the {@link OrderDetail} of a single {@link Order}
     */
    private List<OrderDetail> ExtractOrderDetails(final ArrayList<String> orderContent, final ID orderID)
    {
        UpdateOrderIdGenerator(orderID);

        //OrderContent Format for each String : ID(Name-Number)=Quantity
        String[] productDetail;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (String string : orderContent)
        {
            productDetail = string.split("=");
            ID productID = new ID(productDetail[0]);

            if (!ReadProductInternal(productID).HasSucceeded())
            {
                LoggerHelper.log.warn("You are trying to initialize an Order from the file with an unknown Product ID." +
                                              "Order ID targeted : " + orderID.id());
                continue;
            }

            final int productQuantity = Integer.parseInt(productDetail[1]);
            orderDetails.add(new OrderDetail(productID, productQuantity));
        }

        return orderDetails;
    }

    /**
     * Update the internal {@link DataIdGenerator} {@link #orderIDGenerator} during the initialisation of the {@link Database}
     * @param orderID The {@link ID} currently in insertion
     */
    private void UpdateOrderIdGenerator(final ID orderID)
    {
        //Order ID Format : Number
        long idValue = Long.parseLong(orderID.id());
        orderIDGenerator.TryUpdateCurrentId(idValue);
    }
    //endregion Order_Methods

    //endregion PRIVATE_METHODS
}
