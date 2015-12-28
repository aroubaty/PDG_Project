/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.music.sync;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.music.model.util.MusicBrowser;
import ch.heigvd.flat5.music.view.MusicController;
import ch.heigvd.flat5.sync.SyncHandler;
import ch.heigvd.flat5.sync.SyncManager;
import com.sun.jna.NativeLibrary;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author jermoret
 */
public class MusicSyncController implements Initializable {

    @FXML ProgressIndicator progressIndicator;
    @FXML Button waitingForConnection;
    @FXML ToggleButton waitingForConnection2;
    @FXML Label displaySynched;
    @FXML TextField address;

    private MusicController musicController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void handleWaitingForAClient() {
        waitingForConnection.setDisable(true);
        progressIndicator.setVisible(true);
        new Thread(() -> {
            //musicController.getSyncManager().accept();
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                displaySynched.setText("Connexion établie avec succès.");
            });
            musicController.syncThePlayer();
        }).start();
    }

    @FXML
    public void handleConnectionToAClient() {
        /*if(musicController.getSyncManager().connect(address.getText(), AppConfig.DEFAULT_PORT)) {
            System.out.println("Connecte");
            musicController.syncThePlayer();
        } else {
            System.out.println("Pas connecte");
        }*/
    }

    /**
     * Setter for property 'musicController'.
     *
     * @param musicController Value to set for property 'musicController'.
     */
    public void setMusicController(MusicController musicController) {
        this.musicController = musicController;
    }
}
