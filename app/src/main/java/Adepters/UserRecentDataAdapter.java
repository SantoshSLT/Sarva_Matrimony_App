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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.MemberViewProfile;
import com.thegreentech.PorterShape.PorterShapeImageView;
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
import utills.OnLoadMoreListener;

public class UserRecentDataAdapter extends RecyclerView.Adapter<UserRecentDataAdapter.UserViewHolder>
{
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;

	private List<beanUserData> arrUserList;

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


	public UserRecentDataAdapter(Context context, List<beanUserData> Users, RecyclerView recyclerView,ArrayList<String> tokans)
	{
		this.context =context;
		this.tokans = tokans;
		arrUserList = Users;
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
	public UserRecentDataAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		UserRecentDataAdapter.UserViewHolder vh;
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

			Glide.with(context)
					.load(singleUser.getUser_profile_picture())
					.apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
					.into(holder.imgProfilePicture);


//			if(!singleUser.getUser_profile_picture().equalsIgnoreCase(""))
//			{
//				Picasso.with(context)
//						.load(singleUser.getUser_profile_picture())
//
//						.placeholder(R.drawable.loading1)
//						.error(R.drawable.male)
//						.into(holder.imgProfilePicture);
//				/*Glide.with(context)
//						.load(singleUser.getUser_profile_picture())
//						.apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
//						.into(holder.imgProfilePicture);*/
//			}


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
							sendInterestRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position,holder);
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
							AppConstants.sendPushNotification(tokans.get(position),
									AppConstants.msg_express_intress+" "+singleUser.getMatri_id(),
									AppConstants.msg_send_reminder_title,AppConstants.express_intress_id);
							sendInterestRequestRemind(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position);
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

		public UserViewHolder(View v)
		{
			super(v);
			llInterest = v.findViewById(R.id.llInterest);
			ivInterest = v.findViewById(R.id.ivInterest);
			tvInterest = v.findViewById(R.id.tvInterest);
			//btnRemaind = (Button) v.findViewById(R.id.btnRemaind);
		//	tvBlock = (TextView) v.findViewById(R.id.tvBlock);
		//	tvShortlist = (TextView) v.findViewById(R.id.tvShortlist);
			cardView = (CardView) v.findViewById(R.id.cardView);
			tvUserName = (TextView) v.findViewById(R.id.tvUserName);
			tvUserCode = (TextView) v.findViewById(R.id.tvUserCode);
			tvAddress= (TextView) v.findViewById(R.id.tvAddress);

			//btnShortlist= (ImageButton) v.findViewById(R.id.btnShortlist);
			//btnBlock= (ImageButton) v.findViewById(R.id.btnBlock);
		//	btnSendInterest= (Button) v.findViewById(R.id.btnSendInterest);

			imgProfilePicture= v.findViewById(R.id.imgProfilePicture);

		}
	}

	public static class ProgressViewHolder extends UserRecentDataAdapter.UserViewHolder
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
					Log.e("fjglfjl",t.getMessage());
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





/*
	@Override
	public void onBindViewHolder(final RecyclerView.UserViewHolder holder, final int position)
	{
		if (holder instanceof UserViewHolder)
		{
			final beanUserData singleUser= (beanUserData) arrUserList.get(position);
			((UserViewHolder) holder).tvUserName.setText(singleUser.getUsername());
			((UserViewHolder) holder).tvUserCode.setText(singleUser.getMatri_id());
			((UserViewHolder) holder).tvAddress.setText(singleUser.getAddress().trim());

			Log.e("imageURL",singleUser.getUser_profile_picture());



			if(!singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
				Picasso.with(context)
						.load(singleUser.getUser_profile_picture())

						.placeholder(R.drawable.loading1)
						.error(R.drawable.male)
						.into(((UserViewHolder) holder).imgProfilePicture);
			}

			*/
/*if(! singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
*//*
 */
/*
				Picasso.with(context)
						.load(singleUser.getUser_profile_picture())
						.fit()
						.placeholder(R.drawable.loading1)
						.error(R.drawable.male)
						.into(((UserViewHolder) holder).imgProfilePicture);
*//*
 */
/*
				Glide.with(context)
						.load(singleUser.getUser_profile_picture())
						.apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_my_profile))
						.into(((UserViewHolder) holder).imgProfilePicture);



			} else {
				((UserViewHolder) holder).imgProfilePicture.setImageResource(R.drawable.ic_profile1);
			}*//*



			String is_shortlisted=singleUser.getIs_shortlisted().toString().trim();
			String is_blocked=singleUser.getIs_blocked().toString().trim();
			String is_interest=singleUser.getIs_favourite().toString().trim();


			*/
/*if(is_shortlisted.equalsIgnoreCase("1"))
			{
				((UserViewHolder) holder).btnShortlist.setBackgroundResource(R.drawable.shortlisted_active);
				((UserViewHolder) holder).tvShortlist.setText("Shortlisted");
			}else
			{
				((UserViewHolder) holder).btnShortlist.setBackgroundResource(R.drawable.shortlisted);
				((UserViewHolder) holder).tvShortlist.setText("Shortlist");
			}*//*


 */
/*if(is_blocked.equalsIgnoreCase("1"))
			{
				((UserViewHolder) holder).btnBlock.setBackgroundResource(R.drawable.blocklisted_active);
				((UserViewHolder) holder).tvBlock.setText("Unblock");
			}else
			{
				((UserViewHolder) holder).btnBlock.setBackgroundResource(R.drawable.blocklisted);
				((UserViewHolder) holder).tvBlock.setText("Block");
			}*//*





 */
/*((UserViewHolder) holder).btnShortlist.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					addToShortListRequest(matri_id,singleUser.getMatri_id(),singleUser.getIs_shortlisted(),position);
				}
			});

			((UserViewHolder) holder).btnBlock.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					addToBlockRequest(matri_id,singleUser.getMatri_id(),singleUser.getIs_blocked(),position);
				}
			});*//*


			if(is_interest.equalsIgnoreCase("1"))
			{
				RequestType="Send Reminder";
				((UserViewHolder) holder).ivInterest.setImageResource(R.drawable.ic_reminder);
				((UserViewHolder) holder).tvInterest.setText("Send Reminder");

				//((UserViewHolder) holder).btnSendInterest.setVisibility(View.GONE);
				//((UserViewHolder) holder).btnRemaind.setVisibility(View.VISIBLE);
			}else
			{
				RequestType="Send Request";
				((UserViewHolder) holder).ivInterest.setImageResource(R.drawable.ic_heart);
				((UserViewHolder) holder).tvInterest.setText("Send Request");
			}


			((UserViewHolder) holder).llInterest.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (RequestType.equalsIgnoreCase("Send Request"))
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
							sendInterestRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position);
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

							sendInterestRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position);
						}
					}
				}
			});



			((UserViewHolder) holder).cardView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					MemberViewProfile.matri_id=singleUser.getMatri_id();
					MemberViewProfile.is_shortlist=singleUser.getIs_shortlisted();
					context.startActivity(new Intent(context, MemberViewProfile.class));
				}
			});


			((UserViewHolder) holder).User= singleUser;

		} else
		{
			((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
		}
	}
*/