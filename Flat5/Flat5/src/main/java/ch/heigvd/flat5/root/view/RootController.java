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
import java.io.IOException;import java.lang.System;

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
            BorderPane personOverview = (BorderPane) loader.load();
            rootLayout.setCenter(personOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()-200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleHomer()
    {
        try{
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp2.class.getResource("view/Home.fxml"));
            System.out.print(loader.getLocation());
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlerFilm()
    {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/film/view/Film.fxml").toURI().toURL());
            BorderPane personOverview = (BorderPane) loader.load();
            rootLayout.setCenter(personOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()-200);

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
    public void setRootLayout(BorderPane rootLayout)
    {
        this.rootLayout = rootLayout;
    }
    public BorderPane getRootLayout()
    {
        return rootLayout;
    }
}