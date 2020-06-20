package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thegreentech.ComposeMessage;
import com.thegreentech.R;

import org.json.JSONObject;

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


public class FragmentMessages extends Fragment
{
	public static TabLayout tabLayout;
	public static TabLayout.Tab tab;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private boolean isViewShown = false;
	SharedPreferences prefUpdate;
	FloatingActionButton fab;

	String TAG="FragmentInbox";
	String matri_id="";

	ProgressDialog progresDialog;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";

	public FragmentMessages() {
	}

	public static FragmentMessages newInstance(int sectionNumber)
	{
		FragmentMessages fragment = new FragmentMessages();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View inflatedView=inflater.inflate(R.layout.fragment_messages, container,false);

		prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
		matri_id=prefUpdate.getString("matri_id","");

		mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(3);

		tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
		fab = inflatedView.findViewById(R.id.fab);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getMessagesRequestStatus(matri_id);
			}
		});

		if (!isViewShown)
		{
			setData();
		}

		int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_MESSAGE,0);
		if(selectedTabPos > 0)
		{
			tab = tabLayout.getTabAt(selectedTabPos);
			tab.select();
		}else
		{
			tab = tabLayout.getTabAt(0);
			tab.select();
		}

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}
			@Override
			public void onPageSelected(int position)
			{
				SharedPreferences.Editor editor=prefUpdate.edit();
				editor.putInt(AppConstants.SELECTED_TAB_MESSAGE,position);
				editor.commit();
				Log.i("Selected_pos_Message",position+"");
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});


		return inflatedView;

	}

	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("call onResume","Message");

	}

	private void setData()
	{
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabLayout.setupWithViewPager(mViewPager);
	}


	@Override
	public void setUserVisibleHint(final boolean isVisibleToUser)
	{
		super.setUserVisibleHint(isVisibleToUser);

		if (getView() != null)
		{
			isViewShown = true;
			setData();

		} else
		{
			isViewShown = false;
		}
	}


	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position)
			{
				case 0:
					return FragmentMessagesInbox.newInstance(position+1);
				case 1:
					return FragmentMessagesSent.newInstance(position+1);
				case 2:
					return FragmentMessagesImportant.newInstance(position+1);

				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position) {
				case 0:
					return "INBOX";
				case 1:
					return "SENT";
				case 2:
					return "IMPORTANT";

			}
			return null;
		}
	}



	private void getMessagesRequestStatus(String MatriId)
	{
		progresDialog= new ProgressDialog(getActivity());
		progresDialog.setCancelable(false);
		progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramsMatriId = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"compose_message.php";
				Log.e("compose_message", "== "+URL);
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
				progresDialog.dismiss();


				Log.e("compose_message.php", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						Intent intentComposeMessage = new Intent(activity, ComposeMessage.class);
						startActivity(intentComposeMessage);
					}else
					{
						String msgError=obj.getString("message");
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.dismiss();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}



				} catch (Exception t)
				{
					Log.e("compose",t.getMessage());
				}


			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(MatriId);
	}

}
