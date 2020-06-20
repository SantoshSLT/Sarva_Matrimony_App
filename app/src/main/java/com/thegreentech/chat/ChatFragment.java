package com.thegreentech.chat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.thegreentech.MainActivity;
import com.thegreentech.R;
import com.thegreentech.UpdateHoroScopePhotosActivity;

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

public class ChatFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    View view;
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    RecyclerView rvonlineList;
    SwipeRefreshLayout refresh;
    ImageView textEmptyView;
    ArrayList<OnlineUsersModel> list;
    Timer timer;
    int chattimer = 0;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(int sectionNumber) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        btnBack = (ImageView)view. findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) view.findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView)view. findViewById(R.id.textviewSignUp);
        rvonlineList = (RecyclerView) view.findViewById(R.id.rvonlineList);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        textEmptyView = view.findViewById(R.id.textEmptyView);
        textviewHeaderText.setText("ONLINE MEMBERS");
        btnBack.setVisibility(View.GONE);
        textviewSignUp.setVisibility(View.GONE);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())) {
                    updatestas();
                    getAllOnlineuser();


                } else {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("notification_intent"));
        timer = new Timer();
        checkOnlineStatus();
        getAllOnlineuser();
        updatestas();
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
        client.post(AppConstants.MAIN_URL + "online_list.php", new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);
                    String status = obj.getString("status");
                    String msg = obj.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        list = new ArrayList<OnlineUsersModel>();
                        SharedPreferences prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String gender = prefUpdate.getString("gender", "");
                        JSONObject responseData = obj.getJSONObject("responseData");
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {
                            OnlineUsersModel model = new OnlineUsersModel();
                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            Log.e("status", resItem.getString("online_time"));
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

                        } else {
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
        AdapterOnlineUsers adapterOnlineUsers = new AdapterOnlineUsers(getActivity(), list) {
            public void ItemClick(int pos, OnlineUsersModel model) {
                Intent ii = new Intent(getActivity(), ChatingActivity.class);
                ii.putExtra("uname", model.getUsername());
                ii.putExtra("mid", model.getMatri_id());
                ii.putExtra("profile", model.getProfile());
                ii.putExtra("tokan", model.getTokan());
                startActivity(ii);
            }
        };
        rvonlineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvonlineList.setAdapter(adapterOnlineUsers);
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
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        if (timer!=null){
            timer.cancel();

            updatestas();
            super.onDestroy();
        }

    }

    public void updatestas(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
