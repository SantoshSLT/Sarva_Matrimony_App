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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

public class DialogContacts extends Dialog implements DialogInterface.OnDismissListener
{
    Activity mContext;

    TextView textAlradyMessage,textRemainingContacts;

    TextView btnYes, btnNo;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    public static String matri_id,login_matri_id;
    int remainContact;

    String isMessageViewd,strMatId,strUsename,strEmail,strCountry,strState,strCity,strBithdate,strMobileNumber,
            strCountryCode,strBirthTime;

    public DialogContacts(Activity context, String matri_id,
                          String isMessageViewd,String strMatId,String strUsename,String strEmail,String strCountry,
                          String strState,String strCity,String strBithdate,String strMobileNumber,String CountryCode,
                          String BirthTime)
    {
        super(context);
        Log.e("safsdfvalldfxgdf",BirthTime + "  " +strBithdate );
        this.mContext = context;
        this.matri_id=matri_id;
        this.isMessageViewd=isMessageViewd;
        this.strMatId=strMatId;
        this.strUsename=strUsename;
        this.strEmail=strEmail;
        this.strCountry=strCountry;
        this.strState=strState;
        this.strCity=strCity;
        this.strBithdate=strBithdate;
        this.strMobileNumber=strMobileNumber;
        this.strCountryCode=CountryCode;
        this.strBirthTime = BirthTime;

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(mContext);
        login_matri_id=prefUpdate.getString("matri_id","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_contacts);

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


        textAlradyMessage = (TextView) findViewById(R.id.textAlradyMessage);
        textRemainingContacts = (TextView) findViewById(R.id.textRemainingContacts);

        btnYes =  findViewById(R.id.btnYes);
        btnNo =  findViewById(R.id.btnNo);

        if (isMessageViewd.equalsIgnoreCase("")) {
            textAlradyMessage.setVisibility(View.GONE);
        }
        else {
            textAlradyMessage.setVisibility(View.VISIBLE);
            textAlradyMessage.setText("" + isMessageViewd);
        }


             remainContact = MemberViewProfile.TotalContacts - MemberViewProfile.UsedContact;


        textRemainingContacts.setText(""+remainContact+" / "+MemberViewProfile.TotalContacts);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (NetworkConnection.hasConnection(mContext)){
                    getProfileRequest(login_matri_id, matri_id);

                }else
                {
                    AppConstants.CheckConnection(mContext);
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
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

                Log.e("Contact URL", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        dismiss();
                        if (isMessageViewd.equalsIgnoreCase("")) {
                            MemberViewProfile.UsedContact = MemberViewProfile.UsedContact + 1;
                        }
                        final DialogContactsDetails dgnew = new DialogContactsDetails(mContext,strMatId,strUsename,strEmail,strCountry,strState,strCity,strBithdate,strMobileNumber,strCountryCode,strBirthTime);
                        dgnew.setCancelable(true);
                        dgnew.setCanceledOnTouchOutside(true);
                        dgnew.show();

                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
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


