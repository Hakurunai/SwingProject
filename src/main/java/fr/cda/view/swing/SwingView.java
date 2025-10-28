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

    protected void AutoShow(final boolean autoShow)
    {
        if (autoShow)
            Display();
    }
}
