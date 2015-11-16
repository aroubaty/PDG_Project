/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.music.view;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import ch.heigvd.flat5.music.model.Music;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.jna.NativeLibrary;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

// Problème avec le slider qui chope la mauvaise valeur sur le Slider lorsque la souris est lachée
// Regarder les évennements .

/**
 * FXML Controller class
 *
 * @author jermoret
 */
public class MusicController implements Initializable {

    @FXML
    TableView<Music> musicFiles;

    @FXML
    TableColumn<Music, String> title;

    @FXML
    Label startTime;

    @FXML
    Label endTime;

    @FXML
    Slider positionBar;

    private List<Music> musics = new ArrayList<>();

    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    private AudioMediaPlayerComponent mediaPlayerComponent;
    private String actualPlayMusicPath = "";
    private MediaPlayer player;
    private boolean timeChanging = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File dir = new File("src/main/resources/mp3");
        for (File file : dir.listFiles()) {
            musics.add(new Music(file.getName(), file.getPath()));
        }
        title.setCellValueFactory(new PropertyValueFactory("title"));
        ObservableList<Music> test = FXCollections.observableArrayList(musics);
        musicFiles.setItems(test);
        musicFiles.setRowFactory(tv -> {
            TableRow<Music> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    String data = row.getItem().getPath();
                    playMusic(data);
                }
            });
            return row;
        });

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        mediaPlayerComponent = new AudioMediaPlayerComponent();
        player = mediaPlayerComponent.getMediaPlayer();
        player.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
            }

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> {
                    DateFormat sdf = new SimpleDateFormat("m:ss");
                    endTime.setText(sdf.format(new Date(mediaPlayer.getLength())));
                    positionBar.setMax(mediaPlayer.getLength());
                });
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                Platform.runLater(() -> {
                    DateFormat sdf = new SimpleDateFormat("m:ss");
                    startTime.setText(sdf.format(new Date(newTime)));
                    synchronized (this) {
                        if (!timeChanging)
                            positionBar.setValue(newTime);
                    }
                });
            }
        });

        // Handle Slider value change events.
        /*positionBar.valueProperty().addListener((observable, oldValue, newValue) -> {
             player.setTime(newValue.longValue());
        });*/
    }

    public void playMusic(String path) {
        player.playMedia(path);
        actualPlayMusicPath = path;
    }

    @FXML
    public void handlePlayPauseMusic() {
        if (player.isPlaying())
            player.pause();
        else
            player.play();
    }

    @FXML
    public synchronized void handleMovePositionBar() {
        player.setTime((long) positionBar.getValue());
        timeChanging = false;
    }

    @FXML
    public synchronized void handleStartPositionBar() {
        timeChanging = true;
    }
}
