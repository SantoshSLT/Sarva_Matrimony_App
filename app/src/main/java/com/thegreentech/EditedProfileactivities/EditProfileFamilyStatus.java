package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
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

import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adepters.GeneralAdapter;
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

public class EditProfileFamilyStatus extends AppCompatActivity {


    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    EditText edtStatus, edtType,edtValues, edtFatherOccupation, edtMotherOccupation,
            edtNoOfBrothers, edtNoOfMarriedBrothers,edtNoOfSisters, edtNoOfMarriedSisters;
    TextInputLayout inputNoOfMarriedBrother, inputNoOfMarriedSisters;
    TextInputLayout textInputNoOfChiled, textInputChiledLivingStatus;
    Button btnUpdate;
    RecyclerView rvGeneralView;
    LinearLayout Slidingpage, linGeneralView;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_family_status);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfileFamilyStatus.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        SlidingMenu();
        onClick();
        if (NetworkConnection.hasConnection(EditProfileFamilyStatus.this)){
            getProfilDetail(matri_id);

        }else
        {
            AppConstants.CheckConnection( EditProfileFamilyStatus.this);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkConnection.hasConnection(EditProfileFamilyStatus.this)){
            getProfilDetail(matri_id);

        }else
        {
            AppConstants.CheckConnection( EditProfileFamilyStatus.this);
        }
    }

    public void init() {
        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update Family Status");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        edtStatus=(EditText)findViewById(R.id.edtStatus);
        edtType=(EditText)findViewById(R.id.edtType);
        edtValues = findViewById(R.id.edtValues);
        edtFatherOccupation = findViewById(R.id.edtFatherOccupation);// edit
        edtMotherOccupation = findViewById(R.id.edtMotherOccupation);// edit
        edtNoOfBrothers = findViewById(R.id.edtNoOfBrothers);
        edtNoOfMarriedBrothers = findViewById(R.id.edtNoOfMarriedBrothers);
        edtNoOfSisters = findViewById(R.id.edtNoOfSisters);
        edtNoOfMarriedSisters = findViewById(R.id.edtNoOfMarriedSisters);
        inputNoOfMarriedBrother = (TextInputLayout) findViewById(R.id.inputNoOfMarriedBrother);// edit
        inputNoOfMarriedSisters = (TextInputLayout) findViewById(R.id.inputNoOfMarriedSisters);// edit
        textInputNoOfChiled = (TextInputLayout) findViewById(R.id.textInputNoOfChiled);
        textInputChiledLivingStatus = (TextInputLayout) findViewById(R.id.textInputChiledLivingStatus);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Slidingpage = (LinearLayout) findViewById(R.id.sliding_page);
        SlidingDrawer = (RelativeLayout) findViewById(R.id.sliding_drawer);
        btnMenuClose = (ImageView) findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);


    }

    public void onClick() {

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });

        edtStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtStatus.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_family_status),SlidingDrawer,Slidingpage,btnMenuClose,edtStatus);
                rvGeneralView.setAdapter(generalAdapter);
            }
        });

        edtType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtType.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_family_type),SlidingDrawer,Slidingpage,btnMenuClose,edtType);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtValues.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_family_value), SlidingDrawer, Slidingpage, btnMenuClose, edtValues);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfBrothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfBrothers.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_brothers), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtNoOfBrothers.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                String strSelectedType = edtNoOfBrothers.getText().toString().trim();
                if (strSelectedType.equalsIgnoreCase("No Brothers")) {
                    inputNoOfMarriedBrother.setVisibility(View.GONE);
                } else {
                    inputNoOfMarriedBrother.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtNoOfMarriedBrothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfMarriedBrothers.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_married_brothers), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfMarriedBrothers);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


        edtNoOfSisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfSisters.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_sisters), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfSisters);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtNoOfSisters.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String strSelectedType = edtNoOfSisters.getText().toString().trim();
                if (strSelectedType.equalsIgnoreCase("No Sisters")) {
                    inputNoOfMarriedSisters.setVisibility(View.GONE);
                } else {
                    inputNoOfMarriedSisters.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        edtNoOfMarriedSisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtNoOfMarriedSisters.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter = new GeneralAdapter(EditProfileFamilyStatus.this, getResources().getStringArray(R.array.arr_married_sisters), SlidingDrawer, Slidingpage, btnMenuClose, edtNoOfMarriedSisters);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String familyStatus=edtStatus.getText().toString().trim();
                String familyType=edtType.getText().toString().trim();
                String familyValues = edtValues.getText().toString().trim();
                String FatherOccupation = edtFatherOccupation.getText().toString().trim();
                String MotherOccupation = edtMotherOccupation.getText().toString().trim();
                String NoOfBrothers = edtNoOfBrothers.getText().toString().trim();
                String NoOfMarriedBrothers = edtNoOfMarriedBrothers.getText().toString().trim();
                String NoOfSisters = edtNoOfSisters.getText().toString().trim();
                String NoOfMarriedSisters = edtNoOfMarriedSisters.getText().toString().trim();

                if (hasData(familyStatus)
                        && hasData(familyType)
                        &&hasData(familyValues)
                        && hasData(FatherOccupation)
                        && hasData(MotherOccupation)
                        && hasData(NoOfBrothers)
                        && hasData(NoOfSisters)) {
                    if (!NoOfBrothers.equalsIgnoreCase("") && !NoOfBrothers.equalsIgnoreCase("No Brothers") && !NoOfBrothers.equalsIgnoreCase("0") && NoOfMarriedBrothers.equalsIgnoreCase("")) {
                        Toast.makeText(EditProfileFamilyStatus.this, "Please enter married brothers", Toast.LENGTH_LONG).show();
                    } else if (!NoOfSisters.equalsIgnoreCase("") && !NoOfSisters.equalsIgnoreCase("No Sisters") && !NoOfSisters.equalsIgnoreCase("0") && NoOfMarriedSisters.equalsIgnoreCase("")) {
                        Toast.makeText(EditProfileFamilyStatus.this, "Please enter married sisters", Toast.LENGTH_LONG).show();
                    }
                    else {

                        if (NetworkConnection.hasConnection(EditProfileFamilyStatus.this)){
                            updateFamilyStatus(matri_id,familyValues,FatherOccupation,MotherOccupation,NoOfBrothers,NoOfSisters,
                                    NoOfMarriedBrothers,NoOfMarriedSisters,familyStatus,familyType);


                        }else
                        {
                            AppConstants.CheckConnection( EditProfileFamilyStatus.this);
                        }

                    }
                } else {
                    Toast.makeText(EditProfileFamilyStatus.this,"Please enter all required fields. ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    public boolean hasData(String text) {
        if (text == null || text.length() == 0)
            return false;

        return true;
    }



    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfileFamilyStatus.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileFamilyStatus.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }



    public void SlidingMenu() {
        linGeneralView = (LinearLayout) findViewById(R.id.linGeneralView);

        rvGeneralView = (RecyclerView) findViewById(R.id.rvGeneralView);

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });

    }


    private void getProfilDetail(String strMatriId) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);

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

                Log.e("Profile Id", "==" + result);

                String finalresult = "";
                try {

                    finalresult = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(finalresult);


                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                edtStatus.setText(resItem.getString("family_status"));
                                edtType.setText(resItem.getString("family_type"));
                                edtValues.setText(resItem.getString("family_value"));
                                edtFatherOccupation.setText(resItem.getString("father_occupation"));
                                edtMotherOccupation.setText(resItem.getString("mother_occupation"));
                                edtNoOfBrothers.setText(resItem.getString("no_of_brothers"));
                                edtNoOfSisters.setText(resItem.getString("no_of_sisters"));


                                String noOfBrothers = resItem.getString("no_of_brothers");
                                if (!noOfBrothers.equalsIgnoreCase("0") && !noOfBrothers.equalsIgnoreCase("") && !noOfBrothers.equalsIgnoreCase("No Brothers")) {
                                    inputNoOfMarriedBrother.setVisibility(View.VISIBLE);
                                    edtNoOfMarriedBrothers.setText("" + resItem.getString("no_marri_brother"));
                                } else {
                                    inputNoOfMarriedBrother.setVisibility(View.GONE);
                                }

                                String noOfSister = resItem.getString("no_of_sisters");
                                if (!noOfSister.equalsIgnoreCase("0") && !noOfSister.equalsIgnoreCase("") && !noOfSister.equalsIgnoreCase("No Brothers")) {
                                    inputNoOfMarriedSisters.setVisibility(View.VISIBLE);
                                    edtNoOfMarriedSisters.setText("" + resItem.getString("no_marri_sister"));
                                } else {
                                    inputNoOfMarriedSisters.setVisibility(View.GONE);
                                }

                                String NoOfBrothers = edtNoOfBrothers.getText().toString().trim();
                                String NoOfMarriedBrothers = edtNoOfMarriedBrothers.getText().toString().trim();
                                String NoOfSisters = edtNoOfSisters.getText().toString().trim();
                                String NoOfMarriedSisters = edtNoOfMarriedSisters.getText().toString().trim();

                            }

                        }


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileFamilyStatus.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception t) {
                    Log.e("familyException",t.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    public void updateFamilyStatus(String MatriID,String Values,String FatherOccupation,String MotherOccupation,String NoOfBrothers,String NoOfSisters,
                       String NoOfMarriedBrothers,String NoOfMarriedSisters,String FamilyStatus, String FamilyType)
    {
        progresDialog= new ProgressDialog(EditProfileFamilyStatus.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String Pmatri_id= params[0];
                String PValues= params[1];
                String PFatherOccupation= params[2];
                String PMotherOccupation= params[3];
                String PNoOfBrothers= params[4];
                String PNoOfSisters= params[5];
                String MarridBrothers= params[6];
                String MarridSister= params[7];
                String familystatus= params[8];
                String familytype= params[9];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"edit_family_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", Pmatri_id);
                BasicNameValuePair ValuesPair = new BasicNameValuePair("family_values", PValues);
                BasicNameValuePair FatherOccupationPair = new BasicNameValuePair("father_occupation", PFatherOccupation);
                BasicNameValuePair MotherOccupationPair = new BasicNameValuePair("mother_occupation", PMotherOccupation);
                BasicNameValuePair NoOfBrothersPair = new BasicNameValuePair("no_of_brothers", PNoOfBrothers);
                BasicNameValuePair NoOfSistersPair = new BasicNameValuePair("no_of_sisters", PNoOfSisters);
                BasicNameValuePair MarridBrothersPair = new BasicNameValuePair("no_marri_brother", MarridBrothers);
                BasicNameValuePair MarridSisterPair = new BasicNameValuePair("no_marri_sister", MarridSister);
                BasicNameValuePair FamilystatusPair = new BasicNameValuePair("family_status", familystatus);
                BasicNameValuePair FamilyTypePair = new BasicNameValuePair("family_type", familytype);




                List<NameValuePair> nameValuePairList = new ArrayList<>();

                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(ValuesPair);
                nameValuePairList.add(FatherOccupationPair);
                nameValuePairList.add(MotherOccupationPair);
                nameValuePairList.add(NoOfBrothersPair);
                nameValuePairList.add(NoOfSistersPair);
                nameValuePairList.add(MarridBrothersPair);
                nameValuePairList.add(MarridSisterPair);
                nameValuePairList.add(FamilystatusPair);
                nameValuePairList.add(FamilyTypePair);

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
                    // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        Intent intLogin= new Intent(EditProfileFamilyStatus.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileFamilyStatus.this);
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
                        Log.e("nullllllll",e.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(MatriID,Values,FatherOccupation,MotherOccupation,NoOfBrothers,
                NoOfSisters,NoOfMarriedBrothers,NoOfMarriedSisters,FamilyStatus,FamilyType);
    }

}
