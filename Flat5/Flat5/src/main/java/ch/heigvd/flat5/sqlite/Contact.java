public class Contact
{
    private int id;
    private String name;
    private String address;

    public Contact(String name, String address)
    {
        this.name = name;
        this.address = address;
    }

    public Contact(int id, String name, String address)
    {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
}
