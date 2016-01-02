package ch.heigvd.flat5.api.video;

import com.google.gson.JsonObject;

public class Episode
{
    String title;
    String episode;
    String releaseDate;


    public Episode(String title, String episode, String releaseDate)
    {
        this.title = title;
        this.episode = episode;
        this.releaseDate = releaseDate;
    }

    public Episode(JsonObject episodeObject)
    {
        title = episodeObject.get("Title").getAsString();;
        episode = episodeObject.get("Episode").getAsString();;
        releaseDate = episodeObject.get("Released").getAsString();
    }
}
