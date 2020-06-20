package Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adepters.BlockedUserAdapter;
import Adepters.ShortlistedAdapter;
import Adepters.UserRecentDataAdapter;
import Adepters.UserRecentlyVisitedProfileAdapter;
import Models.beanUserData;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Viewd extends Fragment {


    ArrayList<String> tokans;
    private ImageView textEmptyView;
    private RecyclerView recyclerUser;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<beanUserData> arrShortListedUser;
    ProgressBar progressBar1;
    String HeaderTitle = "", PageType = "";
    Activity activity;
    SharedPreferences prefUpdate;
    String matri_id = "", Gender = "";
    ProgressDialog progresDialog;
    private UserRecentlyVisitedProfileAdapter adapterUserData;
    SwipeRefreshLayout refresh;


    public Fragment_Viewd() {
        // Required empty public constructor
    }


    public static Fragment_Viewd newInstance(int sectionNumber) {
        Fragment_Viewd fragment = new Fragment_Viewd();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.fragment_fragment__viewd, container, false);
        //
        tokans = new ArrayList<>();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");
        Gender = prefUpdate.getString("gender", "");


        Log.e("Save Data= ", " MatriId=> " + matri_id);

        PageType = "3";
        textEmptyView = vv.findViewById(R.id.textEmptyView);
        recyclerUser = (RecyclerView) vv.findViewById(R.id.recyclerUser);
        progressBar1 = (ProgressBar) vv.findViewById(R.id.progressBar1);
        recyclerUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerUser.setHasFixedSize(true);

        arrShortListedUser = new ArrayList<beanUserData>();
        refresh = vv.findViewById(R.id.refresh);


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())){

                    getShortlistedProfileRequest(matri_id, Gender);

                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });
        if (NetworkConnection.hasConnection(getActivity())){

            getShortlistedProfileRequest(matri_id, Gender);

        }else
        {
            AppConstants.CheckConnection(getActivity());
        }

        return vv;
    }


    private void getShortlistedProfileRequest(String strMatriId, final String gender) {
        //progressBar1.setVisibility(View.VISIBLE);
        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];
                String paramGender = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (PageType.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "shortlisted.php";
                    Log.e("URL shortlisted", "== " + URL);
                }
                else if (PageType.equalsIgnoreCase("2")) {
                    URL = AppConstants.MAIN_URL + "blocklist.php";
                    Log.e("blocklist", "== " + URL);
                }
                else if (PageType.equalsIgnoreCase("3")) {
                    URL = AppConstants.MAIN_URL + "profile_viewd_by_me.php";
                    Log.e("profile_viewd_by_me", "== " + URL);
                }
                else if (PageType.equalsIgnoreCase("4")) {
                    URL = AppConstants.MAIN_URL + "profile_visited_by_i.php";
                    Log.e("profile_visited_by_i", "== " + URL);
                }
                else if (PageType.equalsIgnoreCase("5")) {
                    URL = AppConstants.MAIN_URL + "wath_mobileno.php";
                    Log.e("wath_mobileno", "== " + URL);
                }


                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair GenderPAir = new BasicNameValuePair("gender", paramGender);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(GenderPAir);

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

                Log.e("shortlisted", "==" + result);
                refresh.setRefreshing(true);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrShortListedUser = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);


                                String matri_id1 = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String birthdate = resItem.getString("birthdate");
                                String ocp_name = resItem.getString("ocp_name");
                                String height = resItem.getString("height");
                                //String Address=ocp_name;
                                String Address = resItem.getString("profile_text");
                                String city_name = resItem.getString("city_name");
                                String country_name = resItem.getString("country_name");
                                String photo1_approve = resItem.getString("photo1_approve");
                                String photo_view_status = resItem.getString("photo_view_status");
                                String photo_protect = resItem.getString("photo_protect");
                                String photo_pswd = resItem.getString("photo_pswd");
                                String gender1 = resItem.getString("gender");
                                String is_shortlisted = resItem.getString("is_shortlisted");
                                String is_blocked = resItem.getString("is_blocked");
                                String is_favourite = resItem.getString("is_favourite");
                                String user_profile_picture = resItem.getString("user_profile_picture");
                                tokans.add(resItem.getString("tokan"));
                                arrShortListedUser.add(new beanUserData(matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, is_shortlisted, is_blocked, is_favourite, user_profile_picture));

                            }

//                            adapterUserData = new UserRecentlyVisitedProfileAdapter(activity,arrShortListedUser, recyclerUser,tokans);
//                            recyclerUser.setAdapter(adapterUserData);

                            if (arrShortListedUser.size() > 0) {
                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                if (PageType.equalsIgnoreCase("2")) {
                                    BlockedUserAdapter adapterBlockedUser = new BlockedUserAdapter(getActivity(), arrShortListedUser, recyclerUser);
                                    recyclerUser.setAdapter(adapterBlockedUser);

                                } else {
                                    ShortlistedAdapter adapterShortlistedUser =
                                            new ShortlistedAdapter(getActivity(), arrShortListedUser, recyclerUser,tokans);
                                    recyclerUser.setAdapter(adapterShortlistedUser);
                                }

                            } else {
                                recyclerUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        textEmptyView.setVisibility(View.VISIBLE);
                    }
                    refresh.setRefreshing(false);
                    progressBar1.setVisibility(View.GONE);
                } catch (Exception t) {
                    Log.d("ERRRR", t.toString());
                    progressBar1.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                }
                progressBar1.setVisibility(View.GONE);
                refresh.setRefreshing(false);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId, gender);
    }

}
