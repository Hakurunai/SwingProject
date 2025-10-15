package fr.cda.view;

import fr.cda.swing.Screen;
import fr.cda.util.SwingHelper;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends Screen
{
    private List<JButton> allButtons = new ArrayList<>();

    private static final int BUTTON_HORIZONTAL_STRUT = 0;
    private static final int BUTTON_VERTICAL_STRUT = 10;

    private static final int PANEL_BORDER_SIZE = 10;

    public MainApp(String title, int width, int height, boolean autoShow)
    {
        super(title, width, height, autoShow);
    }


    @Override
    public void Init()
    {
        JPanel panelMain = new JPanel(new BorderLayout());

        //West pannel with buttons
        JPanel panelButtons = InitButtonPanel(panelMain);
        panelMain.add(panelButtons, BorderLayout.WEST);

        // Liste des produits
        JList<String> listeProduits = new JList<>(new String[]{"Produit 1", "Produit 2", "Produit 3"});
        JScrollPane scrollListe = new JScrollPane(listeProduits);

        // Détails du produit
        JTextArea detailsProduit = new JTextArea("Détails du produit...");
        JScrollPane scrollDetails = new JScrollPane(detailsProduit);

        // SplitPane entre liste et détails
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollListe, scrollDetails);
        splitPane.setDividerLocation(200);

        // Panneau central qui contient le SplitPane
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(splitPane, BorderLayout.CENTER);

        panelMain.add(panelCentral, BorderLayout.CENTER);

        frame.add(panelMain);
    }

    private static JPanel InitButtonPanel(JPanel panelMain)
    {
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE, PANEL_BORDER_SIZE));

        JButton buttonAdd = new JButton("Ajouter");
        JButton buttonDelete = new JButton("Supprimer");

        JButton[] buttonArray = {buttonAdd, buttonDelete};
        SwingHelper.SetSameSize(buttonArray);

        SwingHelper.AddComponentToPanelWithStruts(panelButtons, buttonArray, BUTTON_HORIZONTAL_STRUT, BUTTON_VERTICAL_STRUT);

        return panelButtons;
    }
}
