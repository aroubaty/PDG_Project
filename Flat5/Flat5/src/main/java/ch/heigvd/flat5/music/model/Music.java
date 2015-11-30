package ch.heigvd.flat5.music.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * Created by jermoret on 02.11.2015.
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
    private ClassLoader cl = getClass().getClassLoader();

    public Music(String title, String path, String length) {
        this.title = new SimpleStringProperty(title);
        this.path = path;
        this.length = new SimpleStringProperty(length);
        setNoCover();
    }

    public Music(String title, String artist, String album, String genre, String year, String length, String path, Image cover) {
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.genre = new SimpleStringProperty(genre);
        this.year = new SimpleStringProperty(year);
        this.length = new SimpleStringProperty(length);
        this.path = path;
        this.cover = cover;
        if(cover == null) {
            setNoCover();
        }
    }

    private void setNoCover() {
        cover = new Image(cl.getResourceAsStream("img/no_cover.png"));
        System.out.println(cover.getHeight());
    }

    public String getArtist() {
        return artist.get();
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album.get();
    }

    public StringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getLength() {
        return length.get();
    }

    public StringProperty lengthProperty() {
        return length;
    }

    public void setLength(String length) {
        this.length.set(length);
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public Image getCover() {
        return cover;
    }
}
