/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.api.sound;

import ch.heigvd.flat5.music.model.Music;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Track;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * Classe permettant la récupération des tags d'une musique selon la politique suivante :
 * On tente de récupérer le maximum de tags ID3, puis l'API Spotify est utilisée pour
 * récupérer une image de l'album ou pour récupérer des éventuels tags ID3 non renseignés.
 *
 * Libs : Jaudiotagger
 * spotify-web-api-java : https://github.com/thelinmichael/spotify-web-api-java
 *
 * @author Anthony & Jérôme
 */
public class GetSoundInfo {

    private GetSoundInfo() {
    }

    /**
     * Rend les informations d'un fichier audio
     * Retourne null s'il n'est pas en mesure de trouver le titre du fichier audio
     *
     * @param fileIO
     * @return Classe contenant les informations, accessibles directement par champs publiques
     */
    public static TrackInfos doIt(File fileIO) {

        // Lecture des tags ID3
        String title;
        AudioFile audioFile = null;
        try {
            audioFile = AudioFileIO.read(fileIO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Tag tag = audioFile.getTag();
        title = tag.getFirst(FieldKey.TITLE);


        boolean tryApiCall = true;

        // Si on n'a pas de titre on arrête la récupération des infos
        // l'interface affichera simplement le nom du fichier comme titre
        if (title.equals("")) {
            tryApiCall = false;
            title = fileIO.getName();
        }

        // Récupération du maximum de tag ID3
        // Lorsque c'est pas possible on tente de questionner l'API Spotify
        String artist, album, genre, year, length, urlCover = null;

        // Récupération de la durée du fichier audio
        DateFormat sdf = new SimpleDateFormat("m:ss");
        length = sdf.format(new Date(audioFile.getAudioHeader().getTrackLength() * 1000));

        // L'API Spotify ne permets pas la récupération du genre et de l'année
        // On compte sur les tags ID3
        genre = tag.getFirst(FieldKey.GENRE);
        year = tag.getFirst(FieldKey.YEAR);


        // Appel de l'API Spotify
        Api api = Api.DEFAULT_API;

        if (tryApiCall) {
            TrackSearchRequest request = api.searchTracks(title)
                    .market("US")
                    .limit(1)
                    .build();

            final Track track; // Premier résultat disponible

            try {
                track = request.get().getItems().get(0);

                if ((artist = tag.getFirst(FieldKey.ARTIST)) == null) {
                    artist = track.getArtists().get(0).getName();
                }
                if ((album = tag.getFirst(FieldKey.ALBUM)) == null) {
                    album = track.getAlbum().getName();
                }

                // Les tags ID3 ne permettent pas la récupération d'une image de l'album
                // On compte sur l'API Spotify
                urlCover = track.getAlbum().getImages().get(0).getUrl();

            } catch (Exception e) { // L'API Spotify ne trouve pas de résultat
                artist = tag.getFirst(FieldKey.ARTIST);
                album = tag.getFirst(FieldKey.ALBUM);
            }


        } else {
            artist = tag.getFirst(FieldKey.ARTIST);
            album = tag.getFirst(FieldKey.ALBUM);
        }

        return new TrackInfos(
                title,
                artist,
                album,
                genre,
                year,
                length,
                urlCover
        );
    }

}
