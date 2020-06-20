package com.thegreentech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.R;

import Models.beanUpgradeMembershipPlan;


public class UpgradeMembershipPlanDetailActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;


    TextView textPlanName,textPlanDuration,textTotalAmount;
    Button btnBuyNow;

    public static beanUpgradeMembershipPlan PlanDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_membership_plan_detail);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("UPGRADE MEMBERSHIP PLAN");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        textPlanName=(TextView)findViewById(R.id.textPlanName);
        textPlanDuration=(TextView)findViewById(R.id.textPlanDuration);
        textTotalAmount=(TextView)findViewById(R.id.textTotalAmount);

        btnBuyNow=(Button) findViewById(R.id.btnBuyNow);


        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });


        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpgradeMembershipPlanDetailActivity.this,"cooming soon",Toast.LENGTH_LONG).show();
            }
        });

        setData();
    }

    public void setData()
    {





        if(PlanDetails != null)
        {
            textPlanName.setText("  "+PlanDetails.getPlan_name());
            textPlanDuration.setText(":  "+PlanDetails.getPlan_duration()+" Days");
            textTotalAmount.setText(":  "+PlanDetails.getPlan_amount_type()+" "+PlanDetails.getPlan_amount());
        }else
        {
            textPlanName.setText("  -");
            textPlanDuration.setText(":  -");
            textTotalAmount.setText(":  -");
        }
    }

}