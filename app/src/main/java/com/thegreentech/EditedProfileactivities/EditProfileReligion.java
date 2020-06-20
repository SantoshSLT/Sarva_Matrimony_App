package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;
import com.thegreentech.SignUpStep1Activity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.CasteAdapter;
import Adepters.ReligionAdapter;
import Adepters.SubCastAdapter;
import Models.SubCast;
import Models.beanCaste;
import Models.beanReligion;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AnimUtils;
import utills.AppConstants;
import utills.NetworkConnection;
import utills.RecyclerTouchListener;

public class EditProfileReligion extends AppCompatActivity {


    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    EditText edtReligion,edtCaste,edtSubCaste;
    Button btnUpdate, btnConfirm;
    SharedPreferences prefUpdate;
    String matri_id="";
    LinearLayout Slidingpage,linSlidingDrawer,linReligion,linCaste,linSBCaste;
    RelativeLayout SlidingDrawer;
    RecyclerView rvReligion,rvCaste,rvSBCaste;
    EditText edtSearchReligion,edtSearchCaste;

    ArrayList<beanReligion> arrReligion=new ArrayList<beanReligion>();
    ReligionAdapter religionAdapter=null;

    ArrayList<beanCaste> arrCaste=new ArrayList<beanCaste>();
    CasteAdapter casteAdapter=null;

