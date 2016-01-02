package ch.heigvd.flat5.root.view;


import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.home.view.HomeController;
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
    private static FXMLLoader settings;

    private static BorderPane viewMusic;
    private static BorderPane viewHome;
    private static BorderPane viewFilm;
    private static BorderPane viewFilmInfo;
    private static BorderPane viewSerie;
    private static BorderPane viewSerieInfo;
    private static GridPane viewSettings;

    public RootController()
    {
        try {
            music = new FXMLLoader();
            music.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
            viewMusic = music.load();

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

            settings = new FXMLLoader();
            settings.setLocation(new File("src/main/java/ch/heigvd/flat5/settings/view/Settings.fxml").toURI().toURL());
            viewSettings = settings.load();

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
        save(viewFilm);
    }
    /**
     *
     * fonction appelé quant nous choisisson un film (version detailée)
     */
    @FXML
    public void handlerFilmInfo() {
        rootLayout.setCenter(viewFilmInfo);
        save(viewFilmInfo);
    }

    /**
     *
     * fonction appelé quant nous cliquons sur le bouton "Serie"
     */
    @FXML
    public void handlerSerie() {
        rootLayout.setCenter(viewSerie);
        save(viewSerie);
    }

    /**
     *
     * fonction appelé quant nous choisissons une serie (version detailée)
     */
    @FXML
    public void handlerSerieInfo() {
        rootLayout.setCenter(viewSerieInfo);
        save(viewSerieInfo);
    }

    @FXML
    public void handlerSettings() {
        rootLayout.setCenter(viewSettings);
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