package fr.cda.util;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class SwingHelper
{
    /**
     * Determine the max size of an array of JButton and set it to all the buttons of this array
     * @param components that we want to normalize the size
     */
    public static void SetSameSize(JComponent[] components)
    {
        LoggerHelper.log.info("START redimension of an array of JComponent");
        Dimension commonDim = DetermineMaxSize(components);

        //The default font is proportional, so we raise the estimated width here before the real display
        //So we can avoid having a truncated text like that : "Generate...", for less than 1 pixel of error
        commonDim.width += 1;
        for (JComponent component : components)
        {
            component.setPreferredSize(commonDim);
            component.setMaximumSize(commonDim);
            component.setMinimumSize(commonDim);
        }
    }

    /**
     * Can be used to determine the max Dimension from an array of JComponent
     * @param components an array of components from who we want to determine the max dimension
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
     * @param targetPanel the panel who will own the components
     * @param components the components who will be added to the target
     * @param horizontalStrut value applied for the horizontalStrut
     * @param verticalStrut value applied for the verticalStrut
     */
    public static void AddComponentToPanelWithStruts(JPanel targetPanel, JComponent[] components, final int horizontalStrut, final int verticalStrut)
    {
        LoggerHelper.log.info("START adding an array of JComponent to a JPanel");
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
     *
     * @return an empty non-editable and non-focusable JTextArea
     */
    public static JTextArea CreateNonEditableTextArea()
    {
        LoggerHelper.log.info("START creation of a non editable text area");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        return textArea;
    }
}
