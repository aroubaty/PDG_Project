package ch.heigvd.flat5.music.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * Classe modèle pour la musique
 * @author Jérôme
 */
public class Music {
    private StringProperty title;
    private StringProperty artist;
    private StringProperty album;
    private StringProperty genre;
    private StringProperty year;
    private StringProperty length;
    private String path;
    private Image cover;

    public Music(String title, String artist, String album, String genre, String year, String length, String path, Image cover) {
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.genre = new SimpleStringProperty(genre);
        this.year = new SimpleStringProperty(year);
        this.length = new SimpleStringProperty(length);
        this.path = path;
        this.cover = cover;
    }

    /**
     * Getter for property 'artist'.
     *
     * @return Value for property 'artist'.
     */
    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    /**
     * Setter for property 'artist'.
     *
     * @param artist Value to set for property 'artist'.
     */
    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * Getter for property 'path'.
     *
     * @return Value for property 'path'.
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for property 'path'.
     *
     * @param path Value to set for property 'path'.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for property 'album'.
     *
     * @return Value for property 'album'.
     */
    public String getAlbum() {
        return album.get();
    }

    public StringProperty albumProperty() {
        return album;
    }

    /**
     * Setter for property 'album'.
     *
     * @param album Value to set for property 'album'.
     */
    public void setAlbum(String album) {
        this.album.set(album);
    }

    /**
     * Getter for property 'genre'.
     *
     * @return Value for property 'genre'.
     */
    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    /**
     * Setter for property 'genre'.
     *
     * @param genre Value to set for property 'genre'.
     */
    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    /**
     * Getter for property 'length'.
     *
     * @return Value for property 'length'.
     */
    public String getLength() {
        return length.get();
    }

    public StringProperty lengthProperty() {
        return length;
    }

    /**
     * Setter for property 'length'.
     *
     * @param length Value to set for property 'length'.
     */
    public void setLength(String length) {
        this.length.set(length);
    }

    /**
     * Getter for property 'year'.
     *
     * @return Value for property 'year'.
     */
    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    /**
     * Setter for property 'year'.
     *
     * @param year Value to set for property 'year'.
     */
    public void setYear(String year) {
        this.year.set(year);
    }

    /**
     * Getter for property 'cover'.
     *
     * @return Value for property 'cover'.
     */
    public Image getCover() {
        return cover;
    }
}
