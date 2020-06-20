package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thegreentech.R;
import com.thegreentech.SavedSearchResultActivity;
import com.thegreentech.SearchResultActivity;
import com.thegreentech.SignUpStep1Activity;
import com.thegreentech.SignUpStep2Activity;
import com.thegreentech.SignUpStep3Activity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import Adepters.AnnualIncomeMultiSelectionAdapter;
import Adepters.CasteMultiSelectionAdapter;
import Adepters.CityMultiSelectionAdapter;
import Adepters.CountryMultiSelectionAdapter;
import Adepters.EducationsMultiSelectionAdapter;
import Adepters.GeneralAdapter;
import Adepters.GeneralAdapter2;
import Adepters.HeightAdapter;
import Adepters.ManglikMultiSelectionAdapter;
import Adepters.MaritalStausMultiSelectionAdapter;
import Adepters.MotherTongueAdapter;
import Adepters.MotherTongueMultiSelectionAdapter;
import Adepters.OccupationAdapter;
import Adepters.PhysicalStatusMultiSelectionAdapter;
import Adepters.ReligionMultiSelectionAdapter;
import Adepters.StarMultiSelectionAdapter;
import Adepters.StateMultiSelectionAdapter;
import Models.HeightBean;
import Models.beanCaste;
import Models.beanCity;
import Models.beanCountries;
import Models.beanEducation;
import Models.beanGeneralData;
import Models.beanGenralModel;
import Models.beanMotherTongue;
import Models.beanOccupation;
import Models.beanReligion;
import Models.beanSaveSearch;
import Models.beanState;
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
import utills.AppMethods;
import utills.NetworkConnection;

