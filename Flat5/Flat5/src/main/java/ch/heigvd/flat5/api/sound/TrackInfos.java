/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.api.sound;

/**
 * Classe publique permettant l'accès aux informations d'un fichier audio si on peut les trouver.
 * Les champs n'ayant pas pu être trouvé sont initialisés à null.
 * title contient le nom du fichier dans le cas ou aucun tag n'a pas être complété.
 *
 * @author Anthony & Jérôme
 */
public class TrackInfos {
    public String title;
    public String artist;
    public String album;
    public String genre;
    public String year;
    public String length;
    public String urlCover;

    public TrackInfos(String title, String artist, String album, String genre, String year, String length, String urlCover) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.length = length;
        this.urlCover = urlCover;
    }
}
