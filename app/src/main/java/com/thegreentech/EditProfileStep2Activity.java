package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import Adepters.EducationsAdapter;
import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter1;
import Adepters.OccupationAdapter;
import Adepters.StateAdapter;
import Models.beanCity;
import Models.beanEducation;
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
import utills.AppConstants;
import utills.RecyclerTouchListener;

public class EditProfileStep2Activity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;


    EditText edtMaritalStatus,edtNoOfChildren,edtChildrenLivingStatus,edtState,edtCity,edtHeight,edtWeight,edtBodyType,edtPhysicalStatus,
            edtHighestEducation,edtDegree,edtOccupation,edtEmployedIn,edtAnnualIncome,
            edtDiet,edtSmoking,edtDrinking,edtHaveDosh/*,edtDoshType*/,edtStar,
            edtRassi,edtBirthTime,edtBirthPlace,edtStatus,edtType,edtValues,edtFatherOccupation,edtMotherOccupation,
            edtNoOfBrothers,edtNoOfMarriedBrothers,edtNoOfSisters,edtNoOfMarriedSisters,edtOtherInformatio;
    TextInputLayout /*inputDoshType,*/inputNoOfMarriedBrother,inputNoOfMarriedSisters;
    //ToggleButton toggleBtnOtherCaste;
    TextInputLayout textInputNoOfChiled,textInputChiledLivingStatus;
    Button btnContinue;


    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    LinearLayout linState,linCity,linHighestEducation,linAdditionalDegree,linOccupation,linGeneralView;
    EditText edtSearchState,edtSearchCity,edtSearchHighestEducation,edtSearchAdditionalDegree,edtSearchOccupation;
    RecyclerView rvState,rvCity,rvHighestEducation,rvAdditionalDegree,rvOccupation,rvGeneralView;

    ArrayList<beanState> arrState=new ArrayList<beanState>();
    StateAdapter stateAdapter=null;

    ArrayList<beanCity> arrCity=new ArrayList<beanCity>();
    CityAdapter cityAdapter=null;

    ArrayList<beanEducation> arrEducation=new ArrayList<beanEducation>();
    EducationsAdapter educationAdapter=null;

    ArrayList<beanEducation> arrAdditionalDgree=new ArrayList<beanEducation>();
    AdditionalDgreeAdapter additionalDgreeAdapter=null;

    ArrayList<beanOccupation> arrOccupation=new ArrayList<beanOccupation>();
    OccupationAdapter occupationAdapter=null;

    private int mYear, mMonth, mDay, mHour, mMinute;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id,WillingToMarry="No";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step2);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(EditProfileStep2Activity.this);

        matri_id=prefUpdate.getString("matri_id","");
//        AppConstants.CountryId= prefUpdate.getString("CountryId","");
//        AppConstants.CountryName= prefUpdate.getString("CountryName","");


        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Profile");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtMaritalStatus=(EditText)findViewById(R.id.edtMaritalStatus);
        edtState=(EditText)findViewById(R.id.edtState);
        edtCity=(EditText)findViewById(R.id.edtCity);
        edtNoOfChildren=(EditText)findViewById(R.id.edtNoOfChildren);
        edtChildrenLivingStatus=(EditText)findViewById(R.id.edtChildrenLivingStatus);
        edtHeight=(EditText)findViewById(R.id.edtHeight);
        edtWeight=(EditText)findViewById(R.id.edtWeight);
        edtBodyType=(EditText)findViewById(R.id.edtBodyType);
        edtPhysicalStatus=(EditText)findViewById(R.id.edtPhysicalStatus);
        edtHighestEducation=(EditText)findViewById(R.id.edtHighestEducation);
        edtDegree=(EditText)findViewById(R.id.edtDegree);
        edtOccupation=(EditText)findViewById(R.id.edtOccupation);
        edtEmployedIn=(EditText)findViewById(R.id.edtEmployedIn);
        edtAnnualIncome=(EditText)findViewById(R.id.edtAnnualIncome);
        edtDiet=(EditText)findViewById(R.id.edtDiet);
        edtSmoking=(EditText)findViewById(R.id.edtSmoking);
        edtDrinking=(EditText)findViewById(R.id.edtDrinking);
        edtHaveDosh=(EditText)findViewById(R.id.edtHaveDosh);
       // edtDoshType=(EditText)findViewById(R.id.edtDoshType);
        edtStar=(EditText)findViewById(R.id.edtStar);
        edtRassi=(EditText)findViewById(R.id.edtRassi);
        edtBirthTime=(EditText)findViewById(R.id.edtBirthTime);// edit
        edtBirthPlace=(EditText)findViewById(R.id.edtBirthPlace);// edit
        edtStatus=(EditText)findViewById(R.id.edtStatus);
        edtType=(EditText)findViewById(R.id.edtType);
        edtValues=(EditText)findViewById(R.id.edtValues);
        edtFatherOccupation=(EditText)findViewById(R.id.edtFatherOccupation);// edit
        edtMotherOccupation=(EditText)findViewById(R.id.edtMotherOccupation);// edit
        edtNoOfBrothers=(EditText)findViewById(R.id.edtNoOfBrothers);
        edtNoOfMarriedBrothers=(EditText)findViewById(R.id.edtNoOfMarriedBrothers);
        edtNoOfSisters=(EditText)findViewById(R.id.edtNoOfSisters);
        edtNoOfMarriedSisters=(EditText)findViewById(R.id.edtNoOfMarriedSisters);
        edtOtherInformatio=(EditText)findViewById(R.id.edtOtherInformatio);// edit

      //  inputDoshType=(TextInputLayout)findViewById(R.id.inputDoshType);// edit
        inputNoOfMarriedBrother=(TextInputLayout)findViewById(R.id.inputNoOfMarriedBrother);// edit
        inputNoOfMarriedSisters=(TextInputLayout)findViewById(R.id.inputNoOfMarriedSisters);// edit

     //   toggleBtnOtherCaste=(ToggleButton)findViewById(R.id.toggleBtnOtherCaste);

        textInputNoOfChiled=(TextInputLayout) findViewById(R.id.textInputNoOfChiled);
        textInputChiledLivingStatus=(TextInputLayout)findViewById(R.id.textInputChiledLivingStatus);

        btnContinue=(Button)findViewById(R.id.btnContinue);
        btnContinue.setText("Update Profile");
        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);

