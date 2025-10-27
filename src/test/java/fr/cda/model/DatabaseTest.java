package fr.cda.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest
{
    Database database;

    Category categoryToAdd;
    Category alwaysUnknownCategory;

    ID alwaysMALFORMED_ID;
    ID alwaysID_WITH_UNKNOWN_CATEGORY;
    ID alwaysBadID;

    Product unknownProduct;


    @BeforeAll
    static void SetUp()
    {
    }

    @BeforeEach
    void setUp()
    {
        database = new Database();

        categoryToAdd = new  Category("Livre");
        alwaysUnknownCategory = new  Category("UNKNOW");

        alwaysMALFORMED_ID = new ID("55se/okf,q");
        alwaysID_WITH_UNKNOWN_CATEGORY = new ID("UNKNOWN_CAT-999999");
        alwaysBadID = new ID("Livre-9999999999");

        unknownProduct = Product.GenerateProductDTO("NAME", categoryToAdd, 1f, 4);

    }

    @AfterEach
    void tearDown()
    {
    }

    @AfterAll
    static void RemoveAll()
    {
    }

    @Test
    void CreateNewProduct()
    {
        final String NEW_PRODUCT_TEST_FAIL = "Create new Product failed : ";

        var catRes = database.CreateNewProductCategory(categoryToAdd);
        assertTrue(catRes.HasSucceeded(), NEW_PRODUCT_TEST_FAIL + "Issue while setting up the category");

        var prodRes = database.CreateNewProduct(unknownProduct);
        assertNotNull(prodRes, NEW_PRODUCT_TEST_FAIL + "the method returned a null object");

        assertNotNull(prodRes.getData(), NEW_PRODUCT_TEST_FAIL + "the returned OperationResult data is null");

        assertTrue(prodRes.HasSucceeded(), NEW_PRODUCT_TEST_FAIL + "the returned OperationResult is marked" +
                                                                            "as failed");

        assertTrue(prodRes.getData().id().contains(categoryToAdd.categoryName() + "-"), NEW_PRODUCT_TEST_FAIL +
                    "the returned ID is malformed : it doesn't start by 'categoryName-'");

        String idNumber = prodRes.getData().id().split("-")[1];
        assertDoesNotThrow(() -> Long.parseLong(idNumber), NEW_PRODUCT_TEST_FAIL +
                                  "the returned ID is malformed : it doesn't end by a long");

        assertTrue(idNumber.length() > 0 && Long.parseLong(idNumber) > 0, NEW_PRODUCT_TEST_FAIL +
                                  "the returned ID is malformed : the long number is not > 0");



        Product prodWithUnknownCategory = Product.GenerateProductDTO("BAD_PRODUCT", alwaysUnknownCategory, 10f, 999);
        var badProdRes = database.CreateNewProduct(prodWithUnknownCategory);
        assertFalse(badProdRes.HasSucceeded(), NEW_PRODUCT_TEST_FAIL + "a product with an unknown category cannot be added");
    }

    @Test
    void createNewProductCategory()
    {
        final String NEW_PRODUCT_CAT_TEST_FAIL = "Create new product category failed : ";

        OperationResult<Void> res = database.CreateNewProductCategory(categoryToAdd);

        assertNotNull(res, NEW_PRODUCT_CAT_TEST_FAIL + "the method returned a null object");

        assertNull(res.getData(), NEW_PRODUCT_CAT_TEST_FAIL + "the returned OperationResult has non null data");

        assertTrue(res.HasSucceeded(), NEW_PRODUCT_CAT_TEST_FAIL +
                                               "the returned OperationResult is marked as FAILED");

        res =  database.CreateNewProductCategory(categoryToAdd);
        assertFalse(res.HasSucceeded(), NEW_PRODUCT_CAT_TEST_FAIL +
                                                "the operation must failed if the same category is already registered");
    }

    @Test
    void readProduct()
    {
        final String READ_PRODUCT_TEST_FAIL = "Read product failed : ";

        var catRes = database.CreateNewProductCategory(categoryToAdd);
        assertTrue(catRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Issue while setting up the category");

        var prodAddRes = database.CreateNewProduct(unknownProduct);
        assertTrue(prodAddRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
        assertNotNull(prodAddRes.getData(), READ_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");



        var readRes = database.ReadProduct(prodAddRes.getData());
        assertNotNull(readRes, READ_PRODUCT_TEST_FAIL + "the method returned a null object");
        assertNotNull(readRes.getData(), READ_PRODUCT_TEST_FAIL + "the returned OperationResult data is null");
        assertTrue(readRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "the returned OperationResult is marked as FAILED");
        assertEquals(readRes.getData().getId(), prodAddRes.getData());

        readRes.getData().setName("A_NAME_DIFFERENT");
        var readRes2 = database.ReadProduct(prodAddRes.getData());
        assertNotEquals(readRes.getData().getName(), readRes2.getData().getName(), READ_PRODUCT_TEST_FAIL +
                                                             "The returned data must be a deep copy, not a shallow one");


        var badRes = database.ReadProduct(alwaysBadID);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read a bad ID must return a FAILED OperationResult");

        badRes = database.ReadProduct(alwaysMALFORMED_ID);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read a malformed ID must return a FAILED OperationResult");

        badRes = database.ReadProduct(alwaysID_WITH_UNKNOWN_CATEGORY);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read an ID with unknown category must return a FAILED OperationResult");
    }

    @Test
    void readAllProduct()
    {
        final String READ_ALL_PRODUCT_TEST_FAIL = "Read all product failed : ";
        final int NB_TO_INSERT = 10;

        var allProdRes = database.ReadAllProduct();
        assertNotNull(allProdRes,  READ_ALL_PRODUCT_TEST_FAIL + "The method returned a null object");
        assertNotNull(allProdRes.getData(), READ_ALL_PRODUCT_TEST_FAIL + "the returned OperationResult data is null");
        assertTrue(allProdRes.HasSucceeded(), READ_ALL_PRODUCT_TEST_FAIL + "the returned OperationResult is marked as FAILED");
        assertEquals(0, allProdRes.getData().length,  READ_ALL_PRODUCT_TEST_FAIL
                                                              + "the returned OperationResult must be empty");

        var catRes = database.CreateNewProductCategory(categoryToAdd);
        assertTrue(catRes.HasSucceeded(), READ_ALL_PRODUCT_TEST_FAIL + "Issue while setting up the category");

        for (int i = 0 ; i  < NB_TO_INSERT ; i++)
        {
            var prodAddRes = database.CreateNewProduct(unknownProduct);
            assertTrue(prodAddRes.HasSucceeded(), READ_ALL_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
            assertNotNull(prodAddRes.getData(), READ_ALL_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
        }

        allProdRes = database.ReadAllProduct();
        assertNotNull(allProdRes,  READ_ALL_PRODUCT_TEST_FAIL + "The method returned a null object");
        assertNotNull(allProdRes.getData(), READ_ALL_PRODUCT_TEST_FAIL + "the returned OperationResult data is null");
        assertTrue(allProdRes.HasSucceeded(), READ_ALL_PRODUCT_TEST_FAIL + "the returned OperationResult is marked as FAILED");
        assertEquals(NB_TO_INSERT, allProdRes.getData().length,  READ_ALL_PRODUCT_TEST_FAIL
                                                 + "the returned OperationResult.getData() must return a non empty array");


        allProdRes.getData()[0].setName("A_NAME_DIFFERENT");
        var allProdRes2 = database.ReadAllProduct();
        assertNotEquals(allProdRes.getData()[0].getName(), allProdRes2.getData()[0].getName(), READ_ALL_PRODUCT_TEST_FAIL +
                                                           "The returned data must be a deep copy, not a shallow one");
    }

    @Test
    void updateProduct()
    {
        final String UPDATE_PRODUCT_TEST_FAIL = "Update product failed : ";

        var catRes = database.CreateNewProductCategory(categoryToAdd);
        assertTrue(catRes.HasSucceeded(), UPDATE_PRODUCT_TEST_FAIL + "Issue while setting up the category");

        var prodAddRes = database.CreateNewProduct(unknownProduct);
        assertTrue(prodAddRes.HasSucceeded(), UPDATE_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
        assertNotNull(prodAddRes.getData(), UPDATE_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");


        Product newValue = new Product(prodAddRes.getData(), unknownProduct.getName(), unknownProduct.getCategory(), unknownProduct.getPrice()
        , unknownProduct.getStoredQuantity());
        newValue.setName("A_NAME_DIFFERENT");

        var readRes = database.ReadProduct(newValue.getId());
        assertTrue(readRes.HasSucceeded(), UPDATE_PRODUCT_TEST_FAIL + "Issue while setting up the product to update");
        assertNotNull(readRes.getData(), UPDATE_PRODUCT_TEST_FAIL + "Issue while setting up the product to update");

        assertNotEquals(newValue.getName(), readRes.getData().getName(), UPDATE_PRODUCT_TEST_FAIL +
                                          "Issue while setting up the product to update : the reading must be a deep copy");

        var updateRes = database.UpdateProduct(newValue);
        assertNotNull(updateRes, UPDATE_PRODUCT_TEST_FAIL + "The update has returned a null object");
        assertTrue(updateRes.HasSucceeded(), UPDATE_PRODUCT_TEST_FAIL +
                                                     "The update has returned an OperationResut marked as FAILED");
        readRes = database.ReadProduct(newValue.getId());
        assertEquals(newValue.getName(), readRes.getData().getName(), UPDATE_PRODUCT_TEST_FAIL +
                                                                              "The update must be stored inside the database");

        newValue.setName("NOTHING_TO_DO_WITH_THE_PREVIOUS_ONE");
        readRes = database.ReadProduct(newValue.getId());
        assertNotEquals(newValue.getName(), readRes.getData().getName(), UPDATE_PRODUCT_TEST_FAIL +
                                                                              "The stored update must be a deep copy of the passed reference");

    }

    @Test
    void deleteProduct()
    {
        final String DELETE_PRODUCT_TEST_FAIL = "Delete product failed : ";

        var catRes = database.CreateNewProductCategory(categoryToAdd);
        assertTrue(catRes.HasSucceeded(), DELETE_PRODUCT_TEST_FAIL + "Issue while setting up the category");

        var prodAddRes = database.CreateNewProduct(unknownProduct);
        assertTrue(prodAddRes.HasSucceeded(), DELETE_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
        assertNotNull(prodAddRes.getData(), DELETE_PRODUCT_TEST_FAIL + "Issue while setting up the product to read");
        Product dataInserted = new Product(prodAddRes.getData(), unknownProduct.getName(), unknownProduct.getCategory(), unknownProduct.getPrice()
        , unknownProduct.getStoredQuantity());

        var deletionRes = database.DeleteProduct(dataInserted.getId());
        assertNotNull(deletionRes, DELETE_PRODUCT_TEST_FAIL + "The deletion has returned a null object");
        assertNull(deletionRes.getData(), DELETE_PRODUCT_TEST_FAIL + "OperationResult.getData() returned must be null");
        assertTrue(deletionRes.HasSucceeded(), DELETE_PRODUCT_TEST_FAIL + "OperationResult is marked as FAILED");

        var readRes = database.ReadProduct(dataInserted.getId());
        assertFalse(readRes.HasSucceeded(), DELETE_PRODUCT_TEST_FAIL + "After a deletion, a read cannot find the object");
        assertNull(readRes.getData(), DELETE_PRODUCT_TEST_FAIL +
                                     "After a deletion, a read on a now non stored value must return an OperationResult with null data");
    }
}