package Models;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanEducation
{
    String education_id,education_name;
    private boolean isSelected;

    public beanEducation(String education_id, String education_name)
    {
        this.education_id = education_id;
        this.education_name = education_name;

    }


    public beanEducation(String education_id, String education_name, boolean isSelected)
    {
        this.education_id = education_id;
        this.education_name = education_name;
        this.isSelected=isSelected;

    }


    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    public String getEducation_name() {
        return education_name;
    }

    public void setEducation_name(String education_name) {
        this.education_name = education_name;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
