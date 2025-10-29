package fr.cda.view.cellRenderer;

import fr.cda.model.Order;
import fr.cda.model.Product;

import javax.swing.*;
import java.awt.*;

public class ProductOrderListCellRenderer implements ListCellRenderer<Object>
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof Product)
            return ProductRenderer(list, (Product)value, isSelected);

        if (value instanceof Order)
            return OrderRenderer(list, (Order)value, isSelected);

        return null;
    }

    private JLabel ProductRenderer(final JList<?> list, final Product product, final boolean isSelected)
    {
        JLabel label = new JLabel(product.getId().id());
        SetLabelColor(list, isSelected, label);
        return label;
    }

    private JLabel OrderRenderer(final JList<?> list, final Order order, final boolean isSelected)
    {
        final String toDisplay = "Order " + order.getId().id();
        JLabel label = new JLabel(toDisplay);
        SetLabelColor(list, isSelected, label);
        return label;
    }

    private void SetLabelColor(final JList<?> list, final boolean isSelected, JLabel label)
    {
        label.setOpaque(true);
        if (isSelected)
        {
            label.setBackground(list.getSelectionBackground());
        }
        else
        {
            label.setBackground(list.getBackground());
        }
    }
}
