package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AppConstants;
import utills.NetworkConnection;

public class VerifyMobileNumberActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp,tvCount;
    
    TextView textMobileNo;
    Button txtSendOTPAgain;
    Button btnEdit, btnVerifyContinue,btnClose,btnSave;
    EditText edtEdtMobileNo;
    Pinview edtOTP;
    RelativeLayout relEditMobileNoView;
    LinearLayout linSlidingDrawer;
    ProgressDialog progresDialog;

    SharedPreferences prefUpdate;
    String UserId,otp,MobileNo,CountryCode;
    boolean isFinish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_moble_number);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(VerifyMobileNumberActivity.this);
        UserId= prefUpdate.getString("user_id_r","");
        otp= prefUpdate.getString("otp","");
        MobileNo= prefUpdate.getString(AppConstants.MOBILE_NO,"");
        CountryCode= prefUpdate.getString("CountryCode","");
        Log.e("Userid=",""+UserId);

        tvCount = findViewById(R.id.tvCount);
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);
        textviewSignUp.setText("REGISTER NEW");
        btnBack.setVisibility(View.GONE);
        textviewHeaderText.setVisibility(View.VISIBLE);
        textviewHeaderText.setText("LOGIN");

        textMobileNo=(TextView)findViewById(R.id.textMobileNo);

        txtSendOTPAgain=findViewById(R.id.txtSendOTPAgain);

        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnVerifyContinue=(Button)findViewById(R.id.btnVerifyContinue);
        btnClose=(Button)findViewById(R.id.btnClose);
        btnSave=(Button)findViewById(R.id.btnSave);

        edtOTP=findViewById(R.id.edtOTP);
        edtEdtMobileNo=(EditText)findViewById(R.id.edtEdtMobileNo);

        relEditMobileNoView=(RelativeLayout) findViewById(R.id.relEditMobileNoView);
        relEditMobileNoView.setVisibility(View.GONE);
        linSlidingDrawer=(LinearLayout) findViewById(R.id.linSlidingDrawer);

        //edtOTP.setText(""+otp);
        textMobileNo.setText(CountryCode+"-"+MobileNo);
        edtEdtMobileNo.setText(MobileNo);

        textviewHeaderText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intLogin= new Intent(VerifyMobileNumberActivity.this,LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });


        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textviewSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMobileNumberActivity.this);
                        builder.setTitle(getResources().getString(R.string.app_name));
                        builder.setMessage("Do you want to clear previous data?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                SharedPreferences.Editor editor=prefUpdate.edit();
                                editor.putString("signup_step","0");
                                editor.commit();
                                Intent intLogin= new Intent(VerifyMobileNumberActivity.this,SignUpStep1Activity.class);
                                startActivity(intLogin);
                                finish();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                });
            }
        });


        startCounter();

        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                relEditMobileNoView.setVisibility(View.VISIBLE);

                linSlidingDrawer.setVisibility(View.VISIBLE);

                Animation bottomUp = AnimationUtils.loadAnimation(VerifyMobileNumberActivity.this,  R.anim.slide_up_dialog);
                linSlidingDrawer.startAnimation(bottomUp) ;
            }
        });


        relEditMobileNoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Animation bottomDown = AnimationUtils.loadAnimation(VerifyMobileNumberActivity.this,  R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown) ;
                linSlidingDrawer.setVisibility(View.GONE);

              //  relEditMobileNoView.startAnimation(bottomDown) ;
                relEditMobileNoView.setVisibility(View.GONE);

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Animation bottomDown = AnimationUtils.loadAnimation(VerifyMobileNumberActivity.this,  R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown) ;
                linSlidingDrawer.setVisibility(View.GONE);

               // relEditMobileNoView.startAnimation(bottomDown) ;
                relEditMobileNoView.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnVerifyContinue.getWindowToken(), 0);

                String stmobileNo= edtEdtMobileNo.getText().toString().trim();
                if(stmobileNo.equalsIgnoreCase(""))
                {
                    Toast.makeText(VerifyMobileNumberActivity.this,getResources().getString(R.string.Please_enter_your_register_your_mobile_no),Toast.LENGTH_LONG).show();
                }else
                {

                    if (NetworkConnection.hasConnection(VerifyMobileNumberActivity.this)){

                        updateContactNoRequest(UserId,stmobileNo);

                    }else
                    {
                        AppConstants.CheckConnection(VerifyMobileNumberActivity.this);
                    }


                }


            }
        });

        txtSendOTPAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(isFinish)
                {
                    reSendOTPRequest(UserId);
                }
            }
        });

        btnVerifyContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnVerifyContinue.getWindowToken(), 0);

                //String strOTP= edtOTP.getText().toString().trim();

                String strOTP= edtOTP.getValue();
                if(strOTP.equalsIgnoreCase(""))
                {
                    Toast.makeText(VerifyMobileNumberActivity.this,getResources().getString(R.string.enter_otp),Toast.LENGTH_LONG).show();
                }else
                {
                    VeryfyOTPRequest(UserId,strOTP);
                }
            }
        });


    }








    void startCounter()
    {
        
        new CountDownTimer(30000, 1) {
            public void onTick(long millisUntilFinished) {
                
                tvCount.setText(millisUntilFinished/1000 +" sec");
            }
            public void onFinish() {

                txtSendOTPAgain.setVisibility(View.VISIBLE);
                isFinish = true;
                cancel();
            }
        }.start();
    }



    private void updateContactNoRequest(String user_id,final String strMobileNo)
    {
        progresDialog= new ProgressDialog(VerifyMobileNumberActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUserId= params[0];
                String paramMobileNo= params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_mobileno.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UserIdPAir = new BasicNameValuePair("user_id", paramUserId);
                BasicNameValuePair MobileNoPAir = new BasicNameValuePair("mobile_no", paramMobileNo);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UserIdPAir);
                nameValuePairList.add(MobileNoPAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                progresDialog.dismiss();
                Log.e("--edit_mobileno --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                   // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String message=responseObj.getString("message");

                        SharedPreferences.Editor editor=prefUpdate.edit();
                        editor.putString(AppConstants.MOBILE_NO,strMobileNo);
                        editor.commit();

                        textMobileNo.setText(CountryCode+"-"+strMobileNo);

                        Animation bottomDown = AnimationUtils.loadAnimation(VerifyMobileNumberActivity.this,  R.anim.slide_out_down);
                        linSlidingDrawer.startAnimation(bottomDown) ;
                        linSlidingDrawer.setVisibility(View.GONE);

                       // relEditMobileNoView.startAnimation(bottomDown) ;
                        relEditMobileNoView.setVisibility(View.GONE);

                        Toast.makeText(VerifyMobileNumberActivity.this,""+message,Toast.LENGTH_LONG).show();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMobileNumberActivity.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(user_id,strMobileNo);
    }



    private void reSendOTPRequest(String user_id)
    {
        progresDialog= new ProgressDialog(VerifyMobileNumberActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUserId= params[0];
              //  String paramOtp= params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"resend_otp.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UserIdPAir = new BasicNameValuePair("user_id", paramUserId);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UserIdPAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                progresDialog.dismiss();
                Log.e("--resend_otp --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                   // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String otp=responseObj.getString("otp");
                        //edtOTP.setText(""+otp);
                        SharedPreferences.Editor editor=prefUpdate.edit();
                        editor.putString("otp",""+otp);
                        editor.commit();

                        String message=responseObj.getString("message");
                      //  Toast.makeText(VerifyMobileNumberActivity.this,""+message,Toast.LENGTH_LONG).show();
                        isFinish = false;
                        startCounter();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMobileNumberActivity.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(user_id);
    }



    private void VeryfyOTPRequest(String user_id,final String strOTP)
    {
        progresDialog= new ProgressDialog(VerifyMobileNumberActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUserId= params[0];
                String paramOtp= params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"verify_otp.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UserIdPAir = new BasicNameValuePair("user_id", paramUserId);
                BasicNameValuePair OTPPAir = new BasicNameValuePair("otp", paramOtp);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UserIdPAir);
                nameValuePairList.add(OTPPAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                progresDialog.dismiss();
                Log.e("--verify_otp --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String message=responseObj.getString("message");
                        //Toast.makeText(VerifyMobileNumberActivity.this,""+message,Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor=prefUpdate.edit();
                        editor.putString("signup_step","2");
                        editor.commit();

                        Intent intLogin= new Intent(VerifyMobileNumberActivity.this,SignUpStep2Activity.class);
                        startActivity(intLogin);
                        finish();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyMobileNumberActivity.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(user_id,strOTP);
    }



}
