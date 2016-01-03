package ch.heigvd.flat5.api.video;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe contenant les informations d'une saison d'une série. Il s'agit d'un simple conteneur d'informations obtenues
 * sur OMDb. Le titre de la saison, son numéro et une liste de ses épisodes.
 * @author Jan Purro
 */
public class Season
{
    String title;
    String season;
    List<Episode> episodes;

    /**
     * Construit un objet Saison à partir d'un objet JSON.
     * Si les champs demandés ne sont pas trouvé dans l'objet JSON, les champs contiendront null.
     * @param seasonObject  L'objet JSON contenant les informations de la saison.
     */
    public Season(JsonObject seasonObject)
    {
        this.episodes = new LinkedList<>();
        title = seasonObject.get("Title").getAsString();
        season = seasonObject.get("Season").getAsString();

        /* On récupère les informations de chaque épisode de la série, s'il sexistent. */
        JsonArray episodes = seasonObject.getAsJsonArray("Episodes");
        if (episodes != null)
        {
            for (JsonElement episode : episodes) {
                JsonObject episodeObject = episode.getAsJsonObject();
                this.episodes.add(new Episode(episodeObject));
            }
        }
    }
}
