package com.thegreentech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adepters.SavedSearchResultAdapter;
import Models.beanSaveSearch;
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


public class SavedSearchResultActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;


    private ImageView textEmptyView;
    private RecyclerView recyclerUser;

    private ArrayList<beanSaveSearch> arrSaveSearchResultList;
    ProgressBar progressBar1;
    SwipeRefreshLayout refresh;

    SharedPreferences prefUpdate;
    String matri_id="";
    String typeOnBack="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(SavedSearchResultActivity.this);
        matri_id=prefUpdate.getString("matri_id","");

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewHeaderText.setText("Saved Search");
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        typeOnBack = getIntent().getStringExtra("search_Saved");

        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        refresh= findViewById(R.id.refresh);
        textEmptyView = (ImageView) findViewById(R.id.textEmptyView);
        recyclerUser = (RecyclerView) findViewById(R.id.recyclerUser);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1 );
        recyclerUser.setLayoutManager(new LinearLayoutManager(SavedSearchResultActivity.this));
        recyclerUser.setHasFixedSize(true);

        arrSaveSearchResultList = new ArrayList<beanSaveSearch>();

       /* Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            textviewHeaderText.setText("SAVED SEARCH RESULT");

            String  MatriId = bundle.getString("MatriId");

                String Gender=bundle.getString("Gender");
                String AgeM=bundle.getString("AgeM");
                String AgeF=bundle.getString("AgeF");
                String HeightM=bundle.getString("HeightM");
                String HeightF=bundle.getString("HeightF");
                String MaritalStatus=bundle.getString("MaritalStatus");
                String PhysicalStatus=bundle.getString("PhysicalStatus");
                String ReligionId=bundle.getString("ReligionId");
                String CasteId=bundle.getString("CasteId");
                String CountryId=bundle.getString("CountryId");
                String StateId=bundle.getString("StateId");
                String CityId=bundle.getString("CityId");
                String HighestEducationId=bundle.getString("HighestEducationId");
                String OccupationId=bundle.getString("OccupationId");
                String AnnualIncome=bundle.getString("AnnualIncome");
                String Manglik=bundle.getString("Manglik");
                String Star=bundle.getString("Star");

                 getSavedSearchResultList(MatriId,matri_id);


            *//*getSavedSearchResultList(Gender,AgeM,AgeF,HeightM,HeightF,MaritalStatus,PhysicalStatus,ReligionId,
                        CasteId,CountryId,StateId,CityId,HighestEducationId,OccupationId,AnnualIncome,Manglik,Star,matri_id);*//*
        }
*/
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(SavedSearchResultActivity.this)){
                    getSavedSearchResultList(matri_id);

                }else
                {
                    AppConstants.CheckConnection(SavedSearchResultActivity.this);
                }
            }
        });

        if (NetworkConnection.hasConnection(SavedSearchResultActivity.this)){
            getSavedSearchResultList(matri_id);

        }else
        {
            AppConstants.CheckConnection(SavedSearchResultActivity.this);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (typeOnBack.equalsIgnoreCase("menu_search_Saved")) {
            Intent intent = new Intent(SavedSearchResultActivity.this, MainActivity.class);
            intent.putExtra("Fragments", "ProfileEdit");
            startActivity(intent);
        }
        else if (typeOnBack.equalsIgnoreCase("frag_search_Saved"))
        {
            Intent intent = new Intent(SavedSearchResultActivity.this, MainActivity.class);
            intent.putExtra("Fragments", "search_frag");
            startActivity(intent);

            MainActivity.tab = MainActivity.tabLayout.getTabAt(1);
            MainActivity.tab.select();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrSaveSearchResultList=null;
        recyclerUser.setAdapter(null);
    }

    private void getSavedSearchResultList(String strMatriId)
    {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"saved_search.php";
                Log.e("seved_search", "== "+URL);

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

                Log.e("seved_search", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        arrSaveSearchResultList= new ArrayList<beanSaveSearch>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext())
                            {
                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String ss_id=resItem.getString("ss_id");
                                String ss_name=resItem.getString("ss_name");
                                String matri_id=resItem.getString("matri_id");
                                String fromage=resItem.getString("fromage");
                                String toage=resItem.getString("toage");
                                String from_height=resItem.getString("from_height");
                                String to_height=resItem.getString("to_height");
                                String marital_status=resItem.getString("marital_status");
                                String religion=resItem.getString("religion");
                                String caste=resItem.getString("caste");
                                String country=resItem.getString("country");
                                String state=resItem.getString("state");
                                String city=resItem.getString("city");
                                String education=resItem.getString("education");
                                String occupation=resItem.getString("occupation");
                                String annual_income=resItem.getString("annual_income");
                                String star=resItem.getString("star");
                                String manglik=resItem.getString("manglik");
                                String saveDate = resItem.getString("save_date");


                                arrSaveSearchResultList.add(new beanSaveSearch(ss_id,fromage,toage,from_height,to_height,marital_status,"",religion,
                                        caste,country,state,city,education,occupation,
                                        annual_income,star,manglik,ss_name,saveDate));
                            }

                            if(arrSaveSearchResultList.size() > 0)
                            {
                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                SavedSearchResultAdapter adapterShortlistedUser = new SavedSearchResultAdapter(SavedSearchResultActivity.this,arrSaveSearchResultList, recyclerUser);
                                recyclerUser.setAdapter(adapterShortlistedUser);
                            }else
                            {
                                recyclerUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
                        }



                    }else
                    {

                        recyclerUser.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                      /*  String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SavedSearchResultActivity.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();*/
                    }
                    progressBar1.setVisibility(View.GONE);
                } catch (Exception t)
                {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }



}
