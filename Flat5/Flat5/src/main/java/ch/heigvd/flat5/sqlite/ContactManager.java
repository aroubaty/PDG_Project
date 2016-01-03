package ch.heigvd.flat5.sqlite;
import java.sql.*;
import java.util.*;

/**
 * Classe permettant de gérer les contacts de l'application.
 *
 * @author Jan Purro
 */
public class ContactManager
{
    // Connection à la base de données de l'application.
    private Connection connection;

    /**
     * Construit un nouveau ContactManager.
     * @param connector Un objet de type SQLiteConnector. Il doit lui-même être déjà connecté à la base de données.
     */
    public ContactManager(SQLiteConnector connector)
    {
        connection = connector.getConnection();
    }

    /**
     * Ajoute un contact à la base de données.
     * @param contact Le contact ajouté.
     * @return L'id du contact ajouté, en cas d'échec retourn -1.
     */
    public long addContact(Contact contact)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO contacts(name, address) VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getAddress());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) {
                return resultSet.getLong(1);
            }
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while adding contact " + e.getClass().getName() + ": " + e.getMessage());
        }
        return -1;
    }

    /**
     * Récupère le contact correspondant à une adresse IP.
     * @param address L'adresse IP dont le contact est recherché.
     * @return Le contact, s'il est trouvé, null sinon.
     */
    public Contact getContactFromAddress(String address)
    {
        for(Contact contact : getContacts())
        {
            if(contact.getAddress().equals(address))
            {
                return contact;
            }
        }
        return null;
    }

    /**
     * Retourne la liste des contacts actuels dans la base de données.
     * @return La liste des contacts.
     */
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
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while getting contacts " + e.getClass().getName() + ": " + e.getMessage());
        }
        return contacts;
    }

    /**
     * Met à jour les données d'un contact existant. L'id doit être le même.
     * @param contact Les nouvelles informations.
     */
    public void updateContact(Contact contact)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE contacts SET name = ?, address = ? WHERE id = ?");
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getAddress());
            statement.setLong(3, contact.getId());
            statement.executeUpdate();
            statement.close();
        }

        catch ( Exception e )
        {
            System.err.println("Error while updating contact " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Retire un contact de la base de donnée.
     * @param id L'id du contact à retirer.
     */
    public void removeContact(long id)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM contacts WHERE id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
            statement.close();
        }
        catch ( Exception e )
        {
            System.err.println("Error while deleting contact " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
