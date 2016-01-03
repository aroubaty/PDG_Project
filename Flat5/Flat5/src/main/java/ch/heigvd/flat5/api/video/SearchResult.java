package ch.heigvd.flat5.api.video;

import com.google.gson.JsonObject;

/**
 * Classe représentant le résultat d'une recherche sur OMdb.
 * Une recherche s'effectue sur le titre d'un film et retourne un tableau contenant des informations basiques sur les
 * films ou séries correspondant.
 *
 * @author Jan Purro
 */
public class SearchResult
{
    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String poster;

    /**
     * Construit un nouvel objet à partir d'un objet JSON.
     * Si certains champs sont absents de l'objet JSON, ils vaudront null.
     * @param movieObject : L'objet JSON contenant les informations.
     */
    public SearchResult (JsonObject movieObject)
    {
        title = movieObject.get("Title").getAsString();
        year = movieObject.get("Year").getAsString();
        imdbID = movieObject.get("imdbID").getAsString();
        type = movieObject.get("Type").getAsString();
        poster = movieObject.get("Poster").getAsString();
    }

    // Méthodes permettant d'obtenir ou modifier la valeur des champs de l'objet.

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

}
