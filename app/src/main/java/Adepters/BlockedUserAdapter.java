package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

public class BlockedUserAdapter extends RecyclerView.Adapter
{
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;

	private List<beanUserData> arrInboxMessagesList;

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 20;
	private int lastVisibleItem, totalItemCount;
	private boolean loading;
	private OnLoadMoreListener onLoadMoreListener;
	Activity activity;
	SharedPreferences prefUpdate;
	ProgressDialog progresDialog;
	String matri_id="";


	public BlockedUserAdapter(Activity activity, List<beanUserData> Users, RecyclerView recyclerView)
	{
		this.activity=activity;
		arrInboxMessagesList = Users;
		prefUpdate= PreferenceManager.getDefaultSharedPreferences(activity);
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
	}

	@Override
	public int getItemViewType(int position)
	{
		return arrInboxMessagesList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		RecyclerView.ViewHolder vh;
		if (viewType == VIEW_ITEM)
		{
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked_user, parent, false);
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
			final beanUserData singleUser= (beanUserData) arrInboxMessagesList.get(position);
			((UserViewHolder) holder).tvUserName.setText(singleUser.getUsername());
			((UserViewHolder) holder).tvUserCode.setText(singleUser.getMatri_id());
			((UserViewHolder) holder).tvAddress.setText(singleUser.getAddress().trim());
			//((UserViewHolder) holder).tvMessage.setText(singleUser.getMessage());


			if(! singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
				Picasso.with(activity)
						.load(singleUser.getUser_profile_picture())
						.placeholder(R.drawable.loading1)
						.into(((UserViewHolder) holder).imgProfilePicture);
			} else {
				((UserViewHolder) holder).imgProfilePicture.setImageResource(R.drawable.ic_profile);
			}


			((UserViewHolder) holder).btnUnblock.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

					if (NetworkConnection.hasConnection(activity)) {

						UnblockRequest(matri_id,singleUser.getMatri_id(),position);
					}else
					{
						AppConstants.CheckConnection(activity);
					}


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
		return arrInboxMessagesList.size();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
	{
		this.onLoadMoreListener = onLoadMoreListener;
	}


	//
	public static class UserViewHolder extends RecyclerView.ViewHolder
	{
		public TextView tvUserName,tvUserCode,tvAddress;
		public Button btnUnblock;
		public ImageView imgProfilePicture;
		public beanUserData User;
		public UserViewHolder(View v)
		{
			super(v);
			tvUserName = (TextView) v.findViewById(R.id.tvUserName);
			tvUserCode = (TextView) v.findViewById(R.id.tvUserCode);
			tvAddress= (TextView) v.findViewById(R.id.tvAddress);

			btnUnblock= (Button) v.findViewById(R.id.btnUnblock);

			imgProfilePicture= (ImageView) v.findViewById(R.id.imgProfilePicture);




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

	private void UnblockRequest(String login_matri_id,String strMatriId, final int pos)
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
				String paramsUserMatriId = params[1];

				HttpClient httpClient = new DefaultHttpClient();

				String URL= AppConstants.MAIN_URL +"remove_blocklist.php";
				Log.e("remove_blocklist", "== "+URL);

				HttpPost httpPost = new HttpPost(URL);

				BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("matri_id", paramsLoginMatriId);
				BasicNameValuePair UserMatriIdPair  = new BasicNameValuePair("block_matri_id", paramsUserMatriId);

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

				Log.e("block_user", "=="+result);

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
		sendPostReqAsyncTask.execute(login_matri_id,strMatriId,String.valueOf(pos));
	}


	public void removeAt(int position)
	{
		arrInboxMessagesList.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, arrInboxMessagesList.size());
	}


}