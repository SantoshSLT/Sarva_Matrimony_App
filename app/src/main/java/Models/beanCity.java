package Models;

import java.io.Serializable;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanCity implements Serializable {
    String city_id,city_name;

    private boolean isSelected;

    public beanCity(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;

    }
    public beanCity(String city_id, String city_name,boolean isSelected) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.isSelected=isSelected;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }





}
