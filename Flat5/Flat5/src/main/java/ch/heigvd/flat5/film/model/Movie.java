package ch.heigvd.flat5.film.model;


import ch.heigvd.flat5.api.video.MovieInfos;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie
{
    private StringProperty title;
    private StringProperty genre;
    private StringProperty date;
    private StringProperty runtime;

    private MovieInfos infos;

    public Movie (MovieInfos infos)
    {
        title = new SimpleStringProperty(infos.getTitle());
        if(infos.getRuntime() == null || infos.getRuntime().isEmpty())
            { runtime = new SimpleStringProperty("Inconnu"); }
        else
            { runtime = new SimpleStringProperty(infos.getRuntime()); }
        if(infos.getGenre() == null || infos.getGenre().isEmpty())
            { genre = new SimpleStringProperty("Inconnu"); }
        else
            { genre = new SimpleStringProperty(infos.getGenre()); }
        if(infos.getReleaseDate() == null || infos.getReleaseDate().isEmpty())
            { date = new SimpleStringProperty("Inconnue"); }
        else
            { date = new SimpleStringProperty(infos.getReleaseDate()); }
        this.infos = infos;
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

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getRuntime() {
        return runtime.get();
    }

    public StringProperty runtimeProperty() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime.set(runtime);
    }

    public MovieInfos getInfos() {
        return infos;
    }

    public void setInfos(MovieInfos infos) {
        this.infos = infos;
    }
}
