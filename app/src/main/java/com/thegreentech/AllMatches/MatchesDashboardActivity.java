package com.thegreentech.AllMatches;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thegreentech.AllMatches.fragments.MatchesFragment;
import com.thegreentech.MainActivity;
import com.thegreentech.ManagePhotoActivity;
import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

import Fragments.FragmentMessages;

public class MatchesDashboardActivity extends AppCompatActivity {


    FrameLayout frame_Inbox;
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_dashboard);

       // getSupportActionBar().hide();

        frame_Inbox = findViewById(R.id.frame_Messages);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("My Matches");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);


        MatchesFragment messages = new MatchesFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_Macthes,messages).commit();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MatchesDashboardActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);
    }
}
