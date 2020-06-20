package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanCaste
{
    String caste_id,name;
    private boolean isSelected;

    public beanCaste(String caste_id, String name) {
        this.caste_id = caste_id;
        this.name = name;

    }

    public beanCaste(String caste_id, String name, boolean isSelected) {
        this.caste_id = caste_id;
        this.name = name;
        this.isSelected=isSelected;
    }


    public String getCaste_id() {
        return caste_id;
    }

    public void setCaste_id(String caste_id) {
        this.caste_id = caste_id;
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
