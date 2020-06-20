package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import utills.AppMethods;
import utills.NetworkConnection;


public class FragmentContactUs extends Fragment
{
	String TAG="FragmentContactUs";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";


	EditText edtFullname,edtEmailId,edtContactNo,edtSubject,edtDescription;
	Button btnSubmit;

	ProgressDialog progresDialog;

	public FragmentContactUs()
	{

	}

	public static FragmentContactUs newInstance(int sectionNumber)
	{
		FragmentContactUs fragment = new FragmentContactUs();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_contact_us, container,false);


		edtFullname=(EditText)rootView.findViewById(R.id.edtFullname);
		edtEmailId=(EditText)rootView.findViewById(R.id.edtEmailId);
		edtContactNo=(EditText)rootView.findViewById(R.id.edtContactNo);
		edtSubject=(EditText)rootView.findViewById(R.id.edtSubject);
		edtDescription=(EditText)rootView.findViewById(R.id.edtDescription);
		btnSubmit=(Button)rootView.findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String strFirstname=edtFullname.getText().toString().trim();
				String strEmailId=edtEmailId.getText().toString().trim();
				String strContactNo=edtContactNo.getText().toString().trim();
				String strSubject=edtSubject.getText().toString().trim();
				String strDescription=edtDescription.getText().toString().trim();

				if(hasData(strFirstname) && hasData(strEmailId) && hasData(strContactNo)&&
						hasData(strSubject)&& hasData(strDescription))
				{

					if (NetworkConnection.hasConnection(getActivity())){
						sendContactUsRequest(strFirstname,strEmailId,strContactNo,strSubject,strDescription);
						AppMethods.hideKeyboard(view);
					}else
					{
						AppConstants.CheckConnection(getActivity());
					}
				}else
				{
					Toast.makeText(activity,"Please fill all field.",Toast.LENGTH_LONG).show();
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



	private void sendContactUsRequest(String strFirstname,String strEmailId,String strContactNo,
									  String strSubject,String strDescription)
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
				String paramFirstname = params[0];
				String paramEmailId = params[1];
				String paramContactNo = params[2];
				String paramSubject = params[3];
				String paramDescription = params[4];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"add_contact.php";
				Log.e("URL", "== "+URL);
				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair full_namePAir = new BasicNameValuePair("full_name", paramFirstname);
				BasicNameValuePair email_idPAir = new BasicNameValuePair("email_id", paramEmailId);
                BasicNameValuePair contactAir = new BasicNameValuePair("contact", paramContactNo);
                BasicNameValuePair subjectPAir = new BasicNameValuePair("subject", paramSubject);
                BasicNameValuePair messagePAir = new BasicNameValuePair("message", paramDescription);

				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(full_namePAir);
				nameValuePairList.add(email_idPAir);
                nameValuePairList.add(contactAir);
                nameValuePairList.add(subjectPAir);
                nameValuePairList.add(messagePAir);

				try
				{
					UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
					httpPost.setEntity(urlEncodedFormEntity);
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

				} catch (UnsupportedEncodingException uee)
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
				Log.e("--add_contact --", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);
					//JSONObject responseData = obj.getJSONObject("responseData");

					String status=obj.getString("status");

					if(status.equalsIgnoreCase("1"))
					{
						String message=obj.getString("message");

						Toast.makeText(activity,""+message,Toast.LENGTH_LONG).show();

						edtFullname.setText("");
						edtEmailId.setText("");
						edtContactNo.setText("");
						edtSubject.setText("");
						edtDescription.setText("");

					}else
					{
						String message=obj.getString("message");
						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setMessage(""+message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.dismiss();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}

				} catch (Throwable t)
				{

				}

			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(strFirstname,strEmailId,strContactNo,strSubject,strDescription);
	}




	public  boolean hasData(String text)
	{
		if (text == null || text.length() == 0)
			return false;

		return true;
	}


}
