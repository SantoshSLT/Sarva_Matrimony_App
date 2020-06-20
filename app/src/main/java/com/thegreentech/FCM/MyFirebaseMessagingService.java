package com.thegreentech.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thegreentech.chat.ChatingActivity;
import com.thegreentech.MainActivity;
import com.thegreentech.R;

import org.json.JSONObject;

import utills.AppConstants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    SharedPreferences prefUpdate;


    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("PRINT", "HELLO");
        mContext = getApplicationContext();

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(mContext);
        String user_id = prefUpdate.getString("user_id", "");

        if(!user_id.equalsIgnoreCase(""))
        {
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData());
                String id = obj.getString("id");
                switch (id) {
                    case "201"://message id
                        Intent send = new Intent();
                        send.setAction(AppConstants.Action_MessageRecived);
                        sendBroadcast(send);
                        if (!ChatingActivity.isOpen) {
                            String title = obj.getString("title");
                            String msg = obj.getString("msg");
                            createNotification(title, msg, "201");
                        }
                        break;
                    case "202":
                        String title = obj.getString("title");
                        String msg = obj.getString("msg");
                        createNotification(title, msg, "202");
                        break;
                }

            } catch (Exception e) {

                Log.d("PRINT", e.toString());
            }
        }


    }

    public void createNotification(String title, String message, String screenId) {
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.putExtra("screenId", screenId);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }


}

