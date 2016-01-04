package ch.heigvd.flat5.serie.view;

import ch.heigvd.flat5.api.video.MovieInfos;
import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.film.player.Player;
import ch.heigvd.flat5.serie.model.Episode;
import ch.heigvd.flat5.sqlite.ContactManager;
import ch.heigvd.flat5.sqlite.MovieManager;
import ch.heigvd.flat5.sqlite.SQLiteConnector;
import com.sun.jna.NativeLibrary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by oem on 02/01/16.
 */
public class SerieInfoController implements Initializable {
    @FXML
    Label serieRuntime;
    @FXML
    Label serieGenre;
    @FXML
    Label serieScore;
    @FXML
    Label serieRelease;
    @FXML
    Label serieMetaScore;
    @FXML
    ImageView seriePoster;
    @FXML
    Text seriePlot;

    @FXML
    TableView<Episode> episodesView;
    @FXML
    TableColumn<Episode, String> episodeNumber;
    @FXML
    TableColumn<Episode, String> episodeTitle;
    @FXML
    TableColumn<Episode, String> episodeSeason;

    Movie currentSerie;
    Episode currentEpisode;
    private static final String LIBVLC_PATH = "src/main/resources/vlc";
    private MovieManager manager;
    private List<Episode> episodes = new ArrayList<>();

    public void setSerie(Movie serieToPlay) {

        currentSerie = serieToPlay;

        serieRuntime.setText(serieToPlay.getRuntime());
        serieGenre.setText(serieToPlay.getGenre());
        serieRelease.setText(serieToPlay.getDate());
        String imdbRating = serieToPlay.getInfos().getImdbRating();
        if (imdbRating != null && !imdbRating.isEmpty() && !imdbRating.equals("N/A"))
        {
            serieScore.setText(imdbRating + "/10 avec " +
                    serieToPlay.getInfos().getImdbVotes() + " votes");
        }

        else
        {
            serieScore.setText("N/A");
        }

        String metascore = serieToPlay.getInfos().getMetaScore();
        if (metascore != null && !metascore.isEmpty() && !metascore.equals("N/A"))
        {
            serieMetaScore.setText(metascore + "/10");
        }

        else
        {
            serieMetaScore.setText("N/A");
        }

        String plot = serieToPlay.getInfos().getPlot();
        if (plot != null && !plot.isEmpty() && !plot.equals("N/A"))
        {
            seriePlot.setText(plot);
        }
        else
        {
            seriePlot.setText("Pas de résumé disponible");
        }

        String imageUrl = serieToPlay.getInfos().getPoster();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("N/A"))
        {
            seriePoster.setImage(new Image(imageUrl));
        }
        else
        {
            ClassLoader cl = getClass().getClassLoader();
            seriePoster.setImage(new Image(cl.getResourceAsStream("img/no_found.jpg")));
        }

        episodes.clear();
        // Récupération des films
        for(Episode episode : manager.getSerieEpisodes(currentSerie.getInfos().getDbID()))
        {
            episodes.add(episode);
        }
        // Chargement des films dans la TableView
        ObservableList<Episode> episodeList = FXCollections.observableArrayList(episodes);
        episodesView.setItems(episodeList);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        SQLiteConnector sqLiteConnector = new SQLiteConnector();
        sqLiteConnector.connectToDB();
        sqLiteConnector.initDB();
        manager = new MovieManager(sqLiteConnector);


        // Configuration du contenu des colonnes de la TableView
        episodeNumber.setCellValueFactory(new PropertyValueFactory("number"));
        episodeTitle.setCellValueFactory(new PropertyValueFactory("title"));
        episodeSeason.setCellValueFactory(new PropertyValueFactory("season"));

        // Lancement d'un épisode lors d'un double clique sur l'un d'eux.
        episodesView.setRowFactory(tv -> {
            TableRow<Episode> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    currentEpisode = row.getItem();
                    launchFilm();
                }
            });
            return row;
        });
    }

    @FXML
    public void launchFilm()
    {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), LIBVLC_PATH);
        SwingUtilities.invokeLater(() -> {
            //Player.getInstance().start("file:///" + currentEpisode.getPath());
        });
    }
}
