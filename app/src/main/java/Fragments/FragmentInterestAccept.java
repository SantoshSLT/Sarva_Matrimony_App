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

import Adepters.ExpressInterestAdapter;
import Models.beanUserData;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;
import utills.NetworkConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInterestAccept extends Fragment {

    String TAG = "Fragmentaccepted";
    View rootView;
    SwipeRefreshLayout refresh;

    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView recyclerPhotoAccepted;

    private ExpressInterestAdapter adapterPhotoAccepted;
    private ArrayList<beanUserData> arrPhotoPasswordList;
    ProgressBar progressBar1;
    ImageView textEmptyView;
    SharedPreferences prefUpdate;
    String matri_id = "";

    public FragmentInterestAccept() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_interest_accept, container, false);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        recyclerPhotoAccepted = (RecyclerView) rootView.findViewById(R.id.recyclerPhotoAccepted);
        recyclerPhotoAccepted.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerPhotoAccepted.setHasFixedSize(true);
        refresh = rootView.findViewById(R.id.accept_refresh);
        textEmptyView = rootView.findViewById(R.id.textEmptyView);
        arrPhotoPasswordList = new ArrayList<beanUserData>();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())){
                    getAcceptedRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });

        if (NetworkConnection.hasConnection(getActivity())){
            getAcceptedRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }


        return rootView;

    }

    private void getAcceptedRequest(String strMatriId) {

       // progressBar1.setVisibility(View.VISIBLE);
        refresh.setRefreshing(true);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("receiver_id", strMatriId);
        client.post(AppConstants.MAIN_URL + "intrest_accept_list.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("intrest_accept_all", "==" + responseString);
                refresh.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrPhotoPasswordList = new ArrayList<beanUserData>();
                        ArrayList<String> tokans = new ArrayList<>();
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
                                String Address = "";
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
                                String ei_id = resItem.getString("ei_id");
                                String receiver_response = resItem.getString("receiver_response");
                                String sent_date = "";
                                tokans.add(resItem.getString("tokan"));
                                //String sent_date = resItem.getString("ei_sent_date");

                                arrPhotoPasswordList.add(new beanUserData(user_id, matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, is_shortlisted, is_blocked, is_favourite, user_profile_picture, ei_id,
                                        receiver_response, sent_date));
                            }

                            if (arrPhotoPasswordList.size() > 0) {
                                recyclerPhotoAccepted.setVisibility(View.VISIBLE);

                                adapterPhotoAccepted = new ExpressInterestAdapter(getActivity(), arrPhotoPasswordList, recyclerPhotoAccepted, "Received",tokans);
                                recyclerPhotoAccepted.setAdapter(adapterPhotoAccepted);
                            } else {
                                recyclerPhotoAccepted.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }

                        }


                    } else {
                        recyclerPhotoAccepted.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }


                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t) {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        });
    }

}
