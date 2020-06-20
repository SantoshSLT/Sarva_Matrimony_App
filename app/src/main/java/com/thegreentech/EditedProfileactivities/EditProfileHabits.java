package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import Adepters.GeneralAdapter;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AnimUtils;
import utills.AppConstants;
import utills.NetworkConnection;

public class EditProfileHabits extends AppCompatActivity {


    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    EditText edtDiet, edtSmoking, edtDrinking;
    Button btnUpdate;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    LinearLayout linGeneralView;
    RecyclerView rvGeneralView;
    Button btnConfirm;
    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_habits);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfileHabits.this);
        matri_id = prefUpdate.getString("matri_id", "");


        init();
        SlidingMenu();
        onclick();

        if (NetworkConnection.hasConnection(EditProfileHabits.this)){
            getProfileData(matri_id);


        }else
        {
            AppConstants.CheckConnection( EditProfileHabits.this);
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        getProfileData(matri_id);
    }

    public void init() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Habits and Hobbies");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        edtDiet = (EditText) findViewById(R.id.edtDiet);
        edtSmoking = (EditText) findViewById(R.id.edtSmoking);
        edtDrinking = (EditText) findViewById(R.id.edtDrinking);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);
        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
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

        edtDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDiet.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileHabits.this, getResources().getStringArray(R.array.arr_diet_2), SlidingDrawer, Slidingpage, btnMenuClose, edtDiet);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtSmoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtSmoking.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileHabits.this, getResources().getStringArray(R.array.arr_smoking_2), SlidingDrawer, Slidingpage, btnMenuClose, edtSmoking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtDrinking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDrinking.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileHabits.this, getResources().getStringArray(R.array.arr_drinking_2), SlidingDrawer, Slidingpage, btnMenuClose, edtDrinking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Diet = edtDiet.getText().toString().trim();
                String Smoking = edtSmoking.getText().toString().trim();
                String Drinking = edtDrinking.getText().toString().trim();

                if (hasData(v)) {
                    if (NetworkConnection.hasConnection(EditProfileHabits.this)){
                        updateHabits(matri_id,Diet,Smoking,Drinking);
                    }else
                    {
                        AppConstants.CheckConnection( EditProfileHabits.this);
                    }


                }
                else {
                    Toast.makeText(EditProfileHabits.this, "Somthing Wrong ", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public boolean hasData(View view) {
        if (edtDiet.getText().toString().trim().equalsIgnoreCase("") && edtDiet.getText().toString() == null) {
            Toast.makeText(EditProfileHabits.this, "Enter Diet Habbit", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtSmoking.getText().toString().trim().equalsIgnoreCase("") && edtSmoking.getText().toString() == null) {
            Toast.makeText(EditProfileHabits.this, "Enter Smoke Habit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtDrinking.getText().toString().trim().equalsIgnoreCase("") && edtDrinking.getText().toString() == null) {
            Toast.makeText(EditProfileHabits.this, "Enter Drink Habit", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfileHabits.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileHabits.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu() {

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);


        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });

    }


    private void getProfileData(String strMatriId) {
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

                                edtDiet.setText(resItem.getString("diet"));
                                edtSmoking.setText(resItem.getString("smoke"));
                                edtDrinking.setText(resItem.getString("drink"));
                            }
                        }

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileHabits.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception t) {
                    Log.e("hobbi", t.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public  void  updateHabits(String MatriId,String Diet,String Smoking,String Drinking){

        progresDialog= new ProgressDialog(EditProfileHabits.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String paramMetriID = params[0];
                String paramDiet = params[1];
                String paramsmoking = params[2];
                String paramDrinking = params[3];




                HttpClient httpClient = new DefaultHttpClient();

                // String URL= AppConstants.MAIN_URL +"edit_step1.php";
                String URL= AppConstants.MAIN_URL +"edit_habbit_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPAir = new BasicNameValuePair("matri_id", paramMetriID);
                BasicNameValuePair DietPAir = new BasicNameValuePair("diet", paramDiet);
                BasicNameValuePair SmokingPAir = new BasicNameValuePair("smoking", paramsmoking);
                BasicNameValuePair DrinkingIdPAir = new BasicNameValuePair("drinking", paramDrinking);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(MatriIdPAir);
                nameValuePairList.add(DietPAir);
                nameValuePairList.add(SmokingPAir);
                nameValuePairList.add(DrinkingIdPAir);


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
//                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String message=responseObj.getString("message");
                        Toast.makeText(EditProfileHabits.this,""+message,Toast.LENGTH_LONG).show();

                        Intent intLogin= new Intent(EditProfileHabits.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileHabits.this);
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
                    Log.e("exception0",e.getMessage());

                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriId,Diet,Smoking,Drinking);




    }


}
