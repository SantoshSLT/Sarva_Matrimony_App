package com.thegreentech;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Thankyou extends AppCompatActivity {

    Button btnLogin;

    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finishAffinity();
            }
        });

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewSignUp.setText("LOGIN");
        textviewHeaderText.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thankyou.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
