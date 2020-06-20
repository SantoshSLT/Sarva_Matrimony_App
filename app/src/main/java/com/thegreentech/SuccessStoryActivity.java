package com.thegreentech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thegreentech.successStory.SuccessStoryDashBoardActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import Adepters.SuccessStoryAdapter;
import Models.beanUserSuccessStory;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utills.AppConstants;
import utills.NetworkConnection;


public class SuccessStoryActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    RecyclerView recyclerPlan;

    ViewPager pagerSuucessStory;
    TextView btnSuucessStoryPrv,btnSuucessStoryNext;
    ProgressBar progressBar1;
    LinearLayout linearLayoutButton;

    ArrayList<beanUserSuccessStory> arrSuccessStory=null;
    SuccessStoryAdapter successStoryAdapter=null;

    int currentCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successs_story);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("SUCCESS STORY");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        pagerSuucessStory = (ViewPager)findViewById(R.id.pagerSuucessStory);
        btnSuucessStoryPrv=(TextView)findViewById(R.id.btnSuucessStoryPrv);
        btnSuucessStoryNext=(TextView)findViewById(R.id.btnSuucessStoryNext);
        progressBar1=(ProgressBar) findViewById(R.id.progressBar1);

        arrSuccessStory= new ArrayList<beanUserSuccessStory>();

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        btnSuucessStoryPrv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pagerSuucessStory.setCurrentItem(pagerSuucessStory.getCurrentItem() - 1);
                currentCounter=pagerSuucessStory.getCurrentItem();
            }
        });

        btnSuucessStoryNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pagerSuucessStory.setCurrentItem(pagerSuucessStory.getCurrentItem() + 1);
                currentCounter=pagerSuucessStory.getCurrentItem();
            }
        });
        if (NetworkConnection.hasConnection(SuccessStoryActivity.this)){

            getSuccessStoryRequest();

        }else
        {
            AppConstants.CheckConnection(SuccessStoryActivity.this);
        }

    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }



    private void getSuccessStoryRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                /*String paramsMatriId = params[0];*/

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"success_story.php";
                Log.e("URL success_story", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                /*BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);*/


                /*List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);*/

                try
                {
                    /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));*/
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

                Log.e("success_story", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        arrSuccessStory= new ArrayList<beanUserSuccessStory>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext())
                            {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String story_id=resItem.getString("story_id");;
                                String weddingphoto=resItem.getString("weddingphoto");
                                String weddingphoto_type=resItem.getString("weddingphoto_type");
                                String bridename=resItem.getString("bridename");
                                String brideid=resItem.getString("brideid");
                                String groomname=resItem.getString("groomname");
                                String groomid=resItem.getString("groomid");
                                String marriagedate=resItem.getString("marriagedate");
                                String engagement_date=resItem.getString("engagement_date");
                                String address=resItem.getString("address");
                                String country=resItem.getString("country");
                                String successmessage=resItem.getString("successmessage");


                                arrSuccessStory.add(new beanUserSuccessStory(story_id,weddingphoto, weddingphoto_type,
                                        bridename,brideid,groomname,groomid,marriagedate,engagement_date,address,
                                        country,successmessage));

                            }

                            if(arrSuccessStory.size() > 0)
                            {
                                successStoryAdapter = new SuccessStoryAdapter(SuccessStoryActivity.this, arrSuccessStory,pagerSuucessStory);
                                pagerSuucessStory.setAdapter(successStoryAdapter);

                                autoPlaySuccessStory(pagerSuucessStory);

                                linearLayoutButton.setVisibility(View.VISIBLE);
                            }else
                            {
                                pagerSuucessStory.setVisibility(View.GONE);
                                //textEmptyView.setVisibility(View.VISIBLE);
                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuccessStoryActivity.this);
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


                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t)
                {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }



 /*   private void getSucccessStoryRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                //String paramUserId = params[0];
                // String paramCurrency = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +".php";
                Log.e("URL Message Inbox", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                // BasicNameValuePair paramUserIdPair = new BasicNameValuePair("user_id", paramUserId);
                // BasicNameValuePair CurrencyPair = new BasicNameValuePair("currency",paramCurrency);


                // List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                // nameValuePairList.add(paramUserIdPair);
                // nameValuePairList.add(CurrencyPair);

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    //httpPost.setEntity(urlEncodedFormEntity);
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

                //Log.e("Recent Listing", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    JSONArray jsnArray=obj.getJSONArray("categories");

                    for (int i=0; i<jsnArray.length(); i++)
                    {
                        JSONObject objData = jsnArray.getJSONObject(i);

                        //String category_id=objData.getString("category_id");
                       // String category_nameE=objData.getString("category_e");

                        String Bride_id=""+i;
                        String Bride_name="";
                        String Groom_id=""+i;
                        String Groom_name="";
                        String Engagement_Date="20 Nov,2014";
                        String Marriage_Date="14 Jan,2015";
                        String Address="150ft Ring Road, Nr.Balagi hall, Rajkot-360004";
                        String Country_Living_In="India";
                        String Profile_picture;
                        String Success_story=getResources().getString(R.string.Privacy_Policy);

                        if ((i % 2) == 0)
                        {
                            Bride_name="Priyanka " +i;//
                            Groom_name="Ranbeer " +i;
                            Profile_picture="http://www.dailyexcelsior.com/wp-content/uploads/2017/03/203.jpg";
                        }
                        else
                        {
                            Bride_name="Deepika " +i;//
                            Groom_name="Kapil " +i;
                            Profile_picture="https://pbs.twimg.com/media/CbBvq6yW4AA6cGG.jpg";
                        }

                        arrSuccessStory.add(new beanUserSuccessStory(Bride_id, Bride_name, Groom_id, Groom_name, Engagement_Date,
                                Marriage_Date,Address, Country_Living_In, Profile_picture,Success_story));


                        if(i == 1)
                        {
                            break;
                        }
                    }

                    if(arrSuccessStory.size() > 0)
                    {
                        successStoryAdapter = new SuccessStoryAdapter(SuccessStoryDashBoardActivity.this, arrSuccessStory,pagerSuucessStory);
                        pagerSuucessStory.setAdapter(successStoryAdapter);

                        autoPlaySuccessStory(pagerSuucessStory);

                    }else
                    {
                        pagerSuucessStory.setVisibility(View.GONE);
                        //textEmptyView.setVisibility(View.VISIBLE);
                    }

                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t)
                {

                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


*/

    private void autoPlaySuccessStory(final ViewPager viewPager)
    {
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (pagerSuucessStory.getAdapter().getCount() > 0) {
                        int position = currentCounter % arrSuccessStory.size();
                        currentCounter++;
                        viewPager.setCurrentItem(position);
                        autoPlaySuccessStory(viewPager);

                    }
                } catch (Exception e)
                {

                }
            }
        }, 3000);
    }



}
