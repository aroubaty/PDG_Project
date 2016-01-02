package ch.heigvd.flat5.api.video;

import java.net.URLEncoder;

public class SearchQuery
{
    private String charset;
    private String title = "";
    private String year = "";
    private String type = "";
    private String imdbID = "";
    private String season = "";
    private String dataType = "&r=json";
    private String version = "&v=1";

    public SearchQuery()
    {
        charset = java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public SearchQuery(String charset)
    {
        this.charset = charset;
    }

    public String getSearchQuery()
    {
        return String.join("&", title, year, type, dataType, version);
    }

    public String getSearchByIdQuery()
    {
        return String.join("&", imdbID, season, dataType, version);
    }

    public String getCharset()
    {
        return charset;
    }

    public void setImdbID(String imdbID)
    {
        try{this.imdbID = "i=" + URLEncoder.encode(imdbID, charset);}
        catch(Exception e){}
    }

    public void setTitle(String title)
    {
        try{this.title = "t=" + URLEncoder.encode(title, charset);}
        catch(Exception e){}
    }

    public void setYear(String year)
    {
        try{this.year = "y=" + URLEncoder.encode(year, charset);}
        catch(Exception e){}
    }

    public void setType(String type)
    {
        try{this.type = "type=" + URLEncoder.encode(type,charset);}
        catch(Exception e){}
    }

    public void setSeason(String season)
    {
        try{this.season = "Season=" + URLEncoder.encode(season,charset);}
        catch(Exception e){}
    }
}
