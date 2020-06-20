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

import Models.beanCountryCode;
import utills.AppConstants;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCountryCode> arrCountryCodeList;
    private ArrayList<beanCountryCode> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
  //  ImageView btnMenuClose;
    EditText edtCountryCode;

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

    public CountryCodeAdapter(Context context, ArrayList<beanCountryCode> fields_list,RelativeLayout SlidingDrawer, LinearLayout  Slidingpage
                             ,EditText edtCountryCode) {

        this.context = context;
        this.arrCountryCodeList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
     //   this.btnMenuClose=btnMenuClose;
        this.edtCountryCode=edtCountryCode;
        this.arrFilter = new ArrayList<beanCountryCode>();
        this.arrFilter.addAll(arrCountryCodeList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCountryCode objCountry = arrCountryCodeList.get(position);

        if (objCountry.getCountry_code() != null)
        {
            holder.tv_name.setText(objCountry.getCountry_code());
        }



        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.CountryCodeName=objCountry.getCountry_code();
                AppConstants.CountryCodeId=objCountry.getCountry_id();

                edtCountryCode.setText(AppConstants.CountryCodeName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

//                btnMenuClose.setVisibility(View.GONE);
//                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrCountryCodeList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCountryCodeList.clear();
        if (charText.length() == 0) {
            arrCountryCodeList.addAll(arrFilter);
        }
        else
        {
            for (beanCountryCode wp : arrFilter)
            {
                String ProductName=wp.getCountry_code();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrCountryCodeList.add(wp);
                }
            }
        }

        if(arrCountryCodeList.size() == 0)
        {
            arrCountryCodeList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
