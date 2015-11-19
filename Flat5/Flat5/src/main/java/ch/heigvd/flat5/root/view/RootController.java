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
import java.util.Objects;

public class RootController {

    // Reference to the main application.
    private MainApp2 mainApp;
    private BorderPane rootLayout;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public void RootOverviewController() {

    }

    @FXML
    private void handleMusic() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
            BorderPane personOverview = loader.load();
            rootLayout.setCenter(personOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            if(!Objects.equals(mainApp.getVecPrev().lastElement().getLocation().toString(), loader.getLocation().toString()))
                mainApp.getVecPrev().add(loader);
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHomer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/home/view/Home.fxml").toURI().toURL());
            AnchorPane personOverview = loader.load();
            rootLayout.setCenter(personOverview);

            if(!Objects.equals(mainApp.getVecPrev().lastElement().getLocation().toString(), loader.getLocation().toString()))
                mainApp.getVecPrev().add(loader);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlerFilm() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/film/view/Film.fxml").toURI().toURL());
            BorderPane personOverview =  loader.load();
            rootLayout.setCenter(personOverview);

            if(!Objects.equals(mainApp.getVecPrev().lastElement().getLocation().toString(), loader.getLocation().toString()))
                mainApp.getVecPrev().add(loader);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlerPrev() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            if (!mainApp.getVecPrev().isEmpty()) {
                loader.setLocation((mainApp.getVecPrev().lastElement()).getLocation());
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