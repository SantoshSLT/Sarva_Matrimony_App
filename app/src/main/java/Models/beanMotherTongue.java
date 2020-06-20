package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanMotherTongue
{
    String motherT_id,name;

    private boolean isSelected;

    public beanMotherTongue(String motherT_id, String name) {
        this.motherT_id = motherT_id;
        this.name = name;

    }
    public beanMotherTongue(String motherT_id, String name,boolean isSelected) {
        this.motherT_id = motherT_id;
        this.name = name;
        this.isSelected=isSelected;

    }

    public String getMotherT_id() {
        return motherT_id;
    }

    public void setMotherT_id(String motherT_id) {
        this.motherT_id = motherT_id;
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
