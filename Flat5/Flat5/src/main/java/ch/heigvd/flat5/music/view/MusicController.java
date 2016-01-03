package ch.heigvd.flat5.music.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import ch.heigvd.flat5.AppConfig;
import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.music.model.util.MusicBrowser;
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
 * Classe contrôleur FXML pour la vue Music.fxml
 *
 * @author Jérôme
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
    ImageView syncImage;

    @FXML
    HBox connDemand;
    @FXML
    Button demandYes;
    @FXML
    Button demandNo;
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
    private MusicBrowser musicBrowser;
    private MainApp2 mainApp;
    private String lastPath;

    // Partie synchronisation
    private SyncManager syncManager;
    private SyncHandler handler;
    private boolean synch;

    // Partie SQLite
    private ContactManager contactManager;
    private ArrayList<String> contactNames;


    private static final String NATIVE_LIBRARY_SEARCH_PATH = "src/main/resources/vlc_library";

    /**
     * Initialisation de la classe contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Connection à la base SQLite
        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        contactManager = new ContactManager(sqLiteConnector);

        // Configuration de la synchronisation
        handler = new MusicSyncHandler(this);
        syncManager = SyncManager.getInstance();
        syncManager.setHandler(handler);


        // Chargement des images pour les boutons
        ClassLoader cl = getClass().getClassLoader();
        playImage = new Image(cl.getResourceAsStream("img/play.png"));
        pauseImage = new Image(cl.getResourceAsStream("img/pause.png"));
        sync = new Image(cl.getResourceAsStream("img/sync.png"));

        // Configuration du contenu des colonnes de la TableView
        title.setCellValueFactory(new PropertyValueFactory("title"));
        artist.setCellValueFactory(new PropertyValueFactory("artist"));
        album.setCellValueFactory(new PropertyValueFactory("album"));
        genre.setCellValueFactory(new PropertyValueFactory("genre"));
        year.setCellValueFactory(new PropertyValueFactory("year"));
        length.setCellValueFactory(new PropertyValueFactory("length"));

        // Récupérations des musiques
        musicBrowser = new MusicBrowser(AppConfig.EXTS_SUPPORT);
        scanMusicFiles();

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

            // Lecture d'une musique
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                // Affiche de la longueur de la musique
                Platform.runLater(() -> {
                    playPauseImage.setImage(pauseImage);
                    DateFormat sdf = new SimpleDateFormat("m:ss");
                    endTime.setText(sdf.format(new Date(mediaPlayer.getLength())));
                    positionBar.setMax(mediaPlayer.getLength());
                });
            }

            // Mise en pause d'une musique
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

    /**
     * Récupère la musique souhaitée selon son chemin
     *
     * @param path
     * @return la musique ou null
     */
    public Music getMusicFromPath(String path) {
        for (Music music : musics) {
            if (music.getPath().equals(path))
                return music;
        }
        return null;
    }

    /**
     * Joue une musique à partir de son path
     *
     * @param path
     * @param notify informe l'ami
     */
    public void playMusic(String path, boolean notify) {

        // Informe l'ami
        if (synch && notify) {
            String[] split = path.split("/");
            syncManager.begin(split[split.length - 1]);
        }


        actualRowIndex = musicFiles.getSelectionModel().getFocusedIndex();

        // Affichage des tags de la musique
        Music toPlay = getMusicFromPath(path);
        Platform.runLater(() -> {
            titleDisplay.setText(toPlay.getTitle());
            artistDisplay.setText(toPlay.getArtist());
            albumDisplay.setText(toPlay.getAlbum());
            coverDisplay.setImage(toPlay.getCover());
        });

        // Lecture de la musique par le player vlcj
        player.playMedia(path);
        actualPlayMusicPath = path;
    }

    /**
     * Fermetture du lecteur vlcj
     */
    public void exit() {
        playerComponent.release();
    }

    /**
     * Récupération des musiques présentes dans la db
     */
    public void scanMusicFiles() {
        boolean scan = false;
        if(mainApp != null) {
            if(mainApp.getPath() != lastPath) {
                lastPath = mainApp.getPath();
                scan = true;
            }
        } else {
            scan = true;
        }

        if(scan) {
            musics = musicBrowser.getMusics();

            // Chargement des musiques dans la TableView
            ObservableList<Music> test = FXCollections.observableArrayList(musics);
            musicFiles.setItems(test);
        }
    }

    /**
     * Echec de l'attente d'un ami
     */
    private void failWaiting() {
        btnAttente.setDisable(false);
        progressIndicator.setVisible(false);
    }

    /**
     * Termine une synchronisation
     *
     * @param notify infrome l'ami
     */
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

    /**
     * Informe le système qu'on est synchronisé
     */
    public void syncThePlayer() {
        synch = true;
    }

    /**
     * Affiche un état, il peut être :
     * - En attente de synchronisation, dans ce cas, on affiche la fenêtre d'autorisation/rejet d'une synchronisation
     * - Connecté, dans ce cas, on affiche avec qui il est synchronisé ainsi que la possiblité d'y sortir
     *
     * @param address
     * @param connected
     */
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

    /**
     * Getter for property 'player'.
     *
     * @return Value for property 'player'.
     */
    public MediaPlayer getPlayer() {
        return player;
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
     * Setter for property 'mainApp'.
     *
     * @param mainApp Value to set for property 'mainApp'.
     */
    public void setMainApp(MainApp2 mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Action du bouton Play/Pause
     */
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

    /**
     * Action du bouton Suivant
     */
    @FXML
    public void handleNextSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectNext();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath(), true);
    }

    /**
     * Action du bouton Précédent
     */
    @FXML
    public void handlePreviousSong() {
        musicFiles.getSelectionModel().select(actualRowIndex);
        musicFiles.getSelectionModel().selectPrevious();
        playMusic(musicFiles.getSelectionModel().getSelectedItem().getPath(), true);
    }

    /**
     * Action du bouton Connection à un ami (éclair)
     */
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

    /**
     * Action du bouton Attente d'un ami (sync)
     */
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

    /**
     * Action du bouton Autorisation d'un ami
     */
    @FXML
    private void handleYesDemand() {
        syncManager.acceptInvitation();
    }

    /**
     * Action du bouton Rejet d'un ami
     */
    @FXML
    private void handleNoDemand() {
        syncManager.denyInvitation();
    }

    /**
     * Action du bouton Fin d'une synchronisation
     */
    @FXML
    private void handleStopSync() {
        unsyncThePlayer(true);
    }

    /**
     * Action d'une requête sur la boîte de choix d'un ami
     * Permet de rafraichir la liste des amis si entre temps des amis aurait été ajoutés
     */
    @FXML
    private void choiceBoxRequested() {
        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));
    }
}
