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

import Models.beanCaste;
import utills.AppConstants;

public class CasteAdapter extends RecyclerView.Adapter<CasteAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCaste> arrCasteList;
    private ArrayList<beanCaste> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
  // ImageView btnMenuClose;
    EditText edtCaste;

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

    public CasteAdapter(Context context, ArrayList<beanCaste> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                         EditText edtCaste) {

        this.context = context;
        this.arrCasteList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        //this.btnMenuClose=btnMenuClose;
        this.edtCaste=edtCaste;
        this.arrFilter = new ArrayList<beanCaste>();
        this.arrFilter.addAll(arrCasteList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCaste objCountry = arrCasteList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.CasteName=objCountry.getName();
                AppConstants.CasteId=objCountry.getCaste_id();

                edtCaste.setText(AppConstants.CasteName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

               // btnMenuClose.setVisibility(View.GONE);
              //  btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrCasteList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCasteList.clear();
        if (charText.length() == 0) {
            arrCasteList.addAll(arrFilter);
        }
        else
        {
            for (beanCaste wp : arrFilter)
            {
                String ProductName=wp.getName();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrCasteList.add(wp);
                }
            }
        }

        if(arrCasteList.size() == 0)
        {
            arrCasteList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
