package Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thegreentech.R;

import java.util.ArrayList;

import Models.OnlineUsersModel;

public abstract  class AdapterOnlineUsers extends RecyclerView.Adapter<AdapterOnlineUsers.Myholder> {
    Context ctx;
    ArrayList<OnlineUsersModel> userList;

    public AdapterOnlineUsers(Context ctx, ArrayList<OnlineUsersModel> userList) {
        this.ctx = ctx;
        this.userList = userList;
    }

    public AdapterOnlineUsers.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vv = LayoutInflater.from(ctx).inflate(R.layout.item_onlineuser, parent, false);
        return new Myholder(vv);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOnlineUsers.Myholder holder, final int position) {

        holder.tvUserName.setText(userList.get(position).getUsername());
        holder.tvUserCode.setText(userList.get(position).getMatri_id());
        Picasso.with(ctx).load(userList.get(position).getProfile()).into(holder.imgProfilePicture);
       /* holder.tvAddress.setText(userList.get(position).getAge() + "," +
                userList.get(position).getHeight() + "," +
                userList.get(position).getCaste() + "," +
                userList.get(position).getReligion() + "," +
                userList.get(position).getCity() + "," +
                userList.get(position).getState() + "," +
                userList.get(position).getCountry());
*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ItemClick(position, userList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView  tvUserCode, tvUserName,btnChat;
        ImageView imgProfilePicture;

        public Myholder(View itemView) {
            super(itemView);

            tvUserCode = itemView.findViewById(R.id.tvUserCode);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            btnChat= itemView.findViewById(R.id.btnChat);

        }
    }

   public abstract void ItemClick(int pos, OnlineUsersModel model);
}
