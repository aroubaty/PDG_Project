package ch.heigvd.flat5.api.video;

import java.net.URLEncoder;

/**
 * Classe représentant une requête pour l'API de OMDb (http://omdbapi.com/).
 * Toute les requêtes s'effectuent sur la v1 de l'api et demande du JSON comme type de retour.
 * Une requête contient plusieurs champs possibles :
 *
 *  - title : le titre du média.
 *  - year : l'année de publication du média.
 *  - imdbID : l'id du média sur IMDb.
 *  - type : le type du média ("movie" ou "series").
 *  - season : le numéro de la saison d'une série.
 *
 *  Tous ces champs sont optionnels et peuvent être modifiés. Par défaut ces champs sont vides, et ne seront donc
 *  pas pris en compte lors de leur recherche.
 *
 *  La requêtes peut être obtenues sous  deux formes :
 *      - getSearchQuery : fait une recherche en utilisant le titre, l'année et le type.
 *      - getSearchByIdQuery : faite une recherche utilisant l'id et la saison.
 *
 * @author Jan Purro
 */
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

    /**
     * Construit une nouvelle requête avec le charset par défaut (UTF-8).
     */
    public SearchQuery()
    {
        charset = java.nio.charset.StandardCharsets.UTF_8.name();
    }

    /**
     * Construit une nouvelle requête avec le charset passé en paramètre.
     * @param charset Le charset de la requête.
     */
    public SearchQuery(String charset)
    {
        this.charset = charset;
    }

    /**
     * Retourne la requête sous forme de chaîne de caractères. La requête sera une requête sur le titre, l'année,
     * et le type de média ("movie" ou "series").
     * @return La chaîne de caractères correspondant à la requête.
     */
    public String getSearchQuery()
    {
        return String.join("&", title, year, type, dataType, version);
    }

    /**
     * Retourne la requête sous forme de chaîne de caractères. La requête sera une requête sur l'id, et la saison.
     * Si le champ saison est vide, il s'agit d'une requête pour le média correspondant à l'id. Si une saison est
     * précisée, il s'agit d'une requête pour cette saison de la série correspondant à l'id.
     * @return La chaîne de caractères correspondant à la requête.
     */
    public String getSearchByIdQuery()
    {
        return String.join("&", imdbID, season, dataType, version);
    }

    /**
     * Retourne le charset de la requête.
     * @return Le charset de la requête.
     */
    public String getCharset()
    {
        return charset;
    }

    // Méthodes permettant de modifier les champs de la requête.
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
