package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import Adepters.BlockedUserAdapter;
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


public class FragmentSettingBlockMembers extends Fragment
{
	String TAG="FragmentSearch";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";


	EditText edtEmailId;
    Button btnBlock;
    RecyclerView recyclerBlockedUser;
    TextView textEmptyView;

    protected Handler handler;
    private BlockedUserAdapter adapterBlocked;
    private ArrayList<beanUserData> arrBlockedUserList;

    private ArrayList<beanUserData> arrShortListedUser;

    ProgressBar progressBar1;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    String matri_id="";

	public FragmentSettingBlockMembers()
	{
	}

	public static FragmentSettingBlockMembers newInstance(int sectionNumber)
	{
		FragmentSettingBlockMembers fragment = new FragmentSettingBlockMembers();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_setting_block_memeber, container,false);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id=prefUpdate.getString("matri_id","");


        edtEmailId=(EditText)rootView.findViewById(R.id.edtEmailId);
        btnBlock=(Button)rootView.findViewById(R.id.btnBlock);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1 );
        recyclerBlockedUser=(RecyclerView)rootView.findViewById(R.id.recyclerBlockedUser);
        textEmptyView = (TextView) rootView.findViewById(R.id.textEmptyView);

        recyclerBlockedUser.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerBlockedUser.setHasFixedSize(true);

        arrBlockedUserList = new ArrayList<beanUserData>();
        handler = new Handler();

        arrShortListedUser = new ArrayList<beanUserData>();

        btnBlock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String blockMatriId= edtEmailId.getText().toString();
                if(blockMatriId.equalsIgnoreCase(""))
                {
                    Toast.makeText(activity,"Please enter matri id.",Toast.LENGTH_LONG).show();
                }else
                {
                    if (NetworkConnection.hasConnection(getActivity())){
                        setPhotoPrivacyRequest(matri_id,blockMatriId);
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }

            }
        });
        if (NetworkConnection.hasConnection(getActivity())){
            getBlockListedUserList(matri_id);
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


	//Block user
    private void setPhotoPrivacyRequest(String strMatriId,String blockMatriId)
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
                String paramsblockMatriId = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"block_user.php";
                Log.e("block_user", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);
                BasicNameValuePair BlockMatriIdPair = new BasicNameValuePair("block_matri_id", paramsblockMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(BlockMatriIdPair);


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

                Log.e("block User", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
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

                    }else
                    {
                        progresDialog.dismiss();

                    }
                    progresDialog.dismiss();
                } catch (Exception t)
                {
                    Log.e("menucontact",t.getMessage());
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId,blockMatriId);
    }

    //Show block user list
    private void getBlockListedUserList(String strMatriId)
    {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL="";

                URL= AppConstants.MAIN_URL +"blocklist.php";
                Log.e("blocklist", "== "+URL);

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

                Log.e("shortlisted", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        arrShortListedUser= new ArrayList<beanUserData>();
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
                                //String Address=ocp_name;
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

                                arrShortListedUser.add(new beanUserData(user_id,matri_id1,username,birthdate,ocp_name,height,Address,city_name,country_name,photo1_approve,photo_view_status,photo_protect,
                                        photo_pswd,gender1,is_shortlisted,is_blocked,is_favourite,user_profile_picture));

                            }

                            if(arrShortListedUser.size() > 0)
                            {
                                recyclerBlockedUser.setVisibility(View.VISIBLE);
                                textEmptyView.setVisibility(View.GONE);

                                BlockedUserAdapter adapterBlockedUser = new BlockedUserAdapter(activity,arrShortListedUser, recyclerBlockedUser);
                                recyclerBlockedUser.setAdapter(adapterBlockedUser);

                            }else
                            {
                                recyclerBlockedUser.setVisibility(View.GONE);
                                textEmptyView.setVisibility(View.VISIBLE);
                            }
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
                    progressBar1.setVisibility(View.GONE);
                } catch (Throwable t)
                {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }
}
