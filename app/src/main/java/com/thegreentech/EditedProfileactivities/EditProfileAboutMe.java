package com.thegreentech.EditedProfileactivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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

public class EditProfileAboutMe extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    EditText edtOtherInformatio;
    Button btnRegisterNow;
    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_about_me);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfileAboutMe.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        onclick();
        if (NetworkConnection.hasConnection(EditProfileAboutMe.this)){
            getProfileDetailRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection( this);
        }

    }

    public void init() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update About Me");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtOtherInformatio = findViewById(R.id.edtOtherInformatio);
        btnRegisterNow = findViewById(R.id.btnRegisterNow);
    }

    public void onclick() {

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });
        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OtherInformatio=edtOtherInformatio.getText().toString().trim();
                if (!OtherInformatio.equalsIgnoreCase("") && OtherInformatio != null) {
                    if (NetworkConnection.hasConnection(EditProfileAboutMe.this)){
                        getUpdateProfileSteps2(OtherInformatio,matri_id);
                    }else
                    {
                        AppConstants.CheckConnection( EditProfileAboutMe.this);
                    }

                }
                else {
                    edtOtherInformatio.setFocusable(true);

                }
            }
        });
    }



    private void getProfileDetailRequest(String strMatriId) {
        progresDialog = new ProgressDialog(EditProfileAboutMe.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee) //UnsupportedEncodingException
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("Profile Id", "==" + result);

                String finalresult = "";
                try {

                    finalresult = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(finalresult);


                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                edtOtherInformatio.setText(resItem.getString("profile_text"));

                                String OtherInformatio = edtOtherInformatio.getText().toString().trim();

                            }

                        }


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileAboutMe.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }



    private void getUpdateProfileSteps2(String strOtherInformatio,
                                        String matri_id)
    {
        progresDialog= new ProgressDialog(EditProfileAboutMe.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String POtherInformatio= params[0];
                String Pmatri_id= params[1];


                HttpClient httpClient = new DefaultHttpClient();

              //  String URL= AppConstants.MAIN_URL +"edit_step2.php";
                String URL= AppConstants.MAIN_URL +"edit_about_me.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair OtherInformatioPair = new BasicNameValuePair("something_about_me", POtherInformatio);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", Pmatri_id);


                List<NameValuePair> nameValuePairList = new ArrayList<>();

                nameValuePairList.add(OtherInformatioPair);
                nameValuePairList.add(MatriIdPair);


                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));
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
                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        Intent intLogin= new Intent(EditProfileAboutMe.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileAboutMe.this);
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

        sendPostReqAsyncTask.execute(strOtherInformatio,matri_id);

    }

}
