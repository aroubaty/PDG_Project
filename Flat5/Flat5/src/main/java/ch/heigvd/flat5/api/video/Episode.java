package ch.heigvd.flat5.api.video;

import com.google.gson.JsonObject;

/**
 * Classe contenant les informations d'un épisode d'une série. Il s'agit simplement d'un conteneur d'informations :
 * le titre, le numéro de l'épisode et sa date de sortie. Ses informations sont obtenues à travers OMDb.
 *
 * @author Jan Purro
 */

public class Episode
{
    private String title;
    private String episode;
    private String releaseDate;


    /**
     * Construit un objet Episode à partir d'un objet JSON.
     * Si les champs demandés ne sont pas trouvé dans l'objet JSON, les champs contiendront null.
     * @param episodeObject L'objet JSON contenant les informations de l'épisode.
     */
    public Episode(JsonObject episodeObject)
    {
        title = episodeObject.get("Title").getAsString();;
        episode = episodeObject.get("Episode").getAsString();;
        releaseDate = episodeObject.get("Released").getAsString();
    }

    /**
     * Retourne le titre de l'épisode.
     * @return Le titre de l'épisode
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Change le titre de l'épisode.
     * @param title Le nouveau nom de l'épisode
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Retourne le numéro de l'épisode.
     * @return Le numéro de l'épisode
     */
    public String getEpisode()
    {
        return episode;
    }

    /**
     * Change le numéro de l'épisode.
     * @param episode Le nouveau numéro de l'épisode
     */
    public void setEpisode(String episode)
    {
        this.episode = episode;
    }

    /**
     * Retourne la date de sortie de l'épisode.
     * @return La date de sortie de l'épisode
     */
    public String getReleaseDate()
    {
        return releaseDate;
    }

    /**
     * Change la date de sortie de l'épisode de l'épisode.
     * @param releaseDate La nouvelle date de sortie de l'épisode
     */
    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }
}
