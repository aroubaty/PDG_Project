package ch.heigvd.flat5.api.video;

import com.google.gson.JsonObject;

public class SearchResult
{
    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String poster;

    public SearchResult() { }

    public SearchResult (String title, String year, String imdbID, String type, String poster)
    {
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
    }

    public SearchResult (JsonObject movieObject)
    {
        title = movieObject.get("Title").getAsString();
        year = movieObject.get("Year").getAsString();
        imdbID = movieObject.get("imdbID").getAsString();
        type = movieObject.get("Type").getAsString();
        poster = movieObject.get("Poster").getAsString();
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getYear ()
    {
        return year;
    }

    public void setYear (String year)
    {
        this.year = year;
    }

    public String getImdbID ()
    {
        return imdbID;
    }

    public void setImdbID (String imdbID)
    {
        this.imdbID = imdbID;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getPoster ()
    {
        return poster;
    }

    public void setPoster (String poster)
    {
        this.poster = poster;
    }

    public String toString()
    {
        return "Title : " + title + "\nYear : " + year + "\nType : " + type;
    }

}
