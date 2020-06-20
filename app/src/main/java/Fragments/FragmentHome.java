package Fragments;

/**
 * Created by Ravi on 9/27/2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thegreentech.R;

import utills.AppConstants;

public class FragmentHome extends Fragment
{
    public static TabLayout tabLayout;
    public static TabLayout.Tab tab;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean isViewShown = false;
    private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences prefUpdate;
    //String UserId="",matri_id="",gender="";

    public FragmentHome() {
    }

    public static FragmentHome newInstance(int sectionNumber)
    {
        FragmentHome fragment1 = new FragmentHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment1.setArguments(args);


        return fragment1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View inflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
      /*  UserId=prefUpdate.getString("user_id","");
        matri_id=prefUpdate.getString("matri_id","");
        gender=prefUpdate.getString("gender","");
        Log.e("UserId= ","=> "+UserId);
        Log.e("matri_id= ","=> "+matri_id);*/

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);

        tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);

        if (!isViewShown)
        {
            setData();
        }

        int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_HOME,0);
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
                editor.putInt(AppConstants.SELECTED_TAB_HOME,position);
                editor.commit();
                Log.i("Selected_pos_Home",position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("call onResume","Home");
       // setData();
    }

    private void setData()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }
    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser)
    {
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


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position)
            {
                case 0:
                    return FragmentHomeRecent.newInstance(position+1);
                case 1:
                    return FragmentHomeMatches.newInstance(position+1);
                case 2:
                    return FragmentHomeFeatured.newInstance(position+1);
               /* case 3:
                    return FrameShortListed.newInstance(position+1);*/
                case 3:
                    return Fragment_Viewd.newInstance(position+1);
               /* case 5:
                    return Fragment_Ivisited.newInstance(position+1);*/
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position) {
                case 0:
                    return "RECENTLY JOINED";
                case 1:
                    return "MY MATCHES";
                case 2:
                    return "FEATURED PROFILE";
              /*  case 3:
                    return "SHORTLISTED";*/
                case 3:
                    return "RECENTLY VISITED PROFILE";
               /* case 5:
                    return "I VISITED PROFILES";*/
            }
            return null;
        }
        public int getItemPosition(Object item)
        {
            return POSITION_NONE;
        }
    }





}