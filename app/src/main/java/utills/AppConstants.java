package utills;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.thegreentech.R;
import com.thegreentech.chat.ChatingActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class AppConstants {

  public static String BASE_URL ="http://sarvamatrimony.com/demo1/";
  // public static String BASE_URL = "http://matrimonialphpscript.com/premium-demo/";
   public static String MAIN_URL = BASE_URL+"api/";
  //  public static String MAIN_URL = BASE_URL;
    public static String m_id = "";
    public static String tokan = "";
    public static String fromNotification = "";

    public static String msg_express_intress = "New Express interest Recived from";
    public static String msg_express_intress_title = "Express Interest";
    public static String msg_send_reminder_title = "Send Reminder";

    public static String msg_massage_sent = "New Message Received From";
    public static String msg_sent_title = "New Message Received From";


    public static String express_intress_id = "202";
    public static String Photo_Password_id = "203";
    public static String Photo_Request_id = "204";
    public static String Check_Contact = "205";
    public static String msg_id = "206";


    //-------Push Notifications-------//
    public static String Action_MessageRecived = "CHAT_RECIVED";
    //-------------------------------//
    public static String SELECTED_TAB_MAIN="MainTab";
    public static String SELECTED_TAB_HOME="HomeTab";
    public static String SELECTED_TAB_MESSAGE="MessageTab";
    public static String SELECTED_TAB_MATCHES="MatchesTab";
    public static String SELECTED_TAB_SEARCH="Search";
    public static String SELECTED_TAB_Success_Story="SuccessStoryTab";

    public static String MaritalStatusName="",EatingHabitNAme="",SmokingHabitsNAME="",DrinkingNAME="",StarNAME="",DosTypeNAME="",Raasi="";

    public static  String BrideID = "",BrideName="";
    public static String CountryId="",StateId="",CityId="";
    public static String CountryName="",StateName="",CityName="";
    public static String ReligionId="",CasteId="",MotherTongueId="",CountryCodeId="",EducationId="",AditionalEducationId="",OccupationID="",HeightID="",HeightFromID="",HeightToID="";
    public static String ReligionName="",CasteName="",MotherTongueName="",CountryCodeName="",EducationName="",AditionalEducationName="",OccupationName="";
    public static String GeneralName="",SubCasteID="",SubCasteName="",UserID ="",UserName="";


    public static String FIRST_NAME="firstName";
    public static String LAST_NAME="lastName";
    public static String MOBILE_NO="mobileno.";
    public static String EMAIL_ID="emailid";
    public static String PASSWORD = "password";
    public static String DOSHTYPE = "";


    public  static String is_shortlisted ;
    public  static String is_blocked;
    public  static String is_interest;


    public static void sendPushNotification(String tokan, String msg, String title, final String id)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("tokan",tokan);
        params.put("msg",msg);
        params.put("title",title);
        params.put("id",id);
        client.post(AppConstants.MAIN_URL + "user_chat_notification.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("NOTIFICATION__fail"+id,responseString);
            }
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                try {
                    Log.d("NOTIFICATION__"+id,responseString);
                }
              catch (Exception e)
              {
                  e.getMessage();
              }
            }
        });

    }

    public static Pattern email_pattern= Pattern.compile
            (  "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            );

    public static Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(700);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToLeftAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(100);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }


    public static Animation FromTopToButtomAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation FromButtomToTopAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }


    public static void CheckConnection(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);
        builder.setTitle(activity.getResources().getString(R.string.app_name));
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String mDateFormateDDMMM(String strDate)
    {
        String finalDate="";
        try
        {
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date newDate=spf.parse(strDate);
            spf= new SimpleDateFormat("hh:mm dd MMM yyyy"); //dd MMM yyyy
            finalDate = spf.format(newDate);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return finalDate;
    }

    public static class chatDb {
        public static final String COLUMN_NAME_DATE = "update_time";
        public static final String COLUMN_NAME_RECORD = "record";
        public  static  final String COLUMN_MSG="message";
    }
}

