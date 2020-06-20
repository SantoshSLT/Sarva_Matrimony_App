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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import java.util.Iterator;
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


public class FragmentSettingContact extends Fragment {
    String TAG = "FragmentSearch";
    View rootView;
    Activity activity;
    private static final String ARG_SECTION_NUMBER = "section_number";
    SwipeRefreshLayout refres;

    TextView textVisibleMembers;
    TextView btnEdit, btnClose, btnSubmit;

    RelativeLayout relEditMembersView;
    LinearLayout linSlidingDrawer;
    RadioButton radioAcceptedPaidMembers, radioPaidMembers;


    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id = "";

    public FragmentSettingContact() {
    }

    public static FragmentSettingContact newInstance(int sectionNumber) {
        FragmentSettingContact fragment = new FragmentSettingContact();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting_contact, container, false);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");

        textVisibleMembers = rootView.findViewById(R.id.textVisibleMembers);
        btnEdit = rootView.findViewById(R.id.btnEdit);

        relEditMembersView = rootView.findViewById(R.id.relEditMembersView);
        relEditMembersView.setVisibility(View.GONE);
        linSlidingDrawer = rootView.findViewById(R.id.linSlidingDrawer);

        radioAcceptedPaidMembers = rootView.findViewById(R.id.radioAcceptedPaidMembers);
        radioPaidMembers = rootView.findViewById(R.id.radioPaidMembers);
        refres = rootView.findViewById(R.id.refresh);
        btnClose = rootView.findViewById(R.id.btnClose);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relEditMembersView.setVisibility(View.VISIBLE);

                linSlidingDrawer.setVisibility(View.VISIBLE);

                Animation bottomUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up_dialog);
                linSlidingDrawer.startAnimation(bottomUp);
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

                if (radioAcceptedPaidMembers.isChecked()) {
                    editContactVisibilityRequest(matri_id, "0");
                } else {
                    editContactVisibilityRequest(matri_id, "1");
                }

            }
        });
        if (NetworkConnection.hasConnection(getActivity())){
            setContactPrivacyRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity1) {
        super.onAttach(activity1);
        activity = activity1;
    }


    private void setContactPrivacyRequest(String strMatriId) {
        refres.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "contact_details.php";
                Log.e("contact_details", "== " + URL);

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

                Log.e("contact_details", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String msg = obj.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {

                            Iterator<String> resIter = responseData.keys();
                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String contact_view_security = resItem.getString("contact_view_security");
                                String memberType = "";
                                if (contact_view_security.equalsIgnoreCase("0")) {
                                    memberType = "0";
                                    textVisibleMembers.setText("Show To Express Interest Accepted Paid Members");
                                    radioAcceptedPaidMembers.setChecked(true);
                                    radioPaidMembers.setChecked(false);
                                } else if (contact_view_security.equalsIgnoreCase("1")) {
                                    memberType = "1";
                                    textVisibleMembers.setText("Show To Paid Members");
                                    radioAcceptedPaidMembers.setChecked(false);
                                    radioPaidMembers.setChecked(true);
                                }

                            }


                        }
                        else {
                            refres.setRefreshing(false);
                            Toast.makeText(activity, "1ooo...." + msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        refres.setRefreshing(false);
                        Toast.makeText(activity, "re...." + msg, Toast.LENGTH_SHORT).show();
                    }


                    refres.setRefreshing(false);
                } catch (Exception t) {
                    refres.setRefreshing(false);
                    Log.e("kflj", t.getMessage());
                    Toast.makeText(activity, "ex..." + t.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    private void editContactVisibilityRequest(String strMatriId, final String visivility_status) {
        refres.setRefreshing(true);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramsVisibilityStatus = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "contact_visible.php";
                Log.e("update_photo_visible", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair photo_view_statusPair = new BasicNameValuePair("contact_view_security", paramsVisibilityStatus);

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

                Log.e("contact_visible", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                        if (visivility_status.equalsIgnoreCase("0")) {
                            textVisibleMembers.setText("Show To Express Interest Accepted Paid Members");
                        } else if (visivility_status.equalsIgnoreCase("1")) {
                            textVisibleMembers.setText("Show To Paid Members");
                        }
                        setContactPrivacyRequest(matri_id);


                    } else {
                        refres.setRefreshing(false);
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                    refres.setRefreshing(false);
                } catch (Exception t) {
                    Toast.makeText(activity, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    refres.setRefreshing(false);
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, visivility_status);
    }


}
