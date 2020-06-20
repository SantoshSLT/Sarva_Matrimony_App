package com.thegreentech;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Observable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import RoboPOJO.ProfileUpdateResponse;
import cz.msebera.android.httpclient.Header;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import RestAPI.ApiClient;
import RestAPI.ApiInterface;
import utills.AppConstants;
import utills.Myprefrence;
import utills.RequestCodes;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.graphics.Bitmap.CompressFormat.PNG;

public class SignUpStep4Activity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    ImageView imgUserPhotos;
    CardView cardView;
    Button btnUpload,btnSkip;

    SharedPreferences prefUpdate;;
    ProgressDialog progresDialog;
    String UserId="";

    String strFilePath="";
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step4);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(SignUpStep4Activity.this);
        UserId= prefUpdate.getString("user_id_r","");

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewSignUp.setText("REGISTRATION NEW");
        textviewHeaderText.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpStep4Activity.this,SignUpStep1Activity.class);
                startActivity(intent);
            }
        });

        imgUserPhotos=(ImageView)findViewById(R.id.imgUserPhotos);
        cardView=(CardView)findViewById(R.id.cardView);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnSkip=(Button)findViewById(R.id.btnSkip);

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intLogin= new Intent(SignUpStep4Activity.this,LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {



            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(btnUpload.getText().toString().equals("SELECT PHOTO"))
                {

                    CropImage.activity(null)
                            .setAllowRotation(false)
                            .setAllowFlipping(false)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(500,500)
                            .start(SignUpStep4Activity.this);
                }
                else {

                    if(!strFilePath.equalsIgnoreCase(""))
                    {
                        uploadImage(UserId,strFilePath);

                    }else
                    {
                        Toast.makeText(SignUpStep4Activity.this,"Please select profile piscture.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view)
            {
                goToLogin();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep4Activity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.go_back));
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
               /* SharedPreferences.Editor editor=prefUpdate.edit();
                editor.putString("signup_step","0");
                editor.commit();*/
                Intent intLogin= new Intent(SignUpStep4Activity.this,LoginActivity.class);
                startActivity(intLogin);
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void goToLogin()
    {
        if(!Myprefrence.getRefralID(getApplicationContext()).equals(""))
        {
            progresDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id",Myprefrence.getRefralID(getApplicationContext()));
            client.post(AppConstants.MAIN_URL + "referal_register.php", params, new TextHttpResponseHandler()
            {
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
                public void onSuccess(int statusCode, Header[] headers, String responseString)
                {
                    progresDialog.dismiss();
                    Log.d("RESSSSSS",responseString);
                    Myprefrence.putRefralID(getApplicationContext(),"");
                    Myprefrence.putUid(getApplicationContext(),"");
                    Myprefrence.putMyrefralLink(getApplicationContext(),"");

                    SharedPreferences.Editor editor=prefUpdate.edit();
                    editor.putString("user_id_r","");
                    editor.putString("otp","");
                    editor.putString(AppConstants.MOBILE_NO,"");
                    editor.putString("CountryId","");
                    editor.putString("CountryName","");
                    editor.putString("CountryCode","");
                    editor.putString("signup_step","");
                    editor.putString("user_id","");
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),Thankyou.class));
                    finishAffinity();
                }
            });
        }
        else {

            startActivity(new Intent(getApplicationContext(),Thankyou.class));
            SharedPreferences.Editor editor=prefUpdate.edit();
            editor.putString("user_id_r","");
            editor.putString("otp","");
            editor.putString(AppConstants.MOBILE_NO,"");
            editor.putString("CountryId","");
            editor.putString("CountryName","");
            editor.putString("CountryCode","");
            editor.putString("signup_step","");
            editor.putString("user_id","");
            editor.commit();
            finishAffinity();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Glide.with(SignUpStep4Activity.this)
                        .load(result.getUri())
                        .into(imgUserPhotos);
                //imgUserPhotos.setImageURI(result.getUri());

               /* Bitmap bitmap=null;
                if (data != null) {
                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        // bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                btnUpload.setText("CONTINUE");
                strFilePath  = result.getUri().toString().replace("file://", "" );
                Log.e("Gallary myBase64Image= ", " "+strFilePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private void uploadImage(String strUserId,String strfilePath)
    {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialogSendReq= new ProgressDialog(SignUpStep4Activity.this);
        progressDialogSendReq.setCancelable(false);
        progressDialogSendReq.setMessage("Please_Wait");
        progressDialogSendReq.setIndeterminate(true);
        progressDialogSendReq.show();

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        //RequestBody fname = RequestBody.create(MediaType.parse("text/plain"), strFirstname);

        File file = new File(strfilePath);
        Log.d("ravi", "file is = " + file.toString());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image_path", file.getName(), requestFile);
        Call<ProfileUpdateResponse> call= apiService.postUpdateProfile(user_id,image);

        call.enqueue(new retrofit2.Callback<ProfileUpdateResponse>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponse> call, retrofit2.Response<ProfileUpdateResponse> response)
            {
                ProfileUpdateResponse updateProfileResponse = response.body();
                Log.e("Responce =",""+updateProfileResponse);

                if (updateProfileResponse.getStatus().equalsIgnoreCase("1"))
                {
                    String message=updateProfileResponse.getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep4Activity.this);
                    builder.setMessage(""+message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            goToLogin();
                            alert.dismiss();
                        }
                    });
                    alert = builder.create();
                    alert.show();

                }else
                {
                    String msgError=updateProfileResponse.getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpStep4Activity.this);
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





}
