package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.Category;
import fr.cda.model.ID;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class CreateProductDialog extends ProductDialog
{
    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param consumer   A {@link Consumer} who can be called by any child class if necessary
     */
    public CreateProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                               final Consumer<OperationResult<Product>> consumer)
    {
        super(config, controller, frameOwner, consumer);
    }

    @Override
    protected void SetValidateButtonListener()
    {
        validateButton.addActionListener(this::OnValidateButton);
    }

    /**
     * Method called when the button Validate is pressed
     * @param e The event sent by the button
     */
    private void OnValidateButton(ActionEvent e)
    {
        String name = nameField.getText().trim();
        String categoryRead = newCategoryCheck.isSelected() ? newCategoryField.getText().trim().toUpperCase()
                                      : (String) categoryCombo.getSelectedItem();
        float price = 0f;
        int quantity = 0;

        try
        {
            price = Float.parseFloat(priceField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());

            if (price <= 0f || quantity < 0)
            {
                JOptionPane.showMessageDialog(dialog, "Price and quantity must be positive",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(dialog, "Price and quantities need to be valid numbers",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (name.isEmpty() || categoryRead.isEmpty())
        {
            JOptionPane.showMessageDialog(dialog, "All fields need a value",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Creation of the Product
        Category category = new Category(categoryRead);
        if (newCategoryCheck.isSelected())
        {
            HandleCategoryThenProductCreation(category, name, price, quantity);
        }
        else
        {
            HandleProductCreation(name, category, price, quantity);
        }
    }

    /**
     * Ask the linekd {@link GUIController} to create a new {@link Category}, and in case of success continue
     * by asking him to create a new {@link Product}
     * @param category The category of the new {@link Product}
     * @param name The name of the new {@link Product}
     * @param price The price of the new {@link Product}
     * @param quantity The quantity of the new {@link Product}
     */
    private void HandleCategoryThenProductCreation(final Category category, final String name, final float price, final int quantity)
    {
        controller.CreateNewProductCategory(category,
        res ->
        {
            if (res.HasSucceeded())
                HandleProductCreation(name, category, price, quantity);
            else
            {
                JOptionPane.showMessageDialog(dialog, res.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * A Method called to create the {@link Product} with the data collected by the Dialog
     * @param name Name of the new {@link Product}
     * @param category {@link Category} of the new {@link Product}
     * @param priceRef Price of the new {@link Product}, wrapped in an AtomicReference cause of lambda usage
     * @param quantity Quantity of the new {@link Product}, wrapped in an AtomicReference cause of lambda usage
     */
    private void HandleProductCreation(final String name, final Category category, final float priceRef,
                                       final int quantity)
    {
        Product product = Product.GenerateProductDTO(name, category, priceRef, quantity);
        controller.CreateNewProduct(product, prodCreationRes ->
        {
            if (prodCreationRes.HasSucceeded())
            {
                CreateProductAndEndSelf(prodCreationRes, product);
            }
        });
    }

    /**
     * Create the {@link Product} to pass to {@link #OnDialogAchieveTask} before closing herself
     * @param prodCreationRes The {@link OperationResult} sent by the {@link fr.cda.model.Database} in response of the
     * creation of a new {@link Product}, containing in case of success the {@link ID} generated for him
     * @param product The model created via {@link Product#GenerateProductDTO(String, Category, float, int)} by the front
     */
    private void CreateProductAndEndSelf(final OperationResult<ID> prodCreationRes, final Product product)
    {
        Product toReturn = new Product(prodCreationRes.getData(), product.getName(),
                product.getCategory(), product.getPrice(), product.getStoredQuantity());

        OnDialogAchieveTask.accept(OperationResult.SUCCESS(toReturn, prodCreationRes.getMessage()));
        Close();
    }
}
