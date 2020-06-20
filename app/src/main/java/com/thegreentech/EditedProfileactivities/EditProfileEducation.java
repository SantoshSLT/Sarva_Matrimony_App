package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

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

import Adepters.AdditionalDgreeAdapter;
import Adepters.EducationsAdapter;
import Adepters.GeneralAdapter;
import Adepters.OccupationAdapter;
import Models.beanEducation;
import Models.beanOccupation;
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

public class EditProfileEducation extends AppCompatActivity {

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id = "";
    ImageView btnMenuClose;
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    EditText edtHighestEducation, edtDegree, edtOccupation, edtEmployedIn, edtAnnualIncome;
    Button btnupdate;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    LinearLayout linHighestEducation, linAdditionalDegree, linOccupation, linGeneralView;
    EditText edtSearchHighestEducation, edtSearchAdditionalDegree, edtSearchOccupation;
    RecyclerView rvHighestEducation, rvAdditionalDegree, rvOccupation, rvGeneralView;
    ArrayList<beanEducation> arrEducation = new ArrayList<beanEducation>();
    EducationsAdapter educationAdapter = null;
    Button btnConfirm;
    ArrayList<beanEducation> arrAdditionalDgree = new ArrayList<beanEducation>();
    AdditionalDgreeAdapter additionalDgreeAdapter = null;

