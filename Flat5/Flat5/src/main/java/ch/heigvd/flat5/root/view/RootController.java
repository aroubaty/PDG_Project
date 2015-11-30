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

    static BorderPane viewMusic;
    static BorderPane viewHome;
    static BorderPane viewFilm;

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMusic() throws IOException {
        rootLayout.setCenter(viewMusic);
        save(music, viewMusic);
        //handlerGeneral("src/main/java/ch/heigvd/flat5/music/view/Music.fxml");
    }

    @FXML
    public void handleHome() throws IOException {

        rootLayout.setCenter(viewHome);
        save(home, viewHome);
    }

    @FXML
    public void handlerFilm()  throws IOException{
        rootLayout.setCenter(viewFilm);
        save(film, viewFilm);
    }


    private void save(FXMLLoader loader, BorderPane view)
    {
        if (!Objects.equals(mainApp.getVecPrev().lastElement().getLocation().toString(), loader.getLocation().toString())) {
            mainApp.getVecPrev().add(loader);
            mainApp.getVecPrevView().add(view);
        }
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        view.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
    }

    @FXML
    private void handlerPrev() {
        // Load person overview.
        FXMLLoader loader = new FXMLLoader();
        BorderPane view ;
        if (mainApp.getVecPrev().size() > 1) {
            loader = mainApp.getVecPrev().get(mainApp.getVecPrev().size()-2);
            view = mainApp.getVecPrevView().get(mainApp.getVecPrevView().size()-2);
            //loader.setLocation((mainApp.getVecPrev().get(mainApp.getVecPrev().size()-2)).getLocation());
            for (int i = 0; i < mainApp.getVecPrev().size(); i++) {
                System.out.println((mainApp.getVecPrev().get(i)).getLocation());
            }
            mainApp.getVecPrev().removeElementAt(mainApp.getVecPrev().size() - 1);
            mainApp.getVecPrevView().removeElementAt(mainApp.getVecPrevView().size() - 1);

            rootLayout.setCenter(view);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            view.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
        }

    }

    @FXML
    private void initialize() throws MalformedURLException {

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp2 mainApp) {
        this.mainApp = mainApp;
        System.out.println("yollloo");

        this.rootLayout = mainApp.getRootLayout();
        System.out.println(rootLayout);
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }
}