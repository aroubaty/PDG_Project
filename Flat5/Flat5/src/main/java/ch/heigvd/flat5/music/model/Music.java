package ch.heigvd.flat5.music.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by jermoret on 02.11.2015.
 */
public class Music {
    private StringProperty title;
    private String path;

    public Music(String title, String path) {
        this.title = new SimpleStringProperty(title);
        this.path = path;
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
}
