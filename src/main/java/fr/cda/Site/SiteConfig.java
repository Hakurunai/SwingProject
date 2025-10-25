package fr.cda.Site;

public class SiteConfig
{
    private String siteName;
    private int appWidth;
    private int appHeight;

    public static final SiteConfig DEFAULT;

    static
    {
        SiteConfig conf = new SiteConfig();
        conf.siteName = "defaultName";
        conf.appWidth = 720;
        conf.appHeight = 400;
        DEFAULT = conf;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String p_siteName)
    {
        siteName = p_siteName;
    }

    public int getAppWidth()
    {
        return appWidth;
    }

    public void setAppWidth(int p_appWidth)
    {
        appWidth = p_appWidth;
    }

    public int getAppHeight()
    {
        return appHeight;
    }

    public void setAppHeight(int p_appHeight)
    {
        appHeight = p_appHeight;
    }
}
