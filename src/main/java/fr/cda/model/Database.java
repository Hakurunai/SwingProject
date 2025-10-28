package fr.cda.model;

import fr.cda.Config;
import fr.cda.util.CSVHelper;
import fr.cda.util.LoggerHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represent a database in this program, living only in RAM
 */
public class Database
{
    private final String ORDER_FILE = "Commandes.txt";
    private final String PRODUCT_FILE = "Produits.txt";

    private static final String ERROR_ID_ALREADY_EXISTING = "The ID used is already present in the database";
    private static final String ERROR_UNKNOWN_ID = "The ID used is unknown from the database";
    private static final String ERROR_UNKNOWN_PRODUCT_CATEGORY = "The Category used is unknown from the database";


    private final Map<Category, ProductCategoryMap> productMap;
    //private final Map<Category, Map<ID, Product>> productMap;
    private final Map<ID, Order> orderMap;

    private DataIdGenerator orderIDGenerator;

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
            return OperationResult.FAILURE(ERROR_ID_ALREADY_EXISTING);


        Product newProduct = new Product(newProductID, product.getName(), CATEGORY, product.getPrice(), product.getStoredQuantity());
        productMap.get(CATEGORY).getProductMap().put(newProductID, newProduct);

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
            return OperationResult.FAILURE("The category is already present in the database : " + category.categoryName());

        productMap.put(category, new ProductCategoryMap());
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


        if (!productMap.get(CATEGORY).getProductMap().containsKey(productId))
            return OperationResult.FAILURE(ERROR_UNKNOWN_ID + " : " + productId.id());

        return OperationResult.SUCCESS(new Product(productMap.get(CATEGORY).getProductMap().get(productId)),
                                                        "Found Product in database");
    }

    /**
     *
     * @return all Product from the database, sorted by category
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
                "SUCCESS : read all Product from database");
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
        productMap.get(product.getCategory()).getProductMap().put(product.getId(), internalCopy);
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
        productMap.get(copy.getCategory()).getProductMap().remove(copy.getId());
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

    /**
     * Use to order the different initialization call needed by the {@link Database}
     */
    private void Init()
    {
        LoggerHelper.log.info("Start initialization of the Database");
        InitProduct();
        InitOrder();
    }

    /**
     * Used internally to read all Orders from the configuration file, called after {@link #InitProduct()}
     */
    private void InitOrder()
    {
        LoggerHelper.log.info("Start initialization of Order in the database");
        try
        {
            final String[] DATA = CSVHelper.ReadCsvFile(Config.DB_ORDER_FILE_PATH);
            for (String line : DATA)
            {
                ExtractAndInsertOrder(line);
            }
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("An error occurred while reading Order file at path : " + Config.DB_ORDER_FILE_PATH);
            e.printStackTrace();
        }
    }

    /**
     * Used in {@link #InitOrder()} to create the Orders from the read CSV file
     * @param line The content of the Order CSV file
     */
    private void ExtractAndInsertOrder(final String line)
    {
        //Todo : check if content is ill-formed

        //FORMAT : ID ; CreationDate ; ClientName ; ProductRef (ID=Quantity)...
        //1;21/09/2022;Macron Brigitte;LIVRE-1=1;LIVRE-2=2;...

        final int ORDER_CONTENT_INDEX_START_POSITION = 3;

        String[] dataContent = line.split(";");
        ID orderID = new ID(dataContent[0]);

        var readRes = ReadOrder(orderID);
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
                dataContent[1], orderDetails);
        orderMap.put(orderID, newOrder);
    }

    /**
     * Use by {@link #ExtractAndInsertOrder(String)} to get all the detail from an Order
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
        for (int i = 0 ; i < orderContent.size(); ++i)
        {
            productDetail = orderContent.get(i).split("=");
            ID productID = new ID(productDetail[0]);

            if (!ReadProduct(productID).HasSucceeded())
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
     * Internally used to Read all the product from the configuration file
     */
    private void InitProduct()
    {
        LoggerHelper.log.info("Start initialization of Product in the database");
        try
        {
            final String[] DATA = CSVHelper.ReadCsvFile(Config.DB_PRODUCT_FILE_PATH);

            for (String line : DATA)
            {
                ExtractAndInsertProduct(line);
            }
        }
        catch (IOException e)
        {
            LoggerHelper.log.error("An error occurred while reading Product file at path : " + Config.DB_PRODUCT_FILE_PATH);
            e.printStackTrace();
        }
    }

    /**
     * Used in {@link #InitProduct()} to create the Products from the read CSV file
     * @param line The content of the Product CSV file
     */
    private void ExtractAndInsertProduct(final String line)
    {
        //Todo : check if content is ill-formed

        //FORMAT : ID (Category-Number) ; Name ; Price ; Quantity
        //LIVRE-1;Les Miserables de Victor Hugo ;8.50;6
        String[] dataContent = line.split(";");
        final ID readedID = new ID(dataContent[0]);

        String[] splitedId = SplitProductID(readedID).getData();
        final Category category = new Category(splitedId[0]);

        var res = CheckIfProductCategoryExist(category);
        if (!res.HasSucceeded())
        {
            CreateNewProductCategory(category);
        }
        else
        {
            var resProd = ReadProduct(readedID);
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
}
