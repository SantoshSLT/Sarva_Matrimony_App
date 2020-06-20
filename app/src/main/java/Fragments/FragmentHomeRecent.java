package Fragments;


import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Adepters.UserRecentDataAdapter;
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


public class FragmentHomeRecent extends Fragment
{
	String TAG="FragmentHomeRecent";
	View rootView;
	Context context;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";
	protected Handler handler;
	private boolean isViewShown = false;
	SwipeRefreshLayout refresh;


	private ImageView textEmptyView;
	private RecyclerView recyclerUser;
	private UserRecentDataAdapter adapterUserData;
	private ArrayList<beanUserData> arrUserDataList;
	//ProgressDialog progresDialog;
	ProgressBar progressBar1;
	SharedPreferences prefUpdate;
	String UserId="",matri_id="",gender="";

	public FragmentHomeRecent()
	{
	}
	public static FragmentHomeRecent newInstance(int sectionNumber)
	{
		FragmentHomeRecent fragment = new FragmentHomeRecent();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_recent, container,false);
		refresh = rootView.findViewById(R.id.refresh);

		prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
		UserId=prefUpdate.getString("user_id","");
		matri_id=prefUpdate.getString("matri_id","");
		gender=prefUpdate.getString("gender","");
		Log.e("Save Data","UserId=> "+UserId+"  MatriId=> "+matri_id+"  Gender=> "+gender);


		textEmptyView = (ImageView) rootView.findViewById(R.id.textEmptyView);
		recyclerUser = (RecyclerView) rootView.findViewById(R.id.recyclerUser);
		progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1 );
		recyclerUser.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerUser.setHasFixedSize(true);

		if (NetworkConnection.hasConnection(getActivity())){
			getUserDataRequest(matri_id,gender);
		}else
		{
			AppConstants.CheckConnection(getActivity());
		}

		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (NetworkConnection.hasConnection(getActivity())){
					getUserDataRequest(matri_id,gender);
				}else
				{
					AppConstants.CheckConnection(getActivity());
				}

			}
		});



//		try
//		{
//			adapterUserData.setOnLoadMoreListener(new OnLoadMoreListener()
//			{
//				@Override
//				public void onLoadMore()
//				{
//					arrUserDataList.add(null);
//					adapterUserData.notifyItemInserted(arrUserDataList.size() - 1);
//
//					handler.postDelayed(new Runnable()
//					{
//						@Override
//						public void run()
//						{
//							//   remove progress item
//							arrUserDataList.remove(arrUserDataList.size() - 1);
//							adapterUserData.notifyItemRemoved(arrUserDataList.size());
//							//add items one by one
//							int start = arrUserDataList.size();
//							int end = start + 20;
//
//							for (int i = start + 1; i <= end; i++)
//							{
//								arrUserDataList.add(new beanUserData(""+i, "Priyanka Chopra " +i, "150ft Ring Road, Nr.Balagi hall, Rajkot-360004 " +i, "GT1234" +i,
//										"NO", "NO", "NO","http://www.dailyexcelsior.com/wp-content/uploads/2017/03/203.jpg"));
//								adapterUserData.notifyItemInserted(arrUserDataList.size());
//							}
//							adapterUserData.setLoaded();
//							//or you can add all at once but do not forget to call adapterUserData.notifyDataSetChanged();
//						}
//					}, 2000);
//
//				}
//			});
//
//		}catch (Exception e)
//		{
//
//		}


		return rootView;

	}

	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;
		Log.e("call onAttach","1");
	}

	@Override
	public void setUserVisibleHint(final boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		adapterUserData=null;
		arrUserDataList=null;
		recyclerUser.setAdapter(null);
	}



	private void getUserDataRequest(String MatriId, String Gender)
	{
		//progressBar1.setVisibility(View.VISIBLE);
		refresh.setRefreshing(true);
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramMatriId = params[0];
				String paramGender = params[1];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"resent_join.php";
				Log.e("URL_Recent", "== "+URL);
				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramMatriId);
				BasicNameValuePair GenderPair = new BasicNameValuePair("gender",paramGender);

				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(MatriIdPair);
				nameValuePairList.add(GenderPair);

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
			//	progressBar1.setVisibility(View.GONE);

				Log.e("Recent_Listing", "=="+result);
				try
				{
					JSONObject obj = new JSONObject(result);
					ArrayList<String> tokans = new ArrayList<>();
					tokans.clear();
					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						arrUserDataList = new ArrayList<beanUserData>();
						JSONObject responseData = obj.getJSONObject("responseData");

						if (responseData.has("1"))
						{
							Iterator<String> resIter = responseData.keys();

							while (resIter.hasNext())
							{
								String key = resIter.next();
								JSONObject resItem = responseData.getJSONObject(key);

								String user_id=resItem.getString("user_id");
								String matri_id1=resItem.getString("matri_id");
								String username=resItem.getString("username");
								String birthdate=resItem.getString("birthdate");
								String ocp_name=resItem.getString("ocp_name");
								String height=resItem.getString("height");
								String Address=resItem.getString("profile_text");
								String city_name=resItem.getString("city_name");
								String country_name=resItem.getString("country_name");
								String photo1_approve=resItem.getString("photo1_approve");
								String photo_view_status=resItem.getString("photo_view_status");
								String photo_protect=resItem.getString("photo_protect");
								String photo_pswd=resItem.getString("photo_pswd");
								String gender1=resItem.getString("gender");
								String is_shortlisted=resItem.getString("is_shortlisted");
								String is_blocked=resItem.getString("is_blocked");
								String is_favourite=resItem.getString("is_favourite");
								String user_profile_picture=resItem.getString("user_profile_picture");
								tokans.add(resItem.getString("tokan"));
								arrUserDataList.add(new beanUserData(user_id,matri_id1,username,birthdate,ocp_name,height,Address,
										city_name,country_name,photo1_approve,photo_view_status,photo_protect,
										photo_pswd,gender1,is_shortlisted,is_blocked,is_favourite,user_profile_picture));
							}

							if(arrUserDataList.size() > 0)
							{
								recyclerUser.setVisibility(View.VISIBLE);
								textEmptyView.setVisibility(View.GONE);

								adapterUserData = new UserRecentDataAdapter(activity,arrUserDataList, recyclerUser,tokans);
								recyclerUser.setAdapter(adapterUserData);
							}else
							{
								recyclerUser.setVisibility(View.GONE);
								textEmptyView.setVisibility(View.VISIBLE);
							}
						}
					}else
					{
						String msgError=obj.getString("message");
						refresh.setRefreshing(false);
						/*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.dismiss();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();*/
					}
					refresh.setRefreshing(false);

				} catch (Exception t)
				{
					refresh.setRefreshing(false);
					//progressBar1.setVisibility(View.GONE);
				}

				refresh.setRefreshing(false);
				//progressBar1.setVisibility(View.GONE);
			}
		}
		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(MatriId,Gender);
	}

}
