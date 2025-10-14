package fr.cda.swing;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Dialog implements ISwingInterface
{
    protected JDialog dialog;

    public Dialog(String title, JFrame frameOwner, final int width, final int height, final boolean autoShow)
    {
        dialog = new JDialog(frameOwner, title);
        dialog.setSize(width, height);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Init();

        if (autoShow)
        {
            Display();
        }
    }

    public abstract void Init();

    public void Display() {dialog.setVisible(true);}

    public void Hide() {dialog.setVisible(false);}
}
