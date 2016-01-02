package ch.heigvd.flat5.api.video;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.LinkedList;

public class OMDbClient
{
    private static final String baseURL ="http://www.omdbapi.com/";
    private JsonParser parser = new JsonParser();

    public String getQuery (String searchQuery, String charset)
    {
        String responseString = "";

        try
        {
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(baseURL + "?" + searchQuery).openConnection();

            InputStream response = httpConnection.getInputStream();
            int responseCode = httpConnection.getResponseCode();

            BufferedReader reader = new BufferedReader (new InputStreamReader(response, charset));
            responseString = reader.readLine();
        }

        catch(Exception e)
        {System.err.println("Error while getting query" + e.getClass().getName() + ": " + e.getMessage());}
        return responseString;
    }

    public List<SearchResult> parseSearchResult (String result)
    {

        JsonObject parsedResult  = parser.parse(result).getAsJsonObject();

        // We check result is a search result containing some movies infos.
        if(parsedResult.get("Error") != null ||
           parsedResult.get("Search") == null)
        {
            return null;
        }

        JsonArray searchResults = parsedResult.getAsJsonArray("Search");
        List<SearchResult> results = new LinkedList<>();

        for (JsonElement movie : searchResults)
        {
            JsonObject movieObject = movie.getAsJsonObject();

            results.add(new SearchResult(movieObject));
        }

        return results;
    }

    public MovieInfos parseMovieInfos (String result)
    {

        JsonObject parsedResult = parser.parse(result).getAsJsonObject();

        // We check for errors.
        if (parsedResult.get("Error") != null) {
            return null;
        }

        return new MovieInfos(parsedResult);
    }

    public Season parseSeasonInfos (String result)
    {

        JsonObject parsedResult = parser.parse(result).getAsJsonObject();

        // We check for errors.
        if (parsedResult.get("Error") != null) {
            return null;
        }

        return new Season(parsedResult);
    }
}
