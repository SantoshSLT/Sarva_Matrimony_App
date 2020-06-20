package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.MemberViewProfile;
import com.thegreentech.R;
import com.thegreentech.ViewProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import utills.OnLoadMoreListener;

public class UserFeaturedDataAdapter extends RecyclerView.Adapter<UserFeaturedDataAdapter.UserViewHolder>
{
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;

	private List<beanUserData> arrUserList;

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 20;
	private int lastVisibleItem, totalItemCount;
	private boolean loading;
	private OnLoadMoreListener onLoadMoreListener;
	Context context;


	SharedPreferences prefUpdate;
	ProgressDialog progresDialog;
	String matri_id="";
	ArrayList<String> tokans;
	String RequestType;


	public UserFeaturedDataAdapter(Context context, List<beanUserData> Users, RecyclerView recyclerView,ArrayList<String> tokans)
	{
		this.context=context;
		arrUserList = Users;
		this.tokans = tokans;
		prefUpdate= PreferenceManager.getDefaultSharedPreferences(context);
		matri_id=prefUpdate.getString("matri_id","");

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
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position)
	{
		return arrUserList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
	}

	@Override
	public UserFeaturedDataAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		UserFeaturedDataAdapter.UserViewHolder vh;
		if (viewType == VIEW_ITEM)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recents, parent, false);
			vh = new UserViewHolder(v);
		} else
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
			vh = new ProgressViewHolder(v);
		}
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
		if (holder instanceof UserViewHolder)
		{
			final beanUserData singleUser= (beanUserData) arrUserList.get(position);
			holder.tvUserName.setText(singleUser.getUsername());
			holder.tvUserCode.setText(singleUser.getMatri_id());
			holder.tvAddress.setText(singleUser.getAddress().trim());

			Log.e("imageURL",singleUser.getUser_profile_picture());

			if(!singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
				Picasso.with(context)
						.load(singleUser.getUser_profile_picture())

						.placeholder(R.drawable.loading1)
						.error(R.drawable.male)
						.into(holder.imgProfilePicture);
				/*Glide.with(context)
						.load(singleUser.getUser_profile_picture())
						.apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
						.into(holder.imgProfilePicture);*/
			}


			String is_interest=singleUser.getIs_favourite().toString().trim();

			if(is_interest.equalsIgnoreCase("1"))
			{
				RequestType="Send Reminder";
				holder.ivInterest.setImageResource(R.drawable.ic_reminder);
				holder.tvInterest.setText("Send Reminder");

				//holder.btnSendInterest.setVisibility(View.GONE);
				//holder.btnRemaind.setVisibility(View.VISIBLE);
			}else
			{
				RequestType="Send Intrest";
				holder.ivInterest.setImageResource(R.drawable.ic_heart);
				holder.tvInterest.setText("Send Intrest");
			}


			holder.llInterest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (RequestType.equalsIgnoreCase("Send Intrest"))
					{
						String test = singleUser.getIs_blocked().toString();
						Log.d("TAG","CHECK ="+test);

						if (singleUser.getIs_blocked().equalsIgnoreCase("1"))
						{
							String msgBlock = "This member has blocked you. You can't express your interest.";
							String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";

							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setMessage(msgBlock).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
									dialog.dismiss();
								}
							});
							AlertDialog alert = builder.create();
							alert.show();
						} else {
							AppConstants.sendPushNotification(tokans.get(position),
									AppConstants.msg_express_intress+" "+singleUser.getMatri_id(),
									AppConstants.msg_express_intress_title,AppConstants.express_intress_id);
							if (NetworkConnection.hasConnection(context)){
								sendInterestRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position,holder);
							}else
							{
								AppConstants.CheckConnection((Activity) context);
							}
						}
					}
					else if (RequestType.equalsIgnoreCase("Send Reminder"))
					{
						if (singleUser.getIs_blocked().equalsIgnoreCase("1"))
						{
							String msgBlock = "This member has blocked you. You can't express your interest.";
							String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";

							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setMessage(msgBlock).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int id)
								{
									dialog.dismiss();
								}
							});
							AlertDialog alert = builder.create();
							alert.show();
						} else {
							if (NetworkConnection.hasConnection(context)){
								sendInterestRequestRemind(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position);
							}else
							{
								AppConstants.CheckConnection((Activity) context);
							}

						}
					}
				}
			});



			holder.cardView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					MemberViewProfile.matri_id=singleUser.getMatri_id();
					MemberViewProfile.is_shortlist=singleUser.getIs_shortlisted();
					context.startActivity(new Intent(context, MemberViewProfile.class));
				}
			});


			holder.User= singleUser;

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
		return arrUserList.size();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.onLoadMoreListener = onLoadMoreListener;
	}


	//
	public static class UserViewHolder extends RecyclerView.ViewHolder
	{
		public TextView tvUserName,tvUserCode,tvAddress;
		public Button btnSendInterest,btnRemaind;
		LinearLayout llInterest;
		//	public ImageButton btnShortlist,btnBlock;
		public ImageView imgProfilePicture;
		public ImageView ivInterest;
		public beanUserData User;
		public CardView cardView;
		public TextView /*tvShortlist, tvBlock,*/tvInterest;

		public UserViewHolder(View v) {
			super(v);
			llInterest = v.findViewById(R.id.llInterest);
			ivInterest = v.findViewById(R.id.ivInterest);
			tvInterest = v.findViewById(R.id.tvInterest);
		//	btnRemaind = (Button) v.findViewById(R.id.btnRemaind);
			//	tvBlock = (TextView) v.findViewById(R.id.tvBlock);
			//	tvShortlist = (TextView) v.findViewById(R.id.tvShortlist);
			cardView = (CardView) v.findViewById(R.id.cardView);
			tvUserName = (TextView) v.findViewById(R.id.tvUserName);
			tvUserCode = (TextView) v.findViewById(R.id.tvUserCode);
			tvAddress = (TextView) v.findViewById(R.id.tvAddress);

			//btnShortlist= (ImageButton) v.findViewById(R.id.btnShortlist);
			//btnBlock= (ImageButton) v.findViewById(R.id.btnBlock);
		//	btnSendInterest = (Button) v.findViewById(R.id.btnSendInterest);

			imgProfilePicture = v.findViewById(R.id.imgProfilePicture);
		}
	}

	public static class ProgressViewHolder extends  UserFeaturedDataAdapter.UserViewHolder
	{
		public ProgressBar progressBar;
		public ProgressViewHolder(View v)
		{
			super(v);
			progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
		}
	}



	private void sendInterestRequest(String login_matri_id, String strMatriId, final String isFavorite, final int pos, final UserViewHolder holder)
	{
		progresDialog= new ProgressDialog(context);
		progresDialog.setCancelable(false);
		progresDialog.setMessage(context.getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramsLoginMatriId = params[0];
				String paramsUserMatriId = params[1];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"send_intrest.php";
				Log.e("send_intrest", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
				BasicNameValuePair UserMatriIdPair  = new BasicNameValuePair("receiver_id", paramsUserMatriId);


				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(LoginMatriIdPair);
				nameValuePairList.add(UserMatriIdPair);

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

				Log.e("send_intrest", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						holder.ivInterest.setImageResource(R.drawable.ic_reminder);
						holder.tvInterest.setText("Send Reminder");
						String message=obj.getString("message").toString().trim();
						Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();

						if(isFavorite.equalsIgnoreCase("1")) {
							//arrUserList.get(pos).setIs_favourite("0");
						}else
						{
							arrUserList.get(pos).setIs_favourite("1");
						}

						refreshAt(pos);

					}else
					{
						String msgError=obj.getString("message");
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
					Log.e("lfgklnm",t.getMessage());
					progresDialog.dismiss();
				}
				progresDialog.dismiss();


			}
		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(login_matri_id,strMatriId);
	}


	private void sendInterestRequestRemind(String login_matri_id, String strMatriId, final String isFavorite, final int pos)
	{
		progresDialog= new ProgressDialog(context);
		progresDialog.setCancelable(false);
		progresDialog.setMessage(context.getResources().getString(R.string.Please_Wait));
		progresDialog.setIndeterminate(true);
		progresDialog.show();

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params)
			{
				String paramsLoginMatriId = params[0];
				String paramsUserMatriId = params[1];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"send_intrest.php";
				Log.e("send_intrest", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
				BasicNameValuePair UserMatriIdPair  = new BasicNameValuePair("receiver_id", paramsUserMatriId);


				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(LoginMatriIdPair);
				nameValuePairList.add(UserMatriIdPair);

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

				Log.e("send_intrest", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{

						String message=obj.getString("message").toString().trim();
						Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();

						if(isFavorite.equalsIgnoreCase("1")) {
							//arrUserList.get(pos).setIs_favourite("0");
						}else
						{
							arrUserList.get(pos).setIs_favourite("1");
						}

						refreshAt(pos);

					}else
					{
						String msgError=obj.getString("message");
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
		sendPostReqAsyncTask.execute(login_matri_id,strMatriId);
	}



	public void refreshAt(int position)
	{
		notifyItemChanged(position);
		notifyItemRangeChanged(position, arrUserList.size());
	}


}