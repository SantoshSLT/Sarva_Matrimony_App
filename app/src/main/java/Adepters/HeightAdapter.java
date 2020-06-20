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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Models.HeightBean;
import utills.AppConstants;

public class HeightAdapter extends RecyclerView.Adapter<HeightAdapter.MyViewHolder> {

    public Context context;
   ArrayList<HeightBean> arrGeneralList;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtGeneral;
    String TypeAge;

    public HeightAdapter(Context context,String type_age,ArrayList<HeightBean> arrGeneralList, LinearLayout slidingpage, RelativeLayout slidingDrawer, ImageView btnMenuClose, EditText edtGeneral) {
        this.context = context;
        this.arrGeneralList = arrGeneralList;
        Slidingpage = slidingpage;
        SlidingDrawer = slidingDrawer;
        this.btnMenuClose = btnMenuClose;
        this.edtGeneral = edtGeneral;
        this.TypeAge = type_age;
    }

    @NonNull
    @Override
    public HeightAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HeightAdapter.MyViewHolder holder, final int position) {

        final HeightBean bean = arrGeneralList.get(position);



        holder.tv_name.setText(bean.getName());




              holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                edtGeneral.setText(bean.getName());

                if (TypeAge.equalsIgnoreCase("agefrom"))
                {
                    AppConstants.HeightFromID="";
                    AppConstants.HeightFromID=bean.getID();
                    Log.e("HeightFromID",AppConstants.HeightFromID);
                }
                else if (TypeAge.equalsIgnoreCase("ageto"))
                {
                    AppConstants.HeightToID ="";
                    AppConstants.HeightToID= bean.getID();
                    Log.e("HeightToID",AppConstants.HeightToID);
                }
                else if (TypeAge.equalsIgnoreCase("hieght"))
                {
                    AppConstants.HeightID = "";
                    AppConstants.HeightID = bean.getID();
                    Log.e("HeightID ",AppConstants.HeightID);
                }


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
        return arrGeneralList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public LinearLayout cardView;

        public MyViewHolder(View view)
        {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }
}
