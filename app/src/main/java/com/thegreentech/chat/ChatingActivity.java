package com.thegreentech.chat;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.thegreentech.R;
import com.thegreentech.chat.helperClass.ChatInterface;
import com.thegreentech.chat.helperClass.Chathelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Adepters.Adapter_OnlineChating;
import Models.Chat_Model;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import utills.AppConstants;
import utills.NetworkConnection;

public class ChatingActivity extends AppCompatActivity {

    ImageView btnBack;
    CircleImageView ivProfile;
    RecyclerView rvmsgs_list;
    TextView tvEmpty, tvName;
    SwipeRefreshLayout refresh;
    ProgressDialog dialog;
    ProgressBar progress_send;
    ImageView ivSend;
    EditText edt_msg;

    SharedPreferences prefUpdate;
    public static boolean isOpen = false;
    String toUserName, ToUserId, ToUSerProfile, UserProfile, tokan;

    //  Adapter_OnlineChating adapterOnlineChating;
    ArrayList<Chat_Model> mesgList;
    ChatMessageAdapter adapterOnlineChating;
    String FromUserId = "";
    ChatInterface chatInterface = new ChatInterface() {
        @Override
        public void NotifyAdapterItem(int i) {

        }

        @Override
        public void displayUsersAndImage() {

        }

        @Override
        public void setMemberNames(String names) {

        }

        @Override
        public void notifyDataSetChanged() {

        }

        @Override
        public void scrollToLast() {

        }

        @Override
        public void showProgressBar() {

        }

        @Override
        public void hideProgressBar() {

        }

        @Override
        public void notifyDataSetChanged(int i) {

        }

        @Override
        public void updateVideoProgress(int i, String id) {

        }

        @Override
        public void DialogModified() {

        }
    };
    Chathelper chathelper;
    boolean isFirst = true;
    Handler handler;
    Runnable runs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chating_activity);
        getSupportActionBar().hide();

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(ChatingActivity.this);
        UserProfile = prefUpdate.getString("profile_image", "");
        FromUserId = prefUpdate.getString("matri_id", "");

        ToUserId = getIntent().getStringExtra("mid");
        ToUSerProfile = getIntent().getStringExtra("profile");
        toUserName = getIntent().getStringExtra("uname");
        tokan = getIntent().getStringExtra("tokan");

        initilize();

        chathelper = new Chathelper(ChatingActivity.this, chatInterface, ToUserId);
        List<Chat_Model> chatList = chathelper.getChatData();
        setAdapterList(chatList);



    }


    public void initilize() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        ivProfile = findViewById(R.id.ivProfile);
        rvmsgs_list = (RecyclerView) findViewById(R.id.rvmsgs_list);
        refresh = findViewById(R.id.refresh);
        // tvEmpty = (TextView) findViewById(R.id.textEmptyView);
        tvName = (TextView) findViewById(R.id.tvName);
        edt_msg = findViewById(R.id.edt_msg);
        ivSend = findViewById(R.id.ivSend);
        progress_send = findViewById(R.id.progress_send);


        Log.e("cht_token", tokan + " " + toUserName);

        Picasso.with(getApplicationContext()).load(ToUSerProfile).into(ivProfile);
        tvName.setText(toUserName + " (" + ToUserId + ")");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialog = new ProgressDialog(ChatingActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading..");

        mesgList = new ArrayList<>();
        handler = new Handler();
        runs = new Runnable() {
            public void run() {
                getAllmessages();
            }
        };

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllmessagesrefresh();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_msg.getText().toString().trim().isEmpty() && edt_msg.getText().toString().trim().length() != 0) {
                    if (mesgList.size() >0)
                        rvmsgs_list.smoothScrollToPosition(mesgList.size() - 1);
                    if (NetworkConnection.hasConnection(ChatingActivity.this)){
                        setndMessage();


                    }else
                    {
                        AppConstants.CheckConnection(ChatingActivity.this);
                    }

                } else {
                    Toast.makeText(ChatingActivity.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


   public void getAllmessages() {
        final ArrayList<Chat_Model> tempList = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("to_id", ToUserId);
        params.put("from_id", prefUpdate.getString("matri_id", ""));
        client.post(AppConstants.MAIN_URL + "chat_history.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("CHAT-HISTORY", responseString);
                try {
                    JSONObject obj = new JSONObject(responseString);
                    String status = obj.getString("status");
                    tempList.clear();
                    mesgList.clear();
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            while (resIter.hasNext()) {
                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);
                                Chat_Model model = new Chat_Model();
                                model.setFrom_id(resItem.getString("from_id"));
                                model.setTo_id(resItem.getString("to_id"));
                                Date dd = format.parse(resItem.getString("sent"));
                                SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");
                                model.setDate(format1.format(dd));
                                model.setMsg(resItem.getString("message"));
                                model.setImgReciverProfileurl(resItem.getString("user_profile_picture"));
                                mesgList.add(model);
                            }

                            tempList.addAll(mesgList);
                            setAdapterList(tempList);
                            chathelper.saveChatData(tempList);
                            //handler.postDelayed(runs, 10000);

                        }
                    }
                } catch (Exception e) {
                    Log.e("mmmmmm", e.getMessage());
                }
            }
        });
    }

    //___________when user refresh________________

    public void getAllmessagesrefresh() {
        refresh.setRefreshing(true);
        final ArrayList<Chat_Model> tempList = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("to_id", ToUserId);
        params.put("from_id", prefUpdate.getString("matri_id", ""));
        client.post(AppConstants.MAIN_URL + "chat_history.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("CHAT-HISTORY", responseString);
                try {
                    JSONObject obj = new JSONObject(responseString);
                    String status = obj.getString("status");
                    tempList.clear();
                    mesgList.clear();
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            while (resIter.hasNext()) {
                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);
                                Chat_Model model = new Chat_Model();
                                model.setFrom_id(resItem.getString("from_id"));
                                model.setTo_id(resItem.getString("to_id"));
                                Date dd = format.parse(resItem.getString("sent"));
                                SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");
                                model.setDate(format1.format(dd));
                                model.setMsg(resItem.getString("message"));
                                model.setImgReciverProfileurl(resItem.getString("user_profile_picture"));
                                mesgList.add(model);
                            }

                            tempList.addAll(mesgList);
                            setAdapterList(tempList);
                            chathelper.saveChatData(tempList);


                        }
                        refresh.setRefreshing(false);
                    }
                    refresh.setRefreshing(false);
                } catch (Exception e) {
                    refresh.setRefreshing(false);
                    Log.e("mmmmmm", e.getMessage());
                }
                refresh.setRefreshing(false);
            }
        });
    }


    void setndMessage() {

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("from_id", FromUserId);
            params.put("to_id", ToUserId);
            params.put("message", edt_msg.getText().toString().trim());
            client.post(AppConstants.MAIN_URL + "chat_insert.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("insertchat", responseString);
                }

                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    Log.e("insertchat", responseString);
                    sendPushNotification(tokan, "Message recived from " + prefUpdate.getString("matri_id", "")
                            , "New Message Received", "201");
                    getAllmessages();
                    edt_msg.setText("");
                }
            });
        }

        public void setAdapterList(List<Chat_Model> list){
            LinearLayoutManager manager = new LinearLayoutManager(ChatingActivity.this);
            manager.setStackFromEnd(true);
            rvmsgs_list.setHasFixedSize(true);
            rvmsgs_list.setLayoutManager(manager);
            adapterOnlineChating = new ChatMessageAdapter(ChatingActivity.this, list, FromUserId, ToUserId, ToUSerProfile, UserProfile);
            rvmsgs_list.setAdapter(adapterOnlineChating);
            adapterOnlineChating.notifyDataSetChanged();
            rvmsgs_list.smoothScrollToPosition(0);
            isFirst = false;
        }

        @Override
        protected void onStart () {
            super.onStart();
            isOpen = true;

        }

        @Override
        protected void onStop () {
            super.onStop();
            isOpen = false;
            Log.e("Stop", "Stop");
            if (runs != null) {
                handler.removeCallbacks(runs);
            }
        }

        @Override
        protected void onPause () {
            super.onPause();
            isOpen = false;

        }

        @Override
        protected void onResume () {
            super.onResume();
            isOpen = true;
            getAllmessages();
        }


        void setNotification_reciver () {
            BroadcastReceiver Notification_Chat_Message = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    getAllmessages();
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConstants.Action_MessageRecived);
            registerReceiver(Notification_Chat_Message, filter);
        }

    public  void sendPushNotification(String tokan, String msg, String title, final String id)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("tokan",tokan);
        params.put("msg",msg);
        params.put("title",title);
        params.put("id",id);
        client.post(AppConstants.MAIN_URL + "user_chat_notification.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
            public void onSuccess(int statusCode, Header[] headers, String responseString)
            {
                    getAllmessages();

                Log.d("NOTIFICATION__"+id,responseString);
            }
        });

    }


    }
