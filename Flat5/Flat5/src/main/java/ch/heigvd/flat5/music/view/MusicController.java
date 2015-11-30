/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heigvd.flat5.music.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import ch.heigvd.flat5.music.model.Music;
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
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

// Problème avec le slider qui chope la mauvaise valeur sur le Slider lorsque la souris est lachée
// Regarder les évennements .

// ORGANISER LE CODE !!

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

    private List<Music> musics = new ArrayList<>();

    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    private AudioMediaPlayerComponent mediaPlayerComponent;
    private String actualPlayMusicPath = "";
    private int actualRowIndex;
    private MediaPlayer player;
    private boolean timeChanging = false;
    private Image playImage;
    private Image pauseImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("Working Directory = " +
                //System.getProperty("user.dir"));
        ClassLoader cl = getClass().getClassLoader();
        playImage = new Image(cl.getResourceAsStream("img/play.png"));
        pauseImage = new Image(cl.getResourceAsStream("img/pause.png"));
        File dir = new File("src/main/resources/mp3");
        for (File file : dir.listFiles()) {
            MP3File mp3File = null;
            try {
                mp3File = (MP3File) AudioFileIO.read(file);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }
            Tag tag = mp3File.getTag();
            musics.add(new Music(tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.ARTIST),
                    tag.getFirst(FieldKey.ALBUM), tag.getFirst(FieldKey.GENRE), tag.getFirst(FieldKey.YEAR),
                    mp3File.getMP3AudioHeader().getTrackLengthAsString(), file.getPath()));
        }
        title.setCellValueFactory(new PropertyValueFactory("title"));
        artist.setCellValueFactory(new PropertyValueFactory("artist"));
        album.setCellValueFactory(new PropertyValueFactory("album"));
        genre.setCellValueFactory(new PropertyValueFactory("genre"));
        year.setCellValueFactory(new PropertyValueFactory("year"));
        length.setCellValueFactory(new PropertyValueFactory("length"));

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

    private void playMusic(String path) {
        actualRowIndex = musicFiles.getSelectionModel().getFocusedIndex();
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
