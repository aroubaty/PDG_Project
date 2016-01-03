package ch.heigvd.flat5.api.video;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe permettant d'effectuer des requêtes sur l'API de OMDb (http://omdbapi.com/). Elle permet principalement de
 * former les requêtes correctement et délègue le travail à un autre objet de type OMDBClient.
 *
 * @author Jan Purro
 */
public class MovieDataGetter
{
    // Client effectuant réellement les requêtes.
    private OMDbClient client = new OMDbClient();

    /**
     * Tente de récupérer les données du film passé en paramaètre. Si plusieurs films possèdent le même titre, seules les
     * informations de l'un d'entre eux seront retournées (a priori le plus récent, mais aucune documentation n'est
     * données à ce sujet).
     * @param title Le nom du film recherché. Le nom doit être dans la langue originale du film.
     * @return Les informations du film qui ont put être obtenues (il est possible que tous les champs soient vides).
     */
    public MovieInfos searchFilm (String title)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setType("movie");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    /**
     * Tente de récupérer les données du film passé en paramaètre. Cette méthode permet de précisé l'année de sortie du film en
     * plus du titre. Ceci permet de distinguer les films ayant le même titre. Toutefois l'année doit être exacte, sinon
     * aucune information ne sera obtenue.
     * @param title Le nom du film recherché. Le nom doit être dans la langue originale du film.
     * @param year L'année de sortie du filme recherché.
     * @return Les informations du film qui ont put être obtenues (il est possible que tous les champs soient vides).
     */
    public MovieInfos searchFilm (String title, String year)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setYear(year);
        query.setType("movie");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    /**
     * Tente de récupérer les données du film passé en paramaètre.
     * @param imdbID L'id unique de IMDb, qui permet d'identifier un film. Si l'id ne correspond à aucun film, les
     *               données retournées seront vides.
     * @return Les informations du film qui ont put être obtenues (il est possible que tous les champs soient vides).
     */
    public MovieInfos searchMovieById (String imdbID)
    {
        SearchQuery query = new SearchQuery();
        query.setImdbID(imdbID);
        return client.parseMovieInfos(client.getQuery(query.getSearchByIdQuery(), query.getCharset()));
    }

    /**
     * Tente de récupérer les informations de la série dont le titre est passé en paramètre.
     * @param title Le nom de la série recherchée. Le nom doit être dans la langue originale de la série.
     * @return Les informations de la série qui ont put être obtenues (il est possible que tous les champs soient vides).
     */
    public MovieInfos searchSerie (String title)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setType("series");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    /**
     * Tente de récupérer la liste des saisons de la série dont l'id est passé en paramètre.
     * @param imdbID L'id unique de IMDb, qui permet d'identifier une série. Si l'id ne correspond à aucune série, les
     *               données retournées seront vides.
     * @return
     */
    public List<Season> searchSeasons (String imdbID)
    {
        List<Season> seasons = new LinkedList<>();
        SearchQuery query = new SearchQuery();
        query.setImdbID(imdbID);

        /* Comme il n'existe aucun moyen de récupérer le nombre de saisons d'une série, nous sommes obligés d'envoyer
           des requêtes jusqu'au moment où une saison nulle nous est retournée.
         */
        boolean continueLoop = true;
        for (int i = 1; continueLoop; i++) {
            query.setSeason(new Integer(i).toString());
            Season season = client.parseSeasonInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
            if (season == null) {continueLoop = false;}
            else { seasons.add(season);}
        }

        return seasons;
    }
}
