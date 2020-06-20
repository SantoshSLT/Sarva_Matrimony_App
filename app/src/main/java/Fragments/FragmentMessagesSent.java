package Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import Adepters.SentMessageAdapter;
import Models.beanMessages;
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


public class FragmentMessagesSent extends Fragment
{

	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";
	protected Handler handler;
	private boolean isViewShown = false;
	SwipeRefreshLayout refresh;
	private ImageView textEmptyView;
	private RecyclerView recyclerUser;
	private SentMessageAdapter adapterMessages;
	private ArrayList<beanMessages> arrSentMessagesList;
	//ProgressDialog progresDialog;
	ProgressBar progressBar1;
	SharedPreferences prefUpdate;
	String UserId="",matri_id="",gender="";

	public FragmentMessagesSent() {
	}


	public static FragmentMessagesSent newInstance(int sectionNumber)
	{
		FragmentMessagesSent fragment = new FragmentMessagesSent();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_messages_sent, container,false);
		prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
		UserId=prefUpdate.getString("user_id","");
		matri_id=prefUpdate.getString("matri_id","");
		gender=prefUpdate.getString("gender","");
		Log.e("Save Data","UserId=> "+UserId+"  MatriId=> "+matri_id+"  Gender=> "+gender);

		refresh = rootView.findViewById(R.id.refresh);
		textEmptyView =  rootView.findViewById(R.id.textEmptyView);
		recyclerUser =  rootView.findViewById(R.id.recyclerUser);
		progressBar1 = rootView.findViewById(R.id.progressBar1);
		recyclerUser.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerUser.setHasFixedSize(true);

		arrSentMessagesList = new ArrayList<beanMessages>();
		handler = new Handler();


		if (NetworkConnection.hasConnection(getActivity())){
			getMessagesRequest(matri_id);
		}else
		{
			AppConstants.CheckConnection(getActivity());
		}
		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (NetworkConnection.hasConnection(getActivity())){
					getMessagesRequest(matri_id);
				}else
				{
					AppConstants.CheckConnection(getActivity());
				}
			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		adapterMessages=null;
		arrSentMessagesList=null;
		recyclerUser.setAdapter(null);
	}

	@Override
	public void setUserVisibleHint(final boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);
	}



	private void getMessagesRequest(String MatriId)
	{
		refresh.setRefreshing(true);
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramsMatriId = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"sent_message.php";
				Log.e("URL_Sent", "== "+URL);
				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);


				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(MatriIdPair);

				try
				{
					UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
					httpPost.setEntity(urlEncodedFormEntity);
					Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));
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
			protected void onPostExecute(String result)
			{
				super.onPostExecute(result);
				refresh.setRefreshing(false);
				Log.e("Sent_Listing", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						arrSentMessagesList= new ArrayList<beanMessages>();
						JSONObject responseData = obj.getJSONObject("responseData");

						if (responseData.has("1"))
						{
							Iterator<String> resIter = responseData.keys();

							while (resIter.hasNext())
							{
								String key = resIter.next();
								JSONObject resItem = responseData.getJSONObject(key);

								String mes_id=resItem.getString("mes_id");
								String username=resItem.getString("username");
								String msg_important_status=resItem.getString("msg_important_status");
								String from_matri_id=resItem.getString("from_id");
								//Log.d("ravi","fromId=" + from_matri_id);
								String to_matri_id=resItem.getString("to_id");
								//Log.d("ravi","toId=" + to_matri_id);
								String subject=resItem.getString("subject");
								String message=resItem.getString("message");
								String sent_date=resItem.getString("sent_date");
								String msg_status=resItem.getString("msg_status");
								String status1=resItem.getString("status");
								String user_photo=resItem.getString("user_profile_picture");
								String is_favourit = resItem.getString("is_favourite");
								//String is_favorite="no";//resItem.getString("is_favorite");

								arrSentMessagesList.add(new beanMessages(mes_id,username,msg_important_status,from_matri_id,to_matri_id,
										subject,message,sent_date,msg_status,status1,user_photo,is_favourit));
							}

							if(arrSentMessagesList.size() > 0)
							{
								recyclerUser.setVisibility(View.VISIBLE);
								textEmptyView.setVisibility(View.GONE);

								adapterMessages = new SentMessageAdapter(activity,arrSentMessagesList, recyclerUser);
								recyclerUser.setAdapter(adapterMessages);
								adapterMessages.notifyDataSetChanged();
							}else
							{
								recyclerUser.setVisibility(View.GONE);
								textEmptyView.setVisibility(View.VISIBLE);
							}

						}



					}else
					{
						String msgError=obj.getString("message");
						recyclerUser.setVisibility(View.GONE);
						textEmptyView.setVisibility(View.VISIBLE);
					}


					refresh.setRefreshing(false);
				} catch (Exception t)
				{
					Log.e("memeee",t.getMessage());
					refresh.setRefreshing(false);
				}
				refresh.setRefreshing(false);
			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(MatriId);
	}


}
