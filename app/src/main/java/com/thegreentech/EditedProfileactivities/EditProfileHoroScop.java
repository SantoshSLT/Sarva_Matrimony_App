package com.thegreentech.EditedProfileactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.thegreentech.MenuProfileEdit;
import com.thegreentech.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter2;
import Models.beanGeneralData;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AnimUtils;
import utills.AppConstants;
import utills.NetworkConnection;

public class EditProfileHoroScop extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    LinearLayout linGeneralView;
    RecyclerView rvGeneralView;
    Button btnUpdate;
    Button btnConfirm;
    LinearLayout Slidingpage, llDoshType;
    RelativeLayout SlidingDrawer;
    ImageView btnMenuClose;

    EditText edtHaveDosh, edtDoshType, edtStar,
            edtRassi, edtBirthTime, edtBirthPlace;
    String DoshType_val = "";
    ArrayList<DoshType> listDosh = new ArrayList<>();
    DoshAdapter doshAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog progresDialog;
    SharedPreferences prefUpdate;
    String matri_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_horo_scop);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(EditProfileHoroScop.this);
        matri_id = prefUpdate.getString("matri_id", "");

        init();
        SlidingMenu();
        Onclick();
        if (NetworkConnection.hasConnection(EditProfileHoroScop.this)){
            getPfileDetail(matri_id);
        }else
        {
            AppConstants.CheckConnection( EditProfileHoroScop.this);
        }

    }

    public void init() {

        btnBack = findViewById(R.id.btnBack);
        textviewHeaderText = findViewById(R.id.textviewHeaderText);
        textviewSignUp = findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("Update HoroScope");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);

        rvGeneralView = findViewById(R.id.rvGeneralView);
        linGeneralView = findViewById(R.id.linGeneralView);

        Slidingpage = findViewById(R.id.sliding_page);
        SlidingDrawer = findViewById(R.id.sliding_drawer);
        btnMenuClose = findViewById(R.id.btnMenuClose);
        Slidingpage.setVisibility(View.GONE);
        btnMenuClose.setVisibility(View.GONE);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnUpdate = findViewById(R.id.btnUpdate);
        edtHaveDosh = (EditText) findViewById(R.id.edtHaveDosh);
        edtDoshType = (EditText) findViewById(R.id.edtDoshType);
        llDoshType = findViewById(R.id.llDoshType);
        edtStar = (EditText) findViewById(R.id.edtStar);
        edtRassi = (EditText) findViewById(R.id.edtRassi);
        edtBirthTime = (EditText) findViewById(R.id.edtBirthTime);// edit
        edtBirthPlace = (EditText) findViewById(R.id.edtBirthPlace);// edit
    }

    public void Onclick() {
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HaveDosh = edtHaveDosh.getText().toString().trim();
                String DoshType = edtDoshType.getText().toString().trim();
                String Star = edtStar.getText().toString().trim();
                String Rassi = edtRassi.getText().toString().trim();
                String BirthTime = edtBirthTime.getText().toString().trim();
                String BirthPlace = edtBirthPlace.getText().toString().trim();

                if (hasData(HaveDosh)&& hasData(Star)&& hasData(Rassi) && hasData(BirthTime) && hasData(BirthPlace))
                {
                    if(HaveDosh.equalsIgnoreCase("Yes") && DoshType.equalsIgnoreCase(""))
                    {
                        Toast.makeText(EditProfileHoroScop.this,"Please select Dosh type.",Toast.LENGTH_LONG).show();
                    }else {
                        updateHoroScope(matri_id,HaveDosh,Star,Rassi,
                                BirthTime,BirthPlace,DoshType);
                    }
                }
                else {
                    Toast.makeText(EditProfileHoroScop.this,"Please enter all required fields. ",Toast.LENGTH_LONG).show();

                }
            }
        });

        edtBirthTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setDateTimes();
            }
        });

        edtHaveDosh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHaveDosh.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileHoroScop.this, getResources().getStringArray(R.array.arr_manglik),SlidingDrawer,Slidingpage,btnMenuClose,edtHaveDosh);
                rvGeneralView.setAdapter(generalAdapter);


            }
        });

        edtHaveDosh.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                String strSelectedType=edtHaveDosh.getText().toString().trim();
                if(strSelectedType.equalsIgnoreCase("Yes"))
                {
                    llDoshType.setVisibility(View.VISIBLE);
                }else
                {
                    llDoshType.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

            }
        });
        edtDoshType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisibleSlidingDrower();
                edtDoshType.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_manglik_type);

                for (int i=0; i < arr_diet.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_diet[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(EditProfileHoroScop.this,"Dosh_Type",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileHoroScop.this, getResources().getStringArray(R.array.arr_diet_2),SlidingDrawer,Slidingpage,btnMenuClose,edtDoshType);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });

        edtStar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtStar.setError(null);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileHoroScop.this, getResources().getStringArray(R.array.arr_star),SlidingDrawer,Slidingpage,btnMenuClose,edtStar);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });
        edtRassi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtRassi.setError(null);

                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(EditProfileHoroScop.this, getResources().getStringArray(R.array.arr_raasi_moon_sign),SlidingDrawer,Slidingpage,btnMenuClose,edtRassi);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });


    }


    public  boolean hasData(String text)
    {

        if (text == null || text.length() == 0)
            return false;

        return true;
    }

    public void VisibleSlidingDrower() {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(EditProfileHoroScop.this, SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }


    public void GonelidingDrower() {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(EditProfileHoroScop.this, SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu() {

        rvGeneralView.setLayoutManager(new LinearLayoutManager(this));
        rvGeneralView.setHasFixedSize(true);

        btnMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GonelidingDrower();

            }
        });

    }

    public void setDateTimes()
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                String time11 =  showTime(hourOfDay, minute);
                edtBirthTime.setText(time11);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    public String showTime(int hour, int min)
    {
        String format="";

        if (hour == 0)
        {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12)
        {
            format = "PM";
        } else if (hour > 12)
        {
            hour -= 12;
            format = "PM";
        } else
        {
            format = "AM";
        }

        NumberFormat ff = new DecimalFormat("00");

        String times= String.valueOf(new StringBuilder().append(ff.format(hour)).append(":").append(ff.format(min)).append(" ").append(format));

        return times;
    }


    private void getPfileDetail(String strMatriId) {


        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL = AppConstants.MAIN_URL + "profile.php";
                Log.e("View Profile", "== " + URL);

                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== " + (nameValuePairList.toString().trim().replaceAll(",", "&")));
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee) //UnsupportedEncodingException
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.e("Profile Id", "==" + result);

                String finalresult = "";
                try {

                    finalresult = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);

                    JSONObject obj = new JSONObject(finalresult);


                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);


                              /*  "dosh": "No",
                                   "manglik": "",*/

                                edtHaveDosh.setText(resItem.getString("dosh"));

                                if (resItem.getString("dosh").equalsIgnoreCase("yes")) {
                                    llDoshType.setVisibility(View.VISIBLE);

                                    edtDoshType.setText(resItem.getString("manglik"));
                                } else
                                    llDoshType.setVisibility(View.GONE);
                                edtStar.setText(resItem.getString("star"));
                                edtRassi.setText(resItem.getString("moonsign"));
                                edtBirthTime.setText(resItem.getString("birthtime"));
                                edtBirthPlace.setText(resItem.getString("birthplace"));
                            }

                        }

                    } else {
                        String msgError = obj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileHoroScop.this);
                        builder.setMessage("" + msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                } catch (Exception t) {
                   Log.e("exceptionHoroscope", t.getMessage());

                }


            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strMatriId);
    }


    public void updateHoroScope(String MatriID,String haveDosh, String star,String rassi, String birthtime, String birthPlace,String doseType){
        progresDialog= new ProgressDialog(EditProfileHoroScop.this);
        progresDialog.setCancelable(false);
        progresDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progresDialog.setIndeterminate(true);
        progresDialog.show();

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String pMatriID= params[0];
                String PHaveDosh= params[1];
                String PStar= params[2];
                String PRassi= params[3];
                String PBirthTime= params[4];
                String PBirthPlace= params[5];
                String DoseType= params[6];

                HttpClient httpClient = new DefaultHttpClient();
                String URL= AppConstants.MAIN_URL +"edit_horoscope_details.php";
                Log.e("URL", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair matriIDPair = new BasicNameValuePair("matri_id", pMatriID);
                BasicNameValuePair HaveDoshPair = new BasicNameValuePair("dosh", PHaveDosh);
                BasicNameValuePair StarPair = new BasicNameValuePair("star", PStar);
                BasicNameValuePair RassiPair = new BasicNameValuePair("raasi", PRassi);
                BasicNameValuePair BirthTimePair = new BasicNameValuePair("birth_time", PBirthTime);
                BasicNameValuePair BirthPlacePair = new BasicNameValuePair("birth_place", PBirthPlace);
                BasicNameValuePair DoshTypePair = new BasicNameValuePair("manglik", DoseType);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(matriIDPair);
                nameValuePairList.add(HaveDoshPair);
                nameValuePairList.add(StarPair);
                nameValuePairList.add(RassiPair);
                nameValuePairList.add(BirthTimePair);
                nameValuePairList.add(BirthPlacePair);
                nameValuePairList.add(DoshTypePair);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (Exception uee)
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String Ressponce)
            {
                super.onPostExecute(Ressponce);
                progresDialog.dismiss();
                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    // JSONObject responseData = responseObj.getJSONObject("responseData");

                    String status=responseObj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        Intent intLogin= new Intent(EditProfileHoroScop.this, MenuProfileEdit.class);
                        startActivity(intLogin);
                        finish();
                    }else
                    {
                        String msgError=responseObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileHoroScop.this);
                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    responseObj = null;

                } catch (Exception e) {
                    Log.e("exceptionhoro", e.getMessage());
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(MatriID,haveDosh,star,rassi,birthtime,birthPlace,doseType);

    }



    class DoshAdapter extends RecyclerView.Adapter<DoshAdapter.ViewHolder> {

        Context context;
        ArrayList<DoshType> listDoshType;
        private ArrayList<DoshType> arrFilter;
        LinearLayout Slidingpage;
        RelativeLayout SlidingDrawer;
        ImageView btnMenuClose;
        EditText edtGeneral;
        Button btnConfirm;


        public DoshAdapter(Context context, ArrayList<DoshType> fields_list, RelativeLayout SlidingDrawer, LinearLayout Slidingpage,
                           ImageView btnMenuClose, EditText edtGeneral, Button btnConfirm) {
            this.context = context;
            this.listDoshType = fields_list;
            this.SlidingDrawer = SlidingDrawer;
            this.Slidingpage = Slidingpage;
            this.btnMenuClose = btnMenuClose;
            this.edtGeneral = edtGeneral;
            this.btnConfirm = btnConfirm;
            // this.arrFilter = new ArrayList<DoshType>();
            //   this.arrFilter.addAll(listDoshType);
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public DoshAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dosh_list, parent, false);
            return new DoshAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final DoshAdapter.ViewHolder holder, final int position) {

            DoshType type = listDoshType.get(position);

            holder.tv_name.setText(type.getName());

            holder.chkSelected.setChecked(type.isSelected);
            holder.chkSelected.setTag(listDoshType.get(position));

            holder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    DoshType contact = (DoshType) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    Log.e("selected", cb.isChecked() + ",");
                    listDoshType.get(position).setSelected(cb.isChecked());
                    notifyDataSetChanged();

                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String Name = "";
                    String ID = "";

                    for (int i = 0; i < listDoshType.size(); i++) {
                        DoshType result_value = listDoshType.get(i);
                        if (result_value.isSelected == true) {
                            if (Name.equalsIgnoreCase("")) {
                                Name = result_value.getName().toString() + ",";

                            } else {
                                DoshType_val += result_value.getName().toString() + ",";
                                Name += result_value.getName().toString() + ", ";
                            }
                            Log.e("type", DoshType_val);
                            Log.e("Name", Name);
                        }
                        notifyDataSetChanged();
                    }

                    String a = Name.substring(0, Name.lastIndexOf(","));
                    edtGeneral.setText(a);

                    SlidingDrawer.setVisibility(View.GONE);
                    SlidingDrawer.startAnimation(AppConstants.outToLeftAnimation());

                    Slidingpage.setVisibility(View.GONE);
                    Slidingpage.startAnimation(AppConstants.outToLeftAnimation());

                    btnMenuClose.setVisibility(View.GONE);
                    btnMenuClose.startAnimation(AppConstants.outToLeftAnimation());

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.cardView.getWindowToken(), 0);

                    //Toast.makeText(context,"Selected Students: " + ReligionsName, Toast.LENGTH_LONG).show();
                }
            });


        }

        @Override
        public int getItemCount() {
            return listDoshType.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_name;
            public LinearLayout cardView;
            public CheckBox chkSelected;
            public View view_line;

            public ViewHolder(View itemView) {
                super(itemView);
                view_line = itemView.findViewById(R.id.view_line);
                view_line.setVisibility(View.GONE);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
                cardView = (LinearLayout) itemView.findViewById(R.id.cardView);
            }
        }


    }


    class DoshType {
        String name, id;
        private boolean isSelected;

        public DoshType() {
        }

        public DoshType(String name, boolean isSelected) {
            this.name = name;
            this.isSelected = isSelected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}