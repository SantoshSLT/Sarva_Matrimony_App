package com.thegreentech.successStory.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.thegreentech.R;
import com.thegreentech.SearchResultActivity;
import com.thegreentech.SuccessStoryActivity;
import com.thegreentech.successStory.adapter.ViewStoryAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import Adepters.SuccessStoryAdapter;
import Models.beanUserSuccessStory;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utills.AppConstants;
import utills.NetworkConnection;


public class ViewSToryFragment extends Fragment {

    View view;
    Context context;
    private static final String ARG_SECTION_NUMBER = "section_number";
    RecyclerView rvStorySuccess;
    ImageView textEmptyView;
    ProgressBar progressBar1;
    SwipeRefreshLayout refresh;

    ArrayList<beanUserSuccessStory> StoryviewList = new ArrayList<>();
    ViewStoryAdapter storyAdapter;

    SharedPreferences prefUpdate;
    String UserId = "", matri_id = "", gender = "";


    public ViewSToryFragment() {
    }

    public static ViewSToryFragment newInstance(int sectionNumber) {
        ViewSToryFragment fragment = new ViewSToryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_story, container, false);
        context = getActivity();
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserId = prefUpdate.getString("user_id", "");
        matri_id = prefUpdate.getString("matri_id", "");
        gender = prefUpdate.getString("gender", "");
        Log.e("Save Data", "UserId=> " + UserId + "  MatriId=> " + matri_id + "  Gender=> " + gender);


        init();

        if (NetworkConnection.hasConnection(getActivity())){
            getSuccessStoryRequest();

        }else
        {
            AppConstants.CheckConnection( getActivity());
        }



        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkConnection.hasConnection(getActivity())){
                    getSuccessStoryRequest();

                }else
                {
                    AppConstants.CheckConnection( getActivity());
                }

            }
        });

        return view;
    }


    public void init() {

        textEmptyView = (ImageView) view.findViewById(R.id.textEmptyView);
        rvStorySuccess = (RecyclerView) view.findViewById(R.id.rvStorySuccess);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1 );
        refresh = view.findViewById(R.id.refresh);
        rvStorySuccess.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStorySuccess.setHasFixedSize(true);


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getSuccessStoryRequest()
    {

        refresh.setRefreshing(true);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                /*String paramsMatriId = params[0];*/

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"success_story.php";
                Log.e("URL success_story", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                /*BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);*/


                /*List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);*/

                try
                {
                    /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));*/
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
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

                Log.e("success_story", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        StoryviewList = new ArrayList<beanUserSuccessStory>();
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);

                                String story_id = resItem.getString("story_id");
                                String weddingphoto = resItem.getString("weddingphoto");
                                String weddingphoto_type = resItem.getString("weddingphoto_type");
                                String bridename = resItem.getString("bridename");
                                String brideid = resItem.getString("brideid");
                                String groomname = resItem.getString("groomname");
                                String groomid = resItem.getString("groomid");
                                String marriagedate = resItem.getString("marriagedate");
                                String engagement_date = resItem.getString("engagement_date");
                                String address = resItem.getString("address");
                                String country = resItem.getString("country");
                                String successmessage = resItem.getString("successmessage");


                                StoryviewList.add(new beanUserSuccessStory(story_id, weddingphoto, weddingphoto_type,
                                        bridename, brideid, groomname, groomid, marriagedate, engagement_date, address,
                                        country, successmessage));

                            }
                            Log.e("list",StoryviewList.size() +"");

                            if (StoryviewList.size() > 0) {

                                storyAdapter = new ViewStoryAdapter(getActivity(), StoryviewList);
                                rvStorySuccess.setAdapter(storyAdapter);

                            } else {
                                rvStorySuccess.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            refresh.setRefreshing(false);
                            rvStorySuccess.setVisibility(View.GONE);
                            textEmptyView.setVisibility(View.VISIBLE);
                        }
                        refresh.setRefreshing(false);

                    } else {
                        refresh.setRefreshing(false);
                        rvStorySuccess.setVisibility(View.GONE);
                        textEmptyView.setVisibility(View.VISIBLE);
                    }
                    refresh.setRefreshing(false);

                } catch (Exception t) {
                    refresh.setRefreshing(false);

                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }



}
