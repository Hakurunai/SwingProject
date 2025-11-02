package fr.cda.model;

/**
 * Represent the detail of a single line of Product on an Order. IE : id and quantity of a {@link Product}
 * @param productID The ID of the {@link Product} the user want to buy
 * @param productQuantity The quantity asked by the buyer
 */
public record OrderDetail(ID productID, int productQuantity)
{
}
