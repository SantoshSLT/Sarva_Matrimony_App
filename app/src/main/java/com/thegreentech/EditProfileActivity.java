package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thegreentech.EditedProfileactivities.EditProfileBasic;

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


public class EditProfileActivity extends AppCompatActivity
{
    Toolbar toolbar;
    TextView txtHeaderTitle;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    ImageView imgProfileImage,btnPopup;
    ProgressBar progressBar1;
    SwipeRefreshLayout refresh;

    Button btnShortlist,btnContact,btnMessage;


    //step 1
    TextView btnEditStaps1;
    TextView textUsername,textProfileCreatedBy,textGender,/*textFirstname,textLastname,*/textBirthdate,textReligion,textCaste,
            textMotherTongue,textCountry,textEmailId;
    //step 2
    TextView  /*btnEditStaps21,*/btnEditStaps22,btnEditStaps23,btnEditStaps24,btnEditStaps25;
    TextView textMaritalStatus,textWillingToMarry,textState,textCity,textHeight,
            textWeight,textBodyType,textPhysicalStates,textHeighestEducation,textAdditionalDegree,textOccupation,textEmployedIn,
            textAnnualIncome,textDietHabite,textDrinking,textSmoking,textHaveDosh,textDoshType,textStar,textRassiMoonSign,
            textBirthtime,textBirthplace,textFamilyStatus,textFamilyType,textFamilyValue,textFatherOccupation,textMotherOccupation,
            textNoOfBrothers,textNoOfMarriedBrothers,textNoOfSisters,textNoOfMarriedSisters,textAbout;
            ;

    //step 3
    TextView  btnEditStaps31,btnEditStaps32,btnReligionPrefr,btnEditStaps34,btnEditStaps35;
    ImageView imgUserPhotos,imgPartnerPhotos;
    TextView textMessage;
    TextView textPMaritalStatus,textPAge,textPHeight,textPDietHabite,textPSmoking,textPDrinking,textPPhysicalStates,textPHeighestEducation,
            textPAnnualIncome,textPEmployedIn,textPOccupation,textPReligion,textPCaste,textPManglik,textPStar,textPMotherTongue,
            textPCountery,textPState,textPCity,textPartnerExpectation;
    LinearLayout linDoshType,linNoOfMarriedBrother,linNoOfMarriedSisters;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id="",matri_name="";

    String mobileNo="";
    String FragType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
        matri_id=prefUpdate.getString("matri_id","");
        FragType = getIntent().getStringExtra("EditProfile");




        txtHeaderTitle = (TextView) findViewById(R.id.txtHeaderTitle );
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        Log.d("ravi",matri_name);

        progressBar1= (ProgressBar) findViewById(R.id.progressBar1);
        imgProfileImage= (ImageView) findViewById(R.id.imgProfileImage);
        btnPopup= (ImageView) findViewById(R.id.btnPopup);
        refresh = findViewById(R.id.refresh);

        btnPopup.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dynamicToolbarColor();
        toolbarTextAppernce();


        btnPopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Context wrapper = new ContextThemeWrapper(EditProfileActivity.this, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, view);


                //PopupMenu popup = new PopupMenu(EditProfileActivity.this, btnPopup);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_profile_menu_block, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if(item.getTitle().equals("Block"))
                        {
                            Toast.makeText(EditProfileActivity.this,"Test",Toast.LENGTH_LONG).show();

                        } else if(item.getTitle().equals("Report Abuse"))
                        {
                            Toast.makeText(EditProfileActivity.this,"Comming soon",Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {

                getProfileData(matri_id);
            }
        });

        setFindViewBy();

