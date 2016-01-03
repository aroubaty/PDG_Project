package ch.heigvd.flat5.serie.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by franz on 03.01.16.
 */
public class Episode
{
    private StringProperty title;
    private StringProperty number;
    private StringProperty season;
    private String path;

    public Episode(String title, String number, String season, String path)
    {
        this.title = new SimpleStringProperty(title);
        if(number == null || number.isEmpty())
            { this.number = new SimpleStringProperty("N/A"); }
        else
            { this.number = new SimpleStringProperty(number); }
        if(season == null || season.isEmpty())
            { this.season = new SimpleStringProperty("Inconnu"); }
        else
            { this.season = new SimpleStringProperty(season); }
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

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getSeason() {
        return season.get();
    }

    public StringProperty seasonProperty() {
        return season;
    }

    public void setSeason(String season) {
        this.season.set(season);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
