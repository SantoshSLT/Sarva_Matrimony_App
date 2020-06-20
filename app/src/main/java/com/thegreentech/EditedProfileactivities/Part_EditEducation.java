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

import Adepters.EducationsMultiSelectionAdapter;
import Adepters.GeneralAdapter;
import Adepters.OccupationMultiSelectionAdapter;
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

public class Part_EditEducation extends AppCompatActivity {


    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    EditText edtHighestEducation,edtOccupation,edtAnnualIncome;
    Button btnUpdate;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    LinearLayout linHighestEducation,linOccupation,linGeneralView;
    EditText edtSearchHighestEducation,edtSearchOccupation;
    RecyclerView rvHighestEducation,rvOccupation,rvGeneralView;
    Button btnConfirm;

    ArrayList<beanEducation> arrEducation = new ArrayList<beanEducation>();
    EducationsMultiSelectionAdapter educationAdapter = null;

    ArrayList<beanOccupation> arrOccupation = new ArrayList<beanOccupation>();
    OccupationMultiSelectionAdapter occupationAdapter=null;

    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_education_prefrence);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(Part_EditEducation.this);
        matri_id=prefUpdate.getString("matri_id","");
        init();
        SlidingMenu();
        onclick();
        if (NetworkConnection.hasConnection(Part_EditEducation.this)){
            getProfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( Part_EditEducation.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(Part_EditEducation.this)){
            getProfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( Part_EditEducation.this);
        }
    }

    public  void  init(){

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Education/Profession Preference");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        edtHighestEducation=(EditText)findViewById(R.id.edtHighestEducation);
        edtOccupation=(EditText)findViewById(R.id.edtOccupation);// edit
        edtAnnualIncome=(EditText)findViewById(R.id.edtAnnualIncome);// edit
        btnUpdate = findViewById(R.id.btnUpdate);

        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);

    }
    public  void onclick(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String AnnualIncome=edtAnnualIncome.getText().toString().trim();

                if (hasData(AppConstants.EducationId)&& hasData(AppConstants.OccupationID) && hasData(AnnualIncome)){
                    updateEducationPrefer(AppConstants.EducationId,AppConstants.OccupationID,AnnualIncome,matri_id);

                }
                else
                    Toast.makeText(Part_EditEducation.this,"Please enter all required fields. ", Toast.LENGTH_LONG).show();


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

                linHighestEducation.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });

        edtOccupation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");

                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
            }
        });


        edtAnnualIncome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);

                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditEducation.this, getResources().getStringArray(R.array.arr_annual_income),SlidingDrawer,Slidingpage,btnMenuClose,edtAnnualIncome);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

    }



    public void SlidingMenu()
    {

        linHighestEducation=(LinearLayout)findViewById(R.id.linHighestEducation);
        linOccupation=(LinearLayout)findViewById(R.id.linOccupation);
        linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);

        edtSearchHighestEducation=(EditText)findViewById(R.id.edtSearchHighestEducation);
        edtSearchOccupation=(EditText)findViewById(R.id.edtSearchOccupation);


        rvHighestEducation=(RecyclerView)findViewById(R.id.rvHighestEducation);
        rvOccupation=(RecyclerView)findViewById(R.id.rvOccupation);
        rvGeneralView=(RecyclerView)findViewById(R.id.rvGeneralView);

        btnConfirm=(Button) findViewById(R.id.btnConfirm);

        rvHighestEducation.setLayoutManager(new LinearLayoutManager(this));
        rvHighestEducation.setHasFixedSize(true);
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
    public  boolean hasData(String text)
    {
        if (text == null || text.length() == 0)
            return false;

        return true;
    }


    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(Part_EditEducation.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(Part_EditEducation.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    private void getHighestEducationRequest()
    {

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

               /* Log.e("educationparamUsername",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try
                {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrEducation= new ArrayList<beanEducation>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("edu_id");
                            String edu_name=resItem.getString("edu_name");

                            arrEducation.add(new beanEducation(edu_id,edu_name,false));

                        }

                        if(arrEducation.size() >0)
                        {
                            Collections.sort(arrEducation, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });
                            educationAdapter = new EducationsMultiSelectionAdapter(Part_EditEducation.this, arrEducation,SlidingDrawer,Slidingpage,btnMenuClose,edtHighestEducation,btnConfirm);
                            rvHighestEducation.setAdapter(educationAdapter);
                        }
                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getOccupationsRequest();
                        getOccupations();
                    }else
                    {
                        AppConstants.CheckConnection(Part_EditEducation.this);
                    }

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
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



    private void getOccupationsRequest()
    {

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
               /* Log.e("Occupationname",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try
                {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrOccupation= new ArrayList<beanOccupation>();


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("ocp_id");
                            String edu_name=resItem.getString("ocp_name");

                            arrOccupation.add(new beanOccupation(edu_id,edu_name,false));

                        }

                        if(arrOccupation.size() >0)
                        {
                            Collections.sort(arrOccupation, new Comparator<beanOccupation>() {
                                @Override
                                public int compare(beanOccupation lhs, beanOccupation rhs) {
                                    return lhs.getOccupation_name().compareTo(rhs.getOccupation_name());
                                }
                            });
                            occupationAdapter = new OccupationMultiSelectionAdapter(Part_EditEducation.this, arrOccupation,SlidingDrawer,Slidingpage,btnMenuClose,edtOccupation,btnConfirm);
                            rvOccupation.setAdapter(occupationAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
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


    private void getProfileDetail(String strMatriId)
    {

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

                Log.e("Search by maitri Id", "=="+result);

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

                                AppConstants.EducationId=resItem.getString("part_edu_id");
                                AppConstants.OccupationID=resItem.getString("part_occu_id");

                                String part_income=resItem.getString("part_income");
                                String part_edu=resItem.getString("part_edu");
                                String part_occu=resItem.getString("part_ocp_name");
                                edtHighestEducation.setText(part_edu);
                                edtOccupation.setText(part_occu);
                                edtAnnualIncome.setText(part_income);


                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditEducation.this);
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


                } catch (Exception t)
                {
                    Log.e("excccccc",t.getMessage());
                }
                getHighestEducationRequest();
                getHighestEducation();


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public void updateEducationPrefer(String EducationId,String OccupationID,String AnnualIncome,String MatriID) {
        progresDialog= new ProgressDialog(Part_EditEducation.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String paramEducationId= params[0];
                String paramOccupationID= params[1];
                String paramAnnualIncome= params[2];
                String paramMatriID= params[3];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_part_edu_occ_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair PairEducationId = new BasicNameValuePair("part_education_id", paramEducationId);
                BasicNameValuePair PairOccupationID = new BasicNameValuePair("part_occupation_id", paramOccupationID);
                BasicNameValuePair PairAnnualIncome = new BasicNameValuePair("part_annual_income", paramAnnualIncome);
                BasicNameValuePair PairMatriID = new BasicNameValuePair("matri_id", paramMatriID);

                List<NameValuePair> nameValuePairList = new ArrayList<>();

                nameValuePairList.add(PairEducationId);
                nameValuePairList.add(PairOccupationID);
                nameValuePairList.add(PairAnnualIncome);
                nameValuePairList.add(PairMatriID);


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
                    Log.e("JSONRESPONSE",responseObj +"");

                    //JSONObject responseData = responseObj.getJSONObject("responseData");
                    // {"user_id":"10","message":"Registration Detail Successfully Update","status":"1"}
                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {

                        Intent intLogin= new Intent(Part_EditEducation.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditEducation.this);
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
                        Log.e("exceeUpdateEdu",e.getMessage());
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(EducationId,OccupationID,AnnualIncome,MatriID);
    }




}
