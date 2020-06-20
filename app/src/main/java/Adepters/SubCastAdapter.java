package Adepters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Locale;

import Models.SubCast;
import Models.beanMotherTongue;
import utills.AppConstants;

public class SubCastAdapter extends RecyclerView.Adapter<SubCastAdapter.MyViewHolder> {

    public Context context;
    ArrayList<SubCast> subCastArrayList;
    private ArrayList<SubCast> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    //ImageView btnMenuClose;
    EditText edtMotherTongue;


    public SubCastAdapter(Context context, ArrayList<SubCast> fields_list, RelativeLayout SlidingDrawer, LinearLayout Slidingpage,
                          EditText edtMotherTongue) {

        this.context = context;
        this.subCastArrayList = fields_list;
        this.SlidingDrawer = SlidingDrawer;
        this.Slidingpage = Slidingpage;

        this.edtMotherTongue = edtMotherTongue;
        this.arrFilter = new ArrayList<SubCast>();
        this.arrFilter.addAll(subCastArrayList);
        Log.e("adapterLisy",subCastArrayList.size() +"");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public LinearLayout cardView;

        public MyViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }


    @Override
    public SubCastAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Log.e("adapterlist_binder",this.subCastArrayList.size() +"");
        final SubCast objCountry = subCastArrayList.get(position);


        if (objCountry.getSB_name() != null) {
            holder.tv_name.setText(objCountry.getSB_name());

        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.SubCasteName = objCountry.getSB_name();
                AppConstants.SubCasteID = objCountry.getSB_id();

                Log.e("subcasteId_adapter",AppConstants.SubCasteID);
                edtMotherTongue.setText(AppConstants.SubCasteName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

//                btnMenuClose.setVisibility(View.GONE);
//                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());
////
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

            }
        });
    }



    @Override
    public int getItemCount() {
        return subCastArrayList.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        subCastArrayList.clear();
        if (charText.length() == 0) {
            subCastArrayList.addAll(arrFilter);
        } else {
            for (SubCast wp : arrFilter) {
                String ProductName = wp.getSB_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    subCastArrayList.add(wp);
                }
            }
        }

        if (subCastArrayList.size() == 0) {
            subCastArrayList.addAll(arrFilter);
        } else {
            notifyDataSetChanged();
        }
    }
}
