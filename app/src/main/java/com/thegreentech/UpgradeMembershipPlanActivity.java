package com.thegreentech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.internal.GoogleApiManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import Adepters.CurrentMembershipPlanAdapter;
import Adepters.UpgradeMembershipPlanAdapter;
import Models.beanUpgradeMembershipPlan;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utills.AppConstants;
import utills.Myprefrence;
import utills.NetworkConnection;


public class UpgradeMembershipPlanActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;

    RecyclerView gridView;
    ProgressBar progressBar1;
    TextView textEmptyView;

    ArrayList<beanUpgradeMembershipPlan> arrMembershipPlan = null;
    CurrentMembershipPlanAdapter membershipplanAdapter = null;

    RelativeLayout linDetailView;
    TextView textClose, textSubHeader;
    RecyclerView recyclerPlan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_membership_plan);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("UPGRADE MEMBERSHIP PLAN");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        textEmptyView = (TextView) findViewById(R.id.textEmptyView);

        gridView =  findViewById(R.id.gridview);

        linDetailView = (RelativeLayout) findViewById(R.id.linDetailView);
        textClose = (TextView) findViewById(R.id.textClose);
        textSubHeader = (TextView) findViewById(R.id.textSubHeader);
        recyclerPlan = (RecyclerView) findViewById(R.id.recyclerPlan);
        recyclerPlan.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlan.setHasFixedSize(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomDown = AnimationUtils.loadAnimation(UpgradeMembershipPlanActivity.this, R.anim.slide_out_down);
                linDetailView.startAnimation(bottomDown);
                linDetailView.setVisibility(View.GONE);

            }
        });

        linDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (NetworkConnection.hasConnection(UpgradeMembershipPlanActivity.this)){

            getMessagesRequest();

        }else
        {
            AppConstants.CheckConnection(UpgradeMembershipPlanActivity.this);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpgradeMembershipPlanActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);

    }


    private void getMessagesRequest() {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                /*String paramsMatriId = params[0];*/

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "all_member_ship_plan.php";
                Log.e("URL Recent", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                /*BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);*/


                /*List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);*/

                try {
                    /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));*/
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

                Log.e("all_member_ship_plan", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrMembershipPlan = new ArrayList<beanUpgradeMembershipPlan>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String plan_id = resItem.getString("plan_id");
                                String plan_name = resItem.getString("plan_name");
                                String plan_amount_type = resItem.getString("plan_amount_type");
                                String plan_amount = resItem.getString("plan_amount");
                                if (Integer.parseInt(plan_amount) > 10) {

                                    if (!Myprefrence.getDownload_bonus(getApplicationContext()).equals("")) {

                                        Double dd = Double.parseDouble(Myprefrence.getDownload_bonus(getApplicationContext()));
                                        Double tt = (Double.parseDouble(plan_amount) * dd) / 100;
                                        Double total = Double.parseDouble(plan_amount) - tt;
                                        plan_amount = String.valueOf((total.intValue()));
                                    }
                                    if (!Myprefrence.getRegistar_bonus(getApplicationContext()).equals("")) {
                                        Double dd = Double.parseDouble(Myprefrence.getRegistar_bonus(getApplicationContext()));
                                        Double tt = (Double.parseDouble(plan_amount) * dd) / 100;
                                        Double total = Double.parseDouble(plan_amount) - tt;
                                        plan_amount = String.valueOf((total.intValue()));
                                    }
                                }
                                String plan_duration = resItem.getString("plan_duration");
                                String plan_msg = resItem.getString("plan_msg");
                                String plan_sms = resItem.getString("plan_sms");
                                String plan_contacts = resItem.getString("plan_contacts");
                                String chat = resItem.getString("chat");
                                String profile = resItem.getString("profile");
                                String status1 = resItem.getString("status");

                                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id, plan_name, plan_amount_type, plan_amount, plan_duration, plan_msg, plan_sms, plan_contacts, chat, profile, status1));
                            }

                            if (arrMembershipPlan.size() > 0) {
                                textEmptyView.setVisibility(View.GONE);

                                LinearLayoutManager manager = new LinearLayoutManager(UpgradeMembershipPlanActivity.this,LinearLayoutManager.VERTICAL,false);
                                gridView.setLayoutManager(manager);
                                gridView.setAdapter(new UpgradeMembershipPlanAdapter(UpgradeMembershipPlanActivity.this, arrMembershipPlan, linDetailView, textSubHeader, recyclerPlan));
                            } else {
                                textEmptyView.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        String msgError = obj.getString("message");
                        textEmptyView.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeMembershipPlanActivity.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();*/
                    }


                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t) {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

}
