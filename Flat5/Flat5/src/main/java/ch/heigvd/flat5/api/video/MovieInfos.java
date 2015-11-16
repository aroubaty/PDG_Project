package ch.heigvd.flat5.api.video;
import com.google.gson.JsonObject;

public class MovieInfos extends SearchResult
{
    private String releaseDate;
    private String runtime;
    private String genre;
    private String plot;
    private String imdbRating;
    private String imdbVotes;
    private String metaScore;


    public MovieInfos () { super(); }

    public MovieInfos (JsonObject movieObject)
    {
        super(
        movieObject.get("Title").getAsString(), movieObject.get("Year").getAsString(), movieObject.get("imdbID").getAsString(), movieObject.get("Type").getAsString(), movieObject.get("Poster").getAsString());

        releaseDate = movieObject.get("Released").getAsString();
        runtime = movieObject.get("Runtime").getAsString();
        genre = movieObject.get("Genre").getAsString();
        plot = movieObject.get("Plot").getAsString();
        imdbRating = movieObject.get("imdbRating").getAsString();
        imdbVotes = movieObject.get("imdbVotes").getAsString();
        metaScore = movieObject.get("Metascore").getAsString();
    }

    public String toString()
    {
        return "Title " + getTitle() + "\nRelease Date : " + releaseDate + "\nRuntime " + runtime + "\nPlot : " + plot + "\nMetascore : " + metaScore;
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

    public String getVotes ()
    {
        return imdbVotes;
    }

    public void setVotes (String imdbVotes)
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
