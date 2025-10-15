package fr.cda.util;

import javax.swing.*;
import java.awt.*;

public class SwingHelper
{
    /**
     * Determine the max size of an array of JButton and set it to all of the buttons of this array
     * @param components that we want to normalize the size
     */
    public static void SetSameSize(JComponent[] components)
    {
        Dimension commonDim = DetermineMaxSize(components);

        for (JComponent component : components)
        {
            component.setPreferredSize(commonDim);
            component.setMaximumSize(commonDim);
        }
    }

    /**
     * Can be used to determine the max size of an array of JComponent
     * @param components
     * @return the Dimension with max value from the provided array, 0,0 if the array is empty
     */
    public static Dimension DetermineMaxSize(JComponent[] components)
    {
        Dimension result = new Dimension(0, 0);
        for (JComponent component : components)
        {
            Dimension buttonPreferredSize = component.getPreferredSize();
            if (buttonPreferredSize.getWidth() > result.getWidth())
                result.width = buttonPreferredSize.width;

            if (buttonPreferredSize.getHeight() > result.getHeight())
                result.height = buttonPreferredSize.height;
        }

        return result;
    }


    /**
     * Is used to add an array of component with a specified strut to space them
     * A 0 size param for a specific strut result in no strut added at all to the JPanel
     * @param targetPanel
     * @param components
     * @param horizontalStrut
     * @param verticalStrut
     */
    public static void AddComponentToPanelWithStruts(JPanel targetPanel, JComponent[] components, final int horizontalStrut, final int verticalStrut)
    {
        for (int i = 0 ; i < components.length; ++i)
        {
            targetPanel.add(components[i]);

            //We only want to add strut if there is another button to add to the panel
            //If we had a verticalStrut, even with size 0, at the end, all buttons will be pushed down in the UI
            if (i < components.length - 1)
            {
                //Strange thing : if a 0 size HorizontalStrut is added before a vertical strut,
                //the component can be pushed to the bottom of the panel
                if (horizontalStrut > 0)
                    targetPanel.add(Box.createHorizontalStrut(horizontalStrut));

                if (verticalStrut > 0)
                    targetPanel.add(Box.createVerticalStrut(verticalStrut));
            }
        }
    }

    /**
     * Allow to set a same border value for each side of a JPanel
     * @param target
     * @param border the size in pixel we want to set the border
     */
    public static void SetPanelAllBorder(JPanel target, final int border)
    {
        target.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
    }
}
