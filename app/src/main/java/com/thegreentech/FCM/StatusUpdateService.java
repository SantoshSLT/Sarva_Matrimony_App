package com.thegreentech.FCM;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.thegreentech.chat.helperClass.Chathelper;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Models.Chat_Model;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;

public class StatusUpdateService extends Service {


   Handler handler;
    Runnable runs;

    public StatusUpdateService() {
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("Not yet implemented", "Not yet implemented");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("remopvwe", "remove");
        stopService(new Intent(getApplicationContext(), StatusUpdateService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("start", "start");
        ChangStatus();

        return super.onStartCommand(intent, flags, startId);
    }


    public void onCreate() {
        super.onCreate();
        Log.e("create", "create");
        ChangStatus();
    }

    @Override
    public void onDestroy() {
        Log.e("destroy", "destroy");
        if (runs != null) {
            handler.removeCallbacks(runs);
        }
        super.onDestroy();
    }

    void ChangStatus() {
        runs = new Runnable() {
            public void run() {
                Update_now();
            }
        };
        handler = new Handler();
        Update_now();
    }

    void Update_now() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String matri_id = preferences.getString("matri_id", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        String dtSend = format.format(d);
        Log.e("date",dtSend);
        Log.e("Today is " ,""+ d.getTime());
        try {
          Log.e("Today is " ,""+ d.getTime());
        } catch (Exception e) {
            Log.e("dateexccc", e.getMessage());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", matri_id);
        params.put("online_time", dtSend);
        client.post(AppConstants.MAIN_URL + "check_online_status.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("service_Running", "" + handler.postDelayed(runs, 20000));
                //handler.postDelayed(runs, 20000);
            }
        });
    }




}
