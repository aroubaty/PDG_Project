package ch.heigvd.flat5.home.view;

import ch.heigvd.flat5.MainApp2;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.lang.System;
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
    private void handleFilm() {
        mainApp.getRootController().handlerFilm();
    }

    @FXML
    private void handleMusic() {
        System.out.println("musoic : " + mainApp);
        mainApp.getRootController().handleMusic();
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
        System.out.println("inthisshit "+  this.mainApp);

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
        System.out.println("init");;
    }
}
