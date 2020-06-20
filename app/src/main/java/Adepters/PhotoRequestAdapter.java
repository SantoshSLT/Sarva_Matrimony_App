package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.Request;
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
import utills.CircleTransform;
import utills.NetworkConnection;
import utills.OnLoadMoreListener;

public class PhotoRequestAdapter extends RecyclerView.Adapter
{
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;
	String Type="";

	private List<beanUserData> arrPhotoPasswordList;

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 20;
	private int lastVisibleItem, totalItemCount;
	private boolean loading;
	private OnLoadMoreListener onLoadMoreListener;

	Activity activity;
	ProgressDialog progresDialog;

	public PhotoRequestAdapter(Activity activity, List<beanUserData> Users, RecyclerView recyclerView,String type)
	{
		this.activity=activity;
		arrPhotoPasswordList = Users;
		this.Type = type;

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
		return arrPhotoPasswordList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		RecyclerView.ViewHolder vh;
		if (viewType == VIEW_ITEM)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_request, parent, false);
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
			final beanUserData singleUser= (beanUserData) arrPhotoPasswordList.get(position);
			((UserViewHolder) holder).tvUserName.setText(singleUser.getUsername());
			((UserViewHolder) holder).tvUserCode.setText(singleUser.getMatri_id());
			((UserViewHolder) holder).tvDateTime.setText(AppConstants.mDateFormateDDMMM(singleUser.getei_reqdate()));
			//((UserViewHolder) holder).tvMessage.setText(singleUser.getAddress().trim());


			if(! singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
				Picasso.with(activity)
						.load(singleUser.getUser_profile_picture())
						.placeholder(R.drawable.ic_my_profile)
						.into(((UserViewHolder) holder).imgProfilePicture);
			} else {
				((UserViewHolder) holder).imgProfilePicture.setImageResource(R.drawable.ic_my_profile);
			}


