package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import RoboPOJO.ProfileUpdateResponse;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import utills.AppConstants;
import RestAPI.*;
import utills.NetworkConnection;

public class UpdateHoroScopePhotosActivity extends AppCompatActivity
{
    static final String TAG = UpdateHoroScopePhotosActivity.class.getSimpleName();

    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    ImageView imgUserPhoto;
    TextView textUsername, textMatriId, tvPhotoCounter;
    SwipeRefreshLayout accept_refresh ;
    ProgressBar progressBar1;
    TextView btnUploadPhotos,btnDelete;
    ImageView imgUserPhotos;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id="";

    String strFilePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_horoscope);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(UpdateHoroScopePhotosActivity.this);
        matri_id=prefUpdate.getString("matri_id","");
        Log.e("matri_id",matri_id);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("HOROSCOPE");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        accept_refresh =  findViewById(R.id.accept_refresh);
        imgUserPhoto = findViewById(R.id.imgUserPhoto);
        textUsername = findViewById(R.id.textUsername);
        textMatriId = findViewById(R.id.textMatriId);

        progressBar1=(ProgressBar) findViewById(R.id.progressBar1);
        btnUploadPhotos=findViewById(R.id.btnUploadPhotos);
        btnDelete=findViewById(R.id.btnDelete);
        imgUserPhotos=(ImageView)findViewById(R.id.imgUserPhotos);

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });


        btnUploadPhotos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if(!strFilePath.equalsIgnoreCase(""))
                {
                    CropImage.activity(null)
                            .setAllowRotation(false)
                            .setAllowFlipping(false)
                            .setAspectRatio(500, 500)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(UpdateHoroScopePhotosActivity.this);
                    if (NetworkConnection.hasConnection(UpdateHoroScopePhotosActivity.this)){

                        uploadImage(matri_id,strFilePath);

                    }else
                    {
                        AppConstants.CheckConnection(UpdateHoroScopePhotosActivity.this);
                    }
                    // getUploadPhoto(UserId,filePath);
                }else
                {
                    CropImage.activity(null)
                            .setAllowRotation(false)
                            .setAllowFlipping(false)
                            .setAspectRatio(500, 500)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(UpdateHoroScopePhotosActivity.this);

//                    Toast.makeText(UpdateHoroScopePhotosActivity.this,"Please select profile picture.",Toast.LENGTH_LONG).show();
                }
            }
        });

        String UserId = prefUpdate.getString("user_id", "");
        if (!UserId.equalsIgnoreCase("")) {
            String username = prefUpdate.getString("username", "");
            String profile_image = prefUpdate.getString("profile_image", "");
            String matri_id = prefUpdate.getString("matri_id", "");

            Log.e("image",profile_image);
            Log.e("username",username);
            textUsername.setText(username);
            textMatriId.setText(matri_id);
            Glide.with(UpdateHoroScopePhotosActivity.this)
                    .load(profile_image)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                    .into(imgUserPhoto);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateHoroScopePhotosActivity.this);
                    alertDialogBuilder
                            .setMessage("Are you sure you want to remove this photo?")
                            .setCancelable(false)
                            .setPositiveButton("Remove",new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int id)
                                {
                                    if (NetworkConnection.hasConnection(UpdateHoroScopePhotosActivity.this)){

                                        RemovePhoto(matri_id);

                                    }else
                                    {
                                        AppConstants.CheckConnection(UpdateHoroScopePhotosActivity.this);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int id)
                                {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
            }
        });

        accept_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkConnection.hasConnection(UpdateHoroScopePhotosActivity.this)){

                    getHoroscopeRequest(matri_id);

                }else
                {
                    AppConstants.CheckConnection(UpdateHoroScopePhotosActivity.this);
                }
            }
        });
        if (NetworkConnection.hasConnection(UpdateHoroScopePhotosActivity.this)){

            getHoroscopeRequest(matri_id);

        }else
        {
            AppConstants.CheckConnection(UpdateHoroScopePhotosActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateHoroScopePhotosActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                imgUserPhotos.setImageURI(result.getUri());

                strFilePath  = result.getUri().toString().replace("file://", "" );

                if (NetworkConnection.hasConnection(UpdateHoroScopePhotosActivity.this)){

                    uploadImage(matri_id,strFilePath);

                }else
                {
                    AppConstants.CheckConnection(UpdateHoroScopePhotosActivity.this);
                }

                Log.e("Gallary myBase64Image= ", " "+strFilePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void uploadImage(String strUserId,String strfilePath) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialogSendReq= new ProgressDialog(UpdateHoroScopePhotosActivity.this);
        progressDialogSendReq.setCancelable(false);
        progressDialogSendReq.setMessage("Please Wait...");
        progressDialogSendReq.setIndeterminate(true);
        progressDialogSendReq.show();

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        //RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), strFirstname);
        Log.e("user_id",strUserId);
        File file = new File(strfilePath);
        Log.d(TAG, "file is = " + file.toString());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image_path", file.getName(), requestFile);

        Call<ProfileUpdateResponse> call= apiService.postUpdateHoroskopyProfile(user_id,image);


        call.enqueue(new retrofit2.Callback<ProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponse> call, retrofit2.Response<ProfileUpdateResponse> response)
            {
                ProfileUpdateResponse updateProfileResponse = response.body();
                Log.e("Response =",""+updateProfileResponse);

                if (updateProfileResponse.getStatus().equalsIgnoreCase("1"))
                {
                    String message=updateProfileResponse.getMessage();
                    btnUploadPhotos.setText("Edit");
                    Toast.makeText(UpdateHoroScopePhotosActivity.this, message, Toast.LENGTH_SHORT).show();

                    /*SharedPreferences.Editor editor=prefUpdate.edit();
                    editor.putString("profile_image",responseData.getString("profile_path"));
                    editor.commit();*/
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHoroScopePhotosActivity.this);
                    builder.setMessage(""+message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();*/

                }else
                {
                    String msgError=updateProfileResponse.getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHoroScopePhotosActivity.this);
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
                progressDialogSendReq.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {

            }
        });

    }

    private void getHoroscopeRequest(String strMatriId)
    {
        accept_refresh.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"horoscope_details.php";
                Log.e("matri_search", "== "+URL);

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

                Log.e("Search by maitri Id", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {

                        JSONObject responseData = obj.getJSONObject("responseData");

                        String hor_photo=responseData.getString("hor_photo").toString().trim();

                        if(!hor_photo.equalsIgnoreCase(""))
                        {
                            btnUploadPhotos.setText("Edit");
                            Glide.with(UpdateHoroScopePhotosActivity.this)
                            .load(hor_photo)
                                    .apply(new RequestOptions().placeholder(R.drawable.horo_placeholder))
                                    .into(imgUserPhotos);
                        }
                        else {

                            btnUploadPhotos.setText("Upload");


                        }


                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHoroScopePhotosActivity.this);
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


                    accept_refresh.setRefreshing(false);
                } catch (Throwable t)
                {
                    accept_refresh.setRefreshing(false);
                }
                accept_refresh.setRefreshing(false);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    private void RemovePhoto(String MatriId)
    {
        progresDialog =new ProgressDialog(UpdateHoroScopePhotosActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage("Please Wait...");
        progresDialog.setIndeterminate(true);
        progresDialog.show();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"remove_horoscope.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPAir = new BasicNameValuePair("matri_id", paramMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPAir);


                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+URL+(nameValuePairList.toString().trim().replaceAll(",","&")));
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

                } catch (UnsupportedEncodingException uee)
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
                progresDialog.dismiss();
                Log.e("--Remove Photo --", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);
                    String status=obj.getString("status");

                    if(status.equalsIgnoreCase("1"))
                    {
                        String message=obj.getString("message");
                        btnUploadPhotos.setText("Upload");
                        imgUserPhotos.setImageResource(R.drawable.horo_placeholder);
                        Toast.makeText(UpdateHoroScopePhotosActivity.this, message, Toast.LENGTH_SHORT).show();
                       // imgUserPhotos.setImageResource(R.drawable.male);

                    }else
                    {
                        String msgError=obj.getString("message");

                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHoroScopePhotosActivity.this);
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
                    progresDialog.dismiss();
                } catch (Exception t)
                {
                    progresDialog.dismiss();
                        Log.e("dhfjsdf",t.getMessage());
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriId);
    }




}
