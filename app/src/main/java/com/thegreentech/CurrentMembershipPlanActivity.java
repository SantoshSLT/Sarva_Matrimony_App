package com.thegreentech;

import android.app.Activity;
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

import Adepters.CurrentMembershipPlanAdapter;
import Models.beanCurrentMembershipPlan;
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


public class CurrentMembershipPlanActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    SwipeRefreshLayout refresh;
    TextView textSubHeader,tvDurationtotal,tvDuration,tvMessagetotal,tvMessage,
            tvMcontactviewtotal,tvContactview,tvChat,tvprofiletotal,tvProfileview;
   // RecyclerView recyclerPlan;
    ProgressBar progressBar1;

    ArrayList<beanCurrentMembershipPlan> arrMembershipPlan=null;
    CurrentMembershipPlanAdapter membershipplanAdapter=null;

    SharedPreferences prefUpdate;
    String matri_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_membership_plan);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(CurrentMembershipPlanActivity.this);
        matri_id=prefUpdate.getString("matri_id","");
        Log.e("Save Data= "," MatriId=> "+matri_id);


        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("CURRENT MEMBERSHIP PLAN");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        textSubHeader=(TextView)findViewById(R.id.textSubHeader);
        tvDurationtotal=(TextView)findViewById(R.id.tvDurationtotal);
        tvDuration=(TextView)findViewById(R.id.tvDuration);
        tvMessagetotal=(TextView)findViewById(R.id.tvMessagetotal);
        tvMessage=(TextView)findViewById(R.id.tvMessage);
        tvMcontactviewtotal=(TextView)findViewById(R.id.tvMcontactviewtotal);
        tvContactview=(TextView)findViewById(R.id.tvContactview);
        tvChat=(TextView)findViewById(R.id.tvChat);
        tvprofiletotal=(TextView)findViewById(R.id.tvprofiletotal);
        tvProfileview=(TextView)findViewById(R.id.tvProfileview);
        progressBar1=(ProgressBar) findViewById(R.id.progressBar1);
        refresh = findViewById(R.id.refresh);


        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CurrentMembershipPlanActivity.this, MainActivity.class);
                intent.putExtra("Fragments", "ProfileEdit");
                startActivity(intent);

            }
        });


        if (NetworkConnection.hasConnection(getApplicationContext())){
            getCurrentPlanRequest(matri_id);

        }else
        {
            AppConstants.CheckConnection((Activity) getApplicationContext());
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getApplicationContext())){
                    getCurrentPlanRequest(matri_id);

                }else
                {
                    AppConstants.CheckConnection((Activity) getApplicationContext());
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CurrentMembershipPlanActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);

    }


/*
    public void getProfileCreated()
    {
        arrMembershipPlan= new ArrayList<beanCurrentMembershipPlan>();
        arrMembershipPlan.add(new beanCurrentMembershipPlan("1","Duration","10","This is a test"));
        arrMembershipPlan.add(new beanCurrentMembershipPlan("2","Messages","100","This is a test"));
        arrMembershipPlan.add(new beanCurrentMembershipPlan("3","SMS","50","This is a test"));
        arrMembershipPlan.add(new beanCurrentMembershipPlan("4","Contact View","80","This is a test"));
        arrMembershipPlan.add(new beanCurrentMembershipPlan("5","Live Chat","500","This is a test"));
        arrMembershipPlan.add(new beanCurrentMembershipPlan("6","Profile Viewd","150","This is a test"));

        if(arrMembershipPlan.size() >0)
        {
            membershipplanAdapter= new CurrentMembershipPlanAdapter(CurrentMembershipPlanActivity.this, arrMembershipPlan);
            recyclerPlan.setAdapter(membershipplanAdapter);
        }



    }

*/


    private void getCurrentPlanRequest(String strMatriId)
    {
      //  progressBar1.setVisibility(View.VISIBLE);
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"current_plan.php";
                Log.e("URL Current Plan", "== "+URL);
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

                Log.e("current_plan", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        arrMembershipPlan= new ArrayList<beanCurrentMembershipPlan>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext())
                            {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String name=resItem.getString("pname");
                                String email=resItem.getString("pemail");
                                //String address=resItem.getString("paddress");
                                String paymode=resItem.getString("paymode");
                                String p_no_contacts=resItem.getString("p_no_contacts");
                                String p_msg=resItem.getString("p_msg");
                                String p_sms=resItem.getString("p_sms");
                                String r_profile=resItem.getString("r_profile");
                                String video=resItem.getString("video");
                                String r_cnt=resItem.getString("r_cnt");
                                String r_msg=resItem.getString("r_msg");
                                String status1=resItem.getString("status");


                                String matri_id=resItem.getString("pmatri_id");
                                String p_plan=resItem.getString("p_plan");
                                String plan_duration=resItem.getString("plan_duration");
                                String r_sms=resItem.getString("r_sms");
                                String chat=resItem.getString("chat");
                                String profile=resItem.getString("profile");
                                String p_amount=resItem.getString("p_amount");
                                String active_dt=resItem.getString("pactive_dt");
                                String exp_date=resItem.getString("exp_date");

                                String remain_duration=resItem.getString("remaining_duration");
                                String remain_msg=resItem.getString("remaining_msg");
                                String remain_contact_view=resItem.getString("remaining_contact_view");
                                String remain_sms=resItem.getString("remaining_sms");
                                String remain_profile_view=resItem.getString("remaining_profile_view");



                                textSubHeader.setText(p_plan);

                                tvDurationtotal.setText(plan_duration+" DAYS / ");
                                if (remain_duration.equalsIgnoreCase("1"))
                                    tvDuration.setText(remain_duration+" DAY");
                                else
                                    tvDuration.setText(remain_duration+" DAYS");

                                tvMessagetotal.setText(p_msg+" / ");
                                tvMessage.setText(remain_msg);

                                tvMcontactviewtotal.setText(p_no_contacts+" / ");
                                tvContactview.setText(remain_contact_view);

                                tvChat.setText(chat);

                                tvprofiletotal.setText(profile+" / ");
                                tvProfileview.setText(remain_profile_view);



                               // arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"PLAN",p_plan));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Duration",plan_duration+" Days"));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"SMS",r_sms));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Live Chat",chat));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Profile Viewed",profile));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Plan Amount",p_amount));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Activation Date",active_dt));
                                arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Expired Date",exp_date));
                               // arrMembershipPlan.add(new beanCurrentMembershipPlan(matri_id,"Offer","-"));

                            }

                            if(arrMembershipPlan.size() > 0)
                            {

                               // membershipplanAdapter= new CurrentMembershipPlanAdapter(CurrentMembershipPlanActivity.this, arrMembershipPlan);
                              //  recyclerPlan.setAdapter(membershipplanAdapter);
                            }else
                            {

                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentMembershipPlanActivity.this);
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


                    refresh.setRefreshing(false);
                } catch (Exception t)
                {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }



}
