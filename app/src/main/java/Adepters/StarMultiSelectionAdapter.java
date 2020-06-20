package Adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import Models.beanGenralModel;
import utills.AppConstants;

public class StarMultiSelectionAdapter extends RecyclerView.Adapter<StarMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanGenralModel> arrMaritalStatusList;
    private ArrayList<beanGenralModel> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    Button btnConfirm;
    ImageView btnMenuClose;
    EditText edtStar;

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

    public StarMultiSelectionAdapter(Context context, ArrayList<beanGenralModel> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                     ImageView btnMenuClose, EditText edtStar, Button btnConfirm)
    {

        this.context = context;
        this.arrMaritalStatusList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtStar=edtStar;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanGenralModel>();
        this.arrFilter.addAll(arrMaritalStatusList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanGenralModel objCountry = arrMaritalStatusList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
            Log.d("TAG",objCountry.getName());
        }

        holder.chkSelected.setChecked(objCountry.isSelected());
        // holder.chkSelected.setTag(position);
        holder.chkSelected.setTag(arrMaritalStatusList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanGenralModel contact = (beanGenralModel) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrMaritalStatusList.get(position).setSelected(cb.isChecked());

              /*  Toast.makeText( v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";

                for (int i = 0; i < arrMaritalStatusList.size(); i++)
                {
                    beanGenralModel singleStudent = arrMaritalStatusList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getName().toString();
                            Log.d("TAG",ReligionsName);


                        }else
                        {
                            ReligionsName = ReligionsName + ", " + singleStudent.getName().toString();
                            Log.d("TAG",ReligionsName);

                        }

                    }

                }

                AppConstants.GeneralName=ReligionsName;
                Log.d("TAG",AppConstants.GeneralName);


                edtStar.setText(AppConstants.GeneralName);

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

       /* holder.cardView.setOnClickListener(new View.OnClickListener()
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

                btnMenuClose.setVisibility(View.GONE);
                btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return arrMaritalStatusList.size();
    }


    /*public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCasteList.clear();
        if (charText.length() == 0) {
            arrCasteList.addAll(arrFilter);
        }
        else
        {
            for (beanGenralModel wp : arrFilter)
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
    }*/

}
