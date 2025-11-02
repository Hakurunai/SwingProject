package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.event.VoidEvent;
import fr.cda.model.*;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The view allowing us to update an existing {@link Order} in the {@link fr.cda.model.Database}
 */
public class UpdateOrderDialog extends OrderDialog
{
    private final Order internalOrder;

    /**
     * @param config              The initial configuration of the {@link JDialog}
     * @param controller          Injection of the {@link GUIController}
     * @param frameOwner          The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask A {@link Consumer} who can be called by any child class if necessary
     * @param orderToUpdate       The Order to update with this Dialog
     */
    public UpdateOrderDialog(final SwingViewConfig config, final GUIController controller, final JFrame frameOwner,
                             final Consumer<OperationResult<Order>> onDialogAchieveTask, final Order orderToUpdate)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
        this.internalOrder = orderToUpdate;

        eventBus.Subscribe(VoidEvent.class, listener ->
        {
            InitGUIWithValueFromObjectToUpdate();
        });
    }

    /**
     * Implementation allowing us to try to update an existing {@link Order} in the {@link Database}
     * @param selectedProduct {@link Product} we are able to retrieve from the selection the user did on the view
     * @param productQuantities Quantities needed for each product
     * @param clientName Name of the client
     */
    @Override
    protected void HandleValidateOperation(List<Product> selectedProduct, List<Integer> productQuantities, String clientName)
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

        final Order orderCreatedFromDialog = new Order(internalOrder.getId(), internalOrder.getCreationDate(),
                                                        clientName, details);

        controller.UpdateOrder(orderCreatedFromDialog, orderCreationRes ->
        {
            if (orderCreationRes.HasSucceeded())
            {
                DialogAchieveTask(OperationResult.SUCCESS(orderCreationRes.getMessage()), orderCreatedFromDialog);
            }
        });
    }

    /**
     * Allow the view to update all his component values based on the {@link Order} we want to update from her
     */
    private void InitGUIWithValueFromObjectToUpdate()
    {
        clientNameField.setText(internalOrder.getClientName());

        if (internalOrder.getOrderedProduct().size() > orderLines.size())
        {
            final int DIFFERENCE = internalOrder.getOrderedProduct().size() - orderLines.size();
            for (int i = 0 ; i < DIFFERENCE ; ++i)
            {
                AddRow();
            }
        }
        else if (internalOrder.getOrderedProduct().size() < orderLines.size())
        {
            final int DIFFERENCE = orderLines.size() - internalOrder.getOrderedProduct().size();
            for (int i = 0 ; i < DIFFERENCE ; ++i)
            {
                RemoveRow(orderLines.getLast().getPanel());
            }
        }

        for (int i = 0 ; i < internalOrder.getOrderedProduct().size(); ++i)
        {
            OrderDetail detail = internalOrder.getOrderedProduct().get(i);
            orderLines.get(i).getQuantityField().setText(Integer.toString(detail.productQuantity()));
            SelectProductInComboBox(orderLines.get(i).getProductCombo(), detail.productID());
        }
    }

    /**
     * Allow us to set the value of a {@link JComboBox} of {@link Product} if this value exist in her model
     * @param comboBox The targeted comboBox
     * @param target The {@link ID} you want to set the value of the comboBox
     */
    private void SelectProductInComboBox(JComboBox<Product> comboBox, ID target)
    {
        ComboBoxModel<Product> model = comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++)
        {
            Product p = model.getElementAt(i);
            if (p.getId().equals(target))
            {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }
}
