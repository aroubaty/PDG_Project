package ch.heigvd.flat5.api.video;

import java.util.LinkedList;
import java.util.List;

public class MovieDataGetter
{
    private OMDbClient client = new OMDbClient();

    public MovieInfos searchFilm (String title)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setType("movie");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    public MovieInfos searchFilm (String title, String year)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setYear(year);
        query.setType("movie");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    public MovieInfos searchMovieById (String imdbID)
    {
        SearchQuery query = new SearchQuery();
        query.setImdbID(imdbID);
        return client.parseMovieInfos(client.getQuery(query.getSearchByIdQuery(), query.getCharset()));
    }

    public MovieInfos searchSerie (String title)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setType("series");
        return client.parseMovieInfos(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    public List<Season> searchSeasons (String imdbID)
    {
        List<Season> seasons = new LinkedList<>();
        SearchQuery query = new SearchQuery();
        query.setImdbID(imdbID);
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
