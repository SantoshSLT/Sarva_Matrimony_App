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

import Models.beanOccupation;
import utills.AppConstants;

public class OccupationAdapter extends RecyclerView.Adapter<OccupationAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanOccupation> arrOccupationList;
    private ArrayList<beanOccupation> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtOccupation;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;

        public MyViewHolder(View view) {
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

    public OccupationAdapter(Context context, ArrayList<beanOccupation> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                             ImageView btnMenuClose, EditText edtOccupationCode) {

        this.context = context;
        this.arrOccupationList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtOccupation=edtOccupationCode;
        this.arrFilter = new ArrayList<beanOccupation>();
        this.arrFilter.addAll(arrOccupationList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanOccupation objOccupation = arrOccupationList.get(position);

        if (objOccupation.getOccupation_name() != null)
        {
            holder.tv_name.setText(objOccupation.getOccupation_name());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.OccupationName=objOccupation.getOccupation_name();
                AppConstants.OccupationID=objOccupation.getOccupation_id();

                edtOccupation.setText(AppConstants.OccupationName);

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
        return arrOccupationList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrOccupationList.clear();
        if (charText.length() == 0) {
            arrOccupationList.addAll(arrFilter);
        }
        else
        {
            for (beanOccupation wp : arrFilter)
            {
                String ProductName=wp.getOccupation_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrOccupationList.add(wp);
                }
            }
        }

        if(arrOccupationList.size() == 0)
        {
            arrOccupationList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
