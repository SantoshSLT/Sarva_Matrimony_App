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

import Models.beanMotherTongue;
import utills.AppConstants;

public class MotherTongueMultiSelectionAdapter extends RecyclerView.Adapter<MotherTongueMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanMotherTongue> arrMotherTongueList;
    private ArrayList<beanMotherTongue> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtMotherTongue;
    Button btnConfirm;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;
        public  CheckBox chkSelected;

        public MyViewHolder(View view) {
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

    public MotherTongueMultiSelectionAdapter(Context context, ArrayList<beanMotherTongue> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                             ImageView btnMenuClose, EditText edtMotherTongue, Button btnConfirm) {

        this.context = context;
        this.arrMotherTongueList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtMotherTongue=edtMotherTongue;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanMotherTongue>();
        this.arrFilter.addAll(arrMotherTongueList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanMotherTongue objCountry = arrMotherTongueList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
        }

        holder.chkSelected.setChecked(objCountry.isSelected());
        holder.chkSelected.setTag(arrMotherTongueList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanMotherTongue contact = (beanMotherTongue) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrMotherTongueList.get(position).setSelected(cb.isChecked());

              /*  Toast.makeText( v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });

       /* holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppConstants.MotherTongueName=objCountry.getName();
                AppConstants.MotherTongueId=objCountry.getMotherT_id();

                edtMotherTongue.setText(AppConstants.MotherTongueName);

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


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrMotherTongueList.size(); i++)
                {
                    beanMotherTongue singleStudent = arrMotherTongueList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getName().toString();
                            ReligionsID = singleStudent.getMotherT_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + "," + singleStudent.getName().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getMotherT_id().toString();
                        }

                    }

                }

                AppConstants.MotherTongueName=ReligionsName;
                AppConstants.MotherTongueId=ReligionsID;

                edtMotherTongue.setText(AppConstants.MotherTongueName);

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
        return arrMotherTongueList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrMotherTongueList.clear();
        if (charText.length() == 0) {
            arrMotherTongueList.addAll(arrFilter);
        }
        else
        {
            for (beanMotherTongue wp : arrFilter)
            {
                String ProductName=wp.getName();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrMotherTongueList.add(wp);
                }
            }
        }

        if(arrMotherTongueList.size() == 0)
        {
            arrMotherTongueList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
