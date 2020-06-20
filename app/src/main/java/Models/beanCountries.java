package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanCountries
{
    String country_id,country_name;
    private boolean isSelected;

    public beanCountries(String country_id, String country_name) {
        this.country_id = country_id;
        this.country_name = country_name;

    }
    public beanCountries(String country_id, String country_name,boolean isSelected) {
        this.country_id = country_id;
        this.country_name = country_name;
        this.isSelected=isSelected;

    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
