package com.thegreentech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Adepters.PhotosAdapter;
import Models.beanPhotos;
import RestAPI.ApiClient;
import RestAPI.ApiInterface;
import RoboPOJO.ProfileManagePhotoResponse;
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
import retrofit2.Response;
import utills.AppConstants;
import utills.NetworkConnection;


public class ManagePhotoActivity extends AppCompatActivity {
    static final String TAG = ManagePhotoActivity.class.getSimpleName();

    private AlertDialog alertDialog;


    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;

    ImageView imgUserPhotos;
    TextView textUsername, textMatriId, tvPhotoCounter;
    String PhotoId = "";
    int position;
    SwipeRefreshLayout refresh;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id = "",gender="";

    private RecyclerView recyclerPhoto;
    private ProgressBar progress1;

    private PhotosAdapter adapterPhotos;
    private ArrayList<beanPhotos> arrPhotosList;

    String strFilePath = "";
    ImageView imgBussiness;
    TextView tvProfilePhoto;

    RelativeLayout relZoomImageView;
    ImageView imgZoomProfilePicture, imgzoomViewClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_photos);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(ManagePhotoActivity.this);
        matri_id = prefUpdate.getString("matri_id", "");
        gender = prefUpdate.getString("gender", "");

        init();
        onClick();

        String UserId = prefUpdate.getString("user_id", "");
        if (!UserId.equalsIgnoreCase("")) {
            String email = prefUpdate.getString("email", "");
            String username = prefUpdate.getString("username", "");
            String profile_image = prefUpdate.getString("profile_image", "");
            String matri_id = prefUpdate.getString("matri_id", "");

            textUsername.setText(username);
            textMatriId.setText(matri_id);
        }

        if (NetworkConnection.hasConnection(ManagePhotoActivity.this)){
            ViewPhoto(matri_id);

        }else
        {
            AppConstants.CheckConnection(ManagePhotoActivity.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //getHoroscopeRequest(matri_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManagePhotoActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);

    }

    public void init() {
        btnBack = findViewById(R.id.btnBack);
        textviewHeaderText = findViewById(R.id.textviewHeaderText);
        textviewSignUp = findViewById(R.id.textviewSignUp);
        imgUserPhotos = findViewById(R.id.imgUserPhotos);
        textUsername = findViewById(R.id.textUsername);
        textMatriId = findViewById(R.id.textMatriId);
        refresh = findViewById(R.id.accept_refresh);
        relZoomImageView = findViewById(R.id.relZoomImageView);
        imgZoomProfilePicture = findViewById(R.id.imgZoomProfilePicture);
        imgzoomViewClose = findViewById(R.id.imgzoomViewClose);

        textviewHeaderText.setText("Manages Photos");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);


        progress1 = findViewById(R.id.progress1);

        recyclerPhoto = findViewById(R.id.recyclerPhoto);
        //GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ManagePhotoActivity.this, 2);
        recyclerPhoto.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerPhoto.setLayoutManager(mLayoutManager);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkConnection.hasConnection(ManagePhotoActivity.this)){
                    ViewPhoto(matri_id);

                }else
                {
                    AppConstants.CheckConnection(ManagePhotoActivity.this);
                }

            }
        });

    }


    public void onClick() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        relZoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        imgzoomViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relZoomImageView.setVisibility(View.GONE);
            }
        });

        // Upload Photo
        recyclerPhoto.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                try {
                    CardView cardView = child.findViewById(R.id.cardView);
                    ImageView imgEdit = child.findViewById(R.id.imgEdit);

                    imgBussiness = child.findViewById(R.id.imgBusinessImage);
                    position = recyclerView.getChildPosition(cardView);

                    //position = recyclerView.getChildPosition(imgEdit);

                    imgEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "position = " + String.valueOf(position));

                            PhotoId = arrPhotosList.get(position).getId().toString().trim();
                            Log.d(TAG, "photo id = " + PhotoId);

                            CropImage.activity(null)
                                    .setAllowRotation(false)
                                    .setAllowFlipping(false)
                                    .setAspectRatio(500, 500)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(ManagePhotoActivity.this);
                        }
                    });

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final ProgressBar progressBar;
                            progressBar = (ProgressBar) findViewById(R.id.progressBar);


                            if (!arrPhotosList.get(position).getImageURL().toString().trim().equalsIgnoreCase("")) {
                                Picasso.with(ManagePhotoActivity.this)
                                        .load(arrPhotosList.get(position).getImageURL().toString().trim())
                                        //.transform(new CircleTransform())
                                        .placeholder(R.drawable.loading1)
                                        .error(R.drawable.male)
                                        .into(imgZoomProfilePicture, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                progressBar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });

                                relZoomImageView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(ManagePhotoActivity.this, "Image not available.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void ViewPhoto(String strMatriId) {
      /*  progresDialog = new ProgressDialog(ManagePhotoActivity.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();*/
        refresh.setRefreshing(true);

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
                        String photo2 = responseKey.getString("photo2").toString().trim();
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

                        if (arrPhotosList.size() > 0) {
                            recyclerPhoto.setVisibility(View.VISIBLE);
                            //textEmptyView.setVisibility(View.GONE);
                            adapterPhotos = new PhotosAdapter(ManagePhotoActivity.this, arrPhotosList,gender);
                            recyclerPhoto.setAdapter(adapterPhotos);

                            String imgProfile = arrPhotosList.get(0).getImageURL().toString().trim();


                            Picasso.with(ManagePhotoActivity.this)
                                    .load(imgProfile)
                                    .placeholder(R.drawable.loadimage)
                                    .error(R.drawable.ic_profile1)
                                    .into(imgUserPhotos);
                        } else {
                            recyclerPhoto.setVisibility(View.GONE);
                        }

                    } else {
                        String msgError = obj.getString("message");
                        Toast.makeText(ManagePhotoActivity.this, "" + msgError, Toast.LENGTH_LONG).show();
                    }
                    refresh.setRefreshing(false);
                } catch (Exception t) {
                    Log.e("ndfjdshbjg",t.getMessage());
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Picasso.with(ManagePhotoActivity.this)
                        .load(result.getUri())
                        .placeholder(R.drawable.loadimage)
                        .error(R.drawable.ic_profile)
                        .into(imgBussiness);


                strFilePath = result.getUri().toString().replace("file://", "");

                if (!strFilePath.equalsIgnoreCase("")) {

                    if (NetworkConnection.hasConnection(ManagePhotoActivity.this)){
                        uploadImage(matri_id, strFilePath, PhotoId);

                    }else
                    {
                        AppConstants.CheckConnection(ManagePhotoActivity.this);
                    }



                } else {
                    Toast.makeText(ManagePhotoActivity.this, "Please select profile picture.", Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }



    private void uploadImage(String strUserId, String strfilePath, final String PhotoId) {


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final ProgressDialog progressDialogSendReq = new ProgressDialog(ManagePhotoActivity.this);
        progressDialogSendReq.setCancelable(false);
        progressDialogSendReq.setMessage("Please_Wait");
        progressDialogSendReq.setIndeterminate(true);
        progressDialogSendReq.show();


        File file = new File(strfilePath);
        Log.d("ravi", "file is = " + file.toString());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody index = RequestBody.create(MediaType.parse("text/plain"), PhotoId);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image_path", file.getName(), requestFile);


        Call<ProfileManagePhotoResponse> call = apiService.postManagePhotoProfile(user_id, image, index);// uplaod_photo.php



//        retrofit2.Call<ProfileManagePhotoResponse> req = apiService.postManagePhotoProfile(user_id, image, index);
//        req.enqueue(new Callback<ProfileManagePhotoResponse>() {
//            @Override
//            public void onResponse(Call<ProfileManagePhotoResponse> call, Response<ProfileManagePhotoResponse> response) {
//                // Do Something
//            }
//
//            @Override
//            public void onFailure(Call<ProfileManagePhotoResponse> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });


        call.enqueue(new retrofit2.Callback<ProfileManagePhotoResponse>() {
            @Override
            public void onResponse(Call<ProfileManagePhotoResponse> call, retrofit2.Response<ProfileManagePhotoResponse> response) {
                ProfileManagePhotoResponse profileManagePhotoResponse = response.body();
                Log.e("Responce =", "" + profileManagePhotoResponse);

                if (profileManagePhotoResponse.getStatus().equalsIgnoreCase("1")) {
                    String message = profileManagePhotoResponse.getMessage();
                    Toast.makeText(ManagePhotoActivity.this, message, Toast.LENGTH_SHORT).show();

                    String responseImage = profileManagePhotoResponse.getImage();

                    if (PhotoId.equalsIgnoreCase("1")) {
                        Picasso.with(ManagePhotoActivity.this)
                                .load(responseImage)
                                .placeholder(R.drawable.loadimage)
                                .into(imgUserPhotos);
                    }
                } else {
                    String msgError = profileManagePhotoResponse.getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagePhotoActivity.this);
                    builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                progressDialogSendReq.dismiss();
            }

            @Override
            public void onFailure(Call<ProfileManagePhotoResponse> call, Throwable t) {

            }
        });

    }

}
