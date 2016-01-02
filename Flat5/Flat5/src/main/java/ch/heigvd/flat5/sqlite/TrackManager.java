package ch.heigvd.flat5.sqlite;

import ch.heigvd.flat5.api.sound.TrackInfos;
import java.sql.*;
import java.util.*;

public class TrackManager
{
    private Connection connection;

    public TrackManager (SQLiteConnector connector)
    {
        connection = connector.getConnection();
    }


    public String getTrackFileName (String title, String artist)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tracks WHERE title = '" + title + "' " +
                    "AND artist = " + artist + "'");
            while(result.next())
            {
                return result.getString("filename");
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting filename " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public boolean isKnown(String fileName)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM tracks WHERE path = '" + fileName + "'");
            while(result.next())
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while loocking for track " + e.getClass().getName() + ": " + e.getMessage());
        }

        return false;
    }

    public void addTrack (TrackInfos track, String fileName)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement( "INSERT INTO tracks(path, title, artist, " +
                    "album, genre, year, length, urlCover) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, fileName);
            statement.setString(2, track.title);
            statement.setString(3, track.artist);
            statement.setString(4, track.album);
            statement.setString(5, track.genre);
            statement.setString(6, track.year);
            statement.setString(7, track.length);
            statement.setString(8, track.urlCover);
            statement.executeUpdate();
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding track " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

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
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting tracks " + e.getClass().getName() + ": " + e.getMessage());
        }
        return tracks;
    }
}
