package ch.heigvd.flat5.film.view;


import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.api.video.MovieInfos;
import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.film.player.Player;
import ch.heigvd.flat5.film.player.sync.VideoSyncHandler;
import ch.heigvd.flat5.music.model.Music;
import ch.heigvd.flat5.root.view.RootController;
import ch.heigvd.flat5.sqlite.Contact;
import ch.heigvd.flat5.sqlite.ContactManager;
import ch.heigvd.flat5.sqlite.MovieManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
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
import javafx.scene.text.Text;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FilmController  implements Initializable
{
    @FXML
    TableView<Movie> movieFiles;
    @FXML
    TableColumn<Movie, String> movieTitle;
    @FXML
    TableColumn<Movie, String> movieGenre;
    @FXML
    TableColumn<Movie, String> movieDate;
    @FXML
    TableColumn<Movie, String> movieRuntime;


    @FXML
    ChoiceBox<String> choiceContact;

    private List<Movie> movies = new ArrayList<>();
    private Movie currentMovie;
    private RootController rootController;
    private MainApp2 mainApp;
    private BorderPane rootLayout;

    private ContactManager contactManager;
    private ArrayList<String> contactNames;
    private String lastPath;
    private MovieManager manager;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        contactManager = new ContactManager(sqLiteConnector);
        manager = new MovieManager(sqLiteConnector);

        // Configuration du contenu des colonnes de la TableView
        movieTitle.setCellValueFactory(new PropertyValueFactory("title"));
        movieGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        movieDate.setCellValueFactory(new PropertyValueFactory("date"));
        movieRuntime.setCellValueFactory(new PropertyValueFactory("runtime"));


        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));

        // Récupération des films
        for(MovieInfos infos : manager.getMovies())
        {
            movies.add(new Movie(infos));
        }
        // Chargement des films dans la TableView
        ObservableList<Movie> movieList = FXCollections.observableArrayList(movies);
        movieFiles.setItems(movieList);

        // Changement de vue et chargement des informations d'un film lors d'un double clique sur l'un d'eux.
        movieFiles.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    currentMovie = row.getItem();
                    rootController.handlerFilmInfo(currentMovie);
                }
            });
            return row;
        });
    }

    public void setRootController(RootController rootController)
    {
        this.rootController = rootController;
    }

    public void setMainApp(MainApp2 mainApp)
    {
        this.mainApp = mainApp;
        this.rootLayout = mainApp.getRootLayout();
    }


    @FXML
    private void handleWaitForAFriend()
    {

    }
    @FXML
    private void handleConnectionToFriend()
    {

    }

    @FXML
    private void choiceBoxRequested() {
        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));
    }

    @FXML
    private void connectButtonClicked() {
        for (Contact c: contactManager.getContacts()) {
            if (c.getName().equals(choiceContact.getValue())) {
                System.out.println("connected to " + c.getAddress());
                VideoSyncHandler.getInstance().connect(c.getAddress());
            }
        }
    }

    @FXML
    private void acceptConnectionButtonClicked() {
        System.out.println("waiting for connection");
        VideoSyncHandler.getInstance().waitForConnection();
    }

    public void scanVideoFiles()
    {
        boolean scan = false;
        if(mainApp != null)
        {
            if(lastPath == null || mainApp.getPath().equals(lastPath))
            {
                lastPath = mainApp.getPath();
                scan = true;
            }
        }
        else
        {
            scan = true;
        }

        if(scan)
        {
            movies.clear();
            // Récupération des films
            for(MovieInfos infos : manager.getMovies())
            {
                movies.add(new Movie(infos));
            }
            // Chargement des films dans la TableView
            ObservableList<Movie> movieList = FXCollections.observableArrayList(movies);
            movieFiles.setItems(movieList);
        }
    }
}
