package Models;

import java.io.Serializable;

public class Chat_Model implements Serializable {



    String id;
    String time;
    String msg;
    String date;
    String from_id;
    String to_id;
    String ImgReciverProfileurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgReciverProfileurl() {
        return ImgReciverProfileurl;
    }

    public void setImgReciverProfileurl(String ImgReciverProfileurl) {
        this.ImgReciverProfileurl = ImgReciverProfileurl;
    }
}
