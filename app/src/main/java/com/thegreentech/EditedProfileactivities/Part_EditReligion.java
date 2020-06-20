package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.EditProfileActivity;
import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

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

import Adepters.CasteMultiSelectionAdapter;
import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter2;
import Adepters.MotherTongueMultiSelectionAdapter;
import Adepters.ReligionMultiSelectionAdapter;
import Models.beanCaste;
import Models.beanGeneralData;
import Models.beanMotherTongue;
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

public class Part_EditReligion extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;
    EditText edtReligion,edtCaste,edtHaveDosh,edtStar,edtMotherTongue,edtDoshType;

    LinearLayout linManglik,llDoshType,linReligion,linCaste,linMotherTongue,linGeneralView;
    EditText edtSearchReligion,edtSearchCaste,edtSearchMotherTongue;
    RecyclerView rvDosh,rvReligion,rvCaste,rvMotherTongue,rvGeneralView;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    Button btnConfirm;
    Button btnUpdate;

    ArrayList<beanReligion> arrReligion=new ArrayList<beanReligion>();
    ReligionMultiSelectionAdapter religionAdapter=null;

    ArrayList<beanCaste> arrCaste=new ArrayList<beanCaste>();
    CasteMultiSelectionAdapter casteAdapter=null;

    ArrayList<beanMotherTongue> arrMotherTongue=new ArrayList<beanMotherTongue>();
    MotherTongueMultiSelectionAdapter motherTongueAdapter=null;

    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_religion_prefr);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(Part_EditReligion.this);
        matri_id=prefUpdate.getString("matri_id","");
        init();
        SlidingMenu();
        onClick();

        if (NetworkConnection.hasConnection(Part_EditReligion.this)){
            getProfileDetailRequest(matri_id);

        }else
        {
            AppConstants.CheckConnection( Part_EditReligion.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(Part_EditReligion.this)){
            getProfileDetailRequest(matri_id);

        }else
        {
            AppConstants.CheckConnection( Part_EditReligion.this);
        }
    }

    public  void  init()
    {
        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Religion Preference");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtReligion=(EditText)findViewById(R.id.edtReligion);
        edtCaste=(EditText)findViewById(R.id.edtCaste);
        edtStar=(EditText)findViewById(R.id.edtStar);
        edtMotherTongue=(EditText)findViewById(R.id.edtMotherTongue);
        linManglik =  findViewById(R.id.linManglik);
        Slidingpage=(LinearLayout)findViewById(R.id.sliding_page);
        SlidingDrawer=(RelativeLayout)findViewById(R.id.sliding_drawer);
        btnMenuClose=(ImageView)findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
        llDoshType = findViewById(R.id.llDoshType);
        edtHaveDosh=(EditText)findViewById(R.id.edtManglik);
        edtDoshType = findViewById(R.id.edtDoshType);
        rvDosh = findViewById(R.id.rvDoshType);
        btnUpdate= findViewById(R.id.btnUpdate);

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
                String dosh=edtHaveDosh.getText().toString().trim();
                String Star=edtStar.getText().toString().trim();
                String DoshType=edtDoshType.getText().toString().trim();


                Log.e("R_id",AppConstants.ReligionId);
                Log.e("HeightID",AppConstants.MotherTongueId);


                if ( hasData(AppConstants.ReligionId) && hasData(AppConstants.CasteId)
                        && hasData(dosh)&& hasData(Star)&& hasData(AppConstants.MotherTongueId)
                        )
                {
                    if(dosh.equalsIgnoreCase("Yes") && DoshType.equalsIgnoreCase(""))
                    {
                        Toast.makeText(Part_EditReligion.this,"Please select Dosh type.",Toast.LENGTH_LONG).show();
                    }

                    if (NetworkConnection.hasConnection(Part_EditReligion.this)){
                        updateReligion_part(AppConstants.ReligionId,AppConstants.CasteId,dosh,
                                AppConstants.StarNAME,AppConstants.MotherTongueId,AppConstants.DosTypeNAME,matri_id);

                    }else
                    {
                        AppConstants.CheckConnection( Part_EditReligion.this);
                    }


                }else
                {
                    Toast.makeText(Part_EditReligion.this,"Please enter all required fields. ", Toast.LENGTH_LONG).show();
                }

            }
        });

        edtReligion.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.CasteId ="";
                    AppConstants.CasteName ="";

                    edtCaste.setText("");

                    GonelidingDrower();

                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getReligionRequest();
                        getReligions();
                    }else
                    {
                        AppConstants.CheckConnection(Part_EditReligion.this);
                    }

                    if (NetworkConnection.hasConnection(getApplicationContext()))
                    {
                        Log.e("edtReligionRelgian_id",AppConstants.ReligionId);
                        rvCaste.setAdapter(null);
                        getCastRequest(AppConstants.ReligionId);
                        getCaste();
                    }else
                    {
                        AppConstants.CheckConnection(Part_EditReligion.this);
                    }
                }
            }
        });
        edtDoshType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                edtDoshType.setError(null);
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_manglik_type);

                for (int i=0; i < arr_diet.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_diet[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(Part_EditReligion.this,"Dosh_Type",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditReligion.this, getResources().getStringArray(R.array.arr_diet),SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType);
                    rvGeneralView.setAdapter(generalAdapter);
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
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.VISIBLE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);


            }
        });

        edtCaste.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCaste.setError(null);
                edtSearchCaste.setText("");
                if(arrCaste.size()>0)
                {

                }else
                {
                    rvCaste.setAdapter(null);
                }
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.VISIBLE);
                linMotherTongue.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });


        edtHaveDosh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHaveDosh.setError(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditReligion.this, getResources().getStringArray(R.array.arr_manglik),SlidingDrawer,Slidingpage,btnMenuClose,edtHaveDosh);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHaveDosh.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String strSelectedType=edtHaveDosh.getText().toString().trim();
                if(strSelectedType.equalsIgnoreCase("Yes"))
                {
                    llDoshType.setVisibility(View.VISIBLE);
                }else
                {
                    llDoshType.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

            }
        });
        edtStar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtStar.setError(null);

                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);



                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_star = res.getStringArray(R.array.arr_star);

                for (int i=0; i < arr_star.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_star[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(Part_EditReligion.this,"Star",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtStar,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(Part_EditReligion.this, getResources().getStringArray(R.array.arr_star),SlidingDrawer,Slidingpage,btnMenuClose,edtStar);
                    rvGeneralView.setAdapter(generalAdapter);
                }




            }
        });


        edtMotherTongue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtMotherTongue.setError(null);
                edtSearchMotherTongue.setText("");
                linManglik.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });



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
        AnimUtils.SlideAnimation(Part_EditReligion.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(Part_EditReligion.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu()
    {
        linReligion=(LinearLayout)findViewById(R.id.linReligion);
        linCaste=(LinearLayout)findViewById(R.id.linCaste);
        linMotherTongue=(LinearLayout)findViewById(R.id.linMotherTongue);
        linGeneralView=(LinearLayout)findViewById(R.id.linGeneralView);

        edtSearchReligion=(EditText)findViewById(R.id.edtSearchReligion);
        edtSearchCaste=(EditText)findViewById(R.id.edtSearchCaste);
        edtSearchMotherTongue=(EditText)findViewById(R.id.edtSearchMotherTongue);

        rvReligion=(RecyclerView)findViewById(R.id.rvReligion);
        rvCaste=(RecyclerView)findViewById(R.id.rvCaste);
        rvMotherTongue=(RecyclerView)findViewById(R.id.rvMotherTongue);
        rvGeneralView=(RecyclerView)findViewById(R.id.rvGeneralView);

        btnConfirm=(Button) findViewById(R.id.btnConfirm);

        rvReligion.setLayoutManager(new LinearLayoutManager(this));
        rvReligion.setHasFixedSize(true);

        rvCaste.setLayoutManager(new LinearLayoutManager(this));
        rvCaste.setHasFixedSize(true);

        rvMotherTongue.setLayoutManager(new LinearLayoutManager(this));
        rvMotherTongue.setHasFixedSize(true);

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        rvDosh.setLayoutManager(new LinearLayoutManager(this));
        rvDosh.setHasFixedSize(true);

        btnMenuClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GonelidingDrower();

            }
        });

    }

    private void getReligionRequest()
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                //  String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"religion.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
               /* Log.e("religionname",paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);*/

                try
                {
                   /*  UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                     httpPost.setEntity(urlEncodedFormEntity);*/
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
                            String religionName=resItem.getString("religion_name");

                            arrReligion.add(new beanReligion(religionId,religionName,false));

                        }

                        if(arrReligion.size() >0)
                        {
                            Collections.sort(arrReligion, new Comparator<beanReligion>() {
                                @Override
                                public int compare(beanReligion lhs, beanReligion rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            religionAdapter = new ReligionMultiSelectionAdapter(Part_EditReligion.this, arrReligion,SlidingDrawer,Slidingpage,btnMenuClose,edtReligion,btnConfirm);
                            rvReligion.setAdapter(religionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    if (NetworkConnection.hasConnection(getApplicationContext())) {
                        getCastRequest( AppConstants.ReligionId);
                        getCaste();
                    }else
                    {
                        AppConstants.CheckConnection(Part_EditReligion.this);
                    }
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
    public void getReligions()
    {
        try {
            edtSearchReligion.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrReligion.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchReligion.getText().toString().toLowerCase(Locale.getDefault());
                        religionAdapter.filter(text);
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


    private void getCastRequest(String strReligion)
    {

        Log.e("edklgjioAJPO",strReligion);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                //  String URL= AppConstants.MAIN_URL +"part_cast.php";/
                //String URL= AppConstants.MAIN_URL +"cast.php";//?religion_id="+paramUsername;
                String URL= AppConstants.MAIN_URL +"caste_multiple.php";//?religion_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                Log.e("castparamUsername",paramUsername);
                // BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion_id", paramUsername);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion", paramUsername);

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
                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCaste= new ArrayList<beanCaste>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String casteId=resItem.getString("caste_id");
                            String casteName=resItem.getString("caste_name");

                            arrCaste.add(new beanCaste(casteId,casteName,false));

                        }

                        if(arrCaste.size() >0)
                        {
                            Collections.sort(arrCaste, new Comparator<beanCaste>() {
                                @Override
                                public int compare(beanCaste lhs, beanCaste rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            casteAdapter = new CasteMultiSelectionAdapter(Part_EditReligion.this, arrCaste,SlidingDrawer,Slidingpage,btnMenuClose,edtCaste,btnConfirm);
                            rvCaste.setAdapter(casteAdapter);

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
        sendPostReqAsyncTask.execute(strReligion);
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


    private void getMotherToungeRequest()
    {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"mother_tounge.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //   Log.e("motherTougueparamUsername",paramUsername);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

//                List<NameValuePair> nameValuePairList = new ArrayList<>();
//                nameValuePairList.add(UsernamePAir);

                try
                {
//                     UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
//                     httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--mother_tounge --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrMotherTongue= new ArrayList<beanMotherTongue>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id=resItem.getString("mtongue_id");
                            String mtongue_name=resItem.getString("mtongue_name");

                            arrMotherTongue.add(new beanMotherTongue(mtongue_id,mtongue_name,false));

                        }

                        if(arrMotherTongue.size() >0)
                        {
                            Collections.sort(arrMotherTongue, new Comparator<beanMotherTongue>() {
                                @Override
                                public int compare(beanMotherTongue lhs, beanMotherTongue rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            motherTongueAdapter = new MotherTongueMultiSelectionAdapter(Part_EditReligion.this, arrMotherTongue,SlidingDrawer,Slidingpage,btnMenuClose,edtMotherTongue,btnConfirm);
                            rvMotherTongue.setAdapter(motherTongueAdapter);

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
    public void getMotherTongue()
    {
        try {
            edtSearchMotherTongue.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrMotherTongue.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchMotherTongue.getText().toString().toLowerCase(Locale.getDefault());
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



    private void getProfileDetailRequest(String strMatriId)
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

                String finalresult="";
                try
                {

                    finalresult=result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(finalresult);


                    String status=obj.getString("status");

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

                                AppConstants.CasteId=resItem.getString("part_caste_id");
                                AppConstants.ReligionId=resItem.getString("part_religion_id");
                                AppConstants.MotherTongueId=resItem.getString("part_mtongue_id");

                                String part_mtongue=resItem.getString("part_mtongue");
                                String part_religion=resItem.getString("part_religion");
                                String part_caste=resItem.getString("part_caste");
                                String part_star=resItem.getString("part_star");
                                String part_manglik=resItem.getString("part_manglik");
                                String part_dosh = resItem.getString("part_dosh");

                                edtReligion.setText(part_religion);
                                edtCaste.setText(part_caste);
                                edtHaveDosh.setText(part_dosh);
                                edtDoshType.setText(part_manglik);
                                edtStar.setText(part_star);
                                edtMotherTongue.setText(part_mtongue);


                            }

                        }



                    }else
                    {
                        String msgError=obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditReligion.this);
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

                } catch (Exception t)
                {
                        Log.e("ttt",t.getMessage());
                }
                getMotherToungeRequest();
                getMotherTongue();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }

    public void   updateReligion_part(String ReligionId, String CasteId,String dosh, String StarNAME,
                                      String MotherTongueId,String DosTypeNAME,String MAtriID){

        final ProgressDialog progresDialog= new ProgressDialog(Part_EditReligion.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String paramReligionId= params[0];
                String paramCasteId= params[1];
                String paramdosh= params[2];
                String paramStar= params[3];
                String paramMotherTongueId= params[4];
                String parammanglik = params[5];
                String paramMatriId= params[6];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_part_religion_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair PairReligionId = new BasicNameValuePair("part_religion_id", paramReligionId);
                BasicNameValuePair PairCasteId = new BasicNameValuePair("part_caste_id", paramCasteId);
                BasicNameValuePair PairManglik = new BasicNameValuePair("part_manglik", parammanglik);
                BasicNameValuePair PairStar = new BasicNameValuePair("part_star", paramStar);
                BasicNameValuePair PairMotherTongueId = new BasicNameValuePair("mother_tongue_id", paramMotherTongueId);
                BasicNameValuePair PairMatriID = new BasicNameValuePair("matri_id", paramMatriId);
                BasicNameValuePair PairDosh = new BasicNameValuePair("part_dosh", paramdosh);



                List<NameValuePair> nameValuePairList = new ArrayList<>();

                nameValuePairList.add(PairReligionId);
                nameValuePairList.add(PairCasteId);
                nameValuePairList.add(PairManglik);
                nameValuePairList.add(PairStar);
                nameValuePairList.add(PairMotherTongueId);
                nameValuePairList.add(PairMatriID);
                nameValuePairList.add(PairDosh);



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
                progresDialog.dismiss();
                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    //JSONObject responseData = responseObj.getJSONObject("responseData");
                    // {"user_id":"10","message":"Registration Detail Successfully Update","status":"1"}
                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        Intent intLogin= new Intent(Part_EditReligion.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Part_EditReligion.this);
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

                } finally
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(ReligionId,CasteId,dosh,StarNAME,MotherTongueId,DosTypeNAME,MAtriID);
    }
}
