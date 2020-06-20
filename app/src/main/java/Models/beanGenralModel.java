package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanGenralModel
{
    String id,name;

    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public beanGenralModel(String id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected=isSelected;
    }






}
