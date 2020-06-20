package com.thegreentech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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

public class DialogPhotoPassword extends Dialog implements DialogInterface.OnDismissListener
{
    Activity mContext;
    ImageView imgProfileImage;

    EditText edtPassword;
    Button btnSubmit;
        TextView btnDontHavePassword;
    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    public static String matri_id,login_matri_id;
    String PhotoType,Tokans="",Photo_pass;

    public DialogPhotoPassword(String Tokans,Activity context,String matri_id, ImageView imgProfileImage,String PhotoTypr,String PhotoPasword) {
        super(context);
        this.mContext = context;
        this.matri_id=matri_id;
        this.Tokans = Tokans;
        this.imgProfileImage=imgProfileImage;
        this.PhotoType = PhotoTypr;
        this.Photo_pass = PhotoPasword;
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(mContext);
        login_matri_id=prefUpdate.getString("matri_id","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_photo_password);

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);



        edtPassword= (EditText) findViewById(R.id.edtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnDontHavePassword =  findViewById(R.id.btnDontHavePassword);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!edtPassword.getText().toString().equalsIgnoreCase(""))
                {
                    if (NetworkConnection.hasConnection(mContext)){
                        if (edtPassword.getText().toString().equalsIgnoreCase(Photo_pass))
                            getProfileRequest(login_matri_id, matri_id,edtPassword.getText().toString());
                        else
                            Toast.makeText(mContext, "Password not Match", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        AppConstants.CheckConnection(mContext);
                    }
                }else
                {
                    Toast.makeText(mContext,"Please enter Password",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnDontHavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                final DialogPhotoRequestPassword dgnew = new DialogPhotoRequestPassword(Tokans,mContext,matri_id,imgProfileImage,PhotoType,Photo_pass);
                dgnew.setCancelable(true);
                dgnew.setCanceledOnTouchOutside(true);
                dgnew.show();
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


    private void getProfileRequest(String strLoginMatriId, String strMatriId,String strPassword)
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
                String paramsPassword = params[2];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"photo_url.php";
                Log.e("photo_url", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LOginMatriIdPair = new BasicNameValuePair("login_matri_id", paramLoginMatriId);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair PasswordPair = new BasicNameValuePair("password", paramsPassword);



                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LOginMatriIdPair);
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(PasswordPair);

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
                    edtPassword.setText("");
                    dismiss();
                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext())
                            {
                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);
                                    if (PhotoType.equalsIgnoreCase("photofram"))
                                    {
                                        Intent intent = new Intent(mContext, ViewImageActivity.class);
                                        intent.putExtra("MATRIiD", matri_id);
                                        intent.putExtra("me","me");
                                        mContext.startActivity(intent);
                                    }
                                String photo1=resItem.getString("photo1");

                                if(!photo1.equalsIgnoreCase(""))
                                {
                                    Picasso.with(mContext)
                                            .load(photo1)
                                            //.fit()
                                            .error(R.drawable.ic_profile)
                                            .into(imgProfileImage, new Callback() {
                                                @Override
                                                public void onSuccess()
                                                {
                                                    //progressBar1.setVisibility(View.GONE);
                                                }
                                                @Override
                                                public void onError() {
                                                   // progressBar1.setVisibility(View.GONE);
                                                }
                                            });
                                }


                            }

                        }

                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                onBackPressed();
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
        sendPostReqAsyncTask.execute(strLoginMatriId, strMatriId,strPassword);
    }


}


