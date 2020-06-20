package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.AdditionalDgreeAdapter;
import Adepters.CityAdapter;
import Adepters.CityMultiSelectionAdapter;
import Adepters.EducationsAdapter;
import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter1;
import Adepters.GeneralAdapter2;
import Adepters.HeightAdapter;
import Adepters.MotherTongueAdapter;
import Adepters.OccupationAdapter;
import Adepters.StateAdapter;
import Adepters.SubCastAdapter;
import Models.HeightBean;
import Models.SubCast;
import Models.beanCity;
import Models.beanEducation;
import Models.SubCast;
import Models.beanGeneralData;
import Models.beanOccupation;
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
import utills.AppMethods;
import utills.NetworkConnection;
import utills.RecyclerTouchListener;

public class SignUpStep2Activity extends AppCompatActivity {
    ArrayList<SubCast> arrMotherTongue = new ArrayList<SubCast>();
    SubCastAdapter motherTongueAdapter = null;

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;

    EditText edtMaritalStatus, edtNoOfChildren, edtChildrenLivingStatus, edtState, edtCity, edtHeight, edtWeight, edtBodyType, edtPhysicalStatus,
            edtHighestEducation, edtDegree, edtOccupation, edtEmployedIn, edtAnnualIncome,
            edtDiet, edtSmoking, edtDrinking, edtHaveDosh, edtDoshType, edtStar,
            edtRassi, edtBirthTime, edtBirthPlace, edtStatus, edtType, edtValues, edtFatherOccupation, edtMotherOccupation,
            edtNoOfBrothers, edtNoOfMarriedBrothers, edtNoOfSisters, edtNoOfMarriedSisters, edtOtherInformatio,
            edtFirstName, edtLastName, edtMobileNo, edtEmailId,/*edtConfirmPass,*/
            edtPAssword, edtSubCaste, edtSearchMotherTongue;
    TextInputLayout /*inputDoshType,*/inputNoOfMarriedBrother, inputNoOfMarriedSisters;
    LinearLayout /*inputDoshType,*/linMotherTongue, llDoshType;
    //  ToggleButton toggleBtnOtherCaste;
    TextInputLayout textInputNoOfChiled, textInputChiledLivingStatus;
    Button btnContinue;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    LinearLayout linState, linCity, linHighestEducation, linAdditionalDegree, linOccupation, linGeneralView, linSBCaste;
    EditText edtSearchState, edtSearchCity, edtSearchHighestEducation, edtSearchAdditionalDegree, edtSearchOccupation;
    RecyclerView rvState, rvCity, rvHighestEducation, rvAdditionalDegree, rvOccupation, rvGeneralView,
            rvDosh, rvSBCaste, rvMotherTongue;

    ArrayList<DoshType> listDosh = new ArrayList<>();
    DoshAdapter doshAdapter;

    ArrayList<beanState> arrState = new ArrayList<beanState>();
    StateAdapter stateAdapter = null;

    ArrayList<beanCity> arrCity = new ArrayList<beanCity>();
    CityAdapter cityAdapter = null;

    ArrayList<beanEducation> arrEducation = new ArrayList<beanEducation>();
    EducationsAdapter educationAdapter = null;

    ArrayList<beanEducation> arrAdditionalDgree = new ArrayList<beanEducation>();
    AdditionalDgreeAdapter additionalDgreeAdapter = null;

    ArrayList<beanOccupation> arrOccupation = new ArrayList<beanOccupation>();
    OccupationAdapter occupationAdapter = null;

    ArrayList<SubCast> arrSubCast = new ArrayList<>();
    SubCastAdapter subCastAdapter = null;

