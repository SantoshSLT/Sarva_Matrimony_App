package com.thegreentech.successStory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thegreentech.AllMatches.MatchesDashboardActivity;
import com.thegreentech.AllMatches.fragments.MatchesFragment;
import com.thegreentech.MainActivity;
import com.thegreentech.R;

public class SuccessStoryDashBoardActivity extends AppCompatActivity {


    FrameLayout frame_Inbox;
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_story);

        //getSupportActionBar().hide();

        frame_Inbox = findViewById(R.id.frame_Messages);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Success Story");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);


        SuccesStoryFrag succesStoryFrag = new SuccesStoryFrag();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_success,succesStoryFrag).commit();


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
        Intent intent = new Intent(SuccessStoryDashBoardActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);
        finish();
    }
}
