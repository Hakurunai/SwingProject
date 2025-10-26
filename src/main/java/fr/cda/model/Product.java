package fr.cda.model;

/**
 * Represent a product with all the mandatory data
 */
public class Product
{
    private ID id;
    private String name;
    private Category category;

    private float price;
    private int storedQuantity;

    /**
     * Complete ctor. If the id is not know when you want
     * to manipulate a Product, use {@link #GenerateProductDTO(String, Category, float)} instead
     * @param id Unique id of the product
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     */
    public Product(final ID id, final String name, final Category category, final float price)
    {
        this(name, category, price);
        this.id = id;
        this.storedQuantity = 0;
    }

    /**
     * Used by {@link #GenerateProductDTO(String, Category, float)} to create a Product with a NULL ID
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     */
    private Product(final String name, final Category category, final float price)
    {
        this.name = name;
        this.category = category;
        this.price = price;
        this.id = null;
        this.storedQuantity = 0;
    }

    /**
     * Generate a Product without an id. Used to transfer the data
     * as a temporary format non-yet-integrated in the {@link Database}
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     * @return a Product with A NULL id
     */
    public static Product GenerateProductDTO(final String name, final Category category, final float price)
    {
        return new Product(name, category, price);
    }


    public ID getId() { return id; }

    public String getName()
    {
        return name;
    }

    public void setName(String p_name)
    {
        name = p_name;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category p_category)
    {
        category = p_category;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float p_price)
    {
        price = p_price;
    }

    public int getStoredQuantity()
    {
        return storedQuantity;
    }

    public void setStoredQuantity(int p_storedQuantity)
    {
        storedQuantity = p_storedQuantity;
    }
}
