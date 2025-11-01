package fr.cda.view.cellRenderer;

import fr.cda.model.Product;

import javax.swing.*;
import java.awt.*;

/**
 * Use to specialize the display of {@link Product} inside a {@link JComboBox}
 */
public class ProductComboBoxCellRenderer implements ListCellRenderer<Product>
{
    private final JLabel label = new JLabel();

    /**
     * Called method when a cell need to render something in a JComboBox
     * @param list The JList owning the item to render (from a JComboBox)
     * @param value The Object to render
     * @param index The index of the targeted object to render inside the JList
     * @param isSelected Indicate if the targeted cell is selected
     * @param cellHasFocus Indicate if the targeted cell has the focus
     * @return A Component used to display {@link Product} inside a JComboBox
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list, Product value,
                                                  int index, boolean isSelected, boolean cellHasFocus)
    {
        if (value != null)
        {
            label.setText(value.getId().id() + " : " + value.getName());
        } else
        {
            label.setText("");
        }

        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        label.setOpaque(true);
        return label;
    }
}
