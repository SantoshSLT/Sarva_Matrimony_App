package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.util.ArrayList;
import java.util.Locale;

import Models.beanState;
import utills.AppConstants;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanState> arrStateList;
    private ArrayList<beanState> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtState;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;

        public MyViewHolder(View view)
        {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public StateAdapter(Context context, ArrayList<beanState> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                        ImageView btnMenuClose, EditText edtStateCode) {

        this.context = context;
        this.arrStateList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtState=edtStateCode;
        this.arrFilter = new ArrayList<beanState>();
        this.arrFilter.addAll(arrStateList);
            notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanState objState = arrStateList.get(position);

        if (objState.getState_name() != null)
        {
            holder.tv_name.setText(objState.getState_name());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.StateName=objState.getState_name();
                AppConstants.StateId=objState.getState_id();

                edtState.setText(AppConstants.StateName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                btnMenuClose.setVisibility(View.GONE);
                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrStateList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrStateList.clear();
        if (charText.length() == 0) {
            arrStateList.addAll(arrFilter);
        }
        else
        {
            for (beanState wp : arrFilter)
            {
                String ProductName=wp.getState_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrStateList.add(wp);
                }
            }
        }

        if(arrStateList.size() == 0)
        {
            arrStateList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
