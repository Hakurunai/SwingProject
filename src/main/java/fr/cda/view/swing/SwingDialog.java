package fr.cda.view.swing;

import fr.cda.controller.GUIController;
import fr.cda.util.LoggerHelper;

import javax.swing.*;

/**
 * Parent class owning internally a {@link JDialog}
 * Automatically {@link #Init()} method at the end of his construction
 * Analog at {@link SwingFrame}
 */
public abstract class SwingDialog extends SwingView
{
    protected JDialog dialog;

    /**
     * @param config     The initial configuration of the {@link JDialog}
     * @param controller Injection of the {@link GUIController}
     * @param frameOwner The {@link JFrame} owning the internal {@link JDialog}
     * @param isModal    Use to determine if the {@link JDialog} will be modal
     */
    public SwingDialog(SwingViewConfig config, GUIController controller, JFrame frameOwner,
                       boolean isModal)
    {
        super(config, controller);
        LoggerHelper.log.info("Creation of a new Dialog named : {}", config.getTitle());


        dialog = new JDialog(frameOwner, config.getTitle(), isModal);
        dialog.setSize(config.getWidth(), config.getHeight());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(dialog.getOwner());

        Init();
    }

    /**
     * A method called internally in the constructor to generate the interface
     */
    protected abstract void Init();

    /**
     * Implementation allowing us to properly close the internal {@link JDialog}
     */
    @Override
    protected void InternalClose()
    {
        if (dialog != null)
        {
            LoggerHelper.log.info("Closing Dialog named : {}", dialog.getTitle());
            dialog.dispose();
        }
    }

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
