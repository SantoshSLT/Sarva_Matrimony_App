package com.thegreentech.successStory.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.EditedProfileactivities.Part_EditReligion;
import com.thegreentech.MainActivity;
import com.thegreentech.R;
import com.thegreentech.successStory.SuccesStoryFrag;
import com.thegreentech.successStory.SuccessStoryDashBoardActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;

import Adepters.CountryAdapter;
import Adepters.GroomBrideAdapter;
import Models.GeneralBean;
import Models.GroomBride;
import Models.beanCountries;
import RestAPI.ApiClient;
import RestAPI.ApiInterface;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import utills.AnimUtils;
import utills.AppConstants;
import utills.AppMethods;
import utills.ImageUtill;
import utills.NetworkConnection;
import utills.RequestPermissionHandler;

import static android.app.Activity.RESULT_OK;

public class PostStoryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int GALINTENT = 2345;

    View view;
    Activity activity;
    EditText edtBrideID, edtBrideName, edtGroomId, edtGroomName,
            edtEngagementDate, edtMarriageDate, edtCountry, edtAddress, edtStory, edtSearchCountry, edtSearchGroom, edtSearchBride;
    ImageView ivProfile, ivAddicon, ivCoverPhoto;
    TextView tvuploadPhot;
    private RelativeLayout rlAddImage;
    Button btnPost;
    LinearLayout Slidingpage, linCountry, linGroom, linBride;
    RelativeLayout SlidingDrawer;
    RecyclerView rvCountry, rvGroom, rvBride;

    ArrayList<beanCountries> arrCountry = new ArrayList<beanCountries>();
    CountryAdapter countryAdapter = null;

    GroomBrideAdapter groomBrideAdapter = null;
    ArrayList<GroomBride> bridelist = new ArrayList<GroomBride>();
    ArrayList<GroomBride> Groomlist = new ArrayList<GroomBride>();


    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    RequestPermissionHandler requestPermissionHandler;
    String matri_id = "";
    String imagePath = "";
    String BrideName = "";
    String GroomName = "";


    private int mYear, mMonth, mDay;


    public PostStoryFragment() {
        // Required empty public constructor
    }

    public static PostStoryFragment newInstance(int sectionNumber) {
        PostStoryFragment fragment = new PostStoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_story, container, false);
        activity = getActivity();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");
        requestPermissionHandler = new RequestPermissionHandler();
        init();
        SlidingMenu();
        onClick();
        return view;
    }

    public void init() {
        edtBrideID = view.findViewById(R.id.edtBrideID);
        edtBrideName = view.findViewById(R.id.edtBrideName);
        edtGroomId = view.findViewById(R.id.edtGroomId);
        edtGroomName = view.findViewById(R.id.edtGroomName);
        edtEngagementDate = view.findViewById(R.id.edtEngagementDate);
        edtMarriageDate = view.findViewById(R.id.edtMarriageDate);
        edtCountry = view.findViewById(R.id.edtCountry);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtStory = view.findViewById(R.id.edtStory);
        // ivProfile = view.findViewById(R.id.ivProfile);
        tvuploadPhot = view.findViewById(R.id.tvuploadPhot);
        rlAddImage = view.findViewById(R.id.rlAddImage);
        ivCoverPhoto = view.findViewById(R.id.ivCoverPhoto);

        ivAddicon = view.findViewById(R.id.ivAddicon);
        edtSearchCountry = view.findViewById(R.id.edtSearchCountry);
        edtSearchBride = view.findViewById(R.id.edtSearchBride);
        edtSearchGroom = view.findViewById(R.id.edtSearchGroom);


        btnPost = view.findViewById(R.id.btnPost);

        Slidingpage = view.findViewById(R.id.sliding_page);
        SlidingDrawer = view.findViewById(R.id.sliding_drawer);
        Slidingpage.setVisibility(View.GONE);

        if (NetworkConnection.hasConnection(getActivity())){
            BrideNameRequest();
            getBridename();

        }else
        {
            AppConstants.CheckConnection( getActivity());
        }

    }

    public void onClick() {

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(v)) {
                    AddPoast();
                }
            }
        });
        rlAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage(v);
            }
        });

        edtEngagementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppMethods.hideKeyboard(view);
                edtEngagementDate.setError(null);
                setDatesEngage();
            }
        });
        edtMarriageDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppMethods.hideKeyboard(view);
                edtEngagementDate.setError(null);
                setDatesmarriage();
            }
        });


        edtBrideID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();


                edtBrideID.setError(null);
                edtSearchBride.setText("");
                linCountry.setVisibility(View.GONE);
                linBride.setVisibility(View.VISIBLE);
                linGroom.setVisibility(View.GONE);
            }
        });
        edtGroomId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();

                edtGroomId.setError(null);
                edtSearchGroom.setText("");
                linCountry.setVisibility(View.GONE);
                linBride.setVisibility(View.GONE);
                linGroom.setVisibility(View.VISIBLE);
            }
        });


        edtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisibleSlidingDrower();
                edtCountry.setError(null);
                edtSearchCountry.setText("");
                linCountry.setVisibility(View.VISIBLE);
                linBride.setVisibility(View.GONE);
                linGroom.setVisibility(View.GONE);

            }
        });

    }


    private void UploadImage(final View v) {
        requestPermissionHandler.requestPermission(getActivity(), new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
                Intent gallaryIntent = ImageUtill.getGalleryIntenr(getActivity());
                startActivityForResult(gallaryIntent, GALINTENT);
            }

            @Override
            public void onFailed() {
                Toast.makeText(getActivity(), "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALINTENT && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "Please upload again", Toast.LENGTH_LONG).show();
            } else {
                Bitmap bitmap = ImageUtill.getImageFromResult(getActivity(), resultCode, data);
                imagePath = ImageUtill.getBitmapPath(bitmap, getActivity());
                Log.e("imagpathhhh", imagePath);
                File f = new File(imagePath);
                Log.e("file", f + "");
                Glide.with(this)
                        .load(Uri.fromFile(f))
                        .apply(new RequestOptions().centerCrop())
                        .into(ivCoverPhoto);
                ivAddicon.setVisibility(View.GONE);
                tvuploadPhot.setVisibility(View.GONE);
            }
        }


    }


    private boolean isValid(View v) {
        if (edtBrideID.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter BrideId", Toast.LENGTH_SHORT).show();
            edtBrideID.requestFocus();
            return false;
        }
        if (edtBrideName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Bride Name", Toast.LENGTH_SHORT).show();
            edtBrideName.requestFocus();
            return false;
        }
        if (edtGroomId.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter GroomId", Toast.LENGTH_SHORT).show();
            edtGroomId.requestFocus();
            return false;
        }
        if (edtGroomName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Groom Name", Toast.LENGTH_SHORT).show();
            edtGroomName.requestFocus();
            return false;
        }
        if (edtEngagementDate.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Engagement Date", Toast.LENGTH_SHORT).show();
            edtEngagementDate.requestFocus();
            return false;
        }
        if (edtCountry.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Country", Toast.LENGTH_SHORT).show();
            edtCountry.requestFocus();
            return false;
        }
        /*if (edtAddress.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_SHORT).show();
            edtAddress.requestFocus();
            return false;
        }*/
        if (edtStory.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Success Story", Toast.LENGTH_SHORT).show();
            edtStory.requestFocus();
            return false;
        }
        if (imagePath.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public void setDatesEngage() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        Log.e("curruntyear", mYear + "");
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String curruntDate = mYear + "-" + mMonth + "-" + mDay + "";
        Log.e("date_TODAY", curruntDate);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                edtEngagementDate.setText(dateDesc);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#EF6C00"))//color of confirm button
                .minYear(2000) //min year in loop
                .maxYear(2020) // max year in loop
                // .dateChose("1999-01-01") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(getActivity());


    }

    public void setDatesmarriage() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        Log.e("curruntyear", mYear + "");
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String curruntDate = mYear + "-" + mMonth + "-" + mDay + "";
        Log.e("date_TODAY", curruntDate);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {

                edtMarriageDate.setText(dateDesc);
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#EF6C00"))//color of confirm button
                .minYear(2000) //min year in loop
                .maxYear(2020) // max year in loop
                //.dateChose("1999-01-01") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(getActivity());


    }


    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(getActivity(), SlidingDrawer, R.anim.slide_right);
        //SlidingDrawer.startAnimation(AppConstants.inFromRightAnimation()) ;
        Slidingpage.setVisibility(View.VISIBLE);


    }

    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(getActivity(), SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }


    public void SlidingMenu() {


        linCountry = view.findViewById(R.id.linCountry);
        linGroom = view.findViewById(R.id.linGroom);
        linBride = view.findViewById(R.id.linBride);

       /* edtSearchCountry = view.findViewById(R.id.edtSearchCountry);
        edtSearchCountry = view.findViewById(R.id.edtSearchCountry);
        edtSearchCountry = view.findViewById(R.id.edtSearchCountry);*/

        rvCountry = view.findViewById(R.id.rvCountry);
        rvGroom = view.findViewById(R.id.rvGroom);
        rvBride = view.findViewById(R.id.rvBride);

        rvCountry.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCountry.setHasFixedSize(true);

        rvGroom.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGroom.setHasFixedSize(true);

        rvBride.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBride.setHasFixedSize(true);


        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });


    }


    public void getCountries() {
        try {
            edtSearchCountry.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if (arrCountry.size() > 0) {
                        String charcter = cs.toString();
                        String text = edtSearchCountry.getText().toString().toLowerCase(Locale.getDefault());
                        countryAdapter.filter(text);
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

    private void getCountrysRequest() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "country.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                try {

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

                } catch (Exception uee) {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce) {
                super.onPostExecute(Ressponce);
                Log.e("--Country --", "==" + Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCountry = new ArrayList<beanCountries>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID = resItem.getString("country_id");
                            String CoName = resItem.getString("country_name");

                            arrCountry.add(new beanCountries(CoID, CoName));

                        }

                        if (arrCountry.size() > 0) {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
                            countryAdapter = new CountryAdapter(getActivity(), arrCountry, SlidingDrawer, Slidingpage, edtCountry);
                            rvCountry.setAdapter(countryAdapter);


                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {
                    Log.e("cccoouunnttryryry", e.getMessage());
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }


    private void BrideNameRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "get_bride_name.php";
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                try {
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
                } catch (Exception uee) {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce) {
                super.onPostExecute(Ressponce);
                //progresDialog.dismiss();
                Log.e("--cast --", "==" + Ressponce);

                try {

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCountry.clear();
                    bridelist = new ArrayList<GroomBride>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID = resItem.getString("bride_id");
                            String CoName = resItem.getString("bride_name");


                            bridelist.add(new GroomBride(CoID, CoName));

                        }

                        if (bridelist.size() > 0) {
                            Collections.sort(bridelist, new Comparator<GroomBride>() {
                                @Override
                                public int compare(GroomBride lhs, GroomBride rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            groomBrideAdapter = new GroomBrideAdapter(getActivity(), bridelist, SlidingDrawer, Slidingpage, edtBrideID, edtBrideName);
                            rvBride.setAdapter(groomBrideAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {
                    Log.e("iddeccc", e.getMessage());
                } finally {
                    if (NetworkConnection.hasConnection(getActivity())){
                        GroomNameRequest();
                        getGroomname();

                    }else
                    {
                        AppConstants.CheckConnection( getActivity());
                    }


                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getBridename() {
        edtSearchBride.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (bridelist.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchBride.getText().toString().toLowerCase(Locale.getDefault());
                    groomBrideAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    public void GroomNameRequest() {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "get_groom_name.php";//?religion_id="+paramUsername;
                Log.e("URL", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);


                try {
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

                } catch (Exception uee) {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce) {
                super.onPostExecute(Ressponce);
                //progresDialog.dismiss();
                Log.e("--cast --", "==" + Ressponce);

                try {

                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    Groomlist = new ArrayList<GroomBride>();

                    if (responseData.has("1")) {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext()) {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID = resItem.getString("groom_id");
                            String CoName = resItem.getString("groom_name");

                            Groomlist.add(new GroomBride(CoID, CoName));

                        }

                        if (Groomlist.size() > 0) {
                            Collections.sort(Groomlist, new Comparator<GroomBride>() {
                                @Override
                                public int compare(GroomBride lhs, GroomBride rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            groomBrideAdapter = new GroomBrideAdapter(getActivity(), Groomlist, SlidingDrawer, Slidingpage, edtGroomId, edtGroomName);
                            rvGroom.setAdapter(groomBrideAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e) {
                    Log.e("iddeccc", e.getMessage());
                } finally {

                    if (NetworkConnection.hasConnection(getActivity())){
                        getCountrysRequest();
                        getCountries();
                    }else
                    {
                        AppConstants.CheckConnection( getActivity());
                    }

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getGroomname() {
        edtSearchGroom.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (Groomlist.size() > 0) {
                    String charcter = cs.toString();
                    String text = edtSearchGroom.getText().toString().toLowerCase(Locale.getDefault());
                    groomBrideAdapter.filter(text);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });

    }


    private void AddPoast() {


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialogSendReq = new ProgressDialog(getActivity());
        progressDialogSendReq.setCancelable(false);
        progressDialogSendReq.setMessage("Please_Wait");
        progressDialogSendReq.setIndeterminate(true);
        progressDialogSendReq.show();

        RequestBody paramBrideID = RequestBody.create(MediaType.parse("text/plain"), edtBrideID.getText().toString());
        RequestBody paramBrideNAme = RequestBody.create(MediaType.parse("text/plain"), edtBrideName.getText().toString());
        RequestBody paramGroomId = RequestBody.create(MediaType.parse("text/plain"), edtGroomId.getText().toString());
        RequestBody paramGroomName = RequestBody.create(MediaType.parse("text/plain"), edtGroomName.getText().toString());
        RequestBody paramEngaeDate = RequestBody.create(MediaType.parse("text/plain"), edtEngagementDate.getText().toString());
        RequestBody paramMarryDate = RequestBody.create(MediaType.parse("text/plain"), edtMarriageDate.getText().toString());
        RequestBody paramSuccessStory = RequestBody.create(MediaType.parse("text/plain"), edtStory.getText().toString());
        RequestBody paramAddress= RequestBody.create(MediaType.parse("text/plain"), edtAddress.getText().toString());
        RequestBody paramCountry = RequestBody.create(MediaType.parse("text/plain"), edtCountry.getText().toString());

        File file = new File(imagePath);
        Log.d("ravi", "file is = " + file.toString());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image_path", file.getName(), requestFile);


        Call<GeneralBean> call = apiService.AddSuccessStory(paramBrideID,paramBrideNAme,paramGroomId,paramGroomName,
                paramEngaeDate,paramMarryDate,paramSuccessStory,paramAddress,paramCountry,image );// uplaod_photo.php


        call.enqueue(new retrofit2.Callback<GeneralBean>() {
            @Override
            public void onResponse(Call<GeneralBean> call, retrofit2.Response<GeneralBean> response) {
                GeneralBean successStory = response.body();
                Log.e("Responce =", "" + successStory);

                if (successStory.getStatus().equalsIgnoreCase("1")) {
                    Toast.makeText(getActivity(), ""+successStory.getMessage(), Toast.LENGTH_SHORT).show();
                    SuccesStoryFrag.tab = SuccesStoryFrag.tabLayout.getTabAt(0);
                    SuccesStoryFrag.tab.select();
                } else {
                    String msgError = successStory.getMessage();
                    Toast.makeText(getActivity(), ""+ msgError, Toast.LENGTH_SHORT).show();
                }
                progressDialogSendReq.dismiss();
            }

            @Override
            public void onFailure(Call<GeneralBean> call, Throwable t) {
                progressDialogSendReq.dismiss();
                Log.e("fhgjhf",t.getMessage());
            }
        });

    }
}