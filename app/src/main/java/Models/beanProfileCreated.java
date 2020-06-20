package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanProfileCreated
{
    String profile_id,name;

    public beanProfileCreated(String profile_id, String name) {
        this.profile_id = profile_id;
        this.name = name;

    }


    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





}
