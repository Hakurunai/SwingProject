package fr.cda.view.swing;

import fr.cda.controller.GUIController;
import fr.cda.util.LoggerHelper;

import javax.swing.*;

/**
 * Parent class owning internally a {@link JFrame}
 * Automatically {@link #Init()} method at the end of his construction
 * Analog at {@link SwingDialog}
 */
public abstract class SwingFrame extends SwingView
{
    protected JFrame frame;

    public SwingFrame(SwingViewConfig config, GUIController controller)
    {
        super(config, controller);
        LoggerHelper.log.info("Creation of a new MainScreen named : {}", config.getTitle());

        frame = new JFrame(config.getTitle());
        frame.setSize(config.getWidth(), config.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Init();
    }

    /**
     * A method called internally in the constructor to generate the interface
     */
    protected abstract void Init();

    /**
     * Implementation allowing us to properly close the internal {@link JFrame}
     */
    @Override
    protected void InternalClose()
    {
        if (frame != null)
        {
            LoggerHelper.log.info("Closing Frame named : {}", frame.getTitle());
            frame.dispose();
        }
    }

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
