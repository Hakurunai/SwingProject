package fr.cda.view.swing;

import fr.cda.controller.GUIController;

/**
 * Mother class implementing {@link ISwingInterface} and common to every custom swing interface in the app
 */
public abstract class SwingView implements ISwingInterface
{
    protected SwingViewConfig config;
    protected GUIController controller;

    /**
     * @param config A configuration object who can be used to do some set up
     * @param controller The controller linked to this object
     */
    public SwingView(final SwingViewConfig config,final GUIController controller)
    {
        this.config = config;
        this.controller = controller;
    }

    /**
     * A method designed to be called to generate the GUI of the object
     */
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
