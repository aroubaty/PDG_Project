package ch.heigvd.flat5.sqlite;
import java.sql.*;
import java.util.*;

public class ContactManager
{
    private Connection connection;

    public ContactManager(SQLiteConnector connector)
    {
        connection = connector.getConnection();
    }

    public void addContact(Contact contact)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement( "INSERT INTO contacts(name, address) VALUES(?, ?)");
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getAddress());
            statement.executeUpdate();
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding contact " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<Contact> getContacts()
    {
        List<Contact> contacts = new LinkedList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM contacts");
            while(result.next())
            {
                contacts.add(new Contact(result.getInt("id"), result.getString("name"), result.getString("address")));
            }
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting contacts " + e.getClass().getName() + ": " + e.getMessage());
        }
        return contacts;
    }

    public void updateContact(Contact contact)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE contacts SET name = ?, address = ? WHERE id = ?");
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getAddress());
            statement.setInt(3, contact.getId());
            statement.executeUpdate();
        }
        catch ( Exception e )
        {
            System.err.println("Error while updating contact " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void removeContact(int id)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts WHERE id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        catch ( Exception e )
        {
            System.err.println("Error while deleting contact " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
