package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import utills.NetworkConnection;


public class FragmentSettingChangePassword extends Fragment
{
	String TAG="FragmentSearch";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";


	EditText edtOldPassword,edtNewPassword,edtConfirmPassword;
	Button btnSubmit;

	SharedPreferences prefUpdate;
	ProgressDialog progresDialog;
	String matri_id="";


	public FragmentSettingChangePassword()
	{
	}

	public static FragmentSettingChangePassword newInstance(int sectionNumber)
	{
		FragmentSettingChangePassword fragment = new FragmentSettingChangePassword();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_setting_change_password, container,false);

		prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
		matri_id=prefUpdate.getString("matri_id","");

		edtOldPassword=(EditText)rootView.findViewById(R.id.edtOldPassword);
		edtNewPassword=(EditText)rootView.findViewById(R.id.edtNewPassword);
		edtConfirmPassword=(EditText)rootView.findViewById(R.id.edtConfirmPassword);

		btnSubmit=(Button)rootView.findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String oldPassword=edtOldPassword.getText().toString().trim();
				String newPassword=edtNewPassword.getText().toString().trim();
				String confirmPassword=edtConfirmPassword.getText().toString().trim();

				if(oldPassword.equalsIgnoreCase(""))
				{
					Toast.makeText(activity,"Please enter your old password.",Toast.LENGTH_LONG).show();

				}else if(newPassword.equalsIgnoreCase(""))
				{
					Toast.makeText(activity,"Please enter your new password.",Toast.LENGTH_LONG).show();

				}else if(confirmPassword.equalsIgnoreCase(""))
				{
					Toast.makeText(activity,"Please enter your confirm password.",Toast.LENGTH_LONG).show();

				}else if(! newPassword.equalsIgnoreCase(confirmPassword))
				{
					Toast.makeText(activity,"You enterd new password and confirm password not match.",Toast.LENGTH_LONG).show();
				}else
				{
					if (NetworkConnection.hasConnection(getActivity())){
						getChangePasswordRequest(matri_id,oldPassword,newPassword);
					}else
					{
						AppConstants.CheckConnection(getActivity());
					}
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



	private void getChangePasswordRequest(String strMatriId,String oldPassword,String newPassword)
	{
		progresDialog= new ProgressDialog(activity);
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
				String paramsoldPassword = params[1];
				String paramsNewPassword = params[2];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"change_password.php";
				Log.e("change_password", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
				BasicNameValuePair oldPasswordPair = new BasicNameValuePair("old_password", paramsoldPassword);
				BasicNameValuePair newPasswordPair = new BasicNameValuePair("new_password", paramsNewPassword);


				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(MatriIdPair);
				nameValuePairList.add(oldPasswordPair);
				nameValuePairList.add(newPasswordPair);


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

				Log.e("change_password", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");
					String msgError=obj.getString("message");
					if (status.equalsIgnoreCase("1"))
					{
						//JSONObject responseData = obj.getJSONObject("responseData");
						Toast.makeText(activity, ""+msgError, Toast.LENGTH_SHORT).show();
						edtOldPassword.clearFocus();
						edtNewPassword.clearFocus();
						edtConfirmPassword.clearFocus();

						edtOldPassword.setText("");
						edtNewPassword.setText("");
						edtConfirmPassword.setText("");
					}else
					{
						progresDialog.dismiss();
						Toast.makeText(activity, ""+msgError, Toast.LENGTH_SHORT).show();
					}


					progresDialog.dismiss();
				} catch (Exception t)
				{
					Log.e("menupass",t.getMessage());
					progresDialog.dismiss();
				}
				progresDialog.dismiss();

			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(strMatriId,oldPassword,newPassword);
	}



}
