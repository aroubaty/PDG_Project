package ch.heigvd.flat5.film.view;


import ch.heigvd.flat5.MainApp2;
import ch.heigvd.flat5.api.video.MovieInfos;
import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.film.player.Player;
import ch.heigvd.flat5.root.view.RootController;
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
    Label infoRuntime;
    @FXML
    Label infoRelease;
    @FXML
    Label infoGenre;
    @FXML
    Label infoPlot;
    @FXML
    Label infoIMDBScore;
    @FXML
    Label infoMetaScore;
    @FXML
    Button launchFilm;
    @FXML
    ImageView infoPoster;

    private List<Movie> movies = new ArrayList<>();
    private Movie currentMovie;
    private RootController rootController;
    private MainApp2 mainApp;
    private BorderPane rootLayout;

    private static final String LIBVLC_PATH = "C:/Program Files/VideoLAN/VLC";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        // Configuration du contenu des colonnes de la TableView
        movieTitle.setCellValueFactory(new PropertyValueFactory("title"));
        movieGenre.setCellValueFactory(new PropertyValueFactory("genre"));
        movieDate.setCellValueFactory(new PropertyValueFactory("date"));
        movieRuntime.setCellValueFactory(new PropertyValueFactory("runtime"));

        // Récupération des films
        SQLiteConnector connector = new SQLiteConnector();
        connector.connectToDB();
        MovieManager manager = new MovieManager(connector);

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
                   if(rootController == null)
                   {
                       System.out.println("yoooolooosadasd");
                   }
                   /* rootController.handlerFilmInfo();
                    // Initialisation des champs de la vue film
                    currentMovie = row.getItem();
                    loadInfos(currentMovie);

                    // Afficher autre vue.
*/
                    //TODO
                    NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), LIBVLC_PATH);
                    if(row.getItem().getInfos() == null)
                    { System.out.println("fdsafdsa"); }
                    System.out.println(row.getItem().getInfos().getPath());
                    SwingUtilities.invokeLater(() -> {
                        Player.getInstance().start("file:///" + row.getItem().getInfos().getPath());
                    });
                }
            });
            return row;
        });
    }

    public void loadInfos(Movie movieToPlay)
    {
        infoRuntime.setText(movieToPlay.getRuntime());
        infoGenre.setText(movieToPlay.getGenre());
        infoRelease.setText(movieToPlay.getDate());
        String imdbRating = movieToPlay.getInfos().getImdbRating();
        if (imdbRating != "" && imdbRating != null) {
            infoIMDBScore.setText(imdbRating + "/10 avec " +
                    movieToPlay.getInfos().getImdbVotes() + " votes");
        } else {
            infoIMDBScore.setText("N/A");
        }

        String metascore = movieToPlay.getInfos().getMetaScore();
        if (metascore != "" && metascore != null) {
            infoMetaScore.setText(metascore + "/10");
        } else {
            infoMetaScore.setText("N/A");
        }

        String plot = movieToPlay.getInfos().getPlot();
        if (plot != "" && plot != null) {
            infoPlot.setText(plot);
        } else {
            infoPlot.setText("Indisponible");
        }

        String imageUrl = movieToPlay.getInfos().getPoster();
        if (imageUrl != "" && imageUrl != null) {
            infoPoster.setImage(new Image(imageUrl));
        }
    }
    public void setRootController(RootController rootController)
    {
        this.rootController = rootController;
    }
    public void setMainApp(MainApp2 mainApp) {
        this.mainApp = mainApp;
        this.rootLayout = mainApp.getRootLayout();
    }
}
