package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.Category;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * The view allowing us to insert a new {@link Product} and a new {@link Category} in the {@link fr.cda.model.Database}
 */
public class CreateProductDialog extends ProductDialog
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask   A {@link Consumer} called inside {@link DataDialog#DialogAchieveTask(OperationResult, Object)}
     */
    public CreateProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                               final Consumer<OperationResult<Product>> onDialogAchieveTask)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
    }

    /**
     * A Method called to create the {@link Product} with the data collected by the Dialog
     * @param name Name of the new {@link Product}
     * @param category {@link Category} of the new {@link Product}
     * @param priceRef Price of the new {@link Product}, wrapped in an AtomicReference cause of lambda usage
     * @param quantity Quantity of the new {@link Product}, wrapped in an AtomicReference cause of lambda usage
     */
    @Override
    protected void HandleProductActionOnDatabase(final String name, final Category category, final float priceRef,
                                               final int quantity)
    {
        Product product = Product.GenerateProductDTO(name, category, priceRef, quantity);
        controller.CreateNewProduct(product, prodCreationRes ->
        {
            if (prodCreationRes.HasSucceeded())
            {
                final Product toReturn = new Product(prodCreationRes.getData(), product.getName(),
                        product.getCategory(), product.getPrice(), product.getStoredQuantity());

                DialogAchieveTask(OperationResult.SUCCESS(prodCreationRes.getMessage()), toReturn);
            }
        });
    }
}
