package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.model.OrderDetail;
import fr.cda.model.Product;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The view allowing us to insert a new {@link Order} in the {@link fr.cda.model.Database}
 */
public class CreateOrderDialog extends OrderDialog
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask   A {@link Consumer} who can be called by any child class if necessary
     */
    public CreateOrderDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner, Consumer<OperationResult<Order>> onDialogAchieveTask)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
    }

    /**
     * Implementation. Allow us to try to insert a new {@link Order} in the {@link fr.cda.model.Database} via the data retrieved from the view
     * @param selectedProduct {@link Product} we are able to retrieve from the selection the user did on the view
     * @param productQuantities Quantities needed for each product
     * @param clientName Name of the client
     */
    @Override
    protected void HandleValidateOperation(final List<Product> selectedProduct,
                                           final List<Integer> productQuantities,
                                           final String clientName)
    {
        if (selectedProduct.size() != productQuantities.size())
            return;

        List<OrderDetail> details = new ArrayList<>();
        for (int i = 0; i < selectedProduct.size(); i++)
        {
            OrderDetail tempDetail = new OrderDetail(selectedProduct.get(i).getId(),
                                                    productQuantities.get(i));
            details.add(tempDetail);
        }

        final Order orderCreatedFromDialog = Order.GenerateOrderDTO(clientName, LocalDate.now(), details);
        controller.CreateNewOrder(orderCreatedFromDialog, orderCreationRes ->
        {
            if (orderCreationRes.HasSucceeded())
            {
                Order orderToreturn = new Order(orderCreationRes.getData(), orderCreatedFromDialog.getCreationDate(),
                        orderCreatedFromDialog.getClientName(), details);

                DialogAchieveTask(OperationResult.SUCCESS(orderCreationRes.getMessage()), orderToreturn);
            }
        });
    }
}
