package fr.cda.view;

import fr.cda.model.Product;

import javax.swing.*;

/**
 * Represent a line when we work with a {@link OrderDialog}
 */
public class OrderLinePanel
{
    final private JPanel panel;
    final private JComboBox<Product> productCombo;
    final private JTextField quantityField;

    public OrderLinePanel(JPanel panel, JComboBox<Product> productCombo, JTextField quantityField)
    {
        this.panel = panel;
        this.productCombo = productCombo;
        this.quantityField = quantityField;
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public JComboBox<Product> getProductCombo()
    {
        return productCombo;
    }

    public JTextField getQuantityField()
    {
        return quantityField;
    }
}
