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

import utills.AppConstants;

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.MyViewHolder> {

     Context context;
    String[] arrGeneralList;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtGeneral;

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

    public GeneralAdapter(Context context, String[] fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                          ImageView btnMenuClose, EditText edtedtGeneralCode) {

        this.context = context;
        this.arrGeneralList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtGeneral=edtedtGeneralCode;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        holder.tv_name.setText(arrGeneralList[position]);
        //Log.d("ravi","holder = "+arrGeneralList[position]);

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                edtGeneral.setText(arrGeneralList[position]);

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
        return arrGeneralList.length;
    }



}
