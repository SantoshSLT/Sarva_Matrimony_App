package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.UserViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<beanUserData> arrUserList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    Activity activity;
    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id = "";
    ArrayList<String> tokans;
    String RequestType;


    public SearchResultAdapter(Activity activity, List<beanUserData> Users, RecyclerView recyclerView, ArrayList<String> tokans) {
        this.activity = activity;
        this.tokans = tokans;
        arrUserList = Users;
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(activity);
        matri_id = prefUpdate.getString("matri_id", "");

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                    notifyDataSetChanged();
                }
            });
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return arrUserList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public SearchResultAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchResultAdapter.UserViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchresult_list, parent, false);
            vh = new UserViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder) {
            final beanUserData singleUser = (beanUserData) arrUserList.get(position);
            holder.tvUserName.setText(singleUser.getUsername());
            holder.tvUserCode.setText(singleUser.getMatri_id());
            holder.tvAddress.setText(singleUser.getAddress().trim());

            Glide.with(activity)
                    .load(singleUser.getUser_profile_picture())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile1))
                    .into(holder.imgProfilePicture);
/*

			if(! singleUser.getUser_profile_picture().equalsIgnoreCase(""))
			{
			    Log.e("image",singleUser.getUser_profile_picture());
				Picasso.with(activity)
						.load(singleUser.getUser_profile_picture())
						.fit()
						.into(holder.imgProfilePicture);

			} else {
				holder.imgProfilePicture.setImageResource(R.drawable.ic_profile1);
			}
*/


            AppConstants.is_shortlisted = singleUser.getIs_shortlisted().toString().trim();
            AppConstants.is_blocked = singleUser.getIs_blocked().toString().trim();
            AppConstants.is_interest = singleUser.getIs_favourite().toString().trim();


            if (AppConstants.is_shortlisted.equalsIgnoreCase("1")) {
                holder.llShortlist.setBackgroundResource(R.drawable.bg_orange_selected);
                holder.btnShortlist.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

            }
            else {
                holder.llShortlist.setBackgroundResource(R.drawable.bg_orange_select);
                holder.btnShortlist.setColorFilter(ContextCompat.getColor(activity, R.color.colorOrange), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            if (AppConstants.is_blocked.equalsIgnoreCase("1")) {
                holder.llBlock.setBackgroundResource(R.drawable.bg_orange_selected);
                holder.btnBlock.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            else {
                holder.llBlock.setBackgroundResource(R.drawable.bg_orange_select);
                holder.btnBlock.setColorFilter(ContextCompat.getColor(activity, R.color.colorOrange), android.graphics.PorterDuff.Mode.SRC_IN);

            }

            if (AppConstants.is_interest.equalsIgnoreCase("1")) {
                RequestType = "Send Reminder";
                holder.ivInterest.setImageResource(R.drawable.ic_reminder);
                holder.tvInterest.setText("Send Reminder");

            }
            else {
                RequestType = "Send Intrest";
                holder.ivInterest.setImageResource(R.drawable.ic_heart);
                holder.tvInterest.setText("Send Intrest");
            }


            holder.llShortlist.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkConnection.hasConnection(activity)) {
                        addToShortListRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_shortlisted(), position, (UserViewHolder) holder);
                    } else {
                        AppConstants.CheckConnection((Activity) activity);
                    }
                }
            });

            holder.llBlock.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("b_id", singleUser.getMatri_id());
                    if (NetworkConnection.hasConnection(activity)) {
                        addToBlockRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_blocked(), position, (UserViewHolder) holder);
                    } else {
                        AppConstants.CheckConnection((Activity) activity);
                    }

                }
            });



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

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                            sendInterestRequest(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position,(UserViewHolder) holder);
                        }
                    }
                    else if (RequestType.equalsIgnoreCase("Send Reminder"))
                    {
                        if (singleUser.getIs_blocked().equalsIgnoreCase("1"))
                        {
                            String msgBlock = "This member has blocked you. You can't express your interest.";
                            String msgNotPaid = "You are not paid member. Please update your membership to express your interest.";

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

                            sendInterestRequestRemind(matri_id, singleUser.getMatri_id(), singleUser.getIs_favourite(), position);
                        }
                    }
                }
            });

            ((SearchResultAdapter.UserViewHolder) holder).cardView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberViewProfile.matri_id = singleUser.getMatri_id();
                    MemberViewProfile.is_shortlist = singleUser.getIs_shortlisted();
                    activity.startActivity(new Intent(activity, MemberViewProfile.class));
                }
            });

            holder.User = singleUser;

        } else {
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvUserCode, tvAddress, tvInterest;
        public LinearLayout  llBlock, llShortlist, llInterest;
        public ImageView imgProfilePicture, btnBlock, btnShortlist, ivInterest;
        public beanUserData User;
        public CardView cardView;

        public UserViewHolder(View itemview) {
            super(itemview);

            cardView = itemview.findViewById(R.id.cardView);
            tvUserName = itemview.findViewById(R.id.tvUserName);
            tvUserCode = itemview.findViewById(R.id.tvUserCode);
            tvAddress = itemview.findViewById(R.id.tvAddress);

            llInterest = itemview.findViewById(R.id.llInterest);
            ivInterest = itemview.findViewById(R.id.ivInterest);
            tvInterest = itemview.findViewById(R.id.tvInterest);

            llBlock = itemview.findViewById(R.id.llBlock);
            llShortlist = itemview.findViewById(R.id.llShortlist);
            btnShortlist = itemview.findViewById(R.id.btnShortlist);
            btnBlock = itemview.findViewById(R.id.btnBlock);
            //	btnSendInterest=  itemview.findViewById(R.id.btnSendInterest);

            imgProfilePicture = itemview.findViewById(R.id.imgProfilePicture);


            itemview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(itemview.getContext(),"OnClick :" + User.getUser_name() + " \n "+User.getUser_code(),
                    //Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends UserViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemview) {
            super(itemview);
            progressBar = (ProgressBar) itemview.findViewById(R.id.progressBar1);
        }
    }


    private void addToShortListRequest(String login_matri_id, String strMatriId, final String isShortlisted, final int pos, final UserViewHolder holder) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (isShortlisted.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "remove_shortlist.php";
                    Log.e("remove_shortlist", "== " + URL);
                } else {
                    URL = AppConstants.MAIN_URL + "add_shortlisted.php";
                    Log.e("add_shortlisted", "== " + URL);
                }

                Log.e("add_shortlisted", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("from_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("to_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("add_shortlisted", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if (isShortlisted.equalsIgnoreCase("1")) {
                            arrUserList.get(pos).setIs_shortlisted("0");
//							holder.llShortlist.setBackgroundResource(R.drawable.bg_orange_selected);
//							holder.btnShortlist.setBackgroundResource(R.drawable.icn_fav);
                        } else {
                            arrUserList.get(pos).setIs_shortlisted("1");
//							holder.llShortlist.setBackgroundResource(R.drawable.bg_orange_select);
//							holder.btnShortlist.setBackgroundResource(R.drawable.icn_fav);
//							holder.btnShortlist.setColorFilter(ContextCompat.getColor(activity, R.color.colorOrange), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        refreshAt(pos);


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId, isShortlisted, String.valueOf(pos));
    }


    private void addToBlockRequest(String login_matri_id, String strMatriId, final String isBlocked, final int pos, final UserViewHolder holder) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (isBlocked.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "remove_blocklist.php";
                    Log.e("remove_blocklist", "== " + URL);
                } else {
                    URL = AppConstants.MAIN_URL + "block_user.php";
                    Log.e("block_user", "== " + URL);
                }


                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("matri_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("block_matri_id", paramsUserMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("block_user", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if (isBlocked.equalsIgnoreCase("1")) {
                            arrUserList.get(pos).setIs_blocked("0");
//							holder.llBlock.setBackgroundResource(R.drawable.bg_orange_selected);
//							holder.btnBlock.setBackgroundResource(R.drawable.icn_block);
                        } else {
                            arrUserList.get(pos).setIs_blocked("1");
//							holder.llBlock.setBackgroundResource(R.drawable.bg_orange_select);
//							holder.btnBlock.setBackgroundResource(R.drawable.icn_block);
//							holder.btnBlock.setColorFilter(ContextCompat.getColor(activity, R.color.colorOrange), android.graphics.PorterDuff.Mode.SRC_IN);

                        }

                        refreshAt(pos);


                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId, isBlocked, String.valueOf(pos));
    }


    private void sendInterestRequest(String login_matri_id, String strMatriId, final String isFavorite, final int pos, final UserViewHolder holder) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "send_intrest.php";
                Log.e("send_intrest", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("receiver_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("send_intrest", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        holder.ivInterest.setImageResource(R.drawable.ic_reminder);
                        holder.tvInterest.setText("Send Reminder");
                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if (isFavorite.equalsIgnoreCase("1")) {
                            arrUserList.get(pos).setIs_favourite("0");
                        } else {
                            arrUserList.get(pos).setIs_favourite("1");
                        }

                        refreshAt(pos);

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId);
    }

    private void sendInterestRequestRemind(String login_matri_id, String strMatriId, final String isFavorite, final int pos) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsLoginMatriId = params[0];
                String paramsUserMatriId = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "send_intrest.php";
                Log.e("send_intrest", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("sender_id", paramsLoginMatriId);
                BasicNameValuePair UserMatriIdPair = new BasicNameValuePair("receiver_id", paramsUserMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);
                nameValuePairList.add(UserMatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("send_intrest", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();

                        if (isFavorite.equalsIgnoreCase("1")) {
                            //arrUserList.get(pos).setIs_favourite("0");
                        } else {
                            arrUserList.get(pos).setIs_favourite("1");
                        }

                        refreshAt(pos);

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                    progresDialog.dismiss();
                } catch (Throwable t) {
                    progresDialog.dismiss();
                }
                progresDialog.dismiss();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login_matri_id, strMatriId);
    }


    public void refreshAt(int position) {
        notifyItemChanged(position);
        notifyItemRangeChanged(position, arrUserList.size());
    }

}