package fr.cda.view.swing;

public class SwingViewConfig
{
    private String title;
    private int width;
    private int height;

    public SwingViewConfig(final String title, final int width, final int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public static final SwingViewConfig DEFAULT;

    static
    {
        DEFAULT = new SwingViewConfig("defaultName", 1066, 600);
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
