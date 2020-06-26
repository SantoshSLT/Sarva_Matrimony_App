package com.thegreentech;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.LoopScrollListener;
import com.bruce.pickerview.LoopView;
import com.bruce.pickerview.popwindow.DatePickerPopWin;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.CasteAdapter;
import Adepters.CountryAdapter;
import Adepters.CountryCodeAdapter;
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
import utills.RecyclerTouchListener;

public class SignUpStep1Activity extends AppCompatActivity {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp, ivMale, ivFemale;

    EditText edtProfileCreatedBy, edtFirstName, edtLastName, edtBirthdate,
            edtReligion, edtCaste, edtMotherTongue, edtCountry, edtCountryCode,
            edtMobileNo, edtEmailId;/*edtPassword*/
    Button /*btnFacebook,*/ btnRegisterNow;

    ProgressBar progressBarSlider;
    TextView tvNoDataFound;

    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;

    LinearLayout linProfileCreatedBy, linReligion, linCaste, linMotherTongue, linCountry, linCountryCode;
    EditText edtSearchProfileCreatedBy, edtSearchReligion, edtSearchCaste, edtSearchMotherTongue, edtSearchCountry,
            edtSearchCountryCode;
    RecyclerView rvProfileCreatedBy, rvReligion, rvCaste, rvMotherTongue, rvCountry, rvCountryCode;

    public static String Gender = "";

    ArrayList<beanProfileCreated> arrProfileCreated = null;
    ProfileCreatedAdapter profileCreatedAdapter = null;

    ArrayList<beanReligion> arrReligion = new ArrayList<beanReligion>();
    ReligionAdapter religionAdapter = null;

    ArrayList<beanCaste> arrCaste = new ArrayList<beanCaste>();
    CasteAdapter casteAdapter = null;

    ArrayList<beanMotherTongue> arrMotherTongue = new ArrayList<beanMotherTongue>();
    MotherTongueAdapter motherTongueAdapter = null;

    ArrayList<beanCountries> arrCountry = new ArrayList<beanCountries>();
    CountryAdapter countryAdapter = null;

    ArrayList<beanCountryCode> arrCountryCode = new ArrayList<beanCountryCode>();
    CountryCodeAdapter countryCodeAdapter = null;

    private int mYear, mMonth, mDay;

    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;

    LoopView loopView;

    RelativeLayout relMoreCMSView;
    LinearLayout linSlidingDrawer;
    TextView textCMSTitle/*textCMSContaints*/;
    WebView textCMSContaints;
    ProgressBar progressBar1;
    ImageView textClose;

