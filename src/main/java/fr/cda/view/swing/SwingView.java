package fr.cda.view.swing;

import fr.cda.controller.GUIController;

public abstract class SwingView implements ISwingInterface
{
    protected SwingViewConfig config;
    protected GUIController controller;

    public SwingView(final SwingViewConfig config,final GUIController controller)
    {
        this.config = config;
        this.controller = controller;
    }

    protected abstract void Init();

    /**
     * Generic method to close the window
     */
    public void Close()
    {
        InternalClose();
    }

    /**
     * Internal method to implement, called by {@link #Close()}
     */
    protected abstract void InternalClose();
}
