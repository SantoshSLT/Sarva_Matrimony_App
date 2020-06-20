package com.thegreentech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import Adepters.MessageSendAdapter;
import Adepters.UserListMatriIdListAdapter;
import Models.MessageSend;
import Models.userListMatriList.UserListMatriIdListModel;
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

public class ComposeMessage extends AppCompatActivity implements UserListMatriIdListAdapter.setOnClickLis, MessageSendAdapter.setOnClickLis {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    LinearLayout Slidingpage, linGeneralView, linSendMessage;
    RelativeLayout SlidingDrawer;
    RecyclerView rvGeneralView, rvSendMessage;
    EditText edtSearchsendMessage;
    Button btnConfirm;
    ImageView btnMenuClose;
    ArrayList<MessageSend> arrCity = new ArrayList<>();
    MessageSendAdapter sendAdapter = null;
    String token_ = "";

    SharedPreferences prefUpdate;
    String matri_id = "";
    String gender = "";

    EditText edtMatriId, edtSubject, edtMessage;
    Button btnLogin;

    ProgressDialog progresDialog;
    EditText edtSearchState;
    RecyclerView rvState;
    UserListMatriIdListAdapter userListMatriIdListAdapter;
    ArrayList<UserListMatriIdListModel> userListMatriIdListModels = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(ComposeMessage.this);
        matri_id = prefUpdate.getString("matri_id", "");
        gender = prefUpdate.getString("gender", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        Bundle extras = getIntent().getExtras();
        String value;

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Message");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtSearchState = findViewById(R.id.edtSearchState);
        rvState = findViewById(R.id.rvState);
        userListMatriIdListAdapter = new UserListMatriIdListAdapter(this, userListMatriIdListModels);
        rvState.setAdapter(userListMatriIdListAdapter);
        rvState.setLayoutManager(new LinearLayoutManager(this));
        userListMatriIdListAdapter.clickOnUserListMatriId(this);

        edtMatriId = (EditText) findViewById(R.id.edtMatriId);
        edtSubject = (EditText) findViewById(R.id.edtSubject);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);
        edtSearchsendMessage = (EditText) findViewById(R.id.edtSearchsendMessage);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setVisibility(View.GONE);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        Slidingpage.setVisibility(View.GONE);
        rvSendMessage = findViewById(R.id.rvSendMessage);
        linSendMessage = findViewById(R.id.linSendMessage);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        btnMenuClose.setVisibility(View.GONE);
        SlidingMenu();

        if (extras != null) {
            value = extras.getString("toId");
            edtMatriId.setText(value);
        }

