package fr.cda.swing;

import fr.cda.util.LoggerHelper;

import javax.swing.*;

public abstract class Screen implements ISwingInterface
{
    protected JFrame frame;

    /**
     * Constructor who will be inherited by all child classes
     * @param title the name of the window
     * @param width the size in pixel for the width
     * @param height the size in pixel for the height
     * @param autoShow if true, the constructor will automatically call Display
     */
    public Screen(final String title, final int width, final int height, final boolean autoShow)
    {
        LoggerHelper.log.info("Creation of a new MainScreen named : {}", title);

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Init();

        if (autoShow)
        {
            Display();
        }
    }

    /**
     * A method called internally in the constructor to generate the interface
     */
    protected abstract void Init();

    /**
     * Implementation of ISwingInterface used to show the interface on screen
     */
    public void Display()
    {
        LoggerHelper.log.info("TRY to set visible Screen named : {}", frame.getTitle());
        frame.setVisible(true);
    }

    /**
     * Implementation of ISwingInterface used to hide the interface
     */
    public void Hide()
    {
        LoggerHelper.log.info("TRY to hide Screen named : {}", frame.getTitle());
        frame.setVisible(false);
    }
}
