package ch.heigvd.flat5.root.view;


import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.film.view.FilmController;
import ch.heigvd.flat5.film.view.FilmInfoController;
import ch.heigvd.flat5.home.view.HomeController;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.serie.view.SerieController;
import ch.heigvd.flat5.serie.view.SerieInfoController;
import ch.heigvd.flat5.settings.view.SettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;
import java.lang.System;
import java.net.MalformedURLException;
import java.util.Objects;

public class RootController {

    // Reference to the main application.
    private MainApp2 mainApp;
    private BorderPane rootLayout;
    private static FXMLLoader music;
    private static FXMLLoader home;
    private static FXMLLoader film;
    private static FXMLLoader filmInfo;
    private static FXMLLoader serie;
    private static FXMLLoader serieInfo;
    private static FXMLLoader settingsLoader;

    private static BorderPane viewMusic;
    private static BorderPane viewHome;
    private static BorderPane viewFilm;
    private static BorderPane viewFilmInfo;
    private static BorderPane viewSerie;
    private static BorderPane viewSerieInfo;
    private static BorderPane viewSettings;

    public RootController()
    {
        try {

            music = new FXMLLoader();
            music.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
            viewMusic = music.load();

            settingsLoader = new FXMLLoader();
            settingsLoader.setLocation(new File("src/main/java/ch/heigvd/flat5/settings/view/Settings.fxml").toURI().toURL());
            viewSettings = settingsLoader.load();

            home = new FXMLLoader();
            home.setLocation(new File("src/main/java/ch/heigvd/flat5/home/view/Home.fxml").toURI().toURL());
            viewHome = home.load();

            film = new FXMLLoader();
            film.setLocation(new File("src/main/java/ch/heigvd/flat5/film/view/Film.fxml").toURI().toURL());
            viewFilm = film.load();

            serie = new FXMLLoader();
            serie.setLocation(new File("src/main/java/ch/heigvd/flat5/serie/view/Serie.fxml").toURI().toURL());
            viewSerie = serie.load();

            serieInfo = new FXMLLoader();
            serieInfo.setLocation(new File("src/main/java/ch/heigvd/flat5/serie/view/Serieinfo.fxml").toURI().toURL());
            viewSerieInfo = serieInfo.load();


            filmInfo = new FXMLLoader();
            filmInfo.setLocation(new File("src/main/java/ch/heigvd/flat5/film/view/FilmInfo.fxml").toURI().toURL());
            viewFilmInfo = filmInfo.load();



            ((FilmController)film.getController()).setRootController(this);
            System.out.println("loader du filmCrt" + this);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * fonction appelé quant nous cliquons sur le bouton "Music"
     */
    @FXML
    public void handleMusic() {
        rootLayout.setCenter(viewMusic);
        ((MusicController)music.getController()).setMainApp(mainApp);
        ((MusicController)music.getController()).scanMusicFiles();
        save(viewMusic);
    }
    /**
     *
     * fonction appelé quant nous cliquons sur le bouton "Home"
     */
    @FXML
    public void handleHome() {
        rootLayout.setCenter(viewHome);
        ((HomeController)home.getController()).setMainApp(mainApp);
        save(viewHome);
    }

    /**
     *
     * fonction appelé quant nous cliquons sur le bouton "Film"
     */
    @FXML
    public void handlerFilm() {
        rootLayout.setCenter(viewFilm);
        ((FilmController)film.getController()).setRootController(this);
        ((FilmController)film.getController()).scanVideoFiles();
        save(viewFilm);
    }
    /**
     *
     * fonction appelé quant nous choisisson un film (version detailée)
     */
    @FXML
    public void handlerFilmInfo(Movie movie) {
        rootLayout.setCenter(viewFilmInfo);
        ((FilmInfoController)filmInfo.getController()).setMovie(movie);
        save(viewFilmInfo);
    }

    /**
     *
     * fonction appelé quant nous cliquons sur le bouton "Serie"
     */
    @FXML
    public void handlerSerie() {
        rootLayout.setCenter(viewSerie);
        ((SerieController)serie.getController()).setRootController(this);
        ((SerieController)serie.getController()).scanSeries();
        save(viewSerie);
    }

    /**
     *
     * fonction appelé quant nous choisissons une serie (version detailée)
     */
    @FXML
    public void handlerSerieInfo(Movie serie) {
        rootLayout.setCenter(viewSerieInfo);
        ((SerieInfoController)serieInfo.getController()).setSerie(serie);
        save(viewSerieInfo);
    }

    @FXML
    public void handlerSettings() {
        rootLayout.setCenter(viewSettings);
        ((SettingsController)settingsLoader.getController()).setMainApp(mainApp);
        ((SettingsController)settingsLoader.getController()).setPath(mainApp.getPath());
        save(viewSettings);
    }

    /**
     *
     * fonction sauvegardant la dernière vue rendue
     * @param view
     */
    private void save(Pane view)
    {
        if (!Objects.equals(mainApp.getVecPrevView().lastElement(), view)) {
            mainApp.getVecPrevView().add(view);
        }
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        view.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
    }

    /**
     *
     * fonction appelé quand nous cliquons sur le bouton précédent
     * @param
     */
    @FXML
    private void handlerPrev() {
        Pane view ;
        if (mainApp.getVecPrevView().size() > 1) {
            view = mainApp.getVecPrevView().get(mainApp.getVecPrevView().size()-2);
            mainApp.getVecPrevView().removeElementAt(mainApp.getVecPrevView().size() - 1);
            rootLayout.setCenter(view);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            view.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
        }

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp2 mainApp) {
        this.mainApp = mainApp;
        this.rootLayout = mainApp.getRootLayout();
        System.out.println(rootLayout);
    }
}