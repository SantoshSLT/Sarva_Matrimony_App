package com.thegreentech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import Fragments.FragmentMessages;

public class MessagesActivity extends AppCompatActivity {

    FrameLayout frame_Inbox;
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    String noty="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportActionBar().hide();

        noty = getIntent().getStringExtra("noti");

        frame_Inbox = findViewById(R.id.frame_Messages);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Message");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        FragmentMessages messages = new FragmentMessages();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_Messages,messages).commit();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noty.equalsIgnoreCase("msg")  )
                {
                    Intent intent = new Intent(MessagesActivity.this,FragmentNotification.class);
                    startActivity(intent);
                    finish();
                }
                else {
                onBackPressed();
                }
            }

        });
    }


}

