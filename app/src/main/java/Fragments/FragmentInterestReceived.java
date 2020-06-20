package Fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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


public class FragmentInterestReceived extends Fragment
{
    String TAG = "FragmentPhotoAccepted";
    View rootView;
    SwipeRefreshLayout refresh;
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView rvexInterest;
    protected Handler handler;
    private ExpressInterestAdapter adapterPhotoAccepted;
    private ArrayList<beanUserData> arrPhotoPasswordList;
    ProgressBar progressBar1;
    ImageView textEmptyView;
    SharedPreferences prefUpdate;
    String matri_id = "";
    Context context;
    //ProgressDialog progresDialog;

    public FragmentInterestReceived() {
    }

    public static FragmentInterestReceived newInstance(int sectionNumber) {
        FragmentInterestReceived fragment = new FragmentInterestReceived();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_express_interest, container, false);

        context  =getActivity();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(context);
        matri_id = prefUpdate.getString("matri_id", "");
        Log.e("Save Data= ", " MatriId=> " + matri_id);

        progressBar1 = rootView.findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.GONE);
        rvexInterest =  rootView.findViewById(R.id.rvexInterest);
        rvexInterest.setLayoutManager(new LinearLayoutManager(context));
        rvexInterest.setHasFixedSize(true);
        refresh = rootView.findViewById(R.id.refresh);
        textEmptyView = rootView.findViewById(R.id.textEmptyView);
        arrPhotoPasswordList = new ArrayList<beanUserData>();
        handler = new Handler();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnection.hasConnection(getActivity())){
                    Receive_all(matri_id);
                }else
                {
                    AppConstants.CheckConnection(getActivity());
                }
            }
        });

        if (NetworkConnection.hasConnection(getActivity())){
            Receive_all(matri_id);
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }

        return rootView;

    }

    private void Receive_all(String strMatriId) {
      refresh.setRefreshing(true);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("matri_id",strMatriId);
        String URL=AppConstants.MAIN_URL +"intrest_receive_all.php";
        client.post(URL, params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("intrest_receive_all", "==" + responseString);
                refresh.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        arrPhotoPasswordList = new ArrayList<beanUserData>();
                        JSONObject responseData = obj.getJSONObject("responseData");
                        ArrayList<String> tokans = new ArrayList<>();

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String user_id = resItem.getString("user_id");
                                String matri_id = resItem.getString("matri_id");
                                String username = resItem.getString("username");
                                String gender1 = resItem.getString("gender");
                                String user_profile_picture = resItem.getString("user_profile_picture");
                                String ei_id = resItem.getString("ei_id");
                                String receiver_response = resItem.getString("receiver_response");
                                String sent_date = resItem.getString("ei_sent_date");
                                tokans.add(resItem.getString("tokan"));
                                arrPhotoPasswordList.add(new beanUserData(user_id, matri_id, username,gender1,user_profile_picture, ei_id,receiver_response, sent_date));
                            }

                            if (arrPhotoPasswordList.size() > 0) {

                                adapterPhotoAccepted = new ExpressInterestAdapter(context, arrPhotoPasswordList, rvexInterest, "Received",tokans);
                                rvexInterest.setAdapter(adapterPhotoAccepted);
                            } else {
                                rvexInterest.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }

                        }


                    } else {
                        String msgError = obj.getString("message");
                        Log.e("mssgggg",msgError);
                        rvexInterest.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }
                    refresh.setRefreshing(false);
                } catch (Exception t) {

                    Log.e("mssgggg",t.getMessage());
                    refresh.setRefreshing(false);
                }
                refresh.setRefreshing(false);
            }
        });
    }


}
