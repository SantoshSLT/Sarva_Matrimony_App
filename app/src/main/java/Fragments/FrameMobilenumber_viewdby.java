package Fragments;

import android.app.ProgressDialog;
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

import Adepters.ShortlistedAdapter;
import Models.beanUserData;
import cz.msebera.android.httpclient.Header;
import utills.AppConstants;
import utills.NetworkConnection;


public class FrameMobilenumber_viewdby extends Fragment {

    ArrayList<String> tokans;
    private RecyclerView recyclerUser;

    private ArrayList<beanUserData> arrShortListedUser;
    ProgressBar progressBar1;
    ImageView textEmptyView;
    SharedPreferences prefUpdate;
    String matri_id = "";
    ProgressDialog progresDialog;
    SwipeRefreshLayout refresh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.frame_mobilenumber_viewdby, container, false);
        tokans = new ArrayList<>();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        textEmptyView =  vv.findViewById(R.id.textEmptyView);
        recyclerUser = (RecyclerView) vv.findViewById(R.id.recyclerUser);
        progressBar1 = (ProgressBar) vv.findViewById(R.id.progressBar1);
        recyclerUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerUser.setHasFixedSize(true);
        refresh = vv.findViewById(R.id.refresh);
        arrShortListedUser = new ArrayList<beanUserData>();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {

                if (NetworkConnection.hasConnection(getActivity())){
                    getShortlistedProfileRequest(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });

        if (NetworkConnection.hasConnection(getActivity())){
            getShortlistedProfileRequest(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }
        return vv;
    }

    private void getShortlistedProfileRequest(String strMatriId) {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("matri_id", strMatriId);
        String URL = AppConstants.MAIN_URL + "wath_mobileno.php";
        client.post(URL, params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                refresh.setRefreshing(false);
                Log.e("shortlisted", "==" + responseString);
                tokans = new ArrayList<>();
                tokans.clear();
                try {
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrShortListedUser = new ArrayList<beanUserData>();
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
                                arrShortListedUser.add(new beanUserData(user_id, matri_id1, username, birthdate, ocp_name, height, Address, city_name, country_name, photo1_approve, photo_view_status, photo_protect,
                                        photo_pswd, gender1, is_shortlisted, is_blocked, is_favourite, user_profile_picture));

                            }

                            if (arrShortListedUser.size() > 0) {

                                recyclerUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                ShortlistedAdapter adapterShortlistedUser =
                                        new ShortlistedAdapter(getActivity(), arrShortListedUser, recyclerUser,tokans);
                                recyclerUser.setAdapter(adapterShortlistedUser);


                            } else {
                                recyclerUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerUser.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }
                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t) {
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });
    }
}
