package ch.heigvd.flat5;

import java.io.File;
import java.io.IOException;import java.lang.Override;import java.lang.String;

import ch.heigvd.flat5.home.view.HomeController;
import ch.heigvd.flat5.root.view.RootController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

public class MainApp2 extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private FXMLLoader rootloader;
    //combinaison pour quiter (ctrl + Q)
    final KeyCombination quit = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    //combinaison pour mettre en fulscreen (alt + enter)
    final KeyCombination fullScreen = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        primaryStage.setFullScreen(true);
        initRootLayout();


        loadRoot();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(MainApp2.class.getResource("root/view/Root.fxml"));
            //loader.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/root/view/Root.fxml").toURI().toURL());
            //loader.setLocation(new File("src/main/java/ch/heigvd/flat5/root/view/Root.fxml").toURI().toURL());
            rootloader = loader;
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setFullScreen(true);
            scene.getStylesheets().add("Buttons.css");
            initKeyPressed(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void loadRoot() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/home/view/Home.fxml").toURI().toURL());
            AnchorPane homeOverview = (AnchorPane) loader.load();

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
            HomeController homeController = loader.getController();
            RootController rootController = rootloader.getController();
            System.out.println("loader.getController(); " + rootloader.getController());
            rootController.setMainApp(this);

            //homeController.setMainApp(this);
            homeController.setRootLayout(rootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initKeyPressed(Scene scene)
    {
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
    public BorderPane getRootLayout() {
        return rootLayout;
    }
}