    ArrayList<beanOccupation> arrOccupation = new ArrayList<beanOccupation>();
    OccupationAdapter occupationAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_education_proffesion);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfileEducation.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        SlidingMenu();
        onClick();

        if (NetworkConnection.hasConnection(EditProfileEducation.this)){
            getProfileDetail(matri_id);
            // getAdditionalDgree();
            getOccupationsRequest();
            getOccupations();
        }else
        {
            AppConstants.CheckConnection( EditProfileEducation.this);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileDetail(matri_id);
    }

    public void init() {
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Education /Profession Information");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtHighestEducation = findViewById(R.id.edtHighestEducation);
        edtDegree = findViewById(R.id.edtDegree);
        edtOccupation = findViewById(R.id.edtOccupation);
        edtEmployedIn = findViewById(R.id.edtEmployedIn);
        edtAnnualIncome = findViewById(R.id.edtAnnualIncome);
        edtSearchHighestEducation = (EditText) findViewById(R.id.edtSearchHighestEducation);
        edtSearchAdditionalDegree = (EditText) findViewById(R.id.edtSearchAdditionalDegree);
        edtSearchOccupation = (EditText) findViewById(R.id.edtSearchOccupation);
        linHighestEducation = (LinearLayout) findViewById(R.id.linHighestEducation);
        linAdditionalDegree = (LinearLayout) findViewById(R.id.linAdditionalDegree);
        linOccupation = (LinearLayout) findViewById(R.id.linOccupation);
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);
        rvHighestEducation = (RecyclerView) findViewById(R.id.rvHighestEducation);
        rvAdditionalDegree = (RecyclerView) findViewById(R.id.rvAdditionalDegree);
        rvOccupation = (RecyclerView) findViewById(R.id.rvOccupation);
        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);

        btnupdate = findViewById(R.id.btnUpdate);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
    }

    public void onClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        edtHighestEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtHighestEducation.setError(null);
                edtSearchHighestEducation.setText("");
                linHighestEducation.setVisibility(View.VISIBLE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
            }
        });


        edtDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtDegree.setError(null);
                edtSearchAdditionalDegree.setText("");
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
            }
        });


        edtOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
            }
        });


        edtEmployedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtEmployedIn.setError(null);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileEducation.this, getResources().getStringArray(R.array.arr_Employed_in), SlidingDrawer, Slidingpage, btnMenuClose, edtEmployedIn);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAnnualIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);
                linHighestEducation.setVisibility(View.GONE);
                linAdditionalDegree.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileEducation.this, getResources().getStringArray(R.array.arr_annual_income), SlidingDrawer, Slidingpage, btnMenuClose, edtAnnualIncome);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmployedIn = edtEmployedIn.getText().toString().trim();
                String AnnualIncome = edtAnnualIncome.getText().toString().trim();

                if (hasData(AppConstants.EducationId) &&
                        hasData(AppConstants.AditionalEducationId) &&
                        hasData(AppConstants.OccupationID) &&
                        hasData(EmployedIn) &&
                        hasData(AnnualIncome)) {

                    if (NetworkConnection.hasConnection(EditProfileEducation.this)){
                        updateEducation(matri_id, AppConstants.EducationId, AppConstants.AditionalEducationId, AppConstants.OccupationID,
                                EmployedIn, AnnualIncome);
                    }else
                    {
                        AppConstants.CheckConnection( EditProfileEducation.this);
                    }


                } else {
                    Toast.makeText(EditProfileEducation.this, "All Field Required", Toast.LENGTH_SHORT).show();
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
        AnimUtils.SlideAnimation(EditProfileEducation.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileEducation.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu() {
        rvHighestEducation.setLayoutManager(new LinearLayoutManager(this));
        rvHighestEducation.setHasFixedSize(true);

        rvAdditionalDegree.setLayoutManager(new LinearLayoutManager(this));
        rvAdditionalDegree.setHasFixedSize(true);

        rvOccupation.setLayoutManager(new LinearLayoutManager(this));
        rvOccupation.setHasFixedSize(true);

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });

    }


    private void getHighestEducationRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
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
                Log.e("--religion --", "==" + Ressponce);

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
                            educationAdapter = new EducationsAdapter(EditProfileEducation.this, arrEducation, SlidingDrawer, Slidingpage, btnMenuClose, edtHighestEducation);
                            rvHighestEducation.setAdapter(educationAdapter);

                        }

                        if (arrEducation.size() > 0) {
                            Collections.sort(arrAdditionalDgree, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });

                            additionalDgreeAdapter = new AdditionalDgreeAdapter(EditProfileEducation.this, arrAdditionalDgree, SlidingDrawer, Slidingpage, btnMenuClose, edtDegree);
                            rvAdditionalDegree.setAdapter(additionalDgreeAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {

                } finally {
                    getAdditionalDgree();
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

    private void getOccupationsRequest() {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
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
                Log.e("--religion --", "==" + Ressponce);

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
                            occupationAdapter = new OccupationAdapter(EditProfileEducation.this, arrOccupation, SlidingDrawer, Slidingpage, btnMenuClose, edtOccupation);
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

    public void getProfileDetail(String MatriID) {

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
                Log.e("View Profile ", "==" + result);

                String finalresult = "";
                try {
                    finalresult = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();
                            while (resIter.hasNext()) {
                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                AppConstants.AditionalEducationId=resItem.getString("addition_dgree_id");
                                AppConstants.EducationId=resItem.getString("edu_detail_id");
                                AppConstants.OccupationID=resItem.getString("occupation_id");

                                edtHighestEducation.setText(resItem.getString("edu_detail"));
                                edtDegree.setText(resItem.getString("addition_detail"));
                                edtOccupation.setText(resItem.getString("occupation"));
                                edtEmployedIn.setText(resItem.getString("emp_in"));
                                edtAnnualIncome.setText(resItem.getString("income"));

                            }
                        }

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileEducation.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(EditProfileEducation.this, "msgError" + msgError, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    Toast.makeText(EditProfileEducation.this, "exception" + e.
                            getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("educationeeeeee", e.getMessage());
                }

                if (NetworkConnection.hasConnection(EditProfileEducation.this)){
                    getHighestEducationRequest();
                    getHighestEducation();
                }else
                {
                    AppConstants.CheckConnection( EditProfileEducation.this);
                }



            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriID);
    }


    public void updateEducation(String MatriId, String educationID, String AditionalDegreeID, String OccupationID, String EmployeedIn
            , String AnnualIncome) {
        progresDialog = new ProgressDialog(EditProfileEducation.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String Pmatri_id = params[0];
                String PEducationId = params[1];
                String PAditionalEducationId = params[2];
                String POccupationID = params[3];
                String PEmployedIn = params[4];
                String PAnnualIncome = params[5];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "edit_edu_occ_details.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", Pmatri_id);
                BasicNameValuePair EducationIdPair = new BasicNameValuePair("education_id", PEducationId);
                BasicNameValuePair AditionalEducationIdPair = new BasicNameValuePair("additional_degree_id", PAditionalEducationId);
                BasicNameValuePair OccupationIDPair = new BasicNameValuePair("occupation_id", POccupationID);
                BasicNameValuePair EmployedInPair = new BasicNameValuePair("employed_in", PEmployedIn);
                BasicNameValuePair AnnualIncomePair = new BasicNameValuePair("annual_income", PAnnualIncome);


                List<NameValuePair> nameValuePairList = new ArrayList<>();

                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(EducationIdPair);
                nameValuePairList.add(AditionalEducationIdPair);
                nameValuePairList.add(OccupationIDPair);
                nameValuePairList.add(EmployedInPair);
                nameValuePairList.add(AnnualIncomePair);


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
                Log.e("--Update Education--", "==" + Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status = responseObj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        getProfileDetail(matri_id);
                        Intent intLogin = new Intent(EditProfileEducation.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    } else {
                        String msgError = responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileEducation.this);
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
                    Log.e("exceptionEdu", e.getMessage());
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(MatriId, educationID, AditionalDegreeID, OccupationID, EmployeedIn, AnnualIncome);

    }


}


