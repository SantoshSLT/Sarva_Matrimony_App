package com.thegreentech;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import utills.RequestPermissionHandler;

public class LoginActivity extends AppCompatActivity
{
    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    LinearLayout llRegister;
    TextInputLayout inputPassword;

    Button /*btnFacebook,*/ btnLogin;
    EditText edtEmailId,edtPassword;
    TextView txtForgotPass;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
   // ImageView ivPassword;
    String isPassword = "hide";

    RequestPermissionHandler requestPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        init();
        onClick();

        requestPermissionHandler = new RequestPermissionHandler();
        requestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {


            }

            @Override
            public void onFailed() {
                Toast.makeText(getApplicationContext(), "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void init()
    {
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        edtEmailId=(EditText)findViewById(R.id.edtEmailId);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        txtForgotPass=(TextView)findViewById(R.id.txtForgotPass);
        llRegister = findViewById(R.id.llRegister);

        textviewHeaderText.setText(getResources().getString(R.string.Registration));
        btnBack.setVisibility(View.GONE);
        textviewHeaderText.setVisibility(View.GONE);
        inputPassword = findViewById(R.id.inputPassword);
       // ivPassword =findViewById(R.id.ivPassword);


        // edtEmailId.setText("GT249");//ghjgjhgj@gmail.com => vikesh.iipl@gmail.com
        //edtPassword.setText("111111");//lucky123
        //btnFacebook=(Button)findViewById(R.id.btnFacebook);


    }

    public  void  onClick(){

/*
    edtPassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isPassword.equalsIgnoreCase("hide")) {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    inputPassword.setPasswordVisibilityToggleEnabled(true);
                inputPassword.setPasswordVisibilityToggleDrawable(R.drawable.icn_show_password);
               isPassword = "show";
            }
            else if(isPassword.equalsIgnoreCase("show"))
            {
                inputPassword.setPasswordVisibilityToggleEnabled(false);
                inputPassword.setPasswordVisibilityToggleDrawable(R.drawable.icn_orange_paswrd);
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isPassword = "hide";

            }
            else {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                inputPassword.setPasswordVisibilityToggleEnabled(true);
                isPassword = "show";
            }
        }
    });
*/

        llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpMethod();
            }
        });
        textviewSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SignUpMethod();

            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtForgotPass.getWindowToken(), 0);

                Intent intLogin= new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intLogin);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnLogin.getWindowToken(), 0);


                String strUsername= edtEmailId.getText().toString().trim();
                String strPassword= edtPassword.getText().toString().trim();

                if(strUsername.equalsIgnoreCase("") && strPassword.equalsIgnoreCase(""))
                {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_username_and_password));
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.Please_enter_username_and_password),Toast.LENGTH_LONG).show();
                }else if(strUsername.equalsIgnoreCase(""))
                {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_username));
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.Please_enter_username),Toast.LENGTH_LONG).show();
                }/*else if(! checkEmail(strUsername))
                {
                    //edtEmailId.setError(getResources().getString(R.string.Please_enter_valid_email_address));
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_enter_valid_email_address), Toast.LENGTH_LONG).show();
                }*/else if(strPassword.equalsIgnoreCase(""))
                {
                    //edtPassword.setError(getResources().getString(R.string.Please_enter_password));
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.Please_enter_password),Toast.LENGTH_LONG).show();
                }else
                {
//                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//
//                            Log.d("TOKEN__",task.getResult().getToken());
//                            AppConstants.tokan = task.getResult().getToken();
//                        }
//                    });
                    //Toast.makeText(LoginActivity.this,"Cooming Soon",Toast.LENGTH_LONG).show();

                    if (NetworkConnection.hasConnection(LoginActivity.this)){
                        sendLoginRequest(strUsername,strPassword);

                    }else
                    {
                        AppConstants.CheckConnection(LoginActivity.this);
                    }

                }
            }
        });
    }
    private boolean checkEmail (String email)
    {
        return AppConstants.email_pattern.matcher(email).matches();
    }


    public void SignUpMethod()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textviewSignUp.getWindowToken(), 0);

        String signup_step= prefUpdate.getString("signup_step","");

        if(signup_step.equalsIgnoreCase("1"))
        {
            Intent intLogin= new Intent(LoginActivity.this,VerifyMobileNumberActivity.class);
            startActivity(intLogin);
        }else if(signup_step.equalsIgnoreCase("2"))
        {
            Intent intLogin= new Intent(LoginActivity.this,SignUpStep2Activity.class);
            startActivity(intLogin);
        }else if(signup_step.equalsIgnoreCase("3"))
        {
            Intent intLogin= new Intent(LoginActivity.this,SignUpStep3Activity.class);
            startActivity(intLogin);
        }else if(signup_step.equalsIgnoreCase("4"))
        {
            Intent intLogin= new Intent(LoginActivity.this,SignUpStep4Activity.class);
            startActivity(intLogin);
        }else
        {
            Intent intLogin= new Intent(LoginActivity.this,SignUpStep1Activity.class);
            startActivity(intLogin);
            finish();
        }
    }

    private void sendLoginRequest(String strUsername, String strPassword)
    {
        progresDialog=new ProgressDialog(LoginActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];
                String paramPasswod = params[1];
               // String paramGcmId = params[2];
              //  String paramLanguage = params[3];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"login.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair UsernamePAir = new BasicNameValuePair("email_id", paramUsername);
                BasicNameValuePair PasswodfiPAir = new BasicNameValuePair("password", paramPasswod);
                BasicNameValuePair tokan = new BasicNameValuePair("tokan", AppConstants.tokan);
                /*BasicNameValuePair GCmPAir = new BasicNameValuePair("gcm_id", paramGcmId);
                BasicNameValuePair deviceTypePAir = new BasicNameValuePair("device_type", "android");
                BasicNameValuePair LanguagePAir = new BasicNameValuePair("language", paramLanguage);*/

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(UsernamePAir);
                nameValuePairList.add(PasswodfiPAir);
                nameValuePairList.add(tokan);
                /*nameValuePairList.add(GCmPAir);
                nameValuePairList.add(deviceTypePAir);
                nameValuePairList.add(LanguagePAir);*/

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--Login --", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if(status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        SharedPreferences.Editor editor=prefUpdate.edit();
                        editor.putString("user_id",responseData.getString("user_id"));
                        editor.putString("email",responseData.getString("email"));
                        editor.putString("profile_image",responseData.getString("profile_path"));
                        editor.putString("matri_id",responseData.getString("matri_id"));
                        editor.putString("username",responseData.getString("username"));
                        editor.putString("gender",responseData.getString("gender"));
                        editor.putString("paid_status",responseData.getString("membership_status"));
                        editor.commit();

                        String dt = responseData.getString("reg_date");
                        getSharedPreferences("data",MODE_PRIVATE).edit().putString("join_date",dt).apply();
                        getSharedPreferences("data",MODE_PRIVATE).edit().putString("isdisplay","false").apply();

                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        myIntent.putExtra("classname","LoginActivity");
                        myIntent.putExtra("newuser_time",dt);
                        startActivity(myIntent);
                        finish();

                    }else
                    {
                        edtEmailId.setText("");
                        edtPassword.setText("");
                        String message=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(""+message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } catch (Throwable t)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                    builder.setMessage("Please enter valid username or password.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strUsername,strPassword/*,GcmId,language1*/);
    }


}
