package com.thegreentech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.thegreentech.chat.ChatingActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import Adepters.AdapterOnlineUsers;
import Models.OnlineUsersModel;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;
import utills.NetworkConnection;

public class OnlinemembersActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    RecyclerView rvonlineList;
    SwipeRefreshLayout refresh;
    ImageView textEmptyView;
    ArrayList<OnlineUsersModel> list;
    Timer timer;
    int chattimer = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinemembers);


        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);
        rvonlineList = (RecyclerView) findViewById(R.id.rvonlineList);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        textEmptyView =  findViewById(R.id.textEmptyView);

        textviewHeaderText.setText("ONLINE MEMBERS");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {

                if (NetworkConnection.hasConnection(OnlinemembersActivity.this)){
                    getAllOnlineuser();
                    updatestas();

                }else
                {
                    AppConstants.CheckConnection(OnlinemembersActivity.this);
                }
            }
        });
    }
    public void checkOnlineStatus(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chattimer ++;

                Log.e("chattimer",chattimer+"");
            }
        },0,20000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try
        {
            if (NetworkConnection.hasConnection(OnlinemembersActivity.this)){
                LocalBroadcastManager.getInstance(OnlinemembersActivity.this).registerReceiver(mMessageReceiver, new IntentFilter("notification_intent"));
                timer = new Timer();
                checkOnlineStatus();
                getAllOnlineuser();
                updatestas();

            }else
            {
                AppConstants.CheckConnection(OnlinemembersActivity.this);
            }
        }
        catch(Exception e)
        {
            Log.e("fkjgvkf",e.getMessage());
        }
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getAllOnlineuser();
        }
    };
    void getAllOnlineuser() {
     refresh.setRefreshing(true);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppConstants.MAIN_URL+"online_list.php", new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String status = obj.getString("status");
                    String msg = obj.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        list = new ArrayList<OnlineUsersModel>();
                        SharedPreferences prefUpdate = PreferenceManager.getDefaultSharedPreferences(OnlinemembersActivity.this);
                        String gender = prefUpdate.getString ("gender", "") ;
                        JSONObject responseData = obj.getJSONObject("responseData");
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {
                            OnlineUsersModel model = new OnlineUsersModel();
                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            Log.e("status",resItem.getString("online_time"));
                            model.setUser_id(resItem.getString("user_id"));
                            model.setMatri_id(resItem.getString("matri_id"));
                            model.setGender(resItem.getString("gender"));
                            model.setUsername(resItem.getString("username"));
                            model.setState(resItem.getString("status"));
                            model.setOnline_time(resItem.getString("online_time"));
                            model.setProfile(resItem.getString("profile_path"));
                            model.setTokan(resItem.getString("tokan"));

                            if (!gender.equalsIgnoreCase(model.getGender())) {
                                list.add(model);
                            }

                        }
                        if (list.size() > 0) {
                            rvonlineList.setVisibility(View.VISIBLE);
                            textEmptyView.setVisibility(View.GONE);
                            setAdapter();

                        }
                        else {
                            refresh.setRefreshing(false);
                            rvonlineList.setVisibility(View.GONE);
                            textEmptyView.setVisibility(View.VISIBLE);
                        }
                    }
                    refresh.setRefreshing(false);

                } catch (Exception t) {
                    refresh.setRefreshing(false);
                    rvonlineList.setVisibility(View.GONE);
                    textEmptyView.setVisibility(View.VISIBLE);
                    Log.d("RESSS", t.getMessage());
                }
                Log.d("RESSS", responseString);
            }
        });
    }

    void setAdapter() {
        AdapterOnlineUsers adapterOnlineUsers = new AdapterOnlineUsers(this, list) {
            public void ItemClick(int pos, OnlineUsersModel model) {
                Intent ii = new Intent(getApplicationContext(), ChatingActivity.class);
                ii.putExtra("uname",model.getUsername());
                ii.putExtra("mid",model.getMatri_id());
                ii.putExtra("profile",model.getProfile());
                ii.putExtra("tokan",model.getTokan());
                startActivity(ii);
            }
        };
        rvonlineList.setLayoutManager(new LinearLayoutManager(this));
        rvonlineList.setAdapter(adapterOnlineUsers);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OnlinemembersActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);
    }

    public void updatestas(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OnlinemembersActivity.this);
        String matri_id = preferences.getString("matri_id", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(System.currentTimeMillis());
        String dtSend = format.format(d);
        Log.e("date",dtSend);
        Log.e("Today is " ,""+ d.getTime());
        try {
            Log.e("Today is " ,""+ d.getTime());
        } catch (Exception e) {
            Log.e("dateexccc", e.getMessage());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", matri_id);
        params.put("online_time", dtSend);
        client.post(AppConstants.MAIN_URL + "check_online_status.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("Onlinestutas", responseString + " " + chattimer);

            }
        });
    }

}