    private int mYear, mMonth, mDay, mHour, mMinute;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String UserId, WillingToMarry = "No";
    String FirstName, LastName, EmailID, MobileNo;
    String DoshType_val = "";
    Button btnConfirm;
    AlertDialog.Builder dialogbuilder;
    ProgressBar progressBarSlider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step2);

        initilise();

    }

    @Override
    public void onBackPressed() {

        dialogbuilder = new AlertDialog.Builder(SignUpStep2Activity.this);
        dialogbuilder.setTitle(getResources().getString(R.string.app_name));
        dialogbuilder.setMessage(getResources().getString(R.string.go_back));
        dialogbuilder.setCancelable(false);
        dialogbuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               /* SharedPreferences.Editor editor=prefUpdate.edit();
                editor.putString("signup_step","0");
                editor.commit();*/
                Intent intLogin = new Intent(SignUpStep2Activity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
                dialog.dismiss();
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
        //super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppConstants.CountryId = "";
        AppConstants.CountryName = "";
        AppConstants.StateId = "";
        AppConstants.StateName = "";
        AppConstants.CityId = "";
        AppConstants.CityName = "";
        AppConstants.EducationId = "";
        AppConstants.EducationName = "";
        AppConstants.AditionalEducationId = "";
        AppConstants.AditionalEducationName = "";
        AppConstants.OccupationID = "";
        AppConstants.OccupationName = "";
        AppConstants.PASSWORD = "";

        ArrayList<beanState> arrState = null;
        StateAdapter stateAdapter = null;

        ArrayList<beanCity> arrCity = null;
        CityAdapter cityAdapter = null;

        ArrayList<beanEducation> arrEducation = null;
        EducationsAdapter educationAdapter = null;

        ArrayList<beanEducation> arrAdditionalDgree = null;
        AdditionalDgreeAdapter additionalDgreeAdapter = null;

        ArrayList<beanOccupation> arrOccupation = null;
        OccupationAdapter occupationAdapter = null;

        ArrayList<SubCast> arrMotherTongue = null;
        MotherTongueAdapter motherTongueAdapter = null;
    }

    public void initilise() {
        //______pref_______________//

//        tvNoDataFound = findViewById(R.id.tvNoDataFound2);
        progressBarSlider2 = findViewById(R.id.progressBarSlider2);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(SignUpStep2Activity.this);
        UserId = prefUpdate.getString("user_id_r", "");
        AppConstants.CountryId = prefUpdate.getString("CountryId", "");
        AppConstants.CountryName = prefUpdate.getString("CountryName", "");
        FirstName = prefUpdate.getString(AppConstants.FIRST_NAME, "");
        LastName = prefUpdate.getString(AppConstants.LAST_NAME, "");
        EmailID = prefUpdate.getString(AppConstants.EMAIL_ID, "");
        MobileNo = prefUpdate.getString(AppConstants.MOBILE_NO, "");

        Log.e("preferance fn", FirstName);
        Log.e("preferance ln", LastName);
        Log.e("preferance email", EmailID);
        Log.e("preferance mobile", MobileNo);


        //____init___________________//
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        rvSBCaste = findViewById(R.id.rvSBCaste);
        llDoshType = findViewById(R.id.llDoshType);
        linSBCaste = findViewById(R.id.linSBCaste);
        rvMotherTongue = (RecyclerView) findViewById(R.id.rvMotherTongue);
        rvMotherTongue.setLayoutManager(new LinearLayoutManager(this));
        rvMotherTongue.setHasFixedSize(true);
        linMotherTongue = (LinearLayout) findViewById(R.id.linMotherTongue);
        edtSearchMotherTongue = findViewById(R.id.edtSearchMotherTongue);
        rvDosh = findViewById(R.id.rvDoshType);
        edtSubCaste = findViewById(R.id.edtSubCaste);
        edtMaritalStatus = (EditText) findViewById(R.id.edtMaritalStatus);
        edtState = (EditText) findViewById(R.id.edtState);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtNoOfChildren = (EditText) findViewById(R.id.edtNoOfChildren);
        edtChildrenLivingStatus = (EditText) findViewById(R.id.edtChildrenLivingStatus);
        edtHeight = (EditText) findViewById(R.id.edtHeight);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        edtBodyType = (EditText) findViewById(R.id.edtBodyType);
        edtPhysicalStatus = (EditText) findViewById(R.id.edtPhysicalStatus);
        edtHighestEducation = (EditText) findViewById(R.id.edtHighestEducation);
        edtDegree = (EditText) findViewById(R.id.edtDegree);
        edtOccupation = (EditText) findViewById(R.id.edtOccupation);
        edtEmployedIn = (EditText) findViewById(R.id.edtEmployedIn);
        edtAnnualIncome = (EditText) findViewById(R.id.edtAnnualIncome);
        edtDiet = (EditText) findViewById(R.id.edtDiet);
        edtSmoking = (EditText) findViewById(R.id.edtSmoking);
        edtDrinking = (EditText) findViewById(R.id.edtDrinking);
        edtHaveDosh = (EditText) findViewById(R.id.edtHaveDosh);
        edtDoshType = (EditText) findViewById(R.id.edtDoshType);
        edtStar = (EditText) findViewById(R.id.edtStar);
        edtRassi = (EditText) findViewById(R.id.edtRassi);
        edtBirthTime = (EditText) findViewById(R.id.edtBirthTime);// edit
        edtBirthPlace = (EditText) findViewById(R.id.edtBirthPlace);// edit
        edtStatus = (EditText) findViewById(R.id.edtStatus);
        edtType = (EditText) findViewById(R.id.edtType);
        edtValues = (EditText) findViewById(R.id.edtValues);
        edtFatherOccupation = (EditText) findViewById(R.id.edtFatherOccupation);// edit
        edtMotherOccupation = (EditText) findViewById(R.id.edtMotherOccupation);// edit
        edtNoOfBrothers = (EditText) findViewById(R.id.edtNoOfBrothers);
        edtNoOfMarriedBrothers = (EditText) findViewById(R.id.edtNoOfMarriedBrothers);
        edtNoOfSisters = (EditText) findViewById(R.id.edtNoOfSisters);
        edtNoOfMarriedSisters = (EditText) findViewById(R.id.edtNoOfMarriedSisters);
        edtOtherInformatio = (EditText) findViewById(R.id.edtOtherInformatio);// edit
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtMobileNo = findViewById(R.id.edtMobileNo);
        edtEmailId = findViewById(R.id.edtEmailId);
        edtPAssword = findViewById(R.id.edtPassword);
        // edtConfirmPass=findViewById(R.id.edtConfirmPassword);
        // inputDoshType=(TextInputLayout)findViewById(R.id.inputDoshType);// edit
        inputNoOfMarriedBrother = (TextInputLayout) findViewById(R.id.inputNoOfMarriedBrother);// edit
        inputNoOfMarriedSisters = (TextInputLayout) findViewById(R.id.inputNoOfMarriedSisters);// edit
        // toggleBtnOtherCaste=(ToggleButton)findViewById(R.id.toggleBtnOtherCaste);
        textInputNoOfChiled = (TextInputLayout) findViewById(R.id.textInputNoOfChiled);
        textInputChiledLivingStatus = (TextInputLayout) findViewById(R.id.textInputChiledLivingStatus);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        linState = (LinearLayout) findViewById(R.id.linState);
        linCity = (LinearLayout) findViewById(R.id.linCity);
        linHighestEducation = (LinearLayout) findViewById(R.id.linHighestEducation);
        linAdditionalDegree = (LinearLayout) findViewById(R.id.linAdditionalDegree);
        linOccupation = (LinearLayout) findViewById(R.id.linOccupation);

        edtSearchState = (EditText) findViewById(R.id.edtSearchState);
        edtSearchCity = (EditText) findViewById(R.id.edtSearchCity);
        edtSearchHighestEducation = (EditText) findViewById(R.id.edtSearchHighestEducation);
        edtSearchAdditionalDegree = (EditText) findViewById(R.id.edtSearchAdditionalDegree);
        edtSearchOccupation = (EditText) findViewById(R.id.edtSearchOccupation);
        rvState = (RecyclerView) findViewById(R.id.rvState);
        rvCity = (RecyclerView) findViewById(R.id.rvCity);
        rvHighestEducation = (RecyclerView) findViewById(R.id.rvHighestEducation);
        rvAdditionalDegree = (RecyclerView) findViewById(R.id.rvAdditionalDegree);
        rvOccupation = (RecyclerView) findViewById(R.id.rvOccupation);
        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);
        textviewHeaderText.setVisibility(View.GONE);
        textviewHeaderText.setText(getResources().getString(R.string.Registration));
        btnBack.setVisibility(View.GONE);
        textviewSignUp.setVisibility(View.VISIBLE);
        textviewSignUp.setText("REGISTER NEW");
        //textviewSignUp.setBackgroundResource(R.drawable.bg_orange_rounded_corner);
        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep2Activity.this);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage("Do you really want to clear previous steps?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefUpdate.edit();
                        editor.putString("signup_step", "0");
                        editor.commit();
                        Intent intLogin = new Intent(SignUpStep2Activity.this, SignUpStep1Activity.class);
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

        //_____________setText_______________________//

        edtFirstName.setText(FirstName);
        edtLastName.setText(LastName);
        edtEmailId.setText(EmailID);
        edtMobileNo.setText(MobileNo);


        listDosh.add(new DoshType("Manglik", false));
        listDosh.add(new DoshType("Sarpa Dosh", false));
        listDosh.add(new DoshType("Kala Sarpa Dosh", false));
        listDosh.add(new DoshType("Rahu Dosh", false));
        listDosh.add(new DoshType("Kethu Dosh", false));
        listDosh.add(new DoshType("KALATHEA Dosh", false));


        getGeneralData();


        edtSubCaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                linSBCaste.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                getSubCasteRequest();
                getSubCaste();
            }
        });

        SlidingMenu();

        getStateRequest(AppConstants.CountryId);
        getStates();


        getAdditionalDgree();
        getOccupations();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intLogin = new Intent(SignUpStep2Activity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });


        edtBirthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimes();
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = edtPAssword.getText().toString().trim();
                String MaritalStatus = edtMaritalStatus.getText().toString().trim();
                String NoOfChildren = edtNoOfChildren.getText().toString().trim();
                String ChildrenLivingstatus = edtChildrenLivingStatus.getText().toString().trim();

                //edtState.getText().toString().trim();
                //edtCity.getText().toString().trim();
                String Height = edtHeight.getText().toString().trim();
                String Weight = edtWeight.getText().toString().trim();
                String BodyType = edtBodyType.getText().toString().trim();
                String PhysicalStatus = edtPhysicalStatus.getText().toString().trim();
                //edtHighestEducation.getText().toString().trim();
                //edtDegree.getText().toString().trim();
                //edtOccupation.getText().toString().trim();
                String EmployedIn = edtEmployedIn.getText().toString().trim();
                String AnnualIncome = edtAnnualIncome.getText().toString().trim();
                String Diet = edtDiet.getText().toString().trim();
                String Smoking = edtSmoking.getText().toString().trim();
                String Drinking = edtDrinking.getText().toString().trim();
                String HaveDosh = edtHaveDosh.getText().toString().trim();
                String DoshType = edtDoshType.getText().toString().trim();
                String Star = edtStar.getText().toString().trim();
                String Rassi = edtRassi.getText().toString().trim();
                String BirthTime = edtBirthTime.getText().toString().trim();
                String BirthPlace = edtBirthPlace.getText().toString().trim();
                String Status = edtStatus.getText().toString().trim();
                String Type = edtType.getText().toString().trim();
                String Values = edtValues.getText().toString().trim();
                String FatherOccupation = edtFatherOccupation.getText().toString().trim();
                String MotherOccupation = edtMotherOccupation.getText().toString().trim();
                String NoOfBrothers = edtNoOfBrothers.getText().toString().trim();
                String NoOfMarriedBrothers = edtNoOfMarriedBrothers.getText().toString().trim();
                String NoOfSisters = edtNoOfSisters.getText().toString().trim();
                String NoOfMarriedSisters = edtNoOfMarriedSisters.getText().toString().trim();
                String OtherInformatio = edtOtherInformatio.getText().toString().trim();

                // String confirmPassword = edtConfirmPass.getText().toString().trim();

                if (hasData(MaritalStatus)
                        && hasData(AppConstants.StateId) && hasData(AppConstants.CityId)
                        && hasData(Height)
                        && hasData(Weight)
                        && hasData(BodyType)
                        && hasData(PhysicalStatus)
                        && hasData(AppConstants.EducationId)
                        && hasData(AppConstants.AditionalEducationId)
                        && hasData(AppConstants.OccupationID)
                        && hasData(EmployedIn)
                        && hasData(AnnualIncome)
                        && hasData(Diet)
                        && hasData(Smoking)
                        && hasData(Drinking)
                        && hasData(HaveDosh)
                        && hasData(Star) && hasData(Rassi) && hasData(BirthTime) && hasData(BirthPlace)
                        && hasData(Status) && hasData(Type) && hasData(Values) && hasData(NoOfBrothers) &&
                        hasData(NoOfSisters) && hasData(OtherInformatio)
                        && hasData(password))
                    /*   && hasData(AppConstants.SubCasteID))*/ {
                    if (HaveDosh.equalsIgnoreCase("Yes") && DoshType.equalsIgnoreCase("")) {
                        Toast.makeText(SignUpStep2Activity.this, "Please select Dosh type.", Toast.LENGTH_LONG).show();
                    } else if (!NoOfBrothers.equalsIgnoreCase("") && !NoOfBrothers.equalsIgnoreCase("No Brothers") && !NoOfBrothers.equalsIgnoreCase("0") && NoOfMarriedBrothers.equalsIgnoreCase("")) {
                        Toast.makeText(SignUpStep2Activity.this, "Please enter married brothers", Toast.LENGTH_LONG).show();
                    } else if (!NoOfSisters.equalsIgnoreCase("") && !NoOfSisters.equalsIgnoreCase("No Sisters") && !NoOfSisters.equalsIgnoreCase("0") && NoOfMarriedSisters.equalsIgnoreCase("")) {

                        Toast.makeText(SignUpStep2Activity.this, "Please enter married sisters", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("DoshType", DoshType);
                        Log.e("DoshType_val", DoshType_val);
                        Log.e("HeightID", AppConstants.HeightID);
                        Log.e("subcasteId", AppConstants.SubCasteID);
                        getRegistationSteps2(MaritalStatus, NoOfChildren, WillingToMarry, AppConstants.StateId, AppConstants.CityId, AppConstants.HeightID, Weight,
                                BodyType, PhysicalStatus, AppConstants.EducationId, AppConstants.AditionalEducationId, AppConstants.OccupationID,
                                EmployedIn, AnnualIncome, Diet, Smoking, Drinking, HaveDosh, Star, Rassi,
                                BirthTime, BirthPlace, Status, Type, Values, FatherOccupation, MotherOccupation, NoOfBrothers, NoOfSisters,
                                OtherInformatio, ChildrenLivingstatus, UserId, DoshType, NoOfMarriedBrothers, NoOfMarriedSisters, password, AppConstants.SubCasteID);
                    }

                } else {
                    Toast.makeText(SignUpStep2Activity.this, "Please enter all required fields. ", Toast.LENGTH_LONG).show();
                }

            }
        });
        rvState.addOnItemTouchListener(new RecyclerTouchListener(SignUpStep2Activity.this, rvState, new SignUpStep1Activity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvState.getWindowToken(), 0);

                beanState arrCo = arrState.get(position);
                AppConstants.StateName = arrCo.getState_name();
                AppConstants.StateId = arrCo.getState_id();

                edtState.setText(AppConstants.StateName);

                AppConstants.CityName = "";
                AppConstants.CityId = "";
                edtCity.setText("");

                ArrayList<beanCity> arrCity = null;
                CityAdapter cityAdapter = null;
                GonelidingDrower();
                getCityRequest(AppConstants.CountryId, AppConstants.StateId);
                getCity();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvCity.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCity, new SignUpStep1Activity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvCity.getWindowToken(), 0);

                beanCity arrCo = arrCity.get(position);
                AppConstants.CityName = arrCo.getCity_name();
                AppConstants.CityId = arrCo.getCity_id();

                edtCity.setText(AppConstants.CityName);

                GonelidingDrower();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    public void getGeneralData() {

        edtDoshType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                edtDoshType.setError(null);
                linMotherTongue.setVisibility(View.GONE);
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
                    GeneralAdapter2 generalAdapter = new GeneralAdapter2(SignUpStep2Activity.this, "Dosh_Type", arrGeneralData, SlidingDrawer, Slidingpage, btnMenuClose, edtDoshType, btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                } else {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_diet_2), SlidingDrawer, Slidingpage, btnMenuClose, edtDoshType);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });





       /* edtDoshType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
              //  edtConfirmPass.setFocusable(false);
                edtDoshType.setError(null);
                llDoshType.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);
                LinearLayoutManager manager = new LinearLayoutManager(SignUpStep2Activity.this,LinearLayoutManager.VERTICAL,false);
                rvDosh.setLayoutManager(manager);
                doshAdapter = new DoshAdapter(SignUpStep2Activity.this,listDosh,SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType,btnConfirm);
                rvDosh.setAdapter(doshAdapter);

            }
        });*/


        edtMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();

                edtMaritalStatus.setFocusable(true);
                edtMaritalStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter1 generalAdapter = new GeneralAdapter1(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_marital_status), SlidingDrawer,
                        Slidingpage, btnMenuClose, edtMaritalStatus, textInputNoOfChiled, textInputChiledLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfChildren.setError(null);
                edtNoOfChildren.setFocusable(true);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_no_of_children), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfChildren);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtChildrenLivingStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtChildrenLivingStatus.setError(null);
                edtChildrenLivingStatus.setFocusable(true);

                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_Chiled_leaving_status), SlidingDrawer, Slidingpage, btnMenuClose, edtChildrenLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);

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

        edtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                if (arrState.size() > 0) {

                } else {
                    rvState.setAdapter(null);
                }
                edtState.setError(null);
                edtSearchState.setText("");
                edtState.setFocusable(true);
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

        edtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCity.setError(null);
                edtSearchCity.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });


        edtHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHeight.setError(null);
                edtHeight.setFocusable(true);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
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

                HeightAdapter adapter = new HeightAdapter(SignUpStep2Activity.this, "hieght", beanArrayList, Slidingpage, SlidingDrawer, btnMenuClose, edtHeight);
                rvGeneralView.setAdapter(adapter);


               /* GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeight);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });

        edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtWeight.setError(null);
                edtWeight.setFocusable(true);

                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_weight), SlidingDrawer, Slidingpage, btnMenuClose, edtWeight);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtBodyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtBodyType.setError(null);
                linState.setVisibility(View.GONE);
                edtBodyType.setFocusable(true);

                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_body_type), SlidingDrawer, Slidingpage, btnMenuClose, edtBodyType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_physical_status), SlidingDrawer, Slidingpage, btnMenuClose, edtPhysicalStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHighestEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHighestEducation.setError(null);
                edtSearchHighestEducation.setText("");
                linState.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.VISIBLE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });


        edtDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDegree.setError(null);
                edtSearchAdditionalDegree.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });


        edtOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");
                linState.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });


        edtEmployedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtEmployedIn.setError(null);
                linState.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_Employed_in), SlidingDrawer, Slidingpage, btnMenuClose, edtEmployedIn);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAnnualIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_annual_income), SlidingDrawer, Slidingpage, btnMenuClose, edtAnnualIncome);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDiet.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_diet_2), SlidingDrawer, Slidingpage, btnMenuClose, edtDiet);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtSmoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtSmoking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_smoking_2), SlidingDrawer, Slidingpage, btnMenuClose, edtSmoking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtDrinking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDrinking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_drinking_2), SlidingDrawer, Slidingpage, btnMenuClose, edtDrinking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHaveDosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHaveDosh.setError(null);

                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_manglik), SlidingDrawer, Slidingpage, btnMenuClose, edtHaveDosh);
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
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_star), SlidingDrawer, Slidingpage, btnMenuClose, edtStar);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtRassi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtRassi.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_raasi_moon_sign), SlidingDrawer, Slidingpage, btnMenuClose, edtRassi);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_family_status), SlidingDrawer, Slidingpage, btnMenuClose, edtStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtType.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_family_type), SlidingDrawer, Slidingpage, btnMenuClose, edtType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtValues.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_family_value), SlidingDrawer, Slidingpage, btnMenuClose, edtValues);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfBrothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfBrothers.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_brothers), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfBrothers.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                String strSelectedType = edtNoOfBrothers.getText().toString().trim();
                if (strSelectedType.equalsIgnoreCase("No Brothers")) {
                    inputNoOfMarriedBrother.setVisibility(View.GONE);
                } else {
                    inputNoOfMarriedBrother.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtNoOfMarriedBrothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfMarriedBrothers.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_married_brothers), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfMarriedBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfSisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfSisters.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_sisters), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfSisters);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfSisters.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String strSelectedType = edtNoOfSisters.getText().toString().trim();
                if (strSelectedType.equalsIgnoreCase("No Sisters")) {
                    inputNoOfMarriedSisters.setVisibility(View.GONE);
                } else {
                    inputNoOfMarriedSisters.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtNoOfMarriedSisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfMarriedSisters.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linSBCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_married_sisters), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfMarriedSisters);
                rvGeneralView.setAdapter(generalAdapter);
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
        AnimUtils.SlideAnimation(SignUpStep2Activity.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);
        edtPAssword.clearFocus();

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(SignUpStep2Activity.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
        edtPAssword.clearFocus();
    }


    public void setDateTimes() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time11 = showTime(hourOfDay, minute);
                edtBirthTime.setText(time11);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    public String showTime(int hour, int min) {
        String format = "";

        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        NumberFormat ff = new DecimalFormat("00");

        String times = String.valueOf(new StringBuilder().append(ff.format(hour)).append(":").append(ff.format(min)).append(" ").append(format));

        return times;
    }


    public void SlidingMenu() {
        rvState.setLayoutManager(new LinearLayoutManager(this));
        rvState.setHasFixedSize(true);

        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setHasFixedSize(true);

        rvHighestEducation.setLayoutManager(new LinearLayoutManager(this));
        rvHighestEducation.setHasFixedSize(true);

        rvAdditionalDegree.setLayoutManager(new LinearLayoutManager(this));
        rvAdditionalDegree.setHasFixedSize(true);

        rvOccupation.setLayoutManager(new LinearLayoutManager(this));
        rvOccupation.setHasFixedSize(true);

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        rvSBCaste.setLayoutManager(new LinearLayoutManager(this));
        rvSBCaste.setHasFixedSize(true);

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


    public void getAdditionalDgree() {
        edtSearchAdditionalDegree.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrAdditionalDgree.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchAdditionalDegree.getText().toString().toLowerCase(Locale.getDefault());
                    additionalDgreeAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    private void getStateRequest(final String CoId) {
//        progresDialog= new ProgressDialog(SignUpStep2Activity.this);
//        progresDialog.setCancelable(false);
//        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
//        progresDialog.setIndeterminate(true);
//        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider2.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "state.php";//?country_id="+paramUsername;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
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
                //progresDialog.dismiss();
                Log.e("--State --", "==" + Ressponce);
                progressBarSlider2.setVisibility(View.GONE);
                try {
                    arrState = new ArrayList<beanState>();
                    JSONObject responseObj = new JSONObject(Ressponce);

                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String SID = resItem.getString("state_id");
                            String SName = resItem.getString("state_name");
                            arrState.add(new beanState(SID, SName));
                        }

                        if (arrState.size() > 0) {
                            Collections.sort(arrState, new Comparator<beanState>() {
                                @Override
                                public int compare(beanState lhs, beanState rhs) {
                                    return lhs.getState_name().compareTo(rhs.getState_name());
                                }
                            });
                            stateAdapter = new StateAdapter(SignUpStep2Activity.this, arrState, SlidingDrawer, Slidingpage, btnMenuClose, edtState);
                            rvState.setAdapter(stateAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    getCityRequest(AppConstants.CountryId, AppConstants.StateId);
                    getCity();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId);
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


    private void getCityRequest(String CoId, String SaId) {
        /*progresDialog= new ProgressDialog(SignUpStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider2.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramCountry = params[0];
                String paramState = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "city.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
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
                progressBarSlider2.setVisibility(View.GONE);
                try {
                    arrCity = new ArrayList<beanCity>();
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CID = resItem.getString("city_id");
                            String CName = resItem.getString("city_name");

                            arrCity.add(new beanCity(CID, CName));

                        }

                        if (arrCity.size() > 0) {
                            Collections.sort(arrCity, new Comparator<beanCity>() {
                                @Override
                                public int compare(beanCity lhs, beanCity rhs) {
                                    return lhs.getCity_name().compareTo(rhs.getCity_name());
                                }
                            });
                            cityAdapter = new CityAdapter(SignUpStep2Activity.this, arrCity, SlidingDrawer, Slidingpage, btnMenuClose, edtCity);
                            rvCity.setAdapter(cityAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;


                } catch (Exception e) {

                } finally {
                    // progresDialog.dismiss();
                    getHighestEducationRequest();
                    getHighestEducation();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId, SaId);
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


    private void getHighestEducationRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider2.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "education.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--education --", "==" + Ressponce);
                progressBarSlider2.setVisibility(View.GONE);
                try {
                    arrEducation = new ArrayList<beanEducation>();
                    arrAdditionalDgree = new ArrayList<beanEducation>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id = resItem.getString("edu_id");
                            String edu_name = resItem.getString("edu_name");

                            arrEducation.add(new beanEducation(edu_id, edu_name));
                            arrAdditionalDgree.add(new beanEducation(edu_id, edu_name));

                        }

                        if (arrEducation.size() > 0) {
                            Collections.sort(arrEducation, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });
                            educationAdapter = new EducationsAdapter(SignUpStep2Activity.this, arrEducation, SlidingDrawer, Slidingpage, btnMenuClose, edtHighestEducation);
                            rvHighestEducation.setAdapter(educationAdapter);

                        }

                        if (arrEducation.size() > 0) {
                            Collections.sort(arrAdditionalDgree, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });

                            additionalDgreeAdapter = new AdditionalDgreeAdapter(SignUpStep2Activity.this, arrAdditionalDgree, SlidingDrawer, Slidingpage, btnMenuClose, edtDegree);
                            rvAdditionalDegree.setAdapter(additionalDgreeAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    getOccupationsRequest();
                    getOccupations();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
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


    private void getOccupationsRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider2.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "occupation.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--occupation --", "==" + Ressponce);
                progressBarSlider2.setVisibility(View.GONE);
                try {
                    arrOccupation = new ArrayList<beanOccupation>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id = resItem.getString("ocp_id");
                            String edu_name = resItem.getString("ocp_name");

                            arrOccupation.add(new beanOccupation(edu_id, edu_name));

                        }

                        if (arrOccupation.size() > 0) {
                            Collections.sort(arrOccupation, new Comparator<beanOccupation>() {
                                @Override
                                public int compare(beanOccupation lhs, beanOccupation rhs) {
                                    return lhs.getOccupation_name().compareTo(rhs.getOccupation_name());
                                }
                            });
                            occupationAdapter = new OccupationAdapter(SignUpStep2Activity.this, arrOccupation, SlidingDrawer, Slidingpage, btnMenuClose, edtOccupation);
                            rvOccupation.setAdapter(occupationAdapter);

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


    private void getSubCasteRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider2.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "sub-caste.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                try {

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
                Log.e("--subcaste --", "==" + Ressponce);
                progressBarSlider2.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrMotherTongue = new ArrayList<SubCast>();


                    Log.e("subcastarrayup", arrMotherTongue.size() + "");
                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id = resItem.getString("sub_caste_id");
                            String mtongue_name = resItem.getString("sub_caste_name");

                            arrMotherTongue.add(new SubCast(mtongue_id, mtongue_name));
                            Log.e("subcastarraystore", arrMotherTongue.size() + "");
                        }

                        if (arrMotherTongue.size() > 0) {
                            Collections.sort(arrMotherTongue, new Comparator<SubCast>() {
                                @Override
                                public int compare(SubCast lhs, SubCast rhs) {
                                    return lhs.getSB_name().compareTo(rhs.getSB_name());
                                }
                            });
                            Log.e("subcastarraydown", arrMotherTongue.size() + "");
                            motherTongueAdapter = new SubCastAdapter(SignUpStep2Activity.this, arrMotherTongue, SlidingDrawer, Slidingpage, edtSubCaste);
                            rvSBCaste.setAdapter(motherTongueAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getSubCaste() {
        try {
            edtSubCaste.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrMotherTongue.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSubCaste.getText().toString().toLowerCase(Locale.getDefault());
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


    private void getRegistationSteps2(String strMaritalStatus, String strNoOfChildren, String strWillingToMarry, String strStateId, String strCityId,
                                      String strHeight, String strWeight, String strBodyType, String strPhysicalStatus, String strEducationId,
                                      String strAditionalEducationId, String strOccupationID, String strEmployedIn, String strAnnualIncome,
                                      String strDiet, String strSmoking, String strDrinking, String strHaveDosh, String strStar, String strRassi,
                                      String strBirthTime, String strBirthPlace, String strStatus, String strType, String strValues, String strFatherOccupation,
                                      String strMotherOccupation, String strNoOfBrothers, String strNoOfSisters, String strOtherInformatio,
                                      String strChildrenLivingstatus, String strUserId, String doseType, String marridBrothers, String marridSister, final String password, String SubCasteId) {
        progresDialog = new ProgressDialog(SignUpStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String PMaritalStatus = params[0];
                String PNoOfChildren = params[1];
                String PWillingToMarry = params[2];
                String PStateId = params[3];
                String PCityId = params[4];
                String PHeight = params[5];
                String PWeight = params[6];
                String PBodyType = params[7];
                String PPhysicalStatus = params[8];
                String PEducationId = params[9];
                String PAditionalEducationId = params[10];
                String POccupationID = params[11];
                String PEmployedIn = params[12];
                String PAnnualIncome = params[13];
                String PDiet = params[14];
                String PSmoking = params[15];
                String PDrinking = params[16];
                String PHaveDosh = params[17];
                String PStar = params[18];
                String PRassi = params[19];
                String PBirthTime = params[20];
                String PBirthPlace = params[21];
                String PStatus = params[22];
                String PType = params[23];
                String PValues = params[24];
                String PFatherOccupation = params[25];
                String PMotherOccupation = params[26];
                String PNoOfBrothers = params[27];
                String PNoOfSisters = params[28];
                String POtherInformatio = params[29];
                String PChildrenLivingstatus = params[30];
                String PUserId = params[31];
                String DoseType = params[32];
                String MarridBrothers = params[33];
                String MarridSister = params[34];
                String Password = params[35];
                String Subcasteid = params[36];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "signup_step2.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair MaritalStatusPair = new BasicNameValuePair("maritat_status", PMaritalStatus);
                BasicNameValuePair NoOfChildrenPair = new BasicNameValuePair("no_of_children", PNoOfChildren);
                BasicNameValuePair WillingToMarryPair = new BasicNameValuePair("marry_into_other_cast", PWillingToMarry);
                BasicNameValuePair StateIdPair = new BasicNameValuePair("state_id", PStateId);
                BasicNameValuePair CityIdPair = new BasicNameValuePair("city_id", PCityId);
                BasicNameValuePair HeightPair = new BasicNameValuePair("height", PHeight);
                BasicNameValuePair WeightPair = new BasicNameValuePair("weight", PWeight);
                BasicNameValuePair BodyTypePair = new BasicNameValuePair("body_type", PBodyType);
                BasicNameValuePair PhysicalStatusPair = new BasicNameValuePair("physical_status", PPhysicalStatus);
                BasicNameValuePair EducationIdPair = new BasicNameValuePair("education_id", PEducationId);
                BasicNameValuePair AditionalEducationIdPair = new BasicNameValuePair("additional_dgree_id", PAditionalEducationId);
                BasicNameValuePair OccupationIDPair = new BasicNameValuePair("occupation_id", POccupationID);
                BasicNameValuePair EmployedInPair = new BasicNameValuePair("employed_in", PEmployedIn);
                BasicNameValuePair AnnualIncomePair = new BasicNameValuePair("annual_income", PAnnualIncome);
                BasicNameValuePair DietPair = new BasicNameValuePair("diet", PDiet);
                BasicNameValuePair SmokingPair = new BasicNameValuePair("smoking", PSmoking);
                BasicNameValuePair DrinkingPair = new BasicNameValuePair("drinking", PDrinking);
                BasicNameValuePair HaveDoshPair = new BasicNameValuePair("dosh", PHaveDosh);
                BasicNameValuePair StarPair = new BasicNameValuePair("star", PStar);
                BasicNameValuePair RassiPair = new BasicNameValuePair("raasi", PRassi);
                BasicNameValuePair BirthTimePair = new BasicNameValuePair("birth_time", PBirthTime);
                BasicNameValuePair BirthPlacePair = new BasicNameValuePair("birth_place", PBirthPlace);
                BasicNameValuePair StatusPair = new BasicNameValuePair("family_status", PStatus);
                BasicNameValuePair TypePair = new BasicNameValuePair("family_type", PType);
                BasicNameValuePair ValuesPair = new BasicNameValuePair("family_values", PValues);
                BasicNameValuePair FatherOccupationPair = new BasicNameValuePair("father_occupation", PFatherOccupation);
                BasicNameValuePair MotherOccupationPair = new BasicNameValuePair("mother_occupation", PMotherOccupation);
                BasicNameValuePair NoOfBrothersPair = new BasicNameValuePair("no_of_brothers", PNoOfBrothers);
                BasicNameValuePair NoOfSistersPair = new BasicNameValuePair("no_of_sisters", PNoOfSisters);
                BasicNameValuePair OtherInformatioPair = new BasicNameValuePair("something_about_me", POtherInformatio);
                BasicNameValuePair ChildrenLivingstatusPair = new BasicNameValuePair("children_living_status", PChildrenLivingstatus);
                BasicNameValuePair UserIdPair = new BasicNameValuePair("user_id", PUserId);
                BasicNameValuePair DoshTypePair = new BasicNameValuePair("manglik", DoseType);
                BasicNameValuePair MarridBrothersPair = new BasicNameValuePair("no_marri_brother", MarridBrothers);
                BasicNameValuePair MarridSisterPair = new BasicNameValuePair("no_marri_sister", MarridSister);
                BasicNameValuePair PasswordPair = new BasicNameValuePair(AppConstants.PASSWORD, Password);
                BasicNameValuePair SubCasteid = new BasicNameValuePair("sub_caste_id", Subcasteid);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(MaritalStatusPair);
                nameValuePairList.add(NoOfChildrenPair);
                nameValuePairList.add(WillingToMarryPair);
                nameValuePairList.add(StateIdPair);
                nameValuePairList.add(CityIdPair);
                nameValuePairList.add(HeightPair);
                nameValuePairList.add(WeightPair);
                nameValuePairList.add(BodyTypePair);
                nameValuePairList.add(PhysicalStatusPair);
                nameValuePairList.add(EducationIdPair);
                nameValuePairList.add(AditionalEducationIdPair);
                nameValuePairList.add(OccupationIDPair);
                nameValuePairList.add(EmployedInPair);
                nameValuePairList.add(AnnualIncomePair);
                nameValuePairList.add(DietPair);
                nameValuePairList.add(SmokingPair);
                nameValuePairList.add(DrinkingPair);
                nameValuePairList.add(HaveDoshPair);
                nameValuePairList.add(StarPair);
                nameValuePairList.add(RassiPair);
                nameValuePairList.add(BirthTimePair);
                nameValuePairList.add(BirthPlacePair);
                nameValuePairList.add(StatusPair);
                nameValuePairList.add(TypePair);
                nameValuePairList.add(ValuesPair);
                nameValuePairList.add(FatherOccupationPair);
                nameValuePairList.add(MotherOccupationPair);
                nameValuePairList.add(NoOfBrothersPair);
                nameValuePairList.add(NoOfSistersPair);
                nameValuePairList.add(OtherInformatioPair);
                nameValuePairList.add(ChildrenLivingstatusPair);
                nameValuePairList.add(UserIdPair);
                nameValuePairList.add(DoshTypePair);
                nameValuePairList.add(MarridBrothersPair);
                nameValuePairList.add(MarridSisterPair);
                nameValuePairList.add(PasswordPair);
                nameValuePairList.add(SubCasteid);

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
                        SharedPreferences.Editor editor = prefUpdate.edit();
                        editor.putString("signup_step", "3");
                        editor.commit();

                        String meassage = responseObj.getString("message");
                        //Toast.makeText(SignUpStep2Activity.this,""+meassage,Toast.LENGTH_LONG).show();

                        Intent intLogin = new Intent(SignUpStep2Activity.this, SignUpStep3Activity.class);
                        startActivity(intLogin);
                        finish();
                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep2Activity.this);
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

        sendPostReqAsyncTask.execute(strMaritalStatus, strNoOfChildren, strWillingToMarry, strStateId, strCityId, strHeight, strWeight,
                strBodyType, strPhysicalStatus, strEducationId, strAditionalEducationId, strOccupationID,
                strEmployedIn, strAnnualIncome, strDiet, strSmoking, strDrinking, strHaveDosh, strStar, strRassi,
                strBirthTime, strBirthPlace, strStatus, strType, strValues, strFatherOccupation, strMotherOccupation, strNoOfBrothers, strNoOfSisters,
                strOtherInformatio, strChildrenLivingstatus, strUserId, doseType, marridBrothers, marridSister, password, SubCasteId);

    }


    class DoshAdapter extends RecyclerView.Adapter<DoshAdapter.ViewHolder> {

        Context context;
        ArrayList<DoshType> listDoshType;
        private ArrayList<DoshType> arrFilter;
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
            // this.arrFilter = new ArrayList<DoshType>();
            //   this.arrFilter.addAll(listDoshType);
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public DoshAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dosh_list, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final DoshAdapter.ViewHolder holder, final int position) {

            DoshType type = listDoshType.get(position);

            holder.tv_name.setText(type.getName());

            holder.chkSelected.setChecked(type.isSelected);
            holder.chkSelected.setTag(listDoshType.get(position));

            holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    DoshType contact = (DoshType) cb.getTag();

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
