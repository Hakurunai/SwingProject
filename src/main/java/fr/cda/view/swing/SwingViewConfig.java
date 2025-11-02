package fr.cda.view.swing;

/**
 * His purpose his to facilitate the initialisation of any {@link SwingView} object
 */
public class SwingViewConfig
{
    private String title;
    private int width;
    private int height;

    /**
     *
     * @param title The title of the view
     * @param width The width, in pixels, of the view
     * @param height The height, in pixels, of the view
     */
    public SwingViewConfig(final String title, final int width, final int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    /**
     * Common and final configuration. Can be used as a last resort if nothing was provided
     */
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
