package com.thegreentech.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.thegreentech.R;

import java.util.Random;

public class NotificationHelper extends ContextWrapper {

    private String Channel_1 = "";
    private String Channel_Name = "";
    private NotificationManager manager;
    private NotificationHelper helper;
    Context ctx;

    public NotificationHelper(Context base) {
        super(base);
        this.ctx = base;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateChannel()
    {

        Channel_1 = getPackageName().toString();
        Channel_Name = getString(R.string.app_name);

        NotificationChannel channel = new NotificationChannel(Channel_1, Channel_Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);

    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder getBuilder(String title, String body, Intent intent) {

        PendingIntent intt = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();

        } catch (Exception e) {
        }

        return new Notification.Builder(getApplicationContext(), Channel_1)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intt)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
    }

    public void notifyNow(String title, String body, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateChannel();
            helper = new NotificationHelper(ctx);
            Notification.Builder builder11 = helper.getBuilder(title, body, intent);
            helper.getManager().notify(new Random().nextInt(), builder11.build());

        } else {
            PendingIntent intt = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(title);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentText(body);
            builder.setAutoCancel(true);
            builder.setContentIntent(intt);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            manager.notify(0, builder.build());
        }
    }

}
