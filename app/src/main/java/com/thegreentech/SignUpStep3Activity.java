package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.CasteMultiSelectionAdapter;
import Adepters.CityMultiSelectionAdapter;
import Adepters.CountryMultiSelectionAdapter;
import Adepters.EducationsMultiSelectionAdapter;
import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter2;
import Adepters.HeightAdapter;
import Adepters.MotherTongueMultiSelectionAdapter;
import Adepters.OccupationMultiSelectionAdapter;
import Adepters.ReligionMultiSelectionAdapter;
import Adepters.StateMultiSelectionAdapter;
import Models.HeightBean;
import Models.beanCaste;
import Models.beanCity;
import Models.beanCountries;
import Models.beanEducation;
import Models.beanGeneralData;
import Models.beanMotherTongue;
import Models.beanOccupation;
import Models.beanReligion;
import Models.beanState;
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

public class SignUpStep3Activity extends AppCompatActivity {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;

    EditText edtAgeM, edtAgeF, edtHeightM, edtHeightF, edtMaritalStatus, edtPhysicalStatus, edtEatingHabits, edtSmokingHabits,
            edtDrinkingHabits, edtReligion, edtCaste, edtHaveDosh, edtStar, edtMotherTongue, edtCountry, edtState, edtCity,
            edtDoshType, edtHighestEducation, edtOccupation, edtAnnualIncome, edtPartnerPreference;

    Button btnContinue;
    String DoshType_val = "";

    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    LinearLayout linManglik, llDoshType, linReligion, linCaste, linMotherTongue, linCountry, linState, linCity, linHighestEducation, linOccupation, linGeneralView;
    EditText edtSearchReligion, edtSearchCaste, edtSearchMotherTongue, edtSearchCountry, edtSearchState, edtSearchCity, edtSearchHighestEducation, edtSearchOccupation;
    RecyclerView rvDosh, rvReligion, rvCaste, rvMotherTongue, rvCountry, rvState, rvCity, rvHighestEducation, rvOccupation, rvGeneralView;
    Button btnConfirm;


    ArrayList<beanReligion> arrReligion = new ArrayList<beanReligion>();
    ReligionMultiSelectionAdapter religionAdapter = null;

    ArrayList<beanCaste> arrCaste = new ArrayList<beanCaste>();
    CasteMultiSelectionAdapter casteAdapter = null;

    ArrayList<beanMotherTongue> arrMotherTongue = new ArrayList<beanMotherTongue>();
    MotherTongueMultiSelectionAdapter motherTongueAdapter = null;

    ArrayList<beanCountries> arrCountry = new ArrayList<beanCountries>();
    CountryMultiSelectionAdapter countryAdapter = null;

    ArrayList<beanState> arrState = new ArrayList<beanState>();
    StateMultiSelectionAdapter stateAdapter = null;

    ArrayList<beanCity> arrCity = new ArrayList<beanCity>();
    CityMultiSelectionAdapter cityAdapter = null;

    ArrayList<beanEducation> arrEducation = new ArrayList<beanEducation>();
    EducationsMultiSelectionAdapter educationAdapter = null;

    ArrayList<beanOccupation> arrOccupation = new ArrayList<beanOccupation>();
    OccupationMultiSelectionAdapter occupationAdapter = null;

