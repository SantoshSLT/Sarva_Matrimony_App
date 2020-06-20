package Adepters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thegreentech.ComposeMessage;
import com.thegreentech.R;
import com.squareup.picasso.Picasso;

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

import Models.beanMessages;
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

public class SentMessageAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<beanMessages> arrMessagesList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    Activity activity;
    ProgressDialog progresDialog;
    String matri_id = "";
    String toID = "";
    SharedPreferences prefUpdate;

    public SentMessageAdapter(Activity activity, List<beanMessages> Users, RecyclerView recyclerView) {
        this.activity = activity;
        arrMessagesList = Users;
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
        return arrMessagesList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages, parent, false);
            vh = new UserViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder) {
            final beanMessages singleUser = (beanMessages) arrMessagesList.get(position);
            ((UserViewHolder) holder).tvUserName.setText(singleUser.getUsername());
            ((UserViewHolder) holder).tvUserCode.setText(singleUser.getTo_matri_id());
            ((UserViewHolder) holder).tvDateTime.setText((AppConstants.mDateFormateDDMMM(singleUser.getSent_date())));
            ((UserViewHolder) holder).tvMessage.setText(singleUser.getMessage());

            ((UserViewHolder) holder).tvReadMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            //.setTitle("Delete Message")
                            .setMessage(singleUser.getMessage())
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });




            if (!singleUser.getUser_photo().equalsIgnoreCase("")) {
                Picasso.with(activity)
                        .load(singleUser.getUser_photo())
                        .placeholder(R.drawable.loading1)
                        .into(((UserViewHolder) holder).imgProfilePicture);
            } else {
                ((UserViewHolder) holder).imgProfilePicture.setImageResource(R.drawable.ic_my_profile);
            }


            ((UserViewHolder) holder).llDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setMessage("Do you want to Delete it?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    getDeleteRequest(singleUser.getMes_id(), position);
                                }
                            })
                            .setNegativeButton("Cancel", null).show();

                }
            });


            String is_important = singleUser.getMsg_important_status().toString().trim();


            if (is_important.equalsIgnoreCase("1")) {
                ((UserViewHolder) holder).llFavourite.setBackgroundResource(R.drawable.bg_orange_selected);
                ((UserViewHolder) holder).btnFavourite.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

            } else {
                ((UserViewHolder) holder).llFavourite.setBackgroundResource(R.drawable.bg_orange_select);
                ((UserViewHolder) holder).btnFavourite.setColorFilter(ContextCompat.getColor(activity, R.color.colorOrange), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            ((UserViewHolder) holder).llFavourite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkConnection.hasConnection(activity)){
                        addFavoriteRequest(singleUser.getMes_id(), singleUser.getMsg_important_status(), position);
                    }else
                    {
                        AppConstants.CheckConnection((Activity) activity);
                    }
                }
            });


            ((UserViewHolder) holder).btnSendMessage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    toID = singleUser.getTo_matri_id();

                    if (NetworkConnection.hasConnection(activity)){
                        if (NetworkConnection.hasConnection(activity)){
                            getMessagesRequestStatus(matri_id);
                        }else
                        {
                            AppConstants.CheckConnection((Activity) activity);
                        }
                    }else
                    {
                        AppConstants.CheckConnection((Activity) activity);
                    }
                    //sendMessageAlert(singleUser.getFrom_matri_id(),singleUser.getTo_matri_id());
                }
            });


            ((UserViewHolder) holder).User = singleUser;

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return arrMessagesList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvUserCode, tvDateTime, tvMessage, tvReadMore;
        public TextView btnSendMessage;
        ImageView btnFavourite, btnDelete;
        LinearLayout llFavourite, llDelete;
        public ImageView imgProfilePicture;
        public beanMessages User;

        public UserViewHolder(View v) {
            super(v);
            tvUserName = v.findViewById(R.id.tvUserName);
            tvUserCode = v.findViewById(R.id.tvUserCode);
            tvDateTime = v.findViewById(R.id.tvDateTime);
            tvMessage = v.findViewById(R.id.tvMessage);
            tvReadMore = v.findViewById(R.id.tvReadMore);
            btnFavourite = v.findViewById(R.id.btnFavourite);
            btnDelete = v.findViewById(R.id.btnDelete);
            llFavourite = v.findViewById(R.id.llFavourite);
            llDelete = v.findViewById(R.id.llDelete);
            btnSendMessage = v.findViewById(R.id.btnSendMessage);
            imgProfilePicture = v.findViewById(R.id.imgProfilePicture);
            btnSendMessage.setText("SEND MESSAGE");

            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(),"OnClick :" + User.getUser_name() + " \n "+User.getUser_code(),
                    //Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


    public void sendMessageAlert(final String FromId, final String toId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Send a Message");

        View view = activity.getLayoutInflater().inflate(R.layout.layout_send_message, null);

        final EditText edtSubject = (EditText) view.findViewById(R.id.edtSubject);
        final EditText edtMessage = (EditText) view.findViewById(R.id.edtMessage);
        Button btnSend = (Button) view.findViewById(R.id.btnSend);
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String strSubject = edtSubject.getText().toString();
                String strMessage = edtMessage.getText().toString();
                if (strSubject.equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please enter your subject", Toast.LENGTH_SHORT).show();
                } else if (strMessage.equalsIgnoreCase("")) {
                    Toast.makeText(activity, "Please enter message.", Toast.LENGTH_SHORT).show();
                } else {
                    if (NetworkConnection.hasConnection(activity)){
                        sendSendMessagesRequest(FromId, toId, strSubject, strMessage);
                    }else
                    {
                        AppConstants.CheckConnection((Activity) activity);
                    }
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    public  String parseDateToddMMyyyy(String time) {
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




    private void sendSendMessagesRequest(String FromId, String toId, String strSubject, String strMessage) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsFromMId = params[0];
                String paramsToMId = params[1];
                String paramsSubject = params[2];
                String paramsMesssage = params[3];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "send_message.php";
                Log.e("URL_Sent_Message", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair FromIDPair = new BasicNameValuePair("from_id", paramsFromMId);
                BasicNameValuePair TOIDPair = new BasicNameValuePair("to_id", paramsToMId);
                BasicNameValuePair SubjectPair = new BasicNameValuePair("subject", paramsSubject);
                BasicNameValuePair MessagePair = new BasicNameValuePair("message", paramsMesssage);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(FromIDPair);
                nameValuePairList.add(TOIDPair);
                nameValuePairList.add(SubjectPair);
                nameValuePairList.add(MessagePair);


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

                Log.e("Sent_Listing", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        //JSONObject responseData = obj.getJSONObject("responseData");

                        String message = obj.getString("message");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();


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
        sendPostReqAsyncTask.execute(FromId, toId, strSubject, strMessage);
    }


    private void addFavoriteRequest(String message_id, final String isFavorite, final int pos) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMessageId = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL = "";
                if (isFavorite.equalsIgnoreCase("1")) {
                    URL = AppConstants.MAIN_URL + "remove_favourite.php";
                    Log.e("remove_favourite", "== " + URL);
                } else {
                    URL = AppConstants.MAIN_URL + "add_favourite.php";
                    Log.e("add_favourite", "== " + URL);
                }


                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair LoginMatriIdPair = new BasicNameValuePair("msg_id", paramsMessageId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(LoginMatriIdPair);

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

                        if (isFavorite.equalsIgnoreCase("1")) {
                            arrMessagesList.get(pos).setMsg_important_status("0");
                        } else {
                            arrMessagesList.get(pos).setMsg_important_status("1");
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
        sendPostReqAsyncTask.execute(message_id, isFavorite, String.valueOf(pos));
    }


    private void getDeleteRequest(String strMatriId, final int pos) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "delete_inbox.php";
                Log.e("delete_inbox", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("mes_id", paramsMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);

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

                Log.e("Search by maitri Id", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        //JSONObject responseData = obj.getJSONObject("responseData");

                        String message = obj.getString("message").toString().trim();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        removeAt(pos);
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
        sendPostReqAsyncTask.execute(strMatriId, String.valueOf(pos));
    }


    public void removeAt(int position) {
        arrMessagesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrMessagesList.size());
    }

    public void refreshAt(int position) {
        notifyItemChanged(position);
        notifyItemRangeChanged(position, arrMessagesList.size());
    }


    private void getMessagesRequestStatus(String MatriId) {
        progresDialog = new ProgressDialog(activity);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(activity.getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "compose_message.php";
                Log.e("compose_message", "== " + URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);


                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);

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
                progresDialog.dismiss();


                Log.e("compose_message.php", "==" + result);

                try {
                    JSONObject obj = new JSONObject(result);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("toId", toID);

                        Intent intentComposeMessage = new Intent(activity, ComposeMessage.class);
                        intentComposeMessage.putExtras(bundle);
                        activity.startActivity(intentComposeMessage);

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


                } catch (Exception t) {
                    Log.e("compossssss",t.getMessage());
                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriId);
    }


}