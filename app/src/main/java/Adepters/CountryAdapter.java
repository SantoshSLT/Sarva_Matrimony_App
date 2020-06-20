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

import Models.beanCountries;
import utills.AppConstants;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCountries> arrCountryList;
    private ArrayList<beanCountries> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtCountry;

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

    public CountryAdapter(Context context, ArrayList<beanCountries> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage, EditText edtCountryCode) {

        this.context = context;
        this.arrCountryList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.edtCountry=edtCountryCode;
        this.arrFilter = new ArrayList<beanCountries>();
        this.arrFilter.addAll(arrCountryList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCountries objCountry = arrCountryList.get(position);

        if (objCountry.getCountry_name() != null)
        {
            holder.tv_name.setText(objCountry.getCountry_name());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.CountryName=objCountry.getCountry_name();
                AppConstants.CountryId=objCountry.getCountry_id();

                edtCountry.setText(AppConstants.CountryName);

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
        return arrCountryList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCountryList.clear();
        if (charText.length() == 0) {
            arrCountryList.addAll(arrFilter);
        }
        else
        {
            for (beanCountries wp : arrFilter)
            {
                String ProductName=wp.getCountry_name();
                String ProductId = wp.getCountry_id();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText) || ProductId.toLowerCase(Locale.getDefault()).contains(charText) )
                {
                    arrCountryList.add(wp);
                }
            }
        }

        if(arrCountryList.size() == 0)
        {
            arrCountryList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
