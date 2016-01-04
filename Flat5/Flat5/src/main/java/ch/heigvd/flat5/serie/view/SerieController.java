package ch.heigvd.flat5.serie.view;

import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.api.video.MovieInfos;
import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.root.view.RootController;
import ch.heigvd.flat5.sqlite.Contact;
import ch.heigvd.flat5.sqlite.ContactManager;
import ch.heigvd.flat5.sqlite.MovieManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by oem on 02/01/16.
 */
public class SerieController implements Initializable
{

    @FXML
    TableView<Movie> serieFiles;
    @FXML
    TableColumn<Movie, String> serieTitle;
    @FXML
    TableColumn<Movie, String> serieGenre;
    @FXML
    TableColumn<Movie, String> serieDate;


    @FXML
    ChoiceBox<String> choiceContact;

    private List<Movie> series = new ArrayList<>();
    private Movie currentSerie;
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

        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));

        // Configuration du contenu des colonnes de la TableView
        serieTitle.setCellValueFactory(new PropertyValueFactory("title"));
        serieGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        serieDate.setCellValueFactory(new PropertyValueFactory("date"));

        // Récupération des séries
        for(MovieInfos infos : manager.getSeries())
        {
            series.add(new Movie(infos));
        }
        // Chargement des films dans la TableView
        ObservableList<Movie> movieList = FXCollections.observableArrayList(series);
        serieFiles.setItems(movieList);

        serieFiles.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    currentSerie = row.getItem();
                    rootController.handlerSerieInfo(currentSerie);
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
    private void handleWaitForAFriend() {

    }

    @FXML
    private void handleConnectionToFriend() {

    }

    @FXML
    private void choiceBoxRequested() {
        contactNames = new ArrayList<>();
        for (Contact contact : contactManager.getContacts()) {
            contactNames.add(contact.getName());
        }
        choiceContact.setItems(FXCollections.observableArrayList(contactNames));
    }

    public void scanSeries()
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
            series.clear();
            // Récupération des séries
            for(MovieInfos infos : manager.getSeries())
            {
                series.add(new Movie(infos));
            }
            // Chargement des films dans la TableView
            ObservableList<Movie> movieList = FXCollections.observableArrayList(series);
            serieFiles.setItems(movieList);
        }
    }
}