public class SearchByDetailFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    Context context;

    EditText edtAgeM,edtAgeF,edtHeightM,edtHeightF,edtMaritalStatus,edtPhysicalStatus,
			edtReligion,edtCaste,edtCountry,edtState,edtCity,
			edtHighestEducation,edtOccupation,edtAnnualIncome,edtMotherTongue, edtDiet,edtSmoking,edtDrinking,edtRassi,edtPhotoAvailable;
	ProgressBar progressBar1;

    RadioButton radioMale,radioFemale;
	Button btnSearchByMatriId,btnGeneralSearch,btnSaveSearch,btnSavedSearch;

	LinearLayout Slidingpage;
	RelativeLayout SlidingDrawer;
	ImageView btnMenuClose;

	LinearLayout linMotherTongue,linReligion,linCaste,linCountry,linState,linCity,linHighestEducation,linOccupation,linGeneralView,
			linAnnualIncome, linMaritalStatus, linPhysicalStatus, linStar, linManglik;
	EditText edtSearchMotherTongue,edtSearchReligion,edtSearchCaste,edtSearchCountry,edtSearchState,edtSearchCity,edtSearchHighestEducation,edtSearchOccupation;
	RecyclerView rvReligion,rvCaste,rvCountry,rvState,rvCity,rvHighestEducation,rvOccupation,rvGeneralView,
			rvAnnualIncome,rvMaritalStatus, rvPhysicalStatus,  rvManglik,rvMotherTongue;
	Button btnConfirm;

    ArrayList<beanReligion> arrReligion=null;
    ReligionMultiSelectionAdapter religionMultiSelectionAdapter=null;
    //ReligionAdapter religionAdapter=null;

    ArrayList<beanCaste> arrCaste=null;
    CasteMultiSelectionAdapter casteMultiSelectionAdapter=null;
    //CasteAdapter casteAdapter=null;

    ArrayList<beanCountries> arrCountry=null;
    CountryMultiSelectionAdapter countryMultiSelectionAdapter=null;
    //CountryAdapter countryAdapter=null;

    ArrayList<beanState> arrState= new ArrayList<beanState>();

    StateMultiSelectionAdapter stateMultiSelectionAdapter=null;
    //StateAdapter stateAdapter=null;

    ArrayList<beanCity> arrCity= new ArrayList<beanCity>();
    CityMultiSelectionAdapter cityMultiSelectionAdapter=null;
    //CityAdapter cityAdapter=null;

    ArrayList<beanEducation> arrEducation=null;
    EducationsMultiSelectionAdapter educationsMultiSelectionAdapter=null;
    //EducationsAdapter educationAdapter=null;

    ArrayList<beanOccupation> arrOccupation=null;
    OccupationAdapter occupationAdapter=null;

    ArrayList<beanGenralModel> arrMaritalStaus = new ArrayList<beanGenralModel>();
    MaritalStausMultiSelectionAdapter MaritalStatusAdapter=null;

    ArrayList<beanGenralModel> arrAnnualIncome = new ArrayList<beanGenralModel>();
    AnnualIncomeMultiSelectionAdapter annualIncomeMultiSelectionAdapter=null;

    ArrayList<beanGenralModel> arrPhysicalStatus = new ArrayList<beanGenralModel>();
    PhysicalStatusMultiSelectionAdapter physicalStatusMultiSelectionAdapter=null;

    ArrayList<beanGenralModel> arrManglik = new ArrayList<beanGenralModel>();
    ManglikMultiSelectionAdapter manglikMultiSelectionAdapter=null;

    ArrayList<beanMotherTongue> arrMotherTongue=new ArrayList<beanMotherTongue>();
    MotherTongueMultiSelectionAdapter motherTongueAdapter=null;

    SharedPreferences prefUpdate;
    String Genderr="";

    String matri_id="",AgeM="20",AgeF="30";

    private ArrayList<beanSaveSearch> arrSaveSearch;


    public SearchByDetailFragment() {
        // Required empty public constructor
    }


    public static SearchByDetailFragment newInstance(int sectionNumber)
    {
        SearchByDetailFragment fragment = new SearchByDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context= getActivity().getApplicationContext();
        view =  inflater.inflate(R.layout.fragment_search_by_detail, container, false);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
        Genderr=prefUpdate.getString("gender","");
        Log.e("genderr",Genderr);
        matri_id=prefUpdate.getString("matri_id","");
        init();
        onClicklist();
        SlidingMenu();
        getGeneralData();

        if (NetworkConnection.hasConnection(getActivity())) {
            getHighestEducationRequest();
            getHighestEducation();
        }else
        {
            AppConstants.CheckConnection(getActivity());
        }
        return view;
    }

    public void init(){
        progressBar1=(ProgressBar) view.findViewById(R.id.progressBar1);

		edtAgeM=(EditText)view.findViewById(R.id.edtAgeM);
		edtAgeF=(EditText)view.findViewById(R.id.edtAgeF);

		edtAgeM.setText(AgeM);
        edtAgeF.setText(AgeF);

		edtHeightM=(EditText)view.findViewById(R.id.edtHeightM);
		edtHeightF=(EditText)view.findViewById(R.id.edtHeightF);
		edtMaritalStatus=(EditText)view.findViewById(R.id.edtMaritalStatus);
		edtPhysicalStatus=(EditText)view.findViewById(R.id.edtPhysicalStatus);
		edtReligion=(EditText)view.findViewById(R.id.edtReligion);
		edtCaste=(EditText)view.findViewById(R.id.edtCaste);
		edtCountry=(EditText)view.findViewById(R.id.edtCountry);
		edtState=(EditText)view.findViewById(R.id.edtState);
		edtCity=(EditText)view.findViewById(R.id.edtCity);
		edtHighestEducation=(EditText)view.findViewById(R.id.edtHighestEducation);
		edtOccupation=(EditText)view.findViewById(R.id.edtOccupation);// edit
		edtAnnualIncome=(EditText)view.findViewById(R.id.edtAnnualIncome);// edit
        edtMotherTongue=(EditText)view.findViewById(R.id.edtMotherTongue);
        edtDiet=(EditText)view.findViewById(R.id.edtDiet);
        edtSmoking=(EditText)view.findViewById(R.id.edtSmoking);
        edtPhotoAvailable = view.findViewById(R.id.edtPhotoAvailable);
        edtDrinking=(EditText)view.findViewById(R.id.edtDrinking);
        edtRassi=(EditText)view.findViewById(R.id.edtRassi);


		radioMale=(RadioButton) view.findViewById(R.id.radioMale);
		radioFemale=(RadioButton)view.findViewById(R.id.radioFemale);

		btnSearchByMatriId=(Button)view.findViewById(R.id.btnSearchByMatriId);
		btnGeneralSearch=(Button)view.findViewById(R.id.btnGeneralSearch);
		btnSaveSearch=(Button)view.findViewById(R.id.btnSaveSearch);
		btnSavedSearch=(Button)view.findViewById(R.id.btnSavedSearch);

		Slidingpage=(LinearLayout)view.findViewById(R.id.sliding_page);
		SlidingDrawer=(RelativeLayout)view.findViewById(R.id.sliding_drawer);
		btnMenuClose=(ImageView)view.findViewById(R.id.btnMenuClose);
		Slidingpage.setVisibility(View.GONE);
		btnMenuClose.setVisibility(View.GONE);

        arrCaste = new ArrayList<>();
        arrEducation= new ArrayList<beanEducation>();
        arrSaveSearch = new ArrayList<beanSaveSearch>();




    }

    public  void  onClicklist(){

        btnGeneralSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //String Gender=
                AgeM=edtAgeM.getText().toString().trim();
                AgeF=edtAgeF.getText().toString().trim();
                String HeightM=edtHeightM.getText().toString().trim();
                String HeightF=edtHeightF.getText().toString().trim();
                String MaritalStatus=edtMaritalStatus.getText().toString().trim();
                String PhysicalStatus=edtPhysicalStatus.getText().toString().trim();
                String ReligionId= AppConstants.ReligionId;
                String CasteId=AppConstants.CasteId;
                String CountryId=AppConstants.CountryId;
                String StateId=AppConstants.StateId;
                String CityId=AppConstants.CityId;
                String HighestEducationId=AppConstants.EducationId;
                String OccupationId=AppConstants.OccupationID;
                String AnnualIncome=edtAnnualIncome.getText().toString().trim();
              //  String Manglik=edtManglik.getText().toString().trim();
                //String Star=edtStar.getText().toString().trim();
                final String Diet=edtDiet.getText().toString().trim();
                final String Smoking=edtSmoking.getText().toString().trim();
                final String Drinking=edtDrinking.getText().toString().trim();
                final String Rassi=edtRassi.getText().toString().trim();
                final  String Photo = edtPhotoAvailable.getText().toString().trim();

                Intent newIntent= new Intent(getActivity(), SearchResultActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra("SearchType","bydata");
                Log.e("genderrsaf",Genderr);
                newIntent.putExtra("Gender",Genderr);
                newIntent.putExtra("AgeM",""+AgeM);
                newIntent.putExtra("AgeF",""+AgeF);
                newIntent.putExtra("HeightM",""+HeightM);
                newIntent.putExtra("HeightF",""+HeightF);
                newIntent.putExtra("MaritalStatus",""+MaritalStatus);
                newIntent.putExtra("PhysicalStatus",""+PhysicalStatus);
                newIntent.putExtra("ReligionId",""+ReligionId);
                newIntent.putExtra("CasteId",""+CasteId);
                newIntent.putExtra("CountryId",""+CountryId);
                newIntent.putExtra("StateId",""+StateId);
                newIntent.putExtra("CityId",""+CityId);
                newIntent.putExtra("HighestEducationId",""+HighestEducationId);
                newIntent.putExtra("OccupationId",""+OccupationId);
                newIntent.putExtra("AnnualIncome",""+AnnualIncome);
                newIntent.putExtra("MotherToungueID",""+AppConstants.MotherTongueId);
                newIntent.putExtra("Diet",""+Diet);
                newIntent.putExtra("Smoking",""+Smoking);
                newIntent.putExtra("Drinking",""+Drinking);
                newIntent.putExtra("Rassi",""+Rassi);
                newIntent.putExtra("Photo",Photo);

                getActivity().startActivity(newIntent);
                getActivity().finish();
            }
        });

        btnSaveSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgeM = edtAgeM.getText().toString().trim();
                AgeF=edtAgeF.getText().toString().trim();
                final String HeightM=edtHeightM.getText().toString().trim();
                final String HeightF=edtHeightF.getText().toString().trim();
                final String MaritalStatus=edtMaritalStatus.getText().toString().trim();
                final String PhysicalStatus=edtPhysicalStatus.getText().toString().trim();
                final String ReligionId=AppConstants.ReligionId;
                final String CasteId=AppConstants.CasteId;
                final String CountryId=AppConstants.CountryId;
                final String StateId=AppConstants.StateId;
                final String CityId=AppConstants.CityId;
                final String HighestEducationId=AppConstants.EducationId;
                final String OccupationId=AppConstants.OccupationID;
                final String AnnualIncome=edtAnnualIncome.getText().toString().trim();
                //final String Manglik=edtManglik.getText().toString().trim();
                //final String Star=edtStar.getText().toString().trim();
                final String Diet=edtDiet.getText().toString().trim();
                final String Smoking=edtSmoking.getText().toString().trim();
                final String Drinking=edtDrinking.getText().toString().trim();
                final String Rassi=edtRassi.getText().toString().trim();
                final String MothertoungueID = AppConstants.MotherTongueId;
                final  String Photo= edtPhotoAvailable.getText().toString().trim();

                if (AgeM.equalsIgnoreCase("") && AgeF.equalsIgnoreCase("") &&
                        HeightM.equalsIgnoreCase("") && HeightF.equalsIgnoreCase("") &&
                        MaritalStatus.equalsIgnoreCase("") && PhysicalStatus.equalsIgnoreCase("")&&
                        ReligionId.equalsIgnoreCase("") && CasteId.equalsIgnoreCase("")&&
                        CountryId.equalsIgnoreCase("") && StateId.equalsIgnoreCase("")&&
                        CityId.equalsIgnoreCase("") && HighestEducationId.equalsIgnoreCase("")&&
                        OccupationId.equalsIgnoreCase("") && AnnualIncome.equalsIgnoreCase(""))

                {
                    Toast.makeText(getActivity(), "Please complete search data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.custom_dialog_save_search, null);
                    dialogBuilder.setView(dialogView);

                    final EditText edtSearchName = (EditText) dialogView.findViewById(R.id.edtSearchName);

                    dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String strSearchName = edtSearchName.getText().toString();
                            if (strSearchName.equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Please enter save search name", Toast.LENGTH_SHORT).show();
                            } else {
                                //setPhotoPasswordRequest(matri_id,strPassword);

                                saveSearchResult(strSearchName,AgeM,AgeF,HeightM,HeightF,MaritalStatus,PhysicalStatus,ReligionId,
                                        CasteId,CountryId,StateId,CityId,HighestEducationId,OccupationId,AnnualIncome,/*Manglik,*//*Star,*/
                                        matri_id,MothertoungueID,Diet,Smoking,Drinking,Rassi,Photo);
                                AppMethods.hideKeyboard(dialogView);
                            }
                        }
                    });
                    dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
            }
        });

        btnSavedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent= new Intent(getActivity(), SavedSearchResultActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra("search_Saved","frag_search_Saved");
                getActivity().startActivity(newIntent);
                getActivity().finish();
            }
        });

        String[] arrTempMaritalStaus = getResources().getStringArray(R.array.arr_marital_status);
        arrMaritalStaus.clear();
        for(int i=0; i<arrTempMaritalStaus.length;i++)
        {
            arrMaritalStaus.add(new beanGenralModel(""+1,arrTempMaritalStaus[i],false));
        }

        String[] arrTempAnnualIncome = getResources().getStringArray(R.array.arr_annual_income);
        for(int i=0; i<arrTempAnnualIncome.length;i++)
        {
            arrAnnualIncome.add(new beanGenralModel(""+1,arrTempAnnualIncome[i],false));
        }

        String[] arrTempPhysicalStatus = getResources().getStringArray(R.array.arr_physical_status);
        for(int i=0; i<arrTempPhysicalStatus.length;i++)
        {
            arrPhysicalStatus.add(new beanGenralModel(""+1,arrTempPhysicalStatus[i],false));
        }

        /*String[] arrTempStar = getResources().getStringArray(R.array.arr_star);
        for(int i=0; i<arrTempStar.length;i++)
        {
            arrStar.add(new beanGenralModel(""+1,arrTempStar[i],false));
        }*/

        String[] arrTempManglik = getResources().getStringArray(R.array.arr_manglik);
        for(int i=0; i<arrTempManglik.length;i++)
        {
            arrManglik.add(new beanGenralModel(""+1,arrTempManglik[i],false));
        }


        edtReligion.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.CasteId ="";
                    AppConstants.CasteName ="";

                    edtCaste.setText("");

                    GonelidingDrower();

                    if (NetworkConnection.hasConnection(getActivity()))
                    {
                        rvCaste.setAdapter(null);
                        getCastRequest(AppConstants.ReligionId);
                        getCaste();
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }
            }
        });

        edtCountry.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.StateName ="";
                    AppConstants.StateId ="";
                    edtState.setText("");
                    rvState.setAdapter(null);

                    AppConstants.CityName ="";
                    AppConstants.CityId ="";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getStateRequest(AppConstants.CountryId);
                    getStates();
                }
            }
        });

        edtState.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() != 0);
                {
                    AppConstants.CityName ="";
                    AppConstants.CityId ="";
                    edtCity.setText("");
                    rvCity.setAdapter(null);

                    GonelidingDrower();
                    getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                    getCity();
                }
            }
        });



    }


    public void getGeneralData()
    {
        edtAgeM.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtAgeM.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.GONE);

                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_age),SlidingDrawer,Slidingpage,btnMenuClose,edtAgeM);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtAgeF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtAgeF.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.GONE);


                rvGeneralView.setAdapter(null);
                GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_age),SlidingDrawer,Slidingpage,btnMenuClose,edtAgeF);
                rvGeneralView.setAdapter(generalAdapter);

            }
        });

        edtHeightM.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHeightM.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.GONE);


                ArrayList<HeightBean> beanArrayList = new ArrayList<>();

                beanArrayList.add(new HeightBean("48","Below 4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("54","4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("55","4ft 7in - 139cm"));
                beanArrayList.add(new HeightBean("56","4ft 8in - 142cm"));
                beanArrayList.add(new HeightBean("57","4ft 9in - 144cm"));
                beanArrayList.add(new HeightBean("58","4ft 10in - 147cm"));
                beanArrayList.add(new HeightBean("59","4ft 11in - 149cm"));
                beanArrayList.add(new HeightBean("60","5ft - 152cm"));
                beanArrayList.add(new HeightBean("61","5ft 1in - 154cm"));
                beanArrayList.add(new HeightBean("62","5ft 2in - 157cm"));
                beanArrayList.add(new HeightBean("63","5ft 3in - 160cm"));
                beanArrayList.add(new HeightBean("64","5ft 4in - 162cm"));
                beanArrayList.add(new HeightBean("65","5ft 5in - 165cm"));
                beanArrayList.add(new HeightBean("66","5ft 6in - 167cm"));
                beanArrayList.add(new HeightBean("67","5ft 7in - 170cm"));
                beanArrayList.add(new HeightBean("68","5ft 8in - 172cm"));
                beanArrayList.add(new HeightBean("69","5ft 9in - 175cm"));
                beanArrayList.add(new HeightBean("70","5ft 10in - 177cm"));
                beanArrayList.add(new HeightBean("71","5ft 11in - 180cm"));
                beanArrayList.add(new HeightBean("72","6ft - 182cm"));
                beanArrayList.add(new HeightBean("73","6ft 1in - 185cm"));
                beanArrayList.add(new HeightBean("74","6ft 2in - 187cm"));
                beanArrayList.add(new HeightBean("75","6ft 3in - 190cm"));
                beanArrayList.add(new HeightBean("76","6ft 4in - 193cm"));
                beanArrayList.add(new HeightBean("77","6ft 5in - 195cm"));
                beanArrayList.add(new HeightBean("78","6ft 6in - 198cm"));
                beanArrayList.add(new HeightBean("79","6ft 7in - 200cm"));
                beanArrayList.add(new HeightBean("80","6ft 8in - 203cm"));
                beanArrayList.add(new HeightBean("81","6ft 9in - 205cm"));
                beanArrayList.add(new HeightBean("82","6ft 10in - 208cm"));
                beanArrayList.add(new HeightBean("83","6ft 11in - 210cm"));
                beanArrayList.add(new HeightBean("84","7ft - 213cm"));
                beanArrayList.add(new HeightBean("89","Above 7ft - 213cm"));

                HeightAdapter adapter = new HeightAdapter(getActivity(),"agefrom",beanArrayList,Slidingpage,SlidingDrawer,btnMenuClose,edtHeightM);
                rvGeneralView.setAdapter(adapter);

            }
        });

        edtHeightF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHeightF.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.GONE);

                ArrayList<HeightBean> beanArrayList = new ArrayList<>();

                beanArrayList.add(new HeightBean("48","Below 4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("54","4ft 6in - 137cm"));
                beanArrayList.add(new HeightBean("55","4ft 7in - 139cm"));
                beanArrayList.add(new HeightBean("56","4ft 8in - 142cm"));
                beanArrayList.add(new HeightBean("57","4ft 9in - 144cm"));
                beanArrayList.add(new HeightBean("58","4ft 10in - 147cm"));
                beanArrayList.add(new HeightBean("59","4ft 11in - 149cm"));
                beanArrayList.add(new HeightBean("60","5ft - 152cm"));
                beanArrayList.add(new HeightBean("61","5ft 1in - 154cm"));
                beanArrayList.add(new HeightBean("62","5ft 2in - 157cm"));
                beanArrayList.add(new HeightBean("63","5ft 3in - 160cm"));
                beanArrayList.add(new HeightBean("64","5ft 4in - 162cm"));
                beanArrayList.add(new HeightBean("65","5ft 5in - 165cm"));
                beanArrayList.add(new HeightBean("66","5ft 6in - 167cm"));
                beanArrayList.add(new HeightBean("67","5ft 7in - 170cm"));
                beanArrayList.add(new HeightBean("68","5ft 8in - 172cm"));
                beanArrayList.add(new HeightBean("69","5ft 9in - 175cm"));
                beanArrayList.add(new HeightBean("70","5ft 10in - 177cm"));
                beanArrayList.add(new HeightBean("71","5ft 11in - 180cm"));
                beanArrayList.add(new HeightBean("72","6ft - 182cm"));
                beanArrayList.add(new HeightBean("73","6ft 1in - 185cm"));
                beanArrayList.add(new HeightBean("74","6ft 2in - 187cm"));
                beanArrayList.add(new HeightBean("75","6ft 3in - 190cm"));
                beanArrayList.add(new HeightBean("76","6ft 4in - 193cm"));
                beanArrayList.add(new HeightBean("77","6ft 5in - 195cm"));
                beanArrayList.add(new HeightBean("78","6ft 6in - 198cm"));
                beanArrayList.add(new HeightBean("79","6ft 7in - 200cm"));
                beanArrayList.add(new HeightBean("80","6ft 8in - 203cm"));
                beanArrayList.add(new HeightBean("81","6ft 9in - 205cm"));
                beanArrayList.add(new HeightBean("82","6ft 10in - 208cm"));
                beanArrayList.add(new HeightBean("83","6ft 11in - 210cm"));
                beanArrayList.add(new HeightBean("84","7ft - 213cm"));
                beanArrayList.add(new HeightBean("89","Above 7ft - 213cm"));

                HeightAdapter adapter = new HeightAdapter(getActivity(),"ageto",beanArrayList,Slidingpage,SlidingDrawer,btnMenuClose,edtHeightF);
                rvGeneralView.setAdapter(adapter);



            }
        });


        edtMaritalStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtMaritalStatus.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.VISIBLE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);

				/*rvGeneralView.setAdapter(null);
				GeneralAdapter generalAdapter= new GeneralAdapter(activity, getResources().getStringArray(R.array.arr_marital_status),SlidingDrawer,Slidingpage,btnMenuClose,edtMaritalStatus);
				rvGeneralView.setAdapter(generalAdapter);*/

                MaritalStatusAdapter = new MaritalStausMultiSelectionAdapter(getActivity(),arrMaritalStaus,
                        SlidingDrawer,Slidingpage,btnMenuClose,edtMaritalStatus,btnConfirm);
                rvMaritalStatus.setAdapter(MaritalStatusAdapter);

            }
        });

        edtPhysicalStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtPhysicalStatus.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.VISIBLE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);

				/*rvGeneralView.setAdapter(null);
				GeneralAdapter generalAdapter= new GeneralAdapter(activity, getResources().getStringArray(R.array.arr_physical_status),SlidingDrawer,Slidingpage,btnMenuClose,edtPhysicalStatus);
				rvGeneralView.setAdapter(generalAdapter);*/

                physicalStatusMultiSelectionAdapter = new PhysicalStatusMultiSelectionAdapter(getActivity(),arrPhysicalStatus,
                        SlidingDrawer,Slidingpage,btnMenuClose,edtPhysicalStatus,btnConfirm);
                rvPhysicalStatus.setAdapter(physicalStatusMultiSelectionAdapter);

            }
        });



        edtReligion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtReligion.setError(null);
                edtSearchReligion.setText("");

                linReligion.setVisibility(View.VISIBLE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });

        edtCaste.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCaste.setError(null);
                edtSearchCaste.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });

        edtCountry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCountry.setError(null);
                edtSearchCountry.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.VISIBLE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });

        edtState.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtState.setError(null);
                edtSearchState.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.VISIBLE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });

        edtCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtCity.setError(null);
                edtSearchCity.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.VISIBLE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });


        edtHighestEducation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtHighestEducation.setError(null);
                edtSearchHighestEducation.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.VISIBLE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);
            }
        });

        edtOccupation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtOccupation.setError(null);
                edtSearchOccupation.setText("");

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.VISIBLE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.GONE);
            }
        });


        edtAnnualIncome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtAnnualIncome.setError(null);

                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);

                linAnnualIncome.setVisibility(View.VISIBLE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);

                btnConfirm.setVisibility(View.VISIBLE);

				/*rvGeneralView.setAdapter(null);
				GeneralAdapter generalAdapter= new GeneralAdapter(activity, getResources().getStringArray(R.array.arr_annual_income),SlidingDrawer,Slidingpage,btnMenuClose,edtAnnualIncome);
				rvGeneralView.setAdapter(generalAdapter);*/

                annualIncomeMultiSelectionAdapter = new AnnualIncomeMultiSelectionAdapter(getActivity(),arrAnnualIncome,
                        SlidingDrawer,Slidingpage,btnMenuClose,edtAnnualIncome,btnConfirm);
                rvAnnualIncome.setAdapter(annualIncomeMultiSelectionAdapter);
            }
        });

        edtMotherTongue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtMotherTongue.setError(null);
                edtSearchMotherTongue.setText("");
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.VISIBLE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

            }
        });

        edtDiet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDiet.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_diet = res.getStringArray(R.array.arr_diet);

                for (int i=0; i < arr_diet.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_diet[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(getActivity(),"eating_Habits",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtDiet,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_diet),SlidingDrawer,Slidingpage,btnMenuClose,edtDiet);
                    rvGeneralView.setAdapter(generalAdapter);
                }
               /* GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_diet_2),SlidingDrawer,Slidingpage,btnMenuClose,edtDiet);
                rvGeneralView.setAdapter(generalAdapter);*/

            }
        });

        edtSmoking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtSmoking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_smoking = res.getStringArray(R.array.arr_smoking_3);

                for (int i=0; i < arr_smoking.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_smoking[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(getActivity(),"Smoking_Habits",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtSmoking,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_smoking_3),SlidingDrawer,Slidingpage,btnMenuClose,edtSmoking);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });


        edtPhotoAvailable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtPhotoAvailable.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_smoking = res.getStringArray(R.array.photo_available);

                for (int i=0; i < arr_smoking.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_smoking[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(getActivity(),"Photo Available",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtPhotoAvailable,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_smoking_3),SlidingDrawer,Slidingpage,btnMenuClose,edtPhotoAvailable);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });



        edtDrinking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtDrinking.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);

                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_drinking = res.getStringArray(R.array.arr_drinking_3);

                for (int i=0; i < arr_drinking.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_drinking[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(getActivity(),"Drinking_Habits",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtDrinking,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_drinking_3),SlidingDrawer,Slidingpage,btnMenuClose,edtDrinking);
                    rvGeneralView.setAdapter(generalAdapter);
                }



            }
        });

        edtRassi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                VisibleSlidingDrower();
                edtRassi.setError(null);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linGeneralView.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                rvGeneralView.setAdapter(null);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linMotherTongue.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linReligion.setVisibility(View.GONE);
                linCaste.setVisibility(View.GONE);
                linCountry.setVisibility(View.GONE);
                linState.setVisibility(View.GONE);
                linCity.setVisibility(View.GONE);
                linHighestEducation.setVisibility(View.GONE);
                linOccupation.setVisibility(View.GONE);
                linAnnualIncome.setVisibility(View.GONE);
                linMaritalStatus.setVisibility(View.GONE);
                linPhysicalStatus.setVisibility(View.GONE);
                linStar.setVisibility(View.GONE);
                linManglik.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.VISIBLE);
                ArrayList<beanGeneralData> arrGeneralData=new ArrayList<beanGeneralData>();
                Resources res = getResources();
                String[] arr_drinking = res.getStringArray(R.array.arr_raasi_moon_sign);

                for (int i=0; i < arr_drinking.length;i++)
                {
                    arrGeneralData.add(new beanGeneralData(""+i,arr_drinking[i],false));
                }

                if(arrGeneralData.size()>0)
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter2 generalAdapter= new GeneralAdapter2(getActivity(),"Raasi",arrGeneralData ,SlidingDrawer,Slidingpage,btnMenuClose,edtRassi,btnConfirm);
                    rvGeneralView.setAdapter(generalAdapter);

                }else
                {
                    rvGeneralView.setAdapter(null);
                    GeneralAdapter generalAdapter= new GeneralAdapter(getActivity(), getResources().getStringArray(R.array.arr_drinking_3),SlidingDrawer,Slidingpage,btnMenuClose,edtRassi);
                    rvGeneralView.setAdapter(generalAdapter);
                }

            }
        });



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppConstants.CountryId="";AppConstants.StateId="";AppConstants.CityId="";
        AppConstants.CountryName="";AppConstants.StateName="";AppConstants.CityName="";
        AppConstants.ReligionId="";AppConstants.CasteId="";AppConstants.EducationId="";AppConstants.OccupationID="";
        AppConstants.ReligionName="";AppConstants.CasteName="";AppConstants.EducationName="";AppConstants.OccupationName="";

        ArrayList<beanReligion> arrReligion=null;
        //ReligionAdapter religionAdapter=null;


        ArrayList<beanCaste> arrCaste=null;
        //CasteAdapter casteAdapter=null;

        ArrayList<beanCountries> arrCountry=null;
        //CountryAdapter countryAdapter=null;

        ArrayList<beanState> arrState=null;
        //StateAdapter stateAdapter=null;

        ArrayList<beanCity> arrCity=null;
        //CityAdapter cityAdapter=null;

        ArrayList<beanEducation> arrEducation=null;
        //EducationsAdapter educationAdapter=null;

        ArrayList<beanOccupation> arrOccupation=null;
        //OccupationAdapter occupationAdapter=null;

    }



    public void VisibleSlidingDrower()
    {
        SlidingDrawer.setVisibility(View.VISIBLE);
        AnimUtils.SlideAnimation(getActivity(), SlidingDrawer, R.anim.slide_right);
        Slidingpage.setVisibility(View.VISIBLE);

    }

    public void GonelidingDrower()
    {
        SlidingDrawer.setVisibility(View.GONE);
        AnimUtils.SlideAnimation(getActivity(), SlidingDrawer, R.anim.slide_left);
        Slidingpage.setVisibility(View.GONE);
    }

    public void SlidingMenu()
    {
        linReligion=(LinearLayout)view.findViewById(R.id.linReligion);
        linCaste=(LinearLayout)view.findViewById(R.id.linCaste);
        linCountry=(LinearLayout)view.findViewById(R.id.linCountry);
        linState=(LinearLayout)view.findViewById(R.id.linState);
        linCity=(LinearLayout)view.findViewById(R.id.linCity);
        linHighestEducation=(LinearLayout)view.findViewById(R.id.linHighestEducation);
        linOccupation=(LinearLayout)view.findViewById(R.id.linOccupation);
        linGeneralView=(LinearLayout)view.findViewById(R.id.linGeneralView);
        linAnnualIncome=(LinearLayout)view.findViewById(R.id.linAnnualIncome);
        linMaritalStatus=(LinearLayout)view.findViewById(R.id.linMaritalStatus);
        linPhysicalStatus=(LinearLayout)view.findViewById(R.id.linPhysicalStatus);
        linStar=(LinearLayout)view.findViewById(R.id.linStar);
        linManglik=(LinearLayout)view.findViewById(R.id.linManglik);
        linMotherTongue=(LinearLayout)view.findViewById(R.id.linMotherTongue);

        edtSearchReligion=(EditText)view.findViewById(R.id.edtSearchReligion);
        edtSearchCaste=(EditText)view.findViewById(R.id.edtSearchCaste);
        edtSearchCountry=(EditText)view.findViewById(R.id.edtSearchCountry);
        edtSearchState=(EditText)view.findViewById(R.id.edtSearchState);
        edtSearchCity=(EditText)view.findViewById(R.id.edtSearchCity);
        edtSearchHighestEducation=(EditText)view.findViewById(R.id.edtSearchHighestEducation);
        edtSearchOccupation=(EditText)view.findViewById(R.id.edtSearchOccupation);
        edtSearchMotherTongue=(EditText)view.findViewById(R.id.edtSearchMotherTongue);

        rvReligion=(RecyclerView)view.findViewById(R.id.rvReligion);
        rvCaste=(RecyclerView)view.findViewById(R.id.rvCaste);
        rvCountry=(RecyclerView)view.findViewById(R.id.rvCountry);
        rvState=(RecyclerView) view.findViewById(R.id.rvState);
        rvCity=(RecyclerView)view.findViewById(R.id.rvCity);
        rvHighestEducation=(RecyclerView)view.findViewById(R.id.rvHighestEducation);
        rvOccupation=(RecyclerView)view.findViewById(R.id.rvOccupation);
        rvGeneralView=(RecyclerView)view.findViewById(R.id.rvGeneralView);
        rvAnnualIncome=(RecyclerView)view.findViewById(R.id.rvAnnualIncome);
        rvMaritalStatus=(RecyclerView)view.findViewById(R.id.rvMaritalStatus);
        rvPhysicalStatus=(RecyclerView)view.findViewById(R.id.rvPhysicalStatus);
      //  rvStar=(RecyclerView)view.findViewById(R.id.rvStar);
        rvManglik=(RecyclerView)view.findViewById(R.id.rvManglik);
        rvMotherTongue=(RecyclerView)view.findViewById(R.id.rvMotherTongue);



        btnConfirm=(Button) view.findViewById(R.id.btnConfirm);


        rvReligion.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReligion.setHasFixedSize(true);
        rvCaste.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCaste.setHasFixedSize(true);
        rvCountry.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCountry.setHasFixedSize(true);
        rvState.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvState.setHasFixedSize(true);
        rvCity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCity.setHasFixedSize(true);
        rvHighestEducation.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHighestEducation.setHasFixedSize(true);
        rvOccupation.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOccupation.setHasFixedSize(true);
        rvGeneralView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGeneralView.setHasFixedSize(true);

        rvAnnualIncome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAnnualIncome.setHasFixedSize(true);
        rvMaritalStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMaritalStatus.setHasFixedSize(true);
        rvPhysicalStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPhysicalStatus.setHasFixedSize(true);
   /*     rvStar.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStar.setHasFixedSize(true);*/
        rvManglik.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvManglik.setHasFixedSize(true);
        rvMotherTongue.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMotherTongue.setHasFixedSize(true);

        btnMenuClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GonelidingDrower();
            }
        });

        Slidingpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GonelidingDrower();

            }
        });
    }

    public void getReligions()
    {
        try {
            edtSearchReligion.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrReligion.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchReligion.getText().toString().toLowerCase(Locale.getDefault());
                        //religionAdapter.filter(text);
                        religionMultiSelectionAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }
    }

    public void getCaste()
    {
        try {
            edtSearchCaste.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrCaste.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchCaste.getText().toString().toLowerCase(Locale.getDefault());
                        //casteAdapter.filter(text);
                        casteMultiSelectionAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }
    }

    public void getCountries()
    {
        try {
            edtSearchCountry.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(!arrCountry.isEmpty() && arrCountry.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchCountry.getText().toString().toLowerCase(Locale.getDefault());
                        //countryAdapter.filter(text);
                        countryMultiSelectionAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }
    }

    public void getStates()
    {
        edtSearchState.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(!arrState.isEmpty() && arrState.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchState.getText().toString().toLowerCase(Locale.getDefault());
                    //stateAdapter.filter(text);
                    stateMultiSelectionAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });
    }

    private void getMotherToungeRequest()
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"mother_tounge.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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
                Log.e("--mother_tounge --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrMotherTongue= new ArrayList<beanMotherTongue>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String mtongue_id=resItem.getString("mtongue_id");
                            String mtongue_name=resItem.getString("mtongue_name");

                            arrMotherTongue.add(new beanMotherTongue(mtongue_id,mtongue_name));

                        }

                        if(arrMotherTongue.size() >0)
                        {
                            Collections.sort(arrMotherTongue, new Comparator<beanMotherTongue>() {
                                @Override
                                public int compare(beanMotherTongue lhs, beanMotherTongue rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            motherTongueAdapter = new MotherTongueMultiSelectionAdapter(getActivity(), arrMotherTongue,SlidingDrawer,Slidingpage,btnMenuClose,edtMotherTongue,btnConfirm);
                            rvMotherTongue.setAdapter(motherTongueAdapter);


                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getMotherTongue()
    {
        try {
            edtSearchMotherTongue.addTextChangedListener(new TextWatcher()
            {
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    if(arrMotherTongue.size()>0)
                    {
                        String charcter=cs.toString();
                        String text = edtSearchMotherTongue.getText().toString().toLowerCase(Locale.getDefault());
                        motherTongueAdapter.filter(text);
                    }
                }
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
                {}
                public void afterTextChanged(Editable arg0)
                {}
            });
        }catch (Exception e)
        {

        }


    }


    public void getCity()
    {
        edtSearchCity.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrCity.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchCity.getText().toString().toLowerCase(Locale.getDefault());
                    //cityAdapter.filter(text);
                    cityMultiSelectionAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }

    public void getHighestEducation()
    {
        edtSearchHighestEducation.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrEducation.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchHighestEducation.getText().toString().toLowerCase(Locale.getDefault());
                    //educationAdapter.filter(text);
                    educationsMultiSelectionAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }

    private void getHighestEducationRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"education.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrEducation= new ArrayList<beanEducation>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("edu_id");
                            String edu_name=resItem.getString("edu_name");

                            arrEducation.add(new beanEducation(edu_id,edu_name,false));

                        }

                        if(arrEducation.size() >0)
                        {
                            Collections.sort(arrEducation, new Comparator<beanEducation>() {
                                @Override
                                public int compare(beanEducation lhs, beanEducation rhs) {
                                    return lhs.getEducation_name().compareTo(rhs.getEducation_name());
                                }
                            });
							/*educationAdapter = new EducationsAdapter(getActivity(), arrEducation,SlidingDrawer,Slidingpage,btnMenuClose,edtHighestEducation);
							rvHighestEducation.setAdapter(educationAdapter);*/

                            educationsMultiSelectionAdapter = new EducationsMultiSelectionAdapter(getActivity(), arrEducation,SlidingDrawer,Slidingpage,btnMenuClose,edtHighestEducation,btnConfirm);
                            rvHighestEducation.setAdapter(educationsMultiSelectionAdapter);
                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    if (NetworkConnection.hasConnection(context)) {
                        getOccupationsRequest();
                        getOccupations();

                        getMotherToungeRequest();
                        getMotherTongue();
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }

                }

                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void getOccupations()
    {
        edtSearchOccupation.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                if(arrOccupation.size()>0)
                {
                    String charcter=cs.toString();
                    String text = edtSearchOccupation.getText().toString().toLowerCase(Locale.getDefault());
                    occupationAdapter.filter(text);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3)
            {}
            public void afterTextChanged(Editable arg0)
            {}
        });

    }

    private void getOccupationsRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"occupation.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrOccupation= new ArrayList<beanOccupation>();


                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String edu_id=resItem.getString("ocp_id");
                            String edu_name=resItem.getString("ocp_name");

                            arrOccupation.add(new beanOccupation(edu_id,edu_name,false));

                        }

                        if(arrOccupation.size() >0)
                        {
                            Collections.sort(arrOccupation, new Comparator<beanOccupation>() {
                                @Override
                                public int compare(beanOccupation lhs, beanOccupation rhs) {
                                    return lhs.getOccupation_name().compareTo(rhs.getOccupation_name());
                                }
                            });
                            occupationAdapter = new OccupationAdapter(getActivity(), arrOccupation,SlidingDrawer,Slidingpage,btnMenuClose,edtOccupation);
                            rvOccupation.setAdapter(occupationAdapter);

                        }


                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally {

                    if (NetworkConnection.hasConnection(context)) {
                        getReligionRequest();
                        getReligions();
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }

                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void getReligionRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"religion.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--religion --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrReligion= new ArrayList<beanReligion>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String religionId=resItem.getString("religion_id");
                            String religionName=resItem.getString("religion_name");

                            arrReligion.add(new beanReligion(religionId,religionName,false));

                        }

                        if(arrReligion.size() >0)
                        {
                            Collections.sort(arrReligion, new Comparator<beanReligion>() {
                                @Override
                                public int compare(beanReligion lhs, beanReligion rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
                            //religionAdapter = new ReligionAdapter(getActivity(), arrReligion,SlidingDrawer,Slidingpage,btnMenuClose,edtReligion);
                            //rvReligion.setAdapter(religionAdapter);

                            religionMultiSelectionAdapter = new ReligionMultiSelectionAdapter(getActivity(),arrReligion,SlidingDrawer,Slidingpage,btnMenuClose,edtReligion,btnConfirm);
                            rvReligion.setAdapter(religionMultiSelectionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    if (NetworkConnection.hasConnection(context)) {
                        getCountrysRequest();
                        getCountries();
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }

                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void getCastRequest(String strReligion)
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"cast.php";//?religion_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("religion_id", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--cast --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCaste= new ArrayList<beanCaste>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String casteId=resItem.getString("caste_id");
                            String casteName=resItem.getString("caste_name");

                            arrCaste.add(new beanCaste(casteId,casteName,false));

                        }

                        if(arrCaste.size() >0)
                        {
                            Collections.sort(arrCaste, new Comparator<beanCaste>() {
                                @Override
                                public int compare(beanCaste lhs, beanCaste rhs) {
                                    return lhs.getName().compareTo(rhs.getName());
                                }
                            });
							/*casteAdapter = new CasteAdapter(getActivity(), arrCaste,SlidingDrawer,Slidingpage,btnMenuClose,edtCaste);
							rvCaste.setAdapter(casteAdapter);*/

                            casteMultiSelectionAdapter = new CasteMultiSelectionAdapter(getActivity(), arrCaste,SlidingDrawer,Slidingpage,btnMenuClose,edtCaste,btnConfirm);
                            rvCaste.setAdapter(casteMultiSelectionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {

                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(strReligion);
    }

    private void getCountrysRequest()
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                // String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"country.php";
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                //BasicNameValuePair UsernamePAir = new BasicNameValuePair("username", paramUsername);

                //List<NameValuePair> nameValuePairList = new ArrayList<>();
                /*   nameValuePairList.add(UsernamePAir);*/

                try
                {
                    // UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    // httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--Country --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");

                    arrCountry= new ArrayList<beanCountries>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CoID=resItem.getString("country_id");
                            String CoName=resItem.getString("country_name");

                            arrCountry.add(new beanCountries(CoID,CoName,false));

                        }

                        if(arrCountry.size() >0)
                        {
                            Collections.sort(arrCountry, new Comparator<beanCountries>() {
                                @Override
                                public int compare(beanCountries lhs, beanCountries rhs) {
                                    return lhs.getCountry_name().compareTo(rhs.getCountry_name());
                                }
                            });
							/*countryAdapter = new CountryAdapter(getActivity(), arrCountry,SlidingDrawer,Slidingpage,btnMenuClose,edtCountry);
							rvCountry.setAdapter(countryAdapter);*/

                            countryMultiSelectionAdapter = new CountryMultiSelectionAdapter(getActivity(), arrCountry,SlidingDrawer,Slidingpage,btnMenuClose,edtCountry,btnConfirm);
                            rvCountry.setAdapter(countryMultiSelectionAdapter);

                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {
                    Log.e("dfkldf",e.getMessage());

                } finally
                {


                }
                progressBar1.setVisibility(View.GONE);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private void getStateRequest(final String CoId)
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramUsername = params[0];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"state.php";//?country_id="+paramUsername;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair UsernamePAir = new BasicNameValuePair("country_id", paramUsername);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(UsernamePAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--State --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrState= new ArrayList<beanState>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String SID=resItem.getString("state_id");
                            String SName=resItem.getString("state_name");

                            arrState.add(new beanState(SID,SName,false));

                        }

                        if(arrState.size() >0)
                        {
                            Collections.sort(arrState, new Comparator<beanState>() {
                                @Override
                                public int compare(beanState lhs, beanState rhs) {
                                    return lhs.getState_name().compareTo(rhs.getState_name());
                                }
                            });
							/*stateAdapter= new StateAdapter(getActivity(), arrState,SlidingDrawer,Slidingpage,btnMenuClose,edtState);
							rvState.setAdapter(stateAdapter);*/

                            stateMultiSelectionAdapter= new StateMultiSelectionAdapter(getActivity(), arrState,SlidingDrawer,Slidingpage,btnMenuClose,edtState,btnConfirm);
                            rvState.setAdapter(stateMultiSelectionAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;

                } catch (Exception e)
                {

                } finally
                {
                    getCityRequest(AppConstants.CountryId,AppConstants.StateId);
                    getCity();
                }

                progressBar1.setVisibility(View.GONE);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId);
    }

    private void getCityRequest(String CoId,String SaId)
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramCountry = params[0];
                String paramState = params[1];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"city.php";//?country_id="+paramCountry+"&state_id="+paramState;
                Log.e("URL", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair CountryPAir = new BasicNameValuePair("country_id", paramCountry);
                BasicNameValuePair CityPAir = new BasicNameValuePair("state_id", paramState);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(CountryPAir);
                nameValuePairList.add(CityPAir);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
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

                Log.e("--City --", "=="+Ressponce);

                try {
                    JSONObject responseObj = new JSONObject(Ressponce);
                    JSONObject responseData = responseObj.getJSONObject("responseData");
                    arrCity= new ArrayList<beanCity>();

                    if (responseData.has("1"))
                    {
                        Iterator<String> resIter = responseData.keys();

                        while (resIter.hasNext())
                        {

                            String key = resIter.next();
                            JSONObject resItem = responseData.getJSONObject(key);

                            String CID=resItem.getString("city_id");
                            String CName=resItem.getString("city_name");

                            arrCity.add(new beanCity(CID,CName,false));

                        }

                        if(arrCity.size() >0)
                        {
                            Collections.sort(arrCity, new Comparator<beanCity>() {
                                @Override
                                public int compare(beanCity lhs, beanCity rhs) {
                                    return lhs.getCity_name().compareTo(rhs.getCity_name());
                                }
                            });
							/*cityAdapter= new CityAdapter(getActivity(), arrCity,SlidingDrawer,Slidingpage,btnMenuClose,edtCity);
							rvCity.setAdapter(cityAdapter);*/

                            cityMultiSelectionAdapter= new CityMultiSelectionAdapter(getActivity(), arrCity,SlidingDrawer,Slidingpage,btnMenuClose,edtCity,btnConfirm);
                            rvCity.setAdapter(cityMultiSelectionAdapter);
                        }

                        resIter = null;

                    }

                    responseData = null;
                    responseObj = null;


                } catch (Exception e)
                {

                } finally
                {


                }
                progressBar1.setVisibility(View.GONE);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(CoId,SaId);
    }

    private void saveSearchResult(final String SaveName, String AgeM, String AgeF, String HeightM, String HeightF,
                                  String MaritalStatus, String PhysicalStatus, String ReligionId,
                                  String CasteId, String CountryId, String StateId, String CityId, String HighestEducationId,
                                  String OccupationId, String AnnualIncome,
                                  String login_matriId,String MotherToungID,String Diet,String Smoking,String Drink,String Raasi,String Photo)
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {

                String paramSaveName= params[0];
                String paramAgeM= params[1];
                String paramAgeF= params[2];
                String paramHeightM= params[3];
                String paramHeightF= params[4];
                String paramMaritalStatus= params[5];
                String paramPhysicalStatus= params[6];
                String paramReligionId= params[7];
                String paramCasteId= params[8];
                String paramCountryId= params[9];
                String paramStateId= params[10];
                String paramCityId= params[11];
                String paramEducationId= params[12];
                String paramOccupationID= params[13];
                String paramAnnualIncome= params[14];
                String paramsLoginMatriId = params[15];
                String paramMothertoungue = params[16];
                String paramsDiet = params[17];
                String paramSmoke = params[18];
                String paramDrink = params[19];
                String paramRaasi = params[20];
                String paramPhoto = params[21];


                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"save_search.php";
                Log.e("URL", "== "+URL);

                //,,,,,,,,,,,,,,star,
                HttpPost httpPost = new HttpPost(URL);
                BasicNameValuePair PairGender = new BasicNameValuePair("ss_name", paramSaveName);
                BasicNameValuePair MatriSeachIdPair = new BasicNameValuePair("matri_id", paramsLoginMatriId);
                BasicNameValuePair PairAgeM = new BasicNameValuePair("fromage", paramAgeM);
                BasicNameValuePair PairAgeF = new BasicNameValuePair("toage", paramAgeF);
                BasicNameValuePair PairHeightM = new BasicNameValuePair("from_height", paramHeightM);
                BasicNameValuePair PairHeightF = new BasicNameValuePair("to_height", paramHeightF);
                BasicNameValuePair PairMaritalStatus = new BasicNameValuePair("marital_status", paramMaritalStatus);
                //BasicNameValuePair PairPhysicalStatus = new BasicNameValuePair("physical_status", paramPhysicalStatus);
                BasicNameValuePair PairReligionId = new BasicNameValuePair("religion", paramReligionId);
                BasicNameValuePair PairCasteId = new BasicNameValuePair("caste", paramCasteId);
                BasicNameValuePair PairCountryId = new BasicNameValuePair("country", paramCountryId);
                BasicNameValuePair PairStateId = new BasicNameValuePair("state", paramStateId);
                BasicNameValuePair PairCityId = new BasicNameValuePair("city", paramCityId);
                BasicNameValuePair PairEducationId = new BasicNameValuePair("education", paramEducationId);
                BasicNameValuePair PairOccupationID = new BasicNameValuePair("occupation", paramOccupationID);
                BasicNameValuePair PairAnnualIncome = new BasicNameValuePair("annual_income", paramAnnualIncome);
                BasicNameValuePair PairMotherToungue = new BasicNameValuePair("mother_tongue",paramMothertoungue);
                BasicNameValuePair DietPair = new BasicNameValuePair("diet", paramsDiet);
                BasicNameValuePair SmokingPair = new BasicNameValuePair("smoke", paramSmoke);
                BasicNameValuePair DrinkingPair = new BasicNameValuePair("drink", paramDrink);
                BasicNameValuePair RassiPair = new BasicNameValuePair("raasi", paramRaasi);
                BasicNameValuePair photoPair = new BasicNameValuePair("with_photo",paramPhoto);


                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(PairGender);
                nameValuePairList.add(PairAgeM);
                nameValuePairList.add(PairAgeF);
                nameValuePairList.add(PairHeightM);
                nameValuePairList.add(PairHeightF);
                nameValuePairList.add(PairMaritalStatus);
                //nameValuePairList.add(PairPhysicalStatus);
                nameValuePairList.add(PairReligionId);
                nameValuePairList.add(PairCasteId);
                nameValuePairList.add(PairCountryId);
                nameValuePairList.add(PairStateId);
                nameValuePairList.add(PairCityId);
                nameValuePairList.add(PairEducationId);
                nameValuePairList.add(PairOccupationID);
                nameValuePairList.add(PairAnnualIncome);
                nameValuePairList.add(MatriSeachIdPair);
                nameValuePairList.add(PairMotherToungue);
                nameValuePairList.add(DietPair);
                nameValuePairList.add(SmokingPair);
                nameValuePairList.add(DrinkingPair);
                nameValuePairList.add(RassiPair);
                nameValuePairList.add(photoPair);


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

                Log.e("--Search by Result --", "=="+Ressponce);

                try
                {
                    JSONObject obj = new JSONObject(Ressponce);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        String msgError=obj.getString("message");
                        Toast.makeText(getActivity(),""+msgError,Toast.LENGTH_LONG).show();

						/*arrSaveSearch= new ArrayList<beanSaveSearch>();
						JSONObject responseData = obj.getJSONObject("responseData");

						if (responseData.has("1"))
						{
							Iterator<String> resIter = responseData.keys();

							while (resIter.hasNext())
							{

								String key = resIter.next();
								JSONObject resItem = responseData.getJSONObject(key);

								String user_id=resItem.getString("user_id");
								String matri_id1=resItem.getString("matri_id");
								String username=resItem.getString("username");
								String birthdate=resItem.getString("birthdate");
								String ocp_name=resItem.getString("ocp_name");
								String height=resItem.getString("height");
								String Address=resItem.getString("profile_text");
								String city_name=resItem.getString("city_name");
								String country_name=resItem.getString("country_name");
								String photo1_approve=resItem.getString("photo1_approve");
								String photo_view_status=resItem.getString("photo_view_status");
								String photo_protect=resItem.getString("photo_protect");
								String photo_pswd=resItem.getString("photo_pswd");
								String gender1=resItem.getString("gender");
								String is_shortlisted=resItem.getString("is_shortlisted");
								String is_blocked=resItem.getString("is_blocked");
								String is_favourite=resItem.getString("is_favourite");
								String user_profile_picture=resItem.getString("user_profile_picture");

								arrSaveSearch.add(new beanSaveSearch(user_id,matri_id1,username,birthdate,ocp_name,height,Address,city_name,country_name,photo1_approve,photo_view_status,photo_protect,
										photo_pswd,gender1,is_shortlisted,is_blocked,strSearchName));
							}*/

                        //Log.d("ravi","searchName ="+arrSaveSearch.get(0).getSearchName());

							/*if(arrSearchResultList.size() > 0)
							{
								recyclerUser.setVisibility(View.VISIBLE);
								textEmptyView.setVisibility(View.GONE);

								SearchResultAdapter adapterShortlistedUser = new SearchResultAdapter(SearchResultActivity.this,arrSearchResultList, recyclerUser);
								recyclerUser.setAdapter(adapterShortlistedUser);

							}else
							{
								recyclerUser.setVisibility(View.GONE);
								textEmptyView.setVisibility(View.VISIBLE);
							}*/
                        //	}



                    }else
                    {
                        String msgError=obj.getString("message");
                        Toast.makeText(context, ""+msgError, Toast.LENGTH_SHORT).show();
                    }


                    progressBar1.setVisibility(View.GONE);
                } catch (Exception t)
                {
                    Log.e("extfngjn",t.getMessage());
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(SaveName,AgeM,AgeF,HeightM,HeightF,MaritalStatus,PhysicalStatus,ReligionId,
                CasteId,CountryId,StateId,CityId,HighestEducationId,OccupationId,AnnualIncome,/*Manglik,*//*Star,*/
                login_matriId,MotherToungID,Diet,Smoking,Drink,Raasi,Photo);

    }



}
