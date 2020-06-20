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

import Models.beanEducation;
import utills.AppConstants;

public class EducationsMultiSelectionAdapter extends RecyclerView.Adapter<EducationsMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanEducation> arrEducationList;
    private ArrayList<beanEducation> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtEducation;
    Button btnConfirm;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;
        public LinearLayout cardView;
        public CheckBox chkSelected ;

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

    public EducationsMultiSelectionAdapter(Context context, ArrayList<beanEducation> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                           ImageView btnMenuClose, EditText edtEducationCode,Button btnConfirm) {

        this.context = context;
        this.arrEducationList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtEducation=edtEducationCode;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanEducation>();
        this.arrFilter.addAll(arrEducationList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanEducation objEducation = arrEducationList.get(position);

        if (objEducation.getEducation_name() != null)
        {
            holder.tv_name.setText(objEducation.getEducation_name());
        }

        holder.chkSelected.setChecked(objEducation.isSelected());
        holder.chkSelected.setTag(arrEducationList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanEducation contact = (beanEducation) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrEducationList.get(position).setSelected(cb.isChecked());

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrEducationList.size(); i++)
                {
                    beanEducation singleStudent = arrEducationList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getEducation_name().toString();
                            ReligionsID = singleStudent.getEducation_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + "," + singleStudent.getEducation_name().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getEducation_id().toString();
                        }

                    }

                }

                AppConstants.EducationName=ReligionsName;
                AppConstants.EducationId=ReligionsID;

                edtEducation.setText(AppConstants.EducationName);

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
                AppConstants.EducationName=objEducation.getEducation_name();
                AppConstants.EducationId=objEducation.getEducation_id();

                edtEducation.setText(AppConstants.EducationName);

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
        return arrEducationList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrEducationList.clear();
        if (charText.length() == 0) {
            arrEducationList.addAll(arrFilter);
        }
        else
        {
            for (beanEducation wp : arrFilter)
            {
                String ProductName=wp.getEducation_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrEducationList.add(wp);
                }
            }
        }

        if(arrEducationList.size() == 0)
        {
            arrEducationList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
