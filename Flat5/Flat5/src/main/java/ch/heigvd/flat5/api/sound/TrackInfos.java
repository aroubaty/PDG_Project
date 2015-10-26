/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.api.sound;

/**
 * 
 * @author Anthony
 */
public class TrackInfos {
    public String genre;
    public String artist;
    public String title;
    public String album;
    public String cover;
    public String fileExtension;

    public TrackInfos(String genre, String artist, 
            String title, String album, String image, String fileExtension) {
        this.genre = genre;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.cover = image;
        this.fileExtension = fileExtension;
    } 
    
    public String toString(){
        return "Artist: "+artist+
                "\nTitle: "+title+
                "\nAlbum: "+album+
                "\nGenre: "+genre+
                "\nCover:" + cover;
    }
    
}
