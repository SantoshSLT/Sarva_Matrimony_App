package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thegreentech.EditedProfileactivities.Part_EditBasic;
import com.thegreentech.EditedProfileactivities.Part_EditEducation;
import com.thegreentech.EditedProfileactivities.PArt_EditExptation;
import com.thegreentech.EditedProfileactivities.Part_EditLocation;
import com.thegreentech.EditedProfileactivities.EditProfileEducation;
import com.thegreentech.EditedProfileactivities.EditProfileAboutMe;
import com.thegreentech.EditedProfileactivities.EditProfileBasic;
import com.thegreentech.EditedProfileactivities.EditProfileFamilyStatus;
import com.thegreentech.EditedProfileactivities.EditProfileHabits;
import com.thegreentech.EditedProfileactivities.EditProfileHoroScop;
import com.thegreentech.EditedProfileactivities.EditProfileLocation;
import com.thegreentech.EditedProfileactivities.EditProfilePhysicalStatus;
import com.thegreentech.EditedProfileactivities.EditProfileReligion;
import com.thegreentech.EditedProfileactivities.Part_EditReligion;

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


public class MenuProfileEdit extends AppCompatActivity {

    FrameLayout btnPhotos;
    Toolbar toolbar;
    TextView txtHeaderTitle, tvPhotoCounter;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    ImageView imgProfileImage, btnPopup;
    ProgressBar progressBar1;
    SwipeRefreshLayout refresh;
    ProgressDialog progresDialog;
    ImageView ivBAck;

    LinearLayout llNoChild, llChildStatus;
    TextView tvNoOfChild, tvChildStatus;

    TextView tvMetriID, tvUserName, tvAge, tvheight, tvCaste, tvReligion, tvCity, tvCountry;
    //__Step 1__personal______//////
    //_________Step1 basic___
    TextView btnEditStaps1;
    TextView textUsername, textEmailId, textMobileNo, textGender, textBirthdate, textMaritalSts,
            textMotherTongue, textProfileCreatedBy;
//__Step 2_

    TextView btnEditAbout;
    TextView textAboutme;
    //religion
    TextView btnEditStapsreligion;
    TextView textReligion, textPerCast, textPerSubCast;
    //education
    TextView btnEditStapseducation;
    TextView textHeighestEducation, textAdditionalDegree, textOccupation, textEmployedIn, textAnnualIncome;
    //Family
    TextView btnEditStapFamilyStatus;
    TextView textFamilyStatus, textFamilyType, textFamilyValue, textFatherOccupation, textMotherOccupation,
            textNoOfBrothers, textNoOfMarriedBrothers, textNoOfSisters, textNoOfMarriedSisters;
    LinearLayout linNoOfMarriedBrother, linNoOfMarriedSisters;
    //Location
    TextView btnEditStapsLocation;
    TextView textCountry, textState, textCity;
    //Habbits
    TextView btnEditStapsHabits;
    TextView textDietHabite, textDrinking, textSmoking;
    //Physical
    TextView btnEditStapsPhysical;
    TextView textHeight, textWeight, textBodyType, textPhysicalStates;
    //Horoscope
    TextView btnEditStapsHoroscop;
    TextView textHaveDosh, textDoshType, textStar, textRassiMoonSign, textBirthtime, textBirthplace;
    LinearLayout linDoshType;

    //__Step 3___Partner_Perefrence
//Basic
    TextView btnBasicPref;
    TextView textPMaritalStatus, textPAge, textPHeight, textPDietHabite, textPSmoking, textPDrinking, textPPhysicalStates;
    //education
    TextView btnEducatonPrefre;
    TextView textPHeighestEducation, textPAnnualIncome, textPEmployedIn, textPOccupation;
    //Religion
    TextView btnReligionPrefr;
    TextView textPReligion, textPCaste, textPManglik, p_textDoshType, textPStar, textPMotherTongue;
    //Location
    TextView btnPArtLocation;
    TextView textPCountry, textPState, textPCity;
    //Ecxpatation
    TextView btnExpectation, textPartnerExpectation;


    SharedPreferences prefUpdate;
    String matri_id = "", matri_name = "";
    String strPhotoProtect = "", strMessage = "", strPhotoPassword;
    String profile_image = "";
    String mobileNo = "";
    String Maritalstatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(MenuProfileEdit.this);
        matri_id = prefUpdate.getString("matri_id", "");
        Log.e("matri_id", matri_id);
        profile_image = prefUpdate.getString("profile_image", "");

