package ch.heigvd.flat5.sqlite;
import java.sql.*;

/**
 * Classe permettant de se connecter à la base de données de l'application.
 *
 * @author Jan Purro
 */
public class SQLiteConnector
{
    private Connection connection = null;

    /**
     * Connection à la base de données de l'application. Si la base de données n'existe pas, elle est crée.
     */
    public void connectToDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:flat5.db");
        }

        catch ( Exception e )
        {
            System.err.println("Error while connecting " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Initialisation de la base de données.
     * Les tables de la base de données seront crées si elle n'existe pas déjà.
     */
    public void initDB()
    {
        try
        {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS movies" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "path TEXT NOT NULL," +
                    "title VARCHAR(50) NOT NULL," +
                    "runtime VARCHAR(15) NOT NULL," +
                    "year VARCHAR(4)," +
                    "type VARCHAR(10)," +
                    "releaseDate VARCHAR(25)," +
                    "genre TEXT," +
                    "plot TEXT," +
                    "imdbID VARCHAR(10)," +
                    "imdbRating VARCHAR(2)," +
                    "imdbVotes VARCHAR(10)," +
                    "metaScore VARCHAR(3)," +
                    "poster TINYTEXT" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS episodes" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "path TEXT NOT NULL," +
                    "title VARCHAR(50) NOT NULL," +
                    "serieID INTEGER," +
                    "season TINYTEXT," +
                    "episode TINYTEXT," +
                    " FOREIGN KEY (serieID) REFERENCES movies(id)" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS tracks" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "path TEXT NOT NULL," +
                    "title VARCHAR (50) NOT NULL," +
                    "artist VARCHAR (50)," +
                    "album VARCHAR (50)," +
                    "genre VARCHAR (50)," +
                    "year VARCHAR (4)," +
                    "length VARCHAR (15)," +
                    "urlCover TINYTEXT" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS contacts" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(50) NOT NULL," +
                    "address VARCHAR(15) NOT NULL);";
            statement.executeUpdate(query);
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while creating DB " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Retourne la connection à la base de données.
     * @return
     */
    public Connection getConnection()
    {
        return connection;
    }
}
