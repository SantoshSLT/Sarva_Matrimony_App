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

import Models.beanState;
import utills.AppConstants;

public class StateMultiSelectionAdapter extends RecyclerView.Adapter<StateMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanState> arrStateList;
    private ArrayList<beanState> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtState;
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

    public StateMultiSelectionAdapter(Context context, ArrayList<beanState> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                      ImageView btnMenuClose, EditText edtStateCode,Button btnConfirm) {

        this.context = context;
        this.arrStateList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtState=edtStateCode;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanState>();
        this.arrFilter.addAll(arrStateList);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanState objState = arrStateList.get(position);

        if (objState.getState_name() != null)
        {
            holder.tv_name.setText(objState.getState_name());
        }

        holder.chkSelected.setChecked(objState.isSelected());
        holder.chkSelected.setTag(arrStateList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanState contact = (beanState) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrStateList.get(position).setSelected(cb.isChecked());

              /*  Toast.makeText( v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrStateList.size(); i++)
                {
                    beanState singleStudent = arrStateList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getState_name().toString();
                            ReligionsID = singleStudent.getState_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + ", " + singleStudent.getState_name().toString();
                            ReligionsID = ReligionsID + ", " + singleStudent.getState_id().toString();
                        }

                    }

                }

                AppConstants.StateName=ReligionsName;
                AppConstants.StateId=ReligionsID;

                edtState.setText(AppConstants.StateName);

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
        return arrStateList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrStateList.clear();
        if (charText.length() == 0) {
            arrStateList.addAll(arrFilter);
        }
        else
        {
            for (beanState wp : arrFilter)
            {
                String ProductName=wp.getState_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrStateList.add(wp);
                }
            }
        }

        if(arrStateList.size() == 0)
        {
            arrStateList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
