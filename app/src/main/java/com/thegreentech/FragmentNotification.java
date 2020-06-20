package com.thegreentech;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adepters.Adapter_notification;
import Models.Notification_Model;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;
import utills.NetworkConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotification extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    SwipeRefreshLayout refresh;
    SharedPreferences prefUpdate;
    private static final String ARG_SECTION_NUMBER = "section_number";
    ArrayList<Notification_Model> notification_models = new ArrayList<>();
    Adapter_notification adapter_notification;
    ImageView textEmptyView;
    RecyclerView rv_notification;

    public FragmentNotification() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_notification);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(FragmentNotification.this);
        refresh = findViewById(R.id.refresh);
        textEmptyView = findViewById(R.id.textEmptyView);
        rv_notification = findViewById(R.id.rv_notification);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);


        textviewHeaderText.setText("NOTIFICATIONS");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.VISIBLE);
        textviewSignUp.setText("CLEAR");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {

                CallAllnotification();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notification_models.size() > 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentNotification.this);
                    builder.setMessage("Do you want to clear all Notifications?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            notification_models.removeAll(notification_models);
                            adapter_notification.clearData();
                            CallApIClearNotiFications();
                            textEmptyView.setVisibility(View.VISIBLE);
                            rv_notification.setVisibility(View.GONE);

                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
                else
                {
                    Toast.makeText(FragmentNotification.this, "You have no Notification", Toast.LENGTH_SHORT).show();
                }
            }

            });
        }


        public static FragmentNotification newInstance () {
            FragmentNotification fragment = new FragmentNotification();
            return fragment;
        }

        @Override
        protected void onResume () {
            super.onResume();
            CallAllnotification();
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            Intent intent = new Intent(FragmentNotification.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }

        public void CallAllnotification () {
            refresh.setRefreshing(true);
            notification_models = new ArrayList<>();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("matri_id", prefUpdate.getString("matri_id", ""));
            params.put("gender", prefUpdate.getString("gender", ""));

            client.post(AppConstants.MAIN_URL + "app_notification.php", params, new TextHttpResponseHandler() {
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    refresh.setRefreshing(false);

                    try {
                        JSONObject obj = new JSONObject(responseString);

                        String status = obj.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            JSONObject responseData = obj.getJSONObject("responseData");
                            Log.e("respone", responseData + "");
                            if (responseData.has("1")) {
                                Iterator<String> resIter = responseData.keys();

                                while (resIter.hasNext()) {

                                    String key = resIter.next();
                                    JSONObject resItem = responseData.getJSONObject(key);

                                    Notification_Model model = new Notification_Model();
//                                model.setReceiver_id(resItem.getString("receiver_id"));
//                                model.setSender_id(resItem.getString("sender_id"));
                                    // model.setReminder_view_status(resItem.getString("reminder_view_status"));
                                    model.setReminder_mes_type(resItem.getString("notification_type"));
                                    model.setImgProfileUrl(resItem.getString("user_profile_picture"));
                                    model.setMatriId(resItem.getString("matri_id"));
                                    notification_models.add(model);
                                }

                                if (notification_models.size() > 0) {
                                    rv_notification.setVisibility(View.VISIBLE);
                                    textEmptyView.setVisibility(View.GONE);

                                    LinearLayoutManager manager = new LinearLayoutManager(FragmentNotification.this, LinearLayoutManager.VERTICAL, false);
                                    rv_notification.setLayoutManager(manager);
                                    adapter_notification = new Adapter_notification(notification_models, FragmentNotification.this);
                                    rv_notification.setAdapter(adapter_notification);

                                } else {
                                    rv_notification.setVisibility(View.GONE);
                                    textEmptyView.setVisibility(View.VISIBLE);
                                }
                            }
                            refresh.setRefreshing(false);
                        } else {
                            refresh.setRefreshing(false);
                            Log.e("respone", status + "");
                            textEmptyView.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception t) {
                        refresh.setRefreshing(false);

                        Log.d("ERRRR", t.toString());
                    }
                }
            });
        }


        public void CallApIClearNotiFications () {
            refresh.setRefreshing(true);
            notification_models = new ArrayList<>();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("matri_id", prefUpdate.getString("matri_id", ""));
            params.put("hide_notification", "No");

            client.post(AppConstants.MAIN_URL + "remove_notification.php", params, new TextHttpResponseHandler() {
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    refresh.setRefreshing(false);

                    try {
                        JSONObject obj = new JSONObject(responseString);

                        String status = obj.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            JSONObject responseData = obj.getJSONObject("responseData");
                            Log.e("responess", responseData + "");
                            if (responseData.has("1")) {
                                Iterator<String> resIter = responseData.keys();


                                notification_models.removeAll(notification_models);
                                rv_notification.setVisibility(View.GONE);
                                rv_notification.setAdapter(null);
                                textEmptyView.setVisibility(View.VISIBLE);
                                CallAllnotification();
                            }
                            refresh.setRefreshing(false);
                        } else {
                            refresh.setRefreshing(false);
                            Log.e("respone", status + "");

                            textEmptyView.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception t) {
                        refresh.setRefreshing(false);

                        Log.d("ERRRR", t.toString());
                    }
                }
            });
        }
    }
