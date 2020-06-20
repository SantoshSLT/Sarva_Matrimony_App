package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utills.AppConstants;
import utills.NetworkConnection;


public class FragmentContactDetails extends Fragment
{
	String TAG="FragmentContactDetails";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";


	TextView tvTitle,textContains;
	ProgressDialog progresDialog;

	public FragmentContactDetails()
	{
	}

	public static FragmentContactDetails newInstance(int sectionNumber)
	{
		FragmentContactDetails fragment = new FragmentContactDetails();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_contact_details, container,false);

		tvTitle=(TextView)rootView.findViewById(R.id.tvTitle);
		textContains=(TextView)rootView.findViewById(R.id.textContains);

		if (NetworkConnection.hasConnection(getActivity())){

			setStaticDataRequest();

		}else
		{
			AppConstants.CheckConnection(getActivity());
		}
		return rootView;
	}

	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;
	}



	private void setStaticDataRequest()
	{
		progresDialog=new ProgressDialog(activity);
		progresDialog.setCancelable(false);
		progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				//String paramsMatriId = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"contact_us.php";
				Log.e("contact_details", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				//BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

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
			protected void onPostExecute(String result)
			{
				super.onPostExecute(result);

				Log.e("contact_details", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						JSONObject responseData = obj.getJSONObject("responseData");
						String page_name=responseData.getString("cms_title");
						String cms_content=responseData.getString("cms_content");

						tvTitle.setText(""+page_name);
						//textCMSContaints.setText(""+cms_content);
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
						{
							textContains.setText(Html.fromHtml(cms_content,Html.FROM_HTML_MODE_LEGACY));
						} else {
							textContains.setText(Html.fromHtml(cms_content));
						}

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


					progresDialog.dismiss();
				} catch (Exception t)
				{
					progresDialog.dismiss();
				}
				progresDialog.dismiss();

			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute();
	}




}
