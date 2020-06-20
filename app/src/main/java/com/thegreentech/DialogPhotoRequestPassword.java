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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class DialogPhotoRequestPassword extends Dialog implements DialogInterface.OnDismissListener {
    Activity mContext;
    ImageView imgProfileImage;

    Button btnSubmit;
    private RadioGroup radioGroup;

    TextView btnHavePassword;
    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    public static String matri_id, login_matri_id;
    String PhotoType;

    String strMessage = "",tokans="",Photo_pass="";

    public DialogPhotoRequestPassword(String Tokans,Activity context, String matri_id, ImageView imgProfileImage, String Phototype,String photopassword) {
        super(context);
        this.mContext = context;
        this.matri_id = matri_id;
        this.tokans = Tokans;
        this.imgProfileImage = imgProfileImage;
        this.PhotoType = Phototype;
        this.Photo_pass = photopassword;
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(mContext);
        login_matri_id = prefUpdate.getString("matri_id", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_photo_request_password);

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnHavePassword = findViewById(R.id.btnHavePassword);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                strMessage = rb.getText().toString();

                //Toast.makeText(mContext, rb.getText(), Toast.LENGTH_SHORT).show();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!strMessage.equalsIgnoreCase("")) {
                    if (NetworkConnection.hasConnection(mContext)){
                        AppConstants.sendPushNotification(tokans,
                                "New Photo Password Request Received"+" "+login_matri_id,
                                "Photo Request",AppConstants.express_intress_id);
                        getProfileRequest(login_matri_id, matri_id, strMessage);

                    }else
                    {
                        AppConstants.CheckConnection(mContext);
                    }
                } else {
                    Toast.makeText(mContext, "Please select request type.", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnHavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final DialogPhotoPassword dgnew = new DialogPhotoPassword(tokans,mContext, matri_id, imgProfileImage,PhotoType,Photo_pass);
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


    private void getProfileRequest(String strLoginMatriId, String strMatriId, String strMess) {
        progresDialog = new ProgressDialog(mContext);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(mContext.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramLoginMatriId = params[0];
                String paramsMatriId = params[1];
                String paramsMessage = params[2];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "photo_pass_req.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LOginMatriIdPair = new BasicNameValuePair("login_matri_id", paramLoginMatriId);
                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair MessagePair = new BasicNameValuePair("message", paramsMessage);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LOginMatriIdPair);
                nameValuePairList.add(MatriIdPair);
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

                Log.e("Photo URL", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    dismiss();
                    if (status.equalsIgnoreCase("1")) {

                         /*   if (PhotoType.equalsIgnoreCase("photofram")) {
                                Intent intent = new Intent(mContext, ViewImageActivity.class);
                                intent.putExtra("MATRIiD", matri_id);
                                intent.putExtra("me","me");
                                mContext.startActivity(intent);
                            }
                            else {

                                String msgError = obj.getString("message");
                                Toast.makeText(mContext, ""+msgError, Toast.LENGTH_SHORT).show();
*//*
                                String msgError = obj.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();*//*
                            }*/

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
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
        sendPostReqAsyncTask.execute(strLoginMatriId, strMatriId, strMess);
    }


}


