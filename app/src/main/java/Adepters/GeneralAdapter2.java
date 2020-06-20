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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import Models.beanGeneralData;
import utills.AppConstants;

public class GeneralAdapter2 extends RecyclerView.Adapter<GeneralAdapter2.MyViewHolder> {

    public Context context;
    ArrayList<beanGeneralData> arrGeneralDataList;
    private ArrayList<beanGeneralData> arrFilter;
    LinearLayout Slidingpage;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;
    EditText edtGeneral;
    Button btnConfirm;
    String Type_Feild;


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

    public GeneralAdapter2(Context context,String FeildType, ArrayList<beanGeneralData> fields_list, RelativeLayout SlidingDrawer, LinearLayout  Slidingpage,
                           ImageView btnMenuClose, EditText edtGeneral, Button btnConfirm) {

        this.context = context;
        this.arrGeneralDataList = fields_list;
        this.SlidingDrawer=SlidingDrawer;
        this.Slidingpage=Slidingpage;
        this.btnMenuClose=btnMenuClose;
        this.edtGeneral=edtGeneral;
        this.btnConfirm=btnConfirm;
        this.arrFilter = new ArrayList<beanGeneralData>();
        this.arrFilter.addAll(arrGeneralDataList);
        this.Type_Feild = FeildType;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final beanGeneralData objCountry = arrGeneralDataList.get(position);

        if (objCountry.getName() != null)
        {
            holder.tv_name.setText(objCountry.getName());
        }

        holder.chkSelected.setChecked(objCountry.isSelected());
        holder.chkSelected.setTag(arrGeneralDataList.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                    final CheckBox cb = (CheckBox) v;
                    final beanGeneralData contact = (beanGeneralData) cb.getTag();

                    contact.setSelected(cb.isChecked());
                if (contact.getName().equalsIgnoreCase("Doesn't Matter")
                        && contact.isSelected() == true)
                {
                    for (beanGeneralData data : arrGeneralDataList)
                    {
                        data.setSelected(false);
                        if (data.equals("Doesn't Matter")){
                            data.isSelected();
                            notifyDataSetChanged();
                        }
                        else {

                        }
                        notifyDataSetChanged();
                    }
                    notifyDataSetChanged();
                }

                else if (contact.getName().equalsIgnoreCase("Any")
                        && contact.isSelected() == true)
                {
                    for (beanGeneralData data : arrGeneralDataList)
                    {
                        data.setSelected(false);
                        if (data.equals("Any")){
                            data.isSelected();
                            notifyDataSetChanged();
                        }
                        else {

                        }
                        notifyDataSetChanged();
                    }
                    notifyDataSetChanged();
                }

               /* else if (!contact.getName().equalsIgnoreCase("Doesn't Matter")
                        &&  contact.isSelected()==true)
                {

                }
                */

                arrGeneralDataList.get(position).setSelected(cb.isChecked());
            }
        });




        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Name = "";
                String ID = "";

                for (int i = 0; i < arrGeneralDataList.size(); i++)
                {
                    beanGeneralData singleStudent = arrGeneralDataList.get(i);

                    if (singleStudent.isSelected() == true)
                    {
                        if(Name.equalsIgnoreCase(""))
                        {
                            Name = singleStudent.getName().toString();
                            ID = singleStudent.getId().toString();
                        }else
                        {
                            Name = Name + "," + singleStudent.getName().toString();
                            ID = ID + "," + singleStudent.getId().toString();
                        }

                    }

                }

               /* AppConstants.MotherTongueName=ReligionsName;
                AppConstants.MotherTongueId=ReligionsID;
*/

               if (Type_Feild.equalsIgnoreCase("MaritalStatus")) {
                   Log.e("MaritalStatus", ID);
                   AppConstants.MaritalStatusName = Name;
                   Log.e("AppMaritalStatusID====", AppConstants.MaritalStatusName);
               }

                if (Type_Feild.equalsIgnoreCase("eating_Habits")) {
                    Log.e("EatingHabit", ID);
                    AppConstants.EatingHabitNAme = Name;
                    Log.e("AppEatingHabitID====", AppConstants.EatingHabitNAme);
                }
                if (Type_Feild.equalsIgnoreCase("Smoking_Habits")) {
                    Log.e("SmokingHabits", ID);
                    AppConstants.SmokingHabitsNAME = Name;
                    Log.e("AppSmokingHabitsID====", AppConstants.SmokingHabitsNAME);
                }
                if (Type_Feild.equalsIgnoreCase("Drinking_Habits")) {
                    Log.e("Drinking_Habits", ID);
                    AppConstants.DrinkingNAME = Name;
                    Log.e("AppDrinkingHabitsID====", AppConstants.DrinkingNAME);
                }

                if (Type_Feild.equalsIgnoreCase("Star")) {
                    Log.e("Drinking_Star", ID);
                    AppConstants.StarNAME = Name;
                    Log.e("AppStarID====", AppConstants.StarNAME);
                }


                if (Type_Feild.equalsIgnoreCase("Dosh_Type")) {
                    Log.e("Dosh_Type", ID);
                    AppConstants.DosTypeNAME = Name;
                    Log.e("AppDosh_TypeID====", AppConstants.DosTypeNAME);
                }
                if (Type_Feild.equalsIgnoreCase("Raasi")) {
                    Log.e("Raasi", ID);
                    AppConstants.Raasi = Name;
                    Log.e("AppDosh_TypeID====", AppConstants.DosTypeNAME);
                }


                edtGeneral.setText(Name);

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
        return arrGeneralDataList.size();
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        arrGeneralDataList.clear();
        if (charText.length() == 0) {
            arrGeneralDataList.addAll(arrFilter);
        }
        else
        {
            for (beanGeneralData wp : arrFilter)
            {
                String ProductName=wp.getName();

                if (ProductName.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    arrGeneralDataList.add(wp);
                }
            }
        }

        if(arrGeneralDataList.size() == 0)
        {
            arrGeneralDataList.addAll(arrFilter);
        }else
        {
            notifyDataSetChanged();
        }
    }

}