        edtMatriId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                openMatriIdAndUserList();
                return false;
            }
        });

        edtMatriId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMatriIdAndUserList();
            }
        });

        if (!edtMatriId.getText().toString().trim().equalsIgnoreCase("")) {
            edtMatriId.setFocusable(false);
            edtMatriId.setFocusableInTouchMode(false);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvSendMessage.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvSendMessage, new SignUpStep1Activity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvSendMessage.getWindowToken(), 0);

                MessageSend arrCo = arrCity.get(position);
                AppConstants.UserName = arrCo.getUser_name();
                AppConstants.UserID = arrCo.getUser_id();


                edtMatriId.setText(AppConstants.UserName);

                GonelidingDrower();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstants.UserID = edtMatriId.getText().toString().trim();
                String strSubject = edtSubject.getText().toString().trim();
                String strMessage = edtMessage.getText().toString().trim();

                if (AppConstants.UserID.equalsIgnoreCase("")) {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_username_and_password));
                    Toast.makeText(ComposeMessage.this, "Enter Sender Matri ID", Toast.LENGTH_LONG).show();
                } else if (strSubject.equalsIgnoreCase("")) {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_username));
                    Toast.makeText(ComposeMessage.this, "Enter Subject", Toast.LENGTH_LONG).show();
                } else if (strMessage.equalsIgnoreCase("")) {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_username));
                    Toast.makeText(ComposeMessage.this, "Enter Message", Toast.LENGTH_LONG).show();
                } else {

                    if (NetworkConnection.hasConnection(getApplicationContext())) {

                        Log.e("messageData", "" + matri_id + " " + AppConstants.UserID + " " + token_);
                        sendSendMessagesRequest(matri_id, AppConstants.UserID, strSubject, strMessage);
                        AppConstants.sendPushNotification(token_,
                                "New Message Received" + " " + matri_id,
                                "New Message", AppConstants.express_intress_id);
                    } else {
                        AppConstants.CheckConnection((Activity) getApplicationContext());
                    }

                }
            }
        });
    }

    public void openMatriIdAndUserList() {

        VisibleSlidingDrower();

        edtMatriId.setError(null);
        linSendMessage.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.GONE);
        linGeneralView.setVisibility(View.GONE);

        if (NetworkConnection.hasConnection(getApplicationContext())) {
            getMAtriIdandUserList(matri_id, gender);
            getMatriID();
        } else {
            AppConstants.CheckConnection(ComposeMessage.this);
        }

    }

    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(ComposeMessage.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(ComposeMessage.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu() {
        rvSendMessage.setLayoutManager(new LinearLayoutManager(this));
        rvSendMessage.setHasFixedSize(true);
        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });

    }


    private void sendSendMessagesRequest(String FromId, String toId, String strSubject, String strMessage) {
        progresDialog = new ProgressDialog(ComposeMessage.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(ComposeMessage.this.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsFromMId = params[0];
                String paramsToMId = params[1];
                String paramsSubject = params[2];
                String paramsMesssage = params[3];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "send_message.php";
                Log.e("URL send_message", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair FromIDPair = new BasicNameValuePair("from_id", paramsFromMId);
                BasicNameValuePair TOIDPair = new BasicNameValuePair("to_id", paramsToMId);
                BasicNameValuePair SubjectPair = new BasicNameValuePair("subject", paramsSubject);
                BasicNameValuePair MessagePair = new BasicNameValuePair("message", paramsMesssage);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(FromIDPair);
                nameValuePairList.add(TOIDPair);
                nameValuePairList.add(SubjectPair);
                nameValuePairList.add(MessagePair);


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

                Log.e("Compose_Message", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        //JSONObject responseData = obj.getJSONObject("responseData");

                        String message = obj.getString("message");
                        Toast.makeText(ComposeMessage.this, "" + message, Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ComposeMessage.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        Log.e("pushmesg",""+FromId+" "+toId+" "+strSubject+" "+strMessage);
        sendPostReqAsyncTask.execute(FromId, toId, strSubject, strMessage);
    }


    private void getMAtriIdandUserList(String MatriID, String Gender) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramMatriID = params[0];
                String paramGender = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "member_message_list.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== " + URL + " " + paramMatriID + " " + paramGender);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair PairMatriID = new BasicNameValuePair("matri_id", paramMatriID);
                BasicNameValuePair PairGender = new BasicNameValuePair("gender", paramGender);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(PairMatriID);
                nameValuePairList.add(PairGender);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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

                        Log.e("URLRepsonce", "" + stringBuilder.toString());
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

                Log.e("----", "==" + Ressponce);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                UserListMatriIdListModel userListMatriIdListModel = gson.fromJson(Ressponce, UserListMatriIdListModel.class);

                userListMatriIdListModels.add(userListMatriIdListModel);
                Log.e("----getsizeUserList", "" + userListMatriIdListModels.size());
                userListMatriIdListAdapter.notifyDataSetChanged();

                try {
                    arrCity = new ArrayList<>();
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String user_matriID = resItem.getString("matri_id");
                            String user_name = resItem.getString("username");
                            String user_status = resItem.getString("status");
                            String gender = resItem.getString("gender");
                            String tokan = resItem.getString("tokan");

                            arrCity.add(new MessageSend(user_matriID, user_name, gender, tokan));


                        }

                        if (arrCity.size() > 0) {
                            Collections.sort(arrCity, new Comparator<MessageSend>() {
                                @Override
                                public int compare(MessageSend lhs, MessageSend rhs) {
                                    return lhs.getUser_name().compareTo(rhs.getUser_name());
                                }
                            });
                            sendAdapter = new MessageSendAdapter(ComposeMessage.this, arrCity, SlidingDrawer, Slidingpage, btnMenuClose, edtMatriId);
                            rvSendMessage.setAdapter(sendAdapter);
                            sendAdapter.clickOnListMatriId(ComposeMessage.this);
                        }


                    }


                } catch (Exception e) {
                    Log.e("eeeeeeee", e.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriID, Gender);
    }

    public void getMatriID() {
        edtSearchsendMessage.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (arrCity.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchsendMessage.getText().toString().toLowerCase(Locale.getDefault());
                    sendAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @Override
    public void clickOnUserListMatriId(int position) {
       /* if (position == 0) {
            token_ = userListMatriIdListModels.get(position).responseData._1.tokan;
            Log.e("clickOnUserListMatriId", "" + userListMatriIdListModels.get(position).responseData._1.matriId);
        }
        if (position == 1) {
            token_ = userListMatriIdListModels.get(position).responseData._2.tokan;
            Log.e("clickOnUserListMatriId", "" + userListMatriIdListModels.get(position).responseData._2.matriId);
        }
        if (position == 2) {
            token_ = userListMatriIdListModels.get(position).responseData._3.tokan;
            Log.e("clickOnUserListMatriId", "" + userListMatriIdListModels.get(position).responseData._3.matriId);
        }
        if (position == 3) {
            token_ = userListMatriIdListModels.get(position).responseData._4.tokan;
            Log.e("clickOnUserListMatriId", "" + userListMatriIdListModels.get(position).responseData._4.matriId);
        }*/
    }

    @Override
    public void clickOnListMatriId(int position) {

        token_ = arrCity.get(position).getTokan();
        edtMatriId.setText(arrCity.get(position).getUser_id());
        Log.e("token_click", "" + token_);
    }
}
