package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.CasteAdapter;
import Adepters.CountryAdapter;
import Adepters.CountryCodeAdapter;
import Adepters.GeneralAdapter;
import Adepters.MaritalStatusAdapter;
import Adepters.MotherTongueAdapter;
import Adepters.ProfileCreatedAdapter;
import Adepters.ReligionAdapter;
import Models.beanCaste;
import Models.beanCountries;
import Models.beanCountryCode;
import Models.beanMotherTongue;
import Models.beanProfileCreated;
import Models.beanReligion;
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

public class EditProfileBasic extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    ImageView btnMenuClose;
    EditText edtProfileCreatedBy,edtFirstName,edtBirthdate,
            edtMotherTongue,edtLastName,
            edtMobileNo,edtNoOfChildren,edtChildrenLivingStatus;

  //  CheckBox checkboxIAgree;
    Button /*btnFacebook,*/ btnRegisterNow;

    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    //ImageView btnMenuClose;

    LinearLayout linGeneralView,linProfileCreatedBy,linMaritalStatus,linReligion,linCaste,linMotherTongue,linCountry,linCountryCode;
    EditText edtdrawerMaritalStatus,edtMaritalStts,edtSearchProfileCreatedBy,edtSearchReligion,edtSearchCaste,edtSearchMotherTongue,edtSearchCountry,edtSearchCountryCode;
    RecyclerView rvProfileCreatedBy,rvMotherTongue,rvMaritalStatus;

    TextInputLayout textInputNoOfChiled,textInputChiledLivingStatus;
    ArrayList<beanProfileCreated> arrProfileCreated=null;
    ArrayList<beanProfileCreated> arrMaritalStatus=null;
    ProfileCreatedAdapter profileCreatedAdapter=null;

    ArrayList<beanReligion> arrReligion=new ArrayList<beanReligion>();
    ReligionAdapter religionAdapter=null;

    ArrayList<beanCaste> arrCaste=new ArrayList<beanCaste>();
    CasteAdapter casteAdapter=null;

    ArrayList<beanMotherTongue> arrMotherTongue=new ArrayList<beanMotherTongue>();
    MotherTongueAdapter motherTongueAdapter=null;

    ArrayList<beanCountries> arrCountry=new ArrayList<beanCountries>();
    CountryAdapter countryAdapter=null;

    ArrayList<beanCountryCode> arrCountryCode=new ArrayList<beanCountryCode>();
    CountryCodeAdapter countryCodeAdapter=null;
    RecyclerView rvGeneralView;
    private int mYear, mMonth, mDay;

    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id="";
    String SignEditType="";
    String Maritalstatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_step_1);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(EditProfileBasic.this);
        matri_id=prefUpdate.getString("matri_id","");
        SignEditType= getIntent().getStringExtra("Step1EditProfile");
        Maritalstatus = getIntent().getStringExtra("status");



        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Basic Details");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        edtMaritalStts = findViewById(R.id.edtMaritalStts);
        edtProfileCreatedBy=(EditText)findViewById(R.id.edtProfileCreatedBy);
        edtFirstName=(EditText)findViewById(R.id.edtFirstName);
        edtBirthdate=(EditText)findViewById(R.id.edtBirthdate);
        edtMotherTongue=(EditText)findViewById(R.id.edtMotherTongue);
        edtMobileNo=(EditText)findViewById(R.id.edtMobileNo);
        textInputNoOfChiled=(TextInputLayout) findViewById(R.id.textInputNoOfChiled);
        textInputChiledLivingStatus=(TextInputLayout)findViewById(R.id.textInputChiledLivingStatus);
        edtNoOfChildren=(EditText)findViewById(R.id.edtNoOfChildren);
        edtChildrenLivingStatus=(EditText)findViewById(R.id.edtChildrenLivingStatus);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        btnMenuClose.setVisibility(View.GONE);
        edtLastName=(EditText)findViewById(R.id.edtLastName);



        btnRegisterNow=(Button)findViewById(R.id.btnRegisterNow);
        btnRegisterNow.setText("Update Profile");

        edtMaritalStts.setText(Maritalstatus);
        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
       // btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        //btnMenuClose.setVisibility(View.GONE);



      onclick();

    }


    public  void onclick(){

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });
        edtLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                edtLastName.requestFocus();
                edtLastName.setError(null);
            }
        });

        edtFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                edtFirstName.requestFocus();
                edtFirstName.setError(null);
            }
        });


        edtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AppMethods.hideKeyboard(view);
                edtBirthdate.setError(null);

                setDatess();
            }
        });
        edtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                edtMobileNo.requestFocus();
                edtMobileNo.setError(null);
            }
        });


        edtProfileCreatedBy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtProfileCreatedBy.setError(null);
                edtSearchProfileCreatedBy.setText("");
                linProfileCreatedBy.setVisibility(View.VISIBLE);
                linMaritalStatus.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
            }
        });

        edtMaritalStts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                edtMaritalStts.setError(null);
              //  edtMaritalStts.setText("");
                edtdrawerMaritalStatus.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.VISIBLE);
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
               /* rvGeneralView.setAdapter(null);
                GeneralAdapter1 generalAdapter= new GeneralAdapter1(EditProfileBasic.this, getResources().getStringArray(R.array.arr_marital_status),SlidingDrawer,
                        Slidingpage,btnMenuClose,edtMaritalStts,textInputNoOfChiled,textInputChiledLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);
*/
            }
        });

        edtNoOfChildren.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtNoOfChildren.setError(null);
                edtNoOfChildren.setFocusable(true);
                linMaritalStatus.setVisibility(View.GONE);
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileBasic.this, getResources().getStringArray(R.array.arr_no_of_children),SlidingDrawer,Slidingpage,btnMenuClose,edtNoOfChildren);
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
                edtChildrenLivingStatus.setFocusable(true);
                linMaritalStatus.setVisibility(View.GONE);
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileBasic.this, getResources().getStringArray(R.array.arr_Chiled_leaving_status),SlidingDrawer,Slidingpage,btnMenuClose,edtChildrenLivingStatus);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });
        edtMotherTongue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();

                getMotherTongue();
                getMotherToungeRequest();

                edtMotherTongue.setError(null);
                edtSearchMotherTongue.setText("");
                linProfileCreatedBy.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);

            }
        });




        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                try
                {
                    if(edtProfileCreatedBy.getText().toString().equalsIgnoreCase("") ||
                            edtFirstName.getText().toString().equalsIgnoreCase("") ||
                            edtLastName.getText().toString().equalsIgnoreCase("") ||
                            edtBirthdate.getText().toString().equalsIgnoreCase("") ||
                            AppConstants.MotherTongueId.equalsIgnoreCase("") ||
                            edtMobileNo.getText().toString().equalsIgnoreCase("")||
                            edtMaritalStts.getText().toString().equalsIgnoreCase(""))
                    {
                        if(edtProfileCreatedBy.getText().toString().equalsIgnoreCase(""))
                        {
                            edtProfileCreatedBy.setError("Please select profile creator.");
                        }
                        if(edtFirstName.getText().toString().equalsIgnoreCase(""))
                        {
                            edtFirstName.requestFocus();
                            edtFirstName.setError("Please enter firstname.");
                        }
                        if(edtLastName.getText().toString().equalsIgnoreCase(""))
                        {
                            edtLastName.requestFocus();
                            edtLastName.setError("Please enter firstname.");
                        }

                        if(edtBirthdate.getText().toString().equalsIgnoreCase(""))
                        {
                            edtBirthdate.requestFocus();
                            edtBirthdate.setError("Please select birthdate");
                        }

                        if(AppConstants.MotherTongueId.equalsIgnoreCase(""))
                        {
                            edtMotherTongue.requestFocus();
                            edtMotherTongue.setError("Please select your mother tongue.");
                        }

                        if(edtMobileNo.getText().toString().equalsIgnoreCase(""))
                        {
                            edtMobileNo.requestFocus();
                            edtMobileNo.setError("Please enter your mobile no.");
                        }


                        if(edtMaritalStts.getText().toString().equalsIgnoreCase(""))
                        {
                            edtMaritalStts.requestFocus();
                            edtMaritalStts.setError("Please enter your mobile no.");
                        }


                    }else
                    {

                        String FirstName=edtFirstName.getText().toString();
                        String LastName=edtLastName.getText().toString();
                        String Birthdate=edtBirthdate.getText().toString();
                        String MobileNo=edtMobileNo.getText().toString();
                        String MaritalStatus = edtMaritalStts.getText().toString().trim();
                        String NoOfChildren=edtNoOfChildren.getText().toString().trim();
                        String ChildrenLivingstatus=edtChildrenLivingStatus.getText().toString().trim();
                        String ProfileCreatedBy=edtProfileCreatedBy.getText().toString();

                        getUpdateSteps(ProfileCreatedBy,FirstName,LastName,Birthdate,
                               AppConstants.MotherTongueId,MaritalStatus,NoOfChildren,ChildrenLivingstatus,
                                MobileNo,matri_id);

                    }
                }catch (Exception e)
                {
                    Log.e("Error=", ""+e);
                }

            }
        });
        SlidingMenu();
        getProfileCreated();
        getMaritalStatus();

        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getViewProfileRequest(matri_id);

        }else
        {
            AppConstants.CheckConnection(EditProfileBasic.this);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppConstants.CountryId="";
        AppConstants.CountryName="";
        AppConstants.ReligionId="";
        AppConstants.CasteId="";
        AppConstants.MotherTongueId="";
        AppConstants.CountryCodeId="";
        AppConstants.ReligionName="";
        AppConstants.CasteName="";
        AppConstants.MotherTongueName="";
        AppConstants.CountryCodeName="";

        ArrayList<beanProfileCreated> arrProfileCreated=null;
        ProfileCreatedAdapter profileCreatedAdapter=null;

        ArrayList<beanReligion> arrReligion=null;
        ReligionAdapter religionAdapter=null;

        ArrayList<beanCaste> arrCaste=null;
        CasteAdapter casteAdapter=null;

        ArrayList<beanMotherTongue> arrMotherTongue=null;
        MotherTongueAdapter motherTongueAdapter=null;

        ArrayList<beanCountries> arrCountry=null;
        CountryAdapter countryAdapter=null;

        ArrayList<beanCountryCode> arrCountryCode=null;
        CountryCodeAdapter countryCodeAdapter=null;



    }

    private boolean checkEmail (String email)
    {
        return AppConstants.email_pattern.matcher(email).matches();
    }


    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfileBasic.this, SlidingDrawer, R.anim.slide_right);
        //SlidingDrawer.startAnimation(AppConstants.inFromRightAnimation()) ;
        Slidingpage.setVisibility(View.VISIBLE);


    }

    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileBasic.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intLogin= new Intent(EditProfileBasic.this, MenuProfileEdit.class);
        startActivity(intLogin);
        finish();
    }

    public void SlidingMenu()
    {
        linMaritalStatus=findViewById(R.id.linMaritalStatus);
        linProfileCreatedBy=(LinearLayout)findViewById(R.id.linProfileCreatedBy);
        linReligion=(LinearLayout)findViewById(R.id.linReligion);
        linCaste=(LinearLayout)findViewById(R.id.linCaste);
        linMotherTongue=(LinearLayout)findViewById(R.id.linMotherTongue);
        linCountry=(LinearLayout)findViewById(R.id.linCountry);
        linCountryCode=(LinearLayout)findViewById(R.id.linCountryCode);
        linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);

        edtSearchProfileCreatedBy=(EditText)findViewById(R.id.edtSearchProfileCreatedBy);
        edtSearchReligion=(EditText)findViewById(R.id.edtSearchReligion);
        edtSearchCaste=(EditText)findViewById(R.id.edtSearchCaste);
        edtSearchMotherTongue=(EditText)findViewById(R.id.edtSearchMotherTongue);
        edtSearchCountry=(EditText)findViewById(R.id.edtSearchCountry);
        edtSearchCountryCode=(EditText)findViewById(R.id.edtSearchCountryCode);
        edtdrawerMaritalStatus = findViewById(R.id.edtdrawerMaritalStatus);
        rvMaritalStatus = findViewById(R.id.rvMaritalStatus);
        rvGeneralView=(RecyclerView)findViewById(R.id.rvGeneralView);
        rvProfileCreatedBy=(RecyclerView) findViewById(R.id.rvProfileCreatedBy);

        rvMotherTongue=(RecyclerView)findViewById(R.id.rvMotherTongue);

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        rvMaritalStatus.setLayoutManager(new LinearLayoutManager(this));
        rvMaritalStatus.setHasFixedSize(true);

        rvProfileCreatedBy.setLayoutManager(new LinearLayoutManager(this));
        rvProfileCreatedBy.setHasFixedSize(true);

        rvMotherTongue.setLayoutManager(new LinearLayoutManager(this));
        rvMotherTongue.setHasFixedSize(true);

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GonelidingDrower();

            }
        });



    }

    public void setDatess()
    {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        Log.e("curruntyear",mYear+"");
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String  curruntDate  = mYear +"-"+ mMonth+"-"+mDay+"";
        Log.e("date_TODAY",curruntDate);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(EditProfileBasic.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                edtBirthdate.setText(dateDesc);

                Toast.makeText(EditProfileBasic.this, dateDesc, Toast.LENGTH_SHORT).show();
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#EF6C00"))//color of confirm button
                .minYear(1924) //min year in loop
                .maxYear(2000) // max year in loop
                .dateChose("1999-01-01") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(EditProfileBasic.this);

    }

    public void getMaritalStatus()
    {

        arrMaritalStatus= new ArrayList<beanProfileCreated>();

        arrMaritalStatus.add(new beanProfileCreated("1","Never Married"));
        arrMaritalStatus.add(new beanProfileCreated("2","Widower"));
        arrMaritalStatus.add(new beanProfileCreated("3","Divorced"));
        arrMaritalStatus.add(new beanProfileCreated("4","Awaiting Divorce"));

        String [] list = {"Never Married","Widower","Divorced","Awaiting Divorce"};

        if(arrMaritalStatus.size() >0)
        {


           /* profileCreatedAdapter= new ProfileCreatedAdapter(EditProfileBasic.this, arrMaritalStatus,SlidingDrawer,Slidingpage,edtMaritalStts);
            rvMaritalStatus.setAdapter(profileCreatedAdapter);*/
            MaritalStatusAdapter generalAdapter= new MaritalStatusAdapter(EditProfileBasic.this, arrMaritalStatus,SlidingDrawer,
                    Slidingpage,btnMenuClose,edtMaritalStts,textInputNoOfChiled,textInputChiledLivingStatus);
            rvMaritalStatus.setAdapter(generalAdapter);

        }

    }



    public void getProfileCreated()
    {
        arrProfileCreated= new ArrayList<beanProfileCreated>();

        arrProfileCreated.add(new beanProfileCreated("1","Self"));
        arrProfileCreated.add(new beanProfileCreated("2","Parents"));
        arrProfileCreated.add(new beanProfileCreated("3","Guardian"));
        arrProfileCreated.add(new beanProfileCreated("4","Friends"));
        arrProfileCreated.add(new beanProfileCreated("5","Sibling"));
        arrProfileCreated.add(new beanProfileCreated("6","Relative"));


        if(arrProfileCreated.size() >0)
        {
            profileCreatedAdapter= new ProfileCreatedAdapter(EditProfileBasic.this, arrProfileCreated,SlidingDrawer,Slidingpage,edtProfileCreatedBy);
            rvProfileCreatedBy.setAdapter(profileCreatedAdapter);
        }

        edtdrawerMaritalStatus.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrProfileCreated.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchProfileCreatedBy.getText().toString().toLowerCase(Locale.getDefault());
                    profileCreatedAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }

    public void getMotherTongue()
    {
        try {
            edtSearchMotherTongue.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrMotherTongue.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchMotherTongue.getText().toString().toLowerCase(Locale.getDefault());
                        motherTongueAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }


    }





    private void getViewProfileRequest(String strMatriId)
    {
        final ProgressDialog progresDialog11= new ProgressDialog(EditProfileBasic.this);
        progresDialog11.setCancelable(false);
        progresDialog11.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog11.setIndeterminate(true);
        progresDialog11.show();

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

                Log.e("updatebasic", "=="+result);

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


                                edtProfileCreatedBy.setText(resItem.getString("profileby"));

                                String LFName = resItem.getString("username");
                                String[] name_array =  LFName.split(" ");

                                edtFirstName.setText(name_array[0]);
                                edtLastName.setText(name_array[1]);


                                edtBirthdate.setText(resItem.getString("birthdate"));
                                edtMotherTongue.setText(resItem.getString("m_tongue"));
                                AppConstants.MotherTongueId=resItem.getString("m_tongue_id");
                                edtMobileNo.setText(resItem.getString("mobile"));
                                String strGender = resItem.getString("gender");
                                Maritalstatus=resItem.getString("m_status");
                                edtMaritalStts.setText(Maritalstatus);
                                if(Maritalstatus.equalsIgnoreCase("Never Married"))
                                {
                                    textInputNoOfChiled.setVisibility(View.GONE);
                                    textInputChiledLivingStatus.setVisibility(View.GONE);
                                }else
                                {
                                    textInputNoOfChiled.setVisibility(View.VISIBLE);
                                    textInputChiledLivingStatus.setVisibility(View.VISIBLE);
                                    String No_Child = resItem.getString("tot_children");
                                    String Child_LivingStatus=resItem.getString("status_children");
                                    edtNoOfChildren.setText(No_Child);
                                    edtChildrenLivingStatus.setText(Child_LivingStatus);
                                }




                               /* if (No_Child!= null && !No_Child.equalsIgnoreCase(""))
                                {
                                    edtNoOfChildren.setVisibility(View.VISIBLE);
                                    edtNoOfChildren.setText(No_Child);
                                }
                                else
                                {
                                    edtNoOfChildren.setVisibility(View.GONE);
                                }
                                if (Child_LivingStatus!= null && !Child_LivingStatus.equalsIgnoreCase(""))
                                {
                                    edtChildrenLivingStatus.setVisibility(View.VISIBLE);
                                    edtChildrenLivingStatus.setText(Child_LivingStatus);
                                }
                                else
                                {
                                    edtChildrenLivingStatus.setVisibility(View.GONE);
                                }*/



                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileBasic.this);
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


                    progresDialog11.dismiss();
                } catch (Exception t)
                {
                 Log.e("eeeeeeee",t.getMessage());
                }
                progresDialog11.dismiss();

