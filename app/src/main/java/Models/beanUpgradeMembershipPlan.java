package Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ravi on 10/24/2016.
 */
public class beanUpgradeMembershipPlan implements Parcelable
{
    String plan_id,plan_name, plan_amount_type,plan_amount,plan_duration,plan_msg,plan_sms,plan_contacts,chat,profile,status;

    String id,title,inforamtion;

    public beanUpgradeMembershipPlan()
    {

    }
    public beanUpgradeMembershipPlan(String id, String Title, String Information) {
        this.id = id;
        this.title = Title;
        this.inforamtion = Information;
    }

    public beanUpgradeMembershipPlan(String plan_id, String plan_name, String plan_amount_type, String plan_amount,
                                     String plan_duration, String plan_msg, String plan_sms, String plan_contacts,
                                     String chat, String profile, String status)
    {
        this.plan_id = plan_id;
        this.plan_name = plan_name;
        this.plan_amount_type = plan_amount_type;
        this.plan_amount=plan_amount;
        this.plan_duration = plan_duration;
        this.plan_msg = plan_msg;
        this.plan_sms = plan_sms;
        this.plan_contacts = plan_contacts;
        this.chat = chat;
        this.profile = profile;

    }


    protected beanUpgradeMembershipPlan(Parcel in) {
        plan_id = in.readString();
        plan_name = in.readString();
        plan_amount_type = in.readString();
        plan_amount = in.readString();
        plan_duration = in.readString();
        plan_msg = in.readString();
        plan_sms = in.readString();
        plan_contacts = in.readString();
        chat = in.readString();
        profile = in.readString();
        status = in.readString();
        id = in.readString();
        title = in.readString();
        inforamtion = in.readString();
    }

    public static final Creator<beanUpgradeMembershipPlan> CREATOR = new Creator<beanUpgradeMembershipPlan>() {
        @Override
        public beanUpgradeMembershipPlan createFromParcel(Parcel in) {
            return new beanUpgradeMembershipPlan(in);
        }

        @Override
        public beanUpgradeMembershipPlan[] newArray(int size) {
            return new beanUpgradeMembershipPlan[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInforamtion() {
        return inforamtion;
    }

    public void setInforamtion(String inforamtion) {
        this.inforamtion = inforamtion;
    }




    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_amount_type() {
        return plan_amount_type;
    }

    public void setPlan_amount_type(String plan_amount_type) {
        this.plan_amount_type = plan_amount_type;
    }

    public String getPlan_amount() {
        return plan_amount;
    }

    public void setPlan_amount(String plan_amount) {
        this.plan_amount = plan_amount;
    }

    public String getPlan_duration() {
        return plan_duration;
    }

    public void setPlan_duration(String plan_duration) {
        this.plan_duration = plan_duration;
    }

    public String getPlan_msg() {
        return plan_msg;
    }

    public void setPlan_msg(String plan_msg) {
        this.plan_msg = plan_msg;
    }

    public String getPlan_sms() {
        return plan_sms;
    }

    public void setPlan_sms(String plan_sms) {
        this.plan_sms = plan_sms;
    }

    public String getPlan_contacts() {
        return plan_contacts;
    }

    public void setPlan_contacts(String plan_contacts) {
        this.plan_contacts = plan_contacts;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plan_id);
        dest.writeString(plan_name);
        dest.writeString(plan_amount_type);
        dest.writeString(plan_amount);
        dest.writeString(plan_duration);
        dest.writeString(plan_msg);
        dest.writeString(plan_sms);
        dest.writeString(plan_contacts);
        dest.writeString(chat);
        dest.writeString(profile);
        dest.writeString(status);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(inforamtion);
    }
}
