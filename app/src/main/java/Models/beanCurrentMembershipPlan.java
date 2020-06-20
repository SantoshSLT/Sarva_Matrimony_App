package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanCurrentMembershipPlan
{
    String id,Title, Information;

    public beanCurrentMembershipPlan(String id, String Title, String Information) {
        this.id = id;
        this.Title = Title;
        this.Information = Information;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }






}
