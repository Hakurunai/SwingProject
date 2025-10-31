package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.model.Category;
import fr.cda.model.OperationResult;
import fr.cda.model.Product;
import fr.cda.util.LoggerHelper;
import fr.cda.view.swing.SwingDialog;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A model shared between class who need to work with the different value of a {@link Product}
 */
public abstract class ProductDialog extends SwingDialog
{
    protected final Consumer<OperationResult<Product>> OnDialogAchieveTask;

    static final Dimension TEXT_DIMENSION = new Dimension(150, 22);
    static final Dimension NUMBER_DIMENSION = new Dimension(50, 22);

    protected JTextField nameField;
    protected JComboBox<String> categoryCombo;
    protected JTextField newCategoryField;
    protected JTextField priceField;
    protected JTextField quantityField;
    protected JButton validateButton;
    protected JButton cancelButton;

    protected JCheckBox newCategoryCheck;

    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param consumer   A {@link Consumer} who can be called by any child class if necessary
     */
    public ProductDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                         Consumer<OperationResult<Product>> consumer)
    {
        super(config, controller, frameOwner, true);
        this.OnDialogAchieveTask = consumer;
    }

    /**
     * Used to set a specific listener to the Validate Button. Is called at the end of {@link #Init()}
     */
    protected abstract void SetValidateButtonListener();

    @Override
    protected void Init()
    {
        LoggerHelper.log.info("START : initialisation of a ProductDialog window");

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
        HandleFillCategoryChoice();
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
        SetValidateButtonListener();

        dialog.setContentPane(mainPanel);
    }

    /**
     * Called to retrieve all the different categories from the linked {@link fr.cda.model.Database} to populate the {@link JComboBox}
     */
    private void HandleFillCategoryChoice()
    {
        controller.ReadAllProductCategory(result ->
        {
            if (!result.HasSucceeded())
            {
                categoryCombo.setModel(new DefaultComboBoxModel<>());
                return;
            }

            String[] catValues = Arrays.stream(result.getData()).map(Category::categoryName).toArray(String[]::new);
            categoryCombo.setModel(new DefaultComboBoxModel<>(catValues));
            if (catValues.length > 0)
            {
                categoryCombo.setSelectedIndex(0);
            }
        });
    }
}
