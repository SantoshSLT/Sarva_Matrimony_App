package com.thegreentech.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.R;

import java.util.List;

import Models.Chat_Model;

public class ChatMessageAdapter extends RecyclerView.Adapter {

    private static final int MESSAGE_SENT_TYPE = 1;
    private static final int MESSAGE_RECEIVED_TYPE = 2;
    Context context;
    List<Chat_Model> sentMessageDataList;
    String matriID = "", ToImgProfile = "",FromUserProfile="";
    String fromId = "";

    public ChatMessageAdapter(Context context, List<Chat_Model> sentMessageDataList, String myid, String fmid, String imgProfile,String imgUserProfile) {
        this.context = context;
        this.sentMessageDataList = sentMessageDataList;
        matriID = myid;
        ToImgProfile = imgProfile;
        fromId = fmid;
        this.FromUserProfile =imgUserProfile;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        if (i == MESSAGE_SENT_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.message_sent, viewGroup, false);
            return new SentMessageHolder(view);
        } else if (i == MESSAGE_RECEIVED_TYPE) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.message_recieve, viewGroup, false);
            return new RecieveMessageHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Chat_Model sentMessageData = sentMessageDataList.get(position);

        if (sentMessageData.getFrom_id().equalsIgnoreCase(matriID))
            return MESSAGE_SENT_TYPE;
        else
            return MESSAGE_RECEIVED_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Chat_Model sentMessageData = sentMessageDataList.get(i);

        switch (viewHolder.getItemViewType()) {
            case MESSAGE_SENT_TYPE:
                ((SentMessageHolder) viewHolder).setSentData(sentMessageData, i);
                break;
            case MESSAGE_RECEIVED_TYPE:
                ((RecieveMessageHolder) viewHolder).setRecieveData(sentMessageData, i);
        }
    }


    @Override
    public int getItemCount() {
        return sentMessageDataList.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView tvSentMessage, tvSentTime;
        ImageView ivSenderProfile;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            tvSentMessage = itemView.findViewById(R.id.tvSentMessage);
            tvSentTime = itemView.findViewById(R.id.tvSentTime);
            ivSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
        }

        void setSentData(final Chat_Model sentData, int position) {
            tvSentMessage.setText(sentData.getMsg());
            tvSentTime.setText(sentData.getDate());
            Glide.with(context)
                    .load(FromUserProfile)
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_my_profile))
                    .into(ivSenderProfile);
            if (position < sentMessageDataList.size() - 1)
                if (sentMessageDataList.get(position + 1).getFrom_id().equalsIgnoreCase(matriID)) {
                    ivSenderProfile.setVisibility(View.INVISIBLE);
                } else {
                    ivSenderProfile.setVisibility(View.VISIBLE);
                }
        }
    }


    private class RecieveMessageHolder extends RecyclerView.ViewHolder {

        TextView tvRecieveMessage, tvRecieveTime;
        ImageView ivRecieverProfile;

        public RecieveMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvRecieveMessage = itemView.findViewById(R.id.tvRecieveMessage);
            tvRecieveTime = itemView.findViewById(R.id.tvRecieveTime);
            ivRecieverProfile = itemView.findViewById(R.id.ivRecieverProfile);
        }

        void setRecieveData(final Chat_Model data, int position) {
            tvRecieveMessage.setText(data.getMsg());
            tvRecieveTime.setText(data.getDate());

            Glide.with(context)
                    .load(ToImgProfile)
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_my_profile))
                    .into(ivRecieverProfile);


            if (position < sentMessageDataList.size() - 1)
                if (!sentMessageDataList.get(position + 1).getFrom_id().equalsIgnoreCase(matriID)) {
                    ivRecieverProfile.setVisibility(View.INVISIBLE);
                } else {
                    ivRecieverProfile.setVisibility(View.VISIBLE);
                }

        }
    }


}
