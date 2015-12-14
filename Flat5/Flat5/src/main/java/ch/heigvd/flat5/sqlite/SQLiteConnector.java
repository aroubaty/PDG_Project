package ch.heigvd.flat5.sqlite;
import java.sql.*;

public class SQLiteConnector
{
    private Connection connection = null;

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

    public void initDB()
    {
        try
        {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS videos" +
                       "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "name VARCHAR(50) NOT NULL);";
            statement.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS musics" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(50) NOT NULL);";
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

    public Connection getConnection()
    {
        return connection;
    }
}
