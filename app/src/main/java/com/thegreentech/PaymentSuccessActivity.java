package com.thegreentech;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.thegreentech.FCM.StatusUpdateService;

import org.json.JSONObject;

import Models.beanUpgradeMembershipPlan;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;

public class PaymentSuccessActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    String plan_id, email_id;
    Button btnOk;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);
        textviewHeaderText = findViewById(R.id.textviewHeaderText);
        textviewSignUp = findViewById(R.id.textviewSignUp);
        btnBack.setVisibility(View.INVISIBLE);
        textviewSignUp.setVisibility(View.INVISIBLE);

        textviewHeaderText.setText("Payment Success");

        plan_id = getIntent().getStringExtra("plan_id");
        email_id = getIntent().getStringExtra("email_id");


        callApiForPlanStatus();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPreferences prefUpdate = PreferenceManager.getDefaultSharedPreferences(PaymentSuccessActivity.this);
                stopService(new Intent(PaymentSuccessActivity.this, StatusUpdateService.class));
                SharedPreferences.Editor editor = prefUpdate.edit();
                editor.putString("user_id", "");
                editor.putString("email", "");
                editor.putString("profile_image", "");
                editor.putString("matri_id", "");
                editor.putString("username", "");
                editor.putString("gender", "");
                editor.commit();
                getSharedPreferences("data", Context.MODE_PRIVATE)
                        .edit().clear().commit();

                Intent intLogin = new Intent(PaymentSuccessActivity.this, LaunchActivity.class);
                startActivity(intLogin);
                finishAffinity();
            }
        });

    }

    private void callApiForPlanStatus() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", email_id);
        params.put("plan_id", plan_id);
        client.post(AppConstants.MAIN_URL + "payment_status.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("onFailure", "" + throwable.getMessage() + " " + throwable.getCause());
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("onSuccess", "" + responseString);
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("1")) {

                        Log.e("getstatuscall", "" + status + " " + message);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(this, UpgradeMembershipPlanActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }


}