//                getCountrysRequest();
//                getCountries();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }





    private void getMotherToungeRequest()
    {
        progresDialog= new ProgressDialog(EditProfileBasic.this);
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

                String URL= AppConstants.MAIN_URL +"mother_tounge.php";
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
                Log.e("--mother_tounge --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrMotherTongue= new ArrayList<beanMotherTongue>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id=resItem.getString("mtongue_id");
                            String mtongue_name=resItem.getString("mtongue_name");

                            arrMotherTongue.add(new beanMotherTongue(mtongue_id,mtongue_name));

                        }

                        if(arrMotherTongue.size() >0)
                        {
                            Collections.sort(arrMotherTongue, new Comparator<beanMotherTongue>() {
                                @Override
                                public int compare(beanMotherTongue lhs, beanMotherTongue rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            motherTongueAdapter = new MotherTongueAdapter(EditProfileBasic.this, arrMotherTongue,SlidingDrawer,Slidingpage,edtMotherTongue);
                            rvMotherTongue.setAdapter(motherTongueAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void getUpdateSteps(String ProfileCreatedBy,String Firstname,String LastName,String Birthdate
                                    ,String MotherTongueId,String MaritalStatus, String NoOfChildren,
                                String ChildrenLivingstatus, String MobileNo,String matri_id)
    {
        progresDialog= new ProgressDialog(EditProfileBasic.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String paramProfileCreatedBy = params[0];
                String paramFirstname = params[1];
                String paramLastname = params[2];
                String paramBirthdate = params[3];
                String paramMotherTongueId = params[4];
                String paramMaritalStatus = params[5];
                String paramNoOfChildren = params[6];
                String paramChildrenLivingStatus = params[7];
                String paramMobileNo = params[8];
                String paramMatriid = params[9];



                HttpClient httpClient = new DefaultHttpClient();

               // String URL= AppConstants.MAIN_URL +"edit_step1.php";
                String URL= AppConstants.MAIN_URL +"edit_basic_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
               BasicNameValuePair ProfileCreatedByPAir = new BasicNameValuePair("profile_created_by", paramProfileCreatedBy);
              //  BasicNameValuePair ProfileCreatedByPAir = new BasicNameValuePair("profileby", paramProfileCreatedBy);

                BasicNameValuePair FirstnamePAir = new BasicNameValuePair("firstname", paramFirstname);
                BasicNameValuePair LastnamePAir = new BasicNameValuePair("lastname", paramLastname);
                BasicNameValuePair BirthdatePAir = new BasicNameValuePair("birthdate", paramBirthdate);
                BasicNameValuePair MotherTongueIdPAir = new BasicNameValuePair("mother_tongue_id", paramMotherTongueId);
                BasicNameValuePair MaritalStatusPAir = new BasicNameValuePair("marital_status", paramMaritalStatus);
                BasicNameValuePair NoOfChildrenPAir = new BasicNameValuePair("no_of_children", paramNoOfChildren);
                BasicNameValuePair ChildrenLivingStatusPAir = new BasicNameValuePair("children_living_status", paramChildrenLivingStatus);
                BasicNameValuePair MobileNoPAir = new BasicNameValuePair("mobile_no", paramMobileNo);
                BasicNameValuePair MatriIdPAir = new BasicNameValuePair("matri_id", paramMatriid);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(ProfileCreatedByPAir);
                nameValuePairList.add(FirstnamePAir);
                nameValuePairList.add(LastnamePAir);
                nameValuePairList.add(BirthdatePAir);
                nameValuePairList.add(MotherTongueIdPAir);
                nameValuePairList.add(MaritalStatusPAir);
                nameValuePairList.add(NoOfChildrenPAir);
                nameValuePairList.add(ChildrenLivingStatusPAir);
                nameValuePairList.add(MobileNoPAir);
                nameValuePairList.add(MatriIdPAir);



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
                        Toast.makeText(EditProfileBasic.this,""+message,Toast.LENGTH_LONG).show();

                        Intent intLogin= new Intent(EditProfileBasic.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileBasic.this);
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
        sendPostReqAsyncTask.execute(ProfileCreatedBy,Firstname,LastName,Birthdate,
                MotherTongueId,MaritalStatus,NoOfChildren,ChildrenLivingstatus,MobileNo,matri_id);




    }







}
