package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import Adepters.GeneralAdapter2;
import Adepters.HeightAdapter;
import Models.HeightBean;
import Models.beanGeneralData;
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

public class Part_EditBasic extends AppCompatActivity {

    EditText edtAgeFrom, edtAgeTo, edtHeightFrom, edtHeightTo, edtMaritalStatus,
            edtEatingHabits, edtSmokingHabits, edtDrinkingHabits, edtPhysicalStatus;
    Button btnUpdate;
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
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
        setContentView(R.layout.activity_edit_basic_pref);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(Part_EditBasic.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        SlidingMenu();
        onclick();

        if (NetworkConnection.hasConnection(Part_EditBasic.this)){
            getProfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( Part_EditBasic.this);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(Part_EditBasic.this)){
            getProfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( Part_EditBasic.this);
        }
    }

    public void init() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Basic Preference");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtAgeFrom = (EditText) findViewById(R.id.edtAgeM);
        edtAgeTo = (EditText) findViewById(R.id.edtAgeF);
        edtHeightFrom = (EditText) findViewById(R.id.edtHeightM);
        edtHeightTo = (EditText) findViewById(R.id.edtHeightF);
        edtMaritalStatus = (EditText) findViewById(R.id.edtMaritalStatus);
        edtEatingHabits = (EditText) findViewById(R.id.edtEatingHabits);
        edtSmokingHabits = (EditText) findViewById(R.id.edtSmokingHabits);
        edtDrinkingHabits = (EditText) findViewById(R.id.edtDrinkingHabits);
        edtPhysicalStatus = findViewById(R.id.edtPhysicalStatus);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        btnUpdate = findViewById(R.id.btnUpdate);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
    }


    public void onclick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Agefrom = edtAgeFrom.getText().toString().trim();
                String Ageto = edtAgeTo.getText().toString().trim();
                String Heightfrom = edtHeightFrom.getText().toString().trim();
                String Heightto = edtHeightTo.getText().toString().trim();
                String MaritalStatus = edtMaritalStatus.getText().toString().trim();
                String PhysicalStatus = edtPhysicalStatus.getText().toString().trim();
                String EatingHabits = edtEatingHabits.getText().toString().trim();
                String SmokingHabits = edtSmokingHabits.getText().toString().trim();
                String DrinkingHabits = edtDrinkingHabits.getText().toString().trim();


                if (hasData(Agefrom) && hasData(Ageto) &&
                        hasData(AppConstants.HeightFromID) && hasData(AppConstants.HeightToID)
                        && hasData(MaritalStatus) &&
                        hasData(PhysicalStatus) &&
                        hasData(EatingHabits) &&
                        hasData(SmokingHabits)) {

                    if (NetworkConnection.hasConnection(Part_EditBasic.this)){
                        updateBasicPreference(Agefrom, Ageto, AppConstants.HeightFromID, AppConstants.HeightToID, MaritalStatus, PhysicalStatus, EatingHabits,
                                SmokingHabits, DrinkingHabits, matri_id);
                    }else
                    {
                        AppConstants.CheckConnection( Part_EditBasic.this);
                    }




                } else {
                    Toast.makeText(Part_EditBasic.this, "Please enter all required fields. ", Toast.LENGTH_LONG).show();
                }

            }
        });
        edtAgeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAgeFrom.setError(null);

                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_age), SlidingDrawer, Slidingpage, btnMenuClose, edtAgeFrom);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAgeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAgeTo.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);


                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_age), SlidingDrawer, Slidingpage, btnMenuClose, edtAgeTo);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHeightFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeightFrom.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);


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

                HeightAdapter adapter = new HeightAdapter(Part_EditBasic.this, "agefrom", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeightFrom);
                rvGeneralView.setAdapter(adapter);


               /* GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeightFrom);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });

        edtHeightTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeightTo.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

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

                HeightAdapter adapter = new HeightAdapter(Part_EditBasic.this, "ageto", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeightTo);
                rvGeneralView.setAdapter(adapter);


               /*
                GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeightTo);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_physical_status1), SlidingDrawer, Slidingpage, btnMenuClose, edtPhysicalStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });
        edtMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtMaritalStatus.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);


                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_marital_status = res.getStringArray(R.array.arr_marital_status1);

                for (int i = 0; i < arr_marital_status.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_marital_status[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(Part_EditBasic.this, "MaritalStatus", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtMaritalStatus, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_marital_status1), SlidingDrawer, Slidingpage, btnMenuClose, edtMaritalStatus);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });

        edtEatingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtEatingHabits.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);


                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_diet);

                for (int i = 0; i < arr_diet.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_diet[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(Part_EditBasic.this, "eating_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtEatingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_diet), SlidingDrawer, Slidingpage, btnMenuClose, edtEatingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });

        edtSmokingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtSmokingHabits.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_smoking = res.getStringArray(R.array.arr_smoking_3);

                for (int i = 0; i < arr_smoking.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_smoking[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(Part_EditBasic.this, "Smoking_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtSmokingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_smoking_3), SlidingDrawer, Slidingpage, btnMenuClose, edtSmokingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });

        edtDrinkingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDrinkingHabits.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_drinking = res.getStringArray(R.array.arr_drinking_3);

                for (int i = 0; i < arr_drinking.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_drinking[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(Part_EditBasic.this, "Drinking_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtDrinkingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(Part_EditBasic.this, getResources().getStringArray(R.array.arr_drinking_3), SlidingDrawer, Slidingpage, btnMenuClose, edtDrinkingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });


    }

    public boolean hasData(String text) {
        if (text == null || text.length() == 0)
            return false;

        return true;
    }

    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(Part_EditBasic.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(Part_EditBasic.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu() {

        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);

        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);


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

    private void getProfileDetail(String strMatriId) {

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

                Log.e("Search by maitri Id", "==" + result);

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

                                String part_frm_age = resItem.getString("part_frm_age");
                                String part_to_age = resItem.getString("part_to_age");
                                String part_height = resItem.getString("part_frm_height");
                                String part_height_to = resItem.getString("part_to_height");

                                String part_m_status = resItem.getString("part_m_status");
                                String part_physical = resItem.getString("part_physical");
                                String part_smoke = resItem.getString("part_smoke");
                                String part_diet = resItem.getString("part_diet");
                                String part_drink = resItem.getString("part_drink");

                                AppConstants.HeightFromID= resItem.getString("part_frm_height_id");
                                AppConstants.HeightToID = resItem.getString("part_to_height_id");

                                edtAgeFrom.setText(part_frm_age);
                                edtAgeTo.setText(part_to_age);
                                edtHeightFrom.setText(part_height);
                                edtHeightTo.setText(part_height_to);
                                edtMaritalStatus.setText(part_m_status);
                                edtPhysicalStatus.setText(part_physical);
                                edtEatingHabits.setText(part_diet);
                                edtSmokingHabits.setText(part_smoke);
                                edtDrinkingHabits.setText(part_drink);


                            }

                        }

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditBasic.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception ex) {
                    Log.e("logg", ex.getMessage());
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public void updateBasicPreference(String Agefrom, String Ageto, String Heightfrom, String Heightto, String MaritalStatus
            , String PhysicalStatus, String EatingHabits, String SmokingHabits, String DrinkingHabits, String MatriID) {
        final ProgressDialog progresDialog = new ProgressDialog(Part_EditBasic.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramAgeM = params[0];
                String paramAgeF = params[1];
                String paramHeightM = params[2];
                String paramHeightF = params[3];
                String paramMaritalStatus = params[4];
                String paramPhysicalStatus = params[5];
                String paramEatingHabits = params[6];
                String paramSmokingHabits = params[7];
                String paramDrinkingHabits = params[8];
                String paramMAtriID = params[9];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "edit_part_basic_details.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair PairAgeM = new BasicNameValuePair("part_age_from", paramAgeM);
                BasicNameValuePair PairAgeF = new BasicNameValuePair("part_age_to", paramAgeF);
                BasicNameValuePair PairHeightM = new BasicNameValuePair("part_height_from", paramHeightM);
                BasicNameValuePair PairHeightF = new BasicNameValuePair("part_height_to", paramHeightF);
                BasicNameValuePair PairMaritalStatus = new BasicNameValuePair("part_marital_status", paramMaritalStatus);
                BasicNameValuePair PairPhysicalStatus = new BasicNameValuePair("part_physical_status", paramPhysicalStatus);
                BasicNameValuePair PairEatingHabits = new BasicNameValuePair("part_diet", paramEatingHabits);
                BasicNameValuePair PairSmokingHabits = new BasicNameValuePair("part_smoking", paramSmokingHabits);
                BasicNameValuePair PairDrinkingHabits = new BasicNameValuePair("part_drinking", paramDrinkingHabits);
                BasicNameValuePair PairMatriID = new BasicNameValuePair("matri_id", paramMAtriID);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(PairAgeM);
                nameValuePairList.add(PairAgeF);
                nameValuePairList.add(PairHeightM);
                nameValuePairList.add(PairHeightF);
                nameValuePairList.add(PairMaritalStatus);
                nameValuePairList.add(PairPhysicalStatus);
                nameValuePairList.add(PairEatingHabits);
                nameValuePairList.add(PairSmokingHabits);
                nameValuePairList.add(PairDrinkingHabits);
                nameValuePairList.add(PairMatriID);


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
                    //JSONObject responseData = responseObj.getJSONObject("responseData");
                    // {"user_id":"10","message":"Registration Detail Successfully Update","status":"1"}
                    String status = responseObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        Intent intLogin = new Intent(Part_EditBasic.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditBasic.this);
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
                    Log.e("exceeee", e.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(Agefrom, Ageto, Heightfrom, Heightto, MaritalStatus, PhysicalStatus, EatingHabits, SmokingHabits, DrinkingHabits, MatriID);
    }

}
