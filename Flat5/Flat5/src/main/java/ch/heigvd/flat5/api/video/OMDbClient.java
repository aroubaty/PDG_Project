package ch.heigvd.flat5.api.video;

import java.io.*;
import java.net.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URLEncoder;
import java.util.*;

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
        if (responseCode >= 200 && responseCode < 300)
        {
            System.out.println("OK");
        }
        else
        {
            System.out.println("KO");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset)))
        {
            responseString = reader.readLine();
        }
        }
        catch(Exception e) {}

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

        JsonObject parsedResult  = parser.parse(result).getAsJsonObject();

        // We check for errors.
        if(parsedResult.get("Error") != null)
        {
            return null;
        }

        return new MovieInfos (parsedResult);
    }
}