        getProfileData(matri_id);
        //collapsingToolbarLayout.setTitle(matri_name);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void dynamicToolbarColor()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photos2);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener()
        {
            @Override
            public void onGenerated(Palette palette)
            {
                collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }

    private void toolbarTextAppernce()
    {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }


    public void setFindViewBy()
    {
        btnShortlist= (Button) findViewById(R.id.btnShortlist);
        btnContact= (Button) findViewById(R.id.btnContact);
        btnMessage= (Button) findViewById(R.id.btnMessage);

        btnEditStaps1=  findViewById(R.id.btnEditStaps1);
       // btnEditStaps21= findViewById(R.id.btnEditStaps21);
        btnEditStaps22=  findViewById(R.id.btnEditStapseducation);
        btnEditStaps23=findViewById(R.id.btnEditStapsHabits);
        btnEditStaps24=  findViewById(R.id.btnEditStapFamilyStatus);
       // btnEditStaps25= findViewById(R.id.btnEditStaps25);
        btnEditStaps31= findViewById(R.id.btnBasicPref);
        btnEditStaps32= findViewById(R.id.btnEducatonPrefre);
        btnReligionPrefr= findViewById(R.id.btnReligionPrefr);
        btnEditStaps34= findViewById(R.id.btnPArtLocation);
        btnEditStaps35= findViewById(R.id.btnExpectation);


        //step 1
        textUsername  = findViewById(R.id.textUsername);
        textProfileCreatedBy= (TextView) findViewById(R.id.textProfileCreatedBy);
        textGender= (TextView) findViewById(R.id.textGender);
       // textFirstname= (TextView) findViewById(R.id.textFirstname);
       // textLastname= (TextView) findViewById(R.id.textLastname);

        textReligion= (TextView) findViewById(R.id.textReligion);
        textCaste= (TextView) findViewById(R.id.textCaste);
        textMotherTongue= (TextView) findViewById(R.id.textMotherTongue);
        textCountry= (TextView) findViewById(R.id.textCountry);
        textEmailId= (TextView) findViewById(R.id.textEmailId);

        //step 2
        textMaritalStatus= (TextView) findViewById(R.id.textMaritalStatus);
        textWillingToMarry= (TextView) findViewById(R.id.textWillingToMarry);
        textState= (TextView) findViewById(R.id.textState);
        textCity= (TextView) findViewById(R.id.textCity);
        textHeight= (TextView) findViewById(R.id.textHeight);
        textWeight= (TextView) findViewById(R.id.textWeight);
        textBodyType= (TextView) findViewById(R.id.textBodyType);
        textPhysicalStates= (TextView) findViewById(R.id.textPhysicalStates);
        textHeighestEducation= (TextView) findViewById(R.id.textHeighestEducation);
        textAdditionalDegree= (TextView) findViewById(R.id.textAdditionalDegree);
        textOccupation= (TextView) findViewById(R.id.textOccupation);
        textEmployedIn= (TextView) findViewById(R.id.textEmployedIn);
        textAnnualIncome= (TextView) findViewById(R.id.textAnnualIncome);
        textDietHabite= (TextView) findViewById(R.id.textDietHabite);
        textDrinking= (TextView) findViewById(R.id.textDrinking);
        textSmoking= (TextView) findViewById(R.id.textSmoking);
        textHaveDosh= (TextView) findViewById(R.id.textHaveDosh);
        textDoshType= (TextView) findViewById(R.id.textDoshType);
        textStar= (TextView) findViewById(R.id.textStar);
        textRassiMoonSign= (TextView) findViewById(R.id.textRassiMoonSign);
        textBirthtime= (TextView) findViewById(R.id.textBirthtime);
        textBirthplace= (TextView) findViewById(R.id.textBirthplace);
        textFamilyStatus= (TextView) findViewById(R.id.textFamilyStatus);
        textFamilyType= (TextView) findViewById(R.id.textFamilyType);
        textFamilyValue= (TextView) findViewById(R.id.textFamilyValue);
        textFatherOccupation= (TextView) findViewById(R.id.textFatherOccupation);
        textMotherOccupation= (TextView) findViewById(R.id.textMotherOccupation);
        textNoOfBrothers= (TextView) findViewById(R.id.textNoOfBrothers);
        textNoOfMarriedBrothers= (TextView) findViewById(R.id.textNoOfMarriedBrothers);
        textNoOfSisters= (TextView) findViewById(R.id.textNoOfSisters);
        textNoOfMarriedSisters= (TextView) findViewById(R.id.textNoOfMarriedSisters);
        textAbout= (TextView) findViewById(R.id.textAbout);

        linDoshType= (LinearLayout) findViewById(R.id.linDoshType);
        linNoOfMarriedBrother= (LinearLayout) findViewById(R.id.linNoOfMarriedBrother);
        linNoOfMarriedSisters= (LinearLayout) findViewById(R.id.linNoOfMarriedSisters);

        //step 3
        imgUserPhotos= (ImageView) findViewById(R.id.imgUserPhotos);
        imgPartnerPhotos= (ImageView) findViewById(R.id.imgPartnerPhotos);
        textMessage= (TextView) findViewById(R.id.textMessage);
        textPMaritalStatus= (TextView) findViewById(R.id.textPMaritalStatus);
        textPAge= (TextView) findViewById(R.id.textPAge);
        textPHeight= (TextView) findViewById(R.id.textPHeight);
        textPDietHabite= (TextView) findViewById(R.id.textPDietHabite);
        textPSmoking= (TextView) findViewById(R.id.textPSmoking);
        textPDrinking= (TextView) findViewById(R.id.textPDrinking);
        textPPhysicalStates= (TextView) findViewById(R.id.textPPhysicalStates);
        textPHeighestEducation= (TextView) findViewById(R.id.textPHeighestEducation);
        textPAnnualIncome= (TextView) findViewById(R.id.textPAnnualIncome);
        textPEmployedIn= (TextView) findViewById(R.id.textPEmployedIn);
        textPOccupation= (TextView) findViewById(R.id.textPOccupation);
        textPReligion= (TextView) findViewById(R.id.textPReligion);
        textPCaste= (TextView) findViewById(R.id.textPCaste);
        textPManglik= (TextView) findViewById(R.id.textPManglik);
        textPStar= (TextView) findViewById(R.id.textPStar);
        textPMotherTongue= (TextView) findViewById(R.id.textPMotherTongue);
        textPCountery= (TextView) findViewById(R.id.textPCountery);
        textPState= (TextView) findViewById(R.id.textPState);
        textPCity= (TextView) findViewById(R.id.textPCity);
        textPartnerExpectation= (TextView) findViewById(R.id.textPartnerExpectation);


        if (FragType.equalsIgnoreCase("View"))
        {
            btnEditStaps1.setVisibility(View.GONE);
            //btnEditStaps21.setVisibility(View.GONE);
            btnEditStaps22.setVisibility(View.GONE);
            btnEditStaps23.setVisibility(View.GONE);
            btnEditStaps24.setVisibility(View.GONE);
            btnEditStaps25.setVisibility(View.GONE);
            btnEditStaps31.setVisibility(View.GONE);
            btnEditStaps32.setVisibility(View.GONE);
            btnReligionPrefr.setVisibility(View.GONE);
            btnEditStaps34.setVisibility(View.GONE);
            btnEditStaps35.setVisibility(View.GONE);


        }

        btnShortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!mobileNo.equalsIgnoreCase(""))
                {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+Uri.encode(mobileNo.trim())));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);

                }else
                {
                    Toast.makeText(EditProfileActivity.this,"Mobile no not available",Toast.LENGTH_LONG).show();
                }
            }
        });


        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEditStaps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileBasic.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
                //finish();
            }
        });

      /*  btnEditStaps21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep2Activity.class)*//*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*//*);

            }
        });*/
        btnEditStaps22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep2Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep2Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep2Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep2Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });

        btnEditStaps31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep3Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep3Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnReligionPrefr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep3Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep3Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });
        btnEditStaps35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileStep3Activity.class)/*.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
            }
        });


    }


  private void getProfileData(String strMatriId)
    {
        progresDialog= new ProgressDialog(EditProfileActivity.this);
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

                refresh.setRefreshing(false);
                Log.e("View Profile ", "=="+result);

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

                                String user_id=resItem.getString("user_id");
                                String matri_id=resItem.getString("matri_id");
                                textEmailId.setText(":  "+resItem.getString("email"));
                                textMaritalStatus.setText(":  "+resItem.getString("m_status"));
                                textProfileCreatedBy.setText(":  "+resItem.getString("profileby"));
                                textUsername.setText(":  "+resItem.getString("username"));


                               // textFirstname.setText(":  "+resItem.getString("firstname"));
                              //  textLastname.setText(":  "+resItem.getString("lastname"));
                                matri_name = resItem.getString("firstname") /*+ " " + resItem.getString("lastname")*/;
                                //Log.d("ravi","api ="+resItem.getString("firstname"));
                                //Log.d("ravi","api2 ="+matri_name);

                                txtHeaderTitle.setText(""+matri_id);
                                collapsingToolbarLayout.setTitle(resItem.getString("firstname")+" "+resItem.getString("lastname"));

                                textGender.setText(":  "+resItem.getString("gender"));
                                textBirthdate.setText(":  "+resItem.getString("birthdate"));
                                textBirthtime.setText(":  "+resItem.getString("birthtime"));
                                textBirthplace.setText(":  "+resItem.getString("birthplace"));
                                //textNoOfChiledren.setText(":  "+resItem.getString("tot_children"));
                                //textChildrenLiving.setText(":  "+"No");

                                textHeighestEducation.setText(":  "+resItem.getString("edu_detail"));
                                textAnnualIncome.setText(":  "+resItem.getString("income"));
                                textOccupation.setText(":  "+resItem.getString("occupation"));

                                textEmployedIn.setText(":  "+resItem.getString("emp_in"));
                                textAdditionalDegree.setText(":  "+resItem.getString("addition_detail")); ///////////
                                String religion_id=resItem.getString("religion");
                               // String cast_id=resItem.getString("caste");
                                //String subcaste=resItem.getString("subcaste");
                                textFamilyType.setText(":  "+resItem.getString("gothra"));///////////////
                                textStar.setText(":  "+resItem.getString("star"));
                                Log.d("ravi","star is = "+resItem.getString("star"));
                                textRassiMoonSign.setText(":  "+resItem.getString("moonsign"));
                                Log.d("ravi","rassi is = "+resItem.getString("moonsign"));
                                //String horoscope=resItem.getString("horoscope");
                                String manglik=resItem.getString("manglik");
                                textMotherTongue.setText(":  "+resItem.getString("m_tongue"));
                                textWillingToMarry.setText(":  "+resItem.getString("will_to_mary_caste"));
                                textHeight.setText(":  "+resItem.getString("height"));
                                textWeight.setText(":  "+resItem.getString("weight")+"kg");
                                String b_group=resItem.getString("b_group");
                                String complexion=resItem.getString("complexion");
                                textPhysicalStates.setText(":  "+resItem.getString("physicalStatus"));
                                textBodyType.setText(":  "+resItem.getString("bodytype"));
                                textDietHabite.setText(":  "+resItem.getString("diet"));
                                textSmoking.setText(":  "+resItem.getString("smoke"));
                                textDrinking.setText(":  "+resItem.getString("drink"));
                                textHaveDosh.setText(":  "+resItem.getString("dosh"));
                                if(resItem.getString("dosh").equalsIgnoreCase("yes"))
                                {
                                    linDoshType.setVisibility(View.VISIBLE);
                                    textDoshType.setText("   : "+resItem.getString("manglik"));
                                }else
                                {
                                    linDoshType.setVisibility(View.GONE);
                                }

                                String address=resItem.getString("address");
                                String countery_id =resItem.getString("country_id");
                                String state_id=resItem.getString("state_id");
                                String city_id=resItem.getString("city");
                                String phone=resItem.getString("phone");
                                mobileNo=resItem.getString("mobile");
                                String residence=resItem.getString("residence");
                                String father_name=resItem.getString("father_name");
                                String mother_name=resItem.getString("mother_name");
                                textFatherOccupation.setText(":  "+resItem.getString("father_occupation"));
                                textMotherOccupation.setText(":  "+resItem.getString("mother_occupation"));;
                                textAbout.setText(resItem.getString("profile_text"));
                                textCountry.setText(":  "+resItem.getString("country_name"));
                                textState.setText(":  "+resItem.getString("state_name"));
                                textCity.setText(":  "+resItem.getString("city_name"));
                                textReligion.setText(":  "+resItem.getString("religion_name"));
                                textCaste.setText(":  "+resItem.getString("caste_name"));
                                textFamilyType.setText(":  "+resItem.getString("family_type"));
                                textFamilyStatus.setText(":  "+resItem.getString("family_status"));
                                textFamilyValue.setText(":  "+resItem.getString("family_value"));
                                textNoOfBrothers.setText(":  "+resItem.getString("no_of_brothers"));
                                textNoOfSisters.setText(":  "+resItem.getString("no_of_sisters"));

                                if(! resItem.getString("no_of_brothers").equalsIgnoreCase("") && !resItem.getString("no_of_brothers").equalsIgnoreCase("0"))
                                {
                                    linNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    textNoOfMarriedBrothers.setText("   : "+resItem.getString("no_marri_brother"));
                                }else
                                {
                                    linNoOfMarriedBrother.setVisibility(View.GONE);
                                }

                                if(! resItem.getString("no_of_sisters").equalsIgnoreCase("") && !resItem.getString("no_of_sisters").equalsIgnoreCase("0"))
                                {
                                    linNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    textNoOfMarriedSisters.setText("   : "+resItem.getString("no_marri_sister"));
                                }else
                                {
                                    linNoOfMarriedSisters.setVisibility(View.GONE);
                                }


                                String part_frm_age=resItem.getString("part_frm_age");
                                String part_to_age=resItem.getString("part_to_age");
                                String part_have_child=resItem.getString("part_have_child");
                                String part_income=resItem.getString("part_income");
                                String part_expect=resItem.getString("part_expect");
                                String part_height=resItem.getString("part_height");
                                String part_height_to=resItem.getString("part_height_to");
                                String part_complexation=resItem.getString("part_expect");
                                String part_mtongue=resItem.getString("part_mtongue");
                                String part_religion=resItem.getString("part_religion");
                                String part_caste=resItem.getString("part_caste");
                                String part_subcaste=resItem.getString("part_subcaste");
                                String sub_caste=resItem.getString("sub_caste");
                                String part_star=resItem.getString("part_star");
                                String part_rasi=resItem.getString("part_rasi");
                                String part_manglik=resItem.getString("part_manglik");
                                String part_edu=resItem.getString("part_edu");
                                String part_occu=resItem.getString("part_ocp_name");
                                String part_state=resItem.getString("part_state");
                                String part_city=resItem.getString("part_city");
                                String part_country_living=resItem.getString("part_country_living");
                                String part_resi_status=resItem.getString("part_resi_status");
                                String part_smoke=resItem.getString("part_smoke");
                                String part_diet=resItem.getString("part_diet");
                                String part_drink=resItem.getString("part_drink");
                                String part_physical=resItem.getString("part_physical");
                                String part_emp_in=resItem.getString("part_emp_in");
                                String hor_photo=resItem.getString("hor_photo");

                                String photo1=resItem.getString("photo1");


                                textPMaritalStatus.setText(":  "+resItem.getString("m_status"));
                                textPAge.setText(":  "+part_to_age);
                                textPHeight.setText(":  "+part_height_to);
                                textPDietHabite.setText(":  "+part_diet);
                                textPSmoking.setText(":  "+part_smoke);
                                textPDrinking.setText(":  "+part_drink);
                                textPPhysicalStates.setText(":  "+part_physical);
                                textPHeighestEducation.setText(":  "+part_edu);
                                textPAnnualIncome.setText(":  "+part_income);
                                textPEmployedIn.setText(":  "+part_emp_in);
                                textPOccupation.setText(":  "+part_occu);
                                textPReligion.setText(":  "+part_religion);
                                textPCaste.setText(":  "+part_caste);
                                textPManglik.setText(":  "+part_manglik);
                                textPStar.setText(":  "+part_star);
                                textPMotherTongue.setText(":  "+part_mtongue);
                                textPCountery.setText(":  "+part_country_living);
                                textPState.setText(":  "+part_state);
                                textPCity.setText(":  "+part_city);
                                textPartnerExpectation.setText(part_complexation);


                                if(!photo1.equalsIgnoreCase(""))
                                {
                                    progressBar1.setVisibility(View.VISIBLE);
                                    Picasso.with(EditProfileActivity.this)
                                    .load(photo1)
                                    //.fit()
                                     .error(R.drawable.ic_profile)
                                    .into(imgProfileImage, new Callback() {
                                        @Override
                                        public void onSuccess()
                                        {
                                            progressBar1.setVisibility(View.GONE);
                                        }
                                        @Override
                                        public void onError() {
                                            progressBar1.setVisibility(View.GONE);
                                        }
                                    });
                                }



                            }

                            SharedPreferences.Editor editor=prefUpdate.edit();
                            editor.putString("username",responseData.getString("username"));
                            editor.putString("gender",responseData.getString("gender"));
                            editor.commit();

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


}
