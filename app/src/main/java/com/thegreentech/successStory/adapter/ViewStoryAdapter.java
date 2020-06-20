package com.thegreentech.successStory.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.R;
import java.util.ArrayList;
import Models.beanUserSuccessStory;


public class ViewStoryAdapter extends RecyclerView.Adapter<ViewStoryAdapter.MyViewHolder>{

    Context context;
    ArrayList<beanUserSuccessStory> successStoryArrayList;

    public ViewStoryAdapter(Context context, ArrayList<beanUserSuccessStory> successStoryArrayList) {
        this.context = context;
        Log.e("listconstruct",successStoryArrayList.size() +"");
        this.successStoryArrayList = successStoryArrayList;
    }

    @NonNull
    @Override
    public ViewStoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewStoryAdapter.MyViewHolder holder, int position) {
        final beanUserSuccessStory story = successStoryArrayList.get(position);

        holder.tvName.setText(story.getGroomname()+ " & "+ story.getBridename());
        holder.tvMessage.setText(story.getSuccessmessage());
        Glide.with(context)
                .load(story.getWeddingphoto())
                .apply(new RequestOptions().centerCrop().placeholder(R.drawable.loadimage))
                .into(holder.imgProfilePicture);

        holder.tvReadMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage(story.getSuccessmessage())
                        .setPositiveButton("Close", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                            }}).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("listcount",successStoryArrayList.size() +"");
            return successStoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfilePicture;
        TextView tvName,tvMessage,tvReadMsg;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgProfilePicture =itemView.findViewById(R.id.imgProfilePicture);
            tvName =itemView.findViewById(R.id.tvName);
            tvMessage =itemView.findViewById(R.id.tvMessage);
            tvReadMsg =itemView.findViewById(R.id.tvReadMsg);

        }
    }
}
