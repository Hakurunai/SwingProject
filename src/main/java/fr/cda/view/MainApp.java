package fr.cda.view;

import fr.cda.swing.Screen;
import fr.cda.util.SwingHelper;

import javax.swing.*;
import java.awt.*;


public class MainApp extends Screen
{
    private JTextArea infoArea;

    private JButton addButton;
    private JButton removeButton;

    private static final int PANEL_BORDER_SIZE = 10;

    public MainApp(String title, int width, int height, boolean autoShow)
    {
        super(title, width, height, autoShow);
    }

    @Override
    public void Init()
    {
        JPanel panelMain = new JPanel(new BorderLayout());

        //West panel with buttons
        JPanel westPanel = CreateButtonPanel();
        panelMain.add(westPanel, BorderLayout.WEST);

        JPanel centerPanel = CreateCenterPanel();
        panelMain.add(centerPanel, BorderLayout.CENTER);

        frame.add(panelMain);
    }

    private JPanel CreateCenterPanel()
    {
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

    private JPanel CreateInfoArea()
    {
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFocusable(false);
        JScrollPane infoScroll = new JScrollPane(infoArea);

        JPanel mainPanel  = new JPanel(new BorderLayout());
        mainPanel.add(infoScroll, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel CreateListAndDescriptionPanel()
    {
        //Todo : replace demo code
        //Todo: ===========================
        //Liste des produits
        JList<String> listeProduits = new JList<>(new String[]{"Produit 1", "Produit 2", "Produit 3"});
        JScrollPane scrollListe = new JScrollPane(listeProduits);

        // Détails du produit
        JTextArea detailsProduit = new JTextArea("Détails du produit...");
        detailsProduit.setEditable(false);
        detailsProduit.setFocusable(false);
        JScrollPane scrollDetails = new JScrollPane(detailsProduit);
        //Todo: ===========================


        final int DIVIDER_LOCATION_PARAM = 100;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollListe, scrollDetails);
        splitPane.setDividerLocation(DIVIDER_LOCATION_PARAM);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Used to create the left pannel containing some buttons
     * References to those buttons will be kept inside this class
     * @return a JPanel with the buttons
     */
    private JPanel CreateButtonPanel()
    {
        final int BUTTON_HORIZONTAL_STRUT = 0;
        final int BUTTON_VERTICAL_STRUT = 10;

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));

        addButton = new JButton("Ajouter");
        removeButton = new JButton("Supprimer");

        //Todo: Add listener to the buttons

        JButton[] buttonArray = {addButton, removeButton};

        SwingHelper.SetSameSize(buttonArray);
        SwingHelper.AddComponentToPanelWithStruts(panelButtons, buttonArray, BUTTON_HORIZONTAL_STRUT, BUTTON_VERTICAL_STRUT);

        return panelButtons;
    }
}
