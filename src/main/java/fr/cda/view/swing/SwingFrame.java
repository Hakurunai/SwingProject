package fr.cda.view.swing;

import fr.cda.controller.GUIController;
import fr.cda.util.LoggerHelper;

import javax.swing.*;

public abstract class SwingFrame extends SwingView
{
    protected JFrame frame;

    public SwingFrame(SwingViewConfig config, GUIController controller, boolean autoShow)
    {
        super(config, controller);
        LoggerHelper.log.info("Creation of a new MainScreen named : {}", config.getTitle());

        frame = new JFrame(config.getTitle());
        frame.setSize(config.getWidth(), config.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Init();
        AutoShow(autoShow);
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
