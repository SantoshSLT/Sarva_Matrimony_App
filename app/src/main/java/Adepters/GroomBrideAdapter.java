package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import Models.GroomBride;
import Models.beanCountries;
import utills.AppConstants;

public class GroomBrideAdapter extends RecyclerView.Adapter<GroomBrideAdapter.MyViewHolder> {

    public Context context;
    ArrayList<GroomBride> arrCountryList;
    private ArrayList<GroomBride> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtBrideId;
    EditText edtBrideName;


    public GroomBrideAdapter(Context context, ArrayList<GroomBride> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage, EditText edtBrideId,EditText edtBrideName) {

        this.context = context;
        this.arrCountryList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.edtBrideId=edtBrideId;
        this.edtBrideName = edtBrideName;
        this.arrFilter = new ArrayList<GroomBride>();
        this.arrFilter.addAll(arrCountryList);
    }


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
    public GroomBrideAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row, parent, false);
        return new GroomBrideAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final GroomBrideAdapter.MyViewHolder holder, final int position)
    {
        final GroomBride objCountry = arrCountryList.get(position);

        Log.e("nameedfewfef",objCountry.getName());
        if (objCountry.getName() != null)

        {
            holder.tv_name.setText(objCountry.getName() + " (" + objCountry.getId()+ ") ");
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                AppConstants.BrideName=objCountry.getName();
                AppConstants.BrideID=objCountry.getId();

                Log.e("app_id",AppConstants.BrideID);
                Log.e("app_name",AppConstants.BrideName);

                edtBrideId.setText(AppConstants.BrideID);
                edtBrideName.setText(AppConstants.BrideName);

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
            for (GroomBride wp : arrFilter)
            {
                String ProductName=wp.getName();
                String ProductId = wp.getId();

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
