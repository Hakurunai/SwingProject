package fr.cda.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent an order with all the mandatory data
 */
public class Order
{
    private ID id;
    private LocalDate creationDate;
    private String clientName;
    private List<OrderDetail> orderedProduct;

    private boolean isDelivered = false;
    private String nonDeliveredExplanation;

    private final static String DEFAULT_NON_DELIVERED_EXPLANATION = "N/A";

    /**
     * Complete ctor. If the id is not know when you want
     * to manipulate an Order, use {@link #GenerateOrderDTO(String, LocalDate, List)} instead
     * @param id Unique id of the Order
     * @param creationDate Date of creation
     * @param clientName Name of the client
     * @param orderedProduct Products contained in the Order
     */
    public Order(final ID id, final LocalDate creationDate, final String clientName, final List<OrderDetail> orderedProduct)
    {
        this(clientName, creationDate, orderedProduct);
        this.id = id;
    }

    /**
     * Copy ctor, used by the {@link Database} to protect her internal objects
     * @param other Object from who we want to copy values
     */
    public Order(final Order other)
    {
        //Immutable (ID is currently immutable in his state)
        id  = other.id;
        creationDate = other.creationDate;
        clientName = other.clientName;
        nonDeliveredExplanation = other.nonDeliveredExplanation;

        //Copy
        isDelivered = other.isDelivered;
        orderedProduct = new ArrayList<>(other.orderedProduct);
    }


    /**
     ** Used by {@link #GenerateOrderDTO(String, LocalDate, List)} to create an Order with a NULL ID
     * @param clientName Name of the client
     * @param creationDate The date of the creation of the Order
     * @param orderedProduct ProductDetail contained in the Order
     */
    private Order(final String clientName, final LocalDate creationDate, final List<OrderDetail> orderedProduct)
    {
        this.clientName = clientName;
        this.orderedProduct = orderedProduct;

        this.creationDate = creationDate;
        this.id = null;
        this.isDelivered = false;
        this.nonDeliveredExplanation = DEFAULT_NON_DELIVERED_EXPLANATION;
    }

    /**
     * Generate an Order without an id. Used to transfer the data
     * as a temporary format non-yet-integrated in the {@link Database}
     * @param clientName Name of the client
     * @param orderedProduct Products contained in the Order
     * @return An order without any valid {@link ID}
     */
    public static Order GenerateOrderDTO(final String clientName, final LocalDate creationDate,
                                         final List<OrderDetail> orderedProduct)
    {
        return new Order(clientName, creationDate, orderedProduct);
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

    public ID getId()
    {
        return id;
    }

    public LocalDate getCreationDate()
    {
        return creationDate;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String p_clientName)
    {
        clientName = p_clientName;
    }

    public List<OrderDetail> getOrderedProduct()
    {
        return orderedProduct;
    }

    public void setOrderedProduct(List<OrderDetail> p_orderedProduct)
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

    public void setNonDeliveredExplanation(String p_nonDeliveredExplanation) {nonDeliveredExplanation = p_nonDeliveredExplanation;}
}
