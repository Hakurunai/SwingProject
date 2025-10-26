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
        database = new Database("");

        categoryToAdd = new  Category("Livre");
        alwaysUnknownCategory = new  Category("UNKNOW");

        alwaysMALFORMED_ID = new ID("55se/okf,q");
        alwaysID_WITH_UNKNOWN_CATEGORY = new ID("UNKNOWN_CAT-999999");
        alwaysBadID = new ID("Livre-9999999999");

        unknownProduct = Product.GenerateProductDTO("NAME", categoryToAdd, 1f);

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

        assertTrue(prodRes.getData().getId().contains(categoryToAdd.getCategoryName() + "-"), NEW_PRODUCT_TEST_FAIL +
                    "the returned ID is malformed : it doesn't start by 'categoryName-'");

        String idNumber = prodRes.getData().getId().split("-")[1];
        assertDoesNotThrow(() -> Long.parseLong(idNumber), NEW_PRODUCT_TEST_FAIL +
                                  "the returned ID is malformed : it doesn't end by a long");

        assertTrue(idNumber.length() > 0 && Long.parseLong(idNumber) > 0, NEW_PRODUCT_TEST_FAIL +
                                  "the returned ID is malformed : the long number is not > 0");



        Product prodWithUnknownCategory = Product.GenerateProductDTO("BAD_PRODUCT", alwaysUnknownCategory, 10f);
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

        var badRes = database.ReadProduct(alwaysBadID);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read a bad ID must return a FAILED OperationResult");

        badRes = database.ReadProduct(alwaysMALFORMED_ID);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read a malformed ID must return a FAILED OperationResult");

        badRes = database.ReadProduct(alwaysID_WITH_UNKNOWN_CATEGORY);
        assertFalse(badRes.HasSucceeded(), READ_PRODUCT_TEST_FAIL + "Trying to read an ID with unknown category must return a FAILED OperationResult");
    }
}