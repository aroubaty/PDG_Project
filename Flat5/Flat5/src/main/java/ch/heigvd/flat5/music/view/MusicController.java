/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.music.view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ch.heigvd.flat5.music.model.Music;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.jna.NativeLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

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

    private List<Music> musics = new ArrayList<>();

    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    private AudioMediaPlayerComponent mediaPlayerComponent;
    private String actualPlayMusicPath = "";

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
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
            }
        });
    }

    public void playMusic(String path) {
        mediaPlayerComponent.getMediaPlayer().playMedia(path);
        actualPlayMusicPath = path;
    }

    @FXML
    public void handlePlayMusic() {
        if(musicFiles.getSelectionModel().getSelectedItem().getPath() == actualPlayMusicPath) {
            mediaPlayerComponent.getMediaPlayer().play();
        } else {
            playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath());
        }
    }

    @FXML
    public void handlePauseMusic() {
        mediaPlayerComponent.getMediaPlayer().pause();
    }
}
