package ch.heigvd.flat5.home.view;

import ch.heigvd.flat5.MainApp2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;import java.lang.System;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Simon on 09.11.2015.
 */
public class HomeController implements Initializable {
    // Reference to the main application.
    private MainApp2 mainApp;
    private BorderPane rootLayout;

    @FXML
    Label CurrentTitle;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public void HomeOverviewController() {

    }
    @FXML
    private void handleMusic() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
            System.out.print(loader.getLocation());
            BorderPane personOverview = (BorderPane) loader.load();
            rootLayout.setCenter(personOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            personOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()-200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleHome()
    {
        mainApp.getRootController().handleHome();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
