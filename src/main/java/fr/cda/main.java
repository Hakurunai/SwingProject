package fr.cda;

import fr.cda.view.MainApp;

import javax.swing.*;

public class main
{
    public final static boolean RUN_APP = true;

    public static final String MAIN_APP_NAME = "MyName";
    public static final int MAIN_APP_WIDTH = 720;
    public static final int MAIN_APP_HEIGHT = 400;

    public static void RunApp()
    {
        SwingUtilities.invokeLater(() ->
        {
            MainApp mainApp = new MainApp(MAIN_APP_NAME, MAIN_APP_WIDTH, MAIN_APP_HEIGHT, true);
//            // Fenêtre principale
//            JFrame frame = new JFrame("Exemple JPanel imbriqués");
//            frame.setSize(600, 400);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            // Panneau principal
//            JPanel panelPrincipal = new JPanel(new BorderLayout());
//
//            // Panneau supérieur avec boutons
//            JPanel panelBoutons = new JPanel();
//            JButton btnAjouter = new JButton("Ajouter");
//            JButton btnSupprimer = new JButton("Supprimer");
//            panelBoutons.add(btnAjouter);
//            panelBoutons.add(btnSupprimer);
//            panelPrincipal.add(panelBoutons, BorderLayout.NORTH);
//
//            // Liste des produits
//            JList<String> listeProduits = new JList<>(new String[]{"Produit 1", "Produit 2", "Produit 3"});
//            JScrollPane scrollListe = new JScrollPane(listeProduits);
//
//            // Détails du produit
//            JTextArea detailsProduit = new JTextArea("Détails du produit...");
//            JScrollPane scrollDetails = new JScrollPane(detailsProduit);
//
//            // SplitPane entre liste et détails
//            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollListe, scrollDetails);
//            splitPane.setDividerLocation(200);
//
//            // Panneau central qui contient le SplitPane
//            JPanel panelCentral = new JPanel(new BorderLayout());
//            panelCentral.add(splitPane, BorderLayout.CENTER);
//
//            panelPrincipal.add(panelCentral, BorderLayout.CENTER);
//
//
//            // Ajouter le panel principal à la fenêtre
//            frame.add(panelPrincipal);
//            frame.setVisible(true);
        });
    }

    public static void QuickTest()
    {

    }

    public static void main(String[] args)
    {
        if (RUN_APP == true)
            RunApp();
        else
            QuickTest();
    }
}
