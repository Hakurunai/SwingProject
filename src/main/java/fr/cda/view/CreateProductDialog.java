package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.Category;
import fr.cda.model.ID;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.util.LoggerHelper;
import fr.cda.view.swing.SwingDialog;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.function.Consumer;

public class CreateProductDialog extends SwingDialog<OperationResult<Product>>
{
    static final Dimension TEXT_DIMENSION = new Dimension(150, 22);
    static final Dimension NUMBER_DIMENSION = new Dimension(50, 22);


    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JTextField newCategoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JButton validateButton;
    private JButton cancelButton;

    private JCheckBox newCategoryCheck;

    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     */
    public CreateProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                               final Consumer<OperationResult<Product>> consumer)
    {
        super(config, controller, frameOwner, consumer, true);
    }


    @Override
    protected void Init()
    {
        LoggerHelper.log.info("START : initialisation of a CreateProductDialog window");

        //mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //product name
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(new JLabel("Product Name : "));
        nameField = new JTextField();
        nameField.setMinimumSize(TEXT_DIMENSION);
        nameField.setMaximumSize(TEXT_DIMENSION);
        namePanel.add(nameField);
        mainPanel.add(namePanel);

        //existing category
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryPanel.add(new JLabel("Categories : "));

        categoryCombo = new JComboBox<>();
        categoryCombo.setMinimumSize(TEXT_DIMENSION);
        categoryCombo.setMaximumSize(TEXT_DIMENSION);

        controller.ReadAllProductCategory(result ->
        {
            if (result.HasSucceeded())
            {
                String[] catValues = Arrays.stream(result.getData()).map(Category::categoryName).toArray(String[]::new);
                categoryCombo.setModel(new DefaultComboBoxModel<>(catValues));
                if (catValues.length > 0)
                {
                    categoryCombo.setSelectedIndex(0);
                }
                return;
            }
            categoryCombo.setModel(new DefaultComboBoxModel<>());
        });

        categoryPanel.add(categoryCombo);

        //checkBox to create a new category
        newCategoryCheck = new JCheckBox("New category ?");
        categoryPanel.add(newCategoryCheck);

        mainPanel.add(categoryPanel);

        // New category, disable by default
        JPanel newCategoryPanel = new JPanel();
        newCategoryPanel.setLayout(new BoxLayout(newCategoryPanel, BoxLayout.X_AXIS));
        newCategoryPanel.add(new JLabel("New category : "));
        newCategoryField = new JTextField();
        newCategoryField.setEnabled(false);
        newCategoryField.setMinimumSize(TEXT_DIMENSION);
        newCategoryField.setMaximumSize(TEXT_DIMENSION);
        newCategoryPanel.add(newCategoryField);
        mainPanel.add(newCategoryPanel);

        // Enabling/Disabling category comboBox
        newCategoryCheck.addActionListener(e ->
        {
            boolean selected = newCategoryCheck.isSelected();
            newCategoryField.setEnabled(selected);
            categoryCombo.setEnabled(!selected);
        });

        //price
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.X_AXIS));
        pricePanel.add(new JLabel("Price : "));
        priceField = new JTextField();
        priceField.setMinimumSize(NUMBER_DIMENSION);
        priceField.setMaximumSize(NUMBER_DIMENSION);
        pricePanel.add(priceField);
        mainPanel.add(pricePanel);

        //Quantity
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.add(new JLabel("Quantity : "));
        quantityField = new JTextField();
        quantityField.setMinimumSize(NUMBER_DIMENSION);
        quantityField.setMaximumSize(NUMBER_DIMENSION);
        quantityPanel.add(quantityField);
        mainPanel.add(quantityPanel);

        //buttons
        JPanel buttonPanel = new JPanel();
        validateButton = new JButton("Validate");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(validateButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        cancelButton.addActionListener(e -> Close());
        validateButton.addActionListener(this::OnValidateButton);

        dialog.setContentPane(mainPanel);
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
        HandleProductCreation(name, category, price, quantity);
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
