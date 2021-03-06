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

import Models.beanCountries;
import utills.AppConstants;

public class CountryMultiSelectionAdapter extends RecyclerView.Adapter<CountryMultiSelectionAdapter.MyViewHolder> {

    public Context context;
    ArrayList<beanCountries> arrCountryList;
    private ArrayList<beanCountries> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtCountry;
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

    public CountryMultiSelectionAdapter(Context context, ArrayList<beanCountries> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                                        ImageView btnMenuClose, EditText edtCountryCode, Button btnConfirm) {

        this.context = context;
        this.arrCountryList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtCountry=edtCountryCode;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanCountries>();
        this.arrFilter.addAll(arrCountryList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanCountries objCountry = arrCountryList.get(position);

        if (objCountry.getCountry_name() != null)
        {
            holder.tv_name.setText(objCountry.getCountry_name());
        }

        holder.chkSelected.setChecked(objCountry.isSelected());
        // holder.chkSelected.setTag(position);
        holder.chkSelected.setTag(arrCountryList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                beanCountries contact = (beanCountries) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrCountryList.get(position).setSelected(cb.isChecked());

              /*  Toast.makeText( v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();*/
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ReligionsName = "";
                String ReligionsID = "";

                for (int i = 0; i < arrCountryList.size(); i++)
                {
                    beanCountries singleStudent = arrCountryList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(ReligionsName.equalsIgnoreCase(""))
                        {
                            ReligionsName = singleStudent.getCountry_name().toString();
                            ReligionsID = singleStudent.getCountry_id().toString();
                        }else
                        {
                            ReligionsName = ReligionsName + "," + singleStudent.getCountry_name().toString();
                            ReligionsID = ReligionsID + "," + singleStudent.getCountry_id().toString();
                        }

                    }

                }

                AppConstants.CountryName=ReligionsName;
                AppConstants.CountryId=ReligionsID;

                edtCountry.setText(AppConstants.CountryName);

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
        return arrCountryList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrCountryList.clear();
        if (charText.length() == 0) {
            arrCountryList.addAll(arrFilter);
        }
        else
        {
            for (beanCountries wp : arrFilter)
            {
                String ProductName=wp.getCountry_name();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrCountryList.add(wp);
                }
            }
        }

        if(arrCountryList.size() == 0)
        {
            arrCountryList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
