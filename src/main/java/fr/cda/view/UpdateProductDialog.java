package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.Category;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * The view allowing us to update an existing {@link Product} in the {@link fr.cda.model.Database}
 */
public class UpdateProductDialog extends ProductDialog
{
    private final Product internalCopy;

    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask   A {@link Consumer} called inside {@link DataDialog#DialogAchieveTask(OperationResult, Object)}
     * @param productToUpdate   The {@link Product} this Dialog will update
     */
    public UpdateProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                               Consumer<OperationResult<Product>> onDialogAchieveTask, final Product productToUpdate)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);
        internalCopy = productToUpdate;
        InitField();
    }

    /**
     * Call the super before selecting the value based on the internal copy of the Product
     * @param result The {@link OperationResult} returned by the {@link fr.cda.model.Database}
     */
    @Override
    protected void OnCategoryComboInitialized(OperationResult<Category[]> result)
    {
        super.OnCategoryComboInitialized(result);
        categoryCombo.setSelectedItem(internalCopy.getCategory());
    }

    /**
     * Called to update the value of the corresponding {@link Product} in the {@link fr.cda.model.Database}
     * The {@link fr.cda.model.ID} of the Product must be known by the Database
     * @param name Name of the {@link Product}
     * @param category {@link Category} of the {@link Product}
     * @param priceRef Price of the {@link Product
     * @param quantity Quantity of the {@link Product}
     */
    @Override
    protected void HandleProductActionOnDatabase(final String name, final Category category,
                                                 final float priceRef, final int quantity)
    {
        internalCopy.setName(name);
        internalCopy.setCategory(category);
        internalCopy.setPrice(priceRef);
        internalCopy.setStoredQuantity(quantity);

        controller.UpdateProduct(internalCopy, updateProdRes ->
        {
            if (updateProdRes.HasSucceeded())
            {
                DialogAchieveTask(updateProdRes, internalCopy);
            }
        });
    }

    /**
     * Used to set the data in the different fields base on the copied {@link Product}
     */
    private void InitField()
    {
        nameField.setText(internalCopy.getName());
        priceField.setText(Float.toString(internalCopy.getPrice()));
        quantityField.setText(Integer.toString(internalCopy.getStoredQuantity()));
    }
}
