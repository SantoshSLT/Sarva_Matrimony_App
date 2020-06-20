package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.R;

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


public class FragmentSettingPhotos extends Fragment {
    String TAG = "FragmentSettingPhotos";
    View rootView;
    Activity activity;
    private static final String ARG_SECTION_NUMBER = "section_number";
    SwipeRefreshLayout refresh;

    TextView textVisibleMembers,
            btnEdit, btnSetPhotoPassword, btnRemovePhotoPassword, btnChangePhotoPassword;

    CardView cardPhoto;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id = "";

    RelativeLayout relEditMembersView;
    LinearLayout linSlidingDrawer;
    RadioButton radioHiddenAll, radioAllMembers, radioPaidMembers;
    TextView btnClose, btnSubmit;

    TextView tvPassword;
    String photo_password = "";

    EditText editPassword;
    EditText edtNewPassword;

    public FragmentSettingPhotos() {
    }

    public static FragmentSettingPhotos newInstance(int sectionNumber) {
        FragmentSettingPhotos fragment = new FragmentSettingPhotos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting_photos, container, false);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        onclick();

        if (NetworkConnection.hasConnection(getActivity())){
            setPhotoPrivacyRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())){
                    setPhotoPrivacyRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPhotoPrivacyRequest(matri_id);
    }

    public void init() {
        textVisibleMembers = rootView.findViewById(R.id.textVisibleMembers);
        btnEdit = rootView.findViewById(R.id.btnEdit);
        btnSetPhotoPassword = rootView.findViewById(R.id.btnSetPhotoPassword);
        btnRemovePhotoPassword = rootView.findViewById(R.id.btnRemovePhotoPassword);
        btnChangePhotoPassword = rootView.findViewById(R.id.btnChangePhotoPassword);
        cardPhoto = rootView.findViewById(R.id.cardPhoto);
        refresh = rootView.findViewById(R.id.refresh);

        relEditMembersView = rootView.findViewById(R.id.relEditMembersView);
        relEditMembersView.setVisibility(View.GONE);
        linSlidingDrawer = rootView.findViewById(R.id.linSlidingDrawer);
        radioHiddenAll = rootView.findViewById(R.id.radioHiddenAll);
        radioAllMembers = rootView.findViewById(R.id.radioAllMembers);
        radioPaidMembers = rootView.findViewById(R.id.radioPaidMembers);

        tvPassword = rootView.findViewById(R.id.tvPassword);

        btnClose = rootView.findViewById(R.id.btnClose);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
    }

    public void onclick() {

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relEditMembersView.setVisibility(View.VISIBLE);

                linSlidingDrawer.setVisibility(View.VISIBLE);

                Animation bottomUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up_dialog);
                linSlidingDrawer.startAnimation(bottomUp);

                if (NetworkConnection.hasConnection(getActivity())){
                    setPhotoPrivacyRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });

        relEditMembersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomDown = AnimationUtils.loadAnimation(activity, R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown);
                linSlidingDrawer.setVisibility(View.GONE);

                relEditMembersView.startAnimation(bottomDown);
                relEditMembersView.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomDown = AnimationUtils.loadAnimation(activity, R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown);
                linSlidingDrawer.setVisibility(View.GONE);

                relEditMembersView.startAnimation(bottomDown);
                relEditMembersView.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomDown = AnimationUtils.loadAnimation(activity, R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown);
                linSlidingDrawer.setVisibility(View.GONE);

                relEditMembersView.startAnimation(bottomDown);
                relEditMembersView.setVisibility(View.GONE);

                if (radioHiddenAll.isChecked()) {

                    if (NetworkConnection.hasConnection(getActivity())) {
                        editVisibilityRequest(matri_id, "0");

                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }


                } else if (radioAllMembers.isChecked()) {
                    if (NetworkConnection.hasConnection(getActivity())) {
                        editVisibilityRequest(matri_id, "1");

                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }

                } else {
                    if (NetworkConnection.hasConnection(getActivity())) {
                        editVisibilityRequest(matri_id, "2");

                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }

                }
            }
        });

        btnRemovePhotoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvPassword.getText().toString().equalsIgnoreCase("") &&  tvPassword.getText().toString() != null)

                    if (NetworkConnection.hasConnection(getActivity())) {

                        removePhotoPassword(matri_id, tvPassword.getText().toString());

                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }


                else

                if (NetworkConnection.hasConnection(getActivity())) {

                    setPhotoPrivacyRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }

            }
        });

        btnSetPhotoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_photo_password, null);
                dialogBuilder.setView(dialogView);

                editPassword = (EditText) dialogView.findViewById(R.id.edtPassword);

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String strPassword = editPassword.getText().toString();
                        if (strPassword.equalsIgnoreCase("")) {
                            Toast.makeText(activity, "Please enter photo password", Toast.LENGTH_SHORT).show();
                        } else {

                            if (NetworkConnection.hasConnection(getActivity())) {

                                setPhotoPasswordRequest(matri_id, strPassword);

                            }else
                            {
                                AppConstants.CheckConnection(getActivity());
                            }

                            btnRemovePhotoPassword.setVisibility(View.VISIBLE);
                            btnChangePhotoPassword.setVisibility(View.VISIBLE);
                            btnSetPhotoPassword.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

            }

        });


        btnChangePhotoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_change_photo_password, null);
                dialogBuilder.setView(dialogView);

                final EditText edtOldPassword = (EditText) dialogView.findViewById(R.id.edtOldPassword);
                edtNewPassword = (EditText) dialogView.findViewById(R.id.edtNewPassword);

                dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String strOldPassword = edtOldPassword.getText().toString();
                        String strNewPassword = edtNewPassword.getText().toString();
                        if (strOldPassword.equalsIgnoreCase("")) {
                            Toast.makeText(activity, "Please enter old photo password", Toast.LENGTH_SHORT).show();
                        } else if (strNewPassword.equalsIgnoreCase("")) {
                            Toast.makeText(activity, "Please enter new photo password", Toast.LENGTH_SHORT).show();
                        } else {

                            if (NetworkConnection.hasConnection(getActivity())) {

                                changePhotoPasswordRequest(matri_id, strOldPassword, strNewPassword);

                            }else
                            {
                                AppConstants.CheckConnection(getActivity());
                            }


                            btnRemovePhotoPassword.setVisibility(View.VISIBLE);
                            btnChangePhotoPassword.setVisibility(View.VISIBLE);
                            btnSetPhotoPassword.setVisibility(View.GONE);
                            dialog.dismiss();

                        }
                    }
                });
                dialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();


            }
        });


    }

    @Override
    public void onAttach(Activity activity1) {
        super.onAttach(activity1);
        activity = activity1;
    }


    private void setPhotoPrivacyRequest(String strMatriId) {
		/*progresDialog= new ProgressDialog(activity);
		progresDialog.setCancelable(false);
		progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();
*/
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "photo_visible.php";
                Log.e("photo_visible", "== " + URL);

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

                Log.e("set photo visibility", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        JSONObject responseArr = responseData.getJSONObject("1");
                        String photo_view_status = responseArr.getString("photo_view_status");
                        photo_password = responseArr.getString("photo_password");

                        if (photo_view_status.equalsIgnoreCase("0")) {
                            textVisibleMembers.setText("Hidden for All");
                            radioHiddenAll.setChecked(true);
                        } else if (photo_view_status.equalsIgnoreCase("1")) {
                            textVisibleMembers.setText("Visible to All Members");
                            radioAllMembers.setChecked(true);
                        } else if (photo_view_status.equalsIgnoreCase("2")) {
                            textVisibleMembers.setText("Visible to Paid Members");
                            radioPaidMembers.setChecked(true);
                        }

                        tvPassword.setText(photo_password);

                        if (!photo_password.equalsIgnoreCase("")) {
                            btnSetPhotoPassword.setVisibility(View.GONE);
                            btnRemovePhotoPassword.setVisibility(View.VISIBLE);
                            btnChangePhotoPassword.setVisibility(View.VISIBLE);
                        }

                    } else {
                        refresh.setRefreshing(false);

                    }


                    refresh.setRefreshing(false);
                } catch (Exception t) {
                    Log.e("menucontact", t.getMessage());
                    refresh.setRefreshing(false);
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    private void editVisibilityRequest(String strMatriId, String visivility_status) {
        refresh.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsVisibilityStatus = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "update_photo_visible.php";
                Log.e("update_photo_visible", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair photo_view_statusPair = new BasicNameValuePair("photo_view_status", paramsVisibilityStatus);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(photo_view_statusPair);

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

                Log.e("update_photo_visible", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String msgError = obj.getString("message");

                    if (status.equalsIgnoreCase("1")) {
                        String message = obj.getString("message");
                        final String photo_view_status = obj.getString("photo_view_status");

                        if (photo_view_status.equalsIgnoreCase("0")) {
                            textVisibleMembers.setText("Hidden for All");
                        } else if (photo_view_status.equalsIgnoreCase("1")) {
                            textVisibleMembers.setText("Visible to All Members");
                        } else if (photo_view_status.equalsIgnoreCase("2")) {
                            textVisibleMembers.setText("Visible to Paid Members");
                        }
                        setPhotoPrivacyRequest(matri_id);

                    } else {
                        refresh.setRefreshing(false);
                        Toast.makeText(activity, "" + msgError, Toast.LENGTH_SHORT).show();
                    }


                    refresh.setRefreshing(false);
                } catch (Throwable t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, visivility_status);
    }


    private void setPhotoPasswordRequest(String strMatriId, String strPassword) {
        refresh.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsPassword = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "photo_set_password.php";
                Log.e("photo_set_password", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair PhotoPasswordPair = new BasicNameValuePair("photo_pass", paramsPassword);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(PhotoPasswordPair);

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

                Log.e("set_photo_password", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String message = obj.getString("message");


                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        //tvPassword.setText(photo_password);

                        String strPassword = editPassword.getText().toString();
                        tvPassword.setText(strPassword);

                        if (!photo_password.equalsIgnoreCase("")) {
                            btnSetPhotoPassword.setVisibility(View.GONE);
                            btnRemovePhotoPassword.setVisibility(View.VISIBLE);
                            btnChangePhotoPassword.setVisibility(View.VISIBLE);
                            setPhotoPrivacyRequest(matri_id);

                        }

                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }


                    refresh.setRefreshing(false);
                } catch (Throwable t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, strPassword);
    }


    private void changePhotoPasswordRequest(String strMatriId, String strOldPassword, String strPassword) {
        refresh.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsOldPassword = params[1];
                String paramsNewPassword = params[2];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "change_photo_password.php";
                Log.e("change_photo_password", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair oldPasswordPair = new BasicNameValuePair("old_pass", paramsOldPassword);
                BasicNameValuePair newPasswordPair = new BasicNameValuePair("new_pass", paramsNewPassword);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(oldPasswordPair);
                nameValuePairList.add(newPasswordPair);

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

                Log.e("change_photo_password", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        String strPassword = edtNewPassword.getText().toString();
                        tvPassword.setText(strPassword);
                        setPhotoPrivacyRequest(matri_id);


                    } else {
                        refresh.setRefreshing(false);
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                    refresh.setRefreshing(false);
                } catch (Throwable t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, strOldPassword, strPassword);
    }


    private void removePhotoPassword(String strMatriId, String photoPassword) {
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsPhotoPassword = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "remove_photo_password.php";
                Log.e("photo_visible", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair PhotoPasswordPair = new BasicNameValuePair("current_photo_password", paramsPhotoPassword);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(PhotoPasswordPair);

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

                Log.e("remove_photo_password", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String msg = obj.getString("message");


                    if (status.equalsIgnoreCase("1")) {
						/*JSONObject responseData = obj.getJSONObject("responseData");
						JSONObject responseArr = responseData.getJSONObject("1");
						String photo_view_status=responseArr.getString("photo_view_status");
						String photo_password = responseArr.getString("photo_password");

						if(photo_view_status.equalsIgnoreCase("0"))
						{
							textVisibleMembers.setText("Hidden for All");
							radioHiddenAll.setChecked(true);
						}else if(photo_view_status.equalsIgnoreCase("1"))
						{
							textVisibleMembers.setText("Visible to All Members");
							radioAllMembers.setChecked(true);
						}else if(photo_view_status.equalsIgnoreCase("2"))
						{
							textVisibleMembers.setText("Visible to Paid Members");
							radioPaidMembers.setChecked(true);
						}*/


                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

                        tvPassword.setText("");

                        //if (!photo_password.equalsIgnoreCase("")) {
                        btnSetPhotoPassword.setVisibility(View.VISIBLE);
                        btnRemovePhotoPassword.setVisibility(View.GONE);
                        btnChangePhotoPassword.setVisibility(View.GONE);
                        setPhotoPrivacyRequest(matri_id);
                        //}

                    } else {
                        Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }


                    refresh.setRefreshing(false);
                } catch (Throwable t) {
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, photoPassword);
    }


}
