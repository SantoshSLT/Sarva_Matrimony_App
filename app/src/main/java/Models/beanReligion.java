package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanReligion
{
    String religion_id,name;
    private boolean isSelected;

    public beanReligion(String religion_id, String name) {
        this.religion_id = religion_id;
        this.name = name;
    }

    public beanReligion(String religion_id, String name, boolean isSelected) {
        this.religion_id = religion_id;
        this.name = name;
        this.isSelected=isSelected;
    }

    public String getReligion_id() {
        return religion_id;
    }

    public void setReligion_id(String religion_id) {
        this.religion_id = religion_id;
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




}
