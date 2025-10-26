package fr.cda.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represent an order with all the mandatory data
 */
public class Order
{
    private String id;
    private LocalDate creationDate;
    private String clientName;
    private List<Product> orderedProduct;

    private boolean isDelivered = false;
    private String nonDeliveredExplanation;

    private final static String DEFAULT_NON_DELIVERED_EXPLANATION = "N/A";

    /**
     * Complete ctor. If the id is not know when you want
     * to manipulate an Order, use {@link #GenerateOrderDTO(LocalDate, String, List)} instead
     * @param id Unique id of the Order
     * @param creationDate Date of creation
     * @param clientName Name of the client
     * @param orderedProduct Products contained in the Order
     */
    public Order(final String id, final LocalDate creationDate, final String clientName, final List<Product> orderedProduct)
    {
        this(creationDate, clientName, orderedProduct);
        this.id = id;
    }

    /**
     * Used by {@link #GenerateOrderDTO(LocalDate, String, List)} to create an Order with a NULL ID
     * @param creationDate Date of creation
     * @param clientName Name of the client
     * @param orderedProduct Products contained in the Order
     */
    private Order(final LocalDate creationDate, final String clientName, final List<Product> orderedProduct)
    {
        this.creationDate = creationDate;
        this.clientName = clientName;
        this.orderedProduct = orderedProduct;

        this.id = null;
        this.isDelivered = false;
        this.nonDeliveredExplanation = DEFAULT_NON_DELIVERED_EXPLANATION;
    }

    /**
     * Generate an Order without an id. Used to transfer the data
     * as a temporary format non-yet-integrated in the {@link Database}
     * @param creationDate Date of creation
     * @param clientName Name of the client
     * @param orderedProduct Products contained in the Order
     * @return
     */
    public static Order GenerateOrderDTO(final LocalDate creationDate, final String clientName, final List<Product> orderedProduct)
    {
        return new Order(creationDate, clientName, orderedProduct);
    }

    /**
     * Use to set the Order as non delivered while providing an explanation for it
     * @param explanation The message explaining the reason of this status
     */
    public void UpdateToNonDeliveredStatus(final String explanation)
    {
        isDelivered = false;
        nonDeliveredExplanation = explanation;
    }

    /**
     * Use to specify that a specific Order is now delivered
     * Erase content of nonDeliveredExplanation
     */
    public void UpdateToDeliveredStatus()
    {
        isDelivered = true;
        nonDeliveredExplanation = DEFAULT_NON_DELIVERED_EXPLANATION;
    }

    public String getId()
    {
        return id;
    }

    public LocalDate getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDate p_creationDate)
    {
        creationDate = p_creationDate;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String p_clientName)
    {
        clientName = p_clientName;
    }

    public List<Product> getOrderedProduct()
    {
        return orderedProduct;
    }

    public void setOrderedProduct(List<Product> p_orderedProduct)
    {
        orderedProduct = p_orderedProduct;
    }

    public boolean isDelivered()
    {
        return isDelivered;
    }


    public String getNonDeliveredExplanation()
    {
        return nonDeliveredExplanation;
    }

    public void setNonDeliveredExplanation(String p_nonDeliveredExplanation)
    {
        nonDeliveredExplanation = p_nonDeliveredExplanation;
    }
}
