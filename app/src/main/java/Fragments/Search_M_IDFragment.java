package Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thegreentech.R;
import com.thegreentech.SearchResultActivity;

public class Search_M_IDFragment extends Fragment {

    View rootView;
    EditText edtMatriId;
    Button btnSearchByMatriId;
    private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences prefUpdate;;
    String matri_id="",Gender="";


    public Search_M_IDFragment() {
        // Required empty public constructor
    }


    public static Search_M_IDFragment newInstance(int sectionNumber)
    {
        Search_M_IDFragment fragment = new Search_M_IDFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id=prefUpdate.getString("matri_id","");
        Gender = prefUpdate.getString("gender","");


        init();
        onclick();

        return rootView;
    }

    public  void init(){
        edtMatriId=(EditText)rootView.findViewById(R.id.edtMatriId);
        btnSearchByMatriId = rootView.findViewById(R.id.btnSearchByMatriId);

    }


    public void onclick(){
        btnSearchByMatriId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMatriId=edtMatriId.getText().toString().trim();
				if(strMatriId.equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(),"Please enter Gender",Toast.LENGTH_LONG).show();
				}else
				{
					Intent newIntent= new Intent(getActivity(), SearchResultActivity.class);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					newIntent.putExtra("MatriId",strMatriId);
					newIntent.putExtra("SearchType","byId");
					newIntent.putExtra("istype","from_M_ID");
					getActivity().startActivity(newIntent);
					getActivity().finish();
				}

            }
        });
    }

}
