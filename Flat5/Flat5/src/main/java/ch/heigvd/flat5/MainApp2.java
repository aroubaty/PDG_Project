package ch.heigvd.flat5;

import java.io.*;
import java.lang.Override;
import java.lang.String;
import java.net.MalformedURLException;
import java.util.Vector;

import ch.heigvd.flat5.home.view.HomeController;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.root.view.RootController;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import ch.heigvd.flat5.utils.LibraryManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

    //vecteur utilisé pour l'utilisation du bouton prcédent
    private Vector<Pane> vecPrevView = new Vector();
    private HomeController homeController;
    private RootController rootController;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private FXMLLoader rootloader;
    private String path;


    public static void main(String[] args) {
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

        String filename = "mediaPath.conf";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            if ((line = reader.readLine()) != null)
            {
                path = line;
                // Init library
                LibraryManager.addFileToDB(path);
            }
            reader.close();
        }
        catch (Exception e)
        {
            path = "-";
        }

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
     * @param
     *
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
            vecPrevView.add(homeOverview);

            //System.out.println("loader.getController(); " + rootloader.getController());
            rootController.setMainApp(this);
            homeController.setMainApp(this);
            homeController.setRootLayout(rootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Permet de mettre l'application en full screen et de la quitter via raccourcis
     * @param scene
     */
    private void initKeyPressed(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (quit.match(keyEvent)) {
                primaryStage.close();
                keyEvent.consume();
            }
            if (fullScreen.match(keyEvent)) {
                primaryStage.setFullScreen(true);
                keyEvent.consume();
            }
        });
    }

    /**
     *
     * Recupère le layout de root
     * @return rootLayout
     */
    public BorderPane getRootLayout() {
        return rootLayout;
    }

    /**
     *
     * Recupère le controlleur du rootlayout
     * @return rootController
     */
    public RootController getRootController()
    {
        return rootController;
    }

    /**
     *
     *  permet de recuprer les vector qui contient les vue precedement ouverte
     * @return vecPrevView
     */
    public Vector<Pane> getVecPrevView() {
        return vecPrevView;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        LibraryManager.addFileToDB(path);
        try {
            PrintWriter pw =  new PrintWriter(new BufferedWriter(new FileWriter("mediaPath.conf", false)));
            System.out.println("Path " + path);
            pw.println(path);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}