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
     * to manipulate a Product, use {@link #GenerateProductDTO(String, Category, float, int)} instead
     * @param id Unique id of the product
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     */
    public Product(final ID id, final String name, final Category category, final float price, final int storedQuantity)
    {
        this(name, category, price, storedQuantity);
        this.id = id;
    }

    /**
     * Copy ctor, used by the {@link Database} to protect her internal objects
     * @param other Object from who we want to copy values
     */
    public Product(final Product other)
    {
        //Immutable (currently ID and Category are immutable)
        this.id = other.id;
        this.category = other.category;
        this.name = other.name;

        //Simple copy
        this.price = other.price;
        this.storedQuantity = other.storedQuantity;
    }

    /**
     * Used by {@link #GenerateProductDTO(String, Category, float, int)} to create a Product with a NULL ID
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     * @param storedQuantity Quantity of the Product
     */
    private Product(final String name, final Category category, final float price, final int storedQuantity)
    {
        this.name = name;
        this.category = category;
        this.price = price;
        this.id = null;
        this.storedQuantity = storedQuantity;
    }

    /**
     * Generate a Product without an id. Used to transfer the data
     * as a temporary format non-yet-integrated in the {@link Database}
     * @param name Name of the product
     * @param category Category of the product
     * @param price Price of the product
     * @param storedQuantity Quantity of the new Product
     * @return a Product with A NULL id
     */
    public static Product GenerateProductDTO(final String name, final Category category, final float price, final int storedQuantity)
    {
        return new Product(name, category, price, storedQuantity);
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
