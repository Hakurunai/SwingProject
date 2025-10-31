package fr.cda.view.cellRenderer;

import fr.cda.model.Order;
import fr.cda.model.Product;

import javax.swing.*;
import java.awt.*;

/**
 * Use to specialize the display of {@link Product} and {@link Order} inside a JList
 */
public class ProductOrderListCellRenderer implements ListCellRenderer<Object>
{
    /**
     * Called method when a cell need to render something in a JList
     * @param list The JList owning the item to render
     * @param value The Object to render
     * @param index The index of the targeted object to render inside the JList
     * @param isSelected Indicate if the targeted cell is selected
     * @param cellHasFocus Indicate if the targeted cell has the focus
     * @return A Component used to display an {@link Order} or a {@link Product} inside a JList
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value instanceof Product)
            return ProductRenderer(list, (Product)value, isSelected);

        if (value instanceof Order)
            return OrderRenderer(list, (Order)value, isSelected);

        return null;
    }

    /**
     * Renderer specific for a {@link Product}
     * @param list The list owning the targeted {@link Product}
     * @param product The {@link Product} currently displayed
     * @param isSelected Indicate if the targeted {@link Product} is currently selected
     * @return A label formatted for a {@link Product} and colored based on the parameter isSelected
     */
    private JLabel ProductRenderer(final JList<?> list, final Product product, final boolean isSelected)
    {
        JLabel label = new JLabel(product.getId().id());
        SetBackGroundLabelColor(isSelected ? list.getSelectionBackground() : list.getBackground(), label);
        return label;
    }

    /**
     * Renderer specific for an {@link Order}
     * @param list The list owning the targeted {@link Order}
     * @param order The {@link Order} currently displayed
     * @param isSelected Indicate if the targeted {@link Order} is currently selected
     * @return A label formatted for an {@link Order} and colored based on the parameter isSelected
     */
    private JLabel OrderRenderer(final JList<?> list, final Order order, final boolean isSelected)
    {
        final String toDisplay = "Order " + order.getId().id();
        JLabel label = new JLabel(toDisplay);
        SetBackGroundLabelColor(isSelected ? list.getSelectionBackground() : list.getBackground(), label);
        return label;
    }

    /**
     * Change the color based on the selected state of the item
     * @param color The Color to set to the background of the targeted label
     * @param target The {@link JLabel} displayed
     */
    private void SetBackGroundLabelColor(final Color color, JLabel target)
    {
        target.setOpaque(true);
        target.setBackground(color);
    }
}
