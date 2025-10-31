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
import java.util.List;
import java.util.Arrays;


public final class HomeFrame extends SwingFrame
{
    private JTextArea infoArea;
    private JTextArea detailArea;

    private DefaultListModel<Object> dataList;
    private JList<Object> dataListView;

    private JButton buttonShowStorage;
    private JButton buttonShowOrder;
    private JButton buttonCreateNewItem;
    private JButton buttonMakeDeliveries;
    private JButton buttonComputeProfit;
    private JButton buttonSendMailData;
    private JButton buttonGenerateBackUp;

    private static final int PANEL_BORDER_SIZE = 10;

    IEventListener<DatabaseConnectionEvent> databaseConnectionEventListener = this::OnDatabaseConnectionEvent;
    IEventListener<DatabaseOperationEndedEvent> databaseOperationEndedEventListener = this::OnDatabaseOperationEnded;

    public HomeFrame(SwingViewConfig config, GUIController controller)
    {
        super(config, controller);

        controller.eventBus.Subscribe(DatabaseConnectionEvent.class, databaseConnectionEventListener);
        controller.eventBus.Subscribe(DatabaseOperationEndedEvent.class, databaseOperationEndedEventListener);
    }

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

    private void OpenCreateNewOrderDialog()
    {
        //todo
    }

    private void OpenCreateNewProductDialog()
    {
        SwingViewConfig config = new SwingViewConfig("Add new Product", 600, 400);
        CreateProductDialog createProductDialog = new CreateProductDialog(config, controller, frame, this::OnNewProductCreated);
        createProductDialog.Display();
    }

    private void OnNewProductCreated( OperationResult<Product> result)
    {
        if (result.HasSucceeded())
        {
            dataList.addElement(result.getData());
        }
    }

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

    private void ShowMessage(String message)
    {
        infoArea.append(message + "\n");
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
        buttonCreateNewItem.setToolTipText("Create New Item");
        buttonCreateNewItem.setVisible(false);
        buttonCreateNewItem.addActionListener(this::OnAddItemButton);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(buttonCreateNewItem, BorderLayout.NORTH);

        dataList = new DefaultListModel<>();
        dataListView = new JList<>(dataList);
        dataListView.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dataListView.setCellRenderer(new ProductOrderListCellRenderer());
        dataListView.addListSelectionListener(this::OnDataListSelectionChanged);

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

        buttonShowOrder = new JButton("Show all order");
        buttonShowOrder.addActionListener(e ->controller.ReadAllOrder(this::ReadAllOrderCallback));

        buttonMakeDeliveries = new JButton("Make deliveries");
        buttonMakeDeliveries.addActionListener(e -> controller.MakeAllDeliveries(this::ReadAllOrderCallback));

        buttonComputeProfit = new JButton("Compute profit");
        buttonSendMailData = new JButton("Send data to mail");
        buttonGenerateBackUp = new JButton("Generate back up");

        //Todo: Add listener to the buttons

        JButton[] buttonArray =
                { buttonShowStorage, buttonShowOrder, buttonMakeDeliveries,
                  buttonComputeProfit, buttonSendMailData, buttonGenerateBackUp };

        SwingHelper.SetSameSize(buttonArray);
        SwingHelper.AddComponentToPanelWithStruts(panelButtons, buttonArray, BUTTON_HORIZONTAL_STRUT, BUTTON_VERTICAL_STRUT);

        return panelButtons;
    }

    private void ReadAllProductCallback(final OperationResult<Product[]> result)
    {
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        DisplayDataListView(result.getData());
    }

    private void ReadAllOrderCallback(final OperationResult<Order[]> result)
    {
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        DisplayDataListView(result.getData());
    }

    private <T> void DisplayDataListView(T[] result)
    {
        dataList.clear();
        dataList.ensureCapacity(result.length);
        dataList.addAll(Arrays.asList(result));
        dataListView.setSelectionInterval(0, dataList.getSize() - 1);

        SetAddItemButtonVisibility(dataList.getSize() > 0);
    }

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

    private void SetAddItemButtonVisibility(final boolean newVisibilityLevel)
    {
        if (buttonCreateNewItem != null && buttonCreateNewItem.isVisible() != newVisibilityLevel)
        {
            buttonCreateNewItem.setVisible(newVisibilityLevel);
            buttonCreateNewItem.getParent().revalidate(); //Recompute the size of the parent
            buttonCreateNewItem.getParent().repaint(); //Redraw the parent
        }
    }

    private void OnDatabaseConnectionEvent(final DatabaseConnectionEvent databaseConnectionEvent)
    {
        ShowMessage(databaseConnectionEvent.message());
    }

    private void OnDatabaseOperationEnded(final DatabaseOperationEndedEvent event)
    {
        ShowMessage(event.message());
    }

}
