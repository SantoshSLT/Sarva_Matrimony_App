package Models;

import java.io.Serializable;

public class beanMessages implements Serializable
{

    private static final long serialVersionUID = 1L;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    private String mes_id;
    private String username;
    private String msg_important_status;
    private String from_matri_id;
    private String to_matri_id;
    private String subject;
    private String message;
    private String sent_date;
    private String msg_status;
    private String status;
    private String user_photo;
    private String is_favorite;


    public beanMessages(String mes_id, String username, String msg_important_status, String from_matri_id, String to_matri_id,
                        String subject, String message, String sent_date, String msg_status, String status, String user_photo,
                        String is_favorite)
    {
        this.mes_id = mes_id;
        this.username=username;
        this.msg_important_status = msg_important_status;
        this.from_matri_id = from_matri_id;
        this.to_matri_id = to_matri_id;
        this.subject = subject;
        this.message = message;
        this.sent_date = sent_date;
        this.msg_status = msg_status;
        this.status = status;
        this.user_photo = user_photo;
        this.is_favorite=is_favorite;
    }

	public beanMessages()
    {

    }

    public String getMes_id() {
        return mes_id;
    }

    public void setMes_id(String mes_id) {
        this.mes_id = mes_id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getMsg_important_status() {
        return msg_important_status;
    }

    public void setMsg_important_status(String msg_important_status) {
        this.msg_important_status = msg_important_status;
    }

    public String getFrom_matri_id() {
        return from_matri_id;
    }

    public void setFrom_matri_id(String from_matri_id) {
        this.from_matri_id = from_matri_id;
    }

    public String getTo_matri_id() {
        return to_matri_id;
    }

    public void setTo_matri_id(String to_matri_id) {
        this.to_matri_id = to_matri_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

}
