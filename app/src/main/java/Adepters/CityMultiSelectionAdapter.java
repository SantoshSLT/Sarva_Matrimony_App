package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thegreentech.R;

import java.util.ArrayList;
import java.util.Locale;

import Models.beanCity;
import utills.AppConstants;

public class CityMultiSelectionAdapter extends RecyclerView.Adapter<CityMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCity> arrCityList;
    private ArrayList<beanCity> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtCity;
    Button btnConfirm;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;
        public  CheckBox chkSelected;


        public MyViewHolder(View view)
        {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_name);
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected );
            cardView = (LinearLayout) view.findViewById(R.id.cardView);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_row1, parent, false);
        return new MyViewHolder(itemView);
    }

    public CityMultiSelectionAdapter(Context context, ArrayList<beanCity> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                     ImageView btnMenuClose, EditText edtCityCode,Button btnConfirm) {

        this.context = context;
        this.arrCityList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtCity=edtCityCode;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanCity>();
        this.arrFilter.addAll(arrCityList);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCity objCity = arrCityList.get(position);

        if (objCity.getCity_name() != null)
        {
            holder.tv_name.setText(objCity.getCity_name());
        }


        holder.chkSelected.setChecked(objCity.isSelected());
        holder.chkSelected.setTag(arrCityList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanCity contact = (beanCity) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrCityList.get(position).setSelected(cb.isChecked());
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrCityList.size(); i++)
                {
                    beanCity singleStudent = arrCityList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getCity_name().toString();
                            ReligionsID = singleStudent.getCity_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + "," + singleStudent.getCity_name().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getCity_id().toString();
                        }

                    }

                }

                AppConstants.CityName=ReligionsName;
                AppConstants.CityId=ReligionsID;

                edtCity.setText(AppConstants.CityName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                btnMenuClose.setVisibility(View.GONE);
                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

                //Toast.makeText(context,"Selected Students: " + ReligionsName, Toast.LENGTH_LONG).show();
            }
        });


/*
        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.CityName=objCity.getCity_name();
                AppConstants.CityId=objCity.getCity_id();

                edtCity.setText(AppConstants.CityName);

                SlidingDrawer.setVisibility(View.GONE);
                SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                Slidingpage.setVisibility(View.GONE);
                Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                btnMenuClose.setVisibility(View.GONE);
                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return arrCityList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCityList.clear();
        if (charText.length() == 0)
        {
            arrCityList.addAll(arrFilter);
        }
        else
        {
            for (beanCity wp : arrFilter)
            {
                String ProductName=wp.getCity_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrCityList.add(wp);
                }
            }
        }

        if(arrCityList.size() == 0)
        {
            arrCityList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
