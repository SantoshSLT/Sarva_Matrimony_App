package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import javax.crypto.ShortBufferException;

import Adepters.BlockedUserAdapter;
import Adepters.SearchResultAdapter;
import Adepters.ShortlistedAdapter;
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


public class ShortedProfileActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
     ArrayList<String> tokans;
    private ImageView textEmptyView;
    private RecyclerView recyclerUser;
    SwipeRefreshLayout refresh;
    String noty = "";
    private ArrayList<beanUserData> arrShortListedUser;
    ProgressBar progressBar1;

    String HeaderTitle = "", PageType = "";

    SharedPreferences prefUpdate;
    String matri_id = "", gender = "";
    ProgressDialog progresDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorted_profile);
        noty = getIntent().getStringExtra("noti");
          tokans = new ArrayList<>();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(ShortedProfileActivity.this);
        matri_id = prefUpdate.getString("matri_id", "");
        gender = prefUpdate.getString("gender", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);
        refresh = findViewById(R.id.refresh);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            HeaderTitle = bundle.getString("HeaderTitle");
            PageType = bundle.getString("PageType");
        }


        textviewHeaderText.setText(HeaderTitle);
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        textEmptyView = (ImageView) findViewById(R.id.textEmptyView);
        recyclerUser = (RecyclerView) findViewById(R.id.recyclerUser);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        recyclerUser.setLayoutManager(new LinearLayoutManager(ShortedProfileActivity.this));
        recyclerUser.setHasFixedSize(true);

        arrShortListedUser = new ArrayList<beanUserData>();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (NetworkConnection.hasConnection(ShortedProfileActivity.this)) {

            getShortlistedProfileRequest(matri_id, gender);
        } else {
            AppConstants.CheckConnection(ShortedProfileActivity.this);
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShortlistedProfileRequest(matri_id, gender);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (noty.equalsIgnoreCase("chk_contact")) {
            Intent intent = new Intent(ShortedProfileActivity.this, FragmentNotification.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ShortedProfileActivity.this, MainActivity.class);
            intent.putExtra("Fragments", "ProfileEdit");
            startActivity(intent);
            finish();
        }
    }


    private void getShortlistedProfileRequest(String strMatriId, String Gender) {
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramGender = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (PageType.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "shortlisted.php";
                    Log.e("URL shortlisted", "== " + URL);
                } else if (PageType.equalsIgnoreCase("2")) {
                    URL = AppConstants.MAIN_URL + "blocklist.php";
                    Log.e("blocklist", "== " + URL);
                } else if (PageType.equalsIgnoreCase("3")) {
                    URL = AppConstants.MAIN_URL + "profile_viewd_by_me.php";
                    Log.e("profile_viewd_by_me", "== " + URL);
                } else if (PageType.equalsIgnoreCase("4")) {
                    URL = AppConstants.MAIN_URL + "profile_visited_by_i.php";
                    Log.e("profile_visited_by_i", "== " + URL);
                } else if (PageType.equalsIgnoreCase("5")) {
                    URL = AppConstants.MAIN_URL + "wath_mobileno.php";
                    Log.e("wath_mobileno", "== " + URL);
                }


                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair GenderPair = new BasicNameValuePair("gender", paramGender);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(GenderPair);

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

                Log.e("shortlisted", "==" + result);
                tokans = new ArrayList<>();
                tokans.clear();
                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrShortListedUser = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                // String user_id = resItem.getString("user_id");
                                String matri_id1 = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String birthdate = resItem.getString("birthdate");
                                String ocp_name = resItem.getString("ocp_name");
                                String height = resItem.getString("height");
                                //String Address=ocp_name;
                                String Address = resItem.getString("profile_text");
                                String city_name = resItem.getString("city_name");
                                String country_name = resItem.getString("country_name");
                                String photo1_approve = resItem.getString("photo1_approve");
                                String photo_view_status = resItem.getString("photo_view_status");
                                String photo_protect = resItem.getString("photo_protect");
                                String photo_pswd = resItem.getString("photo_pswd");
                                String gender1 = resItem.getString("gender");
                                String is_shortlisted = resItem.getString("is_shortlisted");
                                String is_blocked = resItem.getString("is_blocked");
                                String is_favourite = resItem.getString("is_favourite");
                                String user_profile_picture = resItem.getString("user_profile_picture");
                                tokans.add(resItem.getString("tokan"));
                                arrShortListedUser.add(new beanUserData(matri_id1, username, birthdate, ocp_name, height, Address, city_name,
                                        country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, is_shortlisted, is_blocked, is_favourite, user_profile_picture));

                            }

                            if (arrShortListedUser.size() > 0) {
                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                if (PageType.equalsIgnoreCase("2")) {
                                    BlockedUserAdapter adapterBlockedUser = new BlockedUserAdapter(ShortedProfileActivity.this, arrShortListedUser, recyclerUser);
                                    recyclerUser.setAdapter(adapterBlockedUser);
                                } else {
                                    recyclerUser.setVisibility(View.VISIBLE);
                                    textEmptyView.setVisibility(View.GONE);

                                    SearchResultAdapter adapterShortlistedUser = new SearchResultAdapter(ShortedProfileActivity.this, arrShortListedUser, recyclerUser,tokans);
                                    recyclerUser.setAdapter(adapterShortlistedUser);
                                    adapterShortlistedUser.notifyDataSetChanged();

                                    /*ShortlistedAdapter adapterShortlistedUser =
                                            new ShortlistedAdapter(ShortedProfileActivity.this, arrShortListedUser, recyclerUser, tokans);
                                    recyclerUser.setAdapter(adapterShortlistedUser);*/
                                }

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
                } catch (Exception t) {
                    Log.e("kdsngkjlsn", t.getMessage());
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, Gender);
    }

}