    TextView tvpolicy, tvTC;


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step1);
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(SignUpStep1Activity.this);
        initilize();
        SlidingMenu();
        getProfileCreated();
        onClickListner();

        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getCountrysRequest();
            getCountries();
        } else {
            AppConstants.CheckConnection(SignUpStep1Activity.this);
        }

    }

    public void initilize() {
        progressBarSlider = findViewById(R.id.progressBarSlider);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        edtProfileCreatedBy = (EditText) findViewById(R.id.edtProfileCreatedBy);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtBirthdate = (EditText) findViewById(R.id.edtBirthdate);
        edtReligion = (EditText) findViewById(R.id.edtReligion);
        edtCaste = (EditText) findViewById(R.id.edtCaste);
        edtMotherTongue = (EditText) findViewById(R.id.edtMotherTongue);
        edtCountry = (EditText) findViewById(R.id.edtCountry);
        edtCountryCode = (EditText) findViewById(R.id.edtCountryCode);
        edtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);

        ivMale = findViewById(R.id.tvMale);
        ivFemale = findViewById(R.id.tvFemale);
        //btnFacebook=(Button)findViewById(R.id.btnFacebook);
        btnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        Slidingpage.setVisibility(View.GONE);
        //edtPassword=(EditText)findViewById(R.id.edtPassword);
        btnBack.setVisibility(View.GONE);
        textviewHeaderText.setVisibility(View.GONE);
        textviewSignUp.setVisibility(View.VISIBLE);
        textviewSignUp.setText("LOGIN");

        AppConstants.CountryCodeName = "+91";
        edtCountryCode.setText(AppConstants.CountryCodeName);

        loopView = findViewById(R.id.loop_view);
        loopView.setInitPosition(2);
        loopView.setCanLoop(false);
        loopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

            }
        });
        loopView.setTextSize(25);//must be called before setDateList
        loopView.setDataList(getList());

        relMoreCMSView = (RelativeLayout) findViewById(R.id.relMoreCMSView);
        relMoreCMSView.setVisibility(View.GONE);
        linSlidingDrawer = (LinearLayout) findViewById(R.id.linSlidingDrawer);

        textCMSTitle = (TextView) findViewById(R.id.textCMSTitle);
        textCMSContaints = findViewById(R.id.textCMSContaints);
        // textCMSContaints.setMovementMethod(new ScrollingMovementMethod());
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        tvpolicy = findViewById(R.id.tvpolicy);
        tvTC = findViewById(R.id.tvTC);

        textClose = findViewById(R.id.textClose);


    }

    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("DAY TEST:" + i);

        }
        return list;
    }


    public void onClickListner() {
        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpStep1Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bottomDown = AnimationUtils.loadAnimation(SignUpStep1Activity.this, R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown);
                linSlidingDrawer.setVisibility(View.GONE);

                relMoreCMSView.startAnimation(bottomDown);
                relMoreCMSView.setVisibility(View.GONE);
            }
        });
        tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relMoreCMSView.setVisibility(View.VISIBLE);
                linSlidingDrawer.setVisibility(View.VISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(SignUpStep1Activity.this, R.anim.slide_up_dialog);
                linSlidingDrawer.startAnimation(bottomUp);
                setStaticDataRequest("terms.php");
            }
        });

        tvpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relMoreCMSView.setVisibility(View.VISIBLE);
                linSlidingDrawer.setVisibility(View.VISIBLE);
                textCMSTitle.setText("Privacy Policy");
                Animation bottomUp = AnimationUtils.loadAnimation(SignUpStep1Activity.this, R.anim.slide_up_dialog);
                linSlidingDrawer.startAnimation(bottomUp);
                setStaticDataRequest("privecy.php");


            }
        });

        ivMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMale.setBackgroundResource(R.drawable.gender_seleted);
                ivFemale.setBackgroundResource(R.drawable.bg_black_seleted);
                ivFemale.setTextColor(getResources().getColor(R.color.black));
                ivMale.setTextColor(getResources().getColor(R.color.white));
                Gender = "Male";
            }
        });
        ivFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMale.setBackgroundResource(R.drawable.bg_black_seleted);
                ivFemale.setBackgroundResource(R.drawable.gender_seleted);
                ivFemale.setTextColor(getResources().getColor(R.color.white));
                ivMale.setTextColor(getResources().getColor(R.color.black));
                Gender = "Female";
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });


        edtFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtFirstName.requestFocus();
                edtFirstName.setError(null);
            }
        });

        edtLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtLastName.requestFocus();
                edtLastName.setError(null);
            }
        });
        edtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppMethods.hideKeyboard(view);
                edtBirthdate.setError(null);

                setDatess();
            }
        });
        edtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtMobileNo.requestFocus();
                edtMobileNo.setError(null);
            }
        });
        edtEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEmailId.requestFocus();
                edtEmailId.setError(null);
            }
        });
