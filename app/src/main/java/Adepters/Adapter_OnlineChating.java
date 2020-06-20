package Adepters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.R;

import java.util.ArrayList;

import Models.Chat_Model;

public class Adapter_OnlineChating extends RecyclerView.Adapter<Adapter_OnlineChating.Myholder> {

    ArrayList<Chat_Model> list;
    Context ctx;
    String myid = "",FromImgProfile="";
    String fmid;
    SharedPreferences preferences;
    public Adapter_OnlineChating(ArrayList<Chat_Model> list, Context ctx,String myid,String fmid,String imgProfile) {
        this.list = list;
        this.ctx = ctx;
        this.myid = myid;
        this.fmid = fmid;
        this.FromImgProfile = imgProfile;
    }
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vv = LayoutInflater.from(ctx).inflate(R.layout.item_chat_raw,parent,false);
        return new Myholder(vv);
    }
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        holder.setIsRecyclable(false);
        if(myid.equals(list.get(position).getFrom_id()))
        {
            holder.llMe.setVisibility(View.VISIBLE);
            holder.tvMmsg.setText(list.get(position).getMsg());
            holder.tvMtime.setText(list.get(position).getDate());

            Glide.with(ctx)
                    .load(FromImgProfile)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                    .into(holder.ivSenderProfile);
            if (position < list.size() - 1)
                if (list.get(position + 1).getFrom_id().equalsIgnoreCase(myid)){
                    holder.ivSenderProfile.setVisibility(View.INVISIBLE);
                } else {
                    holder.ivSenderProfile.setVisibility(View.VISIBLE);
                }

        }
        if(fmid.equals(list.get(position).getFrom_id()))
        {
            holder.llFrd.setVisibility(View.VISIBLE);
            holder.tvFmsg.setText(list.get(position).getMsg());
            holder.tvFtime.setText(list.get(position).getDate());

            Glide.with(ctx)
                    .load(list.get(position).getImgReciverProfileurl())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                    .into(holder.imgReciverProfile);
            if (position < list.size() - 1)
                if (list.get(position + 1).getFrom_id().equalsIgnoreCase(myid)){
                    holder.imgReciverProfile.setVisibility(View.INVISIBLE);
                } else {
                    holder.imgReciverProfile.setVisibility(View.VISIBLE);
                }

        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount() {
        return list.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        LinearLayout llFrd, llMe;
        TextView tvFtime, tvFmsg;
        TextView tvMtime, tvMmsg;
        TextView tvChatdate;
        ImageView ivSenderProfile,imgReciverProfile;

        public Myholder(View itemView) {
            super(itemView);

            llFrd = itemView.findViewById(R.id.llFrd);
            tvFtime = itemView.findViewById(R.id.tvFtime);
            tvFmsg = itemView.findViewById(R.id.tvFmsg);
            llMe = itemView.findViewById(R.id.llMe);
            tvMtime = itemView.findViewById(R.id.tvMtime);
            tvMmsg = itemView.findViewById(R.id.tvMmsg);
            tvChatdate = itemView.findViewById(R.id.tvChatdate);
            ivSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            imgReciverProfile = itemView.findViewById(R.id.imgReciverProfile);


        }
    }
}
