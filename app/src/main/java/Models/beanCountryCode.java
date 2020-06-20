package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanCountryCode
{

    String country_id,country_code;

    public beanCountryCode(String country_id, String country_code)
    {
        this.country_id = country_id;
        this.country_code = country_code;

    }


    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }



    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }





}