    ArrayList<SubCast> arrMotherTongue=new ArrayList<SubCast>();
    SubCastAdapter motherTongueAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_religion);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(EditProfileReligion.this);
        matri_id=prefUpdate.getString("matri_id","");

        init();
        SlidingMenu();
        onClick();
        if (NetworkConnection.hasConnection(EditProfileReligion.this)){
            getProfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( EditProfileReligion.this);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getProfileDetail(matri_id);
    }

    public  void init(){
        btnConfirm=(Button) findViewById(R.id.btnConfirm);
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);
        textviewHeaderText.setText("Edit Religion Details");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        btnUpdate = findViewById(R.id.btnUpdate);
        edtReligion=(EditText)findViewById(R.id.edtReligion);
        edtCaste=(EditText)findViewById(R.id.edtCaste);
        edtSubCaste = findViewById(R.id.edtSubCaste);
        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        Slidingpage.setVisibility(View.GONE);
        linSlidingDrawer=(LinearLayout) findViewById(R.id.linSlidingDrawer);
        linSBCaste = findViewById(R.id.linSBCaste);



    }

    public  void  onClick(){
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppConstants.ReligionId.equalsIgnoreCase("") && AppConstants.ReligionId == null ||
                        AppConstants.CasteId.equalsIgnoreCase("") &&   AppConstants.CasteId == null ||
                        AppConstants.SubCasteID.equalsIgnoreCase("") && AppConstants.SubCasteID == null) {


                    if (AppConstants.ReligionId.equalsIgnoreCase("") && edtReligion.getText().toString().trim().equalsIgnoreCase("")) {
                        edtReligion.requestFocus();
                        edtReligion.setError("Please select your religion.");
                    }
                    if (AppConstants.CasteId.equalsIgnoreCase("") && edtCaste.getText().toString().trim().equalsIgnoreCase("")) {
                        edtCaste.requestFocus();
                        edtCaste.setError("Please select caste.");
                    }
                    if (AppConstants.SubCasteID.equalsIgnoreCase("") && edtSubCaste.getText().toString().trim().equalsIgnoreCase("")) {
                        edtSubCaste.requestFocus();
                        edtSubCaste.setError("Please select Subcaste.");
                    }
                }
                else {
                        updateReligion(matri_id, AppConstants.ReligionId,
                                AppConstants.CasteId, AppConstants.SubCasteID);
                    }
                }


        });




        edtReligion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtReligion.setError(null);
                edtSearchReligion.setText("");
                linReligion.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);

                if (NetworkConnection.hasConnection(getApplicationContext())) {
                    getReligionRequest();
                    getReligions();
                }else
                {
                    AppConstants.CheckConnection(EditProfileReligion.this);
                }
            }
        });

        edtCaste.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(!edtReligion.getText().toString().isEmpty())
                {
                    VisibleSlidingDrower();
                    Log.e("religionid",AppConstants.ReligionId);
                    getCastRequest(AppConstants.ReligionId);
                    getCaste();
                }else {
                    Toast.makeText(EditProfileReligion.this, "Please select religion First", Toast.LENGTH_SHORT).show();
                }
                edtCaste.setError(null);
                edtSearchCaste.setText("");
                if(arrCaste.size()>0)
                {

                }else
                {
                    rvCaste.setAdapter(null);
                }

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.VISIBLE);
                linSBCaste.setVisibility(View.GONE);

            }
        });



        edtSubCaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                linSBCaste.setVisibility(View.VISIBLE);
                linCaste.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);

                getSubCasteRequest();
                getSubCaste();
            }
        });

        rvReligion.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvReligion, new SignUpStep1Activity.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rvReligion.getWindowToken(), 0);

                beanReligion arrCo = arrReligion.get(position);
                AppConstants.ReligionId =arrCo.getReligion_id();
                AppConstants.ReligionName =arrCo.getName();

                edtReligion.setText(AppConstants.ReligionName);

                AppConstants.CasteId ="";
                AppConstants.StateName ="";

                edtCaste.setText("");

                GonelidingDrower();

                if (NetworkConnection.hasConnection(getApplicationContext()))
                {
                    getCastRequest(AppConstants.ReligionId);
                    getCaste();
                }else
                {
                    AppConstants.CheckConnection(EditProfileReligion.this);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    public  boolean hasData(String text)
    {

        if (text == null || text.length() == 0)
            return false;

        return true;
    }


    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfileReligion.this, SlidingDrawer, R.anim.slide_right);
        //SlidingDrawer.startAnimation(AppConstants.inFromRightAnimation()) ;
        Slidingpage.setVisibility(View.VISIBLE);


    }

    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileReligion.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu()
    {

        linReligion=findViewById(R.id.linReligion);
        linCaste=findViewById(R.id.linCaste);
      //  linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);


        edtSearchReligion=findViewById(R.id.edtSearchReligion);
        edtSearchCaste=findViewById(R.id.edtSearchCaste);


        rvReligion=findViewById(R.id.rvReligion);
        rvCaste=findViewById(R.id.rvCaste);
        rvSBCaste = findViewById(R.id.rvSBCaste);

        rvReligion.setLayoutManager(new LinearLayoutManager(this));
        rvReligion.setHasFixedSize(true);
        rvCaste.setLayoutManager(new LinearLayoutManager(this));
        rvCaste.setHasFixedSize(true);
        rvSBCaste.setLayoutManager(new LinearLayoutManager(this));
        rvSBCaste.setHasFixedSize(true);
//        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
//        rvGeneralView.setHasFixedSize(true);

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GonelidingDrower();

            }
        });



    }


    private void getProfileDetail(String strMatriId)
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {


            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"profile.php";
                Log.e("View Profile", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                Log.e("matri_id",paramsMatriId);

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
//                refresh.setRefreshing(false);
                Log.e("View Profile ", "=="+result);

                String finalresult="";
                try
                {
                    finalresult=result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(result);
                    String status=obj.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        if (responseData.has("1"))
                        {
                            Iterator<String> resIter = responseData.keys();
                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                AppConstants.ReligionId = resItem.getString("religion_id");
                                AppConstants.CasteId = resItem.getString("caste_id");
                                AppConstants.SubCasteID = resItem.getString("subcaste_id");

                                edtCaste.setText(resItem.getString("caste_name"));
                                edtReligion.setText(resItem.getString("religion"));
                                edtSubCaste.setText(resItem.getString("subcaste"));
                            }
                        }


                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileReligion.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(EditProfileReligion.this, "msgError" + msgError, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e)
                {
                    Toast.makeText(EditProfileReligion.this, "exception" +e.
                            getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("print ERRROR",e.getMessage()) ;
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public void getReligions()
    {

        try {
            edtSearchReligion.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if (arrReligion.size() > 0)
                    {
                        String charcter = cs.toString();
                        String text = edtSearchReligion.getText().toString().toLowerCase(Locale.getDefault());
                        religionAdapter.filter(text);
                    }
                }

                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                public void afterTextChanged(Editable arg0) {
                }
            });
        } catch (Exception e) {

        }

    }


    public void getCaste()
    {

        try {
            edtSearchCaste.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrCaste.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchCaste.getText().toString().toLowerCase(Locale.getDefault());
                        casteAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }



    }


    private void getReligionRequest()
    {
       /* progresDialog= new ProgressDialog(SignUpStep1Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"religion.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                //   progresDialog.dismiss();
                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrReligion= new ArrayList<beanReligion>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String religionId=resItem.getString("religion_id");
                            AppConstants.ReligionId =religionId;
                            String religionName=resItem.getString("religion_name");

                            arrReligion.add(new beanReligion(religionId,religionName));

                        }

                        if(arrReligion.size() >0)
                        {
                            Collections.sort(arrReligion, new Comparator<beanReligion>() {
                                @Override
                                public int compare(beanReligion lhs, beanReligion rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            religionAdapter = new ReligionAdapter(EditProfileReligion.this, arrReligion,SlidingDrawer,Slidingpage,edtReligion);
                            rvReligion.setAdapter(religionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {
                    Log.e("relegion",e.getMessage());

                } finally {
                    if (NetworkConnection.hasConnection(getApplicationContext()))
                    {
                        getCastRequest(AppConstants.ReligionId);
                        getCaste();
                    }else
                    {
                        AppConstants.CheckConnection(EditProfileReligion.this);
                    }
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void getCastRequest(String strReligion)
    {
       /* progresDialog= new ProgressDialog(SignUpStep1Activity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();
*/
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"cast.php";//?religion_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion_id", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

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

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                //progresDialog.dismiss();
                Log.e("--cast --", "=="+Ressponce);

                try {
                    arrCaste= new ArrayList<beanCaste>();

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");


                    // {"status":"0","message":"No Cast Data Found"}


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String casteId=resItem.getString("caste_id");
                            String casteName=resItem.getString("caste_name");

                            arrCaste.add(new beanCaste(casteId,casteName));

                        }

                        if(arrCaste.size() >0)
                        {
                            Collections.sort(arrCaste, new Comparator<beanCaste>() {
                                @Override
                                public int compare(beanCaste lhs, beanCaste rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            casteAdapter = new CasteAdapter(EditProfileReligion.this, arrCaste,SlidingDrawer,Slidingpage,edtCaste);
                            rvCaste.setAdapter(casteAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {
                } finally
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strReligion);
    }

    private void getSubCasteRequest()
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"sub-caste.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                try
                {

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

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                Log.e("--subcaste --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrMotherTongue= new ArrayList<SubCast>();
                    Log.e("subcastarrayup",arrMotherTongue.size()+"");
                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id=resItem.getString("sub_caste_id");
                            String mtongue_name=resItem.getString("sub_caste_name");

                            arrMotherTongue.add(new SubCast(mtongue_id,mtongue_name));
                            Log.e("subcastarraystore",arrMotherTongue.size()+"");
                        }

                        if(arrMotherTongue.size() >0)
                        {
                            Collections.sort(arrMotherTongue, new Comparator<SubCast>() {
                                @Override
                                public int compare(SubCast lhs, SubCast rhs) {
                                    return lhs.getSB_name().compareTo(rhs.getSB_name());
                                }
                            });
                            Log.e("subcastarraydown",arrMotherTongue.size()+"");
                            motherTongueAdapter = new SubCastAdapter( EditProfileReligion.this,arrMotherTongue,SlidingDrawer,Slidingpage,edtSubCaste);
                            rvSBCaste.setAdapter(motherTongueAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getSubCaste()
    {
        try {
            edtSubCaste.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrMotherTongue.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSubCaste.getText().toString().toLowerCase(Locale.getDefault());
                        motherTongueAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }


    }



    public void  updateReligion(String MatriID,String religionId,String CasteID,String SubCasteID)
    {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramMatriID = params[0];
                String paramReligionID = params[1];
                String paramCasteId = params[2];
                String paramSubCasteID = params[3];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_religion_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair pairMatriId = new BasicNameValuePair("matri_id", paramMatriID);
                BasicNameValuePair pairReligionID = new BasicNameValuePair("religion_id", paramReligionID);
                BasicNameValuePair pairCasteID = new BasicNameValuePair("caste_id", paramCasteId);
                BasicNameValuePair pairsubCasteId = new BasicNameValuePair("sub_caste_id", paramSubCasteID);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(pairMatriId);
                nameValuePairList.add(pairReligionID);
                nameValuePairList.add(pairCasteID);
                nameValuePairList.add(pairsubCasteId);



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

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                Log.e("--updatereligion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String message=responseObj.getString("message");

                        Log.e("updatereligion",message);
                        Toast.makeText(EditProfileReligion.this,""+message,Toast.LENGTH_LONG).show();

                        Intent intLogin= new Intent(EditProfileReligion.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();

                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileReligion.this);
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

                    responseObj = null;

                } catch (Exception e)

                {
                    Log.e("exaception",""+e);
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriID,religionId,CasteID,SubCasteID);
    }
}

