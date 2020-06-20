package Adepters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.ExpressInterestActivity;
import com.thegreentech.MainActivity;
import com.thegreentech.MemberViewProfile;
import com.thegreentech.MessagesActivity;
import com.thegreentech.PhotoPasswordRequestActivity;
import com.thegreentech.R;
import com.thegreentech.ShortedProfileActivity;

import java.util.ArrayList;

import Models.Notification_Model;
import utills.AppConstants;

public class Adapter_notification extends RecyclerView.Adapter<Adapter_notification.Myholder> {


    ArrayList<Notification_Model> list;
    Context ctx;

    public Adapter_notification(ArrayList<Notification_Model> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
        notifyDataSetChanged();
    }

    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vv = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_notification, parent, false);
        return new Myholder(vv);
    }

    public void onBindViewHolder(@NonNull Myholder holder, final int position) {

        Glide.with(ctx)
                .load(list.get(position).getImgProfileUrl())
                .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_my_profile))
                .into(holder.imgReciverProfile);



        if (list.get(position).getReminder_mes_type().equals("exp_interest")) {

            holder.tvMsg.setText("You have new Express interest Request from"
            );

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AppConstants.fromNotification = "exp_interest";
                    Intent intent = new Intent(ctx, ExpressInterestActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("noti", "exp_interest");
                    ctx.startActivity(intent);

                }
            });

        }

        else if (list.get(position).getReminder_mes_type().equals("msg")) {
            holder.tvMsg.setText("You have new Message Received from");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AppConstants.fromNotification = "msg";
                    Intent intent = new Intent(ctx, MessagesActivity.class);
                    intent.putExtra("noti", "msg");
                    ctx.startActivity(intent);
                }
            });

        }

        else if (list.get(position).getReminder_mes_type().equals("photo_req")) {

            holder.tvMsg.setText("You have Received Photo Request from");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AppConstants.fromNotification = "photo_req";
                    Intent intent = new Intent(ctx, PhotoPasswordRequestActivity.class);
                    intent.putExtra("noti", "photo_req");
                    ctx.startActivity(intent);
                }
            });


        }

        else if (list.get(position).getReminder_mes_type().equals("photo_pass_req")) {
            holder.tvMsg.setText("You have Received Photo Password Request from");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AppConstants.fromNotification = "photo_pass_req";
                    Intent intent = new Intent(ctx, PhotoPasswordRequestActivity.class);
                    intent.putExtra("noti", "photo_pass_req");
                    ctx.startActivity(intent);
                }
            });

        }

        else if (list.get(position).getReminder_mes_type().equals("chk_contact")) {
            holder.tvMsg.setText("View your contact number from");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AppConstants.fromNotification = "chk_contact";
                    Intent newIntent = new Intent(ctx, ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle", "Mobile No Viewed By");
                    newIntent.putExtra("PageType", "5");
                    newIntent.putExtra("noti", "chk_contact");

                    ctx.startActivity(newIntent);
                }
            });
        }



        holder.tvMatriID.setText(list.get(position).getMatriId());
        holder.tvMatriID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberViewProfile.matri_id = list.get(position).getMatriId();
                //   MemberViewProfile.is_shortlist=singleUser.getIs_shortlisted();
                ctx.startActivity(new Intent(ctx, MemberViewProfile.class));
            }
        });
    }

    public int getItemCount() {
        return list.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        TextView tvMsg, tvMatriID;
        ImageView imgReciverProfile;

        public Myholder(View itemView) {
            super(itemView);

            imgReciverProfile = itemView.findViewById(R.id.imgReciverProfile);
            tvMsg = itemView.findViewById(R.id.tv_notimsg);
            tvMatriID = itemView.findViewById(R.id.tvMatriID);
        }
    }

    public void clearData() {
        // clear the data
        list.clear();
    }
}
