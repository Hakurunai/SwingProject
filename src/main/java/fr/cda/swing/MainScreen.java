package fr.cda.swing;

import javax.swing.*;

public abstract class MainScreen implements ISwingInterface
{
    protected JFrame frame;

    public MainScreen(final String title, final int width, final int height, final boolean autoShow)
    {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Init();

        if (autoShow)
        {
            Display();
        }
    }

    public abstract void Init();

    public void Display() {frame.setVisible(true);}

    public void Hide() {frame.setVisible(false);}
}
