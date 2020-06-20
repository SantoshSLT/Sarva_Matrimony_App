package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.R;
import com.thegreentech.SearchResultActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.beanSaveSearch;
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
import utills.OnLoadMoreListener;

import static com.thegreentech.SignUpStep1Activity.Gender;

public class SavedSearchResultAdapter extends RecyclerView.Adapter
{
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;

	private List<beanSaveSearch> arrUserList;

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 20;
	private int lastVisibleItem, totalItemCount;
	private boolean loading;
	private OnLoadMoreListener onLoadMoreListener;

	Activity activity;
	ProgressDialog progresDialog;
	SharedPreferences prefUpdate;
	String matri_id="";
	String Genderr ="";

	public SavedSearchResultAdapter(Activity activity, List<beanSaveSearch> Users, RecyclerView recyclerView)
	{
		this.activity=activity;
		arrUserList = Users;
		prefUpdate= PreferenceManager.getDefaultSharedPreferences(activity);
		matri_id=prefUpdate.getString("matri_id","");
		Genderr= prefUpdate.getString("gender","");

		if (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
		{
			final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
			{
				@Override
				public void onScrolled(RecyclerView recyclerView,
									   int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);

					totalItemCount = linearLayoutManager.getItemCount();
					lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
					if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold))
					{
						// End has been reached
						// Do something
						if (onLoadMoreListener != null)
						{
							onLoadMoreListener.onLoadMore();
						}
						loading = true;
					}
				}
			});
		}
	}

	@Override
	public int getItemViewType(int position)
	{
		return arrUserList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		RecyclerView.ViewHolder vh;
		if (viewType == VIEW_ITEM)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_search_result, parent, false);
			vh = new UserViewHolder(v);
		} else
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
			vh = new ProgressViewHolder(v);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
	{
		if (holder instanceof UserViewHolder)
		{
			final beanSaveSearch singleUser= (beanSaveSearch) arrUserList.get(position);
			((UserViewHolder) holder).tvSearchName.setText(singleUser.getSearchName());

			if(!singleUser.getSearchName().equalsIgnoreCase("") &&  singleUser.getSearchName() !=null)
			{
				((UserViewHolder) holder).tvSearchName.setText(singleUser.getSearchName());

			} else {
				//((UserViewHolder) holder).imgProfilePicture.setImageResource(R.drawable.ic_profile1);
			}
			if(!singleUser.getSaveDate().equalsIgnoreCase("") && singleUser.getSaveDate() != null)
			{
				String date = singleUser.getSaveDate();
				String RealDate = parseDateToddMMyyyy(date);
				((UserViewHolder) holder).tvSearchDate.setText(RealDate);

			} else {

			}
			((UserViewHolder) holder).btnsearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent newIntent= new Intent(activity,SearchResultActivity.class);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					newIntent.putExtra("SearchType","bydata");
					newIntent.putExtra("Gender",Genderr);
					newIntent.putExtra("AgeM",""+singleUser.getAgeM());
					newIntent.putExtra("AgeF",""+singleUser.getAgeF());
					newIntent.putExtra("HeightM",""+singleUser.getHeightM());
					newIntent.putExtra("HeightF",""+singleUser.getHeightF());
					newIntent.putExtra("MaritalStatus",""+singleUser.getMartialStatus());
					newIntent.putExtra("PhysicalStatus","");
					newIntent.putExtra("ReligionId",""+singleUser.getReligion());
					newIntent.putExtra("CasteId",""+singleUser.getCaste());
					newIntent.putExtra("CountryId",""+singleUser.getCountry());
					newIntent.putExtra("StateId",""+singleUser.getState());
					newIntent.putExtra("CityId",""+singleUser.getCity());
					newIntent.putExtra("HighestEducationId",""+singleUser.getEducation());
					newIntent.putExtra("OccupationId",""+singleUser.getOccupation());
					newIntent.putExtra("AnnualIncome",""+singleUser.getAnnualIncome());
					newIntent.putExtra("Manglik",""+singleUser.getManglik());
					newIntent.putExtra("Star",""+singleUser.getStar());
					activity.startActivity(newIntent);
					activity.finish();
				}
			});

			((SavedSearchResultAdapter.UserViewHolder) holder).btnDelete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					new AlertDialog.Builder(activity)
							.setMessage("Do you want to Delete it?")
							.setPositiveButton("Delete", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int whichButton)
								{

									if (NetworkConnection.hasConnection(activity)){
										getDeleteRequest(singleUser.getId(),position);
									}else
									{
										AppConstants.CheckConnection((Activity) activity);
									}

								}})
							.setNegativeButton("Cancel", null).show();
				}
			});


			((SavedSearchResultAdapter.UserViewHolder) holder).cardView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent newIntent= new Intent(activity,SearchResultActivity.class);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					newIntent.putExtra("SearchType","bydata");
					newIntent.putExtra("Gender",Genderr);
					newIntent.putExtra("AgeM",""+singleUser.getAgeM());
					newIntent.putExtra("AgeF",""+singleUser.getAgeF());
					newIntent.putExtra("HeightM",""+singleUser.getHeightM());
					newIntent.putExtra("HeightF",""+singleUser.getHeightF());
					newIntent.putExtra("MaritalStatus",""+singleUser.getMartialStatus());
					newIntent.putExtra("PhysicalStatus","");
					newIntent.putExtra("ReligionId",""+singleUser.getReligion());
					newIntent.putExtra("CasteId",""+singleUser.getCaste());
					newIntent.putExtra("CountryId",""+singleUser.getCountry());
					newIntent.putExtra("StateId",""+singleUser.getState());
					newIntent.putExtra("CityId",""+singleUser.getCity());
					newIntent.putExtra("HighestEducationId",""+singleUser.getEducation());
					newIntent.putExtra("OccupationId",""+singleUser.getOccupation());
					newIntent.putExtra("AnnualIncome",""+singleUser.getAnnualIncome());
					newIntent.putExtra("Manglik",""+singleUser.getManglik());
					newIntent.putExtra("Star",""+singleUser.getStar());
					activity.startActivity(newIntent);
					activity.finish();
				}
			});

			((UserViewHolder) holder).User= singleUser;
			
		} else
		{
			((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
		}
	}

	public void setLoaded() {
		loading = false;
	}

	@Override
	public int getItemCount() {
		Log.e("arrlistsize", arrUserList.size() + "");

		if (arrUserList == null && arrUserList.size()>0 )
			return 0;
		else
			return arrUserList.size();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.onLoadMoreListener = onLoadMoreListener;
	}

	public String parseDateToddMMyyyy(String time) {
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "dd MMM yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	//
	public static class UserViewHolder extends RecyclerView.ViewHolder
	{
		public TextView tvSearchName,tvSearchDate;
		public ImageView btnDelete;
		public beanSaveSearch User;
		public CardView cardView;
		public TextView btnsearch;

		public UserViewHolder(View v)
		{
			super(v);

			cardView = v.findViewById(R.id.cardView);
			tvSearchName =  v.findViewById(R.id.tvSearchName);
			btnDelete=  v.findViewById(R.id.btnDelete);
			btnsearch = v.findViewById(R.id.btnsearch);
			tvSearchDate = v.findViewById(R.id.tvSearchDate);
			v.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//Toast.makeText(v.getContext(),"OnClick :" + User.getUser_name() + " \n "+User.getUser_code(),
					//Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public static class ProgressViewHolder extends RecyclerView.ViewHolder
	{
		public ProgressBar progressBar;
		public ProgressViewHolder(View v)
		{
			super(v);
			progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
		}
	}




	private void getDeleteRequest(String strSSId,final int pos)
	{
		progresDialog= new ProgressDialog(activity);
		progresDialog.setCancelable(false);
		progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramsSSID = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"delete_saved_search.php";
				Log.e("delete_inbox", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair SSIdPair = new BasicNameValuePair("ss_id", paramsSSID);


				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(SSIdPair);

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

				Log.e("delete Saved search", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						String message=obj.getString("message").toString().trim();
						Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
						removeAt(pos);
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
				} catch (Throwable t)
				{
					progresDialog.dismiss();
				}
				progresDialog.dismiss();

			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(strSSId,String.valueOf(pos));
	}


	public void removeAt(int position)
	{
		arrUserList.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, arrUserList.size());
	}



	public void refreshAt(int position)
	{
		notifyItemChanged(position);
		notifyItemRangeChanged(position, arrUserList.size());
	}

}