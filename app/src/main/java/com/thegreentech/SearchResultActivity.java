package com.thegreentech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import Adepters.SearchResultAdapter;
import Models.beanUserData;
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


public class SearchResultActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    ArrayList<String> tokans;
    private ImageView textEmptyView;
    private RecyclerView recyclerUser;
    SwipeRefreshLayout refresh;
    private ArrayList<beanUserData> arrSearchResultList;
    ProgressBar progressBar1;

    SearchResultAdapter adapterShortlistedUser;
    SharedPreferences prefUpdate;
    String matri_id = "";
    String isType = "";
    String MatriId = "", Gender = "";
     String AgeM="", AgeF="", HeightM="", HeightF="", MaritalStatus="", PhysicalStatus="", ReligionId="", CasteId="", CountryId="",
            StateId="", CityId="", HighestEducationId="",
            OccupationId="", AnnualIncome="", MotherToungueID="", Diet="", Smoke="", Drink="", Raasi="",Photo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(SearchResultActivity.this);
        matri_id = prefUpdate.getString("matri_id", "");
        Gender = prefUpdate.getString("gender", "");

        btnBack = findViewById(R.id.btnBack);
        textviewHeaderText = findViewById(R.id.textviewHeaderText);
        textviewSignUp = findViewById(R.id.textviewSignUp);

        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        refresh = findViewById(R.id.refresh);
        textEmptyView = findViewById(R.id.textEmptyView);
        recyclerUser = findViewById(R.id.recyclerUser);
        progressBar1 = findViewById(R.id.progressBar1);
        recyclerUser.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
        recyclerUser.setHasFixedSize(true);

        arrSearchResultList = new ArrayList<beanUserData>();


        getIntetData();



    }

    public void getIntetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle != null) {
            String SearchType = bundle.getString("SearchType");
            if (SearchType.equalsIgnoreCase("byId")) {
                MatriId = bundle.getString("MatriId");
                isType = bundle.getString("istype");
                textviewHeaderText.setText("SEARCH BY MATRI ID");
                if (NetworkConnection.hasConnection(SearchResultActivity.this)) {
                    getSearchByMatriIDRequest(MatriId, matri_id, Gender);

                } else {
                    AppConstants.CheckConnection(SearchResultActivity.this);
                }


            } else if (SearchType.equalsIgnoreCase("bydata")) {
                textviewHeaderText.setText("SEARCH RESULT");
                Gender = bundle.getString("Gender");
                Log.e("genderr", Gender);
                AgeM = bundle.getString("AgeM");
                AgeF = bundle.getString("AgeF");
                HeightM = bundle.getString("HeightM");
                HeightF = bundle.getString("HeightF");
                MaritalStatus = bundle.getString("MaritalStatus");
                PhysicalStatus = bundle.getString("PhysicalStatus");
                ReligionId = bundle.getString("ReligionId");
                CasteId = bundle.getString("CasteId");
                CountryId = bundle.getString("CountryId");
                StateId = bundle.getString("StateId");
                CityId = bundle.getString("CityId");
                HighestEducationId = bundle.getString("HighestEducationId");
                OccupationId = bundle.getString("OccupationId");
                AnnualIncome = bundle.getString("AnnualIncome");
                MotherToungueID = bundle.getString("MotherToungueID");
                Diet = bundle.getString("Diet");
                Smoke = bundle.getString("Smoking");
                Drink = bundle.getString("Drinking");
                Raasi = bundle.getString("Rassi");
                Photo = bundle.getString("Photo");



                if (NetworkConnection.hasConnection(SearchResultActivity.this)) {

                    getSearchResultRequest(Gender, AgeM, AgeF, HeightM, HeightF, MaritalStatus, PhysicalStatus, ReligionId,
                            CasteId, CountryId, StateId, CityId, HighestEducationId, OccupationId, AnnualIncome,
                            matri_id, MotherToungueID, Diet, Smoke, Drink, Raasi,Photo);

                } else {
                    AppConstants.CheckConnection(SearchResultActivity.this);
                }

            }


        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MatriId.equalsIgnoreCase("")) {
                    if (NetworkConnection.hasConnection(SearchResultActivity.this)) {
                        getSearchByMatriIDRequest(MatriId, matri_id, Gender);

                    } else {
                        AppConstants.CheckConnection(SearchResultActivity.this);
                    }

                } else {
                    if (NetworkConnection.hasConnection(SearchResultActivity.this)) {

                        getSearchResultRequest(Gender, AgeM, AgeF, HeightM, HeightF, MaritalStatus, PhysicalStatus, ReligionId,
                                CasteId, CountryId, StateId, CityId, HighestEducationId, OccupationId, AnnualIncome,
                                matri_id, MotherToungueID, Diet, Smoke, Drink, Raasi,Photo);

                    } else {
                        AppConstants.CheckConnection(SearchResultActivity.this);
                    }
                }


            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "search_frag");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        arrSearchResultList = null;
