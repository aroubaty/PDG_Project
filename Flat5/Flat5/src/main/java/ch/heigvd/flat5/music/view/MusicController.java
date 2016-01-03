package ch.heigvd.flat5.music.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.music.model.util.MusicBrowser;
import ch.heigvd.flat5.music.sync.MusicSyncController;
import ch.heigvd.flat5.music.sync.MusicSyncHandler;
import ch.heigvd.flat5.sqlite.Contact;
import ch.heigvd.flat5.sqlite.ContactManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import ch.heigvd.flat5.sync.SyncHandler;
import ch.heigvd.flat5.sync.SyncManager;
import com.google.common.net.InetAddresses;
import com.sun.jna.NativeLibrary;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    @FXML
    Button syncButton;
    @FXML
    ImageView syncImage;

    @FXML
    HBox connDemand;
    @FXML
    Button demandYes;
    @FXML
    Button demandNo;
    @FXML
    HBox connStatus;
    @FXML
    Button statusDisconnect;
    @FXML
    Label synchStatus;
    @FXML
    ChoiceBox<String> choiceContact;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Button btnAttente;
    @FXML
    Button btnConnection;
    @FXML
    Label status;
    @FXML
    Button stopSynch;
    @FXML
    ProgressIndicator progressIndicator2;

    private List<Music> musics = new ArrayList<>();
    private String actualPlayMusicPath = "";
    private int actualRowIndex;
    private AudioMediaPlayerComponent playerComponent;
    private MediaPlayer player;
    private Image playImage;
    private Image pauseImage;
    private Image sync;
    private Image isSync;
    private MusicBrowser musicBrowser;

    //Sync part
    private SyncManager syncManager;
    private SyncHandler handler;
    private boolean synch;
    private boolean isSynch = false;

    //SQLite
    private ContactManager contactManager;
    private ArrayList<String> contactNames;


    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        contactManager = new ContactManager(sqLiteConnector);

        //Sync part
        handler = new MusicSyncHandler(this);
        syncManager = SyncManager.getInstance();
        syncManager.setHandler(handler);


        // Chargement des images pour les boutons
        ClassLoader cl = getClass().getClassLoader();
        playImage = new Image(cl.getResourceAsStream("img/play.png"));
        pauseImage = new Image(cl.getResourceAsStream("img/pause.png"));
        sync = new Image(cl.getResourceAsStream("img/sync.png"));
        isSync = new Image(cl.getResourceAsStream("img/isSync.png"));

        // Configuration du contenu des colonnes de la TableView
        title.setCellValueFactory(new PropertyValueFactory("title"));
        artist.setCellValueFactory(new PropertyValueFactory("artist"));
        album.setCellValueFactory(new PropertyValueFactory("album"));
        genre.setCellValueFactory(new PropertyValueFactory("genre"));
        year.setCellValueFactory(new PropertyValueFactory("year"));
        length.setCellValueFactory(new PropertyValueFactory("length"));

        // Récupérations des musiques
        musicBrowser = new MusicBrowser(AppConfig.EXTS_SUPPORT);
        scanMusicFiles(AppConfig.MUSIC_DIRECTORY);

        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));

        // Configuration de l'action du double-clique sur une musique
        musicFiles.setRowFactory(tv -> {
            TableRow<Music> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    playMusic(row.getItem().getPath(), true);
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
                Platform.runLater(() -> playPauseImage.setImage(playImage));
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

                if (synch)
                    syncManager.setAt((int) (newValue.longValue() / 1000.0));
            }
        });
    }

    public Music getMusicFromPath(String path) {
        for (Music music : musics) {
            if (music.getPath().equals(path))
                return music;
        }
        return null;
    }

    public void playMusic(String path, boolean notify) {
        if (synch && notify) {
            String[] split = path.split("/");
            syncManager.begin(split[split.length - 1]);
        }

        actualRowIndex = musicFiles.getSelectionModel().getFocusedIndex();
        Music toPlay = getMusicFromPath(path);

        Platform.runLater(() -> {
            titleDisplay.setText(toPlay.getTitle());
            artistDisplay.setText(toPlay.getArtist());
            albumDisplay.setText(toPlay.getAlbum());
            coverDisplay.setImage(toPlay.getCover());
        });

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

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    @FXML
    public void handlePlayPauseMusic() {
        if (player.isPlaying()) {
            player.pause();

            if (synch)
                syncManager.pause();
        } else {
            player.play();

            if (synch)
                syncManager.play();
        }
    }

    @FXML
    public void handleNextSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectNext();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath(), true);
    }

    @FXML
    public void handlePreviousSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectPrevious();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath(), true);
    }

    @FXML
    private void handleConnectionToFriend() {
        if (choiceContact.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur connexion ami");
            alert.setHeaderText("Vous devez selectionner un ami. Si vous en n'avez pas, vous pouvez en créer depuis les paramètres.");
            alert.showAndWait();
            return;
        }

        String ip = contactManager.getContacts().get(choiceContact.getSelectionModel().getSelectedIndex()).getAddress();

        btnConnection.setDisable(true);
        progressIndicator2.setVisible(true);
        Thread t = new Thread(() -> {
            if (!syncManager.connect(ip, AppConfig.DEFAULT_PORT, this)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur connexion ami");
                    alert.setHeaderText("Impossible de se connecter à cet ami.");
                    alert.showAndWait();
                });
            } else {
                Platform.runLater(() -> {
                    synchStatus.setText("Activée");
                    btnAttente.setDisable(true);
                    btnConnection.setDisable(true);
                    choiceContact.setDisable(true);
                    synch = true;
                    displayStatus(ip, true);
                });
            }
            btnConnection.setDisable(false);
            progressIndicator2.setVisible(false);
        });
        t.start();

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        if (t.isAlive()) {
                            t.stop();
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur connexion ami");
                                alert.setHeaderText("Temps de 30s écoulé.");
                                alert.showAndWait();
                                btnConnection.setDisable(false);
                                progressIndicator2.setVisible(false);
                            });
                            syncManager.resetSyncManager();
                        }
                    }

                }, 30000);

    }

    private void failWaiting() {
        btnAttente.setDisable(false);
        progressIndicator.setVisible(false);
    }

    @FXML
    private void handleWaitForAFriend() {
        btnAttente.setDisable(true);
        progressIndicator.setVisible(true);
        Thread t = new Thread(() -> {
            if (!syncManager.accept(MusicController.this)) {
                failWaiting();
            } else {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    synchStatus.setText("Activée");
                    btnConnection.setDisable(true);
                    choiceContact.setDisable(true);
                    synch = true;
                });
            }
        });
        t.start();
        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        if (t.isAlive()) {
                            t.stop();
                            failWaiting();
                            syncManager.resetSyncManager();
                        }
                    }

                }, 30000);
    }

    @FXML
    public void handleSync() {
        if (isSynch) {
            unsyncThePlayer(true);
            return;
        }

        // Music view
        FXMLLoader loader = new FXMLLoader();

        try {
            loader.setLocation(new File("src/main/java/ch/heigvd/flat5/music/sync/MusicSync.fxml").toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Stage syncStage = new Stage();
        syncStage.setTitle("Gestion de la synchronisation");
        syncStage.initModality(Modality.WINDOW_MODAL);
        //syncStage.initOwner(primaryStage);

        Parent syncPane = null;
        try {
            syncPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(syncPane);
        syncStage.setScene(scene);

        // Set the person into the controller.
        MusicSyncController controller = loader.getController();
        controller.setMusicController(this);
        /*controller.setDialogStage(dialogStage);
        controller.setPerson(person);*/

        // Show the dialog and wait until the user closes it
        syncStage.showAndWait();
    }

    public void unsyncThePlayer(boolean notify) {
        if (notify) {
            syncManager.bye();
        }

        syncManager.resetSyncManager();
        synch = false;
        Platform.runLater(() -> {
            synchStatus.setText("Desactivée");
            choiceContact.setDisable(false);
            btnConnection.setDisable(false);
            btnAttente.setDisable(false);
            connDemand.setVisible(false);
        });
    }

    public void syncThePlayer() {
        synch = true;
        isSynch = true;
        syncImage.setImage(isSync);
    }

    /**
     * Getter for property 'syncManager'.
     *
     * @return Value for property 'syncManager'.
     */
    public SyncManager getSyncManager() {
        return syncManager;
    }

    /**
     * Setter for property 'isSynch'.
     *
     * @param isSynch Value to set for property 'isSynch'.
     */
    public void setIsSynch(boolean isSynch) {
        this.isSynch = isSynch;
    }

    public void displayStatus(String address, boolean connected) {
        final String name;
        Contact contact = contactManager.getContactFromAddress(address);
        if (contact == null)
            name = address;
        else
            name = contact.getName();

        connDemand.setVisible(true);
        if (connected) {
            Platform.runLater(() -> {
                demandNo.setVisible(false);
                demandYes.setVisible(false);
                status.setText("Connecté avec " + name + "...");
                stopSynch.setVisible(true);
            });
        } else {

            Platform.runLater(() -> {
                status.setText(name + " désire synchroniser de la musique avec vous.");
                demandNo.setVisible(true);
                demandYes.setVisible(true);
                stopSynch.setVisible(false);
            });
        }
    }

    @FXML
    private void handleYesDemand() {
        syncManager.acceptInvitation();
    }

    @FXML
    private void handleNoDemand() {
        syncManager.denyInvitation();
    }

    @FXML
    private void handleStopSync() {
        unsyncThePlayer(true);
    }

    @FXML
    private void choiceBoxRequested() {
        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));
    }
}
