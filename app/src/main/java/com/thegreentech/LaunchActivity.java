package com.thegreentech;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import utills.AppConstants;
import utills.Myprefrence;

public class LaunchActivity extends AppCompatActivity { 

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SharedPreferences prefUpdate;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getToken();
        //AppConstants.tokan = FirebaseInstanceId.getInstance().getToken();
       // AppConstants.CheckConnection(LaunchActivity.this);
        Log.e("TOKEN", "" + AppConstants.tokan);
        Myprefrence.saveDeviceToken(LaunchActivity.this, AppConstants.tokan);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);
        user_id = prefUpdate.getString("user_id", "");

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if (!user_id.equalsIgnoreCase(""))
                {
                    AppConstants.m_id = user_id;
                    getSharedPreferences("data", MODE_PRIVATE).edit().putString("isdisplay", "false").apply();
                    getSharedPreferences("data", MODE_PRIVATE).edit().putString("ad_display", "false").apply();
                    Intent intLogin = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intLogin);
                    finish();


                } else {
                    Intent mainIntent = new Intent(LaunchActivity.this,LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

        FirebaseApp.initializeApp(this);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                Log.d("TOKEN__", task.getResult().getToken());
                AppConstants.tokan = task.getResult().getToken();
                Myprefrence.saveDeviceToken(LaunchActivity.this, AppConstants.tokan);
            }
        });
    }
  /*

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        Log.e("deviceId",telephonyManager.getDeviceId() +"");
        telephonyManager.getDeviceId();
*/


}
