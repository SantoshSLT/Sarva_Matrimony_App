package Models;

public class MessageSend {

    String user_id, user_name, gender, tokan;

    public MessageSend(String user_id, String user_name, String gender, String token) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.gender = gender;
        this.tokan = token;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTokan() {
        return tokan;
    }

    public void setTokan(String tokan) {
        this.tokan = tokan;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
