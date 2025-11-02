package fr.cda.view;

import fr.cda.controller.GUIController;
import fr.cda.event.EventBus;
import fr.cda.event.VoidEvent;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.model.Product;
import fr.cda.util.LoggerHelper;
import fr.cda.view.cellRenderer.ProductComboBoxCellRenderer;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * A model shared between class who need to work with the different value of an {@link Order}
 */
public abstract class OrderDialog extends DataDialog<Order>
{
    protected Product[] existingProducts;

    protected JTextField clientNameField;
    protected JButton addNewProductButton;

    protected JPanel listContainerPanel;
    protected ArrayList<OrderLinePanel> orderLines;

    protected EventBus eventBus;

    /**
     * @param config              The initial configuration of the {@link JDialog}
     * @param controller          Injection of the {@link GUIController}
     * @param frameOwner          The {@link JFrame} owning the internal {@link JDialog}
     * @param onDialogAchieveTask A {@link Consumer} who can be called by any child class if necessary
     */
    public OrderDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                       Consumer<OperationResult<Order>> onDialogAchieveTask)
    {
        super(config, controller, frameOwner, onDialogAchieveTask);

        eventBus = new EventBus();
        orderLines = new ArrayList<>();
    }

    /**
     * Implementation allowing us to retrieve the data inserted by the user and start an action with it
     */
    @Override
    protected void OnValidateButton()
    {
        String clientName = clientNameField.getText();
        if (clientName.isBlank())
        {
            JOptionPane.showMessageDialog(dialog, "The client name is emptuy", "Client name error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.util.List<Product> selectedProducts = new java.util.ArrayList<>();
        java.util.List<Integer> quantities = new java.util.ArrayList<>();

        for (OrderLinePanel line : orderLines)
        {
            Product product = (Product) line.getProductCombo().getSelectedItem();
            String qtyText = line.getQuantityField().getText().trim();
            int quantity;

            try
            {
                quantity = Integer.parseInt(qtyText);
                if (quantity <= 0)
                {
                    JOptionPane.showMessageDialog(dialog, "Quantity is negative for product : " + product.getId().id(),
                            "Quantity error", JOptionPane.WARNING_MESSAGE);
                }
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(dialog, "Quantity invalid for product : " + product.getId().id(),
                        "Quantity invalid format", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedProducts.add(product);
            quantities.add(quantity);
        }

        HandleValidateOperation(selectedProducts, quantities, clientName);
    }

    /**
     * Allow us to generate the GUI of the view
     */
    @Override
    protected void Init()
    {
        super.Init();

        LoggerHelper.log.info("START : initialisation of an OrderDialog window");

        GetProductListFromDatabase();
        GenerateGUI();
    }

    /**
     * Allow us to initiate a demand to the database while setting up the interface. A callback is waiting the result
     */
    private void GetProductListFromDatabase()
    {
        controller.ReadAllProduct(this::ReadAllProductCallback);
    }

    /**
     * The method used to call all the different methods to create the complete interface
     */
    private void GenerateGUI()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel northPanel = CreateNorthPanel();
        JPanel centerPanel = CreateCenterPanel();
        JPanel southPanel = CreateSouthPanel();

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Generate the panel containing the two buttons on the bottom of the view : Validate and Cancel
     * @return A JPanel containing the components forming the desired part of the interface
     */
    private JPanel CreateSouthPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(validateButton);
        mainPanel.add(cancelButton);

        return mainPanel;
    }

    /**
     * Generate the panel containing the scrolling part displaying the {@link Product} of an {@link Order}
     * @return A JPanel containing the components forming the desired part of the interface
     */
    private JPanel CreateCenterPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));

        addNewProductButton = new JButton("+");
        addNewProductButton.addActionListener(this::OnAddItemButton);

        northPanel.add(addNewProductButton);


        listContainerPanel = new JPanel();
        listContainerPanel.setLayout(new BoxLayout(listContainerPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listContainerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        return mainPanel;
    }

    /**
     * Callback called when user click on the "+" button. Add a new row of selected {@link Product} to the view
     * @param actionEvent The event sent by the button
     */
    private void OnAddItemButton(ActionEvent actionEvent)
    {
        AddRow();
    }

    /**
     * Generate the panel containing the client name label and textField
     * @return A JPanel containing some components
     */
    private JPanel CreateNorthPanel()
    {
        JPanel clientNamePanel = new JPanel();
        clientNamePanel.setLayout(new BoxLayout(clientNamePanel, BoxLayout.X_AXIS));
        clientNamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel clientNameLabel = new JLabel("Client Name");

        final Dimension CLIENT_FIELD_DIMENSION = new Dimension(100, 22);
        clientNameField = new JTextField();
        clientNameField.setMaximumSize(CLIENT_FIELD_DIMENSION);
        clientNameField.setMinimumSize(CLIENT_FIELD_DIMENSION);

        clientNamePanel.add(clientNameLabel);
        clientNamePanel.add(Box.createHorizontalStrut(10));
        clientNamePanel.add(clientNameField);

        return clientNamePanel;
    }

    /**
     * Crée une ligne avec ComboBox, JTextField, et bouton Supprimer
     */
    protected void AddRow()
    {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JComboBox<Product> comboBox = GetComboBoxFromExistingProduct();

        JLabel quantityLabel = new JLabel("Quantity");
        JTextField quantityField = new JTextField();
        quantityField.setMaximumSize(new Dimension(50, 22));

        JButton removeButton = new JButton("X");


        rowPanel.add(comboBox);
        rowPanel.add(Box.createHorizontalStrut(20));
        rowPanel.add(quantityLabel);
        rowPanel.add(Box.createHorizontalStrut(5));
        rowPanel.add(quantityField);
        rowPanel.add(Box.createHorizontalStrut(20));
        rowPanel.add(removeButton);

        removeButton.addActionListener(e ->
        {
            RemoveRow(rowPanel);
        });

        orderLines.add(new OrderLinePanel(rowPanel, comboBox, quantityField));

        listContainerPanel.add(rowPanel);
        listContainerPanel.add(Box.createVerticalStrut(5));

        listContainerPanel.revalidate();
        listContainerPanel.repaint();
    }

    /**
     * Call to remove a row from the view AND her representation from {@link #orderLines}
     * @param toRemove The JPanel targeted
     */
    protected void RemoveRow(final JPanel toRemove)
    {
        listContainerPanel.remove(toRemove);
        orderLines.removeIf(line -> line.getPanel() == toRemove);
        listContainerPanel.revalidate();
        listContainerPanel.repaint();
    }

    /**
     * Call during initialisation stage to retrieve all existing Products from the Database
     *
     * @param operationResult An {@link OperationResult} able to tell if the operation succeeded via {@link OperationResult#HasSucceeded()}
     *                        and containing, in case of success, an Array of all the {@link Product} present in the {@link fr.cda.model.Database}
     */
    private void ReadAllProductCallback(OperationResult<Product[]> operationResult)
    {
        if (!operationResult.HasSucceeded())
        {
            final String ERROR_MESSAGE = "An issue occurred while retrieving the existing products : "
                                                 + operationResult.getMessage();
            LoggerHelper.log.error("ERROR : {}", ERROR_MESSAGE);
            JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, "Read all product error", JOptionPane.ERROR_MESSAGE);
            Close();
            return;
        }

        existingProducts = operationResult.getData();
        AddRow();
        eventBus.Publish(new VoidEvent());
    }

    /**
     * Generate a {@link JComboBox} with the choice of all {@link Product} owned by the {@link fr.cda.model.Database}
     * @return A {@link JComboBox} with a special renderer to display {@link Product}
     */
    JComboBox<Product> GetComboBoxFromExistingProduct()
    {
        JComboBox<Product> toReturn = new JComboBox<>(existingProducts);
        if (existingProducts.length > 0)
            toReturn.setSelectedIndex(0);

        toReturn.setRenderer(new ProductComboBoxCellRenderer());
        toReturn.setMaximumSize(new Dimension(250, 22));

        return toReturn;
    }

    /**
     * A Method called to act on the {@link Order} with the data collected by the Dialog
     * @param selectedProduct {@link Product} we are able to retrieve from the selection the user did on the view
     * @param productQuantities Quantities needed for each product
     * @param clientName Name of the client
     */
    protected abstract void HandleValidateOperation(final java.util.List<Product> selectedProduct,
                                                    final java.util.List<Integer> productQuantities,
                                                    final String clientName);
}
