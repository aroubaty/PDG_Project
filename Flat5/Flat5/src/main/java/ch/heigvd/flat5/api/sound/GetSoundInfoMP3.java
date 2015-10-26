/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.api.sound;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Track;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class who get all infos from the spotify API. 
 * It use metadata to do the query.
 * 
 * Libs : mp3agic : https://github.com/mpatric/mp3agic
 *        spotify-web-api-java : https://github.com/thelinmichael/spotify-web-api-java
 * @author Anthony
 */
public class GetSoundInfoMP3 {
    private GetSoundInfoMP3(){}
    
    //TODO get genre from API
    public static TrackInfos doIt(String file){
        try {
            //Reading metadata to get the track's title
            Mp3File mp3file = new Mp3File(file);
            String songName = "NotFound";
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                //System.out.println("Track v1: " + id3v1Tag.getTitle());
                songName = id3v1Tag.getTitle();
            }else{
                if (mp3file.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    //System.out.println("Track v2: " + id3v2Tag.getTitle());
                    songName = id3v2Tag.getTitle();
                }
            }

            if("NotFound".equals(songName))
                return null;
            // ------------------------------------------
            
            //Doing spotify API call
            Api api = Api.DEFAULT_API;
            TrackSearchRequest track = api.searchTracks(songName)
                    .market("US")
                    .limit(1)
                    .build();
            
            final List<Track> artists = track.get().getItems();
            for(Track t: artists){                
                return new TrackInfos(
                        "NotFoundYet", 
                        t.getArtists().get(0).getName(), 
                        t.getName(), 
                        t.getAlbum().getName(), 
                        t.getAlbum().getImages().get(0).getUrl());
            }
            // ------------------------------------------
            
            
        } catch (Exception ex) {
            Logger.getLogger("GetSoundInfoMP3").log(Level.SEVERE, null, ex);
        } 
        
        
        return null;
    }
    
}
