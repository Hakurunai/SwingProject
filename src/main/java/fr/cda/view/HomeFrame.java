package fr.cda.view;

import fr.cda.controller.DatabaseConnectionEvent;
import fr.cda.controller.GUIController;
import fr.cda.event.IEventListener;
import fr.cda.model.OperationResult;
import fr.cda.model.Order;
import fr.cda.model.Product;
import fr.cda.view.cellRenderer.ProductOrderListCellRenderer;
import fr.cda.view.swing.SwingFrame;
import fr.cda.util.LoggerHelper;
import fr.cda.util.SwingHelper;
import fr.cda.view.swing.SwingViewConfig;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

import java.util.Arrays;


public final class HomeFrame extends SwingFrame
{
    private JTextArea infoArea;
    private JTextArea productDetailArea;

    private DefaultListModel<Object> dataList;

    private JButton buttonShowStorage;
    private JButton buttonShowOrder;
    private JButton buttonShowSpecificOrder;
    private JButton buttonMakeDeliveries;
    private JButton buttonComputeProfit;
    private JButton buttonSendMailData;
    private JButton buttonGenerateBackUp;

    private static final int PANEL_BORDER_SIZE = 10;

    IEventListener<DatabaseConnectionEvent> databaseConnectionEventListener =res ->
    {
        ShowMessage(res.message());
    };

    public HomeFrame(SwingViewConfig config, GUIController controller, boolean autoShow)
    {
        super(config, controller, autoShow);

        controller.eventBus.Subscribe(DatabaseConnectionEvent.class, databaseConnectionEventListener);
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
        LoggerHelper.log.info("START Initialisation of the Main App named : {}", frame.getTitle());

        JPanel northPanel = CreateListAndDescriptionPanel();

        JPanel southPanel = CreateInfoArea();

        final int DIVIDER_LOCATION_PARAM = 150;
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
        dataList = new DefaultListModel<>();
        JList<Object> listProducts = new JList<>(dataList);
        listProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listProducts.setCellRenderer(new ProductOrderListCellRenderer());

        JScrollPane scrollList = new JScrollPane(listProducts);

        // Détails du produit
        productDetailArea = SwingHelper.CreateNonEditableTextArea();
        JScrollPane scrollDetails = new JScrollPane(productDetailArea);


        final int DIVIDER_LOCATION_PARAM = 100;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollList, scrollDetails);
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

        buttonShowSpecificOrder = new JButton("Show order");
        buttonMakeDeliveries = new JButton("Make deliveries");
        buttonComputeProfit = new JButton("Compute profit");
        buttonSendMailData = new JButton("Send data to mail");
        buttonGenerateBackUp = new JButton("Generate back up");

        //Todo: Add listener to the buttons

        JButton[] buttonArray =
                { buttonShowStorage, buttonShowOrder, buttonShowSpecificOrder,
                buttonMakeDeliveries, buttonComputeProfit, buttonSendMailData,
                buttonGenerateBackUp };

        SwingHelper.SetSameSize(buttonArray);
        SwingHelper.AddComponentToPanelWithStruts(panelButtons, buttonArray, BUTTON_HORIZONTAL_STRUT, BUTTON_VERTICAL_STRUT);

        return panelButtons;
    }

    private void ReadAllProductCallback(final OperationResult<Product[]> result)
    {
        ShowMessage(result.getMessage());
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        dataList.clear();
        dataList.addAll(Arrays.asList(result.getData()));
    }

    private void ReadAllOrderCallback(final OperationResult<Order[]> result)
    {
        ShowMessage(result.getMessage());
        if (!result.HasSucceeded() || result.getData().length == 0)
            return;

        dataList.clear();
        dataList.addAll(Arrays.asList(result.getData()));
    }
}
