package Models;

import java.io.Serializable;

public class beanUserData implements Serializable
{

    private static final long serialVersionUID = 1L;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    String user_id;
    String matri_id;
    String username;
    String birthdate;
    String ocp_name;
    String height;
    String Address;
    String city_name;
    String country_name;
    String photo1_approve;
    String photo_view_status;
    String photo_protect;
    String photo_pswd;
    String gender;
    String is_shortlisted;
    String is_blocked;
    String is_favourite;
    String user_profile_picture;
    String ei_reqid;
    String receiver_response;
    String ei_reqdate;

    //public beanUserData(){}


    public beanUserData(String user_id, String matri_id1, String username, String gender1,
                        String user_profile_picture, String ei_id, String receiver_response, String sent_date) {

        this.user_id = user_id;
        this.matri_id= matri_id1;
        this.username = username;
        this.user_profile_picture = user_profile_picture;
        this.gender = gender1;
        this.ei_reqid = ei_id;
        this.receiver_response = receiver_response;
        this.ei_reqdate = sent_date;
    }

    public beanUserData(String user_id, String matri_id1, String username, String gender1,
                        String user_profile_picture, String ei_id, String receiver_response, String sent_date,String is_blocked) {

        this.user_id = user_id;
        this.matri_id= matri_id1;
        this.username = username;
        this.user_profile_picture = user_profile_picture;
        this.gender = gender1;
        this.ei_reqid = ei_id;
        this.receiver_response = receiver_response;
        this.ei_reqdate = sent_date;
        this.is_blocked = is_blocked;
    }

    public beanUserData(String matri_id,String username,String birthdate,String ocp_name,String height,
                        String Address,String city_name,String country_name,String photo1_approve,String photo_view_status,
                        String photo_protect,String photo_pswd,String gender,String is_shortlisted,String is_blocked,
                        String is_favourite,String user_profile_picture)
    {
        this.matri_id = matri_id;
        this.username = username;
        this.birthdate = birthdate;
        this.ocp_name = ocp_name;
        this.height = height;
        this.Address = Address;
        this.city_name = city_name;
        this.country_name = country_name;
        this.photo1_approve = photo1_approve;
        this.photo_view_status = photo_view_status;
        this.photo_protect = photo_protect;
        this.photo_pswd = photo_pswd;
        this.gender = gender;
        this.is_shortlisted = is_shortlisted;
        this.is_blocked = is_blocked;
        this.is_favourite = is_favourite;
        this.user_profile_picture = user_profile_picture;
    }
    public beanUserData(String user_id,String matri_id,String username,String birthdate,String ocp_name,String height,
                        String Address,String city_name,String country_name,String photo1_approve,String photo_view_status,
                        String photo_protect,String photo_pswd,String gender,String is_shortlisted,String is_blocked,
                        String is_favourite,String user_profile_picture)
    {
        this.user_id = user_id;
        this.matri_id = matri_id;
        this.username = username;
        this.birthdate = birthdate;
        this.ocp_name = ocp_name;
        this.height = height;
        this.Address = Address;
        this.city_name = city_name;
        this.country_name = country_name;
        this.photo1_approve = photo1_approve;
        this.photo_view_status = photo_view_status;
        this.photo_protect = photo_protect;
        this.photo_pswd = photo_pswd;
        this.gender = gender;
        this.is_shortlisted = is_shortlisted;
        this.is_blocked = is_blocked;
        this.is_favourite = is_favourite;
        this.user_profile_picture = user_profile_picture;
    }

    // Photo Request page
    public beanUserData(String user_id,String matri_id,String username,String birthdate,String ocp_name,String height,
                        String Address,String city_name,String country_name,String photo1_approve,String photo_view_status,
                        String photo_protect,String photo_pswd,String gender,String is_shortlisted,String is_blocked,
                        String is_favourite,String user_profile_picture,String ph_reqid,String receiver_response,String ph_reqdate)
    {
        this.user_id = user_id;
        this.matri_id = matri_id;
        this.username = username;
        this.birthdate = birthdate;
        this.ocp_name = ocp_name;
        this.height = height;
        this.Address = Address;
        this.city_name = city_name;
        this.country_name = country_name;
        this.photo1_approve = photo1_approve;
        this.photo_view_status = photo_view_status;
        this.photo_protect = photo_protect;
        this.photo_pswd = photo_pswd;
        this.gender = gender;
        this.is_shortlisted = is_shortlisted;
        this.is_blocked = is_blocked;
        this.is_favourite = is_favourite;
        this.user_profile_picture = user_profile_picture;
        this.ei_reqid=ph_reqid;
        this.receiver_response=receiver_response;
        this.ei_reqdate=ph_reqdate;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMatri_id() {
        return matri_id;
    }

    public void setMatri_id(String matri_id) {
        this.matri_id = matri_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getOcp_name() {
        return ocp_name;
    }

    public void setOcp_name(String ocp_name) {
        this.ocp_name = ocp_name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getPhoto1_approve() {
        return photo1_approve;
    }

    public void setPhoto1_approve(String photo1_approve) {
        this.photo1_approve = photo1_approve;
    }

    public String getPhoto_view_status() {
        return photo_view_status;
    }

    public void setPhoto_view_status(String photo_view_status) {
        this.photo_view_status = photo_view_status;
    }

    public String getPhoto_protect() {
        return photo_protect;
    }

    public void setPhoto_protect(String photo_protect) {
        this.photo_protect = photo_protect;
    }

    public String getPhoto_pswd() {
        return photo_pswd;
    }

    public void setPhoto_pswd(String photo_pswd) {
        this.photo_pswd = photo_pswd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_shortlisted() {
        return is_shortlisted;
    }

    public void setIs_shortlisted(String is_shortlisted) {
        this.is_shortlisted = is_shortlisted;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(String is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getUser_profile_picture() {
        return user_profile_picture;
    }

    public void setUser_profile_picture(String user_profile_picture) {
        this.user_profile_picture = user_profile_picture;
    }

    public String getei_reqid() {
        return ei_reqid;
    }

    public void setei_reqid(String ei_reqid) {
        this.ei_reqid = ei_reqid;
    }

    public String getReceiver_response() {
        return receiver_response;
    }

    public void setReceiver_response(String receiver_response) {
        this.receiver_response = receiver_response;
    }

    public String getei_reqdate() {
        return ei_reqdate;
    }

    public void setPh_reqdate(String ei_reqdate) {
        this.ei_reqdate = ei_reqdate;
    }
}
