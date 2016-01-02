package ch.heigvd.flat5.api.video;
import com.google.gson.JsonObject;

public class MovieInfos
{
    private String title = "";
    private String year = "";
    private String releaseDate = "";
    private String runtime = "";
    private String genre = "";
    private String plot = "";
    private String imdbRating = "";
    private String imdbVotes = "";
    private String type = "";
    private String metaScore = "";
    private String imdbID = "";
    private String poster = "";
    private String path = "";
    private int dbID = -1;

    public MovieInfos() {}

    public MovieInfos(String title, String year, String releaseDate, String runtime, String genre, String plot,
                      String imdbRating, String imdbVotes, String type, String metaScore, String imdbID, String poster,
                      String path, int dbID)
    {
        this.title = title;
        this.year = year;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.genre = genre;
        this.plot = plot;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.type = type;
        this.metaScore = metaScore;
        this.imdbID = imdbID;
        this.poster = poster;
        this.path = path;
        this.dbID = dbID;
    }

    public MovieInfos (JsonObject movieObject)
    {
        title = movieObject.get("Title").getAsString();
        year = movieObject.get("Year").getAsString();
        imdbID = movieObject.get("imdbID").getAsString();
        poster = movieObject.get("Poster").getAsString();
        releaseDate = movieObject.get("Released").getAsString();
        runtime = movieObject.get("Runtime").getAsString();
        genre = movieObject.get("Genre").getAsString();
        type = movieObject.get("Type").getAsString();
        plot = movieObject.get("Plot").getAsString();
        imdbRating = movieObject.get("imdbRating").getAsString();
        imdbVotes = movieObject.get("imdbVotes").getAsString();
        metaScore = movieObject.get("Metascore").getAsString();
    }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

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

    public String getImdbID () { return imdbID; }

    public void setImdbID (String imdbID)
    {
        this.imdbID = imdbID;
    }

    public String getPoster ()
    {
        return poster;
    }

    public void setPoster (String poster)
    {
        this.poster = poster;
    }

    public String getReleaseDate ()
    {
        return releaseDate;
    }

    public void setReleaseDate (String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getRuntime ()
    {
        return runtime;
    }

    public int getDbID() { return dbID; }

    public void setDbID(int dbID) { this.dbID = dbID; }

    public void setRuntime (String runtime)
    {
        this.runtime = runtime;
    }

    public String getGenre ()
    {
        return genre;
    }

    public void setGenre (String genre)
    {
        this.genre = genre;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getPlot ()
    {
        return plot;
    }

    public void setPlot (String plot)
    {
        this.plot = plot;
    }

    public String getImdbRating ()
    {
        return imdbRating;
    }

    public void setImdbRating (String imdbRating)
    {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes () { return imdbVotes; }

    public void setImdbVotes (String imdbVotes)
    {
        this.imdbVotes = imdbVotes;
    }

    public String getMetaScore ()
    {
        return metaScore;
    }

    public void setMetaScore (String metaScore)
    {
        this.metaScore = metaScore;
    }
}
