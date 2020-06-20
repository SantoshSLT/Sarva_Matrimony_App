package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import Adepters.ManglikMultiSelectionAdapter;
import Adepters.MaritalStausMultiSelectionAdapter;
import Adepters.OccupationAdapter;
import Adepters.PhysicalStatusMultiSelectionAdapter;
import Adepters.ReligionMultiSelectionAdapter;
import Adepters.StarMultiSelectionAdapter;
import Adepters.StateMultiSelectionAdapter;
import Models.beanCaste;
import Models.beanCity;
import Models.beanCountries;
import Models.beanEducation;
import Models.beanGenralModel;
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
import utills.AppConstants;
import utills.NetworkConnection;


public class FragmentSearch extends Fragment
{
	String TAG="FragmentSearch";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static TabLayout tabLayout;
	public static TabLayout.Tab tab;

	private SearchPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private boolean isViewShown = false;

	SharedPreferences prefUpdate;;

	String matri_id="";

	private ArrayList<beanSaveSearch> arrSaveSearch;

	public FragmentSearch()
	{
	}

	public static FragmentSearch newInstance(int sectionNumber)
	{
		FragmentSearch fragment = new FragmentSearch();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView=inflater.inflate(R.layout.fragment_search, container,false);
		prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
		//Gender=prefUpdate.getString("gender","");
		matri_id=prefUpdate.getString("matri_id","");


		mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setCurrentItem(0);

		tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

		if (!isViewShown)
		{
			setData();
		}


		int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_SEARCH,0);
		if(selectedTabPos > 0)
		{
			tab = tabLayout.getTabAt(selectedTabPos);
			tab.select();
		}else
		{
			tab = tabLayout.getTabAt(0);
			tab.select();
		}

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}
			@Override
			public void onPageSelected(int position)
			{
				SharedPreferences.Editor editor=prefUpdate.edit();
				editor.putInt(AppConstants.SELECTED_TAB_SEARCH,position);
				editor.commit();
				Log.i("Selected_pos_Home",position+"");
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

		return rootView;

	}



	private void setData()
	{
		mSectionsPagerAdapter = new SearchPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabLayout.setupWithViewPager(mViewPager);
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getView() != null)
		{
			isViewShown = true;
			setData();
		} else
		{
			isViewShown = false;
		}
	}

	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;
	}



	class SearchPagerAdapter extends FragmentPagerAdapter{

		public SearchPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			switch (position)
			{
				case 0:
					return Search_M_IDFragment.newInstance(position+1);
				case 1:
					return SearchByDetailFragment.newInstance(position+1);

				default:
					return null;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "SERACHED BY MATRI ID";
				case 1:
					return "ADAVANCE SEARCH";

			}
			return null;
		}

		@Override
		public int getItemPosition(@NonNull Object object) {
			return POSITION_NONE;
		}
	}


}
