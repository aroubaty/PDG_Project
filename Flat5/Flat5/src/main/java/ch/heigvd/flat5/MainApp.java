package ch.heigvd.flat5;

import ch.heigvd.flat5.music.view.MusicController;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
        
        // Music view 
        Stage musicStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new File("src/main/java/ch/heigvd/flat5/music/view/Music.fxml").toURI().toURL());
        Parent musicPane = loader.load();
        Scene musicScene = new Scene(musicPane);
        musicStage.setScene(musicScene);
        musicStage.setMinHeight(500);
        musicStage.setMinWidth(700);
        MusicController controller = loader.getController();
        musicStage.setOnCloseRequest(e -> controller.exit());
        musicStage.show();
        
        //TEST API MUSIC
        /*TrackInfos t = GetSoundInfo.doIt("music/michaelJackson-beatIt.mp3");
        System.out.println(t);*/
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
