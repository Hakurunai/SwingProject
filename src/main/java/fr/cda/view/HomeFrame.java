package fr.cda.view;

import fr.cda.controller.DatabaseConnectionEvent;
import fr.cda.controller.DatabaseOperationEndedEvent;
import fr.cda.controller.GUIController;
import fr.cda.event.IEventListener;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.model.OrderDetail;
import fr.cda.model.Product;
import fr.cda.view.cellRenderer.ProductOrderListCellRenderer;
import fr.cda.view.swing.SwingFrame;
import fr.cda.util.LoggerHelper;
import fr.cda.util.SwingHelper;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultCaret;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public final class HomeFrame extends SwingFrame
{
    //region SWING_ATTRIBUTE
    private JTextArea infoArea;
    private JTextArea detailArea;

    private DefaultListModel<Object> dataList;
    private JList<Object> dataListView;

    private JButton buttonShowStorage;
    private JButton buttonUpdateSelectedItem;
    private JButton buttonShowOrder;
    private JButton buttonCreateNewItem;
    private JButton buttonRemoveItem;
    private JButton buttonMakeDeliveries;
    private JButton buttonComputeProfit;
    private JButton buttonSendMailData;
    private JButton buttonGenerateBackUp;
    //endregion SWING_ATTRIBUTE

    private int lastElementIndexSelectedByUserInDataListView = -1;
    private static final int PANEL_BORDER_SIZE = 10;

    //region IEventListener
    IEventListener<DatabaseConnectionEvent> databaseConnectionEventListener = this::OnDatabaseConnectionEvent;
    IEventListener<DatabaseOperationEndedEvent> databaseOperationEndedEventListener = this::OnDatabaseOperationEnded;
    //endregion IEventListener

    //region CTOR
    public HomeFrame(SwingViewConfig config, GUIController controller)
    {
        super(config, controller);

        controller.eventBus.Subscribe(DatabaseConnectionEvent.class, databaseConnectionEventListener);
        controller.eventBus.Subscribe(DatabaseOperationEndedEvent.class, databaseOperationEndedEventListener);
    }
    //endregion CTOR

    //region INIT_PHASE
    /**
     * Implementation of the inherited Init method from Screen class. Used to generate the JFrame and his components
     */
    @Override
    protected void Init()
    {
        LoggerHelper.log.info("START Initialisation of the Main App named : {}", frame.getTitle());
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel westPanel = CreateButtonPanel();
        panelMain.add(westPanel, BorderLayout.WEST);

        JPanel centerPanel = CreateCenterPanel();
        panelMain.add(centerPanel, BorderLayout.CENTER);

        frame.add(panelMain);
        LoggerHelper.log.info("END Initialisation of the Main App named : {}", frame.getTitle());
    }

    /**
     * Generate a JPanel containing 3 parts divided in two SplitPane
     * @return the JPanel used for the center of the main app
     */
    private JPanel CreateCenterPanel()
    {
        JPanel northPanel = CreateListAndDescriptionPanel();

        JPanel southPanel = CreateInfoArea();

        final int DIVIDER_LOCATION_PARAM = 400;
        JSplitPane verticalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northPanel, southPanel);
        verticalPane.setDividerLocation(DIVIDER_LOCATION_PARAM);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, 0, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));
        mainPanel.add(verticalPane, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Generate a JPanel containing a non-editable text-area used to display some information
     * @return the JPanel containing the text area
     */
    private JPanel CreateInfoArea()
    {
        infoArea = SwingHelper.CreateNonEditableTextArea();

        //Automatic scroll
        DefaultCaret caret = (DefaultCaret) infoArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(infoArea);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Generate a JPanel containing a SplitPane with a list and a non-editable text area
     * Some references are kept inside this class to access the data
     * @return a JPanel used to display and select some data
     */
    private JPanel CreateListAndDescriptionPanel()
    {
        buttonCreateNewItem = new JButton("+");
        buttonCreateNewItem.setToolTipText("Create new item");
        buttonCreateNewItem.setVisible(false);
        buttonCreateNewItem.addActionListener(this::OnAddItemButton);

        buttonRemoveItem = new JButton("-");
        buttonRemoveItem.setToolTipText("Delete selected items");
        buttonRemoveItem.setVisible(false);
        buttonRemoveItem.addActionListener(this::OnRemoveItemButton);

        //align the two button
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonsPanel.add(buttonCreateNewItem);
        buttonsPanel.add(buttonRemoveItem);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(buttonsPanel, BorderLayout.NORTH);

        dataList = new DefaultListModel<>();
        dataListView = new JList<>(dataList);
        dataListView.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dataListView.setCellRenderer(new ProductOrderListCellRenderer());
        dataListView.addListSelectionListener(this::OnDataListSelectionChanged);
        dataListView.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                OnListClic(e);
            }
        });

        JScrollPane scrollList = new JScrollPane(dataListView);
        westPanel.add(scrollList, BorderLayout.CENTER);

        // Détails du produit
        detailArea = SwingHelper.CreateNonEditableTextArea();
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetails = new JScrollPane(detailArea);


        final int DIVIDER_LOCATION_PARAM = 100;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, scrollDetails);
        splitPane.setDividerLocation(DIVIDER_LOCATION_PARAM);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Used to create the left pannel containing some buttons
     * References to those buttons will be kept inside this class
     * @return a JPanel with the buttons on one column
     */
    private JPanel CreateButtonPanel()
    {
        final int BUTTON_HORIZONTAL_STRUT = 0;
        final int BUTTON_VERTICAL_STRUT = 10;

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));

        buttonShowStorage = new JButton("Show storage");
        buttonShowStorage.addActionListener(e ->controller.ReadAllProduct(this::ReadAllProductCallback));

        buttonUpdateSelectedItem = new JButton("Update selected");
        buttonUpdateSelectedItem.addActionListener(this::OnOpenUpdateDialog);

        buttonShowOrder = new JButton("Show all order");
        buttonShowOrder.addActionListener(e ->controller.ReadAllOrder(this::ReadAllOrderCallback));

        buttonMakeDeliveries = new JButton("Make deliveries");
        buttonMakeDeliveries.addActionListener(e -> controller.MakeAllDeliveries(this::ReadAllOrderCallback));

        buttonComputeProfit = new JButton("Compute profit");
        buttonSendMailData = new JButton("Send data to mail");
        buttonGenerateBackUp = new JButton("Generate back up");

        //Todo: Add listener to the buttons

        JButton[] buttonArray =
                { buttonShowStorage, buttonShowOrder, buttonUpdateSelectedItem, buttonMakeDeliveries,
                        buttonComputeProfit, buttonSendMailData, buttonGenerateBackUp };

        SwingHelper.SetSameSize(buttonArray);
        SwingHelper.AddComponentToPanelWithStruts(panelButtons, buttonArray, BUTTON_HORIZONTAL_STRUT, BUTTON_VERTICAL_STRUT);

        return panelButtons;
    }
    //endregion INIT_PHASE


    //region PRIVATE_METHODS

    //region Event

    /**
     * Callback called when a user click on the AddItemButton. This will create a new Dialog to fill the data of the new entry
     * @param e The event sent by the button
     */
    private void OnAddItemButton(ActionEvent e)
    {
        if (dataList.isEmpty())
            return;

        LoggerHelper.log.info("TRIGGER HomeFrame::OnAddItemButton");
        Object item = dataList.getElementAt(0);
        if (item instanceof Product)
        {
            OpenCreateNewProductDialog();
        }
        else if (item instanceof Order)
        {
            OpenCreateNewOrderDialog();
        }
    }

    /**
     * Callback called when a new Product is created from an {@link CreateProductDialog} to add it to the view
     * @param result An {@link OperationResult} able to tell if the operation was succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link Product} newly created
     */
    private void OnNewProductCreated( OperationResult<Product> result)
    {
        if (result.HasSucceeded())
        {
            dataList.addElement(result.getData());
        }
    }

    /**
     * Call when {@link #buttonRemoveItem} is pressed. Remove the selected items
     * @param p_actionEvent The event sent by the button
     */
    private void OnRemoveItemButton(ActionEvent p_actionEvent)
    {
        int result = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete these items ?",
                "Deletion confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.NO_OPTION)
        {
            return;
        }

        RemoveItems(dataListView.getSelectedValuesList());
    }

    /**
     * Call when user press the button {@link #buttonUpdateSelectedItem}
     * @param p_actionEvent The event sent by the button
     */
    private void OnOpenUpdateDialog(ActionEvent p_actionEvent)
    {
        if (lastElementIndexSelectedByUserInDataListView < 0
            || lastElementIndexSelectedByUserInDataListView >= dataList.size())
        {
            JOptionPane.showMessageDialog(frame, "Please select one item to update", "Update item error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        OpenUpdateDialog(lastElementIndexSelectedByUserInDataListView);
    }

    /**
     * Callback called when a Product is updated from a {@link UpdateProductDialog} to update the view
     * @param productOperationResult An {@link OperationResult} able to tell if the operation was succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link Product} newly updated
     */
    private void OnProductUpdated(OperationResult<Product> productOperationResult)
    {
        if (!productOperationResult.HasSucceeded())
            return;

        controller.ReadAllProduct(this::ReadAllProductCallback);
    }

    /**
     * Callback called after the operation sent to the Database by the associated button
     * Retrieve the data and ask to display them
     * @param result The {@link OperationResult} from the {@link fr.cda.model.Database}
     */
    private void ReadAllProductCallback(final OperationResult<Product[]> result)
    {
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        DisplayDataListView(result.getData());
    }

    /**
     * Callback called when a new Product is created from an {@link CreateOrderDialog} to add it to the view
     * @param result An {@link OperationResult} able to tell if the operation was succeeded via {@link OperationResult#HasSucceeded()}
     * and, in case of success, containing the {@link Order} newly created
     */
    private void OnNewOrderCreated( OperationResult<Order> result)
    {
        if (result.HasSucceeded())
        {
            dataList.addElement(result.getData());
        }
    }

    /**
     * Callback called after the operation sent to the Database by the associated button
     * Retrieve the data and ask to display them
     * @param result The {@link OperationResult} from the {@link fr.cda.model.Database}
     */
    private void ReadAllOrderCallback(final OperationResult<Order[]> result)
    {
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        DisplayDataListView(result.getData());
    }

    /**
     * Callback called when the {@link #dataListView} selection change
     * Is used to determine the type of object to display and how the data are formatted
     * @param event The event from the ListView
     */
    private void OnDataListSelectionChanged(final ListSelectionEvent event)
    {
        if (!event.getValueIsAdjusting())
        {
            List<Object> selectedItems = dataListView.getSelectedValuesList();
            if (selectedItems.size() <= 0)
                return;

            Object first = selectedItems.getFirst();
            if (first instanceof Order) {
                List<Order> orders = selectedItems.stream()
                                             .filter(Order.class::isInstance)
                                             .map(Order.class::cast)
                                             .toList();
                DisplayOrderDetails(orders);
            }
            else if (first instanceof Product)
            {
                List<Product> products = selectedItems.stream()
                                                 .filter(Product.class::isInstance)
                                                 .map(Product.class::cast)
                                                 .toList();
                DisplayProductDetail(products);
            }
        }
    }

    /**
     * Callback when user clic on an element from the {@link #dataListView}
     * @param e The event sent by the JList
     */
    private void OnListClic(MouseEvent e)
    {
        if (!SwingUtilities.isLeftMouseButton(e))
            return;

        lastElementIndexSelectedByUserInDataListView = dataListView.locationToIndex(e.getPoint());
        if (lastElementIndexSelectedByUserInDataListView < 0)
            return;

        if (e.getClickCount() < 2)
            return;

        OpenUpdateDialog(lastElementIndexSelectedByUserInDataListView);
    }


    /**
     * Callback linked to the {@link GUIController#eventBus} to get the message in case of a Connection Event. Messages
     * are displayed in the {@link #infoArea}
     * @param databaseConnectionEvent The event sent by the controller
     */
    private void OnDatabaseConnectionEvent(final DatabaseConnectionEvent databaseConnectionEvent)
    {
        ShowMessage(databaseConnectionEvent.message());
    }

    /**
     * Callback linked to the {@link GUIController#eventBus} to get the messages sent by the {@link fr.cda.model.Database}
     * each time we need to access her. Messages are displayed in the {@link #infoArea}
     * @param event The event sent by the controller
     */
    private void OnDatabaseOperationEnded(final DatabaseOperationEndedEvent event)
    {
        ShowMessage(event.message());
    }
    //endregion Event

    /**
     * Called to open a new {@link CreateOrderDialog}. This object is then linked to the new Dialog by the callback {@link #OnNewOrderCreated(OperationResult)}
     */
    private void OpenCreateNewOrderDialog()
    {
        //todo : implement
    }

    /**
     * Called to open a new {@link CreateProductDialog}. This object is then linked to the new Dialog by the callback {@link #OnNewProductCreated(OperationResult)}
     */
    private void OpenCreateNewProductDialog()
    {
        SwingViewConfig config = new SwingViewConfig("Add new Product", 500, 200);
        CreateProductDialog createProductDialog = new CreateProductDialog(config, controller, frame, this::OnNewProductCreated);
        createProductDialog.Display();
    }

    /**
     * Call to open an Update Dialog on a selected item from {@link #dataListView}
     * @param INDEX The index of the selected item
     */
    private void OpenUpdateDialog(final int INDEX)
    {
        var item = dataListView.getModel().getElementAt(INDEX);
        if (item instanceof Product)
        {
            OpenUpdateProductDialog((Product)item);
        }
        else if (item instanceof Order)
        {
            Order order = (Order)item;
            if (order.isDelivered())
            {
                JOptionPane.showMessageDialog(frame, "Impossible to update an already delivered Order",
                        "Update Order Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            OpenUpdateOrderDialog((Order)item);
        }
    }

    /**
     * Called to open an {@link UpdateProductDialog} for a specific {@link Product}
     * @param itemToUpdate The product to update
     */
    private void OpenUpdateProductDialog(final Product itemToUpdate)
    {
        SwingViewConfig config = new SwingViewConfig("Update Product", 500, 200);
        UpdateProductDialog updateProductDialog = new UpdateProductDialog(config, controller, frame,
                this::OnProductUpdated, itemToUpdate);
        updateProductDialog.Display();
    }

    private void OpenUpdateOrderDialog(Order item)
    {
        //todo
    }

    /**
     * Used to determine the type of items in the JList and call the correct deletion method on the {@link GUIController}
     * @param selectedValuesList The list of object the user want to delete
     */
    private void RemoveItems(List<Object> selectedValuesList)
    {
        if (selectedValuesList.size() <= 0)
            return;

        Object object = selectedValuesList.getFirst();
        if (object instanceof Order)
        {
            List<Order> orders = selectedValuesList.stream()
                                          .map(obj -> (Order)obj)
                                         .toList();
            DeleteOrder(orders);
        }
        else if (object instanceof Product)
        {
            List<Order> products = selectedValuesList.stream()
                                         .map(obj -> (Order)obj)
                                         .toList();
            DeleteProduct(products);
        }
    }

    /**
     * Use to delete a list of {@link Product}
     * @param products The list of Product the user want to delete
     */
    private void DeleteProduct(List<Order> products)
    {
        //todo
    }

    /**
     * Use to delete a list of {@link Order}
     * @param orders The list of Product the user want to delete
     */
    private void DeleteOrder(List<Order> orders)
    {
        //todo
    }

    /**
     * Called to add a message to the {@link #infoArea} of this frame
     * @param message The message to display
     */
    private void ShowMessage(String message)
    {
        infoArea.append(message + "\n");
    }

    /**
     * Method to update the content of the ListPanel, update the visibility of the AddItemButton
     * @param result The content to display
     * @param <T> The type of the content to display
     */
    private <T> void DisplayDataListView(T[] result)
    {
        dataList.clear();
        dataList.ensureCapacity(result.length);
        dataList.addAll(Arrays.asList(result));
        dataListView.setSelectionInterval(0, dataList.getSize() - 1);

        SetButtonAddAndRemoveItemVisibility(dataList.getSize() > 0);
    }

    /**
     * A method called to format and display a list of {@link Order} object in the list area
     * @param selected The currently selected Orders we need to display
     */
    private void DisplayOrderDetails(final List<Order> selected)
    {
        StringBuilder builder = new StringBuilder();
        final String CHANGE_LINE = "\n";
        final String TABULATION = "\t";
        final String END_ORDER = "\n------------------------------------------------\n\n";
        for (Order order : selected)
        {
            builder.append("Order Number : ").append(order.getId().id()).append(CHANGE_LINE)
                    .append("Date : ").append(order.getCreationDate()).append(CHANGE_LINE)
                    .append("Client : ").append(order.getClientName()).append(CHANGE_LINE)
                    .append("Ref Products : ").append(CHANGE_LINE);

            for (OrderDetail product : order.getOrderedProduct())
            {
                builder.append(TABULATION)
                        .append(product.productID().id())
                        .append(" : ").append(product.productQuantity())
                        .append(CHANGE_LINE);
            }
            builder.append("Status : ").append(order.isDelivered() ? "Delivered" : "Not delivered").append(CHANGE_LINE);
            if (!order.isDelivered())
                builder.append("Non delivered explanation :").append(CHANGE_LINE)
                        .append(order.getNonDeliveredExplanation()).append(CHANGE_LINE);

            builder.append(END_ORDER);
        }
        detailArea.setText(builder.toString());
    }

    /**
     * A method called to format and display a list of {@link Product} object in the {@link #detailArea}
     * @param selected The currently selected Products we need to display
     */
    private void DisplayProductDetail(final List<Product> selected)
    {
        //Generate format by column
        String[] headers = {"ID", "NAME", "PRICE", "QUANTITY"};
        String format = GenerateProductStringFormat(selected, headers);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(format, (Object[])headers));

        for (Product product : selected)
        {
            String[] colProd = ExtractProductDataAsColumn(product);
            builder.append(String.format(format, (Object[])colProd));
        }
        detailArea.setText(builder.toString());
    }

    /**
     * A method specifically designed to format in String the content of a List of {@link Product}
     * for the {@link #detailArea}
     * @param selected The Products to format
     * @param headers A header to place first
     * @return A unique String containing the result of the concatenation of all the data
     */
    private String GenerateProductStringFormat(final List<Product> selected, final String[] headers)
    {
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++)
        {
            columnWidths[i] = headers[i].length();
        }

        for (Product product : selected)
        {
            String[] colProd = ExtractProductDataAsColumn(product);
            for (int i = 0; i < headers.length ; ++i)
            {
                columnWidths[i] = Math.max(columnWidths[i], colProd[i].length());
            }
        }

        StringBuilder formatBuilder =  new StringBuilder();
        for (int i = 0 ; i < columnWidths.length ; ++i)
        {
            //+2 to get some space between columns
            formatBuilder.append("%-").append(columnWidths[i] + 2).append("s");
            if (i < columnWidths.length - 1)
            {
                formatBuilder.append(" ");
            }
        }
        formatBuilder.append("%n");
        return formatBuilder.toString();
    }

    /**
     * Call to format the data of one {@link Product} in column
     * @param product The targeted Product
     * @return An array containing an entry for each column
     */
    private String[] ExtractProductDataAsColumn(final Product product)
    {
        return new String[]
                       {
                               product.getId().id(),
                               product.getName(),
                               String.format("%.2f€", product.getPrice()),
                               String.valueOf(product.getStoredQuantity())
                       };
    }

    /**
     * Update the visibility of {@link #buttonCreateNewItem} and {@link #buttonRemoveItem} buttons and force the interface to be updated accordingly
     * in space and in drawing
     * @param newVisibilityLevel True if the button need to be visible, false otherwise
     */
    private void SetButtonAddAndRemoveItemVisibility(final boolean newVisibilityLevel)
    {
        if (buttonCreateNewItem != null && buttonCreateNewItem.isVisible() != newVisibilityLevel)
        {
            buttonCreateNewItem.setVisible(newVisibilityLevel);
        }

        if (buttonRemoveItem != null && buttonRemoveItem.isVisible() != newVisibilityLevel)
        {
            buttonRemoveItem.setVisible(newVisibilityLevel);
        }

        buttonCreateNewItem.getParent().revalidate(); //Recompute the size of the parent
        buttonCreateNewItem.getParent().repaint(); //Redraw the parent
    }
    //endregion PRIVATE_METHODS
}
