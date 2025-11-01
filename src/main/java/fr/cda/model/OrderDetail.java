package fr.cda.model;

/**
 * Represent the detail of a single line of Product on an Order
 * @param productID The ID of the Product the user want to buy
 * @param productQuantity The quantity asked by the buyer
 */
public record OrderDetail(ID productID, int productQuantity)
{
}
