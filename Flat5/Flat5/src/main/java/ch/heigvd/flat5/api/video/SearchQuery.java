package ch.heigvd.flat5.api.video;

import java.net.URLEncoder;

public class SearchQuery
{
    private String charset;
    private String title = "";
    private String year = "";
    private String type = "";
    private String dataType = "r=json";
    private String version = "v=1";

    public SearchQuery()
    {
        charset = java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public SearchQuery(String charset)
    {
        this.charset = charset;
    }

    public String getQuery()
    {
        return String.join("&", title, year, type, dataType, version);
    }

    public void setTitle(String title)
    {
        this.title = "s=" + URLEncoder.encode(title, charset));
    }

    public void setYear(String year)
    {
        this.year = "y=" + URLEncoder.encode(year, charset);
    }

    public void setType(String type)
    {
        this.type = "t=" + URLEncoder.encode(type,charset);
    }
}
