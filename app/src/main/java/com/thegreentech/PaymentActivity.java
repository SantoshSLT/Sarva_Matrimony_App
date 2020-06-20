package com.thegreentech;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    TextView tvAmount;
    Button btnPay;
    private static final String TAG = PaymentActivity.class.getSimpleName();
    beanUpgradeMembershipPlan plan;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        //
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);
        tvAmount = findViewById(R.id.tvAmount);
        btnPay = findViewById(R.id.btnPay);
        textviewHeaderText.setText("PAYMENT OPTIONS");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("selectpkg")) {
            plan = getIntent().getParcelableExtra("selectpkg");
            tvAmount.setText("Amount :- Rs." + plan.getPlan_amount());
        }
        btnPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startPayment();
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(this, UpgradeMembershipPlanActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", plan.getInforamtion());
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://thegreentech.in/premium-matri-demo/img/thegreentech-upgraded.png");
            options.put("image", getResources().getDrawable(R.drawable.icn_logo_final));
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(plan.getPlan_amount()) * 100));

            /*JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);*/

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            final SharedPreferences prefUpdate = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
            String email = prefUpdate.getString("email", "");
            Intent intLogin = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
            intLogin.putExtra("plan_id", plan.getPlan_id());
            intLogin.putExtra("email_id", email);
            startActivity(intLogin);
            finishAffinity();
            /*Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            final SharedPreferences prefUpdate = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
            String email = prefUpdate.getString("email", "");
            //Logout
            stopService(new Intent(PaymentActivity.this, StatusUpdateService.class));
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
            //
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("email_id", email);
            params.put("plan_id", plan.getPlan_id());
            client.post(AppConstants.MAIN_URL + "payment_status.php", params, new TextHttpResponseHandler() {
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("onFailure", "" + throwable.getMessage() + " " + throwable.getCause());
                }

                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        JSONObject obj = new JSONObject(responseString);

                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("1")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                            builder.setCancelable(false);
                            builder.setMessage(message);
                            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intLogin = new Intent(PaymentActivity.this, LaunchActivity.class);
                                    startActivity(intLogin);
                                    finishAffinity();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
//            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
            builder.setCancelable(false);
            builder.setMessage("Your Payment Failed");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}