/*
        edtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                edtPassword.requestFocus();
                edtPassword.setError(null);
            }
        });
*/

        edtProfileCreatedBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VisibleSlidingDrower();

                edtProfileCreatedBy.setError(null);
                edtSearchProfileCreatedBy.setText("");
                linProfileCreatedBy.setVisibility(View.VISIBLE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
            }
        });


        edtReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtReligion.setError(null);
                edtSearchReligion.setText("");
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.VISIBLE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);
            }
        });

        edtCaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtReligion.getText().toString().isEmpty()) {
                    VisibleSlidingDrower();
                } else {
                    Toast.makeText(SignUpStep1Activity.this, "Please select religion First", Toast.LENGTH_SHORT).show();
                }
                edtCaste.setError(null);
                edtSearchCaste.setText("");
                if (arrCaste.size() > 0) {

                } else {
                    rvCaste.setAdapter(null);
                }


                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.VISIBLE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);

            }
        });

        edtMotherTongue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtMotherTongue.setError(null);
                edtSearchMotherTongue.setText("");
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.GONE);

            }
        });

        edtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCountry.setError(null);
                edtSearchCountry.setText("");
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.VISIBLE);
                linCountryCode.setVisibility(View.GONE);

            }
        });

        edtCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCountryCode.setError(null);
                edtSearchCountryCode.setText("");
                linProfileCreatedBy.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linCountryCode.setVisibility(View.VISIBLE);
            }
        });


        btnRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*SharedPreferences.Editor editor=prefUpdate.edit();
                editor.putString("user_id_r","1");
                editor.putString(AppConstants.MOBILE_NO,"9601562215");
                editor.putString("CountryId",AppConstants.CountryId);
                editor.putString("CountryName",AppConstants.CountryName);
                editor.putString("CountryCode","AD");
                editor.commit();

                Intent intLogin= new Intent(SignUpStep1Activity.this,VerifyMobileNumberActivity.class);
                startActivity(intLogin);
                finish();*/

                try {
                    if (edtProfileCreatedBy.getText().toString().equalsIgnoreCase("") ||
                            Gender.equalsIgnoreCase("") ||
                            edtFirstName.getText().toString().equalsIgnoreCase("") ||
                            edtLastName.getText().toString().equalsIgnoreCase("") ||
                            edtBirthdate.getText().toString().equalsIgnoreCase("") ||
                            AppConstants.ReligionId.equalsIgnoreCase("") ||
                            AppConstants.CasteId.equalsIgnoreCase("") ||
                            AppConstants.MotherTongueId.equalsIgnoreCase("") ||
                            AppConstants.CountryId.equalsIgnoreCase("") ||
                            AppConstants.CountryCodeName.equalsIgnoreCase("") ||
                            edtMobileNo.getText().toString().equalsIgnoreCase("") ||
                            edtEmailId.getText().toString().equalsIgnoreCase(""))
                           /* edtPassword.getText().toString().equalsIgnoreCase("") ||
                            ! checkboxIAgree.isChecked()*/ {

                        if (edtProfileCreatedBy.getText().toString().equalsIgnoreCase("")) {
                            edtProfileCreatedBy.setError("Please select profile creator.");
                        }

                        if (Gender.equalsIgnoreCase("")) {
                            Toast.makeText(SignUpStep1Activity.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                        }
                        if (edtFirstName.getText().toString().equalsIgnoreCase("")) {
                            edtFirstName.requestFocus();
                            edtFirstName.setError("Please enter firstname.");
                        }
                        if (edtLastName.getText().toString().equalsIgnoreCase("")) {
                            edtLastName.requestFocus();
                            edtLastName.setError("Please enter lastname.");
                        }
                        if (edtBirthdate.getText().toString().equalsIgnoreCase("")) {

                            edtBirthdate.setError("Please select birthdate");
                        }

                        if (AppConstants.MotherTongueId.equalsIgnoreCase("")) {
                            edtMotherTongue.requestFocus();
                            edtMotherTongue.setError("Please select your mother tongue.");
                        }
                        if (AppConstants.CountryId.equalsIgnoreCase("")) {
                            edtCountry.requestFocus();
                            edtCountry.setError("Please select your country.");
                        }
                        if (AppConstants.CountryCodeName.equalsIgnoreCase("")) {
                            edtCountryCode.requestFocus();
                            edtCountryCode.setError("Please select your country code.");
                        }
                        if (edtMobileNo.getText().toString().equalsIgnoreCase("")) {
                            edtMobileNo.requestFocus();
                            edtMobileNo.setError("Please enter your mobile no.");
                        }
                        if (edtEmailId.getText().toString().equalsIgnoreCase("")) {
                            edtEmailId.requestFocus();
                            edtEmailId.setError("Please enter email id.");
                        }

                        if (!checkEmail(edtEmailId.getText().toString())) {
                            edtEmailId.requestFocus();
                            edtEmailId.setError("Please enter valid email address.");
                        }
/*
                        if(edtPassword.getText().toString().equalsIgnoreCase(""))
                        {
                            edtPassword.requestFocus();
                            edtPassword.setError("Please enter your password");
                        }

                        if(! checkboxIAgree.isChecked())
                        {
                            Toast.makeText(SignUpStep1Activity.this,"Please accept terms and conditions. ",Toast.LENGTH_LONG).show();
                        }
*/

                    } else {
                        String ProfileCreatedBy = edtProfileCreatedBy.getText().toString();
                        String FirstName = edtFirstName.getText().toString();
                        String LastName = edtLastName.getText().toString();
                        String Birthdate = edtBirthdate.getText().toString();
                        String MobileNo = edtMobileNo.getText().toString();
                        String EmailId = edtEmailId.getText().toString();

                        AppConstants.CountryCodeName = edtCountryCode.getText().toString();
                        Log.e("c_code", AppConstants.CountryCodeName);
                        //String Password=edtPassword.getText().toString();

                        getRegistationSteps(ProfileCreatedBy, Gender, FirstName, LastName, Birthdate, AppConstants.ReligionId,
                                AppConstants.CasteId, AppConstants.MotherTongueId, AppConstants.CountryId, AppConstants.CountryCodeName,
                                MobileNo, EmailId);

                    }
                } catch (Exception e) {
                    Log.e("Error=", "" + e);
                }

            }
        });

        rvReligion.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvReligion, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvReligion.getWindowToken(), 0);

                beanReligion arrCo = arrReligion.get(position);
                AppConstants.ReligionId = arrCo.getReligion_id();
                AppConstants.ReligionName = arrCo.getName();

                edtReligion.setText(AppConstants.ReligionName);

                AppConstants.CasteId = "";
                AppConstants.StateName = "";

                edtCaste.setText("");

                GonelidingDrower();

                if (NetworkConnection.hasConnection(getApplicationContext())) {
                    getCastRequest(AppConstants.ReligionId);
                    getCaste();
                } else {
                    AppConstants.CheckConnection(SignUpStep1Activity.this);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    private void setStaticDataRequest(final String APIStatus) {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                String URL = AppConstants.MAIN_URL + APIStatus;
                Log.e("contact_details", "== " + URL);
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

                Log.e("contact_details", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        String page_name = responseData.getString("page_name");
                        String cms_content = responseData.getString("cms_content");

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textCMSTitle.setText(Html.fromHtml(page_name, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            textCMSTitle.setText(Html.fromHtml(page_name));
                        }

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textCMSContaints.getSettings().setJavaScriptEnabled(true);
                            textCMSContaints.loadDataWithBaseURL("", cms_content, "text/html", "UTF-8", "");

                        } else {
                            textCMSContaints.getSettings().setJavaScriptEnabled(true);
                            textCMSContaints.loadDataWithBaseURL("", cms_content, "text/html", "UTF-8", "");
                        }

                    } else {
                        String msgError = obj.getString("message");

                    }


                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t) {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(APIStatus);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppConstants.CountryId = "";
        AppConstants.CountryName = "";
        AppConstants.ReligionId = "";
        AppConstants.CasteId = "";
        AppConstants.MotherTongueId = "";
        AppConstants.CountryCodeId = "";
        AppConstants.ReligionName = "";
        AppConstants.CasteName = "";
        AppConstants.MotherTongueName = "";
        AppConstants.CountryCodeName = "";

        ArrayList<beanProfileCreated> arrProfileCreated = null;
        ProfileCreatedAdapter profileCreatedAdapter = null;

        ArrayList<beanReligion> arrReligion = null;
        ReligionAdapter religionAdapter = null;

        ArrayList<beanCaste> arrCaste = null;
        CasteAdapter casteAdapter = null;

        ArrayList<beanMotherTongue> arrMotherTongue = null;
        MotherTongueAdapter motherTongueAdapter = null;

        ArrayList<beanCountries> arrCountry = null;
        CountryAdapter countryAdapter = null;

        ArrayList<beanCountryCode> arrCountryCode = null;
        CountryCodeAdapter countryCodeAdapter = null;


    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep1Activity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.go_back));
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              /*  SharedPreferences.Editor editor=prefUpdate.edit();
                editor.putString("signup_step","0");
                editor.commit();*/
                Intent intLogin = new Intent(SignUpStep1Activity.this, LoginActivity.class);
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
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean checkEmail(String email) {
        return AppConstants.email_pattern.matcher(email).matches();
    }


    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(SignUpStep1Activity.this, SlidingDrawer, R.anim.slide_right);
        //SlidingDrawer.startAnimation(AppConstants.inFromRightAnimation()) ;
        Slidingpage.setVisibility(View.VISIBLE);


    }

    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(SignUpStep1Activity.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu() {

        linProfileCreatedBy = (LinearLayout) findViewById(R.id.linProfileCreatedBy);
        linReligion = (LinearLayout) findViewById(R.id.linReligion);
        linCaste = (LinearLayout) findViewById(R.id.linCaste);
        linMotherTongue = (LinearLayout) findViewById(R.id.linMotherTongue);
        linCountry = (LinearLayout) findViewById(R.id.linCountry);
        linCountryCode = (LinearLayout) findViewById(R.id.linCountryCode);

        edtSearchProfileCreatedBy = (EditText) findViewById(R.id.edtSearchProfileCreatedBy);
        edtSearchReligion = (EditText) findViewById(R.id.edtSearchReligion);
        edtSearchCaste = (EditText) findViewById(R.id.edtSearchCaste);
        edtSearchMotherTongue = (EditText) findViewById(R.id.edtSearchMotherTongue);
        edtSearchCountry = (EditText) findViewById(R.id.edtSearchCountry);
        edtSearchCountryCode = (EditText) findViewById(R.id.edtSearchCountryCode);


        rvProfileCreatedBy = (RecyclerView) findViewById(R.id.rvProfileCreatedBy);
        rvReligion = (RecyclerView) findViewById(R.id.rvReligion);
        rvCaste = (RecyclerView) findViewById(R.id.rvCaste);
        rvMotherTongue = (RecyclerView) findViewById(R.id.rvMotherTongue);
        rvCountry = (RecyclerView) findViewById(R.id.rvCountry);
        rvCountryCode = (RecyclerView) findViewById(R.id.rvCountryCode);

        rvProfileCreatedBy.setLayoutManager(new LinearLayoutManager(this));
        rvProfileCreatedBy.setHasFixedSize(true);
        rvReligion.setLayoutManager(new LinearLayoutManager(this));
        rvReligion.setHasFixedSize(true);
        rvCaste.setLayoutManager(new LinearLayoutManager(this));
        rvCaste.setHasFixedSize(true);
        rvMotherTongue.setLayoutManager(new LinearLayoutManager(this));
        rvMotherTongue.setHasFixedSize(true);
        rvCountry.setLayoutManager(new LinearLayoutManager(this));
        rvCountry.setHasFixedSize(true);
        rvCountryCode.setLayoutManager(new LinearLayoutManager(this));
        rvCountryCode.setHasFixedSize(true);

       /* btnMenuClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GonelidingDrower();
            }
        });*/

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });


    }


    public void setDatess() {


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        Log.e("curruntyear", mYear + "");
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String curruntDate = mYear + "-" + mMonth + "-" + mDay + "";
        Log.e("date_TODAY", curruntDate);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(SignUpStep1Activity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                edtBirthdate.setText(dateDesc);

                Toast.makeText(SignUpStep1Activity.this, dateDesc, Toast.LENGTH_SHORT).show();
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
        pickerPopWin.showPopWin(SignUpStep1Activity.this);



        /*



        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                edtBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();*/
    }


    public void getProfileCreated() {
        arrProfileCreated = new ArrayList<beanProfileCreated>();

        arrProfileCreated.add(new beanProfileCreated("1", "Self"));
        arrProfileCreated.add(new beanProfileCreated("2", "Parents"));
        arrProfileCreated.add(new beanProfileCreated("3", "Guardian"));
        arrProfileCreated.add(new beanProfileCreated("4", "Friends"));
        arrProfileCreated.add(new beanProfileCreated("5", "Sibling"));
        arrProfileCreated.add(new beanProfileCreated("6", "Relative"));

        if (arrProfileCreated.size() > 0) {
            profileCreatedAdapter = new ProfileCreatedAdapter(SignUpStep1Activity.this, arrProfileCreated, SlidingDrawer, Slidingpage, edtProfileCreatedBy);
            rvProfileCreatedBy.setAdapter(profileCreatedAdapter);
        }

        edtSearchProfileCreatedBy.addTextChangedListener(new TextWatcher() {


            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrProfileCreated.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchProfileCreatedBy.getText().toString().toLowerCase(Locale.getDefault());
                    profileCreatedAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
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


    public void getCountryCode() {
        try {
            edtSearchCountryCode.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrCountryCode.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchCountryCode.getText().toString().toLowerCase(Locale.getDefault());
                        countryCodeAdapter.filter(text);
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


    private void getCountrysRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "country.php";
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
                Log.e("--Country --", "==" + Ressponce);
                progressBarSlider.setVisibility(View.GONE);
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

                            arrCountry.add(new beanCountries(CoID, CoName));

                        }

                        if (arrCountry.size() > 0) {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
                            countryAdapter = new CountryAdapter(SignUpStep1Activity.this, arrCountry, SlidingDrawer, Slidingpage, edtCountry);
                            rvCountry.setAdapter(countryAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {

                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getMotherToungeRequest();
                        getMotherTongue();
                    } else {
                        AppConstants.CheckConnection(SignUpStep1Activity.this);
                    }
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getMotherToungeRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "mother_tounge.php";
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
                Log.e("--mother_tounge --", "==" + Ressponce);
                progressBarSlider.setVisibility(View.GONE);
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

                            arrMotherTongue.add(new beanMotherTongue(mtongue_id, mtongue_name));

                        }

                        if (arrMotherTongue.size() > 0) {
                            Collections.sort(arrMotherTongue, new Comparator<beanMotherTongue>() {
                                @Override
                                public int compare(beanMotherTongue lhs, beanMotherTongue rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            motherTongueAdapter = new MotherTongueAdapter(SignUpStep1Activity.this, arrMotherTongue, SlidingDrawer, Slidingpage, edtMotherTongue);
                            rvMotherTongue.setAdapter(motherTongueAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getCountryCodeRequest();
                        getCountryCode();
                    } else {
                        AppConstants.CheckConnection(SignUpStep1Activity.this);
                    }

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getCountryCodeRequest() {
        arrCountryCode = new ArrayList<beanCountryCode>();

        String shortcuts[] = getResources().getStringArray(R.array.arr_country_code);


        for (int i = 0; i < shortcuts.length; i++) {
            arrCountryCode.add(new beanCountryCode("" + i, "" + shortcuts[i]));
        }

        if (arrCountryCode.size() > 0) {
            Collections.sort(arrCountryCode, new Comparator<beanCountryCode>() {
                @Override
                public int compare(beanCountryCode lhs, beanCountryCode rhs) {
                    return lhs.getCountry_code().compareTo(rhs.getCountry_code());
                }
            });
            countryCodeAdapter = new CountryCodeAdapter(SignUpStep1Activity.this, arrCountryCode, SlidingDrawer, Slidingpage, edtCountryCode);
            rvCountryCode.setAdapter(countryCodeAdapter);

        }


        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getReligionRequest();
            getReligions();
        } else {
            AppConstants.CheckConnection(SignUpStep1Activity.this);
        }
    }


    private void getReligionRequest() {
       /* progresDialog= new ProgressDialog(SignUpStep1Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "religion.php";
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
                //   progresDialog.dismiss();
                Log.e("--religion --", "==" + Ressponce);
                progressBarSlider.setVisibility(View.GONE);
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

                            arrReligion.add(new beanReligion(religionId, religionName));

                        }

                        if (arrReligion.size() > 0) {
                            Collections.sort(arrReligion, new Comparator<beanReligion>() {
                                @Override
                                public int compare(beanReligion lhs, beanReligion rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            religionAdapter = new ReligionAdapter(SignUpStep1Activity.this, arrReligion, SlidingDrawer, Slidingpage, edtReligion);
                            rvReligion.setAdapter(religionAdapter);

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


    private void getCastRequest(String strReligion) {
       /* progresDialog= new ProgressDialog(SignUpStep1Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("castprogress", "callvisible");
                progressBarSlider.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "cast.php";//?religion_id="+paramUsername;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion_id", paramUsername);

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
                Log.e("--cast --", "==" + Ressponce);
                progressBarSlider.setVisibility(View.GONE);
                try {
                    arrCaste = new ArrayList<beanCaste>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    // {"status":"0","message":"No Cast Data Found"}


                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String casteId = resItem.getString("caste_id");
                            String casteName = resItem.getString("caste_name");

                            arrCaste.add(new beanCaste(casteId, casteName));

                        }

                        if (arrCaste.size() > 0) {
                            Collections.sort(arrCaste, new Comparator<beanCaste>() {
                                @Override
                                public int compare(beanCaste lhs, beanCaste rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            casteAdapter = new CasteAdapter(SignUpStep1Activity.this, arrCaste, SlidingDrawer, Slidingpage, edtCaste);
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

    private void getRegistationSteps(String ProfileCreatedBy, String Gender1, String Firstname, String Lastname, String Birthdate,
                                     String ReligionId, String CasteId, String MotherTongueId, String CountryId,
                                     String CountryCode, String MobileNo, String EmailId/*,String Password*/) {
        /*progresDialog= new ProgressDialog(SignUpStep1Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarSlider.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                String paramProfileCreatedBy = params[0];
                String paramGender1 = params[1];
                String paramFirstname = params[2];
                String paramLastname = params[3];
                String paramBirthdate = params[4];
                String paramReligionId = params[5];
                String paramCasteId = params[6];
                String paramMotherTongueId = params[7];
                String paramCountryId = params[8];
                String paramCountryCode = params[9];
                String paramMobileNo = params[10];
                String paramEmailId = params[11];
                //String paramPassword = params[12];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "signup_step1.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair ProfileCreatedByPAir = new BasicNameValuePair("profile_creaded_for", paramProfileCreatedBy);
                BasicNameValuePair Gender1PAir = new BasicNameValuePair("gender", paramGender1);
                BasicNameValuePair FirstnamePAir = new BasicNameValuePair("firstname", paramFirstname);
                BasicNameValuePair LastnamePAir = new BasicNameValuePair("lastname", paramLastname);
                BasicNameValuePair BirthdatePAir = new BasicNameValuePair("birthdate", paramBirthdate);
                BasicNameValuePair ReligionIdPAir = new BasicNameValuePair("religion_id", paramReligionId);
                BasicNameValuePair CasteIdPAir = new BasicNameValuePair("cast_id", paramCasteId);
                BasicNameValuePair MotherTongueIdPAir = new BasicNameValuePair("mother_tongue_id", paramMotherTongueId);
                BasicNameValuePair CountryIdPAir = new BasicNameValuePair("country_id", paramCountryId);
                BasicNameValuePair CountryCodePAir = new BasicNameValuePair("country_code", paramCountryCode);
                BasicNameValuePair MobileNoPAir = new BasicNameValuePair("mobile_no", paramMobileNo);
                BasicNameValuePair EmailIdPAir = new BasicNameValuePair("email_id", paramEmailId);
                // BasicNameValuePair PasswordPAir = new BasicNameValuePair("password", paramPassword);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(ProfileCreatedByPAir);
                nameValuePairList.add(Gender1PAir);
                nameValuePairList.add(FirstnamePAir);
                nameValuePairList.add(LastnamePAir);
                nameValuePairList.add(BirthdatePAir);
                nameValuePairList.add(ReligionIdPAir);
                nameValuePairList.add(CasteIdPAir);
                nameValuePairList.add(MotherTongueIdPAir);
                nameValuePairList.add(CountryIdPAir);
                nameValuePairList.add(CountryCodePAir);
                nameValuePairList.add(MobileNoPAir);
                nameValuePairList.add(EmailIdPAir);
                // nameValuePairList.add(PasswordPAir);


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
                //  progresDialog.dismiss();
                Log.e("--cast --", "==" + Ressponce);
                progressBarSlider.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
//                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        SharedPreferences.Editor editor = prefUpdate.edit();

                        editor.putString("otp", responseObj.getString("otp"));
                        editor.putString("CountryId", AppConstants.CountryId);
                        editor.putString("CountryName", AppConstants.CountryName);
                        editor.putString("CountryCode", AppConstants.CountryCodeName);
                        editor.putString("signup_step", "1");
                        editor.putString("user_id_r", responseObj.getString("user_id"));
                        editor.putString(AppConstants.FIRST_NAME, edtFirstName.getText().toString());
                        editor.putString(AppConstants.LAST_NAME, edtLastName.getText().toString());
                        editor.putString(AppConstants.EMAIL_ID, edtEmailId.getText().toString());
                        editor.putString(AppConstants.MOBILE_NO, edtMobileNo.getText().toString());
                        editor.commit();


//                        Log.e("signup1",message);
                        //Toast.makeText(SignUpStep1Activity.this,""+message,Toast.LENGTH_LONG).show();

                        Intent intLogin = new Intent(SignUpStep1Activity.this, VerifyMobileNumberActivity.class);
                        startActivity(intLogin);
                        finish();

                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep1Activity.this);
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
                    Log.e("exaception", "" + e);
                } finally {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(ProfileCreatedBy, Gender1, Firstname, Lastname, Birthdate, ReligionId, CasteId,
                MotherTongueId, CountryId, CountryCode, MobileNo, EmailId/*,Password*/);
    }


}
