package ch.heigvd.flat5.api.video;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;

public class Season
{
    String title;
    String season;
    List<Episode> episodes;

    public Season(JsonObject seasonObject)
    {
        this.episodes = new LinkedList<>();
        title = seasonObject.get("Title").getAsString();
        season = seasonObject.get("Season").getAsString();
        JsonArray episodes = seasonObject.getAsJsonArray("Episodes");

        for (JsonElement episode : episodes)
        {
            JsonObject episodeObject = episode.getAsJsonObject();
            this.episodes.add(new Episode(episodeObject));
        }
    }
}
