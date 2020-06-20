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

import Models.beanEducation;
import utills.AppConstants;

public class AdditionalDgreeAdapter extends RecyclerView.Adapter<AdditionalDgreeAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanEducation> arrEducationList;
    private ArrayList<beanEducation> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtEducation;

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

    public AdditionalDgreeAdapter(Context context, ArrayList<beanEducation> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                  ImageView btnMenuClose, EditText edtEducationCode) {

        this.context = context;
        this.arrEducationList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtEducation=edtEducationCode;
        this.arrFilter = new ArrayList<beanEducation>();
        this.arrFilter.addAll(arrEducationList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanEducation objEducation = arrEducationList.get(position);

        if (objEducation.getEducation_name() != null)
        {
            holder.tv_name.setText(objEducation.getEducation_name());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.AditionalEducationName=objEducation.getEducation_name();
                AppConstants.AditionalEducationId=objEducation.getEducation_id();

                edtEducation.setText(AppConstants.AditionalEducationName);


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
        return arrEducationList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrEducationList.clear();
        if (charText.length() == 0) {
            arrEducationList.addAll(arrFilter);
        }
        else
        {
            for (beanEducation wp : arrFilter)
            {
                String ProductName=wp.getEducation_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrEducationList.add(wp);
                }
            }
        }

        if(arrEducationList.size() == 0)
        {
            arrEducationList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
