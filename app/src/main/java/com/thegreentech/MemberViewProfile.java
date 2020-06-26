package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adepters.SearchResultAdapter;
import Adepters.UserRecentDataAdapter;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AppConstants;
import utills.CircleTransform;
import utills.NetworkConnection;

import static utills.AppConstants.sendPushNotification;

public class MemberViewProfile extends AppCompatActivity {


    FrameLayout btnPhotos;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout = null;
    ImageView imgProfileImage;
    ProgressBar progressBar1;
    SwipeRefreshLayout refresh;
    ProgressDialog progresDialog;
    ImageView ivBAck;
    TextView txtHeaderTitle, tvBlock, tvPhotoCounter, tvInterest, tvPhotoRequest, tvMessage;
    LinearLayout llInterest;
    public Button btnSendInterest,btnRemaind;
    ImageView ivInterest;
    ScaleGestureDetector scaleGestureDetector;
    Matrix matrix = new Matrix();
    String is_blocked;
    ImageView imgUserPhotos, imgPartnerPhotos;
    RelativeLayout relZoomImageView;
    ImageView imgZoomProfilePicture, imgzoomViewClose;


    LinearLayout llNoChild, llChildStatus;
    TextView tvNoOfChild, tvChildStatus;

    TextView tvMetriID, tvUserName, tvAge, tvheight, tvCaste, tvReligion, tvCity, tvCountry;
    TextView textMUsername;
    TextView textUsername, textGender, textMaritalSts,
            textMotherTongue, textProfileCreatedBy;

    TextView textAboutme;

    TextView textReligion, textPerCast, textPerSubCast;

    TextView textHeighestEducation, textAdditionalDegree, textOccupation, textEmployedIn, textAnnualIncome;

    TextView textFamilyStatus, textFamilyType, textFamilyValue, textFatherOccupation, textMotherOccupation,
            textNoOfBrothers, textNoOfMarriedBrothers, textNoOfSisters, textNoOfMarriedSisters;
    LinearLayout linNoOfMarriedBrother, linNoOfMarriedSisters;

    TextView textCountry, textState, textCity;

    TextView textDietHabite, textDrinking, textSmoking;

    TextView textHeight, textWeight, textBodyType, textPhysicalStates;

    TextView textHaveDosh, textDoshType, textStar, textRassiMoonSign, textBirthtime, textBirthplace;
    LinearLayout linDoshType;

    TextView textPMaritalStatus, textPAge, textPHeight, textPDietHabite, textPSmoking, textPDrinking, textPPhysicalStates;

    TextView textPHeighestEducation, textPAnnualIncome, textPEmployedIn, textPOccupation;

    TextView textPReligion, textPCaste, textPManglik, p_textDoshType, textPStar, textPMotherTongue;

    TextView textPCountry, textPState, textPCity;
    TextView textPartnerExpectation;

    TextView btnCon, btnShort;
    ImageView iv_star, iv_call;
    LinearLayout llShort, llContact, llMessage;

    SharedPreferences prefUpdate;

    String strPhotoProtect = "", strMessage = "", strPhotoPassword;
    public static int TotalContacts = 0, UsedContact = 0;
    public static String matri_id, login_matri_id, gender, is_shortlist, strUserImage;
    String strMatId = "", strUsename = "", strEmail = "", strCountry = "", strState = "",
            strCity = "", strBithdate = "", strMobileNumber = "",strCountrycode="",strbirthTime="";
    String profile_image = "";
    String isMessageViewd = "";
    String mobileNo = "",  country_code ="";
    String Maritalstatus = "";
    String is_interest="",RequestType="";
    String tokans ="";
    String Photo_Pass= "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // setSupportActionBar(toolbar);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(MemberViewProfile.this);
        login_matri_id = prefUpdate.getString("matri_id", "");
        strUserImage = prefUpdate.getString("profile_image", "");
        gender = prefUpdate.getString("gender", "");

        init();
        Onclick();
       /* refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberProfile(login_matri_id, matri_id);
            }
        });*/

