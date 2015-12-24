package ch.heigvd.flat5.sqlite;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact
{
    private long id;
    private StringProperty name;
    private StringProperty address;

    public Contact(String name, String address)
    {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
    }

    public Contact(long id, String name, String address)
    {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Getter for property 'address'.
     *
     * @return Value for property 'address'.
     */
    public String getAddress() {
        return address.get();
    }

    /**
     * Setter for property 'address'.
     *
     * @param address Value to set for property 'address'.
     */
    public void setAddress(String address) {
        this.address.set(address);
    }
}