/*
        toggleBtnOtherCaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggleBtnOtherCaste.isChecked())
                {
                    WillingToMarry="Yes";
                    //Toast.makeText(SignUpStep2Activity.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                }
                else {
                    WillingToMarry="No";
                    //Toast.makeText(SignUpStep2Activity.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }

            }
        });
*/


        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });


        edtBirthTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setDateTimes();
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                Intent intLogin= new Intent(SignUpStep2Activity.this,SignUpStep3Activity.class);
//                startActivity(intLogin);
//                finish();

                String MaritalStatus=edtMaritalStatus.getText().toString().trim();
                String NoOfChildren=edtNoOfChildren.getText().toString().trim();
                String ChildrenLivingstatus=edtChildrenLivingStatus.getText().toString().trim();

                //edtState.getText().toString().trim();
                //edtCity.getText().toString().trim();
                String Height=edtHeight.getText().toString().trim();
                String Weight=edtWeight.getText().toString().trim();
                String BodyType=edtBodyType.getText().toString().trim();
                String PhysicalStatus=edtPhysicalStatus.getText().toString().trim();
                //edtHighestEducation.getText().toString().trim();
                //edtDegree.getText().toString().trim();
                //edtOccupation.getText().toString().trim();
                String EmployedIn=edtEmployedIn.getText().toString().trim();
                String AnnualIncome=edtAnnualIncome.getText().toString().trim();
                String Diet=edtDiet.getText().toString().trim();
                String Smoking=edtSmoking.getText().toString().trim();
                String Drinking=edtDrinking.getText().toString().trim();
                String HaveDosh=edtHaveDosh.getText().toString().trim();
              //  String DoshType=edtDoshType.getText().toString().trim();
                String Star=edtStar.getText().toString().trim();
                String Rassi=edtRassi.getText().toString().trim();
                Log.d("ravi","get rassi"+Rassi);
                String BirthTime=edtBirthTime.getText().toString().trim();
                String BirthPlace=edtBirthPlace.getText().toString().trim();
                String Status=edtStatus.getText().toString().trim();
                String Type=edtType.getText().toString().trim();
                String Values=edtValues.getText().toString().trim();
                String FatherOccupation=edtFatherOccupation.getText().toString().trim();
                String MotherOccupation=edtMotherOccupation.getText().toString().trim();
                String NoOfBrothers=edtNoOfBrothers.getText().toString().trim();
                String NoOfMarriedBrothers=edtNoOfMarriedBrothers.getText().toString().trim();
                String NoOfSisters=edtNoOfSisters.getText().toString().trim();
                String NoOfMarriedSisters=edtNoOfMarriedSisters.getText().toString().trim();
                String OtherInformatio=edtOtherInformatio.getText().toString().trim();




                if (hasData(MaritalStatus) && hasData(AppConstants.StateId)&& hasData(AppConstants.CityId)
                        && hasData(Height) && hasData(Weight)&& hasData(BodyType)&& hasData(PhysicalStatus)
                        && hasData(AppConstants.EducationId)&& hasData(AppConstants.AditionalEducationId) && hasData(AppConstants.OccupationID)
                        && hasData(EmployedIn)&& hasData(AnnualIncome)&& hasData(Diet)&& hasData(Smoking)&& hasData(Drinking)
                        && hasData(HaveDosh)&& hasData(Star)&& hasData(Rassi) && hasData(BirthTime) && hasData(BirthPlace)
                        && hasData(Status) && hasData(Type)&& hasData(Values)&& hasData(FatherOccupation)&& hasData(MotherOccupation)
                        && hasData(NoOfBrothers)&& hasData(NoOfSisters)&& hasData(OtherInformatio))
                {
                    if(HaveDosh.equalsIgnoreCase("Yes") /*&& DoshType.equalsIgnoreCase("")*/)
                    {
                        Toast.makeText(EditProfileStep2Activity.this,"Please select Dosh type.",Toast.LENGTH_LONG).show();
                    }else if(!NoOfBrothers.equalsIgnoreCase("") && !NoOfBrothers.equalsIgnoreCase("No Brothers") && !NoOfBrothers.equalsIgnoreCase("0") && NoOfMarriedBrothers.equalsIgnoreCase(""))
                    {
                        Toast.makeText(EditProfileStep2Activity.this,"Please enter married brothers",Toast.LENGTH_LONG).show();
                    }else if(!NoOfSisters.equalsIgnoreCase("") && !NoOfSisters.equalsIgnoreCase("No Sisters") && !NoOfSisters.equalsIgnoreCase("0") && NoOfMarriedSisters.equalsIgnoreCase(""))
                    {
                        Toast.makeText(EditProfileStep2Activity.this,"Please enter married sisters",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        getUpdateProfileSteps2(MaritalStatus,NoOfChildren,WillingToMarry,AppConstants.StateId,AppConstants.CityId,Height,Weight,
                                BodyType,PhysicalStatus,AppConstants.EducationId,AppConstants.AditionalEducationId,AppConstants.OccupationID,
                                EmployedIn,AnnualIncome,Diet,Smoking,Drinking,HaveDosh,Star,Rassi,
                                BirthTime,BirthPlace,Status,Type,Values,FatherOccupation,MotherOccupation,NoOfBrothers,NoOfSisters,
                                OtherInformatio,ChildrenLivingstatus,matri_id,/*DoshType,*/NoOfMarriedBrothers,NoOfMarriedSisters);

                    }


                }else
                {
                    Toast.makeText(EditProfileStep2Activity.this,"Please enter all required fields. ",Toast.LENGTH_LONG).show();
                }

            }
        });


        SlidingMenu();

        rvState.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvState, new SignUpStep1Activity.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvState.getWindowToken(), 0);

                beanState arrCo = arrState.get(position);
                AppConstants.StateName =arrCo.getState_name();
                AppConstants.StateId =arrCo.getState_id();

                edtState.setText(AppConstants.StateName);

                AppConstants.CityName ="";
                AppConstants.CityId ="";
                edtCity.setText("");

                ArrayList<beanCity> arrCity=null;
                CityAdapter cityAdapter=null;
                GonelidingDrower();
                getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                getCity();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvCity.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCity, new SignUpStep1Activity.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvCity.getWindowToken(), 0);

                beanCity arrCo = arrCity.get(position);
                AppConstants.CityName =arrCo.getCity_name();
                AppConstants.CityId =arrCo.getCity_id();

                edtCity.setText(AppConstants.CityName);

                GonelidingDrower();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getProfileDetailRequest(matri_id);


        getAdditionalDgree();
        getGeneralData();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppConstants.CountryId="";
        AppConstants.CountryName="";
        AppConstants.StateId="";
        AppConstants.StateName="";
        AppConstants.CityId="";
        AppConstants.CityName="";
        AppConstants.EducationId="";
        AppConstants.EducationName="";
        AppConstants.AditionalEducationId="";
        AppConstants.AditionalEducationName="";
        AppConstants.OccupationID="";
        AppConstants.OccupationName="";

        ArrayList<beanState> arrState=null;
        StateAdapter stateAdapter=null;

        ArrayList<beanCity> arrCity=null;
        CityAdapter cityAdapter=null;

        ArrayList<beanEducation> arrEducation=null;
        EducationsAdapter educationAdapter=null;

        ArrayList<beanEducation> arrAdditionalDgree=null;
        AdditionalDgreeAdapter additionalDgreeAdapter=null;

        ArrayList<beanOccupation> arrOccupation=null;
        OccupationAdapter occupationAdapter=null;

    }

    public  boolean hasData(String text)
    {
        if (text == null || text.length() == 0)
            return false;

        return true;
    }


    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        SlidingDrawer.startAnimation(AppConstants.inFromRightAnimation()) ;

        Slidingpage.setVisibility(View.VISIBLE);
        Slidingpage.startAnimation(AppConstants.inFromRightAnimation()) ;

        btnMenuClose.setVisibility(View.VISIBLE);
        btnMenuClose.startAnimation(AppConstants.inFromRightAnimation()) ;
    }

    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

        Slidingpage.setVisibility(View.GONE);
        Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

        btnMenuClose.setVisibility(View.GONE);
        btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());
    }


    public void setDateTimes()
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,int minute)
            {
                String time11 =  showTime(hourOfDay, minute);
                edtBirthTime.setText(time11);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    public String showTime(int hour, int min)
    {
        String format="";

        if (hour == 0)
        {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12)
        {
            format = "PM";
        } else if (hour > 12)
        {
            hour -= 12;
            format = "PM";
        } else
        {
            format = "AM";
        }

        NumberFormat ff = new DecimalFormat("00");

        String times= String.valueOf(new StringBuilder().append(ff.format(hour)).append(":").append(ff.format(min)).append(" ").append(format));

        return times;
    }


    public void SlidingMenu()
    {

        linState=(LinearLayout)findViewById(R.id.linState);
        linCity=(LinearLayout)findViewById(R.id.linCity);
        linHighestEducation=(LinearLayout)findViewById(R.id.linHighestEducation);
        linAdditionalDegree=(LinearLayout)findViewById(R.id.linAdditionalDegree);
        linOccupation=(LinearLayout)findViewById(R.id.linOccupation);
        linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);

        edtSearchState=(EditText)findViewById(R.id.edtSearchState);
        edtSearchCity=(EditText)findViewById(R.id.edtSearchCity);
        edtSearchHighestEducation=(EditText)findViewById(R.id.edtSearchHighestEducation);
        edtSearchAdditionalDegree=(EditText)findViewById(R.id.edtSearchAdditionalDegree);
        edtSearchOccupation=(EditText)findViewById(R.id.edtSearchOccupation);

        rvState=(RecyclerView) findViewById(R.id.rvState);
        rvCity=(RecyclerView)findViewById(R.id.rvCity);
        rvHighestEducation=(RecyclerView)findViewById(R.id.rvHighestEducation);
        rvAdditionalDegree=(RecyclerView)findViewById(R.id.rvAdditionalDegree);
        rvOccupation=(RecyclerView)findViewById(R.id.rvOccupation);
        rvGeneralView=(RecyclerView)findViewById(R.id.rvGeneralView);

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

        btnMenuClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GonelidingDrower();

            }
        });

    }



    public void getStates()
    {
        edtSearchState.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrState.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchState.getText().toString().toLowerCase(Locale.getDefault());
                    stateAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });
    }



    public void getCity()
    {
        edtSearchCity.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrCity.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchCity.getText().toString().toLowerCase(Locale.getDefault());
                    cityAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }



    public void getHighestEducation()
    {
        edtSearchHighestEducation.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrEducation.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchHighestEducation.getText().toString().toLowerCase(Locale.getDefault());
                    educationAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }

    public void getAdditionalDgree()
    {
        edtSearchAdditionalDegree.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrAdditionalDgree.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchAdditionalDegree.getText().toString().toLowerCase(Locale.getDefault());
                    additionalDgreeAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }


    public void getOccupations()
    {
        edtSearchOccupation.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrOccupation.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchOccupation.getText().toString().toLowerCase(Locale.getDefault());
                    occupationAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }



    public void getGeneralData()
    {
        edtMaritalStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtMaritalStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter1 generalAdapter= new GeneralAdapter1(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_marital_status),SlidingDrawer,
                        Slidingpage,btnMenuClose,edtMaritalStatus,textInputNoOfChiled,textInputChiledLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfChildren.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfChildren.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_no_of_children),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfChildren);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtChildrenLivingStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtChildrenLivingStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_Chiled_leaving_status),SlidingDrawer,Slidingpage,btnMenuClose,edtChildrenLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtState.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                if(arrState.size()>0)
                {

                }else
                {
                    rvState.setAdapter(null);
                }
                edtState.setError(null);
                edtSearchState.setText("");
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

        edtCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                if(arrCity.size()>0)
                {

                }else
                {
                    rvCity.setAdapter(null);
                }
                edtCity.setError(null);
                edtSearchCity.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });



        edtHeight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHeight.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_height),SlidingDrawer,Slidingpage,btnMenuClose,edtHeight);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtWeight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtWeight.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_weight),SlidingDrawer,Slidingpage,btnMenuClose,edtWeight);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtBodyType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtBodyType.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_body_type),SlidingDrawer,Slidingpage,btnMenuClose,edtBodyType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_physical_status),SlidingDrawer,Slidingpage,btnMenuClose,edtPhysicalStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHighestEducation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHighestEducation.setError(null);
                edtSearchHighestEducation.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.VISIBLE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