        if (NetworkConnection.hasConnection(MemberViewProfile.this)){
            Log.e("getMemberData",""+login_matri_id+" "+matri_id);
            getMemberProfile(login_matri_id, matri_id);

        }else
        {
            AppConstants.CheckConnection(MemberViewProfile.this);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();


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
        txtHeaderTitle.setText("" + matri_id);

        tvMetriID.setText("(" + matri_id + ")");
/////
        tvBlock = findViewById(R.id.tvBlock);
        tvInterest = findViewById(R.id.tvInterest);
        tvPhotoRequest = findViewById(R.id.tvPhotoRequest);
        llInterest = findViewById(R.id.llInterest);
       // btnSendInterest = findViewById(R.id.btnSendInterest);
       // btnRemaind = findViewById(R.id.btnRemaind);
        ivInterest = findViewById(R.id.ivInterest);
        imgProfileImage = findViewById(R.id.imgProfileImage);
        llShort = findViewById(R.id.llShort);
        btnShort = findViewById(R.id.btnShort);
        iv_star = findViewById(R.id.iv_star);
        llContact = findViewById(R.id.llContatct);
        llMessage = findViewById(R.id.llMessage);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        imgUserPhotos = (ImageView) findViewById(R.id.imgUserPhotos);
        imgPartnerPhotos = (ImageView) findViewById(R.id.imgPartnerPhotos);
        relZoomImageView = (RelativeLayout) findViewById(R.id.relZoomImageView);
        imgZoomProfilePicture = (ImageView) findViewById(R.id.imgZoomProfilePicture);
        imgzoomViewClose = (ImageView) findViewById(R.id.imgzoomViewClose);


        // imgProfileImage = findViewById(R.id.imgProfileImage);
        textUsername = findViewById(R.id.textUsername);
        textMUsername = findViewById(R.id.textMUsername);
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
        textMotherTongue = findViewById(R.id.textMotherTongue);

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

            Glide.with(
                    MemberViewProfile.this)
                    .load(profile_image)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                    .into(imgProfileImage);

        }


    }

    private void Onclick() {

        llInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RequestType.equalsIgnoreCase("Send Intrest"))
                {
                    String test = is_blocked;
                    Log.d("TAG","CHECK ="+test);

                    if (is_blocked.equalsIgnoreCase("1"))
                    {
                        String msgBlock = "This member has blocked you. You can't express your interest.";
                        String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";

                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                        builder.setMessage(msgBlock).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
							sendPushNotification(tokans,
									AppConstants.msg_express_intress+" "+login_matri_id,
									AppConstants.msg_express_intress_title,AppConstants.express_intress_id);
                        sendInterestRequest(matri_id, login_matri_id, is_interest);
                    }
                }
                else if (RequestType.equalsIgnoreCase("Send Reminder"))
                {
                    if (is_blocked.equalsIgnoreCase("1"))
                    {
                        String msgBlock = "This member has blocked you. You can't express your interest.";
                        String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";

                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                        builder.setMessage(msgBlock).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                        sendInterestRequestRemind(matri_id,login_matri_id, is_interest);
                    }
                }
            }
        });


        btnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strPhotoProtect.equalsIgnoreCase("Yes") && !strPhotoPassword.equalsIgnoreCase("")) {

                    final DialogPhotoRequestPassword dgnew = new DialogPhotoRequestPassword(tokans,MemberViewProfile.this,matri_id,imgProfileImage,"photofram",Photo_Pass);
                    dgnew.setCancelable(true);
                    dgnew.setCanceledOnTouchOutside(true);
                    dgnew.show();


                } else {
                    //relZoomImageView.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(MemberViewProfile.this, ViewImageActivity.class);
                    intent.putExtra("MATRIiD", matri_id);
                    intent.putExtra("me","me");
                    startActivity(intent);
                    finish();
                }



            }
        });




