package Models;

import java.io.Serializable;

public class beanUserSuccessStory implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String story_id;
    private String weddingphoto;
    private String weddingphoto_type;
    private String bridename;
    private String brideid;
    private String groomname;
    private String groomid;
    private String marriagedate;
    private String engagement_date;
    private String address;
    private String country;
    private String successmessage;


	public beanUserSuccessStory()
    {

    }

    public beanUserSuccessStory(String story_id, String weddingphoto, String weddingphoto_type, String bridename,
                                String brideid, String groomname, String groomid, String marriagedate,
                                String engagement_date, String address, String country, String  successmessage)
    {
        this.story_id = story_id;
        this.weddingphoto = weddingphoto;
        this.weddingphoto_type = weddingphoto_type;
        this.bridename = bridename;
        this.brideid = brideid;
        this.groomname = groomname;
        this.groomid = groomid;
        this.marriagedate = marriagedate;
        this.engagement_date = engagement_date;
        this.address = address;
        this.country=country;
        this.successmessage=successmessage;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getWeddingphoto() {
        return weddingphoto;
    }

    public void setWeddingphoto(String weddingphoto) {
        this.weddingphoto = weddingphoto;
    }

    public String getWeddingphoto_type() {
        return weddingphoto_type;
    }

    public void setWeddingphoto_type(String weddingphoto_type) {
        this.weddingphoto_type = weddingphoto_type;
    }

    public String getBridename() {
        return bridename;
    }

    public void setBridename(String bridename) {
        this.bridename = bridename;
    }

    public String getBrideid() {
        return brideid;
    }

    public void setBrideid(String brideid) {
        this.brideid = brideid;
    }

    public String getGroomname() {
        return groomname;
    }

    public void setGroomname(String groomname) {
        this.groomname = groomname;
    }

    public String getGroomid() {
        return groomid;
    }

    public void setGroomid(String groomid) {
        this.groomid = groomid;
    }

    public String getMarriagedate() {
        return marriagedate;
    }

    public void setMarriagedate(String marriagedate) {
        this.marriagedate = marriagedate;
    }

    public String getEngagement_date() {
        return engagement_date;
    }

    public void setEngagement_date(String engagement_date) {
        this.engagement_date = engagement_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSuccessmessage() {
        return successmessage;
    }

    public void setSuccessmessage(String successmessage) {
        this.successmessage = successmessage;
    }









}
