/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.api.sound;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Track;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * class who get all infos from the spotify API. It use metadata to do the
 * query.
 *
 * Libs : Jaudiotagger  
 * spotify-web-api-java : https://github.com/thelinmichael/spotify-web-api-java
 *
 * @author Anthony
 */
public class GetSoundInfoMP3 {

    private GetSoundInfoMP3() {
    }

    //TODO get genre from API
    public static TrackInfos doIt(String file) {
        String songName = "NotFound";
        try {
            File fileIO = new File(file);
            //extension
            String ext = fileIO.getName().substring(
                    fileIO.getName().lastIndexOf('.') + 1);

            AudioFile f = AudioFileIO.read(fileIO);
            Tag tag = f.getTag();
            songName = tag.getFirst(FieldKey.TITLE);

        
            if ("NotFound".equals(songName)) 
                return null;
            
            // ------------------------------------------

            //Doing spotify API call
            Api api = Api.DEFAULT_API;
            TrackSearchRequest track = api.searchTracks(songName)
                    .market("US")
                    .limit(1)
                    .build();

            final List<Track> artists = track.get().getItems();
            for (Track t : artists) {
                return new TrackInfos(
                        "NotFoundYet",
                        t.getArtists().get(0).getName(),
                        t.getName(),
                        t.getAlbum().getName(),
                        t.getAlbum().getImages().get(0).getUrl(),
                        ext);
            }
            // ------------------------------------------

        } catch (Exception ex) {
            Logger.getLogger("GetSoundInfoMP3").log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
