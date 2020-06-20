package Fragments;


import android.content.SharedPreferences;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.thegreentech.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adepters.PhotoRequestAdapter;
import Models.beanUserData;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;
import utills.NetworkConnection;


public class FragmentPhotoRequestReceived extends Fragment {
    String TAG = "FragmentPhotoAccepted";
    View rootView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerPhotoAccepted;
    private PhotoRequestAdapter adapterPhotoAccepted;
    private ArrayList<beanUserData> arrPhotoPasswordList;
    ProgressBar progressBar1;
    ImageView textEmptyView;
    SwipeRefreshLayout refresh;
    SharedPreferences prefUpdate;
    String matri_id = "";
    //ProgressDialog progresDialog;

    public FragmentPhotoRequestReceived() {
    }

    public static FragmentPhotoRequestReceived newInstance(int sectionNumber) {
        FragmentPhotoRequestReceived fragment = new FragmentPhotoRequestReceived();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photo_request, container, false);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        textEmptyView = rootView.findViewById(R.id.textEmptyView);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        recyclerPhotoAccepted = (RecyclerView) rootView.findViewById(R.id.recyclerPhotoAccepted);
        recyclerPhotoAccepted.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPhotoAccepted.setHasFixedSize(true);
        arrPhotoPasswordList = new ArrayList<beanUserData>();
        refresh = rootView.findViewById(R.id.refresh);


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())){
                    getPhotoAcceptedRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });
        if (NetworkConnection.hasConnection(getActivity())){
            getPhotoAcceptedRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }

        return rootView;

    }

    private void getPhotoAcceptedRequest(String strMatriId) {
        refresh.setRefreshing(true);
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("ph_reqid", strMatriId);
        client.post(AppConstants.MAIN_URL + "photo_request_receive_all.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                try {
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrPhotoPasswordList = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String user_id = resItem.getString("user_id");
                                String matri_id1 = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String birthdate = resItem.getString("birthdate");
                                String ocp_name = resItem.getString("ocp_name");
                                String height = resItem.getString("height");
                                //String Address=ocp_name;//resItem.getString("height");
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
                                String ph_reqid = resItem.getString("ph_reqid");
                                String receiver_response = resItem.getString("receiver_response");
                                String ph_reqdate = resItem.getString("ph_reqdate");


                                arrPhotoPasswordList.add(new beanUserData(user_id, matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, is_shortlisted, is_blocked, is_favourite, user_profile_picture, ph_reqid,
                                        receiver_response, ph_reqdate));

                            }

                            if (arrPhotoPasswordList.size() > 0) {

                                adapterPhotoAccepted = new PhotoRequestAdapter(getActivity(), arrPhotoPasswordList, recyclerPhotoAccepted, "PhotoReqReceived");
                                recyclerPhotoAccepted.setAdapter(adapterPhotoAccepted);
                            } else {
                                recyclerPhotoAccepted.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }

                        }


                    } else {
                        String msgError = obj.getString("message");
                        Log.e("msgg",msgError);
                        refresh.setRefreshing(false);
                        progressBar1.setVisibility(View.GONE);
                        recyclerPhotoAccepted.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }
                    refresh.setRefreshing(false);
                    progressBar1.setVisibility(View.GONE);

                } catch (Exception t) {
                    Log.e("msgg",t.getMessage());
                    progressBar1.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                }

            }
        });
    }
}
