package fr.cda.view.swing;

public class SwingViewConfig
{
    private String title;
    private int width;
    private int height;

    public static final SwingViewConfig DEFAULT;

    static
    {
        SwingViewConfig conf = new SwingViewConfig();
        conf.title = "defaultName";
        conf.width = 1066;
        conf.height = 600;
        DEFAULT = conf;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String appTitle)
    {
        title = appTitle;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int appWidth)
    {
        width = appWidth;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int appHeight) { height = appHeight;}
}
