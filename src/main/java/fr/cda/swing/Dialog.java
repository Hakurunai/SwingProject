package fr.cda.swing;

import fr.cda.util.LoggerHelper;

import javax.swing.*;

public abstract class Dialog implements ISwingInterface
{
    protected JDialog dialog;

    /**
     * Constructor who will be inherited by all child classes
     * @param title the name of the window
     * @param frameOwner the JFrame who own this JDialog
     * @param width the size in pixel for the width
     * @param height the size in pixel for the height
     * @param autoShow if true, the constructor will automatically call Display
     */
    protected Dialog(final String title, JFrame frameOwner, final int width, final int height, final boolean autoShow, final boolean isModal)
    {
        LoggerHelper.log.info("Creation of a new Dialog named : {}", title);

        dialog = new JDialog(frameOwner, title, isModal);
        dialog.setSize(width, height);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(dialog.getOwner());

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
        LoggerHelper.log.info("ACTION set visible Dialog named : {}", dialog.getTitle());
        dialog.setVisible(true);
    }

    /**
     * Implementation of ISwingInterface used to hide the interface
     */
    public void Hide()
    {
        LoggerHelper.log.info("ACTION hide Dialog named : {}", dialog.getTitle());
        dialog.setVisible(false);
    }
}