//____NOTE:____________0 = BLOCK_________1=UNBLOCK____________



        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvBlock.getText().toString().equalsIgnoreCase("Block")) {

                    addToBlockRequest(login_matri_id, matri_id, "0");

                    //}

                } else if (tvBlock.getText().toString().equalsIgnoreCase("Unblock")) {
                    addToBlockRequest(login_matri_id, matri_id, "1");
                }
            }
        });
        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("vall",strbirthTime + "  " +strBithdate );
                if (strMessage.equalsIgnoreCase("")) {
                    final DialogContacts dgnew = new DialogContacts(MemberViewProfile.this, matri_id,
                            isMessageViewd, strMatId, strUsename, strEmail, strCountry, strState, strCity,
                            strBithdate, strMobileNumber,strCountrycode,strbirthTime);
                    dgnew.setCancelable(true);
                    dgnew.setCanceledOnTouchOutside(true);
                    dgnew.show();
                } else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                    builder.setMessage("" + strMessage).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                         dialog.dismiss();
                         dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        llShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToShortListRequest(login_matri_id, matri_id,/*singleUser.getMatri_id()*/is_shortlist);
            }
        });
        llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL1 = AppConstants.MAIN_URL + "compose_message.php";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("matri_id", login_matri_id);
                client.post(URL1, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        try {
                            JSONObject obj = new JSONObject(responseString);
                            String status = obj.getString("status");
                            if (status.equalsIgnoreCase("1")) {

                                Intent intentComposeMessage = new Intent(MemberViewProfile.this, ComposeMessage.class);
                                intentComposeMessage.putExtra("toId", matri_id);
                                startActivity(intentComposeMessage);
                            } else {
                                String msgError = obj.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                                builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });

        imgzoomViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // relZoomImageView.setVisibility(View.GONE);
                Intent intent = new Intent(MemberViewProfile.this, ViewImageActivity.class);
                intent.putExtra("MATRIiD", matri_id);
                intent.putExtra("me","me");
                startActivity(intent);
                finish();
            }
        });

        tvPhotoRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strPhotoProtect.equalsIgnoreCase("Yes") && !strPhotoPassword.equalsIgnoreCase("")) {

                    final DialogPhotoRequestPassword dgnew = new DialogPhotoRequestPassword(tokans,MemberViewProfile.this,matri_id,imgProfileImage,"photo",Photo_Pass);
                    dgnew.setCancelable(true);
                    dgnew.setCanceledOnTouchOutside(true);
                    dgnew.show();

                } else {
                    Intent intent = new Intent(MemberViewProfile.this, ViewImageActivity.class);
                    intent.putExtra("MATRIiD", matri_id);
                    intent.putExtra("me","me");
                    startActivity(intent);
                    finish();
                    //relZoomImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.tab = MainActivity.tabLayout.getTabAt(0);
        MainActivity.tab.select();
    }


    private void addToBlockRequest(String login_matri_id, String strMatriId, final String isBlocked) {
        progresDialog = new ProgressDialog(MemberViewProfile.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (isBlocked.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "remove_blocklist.php";
                    Log.e("remove_blocklist", "== " + URL);
                } else {
                    URL = AppConstants.MAIN_URL + "block_user.php";
                    Log.e("block_user", "== " + URL);
                }


                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("matri_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("block_matri_id", paramsUserMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

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

                Log.e("block_user", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        //String message=obj.getString("message").toString().trim();

                        if (isBlocked.equalsIgnoreCase("1")) {
                            is_blocked = "0";
                            tvBlock.setText("BLOCK");
                            Toast.makeText(MemberViewProfile.this, "Successfully unblock.", Toast.LENGTH_LONG).show();
                        } else {
                            is_blocked = "1";
                            tvBlock.setText("UNBLOCK");
                            Toast.makeText(MemberViewProfile.this, "Successfully blocked.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    progresDialog.dismiss();
                } catch (Exception t) {
                    Log.e("fjkhgjkfa",t.getMessage());
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId, isBlocked);
    }


    public void getMemberProfile(String strLoginMatriId, String strMatriId) {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramLoginMatriId = params[0];
                String paramsMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile_view.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                Log.e("getMemberData11",""+paramLoginMatriId+" "+paramsMatriId);

                BasicNameValuePair LOginMatriIdPair = new BasicNameValuePair("login_matri_id", paramLoginMatriId);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LOginMatriIdPair);
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

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String matri_id = resItem.getString("matri_id");
                                String email = resItem.getString("email");
                                mobileNo = resItem.getString("mobile");
                                String firstname = resItem.getString("firstname");
                                String lastname = resItem.getString("lastname");
                                String userNname = resItem.getString("username");
                                String moonsign = resItem.getString("moonsign");
                                country_code = resItem.getString("country_code");
                                String subcaste = resItem.getString("subcaste");
                                String gender = resItem.getString("gender");
                                String birthdate = resItem.getString("birthdate");
                                String birthTime = resItem.getString("birthtime");
                                tokans = resItem.getString("tokan");
                                Photo_Pass = resItem.getString("photo_pass");
                                String about_me_status = resItem.getString("about_me_status");
                                String profile_text = resItem.getString("profile_text").trim();

                                if (about_me_status.equalsIgnoreCase("Approved"))
                                    textAboutme.setText(profile_text);
                                else {
                                    textAboutme.setText("Not yet Approved");
                                }


                                is_shortlist = resItem.getString("is_shortlisted");
                                is_interest= resItem.getString("is_favourite");

                                if (is_shortlist.equalsIgnoreCase("1")) {
                                    is_shortlist = "0";
                                    llShort.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    iv_star.setImageResource(R.drawable.ic_membership1);
                                    btnShort.setText("Shortlist");
                                    btnShort.setTextColor(MemberViewProfile.this.getResources().getColor(R.color.black));
                                }
                                else
                                    {
                                    is_shortlist = "1";
                                    llShort.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                                    iv_star.setImageResource(R.drawable.ic_membership2);
                                    btnShort.setText("Shortlisted");
                                    btnShort.setTextColor(MemberViewProfile.this.getResources().getColor(R.color.colorWhite));
                                }




                                if(is_interest.equalsIgnoreCase("1"))
                                {
                                    RequestType="Send Reminder";
                                    ivInterest.setImageResource(R.drawable.ic_reminder);
                                    tvInterest.setText("Send Reminder");

                                }else
                                {
                                    RequestType="Send Intrest";
                                    ivInterest.setImageResource(R.drawable.ic_heart);
                                    tvInterest.setText("Send Intrest");
                                }


                                textMaritalSts.setText(":  " + resItem.getString("m_status"));
                                textProfileCreatedBy.setText(":  " + resItem.getString("profileby"));
                                textUsername.setText(":  " + resItem.getString("username"));
                                textMUsername.setText(resItem.getString("firstname") + " 's preferences " );
                                tvUserName.setText(userNname);
                                tvAge.setText(resItem.getString("age") + ", ");
                                tvheight.setText(resItem.getString("height") + ", ");
                                tvCaste.setText(resItem.getString("caste_name") + ", ");
                                tvReligion.setText(resItem.getString("religion"));
                                tvCity.setText(resItem.getString("city_name") + ", ");
                                tvCountry.setText(resItem.getString("country_name") + ".");
                                tvPhotoCounter.setText(resItem.getString("photo_count"));
                                txtHeaderTitle.setText("" + matri_id);
                                collapsingToolbarLayout.setTitle(firstname + " " + lastname);
                                textOccupation.setText(":  " + resItem.getString("occupation"));
                                textPerSubCast.setText(":  " + subcaste);
                                textBirthtime.setText(":  " + resItem.getString("birthtime"));
                                textBirthplace.setText(":  " + resItem.getString("birthplace"));
                                tvNoOfChild.setText(":  " + resItem.getString("tot_children"));
                                tvChildStatus.setText(":  " + resItem.getString("status_children"));
                                textHeighestEducation.setText(":  " + resItem.getString("edu_detail"));
                                textAnnualIncome.setText(":  " + resItem.getString("income"));
                                textEmployedIn.setText(":  " + resItem.getString("emp_in"));
                                textAdditionalDegree.setText(":  " + resItem.getString("addition_detail")); ///////////
                                textFamilyType.setText(":  " + resItem.getString("family_type"));
                                textStar.setText(":  " + resItem.getString("star"));
                                textRassiMoonSign.setText(":  "+moonsign);
                                textMotherTongue.setText(":  " + resItem.getString("m_tongue"));
                                textHeight.setText(":  " + resItem.getString("height"));
                                textWeight.setText(":  " + resItem.getString("weight") + "kg");
                                textPhysicalStates.setText(":  " + resItem.getString("physicalStatus"));
                                textBodyType.setText(":  " + resItem.getString("bodytype"));
                                textDietHabite.setText(":  " + resItem.getString("diet"));
                                textSmoking.setText(":  " + resItem.getString("smoke"));
                                textDrinking.setText(":  " + resItem.getString("drink"));
                                textFatherOccupation.setText(":  " + resItem.getString("father_occupation"));
                                textMotherOccupation.setText(":  " + resItem.getString("mother_occupation"));

                                textCountry.setText(":  " + resItem.getString("country_name"));
                                textState.setText(":  " + resItem.getString("state_name"));
                                textCity.setText(":  " + resItem.getString("city_name"));
                                textReligion.setText(":  " + resItem.getString("religion_name"));
                                textPerCast.setText(":  " + resItem.getString("caste_name"));
                                textFamilyType.setText(":  " + resItem.getString("family_type"));
                                textFamilyStatus.setText(":  " + resItem.getString("family_status"));
                                textFamilyValue.setText(":  " + resItem.getString("family_value"));
                                textNoOfBrothers.setText(":  " + resItem.getString("no_of_brothers"));
                                textHaveDosh.setText(":  " + resItem.getString("dosh"));
                                if (resItem.getString("dosh").equalsIgnoreCase("yes")) {
                                    linDoshType.setVisibility(View.VISIBLE);
                                    textDoshType.setText("  :  " + resItem.getString("manglik"));
                                } else {
                                    linDoshType.setVisibility(View.GONE);
                                }

                                if (resItem.has("is_blocked")) {
                                    is_blocked = resItem.getString("is_blocked");

                                    if (is_blocked.equalsIgnoreCase("1")) {
                                        is_blocked = "0";
                                        tvBlock.setText("BLOCK");
                                    } else {
                                        is_blocked = "1";
                                        tvBlock.setText("UNBLOCK");
                                    }
                                } else {
                                    is_blocked = "0";
                                }
                              Log.e("bfgh",resItem.getString("is_blocked"));


                              /*  if(resItem.getString("is_blocked").equalsIgnoreCase("1"))
                                {
                                    tvBlock.setBackgroundResource(R.drawable.bg_orange_selected);
                                }else
                                {
                                    tvBlock.setBackgroundResource(R.drawable.bg_orange_select);

                                }*/
                               /* if (resItem.getString("is_blocked").equalsIgnoreCase("1")) {
                                    is_blocked = "0";
                                    tvBlock.setText("BLOCK");
                                } else {
                                    is_blocked = "1";
                                    tvBlock.setText("UNBLOCK");
                                }*/

                                textNoOfSisters.setText(":  " + resItem.getString("no_of_sisters"));
                                if (!resItem.getString("no_of_brothers").equalsIgnoreCase("") && !resItem.getString("no_of_brothers").equalsIgnoreCase("0")) {
                                    linNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    textNoOfMarriedBrothers.setText("   :  " + resItem.getString("no_marri_brother"));
                                } else {
                                    linNoOfMarriedBrother.setVisibility(View.GONE);
                                }
                                if (!resItem.getString("no_of_sisters").equalsIgnoreCase("") && !resItem.getString("no_of_sisters").equalsIgnoreCase("0")) {
                                    linNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    textNoOfMarriedSisters.setText("  :  " + resItem.getString("no_marri_sister"));
                                } else {
                                    linNoOfMarriedSisters.setVisibility(View.GONE);
                                }


                                String part_to_age = resItem.getString("part_age");
                                String part_income = resItem.getString("part_income");
                                String part_height_to = resItem.getString("part_height");
                                String part_expect = resItem.getString("part_expect");
                                String part_mtongue = resItem.getString("part_mtongue");
                                String part_religion = resItem.getString("part_religion");
                                String part_caste = resItem.getString("part_caste");
                                String part_star = resItem.getString("part_star");
                                String part_rasi = resItem.getString("part_rasi");
                                String part_manglik = resItem.getString("part_manglik");
                                String part_edu = resItem.getString("part_edu");
                                String part_occu = resItem.getString("part_occu");
                                String part_state = resItem.getString("part_state");
                                String part_city = resItem.getString("part_city");
                                String part_country_living = resItem.getString("part_country_living");
                                String part_smoke = resItem.getString("part_smoke");
                                String part_diet = resItem.getString("part_diet");
                                String part_drink = resItem.getString("part_drink");
                                String part_physical = resItem.getString("part_physical");
                                String part_emp_in = resItem.getString("part_emp_in");
                                String part_expect_status = resItem.getString("part_expect_status");


                                strPhotoProtect = resItem.getString("photo_protect");
                                strPhotoPassword = resItem.getString("photo_pswd");
                                if (strPhotoProtect.equalsIgnoreCase("Yes") && !strPhotoPassword.equalsIgnoreCase("")){
                                    tvPhotoRequest.setVisibility(View.VISIBLE);
                                }
                                else
                                    tvPhotoRequest.setVisibility(View.GONE);




                                String photo1 = resItem.getString("photo1");
                                String TotalMs = resItem.getString("total_cnt");
                                String useMs = resItem.getString("used_cnt");
                                Log.e("cnnttt", TotalMs + "  " + useMs);

                                if (!TotalMs.equalsIgnoreCase("") && TotalMs != null) {
                                    TotalContacts = Integer.parseInt(TotalMs);
                                }

                                if (!useMs.equalsIgnoreCase("") && useMs != null) {
                                    UsedContact = Integer.parseInt(useMs);
                                }

                                isMessageViewd = resItem.getString("is_viewed");

                                String TotalMatches = resItem.getString("match_pre");
                                String TotalProfiles = resItem.getString("total_pre");
                                String strUsername = firstname + " " + lastname;

                                strMessage = resItem.getString("status_contact");
                                strMatId = matri_id;
                                strUsename = strUsername;
                                strEmail = email;
                                strCountry = resItem.getString("country_name");
                                strState = resItem.getString("state_name");
                                strCity = resItem.getString("city_name");
                                strBithdate = birthdate;
                                strMobileNumber = mobileNo;
                                strCountrycode = country_code;
                                strbirthTime= birthTime;

                                textGender.setText(":  "+gender);
                                textPCaste.setText(resItem.getString("caste_name"));
                                tvCaste.setText(resItem.getString("caste_name"));

                                textPMaritalStatus.setText(":  " + resItem.getString("m_status"));
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
                                textPManglik.setText(":  " + part_manglik);
                                textPStar.setText(":  " + part_star);
                                textPMotherTongue.setText(":  " + part_mtongue);
                                textPCountry.setText(":  " + part_country_living);
                                textPState.setText(":  " + part_state);
                                textPCity.setText(":  " + part_city);

                                if (part_expect_status.equalsIgnoreCase("Approved"))
                                   textPartnerExpectation.setText(part_expect);
                                else {
                                    textPartnerExpectation.setText("Not yet Approved");
                                }

                                tvMessage.setText(TotalMatches + "/" + TotalProfiles);
                                if (!photo1.equalsIgnoreCase("")) {
                                    Log.d("PROFILE_____", photo1);

                                    progressBar1.setVisibility(View.VISIBLE);
                                    Picasso.with(MemberViewProfile.this)
                                            .load(photo1)
                                            //.fit()
                                            .error(R.drawable.ic_profile)
                                            .into(imgProfileImage, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    progressBar1.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    progressBar1.setVisibility(View.GONE);
                                                }
                                            });
                                }
                                if (!strUserImage.equalsIgnoreCase("")) {

                                    Picasso.with(MemberViewProfile.this)
                                            .load(strUserImage)
                                            .fit()
                                            .transform(new CircleTransform())
                                            .error(R.drawable.ic_profile)
                                            .into(imgUserPhotos, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                }
                                if (!photo1.equalsIgnoreCase("")) {

                                    Picasso.with(MemberViewProfile.this)
                                            .load(photo1)
                                            .fit()
                                            .transform(new CircleTransform())
                                            .error(R.drawable.ic_profile)
                                            .into(imgPartnerPhotos, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                }


                                final ProgressBar progressBar;
                                progressBar = (ProgressBar) findViewById(R.id.progressBar);

                                if (!photo1.equalsIgnoreCase("")) {
                                    Picasso.with(MemberViewProfile.this)
                                            .load(photo1)
                                            .placeholder(R.drawable.loading1)
                                            .error(R.drawable.male)
                                            .into(imgZoomProfilePicture, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    progressBar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });

                                } else {
                                    //Toast.makeText(ViewProfileActivity.this, "Image not available.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                            }

                        }

                    } else {
                        progressBar1.setVisibility(View.GONE);
                        String msgError = obj.getString("message");
                        Toast.makeText(MemberViewProfile.this, "" + msgError, Toast.LENGTH_SHORT).show();
                    }


                    progressBar1.setVisibility(View.GONE);
                } catch (Exception t) {
                    Log.e("edrj", t.getMessage());
                    Toast.makeText(MemberViewProfile.this, ""+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strLoginMatriId, strMatriId);
    }


    private void sendInterestRequest(String login_matri_id, String strMatriId, final String isFavorite)
    {
        progresDialog= new ProgressDialog(MemberViewProfile.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"send_intrest.php";
                Log.e("send_intrest", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair  = new BasicNameValuePair("receiver_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

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

                Log.e("send_intrest", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        ivInterest.setImageResource(R.drawable.ic_reminder);
                        tvInterest.setText("Send Reminder");
                        String message=obj.getString("message").toString().trim();
                        Toast.makeText(MemberViewProfile.this, ""+message, Toast.LENGTH_SHORT).show();

                        if(isFavorite.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_favourite("0");
                        }else
                        {
                           // arrUserList.get(pos).setIs_favourite("1");
                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
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
                } catch (Exception t)
                {
                    Log.e("fjglfjl",t.getMessage());
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id,strMatriId);
    }


    private void sendInterestRequestRemind(String login_matri_id, String strMatriId, final String isFavorite)
    {
        progresDialog= new ProgressDialog(MemberViewProfile.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"send_intrest.php";
                Log.e("send_intrest", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair  = new BasicNameValuePair("receiver_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

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

                Log.e("send_intrest", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {

                        String message=obj.getString("message").toString().trim();
                        Toast.makeText(MemberViewProfile.this, ""+message, Toast.LENGTH_SHORT).show();

                        if(isFavorite.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_favourite("0");
                        }else
                        {
                          //  arrUserList.get(pos).setIs_favourite("1");
                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
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
        sendPostReqAsyncTask.execute(login_matri_id,strMatriId);
    }


    private void addToShortListRequest(String login_matri_id, String strMatriId, final String isShortlisted) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (isShortlisted.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "remove_shortlist.php";
                    Log.e("remove_shortlist", "== " + URL);
                } else {
                    URL = AppConstants.MAIN_URL + "add_shortlisted.php";
                    Log.e("add_shortlisted", "== " + URL);
                }

                Log.e("URL shortlisted", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("from_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("to_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

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

                Log.e("add_shortlisted", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(MemberViewProfile.this, "" + message, Toast.LENGTH_SHORT).show();

                        if (isShortlisted.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_shortlisted("0");
                            is_shortlist = "0";
                            llShort.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            iv_star.setImageResource(R.drawable.ic_membership1);
                            btnShort.setText("Shortlist");
                            btnShort.setTextColor(MemberViewProfile.this.getResources().getColor(R.color.black));
                        } else {
                            //arrUserList.get(pos).setIs_shortlisted("1");
                            is_shortlist = "1";
                            llShort.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                            iv_star.setImageResource(R.drawable.ic_membership2);
                            btnShort.setText("Shortlisted");
                            btnShort.setTextColor(MemberViewProfile.this.getResources().getColor(R.color.colorWhite));
                        }

                        //refreshAt(pos);

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberViewProfile.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    //progresDialogShortlist.dismiss();
                } catch (Throwable t) {
                    //progresDialogShortlist.dismiss();
                }
                //progresDialogShortlist.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId, isShortlisted);
    }


}
