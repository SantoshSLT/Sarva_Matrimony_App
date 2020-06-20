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

import Models.beanOccupation;
import utills.AppConstants;

public class OccupationMultiSelectionAdapter extends RecyclerView.Adapter<OccupationMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanOccupation> arrOccupationList;
    private ArrayList<beanOccupation> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtOccupation;
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

    public OccupationMultiSelectionAdapter(Context context, ArrayList<beanOccupation> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                           ImageView btnMenuClose, EditText edtOccupationCode,Button btnConfirm) {

        this.context = context;
        this.arrOccupationList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtOccupation=edtOccupationCode;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanOccupation>();
        this.arrFilter.addAll(arrOccupationList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanOccupation objOccupation = arrOccupationList.get(position);

        if (objOccupation.getOccupation_name() != null)
        {
            holder.tv_name.setText(objOccupation.getOccupation_name());
        }

        holder.chkSelected.setChecked(objOccupation.isSelected());
        holder.chkSelected.setTag(arrOccupationList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanOccupation contact = (beanOccupation) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrOccupationList.get(position).setSelected(cb.isChecked());
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrOccupationList.size(); i++)
                {
                    beanOccupation singleStudent = arrOccupationList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getOccupation_name().toString();
                            ReligionsID = singleStudent.getOccupation_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + ", " + singleStudent.getOccupation_name().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getOccupation_id().toString();
                        }

                    }

                }

                AppConstants.OccupationName=ReligionsName;
                AppConstants.OccupationID=ReligionsID;

                edtOccupation.setText(AppConstants.OccupationName);

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
                AppConstants.OccupationName=objOccupation.getOccupation_name();
                AppConstants.OccupationID=objOccupation.getOccupation_id();

                edtOccupation.setText(AppConstants.OccupationName);

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
        return arrOccupationList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrOccupationList.clear();
        if (charText.length() == 0) {
            arrOccupationList.addAll(arrFilter);
        }
        else
        {
            for (beanOccupation wp : arrFilter)
            {
                String ProductName=wp.getOccupation_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrOccupationList.add(wp);
                }
            }
        }

        if(arrOccupationList.size() == 0)
        {
            arrOccupationList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
