package com.thegreentech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import utills.AppConstants;

public class SplashActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp;
    SharedPreferences prefUpdate;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        user_id = prefUpdate.getString("user_id", "");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        getToken();

        if (!user_id.equalsIgnoreCase(""))
        {

            AppConstants.m_id = user_id;
            getSharedPreferences("data", MODE_PRIVATE).edit().putString("isdisplay", "false").apply();
            getSharedPreferences("data", MODE_PRIVATE).edit().putString("ad_display", "false").apply();
            Intent intLogin = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intLogin);
            finish();

            btnSignUp.setVisibility(View.GONE);
            btnLogin.setText("Let's Start");

        } else {
            btnSignUp.setVisibility(View.VISIBLE);
            btnLogin.setText("Login");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_id.equalsIgnoreCase("")) {
                    Intent intLogin = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intLogin);
                    finish();
                } else {
                    Intent intLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intLogin);
                }

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signup_step = prefUpdate.getString("signup_step", "");
                if (signup_step.equalsIgnoreCase("1")) {
                    Intent intLogin = new Intent(SplashActivity.this, VerifyMobileNumberActivity.class);
                    startActivity(intLogin);
                } else if (signup_step.equalsIgnoreCase("2")) {
                    Intent intLogin = new Intent(SplashActivity.this, SignUpStep2Activity.class);
                    startActivity(intLogin);
                } else if (signup_step.equalsIgnoreCase("3")) {
                    Intent intLogin = new Intent(SplashActivity.this, SignUpStep3Activity.class);
                    startActivity(intLogin);
                } else if (signup_step.equalsIgnoreCase("4")) {
                    Intent intLogin = new Intent(SplashActivity.this, SignUpStep4Activity.class);
                    startActivity(intLogin);
                } else {
                    Intent intLogin = new Intent(SplashActivity.this, SignUpStep1Activity.class);
                    startActivity(intLogin);
                }

            }
        });

    }

    void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                Log.d("TOKEN__", task.getResult().getToken());
                AppConstants.tokan = task.getResult().getToken();
            }
        });
    }

}
