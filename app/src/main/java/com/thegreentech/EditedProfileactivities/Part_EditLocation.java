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

import Adepters.CityMultiSelectionAdapter;
import Adepters.CountryMultiSelectionAdapter;
import Adepters.StateMultiSelectionAdapter;
import Models.beanCity;
import Models.beanCountries;
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
import utills.NetworkConnection;

public class Part_EditLocation extends AppCompatActivity {


    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    EditText edtCountry,edtState,edtCity;
    Button btnUpdate;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    LinearLayout linCountry,linState,linCity,linGeneralView;
    EditText edtSearchCountry,edtSearchState,edtSearchCity;
    RecyclerView rvCountry,rvState,rvCity,rvGeneralView;
    Button btnConfirm;

    ArrayList<beanCountries> arrCountry=new ArrayList<beanCountries>();
    CountryMultiSelectionAdapter countryAdapter=null;

    ArrayList<beanState> arrState=new ArrayList<beanState>();
    StateMultiSelectionAdapter stateAdapter=null;

    ArrayList<beanCity> arrCity=new ArrayList<beanCity>();
    CityMultiSelectionAdapter cityAdapter=null;


    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location_prefer);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(Part_EditLocation.this);
        matri_id=prefUpdate.getString("matri_id","");

        init();
        SlidingMenu();
        onclick();
        if (NetworkConnection.hasConnection(Part_EditLocation.this)){
            getProfileDetail(matri_id);

        }else
        {
            AppConstants.CheckConnection( Part_EditLocation.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(Part_EditLocation.this)){
            getProfileDetail(matri_id);

        }else
        {
            AppConstants.CheckConnection( Part_EditLocation.this);
        }
    }

    public  void  init(){
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Location Preference");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtCountry=(EditText)findViewById(R.id.edtCountry);
        edtState=(EditText)findViewById(R.id.edtState);
        edtCity=(EditText)findViewById(R.id.edtCity);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
    }

    public void onclick()
    {
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               onBackPressed();
               finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasData(AppConstants.CountryId)&& hasData(AppConstants.StateId)
                        && hasData(AppConstants.CityId)) {

                    if (NetworkConnection.hasConnection(Part_EditLocation.this)){
                        UpdateLocationPref(AppConstants.CountryId, AppConstants.StateId, AppConstants.CityId, matri_id);

                    }else
                    {
                        AppConstants.CheckConnection( Part_EditLocation.this);
                    }

                }else
                {
                    Toast.makeText(Part_EditLocation.this,"Please enter all required fields. ", Toast.LENGTH_LONG).show();
                }
            }
        });

        edtCountry.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.StateName ="";
                    AppConstants.StateId ="";
                    edtState.setText("");
                    rvState.setAdapter(null);

                    AppConstants.CityName ="";
                    AppConstants.CityId ="";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getStateRequest(AppConstants.CountryId);
                    getStates();
                }
            }
        });


        edtState.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.CityName ="";
                    AppConstants.CityId ="";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                    getCity();
                }
            }
        });


        edtCountry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCountry.setError(null);
                edtSearchCountry.setText("");

                linCountry.setVisibility(View.VISIBLE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);


            }
        });

        edtState.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtState.setError(null);
                edtSearchState.setText("");

                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });

        edtCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCity.setError(null);
                edtSearchCity.setText("");

                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);

                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

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
        AnimUtils.SlideAnimation(Part_EditLocation.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(Part_EditLocation.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu()
    {

        linCountry=(LinearLayout)findViewById(R.id.linCountry);
        linState=(LinearLayout)findViewById(R.id.linState);
        linCity=(LinearLayout)findViewById(R.id.linCity);

        linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);

        edtSearchCountry=(EditText)findViewById(R.id.edtSearchCountry);
        edtSearchState=(EditText)findViewById(R.id.edtSearchState);
        edtSearchCity=(EditText)findViewById(R.id.edtSearchCity);

        rvCountry=(RecyclerView)findViewById(R.id.rvCountry);
        rvState=(RecyclerView) findViewById(R.id.rvState);
        rvCity=(RecyclerView)findViewById(R.id.rvCity);
        rvGeneralView=(RecyclerView)findViewById(R.id.rvGeneralView);

        btnConfirm=(Button) findViewById(R.id.btnConfirm);


        rvCountry.setLayoutManager(new LinearLayoutManager(this));
        rvCountry.setHasFixedSize(true);
        rvState.setLayoutManager(new LinearLayoutManager(this));
        rvState.setHasFixedSize(true);
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setHasFixedSize(true);

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



    private void getCountrysRequest()
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                //  String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"country.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                /*Log.e("countrynameid",paramUsername);
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
                Log.e("--Country --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCountry= new ArrayList<beanCountries>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID=resItem.getString("country_id");
                            String CoName=resItem.getString("country_name");

                            arrCountry.add(new beanCountries(CoID,CoName,false));

                        }

                        if(arrCountry.size() >0)
                        {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
                            countryAdapter = new CountryMultiSelectionAdapter(Part_EditLocation.this, arrCountry,SlidingDrawer,Slidingpage,btnMenuClose,edtCountry,btnConfirm);
                            rvCountry.setAdapter(countryAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    getStateRequest(AppConstants.CountryId);
                    getStates();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getCountries()
    {
        try {
            edtSearchCountry.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrCountry.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchCountry.getText().toString().toLowerCase(Locale.getDefault());
                        countryAdapter.filter(text);
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



    private void getStateRequest(final String CoId)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"state_multiple.php";//?country_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                Log.e("stateparamUsername",paramUsername);

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
                Log.e("--State --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrState= new ArrayList<beanState>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String SID=resItem.getString("state_id");
                            String SName=resItem.getString("state_name");

                            arrState.add(new beanState(SID,SName,false));

                        }

                        if(arrState.size() >0)
                        {
                            Collections.sort(arrState, new Comparator<beanState>() {
                                @Override
                                public int compare(beanState lhs, beanState rhs) {
                                    return lhs.getState_name().compareTo(rhs.getState_name());
                                }
                            });
                            stateAdapter= new StateMultiSelectionAdapter(Part_EditLocation.this, arrState,SlidingDrawer,Slidingpage,btnMenuClose,edtState,btnConfirm);
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



    private void getCityRequest(String CoId,String SaId)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramCountry = params[0];
                String paramState = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"city_multiple.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                Log.e("country",paramCountry);
                Log.e("State",paramState);

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
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrCity= new ArrayList<beanCity>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CID=resItem.getString("city_id");
                            String CName=resItem.getString("city_name");

                            arrCity.add(new beanCity(CID,CName,false));

                        }

                        if(arrCity.size() >0)
                        {
                            Collections.sort(arrCity, new Comparator<beanCity>() {
                                @Override
                                public int compare(beanCity lhs, beanCity rhs) {
                                    return lhs.getCity_name().compareTo(rhs.getCity_name());
                                }
                            });
                            cityAdapter= new CityMultiSelectionAdapter(Part_EditLocation.this, arrCity,SlidingDrawer,Slidingpage,btnMenuClose,edtCity,btnConfirm);
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

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId,SaId);
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

                                AppConstants.CountryId=resItem.getString("part_country_id");
                                AppConstants.StateId=resItem.getString("part_state_id");
                                AppConstants.CityId=resItem.getString("part_city_id");

                                String part_country_living=resItem.getString("part_country_living");
                                String part_state=resItem.getString("part_state");
                                String part_city=resItem.getString("part_city");



                                edtCountry.setText(part_country_living);
                                edtState.setText(part_state);
                                edtCity.setText(part_city);

                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditLocation.this);
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
                    Log.e("eccccc",t.getMessage());

                }

                getCountrysRequest();
                getCountries();

                getStateRequest(AppConstants.CountryId);
                getStates();

                getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                getCity();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }



   public void UpdateLocationPref(String CountryId,String StateId,String CityId,String MatriID){

       progresDialog= new ProgressDialog(Part_EditLocation.this);
       progresDialog.setCancelable(false);
       progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
       progresDialog.setIndeterminate(true);
       progresDialog.show();

       class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
       {
           @Override
           protected String doInBackground(String... params)
           {

               String paramCountryId= params[0];
               String paramStateId= params[1];
               String paramCityId= params[2];
                String paramMatriID = params[3];


               HttpClient httpClient = new DefaultHttpClient();

               String URL= AppConstants.MAIN_URL +"edit_part_location_details.php";
               Log.e("URL", "== "+URL);

               HttpPost httpPost = new HttpPost(URL);

               BasicNameValuePair PairCountryId = new BasicNameValuePair("part_country_id", paramCountryId);
               BasicNameValuePair PairStateId = new BasicNameValuePair("part_state_id", paramStateId);
               BasicNameValuePair PairCityId = new BasicNameValuePair("part_city_id", paramCityId);
               BasicNameValuePair PairMatriID = new BasicNameValuePair("matri_id", paramMatriID);


               List<NameValuePair> nameValuePairList = new ArrayList<>();

               nameValuePairList.add(PairCountryId);
               nameValuePairList.add(PairStateId);
               nameValuePairList.add(PairCityId);
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

                       Intent intLogin= new Intent(Part_EditLocation.this, MenuProfileEdit.class);
                       startActivity(intLogin);
                       finish();
                   }else
                   {
                       String msgError=responseObj.getString("message");
                       AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditLocation.this);
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

       sendPostReqAsyncTask.execute(CountryId,StateId,CityId,MatriID);

   }


}
