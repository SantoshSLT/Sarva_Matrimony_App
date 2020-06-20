package Models;

public class HeightBean
{

    String ID,Name;


    public HeightBean(String ID, String name) {
        this.ID = ID;
        Name = name;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
