package Models;

public class Notification_Model{


    String receiver_id;
    String sender_id;
    String MatriId;
    String reminder_mes_type;
    String reminder_view_status;
    String imgProfileUrl;


    public String getMatriId() {
        return MatriId;
    }

    public void setMatriId(String matriId) {
        MatriId = matriId;
    }

    public String getImgProfileUrl() {
        return imgProfileUrl;
    }

    public void setImgProfileUrl(String imgProfileUrl) {
        this.imgProfileUrl = imgProfileUrl;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReminder_mes_type() {
        return reminder_mes_type;
    }

    public void setReminder_mes_type(String reminder_mes_type) {
        this.reminder_mes_type = reminder_mes_type;
    }

    public String getReminder_view_status() {
        return reminder_view_status;
    }

    public void setReminder_view_status(String reminder_view_status) {
        this.reminder_view_status = reminder_view_status;
    }
}