//        edtHighestEducation.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                VisibleSlidingDrower();
//                edtHighestEducation.setError(null);
//                linState.setVisibility(View.GONE);  linCity.setVisibility(View.GONE); linGeneralView.setVisibility(View.VISIBLE);
//
//                GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_highest_education),SlidingDrawer,Slidingpage,btnMenuClose,edtHighestEducation);
//                rvGeneralView.setAdapter(generalAdapter);
//
//            }
//        });


        edtDegree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDegree.setError(null);
                edtSearchAdditionalDegree.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

//        edtDegree.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                VisibleSlidingDrower();
//                edtDegree.setError(null);
//                linState.setVisibility(View.GONE);  linCity.setVisibility(View.GONE); linGeneralView.setVisibility(View.VISIBLE);
//
//                rvGeneralView.setAdapter(null);
//                GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_highest_education),SlidingDrawer,Slidingpage,btnMenuClose,edtDegree);
//                rvGeneralView.setAdapter(generalAdapter);
//
//            }
//        });

        edtOccupation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

//        edtOccupation.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                VisibleSlidingDrower();
//                edtOccupation.setError(null);
//                linState.setVisibility(View.GONE);  linCity.setVisibility(View.GONE); linGeneralView.setVisibility(View.VISIBLE);
//
//                rvGeneralView.setAdapter(null);
//                GeneralAdapter generalAdapter= new GeneralAdapter(SignUpStep2Activity.this, getResources().getStringArray(R.array.arr_occcupation),SlidingDrawer,Slidingpage,btnMenuClose,edtOccupation);
//                rvGeneralView.setAdapter(generalAdapter);
//
//            }
//        });

        edtEmployedIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtEmployedIn.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_Employed_in),SlidingDrawer,Slidingpage,btnMenuClose,edtEmployedIn);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAnnualIncome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_annual_income),SlidingDrawer,Slidingpage,btnMenuClose,edtAnnualIncome);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtDiet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDiet.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_diet),SlidingDrawer,Slidingpage,btnMenuClose,edtDiet);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtSmoking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtSmoking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_smoking_2),SlidingDrawer,Slidingpage,btnMenuClose,edtSmoking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtDrinking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDrinking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_drinking_2),SlidingDrawer,Slidingpage,btnMenuClose,edtDrinking);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHaveDosh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHaveDosh.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_manglik_2),SlidingDrawer,Slidingpage,btnMenuClose,edtHaveDosh);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHaveDosh.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String strSelectedType=edtHaveDosh.getText().toString().trim();
                if(strSelectedType.equalsIgnoreCase("Yes"))
                {
                    //inputDoshType.setVisibility(View.VISIBLE);
                }else
                {
                   // inputDoshType.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

            }
        });

