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
import java.util.Locale;

import Models.beanReligion;
import utills.AppConstants;

public class ReligionMultiSelectionAdapter extends RecyclerView.Adapter<ReligionMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanReligion> arrReligionList;
    private ArrayList<beanReligion> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtReligion;
    Button btnConfirm;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;
        public CheckBox chkSelected;

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

    public ReligionMultiSelectionAdapter(Context context, ArrayList<beanReligion> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                         ImageView btnMenuClose, EditText edtReligion, Button btnConfirm) {

        this.context = context;
        this.arrReligionList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtReligion=edtReligion;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanReligion>();
        this.arrFilter.addAll(arrReligionList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanReligion objCountry = arrReligionList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
        }

        holder.chkSelected.setChecked(objCountry.isSelected());
       // holder.chkSelected.setTag(position);
        holder.chkSelected.setTag(arrReligionList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanReligion contact = (beanReligion) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrReligionList.get(position).setSelected(cb.isChecked());

              /*  Toast.makeText( v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrReligionList.size(); i++)
                {
                    beanReligion singleStudent = arrReligionList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getName().toString();
                            ReligionsID = singleStudent.getReligion_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + "," + singleStudent.getName().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getReligion_id().toString();
                        }

                    }

                }

                AppConstants.ReligionName=ReligionsName;
                AppConstants.ReligionId=ReligionsID;

                Log.e("id",AppConstants.ReligionId);
                Log.e("id",AppConstants.ReligionName);


                edtReligion.setText(AppConstants.ReligionName);

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

    }

    @Override
    public int getItemCount() {
        return arrReligionList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrReligionList.clear();
        if (charText.length() == 0)
        {
            arrReligionList.addAll(arrFilter);
        }
        else
        {
            for (beanReligion wp : arrFilter)
            {
                String ProductName=wp.getName();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrReligionList.add(wp);
                }
            }
        }
        if(arrReligionList.size() == 0)
        {
            arrReligionList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }

    }

}
