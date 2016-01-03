package ch.heigvd.flat5.sqlite;

import ch.heigvd.flat5.api.sound.TrackInfos;
import java.sql.*;
import java.util.*;

/**
 * Classe permettant de gérer les médias de type audio de la base de données.
 *
 * @author Jan Purro
 */
public class TrackManager
{
    // Connection à la base de données de l'application.
    private Connection connection;

    /**
     * Construit un nouveau TrackManager
     * @param connector Un objet de type SQLiteConnector. Il doit lui-même être déjà connecté à la base de données.
     */
    public TrackManager (SQLiteConnector connector)
    {
        connection = connector.getConnection();
    }


    /**
     * Vérifier si le fichier audio est connu de la base de données.
     * @param path Le chemin du fichier en question.
     * @return true si le fichier est connue, false sinon.
     */
    public boolean isKnown(String path)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tracks WHERE path = '" + path + "'");
            while(result.next())
            {
                return true;
            }
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while loocking for track " + e.getClass().getName() + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Ajoute une musique dans la base de données.
     * @param track Les informations du fichier audio.
     * @param path Le chemin du fichier audio.
     */
    public void addTrack (TrackInfos track, String path)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement( "INSERT INTO tracks(path, title, artist, " +
                    "album, genre, year, length, urlCover) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, path);
            statement.setString(2, track.title);
            statement.setString(3, track.artist);
            statement.setString(4, track.album);
            statement.setString(5, track.genre);
            statement.setString(6, track.year);
            statement.setString(7, track.length);
            statement.setString(8, track.urlCover);
            statement.executeUpdate();
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding track " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Retourne l'ensemble des fichiers audios contenus dans la base de données.
     * @return La liste des fichier audios.
     */
    public List<TrackInfos> getTracks()
    {
        List<TrackInfos> tracks = new LinkedList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tracks");
            while(result.next())
            {
                TrackInfos track = new TrackInfos(result.getString("title"),
                        result.getString("artist"), result.getString("album"),
                        result.getString("genre"), result.getString("year"),
                        result.getString("length"), result.getString("urlCover"));
                track.path = result.getString("path");
                tracks.add(track);

            }
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting tracks " + e.getClass().getName() + ": " + e.getMessage());
        }
        return tracks;
    }
}
