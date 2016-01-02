package ch.heigvd.flat5.sqlite;

import ch.heigvd.flat5.api.video.Episode;
import ch.heigvd.flat5.api.video.MovieInfos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class MovieManager
{
    private Connection connection;

    public MovieManager(SQLiteConnector connector)
    {
        connection = connector.getConnection();
    }


    public boolean isKnown(String fileName)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies WHERE path = '" + fileName + "'");
            while(result.next())
            {
                return true;
            }

            result = statement.executeQuery("SELECT * FROM episodes WHERE path = '" + fileName + "'");
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

    public boolean serieIsKnown (String path)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies WHERE  type = 'series' " +
                    "AND path = '" + path + "'");
            while(result.next())
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while loocking for serie " + e.getClass().getName() + ": " + e.getMessage());
        }

        return false;
    }

    public int addMovie (MovieInfos infos, String fileName)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement( "INSERT INTO movies(" +
                    "path, title, runtime, year, type, releaseDate, genre, plot, imdbID, imdbRating," +
                    "imdbVotes, metaScore, poster) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, fileName);
            statement.setString(2, infos.getTitle());
            statement.setString(3, infos.getRuntime());
            statement.setString(4, infos.getYear());
            statement.setString(5, infos.getType());
            statement.setString(6, infos.getReleaseDate());
            statement.setString(7, infos.getGenre());
            statement.setString(8, infos.getPlot());
            statement.setString(9, infos.getImdbID());
            statement.setString(10, infos.getImdbRating());
            statement.setString(11, infos.getImdbVotes());
            statement.setString(12, infos.getMetaScore());
            statement.setString(13, infos.getPoster());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding movie " + e.getClass().getName() + ": " + e.getMessage());
        }
        return -1;
    }

    public List<MovieInfos> getMovies()
    {
        List<MovieInfos> movies = new LinkedList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies WHERE type = 'movie'");
            while(result.next())
            {
                movies.add(new MovieInfos(
                        result.getString("title"),
                        result.getString("year"),
                        result.getString("releaseDate"),
                        result.getString("runtime"),
                        result.getString("genre"),
                        result.getString("plot"),
                        result.getString("imdbRating"),
                        result.getString("imdbVotes"),
                        result.getString("type"),
                        result.getString("metaScore"),
                        result.getString("imdbID"),
                        result.getString("poster"),
                        result.getString("path"),
                        result.getInt("id")
                        ));
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting movies " + e.getClass().getName() + ": " + e.getMessage());
        }
        return movies;
    }

    public List<MovieInfos> getSeries()
    {
        List<MovieInfos> movies = new LinkedList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM movies WHERE type = 'series'");
            while(result.next())
            {
                movies.add(new MovieInfos(
                        result.getString("title"),
                        result.getString("year"),
                        result.getString("releaseDate"),
                        result.getString("runtime"),
                        result.getString("genre"),
                        result.getString("plot"),
                        result.getString("imdbRating"),
                        result.getString("imdbVotes"),
                        result.getString("type"),
                        result.getString("metaScore"),
                        result.getString("imdbID"),
                        result.getString("poster"),
                        result.getString("path"),
                        result.getInt("id")
                ));
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting series " + e.getClass().getName() + ": " + e.getMessage());
        }
        return movies;
    }

    public void addEpisode(String title, int serieId, String season, String episode, String fileName)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO episodes(" +
                    "path, title, serieID, season, episode) " +
                    "VALUES(?, ?, ?, ?, ?)");

            statement.setString(1, fileName);
            statement.setString(2, title);
            statement.setInt(3, serieId);
            statement.setString(4, season);
            statement.setString(5, episode);
            statement.executeUpdate();
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding epsiode " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<String> getSerieEpisodes(String serieId)
    {
        List<String> episodes = new LinkedList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM episodes WHERE serieId = "+ new Integer(serieId));
            while(result.next())
            {
                episodes.add(result.getString("title"));
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting movies " + e.getClass().getName() + ": " + e.getMessage());
        }
        return episodes;
    }
}
