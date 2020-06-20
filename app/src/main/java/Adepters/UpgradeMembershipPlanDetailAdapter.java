package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.util.ArrayList;

import Models.beanUpgradeMembershipPlan;

/**
 * Created by royal on 9/2/18.
 */

public class UpgradeMembershipPlanDetailAdapter extends RecyclerView.Adapter<UpgradeMembershipPlanDetailAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanUpgradeMembershipPlan> arrMembershipPlan;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_title,tv_information;
        public LinearLayout cardView;

        public MyViewHolder(View view)
        {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_information = (TextView) view.findViewById(R.id.tv_information);
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upgrade_membership_plan_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    public UpgradeMembershipPlanDetailAdapter(Context context, ArrayList<beanUpgradeMembershipPlan> fields_list) {

        this.context = context;
        this.arrMembershipPlan = fields_list;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanUpgradeMembershipPlan objCity = arrMembershipPlan.get(position);

        if (objCity.getTitle() != null)
        {
            holder.tv_title.setText(objCity.getTitle());
        }

        if (objCity.getInforamtion() != null)
        {
            holder.tv_information.setText(objCity.getInforamtion());
        }



  /*      holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strPlanName=objCity.getPlan_name();
                String strCityID=objCity.getPlan_days();

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return arrMembershipPlan.size();
    }




}

