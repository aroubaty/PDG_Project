package ch.heigvd.flat5.api.video;

import java.util.*;

public class MovieDataGetter
{
    private OMDbClient client = new OMDbClient();

    public List<SearchResult> searchFilm (String title)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        return client.parseSearchResult(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    public List<SearchResult> searchFilm (String title, String year)
    {
        SearchQuery query = new SearchQuery();
        query.setTitle(title);
        query.setYear(year);
        return client.parseSearchResult(client.getQuery(query.getSearchQuery(), query.getCharset()));
    }

    public MovieInfos searchMovieById (String imdbID)
    {
        SearchQuery query = new SearchQuery();
        query.setImdbID(imdbID);

        return client.parseMovieInfos(client.getQuery(query.getSearchByIdQuery(), query.getCharset()));
    }
}
