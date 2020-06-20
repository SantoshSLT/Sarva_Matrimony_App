package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanState
{
    String state_id,state_name;
    private boolean isSelected;

    public beanState(String state_id, String state_name) {
        this.state_id = state_id;
        this.state_name = state_name;

    }

    public beanState(String state_id, String state_name, boolean isSelected) {
        this.state_id = state_id;
        this.state_name = state_name;
        this.isSelected=isSelected;

    }
    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }







}