			((UserViewHolder) holder).btnDelete.setOnClickListener(new OnClickListener()
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
                                        getDeleteRequest(singleUser.getei_reqid(),position);
                                    }else
                                    {
                                        AppConstants.CheckConnection((Activity) activity);
                                    }
								}})
							.setNegativeButton("Cancel", null).show();

				}
			});

            String RequestStatus = singleUser.getReceiver_response();

            if (Type.equalsIgnoreCase("PhotoReqReceived")) {
                ((UserViewHolder) holder).tvRequest.setText("Photo Req Received");

                if (RequestStatus.equalsIgnoreCase("Accepted")) {
                    ((UserViewHolder) holder).tvStatus.setText("Accepted");
                    ((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                    ((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
                    ((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
                    ((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
					((UserViewHolder) holder).llphotoPass.setVisibility(View.GONE);


                } else if (RequestStatus.equalsIgnoreCase("Rejected")) {
                    ((UserViewHolder) holder).tvStatus.setText("Rejected");
                    ((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorRed));
                    ((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
                    ((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
                    ((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
					((UserViewHolder) holder).llphotoPass.setVisibility(View.GONE);

                } else {
                    ((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.VISIBLE);
                    ((UserViewHolder) holder).btnRequestRejected.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).llphotoPass.setVisibility(View.GONE);

					((UserViewHolder) holder).tvStatus.setText(RequestStatus);

                }

            }

            else if (Type.equalsIgnoreCase("PhotoReqSent")) {
                ((UserViewHolder) holder).tvRequest.setText("Photo Req Sent");
				((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
				((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);

                if (RequestStatus.equalsIgnoreCase("Accepted")) {
                    ((UserViewHolder) holder).tvStatus.setText("Accepted");
                    ((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                    ((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
                    ((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
                    ((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);

					((UserViewHolder) holder).llphotoPass.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvphoto.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvPhotoPassword.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvPhotoPassword.setText(singleUser.getPhoto_pswd());


                } else if (RequestStatus.equalsIgnoreCase("Rejected")) {
                    ((UserViewHolder) holder).tvStatus.setText("Rejected");
                    ((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorRed));
                    ((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
                    ((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
                    ((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);

                } else if (RequestStatus.equalsIgnoreCase("Pending")){

					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
                    ((UserViewHolder) holder).tvStatus.setText(RequestStatus);
                }
                else {
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
                    ((UserViewHolder) holder).tvStatus.setText(RequestStatus);
                }

            }


/*
			if(Type.equalsIgnoreCase("PhotoReqReceived")) {// add if
				((UserViewHolder) holder).tvRequest.setText(" Photo Req Received");
				if (RequestStatus.equalsIgnoreCase("Accepted")) {
					((UserViewHolder) holder).tvStatus.setText("" + RequestStatus);
					((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
				} else if (RequestStatus.equalsIgnoreCase("Reject")) {
					((UserViewHolder) holder).tvStatus.setText("" + RequestStatus);
					((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorRed));
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
				} else {
					((UserViewHolder) holder).tvStatus.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.VISIBLE);
				}
			} else {// add else
				((UserViewHolder) holder).tvRequest.setText(" Photo Req Sent");
				if (RequestStatus.equalsIgnoreCase("Accepted")) {
					((UserViewHolder) holder).tvStatus.setText("" + RequestStatus);
					((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorGreen));
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
				} else if (RequestStatus.equalsIgnoreCase("Reject")) {
					((UserViewHolder) holder).tvStatus.setText("" + RequestStatus);
					((UserViewHolder) holder).tvStatus.setVisibility(View.VISIBLE);
					((UserViewHolder) holder).tvStatus.setTextColor(activity.getResources().getColor(R.color.colorRed));
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
				} else {
					((UserViewHolder) holder).tvStatus.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestAccepted.setVisibility(View.GONE);
					((UserViewHolder) holder).btnRequestRejected.setVisibility(View.GONE);
				}

				*/
/*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)((UserViewHolder) holder).btnDelete.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);*//*


				//((UserViewHolder) holder).btnDelete.setGravity(Gravity.RIGHT);
			}
*/


			((UserViewHolder) holder).btnRequestAccepted.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

                    if (NetworkConnection.hasConnection(activity)){
                        addAcceptRejectRequest(singleUser.getei_reqid(),"photo_accept.php",position);
                    }else
                    {
                        AppConstants.CheckConnection((Activity) activity);
                    }



				}
			});

			((UserViewHolder) holder).btnRequestRejected.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

                    if (NetworkConnection.hasConnection(activity)){
                        addAcceptRejectRequest(singleUser.getei_reqid(),"photo_reject.php",position);
                    }else
                    {
                        AppConstants.CheckConnection((Activity) activity);
                    }
				}
			});

			((PhotoRequestAdapter.UserViewHolder) holder).cardView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					MemberViewProfile.matri_id=singleUser.getMatri_id();
					MemberViewProfile.is_shortlist=singleUser.getIs_shortlisted();
					activity.startActivity(new Intent(activity, MemberViewProfile.class));
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
		return arrPhotoPasswordList.size();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.onLoadMoreListener = onLoadMoreListener;
	}


	//
	public static class UserViewHolder extends RecyclerView.ViewHolder
	{
		public TextView tvUserName,tvUserCode,tvDateTime,tvMessage,tvStatus,
				tvRequest,btnRequestAccepted,btnRequestRejected,tvphoto,tvPhotoPassword;
		//public Button btnDelete,btnRequestAccepted,btnRequestRejected;
		public ImageView imgProfilePicture, btnDelete;
		public beanUserData User;
		public CardView cardView;
		public LinearLayout llAccept, llReject, llReminders,llphotoPass;

		public UserViewHolder(View v)
		{
			super(v);
			cardView =  v.findViewById(R.id.cardView);
			tvUserName =  v.findViewById(R.id.tvUserName);
			tvUserCode =  v.findViewById(R.id.tvUserCode);
			tvDateTime=  v.findViewById(R.id.tvDateTime);
			tvMessage= v.findViewById(R.id.tvMessage);
			tvStatus=  v.findViewById(R.id.tvStatus);
			tvRequest=  v.findViewById(R.id.tvRequest);
			llReject = v.findViewById(R.id.llReject);
			llAccept = v.findViewById(R.id.llAccept);
			btnDelete=  v.findViewById(R.id.btnDelete);
			btnRequestAccepted=  v.findViewById(R.id.btnRequestAccepted);
			btnRequestRejected=  v.findViewById(R.id.btnRequestRejected);
			llphotoPass=  v.findViewById(R.id.llphotoPass);
			tvphoto=  v.findViewById(R.id.tvphoto);
			tvPhotoPassword=  v.findViewById(R.id.tvPhotoPassword);

			imgProfilePicture= (ImageView) v.findViewById(R.id.imgProfilePicture);



			/*btnRequestAccepted.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Toast.makeText(v.getContext(),"Comming soon",Toast.LENGTH_LONG).show();
				}
			});*/



			v.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					/*Toast.makeText(v.getContext(),"OnClick :" + User.getUser_name() + " \n "+User.getUser_code(),
					Toast.LENGTH_SHORT).show();*/

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


	private void getDeleteRequest(String strMatriId,final int pos)
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
				String paramsMatriId = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"remove_photo_request.php";
				Log.e("remove_photo_request", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair MatriIdPair = new BasicNameValuePair("ph_reqid", paramsMatriId);


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

				Log.e("remove_photo_request", "=="+result);

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
		sendPostReqAsyncTask.execute(strMatriId,String.valueOf(pos));
	}


	public void removeAt(int position)
	{
		arrPhotoPasswordList.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, arrPhotoPasswordList.size());
	}

	public void refreshAt(int position)
	{
		notifyItemChanged(position);
		notifyItemRangeChanged(position, arrPhotoPasswordList.size());
	}





	private void addAcceptRejectRequest(String strMatriId, final String APICall, final int pos)
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
				String paramsLoginMatriId = params[0];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +APICall;
				Log.e("=>"+APICall, "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("ph_reqid", paramsLoginMatriId);

				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(LoginMatriIdPair);

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

				Log.e("Accept or Reject ", "=="+result);

				try
				{
					JSONObject obj = new JSONObject(result);

					String status=obj.getString("status");

					if (status.equalsIgnoreCase("1"))
					{
						String message=obj.getString("message").toString().trim();
						Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();

						if(APICall.contains("photo_accept"))
						{
							arrPhotoPasswordList.get(pos).setReceiver_response("Accepted");
						}else if(APICall.contains("photo_reject"))
						{
							arrPhotoPasswordList.get(pos).setReceiver_response("Reject");
						}
						refreshAt(pos);

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
		sendPostReqAsyncTask.execute(strMatriId,APICall,String.valueOf(pos));
	}


}