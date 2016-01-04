package ch.heigvd.flat5.sqlite;

import ch.heigvd.flat5.api.sound.TrackInfos;

import java.io.File;
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
            List<Integer> toDelete = new LinkedList<>();
            File file;

            while(result.next())
            {
                 /* On vérifie que la série existe encore dans la collection, si ce n'est pas le cas on l'enlève de la
                    base de données. Sinon on l'ajoute à la liste des séries. */
                file = new File(result.getString("path"));
                if(file.exists())
                {
                    TrackInfos track = new TrackInfos(result.getString("title"),
                            result.getString("artist"), result.getString("album"),
                            result.getString("genre"), result.getString("year"),
                            result.getString("length"), result.getString("urlCover"));
                    track.path = result.getString("path");
                    tracks.add(track);
                }

                else
                {
                    toDelete.add(result.getInt("id"));
                }

                //Suppressions de la base de données des infromations dont le fichier n'existe plus.
                for(Integer i : toDelete)
                {
                    String query = "DELETE FROM tracks WHERE id = " + i.toString();
                    statement.executeUpdate(query);
                }

            }
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting tracks " + e.getClass().getName() + ": " + e.getMessage());
        }
        return tracks;
    }

    /**
     * Retourn un path correspondant à un fichier parmi les films et les épisodes.
     * @param filename le nom du fichier recherché.
     * @return Le path complet du fichier ou null, s'il n'a pas été trouvé.
     */
    public String findPathFromFile(String filename)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tracks WHERE path LIKE '%" +
                    filename + "';");
            while(result.next())
            {
                return result.getString("path");
            }
            statement.close();
        }

        catch ( Exception e )
        {
            System.err.println("Error while getting movies " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
}