/*
        edtDoshType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDoshType.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_dosh_type),SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });
*/

        edtStar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtStar.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_star),SlidingDrawer,Slidingpage,btnMenuClose,edtStar);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtRassi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtRassi.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_raasi_moon_sign),SlidingDrawer,Slidingpage,btnMenuClose,edtRassi);
                rvGeneralView.setAdapter(generalAdapter);
            }
        });

        edtStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtStatus.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_family_status),SlidingDrawer,Slidingpage,btnMenuClose,edtStatus);
                rvGeneralView.setAdapter(generalAdapter);
            }
        });

        edtType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtType.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_family_type),SlidingDrawer,Slidingpage,btnMenuClose,edtType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtValues.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtValues.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_family_value),SlidingDrawer,Slidingpage,btnMenuClose,edtValues);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfBrothers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfBrothers.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_brothers),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtNoOfBrothers.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {

                String strSelectedType=edtNoOfBrothers.getText().toString().trim();
                if(strSelectedType.equalsIgnoreCase("No Brothers"))
                {
                    inputNoOfMarriedBrother.setVisibility(View.GONE);
                }else
                {
                    inputNoOfMarriedBrother.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

            }
        });

        edtNoOfMarriedBrothers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfMarriedBrothers.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_married_brothers),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfMarriedBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });



        edtNoOfSisters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfSisters.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_sisters),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfSisters);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfSisters.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String strSelectedType=edtNoOfSisters.getText().toString().trim();
                if(strSelectedType.equalsIgnoreCase("No Sisters"))
                {
                    inputNoOfMarriedSisters.setVisibility(View.GONE);
                }else
                {
                    inputNoOfMarriedSisters.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

            }
        });

        edtNoOfMarriedSisters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfMarriedSisters.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileStep2Activity.this, getResources().getStringArray(R.array.arr_married_sisters),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfMarriedSisters);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

    }


    private void getProfileDetailRequest(String strMatriId)
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"profile.php";
                Log.e("View Profile", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
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

                } catch (Exception uee) //UnsupportedEncodingException
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);

                Log.e("Profile Id", "=="+result);

                String finalresult="";
                try
                {

                    finalresult=result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(finalresult);


                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext())
                            {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);


                                String strMaritStatus=resItem.getString("m_status");
                                edtMaritalStatus.setText(strMaritStatus);
                                if(strMaritStatus.equalsIgnoreCase("Never Married"))
                                {
                                    textInputNoOfChiled.setVisibility(View.GONE);
                                    textInputChiledLivingStatus.setVisibility(View.GONE);
                                }else
                                {
                                    textInputNoOfChiled.setVisibility(View.VISIBLE);
                                    textInputChiledLivingStatus.setVisibility(View.VISIBLE);
                                }

                                AppConstants.CountryId=resItem.getString("country_id");
                                edtState.setText(resItem.getString("state_name"));
                                AppConstants.StateId=resItem.getString("state_id");
                                edtCity.setText(resItem.getString("city_name"));
                                AppConstants.CityId=resItem.getString("city");
                                edtNoOfChildren.setText(resItem.getString("tot_children"));
                                edtChildrenLivingStatus.setText(resItem.getString("status_children"));
                                edtHeight.setText(resItem.getString("height"));
                                edtWeight.setText(resItem.getString("weight"));
                                edtBodyType.setText(resItem.getString("bodytype"));
                                edtPhysicalStatus.setText(resItem.getString("physicalStatus"));
                                edtHighestEducation.setText(resItem.getString("edu_detail"));
                                AppConstants.EducationId=resItem.getString("edu_detail_id");
                                edtDegree.setText(resItem.getString("addition_detail"));
                                AppConstants.AditionalEducationId=resItem.getString("addition_dgree_id");
                                edtOccupation.setText(resItem.getString("occupation"));
                                AppConstants.OccupationID=resItem.getString("occupation_id");
                                edtEmployedIn.setText(resItem.getString("emp_in"));
                                edtAnnualIncome.setText(resItem.getString("income"));
                                edtDiet.setText(resItem.getString("diet"));
                                edtSmoking.setText(resItem.getString("smoke"));
                                edtDrinking.setText(resItem.getString("drink"));
                                edtHaveDosh.setText(resItem.getString("dosh"));
                                edtStar.setText(resItem.getString("star"));
                                //Log.d("ravi",resItem.getString("star"));
                                edtRassi.setText(resItem.getString("moonsign"));
                                //Log.d("ravi",resItem.getString("moonsign"));
                                edtBirthTime.setText(resItem.getString("birthtime"));
                                edtBirthPlace.setText(resItem.getString("birthplace"));
                                edtStatus.setText(resItem.getString("family_status"));
                                edtType.setText(resItem.getString("family_type"));
                                edtValues.setText(resItem.getString("family_value"));
                                edtFatherOccupation.setText(resItem.getString("father_occupation"));
                                edtMotherOccupation.setText(resItem.getString("mother_occupation"));
                                edtNoOfBrothers.setText(resItem.getString("no_of_brothers"));
                                edtNoOfSisters.setText(resItem.getString("no_of_sisters"));
                                edtOtherInformatio.setText(resItem.getString("profile_text"));

                               /* if(resItem.getString("dosh").equalsIgnoreCase("Yes"))
                                {
                                   // inputDoshType.setVisibility(View.VISIBLE);
                                    //edtDoshType.setText(""+resItem.getString("manglik"));
                                }else
                                {
                                   // inputDoshType.setVisibility(View.GONE);
                                }*/

                                String noOfBrothers=resItem.getString("no_of_brothers");
                                if(!noOfBrothers.equalsIgnoreCase("0") && !noOfBrothers.equalsIgnoreCase("") && !noOfBrothers.equalsIgnoreCase("No Brothers"))
                                {
                                    inputNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    edtNoOfMarriedBrothers.setText(""+resItem.getString("no_marri_brother"));
                                }else
                                {
                                    inputNoOfMarriedBrother.setVisibility(View.GONE);
                                }

                                String noOfSister=resItem.getString("no_of_sisters");
                                if(!noOfSister.equalsIgnoreCase("0") && !noOfSister.equalsIgnoreCase("") && !noOfSister.equalsIgnoreCase("No Brothers"))
                                {
                                    inputNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    edtNoOfMarriedSisters.setText(""+resItem.getString("no_marri_sister"));
                                }else
                                {
                                    inputNoOfMarriedSisters.setVisibility(View.GONE);
                                }

                                String NoOfBrothers=edtNoOfBrothers.getText().toString().trim();
                                String NoOfMarriedBrothers=edtNoOfMarriedBrothers.getText().toString().trim();
                                String NoOfSisters=edtNoOfSisters.getText().toString().trim();
                                String NoOfMarriedSisters=edtNoOfMarriedSisters.getText().toString().trim();
                                String OtherInformatio=edtOtherInformatio.getText().toString().trim();


                                String strWillToMaryCaste=resItem.getString("will_to_mary_caste");
                                if(strWillToMaryCaste.equalsIgnoreCase("Yes"))
                                {
                                    //toggleBtnOtherCaste.setChecked(true);
                                    WillingToMarry="Yes";
                                }else
                                {
                                   // toggleBtnOtherCaste.setChecked(false);
                                    WillingToMarry="No";
                                }


                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileStep2Activity.this);
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


                    progresDialog.dismiss();
                } catch (Throwable t)
                {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();


                getStateRequest(AppConstants.CountryId);
                getStates();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    private void getStateRequest(final String CoId)
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"state.php";//?country_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("country_id", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

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
                Log.e("--State --", "=="+Ressponce);

                try {
                    arrState= new ArrayList<beanState>();
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String SID=resItem.getString("state_id");
                            String SName=resItem.getString("state_name");

                            arrState.add(new beanState(SID,SName));

                        }

                        if(arrState.size() >0)
                        {
                            Collections.sort(arrState, new Comparator<beanState>() {
                                @Override
                                public int compare(beanState lhs, beanState rhs) {
                                    return lhs.getState_name().compareTo(rhs.getState_name());
                                }
                            });
                            stateAdapter= new StateAdapter(EditProfileStep2Activity.this, arrState,SlidingDrawer,Slidingpage,btnMenuClose,edtState);
                            rvState.setAdapter(stateAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                    getCity();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId);
    }



    private void getCityRequest(String CoId,String SaId)
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramCountry = params[0];
                String paramState = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"city.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair CountryPAir = new BasicNameValuePair("country_id", paramCountry);
                BasicNameValuePair CityPAir = new BasicNameValuePair("state_id", paramState);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(CountryPAir);
                nameValuePairList.add(CityPAir);

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

                Log.e("--City --", "=="+Ressponce);

                try {
                    arrCity= new ArrayList<beanCity>();
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CID=resItem.getString("city_id");
                            String CName=resItem.getString("city_name");

                            arrCity.add(new beanCity(CID,CName));

                        }

                        if(arrCity.size() >0)
                        {
                            Collections.sort(arrCity, new Comparator<beanCity>() {
                                @Override
                                public int compare(beanCity lhs, beanCity rhs) {
                                    return lhs.getCity_name().compareTo(rhs.getCity_name());
                                }
                            });
                            cityAdapter= new CityAdapter(EditProfileStep2Activity.this, arrCity,SlidingDrawer,Slidingpage,btnMenuClose,edtCity);
                            rvCity.setAdapter(cityAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;


                } catch (Exception e)
                {

                } finally
                {
                    progresDialog.dismiss();
                    getHighestEducationRequest();
                    getHighestEducation();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId,SaId);
    }




    private void getHighestEducationRequest()
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"education.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
             /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "=="+Ressponce);

                try {
                    arrEducation= new ArrayList<beanEducation>();
                    arrAdditionalDgree= new ArrayList<beanEducation>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");



                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("edu_id");
                            String edu_name=resItem.getString("edu_name");

                            arrEducation.add(new beanEducation(edu_id,edu_name));
                            arrAdditionalDgree.add(new beanEducation(edu_id,edu_name));

                        }

                        if(arrEducation.size() >0)
                        {
                            Collections.sort(arrEducation, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });
                            educationAdapter = new EducationsAdapter(EditProfileStep2Activity.this, arrEducation,SlidingDrawer,Slidingpage,btnMenuClose,edtHighestEducation);
                            rvHighestEducation.setAdapter(educationAdapter);

                        }

                        if(arrEducation.size() >0)
                        {
                            Collections.sort(arrAdditionalDgree, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });

                            additionalDgreeAdapter= new AdditionalDgreeAdapter(EditProfileStep2Activity.this, arrAdditionalDgree,SlidingDrawer,Slidingpage,btnMenuClose,edtDegree);
                            rvAdditionalDegree.setAdapter(additionalDgreeAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    getOccupationsRequest();
                    getOccupations();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }



    private void getOccupationsRequest()
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"occupation.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
             /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "=="+Ressponce);

                try {
                    arrOccupation= new ArrayList<beanOccupation>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("ocp_id");
                            String edu_name=resItem.getString("ocp_name");

                            arrOccupation.add(new beanOccupation(edu_id,edu_name));

                        }

                        if(arrOccupation.size() >0)
                        {
                            Collections.sort(arrOccupation, new Comparator<beanOccupation>() {
                                @Override
                                public int compare(beanOccupation lhs, beanOccupation rhs) {
                                    return lhs.getOccupation_name().compareTo(rhs.getOccupation_name());
                                }
                            });
                            occupationAdapter = new OccupationAdapter(EditProfileStep2Activity.this, arrOccupation,SlidingDrawer,Slidingpage,btnMenuClose,edtOccupation);
                            rvOccupation.setAdapter(occupationAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }



    private void getUpdateProfileSteps2(String strMaritalStatus,String strNoOfChildren,String strWillingToMarry,String strStateId,String strCityId,
                                      String strHeight,String strWeight,String strBodyType,String strPhysicalStatus,String strEducationId,
                                      String strAditionalEducationId,String strOccupationID,String strEmployedIn,String strAnnualIncome,
                                      String strDiet,String strSmoking,String strDrinking,String strHaveDosh,String strStar,String strRassi,
                                      String strBirthTime,String strBirthPlace,String strStatus,String strType,String strValues,String strFatherOccupation,
                                      String strMotherOccupation,String strNoOfBrothers,String strNoOfSisters,String strOtherInformatio,
                                        String strChildrenLivingstatus,String matri_id,/*String doseType,*/String marridBrothers,String marridSister)
    {
        progresDialog= new ProgressDialog(EditProfileStep2Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String PMaritalStatus= params[0];
                String PNoOfChildren= params[1];
                String PWillingToMarry= params[2];
                String PStateId= params[3];
                String PCityId= params[4];
                String PHeight= params[5];
                String PWeight= params[6];
                String PBodyType= params[7];
                String PPhysicalStatus= params[8];
                String PEducationId= params[9];
                String PAditionalEducationId= params[10];
                String POccupationID= params[11];
                String PEmployedIn= params[12];
                String PAnnualIncome= params[13];
                String PDiet= params[14];
                String PSmoking= params[15];
                String PDrinking= params[16];
                String PHaveDosh= params[17];
                String PStar= params[18];
                String PRassi= params[19];
                String PBirthTime= params[20];
                String PBirthPlace= params[21];
                String PStatus= params[22];
                String PType= params[23];
                String PValues= params[24];
                String PFatherOccupation= params[25];
                String PMotherOccupation= params[26];
                String PNoOfBrothers= params[27];
                String PNoOfSisters= params[28];
                String POtherInformatio= params[29];
                String PChildrenLivingstatus= params[30];
                String Pmatri_id= params[31];
               // String DoseType= params[32];
                String MarridBrothers= params[33];
                String MarridSister= params[34];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_step2.php";
                Log.e("URL", "== "+URL);

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
                BasicNameValuePair RassiPair = new BasicNameValuePair("rassi", PRassi);
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
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", Pmatri_id);
               // BasicNameValuePair DoshTypePair = new BasicNameValuePair("manglik", DoseType);
                BasicNameValuePair MarridBrothersPair = new BasicNameValuePair("no_marri_brother", MarridBrothers);
                BasicNameValuePair MarridSisterPair = new BasicNameValuePair("no_marri_sister", MarridSister);

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
                nameValuePairList.add(MatriIdPair);
               // nameValuePairList.add(DoshTypePair);
                nameValuePairList.add(MarridBrothersPair);
                nameValuePairList.add(MarridSisterPair);

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
                        Intent intLogin= new Intent(EditProfileStep2Activity.this,EditProfileActivity.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileStep2Activity.this);
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

        sendPostReqAsyncTask.execute(strMaritalStatus,strNoOfChildren,strWillingToMarry,strStateId,strCityId,strHeight,strWeight,
                strBodyType,strPhysicalStatus,strEducationId,strAditionalEducationId,strOccupationID,
                strEmployedIn,strAnnualIncome,strDiet,strSmoking,strDrinking,strHaveDosh,strStar,strRassi,
                strBirthTime,strBirthPlace,strStatus,strType,strValues,strFatherOccupation,strMotherOccupation,strNoOfBrothers,strNoOfSisters,
                strOtherInformatio,strChildrenLivingstatus,matri_id,/*doseType,*/marridBrothers,marridSister);

    }



}
