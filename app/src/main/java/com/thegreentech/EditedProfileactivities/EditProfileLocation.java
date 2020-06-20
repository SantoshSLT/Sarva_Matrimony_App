package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;
import com.thegreentech.SignUpStep1Activity;

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

import Adepters.CityAdapter;
import Adepters.CountryAdapter;
import Adepters.StateAdapter;
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
import utills.RecyclerTouchListener;

public class EditProfileLocation extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    ImageView btnMenuClose;
    Button  btnUpdate;
    LinearLayout Slidingpage,linGeneralView;
    RelativeLayout SlidingDrawer;
    LinearLayout linCountry,linState,linCity;
    EditText edtSearchCountry,edtSearchState,edtSearchCity;
    EditText edtCountry,edtState,edtCity;
    RecyclerView rvCountry,rvState,rvCity/*rvGeneralView*/;
  //  Button btnConfirm;

    ArrayList<beanCountries> arrCountry=new ArrayList<beanCountries>();
    CountryAdapter countryAdapter=null;

    ArrayList<beanState> arrState=new ArrayList<beanState>();
    StateAdapter stateAdapter=null;

    ArrayList<beanCity> arrCity=new ArrayList<beanCity>();
    CityAdapter cityAdapter=null;


    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_location);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(EditProfileLocation.this);
        matri_id=prefUpdate.getString("matri_id","");


        init();
        SlidingMenu();
        onclick();

        if (NetworkConnection.hasConnection(EditProfileLocation.this)){
            getProfile(matri_id);
        }else
        {
            AppConstants.CheckConnection( EditProfileLocation.this);
        }






        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getCountrysRequest();
            getCountries();


            getCityRequest(AppConstants.CountryId,AppConstants.StateId);
            getCity();


        }else
        {
            AppConstants.CheckConnection(EditProfileLocation.this);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(EditProfileLocation.this)){
            getProfile(matri_id);
        }else
        {
            AppConstants.CheckConnection( EditProfileLocation.this);
        }
    }

    public  void init(){
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Location Information");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtCountry=(EditText)findViewById(R.id.edtCountry);
        edtState=(EditText)findViewById(R.id.edtState);
        edtCity=(EditText)findViewById(R.id.edtCity);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        Slidingpage=findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        linGeneralView=findViewById(R.id.linGeneralView);

    }

    public  void  onclick(){
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

                Log.e("btn_country", AppConstants.CountryId);
                Log.e("btn_state",AppConstants.StateId);
                Log.e("btn_city", AppConstants.CityId);
                if (hasData(AppConstants.CountryId)
                        && hasData(AppConstants.StateId)
                        && hasData(AppConstants.CityId))
                       {
                           updateLocation(matri_id,AppConstants.CountryId,AppConstants.StateId,AppConstants.CityId);

                       }
                else
                {
                    Toast.makeText(EditProfileLocation.this, "Not Valid Data", Toast.LENGTH_SHORT).show();

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
                linCity.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCountry.setVisibility(View.VISIBLE);

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
                edtState.setFocusable(true);
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);

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
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);

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


        rvState.addOnItemTouchListener(new RecyclerTouchListener(EditProfileLocation.this, rvState, new SignUpStep1Activity.ClickListener()
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

                if (arrCity != null && arrCity.size() !=0) {
                    beanCity arrCo = arrCity.get(position);
                    AppConstants.CityName = arrCo.getCity_name();
                    AppConstants.CityId = arrCo.getCity_id();
                }
                else {
                    Toast.makeText(EditProfileLocation.this, "Select State ", Toast.LENGTH_SHORT).show();
                }
                edtCity.setText(AppConstants.CityName);

                GonelidingDrower();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



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
        AnimUtils.SlideAnimation(EditProfileLocation.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);
    }

    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileLocation.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu()
    {

        linCountry=findViewById(R.id.linCountry);
        linState=findViewById(R.id.linState);
        linCity=findViewById(R.id.linCity);

        edtSearchCountry=findViewById(R.id.edtSearchCountry);
        edtSearchState=findViewById(R.id.edtSearchState);
        edtSearchCity=findViewById(R.id.edtSearchCity);

        rvCountry=findViewById(R.id.rvCountry);
        rvState= findViewById(R.id.rvState);
        rvCity=findViewById(R.id.rvCity);

        rvCountry.setLayoutManager(new LinearLayoutManager(this));
        rvCountry.setHasFixedSize(true);

        rvState.setLayoutManager(new LinearLayoutManager(this));
        rvState.setHasFixedSize(true);

        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setHasFixedSize(true);


//        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
//        rvGeneralView.setHasFixedSize(true);
//


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
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"country.php";
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

                            arrCountry.add(new beanCountries(CoID,CoName));

                        }

                        if(arrCountry.size() >0)
                        {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
                            countryAdapter = new CountryAdapter(EditProfileLocation.this, arrCountry,SlidingDrawer,Slidingpage,edtCountry);
                            rvCountry.setAdapter(countryAdapter);
                            getStateRequest(AppConstants.CountryId);
                            getStates();
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                }
                finally {

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
                String paramcountryID = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"state.php";//?country_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                Log.e("countryID",paramcountryID);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("country_id", paramcountryID);

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
                //progresDialog.dismiss();
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
                            stateAdapter= new StateAdapter(EditProfileLocation.this, arrState,SlidingDrawer,Slidingpage,btnMenuClose,edtState);
                            rvState.setAdapter(stateAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {
                    Log.e("ex",e.getMessage());

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
                            cityAdapter= new CityAdapter(EditProfileLocation.this, arrCity,SlidingDrawer,Slidingpage,btnMenuClose,edtCity);
                            rvCity.setAdapter(cityAdapter);
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

    private void getProfile(String strMatriId)
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

                                edtCountry.setText(resItem.getString("country_name"));
                                AppConstants.CountryId=resItem.getString("country_id");

                                edtState.setText(resItem.getString("state_name"));
                                AppConstants.StateId = resItem.getString("state_id");

                                edtCity.setText(resItem.getString("city_name"));
                                AppConstants.CityId=resItem.getString("city");

                                Log.e("Profile_country", AppConstants.CountryId);
                                Log.e("Profile_state",AppConstants.StateId);
                                Log.e("Profile_city", AppConstants.CityId);

                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileLocation.this);
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


                } catch (Throwable t)
                {
                }


                getCountrysRequest();
                getCountries();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public void updateLocation(String matriID,String CountryIds,String StateId,String CityId){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramMatriId = params[0];
                String paramCountryID = params[1];
                String paramStateID = params[2];
                String paramCityID = params[3];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_location_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair pairMatriID = new BasicNameValuePair("matri_id", paramMatriId);
                BasicNameValuePair pairCountryID = new BasicNameValuePair("country_id", paramCountryID);
                BasicNameValuePair pairStateID = new BasicNameValuePair("state_id", paramStateID);
                BasicNameValuePair pairCityID = new BasicNameValuePair("city_id", paramCityID);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(pairMatriID);
                nameValuePairList.add(pairCountryID);
                nameValuePairList.add(pairStateID);
                nameValuePairList.add(pairCityID);


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
                //  progresDialog.dismiss();
                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
//                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String message=responseObj.getString("message");
                        Intent intLogin= new Intent(EditProfileLocation.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileLocation.this);
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
                    Log.e("exaception",""+e);
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(matriID,CountryIds,StateId,CityId);
    }



}

