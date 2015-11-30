package ch.heigvd.flat5;

import java.io.File;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.Vector;

import ch.heigvd.flat5.home.view.HomeController;
import ch.heigvd.flat5.root.view.RootController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

public class MainApp2 extends Application {

    //combinaison pour quiter (ctrl + Q)
    private final KeyCombination quit = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    //combinaison pour mettre en fulscreen (alt + enter)
    private final KeyCombination fullScreen = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    private Vector<FXMLLoader> vecPrev = new Vector();
    private HomeController homeController;
    private RootController rootController;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private FXMLLoader rootloader;
    private FXMLLoader lastloader;

    public void MainApp()
    {

    }

    public static void main(String[] args) {
        System.out.println("Boooooooooooooom");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Flat5");
        primaryStage.setFullScreen(true);

        initRootLayout();
        loadRoot();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/root/view/Root.fxml").toURI().toURL());
            rootloader = loader;
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setFullScreen(true);
            scene.getStylesheets().add("Styles.css");
            initKeyPressed(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    private void loadRoot() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/home/view/Home.fxml").toURI().toURL());
            BorderPane homeOverview = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(homeOverview);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //set Stage boundaries to visible bounds of the main screen
            primaryStage.setX(primaryScreenBounds.getMinX());
            primaryStage.setY(primaryScreenBounds.getMinY());
            primaryStage.setWidth(primaryScreenBounds.getWidth());
            primaryStage.setHeight(primaryScreenBounds.getHeight());
            homeOverview.setPrefSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());

            // Give the controller access to the main app.
            homeController = loader.getController();
            rootController = rootloader.getController();
            vecPrev.add(loader);
            for(int i = 0 ; i < vecPrev.size(); i++)
            {
                System.out.println((vecPrev.get(i)).getLocation());
            }

            System.out.println("loader.getController(); " + rootloader.getController());
            rootController.setMainApp(this);
            homeController.setMainApp(this);
            homeController.setRootLayout(rootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */


    private void initKeyPressed(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (quit.match(keyEvent)) {
                    primaryStage.close();
                    keyEvent.consume();
                }
                if (fullScreen.match(keyEvent)) {
                    primaryStage.setFullScreen(true);
                    keyEvent.consume();
                }
            }
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public FXMLLoader getLastloader() {

        return lastloader;
    }

    public void setLastloader(FXMLLoader lastloader) {
        this.lastloader = lastloader;
    }

    public FXMLLoader getRootloader() {
        return rootloader;
    }

    public void setRootloader(FXMLLoader rootloader) {
        this.rootloader = rootloader;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    public Vector<FXMLLoader> getVecPrev() {
        return vecPrev;
    }

    public RootController getRootController()
    {
        return rootController;
    }

    public HomeController getHomeController()
    {
        return homeController;
    }
}