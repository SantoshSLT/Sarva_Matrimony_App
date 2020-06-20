package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Models.beanUserData;
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



public class ViewProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtHeaderTitle;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    ImageView imgProfileImage, btnPopup;
    ProgressBar progressBar1;

    Button btnCon, btnShort;
    ImageView iv_star, iv_call;
    LinearLayout btnShortlist, btnContact, btnMessage;

    TextView textUsername, textMaritalStatus, textNoOfChiledren, textChildrenLiving, textMotherTongue, textProfileCreatedBy, textAbout,
            textHeight, textWeight, textBodyType, textPhysicalStates, textReligion, textCaste, textWillingToMarry, textHeighestEducation,
            textAdditionalDegree, textEmployedIn, textAnnualIncome, textFamilyType, textFamilyStatus, textFamilyValue, textFatherOccupation,
            textMotherOccupation, textNoOfBrothers, textNoOfMarriedBrothers, textNoOfSisters, textNoOfMarriedSisters, textHaveDosh, textDoshType, textStar, textBirthtime, textBirthplace,
            textCountery, textState, textCity, textDietHabite, textDrinking, textSmoking
            ;

    ImageView imgUserPhotos, imgPartnerPhotos;
    TextView textMessage;
    TextView textPMaritalStatus, textPAge, textPHeight, textPDietHabite, textPSmoking, textPDrinking, textPPhysicalStates, textPHeighestEducation,
            textPAnnualIncome, textPEmployedIn, textPOccupation, textPReligion, textPCaste, textPManglik, textPStar, textPMotherTongue,
            textPCountery, textPState, textPCity, textPartnerExpectation;
    LinearLayout linDoshType, linNoOfMarriedBrother, linNoOfMarriedSisters, llShort;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog, progresDialogShortlist;
    public static String matri_id, login_matri_id, gender, is_shortlist, strUserImage;

    String mobileNo = "";

    beanUserData singleUser;
    ArrayList<beanUserData> beanUserData = null;

    String strPhotoProtect = "", strMessage = "", strPhotoPassword;
    public static int TotalContacts = 0, UsedContact = 0;
    String isMessageViewd = "";
    String strMatId = "", strUsename = "", strEmail = "", strCountry = "", strState = "", strCity = "", strBithdate = "", strMobileNumber = "";

    RelativeLayout relZoomImageView;
    ImageView imgZoomProfilePicture, imgzoomViewClose;


    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    String is_blocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(ViewProfileActivity.this);
        login_matri_id = prefUpdate.getString("matri_id", "");
        strUserImage = prefUpdate.getString("profile_image", "");
        gender = prefUpdate.getString("gender", "");


        txtHeaderTitle = (TextView) findViewById(R.id.txtHeaderTitle);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        imgProfileImage = (ImageView) findViewById(R.id.imgProfileImage);
        btnPopup = (ImageView) findViewById(R.id.btnPopup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        relZoomImageView = (RelativeLayout) findViewById(R.id.relZoomImageView);
        imgZoomProfilePicture = (ImageView) findViewById(R.id.imgZoomProfilePicture);
        imgzoomViewClose = (ImageView) findViewById(R.id.imgzoomViewClose);
        llShort = findViewById(R.id.llShort);
        beanUserData = new ArrayList<beanUserData>();

        Log.d("TAG", "login_matri_id = " + login_matri_id);
        Log.d("TAG", "other_matri_id = " + matri_id);

        dynamicToolbarColor();
        toolbarTextAppernce();


        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(ViewProfileActivity.this, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, view);

                PopupMenu popup1 = new PopupMenu(ViewProfileActivity.this, btnPopup);

                if (is_blocked.equalsIgnoreCase("1")) {
                    popup.getMenuInflater().inflate(R.menu.poupup_profile_menu_unblock, popup.getMenu());
                } else {
                    popup.getMenuInflater().inflate(R.menu.poupup_profile_menu_block, popup.getMenu());
                }


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Block")) {
//                            if(is_blocked.equalsIgnoreCase("1"))
//                            {
//
//                                addToBlockRequest(login_matri_id,matri_id,"1");
//                            }else
//                            {
                            addToBlockRequest(login_matri_id, matri_id, "0");

                            //}

                        } else if (item.getTitle().equals("Unblock")) {
                            addToBlockRequest(login_matri_id, matri_id, "1");
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


        setFindViewBy();
        getProfileRequest(login_matri_id, matri_id);

        //getUserDataRequest(matri_id,gender);
        //getShortlistedProfileRequest("SM233");
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);
            imgProfileImage.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void dynamicToolbarColor() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photos2);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }


    public void setFindViewBy() {
        btnShortlist = findViewById(R.id.btnShortlist);
        btnShort = findViewById(R.id.btnShort);
        iv_star = findViewById(R.id.iv_star);
        btnContact = findViewById(R.id.btnContact);
        btnMessage = findViewById(R.id.btnMessage);


        textUsername = (TextView) findViewById(R.id.textUsername);
        textMaritalStatus = (TextView) findViewById(R.id.textMaritalStatus);
        textNoOfChiledren = (TextView) findViewById(R.id.textNoOfChiledren);
        textChildrenLiving = (TextView) findViewById(R.id.textChildrenLiving);
        textMotherTongue = (TextView) findViewById(R.id.textMotherTongue);
        textProfileCreatedBy = (TextView) findViewById(R.id.textProfileCreatedBy);
        textAbout = (TextView) findViewById(R.id.textAbout);
        textHeight = (TextView) findViewById(R.id.textHeight);
        textWeight = (TextView) findViewById(R.id.textWeight);
        textBodyType = (TextView) findViewById(R.id.textBodyType);
        textPhysicalStates = (TextView) findViewById(R.id.textPhysicalStates);
        textReligion = (TextView) findViewById(R.id.textReligion);
        textCaste = (TextView) findViewById(R.id.textCaste);
        textWillingToMarry = (TextView) findViewById(R.id.textWillingToMarry);
        textHeighestEducation = (TextView) findViewById(R.id.textHeighestEducation);
        textAdditionalDegree = (TextView) findViewById(R.id.textAdditionalDegree);
        textEmployedIn = (TextView) findViewById(R.id.textEmployedIn);
        textAnnualIncome = (TextView) findViewById(R.id.textAnnualIncome);
        textFamilyType = (TextView) findViewById(R.id.textFamilyType);
        textFamilyStatus = (TextView) findViewById(R.id.textFamilyStatus);
        textFamilyValue = (TextView) findViewById(R.id.textFamilyValue);
        textFatherOccupation = (TextView) findViewById(R.id.textFatherOccupation);
        textMotherOccupation = (TextView) findViewById(R.id.textMotherOccupation);
        textNoOfBrothers = (TextView) findViewById(R.id.textNoOfBrothers);
        textNoOfMarriedBrothers = (TextView) findViewById(R.id.textNoOfMarriedBrothers);
        textNoOfSisters = (TextView) findViewById(R.id.textNoOfSisters);
        textNoOfMarriedSisters = (TextView) findViewById(R.id.textNoOfMarriedSisters);
        textHaveDosh = (TextView) findViewById(R.id.textHaveDosh);
        textDoshType = (TextView) findViewById(R.id.textDoshType);
        textStar = (TextView) findViewById(R.id.textStar);
        textBirthtime = (TextView) findViewById(R.id.textBirthtime);
        textBirthplace = (TextView) findViewById(R.id.textBirthplace);
        textCountery = (TextView) findViewById(R.id.textCountery);
        textState = (TextView) findViewById(R.id.textState);
        textCity = (TextView) findViewById(R.id.textCity);
        textDietHabite = (TextView) findViewById(R.id.textDietHabite);
        textDrinking = (TextView) findViewById(R.id.textDrinking);
        textSmoking = (TextView) findViewById(R.id.textSmoking);

        linDoshType = (LinearLayout) findViewById(R.id.linDoshType);
        linNoOfMarriedBrother = (LinearLayout) findViewById(R.id.linNoOfMarriedBrother);
        linNoOfMarriedSisters = (LinearLayout) findViewById(R.id.linNoOfMarriedSisters);

        imgUserPhotos = (ImageView) findViewById(R.id.imgUserPhotos);
        imgPartnerPhotos = (ImageView) findViewById(R.id.imgPartnerPhotos);

        textMessage = (TextView) findViewById(R.id.textMessage);

        textPMaritalStatus = (TextView) findViewById(R.id.textPMaritalStatus);
        textPAge = (TextView) findViewById(R.id.textPAge);
        textPHeight = (TextView) findViewById(R.id.textPHeight);
        textPDietHabite = (TextView) findViewById(R.id.textPDietHabite);
        textPSmoking = (TextView) findViewById(R.id.textPSmoking);
        textPDrinking = (TextView) findViewById(R.id.textPDrinking);
        textPPhysicalStates = (TextView) findViewById(R.id.textPPhysicalStates);
        textPHeighestEducation = (TextView) findViewById(R.id.textPHeighestEducation);
        textPAnnualIncome = (TextView) findViewById(R.id.textPAnnualIncome);
        textPEmployedIn = (TextView) findViewById(R.id.textPEmployedIn);
        textPOccupation = (TextView) findViewById(R.id.textPOccupation);
        textPReligion = (TextView) findViewById(R.id.textPReligion);
        textPCaste = (TextView) findViewById(R.id.textPCaste);
        textPManglik = (TextView) findViewById(R.id.textPManglik);
        textPStar = (TextView) findViewById(R.id.textPStar);
        textPMotherTongue = (TextView) findViewById(R.id.textPMotherTongue);
        textPCountery = (TextView) findViewById(R.id.textPCountery);
        textPState = (TextView) findViewById(R.id.textPState);
        textPCity = (TextView) findViewById(R.id.textPCity);
        textPartnerExpectation = (TextView) findViewById(R.id.textPartnerExpectation);

        if (is_shortlist.equalsIgnoreCase("1")) {
            llShort.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            iv_star.setImageResource(R.drawable.ic_membership2);
            btnShort.setText("Shortlisted");
            btnShort.setTextColor(ViewProfileActivity.this.getResources().getColor(R.color.colorWhite));
            //arrUserList.get(pos).setIs_shortlisted("0");

        } else {
            //arrUserList.get(pos).setIs_shortlisted("1");
            llShort.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            iv_star.setImageResource(R.drawable.ic_membership1);
            btnShort.setText("Shortlist");
            btnShort.setTextColor(ViewProfileActivity.this.getResources().getColor(R.color.colorOrange));
        }

        btnShortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strShort;
                /*if (is_shortlist.equalsIgnoreCase("1")) {
                    strShort="0";
                } else {
                    strShort="1";
                }*/
                addToShortListRequest(login_matri_id, matri_id,/*singleUser.getMatri_id()*/is_shortlist);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!mobileNo.equalsIgnoreCase(""))