    SharedPreferences prefUpdate;
    ;
    ProgressDialog progresDialog;
    String UserId = "";
    AlertDialog.Builder dialogbuilder;
    ProgressBar progressBarSlider4;
    TextView tvNoDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step3);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(SignUpStep3Activity.this);
        UserId = prefUpdate.getString("user_id_r", "");

        init();
        onClickEvent();
        SlidingMenu();

        getGeneralData();

        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getMotherToungeRequest();
            getMotherTongue();
        } else {
            AppConstants.CheckConnection(SignUpStep3Activity.this);
        }

    }

    public void init() {
        progressBarSlider4 = findViewById(R.id.progressBarSlider4);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewSignUp.setText("REGISTER NEW");
        btnBack.setVisibility(View.GONE);
        textviewHeaderText.setVisibility(View.GONE);
        // textviewHeaderText.setText("LOGIN");
        //textviewSignUp.setBackgroundResource(R.drawable.bg_orange_rounded_corner);

        linManglik = findViewById(R.id.linManglik);
        edtAgeM = (EditText) findViewById(R.id.edtAgeM);
        edtAgeF = (EditText) findViewById(R.id.edtAgeF);
        edtHeightM = (EditText) findViewById(R.id.edtHeightM);
        edtHeightF = (EditText) findViewById(R.id.edtHeightF);
        edtMaritalStatus = (EditText) findViewById(R.id.edtMaritalStatus);
        edtPhysicalStatus = (EditText) findViewById(R.id.edtPhysicalStatus);
        edtEatingHabits = (EditText) findViewById(R.id.edtEatingHabits);
        edtSmokingHabits = (EditText) findViewById(R.id.edtSmokingHabits);
        edtDrinkingHabits = (EditText) findViewById(R.id.edtDrinkingHabits);
        edtReligion = (EditText) findViewById(R.id.edtReligion);
        edtCaste = (EditText) findViewById(R.id.edtCaste);
        edtStar = (EditText) findViewById(R.id.edtStar);
        edtMotherTongue = (EditText) findViewById(R.id.edtMotherTongue);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        edtState = (EditText) findViewById(R.id.edtState);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtHighestEducation = (EditText) findViewById(R.id.edtHighestEducation);
        edtOccupation = (EditText) findViewById(R.id.edtOccupation);// edit
        edtAnnualIncome = (EditText) findViewById(R.id.edtAnnualIncome);// edit
        edtPartnerPreference = (EditText) findViewById(R.id.edtPartnerPreference);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
        llDoshType = findViewById(R.id.llDoshType);
        edtHaveDosh = (EditText) findViewById(R.id.edtManglik);
        edtDoshType = findViewById(R.id.edtDoshType);

        rvDosh = findViewById(R.id.rvDoshType);
        edtDoshType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                edtDoshType.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_manglik_type);

                for (int i = 0; i < arr_diet.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_diet[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "Dosh_Type", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtDoshType, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_diet), SlidingDrawer, Slidingpage, btnMenuClose, edtDoshType);
                    rvGeneralView.setAdapter(generalAdapter);
                }


                   /* listDosh.add(new DoshType("Manglik",false));
                    listDosh.add(new DoshType("Sarpa Dosh",false));
                    listDosh.add(new DoshType("Kala Sarpa Dosh",false));
                    listDosh.add(new DoshType("Rahu Dosh",false));
                    listDosh.add(new DoshType("Kethu Dosh",false));
                    listDosh.add(new DoshType("KALATHEA Dosh",false));

                    LinearLayoutManager manager = new LinearLayoutManager(SignUpStep3Activity.this,LinearLayoutManager.VERTICAL,false);
                    rvDosh.setLayoutManager(manager);
                    doshAdapter = new DoshAdapter(SignUpStep3Activity.this,listDosh,SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType,btnConfirm);
                    rvDosh.setAdapter(doshAdapter);*/


            }
        });


    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        dialogbuilder = new AlertDialog.Builder(SignUpStep3Activity.this);
        dialogbuilder.setTitle(getResources().getString(R.string.app_name));
        dialogbuilder.setMessage(getResources().getString(R.string.go_back));
        dialogbuilder.setCancelable(false);
        dialogbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             /*   SharedPreferences.Editor editor=prefUpdate.edit();
                editor.putString("signup_step","0");
                editor.commit();*/
                Intent intLogin = new Intent(SignUpStep3Activity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });

        dialogbuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogbuilder.create();
        alert.show();
    }

    public void onClickEvent() {

        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep3Activity.this);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage("Do you want to clear previous data?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefUpdate.edit();
                        editor.putString("signup_step", "0");
                        editor.commit();
                        Intent intLogin = new Intent(SignUpStep3Activity.this, SignUpStep1Activity.class);
                        startActivity(intLogin);
                        finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intLogin = new Intent(SignUpStep3Activity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intLogin= new Intent(SignUpStep3Activity.this,SignUpStep4Activity.class);
//                startActivity(intLogin);
//                finish();

                String AgeM = edtAgeM.getText().toString().trim();
                String AgeF = edtAgeF.getText().toString().trim();
                String HeightM = edtHeightM.getText().toString().trim();
                String HeightF = edtHeightF.getText().toString().trim();
                String MaritalStatus = edtMaritalStatus.getText().toString().trim();
                String PhysicalStatus = edtPhysicalStatus.getText().toString().trim();
                String EatingHabits = edtEatingHabits.getText().toString().trim();
                String SmokingHabits = edtSmokingHabits.getText().toString().trim();
                String DrinkingHabits = edtDrinkingHabits.getText().toString().trim();
                String DoshType = edtDoshType.getText().toString().trim();
                //String Religion=edtReligion.getText().toString().trim();
                //String Caste=edtCaste.getText().toString().trim();
                String Manglik = edtHaveDosh.getText().toString().trim();
                String Star = edtStar.getText().toString().trim();
                //String MotherTongue=edtMotherTongue.getText().toString().trim();
                //String Country=edtCountry.getText().toString().trim();
                //String State=edtState.getText().toString().trim();
                //String City=edtCity.getText().toString().trim();
                //String HighestEducation=edtHighestEducation.getText().toString().trim();
                //String Occupation=edtOccupation.getText().toString().trim();
                String AnnualIncome = edtAnnualIncome.getText().toString().trim();
                String PartnerPreference = edtPartnerPreference.getText().toString().trim();

                Log.e("R_id", AppConstants.ReligionId);
                Log.e("HeightID", AppConstants.HeightFromID);
                Log.e("HeightID", AppConstants.HeightToID);
                Log.e("HeightID", AppConstants.MotherTongueId);
                Log.e("HeightID", AppConstants.CountryId);
                Log.e("HeightID", AppConstants.StateId);
                Log.e("HeightID", AppConstants.CityId);
                Log.e("HeightID", AppConstants.EducationId);
                Log.e("HeightID", AppConstants.OccupationID);


                if (hasData(AgeM) && hasData(AgeF) && hasData(HeightM) && hasData(HeightF)
                        && hasData(MaritalStatus) && hasData(PhysicalStatus) && hasData(EatingHabits) && hasData(SmokingHabits)
                        && hasData(DrinkingHabits) && hasData(AppConstants.ReligionId) && hasData(AppConstants.CasteId)
                        && hasData(Manglik) && hasData(Star) && hasData(AppConstants.MotherTongueId) && hasData(AppConstants.CountryId) && hasData(AppConstants.StateId)
                        && hasData(AppConstants.CityId) && hasData(AppConstants.EducationId) && hasData(AppConstants.OccupationID) && hasData(AnnualIncome)
                        && hasData(PartnerPreference)) {
                    if (Manglik.equalsIgnoreCase("Yes") && DoshType.equalsIgnoreCase("")) {
                        Toast.makeText(SignUpStep3Activity.this, "Please select Dosh type.", Toast.LENGTH_LONG).show();
                    }
                    getRegistationSteps3(AgeM, AgeF, AppConstants.HeightFromID, AppConstants.HeightToID,/*MaritalStatus,*/ AppConstants.MaritalStatusName, PhysicalStatus,/*EatingHabits,*/
                            AppConstants.EatingHabitNAme
                            /*SmokingHabits,*/, AppConstants.SmokingHabitsNAME,/*DrinkingHabits,*/AppConstants.DrinkingNAME, AppConstants.ReligionId, AppConstants.CasteId, Manglik
                            /*Star,*/, AppConstants.StarNAME, AppConstants.MotherTongueId, AppConstants.CountryId, AppConstants.StateId, AppConstants.CityId,
                            AppConstants.EducationId, AppConstants.OccupationID, AnnualIncome, PartnerPreference, UserId,/*DoshType*/AppConstants.DosTypeNAME);
                } else {
                    Toast.makeText(SignUpStep3Activity.this, "Please enter all required fields. ", Toast.LENGTH_LONG).show();
                }

            }
        });


        edtReligion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) ;
                {
                    AppConstants.CasteId = "";
                    AppConstants.CasteName = "";

                    edtCaste.setText("");

                    GonelidingDrower();

                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getReligionRequest();
                        getReligions();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }

                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        Log.e("edtReligionRelgian_id", AppConstants.ReligionId);
                        rvCaste.setAdapter(null);
                        getCastRequest(AppConstants.ReligionId);
                        getCaste();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }
                }
            }
        });


        edtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) ;
                {
                    AppConstants.StateName = "";
                    AppConstants.StateId = "";
                    edtState.setText("");
                    rvState.setAdapter(null);

                    AppConstants.CityName = "";
                    AppConstants.CityId = "";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getStateRequest(AppConstants.CountryId);
                    getStates();
                }
            }
        });


        edtState.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) ;
                {
                    AppConstants.CityName = "";
                    AppConstants.CityId = "";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getCityRequest(AppConstants.CountryId, AppConstants.StateId);
                    getCity();
                }
            }
        });

    }


    public void getGeneralData() {
        edtAgeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAgeM.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                llDoshType.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_age), SlidingDrawer, Slidingpage, btnMenuClose, edtAgeM);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAgeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAgeF.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                llDoshType.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_age), SlidingDrawer, Slidingpage, btnMenuClose, edtAgeF);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHeightM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeightM.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                llDoshType.setVisibility(View.GONE);

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

                HeightAdapter adapter = new HeightAdapter(SignUpStep3Activity.this, "agefrom", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeightM);
                rvGeneralView.setAdapter(adapter);


               /* GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeightM);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });

        edtHeightF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeightF.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                llDoshType.setVisibility(View.GONE);
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

                HeightAdapter adapter = new HeightAdapter(SignUpStep3Activity.this, "ageto", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeightF);
                rvGeneralView.setAdapter(adapter);


               /*
                GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeightF);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });


        edtMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtMaritalStatus.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                llDoshType.setVisibility(View.GONE);

                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_marital_status = res.getStringArray(R.array.arr_marital_status1);

                for (int i = 0; i < arr_marital_status.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_marital_status[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "MaritalStatus", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtMaritalStatus, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_marital_status1), SlidingDrawer, Slidingpage, btnMenuClose, edtMaritalStatus);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                llDoshType.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_physical_status1), SlidingDrawer, Slidingpage, btnMenuClose, edtPhysicalStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtEatingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtEatingHabits.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                llDoshType.setVisibility(View.GONE);

                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_diet);

                for (int i = 0; i < arr_diet.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_diet[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "eating_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtEatingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_diet), SlidingDrawer, Slidingpage, btnMenuClose, edtEatingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });

        edtSmokingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtSmokingHabits.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
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
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "Smoking_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtSmokingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_smoking_3), SlidingDrawer, Slidingpage, btnMenuClose, edtSmokingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });


        edtDrinkingHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDrinkingHabits.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
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
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "Drinking_Habits", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtDrinkingHabits, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_drinking_3), SlidingDrawer, Slidingpage, btnMenuClose, edtDrinkingHabits);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });


        edtReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtReligion.setError(null);
                edtSearchReligion.setText("");
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.VISIBLE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);


            }
        });

        edtCaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCaste.setError(null);
                edtSearchCaste.setText("");
                if (arrCaste.size() > 0) {

                } else {
                    rvCaste.setAdapter(null);
                }
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.VISIBLE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });


        edtHaveDosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHaveDosh.setError(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_manglik), SlidingDrawer, Slidingpage, btnMenuClose, edtHaveDosh);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHaveDosh.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String strSelectedType = edtHaveDosh.getText().toString().trim();
                if (strSelectedType.equalsIgnoreCase("Yes")) {
                    llDoshType.setVisibility(View.VISIBLE);
                } else {
                    llDoshType.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        edtStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtStar.setError(null);

                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);


                ArrayList<beanGeneralData> arrGeneralData = new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_star = res.getStringArray(R.array.arr_star);

                for (int i = 0; i < arr_star.length; i++) {
                    arrGeneralData.add(new beanGeneralData("" + i, arr_star[i], false));
                }

                if (arrGeneralData.size() > 0) {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep3Activity.this, "Star", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtStar, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_star), SlidingDrawer, Slidingpage, btnMenuClose, edtStar);
                    rvGeneralView.setAdapter(generalAdapter);
                }


            }
        });


        edtMotherTongue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtMotherTongue.setError(null);
                edtSearchMotherTongue.setText("");
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });


        edtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCountry.setError(null);
                edtSearchCountry.setText("");

                linReligion.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.VISIBLE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);


            }
        });

        edtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtState.setError(null);
                edtSearchState.setText("");
                if (arrState.size() > 0) {

                } else {
                    rvState.setAdapter(null);
                }
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });

        edtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCity.setError(null);
                edtSearchCity.setText("");

                if (arrCity.size() > 0) {

                } else {
                    rvCity.setAdapter(null);
                }
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });


        edtHighestEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHighestEducation.setError(null);
                edtSearchHighestEducation.setText("");
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });

        edtOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });


        edtAnnualIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep3Activity.this, getResources().getStringArray(R.array.arr_annual_income), SlidingDrawer, Slidingpage, btnMenuClose, edtAnnualIncome);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConstants.MaritalStatusName = "";
        AppConstants.MaritalStatusName = "";
        AppConstants.EatingHabitNAme = "";
        AppConstants.SmokingHabitsNAME = "";
        AppConstants.DrinkingNAME = "";
        AppConstants.StarNAME = "";
        AppConstants.DosTypeNAME = "";
        AppConstants.CountryId = "";
        AppConstants.StateId = "";
        AppConstants.CityId = "";
        AppConstants.CountryName = "";
        AppConstants.StateName = "";
        AppConstants.CityName = "";
        AppConstants.ReligionId = "";
        AppConstants.CasteId = "";
        AppConstants.MotherTongueId = "";
        AppConstants.EducationId = "";
        AppConstants.AditionalEducationId = "";
        AppConstants.OccupationID = "";
        AppConstants.ReligionName = "";
        AppConstants.CasteName = "";
        AppConstants.MotherTongueName = "";
        AppConstants.EducationName = "";
        AppConstants.AditionalEducationName = "";
        AppConstants.OccupationName = "";

        ArrayList<beanReligion> arrReligion = null;
        ReligionMultiSelectionAdapter religionAdapter = null;

        ArrayList<beanCaste> arrCaste = null;
        CasteMultiSelectionAdapter casteAdapter = null;

        ArrayList<beanMotherTongue> arrMotherTongue = null;
        MotherTongueMultiSelectionAdapter motherTongueAdapter = null;

        ArrayList<beanCountries> arrCountry = null;
        CountryMultiSelectionAdapter countryAdapter = null;

        ArrayList<beanState> arrState = null;
        StateMultiSelectionAdapter stateAdapter = null;

        ArrayList<beanCity> arrCity = null;
        CityMultiSelectionAdapter cityAdapter = null;

        ArrayList<beanEducation> arrEducation = null;
        EducationsMultiSelectionAdapter educationAdapter = null;

        ArrayList<beanOccupation> arrOccupation = null;
        OccupationMultiSelectionAdapter occupationAdapter = null;

    }


    public boolean hasData(String text) {
        if (text == null || text.length() == 0)
            return false;

        return true;
    }


    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(SignUpStep3Activity.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(SignUpStep3Activity.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu() {
        linReligion = (LinearLayout) findViewById(R.id.linReligion);
        linCaste = (LinearLayout) findViewById(R.id.linCaste);
        linMotherTongue = (LinearLayout) findViewById(R.id.linMotherTongue);
        linCountry = (LinearLayout) findViewById(R.id.linCountry);
        linState = (LinearLayout) findViewById(R.id.linState);
        linCity = (LinearLayout) findViewById(R.id.linCity);
        linHighestEducation = (LinearLayout) findViewById(R.id.linHighestEducation);
        linOccupation = (LinearLayout) findViewById(R.id.linOccupation);
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);

        edtSearchReligion = (EditText) findViewById(R.id.edtSearchReligion);
        edtSearchCaste = (EditText) findViewById(R.id.edtSearchCaste);
        edtSearchMotherTongue = (EditText) findViewById(R.id.edtSearchMotherTongue);
        edtSearchCountry = (EditText) findViewById(R.id.edtSearchCountry);
        edtSearchState = (EditText) findViewById(R.id.edtSearchState);
        edtSearchCity = (EditText) findViewById(R.id.edtSearchCity);
        edtSearchHighestEducation = (EditText) findViewById(R.id.edtSearchHighestEducation);
        edtSearchOccupation = (EditText) findViewById(R.id.edtSearchOccupation);


        rvReligion = (RecyclerView) findViewById(R.id.rvReligion);
        rvCaste = (RecyclerView) findViewById(R.id.rvCaste);
        rvMotherTongue = (RecyclerView) findViewById(R.id.rvMotherTongue);
        rvCountry = (RecyclerView) findViewById(R.id.rvCountry);
        rvState = (RecyclerView) findViewById(R.id.rvState);
        rvCity = (RecyclerView) findViewById(R.id.rvCity);
        rvHighestEducation = (RecyclerView) findViewById(R.id.rvHighestEducation);
        rvOccupation = (RecyclerView) findViewById(R.id.rvOccupation);
        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        rvReligion.setLayoutManager(new LinearLayoutManager(this));
        rvReligion.setHasFixedSize(true);
        rvCaste.setLayoutManager(new LinearLayoutManager(this));
        rvCaste.setHasFixedSize(true);
        rvMotherTongue.setLayoutManager(new LinearLayoutManager(this));
        rvMotherTongue.setHasFixedSize(true);
        rvCountry.setLayoutManager(new LinearLayoutManager(this));
        rvCountry.setHasFixedSize(true);
        rvState.setLayoutManager(new LinearLayoutManager(this));
        rvState.setHasFixedSize(true);
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setHasFixedSize(true);
        rvHighestEducation.setLayoutManager(new LinearLayoutManager(this));
        rvHighestEducation.setHasFixedSize(true);
        rvOccupation.setLayoutManager(new LinearLayoutManager(this));
        rvOccupation.setHasFixedSize(true);
        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);
        rvDosh.setLayoutManager(new LinearLayoutManager(this));
        rvDosh.setHasFixedSize(true);

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


    public void getReligions() {
        try {
            edtSearchReligion.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrReligion.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchReligion.getText().toString().toLowerCase(Locale.getDefault());
                        religionAdapter.filter(text);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void afterTextChanged(Editable arg0) {
                }
            });
        } catch (Exception e) {

        }
    }


    public void getCaste() {
        try {
            edtSearchCaste.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrCaste.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchCaste.getText().toString().toLowerCase(Locale.getDefault());
                        casteAdapter.filter(text);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void afterTextChanged(Editable arg0) {
                }
            });
        } catch (Exception e) {

        }
    }


    public void getMotherTongue() {
        try {
            edtSearchMotherTongue.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrMotherTongue.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchMotherTongue.getText().toString().toLowerCase(Locale.getDefault());
                        motherTongueAdapter.filter(text);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void afterTextChanged(Editable arg0) {
                }
            });
        } catch (Exception e) {

        }
    }


    public void getCountries() {
        try {
            edtSearchCountry.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrCountry.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchCountry.getText().toString().toLowerCase(Locale.getDefault());
                        countryAdapter.filter(text);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void afterTextChanged(Editable arg0) {
                }
            });
        } catch (Exception e) {

        }
    }


    public void getStates() {
        edtSearchState.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrState.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchState.getText().toString().toLowerCase(Locale.getDefault());
                    stateAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });
    }


    public void getCity() {
        edtSearchCity.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrCity.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchCity.getText().toString().toLowerCase(Locale.getDefault());
                    cityAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    public void getHighestEducation() {
        edtSearchHighestEducation.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrEducation.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchHighestEducation.getText().toString().toLowerCase(Locale.getDefault());
                    educationAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    public void getOccupations() {
        edtSearchOccupation.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrOccupation.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchOccupation.getText().toString().toLowerCase(Locale.getDefault());
                    occupationAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    private void getMotherToungeRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "mother_tounge.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                //   Log.e("motherTougueparamUsername",paramUsername);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

//                List<NameValuePair> nameValuePairList = new ArrayList<>();
//                nameValuePairList.add(UsernamePAir);

                try {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--mother_tounge --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    arrMotherTongue = new ArrayList<beanMotherTongue>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id = resItem.getString("mtongue_id");
                            String mtongue_name = resItem.getString("mtongue_name");

                            arrMotherTongue.add(new beanMotherTongue(mtongue_id, mtongue_name, false));

                        }

                        if (arrMotherTongue.size() > 0) {
                            Collections.sort(arrMotherTongue, new Comparator<beanMotherTongue>() {
                                @Override
                                public int compare(beanMotherTongue lhs, beanMotherTongue rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            motherTongueAdapter = new MotherTongueMultiSelectionAdapter(SignUpStep3Activity.this, arrMotherTongue, SlidingDrawer, Slidingpage, btnMenuClose, edtMotherTongue, btnConfirm);
                            rvMotherTongue.setAdapter(motherTongueAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getHighestEducationRequest();
                        getHighestEducation();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getHighestEducationRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "education.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

               /* Log.e("educationparamUsername",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    arrEducation = new ArrayList<beanEducation>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id = resItem.getString("edu_id");
                            String edu_name = resItem.getString("edu_name");

                            arrEducation.add(new beanEducation(edu_id, edu_name, false));

                        }

                        if (arrEducation.size() > 0) {
                            Collections.sort(arrEducation, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });
                            educationAdapter = new EducationsMultiSelectionAdapter(SignUpStep3Activity.this, arrEducation, SlidingDrawer, Slidingpage, btnMenuClose, edtHighestEducation, btnConfirm);
                            rvHighestEducation.setAdapter(educationAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getOccupationsRequest();
                        getOccupations();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getOccupationsRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "occupation.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
               /* Log.e("Occupationname",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");



                    arrOccupation = new ArrayList<beanOccupation>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id = resItem.getString("ocp_id");
                            String edu_name = resItem.getString("ocp_name");

                            arrOccupation.add(new beanOccupation(edu_id, edu_name, false));

                        }

                        if (arrOccupation.size() > 0) {
                            Collections.sort(arrOccupation, new Comparator<beanOccupation>() {
                                @Override
                                public int compare(beanOccupation lhs, beanOccupation rhs) {
                                    return lhs.getOccupation_name().compareTo(rhs.getOccupation_name());
                                }
                            });
                            occupationAdapter = new OccupationMultiSelectionAdapter(SignUpStep3Activity.this, arrOccupation, SlidingDrawer, Slidingpage, btnMenuClose, edtOccupation, btnConfirm);
                            rvOccupation.setAdapter(occupationAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getReligionRequest();
                        getReligions();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getReligionRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                //  String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "religion.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
               /* Log.e("religionname",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try {
                   /*  UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                     httpPost.setEntity(urlEncodedFormEntity);*/
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
                Log.e("--religion --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");



                    arrReligion = new ArrayList<beanReligion>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String religionId = resItem.getString("religion_id");
                            String religionName = resItem.getString("religion_name");

                            arrReligion.add(new beanReligion(religionId, religionName, false));

                        }

                        if (arrReligion.size() > 0) {
                            Collections.sort(arrReligion, new Comparator<beanReligion>() {
                                @Override
                                public int compare(beanReligion lhs, beanReligion rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            religionAdapter = new ReligionMultiSelectionAdapter(SignUpStep3Activity.this, arrReligion, SlidingDrawer, Slidingpage, btnMenuClose, edtReligion, btnConfirm);
                            rvReligion.setAdapter(religionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getCountrysRequest();
                        getCountries();
                    } else {
                        AppConstants.CheckConnection(SignUpStep3Activity.this);
                    }
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getCastRequest(String strReligion) {

        Log.e("edklgjioAJPO", strReligion);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                //  String URL= AppConstants.MAIN_URL +"part_cast.php";/
                //String URL= AppConstants.MAIN_URL +"cast.php";//?religion_id="+paramUsername;
                String URL = AppConstants.MAIN_URL + "caste_multiple.php";//?religion_id="+paramUsername;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                Log.e("castparamUsername", paramUsername);
                // BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion_id", paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--cast --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");



                    arrCaste = new ArrayList<beanCaste>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String casteId = resItem.getString("caste_id");
                            String casteName = resItem.getString("caste_name");

                            arrCaste.add(new beanCaste(casteId, casteName, false));

                        }

                        if (arrCaste.size() > 0) {
                            Collections.sort(arrCaste, new Comparator<beanCaste>() {
                                @Override
                                public int compare(beanCaste lhs, beanCaste rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            casteAdapter = new CasteMultiSelectionAdapter(SignUpStep3Activity.this, arrCaste, SlidingDrawer, Slidingpage, btnMenuClose, edtCaste, btnConfirm);
                            rvCaste.setAdapter(casteAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strReligion);
    }


    private void getCountrysRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                //  String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "country.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                /*Log.e("countrynameid",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--Country --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    arrCountry = new ArrayList<beanCountries>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID = resItem.getString("country_id");
                            String CoName = resItem.getString("country_name");

                            arrCountry.add(new beanCountries(CoID, CoName, false));

                        }

                        if (arrCountry.size() > 0) {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
                            countryAdapter = new CountryMultiSelectionAdapter(SignUpStep3Activity.this, arrCountry, SlidingDrawer, Slidingpage, btnMenuClose, edtCountry, btnConfirm);
                            rvCountry.setAdapter(countryAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getStateRequest(final String CoId) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "state_multiple.php";//?country_id="+paramUsername;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                Log.e("stateparamUsername", paramUsername);

                BasicNameValuePair UsernamePAir = new BasicNameValuePair("country_id", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--State --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrState = new ArrayList<beanState>();


                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String SID = resItem.getString("state_id");
                            String SName = resItem.getString("state_name");

                            arrState.add(new beanState(SID, SName, false));

                        }

                        if (arrState.size() > 0) {
                            Collections.sort(arrState, new Comparator<beanState>() {
                                @Override
                                public int compare(beanState lhs, beanState rhs) {
                                    return lhs.getState_name().compareTo(rhs.getState_name());
                                }
                            });
                            stateAdapter = new StateMultiSelectionAdapter(SignUpStep3Activity.this, arrState, SlidingDrawer, Slidingpage, btnMenuClose, edtState, btnConfirm);
                            rvState.setAdapter(stateAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
//                    getCityRequest(AppConstants.CountryId,AppConstants.StateId);
//                    getCity();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId);
    }


    private void getCityRequest(String CoId, String SaId) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider4.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramCountry = params[0];
                String paramState = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "city_multiple.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                Log.e("country", paramCountry);
                Log.e("State", paramState);

                BasicNameValuePair CountryPAir = new BasicNameValuePair("country_id", paramCountry);
                BasicNameValuePair CityPAir = new BasicNameValuePair("state_id", paramState);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(CountryPAir);
                nameValuePairList.add(CityPAir);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--City --", "==" + Ressponce);
                progressBarSlider4.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrCity = new ArrayList<beanCity>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CID = resItem.getString("city_id");
                            String CName = resItem.getString("city_name");

                            arrCity.add(new beanCity(CID, CName, false));

                        }

                        if (arrCity.size() > 0) {
                            Collections.sort(arrCity, new Comparator<beanCity>() {
                                @Override
                                public int compare(beanCity lhs, beanCity rhs) {
                                    return lhs.getCity_name().compareTo(rhs.getCity_name());
                                }
                            });
                            cityAdapter = new CityMultiSelectionAdapter(SignUpStep3Activity.this, arrCity, SlidingDrawer, Slidingpage, btnMenuClose, edtCity, btnConfirm);
                            rvCity.setAdapter(cityAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId, SaId);
    }


    private void getRegistationSteps3(String strAgeM, String strAgeF, String strHeightM, String strHeightF, String strMaritalStatus,
                                      String strPhysicalStatus, String strEatingHabits, String strSmokingHabits, String strDrinkingHabits,
                                      String strReligionId, String strCasteId, String strManglik,
                                      String strStar, String strMotherTongueId, String strCountryId, String strStateId, String strCityId,
                                      String strEducationId, String strOccupationID, String strAnnualIncome, String strPartnerPreference, String strUserId, String doseType) {
        progresDialog = new ProgressDialog(SignUpStep3Activity.this);
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
                String paramReligionId = params[9];
                String paramCasteId = params[10];
                String paramManglik = params[11];
                String paramStar = params[12];
                String paramMotherTongueId = params[13];
                String paramCountryId = params[14];
                String paramStateId = params[15];
                String paramCityId = params[16];
                String paramEducationId = params[17];
                String paramOccupationID = params[18];
                String paramAnnualIncome = params[19];
                String paramPartnerPreference = params[20];
                String paramUserId = params[21];
                String DoseType = params[22];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "signup_step3.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair PairAgeM = new BasicNameValuePair("age_male", paramAgeM);
                BasicNameValuePair PairAgeF = new BasicNameValuePair("age_female", paramAgeF);
                BasicNameValuePair PairHeightM = new BasicNameValuePair("height_male", paramHeightM);
                BasicNameValuePair PairHeightF = new BasicNameValuePair("height_female", paramHeightF);
                BasicNameValuePair PairMaritalStatus = new BasicNameValuePair("maritat_status", paramMaritalStatus);
                BasicNameValuePair PairPhysicalStatus = new BasicNameValuePair("physical_status", paramPhysicalStatus);
                BasicNameValuePair PairEatingHabits = new BasicNameValuePair("diet", paramEatingHabits);
                BasicNameValuePair PairSmokingHabits = new BasicNameValuePair("smoking", paramSmokingHabits);
                BasicNameValuePair PairDrinkingHabits = new BasicNameValuePair("drinking", paramDrinkingHabits);
                BasicNameValuePair PairReligionId = new BasicNameValuePair("religion_id", paramReligionId);
                BasicNameValuePair PairCasteId = new BasicNameValuePair("cast_id", paramCasteId);
                BasicNameValuePair PairManglik = new BasicNameValuePair("dosh", paramManglik);
                BasicNameValuePair PairStar = new BasicNameValuePair("star", paramStar);
                BasicNameValuePair PairMotherTongueId = new BasicNameValuePair("mother_tongue_id", paramMotherTongueId);
                BasicNameValuePair PairCountryId = new BasicNameValuePair("country_id", paramCountryId);
                BasicNameValuePair PairStateId = new BasicNameValuePair("state_id", paramStateId);
                BasicNameValuePair PairCityId = new BasicNameValuePair("city_id", paramCityId);
                BasicNameValuePair PairEducationId = new BasicNameValuePair("education_id", paramEducationId);
                BasicNameValuePair PairOccupationID = new BasicNameValuePair("occupation_id", paramOccupationID);
                BasicNameValuePair PairAnnualIncome = new BasicNameValuePair("annual_income", paramAnnualIncome);
                BasicNameValuePair PairPartnerPreference = new BasicNameValuePair("partner_preference", paramPartnerPreference);
                BasicNameValuePair PairUserId = new BasicNameValuePair("user_id", paramUserId);
                BasicNameValuePair DoshTypePair = new BasicNameValuePair("manglik", DoseType);


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
                nameValuePairList.add(PairReligionId);
                nameValuePairList.add(PairCasteId);
                nameValuePairList.add(PairManglik);
                nameValuePairList.add(PairStar);
                nameValuePairList.add(PairMotherTongueId);
                nameValuePairList.add(PairCountryId);
                nameValuePairList.add(PairStateId);
                nameValuePairList.add(PairCityId);
                nameValuePairList.add(PairEducationId);
                nameValuePairList.add(PairOccupationID);
                nameValuePairList.add(PairAnnualIncome);
                nameValuePairList.add(PairPartnerPreference);
                nameValuePairList.add(PairUserId);
                nameValuePairList.add(DoshTypePair);


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
                    Log.e("JSONRESPONSE", responseObj + "");

                    //JSONObject responseData = responseObj.getJSONObject("responseData");
                    // {"user_id":"10","message":"Registration Detail Successfully Update","status":"1"}
                    String status = responseObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        SharedPreferences.Editor editor = prefUpdate.edit();
                        editor.putString("signup_step", "4");
                        editor.commit();

                        // String meassage=responseObj.getString("message");
                        // Toast.makeText(SignUpStep3Activity.this,""+meassage,Toast.LENGTH_LONG).show();

                        Intent intLogin = new Intent(SignUpStep3Activity.this, SignUpStep4Activity.class);
                        startActivity(intLogin);
                        finish();
                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep3Activity.this);
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

        sendPostReqAsyncTask.execute(strAgeM, strAgeF, strHeightM, strHeightF, strMaritalStatus, strPhysicalStatus, strEatingHabits,
                strSmokingHabits, strDrinkingHabits, strReligionId, strCasteId, strManglik, strStar, strMotherTongueId, strCountryId,
                strStateId, strCityId, strEducationId, strOccupationID, strAnnualIncome, strPartnerPreference, strUserId, doseType);

    }


    class DoshAdapter extends RecyclerView.Adapter<DoshAdapter.ViewHolder> {

        Context context;
        ArrayList<DoshType> listDoshType;
        LinearLayout Slidingpage;
        RelativeLayout SlidingDrawer;
        ImageView btnMenuClose;
        EditText edtGeneral;
        Button btnConfirm;


        public DoshAdapter(Context context, ArrayList<DoshType> fields_list, RelativeLayout SlidingDrawer, LinearLayout Slidingpage,
                           ImageView btnMenuClose, EditText edtGeneral, Button btnConfirm) {
            this.context = context;
            this.listDoshType = fields_list;
            this.SlidingDrawer = SlidingDrawer;
            this.Slidingpage = Slidingpage;
            this.btnMenuClose = btnMenuClose;
            this.edtGeneral = edtGeneral;
            this.btnConfirm = btnConfirm;

            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dosh_list, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final DoshAdapter.ViewHolder holder, final int position) {

            DoshType type = listDoshType.get(position);

            holder.tv_name.setText(type.getName());
            Log.e("name", type.getName());

            holder.chkSelected.setChecked(type.isSelected);
            holder.chkSelected.setTag(listDoshType.get(position));

            holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    SignUpStep2Activity.DoshType contact = (SignUpStep2Activity.DoshType) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    Log.e("selected", cb.isChecked() + ",");
                    listDoshType.get(position).setSelected(cb.isChecked());
                    notifyDataSetChanged();

                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String Name = "";
                    String ID = "";

                    for (int i = 0; i < listDoshType.size(); i++) {
                        DoshType result_value = listDoshType.get(i);
                        if (result_value.isSelected == true) {
                            if (Name.equalsIgnoreCase("")) {
                                Name = result_value.getName().toString() + ",";

                            } else {
                                DoshType_val += result_value.getName().toString() + ",";
                                Name += result_value.getName().toString() + ", ";
                            }
                            Log.e("type", DoshType_val);
                            Log.e("Name", Name);
                        }
                        notifyDataSetChanged();
                    }

                    String a = Name.substring(0, Name.lastIndexOf(","));
                    edtGeneral.setText(a);

                    SlidingDrawer.setVisibility(View.GONE);
                    SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                    Slidingpage.setVisibility(View.GONE);
                    Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                    btnMenuClose.setVisibility(View.GONE);
                    btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

                    //Toast.makeText(context,"Selected Students: " + ReligionsName, Toast.LENGTH_LONG).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return listDoshType.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_name;
            public LinearLayout cardView;
            public CheckBox chkSelected;
            public View view_line;

            public ViewHolder(View itemView) {
                super(itemView);
                view_line = itemView.findViewById(R.id.view_line);
                view_line.setVisibility(View.GONE);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
                cardView = (LinearLayout) itemView.findViewById(R.id.cardView);
            }
        }


    }


    class DoshType {
        String name, id;
        private boolean isSelected;

        public DoshType() {
        }

        public DoshType(String name, boolean isSelected) {
            this.name = name;
            this.isSelected = isSelected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
