/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.music.view;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.music.model.util.MusicBrowser;
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
import javafx.scene.layout.BorderPane;
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

    @FXML
    TableColumn<Music, String> artist;

    @FXML
    TableColumn<Music, String> album;

    @FXML
    TableColumn<Music, String> genre;

    @FXML
    TableColumn<Music, String> year;

    @FXML
    TableColumn<Music, String> length;

    @FXML
    Label startTime;

    @FXML
    Label endTime;

    @FXML
    Slider positionBar;

    @FXML
    ImageView playPauseImage;

    @FXML
    Label titleDisplay;

    @FXML
    Label artistDisplay;

    @FXML
    Label albumDisplay;

    @FXML
    ImageView coverDisplay;

    private List<Music> musics = new ArrayList<>();
    private String actualPlayMusicPath = "";
    private int actualRowIndex;
    private AudioMediaPlayerComponent playerComponent;
    private MediaPlayer player;
    private Image playImage;
    private Image pauseImage;
    private MusicBrowser musicBrowser;

    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Chargement des images pour les boutons
        ClassLoader cl = getClass().getClassLoader();
        playImage = new Image(cl.getResourceAsStream("img/play.png"));
        pauseImage = new Image(cl.getResourceAsStream("img/pause.png"));

        // Configuration du contenu des colonnes de la TableView
        title.setCellValueFactory(new PropertyValueFactory("title"));
        artist.setCellValueFactory(new PropertyValueFactory("artist"));
        album.setCellValueFactory(new PropertyValueFactory("album"));
        genre.setCellValueFactory(new PropertyValueFactory("genre"));
        year.setCellValueFactory(new PropertyValueFactory("year"));
        length.setCellValueFactory(new PropertyValueFactory("length"));

        // Récupérations des musiques
        musicBrowser = new MusicBrowser(new String[]{".mp3", ".ogg", ".flac", ".wav"});
        scanMusicFiles("src/main/resources/audio_files");

        // Configuration de l'action du double-clique sur une musique
        musicFiles.setRowFactory(tv -> {
            TableRow<Music> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Music toPlay = row.getItem();
                    String data = toPlay.getPath();
                    titleDisplay.setText(toPlay.getTitle());
                    artistDisplay.setText(toPlay.getArtist());
                    albumDisplay.setText(toPlay.getAlbum());
                    coverDisplay.setImage(toPlay.getCover());
                    playMusic(data);
                }
            });
            return row;
        });

        // Chargement de la librairie vlcj et création du lecteur vlcj
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        playerComponent = new AudioMediaPlayerComponent();
        player = playerComponent.getMediaPlayer();

        // Définition des actions sur l'interface lors des évènements du lecteur
        player.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void playing(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> {
                    playPauseImage.setImage(pauseImage);
                    DateFormat sdf = new SimpleDateFormat("m:ss");
                    endTime.setText(sdf.format(new Date(mediaPlayer.getLength())));
                    positionBar.setMax(mediaPlayer.getLength());
                });
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                Platform.runLater(() -> {
                    playPauseImage.setImage(playImage);
                });
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                Platform.runLater(() -> {
                    DateFormat sdf = new SimpleDateFormat("m:ss");
                    startTime.setText(sdf.format(new Date(newTime)));
                    positionBar.setValue(newTime);
                });
            }
        });

        // Gestion du changement de valeur du slider
        positionBar.valueProperty().addListener((observable, oldValue, newValue) -> {

            // Détection d'un déplacement réalisé par un utilisateur et pas par l'avancement automatique
            if ((double) newValue % 1 != 0) {
                player.setTime(newValue.longValue());
            }
        });
    }

    public void playMusic(String path) {
        actualRowIndex = musicFiles.getSelectionModel().getFocusedIndex();
        player.playMedia(path);
        actualPlayMusicPath = path;
    }

    public void exit() {
        playerComponent.release();
    }

    public void scanMusicFiles(String path) {
        musics = musicBrowser.getMusicsInFolder(path);

        // Chargement des musiques dans la TableView
        ObservableList<Music> test = FXCollections.observableArrayList(musics);
        musicFiles.setItems(test);
    }

    @FXML
    public void handlePlayPauseMusic() {
        if (player.isPlaying())
            player.pause();
        else
            player.play();
    }

    @FXML
    public void handleNextSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectNext();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath());
    }

    @FXML
    public void handlePreviousSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectPrevious();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath());
    }
}
