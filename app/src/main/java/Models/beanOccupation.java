package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanOccupation
{
    String occupation_id,occupation_name;
    private boolean isSelected;

    public beanOccupation(String occupation_id, String occupation_name)
    {
        this.occupation_id = occupation_id;
        this.occupation_name = occupation_name;

    }


    public beanOccupation(String occupation_id, String occupation_name, boolean isSelected)
    {
        this.occupation_id = occupation_id;
        this.occupation_name = occupation_name;
        this.isSelected=isSelected;

    }

    public String getOccupation_id() {
        return occupation_id;
    }

    public void setOccupation_id(String occupation_id) {
        this.occupation_id = occupation_id;
    }

    public String getOccupation_name() {
        return occupation_name;
    }

    public void setOccupation_name(String occupation_name) {
        this.occupation_name = occupation_name;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }




}
