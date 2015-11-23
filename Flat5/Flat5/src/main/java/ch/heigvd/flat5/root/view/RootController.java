package ch.heigvd.flat5.root.view;


import ch.heigvd.flat5.MainApp2;
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
    

    @FXML
    public void handleMusic() {
        handlerGeneral("src/main/java/ch/heigvd/flat5/music/view/Music.fxml");
    }

    @FXML
    public void handleHome() {
        handlerGeneral("src/main/java/ch/heigvd/flat5/home/view/Home.fxml");
    }

    @FXML
    public void handlerFilm() {
        handlerGeneral("src/main/java/ch/heigvd/flat5/film/view/Film.fxml");
    }

    private void handlerGeneral(String filepath)
    {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File(filepath).toURI().toURL());
            BorderPane personOverview = loader.load();
            rootLayout.setCenter(personOverview);

            if (!Objects.equals(mainApp.getVecPrev().lastElement().getLocation().toString(), loader.getLocation().toString()))
                mainApp.getVecPrev().add(loader);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlerPrev() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            if (mainApp.getVecPrev().size() > 1) {
                loader.setLocation((mainApp.getVecPrev().get(mainApp.getVecPrev().size()-2)).getLocation());
                for (int i = 0; i < mainApp.getVecPrev().size(); i++) {
                    System.out.println((mainApp.getVecPrev().get(i)).getLocation());
                }
                mainApp.getVecPrev().removeElementAt(mainApp.getVecPrev().size() - 1);

                BorderPane personOverview =  loader.load();
                rootLayout.setCenter(personOverview);

                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {

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