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
import Adepters.HeightAdapter;
import Models.HeightBean;
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

public class EditProfilePhysicalStatus extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    EditText edtHeight, edtWeight, edtBodyType, edtPhysicalStatus;
    LinearLayout linGeneralView;
    RecyclerView rvGeneralView;
    Button btnUpdate;
    Button btnConfirm;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_physical_status);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfilePhysicalStatus.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        SlidingMenu();
        Onclick();
        if (NetworkConnection.hasConnection(EditProfilePhysicalStatus.this)){
            getPRofileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( EditProfilePhysicalStatus.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPRofileDetail(matri_id);
    }

    public void init() {
        btnBack = findViewById(R.id.btnBack);
        textviewHeaderText = findViewById(R.id.textviewHeaderText);
        textviewSignUp = findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Physical Status");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtHeight = (EditText) findViewById(R.id.edtHeight);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        edtBodyType = (EditText) findViewById(R.id.edtBodyType);
        edtPhysicalStatus = (EditText) findViewById(R.id.edtPhysicalStatus);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);

        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);

    }

    public void Onclick() {
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Height = edtHeight.getText().toString().trim();
                String Weight = edtWeight.getText().toString().trim();
                String BodyType = edtBodyType.getText().toString().trim();
                String PhysicalStatus = edtPhysicalStatus.getText().toString().trim();

                Log.e("hight", AppConstants.HeightID);
                if (isValid(v)) {
                    updatePhysicalStatus(matri_id, AppConstants.HeightID, Weight, BodyType, PhysicalStatus);
                } else {
                    Toast.makeText(EditProfilePhysicalStatus.this, "Somthing Wrong ", Toast.LENGTH_SHORT).show();

                }
            }

        });


        edtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeight.setError(null);
                edtHeight.setFocusable(true);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                rvGeneralView.setAdapter(null);
                ArrayList<HeightBean> beanArrayList = new ArrayList<>();
                beanArrayList.add(new HeightBean("48", "Below 4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("54", "4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("55", "4ft 7in - 139cm"));
                beanArrayList.add(new HeightBean("56", "4ft 8in - 142cm"));
                beanArrayList.add(new HeightBean("57", "4ft 9in - 144cm"));
                beanArrayList.add(new HeightBean("58", "4ft 10in - 147cm"));
                beanArrayList.add(new HeightBean("59", "4ft 11in - 149cm"));
                beanArrayList.add(new HeightBean("60", "5ft - 152cm"));
                beanArrayList.add(new HeightBean("61", "5ft 1in - 154cm"));
                beanArrayList.add(new HeightBean("62", "5ft 2in - 157cm"));
                beanArrayList.add(new HeightBean("63", "5ft 3in - 160cm"));
                beanArrayList.add(new HeightBean("64", "5ft 4in - 162cm"));
                beanArrayList.add(new HeightBean("65", "5ft 5in - 165cm"));
                beanArrayList.add(new HeightBean("66", "5ft 6in - 167cm"));
                beanArrayList.add(new HeightBean("67", "5ft 7in - 170cm"));
                beanArrayList.add(new HeightBean("68", "5ft 8in - 172cm"));
                beanArrayList.add(new HeightBean("69", "5ft 9in - 175cm"));
                beanArrayList.add(new HeightBean("70", "5ft 10in - 177cm"));
                beanArrayList.add(new HeightBean("71", "5ft 11in - 180cm"));
                beanArrayList.add(new HeightBean("72", "6ft - 182cm"));
                beanArrayList.add(new HeightBean("73", "6ft 1in - 185cm"));
                beanArrayList.add(new HeightBean("74", "6ft 2in - 187cm"));
                beanArrayList.add(new HeightBean("75", "6ft 3in - 190cm"));
                beanArrayList.add(new HeightBean("76", "6ft 4in - 193cm"));
                beanArrayList.add(new HeightBean("77", "6ft 5in - 195cm"));
                beanArrayList.add(new HeightBean("78", "6ft 6in - 198cm"));
                beanArrayList.add(new HeightBean("79", "6ft 7in - 200cm"));
                beanArrayList.add(new HeightBean("80", "6ft 8in - 203cm"));
                beanArrayList.add(new HeightBean("81", "6ft 9in - 205cm"));
                beanArrayList.add(new HeightBean("82", "6ft 10in - 208cm"));
                beanArrayList.add(new HeightBean("83", "6ft 11in - 210cm"));
                beanArrayList.add(new HeightBean("84", "7ft - 213cm"));
                beanArrayList.add(new HeightBean("89", "Above 7ft - 213cm"));

                HeightAdapter adapter = new HeightAdapter(EditProfilePhysicalStatus.this, "hieght", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeight);
                rvGeneralView.setAdapter(adapter);

            }
        });

        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtWeight.setError(null);
                edtWeight.setFocusable(true);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfilePhysicalStatus.this, getResources().getStringArray(R.array.arr_weight), SlidingDrawer, Slidingpage, btnMenuClose, edtWeight);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtBodyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtBodyType.setError(null);
                edtBodyType.setFocusable(true);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfilePhysicalStatus.this, getResources().getStringArray(R.array.arr_body_type), SlidingDrawer, Slidingpage, btnMenuClose, edtBodyType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfilePhysicalStatus.this, getResources().getStringArray(R.array.arr_physical_status), SlidingDrawer, Slidingpage, btnMenuClose, edtPhysicalStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


    }


    public boolean isValid(View view) {
        if (edtHeight.getText().toString().trim().equalsIgnoreCase("") && edtHeight.getText().toString() == null) {
            edtHeight.setError("Enter Hieght");
            return false;
        }
        if (edtWeight.getText().toString().trim().equalsIgnoreCase("") && edtWeight.getText().toString() == null) {
            edtWeight.setError("Enter Weight");
            return false;
        }

        if (edtBodyType.getText().toString().trim().equalsIgnoreCase("") && edtBodyType.getText().toString() == null) {
            edtBodyType.setError("Enter BodyType");
            return false;
        }
        if (edtPhysicalStatus.getText().toString().trim().equalsIgnoreCase("") && edtPhysicalStatus.getText().toString() == null) {
            edtPhysicalStatus.setError("Enter PhysicalStatus");
            return false;
        }


        return true;
    }

    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfilePhysicalStatus.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfilePhysicalStatus.this, SlidingDrawer, R.anim.slide_left);
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


    private void getPRofileDetail(String strMatriId) {

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

                                edtHeight.setText(resItem.getString("height"));
                                edtWeight.setText(resItem.getString("weight"));
                                edtBodyType.setText(resItem.getString("bodytype"));
                                edtPhysicalStatus.setText(resItem.getString("physicalStatus"));

                            }
                        }


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilePhysicalStatus.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception t) {
                    Log.e("ecxptionphysical", t.getMessage());
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    private void updatePhysicalStatus(String MatriID, String strHeight, String strWeight, String strBodyType, String strPhysicalStatus) {
        progresDialog = new ProgressDialog(EditProfilePhysicalStatus.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String PMatriID = params[0];
                String PHeight = params[1];
                String PWeight = params[2];
                String PBodyType = params[3];
                String PPhysicalStatus = params[4];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "edit_physical_details.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair matriIDPair = new BasicNameValuePair("matri_id", PMatriID);
                BasicNameValuePair HeightPair = new BasicNameValuePair("height", PHeight);
                BasicNameValuePair WeightPair = new BasicNameValuePair("weight", PWeight);
                BasicNameValuePair BodyTypePair = new BasicNameValuePair("body_type", PBodyType);
                BasicNameValuePair PhysicalStatusPair = new BasicNameValuePair("physical_status", PPhysicalStatus);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(matriIDPair);
                nameValuePairList.add(HeightPair);
                nameValuePairList.add(WeightPair);
                nameValuePairList.add(BodyTypePair);
                nameValuePairList.add(PhysicalStatusPair);

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

                } catch (Exception uee) {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce) {
                super.onPostExecute(Ressponce);
                progresDialog.dismiss();
                Log.e("--cast --", "==" + Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status = responseObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        Intent intLogin = new Intent(EditProfilePhysicalStatus.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilePhysicalStatus.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(MatriID, strHeight, strWeight, strBodyType, strPhysicalStatus);

    }


}