//        recyclerUser.setAdapter(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntetData();
    }

    private void getSearchByMatriIDRequest(String strMatriId, String login_matriId, String Gender) {
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsLoginMatriId = params[1];
                String paramsGender = params[2];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "matri_search.php";
                Log.e("matri_search", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair GenderPair = new BasicNameValuePair("gender", paramsGender);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair MatriSeachIdPair = new BasicNameValuePair("login_matri_id", paramsLoginMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(GenderPair);
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(MatriSeachIdPair);


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
                tokans = new ArrayList<>();
                Log.e("Search by maitri Id", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrSearchResultList = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                //  String user_id = resItem.getString("user_id");
                                String matri_id1 = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String birthdate = resItem.getString("birthdate");
                                String ocp_name = resItem.getString("ocp_name");
                                String height = resItem.getString("height");
                                String Address = resItem.getString("profile_text");
                                String city_name = resItem.getString("city_name");
                                String country_name = resItem.getString("country_name");
                                String photo1_approve = resItem.getString("photo1_approve");
                                String photo_view_status = resItem.getString("photo_view_status");
                                String photo_protect = resItem.getString("photo_protect");
                                String photo_pswd = resItem.getString("photo_pswd");
                                String gender1 = resItem.getString("gender");
                                AppConstants.is_shortlisted = resItem.getString("is_shortlisted");
                                AppConstants.is_blocked = resItem.getString("is_blocked");
                                String is_favourite = resItem.getString("is_favourite");
                                String user_profile_picture = resItem.getString("user_profile_picture");
                                tokans.add(resItem.getString("tokan"));

                                arrSearchResultList.add(new beanUserData(matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, AppConstants.is_shortlisted, AppConstants.is_blocked, is_favourite, user_profile_picture));

                            }

                            if (arrSearchResultList.size() > 0) {
                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                adapterShortlistedUser = new SearchResultAdapter(SearchResultActivity.this, arrSearchResultList, recyclerUser, tokans);
                                recyclerUser.setAdapter(adapterShortlistedUser);
                                adapterShortlistedUser.notifyDataSetChanged();
                            } else {
                                recyclerUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
                        }


                    } else {
                        String msgError = obj.getString("message");
                        recyclerUser.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }


                    refresh.setRefreshing(false);
                } catch (Exception t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, login_matriId, Gender);
    }


    private void getSearchResultRequest(final String Gender, String AgeM, String AgeF, String HeightM, String HeightF,
                                        String MaritalStatus, String PhysicalStatus, String ReligionId,
                                        String CasteId, String CountryId, String StateId, String CityId, String HighestEducationId,
                                        String OccupationId, String AnnualIncome,
                                        String login_matriId, String MotherToungueID, String Diet, String Smoke, String Drink, String Raasi,String Photo) {
        refresh.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramGender = params[0];
                String paramAgeM = params[1];
                String paramAgeF = params[2];
                String paramHeightM = params[3];
                String paramHeightF = params[4];
                String paramMaritalStatus = params[5];
                String paramPhysicalStatus = params[6];
                String paramReligionId = params[7];
                String paramCasteId = params[8];
                String paramCountryId = params[9];
                String paramStateId = params[10];
                String paramCityId = params[11];
                String paramEducationId = params[12];
                String paramOccupationID = params[13];
                String paramAnnualIncome = params[14];

                String paramsLoginMatriId = params[15];
                String paramsMotherTounguId = params[16];
                String paramsDiet = params[17];
                String paramsSmoke = params[18];
                String paramsDrink = params[19];
                String paramsRaasi = params[20];
                String paramsPhoto = params[21];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "search.php";
                Log.e("URL", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair PairGender = new BasicNameValuePair("gender", paramGender);
                BasicNameValuePair PairAgeM = new BasicNameValuePair("fromage", paramAgeM);
                BasicNameValuePair PairAgeF = new BasicNameValuePair("toage", paramAgeF);
                BasicNameValuePair PairHeightM = new BasicNameValuePair("fromheight", paramHeightM);
                BasicNameValuePair PairHeightF = new BasicNameValuePair("toheight", paramHeightF);
                BasicNameValuePair PairMaritalStatus = new BasicNameValuePair("m_status", paramMaritalStatus);
                BasicNameValuePair PairPhysicalStatus = new BasicNameValuePair("physical_status", paramPhysicalStatus);
                BasicNameValuePair PairReligionId = new BasicNameValuePair("religion", paramReligionId);
                BasicNameValuePair PairCasteId = new BasicNameValuePair("caste", paramCasteId);
                BasicNameValuePair PairCountryId = new BasicNameValuePair("country", paramCountryId);
                BasicNameValuePair PairStateId = new BasicNameValuePair("state", paramStateId);
                BasicNameValuePair PairCityId = new BasicNameValuePair("city", paramCityId);
                BasicNameValuePair PairEducationId = new BasicNameValuePair("education", paramEducationId);
                BasicNameValuePair PairOccupationID = new BasicNameValuePair("occupation", paramOccupationID);
                BasicNameValuePair PairAnnualIncome = new BasicNameValuePair("annual_income", paramAnnualIncome);
                BasicNameValuePair MatriSeachIdPair = new BasicNameValuePair("login_matri_id", paramsLoginMatriId);
                BasicNameValuePair PairMotherToungue = new BasicNameValuePair("mother_tongue_id", paramsMotherTounguId);
                BasicNameValuePair DietPair = new BasicNameValuePair("diet", paramsDiet);
                BasicNameValuePair SmokingPair = new BasicNameValuePair("smoke", paramsSmoke);
                BasicNameValuePair DrinkingPair = new BasicNameValuePair("drink", paramsDrink);
                BasicNameValuePair RassiPair = new BasicNameValuePair("raasi", paramsRaasi);
                BasicNameValuePair photoPair = new BasicNameValuePair("photo_search",paramsPhoto);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(PairGender);
                nameValuePairList.add(PairAgeM);
                nameValuePairList.add(PairAgeF);
                nameValuePairList.add(PairHeightM);
                nameValuePairList.add(PairHeightF);
                nameValuePairList.add(PairMaritalStatus);
                nameValuePairList.add(PairPhysicalStatus);
                nameValuePairList.add(PairReligionId);
                nameValuePairList.add(PairCasteId);
                nameValuePairList.add(PairCountryId);
                nameValuePairList.add(PairStateId);
                nameValuePairList.add(PairCityId);
                nameValuePairList.add(PairEducationId);
                nameValuePairList.add(PairOccupationID);
                nameValuePairList.add(PairAnnualIncome);
                nameValuePairList.add(MatriSeachIdPair);
                nameValuePairList.add(PairMotherToungue);
                nameValuePairList.add(DietPair);
                nameValuePairList.add(SmokingPair);
                nameValuePairList.add(DrinkingPair);
                nameValuePairList.add(RassiPair);
                nameValuePairList.add(photoPair);


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

                Log.e("--Search by Result --", "==" + Ressponce);
                tokans = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(Ressponce);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrSearchResultList = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String user_id = resItem.getString("user_id");
                                String matri_id1 = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String birthdate = resItem.getString("birthdate");
                                String ocp_name = resItem.getString("ocp_name");
                                String height = resItem.getString("height");
                                String Address = resItem.getString("profile_text");
                                String city_name = resItem.getString("city_name");
                                String country_name = resItem.getString("country_name");
                                String photo1_approve = resItem.getString("photo1_approve");
                                String photo_view_status = resItem.getString("photo_view_status");
                                String photo_protect = resItem.getString("photo_protect");
                                String photo_pswd = resItem.getString("photo_pswd");
                                String gender1 = resItem.getString("gender");
                                AppConstants.is_shortlisted = resItem.getString("is_shortlisted");
                                AppConstants.is_blocked = resItem.getString("is_blocked");

                                String is_favourite = resItem.getString("is_favourite");
                                String user_profile_picture = resItem.getString("user_profile_picture");
                                tokans.add(resItem.getString("tokan"));
                                arrSearchResultList.add(new beanUserData(user_id, matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, AppConstants.is_shortlisted, AppConstants.is_blocked, is_favourite, user_profile_picture));

                            }

                            if (arrSearchResultList.size() > 0) {
                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                adapterShortlistedUser = new SearchResultAdapter(SearchResultActivity.this, arrSearchResultList, recyclerUser, tokans);
                                recyclerUser.setAdapter(adapterShortlistedUser);
                                adapterShortlistedUser.notifyDataSetChanged();

                            } else {
                                recyclerUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
                        }


                    } else {
                        recyclerUser.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }


                    refresh.setRefreshing(false);
                } catch (Throwable t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(Gender, AgeM, AgeF, HeightM, HeightF, MaritalStatus, PhysicalStatus, ReligionId,
                CasteId, CountryId, StateId, CityId, HighestEducationId, OccupationId, AnnualIncome,
                login_matriId, MotherToungueID, Diet, Smoke, Drink, Raasi,Photo);

    }


}
