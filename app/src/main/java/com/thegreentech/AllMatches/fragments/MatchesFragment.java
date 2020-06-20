package com.thegreentech.AllMatches.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thegreentech.R;

import utills.AppConstants;

public class MatchesFragment extends Fragment {

    View inflatedView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static TabLayout tabLayout;
    public static TabLayout.Tab tab;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Activity activity;
    private ViewPager mViewPager;
    private boolean isViewShown = false;
    SharedPreferences prefUpdate;
    String matri_id="";


    public MatchesFragment() {
    }

    public static MatchesFragment newInstance(int sectionNumber)
    {
        MatchesFragment fragment = new MatchesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


         inflatedView=inflater.inflate(R.layout.matches_fragment, container,false);

        init();
        return inflatedView;
    }


    public  void  init(){
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id=prefUpdate.getString("matri_id","");

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);

        if (!isViewShown)
        {
            setData();
        }

        int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_MATCHES,0);
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
                editor.putInt(AppConstants.SELECTED_TAB_MESSAGE,position);
                editor.commit();
                Log.i("Selected_pos_Message",position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity=context;
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

    private void setData()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return OneMatchFrag.newInstance(position+1);
                case 1:
                    return TwoMatchFrag.newInstance(position+1);
                case 2:
                    return BroaderMatchFrag.newInstance(position+1);
                case 3:
                    return PreferedMatchFrag.newInstance(position+1);


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position) {
                case 0:
                    return "One way matches";
                case 1:
                    return "Two way matches";
                case 2:
                    return "Broader Matches";
                case 3:
                    return "Preferred Matches";


            }
            return null;
        }
    }

}
