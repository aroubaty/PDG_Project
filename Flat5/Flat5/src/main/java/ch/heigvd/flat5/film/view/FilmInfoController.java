package ch.heigvd.flat5.film.view;

import ch.heigvd.flat5.film.model.Movie;
import ch.heigvd.flat5.film.player.Player;
import com.sun.jna.NativeLibrary;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by oem on 02/01/16.
 */
public class FilmInfoController implements Initializable {
    @FXML
    Label infoRuntime;
    @FXML
    Label infoRelease;
    @FXML
    Label infoGenre;
    @FXML
    Label infoIMDBScore;
    @FXML
    Label infoMetaScore;
    @FXML
    Button launchFilm;
    @FXML
    ImageView infoPoster;

    @FXML
    Text synop ;

    Movie currentMovie;
    private static final String LIBVLC_PATH = "src/main/resources/vlc";

    public void setMovie(Movie movieToPlay) {

        currentMovie = movieToPlay;

        infoRuntime.setText(movieToPlay.getRuntime());
        infoGenre.setText(movieToPlay.getGenre());
        infoRelease.setText(movieToPlay.getDate());
        String imdbRating = movieToPlay.getInfos().getImdbRating();
        if (imdbRating != null && !imdbRating.isEmpty() && !imdbRating.equals("N/A"))
        {
            infoIMDBScore.setText(imdbRating + "/10 avec " +
                    movieToPlay.getInfos().getImdbVotes() + " votes");
        }

        else
        {
            infoIMDBScore.setText("N/A");
        }

        String metascore = movieToPlay.getInfos().getMetaScore();
        if (metascore != null && !metascore.isEmpty() && !metascore.equals("N/A"))
        {
            infoMetaScore.setText(metascore + "/10");
        }

        else
        {
            infoMetaScore.setText("N/A");
        }

        String plot = movieToPlay.getInfos().getPlot();
        if (plot != null && !plot.isEmpty() && !plot.equals("N/A"))
        {
            synop.setText(plot);
        }
        else
        {
            synop.setText("Pas de résumé disponible");
        }

        String imageUrl = movieToPlay.getInfos().getPoster();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("N/A"))
        {
            System.out.println(imageUrl);
            infoPoster.setImage(new Image(imageUrl));
        }
        else
        {
            ClassLoader cl = getClass().getClassLoader();
            infoPoster.setImage(new Image(cl.getResourceAsStream("img/no_found.jpg")));

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    @FXML
    public void launchFilm()
    {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), LIBVLC_PATH);
        SwingUtilities.invokeLater(() -> {
            Player.getInstance().start("file:///" + currentMovie.getInfos().getPath());
        });
    }
}
