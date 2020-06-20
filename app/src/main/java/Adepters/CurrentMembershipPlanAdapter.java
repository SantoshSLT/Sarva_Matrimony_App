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

import Models.beanCurrentMembershipPlan;

public class CurrentMembershipPlanAdapter extends RecyclerView.Adapter<CurrentMembershipPlanAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCurrentMembershipPlan> arrMembershipPlan;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_membership_plan, parent, false);
        return new MyViewHolder(itemView);
    }

    public CurrentMembershipPlanAdapter(Context context, ArrayList<beanCurrentMembershipPlan> fields_list) {

        this.context = context;
        this.arrMembershipPlan = fields_list;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCurrentMembershipPlan objCity = arrMembershipPlan.get(position);

        if (objCity.getTitle() != null)
        {
            holder.tv_title.setText(objCity.getTitle());
        }

        if (objCity.getInformation() != null)
        {
            holder.tv_information.setText(objCity.getInformation());
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
