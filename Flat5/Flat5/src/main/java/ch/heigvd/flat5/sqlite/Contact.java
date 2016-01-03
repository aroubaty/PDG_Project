package ch.heigvd.flat5.sqlite;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Classe représentant un contact pour la synchronisation.
 * Contient simplement un id, un nom et une adresse ip. L'id correspond à l'id de la base de donnée de l'application.
 *
 * @author Jan Purro
 */
public class Contact
{
    private long id;
    private StringProperty name;
    private StringProperty address;

    /**
     * Construit un objet sans intitialisé l'id.
     * @param name Le nom du contact.
     * @param address L'adresse IP du contact.
     */
    public Contact(String name, String address)
    {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
    }

    /**
     * Construit un nouveau contact.
     * @param id L'id du contact dans la base de données de l'application.
     * @param name Le nom du contact.
     * @param address L'adresse du contact.
     */
    public Contact(long id, String name, String address)
    {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) { this.name.set(name); }

    public String getAddress() { return address.get(); }

    public void setAddress(String address) { this.address.set(address); }
}
