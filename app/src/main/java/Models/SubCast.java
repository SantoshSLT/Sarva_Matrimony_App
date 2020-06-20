package Models;

public class SubCast {

    String SB_id,SB_name;
    private boolean isSelected;


    public SubCast(String SB_id, String SB_name) {
        this.SB_id = SB_id;
        this.SB_name = SB_name;
    }

    public SubCast(String SB_id, String SB_name, boolean isSelected) {
        this.SB_id = SB_id;
        this.SB_name = SB_name;
        this.isSelected = isSelected;
    }

    public String getSB_id() {
        return SB_id;
    }

    public void setSB_id(String SB_id) {
        this.SB_id = SB_id;
    }

    public String getSB_name() {
        return SB_name;
    }

    public void setSB_name(String SB_name) {
        this.SB_name = SB_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