//                {
//                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                    callIntent.setData(Uri.parse("tel:"+Uri.encode(mobileNo.trim())));
//                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(callIntent);
//                }else
//                {
//                    Toast.makeText(ViewProfileActivity.this,"Mobile no not available",Toast.LENGTH_LONG).show();
//                }


                if (strMessage.equalsIgnoreCase("")) {
                   /* final DialogContacts dgnew = new DialogContacts(ViewProfileActivity.this, matri_id,
                            isMessageViewd, strMatId, strUsename, strEmail, strCountry, strState, strCity, strBithdate, strMobileNumber);
                    dgnew.setCancelable(true);
                    dgnew.setCanceledOnTouchOutside(true);
                    dgnew.show();*/
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
                    builder.setMessage("" + strMessage).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });


        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (singleUser.getIs_blocked().equalsIgnoreCase("1"))
//                {
//                    String msgBlock = "This member has blocked you. You can't express your interest.";
//                    String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
//                    builder.setMessage(msgBlock).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int id)
//                        {
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                } else {
                //sendInterestRequest(matri_id, strMatId, "1");
                //}

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
                                Intent intentComposeMessage = new Intent(ViewProfileActivity.this, ComposeMessage.class);
                                intentComposeMessage.putExtra("toId",matri_id);
                                startActivity(intentComposeMessage);
                            } else {
                                String msgError = obj.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
                                builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }catch (Exception e){}
                    }
                });
            }
        });


        relZoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        imgProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strPhotoProtect.equalsIgnoreCase("Yes") && !strPhotoPassword.equalsIgnoreCase("")) {
                    final DialogPasswordButtons dgnew = new DialogPasswordButtons(ViewProfileActivity.this, matri_id, imgProfileImage);
                    dgnew.setCancelable(true);
                    dgnew.setCanceledOnTouchOutside(true);
                    dgnew.show();

                } else {
                    relZoomImageView.setVisibility(View.VISIBLE);
                }

            }
        });


        imgzoomViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relZoomImageView.setVisibility(View.GONE);
            }
        });

    }


    private void getProfileRequest(String strLoginMatriId, String strMatriId) {
        progresDialog = new ProgressDialog(ViewProfileActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramLoginMatriId = params[0];
                String paramsMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile_view.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

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

                                String user_id = resItem.getString("user_id");
                                String matri_id = resItem.getString("matri_id");
                                String email = resItem.getString("email");
                                textMaritalStatus.setText(":  " + resItem.getString("m_status"));
                                textProfileCreatedBy.setText(":  " + resItem.getString("profileby"));
                                textUsername.setText(":  " + resItem.getString("username"));
                                String firstname = resItem.getString("firstname");
                                String lastname = resItem.getString("lastname");

                                txtHeaderTitle.setText("" + matri_id);
                                collapsingToolbarLayout.setTitle(firstname + " " + lastname);

                                String gender = resItem.getString("gender");
                                String birthdate = resItem.getString("birthdate");
                                textBirthtime.setText(":  " + resItem.getString("birthtime"));
                                textBirthplace.setText(":  " + resItem.getString("birthplace"));
                                textNoOfChiledren.setText(":  " + resItem.getString("tot_children"));
                                textChildrenLiving.setText(":  " + "No");

                                textHeighestEducation.setText(":  " + resItem.getString("edu_detail"));
                                textAnnualIncome.setText(":  " + resItem.getString("income"));
                                String occupation_id = resItem.getString("occupation");

                                textEmployedIn.setText(":  " + resItem.getString("emp_in"));
                                textAdditionalDegree.setText(":  " + resItem.getString("addition_detail")); ///////////
                                String religion_id = resItem.getString("religion");
                              //  String cast_id = resItem.getString("caste");
                                //String subcaste=resItem.getString("subcaste");
                                textFamilyType.setText(":  " + resItem.getString("gothra"));///////////////
                                textStar.setText(":  " + resItem.getString("star"));

                                String moonsign = resItem.getString("moonsign");
                                //String horoscope=resItem.getString("horoscope");
                                String manglik = resItem.getString("manglik");
                                textMotherTongue.setText(":  " + resItem.getString("m_tongue"));
                                textWillingToMarry.setText(":  " + resItem.getString("will_to_mary_caste"));
                                textHeight.setText(":  " + resItem.getString("height"));
                                textWeight.setText(":  " + resItem.getString("weight") + "kg");
                                String b_group = resItem.getString("b_group");
                                String complexion = resItem.getString("complexion");
                                textPhysicalStates.setText(":  " + resItem.getString("physicalStatus"));
                                textBodyType.setText(":  " + resItem.getString("bodytype"));
                                textDietHabite.setText(":  " + resItem.getString("diet"));
                                textSmoking.setText(":  " + resItem.getString("smoke"));
                                textDrinking.setText(":  " + resItem.getString("drink"));
                                textHaveDosh.setText(":  " + resItem.getString("dosh"));
                                if (resItem.getString("dosh").equalsIgnoreCase("yes")) {
                                    linDoshType.setVisibility(View.VISIBLE);
                                    textDoshType.setText(":  " + resItem.getString("manglik"));
                                } else {
                                    linDoshType.setVisibility(View.GONE);
                                }

                                String address = resItem.getString("address");
                                String countery_id = resItem.getString("country_id");
                                String state_id = resItem.getString("state_id");
                                String city_id = resItem.getString("city");
                                String phone = resItem.getString("phone");
                                String country_code = resItem.getString("country_code");

                                if (resItem.has("is_blocked")) {
                                    is_blocked = resItem.getString("is_blocked");
                                } else {
                                    is_blocked = "0";
                                }


                                mobileNo = resItem.getString("mobile");
                                String residence = resItem.getString("residence");
                                String father_name = resItem.getString("father_name");
                                String mother_name = resItem.getString("mother_name");
                                textFatherOccupation.setText(":  " + resItem.getString("father_occupation"));
                                textMotherOccupation.setText(":  " + resItem.getString("mother_occupation"));
                                textAbout.setText(resItem.getString("profile_text").trim());
                                textCountery.setText(":  " + resItem.getString("country_name"));
                                textState.setText(":  " + resItem.getString("state_name"));
                                textCity.setText(":  " + resItem.getString("city_name"));
                                textReligion.setText(":  " + resItem.getString("religion_name"));
                                textCaste.setText(":  " + resItem.getString("caste_name"));
                                textFamilyType.setText(":  " + resItem.getString("family_type"));
                                textFamilyStatus.setText(":  " + resItem.getString("family_status"));
                                textFamilyValue.setText(":  " + resItem.getString("family_value"));
                                textNoOfBrothers.setText(":  " + resItem.getString("no_of_brothers"));
                                textNoOfSisters.setText(":  " + resItem.getString("no_of_sisters"));
                                if (!resItem.getString("no_of_brothers").equalsIgnoreCase("") && !resItem.getString("no_of_brothers").equalsIgnoreCase("0")) {
                                    linNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    textNoOfMarriedBrothers.setText(":  " + resItem.getString("no_marri_brother"));
                                } else {
                                    linNoOfMarriedBrother.setVisibility(View.GONE);
                                }


                                if (!resItem.getString("no_of_sisters").equalsIgnoreCase("") && !resItem.getString("no_of_sisters").equalsIgnoreCase("0")) {
                                    linNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    textNoOfMarriedSisters.setText(":  " + resItem.getString("no_marri_sister"));
                                } else {
                                    linNoOfMarriedSisters.setVisibility(View.GONE);
                                }

                                String part_frm_age = resItem.getString("part_frm_age");
                                String part_to_age = resItem.getString("part_to_age");
                                String part_have_child = resItem.getString("part_have_child");
                                String part_income = resItem.getString("part_income");
                                String part_expect = resItem.getString("part_expect");
                                String part_height = resItem.getString("part_height");
                                String part_height_to = resItem.getString("part_height_to");
                                String part_complexation = resItem.getString("part_expect");
                                String part_mtongue = resItem.getString("part_mtongue");
                                String part_religion = resItem.getString("part_religion");
                                String part_caste = resItem.getString("part_caste");
                                String part_subcaste = resItem.getString("part_subcaste");
                                String sub_caste = resItem.getString("sub_caste");
                                String part_star = resItem.getString("part_star");
                                String part_rasi = resItem.getString("part_rasi");
                                String part_manglik = resItem.getString("part_manglik");
                                String part_edu = resItem.getString("part_edu");
                                String part_occu = resItem.getString("part_occu");
                                String part_state = resItem.getString("part_state");
                                String part_city = resItem.getString("part_city");
                                String part_country_living = resItem.getString("part_country_living");
                                String part_resi_status = resItem.getString("part_resi_status");
                                String part_smoke = resItem.getString("part_smoke");
                                String part_diet = resItem.getString("part_diet");
                                String part_drink = resItem.getString("part_drink");
                                String part_physical = resItem.getString("part_physical");
                                String part_emp_in = resItem.getString("part_emp_in");
                                String hor_photo = resItem.getString("hor_photo");

                                strPhotoProtect = resItem.getString("photo_protect");
                                strPhotoPassword = resItem.getString("photo_pswd");

                                String photo1 = resItem.getString("photo1");

                                String TotalMs = resItem.getString("total_cnt");
                                String useMs = resItem.getString("used_cnt");
                                if (!TotalMs.equalsIgnoreCase("") && TotalMs != null) {
                                    TotalContacts = Integer.parseInt(resItem.getString("total_cnt"));

                                }

                                if (!useMs.equalsIgnoreCase("") && useMs != null) {
                                    UsedContact = Integer.parseInt(resItem.getString("used_cnt"));
                                }

                                isMessageViewd = resItem.getString("is_viewed");

                                String TotalMatches = resItem.getString("match_pre");
                                String TotalProfiles = resItem.getString("total_pre");
                                String strUsername = firstname + " " + lastname;//resItem.getString("photo1");

                                strMessage = resItem.getString("status_contact");


                                strMatId = matri_id;
                                strUsename = strUsername;
                                strEmail = email;
                                strCountry = resItem.getString("country_name");
                                strState = resItem.getString("state_name");
                                strCity = resItem.getString("city_name");
                                strBithdate = birthdate;
                                strMobileNumber = mobileNo;

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
                                textPCountery.setText(":  " + part_country_living);
                                textPState.setText(":  " + part_state);
                                textPCity.setText(":  " + part_city);
                                textPartnerExpectation.setText(part_complexation);

                                textMessage.setText("Your profile matches with\n" + TotalMatches + "/" + TotalProfiles + " of " + strUsername + " prefrences!");
                                if (!photo1.equalsIgnoreCase("")) {
                                    Log.d("PROFILE_____", photo1);

                                    progressBar1.setVisibility(View.VISIBLE);
                                    Picasso.with(ViewProfileActivity.this)
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

                                    Picasso.with(ViewProfileActivity.this)
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

                                    Picasso.with(ViewProfileActivity.this)
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
                                    Picasso.with(ViewProfileActivity.this)
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
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strLoginMatriId, strMatriId);
    }


    //Shortlist Request
    private void addToShortListRequest(String login_matri_id, String strMatriId, final String isShortlisted) {
        /*progresDialogShortlist= new ProgressDialog(ViewProfileActivity.this);
        progresDialogShortlist.setCancelable(false);
        progresDialogShortlist.setMessage(ViewProfileActivity.this.getResources().getString(R.string.Please_Wait));
        progresDialogShortlist.setIndeterminate(true);
        progresDialogShortlist.show();*/

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
                        Toast.makeText(ViewProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        if (isShortlisted.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_shortlisted("0");
                            is_shortlist = "0";
                            btnShortlist.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            iv_star.setImageResource(R.drawable.ic_membership1);
                            btnShort.setText("Shortlist");
                            btnShort.setTextColor(ViewProfileActivity.this.getResources().getColor(R.color.colorOrange));
                        } else {
                            //arrUserList.get(pos).setIs_shortlisted("1");
                            is_shortlist = "1";
                            btnShortlist.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                            iv_star.setImageResource(R.drawable.ic_membership2);
                            btnShort.setText("Shortlisted");
                            btnShort.setTextColor(ViewProfileActivity.this.getResources().getColor(R.color.colorWhite));
                        }

                        //refreshAt(pos);

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
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


    private void sendInterestRequest(String login_matri_id, String strMatriId, final String isFavorite) {
        progresDialog = new ProgressDialog(ViewProfileActivity.this);
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

                String URL = AppConstants.MAIN_URL + "send_intrest.php";
                Log.e("send_intrest", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("receiver_id", paramsUserMatriId);


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

                Log.e("send_intrest", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(ViewProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        if (isFavorite.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_favourite("0");
                        } else {

                        }


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId);
    }


    private void addToBlockRequest(String login_matri_id, String strMatriId, final String isBlocked) {
        progresDialog = new ProgressDialog(ViewProfileActivity.this);
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

                            Toast.makeText(ViewProfileActivity.this, "Successfully unblock.", Toast.LENGTH_LONG).show();
                        } else {
                            is_blocked = "1";
                            Toast.makeText(ViewProfileActivity.this, "Successfully blocked.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfileActivity.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId, isBlocked);
    }


}
