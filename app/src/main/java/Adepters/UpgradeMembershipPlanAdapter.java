package Adepters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.AllMatches.adapters.OneMatchAdapter;
import com.thegreentech.PaymentActivity;
import com.thegreentech.R;

import java.util.ArrayList;
import java.util.List;

import Models.beanUpgradeMembershipPlan;

public class UpgradeMembershipPlanAdapter extends RecyclerView.Adapter<UpgradeMembershipPlanAdapter.MyViewHolder> {
    private Activity context;
    private List<beanUpgradeMembershipPlan> arrUpgradeMembershipPlan;
    RelativeLayout linDetailView;
    TextView textSubHeader;
    RecyclerView recyclerPlan;

    public UpgradeMembershipPlanAdapter(Activity context, List<beanUpgradeMembershipPlan> arrUpgradeMembershipPlan, RelativeLayout linDetailView,
                                        TextView textSubHeader, RecyclerView recyclerPlan) {
        this.context = context;
        this.arrUpgradeMembershipPlan = arrUpgradeMembershipPlan;
        this.linDetailView = linDetailView;
        this.textSubHeader = textSubHeader;
        this.recyclerPlan = recyclerPlan;
    }

    @NonNull
    @Override
    public UpgradeMembershipPlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upgrade_membership_plan, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull UpgradeMembershipPlanAdapter.MyViewHolder holder, int position) {
        final beanUpgradeMembershipPlan objmembershipPlan = arrUpgradeMembershipPlan.get(position);

        holder.tv_name.setText(objmembershipPlan.getPlan_name());
        holder.tv_Rupees.setText(objmembershipPlan.getPlan_amount_type()+" "+objmembershipPlan.getPlan_amount());
        if (objmembershipPlan.getPlan_duration().equalsIgnoreCase("1"))
             holder.tvDurationTime.setText(objmembershipPlan.getPlan_duration() +" day");
        else
            holder.tvDurationTime.setText(objmembershipPlan.getPlan_duration() +" days");


        holder.linearPlanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                linDetailView.setVisibility(View.VISIBLE);

                Animation bottomUp = AnimationUtils.loadAnimation(context,  R.anim.slide_up_dialog);
                linDetailView.startAnimation(bottomUp) ;

                String plan_id=objmembershipPlan.getPlan_id();
                String plan_name=objmembershipPlan.getPlan_name();
                String plan_amount_type=objmembershipPlan.getPlan_amount_type();
                String plan_amount=objmembershipPlan.getPlan_amount();
                String plan_duration=objmembershipPlan.getPlan_duration();
                String plan_msg=objmembershipPlan.getPlan_msg();
                String plan_sms=objmembershipPlan.getPlan_sms();
                String plan_contacts=objmembershipPlan.getPlan_contacts();
                String chat=objmembershipPlan.getChat();
                String profile=objmembershipPlan.getProfile();
                String status1=objmembershipPlan.getStatus();

                Log.d("TAG","status = "+status1);

                textSubHeader.setText(plan_name);

                ArrayList<beanUpgradeMembershipPlan> arrMembershipPlan= new ArrayList<beanUpgradeMembershipPlan>();
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Plan Name",plan_name));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Amount","Rs. "+plan_amount));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Duration",plan_duration+" Days"));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Message",plan_msg));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"SMS",plan_sms));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Contact",plan_contacts));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Chat",chat));
                arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Profile",profile));
              //  arrMembershipPlan.add(new beanUpgradeMembershipPlan(plan_id,"Offer","-"));

                UpgradeMembershipPlanDetailAdapter upgradeMembershipPlanDetailAdapter = new UpgradeMembershipPlanDetailAdapter(context,arrMembershipPlan);
                recyclerPlan.setAdapter(upgradeMembershipPlanDetailAdapter);

                //UpgradeMembershipPlanDetailActivity.PlanDetails=objmembershipPlan;
                //context.startActivity(new Intent(context, UpgradeMembershipPlanDetailActivity.class));
            }
        });

        holder.textBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intentPayment = new Intent(context, PaymentActivity.class);
                intentPayment.putExtra("selectpkg",objmembershipPlan);
                context.startActivity(intentPayment);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrUpgradeMembershipPlan.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_Rupees,textBuyNow,tvDurationTime;
        LinearLayout linearPlanView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name =  itemView.findViewById(R.id.tv_name);
            tv_Rupees =  itemView.findViewById(R.id.tv_Rupees);
            tvDurationTime =  itemView.findViewById(R.id.tvDurationTime);
            textBuyNow =  itemView.findViewById(R.id.btnSelect);
            linearPlanView = itemView.findViewById(R.id.cardView);

        }
    }
}