        init();
        Onclick();
        if (NetworkConnection.hasConnection(MenuProfileEdit.this)){
            getProfile(matri_id);

        }else
        {
            AppConstants.CheckConnection(MenuProfileEdit.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(MenuProfileEdit.this)){
            getProfile(matri_id);

        }else
        {
            AppConstants.CheckConnection(MenuProfileEdit.this);
        }
    }


    public void init() {

        txtHeaderTitle = (TextView) findViewById(R.id.txtHeaderTitle);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        btnPhotos = findViewById(R.id.btnPhotos);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        refresh = findViewById(R.id.refresh);
        tvPhotoCounter = findViewById(R.id.tvPhotoCounter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvMetriID = findViewById(R.id.tvMetriID);
        tvUserName = findViewById(R.id.tvUserName);

        tvMetriID.setText("(" + matri_id + ")");

        btnEditStaps1 = findViewById(R.id.btnEditStaps1);
        btnEditAbout = findViewById(R.id.btnEditStapsabout);
        btnEditStapsreligion = findViewById(R.id.btnEditStapsreligion);
        btnEditStapseducation = findViewById(R.id.btnEditStapseducation);
        btnEditStapFamilyStatus = findViewById(R.id.btnEditStapFamilyStatus);
        btnEditStapsLocation = findViewById(R.id.btnEditStapsLocation);
        btnEditStapsHabits = findViewById(R.id.btnEditStapsHabits);
        btnEditStapsPhysical = findViewById(R.id.btnEditStapsPhysical);
        btnEditStapsHoroscop = findViewById(R.id.btnEditStapsHoroscop);
        btnBasicPref = findViewById(R.id.btnBasicPref);
        btnEducatonPrefre = findViewById(R.id.btnEducatonPrefre);
        btnReligionPrefr = findViewById(R.id.btnReligionPrefr);
        btnPArtLocation = findViewById(R.id.btnPArtLocation);
        btnExpectation = findViewById(R.id.btnExpectation);


//___step 1____
        imgProfileImage = findViewById(R.id.imgProfileImage);
        textUsername = findViewById(R.id.textUsername);
        tvAge = findViewById(R.id.tvAge);
        tvheight = findViewById(R.id.tvheight);
        tvCaste = findViewById(R.id.tvCaste);
        tvReligion = findViewById(R.id.tvReligion);
        tvCity = findViewById(R.id.tvCity);
        tvCountry = findViewById(R.id.tvCountry);

        llNoChild = findViewById(R.id.llNoChild);
        llChildStatus = findViewById(R.id.llChildStatus);
        tvNoOfChild = findViewById(R.id.tvNoOfChild);
        tvChildStatus = findViewById(R.id.tvChildStatus);

        textProfileCreatedBy = findViewById(R.id.textProfileCreatedBy);
        textGender = findViewById(R.id.textGender);
        textBirthdate = findViewById(R.id.textBirthdate);
        textMotherTongue = findViewById(R.id.textMotherTongue);
        textEmailId = findViewById(R.id.textEmailId);
        textMobileNo = findViewById(R.id.textMobileNo);
        textMaritalSts = findViewById(R.id.textMaritalSts);

        textAboutme = findViewById(R.id.textAboutme);
        textReligion = findViewById(R.id.textReligion);
        textPerCast = findViewById(R.id.textPerCast);
        textPerSubCast = findViewById(R.id.textPerSubCast);

        textHeighestEducation = findViewById(R.id.textHeighestEducation);
        textAdditionalDegree = findViewById(R.id.textAdditionalDegree);
        textOccupation = findViewById(R.id.textOccupation);
        textEmployedIn = findViewById(R.id.textEmployedIn);
        textAnnualIncome = findViewById(R.id.textAnnualIncome);

        textFamilyStatus = findViewById(R.id.textFamilyStatus);
        textFamilyType = findViewById(R.id.textFamilyType);
        textFamilyValue = findViewById(R.id.textFamilyValue);
        textFatherOccupation = findViewById(R.id.textFatherOccupation);
        textMotherOccupation = findViewById(R.id.textMotherOccupation);
        textNoOfBrothers = findViewById(R.id.textNoOfBrothers);
        textNoOfMarriedBrothers = findViewById(R.id.textNoOfMarriedBrothers);
        textNoOfMarriedSisters = findViewById(R.id.textNoOfMarriedSisters);
        textNoOfSisters = findViewById(R.id.textNoOfSisters);
        linNoOfMarriedBrother = findViewById(R.id.linNoOfMarriedBrother);
        linNoOfMarriedSisters = findViewById(R.id.linNoOfMarriedSisters);

        textCountry = findViewById(R.id.textCountry);
        textState = findViewById(R.id.textState);
        textCity = findViewById(R.id.textCity);

        textDietHabite = findViewById(R.id.textDietHabite);
        textDrinking = findViewById(R.id.textDrinking);
        textSmoking = findViewById(R.id.textSmoking);
        // textLanguageKnown  = findViewById(R.id.textLanguageKnown);

        textHeight = findViewById(R.id.textHeight);
        textWeight = findViewById(R.id.textWeight);
        textBodyType = findViewById(R.id.textBodyType);
        textPhysicalStates = findViewById(R.id.textPhysicalStates);

        linDoshType = findViewById(R.id.linDoshType);
        textHaveDosh = findViewById(R.id.textHaveDosh);
        textDoshType = findViewById(R.id.textDoshType);
        textStar = findViewById(R.id.textStar);
        textRassiMoonSign = findViewById(R.id.textRassiMoonSign);
        textBirthtime = findViewById(R.id.textBirthtime);
        textBirthplace = findViewById(R.id.textBirthplace);

        textPMaritalStatus = findViewById(R.id.textPMaritalStatus);
        textPAge = findViewById(R.id.textPAge);
        textPHeight = findViewById(R.id.textPHeight);
        textPDietHabite = findViewById(R.id.textPDietHabite);
        textPSmoking = findViewById(R.id.textPSmoking);
        textPDrinking = findViewById(R.id.textPDrinking);
        textPPhysicalStates = findViewById(R.id.textPPhysicalStates);

        textPHeighestEducation = findViewById(R.id.textPHeighestEducation);
        textPAnnualIncome = findViewById(R.id.textPAnnualIncome);
        textPEmployedIn = findViewById(R.id.textPEmployedIn);
        textPOccupation = findViewById(R.id.textPOccupation);
        //  P_textEmployedIn = findViewById(R.id.P_textEmployedIn);

        textPReligion = findViewById(R.id.textPReligion);
        textPCaste = findViewById(R.id.textPCaste);
        textPManglik = findViewById(R.id.textPManglik);
        p_textDoshType = findViewById(R.id.p_textDoshType);
        //  P_textMoonSign=  findViewById(R.id.PtextMoonSign);
        textPStar = findViewById(R.id.textPStar);
        textPMotherTongue = findViewById(R.id.textPMotherTongue);

        textPCountry = findViewById(R.id.textPCountry);
        textPState = findViewById(R.id.textPState);
        textPCity = findViewById(R.id.textPCity);

        textPartnerExpectation = findViewById(R.id.textPartnerExpectation);

        if (!profile_image.equalsIgnoreCase("")) {

            Glide.with(MenuProfileEdit.this)
                    .load(profile_image)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                    .into(imgProfileImage);

        }


    }


    public void Onclick() {


        btnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, ViewImageActivity.class);
                intent.putExtra("MATRIiD", matri_id);
                intent.putExtra("me","mine");
                startActivity(intent);
                finish();
            }
        });

        btnEditStaps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileBasic.class);
                intent.putExtra("Step1EditProfile", "Edit");
                intent.putExtra("status", Maritalstatus);
                startActivity(intent);
            }
        });

        btnEditAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileAboutMe.class);
                startActivity(intent);
            }
        });
        btnEditStapsreligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuProfileEdit.this, EditProfileReligion.class);
                startActivity(intent);
            }
        });
        btnEditStapseducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileEducation.class);
                startActivity(intent);
            }
        });

        btnEditStapFamilyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuProfileEdit.this, EditProfileFamilyStatus.class);
                startActivity(intent);
            }
        });

        btnEditStapsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileLocation.class);
                startActivity(intent);
            }
        });
        btnEditStapsHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileHabits.class);
                startActivity(intent);
            }
        });

        btnEditStapsPhysical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfilePhysicalStatus.class);
                startActivity(intent);
            }
        });

        btnEditStapsHoroscop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, EditProfileHoroScop.class);
                startActivity(intent);
            }
        });

        btnBasicPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, Part_EditBasic.class);
                startActivity(intent);
            }
        });

        btnEducatonPrefre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, Part_EditEducation.class);
                startActivity(intent);
            }
        });

        btnReligionPrefr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, Part_EditReligion.class);
                startActivity(intent);
            }
        });
        btnPArtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, Part_EditLocation.class);
                startActivity(intent);
            }
        });

        btnExpectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProfileEdit.this, PArt_EditExptation.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MenuProfileEdit.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);

    }

    private void getProfile(String strMatriId) {
        final ProgressDialog progresDialog11= new ProgressDialog(MenuProfileEdit.this);
        progresDialog11.setCancelable(false);
        progresDialog11.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog11.setIndeterminate(true);
        progresDialog11.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                Log.e("matri_id", paramsMatriId);

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
                refresh.setRefreshing(false);
                Log.e("View Profile ", "==" + result);

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

                                tvAge.setText(resItem.getString("age") + ", ");
                                tvheight.setText(resItem.getString("height") + ", ");
                                tvCaste.setText(resItem.getString("caste_name") + ", ");
                                tvReligion.setText(resItem.getString("religion"));
                                tvCity.setText(resItem.getString("city_name") + ", ");
                                tvCountry.setText(resItem.getString("country_name") + ".");
                                tvPhotoCounter.setText(resItem.getString("photo_count"));

                                String userNname = resItem.getString("username");
                                String firstname = resItem.getString("firstname");
                                textUsername.setText(":  " + userNname);
                                tvUserName.setText(userNname);

                                String matri_id = resItem.getString("matri_id");
                                textEmailId.setText(":  " + resItem.getString("email"));
                                Maritalstatus = resItem.getString("m_status");
                                textMaritalSts.setText(":  " + Maritalstatus);

                                if (Maritalstatus.equalsIgnoreCase("Never Married")) {
                                    llNoChild.setVisibility(View.GONE);
                                    llChildStatus.setVisibility(View.GONE);

                                } else {
                                    llNoChild.setVisibility(View.VISIBLE);
                                    llChildStatus.setVisibility(View.VISIBLE);
                                    tvNoOfChild.setText(":  " + resItem.getString("tot_children"));
                                    tvChildStatus.setText(":  " + resItem.getString("status_children"));
                                }


                                textProfileCreatedBy.setText(":  " + resItem.getString("profileby"));

                                matri_name = resItem.getString("firstname") /*+ " " + resItem.getString("lastname")*/;


                                txtHeaderTitle.setText("" + matri_id);

                                textGender.setText(":  " + resItem.getString("gender"));
                                textBirthdate.setText(":  " + resItem.getString("birthdate"));
                                textBirthtime.setText(":  " + resItem.getString("birthtime"));
                                textBirthplace.setText(":  " + resItem.getString("birthplace"));

                                textHeighestEducation.setText(":  " + resItem.getString("edu_detail"));
                                textAnnualIncome.setText(":  " + resItem.getString("income"));
                                textOccupation.setText(":  " + resItem.getString("occupation"));

                                textEmployedIn.setText(":  " + resItem.getString("emp_in"));
                                textAdditionalDegree.setText(":  " + resItem.getString("addition_detail")); ///////////

                                String subcaste = resItem.getString("subcaste");
                                textPerSubCast.setText(":  " + subcaste);

                                textStar.setText(":  " + resItem.getString("star"));
                                Log.d("ravi", "star is = " + resItem.getString("star"));
                                textRassiMoonSign.setText(":  " + resItem.getString("moonsign"));
                                Log.d("ravi", "rassi is = " + resItem.getString("moonsign"));


                                textMotherTongue.setText(":  " + resItem.getString("m_tongue"));
                                textHeight.setText(":  " + resItem.getString("height"));
                                textWeight.setText(":  " + resItem.getString("weight"));

                                String complexion = resItem.getString("complexion");
                                textPhysicalStates.setText(":  " + resItem.getString("physicalStatus"));
                                textBodyType.setText(":  " + resItem.getString("bodytype"));
                                textDietHabite.setText(":  " + resItem.getString("diet"));
                                textSmoking.setText(":  " + resItem.getString("smoke"));
                                textDrinking.setText(":  " + resItem.getString("drink"));
                                textHaveDosh.setText(":  " + resItem.getString("dosh"));
                                if (resItem.getString("dosh").equalsIgnoreCase("yes")) {
                                    linDoshType.setVisibility(View.VISIBLE);
                                    textDoshType.setVisibility(View.VISIBLE);
                                    textDoshType.setText("    :  " + resItem.getString("manglik"));
                                } else {
                                    linDoshType.setVisibility(View.GONE);
                                }


                                mobileNo = resItem.getString("mobile");
                                textMobileNo.setText(":  " + mobileNo);
                                textFatherOccupation.setText(":  " + resItem.getString("father_occupation"));
                                textMotherOccupation.setText(":  " + resItem.getString("mother_occupation"));
                                ;
                                textAboutme.setText(resItem.getString("profile_text"));
                                textCountry.setText(":  " + resItem.getString("country_name"));
                                textState.setText(":  " + resItem.getString("state_name"));
                                textCity.setText(":  " + resItem.getString("city_name"));
                                textReligion.setText(":  " + resItem.getString("religion_name"));
                                textPerCast.setText(":  " + resItem.getString("caste_name"));
                                textFamilyType.setText(":  " + resItem.getString("family_type"));
                                textFamilyStatus.setText(":  " + resItem.getString("family_status"));
                                textFamilyValue.setText(":  " + resItem.getString("family_value"));
                                textNoOfBrothers.setText(":  " + resItem.getString("no_of_brothers"));
                                textNoOfSisters.setText(":  " + resItem.getString("no_of_sisters"));

                                if (!resItem.getString("no_of_brothers").equalsIgnoreCase("") && !resItem.getString("no_of_brothers").equalsIgnoreCase("0")) {
                                    linNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    textNoOfMarriedBrothers.setText("   :  " + resItem.getString("no_marri_brother"));
                                } else {
                                    linNoOfMarriedBrother.setVisibility(View.GONE);
                                }

                                if (!resItem.getString("no_of_sisters").equalsIgnoreCase("") && !resItem.getString("no_of_sisters").equalsIgnoreCase("0")) {
                                    linNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    textNoOfMarriedSisters.setText("   :  " + resItem.getString("no_marri_sister"));
                                } else {
                                    linNoOfMarriedSisters.setVisibility(View.GONE);
                                }


                                String part_to_age = resItem.getString("part_age");
                                String part_income = resItem.getString("part_income");
                                String part_height_to = resItem.getString("part_height");
                                String part_complexation = resItem.getString("part_expect");
                                String part_mtongue = resItem.getString("part_mtongue");
                                String part_religion = resItem.getString("part_religion");
                                String part_caste = resItem.getString("part_caste");
                                String part_star = resItem.getString("part_star");
                                String part_manglik = resItem.getString("part_manglik");
                                String part_edu = resItem.getString("part_edu");
                                String part_occu = resItem.getString("part_ocp_name");
                                String part_state = resItem.getString("part_state");
                                String part_city = resItem.getString("part_city");
                                String part_country_living = resItem.getString("part_country_living");
                                String part_smoke = resItem.getString("part_smoke");
                                String part_diet = resItem.getString("part_diet");
                                String part_drink = resItem.getString("part_drink");
                                String part_physical = resItem.getString("part_physical");
                                String part_emp_in = resItem.getString("part_emp_in");
                                String photo1 = resItem.getString("photo1");
                                String part_dosh = resItem.getString("part_dosh");

                                textPManglik.setText(":  " + part_dosh);
                                p_textDoshType.setText(":  " + part_manglik);

                                textPMaritalStatus.setText(":  " + resItem.getString("part_m_status"));
                                textPAge.setText(":  " + part_to_age);
                                textPHeight.setText(":  " + part_height_to);
                                textPDietHabite.setText(":  " + part_diet);
                                textPSmoking.setText(":  " + part_smoke);
                                textPDrinking.setText(":  " + part_drink);
                                textPPhysicalStates.setText(":  " + part_physical);
                                textPHeighestEducation.setText(":  " + part_edu);
                                textPAnnualIncome.setText(":  " + part_income);
                                textPEmployedIn.setText(":  " + part_emp_in);
                                textPOccupation.setText(":  " + part_occu);
                                textPReligion.setText(":  " + part_religion);
                                textPCaste.setText(":  " + part_caste);

                                textPStar.setText(":  " + part_star);
                                textPMotherTongue.setText(":  " + part_mtongue);
                                textPCountry.setText(":  " + part_country_living);
                                textPState.setText(":  " + part_state);
                                textPCity.setText(":  " + part_city);
                                textPartnerExpectation.setText(part_complexation);

                                if (!photo1.equalsIgnoreCase("")) {
                                    Picasso.with(MenuProfileEdit.this)
                                            .load(photo1)
                                            //.fit()
                                            .error(R.drawable.ic_my_profile)
                                            .into(imgProfileImage, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                }


                                String user_name = resItem.getString("username");
                                String Gender = resItem.getString("gender");
                                SharedPreferences.Editor editor = prefUpdate.edit();
                                editor.putString("username", user_name);
                                editor.putString("gender", Gender);
                                editor.commit();
                            }


                        }


                    } else {
                        progresDialog.dismiss();
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuProfileEdit.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(MenuProfileEdit.this, "msgError" + msgError, Toast.LENGTH_SHORT).show();

                    }

                    progresDialog11.dismiss();
                } catch (Exception e) {
                    progresDialog11.dismiss();
                    Toast.makeText(MenuProfileEdit.this, "exception" + e.
                            getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("print ERRROR", e.getMessage());
                }
                progresDialog11.dismiss();
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

}

