package ch.heigvd.flat5.api.video;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Excluder;
import com.google.gson.stream.MalformedJsonException;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.LinkedList;

/**
 * Classe effectuant des requêtes sur l'API de OMDb (http://www.omdbapi.com) et permettant de parser les réponses à
 * ces requêtes.
 *
 * @author Jan Purro
 */
public class OMDbClient
{
    // URL de base de l'API.
    private static final String baseURL ="http://www.omdbapi.com/";

    private JsonParser parser = new JsonParser();

    /**
     * Effectue une requête sur l'API de OMDb.
     * @param searchQuery La requête effectuée.
     * @param charset Le charset de la requête.
     * @return Le payload de la réponse de l'API si tout ce passe bien. En cas d'erreurs (mauvaise requête, absence de
     * connexion, etc.) retourne simplement null.
     */
    public String getQuery (String searchQuery, String charset)
    {
        String responseString = "";

        try
        {
            // On effectue la requête.
            HttpURLConnection httpConnection = (HttpURLConnection) new URL(baseURL + "?" + searchQuery).openConnection();

            // On récupère la réponse.
            InputStream response = httpConnection.getInputStream();
            /* On vérifier que le code de la réponse indique que la requête à put être effectuée. Si ce n'est pas le cas,
               on retourn null. */
            int responseCode = httpConnection.getResponseCode();
            if(responseCode < 200 && responseCode >= 300)
            {
                return null;
            }

            // On extrait le payload de la réponse.
            BufferedReader reader = new BufferedReader (new InputStreamReader(response, charset));
            responseString = reader.readLine();
        }

        // En cas d'exception, on affiche un message d'erreur dans la console et on retourn null.
        catch(Exception e)
        {
            System.err.println("Error while getting query" + e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
        return responseString;
    }

    /**
     * Parse la répnse d'une recherche sur OMDb.
     * @param result La réponse à parser. La chaîne de caractère doit contenir du JSON.
     * @return La liste des films obtenus. La liste peut être vide, notamment en cas d'erreur (la chaîne result ne
     * contient pas du JSON, par exemple).
     */
    public List<SearchResult> parseSearchResult (String result)
    {
        List<SearchResult> results = new LinkedList<>();
        // Si le résultat est nul, on retourne la liste vide.
        if (result == null)
        {return results;}

        // Si le résultat ne peut pas être parser en tant qu'objet JSON, on retourn la liste vide.
        JsonObject parsedResult;
        try
        {
            parsedResult = parser.parse(result).getAsJsonObject();
        }
        catch (Exception e)
        {return results;}

        /* Si le JSON contient une erreur (indique généralement qu'aucun résultat n'a été trouvé) ou ne contient
           tout simplement aucun résultats, on retourn une liste vide. */
        if(parsedResult.get("Error") != null ||
           parsedResult.get("Search") == null)
        {return results;}

        // On parcourt les résultats de la recherche et on les ajoute à la liste.
        JsonArray searchResults = parsedResult.getAsJsonArray("Search");
        for (JsonElement movie : searchResults)
        {
            JsonObject movieObject = movie.getAsJsonObject();

            results.add(new SearchResult(movieObject));
        }

        return results;
    }

    /**
     * Pare les informations d'un média (film ou série) obtenu après une requête.
     * @param result La réponse à parser. La chaîne de caractère doit contenir du JSON.
     * @return L'objet contenant les informations. En cas d'erreur (la chaîne result ne
     * contient pas du JSON, par exemple), retourn null.
     */
    public MovieInfos parseMovieInfos (String result)
    {
        // Si result est vide ou ne contient rien, on retroune null.
        if (result == null || result.isEmpty())
        { return null;}

        // Si le résultat ne peut pas être parser en tant qu'objet JSON, on retourn null.
        JsonObject parsedResult;
        try
        {
            parsedResult = parser.parse(result).getAsJsonObject();
        }
        catch (Exception e)
        { return null;}

         // Si le JSON contient une erreur (indique généralement qu'aucun résultat n'a été trouvé) on retourn null.
        if (parsedResult.get("Error") != null)
        { return null; }

        return new MovieInfos(parsedResult);
    }

    /**
     * Pares les informations reçues après une recherche concernant une saison d'une série.
     * @param result La réponse à parser. La chaîne de caractère doit contenir du JSON.
     * @return L'objet contenant les informations. En cas d'erreur (la chaîne result ne
     * contient pas du JSON, par exemple), retourn null.
     */
    public Season parseSeasonInfos (String result)
    {
        // Si result est vide ou ne contient rien, on retroune null.
        if (result == null || result.isEmpty())
        { return null;}

        // Si le résultat ne peut pas être parser en tant qu'objet JSON, on retourn null.
        JsonObject parsedResult;
        try
        {
            parsedResult = parser.parse(result).getAsJsonObject();
        }
        catch (Exception e)
        { return null;}

        // Si le JSON contient une erreur (indique généralement qu'aucun résultat n'a été trouvé) on retourn null.
        if (parsedResult.get("Error") != null)
        { return null; }

        return new Season(parsedResult);
    }
}
