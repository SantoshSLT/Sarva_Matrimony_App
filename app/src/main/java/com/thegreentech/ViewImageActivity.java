package com.thegreentech;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Adepters.PhotosAdapter;
import Adepters.ProfilePhotoViewAdapter;
import Models.beanPhotos;
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

public class ViewImageActivity extends AppCompatActivity {

    RecyclerView rvImage;
    String matriID = "";
    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    RelativeLayout rlToolbar;
    ViewPager viewPager;
    ImageView btnPrev, btnNext;
    private ArrayList<beanPhotos> arrPhotosList;
    ProfilePhotoViewAdapter adapter;
    String type;

    String photo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        matriID = getIntent().getStringExtra("MATRIiD");
        type = getIntent().getStringExtra("me");
        initilize();
        onclick();

        if (NetworkConnection.hasConnection(ViewImageActivity.this)) {

            getAllProfileImage(matriID);

        } else {
            AppConstants.CheckConnection(ViewImageActivity.this);
        }


    }


    public void initilize() {

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText(matriID);
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.VISIBLE);

        viewPager = findViewById(R.id.viewpager);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        viewPager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (type.equalsIgnoreCase("mine")) {
            Intent intent = new Intent(ViewImageActivity.this, MainActivity.class);
            intent.putExtra("Fragments", "ProfileEdit");
            startActivity(intent);
            finish();
        } else if (type.equalsIgnoreCase("me")) {
            Intent intent = new Intent(ViewImageActivity.this, MemberViewProfile.class);
            startActivity(intent);
            finish();
        }

    }

    public void onclick() {

        textviewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewImageActivity.this, ManagePhotoActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != (arrPhotosList.size() - 1)) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    btnPrev.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if (position == arrPhotosList.size() - 1) {
                    btnNext.setVisibility(View.GONE);
                    btnPrev.setVisibility(View.VISIBLE);
                } else {
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void getAllProfileImage(String strMatriId) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "view_photo.php";
                Log.e("matri_search", "== " + URL);

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

                Log.e("view_photo", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    arrPhotosList = new ArrayList<beanPhotos>();

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        JSONObject responseData = obj.getJSONObject("responseData");
                        JSONObject responseKey = responseData.getJSONObject("1");

                        String photo1 = responseKey.getString("photo1").toString().trim();
                        photo2 = responseKey.getString("photo2").toString().trim();

                        if (photo2.equalsIgnoreCase("photo2")) {

                            Intent intent = new Intent(ViewImageActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Log.e("first if", "if 1");
                        }

                        String photo3 = responseKey.getString("photo3").toString().trim();
                        String photo4 = responseKey.getString("photo4").toString().trim();
                        String photo5 = responseKey.getString("photo5").toString().trim();
                        String photo6 = responseKey.getString("photo6").toString().trim();

                        arrPhotosList.add(new beanPhotos("1", photo1));
                        arrPhotosList.add(new beanPhotos("2", photo2));
                        arrPhotosList.add(new beanPhotos("3", photo3));
                        arrPhotosList.add(new beanPhotos("4", photo4));
                        arrPhotosList.add(new beanPhotos("5", photo5));
                        arrPhotosList.add(new beanPhotos("6", photo6));


                        Log.e("sizeImages", arrPhotosList.size() + "");
                        if (arrPhotosList.size() > 0) {
                            adapter = new ProfilePhotoViewAdapter(ViewImageActivity.this, arrPhotosList);
                            viewPager.setAdapter(adapter);

                        }

                        if (photo2.equalsIgnoreCase("photo2")) {

                            Intent intent = new Intent(ViewImageActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Log.e("first if", "if 1");
                        }


                    } else if (photo2.equalsIgnoreCase("photo2")) {

                        Intent intent = new Intent(ViewImageActivity.this, LoginActivity.class);
                        startActivity(intent);

                        Log.e("second if", "if 2 ");
                    } else {
                        String msgError = obj.getString("message");
                        Toast.makeText(ViewImageActivity.this, "" + msgError, Toast.LENGTH_LONG).show();
                    }

                } catch (Throwable t) {
                    Log.e("Errrrrr", t.getMessage());
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


}

