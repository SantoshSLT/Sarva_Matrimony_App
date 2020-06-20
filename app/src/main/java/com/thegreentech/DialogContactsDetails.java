package com.thegreentech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Adepters.InboxMessageAdapter;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AppConstants;

public class DialogContactsDetails extends Dialog implements DialogInterface.OnDismissListener
{
    Activity mContext;

    TextView textMatriId,textUsername,textEmail,textMobileNo,textAddress,textBirthTime,textBirthdate;

    TextView btnOK;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;

    String isMessageViewd,strMatId,strUsename,strEmail,strCountry,strState,strCity,strBithdate,strMobileNumber,
            strCountryCode,strBirthTime;

    public DialogContactsDetails(Activity context,String strMatId, String strUsename, String strEmail, String strCountry,
                                 String strState, String strCity, String strBithdate, String strMobileNumber,
                                 String strCountryCode,String strBirthTime)
    {
        super(context);
        Log.e("valldfxgdf",strBirthTime + "  " +strBithdate );

        this.mContext = context;
        this.strMatId=strMatId;
        this.strUsename=strUsename;
        this.strEmail=strEmail;
        this.strCountry=strCountry;
        this.strState=strState;
        this.strCity=strCity;
        this.strBithdate=strBithdate;
        this.strMobileNumber=strMobileNumber;
        this.strCountryCode=strCountryCode;
        this.strBirthTime = strBirthTime;


        prefUpdate= PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_contacts_details);

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


        textMatriId = (TextView) findViewById(R.id.textMatriId);
        textUsername = (TextView) findViewById(R.id.textUsername);
        textEmail = (TextView) findViewById(R.id.textEmail);
        textMobileNo = (TextView) findViewById(R.id.textMobileNo);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textBirthdate = findViewById(R.id.textBirthdate);
        textBirthTime = findViewById(R.id.textBirthTime);
        btnOK =  findViewById(R.id.btnOK);

        textMatriId.setText(": "+strMatId);
        textUsername.setText(": "+strUsename);
        textEmail.setText(": "+strEmail);
        textMobileNo.setText(": "+ "" +strCountryCode + "-"+strMobileNumber);
        textAddress.setText(": "+strCity+" "+strState+" "+strCountry);
        textBirthdate.setText(":  "+strBithdate);
        textBirthTime.setText(":  "+strBirthTime);
        //textBirthdate.setText((":  "+ AppConstants.mDateFormateDDMMM(strBithdate)));


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });


    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        clear();
    }

    public void clear() {
        mContext = null;
    }


    private void getProfileRequest(String strLoginMatriId, String strMatriId)
    {
        progresDialog= new ProgressDialog(mContext);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(mContext.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramLoginMatriId = params[0];
                String paramsMatriId = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"contact.php";
                Log.e("contact", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LOginMatriIdPair = new BasicNameValuePair("login_matri_id", paramLoginMatriId);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);



                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LOginMatriIdPair);
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

                Log.e("Photo URL", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        dismiss();
                        MemberViewProfile.UsedContact=MemberViewProfile.UsedContact+1;


                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t)
                {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strLoginMatriId, strMatriId);
    }



}


