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

import Models.beanProfileCreated;
import utills.AppConstants;

public class ProfileCreatedAdapter extends RecyclerView.Adapter<ProfileCreatedAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanProfileCreated> arrProfileCreatedList;
    private ArrayList<beanProfileCreated> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
  //  ImageView btnMenuClose;
    EditText edtProfileCreated;

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

    public ProfileCreatedAdapter(Context context, ArrayList<beanProfileCreated> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                 EditText edtProfileCreated) {

        this.context = context;
        this.arrProfileCreatedList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        //this.btnMenuClose=btnMenuClose;
        this.edtProfileCreated=edtProfileCreated;
        this.arrFilter = new ArrayList<beanProfileCreated>();
        this.arrFilter.addAll(arrProfileCreatedList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanProfileCreated objCountry = arrProfileCreatedList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
        }


        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strName=objCountry.getName();
                String strProfileCreatedID=objCountry.getProfile_id();

                edtProfileCreated.setText(strName);
                //SignUpStep1Activity. =strProfileCreated;

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

              //  btnMenuClose.setVisibility(View.GONE);
               // btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrProfileCreatedList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrProfileCreatedList.clear();
        if (charText.length() == 0) {
            arrProfileCreatedList.addAll(arrFilter);
        }
        else
        {
            for (beanProfileCreated wp : arrFilter)
            {
                String ProductName=wp.getName();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrProfileCreatedList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }

}
