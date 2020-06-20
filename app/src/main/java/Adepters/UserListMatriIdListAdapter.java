package Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thegreentech.ComposeMessage;
import com.thegreentech.R;

import java.util.ArrayList;

import Models.userListMatriList.UserListMatriIdListModel;

public class UserListMatriIdListAdapter extends RecyclerView.Adapter<UserListMatriIdListAdapter.Viewholder> {

    Context mContext;
    ArrayList<UserListMatriIdListModel> userListMatriIdListModels;
    public setOnClickLis setOnClickLis;

    public UserListMatriIdListAdapter(Context context, ArrayList<UserListMatriIdListModel> userListMatriIdListModels) {
        this.mContext = context;
        this.userListMatriIdListModels = userListMatriIdListModels;
    }

    @NonNull
    @Override
    public UserListMatriIdListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.design_userlist_matri_id_list_item, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListMatriIdListAdapter.Viewholder viewholder, final int i) {

        if (i == 0) {
            viewholder.tvUserListMatriIdListItem.setText(userListMatriIdListModels.get(i).responseData._1.matriId);
        }
        if (i == 1) {
            viewholder.tvUserListMatriIdListItem.setText(userListMatriIdListModels.get(i).responseData._2.matriId);
        }
        if (i == 2) {
            viewholder.tvUserListMatriIdListItem.setText(userListMatriIdListModels.get(i).responseData._3.matriId);
        }
        if (i == 3) {
            viewholder.tvUserListMatriIdListItem.setText(userListMatriIdListModels.get(i).responseData._4.matriId);
        }


        viewholder.tvUserListMatriIdListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (setOnClickLis!=null){
                    setOnClickLis.clickOnUserListMatriId(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userListMatriIdListModels.size();
    }



    class Viewholder extends RecyclerView.ViewHolder {
        TextView tvUserListMatriIdListItem;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvUserListMatriIdListItem = itemView.findViewById(R.id.tvUserListMatriIdListItem);
        }
    }

    public interface setOnClickLis {
        void clickOnUserListMatriId(int position);
    }

    public void clickOnUserListMatriId(setOnClickLis setOnClickLis) {
        this.setOnClickLis = setOnClickLis;
    }
}
